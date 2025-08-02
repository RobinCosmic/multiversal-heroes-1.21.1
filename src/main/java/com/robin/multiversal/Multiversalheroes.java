package com.robin.multiversal;

import com.boundless.networking.PayloadRegistry;
import com.robin.multiversal.item.ItemRegistry;
import com.robin.multiversal.registry.HeroRegistry;
import com.robin.multiversal.registry.SoundRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Multiversalheroes implements ModInitializer {
	public static final String MOD_ID = "multiversal-heroes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		HeroRegistry.initialize();
		//EntityRegistry.initialize();
		//PayloadRegistry.registerPayloads();
		ItemRegistry.registerModItems();
		SoundRegistry.initialize();
		LOGGER.info("Multiversal Heroes Initialized!");
	}

	public static Identifier identifier(String name) {
		return Identifier.of(MOD_ID, name);
	}

	public static Identifier hudPNG(String name) {
		return identifier("textures/gui/sprites/hud/" + name + ".png");
	}

	public static Identifier textureID(String name) {
		return Identifier.of(Multiversalheroes.MOD_ID, "textures/item/hero/" + name + ".png");
	}

	public static Identifier modelID(String name) {
		return Identifier.of(Multiversalheroes.MOD_ID, "geo/item/" + name + ".geo.json");
	}
}