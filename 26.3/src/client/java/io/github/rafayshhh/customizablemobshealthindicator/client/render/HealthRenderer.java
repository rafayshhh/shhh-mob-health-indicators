package io.github.rafayshhh.customizablemobshealthindicator.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.rafayshhh.customizablemobshealthindicator.client.config.CustomizableHealthConfig;
import io.github.rafayshhh.customizablemobshealthindicator.client.config.IndicatorPosition;
import io.github.rafayshhh.customizablemobshealthindicator.client.health.HealthComponentBuilder;
import io.github.rafayshhh.customizablemobshealthindicator.client.health.HealthInfo;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Renders the mob health indicator in 3D world space using native billboarded submitNameTag.
 */
public class HealthRenderer {
	// Standard line height translation multiplier in Minecraft nameplate rendering
	private static final float LINE_HEIGHT_OFFSET = 9.0f * 1.15f * 0.025f;

	public static void render(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		// Double guard: strictly reject any avatar / player render states
		if (state instanceof AvatarRenderState) {
			return;
		}

		CustomizableHealthConfig config = CustomizableHealthConfig.get();
		if (!config.enabled) {
			return;
		}

		// Distance cutoff check
		float maxDist = config.maxDistance;
		if (state.distanceToCameraSq > (maxDist * maxDist)) {
			return;
		}

		// Retrieve extracted health
		if (!(state instanceof HealthRenderStateAccessor accessor)) {
			return;
		}
		HealthInfo info = accessor.cmhi$getHealthInfo();
		if (info == null) {
			return;
		}

		List<Component> rows = HealthComponentBuilder.buildRows(info, config.preset, config);
		if (rows.isEmpty()) {
			return;
		}

		Vec3 attachment = state.nameTagAttachment != null 
			? state.nameTagAttachment 
			: new Vec3(0.0, state.boundingBoxHeight + 0.5, 0.0);

		// Calculate vertical offset relative to name tag if present
		float extraY = 0.0f;
		if (state.nameTag != null || state.scoreText != null) {
			if (config.position == IndicatorPosition.ABOVE_NAME) {
				extraY = (state.scoreText != null && state.nameTag != null ? 2 : 1) * LINE_HEIGHT_OFFSET;
			} else {
				extraY = -LINE_HEIGHT_OFFSET;
			}
		}

		for (int i = 0; i < rows.size(); i++) {
			Component rowText = rows.get(i);
			if (rowText.getString().isEmpty()) {
				continue;
			}

			poseStack.pushPose();

			float rowY = extraY + (config.position == IndicatorPosition.ABOVE_NAME ? i : -i) * LINE_HEIGHT_OFFSET;
			if (rowY != 0.0f) {
				poseStack.translate(0.0f, rowY, 0.0f);
			}

			submitNodeCollector.submitNameTag(
				poseStack,
				attachment,
				0, // yOffset
				rowText,
				!state.isDiscrete,
				state.lightCoords,
				cameraRenderState
			);

			poseStack.popPose();
		}
	}
}
