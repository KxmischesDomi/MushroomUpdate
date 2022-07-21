package de.kxmischesdomi.mushroom.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
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
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;

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

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
		SpawnGroupData groupData = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
		setBrownMushroom(random.nextFloat() > 0.5);
		return groupData;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
		this.goalSelector.addGoal(2, new TurnCropsIntoBoneMealGoal(this, 1, 3));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.75, 1));
		this.goalSelector.addGoal(5, new DanceGoal(this));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(BIG, false);
		entityData.define(BROWN_MUSHROOM, false);
		entityData.define(CROPS_EATEN, 0);
		entityData.define(DANCING, false);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putBoolean("IsBig", isBig());
		compoundTag.putBoolean("IsBrown", isBrownMushroom());
		compoundTag.putInt("CropsEaten", getCropsEaten());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		this.setBig(compoundTag.getBoolean("IsBig"));
		this.setBrownMushroom(compoundTag.getBoolean("IsBrown"));
		this.setCropsEaten(compoundTag.getInt("CropsEaten"));
	}

	public boolean isBig() {
		return entityData.get(BIG);
	}

	public void setBig(boolean baby) {
		entityData.set(BIG, baby);
	}

	public boolean isBrownMushroom() {
		return entityData.get(BROWN_MUSHROOM);
	}

	public void setBrownMushroom(boolean brown) {
		entityData.set(BROWN_MUSHROOM, brown);
	}

	public boolean isDancing() {
		return entityData.get(DANCING);
	}

	public void setDancing(boolean dancing) {
		entityData.set(DANCING, dancing);
	}

	public int getCropsEaten() {
		return entityData.get(CROPS_EATEN);
	}

	public void setCropsEaten(int cropsEaten) {
		entityData.set(CROPS_EATEN, cropsEaten);
	}

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
	public void tick() {
		super.tick();
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	public boolean wantsToPickUp(ItemStack itemStack) {
		return super.wantsToPickUp(itemStack);
	}

	public static boolean checkShroomPalSpawnRules(EntityType<ShroomPal> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
		return levelAccessor.getBlockState(blockPos.below()).is(BlockTags.MOOSHROOMS_SPAWNABLE_ON) && isBrightEnoughToSpawn(levelAccessor, blockPos);
	}

	protected static boolean isBrightEnoughToSpawn(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos) {
		return blockAndTintGetter.getRawBrightness(blockPos, 0) > 8;
	}

	@Override
	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence() || isBig() || getCropsEaten() > 0;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 4)
				.add(Attributes.MOVEMENT_SPEED, 0.25D);
	}

	public static class DanceGoal extends Goal {

		private final ShroomPal pal;

		public DanceGoal(ShroomPal pal) {
			this.pal = pal;
		}

		@Override
		public boolean canUse() {
			return canContinueToUse();
		}

		@Override
		public boolean canContinueToUse() {
			return pal.isOnGround() && !pal.getNavigation().isInProgress();
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
					}
				}

				pal.playSound(SoundEvents.COMPOSTER_FILL_SUCCESS, 1.0f, 1.0f);
				pal.level.setBlock(this.blockPos, blockState.setValue(cropBlock.getAgeProperty(), 1), 2);
			}

		}

		@Override
		public void start() {
			this.ticksWaited = 0;
			super.start();
		}

	}

}
