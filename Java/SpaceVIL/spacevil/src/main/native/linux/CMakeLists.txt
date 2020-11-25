cmake_minimum_required(VERSION 3.10)

# set the project name
project(spwrapper)

find_package(OpenGL REQUIRED)

# specify the C++ standard
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# add the include dirs
include_directories("/usr/lib/jvm/java-11-openjdk-amd64/include")
include_directories("/usr/lib/jvm/java-11-openjdk-amd64/include/linux")
include_directories("${PROJECT_SOURCE_DIR}/../common")
include_directories("${PROJECT_SOURCE_DIR}/../common/GLFW")
include_directories("${PROJECT_SOURCE_DIR}/../common/KHR")

link_directories("/home/rsedaikin/libs/glfw")

add_library(spwrapper SHARED glfwwrapper.cpp openglwrapper.cpp)

target_link_libraries(spwrapper libglfw3.so libGL.so)
