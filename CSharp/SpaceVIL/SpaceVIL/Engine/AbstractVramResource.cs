using System;
using SpaceVIL.Core;
using static OpenGL.OpenGLWrapper;

namespace SpaceVIL
{
    delegate void SendUniformSample2D();

    internal abstract class AbstractVramResource : IVramResource
    {
        internal int GetLocation(Shader shader, String name)
        {
            return glGetUniformLocation(shader.GetProgramID(), name);
        }
        internal bool PrintError(Shader shader, String name)
        {
            Console.WriteLine("Uniform not found: <" + name + "> " + shader.GetShaderName());
            return false;
        }

        public bool SendUniformSample2D(Shader shader, string name)
        {
            int location = GetLocation(shader, name);
            if (location < 0)
                return PrintError(shader, name);

            glUniform1i(location, 0);
            return true;
        }

        public bool SendUniform4f(Shader shader, string name, float[] array)
        {
            int location = GetLocation(shader, name);
            if (location < 0)
                return PrintError(shader, name);

            glUniform4f(location, array[0], array[1], array[2], array[3]);
            return true;
        }

        public bool SendUniform1fv(Shader shader, string name, int count, float[] array)
        {
            int location = GetLocation(shader, name);
            if (location < 0)
                return PrintError(shader, name);

            glUniform1fv(location, count, array);
            return true;
        }

        public bool SendUniform2fv(Shader shader, string name, float[] array)
        {
            int location = GetLocation(shader, name);
            if (location < 0)
                return PrintError(shader, name);

            glUniform2fv(location, 1, array);
            return true;
        }

        public bool SendUniform1f(Shader shader, string name, float array)
        {
            int location = GetLocation(shader, name);
            if (location < 0)
                return PrintError(shader, name);

            glUniform1f(location, array);
            return true;
        }

        public bool SendUniform1i(Shader shader, string name, int value)
        {
            int location = GetLocation(shader, name);
            if (location < 0)
                return PrintError(shader, name);

            glUniform1i(location, value);
            return true;
        }

        public abstract void Draw();

        public abstract void Clear();

        public abstract void Bind();

        public abstract void Unbind();
    }
}