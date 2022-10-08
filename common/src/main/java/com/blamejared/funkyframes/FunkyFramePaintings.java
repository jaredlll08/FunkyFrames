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
    
    public static void loadClass() {}
    
}
