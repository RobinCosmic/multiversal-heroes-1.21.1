package com.robin.multiversal.item;


import com.robin.multiversal.Multiversalheroes;
import com.robin.multiversal.item.MeleeInjectorItem;
import com.robin.multiversal.item.InjectorItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final Item INJECTOR = registerItem("injector", new InjectorItem(new Item.Settings().maxCount(1)));
    public static final Item MELEE_INJECTOR = registerItem("melee_injector", new MeleeInjectorItem(new Item.Settings().maxCount(1)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Multiversalheroes.MOD_ID, name), item);
    }

    public static void registerModItems() {
        // Register items
        registerItem("injector", INJECTOR); // empty injector
        registerItem("injector_melee", MELEE_INJECTOR); // pre-filled

        // Add to creative tab
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(INJECTOR);
            entries.add(MELEE_INJECTOR);
        });
    }
}