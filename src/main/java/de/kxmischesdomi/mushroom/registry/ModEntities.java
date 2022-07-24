package de.kxmischesdomi.mushroom.registry;

import de.kxmischesdomi.mushroom.MushroomMod;
import de.kxmischesdomi.mushroom.entity.Glowfly;
import de.kxmischesdomi.mushroom.entity.ShroomPal;
import de.kxmischesdomi.mushroom.entity.PuffCreeper;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModEntities {

	public static final EntityType<ShroomPal> SHROOM_PAL = register("shroom_pal",
			FabricEntityTypeBuilder
					.create(MobCategory.CREATURE, ShroomPal::new)
					.dimensions(EntityDimensions.fixed(0.6f, 0.85f))
	);

	public static final EntityType<PuffCreeper> PUFF_CREEPER = register("puff_creeper",
			FabricEntityTypeBuilder
					.create(MobCategory.MONSTER, PuffCreeper::new)
					.dimensions(EntityDimensions.fixed(0.6f, 0.85f))
	);

	public static final EntityType<Glowfly> FIREFLY = register("glowfly",
			FabricEntityTypeBuilder
					.create(MobCategory.AMBIENT, Glowfly::new)
					.dimensions(EntityDimensions.fixed(0.1f, 0.05f))
	);


	private static <T extends Entity> EntityType<T> register(String name, FabricEntityTypeBuilder<T> builder) {
		return Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(MushroomMod.MOD_ID, name), builder.build());
	}

	public static void registerAttributes() {
		FabricDefaultAttributeRegistry.register(SHROOM_PAL, ShroomPal.createAttributes());
		FabricDefaultAttributeRegistry.register(PUFF_CREEPER, PuffCreeper.createAttributes());
		FabricDefaultAttributeRegistry.register(FIREFLY, Glowfly.createAttributes());
	}

}
