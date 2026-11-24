package io.github.rafayshhh.shhhmobhealthindicators.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.rafayshhh.shhhmobhealthindicators.ShhhMobHealthIndicators;
import io.github.rafayshhh.shhhmobhealthindicators.client.command.MobHealthCommand;
import io.github.rafayshhh.shhhmobhealthindicators.client.config.ShhhHealthConfig;
import io.github.rafayshhh.shhhmobhealthindicators.client.gui.ShhhHealthConfigScreen;
import io.github.rafayshhh.shhhmobhealthindicators.client.health.HealthTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Main client entrypoint for Shhh Mob Health Indicators.
 * Handles keybinding registration, command registration, lifecycle event hooks, and user feedback.
 */
public class ShhhMobHealthIndicatorsClient implements ClientModInitializer {
	public static final KeyMapping.Category KEY_CATEGORY = KeyMapping.Category.register(
		Identifier.fromNamespaceAndPath(ShhhMobHealthIndicators.MOD_ID, "main")
	);

	// Keybind 1: Toggle Mob Health Indicators ON/OFF (Default: F12)
	public static final KeyMapping TOGGLE_KEY = KeyMappingHelper.registerKeyMapping(
		new KeyMapping(
			"key.shhh-mob-health-indicators.toggle",
			InputConstants.KEY_F12,
			KEY_CATEGORY
		)
	);

	// Keybind 2: Open Mob Health Settings Screen (Default: 0)
	public static final KeyMapping SETTINGS_KEY = KeyMappingHelper.registerKeyMapping(
		new KeyMapping(
			"key.shhh-mob-health-indicators.settings",
			InputConstants.KEY_0,
			KEY_CATEGORY
		)
	);

	@Override
	public void onInitializeClient() {
		// 1. Initialize configuration from disk
		ShhhHealthConfig.load();

		// 2. Register client-side commands (/mobhealth, /smhi, /shhhmobhealth)
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> MobHealthCommand.register(dispatcher));

		// 3. Register memory management events to prevent UUID accumulation
		ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> HealthTracker.forget(entity.getUUID()));
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> HealthTracker.clear());

		// 4. Register tick listener for responsive keybind handling
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null || client.gui.screen() != null) {
				return;
			}

			// Handle Toggle Key (F12)
			while (TOGGLE_KEY.consumeClick()) {
				ShhhHealthConfig config = ShhhHealthConfig.get();
				config.enabled = !config.enabled;
				ShhhHealthConfig.save();

				Component feedback;
				if (config.enabled) {
					feedback = Component.translatable("shhh-mob-health-indicators.feedback.enabled", config.preset.getDisplayName())
						.withStyle(ChatFormatting.GREEN);
				} else {
					feedback = Component.translatable("shhh-mob-health-indicators.feedback.disabled")
						.withStyle(ChatFormatting.RED);
				}
				client.player.sendSystemMessage(feedback);
			}

			// Handle Open Settings Key (0)
			while (SETTINGS_KEY.consumeClick()) {
				client.gui.setScreen(new ShhhHealthConfigScreen(client.gui.screen()));
			}
		});

		ShhhMobHealthIndicators.LOGGER.info("Initialized {} (Fabric 26.3)", ShhhMobHealthIndicators.MOD_NAME);
	}
}
