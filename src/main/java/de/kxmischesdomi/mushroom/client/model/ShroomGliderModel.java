package de.kxmischesdomi.mushroom.client.model;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.item.ShroomGlider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ShroomGliderModel extends AnimatedGeoModel<ShroomGlider> {

	@Override
	public ResourceLocation getModelResource(ShroomGlider object) {
		return new ResourceLocation(MushroomMod.MOD_ID, "geo/shroom_glider.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ShroomGlider object) {
		return new ResourceLocation(MushroomMod.MOD_ID, "textures/armor/shroom_glider.png");

	}

	@Override
	public ResourceLocation getAnimationResource(ShroomGlider animatable) {
		return null;

	}

}
