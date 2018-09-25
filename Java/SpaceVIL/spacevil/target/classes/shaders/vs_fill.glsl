#version 420 core
layout(location = 0) in vec3 vPosition;
layout(location = 1) in vec4 vertexColor;
out vec4 fragmentColor;
void main() 
{
	gl_Position = vec4(vPosition, 1.0);
	fragmentColor = vertexColor;
}
