package de.kxmischesdomi.mushroom.entity;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.entity.ai.goal.PuffLeapAtTargetGoal;
import de.kxmischesdomi.mushroom.registry.ModSounds;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
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
public class PuffCreeper extends Monster implements IAnimatable {

	private final AnimationFactory factory = new AnimationFactory(this);

	private static final EntityDataAccessor<Boolean> POWERED = SynchedEntityData.defineId(PuffCreeper.class, EntityDataSerializers.BOOLEAN);

	private int lastPuffTicks = 0;

	public boolean playPuffAnimation = false;

	public PuffCreeper(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0f, 1.0, 1.2));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0f, 1.0, 1.2));
		this.goalSelector.addGoal(2, new PuffLeapAtTargetGoal(this, 0.4f));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.25, false));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.75, 1));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(POWERED, false);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putBoolean("Powered", isPowered());
		compoundTag.putInt("NextPuff", getLastPuffTicks());
		compoundTag.putInt("PlayPuff", getLastPuffTicks());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		setPowered(compoundTag.getBoolean("Powered"));
		if (compoundTag.contains("NextPuff")) {
			setLastPuffTicks(compoundTag.getInt("NextPuff"));
		}
		if (compoundTag.contains("PlayPuff")) {
			setLastPuffTicks(compoundTag.getInt("PlayPuff"));
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (this.isAlive()) {

			if (puff()) {
				this.playSound(SoundEvents.PUFFER_FISH_BLOW_OUT, 1.0f, 1.0f);
				setLastPuffTicks(0);

				for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(getExplodeRange()))) {
					if (entity instanceof PuffCreeper) continue;

					Difficulty difficulty = getLevel().getDifficulty();
					float multiplier = isPowered() ? 2f : 1;
					if (difficulty == Difficulty.EASY) {
						entity.addEffect(new MobEffectInstance(MobEffects.POISON, (int) (5*20 * multiplier), 1));
					} else if (difficulty == Difficulty.NORMAL) {
						entity.addEffect(new MobEffectInstance(MobEffects.POISON, (int) (6*20 * multiplier), 1));
					} else if (difficulty == Difficulty.HARD) {
						entity.addEffect(new MobEffectInstance(MobEffects.POISON, (int) (7*20 * multiplier), 1));
					}

				}

				FriendlyByteBuf buf = PacketByteBufs.create();
				buf.writeUUID(uuid);
				for (ServerPlayer player : PlayerLookup.tracking(this)) {
					ServerPlayNetworking.send(player, new ResourceLocation(MushroomMod.MOD_ID, "puff"), buf);

					player.getLevel().sendParticles(player,
							new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SANDSTONE.defaultBlockState()),
							true,
							getX(), getY(), getZ(),
							isPowered() ? 400 : 100,
							getExplodeRange() / 2.5, getExplodeRange() / 2.5, getExplodeRange() / 2.5,
							10
					);
				}

				this.playSound(ModSounds.PUFF_CREEPER_PUFF, 1, 1);

			} else {
				setLastPuffTicks(getLastPuffTicks() + 1);
			}

		}

	}

	public float getExplodeRange() {
		return isPowered() ? 5 : 2.5f;
	}

	@Override
	public boolean hurt(DamageSource damageSource, float f) {
		if (damageSource == DamageSource.MAGIC) {
			return false;
		}
		return super.hurt(damageSource, f);
	}

	public boolean puff() {
		return isInTargetReach() && getLastPuffTicks() >= 60;
	}

	public boolean isInTargetReach() {
		if (getTarget() == null) return false;
		if (distanceTo(getTarget()) > getExplodeRange()) return false;
		return getSensing().hasLineOfSight(getTarget());
	}

	public int getLastPuffTicks() {
		return lastPuffTicks;
	}

	public void setLastPuffTicks(int ticks) {
		lastPuffTicks = ticks;
	}

	public boolean isPowered() {
		return entityData.get(POWERED);
	}

	public void setPowered(boolean powered) {
		entityData.set(POWERED, powered);
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSounds.PUFF_CREEPER_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return ModSounds.PUFF_CREEPER_HURT;
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
		animationData.addAnimationController(new AnimationController(this, "controller_puff", 0, event -> {
			if (playPuffAnimation) {
				playPuffAnimation = false;
				event.getController().setAnimation(new AnimationBuilder().addAnimation("puff", false));
				return PlayState.CONTINUE;
			} else {
				event.getController().clearAnimationCache();
			}
			return PlayState.CONTINUE;
		}));
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public static boolean checkMonsterSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
		return serverLevelAccessor.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(serverLevelAccessor, blockPos, randomSource) && Monster.checkMobSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource);
	}

	public static boolean checkAnyLightMonsterSpawnRules(EntityType<? extends Monster> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
		return levelAccessor.getDifficulty() != Difficulty.PEACEFUL && Monster.checkMobSpawnRules(entityType, levelAccessor, mobSpawnType, blockPos, randomSource);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 10)
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.ATTACK_DAMAGE, 1)
				.add(Attributes.ATTACK_SPEED);
	}

}
