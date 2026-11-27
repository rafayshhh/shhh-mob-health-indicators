package io.github.rafayshhh.customizablemobshealthindicator.client.config;

import net.minecraft.network.chat.Component;

public enum HealthPreset {
	HEARTS("customizable-mobs-health-indicator.preset.hearts"),
	COMPACT("customizable-mobs-health-indicator.preset.compact"),
	BAR("customizable-mobs-health-indicator.preset.bar"),
	NUMERIC("customizable-mobs-health-indicator.preset.numeric"),
	LIFESTEAL("customizable-mobs-health-indicator.preset.lifesteal");

	private final String translationKey;

	HealthPreset(String translationKey) {
		this.translationKey = translationKey;
	}

	public String getTranslationKey() {
		return translationKey;
	}

	public Component getDisplayName() {
		return Component.translatable(translationKey);
	}

	public HealthPreset next() {
		HealthPreset[] values = values();
		return values[(this.ordinal() + 1) % values.length];
	}
}
