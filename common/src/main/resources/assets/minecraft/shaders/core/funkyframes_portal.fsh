#version 150

#define TEX_WIDTH 16.0
#define TEX_HEIGHT 16.0

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
    float time = sin(GameTime) * 720.;
    vec2 st = texCoord0;
    vec4 rawColor = texture(Sampler0, st);

    float noise = map(snoise(vec3(quad(st), time)), -1, 1, 0.7, 1);

    replace(rawColor, vec4(1, 0, 1, 1), vec4(noise, 0, noise, 1));

    vec4 color = rawColor * vertexColor * ColorModulator;

    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}