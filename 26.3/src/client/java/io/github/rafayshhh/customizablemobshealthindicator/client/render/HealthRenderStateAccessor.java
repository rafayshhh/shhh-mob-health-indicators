package io.github.rafayshhh.customizablemobshealthindicator.client.render;

import io.github.rafayshhh.customizablemobshealthindicator.client.health.HealthInfo;
import net.minecraft.network.chat.Component;

/**
 * Duck-typing interface injected onto LivingEntityRenderState.
 * Kept outside of the mixin package to comply with SpongePowered Mixin classloader restrictions.
 */
public interface HealthRenderStateAccessor {
	HealthInfo cmhi$getHealthInfo();
	void cmhi$setHealthInfo(HealthInfo info);
	Component cmhi$getHealthComponent();
	void cmhi$setHealthComponent(Component component);
}
