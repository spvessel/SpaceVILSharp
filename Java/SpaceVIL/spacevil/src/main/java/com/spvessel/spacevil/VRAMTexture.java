package com.spvessel.spacevil;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import com.spvessel.spacevil.Flags.ImageQuality;

import com.spvessel.spacevil.internal.Wrapper.OpenGLWrapper;
import static com.spvessel.spacevil.internal.Wrapper.OpenGLWrapper.*;

final class VramTexture extends AbstractVramResource {

    private float[] _vbo_data;
    int VBO;
    private int[] _ibo_data;
    int IBO;
    int texture;

    VramTexture() {
    }

    private void applySmoothFilter() {
        gl.TexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        gl.TexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    private void applyRoughFilter() {
        gl.TexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        gl.TexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    void genTexture(int w, int h, byte[] bitmap, ImageQuality filtering) {
        byte[] array = bitmap;

        texture = gl.GenTexture();
        gl.BindTexture(GL_TEXTURE_2D, texture);

        gl.TexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
        gl.TexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE, array);

        gl.TexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        gl.TexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

        if (filtering == ImageQuality.Smooth) {
            applySmoothFilter();
        } else {
            applyRoughFilter();
        }
    }

    void genTexture(int w, int h, BufferedImage bitmap, ImageQuality filtering) {
        byte[] buffer = getByteBuffer(bitmap);

        texture = gl.GenTexture();
        gl.BindTexture(GL_TEXTURE_2D, texture);

        gl.TexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
        gl.TexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        gl.TexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        gl.TexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

        if (filtering == ImageQuality.Smooth) {
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

        VBO = gl.GenBuffer();
        gl.BindBuffer(GL_ARRAY_BUFFER, VBO);
        gl.BufferDataf(GL_ARRAY_BUFFER, _vbo_data.length * 4, _vbo_data, GL_STATIC_DRAW);
        gl.VertexAttribPointer(0, 2, GL_FLOAT, false, 4 * 4, 0);
        gl.EnableVertexAttribArray(0);

        IBO = gl.GenBuffer();
        gl.BindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
        gl.BufferDatai(GL_ELEMENT_ARRAY_BUFFER, _ibo_data.length * 4, _ibo_data, GL_STATIC_DRAW);
        gl.VertexAttribPointer(1, 2, GL_FLOAT, true, 4 * 4, (2 * 4));
        gl.EnableVertexAttribArray(1);
    }

    @Override
    public void unbind() {
        gl.BindTexture(GL_TEXTURE_2D, 0);
    }

    void bind(int tex) {
        gl.BindTexture(GL_TEXTURE_2D, tex);
    }

    @Override
    public void bind() {
        gl.BindTexture(GL_TEXTURE_2D, texture);
    }

    void bindVboIbo() {
        gl.BindBuffer(GL_ARRAY_BUFFER, VBO);
        gl.VertexAttribPointer(0, 2, GL_FLOAT, false, 4 * 4, 0);
        gl.EnableVertexAttribArray(0);

        gl.BindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
        gl.VertexAttribPointer(1, 2, GL_FLOAT, true, 4 * 4, (2 * 4));
        gl.EnableVertexAttribArray(1);
    }

    @Override
    public void draw() {
        gl.DrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT);
        gl.DisableVertexAttribArray(0);
        gl.DisableVertexAttribArray(1);
    }

    @Override
    public void clear() {
        gl.DeleteBuffer(VBO);
        gl.DeleteBuffer(IBO);
        gl.DeleteTexture(texture);
        _vbo_data = null;
        _ibo_data = null;
    }

    void deleteIBOBuffer() {
        gl.DeleteBuffer(IBO);
    }

    void deleteVBOBuffer() {
        gl.DeleteBuffer(VBO);
    }

    void deleteTexture() {
        gl.DeleteTexture(texture);
    }

    static byte[] getByteBuffer(BufferedImage bitmap) {
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getRGB(0, 0, bitmap.getWidth(), bitmap.getHeight(), pixels, 0, bitmap.getWidth());

        byte[] buffer = new byte[bitmap.getWidth() * bitmap.getHeight() * 4];
        int index = 0;
        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int pixel = pixels[y * bitmap.getWidth() + x];
                buffer[index++] = ((byte) ((pixel >> 16) & 0xFF)); // Red component
                buffer[index++] = ((byte) ((pixel >> 8) & 0xFF)); // Green component
                buffer[index++] = ((byte) (pixel & 0xFF)); // Blue component
                buffer[index++] = ((byte) ((pixel >> 24) & 0xFF)); // Alpha component
            }
        }
        return buffer;
    }
}