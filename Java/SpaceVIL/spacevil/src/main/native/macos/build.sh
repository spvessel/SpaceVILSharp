#!/bin/bash
rm -r build
mkdir build
cd build
cmake -DCMAKE_C_COMPILER="/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc" -DCMAKE_CXX_COMPILER="/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/c++" -DCMAKE_BUILD_TYPE=Release ..
cmake --build . --config Release