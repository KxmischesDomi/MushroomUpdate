package de.kxmischesdomi.mushroom.client.model;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.client.renderer.GlowflyRenderer;
import de.kxmischesdomi.mushroom.entity.Glowfly;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowflyModel extends AnimatedGeoModel<Glowfly> {

	@Override
	public ResourceLocation getModelResource(Glowfly object) {
		return new ResourceLocation(MushroomMod.MOD_ID, "geo/glowfly.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(Glowfly object) {
		return GlowflyRenderer.TEXTURE;
	}

	@Override
	public ResourceLocation getAnimationResource(Glowfly animatable) {
		return new ResourceLocation(MushroomMod.MOD_ID, "animations/glowfly.animation.json");
	}

}
