package com.robin.multiversal.util;

import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class DelayedBlockRestore {
    private final ServerWorld world;
    private final BlockPos pos;
    private final BlockState originalState;
    private int ticksRemaining;

    public DelayedBlockRestore(ServerWorld world, BlockPos pos, BlockState originalState, int delayTicks) {
        this.world = world;
        this.pos = pos;
        this.originalState = originalState;
        this.ticksRemaining = delayTicks;
    }

    public boolean tick() {
        if (--ticksRemaining <= 0) {
            world.setBlockState(pos, originalState);
            world.spawnParticles(ParticleTypes.POOF,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    10, 0.3, 0.5, 0.3, 0.01);
            return true;
        }
        return false;
    }
}