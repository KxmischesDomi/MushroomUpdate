package de.kxmischesdomi.mushroom.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.entity.PuffCreeper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class PuffCreeperArmorLayer extends GeoLayerRenderer<PuffCreeper> {

	private static final ResourceLocation POWER_LOCATION = new ResourceLocation(MushroomMod.MOD_ID, "textures/entity/puff_creeper_armor.png");

	public PuffCreeperArmorLayer(IGeoRenderer<PuffCreeper> entityRendererIn) {
		super(entityRendererIn);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, PuffCreeper entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!(entity).isPowered()) {
			return;
		}
		poseStack.pushPose();
		poseStack.scale(1.1f, 1.1f, 1.1f);
		float m = (float)(entity).tickCount + partialTicks;
		RenderType type = RenderType.energySwirl(POWER_LOCATION, this.xOffset(m) % 1.0f, m * 0.01f % 1.0f);
		VertexConsumer vertexbuilder = bufferIn.getBuffer(type);

		GeoModel model = getEntityModel().getModel(getEntityModel().getModelResource(entity));
		this.getRenderer().render(model, entity, partialTicks, type, poseStack, bufferIn, vertexbuilder,
				packedLightIn, OverlayTexture.NO_OVERLAY, 0.5f, 0.5f, 0.5f, 1.0F);

		poseStack.popPose();
	}

	protected float xOffset(float f) {
		return f * 0.01f;
	}

}
