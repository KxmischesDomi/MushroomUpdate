package de.kxmischesdomi.mushroom.core;

import de.kxmischesdomi.mushroom.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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

	boolean hasHealingPower();

	int getHealingCooldown();

	void setHealingCooldown(int cooldown);

	void setHasHealingPower(boolean hasHealingPower);

	Collection<ServerPlayer> getTrackingPlayers();

	default void glowflyHealMob(Vec3 glowflyPos, LivingEntity entity) {
		if (!hasHealingPower()) return;

		Level level = entity.getLevel();

		if (!level.isClientSide) {
			entity.heal(1.5f);
			setHasHealingPower(false);
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
		if (getHealingCooldown() == 0 && !hasHealingPower()) {
			setHasHealingPower(true);
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
					return entity.getHealth() < entity.getMaxHealth();
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

}
