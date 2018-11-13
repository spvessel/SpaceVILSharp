#version 330
uniform sampler2D tex;
uniform vec2 frame;
uniform float res;
uniform float weights[100];
uniform vec2 xy;
uniform vec2 wh;
in vec2 fragTexCoord;

vec4 blur(sampler2D image, vec2 uv, vec2 resolution)
{
	int rad = 5;
	float uvx = uv.x * resolution.x;
	float uvy = resolution.y - uv.y * resolution.y;
	vec4 color = vec4(0.0);
	vec4 tmp = vec4(0.0);

	if ((uvx >= xy.x - rad && uvx <= xy.x + wh.x + rad &&
		uvy >= xy.y - rad && uvy <= xy.y + wh.y + rad) &&
		(uvx < xy.x + rad || uvx > xy.x + wh.x - rad ||
		uvy < xy.y + rad || uvy > xy.y + wh.y - rad))
	{

		for (int i = -rad; i <= rad; i++) {
			for (int j = -rad; j <= rad; j++) {
				tmp = texture2D(image, uv + vec2(i * res * 1f / resolution.x, j * res * 1f/ resolution.y));
				color += tmp * weights[abs(j)] * weights[abs(i)];
			}
		}
		//color = vec4(1.0);
	} else {
		color = vec4(1.0); //texture2D(image, uv);
	}

	return color;
}


void main()
{
	gl_FragColor = blur(tex, fragTexCoord, frame.xy);
}