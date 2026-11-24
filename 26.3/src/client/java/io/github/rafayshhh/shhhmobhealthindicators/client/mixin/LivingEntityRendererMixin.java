package io.github.rafayshhh.shhhmobhealthindicators.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.rafayshhh.shhhmobhealthindicators.client.config.ShhhHealthConfig;
import io.github.rafayshhh.shhhmobhealthindicators.client.health.HealthInfo;
import io.github.rafayshhh.shhhmobhealthindicators.client.health.HealthTracker;
import io.github.rafayshhh.shhhmobhealthindicators.client.render.HealthRenderStateAccessor;
import io.github.rafayshhh.shhhmobhealthindicators.client.render.HealthRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin injecting into LivingEntityRenderer for Minecraft 26.3's RenderState pipeline.
 * Extracts mob health data and submits custom overhead health indicators.
 * Strictly ignores all players and avatars.
 */
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
		at = @At("TAIL"),
		require = 0
	)
	private void onExtractRenderState(LivingEntity entity, LivingEntityRenderState state, float tickDelta, CallbackInfo ci) {
		// Strictly ignore players and avatars
		if (entity instanceof Player || entity instanceof Avatar || state instanceof AvatarRenderState) {
			((HealthRenderStateAccessor) state).shhh$setHealthInfo(null);
			return;
		}

		if (ShhhHealthConfig.get().enabled) {
			HealthInfo info = HealthTracker.extract(entity);
			((HealthRenderStateAccessor) state).shhh$setHealthInfo(info);

			// Ensure nameTagAttachment is calculated for mobs without custom names
			if (state.nameTagAttachment == null) {
				Vec3 attach = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getYRot(tickDelta));
				if (attach != null) {
					state.nameTagAttachment = attach;
				} else {
					state.nameTagAttachment = new Vec3(0.0, state.boundingBoxHeight + 0.5, 0.0);
				}
			}
		} else {
			((HealthRenderStateAccessor) state).shhh$setHealthInfo(null);
		}
	}

	@Inject(
		method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
		at = @At("TAIL"),
		require = 0
	)
	private void onSubmit(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
		HealthRenderer.render(state, poseStack, submitNodeCollector, cameraRenderState);
	}
}
