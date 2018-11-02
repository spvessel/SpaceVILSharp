#version 430
uniform sampler2D tex;
uniform vec2 frame;
uniform int res;
uniform float weights[100];
in vec2 fragTexCoord;

vec4 blur(sampler2D image, vec2 uv, vec2 resolution) 
{
	vec4 color = vec4(0.0);
	vec4 tmp = vec4(0.0);

	float uvx = uv.x * resolution.x;
	float uvy = uv.y * resolution.y;

	for (int i = -res; i <= res; i++) {
		for (int j = -res; j <= res; j++) {
			if (uvx + i < 0 || uvy + j < 0 || uvx + i >= resolution.x || uvy + j >= resolution.y)
			{
				tmp = texture2D(image, uv);//vec4(0.0);
			}
			else {
			tmp = texture2D(image, uv + vec2(i * 1f / resolution.x, j * 1f/ resolution.y));
			}

			color += tmp * weights[abs(i)] * weights[abs(j)];
		}
	}	
	return color;
}

void main()
{
    gl_FragColor = blur(tex, fragTexCoord, frame.xy);
}