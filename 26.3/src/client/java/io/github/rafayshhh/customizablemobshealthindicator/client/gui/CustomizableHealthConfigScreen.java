package io.github.rafayshhh.customizablemobshealthindicator.client.gui;

import io.github.rafayshhh.customizablemobshealthindicator.client.config.CustomizableHealthConfig;
import io.github.rafayshhh.customizablemobshealthindicator.client.config.IndicatorPosition;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * In-game configuration screen for Customizable Mobs Health Indicator.
 * Accessible via Mod Menu, custom keybinds, or /mobhealth commands.
 */
public class CustomizableHealthConfigScreen extends Screen {
	private final Screen parent;
	private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

	public CustomizableHealthConfigScreen(Screen parent) {
		super(Component.translatable("customizable-mobs-health-indicator.config.title"));
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
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.enabled = !config.enabled;
				button.setMessage(getEnableMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.enabled")))
		.width(310)
		.build();
		content.addChild(enableButton);

		// Row 2: Preset Selector | Position Selector
		LinearLayout row2 = LinearLayout.horizontal().spacing(8);
		Button presetButton = Button.builder(
			getPresetMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.preset = config.preset.next();
				button.setMessage(getPresetMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.preset")))
		.width(151)
		.build();
		row2.addChild(presetButton);

		Button positionButton = Button.builder(
			getPositionMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.position = config.position.next();
				button.setMessage(getPositionMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.position")))
		.width(151)
		.build();
		row2.addChild(positionButton);
		content.addChild(row2);

		// Row 3: Heart Texture Style | Absorption Hearts
		LinearLayout row3 = LinearLayout.horizontal().spacing(8);
		Button textureButton = Button.builder(
			getTextureMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.heartTexture = config.heartTexture.next();
				button.setMessage(getTextureMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.heart_texture")))
		.width(151)
		.build();
		row3.addChild(textureButton);

		Button absorptionButton = Button.builder(
			getAbsorptionMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.showAbsorption = !config.showAbsorption;
				button.setMessage(getAbsorptionMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.show_absorption")))
		.width(151)
		.build();
		row3.addChild(absorptionButton);
		content.addChild(row3);

		// Row 4: Hostile Mobs | Passive Mobs
		LinearLayout row4 = LinearLayout.horizontal().spacing(8);
		Button hostileButton = Button.builder(
			getHostileMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.showHostile = !config.showHostile;
				button.setMessage(getHostileMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.show_hostile")))
		.width(151)
		.build();
		row4.addChild(hostileButton);

		Button passiveButton = Button.builder(
			getPassiveMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.showPassive = !config.showPassive;
				button.setMessage(getPassiveMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.show_passive")))
		.width(151)
		.build();
		row4.addChild(passiveButton);
		content.addChild(row4);

		// Row 5: Boss Mobs | Named Mobs Only
		LinearLayout row5 = LinearLayout.horizontal().spacing(8);
		Button bossButton = Button.builder(
			getBossMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.showBosses = !config.showBosses;
				button.setMessage(getBossMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.show_bosses")))
		.width(151)
		.build();
		row5.addChild(bossButton);

		Button namedOnlyButton = Button.builder(
			getNamedOnlyMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.showNamedOnly = !config.showNamedOnly;
				button.setMessage(getNamedOnlyMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.show_named_only")))
		.width(151)
		.build();
		row5.addChild(namedOnlyButton);
		content.addChild(row5);

		// Row 6: Hide Full Health | Hide Invisible
		LinearLayout row6 = LinearLayout.horizontal().spacing(8);
		Button hideFullHealthButton = Button.builder(
			getHideFullHealthMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.hideFullHealth = !config.hideFullHealth;
				button.setMessage(getHideFullHealthMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.hide_full_health")))
		.width(151)
		.build();
		row6.addChild(hideFullHealthButton);

		Button hideInvisibleButton = Button.builder(
			getHideInvisibleMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.hideInvisible = !config.hideInvisible;
				button.setMessage(getHideInvisibleMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.hide_invisible")))
		.width(151)
		.build();
		row6.addChild(hideInvisibleButton);
		content.addChild(row6);

		// Row 7: Max Distance Slider (Full Width)
		double initialDist = (CustomizableHealthConfig.get().maxDistance - 8.0) / (64.0 - 8.0);
		AbstractSliderButton distanceSlider = new AbstractSliderButton(0, 0, 310, 20, CommonComponents.EMPTY, initialDist) {
			@Override
			protected void updateMessage() {
				int dist = Math.round(CustomizableHealthConfig.get().maxDistance);
				setMessage(Component.translatable("customizable-mobs-health-indicator.config.distance", dist));
			}

			@Override
			protected void applyValue() {
				float dist = 8.0f + (float) this.value * (64.0f - 8.0f);
				CustomizableHealthConfig.get().maxDistance = Math.round(dist);
				updateMessage();
			}
		};
		distanceSlider.setMessage(Component.translatable("customizable-mobs-health-indicator.config.distance", Math.round(CustomizableHealthConfig.get().maxDistance)));
		distanceSlider.setTooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.distance")));
		content.addChild(distanceSlider);

		// Row 8: Dynamic Color Grading (Full Width)
		Button colorGradingButton = Button.builder(
			getColorGradingMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.colorGrading = !config.colorGrading;
				button.setMessage(getColorGradingMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.color_grading")))
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
		CustomizableHealthConfig.save();
		if (this.minecraft != null) {
			this.minecraft.gui.setScreen(this.parent);
		}
	}

	private Component getEnableMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("customizable-mobs-health-indicator.config.enabled"),
			CustomizableHealthConfig.get().enabled
		);
	}

	private Component getPresetMessage() {
		return Component.translatable("customizable-mobs-health-indicator.config.preset")
			.append(": ")
			.append(CustomizableHealthConfig.get().preset.getDisplayName());
	}

	private Component getPositionMessage() {
		return Component.translatable("customizable-mobs-health-indicator.config.position")
			.append(": ")
			.append(CustomizableHealthConfig.get().position.getDisplayName());
	}

	private Component getTextureMessage() {
		return Component.translatable("customizable-mobs-health-indicator.config.heart_texture")
			.append(": ")
			.append(CustomizableHealthConfig.get().heartTexture.getDisplayName());
	}

	private Component getAbsorptionMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("customizable-mobs-health-indicator.config.show_absorption"),
			CustomizableHealthConfig.get().showAbsorption
		);
	}

	private Component getHostileMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("customizable-mobs-health-indicator.config.show_hostile"),
			CustomizableHealthConfig.get().showHostile
		);
	}

	private Component getPassiveMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("customizable-mobs-health-indicator.config.show_passive"),
			CustomizableHealthConfig.get().showPassive
		);
	}

	private Component getBossMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("customizable-mobs-health-indicator.config.show_bosses"),
			CustomizableHealthConfig.get().showBosses
		);
	}

	private Component getNamedOnlyMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("customizable-mobs-health-indicator.config.show_named_only"),
			CustomizableHealthConfig.get().showNamedOnly
		);
	}

	private Component getHideFullHealthMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("customizable-mobs-health-indicator.config.hide_full_health"),
			CustomizableHealthConfig.get().hideFullHealth
		);
	}

	private Component getHideInvisibleMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("customizable-mobs-health-indicator.config.hide_invisible"),
			CustomizableHealthConfig.get().hideInvisible
		);
	}

	private Component getColorGradingMessage() {
		return CommonComponents.optionStatus(
			Component.translatable("customizable-mobs-health-indicator.config.color_grading"),
			CustomizableHealthConfig.get().colorGrading
		);
	}
}
