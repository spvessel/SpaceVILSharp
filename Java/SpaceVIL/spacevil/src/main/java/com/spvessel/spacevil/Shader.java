package com.spvessel.spacevil;

import static org.lwjgl.opengl.GL20.*;

import java.io.FileReader;
import java.io.BufferedReader;

final class Shader {
    private final int VERTEX = 0x8B31;
    private final int FRAGMENT = 0x8B30;
    private final int GEOMETRY = 0x8DD9;
    private final int COMPUTE = 0x91B9;


    private int _programId;

    long getProgramID() {
        return _programId;
    }

    private int _shaderVertex;
    private String _codeVertex;

    private int _shaderFragment;
    private String _codeFragment;

    private int _shaderGeometry;
    private String _codeGeometry;

    private int _shaderCompute;
    private String _codeCompute;

    String getCode(int shaderType) {
        switch (shaderType) {
        case VERTEX:
            return _codeVertex;

        case FRAGMENT:
            return _codeFragment;

        case GEOMETRY:
            return _codeGeometry;

        case COMPUTE:
            return _codeCompute;

        default:
            break;
        }
        return "";
    }

    void setShaderPath(int shaderType, String path) {
        String code = readSource(path);

        switch (shaderType) {
        case VERTEX:
            _codeVertex = code.toString();
            break;

        case FRAGMENT:
            _codeFragment = code.toString();
            break;

        case GEOMETRY:
            _codeGeometry = code.toString();
            break;

        case COMPUTE:
            _codeCompute = code.toString();
            break;

        default:
            break;
        }
    }

    Shader() {
        initDefaults();
    }

    Shader(String vertexCode, String fragmendCode) {
        _codeVertex = vertexCode;
        _codeFragment = fragmendCode;

        // defaults
        _codeGeometry = "";
        _codeCompute = "";
    }

    Shader(BufferedReader vertexCode, BufferedReader fragmendCode) {
        _codeVertex = readSource(vertexCode);
        _codeFragment = readSource(fragmendCode);

        // defaults
        _codeGeometry = "";
        _codeCompute = "";
    }

    private void initDefaults() {
        _codeVertex = "";
        _codeFragment = "";
        _codeGeometry = "";
        _codeCompute = "";
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
        _programId = glCreateProgram();
        // compiling
        if (!_codeVertex.equals("")) {
            _shaderVertex = compileShader(VERTEX, _codeVertex);
            glAttachShader(_programId, _shaderVertex);
        }

        if (!_codeFragment.equals("")) {
            _shaderFragment = compileShader(FRAGMENT, _codeFragment);
            glAttachShader(_programId, _shaderFragment);
        }

        if (!_codeGeometry.equals("")) {
            _shaderGeometry = compileShader(GEOMETRY, _codeGeometry);
            glAttachShader(_programId, _shaderGeometry);
        }

        if (!_codeCompute.equals("")) {
            _shaderCompute = compileShader(COMPUTE, _codeCompute);
            glAttachShader(_programId, _shaderCompute);
        }
        // linking
        glLinkProgram(_programId);

    }

    private int compileShader(int shaderType, String code) {
        int shaderId = glCreateShader(shaderType);
        glShaderSource(shaderId, code);
        glCompileShader(shaderId);
        return shaderId;
    }

    void useShader() {
        glUseProgram(_programId);
    }

    void deleteShader() {
        detachShaders();
        deleteShadersID();
        glDeleteProgram(_programId);
    }

    private void detachShaders() {
        glDetachShader(_programId, _shaderVertex);
        glDetachShader(_programId, _shaderFragment);
        glDetachShader(_programId, _shaderGeometry);
        glDetachShader(_programId, _shaderCompute);
    }

    private void deleteShadersID() {
        glDeleteShader(_shaderVertex);
        glDeleteShader(_shaderFragment);
        glDeleteShader(_shaderGeometry);
        glDeleteShader(_shaderCompute);
    }
}