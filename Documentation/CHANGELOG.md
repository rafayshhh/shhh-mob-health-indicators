# Changelog

All notable changes to the "Shhh Mob Health Indicators" project will be documented in this file.

## [1.0.0] - Official Release - Modern Overhead Mob Health Indicators for Fabric 26.3

**Shhh Mob Health Indicators v1.0.0** is the debut release for Minecraft 26.3 on Fabric (Client-Only).

### Highlights & Core Features:
- **Mob-Specific Overhead Health Indicators**:
  - Renders above mob nameplates and models in 3D world space using native billboarded `submitNameTag`.
  - Zero OpenGL leaks, shader/Sodium/Iris friendly.
  - Exclusively targets living mobs (`LivingEntity`) while strictly ignoring players, avatars, and armor stands.
- **Authentic 9x9 Heart Sprites**:
  - Full red hearts, half hearts, empty containers, absorption gold hearts, and hardcore variants compiled directly from official vanilla assets.
  - Integrated into Minecraft's native font pipeline via Private Use Area codepoints (`\uE000`–`\uE00B`).
- **RPG Tiered Hearts for High-Health Mobs**:
  - Seamlessly activates for mobs with > 12 containers / > 24 HP (Iron Golems, Withers, Wardens, Ravagers, etc.).
  - 5 radiant tiers: Tier 1 (Crimson Red), Tier 2 (Radiant Gold), Tier 3 (Emerald Lime), Tier 4 (Diamond Aqua), Tier 5+ (Ender Magenta).
- **5 Visual Presets**:
  - `Classic Hearts` (Default): Authentic 9x9 pixel-art heart sprites with tiered scaling.
  - `Compact`: Modern HUD indicator (`❤ 20/20 HP`).
  - `Health Bar`: Solid 10-segment segmented bar (`[██████████] 20/20`).
  - `Minimal Numeric`: Tournament badge (`❤ 20.0 / 20.0 HP`).
  - `RPG / Hearts`: Dual Heart and HP readout (`❤ 10/10 Hearts  20/20 HP`).
- **Category & Visual Filters**:
  - Dedicated toggles for Hostile Mobs, Passive Mobs, and Bosses.
  - Named Mobs Only filter to show indicators only for mobs with nametags.
  - Hide at 100% Full Health filter to eliminate farm/spawner clutter.
  - Hide Invisible mobs filter.
- **In-Game Settings Menu**:
  - Native Minecraft UI built with `HeaderAndFooterLayout` and responsive two-column layout.
  - ModMenu integration.
- **Keybindings**:
  - `F12`: Toggle Mob Health Indicators ON/OFF.
  - `0`: Open Settings Menu.
  - Includes chat/screen guards to prevent accidental menu triggers while typing.
