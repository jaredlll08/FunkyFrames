#version 150

#define TEX_WIDTH 16f
#define TEX_HEIGHT 16f

#moj_import <fog.glsl>

in vec4 vertexColor;
in vec2 texCoord0;
in vec4 lightMapColor;
in float vertexDistance;
in vec4 overlayColor;
in vec4 normal;

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float GameTime;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

out vec4 fragColor;

#moj_import <funkyframes_utils.glsl>

void main()
{
    float time = sin(GameTime) * 580.;
    vec2 st = texCoord0;
    vec4 color = texture(Sampler0, st) * vertexColor * ColorModulator;
    if (inside(st, vec2(scaleW(4), scaleH(6)), vec2(scaleW(6), scaleH(9)))) {
        st = tex_to_quad(st);
        float noise = snoise(vec3(st, time));
        noise = map(noise, -1, 1, 0.4, 0.6);
        color = vec4(noise, 0, noise, 1.);
    }

    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}