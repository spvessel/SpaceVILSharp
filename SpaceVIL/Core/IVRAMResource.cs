using System;

namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface for sealed SpaceVIL OpenGL environment.
    /// </summary>
    internal interface IVramResource
    {
        bool SendUniformSample2D(Shader shader, string name);
        bool SendUniform4f(Shader shader, string name, float[] array);
        bool SendUniform1fv(Shader shader, string name, int count, float[] array);
        bool SendUniform2fv(Shader shader, string name, float[] array);
        bool SendUniform1f(Shader shader, string name, float array);
        bool SendUniform1i(Shader shader, String name, int value);
        void Draw();
        void Clear();
        void Bind();
        void Unbind();
    }
}