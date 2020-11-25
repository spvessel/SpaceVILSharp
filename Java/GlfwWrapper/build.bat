cd src/main/java/com/spvessel/glfwwrapper/
javac -h . ^
GlfwWrapper.java ^
NativeLibraryManager.java ^
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
copy com_spvessel_glfwwrapper_GlfwWrapper.h "../../../../native/common/glfwwrapper.h"
cd ../../../../../..
cd src/main/native/windows
call build.bat
cd ../../../../..
copy .\src\main\native\windows\build\Release\glfwwrapper.dll .\src\main\resources\native\windows\
copy .\src\main\native\windows\build\Release\glfwwrapper.exp .\src\main\resources\native\windows\
@REM ./gradlew build
