// #define LINUX 

using System;
using System.Text;
using System.IO;
using static OpenGL.OpenGLWrapper;

namespace SpaceVIL
{
    internal sealed class Shader// : GL.WGL.OpenWGL
    {
        const uint Vertex = 0x8B31;
        const uint Fragment = 0x8B30;
        const uint Geometry = 0x8DD9;
        const uint Compute = 0x91B9;


        private uint _programId;
        internal uint GetProgramID()
        {
            return _programId;
        }

        private uint _shaderVertex;
        private String _codeVertex;

        private uint _shaderFragment;
        private String _codeFragment;

        private uint _shaderGeometry;
        private String _codeGeometry;

        private uint _shaderCompute;
        private String _codeCompute;

        internal String GetCode(uint shaderType)
        {
            switch (shaderType)
            {
                case Vertex:
                    return _codeVertex;

                case Fragment:
                    return _codeFragment;

                case Geometry:
                    return _codeGeometry;

                case Compute:
                    return _codeCompute;

                default:
                    break;
            }
            return String.Empty;
        }

        internal void SetShaderPath(uint shaderType, String path)
        {
            String code = ReadSource(path);

            switch (shaderType)
            {
                case Vertex:
                    _codeVertex = code.ToString();
                    break;

                case Fragment:
                    _codeFragment = code.ToString();
                    break;

                case Geometry:
                    _codeGeometry = code.ToString();
                    break;

                case Compute:
                    _codeCompute = code.ToString();
                    break;

                default:
                    break;
            }
        }

        private String _name;
        internal String GetShaderName()
        {
            return _name;
        }

        internal Shader(String designation)
        {
            _name = designation;
            InitDefaults();
        }

        internal Shader(String vertexCode, String fragmendCode, String designation)
        {
            _name = designation;

            _codeVertex = vertexCode;
            _codeFragment = fragmendCode;

            //defaults
            _codeGeometry = String.Empty;
            _codeCompute = String.Empty;
        }

        internal Shader(Stream vertexCode, Stream fragmendCode, String designation)
        {
            _name = designation;
            _codeVertex = ReadSource(vertexCode);
            _codeFragment = ReadSource(fragmendCode);

            //defaults
            _codeGeometry = String.Empty;
            _codeCompute = String.Empty;
        }

        internal Shader(Stream vertexCode, Stream geometryCode, Stream fragmendCode, String designation)
        {
            _name = designation;
            _codeVertex = ReadSource(vertexCode);
            _codeGeometry = ReadSource(geometryCode);
            _codeFragment = ReadSource(fragmendCode);

            //defaults
            _codeCompute = String.Empty;
        }

        private void InitDefaults()
        {
            _codeVertex = String.Empty;
            _codeFragment = String.Empty;
            _codeGeometry = String.Empty;
            _codeCompute = String.Empty;
        }

        private String ReadSource(Stream source)
        {
            StringBuilder code = new StringBuilder();
            try
            {
                using (StreamReader sr = new StreamReader(source))
                {
                    while (!sr.EndOfStream)
                        code.Append(sr.ReadLine() + "\n");
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("The stream could not be read:");
                Console.WriteLine(e.Message);
            }
            return code.ToString();
        }

        private String ReadSource(String source)
        {
            StringBuilder code = new StringBuilder();
            try
            {
                using (StreamReader sr = new StreamReader(source))
                {
                    while (!sr.EndOfStream)
                        code.Append(sr.ReadLine() + "\n");
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("The file could not be read:");
                Console.WriteLine(e.Message);
            }
            return code.ToString();
        }

        internal void Compile()
        {
            _programId = glCreateProgram();
            //compiling
            if (!_codeVertex.Equals(String.Empty))
            {
                _shaderVertex = CompileShader(Vertex, _codeVertex);
                glAttachShader(_programId, _shaderVertex);
            }

            if (!_codeFragment.Equals(String.Empty))
            {
                _shaderFragment = CompileShader(Fragment, _codeFragment);
                glAttachShader(_programId, _shaderFragment);
            }

            if (!_codeGeometry.Equals(String.Empty))
            {
                _shaderGeometry = CompileShader(Geometry, _codeGeometry);
                glAttachShader(_programId, _shaderGeometry);
            }

            if (!_codeCompute.Equals(String.Empty))
            {
                _shaderCompute = CompileShader(Compute, _codeCompute);
                glAttachShader(_programId, _shaderCompute);
            }
            //linking
            glLinkProgram(_programId);    
        }

        private uint CompileShader(uint shaderType, String code)
        {
            uint shaderId = glCreateShader((uint)shaderType);
            glShaderSource(shaderId, 1, new[] { code }, new[] { code.Length });
            glCompileShader(shaderId);
            return shaderId;
        }

        internal void UseShader()
        {
            glUseProgram(_programId);
        }

        internal void DeleteShader()
        {
            DetachShadersCode();
            DeleteShadersID();
            glDeleteProgram(_programId);
        }

        private void DetachShadersCode()
        {
            glDetachShader(_programId, _shaderVertex);
            glDetachShader(_programId, _shaderFragment);
            glDetachShader(_programId, _shaderGeometry);
            glDetachShader(_programId, _shaderCompute);
        }

        private void DeleteShadersID()
        {
            glDeleteShader(_shaderVertex);
            glDeleteShader(_shaderFragment);
            glDeleteShader(_shaderGeometry);
            glDeleteShader(_shaderCompute);
        }
    }
}