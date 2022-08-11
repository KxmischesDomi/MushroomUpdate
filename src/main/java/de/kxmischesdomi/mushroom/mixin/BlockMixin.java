package de.kxmischesdomi.mushroom.mixin;

import de.kxmischesdomi.mushroom.block.entity.GlowflyGlassBlockEntity;
import de.kxmischesdomi.mushroom.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(Block.class)
public class BlockMixin {

	@Inject(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;)V", at = @At("HEAD"))
	private static void dropResourcesInject(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, BlockEntity blockEntity, CallbackInfo ci) {
		dropGlowflyGlass(levelAccessor, blockPos, blockEntity);
	}

	@Inject(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"))
	private static void dropResourcesInject(BlockState blockState, Level level, BlockPos blockPos, BlockEntity blockEntity, Entity entity, ItemStack itemStack, CallbackInfo ci) {
		dropGlowflyGlass(level, blockPos, blockEntity);
	}

	private static void dropGlowflyGlass(LevelAccessor level, BlockPos pos, BlockEntity blockEntity) {
		if (level instanceof ServerLevel serverLevel && blockEntity instanceof GlowflyGlassBlockEntity glassBlockEntity) {
			ItemStack itemStack1 = new ItemStack(ModItems.GLOWFLY_GLASS);
			glassBlockEntity.saveToItem(itemStack1);
			Block.popResource(serverLevel, pos, itemStack1);
		}
	}

}
