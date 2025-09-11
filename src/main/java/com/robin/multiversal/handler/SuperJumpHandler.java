package com.robin.multiversal.handler;

import com.boundless.ability.components.KeybindHoldData;
import com.boundless.networking.payloads.CameraShakePayload;
import com.boundless.util.AnimationUtils;
import com.boundless.util.DataComponentUtils;
import com.boundless.util.KeybindingUtils;
import com.robin.multiversal.Multiversalheroes;
import com.robin.multiversal.powers.SuperJumpAbility;
import com.robin.multiversal.util.QuakeSoundUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import static com.robin.multiversal.hero.ElementalMaster.LEAP_CHARGE_TICKS;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SuperJumpHandler {
    // must match what the client sends in UpdateHoldStatePayload
    public static final String COMBO_KEY = "key.jump+key.sneak";

    // per-player marker so we only trigger once per press
    private static final Map<UUID, Boolean> chargedSinceLastRelease = new ConcurrentHashMap<>();

    public static void tick(PlayerEntity player) {
        if (player == null) return;

        KeybindHoldData comboHoldData = KeybindingUtils.getHoldData(player, COMBO_KEY);
        if (comboHoldData == null) return;

        UUID uid = player.getUuid();
        boolean alreadyCharged = chargedSinceLastRelease.getOrDefault(uid, false);

        // Reset if combo released or player leaves ground
        if (!comboHoldData.held() || !player.isOnGround()) {
            chargedSinceLastRelease.put(uid, false);
            return;
        }

        // Stop if already triggered this hold session
        if (alreadyCharged) return;

        long heldDuration = player.getWorld().getTime() - comboHoldData.startTimestamp();
        if (heldDuration < 0) heldDuration = 0;

        // --- Charge rumble every tick while holding ---
        if (!player.getWorld().isClient && player instanceof ServerPlayerEntity) {
            QuakeSoundUtils.playChargeRumble((ServerPlayerEntity) player);
        }

        // --- First tick: play animation ---
        if (heldDuration == 1) {
            AnimationUtils.playSyncedAnimation(player, Multiversalheroes.identifier("mudwall"));
        }

        // --- Full charge reached ---
        if (heldDuration >= 20) {
            if (!player.getWorld().isClient && player instanceof ServerPlayerEntity) {
                QuakeSoundUtils.playLaunchImpact((ServerPlayerEntity) player);
            }

            SuperJumpAbility.superJump(player);
            chargedSinceLastRelease.put(uid, true);
        }
    }
    }