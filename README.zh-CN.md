# Vita3K+

[English](./README.md) | 简体中文

本仓库是 [Vita3K+](https://github.com/nckstwrt/Vita3K-Plus) 的一个分支，由 **zFitness（ZM）** 维护；而 Vita3K+ 本身是 Vita3K 的分支，额外提供了游戏兼容性与渲染修复。此处发布的 Android 构建在启动器上显示为 **Vita3K+ ZM**，其中 `ZM` 只是本分支的维护者标识。模拟器本体的功劳归 Vita3K 团队，兼容性修复的功劳归 Vita3K+ 作者。

## 下载

https://github.com/zFitness/Vita3K-Plus/releases

Android 每夜构建（nightly）APK 由 [`nightly-android.yml`](./.github/workflows/nightly-android.yml) 自动发布，标签形如 `android-nightly-<build>-<sha>`。

## 增强内容

# v1.1（尚未发布）
- **Dead Or Alive 5 Plus** - 可运行 - 在 Android 上需要 Adreno、Turnip 以及新增的「线程调度精度」设置为开启
- **Metal Gear Solid 3** - 修复/规避了卡死问题。修复了混合渲染。修复了屏幕触控板相关问题。
- **Resistance** - 修复了 Android 上的渲染问题
- **Assassin's Creed III: Liberation** - 解决了声音问题
- **Killzone** - 修复了第 5 关出现的崩溃
- **Neptunia Re;Birth1** - 修复了选择「新游戏」后的崩溃以及背景图片不显示的问题
- **Little Big Planet** - 修复了「暗角」着色与背景图片
- **Ys: Memories of Celcetta** 修复了图形问题
- **Ragnarok Odyssey ACE** 修复了图形问题
- **Ninja Gaiden Sigma 2 Plus** 修复了图形问题
- **ModNation: Racers Road Trip** - 修复了回归问题
- **RESOGUN** - 修复了图形问题
- **Omega Labyrinth** - 由 nishinji 修复了载入问题
- 现在 Windows 默认使用 External Host，Android 默认使用 Native Buffer。**请使用这些内存映射选项以获得最佳兼容性**
- 大量内存与声音方面的改进
- 感谢 Reyes 在测试方面的所有帮助！

# v1.0
- **Killzone** 运行良好，画面表现出色
- **Resistance** 颜色现已正常。间歇性卡死已修复。
- **Assassin's Creed III: Liberation** 修复了图形问题
- **LittleBigPlanet** 修复了缺失的背景、开场声音以及其他图形问题
- **Need for Speed: Most Wanted** 修复了漂浮的徽章、穿透路面的车灯，声音也基本修复
- **Call of Duty: Declassified** 现在可以启动并良好运行
- **Uncharted** 现在可在任意分辨率下运行，另有一些小修复
- **Devious Dungeon** 现在可以正常启动
- **Sonic Transformed** 运行良好，画面表现出色（新增按游戏设置的线程精度选项以确保顺利启动）
- **Spider-Man** 不再偶发地在载入时卡住
- **Helldivers** 不再偶发地在启动时崩溃
- **Soul Sacrifice Delta** 修复了图形问题
- **Minecraft** 在 Vulkan 下曾是白屏，现已修复
- **Kancolle Kai** 现在不会卡在难度选择界面

希望这些改动修复了大量崩溃与卡死问题。其他游戏在此版本中可能明显变好，也可能变差。

## 注意事项

- 设置现在默认为高质量（High Quality）
- 这些更新是面向较高端设备设计的。虽然我也尝试让它们在基于 Mali 的设备上工作并做过测试（例如 Killzone 在我的 Mali-G78 MP14 设备上运行良好），但那并不是本分支的目标。
- 想在 Killzone 中获得更好的角色阴影，请在 Windows 上使用 External Host、在 Android 上使用 Native Buffer 的内存映射。Double Buffer 也能正常工作，但会让阴影闪烁得很恼人。

## 新截图

上述游戏在 Vita3K+ 中运行的画面见 **[截图页面](./Screenshots.md)**。

[![Vita3K+ 截图](./screenshots/KZ.jpg)](./Screenshots.md)

## 反馈问题

https://github.com/zFitness/Vita3K-Plus/issues

## 代码改动
所有代码改动请查看 all-enhancements 分支

本 ZM 分支在 `dev-nightly` 上开发，其 CI（[`nightly-android.yml`](./.github/workflows/nightly-android.yml)）仅构建 Android 目标。

以下是原版 Vita3K 的 README：
---

# Vita3K

[![C/C++ CI](https://github.com/Vita3K/Vita3K/actions/workflows/c-cpp.yml/badge.svg)](https://github.com/Vita3K/Vita3K/actions/workflows/c-cpp.yml)
[![Release](https://img.shields.io/github/v/release/Vita3K/Vita3K-builds?include_prereleases)](https://github.com/Vita3K/Vita3K/releases)
[![Vita3K discord server](https://img.shields.io/discord/408916678911459329?color=5865F2&label=Vita3K%20&logo=discord&logoColor=white)](https://discord.gg/6aGwQzh)

## 简介

Vita3K 是一款实验性的 PlayStation Vita 模拟器，支持 Windows、Linux、macOS 和 Android。

* [官网](https://vita3k.org/)（面向用户的信息）
* [Wiki](https://github.com/Vita3K/Vita3K/wiki)（面向开发者的信息）
* [Discord 服务器](https://discord.gg/MaWhJVH)（推荐）

## 兼容性

模拟器目前可运行大多数自制程序与商业游戏。

- [自制程序兼容性列表](https://vita3k.org/compatibility-homebrew.html)
- [商业游戏兼容性列表](https://vita3k.org/compatibility.html)

## 画廊

|               **Persona 4 Golden**（Atlus）                   |                     **A Rose in the Twilight**（Nippon Ichi Software）                         |
| :-----------------------------------------------------------: | :--------------------------------------------------------------------------------------------: |
| ![Persona 4 Golden screenshot](./_readme/screenshots/P4G.png) | ![A Rose in the Twilight screenshot](./_readme/screenshots/A%20Rose%20in%20the%20Twilight.png) |

|                  **Alone with You**（Benjamin Rivers）                     |                 **VA-11 HALL-A**（Sukeban Games）                    |
| :------------------------------------------------------------------------: | :------------------------------------------------------------------: |
| ![Alone with You screenshot](./_readme/screenshots/Alone%20With%20You.png) | ![VA-11 HALL-A screenshot](./_readme/screenshots/VA-11%20HALL-A.png) |

|              **Fruit Ninja**（Halfbrick Studios）                  |                **Jetpack Joyride**（Halfbrick Studios）                    |
| :----------------------------------------------------------------: | :------------------------------------------------------------------------: |
| ![Fruit Ninja Screenshot](./_readme/screenshots/Fruit%20Ninja.png) | ![Jetpack Joyride Screenshot](./_readme/screenshots/Jetpack%20Joyride.png) |

## 许可证

Vita3K 采用 **GPLv2** 许可证。这主要由外部依赖决定，其中最主要的是 Unicorn。

## 下载

你可以在[这里](https://github.com/Vita3K/Vita3K/releases/tag/continuous)下载最新的构建。

* Windows
  * 依赖：
    * [Microsoft Visual C++ 2015-2022 Redistributable](https://aka.ms/vs/17/release/vc_redist.x64.exe)
* Linux
  * 基于 Arch 的发行版：
    * [vita3k-bin](https://aur.archlinux.org/packages/vita3k-bin)<sup><small>AUR</small></sup>
    * [vita3k-git](https://aur.archlinux.org/packages/vita3k-git)<sup><small>AUR</small></sup>
  * 依赖：
    * xdg-desktop-portal
* Android
    * [Adreno 驱动](https://github.com/K11MCH1/AdrenoToolsDrivers/releases/)
* 其他
  * [下载构建产物](https://github.com/Vita3K/Vita3K/actions?query=event%3Apush+is%3Asuccess+branch%3Amaster)
  * [旧版本构建](https://github.com/Vita3K/Vita3K-builds/releases)

## 编译

请参阅 [`building.md`](./building.md)。

## 运行
请查看我们的[快速上手指南](https://vita3k.org/quickstart)，以确认你的电脑满足运行 Vita3K 的最低要求。  
别忘了将显卡驱动更新到最新版本；如果你是 Windows 用户，请安装 [Visual C++ 2015-2022 Redistributable](https://aka.ms/vs/17/release/VC_redist.x64.exe)。  

## Bug 与问题
本项目仍处于早期阶段，因此提交新 issue 时请多加留意。请预期会遇到崩溃、图形错误、兼容性不足和性能不佳的情况。

## 致谢
感谢那些提供建议或以其他方式让本项目成为可能的人们，例如 Davee、korruptor、Rinnegatamante、ScHlAuChi、Simon Kilroy、TheFlow、xerpi、xyz、Yifan Lu 以及许多其他人。

## 捐赠
[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/vita3k)
<br>
感谢各位支持者，以及所有在 [ko-fi](https://ko-fi.com/vita3K) 上支持我们的人。
* 其中，订阅了 Nibble Tier 及以上等级的有：**j0hnnybrav0、Mored4u、TacoOblivion、Undeadbob 和 uplush**

## 说明
本模拟器的目的并非助长非法行为。你可以使用 [NoNpDrm](https://github.com/TheOfficialFloW/NoNpDrm) 或 [FAGDec](https://github.com/CelesteBlue-dev/PSVita-RE-tools/tree/master/FAGDec/build) 从 Vita 中导出自己的游戏，也可以从 [VitaDB](https://www.rinnegatamante.eu/vitadb/#/) 获取自制程序。

PlayStation、PlayStation Vita 和 PlayStation Network 均为 Sony Interactive Entertainment Inc. 的注册商标。本模拟器与 Sony 无关、未获其认可，也并非源自属于 Sony 的机密材料。
