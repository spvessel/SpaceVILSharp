dotnet nuget locals all --clear
dotnet add package com.spvessel.spacevil -s ./../ --prerelease
dotnet restore
dotnet run
