package de.kxmischesdomi.mushroom;

import de.kxmischesdomi.mushroom.client.renderer.*;
import de.kxmischesdomi.mushroom.registry.ModBlocks;
import de.kxmischesdomi.mushroom.registry.ModEntities;
import de.kxmischesdomi.mushroom.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class MushroomClient implements ClientModInitializer {


	@Override
	public void onInitializeClient() {

		FabricLoader.getInstance().getModContainer(MushroomMod.MOD_ID).ifPresent(modContainer -> {
			ResourceManagerHelper.registerBuiltinResourcePack(new ResourceLocation(MushroomMod.MOD_ID, "barebones"), modContainer, ResourcePackActivationType.NORMAL);
		});

		EntityRendererRegistry.register(ModEntities.SHROOM_PAL, ShroomPalRenderer::new);
		EntityRendererRegistry.register(ModEntities.GLOWFLY, GlowflyRenderer::new);
		EntityRendererRegistry.register(ModEntities.GLOW_MOOSHROOM, GlowMushroomCowRenderer::new);
		EntityRendererRegistry.register(ModEntities.PUFF_CREEPER, PuffCreeperRenderer::new);
		PuffCreeperRenderer.initPuffReceiver();

		GeoArmorRenderer.registerArmorRenderer(new ShroomGliderRenderer(), ModItems.SHROOM_GLIDER);

		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.GLOW_MUSHROOM,  ModBlocks.POTTED_GLOW_MUSHROOM, ModBlocks.GLOWFLY_GLASS);
	}

	public static int getColor(Entity entity) {
		int m = 25;
		int n = entity.tickCount / m + entity.getId();
		int o = DyeColor.values().length;
		int p = n % o;
		int q = (n + 1) % o;
		float r = ((float) (entity.tickCount % 25)) / 25.0f;
		float[] fs = Sheep.getColorArray(DyeColor.byId(p));
		float[] gs = Sheep.getColorArray(DyeColor.byId(q));
		float s = fs[0] * (1.0f - r) + gs[0] * r;
		float t = fs[1] * (1.0f - r) + gs[1] * r;
		float u = fs[2] * (1.0f - r) + gs[2] * r;
		// turn s, t and u into a hex code with full brightness
		int hex = 255 | ((int) (s * 255.0f) << 16) | ((int) (t * 255.0f) << 8) | (int) (u * 255.0f);
		return hex;
	}

}
