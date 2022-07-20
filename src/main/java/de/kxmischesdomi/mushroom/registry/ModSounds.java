package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.MushroomMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModSounds {

	public static final SoundEvent PUFF_CREEPER_DEATH = register("puff_creeper_death");
	public static final SoundEvent PUFF_CREEPER_HURT = register("puff_creeper_hurt");
	public static final SoundEvent PUFF_CREEPER_PUFF = register("puff_creeper_puff");

	private static SoundEvent register(String name) {
		ResourceLocation location = new ResourceLocation(MushroomMod.MOD_ID, name);
		return Registry.register(Registry.SOUND_EVENT, location, new SoundEvent(location));
	}

	public static void init() { }

}
