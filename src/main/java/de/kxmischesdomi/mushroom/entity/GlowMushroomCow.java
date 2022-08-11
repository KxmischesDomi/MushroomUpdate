package de.kxmischesdomi.mushroom.entity;

import de.kxmischesdomi.mushroom.api.GlowColorable;
import de.kxmischesdomi.mushroom.item.GlowMushroomStewItem;
import de.kxmischesdomi.mushroom.registry.ModEntities;
import de.kxmischesdomi.mushroom.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;
import java.util.Optional;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GlowMushroomCow extends Cow implements Shearable {

	public static final Map<Item, DyeColor> FLOWER_COLORS = Map.ofEntries(
			Map.entry(Items.POPPY, DyeColor.RED),
			Map.entry(Items.RED_TULIP, DyeColor.RED),
			Map.entry(Items.ORANGE_TULIP, DyeColor.ORANGE),
			Map.entry(Items.WHITE_TULIP, DyeColor.WHITE),
			Map.entry(Items.PINK_TULIP, DyeColor.PINK),
			Map.entry(Items.DANDELION, DyeColor.YELLOW),
			Map.entry(Items.OXEYE_DAISY, DyeColor.WHITE),
			Map.entry(Items.AZURE_BLUET, DyeColor.LIGHT_GRAY),
			Map.entry(Items.ALLIUM, DyeColor.PURPLE),
			Map.entry(Items.BLUE_ORCHID, DyeColor.CYAN),
			Map.entry(Items.CORNFLOWER, DyeColor.BLUE),
			Map.entry(Items.LILY_OF_THE_VALLEY, DyeColor.WHITE),
			Map.entry(Items.WITHER_ROSE, DyeColor.BLACK),
			Map.entry(Items.LILAC, DyeColor.MAGENTA),
			Map.entry(Items.ROSE_BUSH, DyeColor.RED),
			Map.entry(Items.PEONY, DyeColor.PINK),
			Map.entry(Items.FERN, DyeColor.GREEN),
			Map.entry(Items.LARGE_FERN, DyeColor.GREEN),
			Map.entry(Items.GRASS, DyeColor.GREEN),
			Map.entry(Items.TALL_GRASS, DyeColor.GREEN),
			Map.entry(Items.DEAD_BUSH, DyeColor.BROWN)
	);

	public GlowMushroomCow(EntityType<? extends Cow> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
		if (levelReader.getBlockState(blockPos.below()).is(Blocks.MYCELIUM)) {
			return 10.0f;
		}
		return levelReader.getPathfindingCostFromLightLevels(blockPos);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (random.nextFloat() > 0.7F) {
			this.level.addParticle(ParticleTypes.GLOW, this.getRandomX(0.8), this.getRandomY(), this.getRandomZ(0.8), 0.0, 0.0, 0.0);
		}
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
		ItemStack itemStack = player.getItemInHand(interactionHand);
		if (itemStack.is(Items.BOWL) && !this.isBaby()) {

			ItemStack itemStack2 = new ItemStack(ModItems.GLOW_MUSHROOM_STEW);
			GlowMushroomStewItem.setGlowColor(itemStack2, ((GlowColorable) this).getGlowColor());
			ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
			player.setItemInHand(interactionHand, itemStack3);

			this.playSound(SoundEvents.MOOSHROOM_MILK, 1.0f, 1.0f);
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}
		if (itemStack.is(Items.SHEARS) && this.readyForShearing()) {
			this.shear(SoundSource.PLAYERS);
			this.gameEvent(GameEvent.SHEAR, player);
			if (!this.level.isClientSide) {
				itemStack.hurtAndBreak(1, player, player2 -> player2.broadcastBreakEvent(interactionHand));
			}
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}
		Optional<DyeColor> colorOptional = getColorFromFlower(itemStack.getItem());
		if (colorOptional.isPresent()) {
			DyeColor color = colorOptional.get();
			boolean different = dyeGlowColor(((GlowColorable) this), color);
			if (different) {
				usePlayerItem(player, interactionHand, itemStack);
				handleFoodConsume();
				for (int i = 0; i < 2; ++i) {
					this.level.addParticle(ParticleTypes.SMOKE, this.getX() + this.random.nextDouble() / 2.0, this.getY(0.5), this.getZ() + this.random.nextDouble() / 2.0, 0.0, this.random.nextDouble() / 5.0, 0.0);
				}
				if (itemStack.is(Items.WITHER_ROSE)) {
					hurt(DamageSource.WITHER, 0.5f);
				}
				return InteractionResult.sidedSuccess(this.level.isClientSide);
			}
			return InteractionResult.CONSUME;
		}

		return super.mobInteract(player, interactionHand);
	}

	@Override
	public void shear(SoundSource soundSource) {
		this.level.playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, soundSource, 1.0f, 1.0f);
		if (!this.level.isClientSide()) {
			((ServerLevel)this.level).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
			this.discard();
			Cow cow = EntityType.COW.create(this.level);
			cow.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
			cow.setHealth(this.getHealth());
			cow.yBodyRot = this.yBodyRot;
			if (this.hasCustomName()) {
				cow.setCustomName(this.getCustomName());
				cow.setCustomNameVisible(this.isCustomNameVisible());
			}
			if (this.isPersistenceRequired()) {
				cow.setPersistenceRequired();
			}
			cow.setInvulnerable(this.isInvulnerable());
			this.level.addFreshEntity(cow);
			for (int i = 0; i < 5; ++i) {
				this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0), this.getZ(), new ItemStack(ModItems.GLOW_MUSHROOM)));
			}
		}
	}

	@Override
	protected void usePlayerItem(Player player, InteractionHand interactionHand, ItemStack itemStack) {
		if (isFood(itemStack)) {
			handleFoodConsume();
		}
		super.usePlayerItem(player, interactionHand, itemStack);
	}

	public void handleFoodConsume() {
		addEffect(new MobEffectInstance(MobEffects.GLOWING, 600, 0, false, false)); // 30 seconds
	}

	@Override
	public boolean isCurrentlyGlowing() {
		return super.isCurrentlyGlowing();
	}

	@Override
	public boolean readyForShearing() {
		return this.isAlive() && !this.isBaby();
	}

	@Override
	public GlowMushroomCow getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		GlowMushroomCow cow = ModEntities.GLOW_MOOSHROOM.create(serverLevel);
		if (cow == null) return null;
		// Colors are not the same depending on the color that is added on top of another so we're gonna make that random
		boolean useFirst = random.nextBoolean();
		GlowColorable first = ((GlowColorable) (useFirst ? this : ageableMob));
		GlowColorable second = ((GlowColorable) (useFirst ? ageableMob : this));
		GlowColorable baby = (GlowColorable) cow;
		baby.setGlowColor(first.getGlowColor());
		dyeGlowColor(baby, second.getGlowColor());
		cow.handleFoodConsume();
		return cow;
	}

	public static boolean checkMushroomSpawnRules(EntityType<GlowMushroomCow> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
		return checkMobSpawnRules(entityType, levelAccessor, mobSpawnType, blockPos, randomSource);
	}

	public static Optional<DyeColor> getColorFromFlower(Item item) {
		DyeColor value = FLOWER_COLORS.get(item);
		return value == null ? Optional.empty() : Optional.of(value);
	}

	public static boolean dyeGlowColor(GlowColorable colorable, DyeColor color) {
		return dyeGlowColor(colorable, color.getTextureDiffuseColors());
	}

	public static boolean dyeGlowColor(GlowColorable colorable, int color) {
		// split hex color color into r, g, b components as float array
		float[] rgb = new float[3];
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;
		rgb[0] = r / 255.0f;
		rgb[1] = g / 255.0f;
		rgb[2] = b / 255.0f;
		return dyeGlowColor(colorable, rgb);
	}

	/**
	 * @return if the color changed
	 */
	public static boolean dyeGlowColor(GlowColorable colorable, float[] diffuseColors) {

		int currentColor = colorable.getGlowColor();
		int newColor = ((int) (diffuseColors[0] * 255) << 16) | ((int) (diffuseColors[1] * 255) << 8) | (int) (diffuseColors[2] * 255);

		if (currentColor == 0x000000 || currentColor == 0xFFFFFF) {
			colorable.setGlowColor(newColor);
			for (int i = 0; i < 3; i++) {
				dyeGlowColor(colorable, diffuseColors);
			}
			return true;
		} else {
			// Query old color
			float f = (float)(currentColor >> 16 & 0xFF) / 255.0f;
			float g = (float)(currentColor >> 8 & 0xFF) / 255.0f;
			float h = (float)(currentColor & 0xFF) / 255.0f;
			int i = (int)(Math.max(f, Math.max(g, h)) * 255.0f);
			int[] is = new int[3];
			is[0] = is[0] + (int)(f * 255.0f);
			is[1] = is[1] + (int)(g * 255.0f);
			is[2] = is[2] + (int)(h * 255.0f);
			int j = 1;

			// Apply new color
			int l = (int)(diffuseColors[0] * 255.0f);
			int m = (int)(diffuseColors[1] * 255.0f);
			int n = (int)(diffuseColors[2] * 255.0f);
			i += Math.max(l, Math.max(m, n));
			is[0] = is[0] + l;
			is[1] = is[1] + m;
			is[2] = is[2] + n;
			++j;

			// Apply changes
			int k = is[0] / j;
			int o = is[1] / j;
			int p = is[2] / j;
			h = (float)i / (float)j;
			float q = Math.max(k, Math.max(o, p));
			k = (int)((float)k * h / q);
			o = (int)((float)o * h / q);
			p = (int)((float)p * h / q);
			n = k;
			n = (n << 8) + o;
			n = (n << 8) + p;
			colorable.setGlowColor(n);
			return currentColor != n;
		}
	}

}
