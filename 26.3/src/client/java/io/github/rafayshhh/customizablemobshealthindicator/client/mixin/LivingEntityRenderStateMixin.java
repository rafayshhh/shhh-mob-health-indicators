package io.github.rafayshhh.customizablemobshealthindicator.client.mixin;

import io.github.rafayshhh.customizablemobshealthindicator.client.health.HealthInfo;
import io.github.rafayshhh.customizablemobshealthindicator.client.render.HealthRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements HealthRenderStateAccessor {
	@Unique
	private HealthInfo cmhi$healthInfo;
	@Unique
	private Component cmhi$healthComponent;

	@Override
	public HealthInfo cmhi$getHealthInfo() {
		return this.cmhi$healthInfo;
	}

	@Override
	public void cmhi$setHealthInfo(HealthInfo info) {
		this.cmhi$healthInfo = info;
	}

	@Override
	public Component cmhi$getHealthComponent() {
		return this.cmhi$healthComponent;
	}

	@Override
	public void cmhi$setHealthComponent(Component component) {
		this.cmhi$healthComponent = component;
	}
}
