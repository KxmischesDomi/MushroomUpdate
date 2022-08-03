package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.trigger.ShroomPalConsumeTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.resources.ResourceLocation;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModCriteriaTriggers {

	public static final ShroomPalConsumeTrigger SHROOM_PAL_CONSUME = CriteriaTriggers.register(new ShroomPalConsumeTrigger());
	public static final PlayerTrigger FILLED_GLASS = CriteriaTriggers.register(new PlayerTrigger(new ResourceLocation(MushroomMod.MOD_ID, "filled_glass")));
	public static final PlayerTrigger CRASH_PARROT_LANDING = CriteriaTriggers.register(new PlayerTrigger(new ResourceLocation(MushroomMod.MOD_ID, "crash_parrot_landing")));

	public static void init() {}

}
