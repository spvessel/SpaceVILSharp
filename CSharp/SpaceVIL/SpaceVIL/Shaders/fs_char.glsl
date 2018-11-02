#version 330
uniform sampler2D tex;
uniform vec4 rgb;
in vec2 fragTexCoord;
out vec4 finalColor;
void main()
{
	vec4 c = texture(tex, fragTexCoord);
	finalColor = vec4(rgb.r, rgb.g, rgb.b, c.a);
	// finalColor = vec4(rgb.r, rgb.g, rgb.b, 1.0);
}