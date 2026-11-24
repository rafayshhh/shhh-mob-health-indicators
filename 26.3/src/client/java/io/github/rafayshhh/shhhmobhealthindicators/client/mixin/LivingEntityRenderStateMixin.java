package io.github.rafayshhh.shhhmobhealthindicators.client.mixin;

import io.github.rafayshhh.shhhmobhealthindicators.client.health.HealthInfo;
import io.github.rafayshhh.shhhmobhealthindicators.client.render.HealthRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements HealthRenderStateAccessor {
	@Unique
	private HealthInfo shhh$healthInfo;

	@Override
	public HealthInfo shhh$getHealthInfo() {
		return this.shhh$healthInfo;
	}

	@Override
	public void shhh$setHealthInfo(HealthInfo info) {
		this.shhh$healthInfo = info;
	}
}
