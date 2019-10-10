using System;
using System.Drawing;
using System.Drawing.Imaging;
using static OpenGL.OpenGLConstants;
using static OpenGL.OpenGLWrapper;

namespace SpaceVIL
{
    internal sealed class VramTexture : AbstractVramResource
    {
        private float[] _vbo_data;
        public uint VBO;
        private int[] _ibo_data;
        public uint IBO;
        public uint[] Texture;

        internal VramTexture()
        {
            Texture = new uint[1];
        }

        internal void GenTexture(int w, int h, byte[] bitmap)
        {
            glGenTextures(1, Texture);
            glBindTexture(GL_TEXTURE_2D, Texture[0]);

            glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
            glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE, bitmap);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        }

        internal void GenTexture(int w, int h, Bitmap bitmap)
        {
            BitmapData bitData = bitmap.LockBits(
                new System.Drawing.Rectangle(Point.Empty, bitmap.Size), ImageLockMode.ReadOnly, PixelFormat.Format32bppArgb);

            glGenTextures(1, Texture);
            glBindTexture(GL_TEXTURE_2D, Texture[0]);

            glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
            // glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE, bitData.Scan0);
            glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_BGRA, GL_UNSIGNED_BYTE, bitData.Scan0);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            bitmap.UnlockBits(bitData);
        }

        internal void GenBuffers(float x0, float x1, float y0, float y1, bool flip = false)
        {
            // if (!flip)
            //     _vbo_data = new float[]
            //     {
            //     //X    Y   U     V
            //     x0,  y0,  0.0f, 1.0f, //x0
            //     x0,  y1,  0.0f, 0.0f, //x1
            //     x1,  y1,  1.0f, 0.0f, //x2
            //     x1,  y0,  1.0f, 1.0f, //x3
            //     };
            // else
                _vbo_data = new float[]
                {
                //X    Y   U     V
                x0,  y0,  0.0f, 0.0f, //x0
                x0,  y1,  0.0f, 1.0f, //x1
                x1,  y1,  1.0f, 1.0f, //x2
                x1,  y0,  1.0f, 0.0f, //x3
                };

            _ibo_data = new int[]
            {
                0, 1, 2, //first triangle
                2, 3, 0, // second triangle
            };

            uint[] buffers = new uint[2];
            glGenBuffers(2, buffers);

            VBO = buffers[0];
            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBufferData(GL_ARRAY_BUFFER, _vbo_data, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * sizeof(float), IntPtr.Zero);
            glEnableVertexAttribArray(0);

            IBO = buffers[1];
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, _ibo_data, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, true, 4 * sizeof(float), IntPtr.Zero + (2 * sizeof(float)));
            glEnableVertexAttribArray(1);
        }

        public override void Unbind()
        {
            glBindTexture(GL_TEXTURE_2D, 0);
        }
        public override void Bind()
        {
            glBindTexture(GL_TEXTURE_2D, Texture[0]);
        }
        internal void Bind(uint[] texture)
        {
            glBindTexture(GL_TEXTURE_2D, texture[0]);
        }

        internal void BindVboIbo()
        {
            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * sizeof(float), IntPtr.Zero);
            glEnableVertexAttribArray(0);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
            glVertexAttribPointer(1, 2, GL_FLOAT, true, 4 * sizeof(float), IntPtr.Zero + (2 * sizeof(float)));
            glEnableVertexAttribArray(1);
        }

        public override void Draw()
        {
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, IntPtr.Zero);
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
        }

        public override void Clear()
        {
            uint[] buffers = new uint[2] { VBO, IBO };
            glDeleteBuffers(2, buffers);
            glDeleteTextures(1, Texture);
            _vbo_data = null;
            _ibo_data = null;
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