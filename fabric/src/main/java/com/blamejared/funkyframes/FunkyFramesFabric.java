package com.blamejared.funkyframes;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.Items;

public class FunkyFramesFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        FunkyFrames.init();
    }
    
}
