package com.robin.multiversal.mixin;

import com.robin.multiversal.access.PlayerPowerAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerPowerAccess {

    @Unique
    private String multiversalPowerId = "";

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void savePowerToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (!multiversalPowerId.isEmpty()) {
            nbt.putString("MultiversalPower", multiversalPowerId);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void loadPowerFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("MultiversalPower")) {
            multiversalPowerId = nbt.getString("MultiversalPower");
        } else {
            multiversalPowerId = "";
        }
    }

    // Provide getter/setter for your power ID
    public String getMultiversalPowerId() {
        return multiversalPowerId;
    }

    public void setMultiversalPowerId(String powerId) {
        this.multiversalPowerId = powerId;
    }
}