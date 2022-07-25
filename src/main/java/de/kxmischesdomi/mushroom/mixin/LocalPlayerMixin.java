package de.kxmischesdomi.mushroom.mixin;

import de.kxmischesdomi.mushroom.client.sounds.GliderOnPlayerSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

	@Shadow @Final protected Minecraft minecraft;

	private GliderOnPlayerSoundInstance gliderOnPlayerSoundInstance;

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo ci) {
		if (GliderOnPlayerSoundInstance.isGliding((LocalPlayer) (Object) this)) {
			if (gliderOnPlayerSoundInstance == null || gliderOnPlayerSoundInstance.isStopped()) {
				gliderOnPlayerSoundInstance = new GliderOnPlayerSoundInstance((LocalPlayer) (Object) this);
			}

			if (!this.minecraft.getSoundManager().isActive(gliderOnPlayerSoundInstance)) {
				this.minecraft.getSoundManager().play(gliderOnPlayerSoundInstance);
			}
		}
	}

}
