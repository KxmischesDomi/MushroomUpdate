package de.kxmischesdomi.mushroom.item;

import de.kxmischesdomi.mushroom.registry.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ShroomGlider extends ArmorItem implements IAnimatable {

	private final AnimationFactory factory = new AnimationFactory(this);

	public ShroomGlider(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Properties properties) {
		super(armorMaterial, equipmentSlot, properties);
	}

	@Override
	public void registerControllers(AnimationData animationData) {

	}

	@Nullable
	@Override
	public SoundEvent getEquipSound() {
		return ModSounds.SHROOM_GLIDER_EQUIP;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

}
