package de.kxmischesdomi.mushroom.block.entity;

import de.kxmischesdomi.mushroom.core.IGlowfly;
import de.kxmischesdomi.mushroom.registry.ModBlockEntities;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowflyGlassBlockEntity extends BlockEntity implements IGlowfly {

	int healingCooldown = 0;

	CompoundTag glowflyNbt;

	public GlowflyGlassBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.GLOWFLY_GLASS_BLOCK_ENTITY, blockPos, blockState);
	}

	public void tick() {
		tickCooldownHealing();

		if (level instanceof ServerLevel serverLevel) {
			if (hasHealingPower()) {
				Vec3 pos = new Vec3(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5);
				checkForMobHealing(serverLevel, pos);
				if (!hasHealingPower()) {
					level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
				}
			}

		}
	}

	@Override
	public boolean hasHealingPower() {
		return healingCooldown == 0;
	}

	@Override
	public int getHealingCooldown() {
		return healingCooldown;
	}

	@Override
	public void setHealingCooldown(int cooldown) {
		this.healingCooldown = cooldown;

	}

	@Override
	public Collection<ServerPlayer> getTrackingPlayers() {
		return PlayerLookup.tracking(this);
	}

	@Override
	public void load(CompoundTag compoundTag) {
		this.glowflyNbt = compoundTag.copy();
		// Remove nbt that minecraft adds after the chunk was reloaded
		glowflyNbt.remove("id");
		glowflyNbt.remove("keepPacked");
		glowflyNbt.remove("x");
		glowflyNbt.remove("y");
		glowflyNbt.remove("z");

		if (compoundTag.contains("HealingCooldown")) {
			this.healingCooldown = compoundTag.getInt("HealingCooldown");
			glowflyNbt.remove("HealingCooldown");
		}
	}

	@Override
	protected void saveAdditional(CompoundTag compoundTag) {
		if (glowflyNbt != null) {
			compoundTag.merge(this.glowflyNbt);
		}
		saveGlowflyHealing(compoundTag);
	}

	private void saveGlowflyHealing(CompoundTag compoundTag) {
		if (!hasHealingPower()) {
			compoundTag.putInt("HealingCooldown", this.healingCooldown);
		}
	}

	@Override
	public void saveToItem(ItemStack itemStack) {
		saveAdditional(itemStack.getOrCreateTag());
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag compoundTag = new CompoundTag();
		saveGlowflyHealing(compoundTag);
		return compoundTag;
	}

}
