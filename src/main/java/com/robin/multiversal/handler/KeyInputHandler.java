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
    private static final Map<String, Boolean> heldKeysMap = new HashMap();

    public KeyInputHandler() {
    }

    public static void keyInputs() {
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            if (client.player != null && HeroUtils.isHero(client.player)) {
                ItemStack stack = HeroUtils.getHeroStack(client.player);
                Map<String, Identifier> abilities = (Map)stack.get(DataComponentRegistry.ABILITY_LOADOUT);
                if (abilities != null) {
                    Iterator var3 = abilities.keySet().iterator();

                    while(var3.hasNext()) {
                        String translatableKey = (String)var3.next();
                        inputLogic(client, translatableKey);
                    }

                    HeroData heroData = HeroUtils.getHeroData(client.player);
                    if (heroData != null) {
                        keybindHoldLogic(client, client.options.jumpKey, client.options.jumpKey.getTranslationKey());
                        keybindHoldLogic(client, client.options.sneakKey, client.options.sneakKey.getTranslationKey());
                    }
                }
            }
        });
    }

    public static void keybindHoldLogic(MinecraftClient client, KeyBinding key, String translationKey) {
        if (client.player != null) {
            if (key.isPressed() && !(Boolean)heldKeysMap.getOrDefault(translationKey, false)) {
                heldKeysMap.put(translationKey, true);
                ClientPlayNetworking.send(new UpdateHoldStatePayload(translationKey, true));
            } else if (!key.isPressed() && (Boolean)heldKeysMap.getOrDefault(translationKey, false)) {
                heldKeysMap.put(translationKey, false);
                ClientPlayNetworking.send(new UpdateHoldStatePayload(translationKey, false));
            }

        }
    }

    public static void inputLogic(MinecraftClient client, String translatableKey) {
        if (KeybindingUtils.getKeyBindingFromTranslation(translatableKey).isPressed()) {
            Identifier abilityID = AbilityUtils.abilityIDFromKeybind(client.player, translatableKey);
            if (AbilityUtils.checkAndUseAbility(client.player, abilityID)) {
                ClientPlayNetworking.send(new AbilityUsePayload(abilityID));
            }
        }

    }
}
