#version 330
uniform sampler2D tex;
uniform vec2 frame;
uniform float res;
uniform float weights[100];
in vec2 fragTexCoord;

vec4 blur(sampler2D image, vec2 uv, vec2 resolution) 
{
	vec4 color = vec4(0.0);
	vec4 tmp = vec4(0.0);

	float uvx = uv.x * resolution.x;
	float uvy = uv.y * resolution.y;

	for (int i = -10; i <= 10; i++) {
		for (int j = -10; j <= 10; j++) {	
			tmp = texture2D(image, uv + vec2(i * res * 1f / resolution.x, j * res * 1f/ resolution.y));
			color += tmp * weights[abs(j)] * weights[abs(i)];
		}
	}
	return color;
}


void main()
{
	gl_FragColor = blur(tex, fragTexCoord, frame.xy);
}