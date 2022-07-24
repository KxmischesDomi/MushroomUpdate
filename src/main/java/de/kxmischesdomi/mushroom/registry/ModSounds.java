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

	public static final SoundEvent SHROOM_PAL_DEATH = register("shroom_pal_death");
	public static final SoundEvent SHROOM_PAL_DEATH_BIG = register("shroom_pal_death_big");
	public static final SoundEvent SHROOM_PAL_HURT = register("shroom_pal_hurt");
	public static final SoundEvent SHROOM_PAL_HURT_BIG = register("shroom_pal_hurt_big");
	public static final SoundEvent SHROOM_PAL_HARVEST = register("shroom_pal_harvest");
	public static final SoundEvent SHROOM_PAL_HARVEST_BIG = register("shroom_pal_harvest_big");
	public static final SoundEvent SHROOM_PAL_AMBIENT = register("shroom_pal_ambient");
	public static final SoundEvent SHROOM_PAL_AMBIENT_BIG = register("shroom_pal_ambient_big");
	public static final SoundEvent SHROOM_PAL_STEP = register("shroom_pal_step");
	public static final SoundEvent SHROOM_PAL_STEP_BIG = register("shroom_pal_step_big");
	public static final SoundEvent SHROOM_PAL_GROW = register("shroom_pal_grow");

	private static SoundEvent register(String name) {
		ResourceLocation location = new ResourceLocation(MushroomMod.MOD_ID, name);
		return Registry.register(Registry.SOUND_EVENT, location, new SoundEvent(location));
	}

	public static void init() { }

}
