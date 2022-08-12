package de.kxmischesdomi.mushroom.item;

import de.kxmischesdomi.mushroom.api.GlowColorable;
import de.kxmischesdomi.mushroom.entity.GlowMushroomCow;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
	public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {
		player.setItemInHand(interactionHand, new ItemStack(Items.BOWL));
		finishUsingItem(itemStack, livingEntity.level, livingEntity);
		return InteractionResult.SUCCESS;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
		livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 600, 0));
		if (hasGlowColor(itemStack)) {
			GlowColorable colorable = (GlowColorable) livingEntity;
			if (colorable.hasGlowColor()) {
				GlowMushroomCow.dyeGlowColor(colorable, getGlowColor(itemStack));
			} else {
				int glowColor = getGlowColor(itemStack);
				colorable.setGlowColor(glowColor);
			}
		}
		return super.finishUsingItem(itemStack, level, livingEntity);
	}

	public static boolean hasGlowColor(ItemStack itemStack) {
		return getGlowColor(itemStack) != 0x000000;
	}

	public static void setGlowColor(ItemStack itemStack, int glowColor) {
		CompoundTag tag = itemStack.getOrCreateTag();
		tag.putInt("GlowColor", glowColor);
	}

	public static int getGlowColor(ItemStack itemStack) {
		CompoundTag tag = itemStack.getTag();
		if (tag == null || !tag.contains("GlowColor")) {
			return 0x000000;
		}
		return tag.getInt("GlowColor");
	}

}
