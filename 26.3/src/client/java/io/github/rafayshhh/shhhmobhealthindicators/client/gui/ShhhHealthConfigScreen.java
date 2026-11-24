package io.github.rafayshhh.shhhmobhealthindicators.client.gui;

import io.github.rafayshhh.shhhmobhealthindicators.client.config.IndicatorPosition;
import io.github.rafayshhh.shhhmobhealthindicators.client.config.ShhhHealthConfig;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * In-game configuration screen for Shhh Mob Health Indicators.
 * Accessible via Mod Menu, custom keybinds, or /mobhealth commands.
 */
public class ShhhHealthConfigScreen extends Screen {
	private final Screen parent;
	private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

	public ShhhHealthConfigScreen(Screen parent) {
		super(Component.translatable("shhh-mob-health-indicators.config.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		this.layout.addTitleHeader(this.title, this.font);

		LinearLayout content = LinearLayout.vertical().spacing(4);

		// 1. Master Enable Toggle (Full Width)
		Button enableButton = Button.builder(
			getEnableMessage(),
			button -> {
				ShhhHealthConfig config = ShhhHealthConfig.get();
				config.enabled = !config.enabled;
				button.setMessage(getEnableMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("shhh-mob-health-indicators.tooltip.enabled")))
		.width(310)
		.build();
		content.addChild(enableButton);

		// Row 2: Preset Selector | Position Selector
		LinearLayout row2 = LinearLayout.horizontal().spacing(8);
		Button presetButton = Button.builder(
			getPresetMessage(),
			button -> {
				ShhhHealthConfig config = ShhhHealthConfig.get();
				config.preset = config.preset.next();
				button.setMessage(getPresetMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("shhh-mob-health-indicators.tooltip.preset")))
		.width(151)
		.build();
		row2.addChild(presetButton);

		Button positionButton = Button.builder(
			getPositionMessage(),
			button -> {
				ShhhHealthConfig config = ShhhHealthConfig.get();
				config.position = config.position.next();
				button.setMessage(getPositionMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("shhh-mob-health-indicators.tooltip.position")))
		.width(151)
		.build();
		row2.addChild(positionButton);
		content.addChild(row2);

		// Row 3: Heart Texture Style | Absorption Hearts
		LinearLayout row3 = LinearLayout.horizontal().spacing(8);
		Button textureButton = Button.builder(
			getTextureMessage(),
			button -> {
				ShhhHealthConfig config = ShhhHealthConfig.get();
				config.heartTexture = config.heartTexture.next();
				button.setMessage(getTextureMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("shhh-mob-health-indicators.tooltip.heart_texture")))
		.width(151)
		.build();
		row3.addChild(textureButton);

		Button absorptionButton = Button.builder(
			getAbsorptionMessage(),
			button -> {
				ShhhHealthConfig config = ShhhHealthConfig.get();
				config.showAbsorption = !config.showAbsorption;
				button.setMessage(getAbsorptionMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("shhh-mob-health-indicators.tooltip.show_absorption")))
		.width(151)
		.build();
		row3.addChild(absorptionButton);
		content.addChild(row3);

		// Row 4: Hostile Mobs | Passive Mobs
		LinearLayout row4 = LinearLayout.horizontal().spacing(8);
		Button hostileButton = Button.builder(
			getHostileMessage(),
			button -> {
				ShhhHealthConfig config = ShhhHealthConfig.get();
				config.showHostile = !config.showHostile;
				button.setMessage(getHostileMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("shhh-mob-health-indicators.tooltip.show_hostile")))
		.width(151)
		.build();
		row4.addChild(hostileButton);

		Button passiveButton = Button.builder(
			getPassiveMessage(),
			button -> {
				ShhhHealthConfig config = ShhhHealthConfig.get();
				config.showPassive = !config.showPassive;
				button.setMessage(getPassiveMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("shhh-mob-health-indicators.tooltip.show_passive")))
		.width(151)
		.build();
		row4.addChild(passiveButton);
		content.addChild(row4);

		// Row 5: Boss Mobs | Named Mobs Only
		LinearLayout row5 = LinearLayout.horizontal().spacing(8);
		Button bossButton = Button.builder(
			getBossMessage(),
			button -> {
				ShhhHealthConfig config = ShhhHealthConfig.get();
				config.showBosses = !config.showBosses;
				button.setMessage(getBossMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("shhh-mob-health-indicators.tooltip.show_bosses")))
		.width(151)
		.build();
		row5.addChild(bossButton);

		Button namedOnlyButton = Button.builder(
			getNamedOnlyMessage(),
			button -> {
				ShhhHealthConfig config = ShhhHealthConfig.get();
				config.showNamedOnly = !config.showNamedOnly;
				button.setMessage(getNamedOnlyMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("shhh-mob-health-indicators.tooltip.show_named_only")))
		.width(151)
		.build();
		row5.addChild(namedOnlyButton);
		content.addChild(row5);

		// Row 6: Hide Full Health | Hide Invisible
		LinearLayout row6 = LinearLayout.horizontal().spacing(8);
		Button hideFullHealthButton = Button.builder(
			getHideFullHealthMessage(),
			button -> {
				ShhhHealthConfig config = ShhhHealthConfig.get();
				config.hideFullHealth = !config.hideFullHealth;
				button.setMessage(getHideFullHealthMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("shhh-mob-health-indicators.tooltip.hide_full_health")))
		.width(151)
		.build();
		row6.addChild(hideFullHealthButton);

		Button hideInvisibleButton = Button.builder(
			getHideInvisibleMessage(),
			button -> {
				ShhhHealthConfig config = ShhhHealthConfig.get();
				config.hideInvisible = !config.hideInvisible;
				button.setMessage(getHideInvisibleMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("shhh-mob-health-indicators.tooltip.hide_invisible")))
		.width(151)
		.build();
		row6.addChild(hideInvisibleButton);
		content.addChild(row6);

		// Row 7: Max Distance Slider (Full Width) - Note: save() deferred to onClose() to avoid disk churn during drag
		double initialDist = (ShhhHealthConfig.get().maxDistance - 8.0) / (64.0 - 8.0);
		AbstractSliderButton distanceSlider = new AbstractSliderButton(0, 0, 310, 20, CommonComponents.EMPTY, initialDist) {
			@Override
			protected void updateMessage() {
				int dist = Math.round(ShhhHealthConfig.get().maxDistance);
				setMessage(Component.translatable("shhh-mob-health-indicators.config.distance", dist));
			}

			@Override
			protected void applyValue() {
				float dist = 8.0f + (float) this.value * (64.0f - 8.0f);
				ShhhHealthConfig.get().maxDistance = Math.round(dist);
				updateMessage();
			}
		};
		distanceSlider.setMessage(Component.translatable("shhh-mob-health-indicators.config.distance", Math.round(ShhhHealthConfig.get().maxDistance)));
		distanceSlider.setTooltip(Tooltip.create(Component.translatable("shhh-mob-health-indicators.tooltip.distance")));
		content.addChild(distanceSlider);

		// Row 8: Dynamic Color Grading (Full Width)
		Button colorGradingButton = Button.builder(
			getColorGradingMessage(),
			button -> {
				ShhhHealthConfig config = ShhhHealthConfig.get();
				config.colorGrading = !config.colorGrading;
				button.setMessage(getColorGradingMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("shhh-mob-health-indicators.tooltip.color_grading")))
		.width(310)
		.build();
		content.addChild(colorGradingButton);

		this.layout.addToContents(content);

		// Done Button in Footer
		Button doneButton = Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(210).build();
		this.layout.addToFooter(doneButton);

		// Register all widgets recursively into screen
		this.layout.visitWidgets(this::addRenderableWidget);

		repositionElements();
	}

	@Override
	protected void repositionElements() {
		this.layout.arrangeElements();
	}

	@Override
	public void onClose() {
		ShhhHealthConfig.save();
		if (this.minecraft != null) {
			this.minecraft.gui.setScreen(this.parent);
		}
	}

	private Component getEnableMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("shhh-mob-health-indicators.config.enabled"),
			ShhhHealthConfig.get().enabled
		);
	}

	private Component getPresetMessage() {
		return Component.translatable("shhh-mob-health-indicators.config.preset")
			.append(": ")
			.append(ShhhHealthConfig.get().preset.getDisplayName());
	}

	private Component getPositionMessage() {
		return Component.translatable("shhh-mob-health-indicators.config.position")
			.append(": ")
			.append(ShhhHealthConfig.get().position.getDisplayName());
	}

	private Component getTextureMessage() {
		return Component.translatable("shhh-mob-health-indicators.config.heart_texture")
			.append(": ")
			.append(ShhhHealthConfig.get().heartTexture.getDisplayName());
	}

	private Component getAbsorptionMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("shhh-mob-health-indicators.config.show_absorption"),
			ShhhHealthConfig.get().showAbsorption
		);
	}

	private Component getHostileMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("shhh-mob-health-indicators.config.show_hostile"),
			ShhhHealthConfig.get().showHostile
		);
	}

	private Component getPassiveMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("shhh-mob-health-indicators.config.show_passive"),
			ShhhHealthConfig.get().showPassive
		);
	}

	private Component getBossMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("shhh-mob-health-indicators.config.show_bosses"),
			ShhhHealthConfig.get().showBosses
		);
	}

	private Component getNamedOnlyMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("shhh-mob-health-indicators.config.show_named_only"),
			ShhhHealthConfig.get().showNamedOnly
		);
	}

	private Component getHideFullHealthMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("shhh-mob-health-indicators.config.hide_full_health"),
			ShhhHealthConfig.get().hideFullHealth
		);
	}

	private Component getHideInvisibleMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("shhh-mob-health-indicators.config.hide_invisible"),
			ShhhHealthConfig.get().hideInvisible
		);
	}

	private Component getColorGradingMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("shhh-mob-health-indicators.config.color_grading"),
			ShhhHealthConfig.get().colorGrading
		);
	}
}
