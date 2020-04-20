package com.spvessel.spacevil.View;

import java.util.List;
import java.awt.*;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.Core.InterfaceOpenGLLayer;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.*;
import java.nio.*;

import com.spvessel.spacevil.Common.*;
import com.spvessel.spacevil.Core.*;
import org.lwjgl.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;

public class OpenGLLayer extends Prototype implements InterfaceOpenGLLayer, InterfaceMovable, InterfaceDraggable {

    private static int _count = 0;

    public OpenGLLayer() {
        setItemName(this.getClass().getSimpleName() + _count++);

        // attr
        setBackground(75, 75, 75);
        setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
    }

    @Override
    public void initElements() {
        eventKeyPress.add((sender, args) -> {
            if (args.key.getValue() < KeyCode.RIGHT.getValue() || args.key.getValue() > KeyCode.UP.getValue())
                return;

            rotate(args.key);
        });

        eventMousePress.add((sender, args) -> {
            System.out.println("press");
            _drag = true;
            _ptr.setPosition(args.position.getX(), args.position.getY());
        });

        eventMouseMove.add((sender, args) -> {
            if (_drag) {
                System.out.println("move");
                float xRot = (float) (args.position.getX() - _ptr.getX()) / 2;
                _model = _model.rotate(radians(xRot), new Vec3(0.0f, 1.0f, 0.0f));

                float yRot = (float) (args.position.getY() - _ptr.getY()) / 2;
                _model = _model.rotate(radians(yRot), new Vec3(1.0f, 0.0f, 0.0f));

                _ptr.setPosition(args.position.getX(), args.position.getY());
            }
        });

        eventMouseDrag.add((sender, args) -> {
            System.out.println("drag");
        });
        
        eventMouseDrop.add((sender, args) -> {
            System.out.println("drop");
        });

        eventScrollUp.add((sender, args) -> {
            _zCamera -= 0.2f;
            if (_zCamera < 2)
                _zCamera = 2;
            setCameraLookAt(_xCamera, _yCamera, _zCamera);
        });

        eventScrollDown.add((sender, args) -> {
            _zCamera += 0.2f;
            setCameraLookAt(_xCamera, _yCamera, _zCamera);
        });

        eventMouseClick.add((sender, args) -> {
            System.out.println("release");
            _drag = false;
            // if (args.Button == MouseButton.ButtonRight)
            // {
            // MessageItem msg = new MessageItem("OK", "Message");
            // msg.Show(GetHandler());
            // }
        });
    }

    private boolean _drag = false;
    private Position _ptr = new Position();
    private int _shaderCommon = 0;
    private int _shaderLamp = 0;
    private int _shaderTexture = 0;
    private int _VBO;
    private int _cubeVAO;
    private int _lightVAO;
    private int _texture;
    private int _buffers;
    private int _FBO;
    private int _depthrenderbuffer;
    private int _VBOlenght = 0;
    private Color _color;
    private Mat4 _projection;
    private Mat4 _view;
    private Mat4 _model;
    private float _xCamera = 0;
    private float _yCamera = 0;
    private float _zCamera = 3;
    private float _aspectRatio = 1f;
    private boolean _isInit = false;
    private int _pixelingStrenght = 1;

    @Override
    public void free() {
        _isInit = false;
        glDeleteProgram(_shaderCommon);
        glDeleteProgram(_shaderLamp);
        glDeleteProgram(_shaderTexture);
        glDeleteBuffers(_VBO);
        glDeleteVertexArrays(_cubeVAO);
        glDeleteVertexArrays(_lightVAO);
    }

    @Override
    public void initialize() {
        glDisable(GL_CULL_FACE);
        _shaderCommon = createShaderProgram(getCommonVertexShaderCode(), getCommonFragmentShaderCode());
        _shaderLamp = createShaderProgram(getLampVertexShaderCode(), getLampFragmentShaderCode());
        _shaderTexture = createShaderProgram(getTextureVertexShaderCode(), getTextureFragmentShaderCode());

        genBuffers(get3DCubeVertex());
        _color = new Color(10, 162, 232);

        _projection = new Mat4().perspective(radians(45f), _aspectRatio, 0.1f, 100.0f);
        _model = new Mat4().identity();
        setCameraLookAt(_xCamera, _yCamera, _zCamera);

        _isInit = true;
    }

    @Override
    public boolean isInitialized() {
        return _isInit;
    }

    @Override
    public void draw() {
        genTexturedFBO();
        glViewport(0, 0, getWidth() / _pixelingStrenght, getHeight() / _pixelingStrenght);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        /////////////////
        glUseProgram(_shaderCommon);
        sendColorAsUniformVariable(Color.white, "lightColor");
        sendVec3AsUniformVariable(new float[] { 1.2f, 1.0f, 2.0f }, "lightPos");
        sendMVPAsUniformVariable(_shaderCommon);
        bindCubeBuffer();
        sendColorAsUniformVariable(_color, "objectColor");
        glDrawArrays(GL_TRIANGLES, 0, _VBOlenght);

        sendColorAsUniformVariable(Color.black, "objectColor");
        glDrawArrays(GL_LINE_STRIP, 0, _VBOlenght);

        glUseProgram(_shaderLamp);
        bindLampBuffer();
        glDrawArrays(GL_TRIANGLES, 0, _VBOlenght);
        /////////////////

        unbindFBO();
        RenderService.setGLLayerViewport(getHandler(), this);

        glUseProgram(_shaderTexture);
        genTextureBuffers();
        bindTexture();

        sendUniformSample2D(_shaderTexture, "tex");
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        glDeleteFramebuffersEXT(_FBO);
        glDeleteRenderbuffersEXT(_depthrenderbuffer);
        glDeleteTextures(_texture);
        glDeleteBuffers(_buffers);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    public void refresh() {
        if (!_isInit)
            return;

        _projection = new Mat4().perspective(radians(45f), _aspectRatio, 0.1f, 100.0f);
    }

    public void restoreView() {
        _xCamera = 0;
        _yCamera = 0;
        _zCamera = 3;
        _projection = new Mat4().perspective(radians(45f), _aspectRatio, 0.1f, 100.0f);
        _model = new Mat4().identity();
        setCameraLookAt(_xCamera, _yCamera, _zCamera);
    }

    public void setZoom(float value) {
        _zCamera = value;
        setCameraLookAt(_xCamera, _yCamera, _zCamera);
    }

    public void resize() {
        _aspectRatio = (float) getWidth() / getHeight();
        refresh();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        resize();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        resize();
    }

    public void rotate(KeyCode code) {
        if (code == KeyCode.LEFT) {
            _model = _model.rotate(radians(1), new Vec3(0.0f, 1.0f, 0.0f));
        }
        if (code == KeyCode.RIGHT) {
            _model = _model.rotate(radians(-1), new Vec3(0.0f, 1.0f, 0.0f));
        }
        if (code == KeyCode.UP) {
            _model = _model.rotate(radians(1), new Vec3(1.0f, 0.0f, 0.0f));
        }
        if (code == KeyCode.DOWN) {
            _model = _model.rotate(radians(-1), new Vec3(1.0f, 0.0f, 0.0f));
        }
    }

    private float radians(float value) {
        return value * (float) Math.PI / 180f;
    }

    private String getCommonVertexShaderCode() {
        return "#version 330 core\n" + "layout(location = 0) in vec3 vPosition;\n"
                + "layout(location = 1) in vec3 vNormal;\n" + "out vec3 FragPos;\n" + "out vec3 Normal;\n"
                + "uniform mat4 model;\n" + "uniform mat4 view;\n" + "uniform mat4 projection;\n" + "void main()\n"
                + "{\n" + "FragPos = vec3(model * vec4(vPosition, 1.0));\n" + "Normal = vNormal;\n"
                + "gl_Position = projection * view * vec4(FragPos, 1.0);\n" + "}\n";
    }

    private String getCommonFragmentShaderCode() {
        return "#version 330 core\n" + "in vec3 FragPos;\n" + "in vec3 Normal;\n" + "uniform vec3 lightPos;\n"
                + "uniform vec3 lightColor;\n" + "uniform vec3 objectColor;\n" + "out vec4 color;\n" + "void main()\n"
                + "{\n" + "float ambientStrength = 0.1;\n" + "vec3 ambient = ambientStrength * lightColor;\n"
                + "vec3 norm = normalize(Normal);\n" + "vec3 lightDir = normalize(lightPos - FragPos);\n"
                + "float diff = max(dot(norm, lightDir), 0.0);\n" + "vec3 diffuse = diff * lightColor;\n"
                + "vec3 result = (ambient + diffuse) * objectColor;\n" + "color = vec4(result, 1.0);\n" + "}\n";
    }

    private String getLampVertexShaderCode() {
        return "#version 330 core\n" + "layout(location = 0) in vec3 vPosition;\n" + "void main()\n" + "{\n"
                + "gl_Position = vec4(vPosition, 1.0f);\n" + "}\n";
    }

    private String getLampFragmentShaderCode() {
        return "#version 330 core\n" + "out vec4 color;\n" + "void main()\n" + "{\n" + "color = vec4(1.0);\n" + "}\n";
    }

    private String getTextureVertexShaderCode() {
        return "#version 330 core\n" + "layout (location = 0) in vec3 vert;\n"
                + "layout (location = 1) in vec2 verTexCoord;\n" + "out vec2 fragTexCoord;\n" + "void main()\n" + "{\n"
                + "fragTexCoord = verTexCoord;\n" + "gl_Position = vec4(vert, 1.0f);\n" + "}\n";
    }

    private String getTextureFragmentShaderCode() {
        return "#version 330 core\n" + "uniform sampler2D tex;\n" + "in vec2 fragTexCoord;\n" + "out vec4 color;\n"
                + "void main()\n" + "{\n" + "color = texture(tex, fragTexCoord);\n" + "}\n";
    }

    private int createShaderProgram(String vertexCode, String fragmentCode) {
        int vertex = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertex, vertexCode);
        glCompileShader(vertex);

        int fragment = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragment, fragmentCode);
        glCompileShader(fragment);

        int shader = glCreateProgram();
        glAttachShader(shader, vertex);
        glAttachShader(shader, fragment);
        glLinkProgram(shader);

        glDetachShader(shader, vertex);
        glDetachShader(shader, fragment);
        glDeleteShader(vertex);
        glDeleteShader(fragment);

        return shader;
    }

    private void genBuffers(List<float[]> vertices) {
        _VBOlenght = vertices.size();
        float[] vboData = new float[vertices.size() * 6];

        for (int i = 0; i < vertices.size(); i++) {
            int index = i * 6;
            vboData[index + 0] = vertices.get(i)[0];
            vboData[index + 1] = vertices.get(i)[1];
            vboData[index + 2] = vertices.get(i)[2];
            vboData[index + 3] = vertices.get(i)[3];
            vboData[index + 4] = vertices.get(i)[4];
            vboData[index + 5] = vertices.get(i)[5];
        }

        _cubeVAO = glGenVertexArrays();
        _VBO = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, _VBO);
        glBufferData(GL_ARRAY_BUFFER, vboData, GL_STATIC_DRAW);

        glBindVertexArray(_cubeVAO);

        // position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * 4, 0);
        glEnableVertexAttribArray(0);
        // normal attribute
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * 4, 0 + (3 * 4));
        glEnableVertexAttribArray(1);

        _lightVAO = glGenVertexArrays();
        glBindVertexArray(_lightVAO);

        glBindBuffer(GL_ARRAY_BUFFER, _VBO);
        // note that we update the lamp's position attribute's stride to reflect the
        // updated buffer data
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * 4, 0);
        glEnableVertexAttribArray(0);
    }

    private void genTextureBuffers() {
        float[] vboData = new float[] {
                // X Y U V
                -1f, 1f, 0f, 0.0f, 1.0f, // x0
                -1f, -1, 0f, 0.0f, 0.0f, // x1
                1f, -1f, 0f, 1.0f, 0.0f, // x2
                1f, 1f, 0f, 1.0f, 1.0f, // x3
        };

        int[] iboData = new int[] { 0, 1, 2, // first triangle
                2, 3, 0, // second triangle
        };

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vboData, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0);
        glEnableVertexAttribArray(0);

        int ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboData, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, true, 5 * 4, (3 * 4));
        glEnableVertexAttribArray(1);
    }

    private void genTexturedFBO() {
        // fbo
        _FBO = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, _FBO);

        // texture
        _texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, _texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, getWidth() / _pixelingStrenght, getHeight() / _pixelingStrenght, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, _texture, 0);

        _depthrenderbuffer = glGenRenderbuffersEXT();
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, _depthrenderbuffer);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT, getWidth() / _pixelingStrenght,
                getHeight() / _pixelingStrenght);
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, 0);

        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT,
                _depthrenderbuffer);

        if (glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT) != GL_FRAMEBUFFER_COMPLETE_EXT)
            System.out.println("fbo fail!");
    }

    private void bindCubeBuffer() {
        glBindVertexArray(_cubeVAO);
    }

    private void bindLampBuffer() {
        glBindVertexArray(_lightVAO);
    }

    private void bindTexture() {
        glBindTexture(GL_TEXTURE_2D, _texture);
    }

    private void unbindFBO() {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    private void sendColorAsUniformVariable(Color fill, String name) {
        float[] argb = { (float) fill.getRed() / 255.0f, (float) fill.getGreen() / 255.0f,
                (float) fill.getBlue() / 255.0f };
        int location = glGetUniformLocation(_shaderCommon, name);
        if (location < 0)
            System.out.println("Uniform <" + name + "> not found.");
        glUniform3f(location, argb[0], argb[1], argb[2]);
    }

    private void sendVec3AsUniformVariable(float[] value, String name) {
        int location = glGetUniformLocation(_shaderCommon, name);
        if (location < 0)
            System.out.println("Uniform <" + name + "> not found.");
        glUniform3f(location, value[0], value[1], value[2]);
    }

    private void sendMVPAsUniformVariable(int shader) {
        int location = glGetUniformLocation(shader, "model");
        if (location < 0)
            System.out.println("Uniform <model> not found.");
        glUniformMatrix4fv(location, false, toFloatBuffer(_model));

        location = glGetUniformLocation(shader, "view");
        if (location < 0)
            System.out.println("Uniform <view> not found.");
        glUniformMatrix4fv(location, false, toFloatBuffer(_view));

        location = glGetUniformLocation(shader, "projection");
        if (location < 0)
            System.out.println("Uniform <projection> not found.");
        glUniformMatrix4fv(location, false, toFloatBuffer(_projection));
    }

    private void sendUniformSample2D(int shader, String name) {
        int location = glGetUniformLocation(shader, name);
        if (location < 0)
            System.out.println("Uniform <" + name + "> not found.");

        glUniform1i(location, 0);
    }

    private void setCameraLookAt(float x, float y, float z) {
        _view = new Mat4().lookAt(new Vec3(x, y, z), new Vec3(0f, 0f, 0f), new Vec3(0f, 1f, 0f));
    }

    private List<float[]> get3DCubeVertex() {
        return new LinkedList<float[]>(Arrays.asList(new float[] { -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f },
                new float[] { 0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f },
                new float[] { 0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f },
                new float[] { 0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f },
                new float[] { -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f },
                new float[] { -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f },

                new float[] { -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f },
                new float[] { 0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f }, new float[] { 0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f },
                new float[] { 0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f }, new float[] { -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f },
                new float[] { -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f },

                new float[] { -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f },
                new float[] { -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f },
                new float[] { -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f },
                new float[] { -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f },
                new float[] { -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f },
                new float[] { -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f },

                new float[] { 0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f }, new float[] { 0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f },
                new float[] { 0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f },
                new float[] { 0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f },
                new float[] { 0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f }, new float[] { 0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f },

                new float[] { -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f },
                new float[] { 0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f },
                new float[] { 0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f },
                new float[] { 0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f },
                new float[] { -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f },
                new float[] { -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f },

                new float[] { -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f },
                new float[] { 0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f }, new float[] { 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f },
                new float[] { 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f }, new float[] { -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f },
                new float[] { -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f }));
    }

    private FloatBuffer toFloatBuffer(Mat4 mat) {
        FloatBuffer bb = BufferUtils.createFloatBuffer(4 * 4);
        float[] array = mat.toFa_();
        bb.put(array);
        bb.rewind();
        return bb;
    }
}