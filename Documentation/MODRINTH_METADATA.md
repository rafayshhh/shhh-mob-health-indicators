# Modrinth Project Metadata & Setup Guide

- **Mod Name**: Shhh Mob Health Indicators
- **Mod Slug**: `shhh-mob-health-indicators`
- **Author**: RafayShhh (https://modrinth.com/user/RafayShhh)
- **Target Version**: Minecraft 26.3 (Fabric)

---

## Project Details

| Field | Value to Enter | Notes |
| :--- | :--- | :--- |
| **Project Name** | Shhh Mob Health Indicators | The public display name. |
| **Project Slug / ID** | `shhh-mob-health-indicators` | URL: `https://modrinth.com/mod/shhh-mob-health-indicators` |
| **Project Type** | Mod | Standard client mod. |
| **Short Summary** | Overhead mob health indicators with authentic heart textures, RPG tiered hearts & category filtering. | Under 100 characters. |
| **Categories** | Utility, Cosmetic | Primary categories. |
| **Client / Server Side** | Client: Required, Server: Unsupported | Pure client-side mod. |
| **License** | MIT | Standard open source license. |
| **Source Repository** | https://github.com/rafayshhh/shhh-mob-health-indicators | GitHub repository link. |
| **Issue Tracker** | https://github.com/rafayshhh/shhh-mob-health-indicators/issues | GitHub issues link. |
| **Project Icon** | Upload `Documentation/showcase/icon.png` (or `Assets/icon.png`) | Square PNG. |

---

## Mod Description Body (Markdown for Modrinth Page)

```markdown
# Shhh Mob Health Indicators

**A modern, customizable client-side Fabric mod that displays overhead mob health indicators with authentic heart textures, RPG tiered hearts, category filters, and multiple visual presets.**

---

## ✨ Features

- **Mob-Dedicated Engine**: Exclusively targets living mobs, animals, monsters, and bosses (`LivingEntity`). Strictly excludes all players and avatars, making it 100% compatible with player indicator mods like *Shhh Player Health Indicators*.
- **Authentic 9x9 Heart Sprites**: True pixel-art Minecraft heart sprites rendered natively in the font engine with billboard orientation and lighting.
- **RPG Tiered Hearts**: Seamlessly scales for high-capacity mob health pools (Iron Golems, Withers, Wardens, Ravagers) with 5 radiant color tiers.
- **Smart Category Filtering**:
  - Independent toggles for **Hostile Mobs**, **Passive Mobs**, and **Bosses**.
  - **Named Mobs Only** filter.
  - **Hide at 100% Full Health** filter to eliminate spawner and farm clutter.
  - **Hide Invisible** filter.
- **Multiple Visual Presets**:
  - **Classic Hearts** *(Default)*: Pixel-art red hearts, coral half hearts, empty containers, and RPG tiered hearts.
  - **Compact**: Clean heart icon with numeric health ratio (e.g. `❤ 20/20 HP`).
  - **Health Bar**: Sleek HUD-style segmented health bar (`[██████████] 20/20`).
  - **Minimal Numeric**: Ultra-clean text (`❤ 20.0 / 20.0 HP`).
  - **RPG / Hearts**: Dual heart count and HP readout (`❤ 10/10 Hearts  20/20 HP`).
- **Positioning**: Configurable **Above Name Tag** *(Default)* or **Below Name Tag**.
- **In-Game Settings Menu**: Built natively using vanilla layout components. Compatible with ModMenu.
- **Streamlined Keybindings**:
  - `F12`: Toggle Mob Health Indicators ON/OFF.
  - `0`: Open Settings Screen.
```
