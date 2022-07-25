package de.kxmischesdomi.mushroom.mixin;

import de.kxmischesdomi.mushroom.entity.GlowMushroomCow;
import de.kxmischesdomi.mushroom.entity.Glowfly;
import de.kxmischesdomi.mushroom.entity.ShroomPal;
import de.kxmischesdomi.mushroom.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(SpawnPlacements.class)
public abstract class SpawnPlacementsMixin {

	@Shadow
	protected static <T extends Mob> void register(EntityType<T> entityType, SpawnPlacements.Type type, Heightmap.Types types, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
	}

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void cinitInject(CallbackInfo ci) {
		register(ModEntities.SHROOM_PAL, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ShroomPal::checkShroomPalSpawnRules);
		register(ModEntities.PUFF_CREEPER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		register(ModEntities.GLOW_MOOSHROOM, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GlowMushroomCow::checkMushroomSpawnRules);
		register(ModEntities.GLOWFLY, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Glowfly::checkGlowflySpawnRules);
	}

}
