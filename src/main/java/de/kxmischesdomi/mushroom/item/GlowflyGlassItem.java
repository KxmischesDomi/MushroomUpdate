package de.kxmischesdomi.mushroom.item;

import de.kxmischesdomi.mushroom.core.IGlowfly;
import de.kxmischesdomi.mushroom.entity.Glowfly;
import de.kxmischesdomi.mushroom.registry.ModEntities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowflyGlassItem extends BlockItem {

	public GlowflyGlassItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
		if (player.isSecondaryUseActive()) {
			ItemStack itemStack = player.getItemInHand(interactionHand);
			BlockPos blockPos;
			BlockHitResult blockHitResult = BucketItem.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
			if (blockHitResult.getType() == HitResult.Type.MISS) {
				blockPos = player.getOnPos();
			} else if (blockHitResult.getType() == HitResult.Type.BLOCK) {
				blockPos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
			} else {
				return InteractionResultHolder.pass(itemStack);
			}

			spawnExtraContent(player, level, itemStack, blockPos);
			if (player instanceof ServerPlayer) {
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockPos, itemStack);
			}

			player.awardStat(Stats.ITEM_USED.get(this));
			return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(itemStack, player), level.isClientSide());
		}

		return super.use(level, player, interactionHand);
	}

	@Override
	public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
		super.inventoryTick(itemStack, level, entity, i, bl);
		int cooldown = getHealingCooldown(itemStack);
		if (cooldown > 0) {
			setHealingCooldown(itemStack, cooldown - 1);
		} else if (hasHealingCooldownTag(itemStack)) {
			removeHealingCooldownTag(itemStack);
		}
	}

	public static ItemStack getEmptySuccessItem(ItemStack itemStack, Player player) {
		if (!player.getAbilities().instabuild) {
			return ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE));
		}
		return itemStack;
	}

	public void spawnExtraContent(Player player, Level level, ItemStack itemStack, BlockPos blockPos) {
		if (level instanceof ServerLevel serverLevel) {
			Entity spawn = ModEntities.GLOWFLY.spawn(serverLevel, itemStack, null, blockPos, MobSpawnType.BUCKET, true, false);
			if (spawn instanceof Glowfly glowfly) {
				IGlowfly.loadDefaultDataFromGlassTag(glowfly, itemStack.getOrCreateTag());
			}
			level.gameEvent(player, GameEvent.ENTITY_PLACE, blockPos);
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext useOnContext) {
		if (useOnContext.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		}
		return super.useOn(useOnContext);
	}

	public boolean hasHealingCooldownTag(ItemStack itemStack) {
		CompoundTag tag = itemStack.getTag();
		return tag != null && tag.contains("HealingCooldown", 3);
	}

	public void removeHealingCooldownTag(ItemStack itemStack) {
		CompoundTag tag = itemStack.getTag();
		if (tag != null) {
			tag.remove("HealingCooldown");
		}
	}

	public int getHealingCooldown(ItemStack itemStack) {
		return itemStack.getOrCreateTag().getInt("HealingCooldown");
	}

	public void setHealingCooldown(ItemStack itemStack, int cooldown) {
		itemStack.getOrCreateTag().putInt("HealingCooldown", cooldown);
	}

}
