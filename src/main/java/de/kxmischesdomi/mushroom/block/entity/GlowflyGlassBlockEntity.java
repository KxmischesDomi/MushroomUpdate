package de.kxmischesdomi.mushroom.block.entity;

import de.kxmischesdomi.mushroom.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowflyGlassBlockEntity extends BlockEntity {

	public GlowflyGlassBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.GLOWFLY_GLASS_BLOCK_ENTITY, blockPos, blockState);
	}

}
