package de.kxmischesdomi.mushroom.client.renderer;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.client.model.ShroomPalModel;
import de.kxmischesdomi.mushroom.entity.ShroomPal;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ShroomPalRenderer extends GeoEntityRenderer<ShroomPal> {

	public ShroomPalRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new ShroomPalModel());
	}

	@Override
	public ResourceLocation getTextureLocation(ShroomPal entity) {
		return new ResourceLocation(MushroomMod.MOD_ID, "textures/entity/redshroom.png");
	}

}
