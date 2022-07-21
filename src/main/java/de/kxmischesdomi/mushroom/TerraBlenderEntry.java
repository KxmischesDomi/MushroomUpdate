package de.kxmischesdomi.mushroom;

import de.kxmischesdomi.mushroom.worldgen.MushroomCavesRegion;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class TerraBlenderEntry implements TerraBlenderApi {

	@Override
	public void onTerraBlenderInitialized() {
		Regions.register(new MushroomCavesRegion(new ResourceLocation(MushroomMod.MOD_ID, "overworld"), 1));
	}

}
