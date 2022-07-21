package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.api.BiomeGeneration;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModConfiguredFeatures {

	public static final Holder<ConfiguredFeature<HugeMushroomFeatureConfiguration, ?>> HUGE_GLOW_MUSHROOM = BiomeGeneration.register(
			"huge_glow_mushroom",
			ModFeatures.HUGE_GLOW_MUSHROOM,
			new HugeMushroomFeatureConfiguration(
					BlockStateProvider.simple((ModBlocks.GLOW_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.UP, true)).setValue(HugeMushroomBlock.DOWN, false)),
					BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, false).setValue(HugeMushroomBlock.DOWN, false)), 3
			)
	);

	public static void init() { }

}
