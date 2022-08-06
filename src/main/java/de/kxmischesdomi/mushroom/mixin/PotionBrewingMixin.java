package de.kxmischesdomi.mushroom.mixin;

import de.kxmischesdomi.mushroom.registry.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {

	@Inject(method = "isIngredient", at = @At("HEAD"), cancellable = true)
	private static void isIngredient(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.is(ModItems.PUFF_SPORES)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "hasPotionMix", at = @At("HEAD"), cancellable = true)
	private static void hasPotionMix(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<Boolean> cir) {
		if (itemStack2.is(ModItems.PUFF_SPORES)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "mix", at = @At("HEAD"))
	private static void mixInject(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<ItemStack> cir) {
			if (!itemStack2.isEmpty()) {
				if (itemStack.is(ModItems.PUFF_SPORES)) {
					PotionUtils.setCustomEffects(itemStack2, Collections.singletonList(new MobEffectInstance(MobEffects.POISON, 10*20, 1)));
				}
			}

		}

}
