package com.blamejared.funkyframes.client.render;

import com.blamejared.funkyframes.util.FunkyFramePaintingVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.decoration.Painting;

public class RenderPainting {
    
    public static void renderPainting(EntityRenderer<Painting> renderer, PoseStack poseStack, MultiBufferSource buffers, Painting painting, FunkyFramePaintingVariant variant, TextureAtlasSprite back) {
        
        if(Minecraft.getInstance().player != null && Minecraft.getInstance().player.isCrouching()) {
            Minecraft.getInstance().gameRenderer.reloadShaders(Minecraft.getInstance().getResourceManager());
        }
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        Matrix3f normal = pose.normal();
        int width = variant.getWidth();
        int height = variant.getHeight();
        float xSize = (float) (-width) / 2.0F;
        float ySize = (float) (-height) / 2.0F;
        int sectionSize = 16;
        int xSections = width / sectionSize;
        int ySections = height / sectionSize;
        
        int light = LevelRenderer.getLightColor(painting.level, painting.blockPosition());
        
        VertexConsumer shaderVertex = buffers.getBuffer(FunkyFramesRenderTypes.getRenderType(variant.renderType())
                .using(variant.textures()));
        
        // Front
        vertex(matrix, normal, shaderVertex, -xSize, +ySize, (float) 0, 1, -0.5F, 0, 0, -1, light);
        vertex(matrix, normal, shaderVertex, +xSize, +ySize, (float) 1, 1, -0.5F, 0, 0, -1, light);
        vertex(matrix, normal, shaderVertex, +xSize, -ySize, (float) 1, 0, -0.5F, 0, 0, -1, light);
        vertex(matrix, normal, shaderVertex, -xSize, -ySize, (float) 0, 0, -0.5F, 0, 0, -1, light);
        
        VertexConsumer miscVertex = buffers.getBuffer(RenderType.entitySolid(renderer.getTextureLocation(painting)));
        
        float backUMin = back.getU0();
        float backUMax = back.getU1();
        float backVMin = back.getV0();
        float backVMax = back.getV1();
        // 1 spelt out because I like things the same length
        float backUOne = back.getU(1);
        float backVOne = back.getV(1);
        
        for(int xSec = 0; xSec < xSections; xSec++) {
            for(int ySec = 0; ySec < ySections; ySec++) {
                
                float minX = xSize + (xSec * sectionSize);
                float minY = ySize + (ySec * sectionSize);
                
                float maxX = xSize + ((xSec + 1) * sectionSize);
                float maxY = ySize + ((ySec + 1) * sectionSize);
                
                // back
                {
                    vertex(matrix, normal, miscVertex, maxX, maxY, backUMin, backVMax, +0.5F, 0, 0, +1, light);
                    vertex(matrix, normal, miscVertex, minX, maxY, backUMax, backVMax, +0.5F, 0, 0, +1, light);
                    vertex(matrix, normal, miscVertex, minX, minY, backUMax, backVMin, +0.5F, 0, 0, +1, light);
                    vertex(matrix, normal, miscVertex, maxX, minY, backUMin, backVMin, +0.5F, 0, 0, +1, light);
                }
                // top
                {
                    vertex(matrix, normal, miscVertex, maxX, maxY, backUMin, backVMin, -0.5F, 0, +1, 0, light);
                    vertex(matrix, normal, miscVertex, minX, maxY, backUMax, backVMin, -0.5F, 0, +1, 0, light);
                    vertex(matrix, normal, miscVertex, minX, maxY, backUMax, backVOne, +0.5F, 0, +1, 0, light);
                    vertex(matrix, normal, miscVertex, maxX, maxY, backUMin, backVOne, +0.5F, 0, +1, 0, light);
                }
                // bottom
                {
                    vertex(matrix, normal, miscVertex, maxX, minY, backUMin, backVMin, +0.5F, 0, -1, 0, light);
                    vertex(matrix, normal, miscVertex, minX, minY, backUMax, backVMin, +0.5F, 0, -1, 0, light);
                    vertex(matrix, normal, miscVertex, minX, minY, backUMax, backVOne, -0.5F, 0, -1, 0, light);
                    vertex(matrix, normal, miscVertex, maxX, minY, backUMin, backVOne, -0.5F, 0, -1, 0, light);
                }
                // left
                {
                    vertex(matrix, normal, miscVertex, maxX, maxY, backUOne, backVMin, +0.5F, -1, 0, 0, light);
                    vertex(matrix, normal, miscVertex, maxX, minY, backUOne, backVMax, +0.5F, -1, 0, 0, light);
                    vertex(matrix, normal, miscVertex, maxX, minY, backUMin, backVMax, -0.5F, -1, 0, 0, light);
                    vertex(matrix, normal, miscVertex, maxX, maxY, backUMin, backVMin, -0.5F, -1, 0, 0, light);
                }
                // right
                {
                    vertex(matrix, normal, miscVertex, minX, maxY, backUOne, backVMin, -0.5F, +1, 0, 0, light);
                    vertex(matrix, normal, miscVertex, minX, minY, backUOne, backVMax, -0.5F, +1, 0, 0, light);
                    vertex(matrix, normal, miscVertex, minX, minY, backUMin, backVMax, +0.5F, +1, 0, 0, light);
                    vertex(matrix, normal, miscVertex, minX, maxY, backUMin, backVMin, +0.5F, +1, 0, 0, light);
                }
            }
        }
    }
    
    private static void vertex(Matrix4f matrix, Matrix3f normal, VertexConsumer vertexConsumer, float x, float y, float u, float v, float z, int nX, int nY, int nZ, int light) {
        
        vertexConsumer.vertex(matrix, x, y, z)
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, (float) nX, (float) nY, (float) nZ)
                .endVertex();
    }
    
}
