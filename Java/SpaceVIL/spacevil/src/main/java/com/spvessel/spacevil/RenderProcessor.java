package com.spvessel.spacevil;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Flags.RedrawFrequency;

final class RenderProcessor {
    private float _intervalVeryLow = 1.0f;
    private float _intervalLow = 1.0f / 10.0f;
    private float _intervalMedium = 1.0f / 30.0f;
    private float _intervalHigh = 1.0f / 60.0f;
    private float _intervalUltra = 1.0f / 120.0f;
    private float _intervalAssigned = 1.0f / 15.0f;

    private RedrawFrequency _frequency = RedrawFrequency.LOW;

    private Lock _locker = new ReentrantLock();

    RenderProcessor() {
    }

    void setFrequency(RedrawFrequency value) {
        _locker.lock();
        try {
            if (value == RedrawFrequency.VERY_LOW) {
                _intervalAssigned = _intervalVeryLow;
            } else if (value == RedrawFrequency.LOW) {
                _intervalAssigned = _intervalLow;
            } else if (value == RedrawFrequency.MEDIUM) {
                _intervalAssigned = _intervalMedium;
            } else if (value == RedrawFrequency.HIGH) {
                _intervalAssigned = _intervalHigh;
            } else if (value == RedrawFrequency.ULTRA) {
                _intervalAssigned = _intervalUltra;
            }
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
        } finally {
            _locker.unlock();
        }
    }

    float getCurrentFrequency() {
        _locker.lock();
        try {
            return _intervalAssigned;
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
            return _intervalLow;
        } finally {
            _locker.unlock();
        }
    }

    RedrawFrequency getRedrawFrequency() {
        _locker.lock();
        try {
            return _frequency;
        } catch (Exception ex) {
            System.out.println("Method - SetFrequency");
            ex.printStackTrace();
            _frequency = RedrawFrequency.LOW;
            return _frequency;
        } finally {
            _locker.unlock();
        }
    }

    void drawVertex(Shader shader, List<float[]> vertex, float level, Color color, int type) {
        shader.useShader();
        VRAMVertex store = new VRAMVertex();
        store.genBuffers(vertex, level);
        store.sendColor(shader, color);
        store.draw(type);
        store.clear();
    }

    void drawVertex(Shader shader, float[] vertex, Color color, int type) {
        shader.useShader();
        VRAMVertex store = new VRAMVertex();
        store.genBuffers(vertex);
        store.sendColor(shader, color);
        store.draw(type);
        store.clear();
    }

    void drawText(Shader shader, float x0, float x1, float y0, float y1, int w, int h, ByteBuffer buffer, float level,
            float[] color) {
        shader.useShader();
        VRAMTexture store = new VRAMTexture();
        store.genBuffers(x0, x1, y0, y1, level, true);
        store.genTexture(w, h, buffer);
        store.sendUniformSample2D(shader);
        store.sendUniform4f(shader, "rgb", color);
        store.draw();
        store.clear();
    }

    void drawShadow(Shader shader, float level, float[] weights, int res, int fbo_texture, float[] xy, float[] wh,
            int width, int height) {
        float i_x0 = -1.0f;
        float i_y0 = 1.0f;
        float i_x1 = 1.0f;
        float i_y1 = -1.0f;
        shader.useShader();
        VRAMTexture store = new VRAMTexture();
        store.genBuffers(i_x0, i_x1, i_y0, i_y1, level);
        store.bind(fbo_texture);
        store.sendUniformSample2D(shader);
        store.sendUniform1fv(shader, "weights", 11, weights);
        store.sendUniform2fv(shader, "frame", new float[] { width, height });
        store.sendUniform1f(shader, "res", (res * 1f / 10));
        store.sendUniform2fv(shader, "point", xy);
        store.sendUniform2fv(shader, "size", wh);
        store.draw();
        store.clear();
    }

    void drawTextureAsIs(Shader shader, float x0, float x1, float y0, float y1, int w, int h, byte[] buffer,
            float level, float alpha) {
        shader.useShader();
        VRAMTexture store = new VRAMTexture();
        store.genBuffers(x0, x1, y0, y1, level);
        store.genTexture(w, h, buffer);
        store.sendUniformSample2D(shader);
        store.sendUniform1i(shader, "overlay", 0);
        store.sendUniform1f(shader, "alpha", alpha);
        store.draw();
        store.clear();
    }

    void drawTextureWithColorOverlay(Shader shader, float x0, float x1, float y0, float y1, int w, int h, byte[] buffer,
            float level, float alpha, Color color) {

        float[] argb = { (float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f,
                (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f };

        shader.useShader();
        VRAMTexture store = new VRAMTexture();
        store.genBuffers(x0, x1, y0, y1, level, true);
        store.genTexture(w, h, buffer);
        store.sendUniformSample2D(shader);
        store.sendUniform1i(shader, "overlay", 1);
        store.sendUniform4f(shader, "rgb", argb);
        store.sendUniform1f(shader, "alpha", alpha);
        store.draw();
        store.clear();
    }

    void drawFreshTexture(ImageItem image, Shader shader, float x0, float x1, float y0, float y1, int w, int h,
            byte[] buffer, float level) {
        VRAMStorage.storageLocker.lock();
        try {
            VRAMStorage.deleteTexture(image);
            shader.useShader();
            VRAMTexture tex = new VRAMTexture();
            tex.genBuffers(x0, x1, y0, y1, level);
            tex.genTexture(w, h, buffer);
            VRAMStorage.addTexture(image, tex);
            image.setNew(false);

            tex.sendUniformSample2D(shader);
            if (image.isColorOverlay()) {
                float[] argb = { (float) image.getColorOverlay().getRed() / 255.0f,
                        (float) image.getColorOverlay().getGreen() / 255.0f,
                        (float) image.getColorOverlay().getBlue() / 255.0f,
                        (float) image.getColorOverlay().getAlpha() / 255.0f };
                tex.sendUniform1i(shader, "overlay", 1);
                tex.sendUniform4f(shader, "rgb", argb);
            } else
                tex.sendUniform1i(shader, "overlay", 0);

            tex.sendUniform1f(shader, "alpha", image.getRotationAngle());
            tex.draw();
            tex.deleteIBOBuffer();
            tex.deleteVBOBuffer();
            tex.unbind();

        } finally {
            VRAMStorage.storageLocker.unlock();
        }
    }

    void drawStoredTexture(ImageItem image, Shader shader, float x0, float x1, float y0, float y1, float level) {
        VRAMStorage.storageLocker.lock();
        try {
            shader.useShader();
            VRAMTexture tex = VRAMStorage.getTexture(image);
            if (tex == null) {
                image.setNew(true);
                return;
            }
            tex.bind();
            tex.genBuffers(x0, x1, y0, y1, level);
            tex.sendUniformSample2D(shader);
            if (image.isColorOverlay()) {
                float[] argb = { (float) image.getColorOverlay().getRed() / 255.0f,
                        (float) image.getColorOverlay().getGreen() / 255.0f,
                        (float) image.getColorOverlay().getBlue() / 255.0f,
                        (float) image.getColorOverlay().getAlpha() / 255.0f };
                tex.sendUniform1i(shader, "overlay", 1);
                tex.sendUniform4f(shader, "rgb", argb);
            } else
                tex.sendUniform1i(shader, "overlay", 0);

            tex.sendUniform1f(shader, "alpha", image.getRotationAngle());
            tex.draw();
            tex.deleteIBOBuffer();
            tex.deleteVBOBuffer();
            tex.unbind();

        } finally {
            VRAMStorage.storageLocker.unlock();
        }
    }

    static List<float[]> getFullWindowRectangle()
    {
        List<float[]> vertex = new LinkedList<>();
        vertex.add(new float[] { -1.0f, 1.0f, 0.0f });
        vertex.add(new float[] { -1.0f, -1.0f, 0.0f });
        vertex.add(new float[] { 1.0f, -1.0f, 0.0f });
        vertex.add(new float[] { 1.0f, -1.0f, 0.0f });
        vertex.add(new float[] { 1.0f, 1.0f, 0.0f });
        vertex.add(new float[] { -1.0f, 1.0f, 0.0f });
        return vertex;
    } 
}