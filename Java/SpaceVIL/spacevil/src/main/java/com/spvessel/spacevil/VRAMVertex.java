package com.spvessel.spacevil;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

final class VRAMVertex {
    private FloatBuffer _vbo_data;
    int VBO;
    private int length;
    // private FloatBuffer _cbo_data;
    // public int CBO;

    VRAMVertex() {
    }

    void sendColor(Shader shader, Color fill) {
        float[] argb = { (float) fill.getRed() / 255.0f, (float) fill.getGreen() / 255.0f,
                (float) fill.getBlue() / 255.0f, (float) fill.getAlpha() / 255.0f };
        sendUniform4f(shader, "background", argb);
    }

    void genBuffers(float[] vertices) {
        length = vertices.length / 3;
        // Vertices
        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        // Color
        // length = vertices.length / 3;
        // float[] argb = { (float) fill.getRed() / 255.0f, (float) fill.getGreen() /
        // 255.0f,
        // (float) fill.getBlue() / 255.0f, (float) fill.getAlpha() / 255.0f };
        // _cbo_data = BufferUtils.createFloatBuffer(length * 4);
        // for (int i = 0; i < length; i++) {
        // _cbo_data.put(i * 4 + 0, argb[0]);
        // _cbo_data.put(i * 4 + 1, argb[1]);
        // _cbo_data.put(i * 4 + 2, argb[2]);
        // _cbo_data.put(i * 4 + 3, argb[3]);
        // }
        // _cbo_data.rewind();
        // CBO = glGenBuffers();
        // glBindBuffer(GL_ARRAY_BUFFER, CBO);
        // glBufferData(GL_ARRAY_BUFFER, _cbo_data, GL_STATIC_DRAW);
        // glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        // glEnableVertexAttribArray(1);
    }

    void genBuffers(List<float[]> vertices) {
        length = vertices.size();
        // Vertices
        _vbo_data = BufferUtils.createFloatBuffer(vertices.size() * 3);
        for (int i = 0; i < vertices.size(); i++) {
            _vbo_data.put(i * 3 + 0, vertices.get(i)[0]);
            _vbo_data.put(i * 3 + 1, vertices.get(i)[1]);
            _vbo_data.put(i * 3 + 2, vertices.get(i)[2]);
        }
        _vbo_data.rewind();

        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, _vbo_data, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        // Color
        // float[] argb = { (float) fill.getRed() / 255.0f, (float) fill.getGreen() /
        // 255.0f,
        // (float) fill.getBlue() / 255.0f, (float) fill.getAlpha() / 255.0f };
        // _cbo_data = BufferUtils.createFloatBuffer(vertices.size() * 4);
        // for (int i = 0; i < vertices.size(); i++) {
        // _cbo_data.put(i * 4 + 0, argb[0]);
        // _cbo_data.put(i * 4 + 1, argb[1]);
        // _cbo_data.put(i * 4 + 2, argb[2]);
        // _cbo_data.put(i * 4 + 3, argb[3]);
        // }
        // _cbo_data.rewind();
        // CBO = glGenBuffers();
        // glBindBuffer(GL_ARRAY_BUFFER, CBO);
        // glBufferData(GL_ARRAY_BUFFER, _cbo_data, GL_STATIC_DRAW);
        // glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        // glEnableVertexAttribArray(1);
    }

    void draw(int type) {
        glDrawArrays(type, 0, length);
        glDisableVertexAttribArray(0);
        // glDisableVertexAttribArray(1);
    }

    boolean sendUniform4f(Shader shader, String name, float[] array) {
        int location = glGetUniformLocation((int) shader.getProgramID(), name);
        if (location >= 0) {
            glUniform4f(location, array[0], array[1], array[2], array[3]);
            return true;
        } else
            System.out.println("Uniform not found: <" + name + ">");
        return false;
    }

    void clear() {
        glDeleteBuffers(VBO);
        // glDeleteBuffers(CBO);
        _vbo_data = null;
        // _cbo_data = null;
    }

    void deleteVBOBuffer() {
        glDeleteBuffers(VBO);
    }

    // protected void deleteVBOBuffer() {
    // glDeleteBuffers(CBO);
    // }
}