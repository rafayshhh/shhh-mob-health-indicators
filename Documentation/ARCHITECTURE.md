# Architecture & Design - Shhh Mob Health Indicators (26.3)

## Overview
**Shhh Mob Health Indicators** is a lightweight, performance-critical client-side Fabric mod for Minecraft 26.3. It renders overhead health indicators (hearts, compact text, bar, and minimal numbers) above mob models in 3D world space.

## Key Subsystems

### 1. Dedicated Mob Health Resolution Engine (`HealthTracker`)
- **Mob-Specific Targeting**: Directly extracts health from `LivingEntity` instances (`entity.getHealth()`, `getMaxHealth()`, `getAbsorptionAmount()`).
- **Strict Player Exclusion**: Explicitly checks `entity instanceof Player || entity instanceof Avatar` and returns `null`, preventing any indicator collision with player health indicator mods.
- **Immediate Lethal Hit Eviction**: Automatically removes health indicators when `entity.isDeadOrDying() || !entity.isAlive() || health <= 0`, eliminating floating indicators over dying corpse animations.
- **Entity Type Filtering**:
  - Distinguishes **Hostile Mobs** (`entity instanceof Enemy`), **Passive Mobs** (`Animal`, `Villager`, etc.), and **Boss Mobs** (`WitherBoss`, `EnderDragon`, `Warden`, `ElderGuardian`).
  - Supports user-configurable toggles for each category.
  - Supports **Named Only** mode to only render on mobs with custom name tags.
  - Supports **Hide at 100% HP** to reduce HUD clutter in busy farms or mob spawners.
  - Supports **Hide Invisible** when mobs have active invisibility status effects.
- **Adaptive Max Health Memory Cache & Event-Driven Eviction**: Tracks and retains observed highest health for mobs with dynamically boosted health pools. Cleans up stale entries via `ClientEntityEvents.ENTITY_UNLOAD` and resets on `ClientPlayConnectionEvents.DISCONNECT` to prevent memory leaks.

### 2. Minecraft 26.3 RenderState Pipeline & Performance Optimizations
In modern Minecraft (1.21.2+ / 26.3), rendering is split into two phases:
1. **Extraction (`LivingEntityRenderer.extractRenderState`)**:
   - `LivingEntityRendererMixin` intercepts the tail of extraction.
   - **Distance Culling**: Checks `state.distanceToCameraSq > (maxDist * maxDist)` and bails out before running entity extraction for distant mobs.
   - Verifies the entity is not a player or avatar.
   - Extracts `HealthInfo` and assigns it via `HealthRenderStateAccessor` duck-typing interface to `LivingEntityRenderState`.
   - **Component Pre-Building**: Formats and pre-builds the `Component` during extraction, caching it directly on the render state.
   - Computes `state.nameTagAttachment` via `entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, yRot)` with automated fallback to `new Vec3(0.0, state.boundingBoxHeight + 0.5, 0.0)` for mobs without vanilla nametags.
2. **Submission (`LivingEntityRenderer.submit`)**:
   - `LivingEntityRendererMixin` intercepts the tail of entity model and layer submission.
   - Delegates to `HealthRenderer.render(...)`.
   - Directly consumes the pre-built `Component` with **zero object allocations, zero string formatting, and zero regex/regex-like parsing** inside the render loop.
   - Uses Minecraft's official `SubmitNodeCollector.submitNameTag` with billboard orientation, lighting, depth, and distance scaling.
   - Respects shaders, Sodium, and Iris by staying within the official submission pipeline.

### 3. Presets & Formatting (`HealthComponentBuilder`)
- **Classic Hearts (`HEARTS`)**:
  - *Standard (<= 12 hearts / 24 HP)*: Full vibrant red hearts (`❤`), coral half hearts, deep charcoal containers (`0x2E2E2E`), and golden absorption hearts.
  - *High Health / Bosses (> 12 hearts / 24 HP)*: **RPG Tiered Hearts** with tier indicator (e.g. `x5 ❤❤❤❤❤❤❤❤❤❤ 100/100`). Tiers change dynamically every 10 hearts: Tier 1 (Crimson Red), Tier 2 (Radiant Gold), Tier 3 (Emerald Lime), Tier 4 (Diamond Aqua), Tier 5+ (Ender Magenta).
- **Compact (`COMPACT`)**: High-contrast HUD style (`❤ 20/20 HP`) with dynamic status coloring on current HP, muted slate `/` divider, silver max HP, and brilliant gold absorption.
- **Health Bar (`BAR`)**: Solid, non-dithered 10-segment bar (`[██████████] 20/20`) with deep charcoal empty background and gold absorption extensions.
- **Minimal Numeric (`NUMERIC`)**: Ultra-clean tactical badge (`❤ 20.0 / 20.0 HP`).
- **RPG / Hearts (`LIFESTEAL`)**: Dual Heart + HP display: `❤ 10/10 Hearts  20/20 HP`.

### 4. Authentic 9x9 Pixel-Art Heart Sprites & Font Provider Architecture
- **Sprite Sheet (`hearts.png`)**: Composed directly from vanilla Minecraft HUD sprites (`container.png`, `full.png`, `half.png`, `absorbing_full.png`, `container_hardcore.png`, etc.).
- **Private Use Area (PUA) Codepoint Mappings**:
  - `\uE000`: Full Red Heart (Tier 1)
  - `\uE001`: Half Red Heart
  - `\uE002`: Empty Heart Container
  - `\uE003`: Golden Apple Absorption Full Heart
  - `\uE004`: Golden Apple Absorption Half Heart
  - `\uE005`–`\uE008`: RPG Tiers 2–5 Hearts (Radiant Gold, Emerald Lime, Diamond Cyan, Ender Magenta)
  - `\uE009`–`\uE00B`: Hardcore Hearts & Container
- **Font Integration (`assets/shhh-mob-health-indicators/font/hearts.json` & `assets/minecraft/font/default.json`)**:
  - Registered via Minecraft's official bitmap font provider with `height: 9, ascent: 7`.
  - Seamlessly integrates into `submitNameTag`: respects billboard rotation, lighting, distance scaling, and automatic text background box padding with zero extra draw calls.
- **Heart Texture Styles**: Configurable via settings (`Vanilla (Textured)`, `Hardcore (Textured)`, `Unicode (❤)`).

### 5. In-Game Settings UI (`ShhhHealthConfigScreen`) & Commands
- Built using vanilla Minecraft's `HeaderAndFooterLayout` and responsive two-column `LinearLayout` without external GUI dependencies.
- Toggle buttons with `CommonComponents.optionStatus` and comprehensive `Tooltip` guidance.
- Distance slider (`AbstractSliderButton` from 8m to 64m) with deferred saving to prevent disk churn during dragging.
- Cycle buttons for Presets, Positioning, and Heart Texture Styles.
- Integrated into ModMenu via `ModMenuIntegration`.
- **Client Commands (`MobHealthCommand`)**: Accessible via `/mobhealth`, `/smhi`, and `/shhhmobhealth` (`toggle`, `config`, `preset <type>`, `status`, `help`).

### 6. SpongePowered Mixin Class Loading Isolation
- **Package Separation**: Interface duck typing definitions (such as `HealthRenderStateAccessor`) are strictly placed in non-mixin packages (`client.render`) rather than `client.mixin`. In modern SpongePowered Mixin, referencing a class directly from a mixin package throws an `IllegalClassLoadError`.
- **Dynamic Name Tag Alignment**: When a mob has a custom name tag or score text, the indicator automatically calculates line-height offsets to stack cleanly above or below the name tag without overlap.
