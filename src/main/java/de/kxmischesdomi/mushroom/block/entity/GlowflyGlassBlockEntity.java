package de.kxmischesdomi.mushroom.block.entity;

import de.kxmischesdomi.mushroom.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowflyGlassBlockEntity extends BlockEntity {

	CompoundTag glowflyNbt;

	public GlowflyGlassBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.GLOWFLY_GLASS_BLOCK_ENTITY, blockPos, blockState);
	}

	@Override
	public void load(CompoundTag compoundTag) {
		this.glowflyNbt = compoundTag;
	}

	@Override
	protected void saveAdditional(CompoundTag compoundTag) {
		if (glowflyNbt != null) {
			compoundTag.merge(this.glowflyNbt);
		}
	}

	@Override
	public void saveToItem(ItemStack itemStack) {
		BlockItem.setBlockEntityData(itemStack, this.getType(), new CompoundTag());
		saveAdditional(itemStack.getOrCreateTag());
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return saveWithoutMetadata();
	}

}
