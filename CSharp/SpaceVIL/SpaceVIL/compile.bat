"C:\Program Files (x86)\Microsoft Visual Studio\2017\Community\MSBuild\15.0\Bin\Roslyn\csc.exe" ^
-platform:x64 ^
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
-res:Fonts\RobotoMono-Regular.ttf,SpaceVIL.Fonts.RobotoMono-Regular.ttf ^
-res:Fonts\OpenGostTypeA-Regular.ttf,SpaceVIL.Fonts.OpenGostTypeA-Regular.ttf ^
-res:Fonts\Verdana.ttf,SpaceVIL.Fonts.Verdana.ttf ^
-res:Fonts\Exo2-Regular.ttf,SpaceVIL.Fonts.Exo2-Regular.ttf ^
-res:Fonts\TitilliumWeb-Regular.ttf,SpaceVIL.Fonts.TitilliumWeb-Regular.ttf ^
-res:Fonts\OpenSans-Regular.ttf,SpaceVIL.Fonts.OpenSans-Regular.ttf ^
-res:Fonts\Sans-Light.ttf,SpaceVIL.Fonts.Sans-Light.ttf ^
-res:Fonts\Muli-Regular.ttf,SpaceVIL.Fonts.Muli-Regular.ttf ^
-res:Fonts\Nunito-Regular.ttf,SpaceVIL.Fonts.Nunito-Regular.ttf ^
-res:Fonts\Quicksand-Regular.ttf,SpaceVIL.Fonts.Quicksand-Regular.ttf ^
-res:Fonts\Ubuntu-Regular.ttf,SpaceVIL.Fonts.Ubuntu-Regular.ttf ^
-res:Fonts\GlacialIndifference-Regular.otf,SpaceVIL.Fonts.GlacialIndifference-Regular.otf ^
-nowarn:CS0414 ^
-nowarn:CS0169 ^
-nowarn:CS0649 ^
-recurse:*.cs
