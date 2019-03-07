#version 330
uniform sampler2D tex;
uniform int overlay;
uniform vec4 rgb;
uniform float alpha;
in vec2 fragTexCoord;
layout(location = 0) out vec4 finalColor;
void main()
{
    vec2 coord = fragTexCoord;
    float sin_factor = sin(alpha);
    float cos_factor = cos(alpha);
    coord = (coord - 0.5) * mat2(cos_factor, sin_factor, -sin_factor, cos_factor);
    coord += 0.5;

    finalColor = texture(tex, coord);
    if(overlay == 1)
    {
        vec4 c = finalColor;
        finalColor = vec4(rgb.r, rgb.g, rgb.b, c.a);
    }
}