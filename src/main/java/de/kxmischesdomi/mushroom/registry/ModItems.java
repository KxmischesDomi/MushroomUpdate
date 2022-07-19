package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.item.ShroomGlider;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModItems {

	public static final CreativeModeTab TAB = FabricItemGroupBuilder.build(
			new ResourceLocation(MushroomMod.MOD_ID, "root"),
			() -> new ItemStack(ModItems.SHROOM_GLIDER)
	);

	public static final Item SHROOM_GLIDER = register("shroom_glider", new ShroomGlider(ArmorMaterials.LEATHER, EquipmentSlot.HEAD, new FabricItemSettings().group(TAB).maxCount(1).durability(100)));

	public static final Item PUFF_CREEPER_SPAWN_EGG = register("puff_creeper_spawn_egg",
			new SpawnEggItem(ModEntities.PUFF_CREEPER, 0xC5AB80, 0x5D5247, new FabricItemSettings().group(TAB)));

	public static final Item SHROOM_PAL_SPAWN_EGG = register("shroom_pal_spawn_egg",
			new SpawnEggItem(ModEntities.SHROOM_PAL, 0x8B1A18, 0xFCBDBD, new FabricItemSettings().group(TAB)));

	private static Item register(String name, Item item) {
		return Registry.register(Registry.ITEM, new ResourceLocation(MushroomMod.MOD_ID, name), item);
	}

	public static void init() { }

}