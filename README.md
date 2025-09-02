# Zaphkiel-Plus

> Powered by TabooLib
>
> A custom item system framework for Minecraft servers based on [TabooLib](https://github.com/TabooLib).
>
> **Zaphkiel-Plus** is a fork and enhanced version of the Zaphkiel project. While retaining the core functionality of
> the original project, it adds new features and fixes several issues, aiming to provide a more stable and powerful
> custom
> item system.
>
> Original Project: [Zaphkiel](https://github.com/TabooLib/zaphkiel)

English | [中文](README_zh_CN.md)

## Overview

Zaphkiel provides powerful APIs for creating, managing,
and using custom items with rich customization options.
It features a modular architecture with support for dynamic display generation,
event-driven behaviors, and advanced NBT data management.

## Features

- **Multi-Language Support** - Automatically detects the player's client language and provides localized item
  experiences
- **Flexible Item Definition**: Define custom items using YAML configuration files
- **Dynamic Display System**: Support for dynamic name and lore generation with data mapping
- **Event-Driven Behavior**: Custom item behaviors using Kether scripts
- **NBT Data Management**: Advanced NBT data handling with custom path support
- **Item Serialization**: Complete serialization system for database persistence
- **Plugin Integration**: Seamless integration with popular Minecraft plugins

## Related Links

- [Documentation](https://iplugin.hiusers.com/en/docs/zaphkiel-plus)

## Architecture

Zaphkiel follows a modular, layered architecture:

- **common**: Core API interfaces
- **common-impl**: Implementation of interfaces
- **module-bukkit**: Bukkit-specific code
- **module-legacy-api**: Legacy version support

## Building

* [Gradle](https://gradle.org/) - Dependency Management

The GradleWrapper is included in this project.

**Windows:**

```bash
gradlew.bat clean build
```

**macOS/Linux:**

```bash
./gradlew clean build
```

Build artifacts can be found in `./plugin/build/libs` folder.

## License

This project is licensed under CC0 1.0 Universal - see the [LICENSE](LICENSE) file for details.
