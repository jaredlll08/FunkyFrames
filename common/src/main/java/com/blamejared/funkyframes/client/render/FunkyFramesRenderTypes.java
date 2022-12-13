package com.blamejared.funkyframes.client.render;

import com.blamejared.funkyframes.FunkyFrames;
import com.blamejared.funkyframes.client.render.shader.FixedMultiTextureStateShard;
import com.blamejared.funkyframes.util.SelfKeyable;
import com.blamejared.funkyframes.util.SelfKeyedHashMap;
import com.blamejared.funkyframes.util.Texture;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class FunkyFramesRenderTypes extends RenderType {
    
    private static final Map<String, ShaderRenderType> RENDER_TYPES = Util.make(() -> {
        SelfKeyedHashMap<String, ShaderRenderType> map = new SelfKeyedHashMap<>();
        map.add(basic("portal"));
        map.add(basic("trans_flag"));
        map.add(basic("troubles_brewing"));
        
        return Collections.unmodifiableMap(map);
    });
    
    private static ShaderRenderType basic(String name) {
        
        return new ShaderRenderType(name, DefaultVertexFormat.NEW_ENTITY, (textures, renderType) -> {
            CompositeState compState = CompositeState.builder()
                    .setShaderState(renderType.shaderState)
                    .setTextureState(new FixedMultiTextureStateShard(textures))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .createCompositeState(true);
            return create(renderType.formattedName(), renderType.format, VertexFormat.Mode.QUADS, 256, true, false, compState);
        });
    }
    
    private static final Map<String, ShaderRenderType> RENDER_TYPES_VIEW = Collections.unmodifiableMap(RENDER_TYPES);
    
    public static Map<String, ShaderRenderType> getRenderTypes() {
        
        return RENDER_TYPES_VIEW;
    }
    
    public static ShaderRenderType getRenderType(String name) {
        
        return getRenderTypes().get(name);
    }
    
    private FunkyFramesRenderTypes(String s, VertexFormat v, VertexFormat.Mode m, int i, boolean b, boolean b2, Runnable r, Runnable r2) {
        
        super(s, v, m, i, b, b2, r, r2);
        throw new IllegalStateException("This class is not meant to be constructed!");
    }
    
    
    public static class ShaderRenderType implements SelfKeyable<String> {
        
        private final String name;
        public ShaderInstance shader;
        private final ShaderStateShard shaderState = new ShaderStateShard(() -> shader);
        private final VertexFormat format;
        private final BiFunction<List<Texture>, ShaderRenderType, RenderType> builder;
        
        public ShaderRenderType(String name, VertexFormat format, BiFunction<List<Texture>, ShaderRenderType, RenderType> builder) {
            
            this.name = name;
            this.format = format;
            this.builder = Util.memoize(builder);
        }
        
        public RenderType using(List<Texture> textures) {
            
            return builder.apply(textures, this);
        }
        
        public void register(ResourceProvider resourceManager, BiConsumer<ShaderInstance, Consumer<ShaderInstance>> registerFunc) throws IOException {
            
            registerFunc.accept(new ShaderInstance(resourceManager, formattedName(), format), this::shader);
        }
        
        public String formattedName() {
            
            return FunkyFrames.MOD_ID + "_" + name;
        }
        
        public String name() {
            
            return name;
        }
        
        @Override
        public String getKey() {
            
            return name();
        }
        
        public VertexFormat format() {
            
            return format;
        }
        
        private void shader(ShaderInstance shader) {
            
            this.shader = shader;
        }
        
    }
    
}
