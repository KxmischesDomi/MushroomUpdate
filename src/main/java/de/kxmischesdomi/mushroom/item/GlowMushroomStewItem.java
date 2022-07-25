package de.kxmischesdomi.mushroom.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowMushroomStewItem extends BowlFoodItem {

	public GlowMushroomStewItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
		livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 600, 0));
		return super.finishUsingItem(itemStack, level, livingEntity);
	}

}
