package de.kxmischesdomi.mushroom;

import de.kxmischesdomi.mushroom.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.example.GeckoLibMod;

public class MushroomMod implements ModInitializer {

	public static final String MOD_ID = "mushroom";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModEntities.registerAttributes();
		ModItems.init();
		ModSounds.init();
		ModConfiguredFeatures.init();
		ModBiomes.init();

		BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.MUSHROOM_FIELDS), MobCategory.MONSTER, ModEntities.PUFF_CREEPER, 100, 1, 3);
		BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.MUSHROOM_FIELDS), MobCategory.CREATURE, ModEntities.SHROOM_PAL, 10, 4, 8);

		GeckoLibMod.DISABLE_IN_DEV = true;
	}

}
