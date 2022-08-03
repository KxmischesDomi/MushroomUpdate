package de.kxmischesdomi.mushroom.entity;

import de.kxmischesdomi.mushroom.core.IGlowfly;
import de.kxmischesdomi.mushroom.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import de.kxmischesdomi.mushroom.registry.ModCriteriaTriggers;
import de.kxmischesdomi.mushroom.registry.ModItems;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Collection;
import java.util.EnumSet;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class Glowfly extends PathfinderMob implements IGlowfly, IAnimatable {

	private static final EntityDataAccessor<Boolean> HAS_HEALING_POWER = SynchedEntityData.defineId(Glowfly.class, EntityDataSerializers.BOOLEAN);

	private final AnimationFactory factory = new AnimationFactory(this);

	private int healingCooldown = 0;

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
		this.goalSelector.addGoal(0, new FlyToMobToHealGoal(this));
		this.goalSelector.addGoal(1, new WaterAvoidingRandomFlyingGoal(this, 1, 1));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(HAS_HEALING_POWER, true);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putBoolean("HasHealingPower", this.hasHealingPower());
		compoundTag.putInt("HealingCooldown", this.healingCooldown);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		if (compoundTag.contains("HasHealingPower")) {
			this.setHasHealingPower(compoundTag.getBoolean("HasHealingPower"));
		}
		if (compoundTag.contains("HealingCooldown")) {
			this.healingCooldown = compoundTag.getInt("HealingCooldown");
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (hasHealingPower()) {
			if (random.nextFloat() > 0.95F) {
				this.level.addParticle(ParticleTypes.GLOW, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();
		this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));

		if (level instanceof ServerLevel) {
			tickCooldownHealing();
		}

	}

	@Override
	public boolean hasHealingPower() {
		return this.entityData.get(HAS_HEALING_POWER);
	}

	@Override
	public int getHealingCooldown() {
		return this.healingCooldown;
	}

	@Override
	public void setHealingCooldown(int cooldown) {
		this.healingCooldown = cooldown;
	}

	@Override
	public void setHasHealingPower(boolean hasHealingPower) {
		this.entityData.set(HAS_HEALING_POWER, hasHealingPower);
	}

	@Override
	public Collection<ServerPlayer> getTrackingPlayers() {
		return PlayerLookup.tracking(this);
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
		ItemStack itemInHand = player.getItemInHand(interactionHand);
		if (itemInHand.is(Items.GLASS_BOTTLE) && isAlive()) {
			ItemStack glowflyGlass = new ItemStack(ModItems.GLOWFLY_GLASS);
			Bucketable.saveDefaultDataToBucketTag(this, glowflyGlass);
			ItemStack result = ItemUtils.createFilledResult(itemInHand, player, glowflyGlass, false);
			player.setItemInHand(interactionHand, result);
			Level level = player.level;
			if (!level.isClientSide) {
				ModCriteriaTriggers.FILLED_GLASS.trigger((ServerPlayer)player);
			}
			discard();
			return InteractionResult.sidedSuccess(level.isClientSide());
		}
		return super.mobInteract(player, interactionHand);
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
			if (!isOnGround() && !isNoAi()) {
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

	public static class FlyToMobToHealGoal extends Goal {

		private static final double TRACKING_DISTANCE = 10;

		private final Glowfly glowfly;
		private LivingEntity mobToHeal;

		private int timeToRecalcPath;

		public FlyToMobToHealGoal(Glowfly glowfly) {
			this.glowfly = glowfly;
			setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			if (!glowfly.hasHealingPower()) {
				return false;
			}
			LivingEntity mobToHeal = IGlowfly.getNearestMobToHeal((ServerLevel) glowfly.level, glowfly.position(), TRACKING_DISTANCE);
			if (mobToHeal == null || mobToHeal.distanceTo(glowfly) <= glowfly.getHealingRange()) {
				return false;
			}
			this.mobToHeal = mobToHeal;
			return true;
		}

		@Override
		public boolean canContinueToUse() {
			LivingEntity mobToHeal = IGlowfly.getNearestMobToHeal((ServerLevel) glowfly.level, glowfly.position(), TRACKING_DISTANCE);
			float d = this.mobToHeal.distanceTo(glowfly);
			if (mobToHeal != this.mobToHeal || !glowfly.hasHealingPower() || d > TRACKING_DISTANCE) {
				return false;
			}
			if (d <= glowfly.getHealingRange()) {
				glowfly.glowflyHealMob(glowfly.position(), this.mobToHeal);
				return false;
			}
			return true;
		}

		@Override
		public void start() {
			this.timeToRecalcPath = 0;
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			if (--this.timeToRecalcPath > 0 || this.mobToHeal == null) {
				return;
			}
			this.timeToRecalcPath = this.adjustedTickDelay(10);
			this.glowfly.getNavigation().moveTo(this.mobToHeal.getX(), mobToHeal.getY() + mobToHeal.getType().getHeight() / 2, mobToHeal.getZ(), 1);
		}

	}


}
