package de.kxmischesdomi.mushroom.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.function.BiConsumer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class PuffSporesItem extends Item {

	public static String TAG_PUFF_SPORES_EFFECT = "PuffSporesEffect";

	public PuffSporesItem(Properties properties) {
		super(properties);
	}

	public static int getPuffSporesEffect(ItemStack itemStack, BiConsumer<Integer, CompoundTag> tagConsumer) {
		CompoundTag tag = itemStack.getTag();
		if (tag == null || !tag.contains(PotionUtils.TAG_CUSTOM_POTION_EFFECTS, 9)) return 0;
		ListTag list = tag.getList(PotionUtils.TAG_CUSTOM_POTION_EFFECTS, 10);

		for (int i = 0; i < list.size(); i++) {
			CompoundTag compound = list.getCompound(i);
			if (compound.contains(TAG_PUFF_SPORES_EFFECT)) {
				int count = compound.getInt("PuffSporesEffect");
				tagConsumer.accept(count, compound);
				return count;
			}
		}
		return 0;
	}

}
