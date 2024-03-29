package de.kxmischesdomi.mushroom.core;

import de.kxmischesdomi.mushroom.entity.Glowfly;
import de.kxmischesdomi.mushroom.registry.ModCriteriaTriggers;
import de.kxmischesdomi.mushroom.registry.ModEntities;
import de.kxmischesdomi.mushroom.registry.ModStats;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface IGlowfly {

	default double getHealingRange() {
		return 1.5;
	}

	default float getHealthRegenerating() {
		return 3f;
	}

	boolean hasHealingPower();

	int getHealingCooldown();

	void setHealingCooldown(int cooldown);

	Collection<ServerPlayer> getTrackingPlayers();

	default void checkForMobHealing(ServerLevel level, Vec3 glowflyPos) {
		if (!hasHealingPower()) return;
		LivingEntity mob = IGlowfly.getNearestMobToHeal(
				level,
				glowflyPos,
				getHealingRange()
		);
		if (mob != null) {
			glowflyHealMob(glowflyPos, mob);
		}
	}

	default void glowflyHealMob(Vec3 glowflyPos, LivingEntity entity) {
		if (!hasHealingPower()) return;

		Level level = entity.getLevel();

		if (!level.isClientSide) {
			float regenerating = getHealthRegenerating();
			entity.heal(regenerating);

			if (entity instanceof ServerPlayer player) {
				player.awardStat(ModStats.GLOWFLY_HEALTH_GAINED, Math.round(regenerating * 10.0f));
				int value = player.getStats().getValue(Stats.CUSTOM.get(ModStats.GLOWFLY_HEALTH_GAINED));
				if (value > 20 * 10) {
					ModCriteriaTriggers.GLOWFLY_REGENERATE_FULL.trigger(player);
				}
			}

			setHealingCooldown(10*20);

			playHealParticles(
					glowflyPos,
					new Vec3(ModEntities.GLOWFLY.getWidth() / 2, ModEntities.GLOWFLY.getHeight() / 2, ModEntities.GLOWFLY.getWidth() / 2),
					5
			);
			playHealParticles(
					entity.position(),
					new Vec3(entity.getType().getWidth() / 2, entity.getType().getHeight() / 2, entity.getType().getWidth() / 2),
					30
			);
		}

	}

	default void tickCooldownHealing() {
		if (getHealingCooldown() > 0) {
			setHealingCooldown(getHealingCooldown() - 1);
		}
	}

	static LivingEntity getNearestMobToHeal(ServerLevel level, Vec3 pos, double searchRange) {

		List<LivingEntity> entities = level.getEntitiesOfClass(
				LivingEntity.class,
				new AABB(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z).inflate(searchRange),
				(entity) -> {
					if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
						return false;
					}
					return entity.isAlive() && entity.getHealth() < entity.getMaxHealth();
				}
		);

		entities.sort((o1, o2) -> {
			double d1 = o1.distanceToSqr(pos);
			double d2 = o2.distanceToSqr(pos);
			return Double.compare(d1, d2);
		});

		return entities.isEmpty() ? null : entities.get(0);
	}

	default void playHealParticles(Vec3 position, Vec3 box, int amount) {
		for (ServerPlayer player : getTrackingPlayers()) {
			ServerLevel level = player.getLevel();

			level.sendParticles(
					player,
					ParticleTypes.GLOW,
					true,
					position.x(),
					position.y(),
					position.z(),
					amount,
					box.x(),
					box.y(),
					box.z(),
					10
					);

		}
	}

	static void saveDefaultDataToGlassTag(Glowfly mob, ItemStack itemStack) {
		CompoundTag compoundTag = itemStack.getOrCreateTag();

		if (mob.hasCustomName()) {
			itemStack.setHoverName(mob.getCustomName());
		}
		if (mob.isNoAi()) {
			compoundTag.putBoolean("NoAI", mob.isNoAi());
		}
		if (mob.isSilent()) {
			compoundTag.putBoolean("Silent", mob.isSilent());
		}
		if (mob.hasGlowingTag()) {
			compoundTag.putBoolean("Glowing", mob.hasGlowingTag());
		}
		if (mob.isInvulnerable()) {
			compoundTag.putBoolean("Invulnerable", mob.isInvulnerable());
		}
		if (mob.getHealth() != mob.getMaxHealth()) {
			compoundTag.putFloat("Health", mob.getHealth());
		}

		if (mob.getHealingCooldown() > 0) {
			compoundTag.putInt("HealingCooldown", mob.getHealingCooldown());
		}
	}

	static void loadDefaultDataFromGlassTag(Glowfly mob, CompoundTag compoundTag) {
		Bucketable.loadDefaultDataFromBucketTag(mob, compoundTag);
		if (compoundTag.contains("HealingCooldown")) {
			mob.setHealingCooldown(compoundTag.getInt("HealingCooldown"));
		}
	}

}
