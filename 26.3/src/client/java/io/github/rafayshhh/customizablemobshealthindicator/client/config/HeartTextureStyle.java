package io.github.rafayshhh.customizablemobshealthindicator.client.config;

import net.minecraft.network.chat.Component;

/**
 * Visual styling mode for heart icons across all presets.
 */
public enum HeartTextureStyle {
	VANILLA("customizable-mobs-health-indicator.heart_texture.vanilla"),
	HARDCORE("customizable-mobs-health-indicator.heart_texture.hardcore"),
	UNICODE("customizable-mobs-health-indicator.heart_texture.unicode");

	private final String translationKey;

	HeartTextureStyle(String translationKey) {
		this.translationKey = translationKey;
	}

	public String getTranslationKey() {
		return translationKey;
	}

	public Component getDisplayName() {
		return Component.translatable(translationKey);
	}

	public HeartTextureStyle next() {
		HeartTextureStyle[] values = values();
		return values[(this.ordinal() + 1) % values.length];
	}
}
