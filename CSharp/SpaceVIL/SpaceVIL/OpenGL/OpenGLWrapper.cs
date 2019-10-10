using System;

namespace OpenGL
{
    internal static class OpenGLWrapper
    {
        internal static IOpenGL GL = null;

        internal static void glAttachShader(uint program, uint shader)
        {
            GL.AttachShader(program, shader);
        }

        internal static void glBindBuffer(uint target, uint buffer)
        {
            GL.BindBuffer(target, buffer);
        }

        internal static void glBindFramebuffer(uint target, uint buffer)
        {
            GL.BindFramebuffer(target, buffer);
        }

        internal static void glBindTexture(uint target, uint texture)
        {
            GL.BindTexture(target, texture);
        }

        internal static void glBindVertexArray(uint array)
        {
            GL.BindVertexArray(array);
        }

        internal static void glBlendFuncSeparate(uint srcRGB, uint dstRGB, uint srcAlpha, uint dstAlpha)
        {
            GL.BlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
        }

        internal static void glBufferData(uint target, float[] data, uint usage)
        {
            GL.BufferData(target, data, usage);
        }

        internal static void glBufferData(uint target, int[] data, uint usage)
        {
            GL.BufferData(target, data, usage);
        }

        internal static void glClear(uint mask)
        {
            GL.Clear(mask);
        }

        internal static void glClearColor(float red, float green, float blue, float alpha)
        {
            GL.ClearColor(red, green, blue, alpha);
        }

        internal static void glCompileShader(uint shader)
        {
            GL.CompileShader(shader);
        }

        internal static uint glCreateProgram()
        {
            return GL.CreateProgram();
        }

        internal static uint glCreateShader(uint shader)
        {
            return GL.CreateShader(shader);
        }

        internal static void glCullFace(uint mode)
        {
            GL.CullFace(mode);
        }

        internal static void glDeleteBuffers(int n, uint[] buffers)
        {
            GL.DeleteBuffers(n, buffers);
        }

        internal static void glDeleteFramebuffers(int n, uint[] buffers)
        {
            GL.DeleteFramebuffers(n, buffers);
        }

        internal static void glDeleteProgram(uint program)
        {
            GL.DeleteProgram(program);
        }

        internal static void glDeleteShader(uint shader)
        {
            GL.DeleteShader(shader);
        }

        internal static void glDeleteTextures(int n, uint[] textures)
        {
            GL.DeleteTextures(n, textures);
        }

        internal static void glDeleteVertexArrays(int n, uint[] arrays)
        {
            GL.DeleteVertexArrays(n, arrays);
        }

        internal static void glDetachShader(uint program, uint shader)
        {
            GL.DetachShader(program, shader);
        }

        internal static void glDisable(uint cap)
        {
            GL.Disable(cap);
        }

        internal static void glDisableVertexAttribArray(uint index)
        {
            GL.DisableVertexAttribArray(index);
        }

        internal static void glDrawArrays(uint mode, int first, int count)
        {
            GL.DrawArrays(mode, first, count);
        }

        internal static void glDrawElements(uint mode, int count, uint type, IntPtr indices)
        {
            GL.DrawElements(mode, count, type, indices);
        }

        internal static void glEnable(uint cap)
        {
            GL.Enable(cap);
        }

        internal static void glEnableVertexAttribArray(uint index)
        {
            GL.EnableVertexAttribArray(index);
        }

        internal static void glFramebufferTexture(uint target, uint attachment, uint texture, int level)
        {
            GL.FramebufferTexture(target, attachment, texture, level);
        }

        internal static void glGenBuffers(int n, uint[] buffers)
        {
            GL.GenBuffers(n, buffers);
        }

        internal static void glGenFramebuffers(int n, uint[] buffers)
        {
            GL.GenFramebuffers(n, buffers);
        }

        internal static void glGenTextures(int n, uint[] textures)
        {
            GL.GenTextures(n, textures);
        }

        internal static void glGenVertexArrays(int n, uint[] arrays)
        {
            GL.GenVertexArrays(n, arrays);
        }

        internal static int glGetUniformLocation(uint shader, string value)
        {
            return GL.GetUniformLocation(shader, value);
        }

        internal static void glLinkProgram(uint program)
        {
            GL.LinkProgram(program);
        }

        internal static void glScissor(int x, int y, int width, int height)
        {
            GL.Scissor(x, y, width, height);
        }

        internal static void glShaderSource(uint shader, int count, string[] source, int[] length)
        {
            GL.ShaderSource(shader, count, source, length);
        }

        internal static void glTexImage2D(uint target, int level, uint internalformat, int width, int height, int border, uint format, uint type, IntPtr pixels)
        {
            GL.TexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
        }

        internal static void glTexParameterf(uint target, uint pname, float param)
        {
            GL.TexParameterf(target, pname, param);
        }

        internal static void glTexParameteri(uint target, uint pname, uint param)
        {
            GL.TexParameteri(target, pname, param);
        }

        internal static void glTexStorage2D(uint target, int level, uint internalformat, int width, int height)
        {
            GL.TexStorage2D(target, level, internalformat, width, height);
        }

        internal static void glTexSubImage2D(uint target, int level, int xoffset, int yoffset, int width, int height, uint format, uint type, byte[] pixels)
        {
            GL.TexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        }

        internal static void glTexSubImage2D(uint target, int level, int xoffset, int yoffset, int width, int height, uint format, uint type, IntPtr pixels)
        {
            GL.TexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        }

        internal static void glUniform1f(int location, float v0)
        {
            GL.Uniform1f(location, v0);
        }

        internal static void glUniform1fv(int location, int count, float[] value)
        {
            GL.Uniform1fv(location, count, value);
        }

        internal static void glUniform1i(int location, int v0)
        {
            GL.Uniform1i(location, v0);
        }

        internal static void glUniform2fv(int location, int count, float[] value)
        {
            GL.Uniform2fv(location, count, value);
        }

        internal static void glUniform4f(int location, float v0, float v1, float v2, float v3)
        {
            GL.Uniform4f(location, v0, v1, v2, v3);
        }

        internal static void glUseProgram(uint program)
        {
            GL.UseProgram(program);
        }

        internal static void glVertexAttribPointer(uint index, int size, uint type, bool normalized, int stride, IntPtr pointer)
        {
            GL.VertexAttribPointer(index, size, type, normalized, stride, pointer);
        }

        internal static void glViewport(int x, int y, int width, int height)
        {
            GL.Viewport(x, y, width, height);
        }
    }
}