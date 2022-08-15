package de.kxmischesdomi.mushroom.mixin;

import de.kxmischesdomi.mushroom.item.PuffSporesItem;
import de.kxmischesdomi.mushroom.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
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
public abstract class PotionBrewingMixin {

	private static final int maxLevel = 6;

	@Inject(method = "isIngredient", at = @At("HEAD"), cancellable = true)
	private static void isIngredient(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.is(ModItems.PUFF_SPORES)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "hasPotionMix", at = @At("HEAD"), cancellable = true)
	private static void hasPotionMix(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<Boolean> cir) {
		if (itemStack2.is(ModItems.PUFF_SPORES)) {
			if (PuffSporesItem.getPuffSporesEffect(itemStack, (count, tag) -> {}) >= maxLevel) {
				cir.setReturnValue(false);
				return;
			}
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "mix", at = @At("HEAD"))
	private static void mixInject(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<ItemStack> cir) {
		if (!itemStack2.isEmpty()) {
			if (itemStack.is(ModItems.PUFF_SPORES)) {

				if (PuffSporesItem.getPuffSporesEffect(itemStack2, (count, compoundTag) -> {
					int i = Math.min(Math.max(1, count + 1), maxLevel);
					compoundTag.putInt(PuffSporesItem.TAG_PUFF_SPORES_EFFECT, i);
					compoundTag.putInt("Duration", calculateNewDuration(i));

				}) == 0) {
					MobEffectInstance instance = new MobEffectInstance(MobEffects.POISON, 0, 1) {
						@Override
						public CompoundTag save(CompoundTag compoundTag) {
							CompoundTag save = super.save(compoundTag);
							save.putInt(PuffSporesItem.TAG_PUFF_SPORES_EFFECT, 1);
							save.putInt("Duration", calculateNewDuration(1));
							return save;
						}
					};
					PotionUtils.setCustomEffects(itemStack2, Collections.singletonList(instance));
				}

			}
		}

	}

	private static int calculateNewDuration(int i) {
		int secondsPerLevel = 5;
		return Math.min(secondsPerLevel * maxLevel, i * secondsPerLevel) * 20;
	}

}
