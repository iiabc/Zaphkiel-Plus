# Zaphkiel-Plus

> Powered by TabooLib
>
> 基于 [TabooLib](https://github.com/TabooLib) 的 Minecraft 服务器自定义物品系统框架。
>
> **Zaphkiel-Plus** 是对 Zaphkiel 项目的派生和增强版本。它在保留原项目核心功能的基础上，增加了一些新功能并修复了若干问题，旨在提供更稳定、更强大的自定义物品系统。
>
> 原项目地址：[Zaphkiel](https://github.com/TabooLib/zaphkiel)

[English](README.md) | 中文

## 概述

Zaphkiel 为创建、管理和使用具有丰富自定义选项的自定义物品提供强大的 API。
它采用模块化架构，支持动态显示生成、事件驱动行为和高级 NBT 数据管理。

## 功能特性

- **多语言支持** - 自动检测玩家客户端语言，提供本地化物品体验
- **灵活的物品定义**: 使用 YAML 配置文件定义自定义物品
- **动态显示系统**: 支持通过数据映射动态生成名称和描述
- **事件驱动行为**: 使用 Kether 脚本实现自定义物品行为
- **NBT 数据管理**: 高级 NBT 数据处理，支持自定义路径
- **物品序列化**: 完整的序列化系统，支持数据库持久化
- **插件集成**: 与流行的 Minecraft 插件无缝集成

## 相关链接

- [文档](https://iplugin.hiusers.com/docs/zaphkiel-plus)
- [下载](https://pakko.hiusers.com/resource/zaphkiel-plus)
- [QQ 群](https://qm.qq.com/q/KqPuII5j2w)

## 架构

Zaphkiel 采用模块化分层架构：

- **common**: 核心 API 接口
- **common-impl**: 接口实现
- **module-bukkit**: Bukkit 特定代码
- **module-legacy-api**: 旧版本支持

## 构建

* [Gradle](https://gradle.org/) - 依赖管理

项目包含 GradleWrapper。

**Windows:**

```bash
gradlew.bat clean build
```

**macOS/Linux:**

```bash
./gradlew clean build
```

构建产物位于 `./plugin/build/libs` 文件夹。

## 许可证

本项目采用 CC0 1.0 Universal 许可证 - 详见 [LICENSE](https://www.google.com/search?q=LICENSE) 文件。