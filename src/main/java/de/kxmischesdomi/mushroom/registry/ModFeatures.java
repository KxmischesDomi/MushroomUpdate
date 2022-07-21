package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.feature.HugeGlowMushroomFeature;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModFeatures {

	public static final Feature<HugeMushroomFeatureConfiguration> HUGE_GLOW_MUSHROOM = register(
			"huge_glow_mushroom",
			new HugeGlowMushroomFeature(HugeMushroomFeatureConfiguration.CODEC)
	);

	private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String string, F feature) {
		return Registry.register(Registry.FEATURE, new ResourceLocation(MushroomMod.MOD_ID, string), feature);
	}

}
