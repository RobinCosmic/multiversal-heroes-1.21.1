package com.robin.multiversal.registry;

import com.boundless.BoundlessAPI;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundRegistry {
    public static final Identifier IMPACT_HEAVY_1 = BoundlessAPI.identifier("impact_heavy_1");
    public static final Identifier MISS_HIT = BoundlessAPI.identifier("miss_hit");
    public static SoundEvent IMPACT_HEAVY_1_EVENT = SoundEvent.of(IMPACT_HEAVY_1);
    public static SoundEvent MISS_HIT_EVENT = SoundEvent.of(MISS_HIT);
    public static final Identifier SHRINE_SLASH = BoundlessAPI.identifier("shrine_slash");
    public static SoundEvent SHRINE_SLASH_EVENT = SoundEvent.of(SHRINE_SLASH);

    public static void initialize() {
        Registry.register(Registries.SOUND_EVENT, IMPACT_HEAVY_1, IMPACT_HEAVY_1_EVENT);
        Registry.register(Registries.SOUND_EVENT, MISS_HIT, MISS_HIT_EVENT);
        Registry.register(Registries.SOUND_EVENT, SHRINE_SLASH, SHRINE_SLASH_EVENT);
    }
}