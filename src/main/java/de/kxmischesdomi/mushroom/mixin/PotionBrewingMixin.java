package de.kxmischesdomi.mushroom.mixin;

import de.kxmischesdomi.mushroom.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import java.util.function.BiConsumer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(PotionBrewing.class)
public abstract class PotionBrewingMixin {

	@Inject(method = "isIngredient", at = @At("HEAD"), cancellable = true)
	private static void isIngredient(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.is(ModItems.PUFF_SPORES)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "hasPotionMix", at = @At("HEAD"), cancellable = true)
	private static void hasPotionMix(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<Boolean> cir) {
		if (itemStack2.is(ModItems.PUFF_SPORES)) {
			if (getPuffSporesEffect(itemStack, (count, tag) -> {}) >= 6) {
				cir.setReturnValue(false);
				return;
			}
			cir.setReturnValue(true);
		}
	}

	private static int getPuffSporesEffect(ItemStack itemStack, BiConsumer<Integer, CompoundTag> tagConsumer) {
		CompoundTag tag = itemStack.getTag();
		if (tag == null || !tag.contains(PotionUtils.TAG_CUSTOM_POTION_EFFECTS, 9)) return 0;
		ListTag list = tag.getList(PotionUtils.TAG_CUSTOM_POTION_EFFECTS, 10);

		for (int i = 0; i < list.size(); i++) {
			CompoundTag compound = list.getCompound(i);
			if (compound.contains("PuffSporesEffect")) {
				int count = compound.getInt("PuffSporesEffect");
				tagConsumer.accept(count, compound);
				return count;
			}
		}
		return 0;
	}

	@Inject(method = "mix", at = @At("HEAD"))
	private static void mixInject(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<ItemStack> cir) {
		if (!itemStack2.isEmpty()) {
			if (itemStack.is(ModItems.PUFF_SPORES)) {

				if (getPuffSporesEffect(itemStack2, (count, compoundTag) -> {
					int i = Math.max(1, count + 1);
					compoundTag.putInt("PuffSporesEffect", i);
					compoundTag.putInt("Duration", calculateNewDuration(i));

				}) == 0) {
					MobEffectInstance instance = new MobEffectInstance(MobEffects.POISON, 0, 1) {
						@Override
						public CompoundTag save(CompoundTag compoundTag) {
							CompoundTag save = super.save(compoundTag);
							save.putInt("PuffSporesEffect", 1);
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
		return Math.min(60, i * 10) * 20;
	}

}
