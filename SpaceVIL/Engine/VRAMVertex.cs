using System;
using System.Collections.Generic;
using System.Drawing;

using static OpenGL.OpenGLConstants;
using static OpenGL.OpenGLWrapper;

namespace SpaceVIL
{
    internal sealed class VramVertex : AbstractVramResource
    {
        private float[] _vbo_data;
        public uint[] VBO;
        private int length;
        internal uint Type = GL_TRIANGLES;

        internal VramVertex() { }

        internal void SendColor(Shader shader, Color fill)
        {
            float[] argb = { (float)fill.R / 255.0f, (float)fill.G / 255.0f, (float)fill.B / 255.0f, (float)fill.A / 255.0f };
            SendUniform4f(shader, "background", argb);
        }

        internal void GenBuffers(float[] vertices)
        {
            length = vertices.Length / 2;

            VBO = new uint[1];
            glGenBuffers(1, VBO);

            glBindBuffer(GL_ARRAY_BUFFER, VBO[0]);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(0);
        }

        internal void GenBuffers(List<float[]> vertices)
        {
            length = vertices.Count;
            _vbo_data = new float[vertices.Count * 2];

            for (int i = 0; i < vertices.Count; i++)
            {
                int index = i * 2;
                _vbo_data[index + 0] = vertices[i][0];
                _vbo_data[index + 1] = vertices[i][1];
            }
            VBO = new uint[1];
            glGenBuffers(1, VBO);
            glBindBuffer(GL_ARRAY_BUFFER, VBO[0]);
            glBufferData(GL_ARRAY_BUFFER, _vbo_data, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(0);
        }

        public override void Draw()
        {
            glDrawArrays(Type, 0, length);
            glDisableVertexAttribArray(0);
        }

        public override void Clear()
        {
            glDeleteBuffers(1, VBO);
            _vbo_data = null;
        }

        public override void Bind()
        {
            glBindBuffer(GL_ARRAY_BUFFER, VBO[0]);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, IntPtr.Zero);
            glEnableVertexAttribArray(0);
        }

        public override void Unbind()
        {
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
    }
}