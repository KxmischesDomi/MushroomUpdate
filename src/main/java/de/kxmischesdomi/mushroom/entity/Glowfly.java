package de.kxmischesdomi.mushroom.entity;

import de.kxmischesdomi.mushroom.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class Glowfly extends PathfinderMob implements IAnimatable {

	private final AnimationFactory factory = new AnimationFactory(this);

	public Glowfly(EntityType<? extends PathfinderMob> entityType, Level level) {
		super(entityType, level);
		this.moveControl = new FlyingMoveControl(this, 10, false);
	}

	/**
	 * Creates the navigation for the glowfly.
	 */
	@Override
	protected PathNavigation createNavigation(Level level) {
		FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level);
		flyingPathNavigation.setCanOpenDoors(false);
		flyingPathNavigation.setCanFloat(true);
		flyingPathNavigation.setCanPassDoors(true);
		return flyingPathNavigation;
	}

	/**
	 * Creates the ai goals for the glowfly.
	 */
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new WaterAvoidingRandomFlyingGoal(this, 1, 1));
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (random.nextFloat() > 0.95F) {
			this.level.addParticle(ParticleTypes.GLOW, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void tick() {
		super.tick();
		this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
	}

	@Override
	public boolean causeFallDamage(float f, float g, DamageSource damageSource) {
		return false;
	}

	@Override
	protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
	}

	@Override
	public boolean isIgnoringBlockTriggers() {
		return true;
	}

	@Override
	protected void pushEntities() {
	}

	@Override
	protected void doPush(Entity entity) {

	}

	@Override
	public float getPickRadius() {
		return super.getPickRadius();
	}

	@Override
	public boolean startRiding(Entity entity, boolean bl) {
		return false;
	}

	@Override
	public boolean startRiding(Entity entity) {
		return false;
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
		return entityDimensions.height / 2.0f;
	}

	@Override
	protected void playStepSound(BlockPos blockPos, BlockState blockState) {
	}

	@Override
	protected float getSoundVolume() {
		return 0.1f;
	}

	@Override
	public float getVoicePitch() {
		return super.getVoicePitch() * 0.95f;
	}

	@Override
	public boolean removeWhenFarAway(double d) {
		return super.removeWhenFarAway(d);
	}

	/**
	 * Registers the animations for the glowfly.
	 */
	@Override
	public void registerControllers(AnimationData animationData) {
		animationData.addAnimationController(new AnimationController(this, "controller", 1, event -> {
			if (!isOnGround()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("fly", true));
				return PlayState.CONTINUE;
			}
			return PlayState.STOP;
		}));
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1).add(Attributes.FLYING_SPEED, 0.4f).add(Attributes.MOVEMENT_SPEED, 0.2);
	}

	/**
	 * @return if the glowfl can naturally spawn with the given parameters.
	 */
	public static boolean checkGlowflySpawnRules(EntityType<Glowfly> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
		return checkMobSpawnRules(entityType, levelAccessor, mobSpawnType, blockPos, randomSource);
	}

	public static boolean checkMobSpawnRules(EntityType<? extends Mob> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
		BlockPos blockPos2 = blockPos.below();
		return mobSpawnType == MobSpawnType.SPAWNER || levelAccessor.getBlockState(blockPos2).isValidSpawn(levelAccessor, blockPos2, entityType);
	}


}
