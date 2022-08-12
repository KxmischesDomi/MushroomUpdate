package de.kxmischesdomi.mushroom.mixin;

import de.kxmischesdomi.mushroom.item.PuffSporesItem;
import de.kxmischesdomi.mushroom.registry.ModCriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(targets = "net/minecraft/world/inventory/BrewingStandMenu$PotionSlot")
public class PotionSlotMixin {

	@Inject(method = "onTake", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/advancements/critereon/BrewedPotionTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/alchemy/Potion;)V"))
	private void onTake(Player player, ItemStack itemStack, CallbackInfo ci) {
		if (player instanceof ServerPlayer serverPlayer) {
			if (PuffSporesItem.getPuffSporesEffect(itemStack, (integer, compoundTag) -> {}) > 0) {
				ModCriteriaTriggers.BREWED_PUFFED_POTION.trigger(serverPlayer);
			}
		}

	}

}
