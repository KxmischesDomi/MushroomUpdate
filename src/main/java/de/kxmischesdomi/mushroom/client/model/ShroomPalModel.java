package de.kxmischesdomi.mushroom.client.model;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.entity.ShroomPal;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ShroomPalModel extends AnimatedGeoModel<ShroomPal> {

	@Override
	public ResourceLocation getModelResource(ShroomPal object) {
		return new ResourceLocation(MushroomMod.MOD_ID, String.format("geo/%s.geo.json", getName(object)));
	}

	@Override
	public ResourceLocation getTextureResource(ShroomPal object) {
		return new ResourceLocation(MushroomMod.MOD_ID, String.format("textures/entity/%s.png", getName(object)));
	}

	@Override
	public ResourceLocation getAnimationResource(ShroomPal animatable) {
		return new ResourceLocation(MushroomMod.MOD_ID, String.format("animations/%s.animation.json", getName(animatable)));
	}

	public static String getName(ShroomPal object) {
		return String.format("%sshroom%s", object.isBrownMushroom() ? "brown" : "red", object.isBig() ? "": "_baby");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setLivingAnimations(ShroomPal entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
			head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
		}
	}

}
