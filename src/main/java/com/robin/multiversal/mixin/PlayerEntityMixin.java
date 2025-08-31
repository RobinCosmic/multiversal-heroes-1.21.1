package com.robin.multiversal.mixin;

import com.boundless.ability.components.KeybindHoldData;
import com.boundless.util.KeybindingUtils;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin  {
    @Inject(at = @At("HEAD"), method = "jump", cancellable = true)
    private void init(CallbackInfo info) {
        PlayerEntity player = ((PlayerEntity) (Object)this);
        KeybindHoldData sneakHoldData = KeybindingUtils.getHoldData(player, "key.sneak");
        if (sneakHoldData.held()){
            info.cancel();
        }
    }
}