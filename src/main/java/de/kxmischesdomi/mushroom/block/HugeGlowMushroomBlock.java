package de.kxmischesdomi.mushroom.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class HugeGlowMushroomBlock extends HugeMushroomBlock {

	public HugeGlowMushroomBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
		super.randomTick(blockState, serverLevel, blockPos, randomSource);
	}

	@Override
	public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
		double d = (double)blockPos.getX() + randomSource.nextDouble();
		double e = (double)blockPos.getY() + randomSource.nextDouble();
		double f = (double)blockPos.getZ() + randomSource.nextDouble();
		level.addParticle(ParticleTypes.GLOW, d, e, f, 0, 0, 0);
	}
}
