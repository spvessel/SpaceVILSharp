# how install [dotnet] and [nuget]:
# https://docs.microsoft.com/en-us/dotnet/core/install/linux-ubuntu
# https://docs.microsoft.com/en-us/nuget/install-nuget-client-tools#cli-tools
dotnet nuget locals all --clear
dotnet add package com.spvessel.spacevil -s ./../ --prerelease
dotnet restore
dotnet build
dotnet run --no-build
