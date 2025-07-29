package com.robin.multiversal.ability.combat;

import com.boundless.ability.combat.AttackDataBuilder;
import com.robin.multiversal.registry.SoundRegistry;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.BiConsumer;

/*@Getter @Setter @Builder
public class CustomAttack extends AttackDataBuilder {
    @Builder.Default
    PlayerEntity player = null;
    @Builder.Default
    float damage = 0;
    @Builder.Default
    float knockbackStrength = 1f;
    @Builder.Default
    DamageSource damageSource = null;
    @Builder.Default
    SoundEvent impactSound = SoundRegistry.IMPACT_HEAVY_1_EVENT;
    @Builder.Default
    SoundEvent missSound = SoundRegistry.MISS_HIT_EVENT;
    @Builder.Default
    BiConsumer<CustomAttack, LivingEntity> customAttackLogic = null;
}
*/