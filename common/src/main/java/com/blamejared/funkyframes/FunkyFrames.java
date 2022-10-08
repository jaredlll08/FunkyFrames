package com.blamejared.funkyframes;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunkyFrames {
    
    public static final String MOD_ID = "funkyframes";
    public static final String MOD_NAME = "Funky Frames";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    
    public static void init() {
        
        FunkyFramePaintings.loadClass();
    }
    
    public static ResourceLocation rl(String name) {
        
        return new ResourceLocation(MOD_ID, name);
    }
    
}
