package com.blamejared.funkyframes.platform.services;

import com.google.auto.service.AutoService;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

@AutoService(IPlatformHelper.class)
public class ForgePlatformHelper implements IPlatformHelper {
    
    @Override
    public String getPlatformName() {
        
        return "Forge";
    }
    
    @Override
    public boolean isModLoaded(String modId) {
        
        return ModList.get().isLoaded(modId);
    }
    
    @Override
    public boolean isDevelopmentEnvironment() {
        
        return !FMLLoader.isProduction();
    }
    
}
