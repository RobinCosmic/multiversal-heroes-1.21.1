package com.robin.multiversal.powers;

import com.boundless.hero.api.Hero;
import com.robin.multiversal.access.PlayerPowerAccess;
import com.robin.multiversal.mixin.PlayerEntityMixin;
import com.robin.multiversal.registry.HeroRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public class PowerManager {

    public static String getCurrentPower(PlayerEntity player) {
        if (player instanceof PlayerPowerAccess access) {
            return access.getMultiversalPowerId();
        }
        return "";
    }

    public static void setPlayerPower(PlayerEntity player, String powerId) {
        if (player instanceof PlayerPowerAccess access) {
            access.setMultiversalPowerId(powerId);
        }
    }

    public static boolean hasAnyPower(PlayerEntity player) {
        return !getCurrentPower(player).isEmpty();
    }

    public static void grantPower(PlayerEntity player, String powerId) {
        // Your existing logic here, e.g.
        setPlayerPower(player, powerId);
        // enable hero, etc...
    }

    public static void removePower(PlayerEntity player) {
        setPlayerPower(player, "");
        // disable hero, etc...
    }
}