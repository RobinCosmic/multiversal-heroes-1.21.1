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

        AnimationUtils.playSyncedAnimation(player, Multiversalheroes.identifier("mudwall"));

        Vec3d lookVec = player.getRotationVec(1.0f).normalize();
        BlockPos playerPos = player.getBlockPos();
        BlockPos wallCenter = playerPos.add(
                (int) Math.round(lookVec.x * 3),
                0,
                (int) Math.round(lookVec.z * 3)
        );

        boolean alignAlongX = Math.abs(lookVec.z) > Math.abs(lookVec.x);

        int wallWidth = 5;
        int wallHeight = 4;
        int startOffset = -(wallWidth / 2);

        for (int y = 0; y < wallHeight; y++) {
            int delay = y * 5;

            for (int offset = 0; offset < wallWidth; offset++) {
                int actualOffset = startOffset + offset;

                BlockPos blockPos = alignAlongX
                        ? wallCenter.add(actualOffset, y, 0)
                        : wallCenter.add(0, y, actualOffset);

                BlockState oldState = serverWorld.getBlockState(blockPos);

                DelayedTaskManager.add(new DelayedTask(delay, () -> {
                    serverWorld.setBlockState(blockPos, Blocks.PACKED_MUD.getDefaultState());

                    serverWorld.spawnParticles(
                            ParticleTypes.CLOUD,
                            blockPos.getX() + 0.5,
                            blockPos.getY() + 0.5,
                            blockPos.getZ() + 0.5,
                            30,
                            0.35, 0.35, 0.35,
                            0.01
                    );

                    serverWorld.playSound(
                            null,
                            blockPos,
                            SoundEvents.BLOCK_ROOTED_DIRT_BREAK,
                            SoundCategory.BLOCKS,
                            1.0f,
                            1.0f
                    );
                }));

                DelayedTaskManager.add(new DelayedTask(85, () -> {
                    serverWorld.setBlockState(blockPos, oldState);

                    serverWorld.spawnParticles(
                            ParticleTypes.POOF,
                            blockPos.getX() + 0.5,
                            blockPos.getY() + 0.5,
                            blockPos.getZ() + 0.5,
                            20,
                            0.25, 0.25, 0.25,
                            0.01
                    );

                    serverWorld.playSound(
                            null,
                            blockPos,
                            SoundEvents.ENTITY_ARROW_SHOOT,
                            SoundCategory.BLOCKS,
                            1.0f,
                            1.0f
                    );
                }));
            }
        }
    }
}