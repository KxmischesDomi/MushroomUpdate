package de.kxmischesdomi.mushroom.client.model;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.entity.PuffCreeper;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class PuffCreeperModel extends AnimatedGeoModel<PuffCreeper> {

	@Override
	public ResourceLocation getModelResource(PuffCreeper object) {
		return new ResourceLocation(MushroomMod.MOD_ID, "geo/puff_creeper.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(PuffCreeper object) {
		return new ResourceLocation(MushroomMod.MOD_ID, "textures/entity/puff_creeper.png");
	}

	@Override
	public ResourceLocation getAnimationResource(PuffCreeper animatable) {
		return new ResourceLocation(MushroomMod.MOD_ID, "animations/puff_creeper.animation.json");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setLivingAnimations(PuffCreeper entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
			head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
		}
	}

}
