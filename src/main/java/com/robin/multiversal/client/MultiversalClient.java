package com.robin.multiversal.client;

import com.robin.multiversal.handler.KeyInputHandler;
import com.robin.multiversal.registry.EntityRegistry;
import com.robin.multiversal.client.render.MudWallEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class MultiversalClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.MUD_WALL, MudWallEntityRenderer::new);
        KeyInputHandler.keyInputs();
    }
}