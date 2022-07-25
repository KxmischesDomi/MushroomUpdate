package de.kxmischesdomi.mushroom.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.kxmischesdomi.mushroom.entity.GlowMushroomCow;
import de.kxmischesdomi.mushroom.registry.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

public class GlowMushroomCowMushroomLayer<T extends GlowMushroomCow> extends RenderLayer<T, CowModel<T>> {

    private final BlockRenderDispatcher blockRenderer;

    public GlowMushroomCowMushroomLayer(RenderLayerParent<T, CowModel<T>> renderLayerParent, BlockRenderDispatcher blockRenderDispatcher) {
        super(renderLayerParent);
        this.blockRenderer = blockRenderDispatcher;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T mushroomCow, float f, float g, float h, float j, float k, float l) {
        if ((mushroomCow).isBaby()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        boolean bl = minecraft.shouldEntityAppearGlowing(mushroomCow) && mushroomCow.isInvisible();
        if (mushroomCow.isInvisible() && !bl) {
            return;
        }
        BlockState blockState = ModBlocks.GLOW_MUSHROOM.defaultBlockState();
        int m = LivingEntityRenderer.getOverlayCoords(mushroomCow, 0.0f);
        BakedModel bakedModel = this.blockRenderer.getBlockModel(blockState);
        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-48.0f));
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.translate(-0.5, -0.5, -0.5);
        this.renderMushroomBlock(poseStack, multiBufferSource, i, bl, blockState, m, bakedModel);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(42.0f));
        poseStack.translate(0.1f, 0.0, -0.6f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-48.0f));
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.translate(-0.5, -0.5, -0.5);
        this.renderMushroomBlock(poseStack, multiBufferSource, i, bl, blockState, m, bakedModel);
        poseStack.popPose();
        poseStack.pushPose();
        this.getParentModel().getHead().translateAndRotate(poseStack);
        poseStack.translate(0.0, -0.7f, -0.2f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-78.0f));
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.translate(-0.5, -0.5, -0.5);
        this.renderMushroomBlock(poseStack, multiBufferSource, i, bl, blockState, m, bakedModel);
        poseStack.popPose();
    }

    private void renderMushroomBlock(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, boolean bl, BlockState blockState, int j, BakedModel bakedModel) {
        if (bl) {
            this.blockRenderer.getModelRenderer().renderModel(poseStack.last(), multiBufferSource.getBuffer(RenderType.outline(TextureAtlas.LOCATION_BLOCKS)), blockState, bakedModel, 0.0f, 0.0f, 0.0f, i, j);
        } else {
            this.blockRenderer.renderSingleBlock(blockState, poseStack, multiBufferSource, i, j);
        }
    }
}