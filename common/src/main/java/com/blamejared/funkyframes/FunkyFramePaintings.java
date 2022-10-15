package com.blamejared.funkyframes;

import com.blamejared.funkyframes.platform.registration.RegistrationProvider;
import com.blamejared.funkyframes.platform.registration.RegistryObject;
import com.blamejared.funkyframes.util.FunkyFramePaintingVariant;
import com.blamejared.funkyframes.util.Texture;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.decoration.PaintingVariant;

import java.util.List;

public class FunkyFramePaintings {
    
    /**
     * The provider for items
     */
    public static final RegistrationProvider<PaintingVariant> PAINTINGS = RegistrationProvider.get(Registry.PAINTING_VARIANT_REGISTRY, FunkyFrames.MOD_ID);
    
    private static final RegistryObject<PaintingVariant> PORTAL = PAINTINGS.register("portal", () -> new FunkyFramePaintingVariant(16, 16, "portal", List.of(new Texture(FunkyFrames.rl("textures/painting/portal.png")))));
    private static final RegistryObject<PaintingVariant> TRANS_FLAG = PAINTINGS.register("trans_flag", () -> new FunkyFramePaintingVariant(32, 32, "trans_flag", List.of(new Texture(FunkyFrames.rl("textures/painting/trans_flag.png")))));
    private static final RegistryObject<PaintingVariant> TROUBLES_BREWING = PAINTINGS.register("troubles_brewing", () -> new FunkyFramePaintingVariant(64, 64, "troubles_brewing", List.of(new Texture(FunkyFrames.rl("textures/painting/troubles_brewing.png")))));
    
    
    
    public static void loadClass() {}
    
}
