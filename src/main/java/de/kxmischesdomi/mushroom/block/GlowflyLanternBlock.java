package de.kxmischesdomi.mushroom.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowflyLanternBlock extends LanternBlock {

	public GlowflyLanternBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
		if (randomSource.nextBoolean()) {
			double d = (double)blockPos.getX() + randomSource.nextDouble() / 2;
			double e = (double)blockPos.getY() + randomSource.nextDouble() / 2;
			double f = (double)blockPos.getZ() + randomSource.nextDouble() / 2;
			level.addParticle(ParticleTypes.GLOW, d, e, f, 0, 0, 0);
		}
	}

}
