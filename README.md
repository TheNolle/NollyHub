<h1 align="center">
  <img src="https://i.imgur.com/zVaoaUf.png" alt="NollyHub" width="300px">
  <br>
  NollyHub Plugin
</h1>

<p align="center">
  A powerful and customizable hub plugin for your Minecraft server.
</p>

<p align="center">
  <img alt="License" src="https://img.shields.io/badge/license-MIT-blue.svg">
  <img alt="Java" src="https://img.shields.io/badge/java-17%2B-blue.svg">
  <img alt="Minecraft" src="https://img.shields.io/badge/minecraft-1.19.4%2B-green.svg">
  <img alt="Spigot" src="https://img.shields.io/badge/paper-1.19.4%2B-orange.svg">
</p>

## Features

🌟 Teleport players to a custom hub location\
🔒 Set permissions for hub usage\
⚡️ Customize hub messages with MiniMessage formatting\
🍽️ Option to disable player hunger\
🏃 Set default gamemode to Adventure\
❌ Disable PvP in the hub
🗃️ And much more

## Requirements

- Java 17 or higher
- Minecraft 1.19.4 or higher
- PaperMC or a compatible server software

## Installation

1. Download the latest plugin version from the [releases page](https://github.com/thenolle/nollyhub/releases).
2. Place the plugin JAR file in the `plugins` directory of your server.
3. Start or reload your server.

## Configuration

The plugin configuration can be found in `plugins/NollyHub/config.yml`. The configuration file allows you to customize various aspects of the plugin, including permissions, messages, hub location, features, etc.

## Commands and Permissions
##### (Some examples)

- `/hub` - Teleport to the hub location.
  - Permission: `nollyhub.default`
- `/sethub` - Set the hub location.
  - Permission: `nollyhub.admin`
- `/hub_reload` - Reload the plugin configuration.
  - Permission: `nollyhub.admin`

## Customizing Messages

You can customize the plugin messages using MiniMessage formatting. Open the `config.yml` file and modify the `messages` section to change the welcome message, hub not set message, set hub message, and more.

## Contributing

Contributions are welcome! If you encounter any issues or have suggestions for improvement, please open an issue or submit a pull request. Make sure to read our [contribution guidelines](CONTRIBUTING.md) before getting started.

## License

This plugin is open-source and available under the [MIT License](LICENSE).
