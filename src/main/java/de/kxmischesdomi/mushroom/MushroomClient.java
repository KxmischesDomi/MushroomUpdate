package de.kxmischesdomi.mushroom;

import de.kxmischesdomi.mushroom.client.renderer.GlowflyRenderer;
import de.kxmischesdomi.mushroom.client.renderer.PuffCreeperRenderer;
import de.kxmischesdomi.mushroom.client.renderer.ShroomGliderRenderer;
import de.kxmischesdomi.mushroom.client.renderer.ShroomPalRenderer;
import de.kxmischesdomi.mushroom.registry.ModBlocks;
import de.kxmischesdomi.mushroom.registry.ModEntities;
import de.kxmischesdomi.mushroom.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class MushroomClient implements ClientModInitializer {


	@Override
	public void onInitializeClient() {

		EntityRendererRegistry.register(ModEntities.SHROOM_PAL, ShroomPalRenderer::new);
		EntityRendererRegistry.register(ModEntities.FIREFLY, GlowflyRenderer::new);
		EntityRendererRegistry.register(ModEntities.PUFF_CREEPER, PuffCreeperRenderer::new);
		PuffCreeperRenderer.initPuffReceiver();

		GeoArmorRenderer.registerArmorRenderer(new ShroomGliderRenderer(), ModItems.SHROOM_GLIDER);

		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.GLOW_MUSHROOM);
	}

}
