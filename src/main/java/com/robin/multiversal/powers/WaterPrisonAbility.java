package com.robin.multiversal.powers;

import com.boundless.BoundlessAPI;
import com.boundless.util.AnimationUtils;
import com.robin.multiversal.util.DelayedTask;
import com.robin.multiversal.util.DelayedTaskManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class WaterPrisonAbility {
    public static void waterPrison(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        ServerWorld serverWorld = (ServerWorld) player.getWorld();

        // Get target entity from crosshair within 5 to 35 blocks
        Entity hit = getLookedAtEntity(player, 5.0, 155.0);
        if (!(hit instanceof LivingEntity target)) return;
        if (target == player) return;

        AnimationUtils.playAnimation(player, BoundlessAPI.identifier("jab"), 1.0f, false);

        int radius = 2;
        int tickInterval = 10;
        int durationTicks = 100;
        float damagePerTick = 2.0f;
        BlockPos center = target.getBlockPos();

        // Dynamically get the water LEVEL property
        Property<?> rawProperty = Blocks.WATER.getDefaultState().getProperties().stream()
                .filter(p -> p.getName().equals("level") && p.getType() == Integer.class)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("LEVEL property not found"));

        if (!(rawProperty instanceof Property<?> property) || property.getType() != Integer.class) {
            throw new IllegalStateException("LEVEL property is not an Integer type");
        }

        @SuppressWarnings("unchecked")
        Property<Integer> levelProperty = (Property<Integer>) property;

        // Build water sphere and outer barrier
        Set<BlockPos> barrierShell = new HashSet<>();
        for (int x = -radius - 1; x <= radius + 1; x++) {
            for (int y = -radius - 1; y <= radius + 1; y++) {
                for (int z = -radius - 1; z <= radius + 1; z++) {
                    double dist = Math.sqrt(x * x + y * y + z * z);
                    BlockPos pos = center.add(x, y, z);

                    if (dist <= radius && dist > radius - 1) {
                        if (serverWorld.isAir(pos)) {
                            BlockState water = Blocks.WATER.getDefaultState().with(levelProperty, 0);
                            serverWorld.setBlockState(pos, water);
                        }
                    } else if (dist > radius && dist <= radius + 1.1) {
                        if (serverWorld.isAir(pos)) {
                            serverWorld.setBlockState(pos, Blocks.BARRIER.getDefaultState());
                            barrierShell.add(pos);
                        }
                    }
                }
            }
        }

        // Trap effect loop
        DamageSource damageSource = serverWorld.getDamageSources().magic();
        LivingEntity trapped = target;

        for (int i = 0; i < durationTicks; i += tickInterval) {
            int delay = i;
            DelayedTaskManager.add(new DelayedTask(delay, () -> {
                trapped.damage(damageSource, damagePerTick);
                trapped.setVelocity(Vec3d.ZERO);
                trapped.velocityModified = true;
                trapped.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, tickInterval + 2, 6, true, false));
            }));
        }

        // Cleanup all water & barriers after delay
        DelayedTaskManager.add(new DelayedTask(durationTicks + 5, () -> {
            int cleanupRadius = radius + 4;
            BlockPos.Mutable mutablePos = new BlockPos.Mutable();

            for (int x = -cleanupRadius; x <= cleanupRadius; x++) {
                for (int y = -cleanupRadius; y <= cleanupRadius; y++) {
                    for (int z = -cleanupRadius; z <= cleanupRadius; z++) {
                        mutablePos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                        BlockState state = serverWorld.getBlockState(mutablePos);

                        // Remove any form of water (including flowing)
                        if (state.getFluidState().isOf(Fluids.WATER)) {
                            serverWorld.setBlockState(mutablePos, Blocks.AIR.getDefaultState());
                            serverWorld.spawnParticles(ParticleTypes.SPLASH,
                                    mutablePos.getX() + 0.5, mutablePos.getY() + 0.5, mutablePos.getZ() + 0.5,
                                    6, 0.2, 0.2, 0.2, 0.05);
                        }

                        // Remove barrier
                        if (state.getBlock() == Blocks.BARRIER) {
                            serverWorld.setBlockState(mutablePos, Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }

            serverWorld.playSound(null, center, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 1.0f, 1.2f);
        }));
    }

    //targeting helper method
    @Nullable
    private static Entity getLookedAtEntity(PlayerEntity player, double minDistance, double maxDistance) {
        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d direction = player.getRotationVec(1.0F);
        Vec3d end = start.add(direction.multiply(maxDistance));

        Box box = player.getBoundingBox().stretch(direction.multiply(maxDistance)).expand(35.0);
        Entity hit = ProjectileUtil.raycast(
                player,
                start,
                end,
                box,
                e -> e instanceof LivingEntity && !e.isSpectator() && e.isAlive() && e != player,
                maxDistance
        ).getEntity();

        if (hit != null && player.squaredDistanceTo(hit) >= minDistance * minDistance) {
            return hit;
        }
        return null;
    }
}