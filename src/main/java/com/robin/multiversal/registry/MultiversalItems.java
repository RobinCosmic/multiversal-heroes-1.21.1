package com.robin.multiversal.registry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class MultiversalItems {
    public static final String MOD_ID = "multiversal-heroes";

    // Food definition for the burger (fills 6 hunger, decent saturation)
    private static final FoodComponent BURGER_FOOD = new FoodComponent.Builder()
            .nutrition(6)              // 3 shanks
            .saturationModifier(0.7f)  // decent saturation
            .build();

    // The edible Burger item
    public static final Item BURGER = register("burger",
            new Item(new Item.Settings().food(BURGER_FOOD)));

    private MultiversalItems() {}

    /** Call this from your mod initializer. */
    public static void init() {
        // Put the burger in the Food & Drinks creative tab
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(BURGER);
        });
    }

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), item);
    }
}