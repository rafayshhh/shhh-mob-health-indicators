package io.github.rafayshhh.shhhmobhealthindicators.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import io.github.rafayshhh.shhhmobhealthindicators.ShhhMobHealthIndicators;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Thread-safe configuration manager for Shhh Mob Health Indicators.
 * Handles loading, dirty-check saving, sanitization, and automatic error recovery.
 */
public class ShhhHealthConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("shhh-mob-health-indicators.json");

	private static ShhhHealthConfig instance;

	// Configurable settings
	public boolean enabled = true;
	public HealthPreset preset = HealthPreset.HEARTS;
	public IndicatorPosition position = IndicatorPosition.ABOVE_NAME;
	public HeartTextureStyle heartTexture = HeartTextureStyle.VANILLA;
	public boolean showAbsorption = true;
	public boolean showHostile = true;
	public boolean showPassive = true;
	public boolean showBosses = true;
	public boolean showNamedOnly = false;
	public boolean hideFullHealth = false;
	public boolean hideInvisible = true;
	public float maxDistance = 32.0f;
	public boolean colorGrading = true;

	// State tracking for dirty-check saving
	private transient boolean lastSavedEnabled = true;
	private transient HealthPreset lastSavedPreset = HealthPreset.HEARTS;
	private transient IndicatorPosition lastSavedPosition = IndicatorPosition.ABOVE_NAME;
	private transient HeartTextureStyle lastSavedHeartTexture = HeartTextureStyle.VANILLA;
	private transient boolean lastSavedShowAbsorption = true;
	private transient boolean lastSavedShowHostile = true;
	private transient boolean lastSavedShowPassive = true;
	private transient boolean lastSavedShowBosses = true;
	private transient boolean lastSavedShowNamedOnly = false;
	private transient boolean lastSavedHideFullHealth = false;
	private transient boolean lastSavedHideInvisible = true;
	private transient float lastSavedMaxDistance = 32.0f;
	private transient boolean lastSavedColorGrading = true;
	private transient boolean isInitialized = false;

	public static ShhhHealthConfig get() {
		if (instance == null) {
			load();
		}
		return instance;
	}

	public static synchronized void load() {
		boolean needsRewrite = false;

		if (Files.exists(CONFIG_PATH)) {
			try {
				long fileSize = Files.size(CONFIG_PATH);
				if (fileSize == 0) {
					ShhhMobHealthIndicators.LOGGER.warn("Config file is empty. Restoring defaults.");
					instance = new ShhhHealthConfig();
					needsRewrite = true;
				} else {
					try (Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
						instance = GSON.fromJson(reader, ShhhHealthConfig.class);
					}
					if (instance == null) {
						ShhhMobHealthIndicators.LOGGER.warn("Config file contained null JSON. Restoring defaults.");
						instance = new ShhhHealthConfig();
						needsRewrite = true;
					}
				}
			} catch (JsonParseException e) {
				ShhhMobHealthIndicators.LOGGER.warn("Config file has invalid JSON syntax. Recovering with defaults: {}", e.getMessage());
				instance = new ShhhHealthConfig();
				needsRewrite = true;
			} catch (Exception e) {
				ShhhMobHealthIndicators.LOGGER.error("Failed to read config file. Using defaults.", e);
				instance = new ShhhHealthConfig();
				needsRewrite = true;
			}
		} else {
			instance = new ShhhHealthConfig();
			needsRewrite = true;
		}

		sanitize(instance);

		instance.lastSavedEnabled = instance.enabled;
		instance.lastSavedPreset = instance.preset;
		instance.lastSavedPosition = instance.position;
		instance.lastSavedHeartTexture = instance.heartTexture;
		instance.lastSavedShowAbsorption = instance.showAbsorption;
		instance.lastSavedShowHostile = instance.showHostile;
		instance.lastSavedShowPassive = instance.showPassive;
		instance.lastSavedShowBosses = instance.showBosses;
		instance.lastSavedShowNamedOnly = instance.showNamedOnly;
		instance.lastSavedHideFullHealth = instance.hideFullHealth;
		instance.lastSavedHideInvisible = instance.hideInvisible;
		instance.lastSavedMaxDistance = instance.maxDistance;
		instance.lastSavedColorGrading = instance.colorGrading;
		instance.isInitialized = true;

		if (needsRewrite) {
			forceSave();
		}
	}

	public static synchronized void save() {
		if (instance == null) {
			instance = new ShhhHealthConfig();
		}

		sanitize(instance);

		if (instance.isInitialized
			&& instance.enabled == instance.lastSavedEnabled
			&& instance.preset == instance.lastSavedPreset
			&& instance.position == instance.lastSavedPosition
			&& instance.heartTexture == instance.lastSavedHeartTexture
			&& instance.showAbsorption == instance.lastSavedShowAbsorption
			&& instance.showHostile == instance.lastSavedShowHostile
			&& instance.showPassive == instance.lastSavedShowPassive
			&& instance.showBosses == instance.lastSavedShowBosses
			&& instance.showNamedOnly == instance.lastSavedShowNamedOnly
			&& instance.hideFullHealth == instance.lastSavedHideFullHealth
			&& instance.hideInvisible == instance.lastSavedHideInvisible
			&& Math.abs(instance.maxDistance - instance.lastSavedMaxDistance) < 0.01f
			&& instance.colorGrading == instance.lastSavedColorGrading) {
			return;
		}

		forceSave();
	}

	private static synchronized void forceSave() {
		try {
			Path parent = CONFIG_PATH.getParent();
			if (parent != null && !Files.exists(parent)) {
				Files.createDirectories(parent);
			}

			try (Writer writer = Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
				GSON.toJson(instance, writer);
			}

			instance.lastSavedEnabled = instance.enabled;
			instance.lastSavedPreset = instance.preset;
			instance.lastSavedPosition = instance.position;
			instance.lastSavedHeartTexture = instance.heartTexture;
			instance.lastSavedShowAbsorption = instance.showAbsorption;
			instance.lastSavedShowHostile = instance.showHostile;
			instance.lastSavedShowPassive = instance.showPassive;
			instance.lastSavedShowBosses = instance.showBosses;
			instance.lastSavedShowNamedOnly = instance.showNamedOnly;
			instance.lastSavedHideFullHealth = instance.hideFullHealth;
			instance.lastSavedHideInvisible = instance.hideInvisible;
			instance.lastSavedMaxDistance = instance.maxDistance;
			instance.lastSavedColorGrading = instance.colorGrading;
			instance.isInitialized = true;
		} catch (Exception e) {
			ShhhMobHealthIndicators.LOGGER.error("Failed to write config file.", e);
		}
	}

	private static void sanitize(ShhhHealthConfig config) {
		if (config.preset == null) {
			config.preset = HealthPreset.HEARTS;
		}
		if (config.position == null) {
			config.position = IndicatorPosition.ABOVE_NAME;
		}
		if (config.heartTexture == null) {
			config.heartTexture = HeartTextureStyle.VANILLA;
		}
		config.maxDistance = Math.clamp(config.maxDistance, 8.0f, 64.0f);
	}
}
