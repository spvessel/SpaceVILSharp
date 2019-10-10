using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using SpaceVIL.Core;

namespace OpenGL
{
    internal class WindowsGL : IOpenGL
    {
        public const string LIBRARY_OPENGL = "opengl32.dll";
        internal WindowsGL() { }

        public delegate void attachShader(uint program, uint shader);
        public void AttachShader(uint program, uint shader)
        {
            InvokeWGL<attachShader>("glAttachShader")(program, shader);
        }
        public delegate void bindBuffer(uint target, uint buffer);
        public void BindBuffer(uint target, uint buffer)
        {
            InvokeWGL<bindBuffer>("glBindBuffer")(target, buffer);
        }

        public delegate void bindFramebuffer(uint target, uint buffer);
        public void BindFramebuffer(uint target, uint buffer)
        {
            InvokeWGL<bindFramebuffer>("glBindFramebufferEXT")(target, buffer);
        }

        public void BindTexture(uint target, uint texture)
        {
            glBindTexture(target, texture);
        }

        public delegate void bindVertexArray(uint array);
        public void BindVertexArray(uint array)
        {
            InvokeWGL<bindVertexArray>("glBindVertexArray")(array);
        }

        public delegate uint blendFuncSeparate(uint srcRGB, uint dstRGB, uint srcAlpha, uint dstAlpha);
        public void BlendFuncSeparate(uint srcRGB, uint dstRGB, uint srcAlpha, uint dstAlpha)
        {
            InvokeWGL<blendFuncSeparate>("glBlendFuncSeparate")(srcRGB, dstRGB, srcAlpha, dstAlpha);
        }
        public delegate void bufferData(uint target, int size, IntPtr data, uint usage);
        public void BufferData(uint target, float[] data, uint usage)
        {
            IntPtr ptr = Marshal.AllocHGlobal(data.Length * sizeof(float));
            Marshal.Copy(data, 0, ptr, data.Length);
            InvokeWGL<bufferData>("glBufferData")(target, data.Length * sizeof(float), ptr, usage);
            Marshal.FreeHGlobal(ptr);
        }

        public void BufferData(uint target, int[] data, uint usage)
        {
            IntPtr ptr = Marshal.AllocHGlobal(data.Length * sizeof(float));
            Marshal.Copy(data, 0, ptr, data.Length);
            InvokeWGL<bufferData>("glBufferData")(target, data.Length * sizeof(uint), ptr, usage);
            Marshal.FreeHGlobal(ptr);
        }

        public void Clear(uint mask)
        {
            glClear(mask);
        }

        public void ClearColor(float red, float green, float blue, float alpha)
        {
            glClearColor(red, green, blue, alpha);
        }

        public delegate void compileShader(uint shader);
        public void CompileShader(uint shader)
        {
            InvokeWGL<compileShader>("glCompileShader")(shader);
        }

        public delegate uint createProgram();
        public uint CreateProgram()
        {
            return InvokeWGL<createProgram>("glCreateProgram")();
        }

        public delegate uint createShader(uint shader);
        public uint CreateShader(uint shader)
        {
            return InvokeWGL<createShader>("glCreateShader")(shader);
        }

        public void CullFace(uint mode)
        {
            glCullFace(mode);
        }

        public delegate void deleteBuffers(int n, uint[] buffers);
        public void DeleteBuffers(int n, uint[] buffers)
        {
            InvokeWGL<deleteBuffers>("glDeleteBuffers")(n, buffers);
        }

        public delegate void deleteFrameBuffers(int n, uint[] buffers);
        public void DeleteFramebuffers(int n, uint[] buffers)
        {
            InvokeWGL<deleteFrameBuffers>("glDeleteFramebuffersEXT")(n, buffers);
        }

        public delegate void deleteProgram(uint program);
        public void DeleteProgram(uint program)
        {
            InvokeWGL<deleteProgram>("glDeleteProgram")(program);
        }

        public delegate void deleteShader(uint shader);
        public void DeleteShader(uint shader)
        {
            InvokeWGL<deleteShader>("glDeleteShader")(shader);
        }

        public void DeleteTextures(int n, uint[] textures)
        {
            glDeleteTextures(n, textures);
        }

        public delegate void deleteVertexArrays(int n, uint[] arrays);
        public void DeleteVertexArrays(int n, uint[] arrays)
        {
            InvokeWGL<deleteVertexArrays>("glDeleteVertexArrays")(n, arrays);
        }

        public delegate void detachShader(uint program, uint shader);
        public void DetachShader(uint program, uint shader)
        {
            InvokeWGL<detachShader>("glDetachShader")(program, shader);
        }

        public void Disable(uint cap)
        {
            glDisable(cap);
        }

        public delegate void disableVertexAttribArray(uint index);
        public void DisableVertexAttribArray(uint index)
        {
            InvokeWGL<disableVertexAttribArray>("glDisableVertexAttribArray")(index);
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

        public delegate void enableVertexAttribArray(uint index);
        public void EnableVertexAttribArray(uint index)
        {
            InvokeWGL<enableVertexAttribArray>("glEnableVertexAttribArray")(index);
        }

        public delegate void framebufferTexture(uint target, uint attachment, uint texture, int level);
        public void FramebufferTexture(uint target, uint attachment, uint texture, int level)
        {
            InvokeWGL<framebufferTexture>("glFramebufferTextureEXT")(target, attachment, texture, level);
        }

        public delegate void genBuffers(int n, uint[] buffers);
        public void GenBuffers(int n, uint[] buffers)
        {
            InvokeWGL<genBuffers>("glGenBuffers")(n, buffers);
        }

        public delegate void genFramebuffers(int n, uint[] buffers);
        public void GenFramebuffers(int n, uint[] buffers)
        {
            InvokeWGL<genFramebuffers>("glGenFramebuffersEXT")(n, buffers);
        }

        public void GenTextures(int n, uint[] textures)
        {
            glGenTextures(n, textures);
        }

        public delegate void genVertexArrays(int n, uint[] arrays);
        public void GenVertexArrays(int n, uint[] arrays)
        {
            InvokeWGL<genVertexArrays>("glGenVertexArrays")(n, arrays);
        }

        public delegate int getuniformlocation(uint shader, string value);
        public int GetUniformLocation(uint shader, string value)
        {
            return InvokeWGL<getuniformlocation>("glGetUniformLocation")(shader, value);
        }

        public delegate void linkProgram(uint program);
        public void LinkProgram(uint program)
        {
            InvokeWGL<linkProgram>("glLinkProgram")(program);
        }

        public void Scissor(int x, int y, int width, int height)
        {
            glScissor(x, y, width, height);
        }

        public delegate void shaderSource(uint shader, int count, string[] source, int[] length);
        public void ShaderSource(uint shader, int count, string[] source, int[] length)
        {
            InvokeWGL<shaderSource>("glShaderSource")(shader, count, source, length);
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

        public delegate void texStorage2D(uint target, int level, uint internalformat, int width, int height);
        public void TexStorage2D(uint target, int level, uint internalformat, int width, int height)
        {
            InvokeWGL<texStorage2D>("glTexStorage2D")(target, level, internalformat, width, height);
        }

        public void TexSubImage2D(uint target, int level, int xoffset, int yoffset, int width, int height, uint format, uint type, byte[] pixels)
        {
            glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        }
        public void TexSubImage2D(uint target, int level, int xoffset, int yoffset, int width, int height, uint format, uint type, IntPtr pixels)
        {
            glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        }

        public delegate void uniform1f(int location, float v0);
        public void Uniform1f(int location, float v0)
        {
            InvokeWGL<uniform1f>("glUniform1f")(location, v0);
        }

        public delegate void uniform1fv(int location, int count, float[] value);
        public void Uniform1fv(int location, int count, float[] value)
        {
            InvokeWGL<uniform1fv>("glUniform1fv")(location, count, value);
        }

        public delegate void uniform1i(int location, int v0);
        public void Uniform1i(int location, int v0)
        {
            InvokeWGL<uniform1i>("glUniform1i")(location, v0);
        }

        public delegate void uniform2fv(int location, int count, float[] value);
        public void Uniform2fv(int location, int count, float[] value)
        {
            InvokeWGL<uniform2fv>("glUniform2fv")(location, count, value);
        }

        public delegate void uniform4f(int location, float v0, float v1, float v2, float v3);
        public void Uniform4f(int location, float v0, float v1, float v2, float v3)
        {
            InvokeWGL<uniform4f>("glUniform4f")(location, v0, v1, v2, v3);
        }

        public delegate void useProgram(uint program);
        public void UseProgram(uint program)
        {
            InvokeWGL<useProgram>("glUseProgram")(program);
        }

        public delegate void vertexAttribPointer(uint index, int size, uint type, bool normalized, int stride, IntPtr pointer);
        public void VertexAttribPointer(uint index, int size, uint type, bool normalized, int stride, IntPtr pointer)
        {
            InvokeWGL<vertexAttribPointer>("glVertexAttribPointer")(index, size, type, normalized, stride, pointer);
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

        //Extensions
        static private Dictionary<string, Delegate> extensionFunctions = new Dictionary<string, Delegate>();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern IntPtr wglGetProcAddress(string name);
        static private Object locker = new Object();

        private static T InvokeWGL<T>(String name) where T : class
        {
            Type delegateType = typeof(T);
            Delegate del = null;
            if (extensionFunctions.TryGetValue(name, out del) == false)
            {
                IntPtr proc = wglGetProcAddress(name);
                if (proc == IntPtr.Zero)
                    throw new SpaceVILException("SpaceVILException: Extension function " + name + " not supported");
                del = Marshal.GetDelegateForFunctionPointer(proc, delegateType);
                extensionFunctions.Add(name, del);
            }
            return del as T;
        }
    }
}
