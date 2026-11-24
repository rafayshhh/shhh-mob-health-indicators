package io.github.rafayshhh.shhhmobhealthindicators.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.rafayshhh.shhhmobhealthindicators.client.config.HealthPreset;
import io.github.rafayshhh.shhhmobhealthindicators.client.config.ShhhHealthConfig;
import io.github.rafayshhh.shhhmobhealthindicators.client.gui.ShhhHealthConfigScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.Locale;

/**
 * Client-side command handler for /mobhealth, /smhi, and /shhhmobhealth.
 */
public class MobHealthCommand {

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		String[] aliases = {"mobhealth", "smhi", "shhhmobhealth"};

		for (String alias : aliases) {
			dispatcher.register(
				ClientCommands.literal(alias)
					.executes(ctx -> openConfig())
					.then(ClientCommands.literal("config")
						.executes(ctx -> openConfig()))
					.then(ClientCommands.literal("toggle")
						.executes(ctx -> toggle(ctx.getSource())))
					.then(ClientCommands.literal("status")
						.executes(ctx -> showStatus(ctx.getSource())))
					.then(ClientCommands.literal("help")
						.executes(ctx -> showHelp(ctx.getSource())))
					.then(ClientCommands.literal("preset")
						.then(ClientCommands.argument("type", StringArgumentType.word())
							.suggests((ctx, builder) -> {
								for (HealthPreset p : HealthPreset.values()) {
									builder.suggest(p.name().toLowerCase(Locale.ROOT));
								}
								return builder.buildFuture();
							})
							.executes(ctx -> setPreset(ctx.getSource(), StringArgumentType.getString(ctx, "type")))))
			);
		}
	}

	private static int openConfig() {
		Minecraft client = Minecraft.getInstance();
		client.execute(() -> client.gui.setScreen(new ShhhHealthConfigScreen(null)));
		return 1;
	}

	private static int toggle(FabricClientCommandSource source) {
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
		source.sendFeedback(feedback);
		return 1;
	}

	private static int setPreset(FabricClientCommandSource source, String type) {
		ShhhHealthConfig config = ShhhHealthConfig.get();
		for (HealthPreset preset : HealthPreset.values()) {
			if (preset.name().equalsIgnoreCase(type)) {
				config.preset = preset;
				ShhhHealthConfig.save();
				source.sendFeedback(
					Component.translatable("shhh-mob-health-indicators.command.preset_changed")
						.append(": ")
						.append(preset.getDisplayName())
						.withStyle(ChatFormatting.GREEN)
				);
				return 1;
			}
		}

		source.sendFeedback(
			Component.translatable("shhh-mob-health-indicators.command.invalid_preset", type)
				.withStyle(ChatFormatting.RED)
		);
		return 0;
	}

	private static int showStatus(FabricClientCommandSource source) {
		ShhhHealthConfig config = ShhhHealthConfig.get();
		source.sendFeedback(
			Component.literal("=== Shhh Mob Health Indicators ===")
				.withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
		);
		source.sendFeedback(
			Component.literal("• Status: ")
				.withStyle(ChatFormatting.GRAY)
				.append(Component.literal(config.enabled ? "ENABLED" : "DISABLED")
					.withStyle(config.enabled ? ChatFormatting.GREEN : ChatFormatting.RED))
		);
		source.sendFeedback(
			Component.literal("• Preset: ")
				.withStyle(ChatFormatting.GRAY)
				.append(config.preset.getDisplayName().copy().withStyle(ChatFormatting.AQUA))
		);
		source.sendFeedback(
			Component.literal("• Max Distance: ")
				.withStyle(ChatFormatting.GRAY)
				.append(Component.literal(Math.round(config.maxDistance) + "m").withStyle(ChatFormatting.YELLOW))
		);
		source.sendFeedback(
			Component.literal("• Filters: ")
				.withStyle(ChatFormatting.GRAY)
				.append(Component.literal("Hostile=" + config.showHostile + " | Passive=" + config.showPassive + " | Boss=" + config.showBosses)
					.withStyle(ChatFormatting.WHITE))
		);
		return 1;
	}

	private static int showHelp(FabricClientCommandSource source) {
		source.sendFeedback(
			Component.literal("=== Shhh Mob Health Indicators Commands ===")
				.withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
		);
		source.sendFeedback(Component.literal("/mobhealth - Open configuration screen").withStyle(ChatFormatting.YELLOW));
		source.sendFeedback(Component.literal("/mobhealth toggle - Toggle indicators ON/OFF").withStyle(ChatFormatting.YELLOW));
		source.sendFeedback(Component.literal("/mobhealth preset <hearts|compact|bar|numeric|lifesteal> - Change preset").withStyle(ChatFormatting.YELLOW));
		source.sendFeedback(Component.literal("/mobhealth status - Display active configuration").withStyle(ChatFormatting.YELLOW));
		source.sendFeedback(Component.literal("Aliases: /smhi, /shhhmobhealth").withStyle(ChatFormatting.DARK_GRAY));
		return 1;
	}
}
