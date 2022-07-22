package de.kxmischesdomi.mushroom.worldgen;

import com.mojang.datafixers.util.Pair;
import de.kxmischesdomi.mushroom.registry.ModBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ParameterUtils;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.List;
import java.util.function.Consumer;

import static terrablender.api.ParameterUtils.*;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowCavesRegion extends Region {

	public GlowCavesRegion(ResourceLocation name, int weight) {
		super(name, RegionType.OVERWORLD, weight);
	}

	@Override
	public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {

		this.addModifiedVanillaOverworldBiomes(mapper, builder -> {

			List<Climate.ParameterPoint> glowCavesPoints = new ParameterUtils.ParameterPointListBuilder()
					.temperature(Temperature.FULL_RANGE)
					.humidity(Climate.Parameter.span(0.7f, 1))
					.continentalness(Continentalness.FULL_RANGE)
					.erosion(Erosion.FULL_RANGE)
					.depth(Depth.FLOOR)
					.weirdness(Weirdness.FULL_RANGE)
					.build();
			glowCavesPoints.forEach(point -> builder.replaceBiome(point, ModBiomes.GLOW_CAVES));
		});

	}

}
