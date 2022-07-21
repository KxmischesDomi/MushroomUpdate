package de.kxmischesdomi.mushroom.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class FollowMobGoal extends Goal {

	public static final int HORIZONTAL_SCAN_RANGE = 8;
	public static final int VERTICAL_SCAN_RANGE = 4;
	public static final int DONT_FOLLOW_IF_CLOSER_THAN = 5;
	public static final int DONT_FOLLOW_IF_FURTHER_THAN = 10;

	private final PathfinderMob mob;
	private final Class<? extends LivingEntity> toFollow;
	private final double speedModifier;

	@Nullable
	private LivingEntity following;
	private int timeToRecalcPath;

	public FollowMobGoal(PathfinderMob mob, Class<? extends LivingEntity> toFollow, double speedModifier) {
		this.mob = mob;
		this.toFollow = toFollow;
		this.speedModifier = speedModifier;
	}

	@Override
	public boolean canUse() {
		List<? extends LivingEntity> list = mob.level.getEntitiesOfClass(toFollow, mob.getBoundingBox().inflate(HORIZONTAL_SCAN_RANGE, VERTICAL_SCAN_RANGE, HORIZONTAL_SCAN_RANGE));

		LivingEntity entity = null;
		double d = Double.MAX_VALUE;
		for (LivingEntity entity2 : list) {
			double e;
			if ((e = this.mob.distanceToSqr(entity2)) > d) continue;
			d = e;
			entity = entity2;
		}

		if (entity == null || d < Math.sqrt(DONT_FOLLOW_IF_CLOSER_THAN)) {
			return false;
		}

		this.following = entity;
		return true;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.following == null || !this.following.isAlive()) {
			return false;
		}
		double d = this.mob.distanceToSqr(this.following);
		return !(d < Math.sqrt(DONT_FOLLOW_IF_CLOSER_THAN)) && !(d > Math.sqrt(DONT_FOLLOW_IF_FURTHER_THAN));
	}

	@Override
	public void start() {
		this.timeToRecalcPath = 0;
	}

	@Override
	public void stop() {
		this.mob.getNavigation().stop();
		this.following = null;
	}

	@Override
	public void tick() {
		if (--this.timeToRecalcPath > 0 || this.following == null) {
			return;
		}
		this.timeToRecalcPath = this.adjustedTickDelay(10);
		this.mob.getNavigation().moveTo(this.following, this.speedModifier);
	}

}
