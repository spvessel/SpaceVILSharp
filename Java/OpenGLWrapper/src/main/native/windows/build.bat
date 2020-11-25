call clean.bat
cd build
cmake -DCMAKE_GENERATOR_PLATFORM=x64 -DCMAKE_BUILD_TYPE=Release ..
cmake --build . --config Release
