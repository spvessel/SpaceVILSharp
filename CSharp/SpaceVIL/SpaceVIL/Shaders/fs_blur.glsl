#version 330
uniform sampler2D tex;
uniform vec2 frame;
uniform float res;
uniform float weights[100];
uniform int isFirst;
in vec2 fragTexCoord;

vec4 blur0(sampler2D image, vec2 uv, vec2 resolution) 
{
	vec4 color = vec4(0.0);
	vec4 tmp1 = vec4(0.0);
    vec4 tmp2 = vec4(0.0);

	float uvx = uv.x * resolution.x;
	float uvy = uv.y * resolution.y;

    color = texture2D(image, uv) * weights[0];

	//for (int i = -res; i <= res; i++) {
    for (int i = 1; i <= 4; i++) {
		if (uvx - i < 0) 
		{
			tmp1 = texture2D(image, uv);//vec4(0.0);
		}
		else {
            tmp1 = texture2D(image, uv + vec2( -i * res * 1f / resolution.x, 0.0));
		}

        if (uvx + i >= resolution.x) 
            tmp2 = texture2D(image, uv);
        else 
            tmp2 = texture2D(image, uv + vec2(i * res * 1f / resolution.x, 0.0));

		color += tmp1 * weights[i];
        color += tmp2 * weights[i];
	}	
	return color;
}

vec4 blur1(sampler2D image, vec2 uv, vec2 resolution) 
{
	vec4 color = vec4(0.0);
	vec4 tmp1 = vec4(0.0);
    vec4 tmp2 = vec4(0.0);

	float uvx = uv.x * resolution.x;
	float uvy = uv.y * resolution.y;

    color = texture2D(image, uv) * weights[0];

	// for (int j = -res; j <= res; j++) {
    for (int j = 1; j <= 4; j++) {
		if (uvy - j < 0 ) 
		{
			tmp1 = texture2D(image, uv);//vec4(0.0);
		}
		else {
            tmp1 = texture2D(image, uv + vec2(0.0, -j * res * 1f/ resolution.y));
		}

        if (uvy + j >= resolution.y)
            tmp2 = texture2D(image, uv);
        else
			tmp2 = texture2D(image, uv + vec2(0.0, j * res * 1f/ resolution.y));


		color += tmp1 * weights[j];
        color += tmp2 * weights[j];
	}
		
	return color;
}

void main()
{
    if (isFirst == 1)
		gl_FragColor = blur0(tex, fragTexCoord, frame.xy);
	else gl_FragColor = blur1(tex, fragTexCoord, frame.xy);
}