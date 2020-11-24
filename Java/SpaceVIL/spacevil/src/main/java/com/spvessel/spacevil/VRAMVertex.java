package com.spvessel.spacevil;

import java.awt.Color;
import java.util.List;

import com.spvessel.spacevil.internal.Wrapper.OpenGLWrapper;
import static com.spvessel.spacevil.internal.Wrapper.OpenGLWrapper.*;

final class VramVertex extends AbstractVramResource {
    private float[] _vbo_data;
    int VBO;
    private int length;
    int type = GL_TRIANGLES;

    VramVertex() {
    }

    void sendColor(Shader shader, Color fill) {
        float[] argb = { (float) fill.getRed() / 255.0f, (float) fill.getGreen() / 255.0f,
                (float) fill.getBlue() / 255.0f, (float) fill.getAlpha() / 255.0f };
        sendUniform4f(shader, "background", argb);
    }

    void genBuffers(float[] vertices) {
        length = vertices.length / 2;
        // Vertices
        VBO = gl.GenBuffer();
        gl.BindBuffer(GL_ARRAY_BUFFER, VBO);
        gl.BufferDataf(GL_ARRAY_BUFFER, vertices.length * 4, vertices, GL_STATIC_DRAW);
        gl.VertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        gl.EnableVertexAttribArray(0);
    }

    void genBuffers(List<float[]> vertices) {
        length = vertices.size();
        // Vertices
        _vbo_data = new float[vertices.size() * 2];
        for (int i = 0; i < vertices.size(); i++) {
            int index = i * 2;
            _vbo_data[index + 0] = vertices.get(i)[0];
            _vbo_data[index + 1] = vertices.get(i)[1];
        }
        VBO = gl.GenBuffer();
        gl.BindBuffer(GL_ARRAY_BUFFER, VBO);
        gl.BufferDataf(GL_ARRAY_BUFFER, _vbo_data.length * 4, _vbo_data, GL_STATIC_DRAW);
        gl.VertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        gl.EnableVertexAttribArray(0);
    }
@Override
    public void draw() {
        gl.DrawArrays(type, 0, length);
        gl.DisableVertexAttribArray(0);
    }
@Override
    public void clear() {
        gl.DeleteBuffer(VBO);
        _vbo_data = null;
    }
@Override
    public void bind() {
        gl.BindBuffer(GL_ARRAY_BUFFER, VBO);
        gl.VertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        gl.EnableVertexAttribArray(0);
    }
@Override
    public void unbind() {
        gl.BindBuffer(GL_ARRAY_BUFFER, 0);
    }
}