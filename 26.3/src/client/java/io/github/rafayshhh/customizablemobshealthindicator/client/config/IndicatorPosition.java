package io.github.rafayshhh.customizablemobshealthindicator.client.config;

import net.minecraft.network.chat.Component;

public enum IndicatorPosition {
	ABOVE_NAME("customizable-mobs-health-indicator.position.above"),
	BELOW_NAME("customizable-mobs-health-indicator.position.below");

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
