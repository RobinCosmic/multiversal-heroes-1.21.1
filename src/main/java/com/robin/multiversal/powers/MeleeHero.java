package com.robin.multiversal.powers;


import com.boundless.BoundlessAPI;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.reusable_abilities.MeleeCombatAbilities;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.robin.multiversal.registry.HeroRegistry;
import com.boundless.registry.DataComponentRegistry;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import com.mojang.serialization.Codec;

public class MeleeHero extends Hero {

    public MeleeHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                //.ability("key.boundless.ability_two", MeleeCombatAbilities.JAB)
                .ability("key.boundless.ability_three", MeleeCombatAbilities.SPIN_KICK)
                .ability("key.boundless.ability_four", MeleeCombatAbilities.DROPKICK)
                .ability("key.boundless.ability_five", MeleeCombatAbilities.DODGE)
                .ability("key.boundless.ability_six", MeleeCombatAbilities.UPPERCUT)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);

        this.heroData = HeroData.builder()
                .name("melee_hero")
                .textureIdentifier(BoundlessAPI.textureID("meleehero"))
                .defaultAbilityLoadout(loadout)
                .build();

        this.registerHero();
    }

    // --- Static methods to act as a "Power" ---

    public static void enable(PlayerEntity player) {
        // Assign the MeleeHero directly
        HeroRegistry.assignHero(player, new MeleeHero());
    }

    public static void disable(PlayerEntity player) {
        // Remove the hero from the player
        HeroRegistry.removeHero(player);
    }
}