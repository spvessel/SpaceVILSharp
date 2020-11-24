// openglapi.cpp : Defines the exported functions for the DLL application.
#define GL_SILENCE_DEPRECATION

#include "openglwrapper.h"
#include <OpenGL/gl3.h>
#include <OpenGL/gl3ext.h>
#include <stdio.h>

// OpenGL extension functions

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_ActiveTexture(JNIEnv *env, jobject object, jint texture)
{
    glActiveTexture(texture);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_AttachShader(JNIEnv *env, jobject object, jint program, jint shader)
{
    glAttachShader(program, shader);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BindBuffer(JNIEnv *env, jobject object, jint target, jint buffer)
{
    glBindBuffer(target, buffer);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BindFramebuffer(JNIEnv *env, jobject object, jint target, jint buffer)
{
    glBindFramebuffer(target, buffer);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BindRenderbuffer(JNIEnv *env, jobject object, jint target, jint buffer)
{
    glBindRenderbuffer(target, buffer);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_FramebufferRenderbuffer(JNIEnv *env, jobject object, jint target, jint attachment, jint component, jint buffer)
{
    glFramebufferRenderbuffer(target, attachment, component, buffer);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BindVertexArray(JNIEnv *env, jobject object, jint array)
{
    glBindVertexArray(array);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BlendFuncSeparate(JNIEnv *env, jobject object, jint srcRGB, jint dstRGB, jint srcAlpha, jint dstAlpha)
{
    glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BufferDataf(JNIEnv *env, jobject object, jint target, jint size, jfloatArray data, jint usage)
{
    jsize lenght = env->GetArrayLength(data);
    jfloat *array = env->GetFloatArrayElements(data, 0);
    glBufferData(target, size, array, usage);
    delete[] array;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_BufferDatai(JNIEnv *env, jobject object, jint target, jint size, jintArray data, jint usage)
{
    jsize lenght = env->GetArrayLength(data);
    jint *array = env->GetIntArrayElements(data, 0);
    glBufferData(target, size, array, usage);
    delete[] array;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DeleteBuffer(JNIEnv *env, jobject object, jint index)
{
    GLuint pointer = (GLuint)index;
    glDeleteBuffers(1, &pointer);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DeleteFramebuffer(JNIEnv *env, jobject object, jint index)
{
    GLuint pointer = (GLuint)index;
    glDeleteFramebuffers(1, &pointer);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DeleteRenderbuffer(JNIEnv *env, jobject object, jint index)
{
    GLuint pointer = (GLuint)index;
    glDeleteRenderbuffers(1, &pointer);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_RenderbufferStorage(JNIEnv *env, jobject object, jint target, jint component, jint w, jint h)
{
    glRenderbufferStorage(target, component, w, h);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DeleteProgram(JNIEnv *env, jobject object, jint program)
{
    glDeleteProgram(program);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_CompileShader(JNIEnv *env, jobject object, jint shader)
{
    glCompileShader(shader);
}

JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_CreateProgram(JNIEnv *env, jobject object)
{
    return (jint)glCreateProgram();
}

JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_CreateShader(JNIEnv *env, jobject object, jint shaderType)
{
    return glCreateShader(shaderType);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DeleteShader(JNIEnv *env, jobject object, jint shader)
{
    glDeleteShader(shader);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DetachShader(JNIEnv *env, jobject object, jint program, jint shader)
{
    glDetachShader(program, shader);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DisableVertexAttribArray(JNIEnv *env, jobject object, jint index)
{
    glDisableVertexAttribArray(index);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_EnableVertexAttribArray(JNIEnv *env, jobject object, jint index)
{
    glEnableVertexAttribArray(index);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_FramebufferTexture(JNIEnv *env, jobject object, jint target, jint attachment, jint texture, jint level)
{
    glFramebufferTexture(target, attachment, texture, level);
}

JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_GenBuffer(JNIEnv *env, jobject object)
{
    GLuint buffer[1];
    glGenBuffers(1, buffer);
    return (jint)buffer[0];
}

JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_GenFramebuffer(JNIEnv *env, jobject object)
{
    GLuint buffer[1];
    glGenFramebuffers(1, buffer);
    return (jint)buffer[0];
}

JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_GenRenderbuffer(JNIEnv *env, jobject object)
{
    GLuint buffer[1];
    glGenRenderbuffers(1, buffer);
    return (jint)buffer[0];
}

JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_GenVertexArray(JNIEnv *env, jobject object)
{
    GLuint buffer[1];
    glGenVertexArrays(1, buffer);
    return (jint)buffer[0];
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_DeleteVertexArray(JNIEnv *env, jobject object, jint array)
{
    GLuint pointer = (GLuint)array;
    glDeleteVertexArrays(1, &pointer);
}

JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_GetUniformLocation(JNIEnv *env, jobject object, jint program, jstring name)
{
    const char *argName = env->GetStringUTFChars(name, NULL);
    return glGetUniformLocation(program, argName);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_LinkProgram(JNIEnv *env, jobject object, jint program)
{
    glLinkProgram(program);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_ShaderSource(JNIEnv *env, jobject object, jint shader, jstring sourceCode)
{
    const char *source = env->GetStringUTFChars(sourceCode, NULL);
    glShaderSource(shader, 1, &source, 0);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_TexStorage2D(JNIEnv *env, jobject object,
                                                                                              jint target,
                                                                                              jint level,
                                                                                              jint internalformat,
                                                                                              jint width,
                                                                                              jint height)
{
    glTexStorage2D(target, level, internalformat, width, height);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Uniform1f(JNIEnv *env, jobject object, jint location, jfloat v0)
{
    glUniform1f(location, v0);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Uniform1fv(JNIEnv *env, jobject object, jint location, jint count, jfloatArray value)
{
    jsize lenght = env->GetArrayLength(value);
    jfloat *array = env->GetFloatArrayElements(value, 0);
    glUniform1fv(location, count, array);
    delete[] array;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Uniform1i(JNIEnv *env, jobject object, jint location, jint v0)
{
    glUniform1i(location, v0);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Uniform2fv(JNIEnv *env, jobject object, jint location, jint count, jfloatArray value)
{
    jsize lenght = env->GetArrayLength(value);
    jfloat *array = env->GetFloatArrayElements(value, 0);
    glUniform2fv(location, count, array);
    delete[] array;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Uniform3f(JNIEnv *env, jobject object, jint location, jfloat v0, jfloat v1, jfloat v2)
{
    glUniform3f(location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_Uniform4f(JNIEnv *env, jobject object, jint location, jfloat v0, jfloat v1, jfloat v2, jfloat v3)
{
    glUniform4f(location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_UniformMatrix4fv(JNIEnv *env, jobject object, jint location, jint count, jboolean transpose, jfloatArray value)
{
    jsize lenght = env->GetArrayLength(value);
    jfloat *array = env->GetFloatArrayElements(value, 0);
    glUniformMatrix4fv(location, count, transpose, array);
    delete[] array;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_UseProgram(JNIEnv *env, jobject object, jint program)
{
    glUseProgram(program);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper_VertexAttribPointer(JNIEnv *env, jobject object,
                                                                                                     jint index,
                                                                                                     jint size,
                                                                                                     jint type,
                                                                                                     jboolean normalized,
                                                                                                     jint stride,
                                                                                                     jlong pointer)
{
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
