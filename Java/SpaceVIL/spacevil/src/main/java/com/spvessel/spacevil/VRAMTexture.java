package com.spvessel.spacevil;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.spvessel.spacevil.Flags.ImageQuality;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

final class VramTexture extends AbstractVramResource {
    private float[] _vbo_data;
    int VBO;
    private int[] _ibo_data;
    int IBO;
    int texture;

    VramTexture() {
    }

    private void applySmoothFilter() {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    private void applyRoughFilter() {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    void genTexture(int w, int h, byte[] bitmap, ImageQuality filtering) {
        byte[] array = bitmap;
        ByteBuffer bb = BufferUtils.createByteBuffer(array.length);// ByteBuffer.allocateDirect(array.length);
        bb.put(array);
        bb.rewind();

        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        GL42.glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE, bb);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

        if (filtering == ImageQuality.SMOOTH) {
            applySmoothFilter();
        } else {
            applyRoughFilter();
        }
    }

    void genTexture(int w, int h, BufferedImage bitmap, ImageQuality filtering) {
        ByteBuffer buffer = getByteBuffer(bitmap);

        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        GL42.glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

        if (filtering == ImageQuality.SMOOTH) {
            applySmoothFilter();
        } else {
            applyRoughFilter();
        }
    }

    void genBuffers(float x0, float x1, float y0, float y1) {
        genBuffers(x0, x1, y0, y1, false);
    }

    void genBuffers(float x0, float x1, float y0, float y1, boolean flip) {
        // Vertices
        // if (!flip) {
        // _vbo_data = new float[] {
        // // X Y Z //U V
        // x0, y0, 0.0f, 1.0f, // x0
        // x0, y1, 0.0f, 0.0f, // x1
        // x1, y1, 1.0f, 0.0f, // x2
        // x1, y0, 1.0f, 1.0f, // x3
        // };
        // } else
        {
            _vbo_data = new float[] {
                    // X Y Z //U V
                    x0, y0, 0.0f, 0.0f, // x0
                    x0, y1, 0.0f, 1.0f, // x1
                    x1, y1, 1.0f, 1.0f, // x2
                    x1, y0, 1.0f, 0.0f, // x3
            };
        }
        // ibo
        _ibo_data = new int[] { 0, 1, 2, // first triangle
                2, 3, 0, // second triangle
        };

        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, _vbo_data, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * 4, 0);
        glEnableVertexAttribArray(0);

        IBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, _ibo_data, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, true, 4 * 4, (2 * 4));
        glEnableVertexAttribArray(1);
    }

    @Override
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    void bind(int tex) {
        glBindTexture(GL_TEXTURE_2D, tex);
    }

    @Override
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    void bindVboIbo() {
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * 4, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
        glVertexAttribPointer(1, 2, GL_FLOAT, true, 4 * 4, (2 * 4));
        glEnableVertexAttribArray(1);
    }

    @Override
    public void draw() {
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    @Override
    public void clear() {
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

    static ByteBuffer getByteBuffer(BufferedImage bitmap) {
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getRGB(0, 0, bitmap.getWidth(), bitmap.getHeight(), pixels, 0, bitmap.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(bitmap.getWidth() * bitmap.getHeight() * 4);

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int pixel = pixels[y * bitmap.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
                buffer.put((byte) (pixel & 0xFF)); // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component
            }
        }
        buffer.flip();
        return buffer;
    }
}