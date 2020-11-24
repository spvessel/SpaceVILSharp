#!/bin/bash
cd src/main/java/com/spvessel/spacevil/internal/Wrapper
javac -h . \
GlfwWrapper.java \
OpenGLWrapper.java \
GLFWImage.java \
GLFWVidMode.java \
GLFWCursorPosCallback.java \
GLFWMouseButtonCallback.java \
GLFWScrollCallback.java \
GLFWKeyCallback.java \
GLFWCharModsCallback.java \
GLFWWindowCloseCallback.java \
GLFWWindowPosCallback.java \
GLFWWindowFocusCallback.java \
GLFWWindowSizeCallback.java \
GLFWWindowIconifyCallback.java \
GLFWWindowRefreshCallback.java \
GLFWFramebufferSizeCallback.java \
GLFWWindowContentScaleCallback.java \
GLFWDropCallback.java
cp com_spvessel_spacevil_internal_Wrapper_GlfwWrapper.h "../../../../../../native/common/glfwwrapper.h"
cp com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper.h "../../../../../../native/common/openglwrapper.h"
cd ../../../../../../../..
cd src/main/native/linux
./build.sh
cd ../../../../
pwd
cp src/main/native/linux/build/libwrapper.so src/main/resources/native/linux
gradle build
