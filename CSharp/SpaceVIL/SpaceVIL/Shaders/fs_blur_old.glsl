#version 430
uniform sampler2D tex;
uniform vec2 frame;
uniform int res;
uniform float weights[100];
uniform int isFirst;
in vec2 fragTexCoord;

vec4 blur0(sampler2D image, vec2 uv, vec2 resolution) 
{
	vec4 color = vec4(0.0);
	vec4 tmp = vec4(0.0);

	float uvx = uv.x * resolution.x;
	float uvy = uv.y * resolution.y;

	for (int i = -res; i <= res; i++) {
		if (uvx + i < 0 || uvx + i >= resolution.x)
		{
			tmp = texture2D(image, uv);//vec4(0.0);
		}
		else {
			tmp = texture2D(image, uv + vec2(i * 1f / resolution.x, 0.0));
		}

		color += tmp * weights[abs(i)];
	}	
	return color;
}

vec4 blur1(sampler2D image, vec2 uv, vec2 resolution) 
{
	vec4 color = vec4(0.0);
	vec4 tmp = vec4(0.0);

	float uvx = uv.x * resolution.x;
	float uvy = uv.y * resolution.y;

	for (int j = -res; j <= res; j++) {
		if (uvy + j < 0 || uvy + j >= resolution.y)
		{
			tmp = texture2D(image, uv);//vec4(0.0);
		}
		else {
			tmp = texture2D(image, uv + vec2(0.0, j * 1f/ resolution.y));
		}

		color += tmp * weights[abs(j)];
	}
		
	return color;
}

void main()
{
    if (isFirst == 1)
		gl_FragColor = blur0(tex, fragTexCoord, frame.xy);
	else gl_FragColor = blur1(tex, fragTexCoord, frame.xy);
}