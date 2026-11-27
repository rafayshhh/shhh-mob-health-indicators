package io.github.rafayshhh.customizablemobshealthindicator.client.health;

import io.github.rafayshhh.customizablemobshealthindicator.client.config.CustomizableHealthConfig;
import io.github.rafayshhh.customizablemobshealthindicator.client.config.HealthPreset;
import io.github.rafayshhh.customizablemobshealthindicator.client.config.HeartTextureStyle;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Formats HealthInfo into rich, styled Component instances based on active preset.
 * Renders authentic vanilla 9x9 textured pixel-art heart sprites, RPG Tiered Hearts,
 * and high-contrast HUD typography.
 */
public class HealthComponentBuilder {
	// Custom Font linking to 9x9 pixel-art heart sprites in textures/font/hearts.png
	public static final Identifier HEARTS_FONT_ID = Identifier.fromNamespaceAndPath("customizable-mobs-health-indicator", "hearts");
	public static final FontDescription.Resource HEARTS_FONT = new FontDescription.Resource(HEARTS_FONT_ID);
	public static final Style HEART_STYLE = Style.EMPTY.withFont(HEARTS_FONT);

	// Private Use Area (PUA) Codepoint Mappings to 9x9 Heart Sprites
	public static final String SPRITE_FULL = "\uE000";         // Vanilla Full Red Heart
	public static final String SPRITE_HALF = "\uE001";         // Vanilla Half Red Heart
	public static final String SPRITE_CONTAINER = "\uE002";    // Vanilla Empty Heart Container
	public static final String SPRITE_ABS_FULL = "\uE003";     // Vanilla Golden Apple Absorption Full Heart
	public static final String SPRITE_ABS_HALF = "\uE004";     // Vanilla Golden Apple Absorption Half Heart
	public static final String SPRITE_TIER_2 = "\uE005";       // Lifesteal/RPG Tier 2 Radiant Gold Heart
	public static final String SPRITE_TIER_3 = "\uE006";       // Lifesteal/RPG Tier 3 Emerald Lime Heart
	public static final String SPRITE_TIER_4 = "\uE007";       // Lifesteal/RPG Tier 4 Diamond Cyan Heart
	public static final String SPRITE_TIER_5 = "\uE008";       // Lifesteal/RPG Tier 5 Ender Magenta Heart
	public static final String SPRITE_HC_FULL = "\uE009";      // Hardcore Full Heart
	public static final String SPRITE_HC_HALF = "\uE00A";      // Hardcore Half Heart
	public static final String SPRITE_HC_CONTAINER = "\uE00B"; // Hardcore Empty Container
	public static final String SPRITE_SPACER = "\uE00F";       // 9px Invisible Spacer for Left-Aligned Grid Rows

	// Status & Health Colors
	public static final TextColor COLOR_HIGH = TextColor.fromRgb(0x44FF66);        // Vibrant Emerald Green
	public static final TextColor COLOR_MED = TextColor.fromRgb(0xFFFF44);         // Bright Yellow
	public static final TextColor COLOR_LOW = TextColor.fromRgb(0xFFAA00);         // Amber Orange
	public static final TextColor COLOR_CRIT = TextColor.fromRgb(0xFF3333);        // Crimson Red
	public static final TextColor COLOR_ABSORPTION = TextColor.fromRgb(0xFFD700);  // Golden Apple Gold

	// Structural Colors
	public static final TextColor COLOR_HEART_FULL = TextColor.fromRgb(0xFF3838);  // Vibrant Red
	public static final TextColor COLOR_HEART_HALF = TextColor.fromRgb(0xFF7B7B);  // Coral
	public static final TextColor COLOR_EMPTY = TextColor.fromRgb(0x2E2E2E);       // Dark Charcoal Container
	public static final TextColor COLOR_DIVIDER = TextColor.fromRgb(0x666666);     // Slate Separator
	public static final TextColor COLOR_MAX_HP = TextColor.fromRgb(0xDDDDDD);      // Crisp Silver Max HP
	public static final TextColor COLOR_MUTED_TAG = TextColor.fromRgb(0x888888);   // Muted Gray Label

	// RPG Tier Colors for High Health (> 20 HP)
	public static final TextColor TIER_1 = TextColor.fromRgb(0xFF3838); // Crimson Red (1-20 HP)
	public static final TextColor TIER_2 = TextColor.fromRgb(0xFFAA00); // Radiant Gold (21-40 HP)
	public static final TextColor TIER_3 = TextColor.fromRgb(0x44FF44); // Emerald Lime (41-60 HP)
	public static final TextColor TIER_4 = TextColor.fromRgb(0x00FFFF); // Diamond Aqua (61-80 HP)
	public static final TextColor TIER_5 = TextColor.fromRgb(0xFF55FF); // Ender Magenta (81-100+ HP)

	public static Component build(HealthInfo info, HealthPreset preset, CustomizableHealthConfig config) {
		if (info == null) {
			return Component.empty();
		}

		return switch (preset) {
			case HEARTS -> buildHearts(info, config);
			case COMPACT -> buildCompact(info, config);
			case BAR -> buildBar(info, config);
			case NUMERIC -> buildNumeric(info, config);
			case LIFESTEAL -> buildLifesteal(info, config);
		};
	}

	/**
	 * Formats HealthInfo into a vertical stack of rows for 3D nametag rendering.
	 * For HEARTS preset, returns 10-heart rows stacked vertically.
	 * For other presets, returns a single-element list.
	 */
	public static List<Component> buildRows(HealthInfo info, HealthPreset preset, CustomizableHealthConfig config) {
		if (info == null) {
			return List.of();
		}
		if (preset == HealthPreset.HEARTS) {
			return buildHeartRows(info, config);
		} else {
			Component single = build(info, preset, config);
			return single.getString().isEmpty() ? List.of() : List.of(single);
		}
	}

	/**
	 * Preset 1: Classic Hearts (Single-component fallback)
	 */
	private static Component buildHearts(HealthInfo info, CustomizableHealthConfig config) {
		List<Component> rows = buildHeartRows(info, config);
		if (rows.isEmpty()) {
			return Component.empty();
		}
		MutableComponent joined = Component.empty();
		for (int i = 0; i < rows.size(); i++) {
			if (i > 0) {
				joined.append(Component.literal(" "));
			}
			joined.append(rows.get(i));
		}
		return joined;
	}

	/**
	 * Preset 1: Classic Hearts - Multi-Row Grid Builder (10 Hearts Per Row).
	 * Formats hearts into rows of up to 10 hearts.
	 * When entity has > 10 hearts (high health or absorption), extra hearts
	 * stack into row 2, row 3, etc.
	 * In a multi-row display, partial rows are padded on the right with SPRITE_SPACER (\uE00F)
	 * so every row has an identical width and aligns perfectly on the left.
	 */
	public static List<Component> buildHeartRows(HealthInfo info, CustomizableHealthConfig config) {
		if (info == null) {
			return List.of();
		}

		float health = Math.max(0.0f, info.health());
		float maxHealth = Math.max(1.0f, info.maxHealth());
		float absorption = (config.showAbsorption && info.hasAbsorption()) ? info.absorption() : 0.0f;

		int fullHearts = (int) (health / 2.0f);
		boolean halfHeart = (health % 2.0f) >= 0.5f;
		int totalContainers = Math.max(10, (int) Math.ceil(maxHealth / 2.0f));

		int goldFullHearts = (int) (absorption / 2.0f);
		boolean goldHalfHeart = (absorption % 2.0f) >= 0.5f;

		boolean useTextured = config.heartTexture != HeartTextureStyle.UNICODE;
		boolean isHardcore = config.heartTexture == HeartTextureStyle.HARDCORE;

		String fullGlyph = isHardcore ? SPRITE_HC_FULL : SPRITE_FULL;
		String halfGlyph = isHardcore ? SPRITE_HC_HALF : SPRITE_HALF;
		String containerGlyph = isHardcore ? SPRITE_HC_CONTAINER : SPRITE_CONTAINER;

		// 1. Collect all heart tokens in order: Red Hearts -> Absorption Hearts -> Empty Containers
		List<Component> slotComponents = new ArrayList<>();

		// 1a. Full and half red hearts
		for (int i = 0; i < fullHearts; i++) {
			if (useTextured) {
				slotComponents.add(Component.literal(fullGlyph).withStyle(HEART_STYLE));
			} else {
				slotComponents.add(Component.literal("❤").withStyle(Style.EMPTY.withColor(COLOR_HEART_FULL)));
			}
		}
		if (halfHeart) {
			if (useTextured) {
				slotComponents.add(Component.literal(halfGlyph).withStyle(HEART_STYLE));
			} else {
				slotComponents.add(Component.literal("❤").withStyle(Style.EMPTY.withColor(COLOR_HEART_HALF)));
			}
		}

		// 1b. Absorption hearts
		for (int i = 0; i < goldFullHearts; i++) {
			if (useTextured) {
				slotComponents.add(Component.literal(SPRITE_ABS_FULL).withStyle(HEART_STYLE));
			} else {
				slotComponents.add(Component.literal("❤").withStyle(Style.EMPTY.withColor(COLOR_ABSORPTION)));
			}
		}
		if (goldHalfHeart) {
			if (useTextured) {
				slotComponents.add(Component.literal(SPRITE_ABS_HALF).withStyle(HEART_STYLE));
			} else {
				slotComponents.add(Component.literal("❤").withStyle(Style.EMPTY.withColor(COLOR_ABSORPTION)));
			}
		}

		// 1c. Empty containers (fill remaining capacity up to totalContainers)
		int filled = slotComponents.size();
		if (filled < totalContainers) {
			int emptyCount = totalContainers - filled;
			for (int i = 0; i < emptyCount; i++) {
				if (useTextured) {
					slotComponents.add(Component.literal(containerGlyph).withStyle(HEART_STYLE));
				} else {
					slotComponents.add(Component.literal("❤").withStyle(Style.EMPTY.withColor(COLOR_EMPTY)));
				}
			}
		}

		if (slotComponents.isEmpty()) {
			return List.of();
		}

		int maxRows = Math.clamp(config.maxHeartRows, 1, 5);
		int totalSlots = slotComponents.size();
		int rowCount = Math.min(maxRows, (int) Math.ceil(totalSlots / 10.0));

		List<Component> rows = new ArrayList<>(rowCount);
		for (int r = 0; r < rowCount; r++) {
			MutableComponent row = Component.empty();
			int startIdx = r * 10;
			int endIdx = Math.min(startIdx + 10, totalSlots);
			int inThisRow = 0;

			for (int i = startIdx; i < endIdx; i++) {
				row.append(slotComponents.get(i));
				inThisRow++;
			}

			// In a multi-row display, pad partial rows on the right to 10 slots with SPRITE_SPACER
			// so that each row is 10 slots wide and aligns perfectly on the left.
			if (rowCount > 1 && inThisRow < 10) {
				int spacersNeeded = 10 - inThisRow;
				for (int s = 0; s < spacersNeeded; s++) {
					row.append(Component.literal(SPRITE_SPACER).withStyle(HEART_STYLE));
				}
			}

			rows.add(row);
		}

		return rows;
	}

	/**
	 * Preset 2: Compact
	 * Example: "[Heart Sprite] 34/40 HP (+4.0 [Gold Heart Sprite])"
	 */
	private static Component buildCompact(HealthInfo info, CustomizableHealthConfig config) {
		MutableComponent root = Component.empty();
		TextColor hpColor = getHealthColor(info.percentage(), config.colorGrading);

		root.append(getHeartIcon(config, COLOR_HEART_FULL)).append(Component.literal(" "));
		root.append(Component.literal(formatNumber(info.health())).withStyle(Style.EMPTY.withColor(hpColor).withBold(true)));
		root.append(Component.literal("/").withStyle(Style.EMPTY.withColor(COLOR_DIVIDER)));
		root.append(Component.literal(formatNumber(info.maxHealth())).withStyle(Style.EMPTY.withColor(COLOR_MAX_HP)));
		root.append(Component.literal(" HP").withStyle(Style.EMPTY.withColor(COLOR_MUTED_TAG)));

		if (config.showAbsorption && info.hasAbsorption()) {
			root.append(Component.literal(" (+" + formatNumber(info.absorption()) + " "));
			root.append(getAbsorptionIcon(config));
			root.append(Component.literal(")"));
		}

		return root;
	}

	/**
	 * Preset 3: Health Bar
	 * Example: "[Heart Sprite] [██████████] 34/40 (+4.0 [Gold Heart Sprite])"
	 */
	private static Component buildBar(HealthInfo info, CustomizableHealthConfig config) {
		MutableComponent root = Component.empty();
		TextColor hpColor = getHealthColor(info.percentage(), config.colorGrading);

		root.append(getHeartIcon(config, hpColor)).append(Component.literal(" "));
		root.append(Component.literal("[").withStyle(Style.EMPTY.withColor(COLOR_DIVIDER)));

		int totalSegments = 10;
		int filledSegments = Math.round(info.percentage() * totalSegments);

		for (int i = 0; i < totalSegments; i++) {
			if (i < filledSegments) {
				root.append(Component.literal("█").withStyle(Style.EMPTY.withColor(hpColor)));
			} else {
				root.append(Component.literal("█").withStyle(Style.EMPTY.withColor(COLOR_EMPTY)));
			}
		}

		if (config.showAbsorption && info.hasAbsorption()) {
			int absSegments = Math.clamp(Math.round((info.absorption() / 4.0f) * 2), 1, 4);
			for (int i = 0; i < absSegments; i++) {
				root.append(Component.literal("█").withStyle(Style.EMPTY.withColor(COLOR_ABSORPTION)));
			}
		}

		root.append(Component.literal("] ").withStyle(Style.EMPTY.withColor(COLOR_DIVIDER)));
		root.append(Component.literal(formatNumber(info.health())).withStyle(Style.EMPTY.withColor(hpColor).withBold(true)));
		root.append(Component.literal("/").withStyle(Style.EMPTY.withColor(COLOR_DIVIDER)));
		root.append(Component.literal(formatNumber(info.maxHealth())).withStyle(Style.EMPTY.withColor(COLOR_MAX_HP)));

		if (config.showAbsorption && info.hasAbsorption()) {
			root.append(Component.literal(" (+" + formatNumber(info.absorption()) + " "));
			root.append(getAbsorptionIcon(config));
			root.append(Component.literal(")"));
		}

		return root;
	}

	/**
	 * Preset 4: Minimal Numeric
	 * Example: "[Heart Sprite] 34.0 / 40.0 HP (+4.0 [Gold Heart Sprite])"
	 */
	private static Component buildNumeric(HealthInfo info, CustomizableHealthConfig config) {
		MutableComponent root = Component.empty();
		TextColor hpColor = getHealthColor(info.percentage(), config.colorGrading);

		root.append(getHeartIcon(config, COLOR_HEART_FULL)).append(Component.literal(" "));
		root.append(Component.literal(formatNumber(info.health())).withStyle(Style.EMPTY.withColor(hpColor).withBold(true)));
		root.append(Component.literal(" / ").withStyle(Style.EMPTY.withColor(COLOR_DIVIDER)));
		root.append(Component.literal(formatNumber(info.maxHealth()) + " HP").withStyle(Style.EMPTY.withColor(COLOR_MAX_HP)));

		if (config.showAbsorption && info.hasAbsorption()) {
			root.append(Component.literal(" (+" + formatNumber(info.absorption()) + " "));
			root.append(getAbsorptionIcon(config));
			root.append(Component.literal(")"));
		}

		return root;
	}

	/**
	 * Preset 5: Lifesteal / RPG
	 * Example: "[Heart Sprite] 17/20 Hearts  34/40 HP (+4.0 [Gold Heart Sprite])"
	 */
	private static Component buildLifesteal(HealthInfo info, CustomizableHealthConfig config) {
		MutableComponent root = Component.empty();
		TextColor hpColor = getHealthColor(info.percentage(), config.colorGrading);

		float hearts = info.health() / 2.0f;
		float maxHearts = info.maxHealth() / 2.0f;

		root.append(getHeartIcon(config, COLOR_HEART_FULL)).append(Component.literal(" "));
		root.append(Component.literal(formatNumber(hearts)).withStyle(Style.EMPTY.withColor(hpColor).withBold(true)));
		root.append(Component.literal("/").withStyle(Style.EMPTY.withColor(COLOR_DIVIDER)));
		root.append(Component.literal(formatNumber(maxHearts)).withStyle(Style.EMPTY.withColor(COLOR_MAX_HP)));
		root.append(Component.literal(" Hearts").withStyle(Style.EMPTY.withColor(COLOR_MUTED_TAG)));

		root.append(Component.literal("  " + formatNumber(info.health())).withStyle(Style.EMPTY.withColor(hpColor)));
		root.append(Component.literal("/").withStyle(Style.EMPTY.withColor(COLOR_DIVIDER)));
		root.append(Component.literal(formatNumber(info.maxHealth()) + " HP").withStyle(Style.EMPTY.withColor(COLOR_MAX_HP)));

		if (config.showAbsorption && info.hasAbsorption()) {
			root.append(Component.literal(" (+" + formatNumber(info.absorption()) + " "));
			root.append(getAbsorptionIcon(config));
			root.append(Component.literal(")"));
		}

		return root;
	}

	public static Component getHeartIcon(CustomizableHealthConfig config, TextColor unicodeColor) {
		if (config.heartTexture == HeartTextureStyle.UNICODE) {
			return Component.literal("❤").withStyle(Style.EMPTY.withColor(unicodeColor));
		}
		String glyph = config.heartTexture == HeartTextureStyle.HARDCORE ? SPRITE_HC_FULL : SPRITE_FULL;
		return Component.literal(glyph).withStyle(HEART_STYLE);
	}

	public static Component getAbsorptionIcon(CustomizableHealthConfig config) {
		if (config.heartTexture == HeartTextureStyle.UNICODE) {
			return Component.literal("💛").withStyle(Style.EMPTY.withColor(COLOR_ABSORPTION));
		}
		return Component.literal(SPRITE_ABS_FULL).withStyle(HEART_STYLE);
	}

	public static String getTierSprite(int tier, boolean isHardcore) {
		return switch (tier) {
			case 1 -> isHardcore ? SPRITE_HC_FULL : SPRITE_FULL;
			case 2 -> SPRITE_TIER_2;
			case 3 -> SPRITE_TIER_3;
			case 4 -> SPRITE_TIER_4;
			default -> SPRITE_TIER_5;
		};
	}

	public static TextColor getTierColor(int tier) {
		return switch (tier) {
			case 1 -> TIER_1;
			case 2 -> TIER_2;
			case 3 -> TIER_3;
			case 4 -> TIER_4;
			default -> TIER_5;
		};
	}

	public static TextColor getHealthColor(float percentage, boolean colorGrading) {
		if (!colorGrading) {
			return COLOR_HIGH;
		}
		if (percentage >= 0.70f) {
			return COLOR_HIGH;
		} else if (percentage >= 0.45f) {
			return COLOR_MED;
		} else if (percentage >= 0.20f) {
			return COLOR_LOW;
		} else {
			return COLOR_CRIT;
		}
	}

	public static String formatNumber(float val) {
		int rounded = Math.round(val);
		if (Math.abs(val - rounded) < 0.05f) {
			return Integer.toString(rounded);
		}
		int tenths = Math.round(val * 10.0f);
		return (tenths / 10) + "." + Math.abs(tenths % 10);
	}
}
