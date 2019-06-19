// #define LINUX 

using System;
using System.Threading;

using static OpenGL.OpenGLConstants;

#if MAC
using static OpenGL.UnixGL;
#elif WINDOWS
using static OpenGL.WindowsGL;
#elif LINUX
using static OpenGL.UnixGL;
#else
using static OpenGL.WindowsGL;
#endif

namespace SpaceVIL
{
    internal sealed class VRAMTexture
    {
        private float[] _vbo_data;
        public uint VBO;
        private int[] _ibo_data;
        public uint IBO;
        public uint[] Texture;

        internal VRAMTexture()
        {
            Texture = new uint[1];
        }

        internal void GenTexture(int w, int h, byte[] bitmap)
        {
            Monitor.Enter(VRAMStorage.StorageLocker);
            try
            {
                glGenTextures(1, Texture);
                glBindTexture(GL_TEXTURE_2D, Texture[0]);

                glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, w, h);
                glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, w, h, GL_RGBA, GL_UNSIGNED_BYTE, bitmap);

                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
                // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            }
            finally
            {
                Monitor.Exit(VRAMStorage.StorageLocker);
            }
        }

        internal void GenBuffers(float x0, float x1, float y0, float y1, float level, bool flip = false)
        {
            if (!flip)
                _vbo_data = new float[]
                {
                //X    Y      Z         //U     V
                x0,  y0,  level,     0.0f, 1.0f, //x0
                x0,  y1,  level,     0.0f, 0.0f, //x1
                x1,  y1,  level,     1.0f, 0.0f, //x2
                x1,  y0,  level,     1.0f, 1.0f, //x3
                };
            else
                _vbo_data = new float[]
                {
                //X    Y      Z         //U     V
                x0,  y0,  level,     0.0f, 0.0f, //x0
                x0,  y1,  level,     0.0f, 1.0f, //x1
                x1,  y1,  level,     1.0f, 1.0f, //x2
                x1,  y0,  level,     1.0f, 0.0f, //x3
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

        internal void Unbind()
        {
            Monitor.Enter(VRAMStorage.StorageLocker);
            try
            {
                //Texture bind
                glBindTexture(GL_TEXTURE_2D, 0);
            }
            finally
            {
                Monitor.Exit(VRAMStorage.StorageLocker);
            }
        }
        internal void Bind()
        {
            Monitor.Enter(VRAMStorage.StorageLocker);
            try
            {
                //Texture bind
                glBindTexture(GL_TEXTURE_2D, Texture[0]);
            }
            finally
            {
                Monitor.Exit(VRAMStorage.StorageLocker);
            }
        }
        internal void Bind(uint[] texture)
        {
            //Texture bind
            glBindTexture(GL_TEXTURE_2D, texture[0]);
        }

        internal bool SendUniformSample2D(Shader shader, string name)
        {
            int location = glGetUniformLocation(shader.GetProgramID(), name);
            if (location >= 0)
            {
                glUniform1i(location, 0);
                return true;
            }
            else
                Console.WriteLine("Uniform not found: <" + name + "> " + shader.GetShaderName());
            return false;
        }

        internal bool SendUniform4f(Shader shader, string name, float[] array)
        {
            int location = glGetUniformLocation(shader.GetProgramID(), name);
            if (location >= 0)
            {
                glUniform4f(location, array[0], array[1], array[2], array[3]);
                return true;
            }
            else
                Console.WriteLine("Uniform not found: <" + name + "> " + shader.GetShaderName());
            return false;
        }
        internal bool SendUniform1fv(Shader shader, string name, int count, float[] array)
        {
            int location = glGetUniformLocation(shader.GetProgramID(), name);
            if (location >= 0)
            {
                glUniform1fv(location, count, array);
                return true;
            }
            else
                Console.WriteLine("Uniform not found: <" + name + "> " + shader.GetShaderName());
            return false;
        }
        internal bool SendUniform2fv(Shader shader, string name, float[] array)
        {
            int location = glGetUniformLocation(shader.GetProgramID(), name);
            if (location >= 0)
            {
                glUniform2fv(location, 1, array);
                return true;
            }
            else
                Console.WriteLine("Uniform not found: <" + name + "> " + shader.GetShaderName());
            return false;
        }
        internal bool SendUniform1f(Shader shader, string name, float array)
        {
            int location = glGetUniformLocation(shader.GetProgramID(), name);
            if (location >= 0)
            {
                glUniform1f(location, array);
                return true;
            }
            else
                Console.WriteLine("Uniform not found: <" + name + "> " + shader.GetShaderName());
            return false;
        }

        internal bool SendUniform1i(Shader shader, String name, int value)
        {
            int location = glGetUniformLocation(shader.GetProgramID(), name);
            if (location >= 0)
            {
                glUniform1i(location, value);
                return true;
            }
            else
                Console.WriteLine("Uniform not found: <" + name + ">");
            return false;
        }

        internal void Draw()
        {
            Monitor.Enter(VRAMStorage.StorageLocker);
            try
            {
                glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, IntPtr.Zero);
                glDisableVertexAttribArray(0);
                glDisableVertexAttribArray(1);
            }
            finally
            {
                Monitor.Exit(VRAMStorage.StorageLocker);
            }
        }

        internal void Clear()
        {
            Monitor.Enter(VRAMStorage.StorageLocker);
            try
            {
                uint[] buffers = new uint[2] { VBO, IBO };
                glDeleteBuffers(2, buffers);
                glDeleteTextures(1, Texture);
                _vbo_data = null;
                _ibo_data = null;
            }
            finally
            {
                Monitor.Exit(VRAMStorage.StorageLocker);
            }
        }

        internal void DeleteIBOBuffer()
        {
            Monitor.Enter(VRAMStorage.StorageLocker);
            try
            {
                uint[] buffers = new uint[] { IBO };
                glDeleteBuffers(1, buffers);
            }
            finally
            {
                Monitor.Exit(VRAMStorage.StorageLocker);
            }
        }

        internal void DeleteVBOBuffer()
        {
            Monitor.Enter(VRAMStorage.StorageLocker);
            try
            {
                uint[] buffers = new uint[] { VBO };
                glDeleteBuffers(1, buffers);
            }
            finally
            {
                Monitor.Exit(VRAMStorage.StorageLocker);
            }
        }

        internal void DeleteTexture()
        {
            Monitor.Enter(VRAMStorage.StorageLocker);
            try
            {
                glDeleteTextures(1, Texture);
            }
            finally
            {
                Monitor.Exit(VRAMStorage.StorageLocker);
            }
        }
    }
}