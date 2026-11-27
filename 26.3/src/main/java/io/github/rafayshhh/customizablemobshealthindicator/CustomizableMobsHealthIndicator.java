package io.github.rafayshhh.customizablemobshealthindicator;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomizableMobsHealthIndicator {
	public static final String MOD_ID = "customizable-mobs-health-indicator";
	public static final String MOD_NAME = "Customizable Mobs Health Indicator";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
