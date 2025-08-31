package com.robin.multiversal.handler;

import com.boundless.ability.components.KeybindHoldData;
import com.boundless.networking.payloads.CameraShakePayload;
import com.boundless.util.AnimationUtils;
import com.boundless.util.KeybindingUtils;
import com.robin.multiversal.Multiversalheroes;
import com.robin.multiversal.powers.SuperJumpAbility;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;


public class SuperJumpHandler {
    public static void tick(PlayerEntity player) {
        KeybindHoldData sneakHoldData = KeybindingUtils.getHoldData(player, "key.sneak");
        KeybindHoldData jumpHoldData = KeybindingUtils.getHoldData(player, "key.jump");

        if (!jumpHoldData.held() || !player.isOnGround() || !sneakHoldData.held()) return;
        long jumpHeldDuration = player.getWorld().getTime() - jumpHoldData.startTimestamp();
        if (leapchargeticks >= 20){

        }

        if (jumpHeldDuration == 1 || jumpHeldDuration > 20) {

            if (!player.getWorld().isClient) {
                //Camera shake
                ServerPlayNetworking.send((ServerPlayerEntity) player, new CameraShakePayload());
            }
        }

        if (jumpHeldDuration == 1) {
            //animation
            AnimationUtils.playSyncedAnimation(player, Multiversalheroes.identifier("mudwall"));
        }
        //component for checking time start of holding the button
        if (jumpHeldDuration > 20) {
            //plays the ability
            SuperJumpAbility.superJump(player);

        }
    }
}