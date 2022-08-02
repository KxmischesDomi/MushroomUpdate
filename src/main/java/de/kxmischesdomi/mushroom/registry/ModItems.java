package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.item.GlowMushroomStewItem;
import de.kxmischesdomi.mushroom.item.ShroomGlider;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModItems {

	public static final CreativeModeTab TAB = FabricItemGroupBuilder.build(
			new ResourceLocation(MushroomMod.MOD_ID, "root"),
			() -> new ItemStack(ModItems.GLOW_MUSHROOM)
	);

	public static final Item GLOW_MUSHROOM = registerBlock(ModBlocks.GLOW_MUSHROOM);
	public static final Item GLOW_MUSHROOM_BLOCK = registerBlock(ModBlocks.GLOW_MUSHROOM_BLOCK);

	public static final Item GLOW_MUSHROOM_STEW = register("glow_mushroom_stew", new GlowMushroomStewItem(new Item.Properties().stacksTo(1).tab(TAB).food(Foods.MUSHROOM_STEW)));

	public static final Item SHROOM_GLIDER = register("shroom_glider", new ShroomGlider(ArmorMaterials.LEATHER, EquipmentSlot.HEAD, new FabricItemSettings().group(TAB).maxCount(1).durability(100)));

	public static final Item MUSIC_DISC_TWILIGHT = registerMusicDisc("music_disc_twilight", 1, ModSounds.MUSIC_DISC_TWILIGHT, 119, new FabricItemSettings().group(TAB).stacksTo(1).rarity(Rarity.RARE));

	public static final Item SHROOM_PAL_SPAWN_EGG = register("shroom_pal_spawn_egg", new SpawnEggItem(ModEntities.SHROOM_PAL, 0x8B1A18, 0xFCBDBD, new FabricItemSettings().group(TAB)));
	public static final Item PUFF_CREEPER_SPAWN_EGG = register("puff_creeper_spawn_egg", new SpawnEggItem(ModEntities.PUFF_CREEPER, 0xC5AB80, 0x5D5247, new FabricItemSettings().group(TAB)));
	public static final Item GLOW_MOOSHROOM_SPAWN_EGG = register("glow_mooshroom_spawn_egg", new SpawnEggItem(ModEntities.GLOW_MOOSHROOM, 0x095656, 0x69E2D0, new FabricItemSettings().group(TAB)));
	public static final Item GLOWFLY_SPAWN_EGG = register("glowfly_spawn_egg", new SpawnEggItem(ModEntities.GLOWFLY, 0x343338, 0x00FFFA, new FabricItemSettings().group(TAB)));

	private static Item register(String name, Item item) {
		return register(new ResourceLocation(MushroomMod.MOD_ID, name), item);
	}

	private static Item register(ResourceLocation location, Item item) {
		return Registry.register(Registry.ITEM, location, item);
	}

	private static Item registerBlock(Block block) {
		return register(Registry.BLOCK.getKey(block), new BlockItem(block, new Item.Properties().tab(TAB)));
	}

	/**
	 * Creates a music disc item that has compatibility for 1.19 and 1.19.1 due to changes in the item's constructor
	 */
	private static Item registerMusicDisc(String name, int output, SoundEvent soundEvent, int length, Item.Properties properties) {

		try {
			RecordItem item;
			String versionString = SharedConstants.getCurrentVersion().getName();
			// Check if the version is newer or equal to 1.19.1
			if (versionString.compareTo("1.19.1") >= 0) {
				Constructor<RecordItem> constructor = RecordItem.class.getDeclaredConstructor(int.class, SoundEvent.class, Item.Properties.class, int.class);
				constructor.setAccessible(true);
				item = constructor.newInstance(output, soundEvent, properties, length);
			} else {
				Constructor<RecordItem> constructor = RecordItem.class.getDeclaredConstructor(int.class, SoundEvent.class, Item.Properties.class);
				constructor.setAccessible(true);
				item = constructor.newInstance(output, soundEvent, properties);
			}
			register(new ResourceLocation(MushroomMod.MOD_ID, name), item);
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			MushroomMod.LOGGER.error("Failed to register music disc item " + name + ": " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	public static void init() {
		CompostingChanceRegistry.INSTANCE.add(ModItems.GLOW_MUSHROOM, 0.65f);
	}

}
