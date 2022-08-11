package de.kxmischesdomi.mushroom.mixin;

import de.kxmischesdomi.mushroom.api.GlowColorable;
import de.kxmischesdomi.mushroom.entity.GlowMushroomCow;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

	@Shadow public abstract boolean isCurrentlyGlowing();

	private static final EntityDataAccessor<Integer> DATA_GLOWING_COLOR = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.INT);

	@Inject(method = "<init>", at = @At("TAIL"))
	public void defineSynchedDataInject(CallbackInfo ci) {
		if (isSupported()) {
			entityData.define(DATA_GLOWING_COLOR, isGlowMooshroom() ? 0xFFFFFF : 0x000000);
		}
	}

	@Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
	public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfoReturnable<CompoundTag> cir) {
		if (isSupported()) {
			if (hasGlowColor()) {
				compoundTag.putInt("GlowColor", getGlowColor());
			}
		}
	}

	@Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
	public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
		if (isSupported()) {
			if (compoundTag.contains("GlowColor", 3)) {
				setGlowColor(compoundTag.getInt("GlowColor"));
			}
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tickInject(CallbackInfo ci) {
		if (isSupported()) {
			if (!getEntity().hasEffect(MobEffects.GLOWING) && hasGlowColor() && !isGlowMooshroom()) {
				setGlowColor(0x000000);
			}
		}
	}

	@Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
	public void getTeamColorInject(CallbackInfoReturnable<Integer> cir) {
		if (isSupported()) {
			if (isCurrentlyGlowing() && hasGlowColor()) {
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

	public boolean isGlowMooshroom() {
		return getEntity() instanceof GlowMushroomCow;
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
