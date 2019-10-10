package com.spvessel.spacevil;

import static org.lwjgl.opengl.GL20.*;

abstract class AbstractVramResource implements InterfaceVramResource {
    public AbstractVramResource() {
    }

    int getLocation(Shader shader, String name) {
        return glGetUniformLocation((int) shader.getProgramID(), name);
    }

    boolean printError(Shader shader, String name) {
        System.out.println("Uniform not found: <" + name + "> ");
        return false;
    }

    @Override
    public boolean sendUniformSample2D(Shader shader, String name) {
        int location = getLocation(shader, name);
        if (location < 0)
            return printError(shader, name);
        glUniform1i(location, 0);
        return true;
    }

    @Override
    public boolean sendUniform4f(Shader shader, String name, float[] array) {
        int location = getLocation(shader, name);
        if (location < 0)
            return printError(shader, name);
        glUniform4f(location, array[0], array[1], array[2], array[3]);
        return true;
    }

    @Override
    public boolean sendUniform1fv(Shader shader, String name, int count, float[] array) {
        int location = getLocation(shader, name);
        if (location < 0)
            return printError(shader, name);
        glUniform1fv(location, array);
        return true;
    }

    @Override
    public boolean sendUniform2fv(Shader shader, String name, float[] array) {
        int location = getLocation(shader, name);
        if (location < 0)
            return printError(shader, name);
        glUniform2fv(location, array);
        return true;
    }

    @Override
    public boolean sendUniform1f(Shader shader, String name, float array) {
        int location = getLocation(shader, name);
        if (location < 0)
            return printError(shader, name);
        glUniform1f(location, array);
        return true;
    }

    @Override
    public boolean sendUniform1i(Shader shader, String name, int value) {
        int location = getLocation(shader, name);
        if (location < 0)
            return printError(shader, name);
        glUniform1i(location, value);
        return true;
    }

    public abstract void draw();

    public abstract void clear();

    public abstract void bind();

    public abstract void unbind();
}