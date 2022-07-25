package de.kxmischesdomi.mushroom.mixin;

import com.mojang.datafixers.util.Pair;
import de.kxmischesdomi.mushroom.registry.ModBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(OverworldBiomeBuilder.class)
public abstract class OverworldBiomeBuilderMixin {

	@Shadow protected abstract void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter parameter, Climate.Parameter parameter2, Climate.Parameter parameter3, Climate.Parameter parameter4, Climate.Parameter parameter5, float f, ResourceKey<Biome> resourceKey);

	@Shadow @Final private Climate.Parameter FULL_RANGE;

	private final Climate.Parameter shroomCavesContinentalness = Climate.Parameter.span(-1.2f, -1.06f);
	private final Climate.Parameter glowCavesContinentalness = Climate.Parameter.span(-1.06f, -1.05f);

	/**
	 * Add custom biomes to vanilla world generation
	 */
	@Inject(method = "addUndergroundBiomes", at = @At("HEAD"))
	public void addUndergroundBiomesInject(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, CallbackInfo ci) {
		this.addUndergroundBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.glowCavesContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0f, ModBiomes.SHROOM_CAVES);
		this.addUndergroundBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.shroomCavesContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0f, ModBiomes.GLOW_CAVES);
	}

}
