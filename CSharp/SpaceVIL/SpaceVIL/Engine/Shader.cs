using System;
using System.Text;
using System.IO;
using Glfw3;

#if OS_LINUX
using static GL.LGL.OpenLGL;
#elif OS_WNDOWS
using static GL.WGL.OpenWGL;
#else
using static GL.WGL.OpenWGL;
#endif

namespace SpaceVIL
{
    internal class Shader// : GL.WGL.OpenWGL
    {
        const uint Vertex = 0x8B31;
        const uint Fragment = 0x8B30;
        const uint Geometry = 0x8DD9;
        const uint Compute = 0x91B9;


        private uint _program_id;
        internal uint GetProgramID()
        {
            return _program_id;
        }

        private uint _shader_vertex;
        private String _code_vertex;

        private uint _shader_fragment;
        private String _code_fragment;

        private uint _shader_geometry;
        private String _code_geometry;

        private uint _shader_compute;
        private String _code_compute;

        public String GetCode(uint shader_type)
        {
            switch (shader_type)
            {
                case Vertex:
                    return _code_vertex;

                case Fragment:
                    return _code_fragment;

                case Geometry:
                    return _code_geometry;

                case Compute:
                    return _code_compute;

                default:
                    break;
            }
            return String.Empty;
        }
        public void SetShaderPath(uint shader_type, String path)
        {
            String code = ReadSource(path);

            switch (shader_type)
            {
                case Vertex:
                    _code_vertex = code.ToString();
                    break;

                case Fragment:
                    _code_fragment = code.ToString();
                    break;

                case Geometry:
                    _code_geometry = code.ToString();
                    break;

                case Compute:
                    _code_compute = code.ToString();
                    break;

                default:
                    break;
            }
        }

        internal Shader()
        {
            InitDefaults();
        }
        internal Shader(String vertex_code, String fragmend_code)
        {
            _code_vertex = vertex_code;
            _code_fragment = fragmend_code;

            //defaults
            _code_geometry = String.Empty;
            _code_compute = String.Empty;
        }
        internal Shader(Stream vertex_code, Stream fragmend_code)
        {
            _code_vertex = ReadSource(vertex_code);
            _code_fragment = ReadSource(fragmend_code);

            //defaults
            _code_geometry = String.Empty;
            _code_compute = String.Empty;
        }
        private void InitDefaults()
        {
            _code_vertex = String.Empty;
            _code_fragment = String.Empty;
            _code_geometry = String.Empty;
            _code_compute = String.Empty;
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
            _program_id = glCreateProgram();
            //compiling
            if (_code_vertex != String.Empty)
            {
                _shader_vertex = CompileShader(Vertex, _code_vertex);
                glAttachShader(_program_id, _shader_vertex);
            }

            if (_code_fragment != String.Empty)
            {
                _shader_fragment = CompileShader(Fragment, _code_fragment);
                glAttachShader(_program_id, _shader_fragment);
            }

            if (_code_geometry != String.Empty)
            {
                _shader_geometry = CompileShader(Geometry, _code_geometry);
                glAttachShader(_program_id, _shader_geometry);
            }

            if (_code_compute != String.Empty)
            {
                _shader_compute = CompileShader(Compute, _code_compute);
                glAttachShader(_program_id, _shader_compute);
            }
            //linking
            glLinkProgram(_program_id);
            
        }
        private uint CompileShader(uint shader_type, String code)
        {
            uint shader_id = glCreateShader((uint)shader_type);
            glShaderSource(shader_id, 1, new[] { code }, new[] { code.Length });
            glCompileShader(shader_id);
            return shader_id;
        }

        internal void UseShader()
        {
            glUseProgram(_program_id);
        }

        internal void DeleteShader()
        {
            DetachShadersCode();
            DeleteShadersID();
            glDeleteProgram(_program_id);
        }
        private void DetachShadersCode()
        {
            glDetachShader(_program_id, _shader_vertex);
            glDetachShader(_program_id, _shader_fragment);
            glDetachShader(_program_id, _shader_geometry);
            glDetachShader(_program_id, _shader_compute);
        }
        private void DeleteShadersID()
        {
            glDeleteShader(_shader_vertex);
            glDeleteShader(_shader_fragment);
            glDeleteShader(_shader_geometry);
            glDeleteShader(_shader_compute);
        }
    }
}