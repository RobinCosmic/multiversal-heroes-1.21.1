package com.robin.multiversal.powers;

import com.boundless.action.Action;
import com.boundless.networking.payloads.evasion.EvasionClientPayload;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.ActionUtils;
import com.boundless.util.AttackUtils;
import com.robin.multiversal.util.DelayedTask;
import com.robin.multiversal.util.DelayedTaskManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class SubtitutionAbility {
    public static void substitution(PlayerEntity player) {

        if (!player.getWorld().isClient) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, new EvasionClientPayload(player.getUuid()));
        }
        //effects
        // INVULNERABILITY effect and animation
        player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.INVULNERABILITY_EFFECT, 20, 0, true, false, false));
        // Invisibility setup
        Action turnInvis = ActionUtils.singleAction(1, (playerEntity, heroAction) -> {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 85, 0, true, false, false));
        });
        //trigger invis
        AttackUtils.triggerAttackAction(player, turnInvis);

        // serverside block placement, teleport, and sound!
        if (!player.getWorld().isClient) {
            ServerWorld serverWorld = (ServerWorld) player.getWorld();

            // Calculate teleport destination
            Vec3d direction = player.getRotationVector().normalize();
            Vec3d currentPos = player.getPos();
            Vec3d targetPos = currentPos.add(direction.multiply(5)).add(0, 10, 0);

            // Delay teleport by 2 ticks
            if (player instanceof ServerPlayerEntity serverPlayer) {
                DelayedTaskManager.add(new DelayedTask(2, () -> {
                    serverPlayer.teleport(serverWorld, targetPos.x, targetPos.y, targetPos.z, player.getYaw(), player.getPitch());
                }));
            }

            // get position and old blockstate for log placement
            BlockPos basePos = player.getBlockPos();
            BlockPos topPos = basePos.up();
            BlockState oldBaseState = serverWorld.getBlockState(basePos);
            BlockState oldTopState = serverWorld.getBlockState(topPos);
            //set blocks oak log
            serverWorld.setBlockState(basePos, Blocks.OAK_LOG.getDefaultState());
            serverWorld.setBlockState(topPos, Blocks.OAK_LOG.getDefaultState());

            // replace logs with old block play sound and particle
            //bottom log
            DelayedTaskManager.add(new DelayedTask(85, () -> {
                serverWorld.setBlockState(basePos, oldBaseState);

                serverWorld.spawnParticles(ParticleTypes.CLOUD, basePos.getX() + 0.5, basePos.getY() + 0.5, basePos.getZ() + 0.5, 20, 0.25, 0.25, 0.25, 0.01);

                serverWorld.playSound(null, basePos, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }));
            //top log
            DelayedTaskManager.add(new DelayedTask(85, () -> {
                serverWorld.setBlockState(topPos, oldTopState);

                serverWorld.spawnParticles(ParticleTypes.CLOUD, topPos.getX() + 0.5, topPos.getY() + 0.5, topPos.getZ() + 0.5, 20, 0.25, 0.25, 0.25, 0.01);

                serverWorld.playSound(null, topPos, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }));

            // sound on place
            serverWorld.playSound(null, basePos, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }

        // CLIENT SIDE: emit poof particles on use
        if (player.getWorld().isClient) {
            ClientWorld world = (ClientWorld) player.getWorld();
            Random random = new Random();
            double radius = 2.0;
            int particlesPerTick = 50;

            for (int i = 0; i < particlesPerTick; i++) {
                double angle = random.nextDouble() * 2 * Math.PI;
                double distance = random.nextDouble() * radius;
                double xOffset = Math.cos(angle) * distance;
                double zOffset = Math.sin(angle) * distance;
                double yOffset = (random.nextDouble() * 1.5) - 0.5;

                double x = player.getX() + xOffset;
                double y = player.getY() + yOffset + 1;
                double z = player.getZ() + zOffset;

                world.addParticle(ParticleTypes.POOF, x, y, z, 0, 0.01, 0);
            }
        }
    }
}