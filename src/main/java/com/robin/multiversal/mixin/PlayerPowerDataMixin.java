package com.robin.multiversal.mixin;

import com.robin.multiversal.access.PlayerPowerDataAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(PlayerEntity.class)
public abstract class PlayerPowerDataMixin implements PlayerPowerDataAccess {
    @Unique
    private String multiversal$power = "";

    @Override
    public String multiversal$getPower() {
        return multiversal$power;
    }

    @Override
    public void multiversal$setPower(String power) {
        this.multiversal$power = power;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void multiversal$writePowerData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putString("CurrentPower", multiversal$power);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void multiversal$readPowerData(NbtCompound nbt, CallbackInfo ci) {
        this.multiversal$power = nbt.getString("CurrentPower");
    }
}