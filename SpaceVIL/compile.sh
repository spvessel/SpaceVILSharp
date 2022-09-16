# how install [dotnet] and [nuget]:
# https://docs.microsoft.com/en-us/dotnet/core/install/linux-ubuntu
# https://docs.microsoft.com/en-us/nuget/install-nuget-client-tools#cli-tools
cd ..
rm -rf com.spvessel.spacevil
mkdir com.spvessel.spacevil
cd SpaceVIL
dotnet nuget locals all --clear
dotnet restore
dotnet msbuild /t:pack -p:Configuration=Release /p:AssemblyName=com.spvessel.spacevil-0.0.0-core-dev
nuget add ./bin/Release/com.spvessel.spacevil.0.0.0-dev.nupkg -source ./..
