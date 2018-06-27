"C:\Program Files (x86)\Microsoft Visual Studio\2017\Community\MSBuild\15.0\Bin\Roslyn\csc.exe" ^
-platform:anycpu32bitpreferred ^
-optimize ^
/unsafe ^
/t:exe ^
/r:System.Drawing.dll ^
-appconfig:App.config ^
-out:spacevil.exe ^
-res:Shaders\fs_fill.glsl,SpaceVIL.Shaders.fs_fill.glsl ^
-res:Shaders\vs_fill.glsl,SpaceVIL.Shaders.vs_fill.glsl ^
-res:Shaders\fs_texture.glsl,SpaceVIL.Shaders.fs_texture.glsl ^
-res:Shaders\vs_texture.glsl,SpaceVIL.Shaders.vs_texture.glsl ^
-recurse:*.cs
