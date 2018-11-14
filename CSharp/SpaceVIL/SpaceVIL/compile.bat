"C:\Program Files (x86)\Microsoft Visual Studio\2017\Community\MSBuild\15.0\Bin\Roslyn\csc.exe" ^
-define:WINDOWS ^
-platform:x64 ^
-optimize ^
/unsafe ^
/t:exe ^
/r:System.Drawing.dll ^
-appconfig:App.config ^
-out:spacevil.exe ^
-res:Shaders\fs_fill.glsl,SpaceVIL.Shaders.fs_fill.glsl ^
-res:Shaders\vs_fill.glsl,SpaceVIL.Shaders.vs_fill.glsl ^
-res:Shaders\fs_char.glsl,SpaceVIL.Shaders.fs_char.glsl ^
-res:Shaders\vs_char.glsl,SpaceVIL.Shaders.vs_char.glsl ^
-res:Shaders\fs_fxaa.glsl,SpaceVIL.Shaders.fs_fxaa.glsl ^
-res:Shaders\vs_fxaa.glsl,SpaceVIL.Shaders.vs_fxaa.glsl ^
-res:Shaders\fs_blur.glsl,SpaceVIL.Shaders.fs_blur.glsl ^
-res:Shaders\vs_blur.glsl,SpaceVIL.Shaders.vs_blur.glsl ^
-res:Shaders\fs_texture.glsl,SpaceVIL.Shaders.fs_texture.glsl ^
-res:Shaders\vs_texture.glsl,SpaceVIL.Shaders.vs_texture.glsl ^
-res:Fonts\Ubuntu-Regular.ttf,SpaceVIL.Fonts.Ubuntu-Regular.ttf ^
-nowarn:CS0414 ^
-nowarn:CS0169 ^
-nowarn:CS0649 ^
-recurse:*.cs
