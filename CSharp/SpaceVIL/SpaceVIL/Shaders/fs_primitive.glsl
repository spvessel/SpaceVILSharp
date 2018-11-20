#version 330
// in vec4 fragmentColor;
// out vec4 color;
uniform vec4 background;
void main()
{
	// color = fragmentColor;
    gl_FragColor = background;
}
