package com.blamejared.funkyframes.mixin.client.transform;

import com.blamejared.funkyframes.util.FunkyFramePaintingVariant;
import com.blamejared.funkyframes.client.render.RenderPainting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.PaintingTextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Painting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PaintingRenderer.class)
public abstract class MixinPaintingRenderer extends EntityRenderer<Painting> {
    
    @Shadow
    public abstract ResourceLocation getTextureLocation(Painting $$0);
    
    @Shadow
    protected abstract void renderPainting(PoseStack $$0, VertexConsumer $$1, Painting $$2, int $$3, int $$4, TextureAtlasSprite $$5, TextureAtlasSprite $$6);
    
    @Shadow
    protected abstract void vertex(Matrix4f p_115537_, Matrix3f p_115538_, VertexConsumer p_115539_, float p_115540_, float p_115541_, float p_115542_, float p_115543_, float p_115544_, int p_115545_, int p_115546_, int p_115547_, int p_115548_);
    
    protected MixinPaintingRenderer(EntityRendererProvider.Context $$0) {
        
        super($$0);
    }
    
    @Inject(method = "render(Lnet/minecraft/world/entity/decoration/Painting;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/PaintingRenderer;renderPainting(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/decoration/Painting;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"), cancellable = true)
    public void funkyframes$render(Painting painting, float $$1, float $$2, PoseStack pose, MultiBufferSource buffers, int packedLight, CallbackInfo ci) {
        
        if(painting.getVariant().value() instanceof FunkyFramePaintingVariant ffVariant) {
            
            PaintingTextureManager paintingtexturemanager = Minecraft.getInstance().getPaintingTextures();
            
            RenderPainting.renderPainting(this, pose, buffers, painting, ffVariant, paintingtexturemanager.getBackSprite());
            pose.popPose();
            super.render(painting, $$1, $$2, pose, buffers, packedLight);
            ci.cancel();
        }
    }
    
}
