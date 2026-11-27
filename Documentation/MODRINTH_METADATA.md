# Modrinth Project Metadata & Setup Guide

- **Mod Name**: Customizable Mobs Health Indicator
- **Mod Slug**: `customizable-mobs-health-indicator`
- **Author**: RafayShhh (https://modrinth.com/user/RafayShhh)
- **Target Version**: Minecraft 26.3 (Fabric)

---

## Project Details

| Field | Value to Enter | Notes |
| :--- | :--- | :--- |
| **Project Name** | Customizable Mobs Health Indicator | The public display name. |
| **Project Slug / ID** | `customizable-mobs-health-indicator` | URL: `https://modrinth.com/mod/customizable-mobs-health-indicator` |
| **Project Type** | Mod | Standard client mod. |
| **Short Summary** | Overhead mob health indicators with authentic heart textures, RPG tiered hearts & category filtering. | Under 100 characters. |
| **Categories** | Utility, Cosmetic | Primary categories. |
| **Client / Server Side** | Client: Required, Server: Unsupported | Pure client-side mod. |
| **License** | MIT | Standard open source license. |
| **Source Repository** | https://github.com/rafayshhh/customizable-mobs-health-indicator | GitHub repository link. |
| **Issue Tracker** | https://github.com/rafayshhh/customizable-mobs-health-indicator/issues | GitHub issues link. |
| **Project Icon** | Upload `Documentation/showcase/icon.png` (or `Assets/icon.png`) | Square PNG. |

---

## Mod Description Body (Markdown for Modrinth Page)

```markdown
# Customizable Mobs Health Indicator

**A modern, customizable client-side Fabric mod that displays overhead mob health indicators with authentic heart textures, RPG tiered hearts, category filters, and multiple visual presets.**

---

## ✨ Features

- **Mob-Dedicated Engine**: Exclusively targets living mobs, animals, monsters, and bosses (`LivingEntity`). Strictly excludes all players and avatars, making it 100% compatible with player indicator mods.
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
  - **RPG / Hearts**: Dual Heart and HP readout (`❤ 10/10 Hearts  20/20 HP`).
- **Heart Texture Styles**: Choose between `Vanilla (Textured)`, `Hardcore (Textured)`, and `Unicode (❤)`.
- **In-Game Settings & Commands**: Native two-column menu (ModMenu compatible) plus `/mobhealth` (or `/cmhi`) client commands.
- **Ultra High Performance**: Pre-built text components during state extraction with 0 object allocations during render submission.
```
