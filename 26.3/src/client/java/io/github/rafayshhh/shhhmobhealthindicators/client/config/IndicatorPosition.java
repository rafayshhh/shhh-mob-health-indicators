package io.github.rafayshhh.shhhmobhealthindicators.client.config;

import net.minecraft.network.chat.Component;

public enum IndicatorPosition {
	ABOVE_NAME("shhh-mob-health-indicators.position.above"),
	BELOW_NAME("shhh-mob-health-indicators.position.below");

	private final String translationKey;

	IndicatorPosition(String translationKey) {
		this.translationKey = translationKey;
	}

	public String getTranslationKey() {
		return translationKey;
	}

	public Component getDisplayName() {
		return Component.translatable(translationKey);
	}

	public IndicatorPosition next() {
		IndicatorPosition[] values = values();
		return values[(this.ordinal() + 1) % values.length];
	}
}
