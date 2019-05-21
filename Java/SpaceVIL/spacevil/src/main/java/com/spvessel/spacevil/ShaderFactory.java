package com.spvessel.spacevil;

final class ShaderFactory {

    final static int PRIMITIVE = 1;
    final static int TEXTURE = 2;
    final static int SYMBOL = 4;
    final static int BLUR = 8;

    static Shader getShader(int type) {
        if (type == PRIMITIVE) {
            return createShader(CommonProcessor.getResourceString("/shaders/vs_primitive.glsl"),
                    CommonProcessor.getResourceString("/shaders/fs_primitive.glsl"), "_primitive");
        } else if (type == TEXTURE) {
            return createShader(CommonProcessor.getResourceString("/shaders/vs_texture.glsl"),
                    CommonProcessor.getResourceString("/shaders/fs_texture.glsl"), "_texture");
        } else if (type == SYMBOL) {
            return createShader(CommonProcessor.getResourceString("/shaders/vs_char.glsl"),
                    CommonProcessor.getResourceString("/shaders/fs_char.glsl"), "_char");
        } else if (type == BLUR) {
            return createShader(CommonProcessor.getResourceString("/shaders/vs_blur.glsl"),
                    CommonProcessor.getResourceString("/shaders/fs_blur.glsl"), "_blur");
        }
        return null;
    }

    private static Shader createShader(String vertexCode, String fragmentCode, String designation) {
        Shader shader = new Shader(vertexCode, fragmentCode);
        shader.compile();
        if (shader.getProgramID() == 0)
            System.out.println("Could not create " + designation + " shaders");
        return shader;
    }
}