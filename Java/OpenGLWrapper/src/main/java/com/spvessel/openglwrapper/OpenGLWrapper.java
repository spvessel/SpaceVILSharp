package com.spvessel.openglwrapper;

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
            NativeLibraryManager.ExtractEmbeddedLibrary();
        }
        return instance;
    }

    // OpenGL constants
    public static final int GL_TRUE = 1;
    public static final int GL_FALSE = 0;

    public static final int GL_BYTE = 0x1400;
    public static final int GL_UNSIGNED_BYTE = 0x1401;
    public static final int GL_SHORT = 0x1402;
    public static final int GL_UNSIGNED_SHORT = 0x1403;
    public static final int GL_INT = 0x1404;
    public static final int GL_UNSIGNED_INT = 0x1405;
    public static final int GL_FLOAT = 0x1406;
    public static final int GL_2_BYTES = 0x1407;
    public static final int GL_3_BYTES = 0x1408;
    public static final int GL_4_BYTES = 0x1409;
    public static final int GL_DOUBLE = 0x140A;

    public static final int GL_FRAMEBUFFER_EXT = 0x8D40;
    public static final int GL_RENDERBUFFER_EXT = 0x8D41;

    public static final int GL_COLOR_ATTACHMENT0_EXT = 0x8CE0;
    public static final int GL_COLOR_ATTACHMENT1_EXT = 0x8CE1;
    public static final int GL_COLOR_ATTACHMENT2_EXT = 0x8CE2;
    public static final int GL_COLOR_ATTACHMENT3_EXT = 0x8CE3;
    public static final int GL_COLOR_ATTACHMENT4_EXT = 0x8CE4;
    public static final int GL_COLOR_ATTACHMENT5_EXT = 0x8CE5;
    public static final int GL_COLOR_ATTACHMENT6_EXT = 0x8CE6;
    public static final int GL_COLOR_ATTACHMENT7_EXT = 0x8CE7;
    public static final int GL_COLOR_ATTACHMENT8_EXT = 0x8CE8;
    public static final int GL_COLOR_ATTACHMENT9_EXT = 0x8CE9;
    public static final int GL_COLOR_ATTACHMENT10_EXT = 0x8CEA;
    public static final int GL_COLOR_ATTACHMENT11_EXT = 0x8CEB;
    public static final int GL_COLOR_ATTACHMENT12_EXT = 0x8CEC;
    public static final int GL_COLOR_ATTACHMENT13_EXT = 0x8CED;
    public static final int GL_COLOR_ATTACHMENT14_EXT = 0x8CEE;
    public static final int GL_COLOR_ATTACHMENT15_EXT = 0x8CEF;
    public static final int GL_DEPTH_ATTACHMENT_EXT = 0x8D00;
    public static final int GL_STENCIL_ATTACHMENT_EXT = 0x8D20;

    public static final int GL_STREAM_DRAW = 0x88E0;
    public static final int GL_STREAM_READ = 0x88E1;
    public static final int GL_STREAM_COPY = 0x88E2;
    public static final int GL_STATIC_DRAW = 0x88E4;
    public static final int GL_STATIC_READ = 0x88E5;
    public static final int GL_STATIC_COPY = 0x88E6;
    public static final int GL_DYNAMIC_DRAW = 0x88E8;
    public static final int GL_DYNAMIC_READ = 0x88E9;
    public static final int GL_DYNAMIC_COPY = 0x88EA;

    public static final int GL_ALPHA_TEST = 3008;
    public static final int GL_ARRAY_BUFFER = 34962;
    public static final int GL_BACK = 1029;
    public static final int GL_BLEND = 3042;
    public static final int GL_BGRA = 0x80E1;
    public static final int GL_CLAMP_TO_BORDER = 33069;
    public static final int GL_CLAMP_TO_EDGE = 0x812F;
    public static final int GL_COLOR_BUFFER_BIT = 0x00004000;
    public static final int GL_CULL_FACE = 2884;
    public static final int GL_DEPTH_BUFFER_BIT = 0x00000100;
    public static final int GL_DEPTH_TEST = 0x0B71;
    public static final int GL_DRAW_FRAMEBUFFER_BINDING = 0x8CA6;
    public static final int GL_DST_ALPHA = 0x0304;
    public static final int GL_ELEMENT_ARRAY_BUFFER = 34963;

    public static final int GL_LINEAR = 0x2601;
    public static final int GL_MODULATE = 0x2100;
    public static final int GL_NEAREST = 0x2600;
    public static final int GL_NICEST = 0x1102;
    public static final int GL_ONE = 1;
    public static final int GL_ONE_MINUS_SRC_ALPHA = 771;
    public static final int GL_RGBA = 6408;
    public static final int GL_RGBA8 = 32856;
    public static final int GL_POLYGON_SMOOTH_HINT = 0x0C53;
    public static final int GL_DRAW = 35044;
    public static final int GL_SCISSOR_TEST = 0x0C11;
    public static final int GL_SRC_ALPHA = 770;

    public static final int GL_STENCIL_BUFFER_BIT = 0x00000400;
    public static final int GL_STENCIL_TEST = 0x0B90;
    public static final int GL_NEVER = 0x0200;
    public static final int GL_LESS = 0x0201;
    public static final int GL_EQUAL = 0x0202;
    public static final int GL_LEQUAL = 0x0203;
    public static final int GL_GREATER = 0x0204;
    public static final int GL_NOTEQUAL = 0x0205;
    public static final int GL_GEQUAL = 0x0206;
    public static final int GL_ALWAYS = 0x0207;

    public static final int GL_KEEP = 0x1E00;
    public static final int GL_REPLACE = 0x1E01;
    public static final int GL_INCR = 0x1E02;
    public static final int GL_DECR = 0x1E03;

    public static final int GL_TEXTURE_2D = 0x0DE1;
    public static final int GL_TEXTURE_BINDING_2D = 0x8069;
    public static final int GL_TEXTURE_MAG_FILTER = 0x2800;
    public static final int GL_TEXTURE_MIN_FILTER = 0x2801;
    public static final int GL_TEXTURE_WRAP_S = 0x2802;
    public static final int GL_TEXTURE_WRAP_T = 0x2803;
    public static final int GL_TEXTURE_ENV = 0x2300;

    public static final int GL_POINTS = 0x0000;
    public static final int GL_LINES = 0x0001;
    public static final int GL_LINE_LOOP = 0x0002;
    public static final int GL_LINE_STRIP = 0x0003;
    public static final int GL_TRIANGLES = 0x0004;
    public static final int GL_TRIANGLE_STRIP = 0x0005;
    public static final int GL_TRIANGLE_FAN = 0x0006;
    public static final int GL_QUADS = 0x0007;
    public static final int GL_QUAD_STRIP = 0x0008;
    public static final int GL_POLYGON = 0x0009;

    public static final int GL_VERTEX_SHADER = 35633;
    public static final int GL_FRAGMENT_SHADER = 35632;

    // OpenGL extension functions

    public native void glActiveTexture(int texture);

    public native void glAttachShader(int program, int shader);

    public native void glBindBuffer(int target, int buffer);

    public native void glBindFramebuffer(int target, int buffer);

    public native void glBindRenderbuffer(int target, int buffer);

    public native void glFramebufferRenderbuffer(int target, int attachment, int component, int buffer);

    public native void glBindVertexArray(int array);

    public native void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha);

    public native void glBufferDataf(int target, int size, float[] data, int usage);

    public native void glBufferDatai(int target, int size, int[] data, int usage);

    public native void glDeleteBuffer(int index);

    public native void glDeleteFramebuffer(int index);

    public native void glDeleteRenderbuffer(int index);

    public native void glRenderbufferStorage(int target, int component, int w, int h);

    public native void glDeleteProgram(int program);

    public native void glCompileShader(int shader);

    public native int glCreateProgram();

    public native int glCreateShader(int shaderType);

    public native void glDeleteShader(int shader);

    public native void glDetachShader(int program, int shader);

    public native void glDisableVertexAttribArray(int index);

    public native void glEnableVertexAttribArray(int index);

    public native void glFramebufferTexture(int target, int attachment, int texture, int level);

    public native int glGenBuffer();

    public native int glGenFramebuffer();

    public native int glGenRenderbuffer();

    public native int glGenVertexArray();

    public native void glDeleteVertexArray(int array);

    public native int glGetUniformLocation(int program, String name);

    public native void glLinkProgram(int program);

    public native void glShaderSource(int shader, String sourceCode);

    public native void glTexStorage2D(int target, int level, int internalformat, int width, int height);

    public native void glUniform1f(int location, float v0);

    public native void glUniform1fv(int location, int count, float[] value);

    public native void glUniform1i(int location, int v0);

    public native void glUniform2fv(int location, int count, float[] value);

    public native void glUniform3f(int location, float v0, float v1, float v2);

    public native void glUniform4f(int location, float v0, float v1, float v2, float v3);

    public native void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value);

    public native void glUseProgram(int program);

    public native void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride,
            long pointer);

    // OpenGL fixed pipeline functions
    public native void glViewport(int x, int y, int width, int height);

    public native void glClearColor(float r, float g, float b, float a);

    public native void glClear(int mask);

    public native void glFinish();

    public native void glEnable(int cap);

    public native void glDisable(int cap);

    public native void glCullFace(int mode);

    public native void glBindTexture(int target, int texture);

    public native void glHint(int target, int mode);

    public native int glGetIntegerv(int pname);

    public native int glGenTexture();

    public native void glBlendFunc(int sfactor, int dfactor);

    public native void glTexImage2DEmpty(int target, int level, int internalformat, int width, int height, int border,
            int format, int type);

    public native void glTexImage2D(int target, int level, int internalformat, int width, int height, int border,
            int format, int type, byte[] data);

    public native void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
            int format, int type, byte[] pixels);

    public native void glTexParameteri(int target, int pname, int param);

    public native void glDrawArrays(int mode, int first, int count);

    public native void glDrawElements(int mode, int count, int type);

    public native void glDeleteTexture(int index);

    public native void glDrawBuffer(int mode);

    public native void glScissor(int x, int y, int width, int height);

    public native void glClearStencil(int s);

    public native void glStencilFunc(int func, int refnotkeword, int mask);

    public native void glStencilMask(int mask);

    public native void glStencilOp(int fail, int zfail, int zpass);

}