package de.kxmischesdomi.mushroom;

import de.kxmischesdomi.mushroom.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		ModTags.init();
		ModCriteriaTriggers.init();
		ModBlockEntities.init();
		ModStats.init();
		ModPaintings.init();

		BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.MUSHROOM_FIELDS), MobCategory.MONSTER, ModEntities.PUFF_CREEPER, 100, 1, 3);
		BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.MUSHROOM_FIELDS), MobCategory.CREATURE, ModEntities.SHROOM_PAL, 10, 4, 8);

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (source.isBuiltin() && BuiltInLootTables.FISHING_TREASURE.equals(id)) {
				LootPool.Builder poolBuilder = LootPool.lootPool().add(
						LootItem.lootTableItem(ModItems.MUSIC_DISC_TWILIGHT)
						.when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(ModBiomes.GLOW_CAVES)))
				);
				tableBuilder.withPool(poolBuilder);
			}
		});
	}

}
