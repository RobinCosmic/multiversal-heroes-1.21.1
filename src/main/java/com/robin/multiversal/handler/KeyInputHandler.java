package com.robin.multiversal.handler;

import com.boundless.hero.api.HeroData;
import com.boundless.networking.payloads.AbilityUsePayload;
import com.boundless.networking.payloads.UpdateHoldStatePayload;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.AbilityUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KeyInputHandler {
    private static final Map<String, Boolean> heldKeysMap = new HashMap<>();
    private static final String COMBO_KEY = "key.jump+key.sneak";

    public KeyInputHandler() {}

    public static void keyInputs() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            if (!HeroUtils.isHero(client.player)) return;

            ItemStack stack = HeroUtils.getHeroStack(client.player);
            Map<String, Identifier> abilities = (Map) stack.get(DataComponentRegistry.ABILITY_LOADOUT);
            if (abilities == null) return;

            for (String translatableKey : abilities.keySet()) {
                inputLogic(client, translatableKey);
            }

            HeroData heroData = HeroUtils.getHeroData(client.player);
            if (heroData == null) return;

            // tick for combo
            updateComboHold(client);

        });
    }

    private static void updateComboHold(MinecraftClient client) {
        boolean bothHeld = client.options.jumpKey.isPressed() && client.options.sneakKey.isPressed();
        boolean wasHeld = heldKeysMap.getOrDefault(COMBO_KEY, false);

        if (bothHeld != wasHeld) {
            heldKeysMap.put(COMBO_KEY, bothHeld);
            ClientPlayNetworking.send(new UpdateHoldStatePayload(COMBO_KEY, bothHeld));
        }

        // stops constant jumping while charging
        if (bothHeld) {
            client.options.jumpKey.setPressed(false);
        }
    }

    // allows for the ability to use 1 keybind aswell
    public static void inputLogic(MinecraftClient client, String translatableKey) {
        KeyBinding kb = KeybindingUtils.getKeyBindingFromTranslation(translatableKey);
        if (kb == null) return;
        if (kb.isPressed()) {
            Identifier abilityID = AbilityUtils.abilityIDFromKeybind(client.player, translatableKey);
            if (AbilityUtils.checkAndUseAbility(client.player, abilityID)) {
                ClientPlayNetworking.send(new AbilityUsePayload(abilityID));
            }
        }
    }
}