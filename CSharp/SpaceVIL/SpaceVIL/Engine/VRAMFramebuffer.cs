// #define LINUX 

using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.IO;
using System.Text;

using Glfw3;
using System.Threading;
using System.Threading.Tasks;
using System.Drawing;

#if LINUX
using static GL.LGL.OpenLGL;
#elif WINDOWS
using static GL.WGL.OpenWGL;
#else
using static GL.WGL.OpenWGL;
#endif

namespace SpaceVIL
{
    internal sealed class VRAMFramebuffer
    {
        public uint[] FBO;
        public uint[] Texture;

        internal VRAMFramebuffer()
        {
            Texture = new uint[1];
            FBO = new uint[1];
        }

        internal void GenFBO()
        {
            glGenFramebuffers(1, FBO);
            glBindFramebuffer(GL_FRAMEBUFFER_EXT, FBO[0]);
            glFramebufferTexture(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, Texture[0], 0);
            uint[] draw_bufs = new uint[] { GL_COLOR_ATTACHMENT0_EXT };
            glDrawBuffers(1, draw_bufs);
        }
        internal void GenFBOTexture(int w, int h)
        {
            glGenTextures(1, Texture);
            glBindTexture(GL_TEXTURE_2D, Texture[0]);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_BGRA, GL_UNSIGNED_BYTE, IntPtr.Zero);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glBindTexture(GL_TEXTURE_2D, 0);
        }
        internal void BindFBO()
        {
            glBindFramebuffer(GL_FRAMEBUFFER_EXT, FBO[0]);
        }
        internal void UnbindFBO()
        {
            glBindFramebuffer(GL_FRAMEBUFFER_EXT, 0);
        }
        internal void ClearFBO()
        {
            glDeleteFramebuffers(1, FBO);
        }
        internal void ClearTexture()
        {
            glDeleteTextures(1, Texture);
        }
    }
}