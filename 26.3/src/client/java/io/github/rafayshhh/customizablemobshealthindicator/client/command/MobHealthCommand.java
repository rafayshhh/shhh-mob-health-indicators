package io.github.rafayshhh.customizablemobshealthindicator.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.rafayshhh.customizablemobshealthindicator.client.config.CustomizableHealthConfig;
import io.github.rafayshhh.customizablemobshealthindicator.client.config.HealthPreset;
import io.github.rafayshhh.customizablemobshealthindicator.client.config.HeartTextureStyle;
import io.github.rafayshhh.customizablemobshealthindicator.client.config.IndicatorPosition;
import io.github.rafayshhh.customizablemobshealthindicator.client.gui.CustomizableHealthConfigScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Client-side command handler for /mobhealth, /cmhi, /custommobhealth, and /mobhealthindicators.
 * Provides exhaustive runtime configuration and status inspection without opening menus.
 */
public class MobHealthCommand {

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		String[] aliases = {"mobhealth", "cmhi", "custommobhealth", "mobhealthindicators"};
		for (String alias : aliases) {
			registerCommands(dispatcher, alias);
		}
	}

	private static void openGui(FabricClientCommandSource source) {
		Minecraft client = source.getClient();
		client.schedule(() -> client.gui.setScreen(new CustomizableHealthConfigScreen(null)));
	}

	private static Component formatStatus(boolean status) {
		return status ? Component.literal("ON").withStyle(ChatFormatting.GREEN) : Component.literal("OFF").withStyle(ChatFormatting.RED);
	}

	private static LiteralArgumentBuilder<FabricClientCommandSource> createToggleSubcommand(
		String name,
		String translationKey,
		Consumer<CustomizableHealthConfig> toggler,
		BiConsumer<CustomizableHealthConfig, Boolean> setter,
		Predicate<CustomizableHealthConfig> getter
	) {
		return ClientCommands.literal(name)
			.executes(context -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				toggler.accept(config);
				CustomizableHealthConfig.save();
				boolean newVal = getter.test(config);
				context.getSource().sendFeedback(
					Component.translatable(translationKey, formatStatus(newVal))
				);
				return 1;
			})
			.then(
				ClientCommands.argument("state", StringArgumentType.word())
					.suggests((context, builder) -> {
						builder.suggest("on");
						builder.suggest("off");
						builder.suggest("toggle");
						return builder.buildFuture();
					})
					.executes(context -> {
						String state = StringArgumentType.getString(context, "state").toLowerCase(Locale.ROOT);
						CustomizableHealthConfig config = CustomizableHealthConfig.get();
						boolean newVal;
						if ("on".equals(state) || "true".equals(state)) {
							newVal = true;
							setter.accept(config, true);
						} else if ("off".equals(state) || "false".equals(state)) {
							newVal = false;
							setter.accept(config, false);
						} else {
							toggler.accept(config);
							newVal = getter.test(config);
						}
						CustomizableHealthConfig.save();
						context.getSource().sendFeedback(
							Component.translatable(translationKey, formatStatus(newVal))
						);
						return 1;
					})
			);
	}

	private static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, String commandName) {
		dispatcher.register(
			ClientCommands.literal(commandName)
				.executes(context -> {
					openGui(context.getSource());
					return 1;
				})
				.then(ClientCommands.literal("gui").executes(context -> { openGui(context.getSource()); return 1; }))
				.then(ClientCommands.literal("config").executes(context -> { openGui(context.getSource()); return 1; }))
				.then(
					ClientCommands.literal("toggle")
						.executes(context -> {
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
							context.getSource().sendFeedback(feedback);
							return 1;
						})
				)
				.then(
					ClientCommands.literal("enable")
						.executes(context -> {
							CustomizableHealthConfig config = CustomizableHealthConfig.get();
							config.enabled = true;
							CustomizableHealthConfig.save();
							context.getSource().sendFeedback(
								Component.translatable("customizable-mobs-health-indicator.feedback.enabled", config.preset.getDisplayName())
									.withStyle(ChatFormatting.GREEN)
							);
							return 1;
						})
				)
				.then(
					ClientCommands.literal("disable")
						.executes(context -> {
							CustomizableHealthConfig config = CustomizableHealthConfig.get();
							config.enabled = false;
							CustomizableHealthConfig.save();
							context.getSource().sendFeedback(
								Component.translatable("customizable-mobs-health-indicator.feedback.disabled")
									.withStyle(ChatFormatting.RED)
							);
							return 1;
						})
				)
				.then(
					ClientCommands.literal("preset")
						.then(
							ClientCommands.argument("preset", StringArgumentType.word())
								.suggests((context, builder) -> {
									for (HealthPreset p : HealthPreset.values()) {
										builder.suggest(p.name().toLowerCase(Locale.ROOT));
									}
									return builder.buildFuture();
								})
								.executes(context -> {
									String raw = StringArgumentType.getString(context, "preset");
									try {
										HealthPreset selected = HealthPreset.valueOf(raw.toUpperCase(Locale.ROOT));
										CustomizableHealthConfig config = CustomizableHealthConfig.get();
										config.preset = selected;
										CustomizableHealthConfig.save();

										context.getSource().sendFeedback(
											Component.translatable("customizable-mobs-health-indicator.feedback.preset_changed", selected.getDisplayName())
												.withStyle(ChatFormatting.GREEN)
										);
										return 1;
									} catch (IllegalArgumentException e) {
										context.getSource().sendError(
											Component.translatable("customizable-mobs-health-indicator.command.invalid_preset", raw)
												.withStyle(ChatFormatting.RED)
										);
										return 0;
									}
								})
						)
				)
				.then(
					ClientCommands.literal("position")
						.then(
							ClientCommands.argument("position", StringArgumentType.word())
								.suggests((context, builder) -> {
									builder.suggest("above");
									builder.suggest("below");
									return builder.buildFuture();
								})
								.executes(context -> {
									String raw = StringArgumentType.getString(context, "position").toLowerCase(Locale.ROOT);
									IndicatorPosition selected = "below".equals(raw) ? IndicatorPosition.BELOW_NAME : IndicatorPosition.ABOVE_NAME;
									CustomizableHealthConfig config = CustomizableHealthConfig.get();
									config.position = selected;
									CustomizableHealthConfig.save();
									context.getSource().sendFeedback(
										Component.translatable("customizable-mobs-health-indicator.command.position_changed", selected.getDisplayName())
											.withStyle(ChatFormatting.GREEN)
									);
									return 1;
								})
						)
				)
				.then(
					ClientCommands.literal("texture")
						.then(
							ClientCommands.argument("texture", StringArgumentType.word())
								.suggests((context, builder) -> {
									for (HeartTextureStyle s : HeartTextureStyle.values()) {
										builder.suggest(s.name().toLowerCase(Locale.ROOT));
									}
									return builder.buildFuture();
								})
								.executes(context -> {
									String raw = StringArgumentType.getString(context, "texture");
									try {
										HeartTextureStyle selected = HeartTextureStyle.valueOf(raw.toUpperCase(Locale.ROOT));
										CustomizableHealthConfig config = CustomizableHealthConfig.get();
										config.heartTexture = selected;
										CustomizableHealthConfig.save();
										context.getSource().sendFeedback(
											Component.translatable("customizable-mobs-health-indicator.command.texture_changed", selected.getDisplayName())
												.withStyle(ChatFormatting.GREEN)
										);
										return 1;
									} catch (IllegalArgumentException e) {
										context.getSource().sendError(
											Component.translatable("customizable-mobs-health-indicator.command.invalid_texture", raw)
												.withStyle(ChatFormatting.RED)
										);
										return 0;
									}
								})
						)
				)
				.then(
					ClientCommands.literal("distance")
						.then(
							ClientCommands.argument("meters", IntegerArgumentType.integer(8, 64))
								.executes(context -> {
									int dist = IntegerArgumentType.getInteger(context, "meters");
									CustomizableHealthConfig config = CustomizableHealthConfig.get();
									config.maxDistance = dist;
									CustomizableHealthConfig.save();
									context.getSource().sendFeedback(
										Component.translatable("customizable-mobs-health-indicator.command.distance_changed", dist)
											.withStyle(ChatFormatting.GREEN)
									);
									return 1;
								})
						)
				)
				.then(createToggleSubcommand("absorption", "customizable-mobs-health-indicator.command.absorption_status",
					config -> config.showAbsorption = !config.showAbsorption,
					(config, val) -> config.showAbsorption = val,
					config -> config.showAbsorption))
				.then(createToggleSubcommand("hostile", "customizable-mobs-health-indicator.command.hostile_status",
					config -> config.showHostile = !config.showHostile,
					(config, val) -> config.showHostile = val,
					config -> config.showHostile))
				.then(createToggleSubcommand("passive", "customizable-mobs-health-indicator.command.passive_status",
					config -> config.showPassive = !config.showPassive,
					(config, val) -> config.showPassive = val,
					config -> config.showPassive))
				.then(createToggleSubcommand("bosses", "customizable-mobs-health-indicator.command.bosses_status",
					config -> config.showBosses = !config.showBosses,
					(config, val) -> config.showBosses = val,
					config -> config.showBosses))
				.then(createToggleSubcommand("namedonly", "customizable-mobs-health-indicator.command.named_only_status",
					config -> config.showNamedOnly = !config.showNamedOnly,
					(config, val) -> config.showNamedOnly = val,
					config -> config.showNamedOnly))
				.then(createToggleSubcommand("hidefullhealth", "customizable-mobs-health-indicator.command.hide_full_health_status",
					config -> config.hideFullHealth = !config.hideFullHealth,
					(config, val) -> config.hideFullHealth = val,
					config -> config.hideFullHealth))
				.then(createToggleSubcommand("invisible", "customizable-mobs-health-indicator.command.invisible_status",
					config -> config.hideInvisible = !config.hideInvisible,
					(config, val) -> config.hideInvisible = val,
					config -> config.hideInvisible))
				.then(createToggleSubcommand("colorgrading", "customizable-mobs-health-indicator.command.color_grading_status",
					config -> config.colorGrading = !config.colorGrading,
					(config, val) -> config.colorGrading = val,
					config -> config.colorGrading))
				.then(
					ClientCommands.literal("status")
						.executes(context -> {
							sendStatus(context.getSource());
							return 1;
						})
				)
				.then(
					ClientCommands.literal("reset")
						.executes(context -> {
							CustomizableHealthConfig config = CustomizableHealthConfig.get();
							config.enabled = true;
							config.preset = HealthPreset.HEARTS;
							config.position = IndicatorPosition.ABOVE_NAME;
							config.heartTexture = HeartTextureStyle.VANILLA;
							config.showAbsorption = true;
							config.showHostile = true;
							config.showPassive = true;
							config.showBosses = true;
							config.showNamedOnly = false;
							config.hideFullHealth = false;
							config.hideInvisible = true;
							config.maxDistance = 32.0f;
							config.colorGrading = true;
							CustomizableHealthConfig.save();

							context.getSource().sendFeedback(
								Component.translatable("customizable-mobs-health-indicator.command.reset_success")
									.withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
							);
							return 1;
						})
				)
				.then(
					ClientCommands.literal("help")
						.executes(context -> {
							sendHelp(context.getSource(), commandName);
							return 1;
						})
				)
		);
	}

	private static void sendStatus(FabricClientCommandSource source) {
		CustomizableHealthConfig config = CustomizableHealthConfig.get();
		source.sendFeedback(Component.translatable("customizable-mobs-health-indicator.command.status_title").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
		source.sendFeedback(Component.literal(" - Status: ").withStyle(ChatFormatting.GRAY).append(formatStatus(config.enabled)));
		source.sendFeedback(Component.literal(" - Preset: ").withStyle(ChatFormatting.GRAY).append(config.preset.getDisplayName().copy().withStyle(ChatFormatting.YELLOW)));
		source.sendFeedback(Component.literal(" - Position: ").withStyle(ChatFormatting.GRAY).append(config.position.getDisplayName().copy().withStyle(ChatFormatting.YELLOW)));
		source.sendFeedback(Component.literal(" - Texture: ").withStyle(ChatFormatting.GRAY).append(config.heartTexture.getDisplayName().copy().withStyle(ChatFormatting.YELLOW)));
		source.sendFeedback(Component.literal(" - Distance: ").withStyle(ChatFormatting.GRAY).append(Component.literal(Math.round(config.maxDistance) + "m").withStyle(ChatFormatting.YELLOW)));
		source.sendFeedback(Component.literal(" - Absorption: ").withStyle(ChatFormatting.GRAY).append(formatStatus(config.showAbsorption)));
		source.sendFeedback(Component.literal(" - Hostile Mobs: ").withStyle(ChatFormatting.GRAY).append(formatStatus(config.showHostile)));
		source.sendFeedback(Component.literal(" - Passive Mobs: ").withStyle(ChatFormatting.GRAY).append(formatStatus(config.showPassive)));
		source.sendFeedback(Component.literal(" - Boss Mobs: ").withStyle(ChatFormatting.GRAY).append(formatStatus(config.showBosses)));
		source.sendFeedback(Component.literal(" - Named Only: ").withStyle(ChatFormatting.GRAY).append(formatStatus(config.showNamedOnly)));
		source.sendFeedback(Component.literal(" - Hide Full HP: ").withStyle(ChatFormatting.GRAY).append(formatStatus(config.hideFullHealth)));
		source.sendFeedback(Component.literal(" - Hide Invisible: ").withStyle(ChatFormatting.GRAY).append(formatStatus(config.hideInvisible)));
		source.sendFeedback(Component.literal(" - Color Grading: ").withStyle(ChatFormatting.GRAY).append(formatStatus(config.colorGrading)));
	}

	private static void sendHelp(FabricClientCommandSource source, String commandName) {
		source.sendFeedback(Component.translatable("customizable-mobs-health-indicator.command.help_header").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
		addHelpLine(source, "/" + commandName + " [gui|config]", "Open settings screen");
		addHelpLine(source, "/" + commandName + " toggle", "Quick toggle indicators ON / OFF");
		addHelpLine(source, "/" + commandName + " enable", "Turn indicators ON");
		addHelpLine(source, "/" + commandName + " disable", "Turn indicators OFF");
		addHelpLine(source, "/" + commandName + " preset <name>", "Switch preset (hearts, compact, bar, numeric, lifesteal)");
		addHelpLine(source, "/" + commandName + " position <above|below>", "Set position above or below nametag");
		addHelpLine(source, "/" + commandName + " texture <vanilla|hardcore|unicode>", "Set heart texture style");
		addHelpLine(source, "/" + commandName + " distance <8-64>", "Set max render distance in blocks");
		addHelpLine(source, "/" + commandName + " absorption [on|off]", "Toggle golden apple absorption");
		addHelpLine(source, "/" + commandName + " hostile [on|off]", "Toggle hostile mobs visibility");
		addHelpLine(source, "/" + commandName + " passive [on|off]", "Toggle passive mobs visibility");
		addHelpLine(source, "/" + commandName + " bosses [on|off]", "Toggle boss mobs visibility");
		addHelpLine(source, "/" + commandName + " namedonly [on|off]", "Toggle named mobs only");
		addHelpLine(source, "/" + commandName + " hidefullhealth [on|off]", "Toggle hide at 100% health");
		addHelpLine(source, "/" + commandName + " invisible [on|off]", "Toggle hide invisible mobs");
		addHelpLine(source, "/" + commandName + " colorgrading [on|off]", "Toggle health color grading");
		addHelpLine(source, "/" + commandName + " status", "View active configuration breakdown");
		addHelpLine(source, "/" + commandName + " reset", "Reset all settings to default");
	}

	private static void addHelpLine(FabricClientCommandSource source, String usage, String description) {
		source.sendFeedback(
			Component.literal(usage).withStyle(ChatFormatting.YELLOW)
				.append(Component.literal(" - ").withStyle(ChatFormatting.DARK_GRAY))
				.append(Component.literal(description).withStyle(ChatFormatting.WHITE))
		);
	}
}
