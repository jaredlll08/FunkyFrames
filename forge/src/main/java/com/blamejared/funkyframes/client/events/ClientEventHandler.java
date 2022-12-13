package com.blamejared.funkyframes.client.events;

import com.blamejared.funkyframes.FunkyFrames;
import com.blamejared.funkyframes.client.render.FunkyFramesRenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = FunkyFrames.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventHandler {
    
    @SubscribeEvent
    public static void register(RegisterShadersEvent event) {
        
        try {
            for(FunkyFramesRenderTypes.ShaderRenderType type : FunkyFramesRenderTypes.getRenderTypes().values()) {
                type.register(event.getResourceProvider(), event::registerShader);
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
