package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.MushroomMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModStats {

	public static final ResourceLocation GLOWFLY_HEALTH_GAINED = makeCustomStat("glowfly_health_gained", StatFormatter.DIVIDE_BY_TEN);

	private static ResourceLocation makeCustomStat(String string, StatFormatter statFormatter) {
		ResourceLocation resourceLocation = new ResourceLocation(MushroomMod.MOD_ID, string);
		Registry.register(Registry.CUSTOM_STAT, string, resourceLocation);
		Stats.CUSTOM.get(resourceLocation, statFormatter);
		return resourceLocation;
	}

	public static void init() { }

}
