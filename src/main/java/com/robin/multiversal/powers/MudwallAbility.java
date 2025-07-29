package com.robin.multiversal.powers;

import com.boundless.util.AnimationUtils;
import com.robin.multiversal.Multiversalheroes;
import com.robin.multiversal.util.DelayedTask;
import com.robin.multiversal.util.DelayedTaskManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


public class MudwallAbility {

    public static void mudwall(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        ServerWorld serverWorld = (ServerWorld) player.getWorld();
        // Trigger animation
        AnimationUtils.playAnimation(player, Multiversalheroes.identifier("mudwall"), 2.0f, false);

        // Determine where the wall should appear
        Vec3d lookVec = player.getRotationVec(1.0f).normalize();
        BlockPos playerPos = player.getBlockPos();
        BlockPos wallCenter = playerPos.add((int) Math.round(lookVec.x * 3), 0, (int) Math.round(lookVec.z * 3));

        // Align long side of the wall to face the player
        boolean alignAlongX = Math.abs(lookVec.z) > Math.abs(lookVec.x);

        int wallWidth = 5;
        int wallHeight = 4;
        int startOffset = -(wallWidth / 2);

        for (int y = 0; y < wallHeight; y++) {
            int delay = y * 5; // 5 tick delay between each vertical layer

            for (int offset = 0; offset < wallWidth; offset++) {
                int actualOffset = startOffset + offset;

                BlockPos blockPos = alignAlongX ? wallCenter.add(actualOffset, y, 0) : wallCenter.add(0, y, actualOffset);

                BlockState oldState = serverWorld.getBlockState(blockPos);

                // Schedule block placement + particles + sound
                DelayedTaskManager.add(new DelayedTask(delay, () -> {
                    serverWorld.setBlockState(blockPos, Blocks.PACKED_MUD.getDefaultState());

                    // Emit cloud particles
                    serverWorld.spawnParticles(ParticleTypes.CLOUD, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 30, 0.35, 0.35, 0.35, 0.01);

                    // Play dirt breaking sound
                    serverWorld.playSound(null, blockPos, SoundEvents.BLOCK_ROOTED_DIRT_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }));

                // Schedule wall disappearance (restore block + particles + bow sound)
                DelayedTaskManager.add(new DelayedTask(85, () -> {
                    serverWorld.setBlockState(blockPos, oldState);

                    // Emit particles on disappear
                    serverWorld.spawnParticles(ParticleTypes.POOF, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 20, 0.25, 0.25, 0.25, 0.01);

                    // Play bow shoot sound
                    serverWorld.playSound(null, blockPos, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }));
            }
            }
        }
    }