package de.kxmischesdomi.mushroom.block;

import de.kxmischesdomi.mushroom.block.entity.GlowflyGlassBlockEntity;
import de.kxmischesdomi.mushroom.registry.ModBlockEntities;
import de.kxmischesdomi.mushroom.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowflyLanternBlock extends LanternBlock implements EntityBlock {

	protected static final VoxelShape AABB = Shapes.or(Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0), Block.box(5.3, 7.0, 5.3, 10.7, 9.0, 10.7));
	protected static final VoxelShape HANGING_AABB = Shapes.or(Block.box(4.0, 1.0, 4.0, 12.0, 9.0, 12.0), Block.box(5.3, 8.0, 5.3, 10.7, 10.0, 10.7));

	public GlowflyLanternBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return Shapes.or(Block.box(4.0, 0.0, 4.0, 12.0, 12.0, 12.0), Block.box(5.3, 12.0, 5.3, 10.7, 14.0, 10.7));
	}

	@Override
	public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		if (blockEntity instanceof GlowflyGlassBlockEntity glowflyGlassBlockEntity) {
			if (glowflyGlassBlockEntity.hasHealingPower()) {
				if (randomSource.nextBoolean()) {
					double xRand = randomSource.nextDouble();
					double x = (double)blockPos.getX() + xRand / 2 + xRand / 2;
					double yRand= randomSource.nextDouble();
					double y = (double)blockPos.getY() + yRand / 2 + xRand / 2;
					double zRand = randomSource.nextDouble();
					double z = (double)blockPos.getZ() + zRand / 2 + xRand / 2;
					level.addParticle(ParticleTypes.GLOW, x, y, z, 0, 0, 0);
				}
			}
		}

	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new GlowflyGlassBlockEntity(blockPos, blockState);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
		if (level.getBlockEntity(blockPos) instanceof GlowflyGlassBlockEntity glassBlockEntity) {
			glassBlockEntity.load(itemStack.getOrCreateTag());
		}
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		if (blockEntity instanceof GlowflyGlassBlockEntity glassBlockEntity) {
			if (!level.isClientSide && player.isCreative() && !glassBlockEntity.saveWithoutMetadata().isEmpty()) {
				ItemStack itemStack = new ItemStack(ModItems.GLOWFLY_GLASS);
				blockEntity.saveToItem(itemStack);
				ItemEntity itemEntity = new ItemEntity(level, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, itemStack);
				itemEntity.setDefaultPickUpDelay();
				level.addFreshEntity(itemEntity);
			}
		}
		super.playerWillDestroy(level, blockPos, blockState, player);
	}

	@Override
	public void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
		ItemStack itemStack1 = new ItemStack(ModItems.GLOWFLY_GLASS);
		BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
		if (blockEntity instanceof GlowflyGlassBlockEntity glassBlockEntity) {
			glassBlockEntity.saveToItem(itemStack1);
		}
		Block.popResource(serverLevel, blockPos, itemStack1);
		super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
	}

	// Block Entity //


	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ModBlockEntities.GLOWFLY_GLASS_BLOCK_ENTITY, (level1, blockPos, blockState1, blockEntity) -> {
			if (blockEntity instanceof GlowflyGlassBlockEntity entity) {
				entity.tick();
			}
		});
	}

	@Override
	public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j) {
		super.triggerEvent(blockState, level, blockPos, i, j);
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		if (blockEntity == null) {
			return false;
		}
		return blockEntity.triggerEvent(i, j);
	}

	@Override
	@Nullable
	public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		return blockEntity instanceof MenuProvider ? (MenuProvider) blockEntity : null;
	}

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityType, BlockEntityType<E> blockEntityType2, BlockEntityTicker<A> blockEntityTicker) {
		return blockEntityType2 == blockEntityType ? blockEntityTicker : null;
	}

}
