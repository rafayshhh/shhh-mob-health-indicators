package io.github.rafayshhh.shhhmobhealthindicators.client.render;

import io.github.rafayshhh.shhhmobhealthindicators.client.health.HealthInfo;

/**
 * Duck-typing interface injected onto LivingEntityRenderState.
 * Kept outside of the mixin package to comply with SpongePowered Mixin classloader restrictions.
 */
public interface HealthRenderStateAccessor {
	HealthInfo shhh$getHealthInfo();
	void shhh$setHealthInfo(HealthInfo info);
}
