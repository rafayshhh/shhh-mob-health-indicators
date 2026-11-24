package io.github.rafayshhh.shhhmobhealthindicators.client.health;

import io.github.rafayshhh.shhhmobhealthindicators.client.config.ShhhHealthConfig;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Health resolution engine for mobs and non-player living entities.
 * Strictly excludes all players, avatars, and armor stands.
 */
public class HealthTracker {
	// Thread-safe cache storing highest observed max health per mob UUID
	private static final Map<UUID, Float> MAX_HEALTH_CACHE = new ConcurrentHashMap<>();

	public static HealthInfo extract(LivingEntity entity) {
		if (entity == null) {
			return null;
		}

		// Strictly reject any players or avatars - this mod is mob-only!
		if (entity instanceof Player || entity instanceof Avatar) {
			return null;
		}

		// Exclude ArmorStands
		if (entity instanceof ArmorStand) {
			return null;
		}

		ShhhHealthConfig config = ShhhHealthConfig.get();
		if (!config.enabled) {
			return null;
		}

		// Filter: Named only
		if (config.showNamedOnly && !entity.hasCustomName()) {
			return null;
		}

		// Filter: Invisible mobs
		if (config.hideInvisible && entity.isInvisible()) {
			return null;
		}

		// Classify mob type
		boolean isBoss = isBossEntity(entity);
		boolean isHostile = entity instanceof Enemy;
		boolean isPassive = !isHostile && !isBoss;

		// Category toggles
		if (isBoss && !config.showBosses) {
			return null;
		}
		if (isHostile && !isBoss && !config.showHostile) {
			return null;
		}
		if (isPassive && !config.showPassive) {
			return null;
		}

		UUID uuid = entity.getUUID();
		if (entity.isDeadOrDying()) {
			MAX_HEALTH_CACHE.remove(uuid);
		}

		float health = entity.getHealth();
		float maxHealth = entity.getMaxHealth();
		float absorption = entity.getAbsorptionAmount();

		// Filter: Hide full health
		if (config.hideFullHealth && health >= maxHealth && absorption <= 0.0f) {
			return null;
		}

		float effectiveMax = updateMaxHealth(uuid, health, maxHealth);
		return HealthInfo.of(health, effectiveMax, absorption);
	}

	public static boolean isBossEntity(LivingEntity entity) {
		return entity instanceof WitherBoss
			|| entity instanceof EnderDragon
			|| entity instanceof Warden
			|| entity instanceof ElderGuardian;
	}

	private static float updateMaxHealth(UUID uuid, float health, float maxHealth) {
		float observedHighest = Math.max(maxHealth, health);
		if (MAX_HEALTH_CACHE.size() > 1000) {
			MAX_HEALTH_CACHE.clear();
		}
		Float cached = MAX_HEALTH_CACHE.compute(uuid, (k, prev) -> prev == null ? observedHighest : Math.max(prev, observedHighest));
		return Math.max(maxHealth, cached != null ? cached : observedHighest);
	}
}
