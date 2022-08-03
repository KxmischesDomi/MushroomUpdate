package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.block.entity.GlowflyGlassBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModBlockEntities {

	public static final BlockEntityType<GlowflyGlassBlockEntity> GLOWFLY_GLASS_BLOCK_ENTITY = register("glowfly_glass",
			FabricBlockEntityTypeBuilder.create(GlowflyGlassBlockEntity::new, ModBlocks.GLOWFLY_GLASS)
	);

	private static <T extends BlockEntity> BlockEntityType<T> register(String string, FabricBlockEntityTypeBuilder<T> builder) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, string, builder.build());
	}

	public static void init() { }

}
