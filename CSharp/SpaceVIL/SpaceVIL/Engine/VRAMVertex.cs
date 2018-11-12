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
    internal sealed class VRAMVertex
    {
        private float[] _vbo_data;
        public uint VBO;
        private int length;
        private float[] _cbo_data;
        public uint CBO;

        internal VRAMVertex() { }

        internal void GenBuffers(List<float[]> vertices, Color fill)
        {
            length = vertices.Count;
            float[] _vbo_data = new float[vertices.Count * 3];

            for (int i = 0; i < _vbo_data.Length / 3; i++)
            {
                _vbo_data[i * 3 + 0] = vertices.ElementAt(i)[0];
                _vbo_data[i * 3 + 1] = vertices.ElementAt(i)[1];
                _vbo_data[i * 3 + 2] = vertices.ElementAt(i)[2];
            }

            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);
            VBO = buffers[0];
            CBO = buffers[1];

            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBufferData(GL_ARRAY_BUFFER, _vbo_data, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(0);

            //Color
            float[] argb = {
                (float)fill.R / 255.0f,
                (float)fill.G / 255.0f,
                (float)fill.B / 255.0f,
                (float)fill.A / 255.0f};

            float[] _cbo_data = new float[vertices.Count * 4];
            for (int i = 0; i < _cbo_data.Length / 4; i++)
            {
                _cbo_data[i * 4 + 0] = argb[0];
                _cbo_data[i * 4 + 1] = argb[1];
                _cbo_data[i * 4 + 2] = argb[2];
                _cbo_data[i * 4 + 3] = argb[3];
            }
            glBindBuffer(GL_ARRAY_BUFFER, CBO);
            glBufferData(GL_ARRAY_BUFFER, _cbo_data, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(1);
        }

        internal void Draw(uint type)
        {
            glDrawArrays(type, 0, length);
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
        }

        internal void Clear()
        {
            uint[] buffers = new uint[2] { VBO, CBO };
            glDeleteBuffers(2, buffers);
            _vbo_data = null;
            _cbo_data = null;
        }

        internal void DeleteIBOBuffer()
        {
            uint[] buffers = new uint[] { CBO };
            glDeleteBuffers(1, buffers);
        }

        internal void DeleteVBOBuffer()
        {
            uint[] buffers = new uint[] { VBO };
            glDeleteBuffers(1, buffers);
        }
    }
}