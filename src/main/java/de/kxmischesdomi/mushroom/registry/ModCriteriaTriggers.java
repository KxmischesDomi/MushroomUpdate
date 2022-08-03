package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.trigger.FilledGlassTrigger;
import de.kxmischesdomi.mushroom.trigger.ShroomPalConsumeTrigger;
import net.minecraft.advancements.CriteriaTriggers;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModCriteriaTriggers {

	public static final ShroomPalConsumeTrigger SHROOM_PAL_CONSUME = CriteriaTriggers.register(new ShroomPalConsumeTrigger());
	public static final FilledGlassTrigger FILLED_GLASS = CriteriaTriggers.register(new FilledGlassTrigger());

	public static void init() {}

}
