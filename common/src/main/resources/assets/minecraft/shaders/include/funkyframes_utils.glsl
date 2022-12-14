#version 150

#define PI 3.14159265359
#define NUM_OCTAVES 5

// General utility methods, can be imported using:
// #moj_import <funkyframes:utils.glsl>
// NOTE: the import needs to happen after ins and uniforms
// have been defined, but before any of these methods need to be called.

/**
 * Scales the given width value to the texture width.
 */
float scaleW(float wIn) {
    return wIn / TEX_WIDTH;
}

/**
 * Scales the given height value to the texture height.
 */
float scaleH(float hIn) {
    return hIn / TEX_HEIGHT;
}

/**
 * Scales the given vector to the texture.
 */
vec2 scale(vec2 vIn) {
    return vIn / vec2(TEX_WIDTH, TEX_HEIGHT);
}

/**
 * Checks if the given uv is inside the bounds of min and max
 */
bool inside(vec2 uv, vec2 min, vec2 max) {
    return uv.x >= min.x && uv.x <= max.x && uv.y >= min.y && uv.y <= max.y;
}

/**
 * Rotates the given uv by the angle
 */
void rotate2d(float angle, inout vec2 uv) {
    mat2 rotMat = mat2(cos(angle), -sin(angle), sin(angle), cos(angle));
    uv = rotMat * uv;
}

/**
 * Rotates the given uv by the angle around the center
 */
void rotate2dAround(vec2 center, float angle, inout vec2 uv) {
    uv -= center;
    rotate2d(angle, uv);
    uv += center;
}

/**
 * Steps the given uv to be the same size as a texture pixel.
 */
vec2 quad(vec2 uv) {
    return uv - vec2(mod(uv.x, 1.0 / TEX_WIDTH), mod(uv.y, 1.0 / TEX_HEIGHT));
}

/**
 * Maps the given value to the given range.
 */
float map(float value, float min1, float max1, float min2, float max2) {
    return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
}

/**
 * Replaces the given color with the new color.
 */
void replace(inout vec4 color, vec4 replace, vec4 with) {
    if (color == replace) {
        color = with;
    }
}

// Source: https://gist.github.com/patriciogonzalezvivo/670c22f3966e662d2f83#simplex-noise
//	Simplex 3D Noise
//	by Ian McEwan, Ashima Arts
//
vec4 permute(vec4 x) { return mod(((x*34.0)+1.0)*x, 289.0); }
vec4 taylorInvSqrt(vec4 r) { return 1.79284291400159 - 0.85373472095314 * r; }

float snoise(vec3 v) {
    const vec2 C = vec2(1.0/6.0, 1.0/3.0);
    const vec4 D = vec4(0.0, 0.5, 1.0, 2.0);

    // First corner
    vec3 i = floor(v + dot(v, C.yyy));
    vec3 x0 = v - i + dot(i, C.xxx);

    // Other corners
    vec3 g = step(x0.yzx, x0.xyz);
    vec3 l = 1.0 - g;
    vec3 i1 = min(g.xyz, l.zxy);
    vec3 i2 = max(g.xyz, l.zxy);

    //  x0 = x0 - 0. + 0.0 * C
    vec3 x1 = x0 - i1 + 1.0 * C.xxx;
    vec3 x2 = x0 - i2 + 2.0 * C.xxx;
    vec3 x3 = x0 - 1. + 3.0 * C.xxx;

    // Permutations
    i = mod(i, 289.0);
    vec4 p = permute(permute(permute(
    i.z + vec4(0.0, i1.z, i2.z, 1.0))
    + i.y + vec4(0.0, i1.y, i2.y, 1.0))
    + i.x + vec4(0.0, i1.x, i2.x, 1.0));

    // Gradients
    // ( N*N points uniformly over a square, mapped onto an octahedron.)
    float n_ = 1.0/7.0;// N=7
    vec3  ns = n_ * D.wyz - D.xzx;

    vec4 j = p - 49.0 * floor(p * ns.z *ns.z);//  mod(p,N*N)

    vec4 x_ = floor(j * ns.z);
    vec4 y_ = floor(j - 7.0 * x_);// mod(j,N)

    vec4 x = x_ *ns.x + ns.yyyy;
    vec4 y = y_ *ns.x + ns.yyyy;
    vec4 h = 1.0 - abs(x) - abs(y);

    vec4 b0 = vec4(x.xy, y.xy);
    vec4 b1 = vec4(x.zw, y.zw);

    vec4 s0 = floor(b0)*2.0 + 1.0;
    vec4 s1 = floor(b1)*2.0 + 1.0;
    vec4 sh = -step(h, vec4(0.0));

    vec4 a0 = b0.xzyw + s0.xzyw*sh.xxyy;
    vec4 a1 = b1.xzyw + s1.xzyw*sh.zzww;

    vec3 p0 = vec3(a0.xy, h.x);
    vec3 p1 = vec3(a0.zw, h.y);
    vec3 p2 = vec3(a1.xy, h.z);
    vec3 p3 = vec3(a1.zw, h.w);

    //Normalise gradients
    vec4 norm = taylorInvSqrt(vec4(dot(p0, p0), dot(p1, p1), dot(p2, p2), dot(p3, p3)));
    p0 *= norm.x;
    p1 *= norm.y;
    p2 *= norm.z;
    p3 *= norm.w;

    // Mix final noise value
    vec4 m = max(0.6 - vec4(dot(x0, x0), dot(x1, x1), dot(x2, x2), dot(x3, x3)), 0.0);
    m = m * m;
    return 42.0 * dot(m*m, vec4(dot(p0, x0), dot(p1, x1),
    dot(p2, x2), dot(p3, x3)));
}

float mapSnoise(vec3 v, float min, float max) {
    return map(snoise(v), -1, 1, min, max);
}

// Source: https://thebookofshaders.com/13/

float fbm ( in vec2 _st) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(100.0);
    // Rotate to reduce axial bias
    mat2 rot = mat2(cos(0.5), sin(0.5),
    -sin(0.5), cos(0.50));
    for (int i = 0; i < NUM_OCTAVES; ++i) {
        v += a * snoise(vec3(_st, 0));
        _st = rot * _st * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}