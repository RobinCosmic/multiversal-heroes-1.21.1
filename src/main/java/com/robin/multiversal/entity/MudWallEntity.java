package com.robin.multiversal.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public class MudWallEntity extends Entity {

    private int lifetime = 85;
    private int riseTicks = 20;
    private double startY;
    private boolean spawnEffectsPlayed = false;

    public MudWallEntity(EntityType<? extends MudWallEntity> type, World world) {
        super(type, world);
        this.noClip = false; // important so it interacts with collisions
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        // No tracked data
    }

    @Override
    public void tick() {
        super.tick();

        if (!spawnEffectsPlayed && !getWorld().isClient) {
            spawnEffectsPlayed = true;

            getWorld().playSound(
                    null,
                    getBlockPos(),
                    SoundEvents.BLOCK_ROOTED_DIRT_BREAK,
                    SoundCategory.BLOCKS,
                    1.5f,
                    1.0f
            );

            ((ServerWorld) getWorld()).spawnParticles(
                    ParticleTypes.CLOUD,
                    getX(),
                    getY(),
                    getZ(),
                    50,
                    2.5, 1.0, 0.5,
                    0.05
            );

            for (PlayerEntity player : getWorld().getPlayers()) {
                if (player.squaredDistanceTo(this) <= 25) {
                    double shakeX = (random.nextDouble() - 0.5) * 0.1;
                    double shakeY = (random.nextDouble() - 0.5) * 0.05;
                    double shakeZ = (random.nextDouble() - 0.5) * 0.1;
                    player.addVelocity(shakeX, shakeY, shakeZ);
                    player.velocityModified = true;
                }
            }
        }

        if (this.age < riseTicks) {
            double progress = (double) this.age / riseTicks;
            this.setPos(getX(), startY + progress * 2.0, getZ());
        }

        if (this.age >= lifetime) {
            this.discard();
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        lifetime = nbt.getInt("Lifetime");
        startY = nbt.getDouble("StartY");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Lifetime", lifetime);
        nbt.putDouble("StartY", startY);
    }


    public boolean collides() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    public void setStartY(double y) {
        this.startY = y;
    }

    @Override
    public EntityDimensions getDimensions(net.minecraft.entity.EntityPose pose) {
        // Width: 5 blocks, Height: 4 blocks
        return EntityDimensions.fixed(5.0f, 4.0f);
    }


    public VoxelShape getCollisionShape() {
        // Create a collision box 5 blocks wide (X), 4 blocks tall (Y), and 1 block thick (Z)
        // VoxelShapes.cuboid takes values in "block space" where 1.0 = 1 block
        return VoxelShapes.cuboid(
                0.0,                // minX
                0.0,                // minY
                0.0,                // minZ
                5.0,                // maxX
                4.0,                // maxY
                1.0                 // maxZ
        );
    }
}