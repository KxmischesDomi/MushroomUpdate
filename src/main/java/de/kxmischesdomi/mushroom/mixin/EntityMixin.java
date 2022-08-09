package de.kxmischesdomi.mushroom.mixin;

import de.kxmischesdomi.mushroom.api.GlowColorable;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(Entity.class)
public abstract class EntityMixin implements GlowColorable {

	@Shadow @Final protected SynchedEntityData entityData;

	@Shadow public abstract Vec3 getLookAngle();

	private static final EntityDataAccessor<Integer> DATA_GLOWING_COLOR = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.INT);

	@Inject(method = "<init>", at = @At("TAIL"))
	public void defineSynchedDataInject(CallbackInfo ci) {
		if (isSupported()) {
			entityData.define(DATA_GLOWING_COLOR, 0x000000);
		}

	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tickInject(CallbackInfo ci) {
		if (isSupported()) {
			if (!getEntity().hasEffect(MobEffects.GLOWING) && hasGlowColor()) {
				setGlowColor(0x000000);
			}
		}
	}

	@Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
	public void getTeamColorInject(CallbackInfoReturnable<Integer> cir) {
		if (isSupported()) {
			if (getEntity().hasEffect(MobEffects.GLOWING) && hasGlowColor()) {
				cir.setReturnValue(getGlowColor());
			}
		}
	}

	@Override
	public int getGlowColor() {
		return entityData.get(DATA_GLOWING_COLOR);
	}

	@Override
	public void setGlowColor(int color) {
		entityData.set(DATA_GLOWING_COLOR, color);
	}

	public boolean isSupported() {
		return getEntity() != null;
	}

	@SuppressWarnings("ConstantConditions")
	public LivingEntity getEntity() {
		if (((Entity) (Object) this) instanceof LivingEntity livingEntity) {
			return livingEntity;
		}
		return null;
	}


}
