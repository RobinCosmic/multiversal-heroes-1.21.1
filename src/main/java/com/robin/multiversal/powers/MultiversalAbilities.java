package com.robin.multiversal.powers;

import com.boundless.ability.Ability;
import com.boundless.hero.api.Hero;
import com.robin.multiversal.Multiversalheroes;

public class MultiversalAbilities extends Hero {
    public static Ability WATER_PRISON = Ability.builder()
            .abilityConsumer(WaterPrisonAbility::waterPrison)
            .cooldown(120)
            .abilityID(Multiversalheroes.identifier("water_prison"))
            .abilityIcon(Multiversalheroes.hudPNG("water_prison"))
            .build();

    public static Ability MUD_WALL = Ability.builder()
            .abilityConsumer(MudwallAbility::mudwall)
            .cooldown(120)
            .abilityID(Multiversalheroes.identifier("mudwall"))
            .abilityIcon(Multiversalheroes.hudPNG("mudwall"))
            .build();

    public static Ability SUBSTITUTION = Ability.builder()
            .abilityConsumer(SubtitutionAbility::substitution)
            .cooldown(120)
            .abilityID(Multiversalheroes.identifier("substitution"))
            .abilityIcon(Multiversalheroes.hudPNG("substitution"))
            .build();

    public static Ability FLAMETHROWER = Ability.builder()
            .abilityConsumer(FlamethrowerAbility::useFlamethrower)
            .cooldown(120)
            .abilityID(Multiversalheroes.identifier("flamethrower"))
            .abilityIcon(Multiversalheroes.hudPNG("flamethrower"))
            .build();
}