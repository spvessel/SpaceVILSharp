"C:\Program Files (x86)\Microsoft Visual Studio\2017\Community\MSBuild\15.0\Bin\Roslyn\csc.exe" ^
-platform:anycpu ^
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
-res:Fonts\verdana.ttf,SpaceVIL.Fonts.verdana.ttf ^
-res:Fonts\moireregular.ttf,SpaceVIL.Fonts.moireregular.ttf ^
-res:Fonts\opensans.ttf,SpaceVIL.Fonts.opensans.ttf ^
-res:Fonts\Muli-Regular.ttf,SpaceVIL.Fonts.Muli-Regular.ttf ^
-res:Fonts\Nunito-Regular.ttf,SpaceVIL.Fonts.Nunito-Regular.ttf ^
-res:Fonts\Quicksand-Regular.ttf,SpaceVIL.Fonts.Quicksand-Regular.ttf ^
-res:Fonts\Ubuntu-Regular.ttf,SpaceVIL.Fonts.Ubuntu-Regular.ttf ^
-recurse:*.cs
