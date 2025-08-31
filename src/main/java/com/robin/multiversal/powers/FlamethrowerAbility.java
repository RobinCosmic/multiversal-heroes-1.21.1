package com.robin.multiversal.powers;

import com.robin.multiversal.util.EffekUtils;
import dev.kosmx.playerAnim.core.util.Vec3f;
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

        // --- Effekseer bound effect ---
        // Attach to the body bone (chest area)
        Vec3d chestOffset = new Vec3d(0.0, 0.2, 0.4);
        // y = lift a little above waist, z = push forward so it doesn’t clip into body

        EffekUtils.playBoundEffect(
                Identifier.of("multiversal-heroes", "flamethrower"),
                player,
                new Vec3d(1.0, 1.0, 1.0),  // scale
                chestOffset,               // relative offset
                "body"                     // bind to body bone (chest)
        );

        // --- Damage + Fire logic ---
        int effectDuration = 60;
        for (int t = 0; t < effectDuration; t++) {
            int delay = t;

            world.getServer().execute(() -> {
                if (!player.isAlive()) return;

                Vec3d look = player.getRotationVec(1.0F).normalize();

                // approximate chest position for hit detection
                Vec3d chestPos = player.getPos()
                        .add(0, player.getStandingEyeHeight() - 0.5, 0)
                        .add(look.multiply(0.5));

                double range = 6.0;
                int steps = 10;

                for (int i = 1; i <= steps; i++) {
                    Vec3d pos = chestPos.add(look.multiply(i * (range / steps)));

                    // Entity hitbox
                    Box hitBox = new Box(
                            pos.x - 0.5, pos.y - 0.5, pos.z - 0.5,
                            pos.x + 0.5, pos.y + 0.5, pos.z + 0.5
                    );

                    List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, hitBox, e -> e != player);
                    for (LivingEntity target : entities) {
                        target.damage(world.getDamageSources().playerAttack(player), 2.0f);
                        target.setOnFireFor(3);
                    }

                    // Ignite blocks
                    BlockPos blockPos = BlockPos.ofFloored(pos);
                    if (world.getBlockState(blockPos).isAir() &&
                            world.getBlockState(blockPos.down()).isSolidBlock(world, blockPos.down())) {
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