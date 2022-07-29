package de.kxmischesdomi.mushroom.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.function.Supplier;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowMushroomBlock extends MushroomBlock {

	public GlowMushroomBlock(Properties properties, Supplier<Holder<? extends ConfiguredFeature<?, ?>>> supplier) {
		super(properties, supplier);
	}

	@Override
	public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
		if (randomSource.nextBoolean()) {
			double d = (double)blockPos.getX() + randomSource.nextDouble();
			double e = (double)blockPos.getY() + randomSource.nextDouble() / 2;
			double f = (double)blockPos.getZ() + randomSource.nextDouble();
			level.addParticle(ParticleTypes.GLOW, d, e, f, 0, 0, 0);
		}
	}
}
