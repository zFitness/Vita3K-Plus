package org.vita3k.emulator.data

import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract.Document
import android.provider.DocumentsContract.Root
import android.provider.DocumentsProvider
import android.webkit.MimeTypeMap
import org.vita3k.emulator.NativeLib
import org.vita3k.emulator.R
import java.io.File
import java.io.FileNotFoundException

class VitaDocumentsProvider : DocumentsProvider() {

    companion object {
        const val ROOT_ID = "vita3k"
        private const val DOC_ROOT = "vita:"

        private val ROOT_PROJECTION = arrayOf(
            Root.COLUMN_ROOT_ID,
            Root.COLUMN_FLAGS,
            Root.COLUMN_ICON,
            Root.COLUMN_TITLE,
            Root.COLUMN_SUMMARY,
            Root.COLUMN_DOCUMENT_ID,
            Root.COLUMN_AVAILABLE_BYTES
        )

        private val DOCUMENT_PROJECTION = arrayOf(
            Document.COLUMN_DOCUMENT_ID,
            Document.COLUMN_MIME_TYPE,
            Document.COLUMN_DISPLAY_NAME,
            Document.COLUMN_LAST_MODIFIED,
            Document.COLUMN_FLAGS,
            Document.COLUMN_SIZE
        )
    }

    override fun onCreate(): Boolean = true

    // The provider can be spawned by a file manager before the emulator ever runs, so the native
    // side may not be available; fall back to the default storage location in that case.
    private fun baseDir(): File {
        val nativePath = runCatching {
            if (NativeLib.isInitialized()) NativeLib.getCurrentEmulatorPath() else null
        }.getOrNull()
        val path = nativePath?.takeIf { it.isNotEmpty() }
            ?: AppStorage.defaultStoragePath(requireNotNull(context))
        return File(path)
    }

    private fun fileForDocId(docId: String, mustExist: Boolean = true): File {
        if (!docId.startsWith(DOC_ROOT))
            throw FileNotFoundException("Unknown document $docId")
        val base = baseDir()
        val rel = docId.substring(DOC_ROOT.length)
        val file = if (rel.isEmpty()) base else File(base, rel)
        val baseCanonical = base.canonicalPath
        val canonical = file.canonicalPath
        if (canonical != baseCanonical && !canonical.startsWith(baseCanonical + File.separator))
            throw FileNotFoundException("Document $docId is outside the emulator storage")
        if (mustExist && !file.exists())
            throw FileNotFoundException("Missing document $docId")
        return file
    }

    private fun childDocId(parentDocId: String, name: String): String =
        if (parentDocId == DOC_ROOT) DOC_ROOT + name else "$parentDocId/$name"

    private fun mimeType(file: File): String {
        if (file.isDirectory)
            return Document.MIME_TYPE_DIR
        val ext = file.extension.lowercase()
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) ?: "application/octet-stream"
    }

    private fun includeFile(cursor: MatrixCursor, docId: String, file: File) {
        val isRoot = docId == DOC_ROOT
        var flags = 0
        if (file.isDirectory) {
            flags = flags or Document.FLAG_DIR_SUPPORTS_CREATE
        } else {
            flags = flags or Document.FLAG_SUPPORTS_WRITE
        }
        if (!isRoot) {
            flags = flags or Document.FLAG_SUPPORTS_DELETE or
                Document.FLAG_SUPPORTS_RENAME or Document.FLAG_SUPPORTS_MOVE
        }
        cursor.newRow().apply {
            add(Document.COLUMN_DOCUMENT_ID, docId)
            add(Document.COLUMN_MIME_TYPE, mimeType(file))
            add(Document.COLUMN_DISPLAY_NAME, if (isRoot) "Vita3K+" else file.name)
            add(Document.COLUMN_LAST_MODIFIED, file.lastModified())
            add(Document.COLUMN_FLAGS, flags)
            add(Document.COLUMN_SIZE, if (file.isFile) file.length() else null)
        }
    }

    override fun queryRoots(projection: Array<out String>?): Cursor {
        val cursor = MatrixCursor(projection ?: ROOT_PROJECTION)
        cursor.newRow().apply {
            add(Root.COLUMN_ROOT_ID, ROOT_ID)
            add(Root.COLUMN_FLAGS, Root.FLAG_SUPPORTS_CREATE or Root.FLAG_SUPPORTS_IS_CHILD or Root.FLAG_LOCAL_ONLY)
            add(Root.COLUMN_ICON, R.mipmap.ic_launcher_plus)
            add(Root.COLUMN_TITLE, "Vita3K+")
            add(Root.COLUMN_SUMMARY, context?.getString(R.string.documents_root_summary))
            add(Root.COLUMN_DOCUMENT_ID, DOC_ROOT)
            add(Root.COLUMN_AVAILABLE_BYTES, baseDir().usableSpace)
        }
        return cursor
    }

    override fun queryDocument(documentId: String, projection: Array<out String>?): Cursor {
        val cursor = MatrixCursor(projection ?: DOCUMENT_PROJECTION)
        includeFile(cursor, documentId, fileForDocId(documentId))
        return cursor
    }

    override fun queryChildDocuments(
        parentDocumentId: String,
        projection: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val cursor = MatrixCursor(projection ?: DOCUMENT_PROJECTION)
        val dir = fileForDocId(parentDocumentId)
        dir.listFiles()?.forEach { child ->
            includeFile(cursor, childDocId(parentDocumentId, child.name), child)
        }
        return cursor
    }

    override fun isChildDocument(parentDocumentId: String, documentId: String): Boolean {
        val prefix = if (parentDocumentId == DOC_ROOT) DOC_ROOT else "$parentDocumentId/"
        return documentId != parentDocumentId && documentId.startsWith(prefix)
    }

    override fun getDocumentType(documentId: String): String = mimeType(fileForDocId(documentId))

    override fun openDocument(
        documentId: String,
        mode: String,
        signal: CancellationSignal?
    ): ParcelFileDescriptor {
        val file = fileForDocId(documentId, mustExist = !mode.contains('w'))
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.parseMode(mode))
    }

    override fun createDocument(parentDocumentId: String, mimeType: String, displayName: String): String {
        val parent = fileForDocId(parentDocumentId)
        val safeName = displayName.replace('/', '_').replace('\\', '_').trim()
        if (safeName.isEmpty())
            throw FileNotFoundException("Invalid name")
        var target = File(parent, safeName)
        var attempt = 1
        while (target.exists()) {
            target = File(parent, "$safeName (${attempt++})")
        }
        val ok = if (mimeType == Document.MIME_TYPE_DIR) target.mkdirs() else runCatching { target.createNewFile() }.getOrDefault(false)
        if (!ok)
            throw FileNotFoundException("Failed to create $displayName in $parentDocumentId")
        return childDocId(parentDocumentId, target.name)
    }

    override fun deleteDocument(documentId: String) {
        val file = fileForDocId(documentId)
        if (!file.deleteRecursively())
            throw FileNotFoundException("Failed to delete $documentId")
    }

    override fun renameDocument(documentId: String, displayName: String): String {
        val file = fileForDocId(documentId)
        val safeName = displayName.replace('/', '_').replace('\\', '_').trim()
        if (safeName.isEmpty() || safeName == file.name)
            return documentId
        val target = File(file.parentFile, safeName)
        if (target.exists() || !file.renameTo(target))
            throw FileNotFoundException("Failed to rename $documentId to $displayName")
        val parentDocId = documentId.substringBeforeLast('/', DOC_ROOT)
        return childDocId(parentDocId, safeName)
    }

    override fun moveDocument(
        sourceDocumentId: String,
        sourceParentDocumentId: String,
        targetParentDocumentId: String
    ): String {
        val source = fileForDocId(sourceDocumentId)
        val targetParent = fileForDocId(targetParentDocumentId)
        val target = File(targetParent, source.name)
        if (target.exists() || !source.renameTo(target))
            throw FileNotFoundException("Failed to move $sourceDocumentId to $targetParentDocumentId")
        return childDocId(targetParentDocumentId, source.name)
    }
}
