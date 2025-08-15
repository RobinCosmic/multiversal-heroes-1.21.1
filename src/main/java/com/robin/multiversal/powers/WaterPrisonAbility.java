package com.robin.multiversal.powers;

import com.boundless.BoundlessAPI;
import com.boundless.util.AnimationUtils;
import com.robin.multiversal.Multiversalheroes;
import com.robin.multiversal.util.DelayedTask;
import com.robin.multiversal.util.DelayedTaskManager;
import com.robin.multiversal.util.EffekUtils;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class WaterPrisonAbility {

        public static void waterPrison(PlayerEntity player) {
            if (player.getWorld().isClient) return;

            ServerWorld serverWorld = (ServerWorld) player.getWorld();

            // Get target
            Entity hit = getLookedAtEntity(player, 5.0, 155.0);
            if (!(hit instanceof LivingEntity target)) return;
            if (target == player) return;

            // Player animation
            AnimationUtils.playAnimation(player, BoundlessAPI.identifier("jab"), 1.0f, false);

            // Play bound Effekseer water prison effect
            EffekUtils.playBoundEffect(
                    Identifier.of("multiversal-heroes", "water_prison"),
                    target,
                    new Vec3d(1, 1, 1),
                    Vec3d.ZERO
            );

            int tickInterval = 10;
            int durationTicks = 40;
            float damagePerTick = 5.0f;
            DamageSource damageSource = serverWorld.getDamageSources().magic();

            for (int i = 0; i < durationTicks; i += tickInterval) {
                int delay = i;
                DelayedTaskManager.add(new DelayedTask(delay, () -> {
                    // Damage
                    target.damage(damageSource, damagePerTick);

                    // Lock movement
                    target.setVelocity(Vec3d.ZERO);
                    target.velocityModified = true;
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, tickInterval + 2, 6, true, false, false));

                    // Vortex bubbles
                    double radius = 1.5;
                    int particleCount = 16;
                    double heightStep = 0.15;
                    long worldTime = serverWorld.getTime();

                    for (int j = 0; j < particleCount; j++) {
                        double angle = (j / (double) particleCount) * 2 * Math.PI + (worldTime / 6.0);
                        double x = target.getX() + radius * Math.cos(angle);
                        double y = target.getY() + 0.3 + (j * heightStep % 2.0);
                        double z = target.getZ() + radius * Math.sin(angle);
                        serverWorld.spawnParticles(ParticleTypes.BUBBLE, x, y, z, 1, 0, 0, 0, 0);
                    }
                }));
            }

            player.getWorld().playSound(
                    null,
                    player.getBlockPos(),
                    SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE,
                    SoundCategory.PLAYERS,
                    1.0f,  // volume
                    1.0f   // pitch
            );

            // End splash sound
            DelayedTaskManager.add(new DelayedTask(19, () -> {
                player.getWorld().playSound(
                        null,
                        player.getBlockPos(),
                        SoundEvents.ENTITY_GENERIC_SPLASH,
                        SoundCategory.PLAYERS,
                        1.0f,  // volume
                        1.0f   // pitch
                );
            }));
        }

        // Targeting helper method
        @Nullable
        private static Entity getLookedAtEntity(PlayerEntity player, double minDistance, double maxDistance) {
            Vec3d start = player.getCameraPosVec(1.0F);
            Vec3d direction = player.getRotationVec(1.0F);
            Vec3d end = start.add(direction.multiply(maxDistance));

            Box box = player.getBoundingBox().stretch(direction.multiply(maxDistance)).expand(35.0);
            var result = ProjectileUtil.raycast(
                    player,
                    start,
                    end,
                    box,
                    e -> e instanceof LivingEntity && !e.isSpectator() && e.isAlive() && e != player,
                    maxDistance
            );

            Entity hit = result != null ? result.getEntity() : null;
            if (hit != null && player.squaredDistanceTo(hit) >= minDistance * minDistance) {
                return hit;
            }
            return null;
        }
    }