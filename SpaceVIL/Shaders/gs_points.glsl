#version 330

layout (points) in;
layout (triangle_strip) out;
layout (max_vertices = 256) out;

uniform int size;
uniform vec2 shape[256];

void main() {
    vec4 diff = gl_in[0].gl_Position + vec4(-shape[0].x, -shape[0].y, 0.0, 0.0);
    for (int i = 0; i < size; i++) {
        gl_Position = diff + vec4(shape[i].x, shape[i].y, 0.0, 0.0);
        EmitVertex();
    }
    EndPrimitive();
}