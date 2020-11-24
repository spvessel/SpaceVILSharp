package com.spvessel.spacevil;

import com.spvessel.spacevil.internal.Wrapper.OpenGLWrapper;
import static com.spvessel.spacevil.internal.Wrapper.OpenGLWrapper.*;

final class VramFramebuffer {

    private OpenGLWrapper gl = null;

    int FBO;
    int texture;

    VramFramebuffer() {
        gl = OpenGLWrapper.get();
    }

    void genFBO() {
        FBO = gl.GenFramebuffer();
        gl.BindFramebuffer(GL_FRAMEBUFFER_EXT, FBO);
    }

    void genFboTexture(int w, int h) {
        texture = gl.GenTexture();
        gl.BindTexture(GL_TEXTURE_2D, texture);
        gl.TexImage2DEmpty(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE);
        gl.TexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        gl.TexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        gl.TexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        gl.TexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        gl.BindTexture(GL_TEXTURE_2D, 0);

        gl.FramebufferTexture(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, texture, 0);
    }

    void bind() {
        gl.BindFramebuffer(GL_FRAMEBUFFER_EXT, FBO);
    }

    void bindTexture() {
        gl.FramebufferTexture(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, texture, 0);
    }

    void unbind() {
        gl.BindFramebuffer(GL_FRAMEBUFFER_EXT, 0);
    }

    void unbindTexture() {
        gl.FramebufferTexture(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, 0, 0);
    }

    public void clear() {
        gl.DeleteFramebuffer(FBO);
    }

    void clearTexture() {
        gl.DeleteTexture(texture);
    }
}