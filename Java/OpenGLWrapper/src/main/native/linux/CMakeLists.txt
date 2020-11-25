cmake_minimum_required(VERSION 3.10)

# set the project name
project(openglapi)

# specify the C++ standard
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# add the include dirs
include_directories("C:\\Program Files\\AdoptOpenJDK\\jdk-11.0.8.10-hotspot\\include")
include_directories("C:\\Program Files\\AdoptOpenJDK\\jdk-11.0.8.10-hotspot\\include\\win32")
include_directories("${PROJECT_SOURCE_DIR}/../common")
include_directories("${PROJECT_SOURCE_DIR}/../common/KHR")

add_library(openglapi SHARED openglapi.cpp)

target_link_libraries(openglapi libGL.so)
