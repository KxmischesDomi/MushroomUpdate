package de.kxmischesdomi.mushroom.client.sounds;

import de.kxmischesdomi.mushroom.registry.ModItems;
import de.kxmischesdomi.mushroom.registry.ModSounds;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

public class GliderOnPlayerSoundInstance extends AbstractTickableSoundInstance {
    public static final int DELAY = 5;
    private final LocalPlayer player;
    private int time;

    public GliderOnPlayerSoundInstance(LocalPlayer localPlayer) {
        super(ModSounds.SHROOM_GLIDER_GLIDING, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = localPlayer;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.1f;
    }

    @Override
    public void tick() {
        ++this.time;
        boolean gliding = isGliding(player);
        if (this.player.isRemoved() || !gliding) {
            if (!isStopped() && this.time > DELAY) {
                player.playNotifySound(ModSounds.SHROOM_GLIDER_CLOSE, SoundSource.PLAYERS, 1, 1);
            }
            this.stop();
            return;
        }
        this.x = (float)this.player.getX();
        this.y = (float)this.player.getY();
        this.z = (float)this.player.getZ();
        float f = (float)this.player.getDeltaMovement().lengthSqr();
        this.volume = Mth.clamp(f, 0.0f, 1.0f);
        if (this.time < DELAY) {
            this.volume = 0.0f;
        } else if (this.time < DELAY*2) {
            this.volume *= (float)(this.time - DELAY) / DELAY;
        }
        float g = 0.8f;
        this.pitch = this.volume > g ? 1.0f + (this.volume - g) : 1.0f;
    }

    public static boolean isGliding(Player player) {
        return !player.isOnGround() && !player.getAbilities().flying && playerHasShroomGlider(player);
    }

    public static boolean playerHasShroomGlider(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.SHROOM_GLIDER);
    }

}