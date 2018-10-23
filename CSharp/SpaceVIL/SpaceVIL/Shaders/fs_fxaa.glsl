#version 420 core

uniform sampler2D tex; 
uniform vec2 frame;
uniform int is_enable;

in vec2 v_texCoord;

out vec4 fragColor;

float rgb2luma(vec3 rgb){
    return sqrt(dot(rgb, vec3(0.299, 0.587, 0.114)));
}

void main(void)
{
    if(is_enable == 0)
    {
        fragColor = texture(tex, v_texCoord);
        return;
    }

    // vec2 inverseScreenSize = vec2(1.0, 1.0);
    // inverseScreenSize = inverseScreenSize/frame;
    // float EDGE_THRESHOLD_MIN = 0.0312;
    // float EDGE_THRESHOLD_MAX = 0.125
    // vec3 colorCenter = texture2D(tex,v_texCoord).rgb;

    // // Luma at the current fragment
    // float lumaCenter = rgb2luma(colorCenter);

    // // Luma at the four direct neighbours of the current fragment.
    // float lumaDown = rgb2luma(texture2D(tex,v_texCoord,ivec2(0,-1)).rgb);
    // float lumaUp = rgb2luma(texture2D(tex,v_texCoord,ivec2(0,1)).rgb);
    // float lumaLeft = rgb2luma(texture2D(tex,v_texCoord,ivec2(-1,0)).rgb);
    // float lumaRight = rgb2luma(texture2D(tex,v_texCoord,ivec2(1,0)).rgb);

    // // Find the maximum and minimum luma around the current fragment.
    // float lumaMin = min(lumaCenter,min(min(lumaDown,lumaUp),min(lumaLeft,lumaRight)));
    // float lumaMax = max(lumaCenter,max(max(lumaDown,lumaUp),max(lumaLeft,lumaRight)));

    // // Compute the delta.
    // float lumaRange = lumaMax - lumaMin;

    // // If the luma variation is lower that a threshold (or if we are in a really dark area), we are not on an edge, don't perform any AA.
    // // if(lumaRange < max(EDGE_THRESHOLD_MIN,lumaMax*EDGE_THRESHOLD_MAX))
    // // {
    // //     fragColor = colorCenter;
    // //     return;
    // // }

    // // Query the 4 remaining corners lumas.
    // float lumaDownLeft = rgb2luma(texture2D(tex,v_texCoord,ivec2(-1,-1)).rgb);
    // float lumaUpRight = rgb2luma(texture2D(tex,v_texCoord,ivec2(1,1)).rgb);
    // float lumaUpLeft = rgb2luma(texture2D(tex,v_texCoord,ivec2(-1,1)).rgb);
    // float lumaDownRight = rgb2luma(texture2D(tex,v_texCoord,ivec2(1,-1)).rgb);
    
    // // Combine the four edges lumas (using intermediary variables for future computations with the same values).
    // float lumaDownUp = lumaDown + lumaUp;
    // float lumaLeftRight = lumaLeft + lumaRight;
    
    // // Same for corners
    // float lumaLeftCorners = lumaDownLeft + lumaUpLeft;
    // float lumaDownCorners = lumaDownLeft + lumaDownRight;
    // float lumaRightCorners = lumaDownRight + lumaUpRight;
    // float lumaUpCorners = lumaUpRight + lumaUpLeft;
    
    // // Compute an estimation of the gradient along the horizontal and vertical axis.
    // float edgeHorizontal =  abs(-2.0 * lumaLeft + lumaLeftCorners)  + abs(-2.0 * lumaCenter + lumaDownUp ) * 2.0    + abs(-2.0 * lumaRight + lumaRightCorners);
    // float edgeVertical =    abs(-2.0 * lumaUp + lumaUpCorners)      + abs(-2.0 * lumaCenter + lumaLeftRight) * 2.0  + abs(-2.0 * lumaDown + lumaDownCorners);
    
    // // Is the local edge horizontal or vertical ?
    // bool isHorizontal = (edgeHorizontal >= edgeVertical);
    
    // // Select the two neighboring texels lumas in the opposite direction to the local edge.
    // float luma1 = isHorizontal ? lumaDown : lumaLeft;
    // float luma2 = isHorizontal ? lumaUp : lumaRight;
    // // Compute gradients in this direction.
    // float gradient1 = luma1 - lumaCenter;
    // float gradient2 = luma2 - lumaCenter;
    
    // // Which direction is the steepest ?
    // bool is1Steepest = abs(gradient1) >= abs(gradient2);
    
    // // Gradient in the corresponding direction, normalized.
    // float gradientScaled = 0.25*max(abs(gradient1),abs(gradient2));
    
    // // Choose the step size (one pixel) according to the edge direction.
    // float stepLength = isHorizontal ? inverseScreenSize.y : inverseScreenSize.x;
    
    // // Average luma in the correct direction.
    // float lumaLocalAverage = 0.0;
    
    // if(is1Steepest){
    //     // Switch the direction
    //     stepLength = - stepLength;
    //     lumaLocalAverage = 0.5*(luma1 + lumaCenter);
    // } else {
    //     lumaLocalAverage = 0.5*(luma2 + lumaCenter);
    // }
    
    // // Shift UV in the correct direction by half a pixel.
    // vec2 currentUv = v_texCoord;
    // if(isHorizontal)
    // {
    //     currentUv.y += stepLength * 0.5;
    // } else {
    //     currentUv.x += stepLength * 0.5;
    // }
    
    // vec2 offset = isHorizontal ? vec2(inverseScreenSize.x,0.0) : vec2(0.0,inverseScreenSize.y);
    // // Compute UVs to explore on each side of the edge, orthogonally. The QUALITY allows us to step faster.
    // vec2 uv1 = currentUv - offset;
    // vec2 uv2 = currentUv + offset;
    
    // // Read the lumas at both current extremities of the exploration segment, and compute the delta wrt to the local average luma.
    // float lumaEnd1 = rgb2luma(texture2D(tex,uv1).rgb);
    // float lumaEnd2 = rgb2luma(texture2D(tex,uv2).rgb);
    // lumaEnd1 -= lumaLocalAverage;
    // lumaEnd2 -= lumaLocalAverage;
    
    // // If the luma deltas at the current extremities are larger than the local gradient, we have reached the side of the edge.
    // bool reached1 = abs(lumaEnd1) >= gradientScaled;
    // bool reached2 = abs(lumaEnd2) >= gradientScaled;
    // bool reachedBoth = reached1 && reached2;
    
    // // If the side is not reached, we continue to explore in this direction.
    // if(!reached1){
    //     uv1 -= offset;
    // }
    // if(!reached2){
    //     uv2 += offset;
    // }   

    // int ITERATIONS = 12;    
    // if(!reachedBoth)
    // {
    //     for(int i = 2; i < ITERATIONS; i++){
    //         // If needed, read luma in 1st direction, compute delta.
    //         if(!reached1){
    //             lumaEnd1 = rgb2luma(texture2D(tex, uv1).rgb);
    //             lumaEnd1 = lumaEnd1 - lumaLocalAverage;
    //         }
    //         // If needed, read luma in opposite direction, compute delta.
    //         if(!reached2){
    //             lumaEnd2 = rgb2luma(texture2D(tex, uv2).rgb);
    //             lumaEnd2 = lumaEnd2 - lumaLocalAverage;
    //         }
    //         // If the luma deltas at the current extremities is larger than the local gradient, we have reached the side of the edge.
    //         reached1 = abs(lumaEnd1) >= gradientScaled;
    //         reached2 = abs(lumaEnd2) >= gradientScaled;
    //         reachedBoth = reached1 && reached2;
    
    //         // If the side is not reached, we continue to explore in this direction, with a variable quality.
    //         // if(!reached1){
    //         //     uv1 -= offset * QUALITY(i);
    //         // }
    //         // if(!reached2){
    //         //     uv2 += offset * QUALITY(i);
    //         // }
    
    //         // If both sides have been reached, stop the exploration.
    //         if(reachedBoth){ break;}
    //     }
    // }
    
    // // Compute the distances to each extremity of the edge.
    // float distance1 = isHorizontal ? (v_texCoord.x - uv1.x) : (v_texCoord.y - uv1.y);
    // float distance2 = isHorizontal ? (uv2.x - v_texCoord.x) : (uv2.y - v_texCoord.y);
    
    // // In which direction is the extremity of the edge closer ?
    // bool isDirection1 = distance1 < distance2;
    // float distanceFinal = min(distance1, distance2);
    
    // // Length of the edge.
    // float edgeThickness = (distance1 + distance2);
    
    // // UV offset: read in the direction of the closest side of the edge.
    // float pixelOffset = - distanceFinal / edgeThickness + 0.5;
    
    // // Is the luma at center smaller than the local average ?
    // bool isLumaCenterSmaller = lumaCenter < lumaLocalAverage;
    
    // // If the luma at center is smaller than at its neighbour, the delta luma at each end should be positive (same variation).
    // // (in the direction of the closer side of the edge.)
    // bool correctVariation = ((isDirection1 ? lumaEnd1 : lumaEnd2) < 0.0) != isLumaCenterSmaller;
    
    // // If the luma variation is incorrect, do not offset.
    // float finalOffset = correctVariation ? pixelOffset : 0.0;
    // // Sub-pixel shifting
    // // Full weighted average of the luma over the 3x3 neighborhood.
    // float lumaAverage = (1.0/12.0) * (2.0 * (lumaDownUp + lumaLeftRight) + lumaLeftCorners + lumaRightCorners);
    // // Ratio of the delta between the global average and the center luma, over the luma range in the 3x3 neighborhood.
    // float subPixelOffset1 = clamp(abs(lumaAverage - lumaCenter)/lumaRange,0.0,1.0);
    // float subPixelOffset2 = (-2.0 * subPixelOffset1 + 3.0) * subPixelOffset1 * subPixelOffset1;
    // // Compute a sub-pixel offset based on this delta.
    // float SUBPIXEL_QUALITY = 0.75;
    // float subPixelOffsetFinal = subPixelOffset2 * subPixelOffset2 * SUBPIXEL_QUALITY;
    
    // // Pick the biggest of the two offsets.
    // finalOffset = max(finalOffset,subPixelOffsetFinal);
    
    // // Compute the final UV coordinates.
    // vec2 finalUv = v_texCoord;
    // if(isHorizontal)
    // {
    //     finalUv.y += finalOffset * stepLength;
    // }
    // else 
    // {
    //     finalUv.x += finalOffset * stepLength;
    // }
    
    // // Read the color at the new UV coordinates, and use it.
    // vec3 finalColor = texture2D(tex,finalUv).rgb;
    // fragColor = finalColor;

///////////////////////////////////////////////////////////////////////////////////////////////////

    float FXAA_SPAN_MAX = 1.0;
    float FXAA_REDUCE_MUL = 1.0/8.0;
    float FXAA_REDUCE_MIN = 1.0/128.0;

    vec3 rgbNW=texture2D(tex,v_texCoord+(vec2(-1.0,-1.0)/frame)).xyz;
    vec3 rgbNE=texture2D(tex,v_texCoord+(vec2(1.0,-1.0)/frame)).xyz;
    vec3 rgbSW=texture2D(tex,v_texCoord+(vec2(-1.0,1.0)/frame)).xyz;
    vec3 rgbSE=texture2D(tex,v_texCoord+(vec2(1.0,1.0)/frame)).xyz;
    vec3 rgbM=texture2D(tex,v_texCoord).xyz;

    vec3 luma = vec3(0.299, 0.587, 0.114);
    // vec3 luma = vec3(0.700, 0.150, 0.150);
    float lumaNW = dot(rgbNW, luma);
    float lumaNE = dot(rgbNE, luma);
    float lumaSW = dot(rgbSW, luma);
    float lumaSE = dot(rgbSE, luma);
    float lumaM  = dot(rgbM,  luma);

    float lumaMin = min(lumaM, min(min(lumaNW, lumaNE), min(lumaSW, lumaSE)));
    float lumaMax = max(lumaM, max(max(lumaNW, lumaNE), max(lumaSW, lumaSE)));

    vec2 dir;
    dir.x = -((lumaNW + lumaNE) - (lumaSW + lumaSE));
    dir.y =  ((lumaNW + lumaSW) - (lumaNE + lumaSE));

    float dirReduce = max((lumaNW + lumaNE + lumaSW + lumaSE) * (0.05 * FXAA_REDUCE_MUL), FXAA_REDUCE_MIN);

    float rcpDirMin = 1.0/(min(abs(dir.x), abs(dir.y)) + dirReduce);

    dir = min(vec2( FXAA_SPAN_MAX,  FXAA_SPAN_MAX), max(vec2(-FXAA_SPAN_MAX, -FXAA_SPAN_MAX), dir * rcpDirMin)) / frame;

    vec3 rgbA = (1.0/2.0) * (texture2D(tex, v_texCoord.xy + dir * (1.0/3.0 - 0.5)).xyz + texture2D(tex, v_texCoord.xy + dir * (2.0/3.0 - 0.5)).xyz);
    vec3 rgbB = rgbA * (1.0/2.0) + (1.0/4.0) * (texture2D(tex, v_texCoord.xy + dir * (0.0/3.0 - 0.5)).xyz + texture2D(tex, v_texCoord.xy + dir * (3.0/3.0 - 0.5)).xyz);
    float lumaB = dot(rgbB, luma);

    if((lumaB < lumaMin) || (lumaB > lumaMax))
    {
        fragColor = vec4(rgbA,1.0);
    }
    else
    {
        fragColor = vec4(rgbB,1.0);
    }
}