package com.blamejared.funkyframes.platform.services;

import com.google.auto.service.AutoService;
import net.fabricmc.loader.api.FabricLoader;

@AutoService(IPlatformHelper.class)
public class FabricPlatformHelper implements IPlatformHelper {
    
    @Override
    public String getPlatformName() {
        
        return "Fabric";
    }
    
    @Override
    public boolean isModLoaded(String modId) {
        
        return FabricLoader.getInstance().isModLoaded(modId);
    }
    
    @Override
    public boolean isDevelopmentEnvironment() {
        
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
    
}
