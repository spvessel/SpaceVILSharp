// openglapi.cpp : Defines the exported functions for the DLL application.

#include "openglapi.h"
#include <gl/GL.h>
#include "glext.h"

// OpenGL extension functions

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glActiveTexture(JNIEnv *env, jobject object, jint texture)
{
    glActiveTexture(texture);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glAttachShader(JNIEnv *env, jobject object, jint program, jint shader)
{
    glAttachShader(program, shader);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glBindBuffer(JNIEnv *env, jobject object, jint target, jint buffer)
{
    glBindBuffer(target, buffer);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glBindFramebuffer(JNIEnv *env, jobject object, jint target, jint buffer)
{
    glBindFramebufferEXT(target, buffer);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glBindRenderbuffer(JNIEnv *env, jobject object, jint target, jint buffer)
{
    glBindRenderbufferEXT(target, buffer);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glFramebufferRenderbuffer(JNIEnv *env, jobject object, jint target, jint attachment, jint component, jint buffer)
{
    glFramebufferRenderbufferEXT(target, attachment, component, buffer);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glBindVertexArray(JNIEnv *env, jobject object, jint array)
{
    glBindVertexArray(array);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glBlendFuncSeparate(JNIEnv *env, jobject object, jint srcRGB, jint dstRGB, jint srcAlpha, jint dstAlpha)
{
    glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glBufferDataf(JNIEnv *env, jobject object, jint target, jint size, jfloatArray data, jint usage)
{
    jsize lenght = env->GetArrayLength(data);
    jfloat *array = env->GetFloatArrayElements(data, 0);
    glBufferData(target, size, array, usage);
    delete[] array;
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glBufferDatai(JNIEnv *env, jobject object, jint target, jint size, jintArray data, jint usage)
{
    jsize lenght = env->GetArrayLength(data);
    jint *array = env->GetIntArrayElements(data, 0);
    glBufferData(target, size, array, usage);
    delete[] array;
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glDeleteBuffer(JNIEnv *env, jobject object, jint index)
{
    GLuint pointer = (GLuint)index;
    glDeleteBuffers(1, &pointer);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glDeleteFramebuffer(JNIEnv *env, jobject object, jint index)
{
    GLuint pointer = (GLuint)index;
    glDeleteFramebufferEXT(1, &pointer);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glDeleteRenderbuffer(JNIEnv *env, jobject object, jint index)
{
    glDeleteRenderbufferEXT(1, &pointer);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glRenderbufferStorage(JNIEnv *env, jobject object, jint target, jint component, jint w, jint h)
{
    glRenderbufferStorageEXT(target, component, w, h);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glDeleteProgram(JNIEnv *env, jobject object, jint program)
{
    glDeleteProgram(program);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glCompileShader(JNIEnv *env, jobject object, jint shader)
{
    glCompileShader(shader);
}

JNIEXPORT jint JNICALL Java_com_spacevil_openglapi_OpenGLApi_glCreateProgram(JNIEnv *env, jobject object)
{
    return (jint)glCreateProgram();
}

JNIEXPORT jint JNICALL Java_com_spacevil_openglapi_OpenGLApi_glCreateShader(JNIEnv *env, jobject object, jint shaderType)
{
    return glCreateShader(shaderType);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glDeleteShader(JNIEnv *env, jobject object, jint shader)
{
    glDeleteShader(shader);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glDetachShader(JNIEnv *env, jobject object, jint program, jint shader)
{
    glDetachShader(program, shader);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glDisableVertexAttribArray(JNIEnv *env, jobject object, jint index)
{
    glDisableVertexAttribArray(index);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glEnableVertexAttribArray(JNIEnv *env, jobject object, jint index)
{
    glEnableVertexAttribArray(index);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glFramebufferTexture(JNIEnv *env, jobject object, jint target, jint attachment, jint texture, jint level)
{
    glFramebufferTexture(target, attachment, texture, level);
}

JNIEXPORT jint JNICALL Java_com_spacevil_openglapi_OpenGLApi_glGenBuffer(JNIEnv *env, jobject object)
{
    GLuint buffer[1];
    glGenBuffers(1, buffer);
    return (jint)buffer[0];
}

JNIEXPORT jint JNICALL Java_com_spacevil_openglapi_OpenGLApi_glGenFramebuffer(JNIEnv *env, jobject object)
{
    GLuint buffer[1];
    glGenFramebufferEXT(1, buffer);
    return (jint)buffer[0];
}

JNIEXPORT jint JNICALL Java_com_spacevil_openglapi_OpenGLApi_glGenRenderbuffer(JNIEnv *env, jobject object)
{
    GLuint buffer[1];
    glGenRenderbufferEXT(1, buffer);
    return (jint)buffer[0];
}

JNIEXPORT jint JNICALL Java_com_spacevil_openglapi_OpenGLApi_glGenVertexArray(JNIEnv *env, jobject object)
{
    GLuint buffer[1];
    glGenVertexArray(1, buffer);
    return (jint)buffer[0];
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glDeleteVertexArray(JNIEnv *env, jobject object, jint array)
{
    GLuint pointer = (GLuint)array;
    glDeleteVertexArrays(1, &pointer);
}

JNIEXPORT jint JNICALL Java_com_spacevil_openglapi_OpenGLApi_glGetUniformLocation(JNIEnv *env, jobject object, jint program, jstring name)
{
    const char *argName = env->GetStringUTFChars(name, NULL);
    return glGetUniformLocation(program, argName);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glLinkProgram(JNIEnv *env, jobject object, jint program)
{
    glLinkProgram(program);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glShaderSource(JNIEnv *env, jobject object, jint shader, jstring sourceCode)
{
    const char *source = env->GetStringUTFChars(sourceCode, NULL);
    glShaderSource(shader, 1, &source, 0);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glTexStorage2D(JNIEnv *env, jobject object,
                                                                            jint target,
                                                                            jint level,
                                                                            jint internalformat,
                                                                            jint width,
                                                                            jint height)
{
    glTexStorage2D(target, level, internalformat, width, height);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glUniform1f(JNIEnv *env, jobject object, jint location, jfloat v0)
{
    glUniform1f(location, v0);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glUniform1fv(JNIEnv *env, jobject object, jint location, jint count, jfloatArray value)
{
    jsize lenght = env->GetArrayLength(value);
    jfloat *array = env->GetFloatArrayElements(value, 0);
    glUniform1fv(location, count, array);
    delete[] array;
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glUniform1i(JNIEnv *env, jobject object, jint location, jint v0)
{
    glUniform1i(location, v0);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glUniform2fv(JNIEnv *env, jobject object, jint location, jint count, jfloatArray value)
{
    jsize lenght = env->GetArrayLength(value);
    jfloat *array = env->GetFloatArrayElements(value, 0);
    glUniform2fv(location, count, array);
    delete[] array;
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glUniform3f(JNIEnv *env, jobject object, jint location, jfloat v0, jfloat v1, jfloat v2)
{
    glUniform3f(location, v0, v1, v2);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glUniform4f(JNIEnv *env, jobject object, jint location, jfloat v0, jfloat v1, jfloat v2, jfloat v3)
{
    glUniform4f(location, v0, v1, v2, v3);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glUniformMatrix4fv(JNIEnv *env, jobject object, jint location, jint count, jboolean transpose, jfloatArray value)
{
    jsize lenght = env->GetArrayLength(value);
    jfloat *array = env->GetFloatArrayElements(value, 0);
    glUniformMatrix4fv(location, count, transpose, array);
    delete[] array;
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glUseProgram(JNIEnv *env, jobject object, jint program)
{
    glUseProgram(program);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glVertexAttribPointer(JNIEnv *env, jobject object,
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

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glViewport(JNIEnv *env, jobject object, jint x, jint y, jint w, jint h)
{
    glViewport(x, y, w, h);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glClearColor(JNIEnv *env, jobject object, jfloat r, jfloat g, jfloat b, jfloat a)
{
    glClearColor(r, g, b, a);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glClear(JNIEnv *env, jobject object, jint mask)
{
    glClear(mask);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glFinish(JNIEnv *env, jobject object)
{
    glFinish();
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glEnable(JNIEnv *env, jobject object, jint cap)
{
    glEnable(cap);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glDisable(JNIEnv *env, jobject object, jint cap)
{
    glDisable(cap);
}
JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glCullFace(JNIEnv *env, jobject object, jint mode)
{
    glCullFace(mode);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glBindTexture(JNIEnv *env, jobject object, jint target, jint texture)
{
    glBindTexture(target, texture);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glHint(JNIEnv *env, jobject object, jint target, jint mode)
{
    glHint(target, mode);
}

JNIEXPORT jint JNICALL Java_com_spacevil_openglapi_OpenGLApi_glGetIntegerv(JNIEnv *env, jobject object, jint pname)
{
    GLint data;
    glGetIntegerv(pname, &data);
    return (jint)data;
}

JNIEXPORT jint JNICALL Java_com_spacevil_openglapi_OpenGLApi_glGenTexture(JNIEnv *env, jobject object)
{
    GLuint texture[1];
    glGenTextures(1, texture);
    return (jint)texture[0];
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glBlendFunc(JNIEnv *env, jobject object, jint sfactor, jint dfactor)
{
    glBlendFunc(sfactor, dfactor);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glTexImage2D(JNIEnv *env, jobject object,
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

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glTexSubImage2D(JNIEnv *env, jobject object,
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

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glTexParameteri(JNIEnv *env, jobject object, jint target, jint pname, jint param)
{
    glTexParameteri(target, pname, param);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glDrawArrays(JNIEnv *env, jobject object, jint mode, jint first, jint count)
{
    glDrawArrays(mode, first, count);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glDrawElements(JNIEnv *env, jobject object, jint mode, jint count, jint type)
{
    glDrawElements(mode, count, type, 0);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glDeleteTexture(JNIEnv *env, jobject object, jint index)
{
    GLuint pointer = (GLuint)index;
    glDeleteTextures(1, &pointer);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glDrawBuffer(JNIEnv *env, jobject object, jint mode)
{
    glDrawBuffer(mode);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glScissor(JNIEnv *env, jobject object, jint x, jint y, jint width, jint height)
{
    glScissor(x, y, width, height);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glClearStencil(JNIEnv *env, jobject object, jint s)
{
    glClearStencil(s);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glStencilFunc(JNIEnv *env, jobject object, jint func, jint refnotkeword, jint mask)
{
    glStencilFunc(func, refnotkeword, mask);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glStencilMask(JNIEnv *env, jobject object, jint mask)
{
    glStencilMask(mask);
}

JNIEXPORT void JNICALL Java_com_spacevil_openglapi_OpenGLApi_glStencilOp(JNIEnv *env, jobject object, jint fail, jint zfail, jint zpass)
{
    glStencilOp(fail, zfail, zpass);
}
