package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.MushroomMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModTags {

	public static final TagKey<Block> SHROOMPALS_SPAWNABLE_ON = createBlockTag("shroompals_spawnable_on");

	private static TagKey<Block> createBlockTag(String name) {
		return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(MushroomMod.MOD_ID, name));
	}

	public static void init() { }

}
