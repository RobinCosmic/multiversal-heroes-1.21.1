package com.robin.multiversal.client.render;

import com.robin.multiversal.entity.MudWallEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class MudWallEntityRenderer extends EntityRenderer<MudWallEntity> {

    private final BlockRenderManager blockRenderManager;

    public MudWallEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.blockRenderManager = ctx.getBlockRenderManager();
    }

    @Override
    public void render(MudWallEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        // Center the wall rendering around the entity position
        matrices.translate(-2.0, 0, -0.5); // adjust based on your wall dimensions

        // Render a 5x4 wall of packed mud blocks
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 5; x++) {
                matrices.push();
                matrices.translate(x, y, 0);
                blockRenderManager.renderBlockAsEntity(Blocks.PACKED_MUD.getDefaultState(), matrices, vertexConsumers, light, 0);
                matrices.pop();
            }
        }

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(MudWallEntity entity) {
        return null; // block textures do not use an entity texture
    }
}