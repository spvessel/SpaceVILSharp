#version 330
uniform sampler2D tex;
uniform vec2 frame;
uniform float res;
uniform float weights[100];
in vec2 fragTexCoord;

vec4 blur(sampler2D image, vec2 uv, vec2 resolution) 
{
	vec4 color = vec4(0.0);
	vec4 tmp1 = vec4(0.0);
    vec4 tmp2 = vec4(0.0);

	float uvx = uv.x * resolution.x;
	float uvy = uv.y * resolution.y;

	for (int i = -4; i <= 4; i++) {
		for (int j = -4; j <= 4; j++) {	
			tmp = texture2D(image, uv + vec2(i * 1f / resolution.x, j * 1f/ resolution.y));
			color += tmp * weights[abs(j)] * weights[abs(i)];
		}
	}
	return texture2D(image, uv);//color;
}


void main()
{
	gl_FragColor = blur(tex, fragTexCoord, frame.xy);
}