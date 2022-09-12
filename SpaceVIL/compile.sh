cd ..
rm -rf com.spvessel.spacevil
mkdir com.spvessel.spacevil
cd SpaceVIL
dotnet nuget locals all --clear
dotnet restore
dotnet msbuild /t:pack -p:Configuration=Release /p:AssemblyName=com.spvessel.spacevil-0.0.0-core-dev
nuget add ./bin/Release/com.spvessel.spacevil.0.0.0-dev.nupkg -source ./..
