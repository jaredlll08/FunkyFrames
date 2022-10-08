package com.blamejared.funkyframes.client.render.shader;

import com.blamejared.funkyframes.util.Texture;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * The vanilla {@link net.minecraft.client.renderer.RenderStateShard.MultiTextureStateShard} class uses `i++` to set each texture,
 * however the lightmap and overlay texture are hardcoded to use id 1 and 2 respectively. Which overrides any texture set to those IDs.
 * This class will skip ID 1 and 2, and start putting any extra textures on ID 3 and above.
 */
public class FixedMultiTextureStateShard extends RenderStateShard.EmptyTextureStateShard {
    
    private final Optional<ResourceLocation> cutoutTexture;
    
    public FixedMultiTextureStateShard(List<Texture> textures) {
        
        super(() -> {
            int i = 0;
            
            for(Texture texture : textures) {
                TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
                texturemanager.getTexture(texture.location()).setFilter(texture.blur(), texture.mipmap());
                RenderSystem.setShaderTexture(i, texture.location());
                if(i == 0) {
                    i = 3;
                } else {
                    i++;
                }
            }
            
        }, () -> {
        });
        this.cutoutTexture = textures.stream().findFirst().map(Texture::location);
    }
    
    protected @NotNull Optional<ResourceLocation> cutoutTexture() {
        
        return this.cutoutTexture;
    }
    
}
