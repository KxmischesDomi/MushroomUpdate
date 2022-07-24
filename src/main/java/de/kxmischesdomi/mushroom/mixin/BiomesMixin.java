package de.kxmischesdomi.mushroom.mixin;

import de.kxmischesdomi.mushroom.registry.ModBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(Biomes.class)
public class BiomesMixin {

	/**
	 * Registers dummies of the biomes to prevent issues with world generation additions
	 */
	@Inject(method = "bootstrap", at = @At("HEAD"))
	private static void bootstrapMixin(Registry<Biome> registry, CallbackInfoReturnable<Holder<Biome>> cir) {
		BuiltinRegistries.register(registry, ModBiomes.SHROOM_CAVES, OverworldBiomes.lushCaves());
		BuiltinRegistries.register(registry, ModBiomes.GLOW_CAVES, OverworldBiomes.lushCaves());
	}

}
