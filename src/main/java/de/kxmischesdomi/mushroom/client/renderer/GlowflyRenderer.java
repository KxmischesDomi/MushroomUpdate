package de.kxmischesdomi.mushroom.client.renderer;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.client.model.GlowflyModel;
import de.kxmischesdomi.mushroom.entity.Glowfly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowflyRenderer extends GeoEntityRenderer<Glowfly> {

	public static ResourceLocation TEXTURE = new ResourceLocation(MushroomMod.MOD_ID, "textures/entity/glowfly.png");

	public GlowflyRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new GlowflyModel());
	}

	/**
	 * @return the firefly's texture location
	 */
	@Override
	public ResourceLocation getTextureLocation(Glowfly entity) {
		return TEXTURE;
	}

	@Override
	protected int getBlockLightLevel(Glowfly entity, BlockPos blockPos) {
		return entity.hasHealingPower() ? 15 : super.getBlockLightLevel(entity, blockPos);
	}

}
