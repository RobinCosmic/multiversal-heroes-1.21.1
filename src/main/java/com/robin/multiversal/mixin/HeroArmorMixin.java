package com.robin.multiversal.mixin;

import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroArmor;
import com.robin.multiversal.powers.PowerManager;
import com.robin.multiversal.registry.HeroRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeroArmor.class)
public abstract class HeroArmorMixin {

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    private void runAbilitiesWithoutArmor(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if (!(entity instanceof PlayerEntity player) || world.isClient) return;

        // If the player has HeroArmor equipped, let the original HeroArmor logic run
        if (player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof HeroArmor) {
            return;
        }

        // Check if the player currently has a hero/power assigned
        HeroRegistry.getHero(player).ifPresent(hero -> {
            if (hero.getHeroData() != null) {
                if (PowerManager.canUseAbilitiesWithoutArmor(player)) {
                    if (!hero.getHeroData().getTickHandlers().isEmpty()) {
                        hero.getHeroData().getTickHandlers().forEach(handler -> handler.accept(player));
                    }
                }
            }
        });
    }
}