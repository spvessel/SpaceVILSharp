using System;
using System.Runtime.InteropServices;

namespace OpenGL
{
    internal class MacGL : IOpenGL
    {
        public const string LIBRARY_OPENGL = "//System//Library//Frameworks//OpenGL.framework//Libraries//libGL.dylib";
        internal MacGL() { }

        public void AttachShader(uint program, uint shader)
        {
            glAttachShader(program, shader);
        }

        public void BindBuffer(uint target, uint buffer)
        {
            glBindBuffer(target, buffer);
        }

        public void BindFramebuffer(uint target, uint buffer)
        {
            glBindFramebufferEXT(target, buffer);
        }

        public void BindTexture(uint target, uint texture)
        {
            glBindTexture(target, texture);
        }

        public void BindVertexArray(uint array)
        {
            glBindVertexArray(array);
        }

        public void BlendFuncSeparate(uint srcRGB, uint dstRGB, uint srcAlpha, uint dstAlpha)
        {
            glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
        }

        public void BufferData(uint target, float[] data, uint usage)
        {
            IntPtr vp = Marshal.AllocHGlobal(data.Length * sizeof(float));
            Marshal.Copy(data, 0, vp, data.Length);
            glBufferData(target, data.Length * sizeof(float), vp, usage);
            Marshal.FreeHGlobal(vp);
        }

        public void BufferData(uint target, int[] data, uint usage)
        {
            IntPtr vp = Marshal.AllocHGlobal(data.Length * sizeof(float));
            Marshal.Copy(data, 0, vp, data.Length);
            glBufferData(target, data.Length * sizeof(float), vp, usage);
            Marshal.FreeHGlobal(vp);
        }

        public void Clear(uint mask)
        {
            glClear(mask);
        }

        public void ClearColor(float red, float green, float blue, float alpha)
        {
            glClearColor(red, green, blue, alpha);
        }

        public void CompileShader(uint shader)
        {
            glCompileShader(shader);
        }

        public uint CreateProgram()
        {
            return glCreateProgram();
        }

        public uint CreateShader(uint shader)
        {
            return glCreateShader(shader);
        }

        public void CullFace(uint mode)
        {
            glCullFace(mode);
        }

        public void DeleteBuffers(int n, uint[] buffers)
        {
            glDeleteBuffers(n, buffers);
        }

        public void DeleteFramebuffers(int n, uint[] buffers)
        {
            glDeleteFramebuffersEXT(n, buffers);
        }

        public void DeleteProgram(uint program)
        {
            glDeleteProgram(program);
        }

        public void DeleteShader(uint shader)
        {
            glDeleteShader(shader);
        }

        public void DeleteTextures(int n, uint[] textures)
        {
            glDeleteTextures(n, textures);
        }

        public void DeleteVertexArrays(int n, uint[] arrays)
        {
            glDeleteVertexArrays(n, arrays);
        }

        public void DetachShader(uint program, uint shader)
        {
            glDetachShader(program, shader);
        }

        public void Disable(uint cap)
        {
            glDisable(cap);
        }

        public void DisableVertexAttribArray(uint index)
        {
            glDisableVertexAttribArray(index);
        }

        public void DrawArrays(uint mode, int first, int count)
        {
            glDrawArrays(mode, first, count);
        }

        public void DrawElements(uint mode, int count, uint type, IntPtr indices)
        {
            glDrawElements(mode, count, type, indices);
        }

        public void Enable(uint cap)
        {
            glEnable(cap);
        }

        public void EnableVertexAttribArray(uint index)
        {
            glEnableVertexAttribArray(index);
        }

        public void FramebufferTexture(uint target, uint attachment, uint texture, int level)
        {
            glFramebufferTextureEXT(target, attachment, texture, level);
        }

        public void GenBuffers(int n, uint[] buffers)
        {
            glGenBuffers(n, buffers);
        }

        public void GenFramebuffers(int n, uint[] buffers)
        {
            glGenFramebuffersEXT(n, buffers);
        }

        public void GenTextures(int n, uint[] textures)
        {
            glGenTextures(n, textures);
        }

        public void GenVertexArrays(int n, uint[] arrays)
        {
            glGenVertexArrays(n, arrays);
        }

        public int GetUniformLocation(uint shader, string value)
        {
            return glGetUniformLocation(shader, value);
        }

        public void LinkProgram(uint program)
        {
            glLinkProgram(program);
        }

        public void Scissor(int x, int y, int width, int height)
        {
            glScissor(x, y, width, height);
        }

        public void ShaderSource(uint shader, int count, string[] source, int[] length)
        {
            glShaderSource(shader, count, source, length);
        }

        public void TexImage2D(uint target, int level, uint internalformat, int width, int height, int border, uint format, uint type, IntPtr pixels)
        {
            glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
        }

        public void TexParameterf(uint target, uint pname, float param)
        {
            glTexParameterf(target, pname, param);
        }

        public void TexParameteri(uint target, uint pname, uint param)
        {
            glTexParameteri(target, pname, param);
        }

        public void TexStorage2D(uint target, int level, uint internalformat, int width, int height)
        {
            glTexStorage2D(target, level, internalformat, width, height);
        }

        public void TexSubImage2D(uint target, int level, int xoffset, int yoffset, int width, int height, uint format, uint type, byte[] pixels)
        {
            glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        }

        public void TexSubImage2D(uint target, int level, int xoffset, int yoffset, int width, int height, uint format, uint type, IntPtr pixels)
        {
            glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        }

        public void Uniform1f(int location, float v0)
        {
            glUniform1f(location, v0);
        }

        public void Uniform1fv(int location, int count, float[] value)
        {
            glUniform1fv(location, count, value);
        }

        public void Uniform1i(int location, int v0)
        {
            glUniform1i(location, v0);
        }

        public void Uniform2fv(int location, int count, float[] value)
        {
            glUniform2fv(location, count, value);
        }

        public void Uniform3f(int location, float v0, float v1, float v2)
        {
            glUniform3f(location, v0, v1, v2);
        }

        public void Uniform4f(int location, float v0, float v1, float v2, float v3)
        {
            glUniform4f(location, v0, v1, v2, v3);
        }

        public void UniformMatrix4fv(int location, int count, bool transpose, float[] value)
        {
            glUniformMatrix4fv(location, count, transpose, value);
        }

        public void UseProgram(uint program)
        {
            glUseProgram(program);
        }

        public void VertexAttribPointer(uint index, int size, uint type, bool normalized, int stride, IntPtr pointer)
        {
            glVertexAttribPointer(index, size, type, normalized, stride, pointer);
        }

        public void Viewport(int x, int y, int width, int height)
        {
            glViewport(x, y, width, height);
        }


        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBindTexture(uint target, uint texture);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glClear(uint mask);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glClearColor(float red, float green, float blue, float alpha);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCullFace(uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteTextures(int n, uint[] textures);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDisable(uint cap);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawArrays(uint mode, int first, int count);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawBuffer(uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawElements(uint mode, int count, uint type, IntPtr indices);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEnable(uint cap);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGenTextures(int n, uint[] textures);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glScissor(int x, int y, int width, int height);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexImage2D(uint target, int level, uint internalformat, int width, int height, int border, uint format, uint type, IntPtr pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexParameterf(uint target, uint pname, float param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexParameteri(uint target, uint pname, uint param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexSubImage2D(uint target, int level, int xoffset, int yoffset, int width, int height, uint format, uint type, byte[] pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexSubImage2D(uint target, int level, int xoffset, int yoffset, int width, int height, uint format, uint type, IntPtr pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glViewport(int x, int y, int width, int height);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexStorage2D(uint target, int level, uint internalformat, int width, int height);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern int glGetUniformLocation(uint shader, string value);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern uint glCreateShader(uint type);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glShaderSource(uint shader, int count, string[] source, int[] length);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCompileShader(uint shader);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern uint glCreateProgram();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glAttachShader(uint program, uint shader);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLinkProgram(uint program);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGenVertexArrays(int n, uint[] arrays);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBindVertexArray(uint array);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUseProgram(uint program);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDetachShader(uint program, uint shader);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteShader(uint shader);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteVertexArrays(int n, uint[] arrays);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteProgram(uint program);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGenBuffers(int n, uint[] buffers);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGenFramebuffersEXT(int n, uint[] buffers);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBlendFuncSeparate(uint srcRGB, uint dstRGB, uint srcAlpha, uint dstAlpha);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBindBuffer(uint target, uint buffer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBindFramebufferEXT(uint target, uint buffer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBufferData(uint target, int size, IntPtr data, uint usage);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertexAttribPointer(uint index, int size, uint type, bool normalized, int stride, IntPtr pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDisableVertexAttribArray(uint index);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEnableVertexAttribArray(uint index);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteBuffers(int n, uint[] buffers);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteFramebuffersEXT(int n, uint[] buffers);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform1f(int location, float v0);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform3f(int location, float v0, float v1, float v2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform4f(int location, float v0, float v1, float v2, float v3);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniformMatrix4fv(int location, int count, bool transpose, float[] value);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform1i(int location, int v0);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform1fv(int location, int count, float[] value);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform2fv(int location, int count, float[] value);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glFramebufferTextureEXT(uint target, uint attachment, uint texture, int level);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glFramebufferTexture2DEXT(uint target, uint attachment, uint textarget, uint texture, int level);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawBuffers(int n, uint[] bufs);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCheckFramebufferStatusEXT(uint target);
        // stencil
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glClearStencil(int s);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glStencilFunc(uint func, int refnotkeword, uint mask);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glStencilMask(uint mask);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glStencilOp(uint fail, uint zfail, uint zpass);

        public void ClearStencil(int value)
        {
            glClearStencil(value);
        }

        public void StencilMask(uint mask)
        {
            glStencilMask(mask);
        }

        public void StencilFunc(uint func, int refnotkeword, uint mask)
        {
            glStencilFunc(func, refnotkeword, mask);
        }

        public void StencilOp(uint fail, uint zfail, uint zpass)
        {
            glStencilOp(fail, zfail, zpass);
        }
    }
}
