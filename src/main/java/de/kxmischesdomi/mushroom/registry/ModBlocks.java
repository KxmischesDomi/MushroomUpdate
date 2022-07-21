package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.MushroomMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModBlocks {

	public static final Block GLOW_MUSHROOM = register("glow_mushroom", new MushroomBlock(BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.COLOR_CYAN)
			.noCollission()
			.randomTicks()
			.instabreak()
			.sound(SoundType.GRASS)
			.lightLevel(blockState -> 14)
			.hasPostProcess(ModBlocks::always), () -> ModConfiguredFeatures.HUGE_GLOW_MUSHROOM
			)
	);

	public static final Block GLOW_MUSHROOM_BLOCK = register("glow_mushroom_block", new HugeMushroomBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_CYAN)
			.strength(0.2f)
			.sound(SoundType.WOOD)
			.lightLevel(state -> 14)
			)
	);

	private static Block register(String name, Block block) {
		return Registry.register(Registry.BLOCK, new ResourceLocation(MushroomMod.MOD_ID, name), block);
	}

	private static boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return true;
	}

}
