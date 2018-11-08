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

#if OS_LINUX
using static GL.LGL.OpenLGL;
#elif OS_WNDOWS
using static GL.WGL.OpenWGL;
#else
using static GL.WGL.OpenWGL;
#endif

namespace SpaceVIL
{
    internal sealed class VRAMStorage
    {
        public BaseItem Link = null;
        private float[] _vbo_data;
        public uint VBO;
        private int[] _ibo_data;
        public uint IBO;
        public uint[] Texture;
        internal VRAMStorage(BaseItem link = null)
        {
            Texture = new uint[1];
            Link = link;
        }

        internal void GenTexture(int w, int h, byte[] bitmap)
        {
            glGenTextures(1, Texture);
            glBindTexture(GL_TEXTURE_2D, Texture[0]);

            glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
            glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_BGRA, GL_UNSIGNED_BYTE, bitmap);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        }

        internal void GenBuffers(float x0, float x1, float y0, float y1)
        {
            _vbo_data = new float[]
            {
                //X    Y      Z         //U     V
                x0,  y0,  0.0f,     0.0f, 1.0f, //x0
                x0,  y1,  0.0f,     0.0f, 0.0f, //x1
                x1,  y1,  0.0f,     1.0f, 0.0f, //x2
                x1,  y0,  0.0f,     1.0f, 1.0f, //x3
            };
            _ibo_data = new int[]
            {
                0, 1, 2, //first triangle
                2, 3, 0, // second triangle
            };

            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);
            VBO = buffers[0];
            IBO = buffers[1];

            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBufferData(GL_ARRAY_BUFFER, _vbo_data, GL_STATIC_DRAW);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, _ibo_data, GL_STATIC_DRAW);

            //Position attribute
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * sizeof(float), IntPtr.Zero);
            glEnableVertexAttribArray(0);
            //TexCoord attribute
            glVertexAttribPointer(1, 2, GL_FLOAT, true, 5 * sizeof(float), IntPtr.Zero + (3 * sizeof(float)));
            glEnableVertexAttribArray(1);
        }

        internal void Bind()
        {
            //Texture bind
            glBindTexture(GL_TEXTURE_2D, Texture[0]);
        }

        internal void Draw()
        {
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, IntPtr.Zero);
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
        }

        internal void Clear()
        {
            uint[] buffers = new uint[2] { VBO, IBO };
            glDeleteBuffers(2, buffers);
            glDeleteTextures(1, Texture);
            _vbo_data = null;
            _ibo_data= null;
        }

        internal void DeleteIBOBuffer()
        {
            uint[] buffers = new uint[] { IBO };
            glDeleteBuffers(1, buffers);
        }

        internal void DeleteVBOBuffer()
        {
            uint[] buffers = new uint[] { VBO };
            glDeleteBuffers(1, buffers);
        }

        internal void DeleteTexture()
        {
            glDeleteTextures(1, Texture);
        }
    }
}