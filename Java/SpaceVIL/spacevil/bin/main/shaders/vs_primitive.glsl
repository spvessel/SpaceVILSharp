#version 330
layout(location = 0) in vec2 vPosition;
uniform vec4 position;
uniform float level;
void main() 
{
    float offset_x = ((vPosition.x + position.x) / position.z) * 2.0 - 1.0;
    float offset_y = ((vPosition.y + position.y) / position.w * 2.0 - 1.0) * (-1.0);
	gl_Position = vec4(offset_x, offset_y, level, 1.0);

	// gl_Position = vec4(vPosition, 1.0);
}
