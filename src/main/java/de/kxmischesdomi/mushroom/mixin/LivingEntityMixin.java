package de.kxmischesdomi.mushroom.mixin;

import de.kxmischesdomi.mushroom.registry.ModItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	/**
	 * Checks if the entity is wearing a shroom glider and if so, changes the item stack's durability and returns true.
	 * @return true if the entity is wearing a shroom glider.
	 */
	@Inject(method = "hasEffect", at = @At("HEAD"), cancellable = true)
	public void hasEffectInject(MobEffect mobEffect, CallbackInfoReturnable<Boolean> cir) {
		if (mobEffect == MobEffects.SLOW_FALLING) {
			for (ItemStack item : getArmorSlots()) {
				if (item.is(ModItems.SHROOM_GLIDER)) {
					if (!this.onGround) {
						item.hurtAndBreak(1, (LivingEntity) (Object) this, entity -> entity.broadcastBreakEvent(entity.getUsedItemHand()));
					} else {
						item.setDamageValue(Math.max(item.getDamageValue() - 1, 0));
					}
					cir.setReturnValue(true);
				}
			}

		}
	}

}
