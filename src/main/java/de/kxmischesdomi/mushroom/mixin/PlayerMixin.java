package de.kxmischesdomi.mushroom.mixin;

import de.kxmischesdomi.mushroom.registry.ModCriteriaTriggers;
import de.kxmischesdomi.mushroom.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(Player.class)
public class PlayerMixin {

	@Inject(method = "travel", at = @At("HEAD"))
	public void travelInject(Vec3 vec3, CallbackInfo ci) {
		if ((Object) this instanceof ServerPlayer player) {
			ItemStack item = player.getItemBySlot(EquipmentSlot.HEAD);
			if (item.is(ModItems.SHROOM_GLIDER)) {
				if (!player.isOnGround() && item.getMaxDamage() - item.getDamageValue() < 5) {
					if (isParrot(player.getShoulderEntityLeft()) || isParrot(player.getShoulderEntityRight())) {
						ModCriteriaTriggers.CRASH_PARROT_LANDING.trigger(player);
					}
				}
			}
		}
	}

	private boolean isParrot(CompoundTag nbt) {
		return EntityType.byString(nbt.getString("id")).filter(entityType -> entityType == EntityType.PARROT).isPresent();
	}

}
