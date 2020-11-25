cmake_minimum_required(VERSION 3.6)

# set the project name
project(glfwwrapper)

# specify the C++ standard

# add the include dirs
include_directories("C:\\Program Files\\AdoptOpenJDK\\jdk-11.0.8.10-hotspot\\include")
include_directories("C:\\Program Files\\AdoptOpenJDK\\jdk-11.0.8.10-hotspot\\include\\win32")
include_directories("${PROJECT_SOURCE_DIR}/../common")
include_directories("${PROJECT_SOURCE_DIR}/../common/GLFW")

link_directories("C:\\glfw")

add_library(glfwwrapper SHARED glfwwrapper.cpp)

target_link_libraries(glfwwrapper PUBLIC glfw3.dll)
