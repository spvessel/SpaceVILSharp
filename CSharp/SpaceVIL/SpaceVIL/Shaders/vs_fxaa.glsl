#version 420 core

layout (location = 0) in vec3 vert;
layout (location = 1) in vec2 verTexCoord;
out vec2 v_texCoord;

void main(void)
{
	v_texCoord = verTexCoord;
	gl_Position = vec4(vert, 1.0f);
}