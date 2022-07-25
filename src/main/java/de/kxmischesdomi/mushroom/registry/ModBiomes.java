package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.MushroomMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModBiomes {

	public static final ResourceKey<Biome> SHROOM_CAVES = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MushroomMod.MOD_ID, "shroom_caves"));
	public static final ResourceKey<Biome> GLOW_CAVES = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MushroomMod.MOD_ID, "glowing_shroom_caves"));

	public static void init() {

	}

}
