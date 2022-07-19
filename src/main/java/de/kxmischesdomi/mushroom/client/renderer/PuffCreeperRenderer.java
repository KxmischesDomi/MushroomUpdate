package de.kxmischesdomi.mushroom.client.renderer;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.client.model.PuffCreeperModel;
import de.kxmischesdomi.mushroom.entity.PuffCreeper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.UUID;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class PuffCreeperRenderer extends GeoEntityRenderer<PuffCreeper> {

	public PuffCreeperRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new PuffCreeperModel());
	}

	@Override
	public ResourceLocation getTextureLocation(PuffCreeper entity) {
		return new ResourceLocation(MushroomMod.MOD_ID, "textures/entity/puff_creeper.png");
	}

	public static void initPuffReceiver() {
		ClientPlayNetworking.registerGlobalReceiver(new ResourceLocation(MushroomMod.MOD_ID, "puff"), (client, handler, buf, responseSender) -> {
			UUID uuid = buf.readUUID();
			client.execute(() -> {
				for (Entity entity : client.level.entitiesForRendering()) {
					if (entity.getUUID().equals(uuid) && entity instanceof PuffCreeper puffCreeper) {
						puffCreeper.playPuffAnimation = true;
					}
				}
			});
		});
	}

}
