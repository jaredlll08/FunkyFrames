#version 150

#define TEX_WIDTH 32f
#define TEX_HEIGHT 32f

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
    st = tex_to_quad(st);

    st.y = st.y + 0.01 * sin(time + map(sin(time), -1, 1, 10, 20) * st.x);
    if (inside(st, scale(vec2(5, 5)), scale(vec2(25, 15)))) {
        rawColor = vec4(.33, .8, .98, 1);
    }
    if (inside(st, scale(vec2(5, 7)), scale(vec2(25, 13)))) {
        rawColor = vec4(.96, .65, .72, 1);
    }
    if (inside(st, scale(vec2(5, 9)), scale(vec2(25, 11)))) {
        rawColor = vec4(1, 1, 1, 1);
    }

    vec4 color = rawColor * vertexColor * ColorModulator;

    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}