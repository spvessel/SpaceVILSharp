package com.spvessel.spacevil.internal.Wrapper;

/**
 * The minimum required OpenGL constants and functions for SpaceVIL framework to
 * work.
 */
public final class OpenGLWrapper {

    private OpenGLWrapper() {
    }

    private static OpenGLWrapper instance = null;

    public static OpenGLWrapper get() {
        if (instance == null) {
            instance = new OpenGLWrapper();
        }
        return instance;
    }

    // OpenGL constants
    public static int GL_TRUE = 1;
    public static int GL_FALSE = 0;

    public static int GL_BYTE = 0x1400;
    public static int GL_UNSIGNED_BYTE = 0x1401;
    public static int GL_SHORT = 0x1402;
    public static int GL_UNSIGNED_SHORT = 0x1403;
    public static int GL_INT = 0x1404;
    public static int GL_UNSIGNED_INT = 0x1405;
    public static int GL_FLOAT = 0x1406;
    public static int GL_2_BYTES = 0x1407;
    public static int GL_3_BYTES = 0x1408;
    public static int GL_4_BYTES = 0x1409;
    public static int GL_DOUBLE = 0x140A;

    public static int GL_FRAMEBUFFER_EXT = 0x8D40;
    public static int GL_RENDERBUFFER_EXT = 0x8D41;

    public static int GL_COLOR_ATTACHMENT0_EXT = 0x8CE0;
    public static int GL_COLOR_ATTACHMENT1_EXT = 0x8CE1;
    public static int GL_COLOR_ATTACHMENT2_EXT = 0x8CE2;
    public static int GL_COLOR_ATTACHMENT3_EXT = 0x8CE3;
    public static int GL_COLOR_ATTACHMENT4_EXT = 0x8CE4;
    public static int GL_COLOR_ATTACHMENT5_EXT = 0x8CE5;
    public static int GL_COLOR_ATTACHMENT6_EXT = 0x8CE6;
    public static int GL_COLOR_ATTACHMENT7_EXT = 0x8CE7;
    public static int GL_COLOR_ATTACHMENT8_EXT = 0x8CE8;
    public static int GL_COLOR_ATTACHMENT9_EXT = 0x8CE9;
    public static int GL_COLOR_ATTACHMENT10_EXT = 0x8CEA;
    public static int GL_COLOR_ATTACHMENT11_EXT = 0x8CEB;
    public static int GL_COLOR_ATTACHMENT12_EXT = 0x8CEC;
    public static int GL_COLOR_ATTACHMENT13_EXT = 0x8CED;
    public static int GL_COLOR_ATTACHMENT14_EXT = 0x8CEE;
    public static int GL_COLOR_ATTACHMENT15_EXT = 0x8CEF;
    public static int GL_DEPTH_ATTACHMENT_EXT = 0x8D00;
    public static int GL_STENCIL_ATTACHMENT_EXT = 0x8D20;

    public static int GL_STREAM_DRAW = 0x88E0;
    public static int GL_STREAM_READ = 0x88E1;
    public static int GL_STREAM_COPY = 0x88E2;
    public static int GL_STATIC_DRAW = 0x88E4;
    public static int GL_STATIC_READ = 0x88E5;
    public static int GL_STATIC_COPY = 0x88E6;
    public static int GL_DYNAMIC_DRAW = 0x88E8;
    public static int GL_DYNAMIC_READ = 0x88E9;
    public static int GL_DYNAMIC_COPY = 0x88EA;

    public static int GL_ALPHA_TEST = 3008;
    public static int GL_ARRAY_BUFFER = 34962;
    public static int GL_BACK = 1029;
    public static int GL_BLEND = 3042;
    public static int GL_BGRA = 0x80E1;
    public static int GL_CLAMP_TO_BORDER = 33069;
    public static int GL_CLAMP_TO_EDGE = 0x812F;
    public static int GL_COLOR_BUFFER_BIT = 0x00004000;
    public static int GL_CULL_FACE = 2884;
    public static int GL_DEPTH_BUFFER_BIT = 0x00000100;
    public static int GL_DEPTH_TEST = 0x0B71;
    public static int GL_DRAW_FRAMEBUFFER_BINDING = 0x8CA6;
    public static int GL_DST_ALPHA = 0x0304;
    public static int GL_ELEMENT_ARRAY_BUFFER = 34963;

    public static int GL_LINEAR = 0x2601;
    public static int GL_MODULATE = 0x2100;
    public static int GL_NEAREST = 0x2600;
    public static int GL_NICEST = 0x1102;
    public static int GL_ONE = 1;
    public static int GL_ONE_MINUS_SRC_ALPHA = 771;
    public static int GL_RGBA = 6408;
    public static int GL_RGBA8 = 32856;
    public static int GL_POLYGON_SMOOTH_HINT = 0x0C53;
    public static int GL_DRAW = 35044;
    public static int GL_SCISSOR_TEST = 0x0C11;
    public static int GL_SRC_ALPHA = 770;

    public static int GL_STENCIL_BUFFER_BIT = 0x00000400;
    public static int GL_STENCIL_TEST = 0x0B90;
    public static int GL_NEVER = 0x0200;
    public static int GL_LESS = 0x0201;
    public static int GL_EQUAL = 0x0202;
    public static int GL_LEQUAL = 0x0203;
    public static int GL_GREATER = 0x0204;
    public static int GL_NOTEQUAL = 0x0205;
    public static int GL_GEQUAL = 0x0206;
    public static int GL_ALWAYS = 0x0207;

    public static int GL_KEEP = 0x1E00;
    public static int GL_REPLACE = 0x1E01;
    public static int GL_INCR = 0x1E02;
    public static int GL_DECR = 0x1E03;

    public static int GL_TEXTURE_2D = 0x0DE1;
    public static int GL_TEXTURE_BINDING_2D = 0x8069;
    public static int GL_TEXTURE_MAG_FILTER = 0x2800;
    public static int GL_TEXTURE_MIN_FILTER = 0x2801;
    public static int GL_TEXTURE_WRAP_S = 0x2802;
    public static int GL_TEXTURE_WRAP_T = 0x2803;
    public static int GL_TEXTURE_ENV = 0x2300;

    public static int GL_POINTS = 0x0000;
    public static int GL_LINES = 0x0001;
    public static int GL_LINE_LOOP = 0x0002;
    public static int GL_LINE_STRIP = 0x0003;
    public static int GL_TRIANGLES = 0x0004;
    public static int GL_TRIANGLE_STRIP = 0x0005;
    public static int GL_TRIANGLE_FAN = 0x0006;
    public static int GL_QUADS = 0x0007;
    public static int GL_QUAD_STRIP = 0x0008;
    public static int GL_POLYGON = 0x0009;

    public static int GL_VERTEX_SHADER = 35633;
    public static int GL_FRAGMENT_SHADER = 35632;

    // OpenGL extension functions

    public native void ActiveTexture(int texture);

    public native void AttachShader(int program, int shader);

    public native void BindBuffer(int target, int buffer);

    public native void BindFramebuffer(int target, int buffer);

    public native void BindRenderbuffer(int target, int buffer);

    public native void FramebufferRenderbuffer(int target, int attachment, int component, int buffer);

    public native void BindVertexArray(int array);

    public native void BlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha);

    public native void BufferDataf(int target, int size, float[] data, int usage);

    public native void BufferDatai(int target, int size, int[] data, int usage);

    public native void DeleteBuffer(int index);

    public native void DeleteFramebuffer(int index);

    public native void DeleteRenderbuffer(int index);

    public native void RenderbufferStorage(int target, int component, int w, int h);

    public native void DeleteProgram(int program);

    public native void CompileShader(int shader);

    public native int CreateProgram();

    public native int CreateShader(int shaderType);

    public native void DeleteShader(int shader);

    public native void DetachShader(int program, int shader);

    public native void DisableVertexAttribArray(int index);

    public native void EnableVertexAttribArray(int index);

    public native void FramebufferTexture(int target, int attachment, int texture, int level);

    public native int GenBuffer();

    public native int GenFramebuffer();

    public native int GenRenderbuffer();

    public native int GenVertexArray();

    public native void DeleteVertexArray(int array);

    public native int GetUniformLocation(int program, String name);

    public native void LinkProgram(int program);

    public native void ShaderSource(int shader, String sourceCode);

    public native void TexStorage2D(int target, int level, int internalformat, int width, int height);

    public native void Uniform1f(int location, float v0);

    public native void Uniform1fv(int location, int count, float[] value);

    public native void Uniform1i(int location, int v0);

    public native void Uniform2fv(int location, int count, float[] value);

    public native void Uniform3f(int location, float v0, float v1, float v2);

    public native void Uniform4f(int location, float v0, float v1, float v2, float v3);

    public native void UniformMatrix4fv(int location, int count, boolean transpose, float[] value);

    public native void UseProgram(int program);

    public native void VertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long pointer);

    // OpenGL fixed pipeline functions
    public native void Viewport(int x, int y, int width, int height);

    public native void ClearColor(float r, float g, float b, float a);

    public native void Clear(int mask);

    public native void Finish();

    public native void Enable(int cap);

    public native void Disable(int cap);

    public native void CullFace(int mode);

    public native void BindTexture(int target, int texture);

    public native void Hint(int target, int mode);

    public native int GetIntegerv(int pname);

    public native int GenTexture();

    public native void BlendFunc(int sfactor, int dfactor);

    public native void TexImage2DEmpty(int target, int level, int internalformat, int width, int height, int border,
            int format, int type);

    public native void TexImage2D(int target, int level, int internalformat, int width, int height, int border, int format,
            int type, byte[] data);

    public native void TexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format,
            int type, byte[] pixels);

    public native void TexParameteri(int target, int pname, int param);

    public native void DrawArrays(int mode, int first, int count);

    public native void DrawElements(int mode, int count, int type);

    public native void DeleteTexture(int index);

    public native void DrawBuffer(int mode);

    public native void Scissor(int x, int y, int width, int height);

    public native void ClearStencil(int s);

    public native void StencilFunc(int func, int refnotkeword, int mask);

    public native void StencilMask(int mask);

    public native void StencilOp(int fail, int zfail, int zpass);

}