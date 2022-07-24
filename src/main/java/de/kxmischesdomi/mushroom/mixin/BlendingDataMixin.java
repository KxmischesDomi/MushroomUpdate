package de.kxmischesdomi.mushroom.mixin;

import de.kxmischesdomi.mushroom.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(BlendingData.class)
public class BlendingDataMixin {

	@Inject(method = "isGround", at = @At("TAIL"), cancellable = true)
	private static void isGroundInject(ChunkAccess chunkAccess, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
		BlockState blockState = chunkAccess.getBlockState(blockPos);
		if (blockState.is(ModBlocks.GLOW_MUSHROOM_BLOCK)) {
			cir.setReturnValue(false);
		}
	}

}
