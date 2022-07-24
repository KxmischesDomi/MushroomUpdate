package de.kxmischesdomi.mushroom.entity;

import de.kxmischesdomi.mushroom.registry.ModSounds;
import de.kxmischesdomi.mushroom.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ShroomPal extends PathfinderMob implements IAnimatable {

	private static final EntityDataAccessor<Boolean> BROWN_MUSHROOM = SynchedEntityData.defineId(ShroomPal.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> BIG = SynchedEntityData.defineId(ShroomPal.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DANCING = SynchedEntityData.defineId(ShroomPal.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> CROPS_EATEN = SynchedEntityData.defineId(ShroomPal.class, EntityDataSerializers.INT);

	private final AnimationFactory factory = new AnimationFactory(this);

	public ShroomPal(EntityType<? extends PathfinderMob> entityType, Level level) {
		super(entityType, level);
	}

	/**
	 * Give natural generated shroom pals a random chance to be brown
	 */
	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
		SpawnGroupData groupData = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
		setBrownMushroom(random.nextBoolean());
		return groupData;
	}

	/**
	 * Creates the ai goals for the shroom pal.
	 */
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
		this.goalSelector.addGoal(2, new TurnCropsIntoBoneMealGoal(this, 1, 3));
		this.goalSelector.addGoal(3, new FollowMobGoal(this, Player.class, 1.25));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.75, 1));
		this.goalSelector.addGoal(5, new DanceGoal(this));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
	}

	/**
	 * Defines the attributes for the shroom pal.
	 */
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(BIG, false);
		entityData.define(BROWN_MUSHROOM, false);
		entityData.define(CROPS_EATEN, 0);
		entityData.define(DANCING, false);
	}

	/**
	 * Writes the shroom pal's data to the nbt.
	 */
	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putBoolean("IsBig", isBig());
		compoundTag.putBoolean("IsBrown", isBrownMushroom());
		compoundTag.putInt("CropsEaten", getCropsEaten());
	}

	/**
	 * Reads the shroom pal's data from the nbt.
	 */
	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		this.setBig(compoundTag.getBoolean("IsBig"));
		this.setBrownMushroom(compoundTag.getBoolean("IsBrown"));
		this.setCropsEaten(compoundTag.getInt("CropsEaten"));
	}

	/**
	 * @return if the shroom pal is big.
	 */
	public boolean isBig() {
		return entityData.get(BIG);
	}

	/**
	 * @param big if the shroom pal should be big.
	 */
	public void setBig(boolean big) {
		entityData.set(BIG, big);
	}

	/**
	 * @return if the shroom pal is brown.
	 */
	public boolean isBrownMushroom() {
		return entityData.get(BROWN_MUSHROOM);
	}

	/**
	 * @param brown if the shroom pal should be brown.
	 */
	public void setBrownMushroom(boolean brown) {
		entityData.set(BROWN_MUSHROOM, brown);
	}

	/**
	 * @return if the shroom pal is dancing.
	 */
	public boolean isDancing() {
		return entityData.get(DANCING);
	}

	/**
	 * @param dancing if the shroom pal should be dancing.
	 */
	public void setDancing(boolean dancing) {
		entityData.set(DANCING, dancing);
	}

	/**
	 * @return the number of crops eaten by the shroom pal
	 */
	public int getCropsEaten() {
		return entityData.get(CROPS_EATEN);
	}

	/**
	 * @param cropsEaten the number of crops eaten by the shroom pal
	 */
	public void setCropsEaten(int cropsEaten) {
		entityData.set(CROPS_EATEN, cropsEaten);
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return isBig() ? ModSounds.SHROOM_PAL_DEATH_BIG : ModSounds.SHROOM_PAL_DEATH;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return isBig() ? ModSounds.SHROOM_PAL_HURT_BIG : ModSounds.SHROOM_PAL_HURT;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		boolean play = random.nextFloat() < 0.3;
		if (!play) return null;
		return isBig() ? ModSounds.SHROOM_PAL_AMBIENT_BIG : ModSounds.SHROOM_PAL_AMBIENT;
	}

	@Override
	protected void playStepSound(BlockPos blockPos, BlockState blockState) {
		if (blockState.getMaterial().isLiquid()) {
			return;
		}
		BlockState blockState2 = this.level.getBlockState(blockPos.above());
		SoundType soundType = blockState2.is(BlockTags.INSIDE_STEP_SOUND_BLOCKS) ? blockState2.getSoundType() : blockState.getSoundType();
		this.playSound(isBig() ? ModSounds.SHROOM_PAL_STEP_BIG : ModSounds.SHROOM_PAL_STEP, soundType.getVolume() * 0.15f, soundType.getPitch());
	}

	/**
	 * Registers the animation controllers for the shroom pal.
	 */
	@Override
	public void registerControllers(AnimationData animationData) {
		animationData.addAnimationController(new AnimationController(this, "controller_walk", 0, event -> {
			if (event.isMoving()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
				return PlayState.CONTINUE;
			}
			return PlayState.STOP;
		}));
		animationData.addAnimationController(new AnimationController(this, "controller_bounce", 0, event -> {
			if (isDancing()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("bounce", true));
				return PlayState.CONTINUE;
			}
			return PlayState.STOP;
		}));
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	/**
	 * @return if the shroom pal is able to spawn with the given parameters.
	 */
	public static boolean checkShroomPalSpawnRules(EntityType<ShroomPal> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
		return levelAccessor.getBlockState(blockPos.below()).is(ModTags.SHROOMPALS_SPAWNABLE_ON);
	}

	@Override
	public boolean removeWhenFarAway(double d) {
		return false;
	}

	@Override
	public int getExperienceReward() {
		return 1 + this.level.random.nextInt(3);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 8.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.25D);
	}

	public static class DanceGoal extends Goal {

		private final ShroomPal pal;

		public DanceGoal(ShroomPal pal) {
			this.pal = pal;
			setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			return canContinueToUse();
		}

		/**
		 * @return if the shroom pal is able to dance.
		 */
		@Override
		public boolean canContinueToUse() {
			return pal.isOnGround() && pal.getNavigation().isDone();
		}

		@Override
		public void start() {
			pal.setDancing(true);
		}

		@Override
		public void stop() {
			pal.setDancing(false);
		}

	}

	public static class TurnCropsIntoBoneMealGoal extends MoveToBlockGoal {

		private static final int WAIT_TICKS = 40;
		protected int ticksWaited;

		private final ShroomPal pal;

		public TurnCropsIntoBoneMealGoal(ShroomPal pal, double d, int i) {
			super(pal, d, 5, i);
			this.pal = pal;
			setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean shouldRecalculatePath() {
			return this.tryTicks % 100 == 0;
		}

		@Override
		public double acceptedDistance() {
			return 2;
		}

		@Override
		protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
			return isValidTarget(levelReader.getBlockState(blockPos));
		}

		private boolean isValidTarget(BlockState blockState) {
			return blockState.getBlock() instanceof CropBlock cropBlock && blockState.getValue(cropBlock.getAgeProperty()) >= cropBlock.getMaxAge();
		}

		@Override
		public void tick() {
			if (this.isReachedTarget()) {
				if (this.ticksWaited >= WAIT_TICKS) {
					this.onReachedTarget();
				} else {
					++this.ticksWaited;
				}
			}
			super.tick();
		}

		protected void onReachedTarget() {
			if (!pal.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
				return;
			}
			BlockState blockState = pal.level.getBlockState(this.blockPos);

			if (!isValidTarget(blockState)) {
				return;
			}

			if (blockState.getBlock() instanceof CropBlock cropBlock) {
				if (pal.isBig() || pal.random.nextInt(10) <= 3) {
					Block.popResource(pal.level, this.blockPos, new ItemStack(Items.BONE_MEAL));
				}

				if (!pal.isBig()) {
					pal.setCropsEaten(pal.getCropsEaten() + 1);
					if (pal.getCropsEaten() >= 50) {
						pal.setBig(true);
						pal.playSound(ModSounds.SHROOM_PAL_GROW);
					}
				}

				pal.playSound(pal.isBig() ? ModSounds.SHROOM_PAL_HARVEST_BIG : ModSounds.SHROOM_PAL_HARVEST);
				pal.level.setBlock(this.blockPos, blockState.setValue(cropBlock.getAgeProperty(), 1), 2);
			}

		}

		@Override
		public void start() {
			this.ticksWaited = 0;
			super.start();
		}

	}

	public static class FollowMobGoal extends Goal {

		public static final int HORIZONTAL_SCAN_RANGE = 8;
		public static final int VERTICAL_SCAN_RANGE = 4;
		public static final int DONT_FOLLOW_IF_CLOSER_THAN = 5;
		public static final int DONT_FOLLOW_IF_FURTHER_THAN = 10;

		private final ShroomPal mob;
		private final Class<? extends LivingEntity> toFollow;
		private final double speedModifier;

		@Nullable
		private LivingEntity following;
		private int timeToRecalcPath;

		public FollowMobGoal(ShroomPal mob, Class<? extends LivingEntity> toFollow, double speedModifier) {
			this.mob = mob;
			this.toFollow = toFollow;
			this.speedModifier = speedModifier;
		}

		@Override
		public boolean canUse() {
			if (mob.lastHurtByPlayerTime > 0) {
				return false;
			}
			if (mob.isBig()) {
				return false;
			}

			List<? extends LivingEntity> list = mob.level.getEntitiesOfClass(toFollow, mob.getBoundingBox().inflate(HORIZONTAL_SCAN_RANGE, VERTICAL_SCAN_RANGE, HORIZONTAL_SCAN_RANGE));

			LivingEntity entity = null;
			double d = Double.MAX_VALUE;
			for (LivingEntity entity2 : list) {
				double e;
				if ((e = this.mob.distanceToSqr(entity2)) > d) continue;
				d = e;
				entity = entity2;
			}

			if (entity == null || d < (DONT_FOLLOW_IF_CLOSER_THAN^2)) {
				return false;
			}

			this.following = entity;
			return true;
		}

		@Override
		public boolean canContinueToUse() {
			if (mob.lastHurtByPlayerTime > 0) {
				return false;
			}
			if (this.following == null || !this.following.isAlive() || mob.isBig()) {
				return false;
			}
			double d = this.mob.distanceToSqr(this.following);
			return !(d < (DONT_FOLLOW_IF_CLOSER_THAN^2)) && !(d > (DONT_FOLLOW_IF_FURTHER_THAN^2));
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

}
