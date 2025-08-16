package com.robin.multiversal.hero;

import com.boundless.ability.AbilityLoadout;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import com.robin.multiversal.Multiversalheroes;
import com.robin.multiversal.powers.MultiversalAbilities;
import net.minecraft.component.ComponentType;

public class ElementalMaster extends Hero {
    public static ComponentType<Integer> JAB_ATTACK_COUNT = DataComponentRegistry.registerComponent("jab_attack_count", builder -> ComponentType.<Integer>builder().codec(Codec.INT));

    public ElementalMaster() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.boundless.ability_one", MultiversalAbilities.WATER_PRISON)
                .ability("key.boundless.ability_two", MultiversalAbilities.MUD_WALL)
                .ability("key.boundless.ability_three", MultiversalAbilities.SUBSTITUTION)
                .build();

        ABILITY_LOADOUTS.put("ElementalMaster", loadout);

        this.heroData = HeroData
                .builder()
                .name("ElementalMaster")
                .displayName("Elemental Master")
                .textureIdentifier(Multiversalheroes.textureID("elementalmaster"))
                .defaultAbilityLoadout(ABILITY_LOADOUTS.get("ElementalMaster"))
                .build();

        this.registerHero();
    }
}