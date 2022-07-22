package de.kxmischesdomi.mushroom.client.renderer;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.client.model.FireflyModel;
import de.kxmischesdomi.mushroom.entity.Firefly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class FireflyRenderer extends GeoEntityRenderer<Firefly> {

	public static ResourceLocation TEXTURE = new ResourceLocation(MushroomMod.MOD_ID, "textures/entity/firefly.png");

	public FireflyRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new FireflyModel());
	}

	@Override
	public ResourceLocation getTextureLocation(Firefly entity) {
		return TEXTURE;
	}

	@Override
	protected int getBlockLightLevel(Firefly entity, BlockPos blockPos) {
		return 15;
	}

}
