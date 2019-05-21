package com.spvessel.spacevil;

import com.spvessel.spacevil.Flags.ShaderType;
import static org.lwjgl.opengl.GL20.*;

import java.io.FileReader;
import java.io.BufferedReader;

final class Shader {

    private int _program_id;

    long getProgramID() {
        return _program_id;
    }

    private int _shader_vertex;
    private String _code_vertex;

    private int _shader_fragment;
    private String _code_fragment;

    private int _shader_geometry;
    private String _code_geometry;

    private int _shader_compute;
    private String _code_compute;

    String getCode(ShaderType shader_type) {
        switch (shader_type) {
        case VERTEX:
            return _code_vertex;

        case FRAGMENT:
            return _code_fragment;

        case GEOMETRY:
            return _code_geometry;

        case COMPUTE:
            return _code_compute;

        default:
            break;
        }
        return "";
    }

    void setShaderPath(ShaderType shader_type, String path) {
        String code = readSource(path);

        switch (shader_type) {
        case VERTEX:
            _code_vertex = code.toString();
            break;

        case FRAGMENT:
            _code_fragment = code.toString();
            break;

        case GEOMETRY:
            _code_geometry = code.toString();
            break;

        case COMPUTE:
            _code_compute = code.toString();
            break;

        default:
            break;
        }
    }

    Shader() {
        initDefaults();
    }

    Shader(String vertex_code, String fragmend_code) {
        _code_vertex = vertex_code;
        _code_fragment = fragmend_code;

        // defaults
        _code_geometry = "";
        _code_compute = "";
    }

    Shader(BufferedReader vertex_code, BufferedReader fragmend_code) {
        _code_vertex = readSource(vertex_code);
        _code_fragment = readSource(fragmend_code);

        // defaults
        _code_geometry = "";
        _code_compute = "";
    }

    private void initDefaults() {
        _code_vertex = "";
        _code_fragment = "";
        _code_geometry = "";
        _code_compute = "";
    }

    private String readSource(BufferedReader source) {
        StringBuilder code = new StringBuilder();
        String line = null;
        try {
            while ((line = source.readLine()) != null) {
                code.append(line);
                code.append('\n');
            }
        } catch (Exception e) {
            System.out.println("The stream could not be read:");
            System.out.println(e.getMessage());
        }
        return code.toString();
    }

    private String readSource(String source) {
        StringBuilder code = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(source));
            while ((line = reader.readLine()) != null) {
                code.append(line);
                code.append('\n');
            }
            reader.close();
        }

        catch (Exception e) {
            System.out.println("The file could not be read:");
            System.out.println(e.getMessage());
        }
        return code.toString();
    }

    void compile() {
        _program_id = glCreateProgram();
        // compiling
        if (!_code_vertex.equals("")) {
            _shader_vertex = compileShader(ShaderType.VERTEX, _code_vertex);
            glAttachShader(_program_id, _shader_vertex);
        }

        if (!_code_fragment.equals("")) {
            _shader_fragment = compileShader(ShaderType.FRAGMENT, _code_fragment);
            glAttachShader(_program_id, _shader_fragment);
        }

        if (!_code_geometry.equals("")) {
            _shader_geometry = compileShader(ShaderType.GEOMETRY, _code_geometry);
            glAttachShader(_program_id, _shader_geometry);
        }

        if (!_code_compute.equals("")) {
            _shader_compute = compileShader(ShaderType.COMPUTE, _code_compute);
            glAttachShader(_program_id, _shader_compute);
        }
        // linking
        glLinkProgram(_program_id);

    }

    private int compileShader(ShaderType shader_type, String code) {
        int shader_id = glCreateShader(shader_type.getValue());
        glShaderSource(shader_id, code);
        glCompileShader(shader_id);
        return shader_id;
    }

    void useShader() {
        glUseProgram(_program_id);
    }

    void deleteShader() {
        detachShadersCode();
        deleteShadersID();
        glDeleteProgram(_program_id);
    }

    private void detachShadersCode() {
        glDetachShader(_program_id, _shader_vertex);
        glDetachShader(_program_id, _shader_fragment);
        glDetachShader(_program_id, _shader_geometry);
        glDetachShader(_program_id, _shader_compute);
    }

    private void deleteShadersID() {
        glDeleteShader(_shader_vertex);
        glDeleteShader(_shader_fragment);
        glDeleteShader(_shader_geometry);
        glDeleteShader(_shader_compute);
    }
}