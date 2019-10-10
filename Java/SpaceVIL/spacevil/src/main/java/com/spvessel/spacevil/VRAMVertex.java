package com.spvessel.spacevil;

import java.awt.Color;
import java.util.List;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

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
        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
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
        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, _vbo_data, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
    }
@Override
    public void draw() {
        glDrawArrays(type, 0, length);
        glDisableVertexAttribArray(0);
    }
@Override
    public void clear() {
        glDeleteBuffers(VBO);
        _vbo_data = null;
    }
@Override
    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
    }
@Override
    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}