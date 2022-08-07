package de.kxmischesdomi.mushroom;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import software.bernie.example.GeckoLibMod;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class MushroomPreLaunch implements PreLaunchEntrypoint {

	@Override
	public void onPreLaunch() {
		GeckoLibMod.DISABLE_IN_DEV = true;
	}

}
