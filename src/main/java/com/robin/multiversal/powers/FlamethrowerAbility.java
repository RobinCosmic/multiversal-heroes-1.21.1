package com.robin.multiversal.powers;

import com.robin.multiversal.util.EffekUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class FlamethrowerAbility {

    public static void useFlamethrower(PlayerEntity player) {
        World world = player.getWorld();
        if (world.isClient) return;

        // --- Calculate hand position ---
        Vec3d look = player.getRotationVec(1.0F).normalize();
        Vec3d up = new Vec3d(0, 1, 0);
        Vec3d right = look.crossProduct(up).normalize();

        // Right hand position
        Vec3d handPos = player.getPos()
                .add(0, player.getStandingEyeHeight() - 0.2, 0) // arm height
                .add(right.multiply(0.35))                      // move sideways to hand
                .add(look.multiply(0.2));                       // small forward

        // --- Play Effekseer effect ---
        // Effect's local +X (left→right) axis will be aligned to the player's look vector
        EffekUtils.playBoundEffect(
                Identifier.of("multiversal-heroes", "flamethrower"),
                player,
                new Vec3d(1.0, 1.0, 1.0),   // scale
                handPos.subtract(player.getPos()), // relative offset
                look // 🔥 map effect’s "left→right" axis to player's forward
        );

        // --- Damage + Fire logic ---
        int effectDuration = 60;
        for (int t = 0; t < effectDuration; t++) {
            int delay = t;

            world.getServer().execute(() -> {
                if (!player.isAlive()) return;

                double range = 6.0;
                int steps = 10;

                for (int i = 1; i <= steps; i++) {
                    Vec3d pos = handPos.add(look.multiply(i * (range / steps)));

                    // Entity hitbox
                    Box hitBox = new Box(pos.x - 0.5, pos.y - 0.5, pos.z - 0.5,
                            pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);

                    List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, hitBox, e -> e != player);
                    for (LivingEntity target : entities) {
                        target.damage(world.getDamageSources().playerAttack(player), 2.0f);
                        target.setOnFireFor(3);
                    }

                    // Ignite blocks
                    BlockPos blockPos = BlockPos.ofFloored(pos);
                    if (world.getBlockState(blockPos).isAir() && world.getBlockState(blockPos.down()).isSolidBlock(world, blockPos.down())) {
                        if (world.random.nextFloat() < 0.1f) {
                            world.setBlockState(blockPos, Blocks.FIRE.getDefaultState());
                        }
                    }
                }
            });
        }

        // --- Sound ---
        world.playSound(null, player.getBlockPos(),
                SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }
}