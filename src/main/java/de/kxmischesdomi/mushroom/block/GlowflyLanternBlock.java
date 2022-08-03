package de.kxmischesdomi.mushroom.block;

import de.kxmischesdomi.mushroom.block.entity.GlowflyGlassBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowflyLanternBlock extends LanternBlock implements EntityBlock {

	public GlowflyLanternBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
		if (randomSource.nextBoolean()) {
			double xRand = randomSource.nextDouble();
			double x = (double)blockPos.getX() + xRand / 2 + xRand / 2;
			double yRand= randomSource.nextDouble();
			double y = (double)blockPos.getY() + yRand / 2 + xRand / 2;
			double zRand = randomSource.nextDouble();
			double z = (double)blockPos.getZ() + zRand / 2 + xRand / 2;
			level.addParticle(ParticleTypes.GLOW, x, y, z, 0, 0, 0);
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new GlowflyGlassBlockEntity(blockPos, blockState);
	}

	// BaseEntityBlock //

	@Override
	public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j) {
		super.triggerEvent(blockState, level, blockPos, i, j);
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		if (blockEntity == null) {
			return false;
		}
		return blockEntity.triggerEvent(i, j);
	}

	@Override
	@Nullable
	public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		return blockEntity instanceof MenuProvider ? (MenuProvider) blockEntity : null;
	}

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityType, BlockEntityType<E> blockEntityType2, BlockEntityTicker<A> blockEntityTicker) {
		return blockEntityType2 == blockEntityType ? blockEntityTicker : null;
	}

}
