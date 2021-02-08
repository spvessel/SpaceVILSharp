using System;

using static OpenGL.OpenGLConstants;
using static OpenGL.OpenGLWrapper;

namespace SpaceVIL
{
    internal sealed class VramFramebuffer
    {
        public uint[] FBO;
        public uint[] Texture;

        internal VramFramebuffer()
        {
            Texture = new uint[1];
            FBO = new uint[1];
        }

        internal void GenFBO()
        {
            glGenFramebuffers(1, FBO);
            glBindFramebuffer(GL_FRAMEBUFFER_EXT, FBO[0]);
        }
        internal void GenFboTexture(int w, int h)
        {
            glGenTextures(1, Texture);
            glBindTexture(GL_TEXTURE_2D, Texture[0]);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, IntPtr.Zero);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glBindTexture(GL_TEXTURE_2D, 0);

            glFramebufferTexture(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, Texture[0], 0);
        }
        internal void Bind()
        {
            glBindFramebuffer(GL_FRAMEBUFFER_EXT, FBO[0]);
        }
        internal void BindTexture()
        {
            glFramebufferTexture(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, Texture[0], 0);
        }
        internal void Unbind()
        {
            glBindFramebuffer(GL_FRAMEBUFFER_EXT, 0);
        }
        internal void UnbindTexture()
        {
            glFramebufferTexture(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, 0, 0);
        }
        internal void Clear()
        {
            glDeleteFramebuffers(1, FBO);
        }
        internal void ClearTexture()
        {
            glDeleteTextures(1, Texture);
        }
    }
}