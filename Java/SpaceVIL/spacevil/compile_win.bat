cd src/main/java/com/spvessel/spacevil/internal/Wrapper
javac -h . ^
GlfwWrapper.java ^
OpenGLWrapper.java ^
GLFWImage.java ^
GLFWVidMode.java ^
GLFWCursorPosCallback.java ^
GLFWMouseButtonCallback.java ^
GLFWScrollCallback.java ^
GLFWKeyCallback.java ^
GLFWCharModsCallback.java ^
GLFWWindowCloseCallback.java ^
GLFWWindowPosCallback.java ^
GLFWWindowFocusCallback.java ^
GLFWWindowSizeCallback.java ^
GLFWWindowIconifyCallback.java ^
GLFWWindowRefreshCallback.java ^
GLFWFramebufferSizeCallback.java ^
GLFWWindowContentScaleCallback.java ^
GLFWDropCallback.java
copy com_spvessel_spacevil_internal_Wrapper_GlfwWrapper.h "../../../../../../native/common/glfwwrapper.h"
copy com_spvessel_spacevil_internal_Wrapper_OpenGLWrapper.h "../../../../../../native/common/openglwrapper.h"
cd ../../../../../../../..
cd src/main/native/windows
call build.bat
cd ../../../../..
copy .\src\main\native\windows\build\Release\wrapper.dll .\src\main\resources\native\windows\
call ./gradlew build
copy build\libs\spacevil.jar ..\sandbox\libs
