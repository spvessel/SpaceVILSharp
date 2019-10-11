#version 330
layout (location = 0) in vec2 vPosition;
layout (location = 1) in vec2 verTexCoord;
uniform vec4 position;
uniform float level;
out vec2 fragTexCoord;
void main()
{
    float offset_x = ((vPosition.x + position.x) / position.z) * 2.0 - 1.0;
    float offset_y = ((vPosition.y + position.y) / position.w * 2.0 - 1.0) * (-1.0);
	gl_Position = vec4(offset_x, offset_y, level, 1.0);

	fragTexCoord = verTexCoord;
}