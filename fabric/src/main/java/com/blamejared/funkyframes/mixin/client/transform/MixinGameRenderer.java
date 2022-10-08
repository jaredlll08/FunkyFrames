package com.blamejared.funkyframes.mixin.client.transform;

import com.blamejared.funkyframes.client.render.FunkyFramesRenderTypes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    
    @ModifyVariable(method = "reloadShaders", at = @At(value = "STORE", target = "Lnet/minecraft/client/renderer/GameRenderer;reloadShaders(Lnet/minecraft/server/packs/resources/ResourceManager;)V"), print = true, index = 3, ordinal = 1)
    public List<Pair<ShaderInstance, Consumer<ShaderInstance>>> modify(List<Pair<ShaderInstance, Consumer<ShaderInstance>>> value, ResourceManager resourceManager) {
        
        try {
            for(FunkyFramesRenderTypes.ShaderRenderType type : FunkyFramesRenderTypes.getRenderTypes().values()) {
                type.register(resourceManager, (shaderInstance, onLoaded) -> value.add(Pair.of(shaderInstance, onLoaded)));
            }
        } catch(IOException e) {
            value.forEach((p_172729_) -> {
                p_172729_.getFirst().close();
            });
            throw new RuntimeException("could not reload shaders", e);
        }
        return value;
    }
    
}
