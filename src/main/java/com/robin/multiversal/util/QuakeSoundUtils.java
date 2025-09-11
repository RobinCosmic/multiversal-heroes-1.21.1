package com.robin.multiversal.util;

import com.boundless.networking.payloads.CameraShakePayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class QuakeSoundUtils {

    /**
     * Rumble + camera shake while charging
     */
    public static void playChargeRumble(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        Vec3d pos = player.getPos();

        // Camera shake packet
        ServerPlayNetworking.send(player, new CameraShakePayload());

        // Play MANY overlapping deep rumbles every tick
        int soundCount = 20; // number of overlapping sounds per tick
        for (int i = 0; i < soundCount; i++) {
            // small random spatial offsets to make it feel layered
            double offsetX = (world.random.nextDouble() - 0.5) * 4.0;
            double offsetZ = (world.random.nextDouble() - 0.5) * 4.0;

            // very low pitch for deep rumble
            float pitch = 0.02f + world.random.nextFloat() * 0.08f;

            // slightly random volume
            float volume = 1.2f + world.random.nextFloat() * 0.6f;

            world.playSound(
                    null,
                    pos.x + offsetX,
                    pos.y,
                    pos.z + offsetZ,
                    SoundEvents.BLOCK_ANCIENT_DEBRIS_BREAK, // deep rumble sound
                    SoundCategory.AMBIENT,
                    volume,
                    pitch
            );
        }
    }

    /**
     * Impact + camera shake on launch
     */
    public static void playLaunchImpact(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        Vec3d pos = player.getPos();

        ServerPlayNetworking.send(player, new CameraShakePayload());

        world.playSound(
                null,
                pos.x, pos.y, pos.z,
                SoundEvents.ENTITY_GENERIC_EXPLODE,
                SoundCategory.AMBIENT,
                2.0f,
                0.5f
        );
    }
}