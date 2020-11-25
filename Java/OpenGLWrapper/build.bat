cd src/main/java/com/spvessel/openglwrapper/
javac -h . OpenGLWrapper.java NativeLibraryManager.java
copy com_spvessel_openglwrapper_OpenGLWrapper.h "../../../../native/common/openglwrapper.h"
cd ../../../../../..
cd src/main/native/windows
call build.bat
cd ../../../../..
copy .\src\main\native\windows\build\Release\openglwrapper.dll .\src\main\resources\native\windows\
./gradlew build
