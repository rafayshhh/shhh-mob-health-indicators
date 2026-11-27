package io.github.rafayshhh.customizablemobshealthindicator.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.rafayshhh.customizablemobshealthindicator.CustomizableMobsHealthIndicator;
import io.github.rafayshhh.customizablemobshealthindicator.client.command.MobHealthCommand;
import io.github.rafayshhh.customizablemobshealthindicator.client.config.CustomizableHealthConfig;
import io.github.rafayshhh.customizablemobshealthindicator.client.gui.CustomizableHealthConfigScreen;
import io.github.rafayshhh.customizablemobshealthindicator.client.health.HealthTracker;
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
 * Main client entrypoint for Customizable Mobs Health Indicator.
 * Handles keybinding registration, command registration, lifecycle event hooks, and user feedback.
 */
public class CustomizableMobsHealthIndicatorClient implements ClientModInitializer {
	public static final KeyMapping.Category KEY_CATEGORY = KeyMapping.Category.register(
		Identifier.fromNamespaceAndPath(CustomizableMobsHealthIndicator.MOD_ID, "main")
	);

	// Keybind 1: Toggle Mob Health Indicators ON/OFF (Default: F12)
	public static final KeyMapping TOGGLE_KEY = KeyMappingHelper.registerKeyMapping(
		new KeyMapping(
			"key.customizable-mobs-health-indicator.toggle",
			InputConstants.KEY_F12,
			KEY_CATEGORY
		)
	);

	// Keybind 2: Open Mob Health Settings Screen (Default: 0)
	public static final KeyMapping SETTINGS_KEY = KeyMappingHelper.registerKeyMapping(
		new KeyMapping(
			"key.customizable-mobs-health-indicator.settings",
			InputConstants.KEY_0,
			KEY_CATEGORY
		)
	);

	@Override
	public void onInitializeClient() {
		// 1. Initialize configuration from disk
		CustomizableHealthConfig.load();

		// 2. Register client-side commands (/mobhealth, /cmhi, /custommobhealth)
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
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.enabled = !config.enabled;
				CustomizableHealthConfig.save();

				Component feedback;
				if (config.enabled) {
					feedback = Component.translatable("customizable-mobs-health-indicator.feedback.enabled", config.preset.getDisplayName())
						.withStyle(ChatFormatting.GREEN);
				} else {
					feedback = Component.translatable("customizable-mobs-health-indicator.feedback.disabled")
						.withStyle(ChatFormatting.RED);
				}
				client.player.sendSystemMessage(feedback);
			}

			// Handle Open Settings Key (0)
			while (SETTINGS_KEY.consumeClick()) {
				client.gui.setScreen(new CustomizableHealthConfigScreen(client.gui.screen()));
			}
		});

		CustomizableMobsHealthIndicator.LOGGER.info("Initialized {} (Fabric 26.3)", CustomizableMobsHealthIndicator.MOD_NAME);
	}
}
