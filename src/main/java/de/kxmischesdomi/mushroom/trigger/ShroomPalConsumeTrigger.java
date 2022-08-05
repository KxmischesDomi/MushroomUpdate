package de.kxmischesdomi.mushroom.trigger;

import com.google.gson.JsonObject;
import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.entity.ShroomPal;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ShroomPalConsumeTrigger extends SimpleCriterionTrigger<ShroomPalConsumeTrigger.TriggerInstance> {

	static final ResourceLocation ID = new ResourceLocation(MushroomMod.MOD_ID, "shroom_pal_consume");

	@Override
	protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite composite, DeserializationContext deserializationContext) {
		EntityPredicate.Composite composite2 = EntityPredicate.Composite.fromJson(jsonObject, "entity", deserializationContext);
		return new TriggerInstance(composite, composite2);
	}

	public void trigger(ServerPlayer player, ShroomPal pal) {
		LootContext lootContext = EntityPredicate.createContext(player, pal);
		this.trigger(player, (triggerInstance) -> triggerInstance.matches(lootContext));
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {

		private final EntityPredicate.Composite shroomPal;

		public TriggerInstance(EntityPredicate.Composite composite, EntityPredicate.Composite composite2) {
			super(ID, composite);
			this.shroomPal = composite2;
		}

		public boolean matches(LootContext lootContext) {
			return this.shroomPal.matches(lootContext);
		}

		@Override
		public JsonObject serializeToJson(SerializationContext serializationContext) {
			JsonObject jsonObject = super.serializeToJson(serializationContext);
			jsonObject.add("entity", this.shroomPal.toJson(serializationContext));
			return jsonObject;
		}

	}

}
