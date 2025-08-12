package com.robin.multiversal.registry;

import com.robin.multiversal.entity.MudWallEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class EntityRegistry {

    public static final EntityType<MudWallEntity> MUD_WALL = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of("multiversal", "mud_wall"), // using the public factory method
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, MudWallEntity::new)
                    .dimensions(EntityDimensions.fixed(3.0F, 4.0F))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(1)
                    .build()
    );

    public static void register() {
        // call to load registry entries
    }
}