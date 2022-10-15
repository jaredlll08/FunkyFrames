#version 150

#define TEX_WIDTH 64f
#define TEX_HEIGHT 64f

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

    vec2 cauldronCenter = vec2(35. / 64., 42. / 64.);

    vec4 rawColor = texture(Sampler0, st);
    vec4 newColor = vec4(rawColor.rgb, 1);

    if (rawColor.a < 1) {
        vec2 currentQuad = tex_to_quad(st) * 2.;
        float noiseMap = mapSnoise(vec3(currentQuad, time), 0, 1);
        rotate2dAround(cauldronCenter*2, time, currentQuad);

        vec2 q = vec2(0.);
        q.x = fbm(currentQuad);
        q.y = fbm(currentQuad);

        vec2 r = vec2(0.);
        r.x = fbm(currentQuad + q + 0.15 * time);
        r.y = fbm(currentQuad + q + 0.126 * time);

        float f = fbm(currentQuad + r);
        newColor.rgb = mix(vec3(0.666, 0.4, 0.666), vec3(0.5, noiseMap, 0.5), clamp((f*f)*4.0, 0.0, 1.0));
        newColor.rgb = mix(newColor.rgb, vec3(0.164706, 0, 0.864706), clamp(length(q), 0.0, 1.0));
        newColor.rgb = mix(newColor.rgb, vec3(0.666667, noiseMap, 0.666667), clamp(length(r.x), 0.0, 1.0));

        newColor.rgb = mix(newColor.rgb * 1.5, rawColor.rgb, rawColor.a);
    }

    vec4 color = newColor * vertexColor * ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}