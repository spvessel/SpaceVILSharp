using System;
using System.Reflection;

namespace SpaceVIL
{
    internal class ShaderFactory
    {
        internal static readonly int Primitive = 1;
        internal static readonly int Texture = 2;
        internal static readonly int Symbol = 4;
        internal static readonly int Blur = 8;

        internal static Shader GetShader(int type)
        {
            if (type == Primitive)
            {
                return CreateShader("SpaceVIL.Shaders.vs_primitive.glsl",
                                    "SpaceVIL.Shaders.fs_primitive.glsl", 
                                    "_primitive");
            }
            else if (type == Texture)
            {
                return CreateShader("SpaceVIL.Shaders.vs_texture.glsl",
                                    "SpaceVIL.Shaders.fs_texture.glsl",
                                    "_texture");
            }
            else if (type == Symbol)
            {
                return CreateShader("SpaceVIL.Shaders.vs_char.glsl",
                                    "SpaceVIL.Shaders.fs_char.glsl",
                                    "_char");
            }
            else if (type == Blur)
            {
                return CreateShader("SpaceVIL.Shaders.vs_blur.glsl",
                                    "SpaceVIL.Shaders.fs_blur.glsl",
                                    "_blur");
            }
            return null;
        }

        private static Shader CreateShader(String vertexCode, String fragmentCode, String designation)
        {
            Shader shader = new Shader(Assembly.GetExecutingAssembly().GetManifestResourceStream(vertexCode), 
                                        Assembly.GetExecutingAssembly().GetManifestResourceStream(fragmentCode), designation);
            shader.Compile();
            if (shader.GetProgramID() == 0)
                Console.WriteLine("Could not create " + designation + " shaders");
            return shader;
        }
    }
}