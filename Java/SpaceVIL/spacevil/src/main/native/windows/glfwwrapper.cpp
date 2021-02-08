// glfwapi.cpp : Defines the exported functions for the DLL application.
#include "common.h"
#include "glfwwrapper.h"
#include <glfw3.h>
#include <glfw3native.h>
#include <map>
#include <vector>

JavaVM *jvm = NULL;

struct Callbacks
{
    jobject charMods = NULL;
    jobject cursorPos = NULL;
    jobject drop = NULL;
    jobject framebufferSize = NULL;
    jobject key = NULL;
    jobject mouseButton = NULL;
    jobject scroll = NULL;
    jobject windowClose = NULL;
    jobject windowContentScale = NULL;
    jobject windowFocus = NULL;
    jobject windowIconify = NULL;
    jobject windowPos = NULL;
    jobject windowRefresh = NULL;
    jobject windowSize = NULL;
};

std::map<GLFWwindow *, Callbacks> *callbacks = new std::map<GLFWwindow *, Callbacks>();

JNIEXPORT jint JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_Init(JNIEnv *env, jobject sender)
{
    return glfwInit();
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_Terminate(JNIEnv *env, jobject sender)
{
    glfwTerminate();
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SwapInterval(JNIEnv *env, jobject sender, jint interval)
{
    glfwSwapInterval(interval);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SwapBuffers(JNIEnv *env, jobject sender, jlong window)
{
    glfwSwapBuffers((GLFWwindow *)window);
}

JNIEXPORT jstring JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_GetClipboardString(JNIEnv *env, jobject sender, jlong window)
{
    const char *content = glfwGetClipboardString((GLFWwindow *)window);
    jstring result = env->NewStringUTF(content);
    delete content;
    return result;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetClipboardString(JNIEnv *env, jobject sender, jlong window, jstring string)
{
    const char *content = env->GetStringUTFChars(string, 0);
    glfwSetClipboardString((GLFWwindow *)window, content);
    env->ReleaseStringUTFChars(string, content);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_WaitEventsTimeout(JNIEnv *env, jobject sender, jdouble timeout)
{
    glfwWaitEventsTimeout(timeout);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_WaitEvents(JNIEnv *env, jobject sender)
{
    glfwWaitEvents();
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_PollEvents(JNIEnv *env, jobject sender)
{
    glfwPollEvents();
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetInputMode(JNIEnv *env, jobject sender, jlong window, jint mode, jint value)
{
    glfwSetInputMode((GLFWwindow *)window, mode, value);
}

//// CursorPosFunc(GLFWwindow *window, double xpos, double ypos)
void CursorPosCallback(GLFWwindow *window, double xpos, double ypos)
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).cursorPos);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(JDD)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWCursorPosCallback.invoke() not found!\n");
            return;
        }
        env->CallVoidMethod(callbacks->at(window).cursorPos, method, window, xpos, ypos);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetCursorPosCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).cursorPos = callbackRef;
    glfwSetCursorPosCallback((GLFWwindow *)window, CursorPosCallback);
}

//// GLFWMouseButtonCallback(GLFWwindow *window, int button, int action, int mods)
void MouseButtonCallback(GLFWwindow *window, int button, int action, int mods)
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).mouseButton);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(JIII)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWMouseButtonCallback.invoke() not found!\n");
            return;
        }
        env->CallVoidMethod(callbacks->at(window).mouseButton, method, window, button, action, mods);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetMouseButtonCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).mouseButton = callbackRef;
    glfwSetMouseButtonCallback((GLFWwindow *)window, MouseButtonCallback);
}

//// GLFWScrollCallback(GLFWwindow *window, double dx, double dy)
void ScrollCallback(GLFWwindow *window, double dx, double dy)
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).scroll);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(JDD)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWScrollCallback.invoke() not found!\n");
            return;
        }
        env->CallVoidMethod(callbacks->at(window).scroll, method, window, dx, dy);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetScrollCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).scroll = callbackRef;
    glfwSetScrollCallback((GLFWwindow *)window, ScrollCallback);
}

//// GLFWKeyCallback(long window, int key, int scancode, int action, int mods)
void KeyCallback(GLFWwindow *window, int key, int scancode, int action, int mods)
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).key);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(JIIII)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWKeyCallback.invoke() not found!\n");
            return;
        }
        env->CallVoidMethod(callbacks->at(window).key, method, window, key, scancode, action, mods);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetKeyCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).key = callbackRef;
    glfwSetKeyCallback((GLFWwindow *)window, KeyCallback);
}

//// GLFWCharModsCallback(long window, int codepoint, int mods)
void CharModsCallback(GLFWwindow *window, unsigned int codepoint, int mods)
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).charMods);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(JII)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWCharModsCallback.invoke() not found!\n");
            return;
        }
        env->CallVoidMethod(callbacks->at(window).charMods, method, window, codepoint, mods);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetCharModsCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).charMods = callbackRef;
    glfwSetCharModsCallback((GLFWwindow *)window, CharModsCallback);
}

//// GLFWWindowCloseCallback(long window)
void WindowCloseCallback(GLFWwindow *window)
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).windowClose);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(J)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWWindowCloseCallback.invoke() not found!\n");
            return;
        }
        env->CallVoidMethod(callbacks->at(window).windowClose, method, window);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowCloseCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).windowClose = callbackRef;
    glfwSetWindowCloseCallback((GLFWwindow *)window, WindowCloseCallback);
}

//// GLFWWindowPosCallback(long window, int xpos, int ypos)
void WindowPosCallback(GLFWwindow *window, int xpos, int ypos)
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).windowPos);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(JII)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWWindowPosCallback.invoke() not found!\n");
            return;
        }
        env->CallVoidMethod(callbacks->at(window).windowPos, method, window, xpos, ypos);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowPosCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).windowPos = callbackRef;
    glfwSetWindowPosCallback((GLFWwindow *)window, WindowPosCallback);
}

//// GLFWWindowFocusCallback(long window, boolean value)
void WindowFocusCallback(GLFWwindow *window, int value)
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).windowFocus);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(JZ)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWWindowFocusCallback.invoke() not found!\n");
            return;
        }
        bool result = (value == GLFW_TRUE);
        env->CallVoidMethod(callbacks->at(window).windowFocus, method, window, result);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowFocusCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).windowFocus = callbackRef;
    glfwSetWindowFocusCallback((GLFWwindow *)window, WindowFocusCallback);
}

//// GLFWWindowSizeCallback(long window, int width, int height)
void WindowSizeCallback(GLFWwindow *window, int width, int height)
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).windowSize);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(JII)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWWindowSizeCallback.invoke() not found!\n");
            return;
        }
        env->CallVoidMethod(callbacks->at(window).windowSize, method, window, width, height);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowSizeCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).windowSize = callbackRef;
    glfwSetWindowSizeCallback((GLFWwindow *)window, WindowSizeCallback);
}

//// GLFWWindowIconifyCallback(long window, boolean value)
void WindowIconifyCallback(GLFWwindow *window, int value)
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).windowIconify);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(JZ)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWWindowIconifyCallback.invoke() not found!\n");
            return;
        }
        bool result = (value == GLFW_TRUE);
        env->CallVoidMethod(callbacks->at(window).windowIconify, method, window, result);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowIconifyCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).windowIconify = callbackRef;
    glfwSetWindowIconifyCallback((GLFWwindow *)window, WindowIconifyCallback);
}

//// GLFWWindowRefreshCallback(long window)
void WindowRefreshCallback(GLFWwindow *window)
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).windowRefresh);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(J)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWWindowRefreshCallback.invoke() not found!\n");
            return;
        }
        env->CallVoidMethod(callbacks->at(window).windowRefresh, method, window);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowRefreshCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).windowRefresh = callbackRef;
    glfwSetWindowRefreshCallback((GLFWwindow *)window, WindowRefreshCallback);
}

//// GLFWFramebufferSizeCallbacke(long window, int w, int h)
void FramebufferSizeCallbacke(GLFWwindow *window, int width, int height)
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).framebufferSize);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(JII)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWFramebufferSizeCallbacke.invoke() not found!\n");
            return;
        }
        env->CallVoidMethod(callbacks->at(window).framebufferSize, method, window, width, height);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetFramebufferSizeCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).framebufferSize = callbackRef;
    glfwSetFramebufferSizeCallback((GLFWwindow *)window, FramebufferSizeCallbacke);
}

//// GLFWWindowContentScaleCallback(long window, float xscale, float yscale)
void WindowContentScaleCallback(GLFWwindow *window, float xscale, float yscale)
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).windowContentScale);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(JFF)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWWindowContentScaleCallback.invoke() not found!\n");
            return;
        }
        env->CallVoidMethod(callbacks->at(window).windowContentScale, method, window, xscale, yscale);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowContentScaleCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).windowContentScale = callbackRef;
    glfwSetWindowContentScaleCallback((GLFWwindow *)window, WindowContentScaleCallback);
}

//// GLFWDropCallback(long window, int count, long paths)
void DropCallback(GLFWwindow *window, int count, const char *paths[])
{
    if (jvm != NULL)
    {
        JNIEnv *env;
        jvm->GetEnv((void **)&env, JNI_VERSION_10);
        jclass clazz = env->GetObjectClass(callbacks->at(window).drop);
        jmethodID method = env->GetMethodID(clazz, "invoke", "(JI[Ljava/lang/String;)V");
        if (NULL == method)
        {
            fprintf(stderr, "The method GLFWDropCallback.invoke() not found!\n");
            return;
        }
        jobjectArray result = (jobjectArray)env->NewObjectArray(count, env->FindClass("java/lang/String"), env->NewStringUTF(""));
        for (int i = 0; i < count; i++)
        {
            env->SetObjectArrayElement(result, i, env->NewStringUTF(paths[i]));
        }
        env->CallVoidMethod(callbacks->at(window).drop, method, window, count, result);
    }
}
JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetDropCallback(JNIEnv *env, jobject sender, jlong window, jobject callback)
{
    if (jvm == NULL)
    {
        env->GetJavaVM(&jvm);
    }
    if (callbacks->count((GLFWwindow *)window) == 0)
    {
        Callbacks wndCallbacks;
        callbacks->insert(std::pair<GLFWwindow *, Callbacks>((GLFWwindow *)window, wndCallbacks));
    }
    jobject callbackRef = env->NewGlobalRef(callback);
    callbacks->at((GLFWwindow *)window).drop = callbackRef;
    glfwSetDropCallback((GLFWwindow *)window, DropCallback);
}


JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_DefaultWindowHinst(JNIEnv *env, jobject sender)
{
    glfwDefaultWindowHints();
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_WindowHint(JNIEnv *env, jobject sender, jint hint, jint value)
{
    glfwWindowHint(hint, value);
}

JNIEXPORT jlong JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_CreateWindow(JNIEnv *env, jobject sender, jint width, jint height, jstring title, jlong monitor, jlong share)
{
    const char *content = env->GetStringUTFChars(title, 0);
    GLFWwindow *window = glfwCreateWindow(width, height, content, (GLFWmonitor *)monitor, (GLFWwindow *)share);
    env->ReleaseStringUTFChars(title, content);
    return (jlong)window;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowShouldClose(JNIEnv *env, jobject sender, jlong window, jint value)
{
    glfwSetWindowShouldClose((GLFWwindow *)window, value);
}

JNIEXPORT jboolean JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_WindowShouldClose(JNIEnv *env, jobject sender, jlong window)
{
    int result = glfwWindowShouldClose((GLFWwindow *)window);
    return (result == 1) ? true : false;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_MakeContextCurrent(JNIEnv *env, jobject sender, jlong window)
{
    glfwMakeContextCurrent((GLFWwindow *)window);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowPos(JNIEnv *env, jobject sender, jlong window, jint xpos, jint ypos)
{
    glfwSetWindowPos((GLFWwindow *)window, xpos, ypos);
}

JNIEXPORT jintArray JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_GetWindowPos(JNIEnv *env, jobject sender, jlong window)
{
    int x, y;
    glfwGetWindowPos((GLFWwindow *)window, &x, &y);
    jint location[2];
    location[0] = x;
    location[1] = y;
    jintArray result = env->NewIntArray(2);
    if (result == NULL)
    {
        return NULL;
    }
    env->SetIntArrayRegion(result, 0, 2, location);
    return result;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowSize(JNIEnv *env, jobject sender, jlong window, jint width, jint height)
{
    glfwSetWindowSize((GLFWwindow *)window, width, height);
}

JNIEXPORT jintArray JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_GetWindowSize(JNIEnv *env, jobject sender, jlong window)
{
    int w, h;
    glfwGetWindowSize((GLFWwindow *)window, &w, &h);
    jint size[2];
    size[0] = w;
    size[1] = h;
    jintArray result = env->NewIntArray(2);
    if (result == NULL)
    {
        return NULL;
    }
    env->SetIntArrayRegion(result, 0, 2, size);
    return result;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowSizeLimits(JNIEnv *env, jobject sender, jlong window, jint minWidth, jint minHeight, jint maxWidth, jint maxHeight)
{
    glfwSetWindowSizeLimits((GLFWwindow *)window, minWidth, minHeight, maxWidth, maxHeight);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowAspectRatio(JNIEnv *env, jobject sender, jlong window, jint number, jint denom)
{
    glfwSetWindowAspectRatio((GLFWwindow *)window, number, denom);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_ShowWindow(JNIEnv *env, jobject sender, jlong window)
{
    glfwShowWindow((GLFWwindow *)window);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_HideWindow(JNIEnv *env, jobject sender, jlong window)
{
    glfwHideWindow((GLFWwindow *)window);
}

JNIEXPORT jfloatArray JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_GetWindowContentScale(JNIEnv *env, jobject sender, jlong window)
{
    float xscale, yscale;
    glfwGetWindowContentScale((GLFWwindow *)window, &xscale, &yscale);
    jfloat scale[2];
    scale[0] = xscale;
    scale[1] = yscale;
    jfloatArray result = env->NewFloatArray(2);
    if (result == NULL)
    {
        return NULL;
    }
    env->SetFloatArrayRegion(result, 0, 2, scale);
    return result;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_MaximizeWindow(JNIEnv *env, jobject sender, jlong window)
{
    glfwMaximizeWindow((GLFWwindow *)window);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_RestoreWindow(JNIEnv *env, jobject sender, jlong window)
{
    glfwRestoreWindow((GLFWwindow *)window);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_FocusWindow(JNIEnv *env, jobject sender, jlong window)
{
    glfwFocusWindow((GLFWwindow *)window);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_IconifyWindow(JNIEnv *env, jobject sender, jlong window)
{
    glfwIconifyWindow((GLFWwindow *)window);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_DestroyWindow(JNIEnv *env, jobject sender, jlong window)
{
    glfwDestroyWindow((GLFWwindow *)window);
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowIcon(JNIEnv *env, jobject sender, jlong window, jint count, jobjectArray image)
{
    GLFWimage *images = new GLFWimage[count];
    std::vector<jbyteArray> dataArrays;

    for (int i = 0; i < count; i++)
    {
        GLFWimage img;
        jobject obj = env->GetObjectArrayElement(image, i);
        jclass clazz = env->GetObjectClass(obj);

        jfieldID widthId = env->GetFieldID(clazz, "width", "I");
        img.width = env->GetIntField(obj, widthId);

        jfieldID heightId = env->GetFieldID(clazz, "height", "I");
        img.height = env->GetIntField(obj, heightId);

        jfieldID pixelsId = env->GetFieldID(clazz, "pixels", "[B");
        jobject arrayData = env->GetObjectField(obj, pixelsId);
        jbyteArray *arr = reinterpret_cast<jbyteArray *>(&arrayData);
        dataArrays.push_back(*arr);

        img.pixels = (unsigned char *)env->GetByteArrayElements(dataArrays[i], NULL);

        images[i] = img;
    }
    glfwSetWindowIcon((GLFWwindow *)window, count, images);
    for (int i = 0; i < count; i++)
    {
        env->ReleaseByteArrayElements(dataArrays[i], (jbyte *)images[i].pixels, 0);
    }
    delete[] images;
}

JNIEXPORT jlong JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_GetPrimaryMonitor(JNIEnv *env, jobject sender)
{
    GLFWmonitor *monitor = glfwGetPrimaryMonitor();
    return (jlong)monitor;
}

JNIEXPORT jintArray JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_GetMonitorWorkarea(JNIEnv *env, jobject sender, jlong monitor)
{
    int x, y, w, h;
    glfwGetMonitorWorkarea((GLFWmonitor *)monitor, &x, &y, &w, &h);
    jint area[4];
    area[0] = x;
    area[1] = y;
    area[2] = w;
    area[3] = h;
    jintArray result = env->NewIntArray(4);
    if (result == NULL)
    {
        return NULL;
    }
    env->SetIntArrayRegion(result, 0, 4, area);
    return result;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetWindowMonitor(JNIEnv *env, jobject sender, jlong window, jlong monitor, jint xpos, jint ypos, jint width, jint height, jint refreshRate)
{
    glfwSetWindowMonitor((GLFWwindow *)window, (GLFWmonitor *)monitor, xpos, ypos, width, height, refreshRate);
}

JNIEXPORT jintArray JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_GetFramebufferSize(JNIEnv *env, jobject sender, jlong window)
{
    int w, h;
    glfwGetFramebufferSize((GLFWwindow *)window, &w, &h);
    jint size[2];
    size[0] = w;
    size[1] = h;
    jintArray result = env->NewIntArray(2);
    if (result == NULL)
    {
        return NULL;
    }
    env->SetIntArrayRegion(result, 0, 2, size);
    return result;
}

JNIEXPORT jobject JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_GetVideoMode(JNIEnv *env, jobject sender, jlong monitor)
{
    const GLFWvidmode *nativeVidMode = glfwGetVideoMode((GLFWmonitor *)monitor);
    jclass clazz = env->FindClass("com/spvessel/spacevil/internal/Wrapper/GLFWVidMode");
    jmethodID constructor = env->GetMethodID(clazz, "<init>", "()V");
    jobject javaVidMode = env->NewObject(clazz, constructor);

    jfieldID widthId = env->GetFieldID(clazz, "width", "I");
    env->SetIntField(javaVidMode, widthId, nativeVidMode->width);

    jfieldID heightId = env->GetFieldID(clazz, "height", "I");
    env->SetIntField(javaVidMode, heightId, nativeVidMode->height);

    jfieldID redBitsId = env->GetFieldID(clazz, "redBits", "I");
    env->SetIntField(javaVidMode, redBitsId, nativeVidMode->redBits);

    jfieldID greenBitsId = env->GetFieldID(clazz, "greenBits", "I");
    env->SetIntField(javaVidMode, greenBitsId, nativeVidMode->greenBits);

    jfieldID blueBitsId = env->GetFieldID(clazz, "blueBits", "I");
    env->SetIntField(javaVidMode, blueBitsId, nativeVidMode->blueBits);

    jfieldID refreshRateId = env->GetFieldID(clazz, "refreshRate", "I");
    env->SetIntField(javaVidMode, refreshRateId, nativeVidMode->refreshRate);

    return javaVidMode;
}

JNIEXPORT jfloatArray JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_GetMonitorContentScale(JNIEnv *env, jobject sender, jlong monitor)
{
    float xscale, yscale;
    glfwGetMonitorContentScale((GLFWmonitor *)monitor, &xscale, &yscale);
    jfloat scale[2];
    scale[0] = xscale;
    scale[1] = yscale;
    jfloatArray result = env->NewFloatArray(2);
    if (result == NULL)
    {
        return NULL;
    }
    env->SetFloatArrayRegion(result, 0, 2, scale);
    return result;
}

JNIEXPORT jlong JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_CreateStandardCursor(JNIEnv *env, jobject sender, jint shape)
{
    return (jlong)glfwCreateStandardCursor(shape);
}

JNIEXPORT jlong JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_CreateCursor(JNIEnv *env, jobject sender, jobject image, jint xhot, jint yhot)
{
    GLFWimage img;
    jclass clazz = env->GetObjectClass(image);

    jfieldID widthId = env->GetFieldID(clazz, "width", "I");
    img.width = env->GetIntField(image, widthId);

    jfieldID heightId = env->GetFieldID(clazz, "height", "I");
    img.height = env->GetIntField(image, heightId);

    jfieldID pixelsId = env->GetFieldID(clazz, "pixels", "[B");
    jobject arrayData = env->GetObjectField(image, pixelsId);
    jbyteArray *arr = reinterpret_cast<jbyteArray *>(&arrayData);
    img.pixels = (unsigned char *)env->GetByteArrayElements(*arr, NULL);

    GLFWcursor *cursor = glfwCreateCursor(&img, xhot, yhot);

    env->ReleaseByteArrayElements(*arr, (jbyte *)img.pixels, 0);

    return (jlong)cursor;
}

JNIEXPORT void JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_SetCursor(JNIEnv *env, jobject sender, jlong window, jlong cursor)
{
    glfwSetCursor((GLFWwindow *)window, (GLFWcursor *)cursor);
}

JNIEXPORT jdoubleArray JNICALL Java_com_spvessel_spacevil_internal_Wrapper_GlfwWrapper_GetCursorPos(JNIEnv *env, jobject sender, jlong window)
{
    double x, y;
    glfwGetCursorPos((GLFWwindow *)window, &x, &y);
    jdouble pos[2];
    pos[0] = x;
    pos[1] = y;
    jdoubleArray result = env->NewDoubleArray(2);
    if (result == NULL)
    {
        return NULL;
    }
    env->SetDoubleArrayRegion(result, 0, 2, pos);
    return result;
}
