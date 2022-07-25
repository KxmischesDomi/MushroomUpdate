package de.kxmischesdomi.mushroom.client.renderer;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.client.model.GlowMushroomCowMushroomLayer;
import de.kxmischesdomi.mushroom.entity.GlowMushroomCow;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowMushroomCowRenderer extends MobRenderer<GlowMushroomCow, CowModel<GlowMushroomCow>> {

	public GlowMushroomCowRenderer(EntityRendererProvider.Context context) {
		super(context, new CowModel<>(context.bakeLayer(ModelLayers.MOOSHROOM)), 0.7f);
		this.addLayer(new GlowMushroomCowMushroomLayer<>(this, context.getBlockRenderDispatcher()));
	}

	/**
	 * @return the cow's texture location
	 */
	@Override
	public ResourceLocation getTextureLocation(GlowMushroomCow mushroomCow) {
		return new ResourceLocation(MushroomMod.MOD_ID, "textures/entity/glow_mooshroom.png");
	}

	@Override
	protected int getBlockLightLevel(GlowMushroomCow entity, BlockPos blockPos) {
		return 15;
	}

}