package com.robin.multiversal.powers;

import com.boundless.networking.payloads.CameraShakePayload;
import com.boundless.util.AnimationUtils;
import com.robin.multiversal.Multiversalheroes;
import com.robin.multiversal.util.DelayedTask;
import com.robin.multiversal.util.DelayedTaskManager;
import com.robin.multiversal.util.EffekUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SuperJumpAbility {

    private static final int ANIMATION_DURATION = 0; // 4 seconds (20 ticks per sec)
    private static final double JUMP_HEIGHT = 15.0;   // blocks

    public static void superJump(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        ServerWorld serverWorld = (ServerWorld) player.getWorld();

        // --- Play animation synced across clients
        //AnimationUtils.playSyncedAnimation(player, Multiversalheroes.identifier("mudwall"));

        // --- Schedule jump after animation finishes
        DelayedTaskManager.add(new DelayedTask(ANIMATION_DURATION, () -> {
            if (!player.isAlive()) return;

            // calculate jump velocity
            double jumpVelocity = Math.sqrt(2 * 0.08 * JUMP_HEIGHT);
            player.addVelocity(0, jumpVelocity, 0);
            player.velocityModified = true;

            // play effekseer effect
            EffekUtils.playBoundEffect(
                    Identifier.of("multiversal-heroes", "super_jump"),
                    player,
                    new Vec3d(1.5, 1.5, 1.5),
                    new Vec3d(0.0, -0.5, 0.0),
                    "body"
            );

            // play launch sound
            serverWorld.playSound(
                    null,
                    player.getBlockPos(),
                    SoundEvents.ENTITY_SLIME_JUMP,
                    SoundCategory.PLAYERS,
                    1.0f,
                    1.0f
            );

            // prevent fall damage from this jump
            player.fallDistance = 0.0f;
        }));
    }
}