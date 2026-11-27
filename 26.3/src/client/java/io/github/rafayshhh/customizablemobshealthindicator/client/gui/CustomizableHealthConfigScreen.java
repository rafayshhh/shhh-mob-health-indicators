package io.github.rafayshhh.customizablemobshealthindicator.client.gui;

import io.github.rafayshhh.customizablemobshealthindicator.client.config.CustomizableHealthConfig;
import io.github.rafayshhh.customizablemobshealthindicator.client.config.HealthPreset;
import io.github.rafayshhh.customizablemobshealthindicator.client.config.HeartTextureStyle;
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
 * Balanced 2-column responsive layout preventing screen overflow on all GUI scales.
 */
public class CustomizableHealthConfigScreen extends Screen {
	private static final int BUTTON_WIDTH = 150;
	private static final int BUTTON_HEIGHT = 20;

	private final Screen parent;
	private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

	public CustomizableHealthConfigScreen(Screen parent) {
		super(Component.translatable("customizable-mobs-health-indicator.config.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		this.layout.addTitleHeader(this.title, this.font);

		LinearLayout content = LinearLayout.horizontal().spacing(8);
		LinearLayout leftCol = LinearLayout.vertical().spacing(4);
		LinearLayout rightCol = LinearLayout.vertical().spacing(4);

		// --- Left Column (7 Options) ---

		// 1. Master Enable Toggle
		Button enableButton = Button.builder(
			getEnableMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.enabled = !config.enabled;
				CustomizableHealthConfig.save();
				button.setMessage(getEnableMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.enabled")))
		.width(BUTTON_WIDTH)
		.build();
		leftCol.addChild(enableButton);

		// 2. Preset Selector
		Button presetButton = Button.builder(
			getPresetMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.preset = config.preset.next();
				CustomizableHealthConfig.save();
				button.setMessage(getPresetMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.preset")))
		.width(BUTTON_WIDTH)
		.build();
		leftCol.addChild(presetButton);

		// 3. Position Selector
		Button positionButton = Button.builder(
			getPositionMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.position = config.position.next();
				CustomizableHealthConfig.save();
				button.setMessage(getPositionMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.position")))
		.width(BUTTON_WIDTH)
		.build();
		leftCol.addChild(positionButton);

		// 4. Heart Texture Style
		Button textureButton = Button.builder(
			getTextureMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.heartTexture = config.heartTexture.next();
				CustomizableHealthConfig.save();
				button.setMessage(getTextureMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.heart_texture")))
		.width(BUTTON_WIDTH)
		.build();
		leftCol.addChild(textureButton);

		// 5. Show Absorption
		Button absorptionButton = Button.builder(
			getAbsorptionMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.showAbsorption = !config.showAbsorption;
				CustomizableHealthConfig.save();
				button.setMessage(getAbsorptionMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.show_absorption")))
		.width(BUTTON_WIDTH)
		.build();
		leftCol.addChild(absorptionButton);

		// 6. Hide Invisible
		Button hideInvisibleButton = Button.builder(
			getHideInvisibleMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.hideInvisible = !config.hideInvisible;
				CustomizableHealthConfig.save();
				button.setMessage(getHideInvisibleMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.hide_invisible")))
		.width(BUTTON_WIDTH)
		.build();
		leftCol.addChild(hideInvisibleButton);

		// --- Right Column (7 Options) ---

		// 1. Max Distance Slider (8 to 64 blocks)
		double initialDist = (CustomizableHealthConfig.get().maxDistance - 8.0) / (64.0 - 8.0);
		AbstractSliderButton distanceSlider = new AbstractSliderButton(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, CommonComponents.EMPTY, initialDist) {
			@Override
			protected void updateMessage() {
				int dist = Math.round(CustomizableHealthConfig.get().maxDistance);
				setMessage(Component.translatable("customizable-mobs-health-indicator.config.distance", dist));
			}

			@Override
			protected void applyValue() {
				float dist = 8.0f + (float) this.value * (64.0f - 8.0f);
				CustomizableHealthConfig.get().maxDistance = Math.round(dist);
				CustomizableHealthConfig.save();
				updateMessage();
			}
		};
		distanceSlider.setMessage(Component.translatable("customizable-mobs-health-indicator.config.distance", Math.round(CustomizableHealthConfig.get().maxDistance)));
		distanceSlider.setTooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.distance")));
		rightCol.addChild(distanceSlider);

		// 2. Dynamic Color Grading
		Button colorGradingButton = Button.builder(
			getColorGradingMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.colorGrading = !config.colorGrading;
				CustomizableHealthConfig.save();
				button.setMessage(getColorGradingMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.color_grading")))
		.width(BUTTON_WIDTH)
		.build();
		rightCol.addChild(colorGradingButton);

		// 3. Hostile Mobs
		Button hostileButton = Button.builder(
			getHostileMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.showHostile = !config.showHostile;
				CustomizableHealthConfig.save();
				button.setMessage(getHostileMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.show_hostile")))
		.width(BUTTON_WIDTH)
		.build();
		rightCol.addChild(hostileButton);

		// 4. Passive Mobs
		Button passiveButton = Button.builder(
			getPassiveMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.showPassive = !config.showPassive;
				CustomizableHealthConfig.save();
				button.setMessage(getPassiveMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.show_passive")))
		.width(BUTTON_WIDTH)
		.build();
		rightCol.addChild(passiveButton);

		// 5. Boss Mobs
		Button bossButton = Button.builder(
			getBossMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.showBosses = !config.showBosses;
				CustomizableHealthConfig.save();
				button.setMessage(getBossMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.show_bosses")))
		.width(BUTTON_WIDTH)
		.build();
		rightCol.addChild(bossButton);

		// 6. Named Mobs Only
		Button namedOnlyButton = Button.builder(
			getNamedOnlyMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.showNamedOnly = !config.showNamedOnly;
				CustomizableHealthConfig.save();
				button.setMessage(getNamedOnlyMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.show_named_only")))
		.width(BUTTON_WIDTH)
		.build();
		rightCol.addChild(namedOnlyButton);

		// 7. Hide Full Health
		Button hideFullHealthButton = Button.builder(
			getHideFullHealthMessage(),
			button -> {
				CustomizableHealthConfig config = CustomizableHealthConfig.get();
				config.hideFullHealth = !config.hideFullHealth;
				CustomizableHealthConfig.save();
				button.setMessage(getHideFullHealthMessage());
			}
		)
		.tooltip(Tooltip.create(Component.translatable("customizable-mobs-health-indicator.tooltip.hide_full_health")))
		.width(BUTTON_WIDTH)
		.build();
		rightCol.addChild(hideFullHealthButton);

		// Assemble columns into horizontal content
		content.addChild(leftCol);
		content.addChild(rightCol);
		this.layout.addToContents(content);

		// Footer: Standard 200px Done Button
		Button doneButton = Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(200).build();
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
