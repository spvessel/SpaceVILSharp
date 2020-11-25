cmake_minimum_required(VERSION 3.10)

# set the project name
project(spwrapper)

find_package(OpenGL REQUIRED)

# specify the C++ standard
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# add the include dirs
include_directories("/Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home/include")
include_directories("/Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home/include/darwin")
include_directories("${PROJECT_SOURCE_DIR}/../common")
include_directories("${PROJECT_SOURCE_DIR}/../common/GLFW")

link_directories("/Users/Okami-tyan/Documents/libs/glfw")
link_directories("/System/Library/Frameworks/OpenGL.framework/Libraries/")

add_library(spwrapper SHARED glfwwrapper.cpp openglwrapper.cpp)

target_link_libraries(spwrapper libglfw.dylib libGL.dylib)
