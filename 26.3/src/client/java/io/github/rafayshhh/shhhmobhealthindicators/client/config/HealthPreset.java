package io.github.rafayshhh.shhhmobhealthindicators.client.config;

import net.minecraft.network.chat.Component;

public enum HealthPreset {
	HEARTS("shhh-mob-health-indicators.preset.hearts"),
	COMPACT("shhh-mob-health-indicators.preset.compact"),
	BAR("shhh-mob-health-indicators.preset.bar"),
	NUMERIC("shhh-mob-health-indicators.preset.numeric"),
	LIFESTEAL("shhh-mob-health-indicators.preset.lifesteal");

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
