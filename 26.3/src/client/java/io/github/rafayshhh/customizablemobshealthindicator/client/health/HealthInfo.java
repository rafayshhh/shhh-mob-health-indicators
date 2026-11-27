package io.github.rafayshhh.customizablemobshealthindicator.client.health;

/**
 * Immutable data container for a mob's extracted health state.
 */
public record HealthInfo(
	float health,
	float maxHealth,
	float absorption,
	boolean hasAbsorption,
	float percentage
) {
	public static HealthInfo of(float health, float maxHealth, float absorption) {
		float safeMax = Math.max(1.0f, Math.max(maxHealth, health));
		float safeHealth = Math.clamp(health, 0.0f, safeMax);
		float safeAbsorption = Math.max(0.0f, absorption);
		float pct = Math.clamp(safeHealth / safeMax, 0.0f, 1.0f);
		return new HealthInfo(safeHealth, safeMax, safeAbsorption, safeAbsorption > 0.0f, pct);
	}
}
