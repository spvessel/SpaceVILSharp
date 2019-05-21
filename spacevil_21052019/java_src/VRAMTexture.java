package com.spvessel.spacevil;

import java.nio.ByteBuffer;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

final class VRAMTexture {
    private float[] _vbo_data;
    int VBO;
    private int[] _ibo_data;
    int IBO;
    int texture;

    VRAMTexture() {
    }

    void genTexture(int w, int h, ByteBuffer bitmap) {
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        GL42.glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
        GL11.glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE, bitmap);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    }

    void genTexture(int w, int h, byte[] bitmap) {
        ByteBuffer bb = BufferUtils.createByteBuffer(bitmap.length);
        bb.put(bitmap);
        bb.rewind();
        genTexture(w, h, bb);
    }

    void genBuffers(float x0, float x1, float y0, float y1, float level) {
        genBuffers(x0, x1, y0, y1, level, false);
    }

    void genBuffers(float x0, float x1, float y0, float y1, float level, boolean flip) {
        // Vertices
        if (!flip) {
            _vbo_data = new float[] {
                    // X Y Z //U V
                    x0, y0, level, 0.0f, 1.0f, // x0
                    x0, y1, level, 0.0f, 0.0f, // x1
                    x1, y1, level, 1.0f, 0.0f, // x2
                    x1, y0, level, 1.0f, 1.0f, // x3
            };
        } else {
            _vbo_data = new float[] {
                    // X Y Z //U V
                    x0, y0, level, 0.0f, 0.0f, // x0
                    x0, y1, level, 0.0f, 1.0f, // x1
                    x1, y1, level, 1.0f, 1.0f, // x2
                    x1, y0, level, 1.0f, 0.0f, // x3
            };
        }
        // ibo
        _ibo_data = new int[] { 0, 1, 2, // first triangle
                2, 3, 0, // second triangle
        };

        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, _vbo_data, GL_STATIC_DRAW);

        IBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, _ibo_data, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, true, 5 * 4, (3 * 4));
        glEnableVertexAttribArray(1);
    }

    void unbind() {
        // Texture bind
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    void bind(int tex) {
        // Texture bind
        glBindTexture(GL_TEXTURE_2D, tex);
    }

    void bind() {
        // Texture bind
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    void draw() {
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    boolean sendUniformSample2D(Shader shader) {
        int location = glGetUniformLocation((int) shader.getProgramID(), "tex");
        if (location >= 0) {
            glUniform1i(location, 0);
            return true;
        } else
            System.out.println("Uniform not found: <tex>");
        return false;
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

    boolean sendUniform1fv(Shader shader, String name, int count, float[] array) {
        int location = glGetUniformLocation((int) shader.getProgramID(), name);
        if (location >= 0) {
            glUniform1fv(location, array);
            return true;
        } else
            System.out.println("Uniform not found: <" + name + ">");
        return false;
    }

    boolean sendUniform2fv(Shader shader, String name, float[] array) {
        int location = glGetUniformLocation((int) shader.getProgramID(), name);
        if (location >= 0) {
            glUniform2fv(location, array);
            return true;
        } else
            System.out.println("Uniform not found: <" + name + ">");
        return false;
    }

    boolean sendUniform1f(Shader shader, String name, float array) {
        int location = glGetUniformLocation((int) shader.getProgramID(), name);
        if (location >= 0) {
            glUniform1f(location, array);
            return true;
        } else
            System.out.println("Uniform not found: <" + name + ">");
        return false;
    }

    boolean sendUniform1i(Shader shader, String name, int value) {
        int location = glGetUniformLocation((int) shader.getProgramID(), name);
        if (location >= 0) {
            glUniform1i(location, value);
            return true;
        } else
            System.out.println("Uniform not found: <" + name + ">");
        return false;
    }

    void clear() {
        glDeleteBuffers(VBO);
        glDeleteBuffers(IBO);
        glDeleteTextures(texture);
        _vbo_data = null;
        _ibo_data = null;
    }

    void deleteIBOBuffer() {
        glDeleteBuffers(IBO);
    }

    void deleteVBOBuffer() {
        glDeleteBuffers(VBO);
    }

    void deleteTexture() {
        glDeleteTextures(texture);
    }
}