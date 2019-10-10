using System;

namespace OpenGL
{
    internal interface IOpenGL
    {
        //Common
        void Clear(uint mask);
        void Enable(uint cap);
        void Disable(uint cap);
        void CullFace(uint mode);
        void BlendFuncSeparate(uint srcRGB, uint dstRGB, uint srcAlpha, uint dstAlpha);
        void Viewport(int x, int y, int width, int height);
        void GenVertexArrays(int n, uint[] arrays);
        void BindVertexArray(uint array);
        void DeleteVertexArrays(int n, uint[] arrays);
        void ClearColor(float red, float green, float blue, float alpha);
        void GenBuffers(int n, uint[] buffers);
        void BindBuffer(uint target, uint buffer);
        void BufferData(uint target, float[] data, uint usage);
        void DeleteBuffers(int n, uint[] buffers);
        void VertexAttribPointer(uint index, int size, uint type, bool normalized, int stride, IntPtr pointer);
        void EnableVertexAttribArray(uint index);
        void DisableVertexAttribArray(uint index);
        void DrawArrays(uint mode, int first, int count);
        void TexParameteri(uint target, uint pname, uint param);

        //Texture
        void GenTextures(int n, uint[] textures);
        void BindTexture(uint target, uint texture);
        void DeleteTextures(int n, uint[] textures);
        void TexStorage2D(uint target, int level, uint internalformat, int width, int height);
        void TexSubImage2D(uint target, int level, int xoffset, int yoffset, int width, int height, uint format, uint type, byte[] pixels);
        void TexSubImage2D(uint target, int level, int xoffset, int yoffset, int width, int height, uint format, uint type, IntPtr pixels);
        void DrawElements(uint mode, int count, uint type, IntPtr indices);
        void BufferData(uint target, int[] data, uint usage);

        //Shader
        void UseProgram(uint program);
        uint CreateProgram();
        void LinkProgram(uint program);
        void DeleteProgram(uint program);
        uint CreateShader(uint shader);
        void AttachShader(uint program, uint shader);
        void ShaderSource(uint shader, int count, string[] source, int[] length);
        void CompileShader(uint shader);
        void DetachShader(uint program, uint shader);
        void DeleteShader(uint shader);
        int GetUniformLocation(uint shader, string value);
        void Uniform1i(int location, int v0);
        void Uniform4f(int location, float v0, float v1, float v2, float v3);
        void Uniform1fv(int location, int count, float[] value);
        void Uniform2fv(int location, int count, float[] value);
        void Uniform1f(int location, float v0);

        //Framebuffer
        void GenFramebuffers(int n, uint[] buffers);
        void BindFramebuffer(uint target, uint buffer);
        void TexImage2D(uint target, int level, uint internalformat, int width, int height, int border, uint format, uint type, IntPtr pixels);
        void FramebufferTexture(uint target, uint attachment, uint texture, int level);
        void DeleteFramebuffers(int n, uint[] buffers);
        void TexParameterf(uint target, uint pname, float param);

        //Stencil & Scissors
        void Scissor(int x, int y, int width, int height);
    }
}