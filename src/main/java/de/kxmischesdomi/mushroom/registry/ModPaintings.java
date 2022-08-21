package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.MushroomMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModPaintings {

	public static final ResourceKey<PaintingVariant> FISHING_TRIP = create("fishing_trip");

	public static void init() {
		Registry.register(Registry.PAINTING_VARIANT, ModPaintings.FISHING_TRIP, new PaintingVariant(64, 48));
	}

	private static ResourceKey<PaintingVariant> create(String string) {
		return ResourceKey.create(Registry.PAINTING_VARIANT_REGISTRY, new ResourceLocation(MushroomMod.MOD_ID, string));
	}

}
