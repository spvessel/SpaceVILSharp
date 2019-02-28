#version 330
uniform sampler2D tex;
uniform int overlay;
uniform vec4 rgb;
in vec2 fragTexCoord;
layout(location = 0) out vec4 finalColor;
void main()
{
    finalColor = texture(tex, fragTexCoord);
    if(overlay == 1)
    {
        vec4 c = finalColor;
        finalColor = vec4(rgb.r, rgb.g, rgb.b, c.a);
    }
}