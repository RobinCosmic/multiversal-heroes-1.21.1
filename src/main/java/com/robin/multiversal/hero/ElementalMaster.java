package com.robin.multiversal.hero;

import com.boundless.ability.AbilityLoadout;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.DataComponentUtils;
import com.mojang.serialization.Codec;
import com.robin.multiversal.Multiversalheroes;
import com.robin.multiversal.handler.SuperJumpHandler;
import com.robin.multiversal.powers.MultiversalAbilities;
import net.minecraft.component.ComponentType;

public class ElementalMaster extends Hero {
    public static ComponentType<Long> LEAP_NEXT_USABLE = DataComponentRegistry.registerLong("leap_next_usable");
    public static ComponentType<Integer> LEAP_CHARGE_TICKS = DataComponentRegistry.registerInt("leap_charge_ticks");

    public ElementalMaster() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.boundless.ability_one", MultiversalAbilities.WATER_PRISON)
                //.ability("key.boundless.ability_one", MultiversalAbilities.FLAMETHROWER)
                .ability("key.boundless.ability_two", MultiversalAbilities.MUD_WALL)
                .ability("key.boundless.ability_three", MultiversalAbilities.SUBSTITUTION)
                .build();

        ABILITY_LOADOUTS.put("ElementalMaster", loadout);

        this.heroData = HeroData
                .builder()
                .name("ElementalMaster")
                .displayName("Elemental Master")
                .tickHandler(SuperJumpHandler::tick)
                .textureIdentifier(Multiversalheroes.textureID("elementalmaster"))
                .defaultAbilityLoadout(ABILITY_LOADOUTS.get("ElementalMaster"))
                .build();

        this.registerHero();
    }
}