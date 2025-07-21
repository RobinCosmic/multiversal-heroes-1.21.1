package com.robin.multiversal;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Multiversalheroes implements ModInitializer {
	public static final String MOD_ID = "multiversal-heroes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Multiversal Heroes Initialized!");
	}
}