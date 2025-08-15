package com.robin.multiversal.registry;


import com.boundless.hero.api.Hero;
import com.robin.multiversal.hero.ElementalMaster;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HeroRegistry {
    private static final Map<String, Hero> HEROES_BY_ID = new HashMap<>();
    private static final Map<PlayerEntity, Hero> PLAYER_HEROES = new HashMap<>();

    public static final Hero ELEMENTALMASTER = new ElementalMaster();

    public static void initialize() {
        registerHero(ELEMENTALMASTER);
    }

    public static void registerHero(Hero hero) {
        HEROES_BY_ID.put(hero.getHeroData().getName(), hero);
    }

    public static Optional<Hero> getHeroById(String id) {
        return Optional.ofNullable(HEROES_BY_ID.get(id));
    }

    public static void assignHero(PlayerEntity player, Hero hero) {
        PLAYER_HEROES.put(player, hero);
        // Additional logic (syncing etc.) here if needed
    }

    public static void removeHero(PlayerEntity player) {
        PLAYER_HEROES.remove(player);
        // Additional logic for removal here if needed
    }

    // This is the method you need to fix your error:
    public static Optional<Hero> getHero(PlayerEntity player) {
        return Optional.ofNullable(PLAYER_HEROES.get(player));
    }
}