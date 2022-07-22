package de.kxmischesdomi.mushroom.client.model;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.client.renderer.FireflyRenderer;
import de.kxmischesdomi.mushroom.entity.Firefly;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class FireflyModel extends AnimatedGeoModel<Firefly> {

	@Override
	public ResourceLocation getModelResource(Firefly object) {
		return new ResourceLocation(MushroomMod.MOD_ID, "geo/firefly.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(Firefly object) {
		return FireflyRenderer.TEXTURE;
	}

	@Override
	public ResourceLocation getAnimationResource(Firefly animatable) {
		return new ResourceLocation(MushroomMod.MOD_ID, "animations/firefly.animation.json");
	}

}
