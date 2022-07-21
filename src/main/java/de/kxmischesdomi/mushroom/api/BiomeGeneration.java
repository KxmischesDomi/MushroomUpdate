package de.kxmischesdomi.mushroom.api;

import de.kxmischesdomi.mushroom.MushroomMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BiomeGeneration {

	public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<FC, ?>> register(String string, F feature, FC featureConfiguration) {
		return registerExact(BuiltinRegistries.CONFIGURED_FEATURE, string, new ConfiguredFeature<>(feature, featureConfiguration));
	}

	public static <V extends T, T> Holder<V> registerExact(Registry<T> registry, String string, V object) {
		return BuiltinRegistries.<V>register((Registry<V>) registry, new ResourceLocation(MushroomMod.MOD_ID, string), object);
	}

	public static Holder<PlacedFeature> register(String string, Holder<? extends ConfiguredFeature<?, ?>> holder, List<PlacementModifier> list) {
		return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(MushroomMod.MOD_ID, string), new PlacedFeature(Holder.hackyErase(holder), List.copyOf(list)));
	}

	public static Holder<PlacedFeature> register(String string, Holder<? extends ConfiguredFeature<?, ?>> holder, PlacementModifier ... placementModifiers) {
		return PlacementUtils.register(string, holder, List.of(placementModifiers));
	}

	}

}
