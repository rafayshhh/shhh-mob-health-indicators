package io.github.rafayshhh.shhhmobhealthindicators;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShhhMobHealthIndicators {
	public static final String MOD_ID = "shhh-mob-health-indicators";
	public static final String MOD_NAME = "Shhh Mob Health Indicators";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
