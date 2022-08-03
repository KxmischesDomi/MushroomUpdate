package de.kxmischesdomi.mushroom.trigger;

import com.google.gson.JsonObject;
import de.kxmischesdomi.mushroom.MushroomMod;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FilledGlassTrigger extends SimpleCriterionTrigger<FilledGlassTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation(MushroomMod.MOD_ID, "filled_glass");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite composite, DeserializationContext deserializationContext) {
        return new TriggerInstance(composite);
    }

    public void trigger(ServerPlayer serverPlayer) {
        this.trigger(serverPlayer, triggerInstance -> true);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(EntityPredicate.Composite composite) {
            super(ID, composite);
        }

    }

}