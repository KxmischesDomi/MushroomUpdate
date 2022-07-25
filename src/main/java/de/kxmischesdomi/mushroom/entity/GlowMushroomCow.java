package de.kxmischesdomi.mushroom.entity;

import de.kxmischesdomi.mushroom.registry.ModEntities;
import de.kxmischesdomi.mushroom.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowMushroomCow extends Cow implements Shearable {

	public GlowMushroomCow(EntityType<? extends Cow> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
		if (levelReader.getBlockState(blockPos.below()).is(Blocks.MYCELIUM)) {
			return 10.0f;
		}
		return levelReader.getPathfindingCostFromLightLevels(blockPos);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
		ItemStack itemStack = player.getItemInHand(interactionHand);

		if (itemStack.is(Items.SHEARS) && this.readyForShearing()) {
			this.shear(SoundSource.PLAYERS);
			this.gameEvent(GameEvent.SHEAR, player);
			if (!this.level.isClientSide) {
				itemStack.hurtAndBreak(1, player, player2 -> player2.broadcastBreakEvent(interactionHand));
			}
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}

		return super.mobInteract(player, interactionHand);
	}

	@Override
	public void shear(SoundSource soundSource) {
		this.level.playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, soundSource, 1.0f, 1.0f);
		if (!this.level.isClientSide()) {
			((ServerLevel)this.level).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
			this.discard();
			Cow cow = EntityType.COW.create(this.level);
			cow.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
			cow.setHealth(this.getHealth());
			cow.yBodyRot = this.yBodyRot;
			if (this.hasCustomName()) {
				cow.setCustomName(this.getCustomName());
				cow.setCustomNameVisible(this.isCustomNameVisible());
			}
			if (this.isPersistenceRequired()) {
				cow.setPersistenceRequired();
			}
			cow.setInvulnerable(this.isInvulnerable());
			this.level.addFreshEntity(cow);
			for (int i = 0; i < 5; ++i) {
				this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0), this.getZ(), new ItemStack(ModItems.GLOW_MUSHROOM)));
			}
		}
	}

	@Override
	public boolean readyForShearing() {
		return this.isAlive() && !this.isBaby();
	}

	@Override
	public GlowMushroomCow getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		return ModEntities.GLOW_MOOSHROOM.create(serverLevel);
	}

	public static boolean checkMushroomSpawnRules(EntityType<GlowMushroomCow> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
		return levelAccessor.getBlockState(blockPos.below()).is(BlockTags.MOOSHROOMS_SPAWNABLE_ON);
	}

}
