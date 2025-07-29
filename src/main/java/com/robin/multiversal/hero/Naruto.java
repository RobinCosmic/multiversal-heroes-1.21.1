package com.robin.multiversal.hero;

import com.boundless.ability.AbilityLoadout;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import com.robin.multiversal.Multiversalheroes;
import com.robin.multiversal.powers.MultiversalAbilities;
import net.minecraft.component.ComponentType;

public class Naruto extends Hero {
    public static ComponentType<Integer> JAB_ATTACK_COUNT = DataComponentRegistry.registerComponent("jab_attack_count", builder -> ComponentType.<Integer>builder().codec(Codec.INT));

    public Naruto() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.boundless.ability_one", MultiversalAbilities.WATER_PRISON)
                .ability("key.boundless.ability_two", MultiversalAbilities.MUD_WALL)
                .ability("key.boundless.ability_three", MultiversalAbilities.SUBSTITUTION)
                .build();

        ABILITY_LOADOUTS.put("Naruto", loadout);

        this.heroData = HeroData
                .builder()
                .name("naruto")
                .displayName("Naruto")
                .textureIdentifier(Multiversalheroes.textureID("naruto"))
                .defaultAbilityLoadout(ABILITY_LOADOUTS.get("Naruto"))
                .build();

        this.registerHero();
    }
}
        /*this.heroData = HeroData.builder()
                .name("naruto")
                .textureIdentifier("naruto")
                .ability("key.use", spinKick(40, 6f, 10))
                .ability("key.attack", MeleeCombatAbilities.jab())
                //.ability("key.boundless.ability_one", new Ability(JutsuStorage::rasenganCharge, 160, JutsuStorage::RasenganPredicate, (JutsuStorage.RASENGAN), 22, 22))
                .ability("key.boundless.ability_one", new Ability(JutsuStorage::waterPrison, 400))
                .ability("key.boundless.ability_three", new Ability(JutsuStorage::mudwall, 400))
                .ability("key.boundless.ability_four", new Ability(MiscAbilities::toggleMechanics, 10, null, BoundlessAPI.identifier("textures/gui/sprites/hud/toggle_mechanics.png"), 22, 22))
                .build();
        this.registerHero();
    }
}*/