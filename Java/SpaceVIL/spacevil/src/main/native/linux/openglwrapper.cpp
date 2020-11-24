// openglapi.cpp : Defines the exported functions for the DLL application.

#include "openglwrapper.h"
#include <stdio.h>
#include <GL/glx.h>
#include <GL/gl.h>
#include "glext.h"

// OpenGL extension functions

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_ActiveTexture(JNIEnv *env, jobject object, jint texture)
{
    glActiveTexture(texture);
}

PFNGLATTACHSHADERPROC glAttachShader = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_AttachShader(JNIEnv *env, jobject object, jint program, jint shader)
{
    if (glAttachShader == NULL)
    {
        glAttachShader = (PFNGLATTACHSHADERPROC)glXGetProcAddress((const unsigned char*)"glAttachShader");
        if (glAttachShader == NULL)
            return;
    }
    glAttachShader(program, shader);
}

PFNGLBINDBUFFERPROC glBindBuffer = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BindBuffer(JNIEnv *env, jobject object, jint target, jint buffer)
{
    if (glBindBuffer == NULL)
    {
        glBindBuffer = (PFNGLBINDBUFFERPROC)glXGetProcAddress((const unsigned char*)"glBindBuffer");
        if (glBindBuffer == NULL)
            return;
    }

    glBindBuffer(target, buffer);
}

PFNGLBINDFRAMEBUFFEREXTPROC glBindFramebuffer = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BindFramebuffer(JNIEnv *env, jobject object, jint target, jint buffer)
{
    if (glBindFramebuffer == NULL)
    {
        glBindFramebuffer = (PFNGLBINDFRAMEBUFFEREXTPROC)glXGetProcAddress((const unsigned char*)"glBindFramebufferEXT");
        if (glBindFramebuffer == NULL)
            return;
    }

    glBindFramebuffer(target, buffer);
}

PFNGLBINDRENDERBUFFEREXTPROC glBindRenderbuffer = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BindRenderbuffer(JNIEnv *env, jobject object, jint target, jint buffer)
{
    if (glBindRenderbuffer == NULL)
    {
        glBindRenderbuffer = (PFNGLBINDRENDERBUFFEREXTPROC)glXGetProcAddress((const unsigned char*)"glBindRenderbufferEXT");
        if (glBindRenderbuffer == NULL)
            return;
    }

    glBindRenderbuffer(target, buffer);
}

PFNGLFRAMEBUFFERRENDERBUFFEREXTPROC glFramebufferRenderbuffer = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_FramebufferRenderbuffer(JNIEnv *env, jobject object, jint target, jint attachment, jint component, jint buffer)
{
    if (glFramebufferRenderbuffer == NULL)
    {
        glFramebufferRenderbuffer = (PFNGLFRAMEBUFFERRENDERBUFFEREXTPROC)glXGetProcAddress((const unsigned char*)"glFramebufferRenderbufferEXT");
        if (glFramebufferRenderbuffer == NULL)
            return;
    }

    glFramebufferRenderbuffer(target, attachment, component, buffer);
}

PFNGLBINDVERTEXARRAYPROC glBindVertexArray = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BindVertexArray(JNIEnv *env, jobject object, jint array)
{
    if (glBindVertexArray == NULL)
    {
        glBindVertexArray = (PFNGLBINDVERTEXARRAYPROC)glXGetProcAddress((const unsigned char*)"glBindVertexArray");
        if (glBindVertexArray == NULL)
            return;
    }

    glBindVertexArray(array);
}

PFNGLBLENDFUNCSEPARATEPROC glBlendFuncSeparate = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BlendFuncSeparate(JNIEnv *env, jobject object, jint srcRGB, jint dstRGB, jint srcAlpha, jint dstAlpha)
{
    if (glBlendFuncSeparate == NULL)
    {
        glBlendFuncSeparate = (PFNGLBLENDFUNCSEPARATEPROC)glXGetProcAddress((const unsigned char*)"glBlendFuncSeparate");
        if (glBlendFuncSeparate == NULL)
            return;
    }

    glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
}

PFNGLBUFFERDATAPROC glBufferData = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BufferDataf(JNIEnv *env, jobject object, jint target, jint size, jfloatArray data, jint usage)
{
    if (glBufferData == NULL)
    {
        glBufferData = (PFNGLBUFFERDATAPROC)glXGetProcAddress((const unsigned char*)"glBufferData");
        if (glBufferData == NULL)
            return;
    }

    jsize lenght = env->GetArrayLength(data);
    jfloat *array = env->GetFloatArrayElements(data, 0);
    glBufferData(target, size, array, usage);
    delete[] array;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BufferDatai(JNIEnv *env, jobject object, jint target, jint size, jintArray data, jint usage)
{
    if (glBufferData == NULL)
    {
        glBufferData = (PFNGLBUFFERDATAPROC)glXGetProcAddress((const unsigned char*)"glBufferData");
        if (glBufferData == NULL)
            return;
    }

    jsize lenght = env->GetArrayLength(data);
    jint *array = env->GetIntArrayElements(data, 0);
    glBufferData(target, size, array, usage);
    delete[] array;
}

PFNGLDELETEBUFFERSPROC glDeleteBuffers = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DeleteBuffer(JNIEnv *env, jobject object, jint index)
{
    if (glDeleteBuffers == NULL)
    {
        glDeleteBuffers = (PFNGLDELETEBUFFERSPROC)glXGetProcAddress((const unsigned char*)"glDeleteBuffers");
        if (glDeleteBuffers == NULL)
            return;
    }
    GLuint pointer = (GLuint)index;
    glDeleteBuffers(1, &pointer);
}

PFNGLDELETEFRAMEBUFFERSEXTPROC glDeleteFramebuffer = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DeleteFramebuffer(JNIEnv *env, jobject object, jint index)
{
    if (glDeleteFramebuffer == NULL)
    {
        glDeleteFramebuffer = (PFNGLDELETEFRAMEBUFFERSEXTPROC)glXGetProcAddress((const unsigned char*)"glDeleteFramebuffersEXT");
        if (glDeleteFramebuffer == NULL)
            return;
    }
    GLuint pointer = (GLuint)index;
    glDeleteFramebuffer(1, &pointer);
}

PFNGLDELETERENDERBUFFERSEXTPROC glDeleteRenderbuffer = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DeleteRenderbuffer(JNIEnv *env, jobject object, jint index)
{
    if (glDeleteRenderbuffer == NULL)
    {
        glDeleteRenderbuffer = (PFNGLDELETERENDERBUFFERSEXTPROC)glXGetProcAddress((const unsigned char*)"glDeleteRenderbuffersEXT");
        if (glDeleteRenderbuffer == NULL)
            return;
    }
    GLuint pointer = (GLuint)index;
    glDeleteRenderbuffer(1, &pointer);
}

PFNGLRENDERBUFFERSTORAGEEXTPROC glRenderbufferStorage = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_RenderbufferStorage(JNIEnv *env, jobject object, jint target, jint component, jint w, jint h)
{
    if (glRenderbufferStorage == NULL)
    {
        glRenderbufferStorage = (PFNGLRENDERBUFFERSTORAGEEXTPROC)glXGetProcAddress((const unsigned char*)"glRenderbufferStorageEXT");
        if (glRenderbufferStorage == NULL)
            return;
    }

    glRenderbufferStorage(target, component, w, h);
}

PFNGLDELETEPROGRAMPROC glDeleteProgram = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DeleteProgram(JNIEnv *env, jobject object, jint program)
{
    if (glDeleteProgram == NULL)
    {
        glDeleteProgram = (PFNGLDELETEPROGRAMPROC)glXGetProcAddress((const unsigned char*)"glDeleteProgram");
        if (glDeleteProgram == NULL)
            return;
    }
    glDeleteProgram(program);
}

PFNGLCOMPILESHADERPROC glCompileShader = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_CompileShader(JNIEnv *env, jobject object, jint shader)
{
    if (glCompileShader == NULL)
    {
        glCompileShader = (PFNGLCOMPILESHADERPROC)glXGetProcAddress((const unsigned char*)"glCompileShader");
        if (glCompileShader == NULL)
            return;
    }
    glCompileShader(shader);
}

PFNGLCREATEPROGRAMPROC glCreateProgram = NULL;
JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_CreateProgram(JNIEnv *env, jobject object)
{
    if (glCreateProgram == NULL)
    {
        glCreateProgram = (PFNGLCREATEPROGRAMPROC)glXGetProcAddress((const unsigned char*)"glCreateProgram");
        if (glCreateProgram == NULL)
            return -1;
    }
    return (jint)glCreateProgram();
}

PFNGLCREATESHADERPROC glCreateShader = NULL;
JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_CreateShader(JNIEnv *env, jobject object, jint shaderType)
{
    if (glCreateShader == NULL)
    {
        glCreateShader = (PFNGLCREATESHADERPROC)glXGetProcAddress((const unsigned char*)"glCreateShader");
        if (glCreateShader == NULL)
            return -1;
    }
    return glCreateShader(shaderType);
}

PFNGLDELETESHADERPROC glDeleteShader = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DeleteShader(JNIEnv *env, jobject object, jint shader)
{
    if (glDeleteShader == NULL)
    {
        glDeleteShader = (PFNGLDELETESHADERPROC)glXGetProcAddress((const unsigned char*)"glDeleteShader");
        if (glDeleteShader == NULL)
            return;
    }
    glDeleteShader(shader);
}

PFNGLDETACHSHADERPROC glDetachShader = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DetachShader(JNIEnv *env, jobject object, jint program, jint shader)
{
    if (glDetachShader == NULL)
    {
        glDetachShader = (PFNGLDETACHSHADERPROC)glXGetProcAddress((const unsigned char*)"glDetachShader");
        if (glDetachShader == NULL)
            return;
    }
    glDetachShader(program, shader);
}

PFNGLDISABLEVERTEXATTRIBARRAYPROC glDisableVertexAttribArray = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DisableVertexAttribArray(JNIEnv *env, jobject object, jint index)
{
    if (glDisableVertexAttribArray == NULL)
    {
        glDisableVertexAttribArray = (PFNGLDISABLEVERTEXATTRIBARRAYPROC)glXGetProcAddress((const unsigned char*)"glDisableVertexAttribArray");
        if (glDisableVertexAttribArray == NULL)
            return;
    }

    glDisableVertexAttribArray(index);
}

PFNGLENABLEVERTEXATTRIBARRAYPROC glEnableVertexAttribArray = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_EnableVertexAttribArray(JNIEnv *env, jobject object, jint index)
{
    if (glEnableVertexAttribArray == NULL)
    {
        glEnableVertexAttribArray = (PFNGLENABLEVERTEXATTRIBARRAYPROC)glXGetProcAddress((const unsigned char*)"glEnableVertexAttribArray");
        if (glEnableVertexAttribArray == NULL)
            return;
    }

    glEnableVertexAttribArray(index);
}

PFNGLFRAMEBUFFERTEXTUREEXTPROC glFramebufferTexture = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_FramebufferTexture(JNIEnv *env, jobject object, jint target, jint attachment, jint texture, jint level)
{
    if (glFramebufferTexture == NULL)
    {
        glFramebufferTexture = (PFNGLFRAMEBUFFERTEXTUREEXTPROC)glXGetProcAddress((const unsigned char*)"glFramebufferTextureEXT");
        if (glFramebufferTexture == NULL)
            return;
    }

    glFramebufferTexture(target, attachment, texture, level);
}

PFNGLGENBUFFERSPROC glGenBuffers = NULL;
JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_GenBuffer(JNIEnv *env, jobject object)
{
    if (glGenBuffers == NULL)
    {
        glGenBuffers = (PFNGLGENBUFFERSPROC)glXGetProcAddress((const unsigned char*)"glGenBuffers");
        if (glGenBuffers == NULL)
            return -1;
    }

    GLuint buffer[1];
    glGenBuffers(1, buffer);
    return (jint)buffer[0];
}

PFNGLGENFRAMEBUFFERSEXTPROC glGenFramebuffer = NULL;
JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_GenFramebuffer(JNIEnv *env, jobject object)
{
    if (glGenFramebuffer == NULL)
    {
        glGenFramebuffer = (PFNGLGENFRAMEBUFFERSEXTPROC)glXGetProcAddress((const unsigned char*)"glGenFramebuffersEXT");
        if (glGenFramebuffer == NULL)
            return -1;
    }

    GLuint buffer[1];
    glGenFramebuffer(1, buffer);
    return (jint)buffer[0];
}

PFNGLGENRENDERBUFFERSEXTPROC glGenRenderbuffer = NULL;
JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_GenRenderbuffer(JNIEnv *env, jobject object)
{
    if (glGenRenderbuffer == NULL)
    {
        glGenRenderbuffer = (PFNGLGENRENDERBUFFERSEXTPROC)glXGetProcAddress((const unsigned char*)"glGenRenderbuffersEXT");
        if (glGenRenderbuffer == NULL)
            return -1;
    }

    GLuint buffer[1];
    glGenRenderbuffer(1, buffer);
    return (jint)buffer[0];
}

PFNGLGENVERTEXARRAYSPROC glGenVertexArray = NULL;
JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_GenVertexArray(JNIEnv *env, jobject object)
{
    if (glGenVertexArray == NULL)
    {
        glGenVertexArray = (PFNGLGENVERTEXARRAYSPROC)glXGetProcAddress((const unsigned char*)"glGenVertexArrays");
        if (glGenVertexArray == NULL)
            return -1;
    }

    GLuint buffer[1];
    glGenVertexArray(1, buffer);
    return (jint)buffer[0];
}

PFNGLDELETEVERTEXARRAYSPROC glDeleteVertexArray = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DeleteVertexArray(JNIEnv *env, jobject object, jint array)
{
    if (glDeleteVertexArray == NULL)
    {
        glDeleteVertexArray = (PFNGLDELETEVERTEXARRAYSPROC)glXGetProcAddress((const unsigned char*)"glDeleteVertexArrays");
        if (glDeleteVertexArray == NULL)
            return;
    }

    GLuint pointer = (GLuint)array;
    glDeleteVertexArray(1, &pointer);
}

PFNGLGETUNIFORMLOCATIONPROC glGetUniformLocation = NULL;
JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_GetUniformLocation(JNIEnv *env, jobject object, jint program, jstring name)
{
    if (glGetUniformLocation == NULL)
    {
        glGetUniformLocation = (PFNGLGETUNIFORMLOCATIONPROC)glXGetProcAddress((const unsigned char*)"glGetUniformLocation");
        if (glGetUniformLocation == NULL)
            return -1;
    }
    const char *argName = env->GetStringUTFChars(name, NULL);
    return glGetUniformLocation(program, argName);
}

PFNGLLINKPROGRAMPROC glLinkProgram = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_LinkProgram(JNIEnv *env, jobject object, jint program)
{
    if (glLinkProgram == NULL)
    {
        glLinkProgram = (PFNGLLINKPROGRAMPROC)glXGetProcAddress((const unsigned char*)"glLinkProgram");
        if (glLinkProgram == NULL)
            return;
    }
    glLinkProgram(program);
}

PFNGLSHADERSOURCEPROC glShaderSource = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_ShaderSource(JNIEnv *env, jobject object, jint shader, jstring sourceCode)
{
    if (glShaderSource == NULL)
    {
        glShaderSource = (PFNGLSHADERSOURCEPROC)glXGetProcAddress((const unsigned char*)"glShaderSource");
        if (glShaderSource == NULL)
            return;
    }
    const char *source = env->GetStringUTFChars(sourceCode, NULL);
    glShaderSource(shader, 1, &source, 0);
}

PFNGLTEXSTORAGE2DPROC glTexStorage2D = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_TexStorage2D(JNIEnv *env, jobject object,
                                                                            jint target,
                                                                            jint level,
                                                                            jint internalformat,
                                                                            jint width,
                                                                            jint height)
{
    if (glTexStorage2D == NULL)
    {
        glTexStorage2D = (PFNGLTEXSTORAGE2DPROC)glXGetProcAddress((const unsigned char*)"glTexStorage2D");
        if (glTexStorage2D == NULL)
            return;
    }

    glTexStorage2D(target, level, internalformat, width, height);
}

PFNGLUNIFORM1FPROC glUniform1f = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Uniform1f(JNIEnv *env, jobject object, jint location, jfloat v0)
{
    if (glUniform1f == NULL)
    {
        glUniform1f = (PFNGLUNIFORM1FPROC)glXGetProcAddress((const unsigned char*)"glUniform1f");
        if (glUniform1f == NULL)
            return;
    }
    glUniform1f(location, v0);
}

PFNGLUNIFORM1FVPROC glUniform1fv = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Uniform1fv(JNIEnv *env, jobject object, jint location, jint count, jfloatArray value)
{
    if (glUniform1fv == NULL)
    {
        glUniform1fv = (PFNGLUNIFORM1FVPROC)glXGetProcAddress((const unsigned char*)"glUniform1fv");
        if (glUniform1fv == NULL) {
            fprintf(stderr, "glUniform1fv\n");
            return;
        }
    }
    float *array = (float *)env->GetFloatArrayElements(value, 0);
    glUniform1fv(location, count, (const GLfloat*) array);
    delete[] array;
}

PFNGLUNIFORM1IPROC glUniform1i = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Uniform1i(JNIEnv *env, jobject object, jint location, jint v0)
{
    if (glUniform1i == NULL)
    {
        glUniform1i = (PFNGLUNIFORM1IPROC)glXGetProcAddress((const unsigned char*)"glUniform1i");
        if (glUniform1i == NULL)
        {
            return;
        }
    }
    glUniform1i(location, v0);
}

PFNGLUNIFORM2FVPROC glUniform2fv = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Uniform2fv(JNIEnv *env, jobject object, jint location, jint count, jfloatArray value)
{
    if (glUniform2fv == NULL)
    {
        glUniform2fv = (PFNGLUNIFORM2FVPROC)glXGetProcAddress((const unsigned char*)"glUniform2fv");
        if (glUniform2fv == NULL)
        {
            fprintf(stderr, "glUniform2fv\n");
            return;
        }
    }
    float *array = (float *)env->GetFloatArrayElements(value, 0);
    glUniform2fv(location, count, (const GLfloat*) array);
    delete[] array;
}

PFNGLUNIFORM3FPROC glUniform3f = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Uniform3f(JNIEnv *env, jobject object, jint location, jfloat v0, jfloat v1, jfloat v2)
{
    if (glUniform3f == NULL)
    {
        glUniform3f = (PFNGLUNIFORM3FPROC)glXGetProcAddress((const unsigned char*)"glUniform3f");
        if (glUniform3f == NULL)
            return;
    }
    glUniform3f(location, v0, v1, v2);
}

PFNGLUNIFORM4FPROC glUniform4f = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Uniform4f(JNIEnv *env, jobject object, jint location, jfloat v0, jfloat v1, jfloat v2, jfloat v3)
{
    if (glUniform4f == NULL)
    {
        glUniform4f = (PFNGLUNIFORM4FPROC)glXGetProcAddress((const unsigned char*)"glUniform4f");
        if (glUniform4f == NULL)
            return;
    }
    glUniform4f(location, v0, v1, v2, v3);
}

PFNGLUNIFORMMATRIX4FVPROC glUniformMatrix4fv = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_UniformMatrix4fv(JNIEnv *env, jobject object, jint location, jint count, jboolean transpose, jfloatArray value)
{
    if (glUniformMatrix4fv == NULL)
    {
        glUniformMatrix4fv = (PFNGLUNIFORMMATRIX4FVPROC)glXGetProcAddress((const unsigned char*)"glUniformMatrix4fv");
        if (glUniformMatrix4fv == NULL)
            return;
    }
    jsize lenght = env->GetArrayLength(value);
    jfloat *array = env->GetFloatArrayElements(value, 0);
    glUniformMatrix4fv(location, count, transpose, array);
    delete[] array;
}

PFNGLUSEPROGRAMPROC glUseProgram = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_UseProgram(JNIEnv *env, jobject object, jint program)
{
    if (glUseProgram == NULL)
    {
        glUseProgram = (PFNGLUSEPROGRAMPROC)glXGetProcAddress((const unsigned char*)"glUseProgram");
        if (glUseProgram == NULL)
            return;
    }
    glUseProgram(program);
}

PFNGLVERTEXATTRIBPOINTERPROC glVertexAttribPointer = NULL;
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_VertexAttribPointer(JNIEnv *env, jobject object,
                                                                                   jint index,
                                                                                   jint size,
                                                                                   jint type,
                                                                                   jboolean normalized,
                                                                                   jint stride,
                                                                                   jlong pointer)
{
    if (glVertexAttribPointer == NULL)
    {
        glVertexAttribPointer = (PFNGLVERTEXATTRIBPOINTERPROC)glXGetProcAddress((const unsigned char*)"glVertexAttribPointer");
        if (glVertexAttribPointer == NULL)
            return;
    }
    glVertexAttribPointer(index, size, type, normalized, stride, (const GLvoid *)pointer);
}

// OpenGL fixed pipeline functions

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Viewport(JNIEnv *env, jobject object, jint x, jint y, jint w, jint h)
{
    glViewport(x, y, w, h);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_ClearColor(JNIEnv *env, jobject object, jfloat r, jfloat g, jfloat b, jfloat a)
{
    glClearColor(r, g, b, a);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Clear(JNIEnv *env, jobject object, jint mask)
{
    glClear(mask);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Finish(JNIEnv *env, jobject object)
{
    glFinish();
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Enable(JNIEnv *env, jobject object, jint cap)
{
    glEnable(cap);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Disable(JNIEnv *env, jobject object, jint cap)
{
    glDisable(cap);
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_CullFace(JNIEnv *env, jobject object, jint mode)
{
    glCullFace(mode);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BindTexture(JNIEnv *env, jobject object, jint target, jint texture)
{
    glBindTexture(target, texture);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Hint(JNIEnv *env, jobject object, jint target, jint mode)
{
    glHint(target, mode);
}

JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_GetIntegerv(JNIEnv *env, jobject object, jint pname)
{
    GLint data;
    glGetIntegerv(pname, &data);
    return (jint)data;
}

JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_GenTexture(JNIEnv *env, jobject object)
{
    GLuint texture[1];
    glGenTextures(1, texture);
    return (jint)texture[0];
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BlendFunc(JNIEnv *env, jobject object, jint sfactor, jint dfactor)
{
    glBlendFunc(sfactor, dfactor);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_TexImage2DEmpty(JNIEnv *env, jobject object,
                                                                                                 jint target,
                                                                                                 jint level,
                                                                                                 jint internalformat,
                                                                                                 jint width,
                                                                                                 jint height,
                                                                                                 jint border,
                                                                                                 jint format,
                                                                                                 jint type)
{
    glTexImage2D(target, level, internalformat, width, height, border, format, type, NULL);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_TexImage2D(JNIEnv *env, jobject object,
                                                                                            jint target,
                                                                                            jint level,
                                                                                            jint internalformat,
                                                                                            jint width,
                                                                                            jint height,
                                                                                            jint border,
                                                                                            jint format,
                                                                                            jint type,
                                                                                            jbyteArray data)
{
    unsigned char *textureData = (unsigned char *)env->GetByteArrayElements(data, 0);
    glTexImage2D(target, level, internalformat, width, height, border, format, type, textureData);
    delete[] textureData;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_TexSubImage2D(JNIEnv *env, jobject object,
                                                                                               jint target,
                                                                                               jint level,
                                                                                               jint xoffset,
                                                                                               jint yoffset,
                                                                                               jint width,
                                                                                               jint height,
                                                                                               jint format,
                                                                                               jint type,
                                                                                               jbyteArray pixels)
{
    unsigned char *textureData = (unsigned char *)env->GetByteArrayElements(pixels, 0);
    glTexSubImage2D(target, level, xoffset, xoffset, width, height, format, type, textureData);
    delete[] textureData;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_TexParameteri(JNIEnv *env, jobject object, jint target, jint pname, jint param)
{
    glTexParameteri(target, pname, param);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DrawArrays(JNIEnv *env, jobject object, jint mode, jint first, jint count)
{
    glDrawArrays(mode, first, count);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DrawElements(JNIEnv *env, jobject object, jint mode, jint count, jint type)
{
    glDrawElements(mode, count, type, 0);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DeleteTexture(JNIEnv *env, jobject object, jint index)
{
    GLuint pointer = (GLuint)index;
    glDeleteTextures(1, &pointer);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DrawBuffer(JNIEnv *env, jobject object, jint mode)
{
    glDrawBuffer(mode);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Scissor(JNIEnv *env, jobject object, jint x, jint y, jint width, jint height)
{
    glScissor(x, y, width, height);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_ClearStencil(JNIEnv *env, jobject object, jint s)
{
    glClearStencil(s);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_StencilFunc(JNIEnv *env, jobject object, jint func, jint refnotkeword, jint mask)
{
    glStencilFunc(func, refnotkeword, mask);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_StencilMask(JNIEnv *env, jobject object, jint mask)
{
    glStencilMask(mask);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_StencilOp(JNIEnv *env, jobject object, jint fail, jint zfail, jint zpass)
{
    glStencilOp(fail, zfail, zpass);
}
