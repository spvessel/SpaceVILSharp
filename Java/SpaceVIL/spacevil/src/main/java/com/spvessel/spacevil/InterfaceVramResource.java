package com.spvessel.spacevil;

/**
 * An interface for sealed SpaceVIL OpenGL environment.
 */
interface InterfaceVramResource {

    boolean sendUniformSample2D(Shader shader, String name);

    boolean sendUniform4f(Shader shader, String name, float[] array);

    boolean sendUniform1fv(Shader shader, String name, int count, float[] array);

    boolean sendUniform2fv(Shader shader, String name, float[] array);

    boolean sendUniform1f(Shader shader, String name, float array);

    boolean sendUniform1i(Shader shader, String name, int value);

    void draw();

    void clear();

    void bind();

    void unbind();
}