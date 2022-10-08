package com.blamejared.funkyframes.util;

import com.blamejared.funkyframes.util.Texture;
import net.minecraft.world.entity.decoration.PaintingVariant;

import java.util.List;

public class FunkyFramePaintingVariant extends PaintingVariant {
    
    private final String renderType;
    private final List<Texture> textures;
    
    public FunkyFramePaintingVariant(int width, int height, String renderType, List<Texture> textures) {
        
        super(width, height);
        this.renderType = renderType;
        this.textures = textures;
    }
    
    public String renderType() {
        
        return renderType;
    }
    
    public List<Texture> textures() {
        
        return textures;
    }
    
}
