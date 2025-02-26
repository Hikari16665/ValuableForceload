# ValuableForceload (VFL) - Chunk Forceload Plugin!

![Bukkit](https://img.shields.io/badge/Bukkit-1.13%2B-blue)
![Spigot](https://img.shields.io/badge/Spigot-1.13%2B-orange)

A Bukkit plugin that allows players to purchase and manage forceloaded chunks through an intuitive GUI, supporting multiple economy systems!

## ⚠️ **Important Note**:  
- Released JAR files are **obfuscated** for protection
- GitHub repository contains **original non-obfuscated** source code


## **Gallery**:  
![Main GUI](https://cdn.modrinth.com/data/cached_images/c016da496e8681665caa7dfeeec721071018ceaa.png)
*Main Interface - Profile & Purchase chunks*

![Manage UI](https://cdn.modrinth.com/data/cached_images/ec3039ad76688409c35eaf428f4c828a27891756.png)
*Management Interface - Configure chunks*

## ✨ Features
- GUI-based chunk management
- Multi-economy support (Vault/PlayerPoints/XConomy)
- Configurable pricing and expiration
- Multi-language localization (English/Simplified Chinese)
- Player quota system

## 📥 Installation
1. Install required dependencies:
   - [Vault](https://www.spigotmc.org/resources/vault.34315/) (Optional)
   - [PlayerPoints](https://www.spigotmc.org/resources/playerpoints.80745/) (Optional)
   - [XConomy](https://www.spigotmc.org/resources/xconomy.75669/) (Optional)
2. Download `ValuableForceload.jar`
3. Place the JAR in your server's `plugins` folder
4. Restart the server



## ⚙️ Configuration
```yaml
# config.yml
max-forceload-chunks-per-player: 3   # Maximum chunks per player
forceload-chunk-price:
  economy-type: PLUGIN_PLAYERPOINTS  # Economy provider
  value: 200                         # Chunk price
  days: 3                            # Expiration days
locale: zh_CN                        # Interface language
```

# ⚙️ Commands
### /valuableforceload open
Permission: valuableforceload.command.gui

Effect: Open Main GUI for player.
### /valuableforceload version
Permission: [no permission required]

Effect: Show version info for player.
## /valuableforceload admin
### /valuableforceload admin erase_all
Permission: valuableforceload.command.admin

Effect: Erase all data in VFL (**_DANGEROUS!_**)


# 🤝 Contributing
Contributions are welcome through:
- Issue reporting
- Pull requests
- Localization assistance
