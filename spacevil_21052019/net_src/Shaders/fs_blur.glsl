#version 330
uniform sampler2D tex;
uniform vec2 frame;
uniform float res;
uniform float weights[100];
uniform vec2 point;
uniform vec2 size;
in vec2 fragTexCoord;
out vec4 fragColor;
vec4 blur(sampler2D image, vec2 uv, vec2 resolution)
{
	int rad = 5;
	float uvx = uv.x * resolution.x;
	float uvy = resolution.y - uv.y * resolution.y;
	vec4 color = vec4(0.0);
	vec4 tmp = vec4(0.0);

	if (uvx >= point.x - rad && uvx <= point.x + size.x + rad && uvy >= point.y - rad && uvy <= point.y + size.y + rad)
	{
		for (int i = -rad; i <= rad; i++) {
				for (int j = -rad; j <= rad; j++) {
					tmp = texture(image, uv + vec2(i * res * 1.0 / resolution.x, j * res * 1.0/ resolution.y));
					int i_ind = abs(i);
					int j_ind = abs(j);
					color += tmp * weights[j_ind] * weights[i_ind];
				}
			}
	}
	else {
		color = texture(image, uv);
	}
	return color;
}


void main()
{
	fragColor = blur(tex, fragTexCoord, frame.xy);
}