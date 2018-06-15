package CommonVesselGUI;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class DrawEngine {


    ////////////////////////////
    GLFWWindowSizeCallback resizeCallback;
    GLFWCursorPosCallback mouseMoveCallback;
    GLFWMouseButtonCallback mouseClick;
    GLFWWindowCloseCallback closeWindowCallback;
    ///////////////////////////

    private VisualItem HoveredItem;
    private Pointer ptrPress = new Pointer();
    private Pointer ptrRelease = new Pointer();

    public WindowLayout wnd;
    private int window;
    private int gVAO = 0;
    private int programPrimitives;
    private List<String> vertexShaders = new ArrayList<String>();
    private List<String> fragmentShaders = new ArrayList<String>();

    public DrawEngine(WindowLayout handler, String title, int msaa, int w, int h) {

        wnd = handler;
        createWindow(title, msaa, w, h);
        setEventsCallbacks();

        glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        createShaderProgram("shaders/vs_fill.glsl", "shaders/fs_fill.glsl");
        if (programPrimitives == 0)
            System.out.println("Could not create the fill shaders");

        //устанавливаем параметры отрисовки
        glEnable(GL_BLEND);
        //glEnable(GL_CULL_FACE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_ALPHA_TEST);
    }

    private void setEventsCallbacks() {

        glfwSetWindowCloseCallback(window, closeWindowCallback = new GLFWWindowCloseCallback(){
           @Override
           public void invoke(long window) {
             glfwSetWindowShouldClose(window, true);
            }
        });
        //resize event

        glfwSetWindowSizeCallback(window, resizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                //refactor needed
                if (width <= wnd.getMinimumWidth())
                    width = wnd.getMinimumWidth();
                wnd.setWidth(width);
                if (height <= wnd.getMinimumHeight())
                    height = wnd.getMinimumHeight();
                wnd.setHeight(height);
                glfwSetWindowSize(window, wnd.getWidth(), wnd.getHeight());
                glViewport(0, 0, wnd.getWidth(), wnd.getHeight());
                render();
            }
        });

        glfwSetCursorPosCallback(window, mouseMoveCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {

                ptrRelease.x = (int) xpos;
                ptrRelease.y = (int) ypos;

                HoveredItem = GetHoverVisualItem(ptrRelease.x, ptrRelease.y);

                ptrPress.x = ptrRelease.x;
                ptrPress.y = ptrRelease.y;
            }
        });

        glfwSetMouseButtonCallback(window, mouseClick = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                switch (action) {
                    case GLFW_PRESS:
                        break;
                    case GLFW_RELEASE: {
                        if (HoveredItem != null) {
                            HoveredItem.invokePoolEvents();
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }

    private VisualItem GetHoverVisualItem(int xpos, int ypos) {
        int index = -1;

        for (BaseItem item : ItemsLayoutBox.getLayoutItems(wnd.getId())) {
            if (item instanceof VisualItem) {
                if (((VisualItem) item).getHoverVerification(xpos, ypos))
                    index = ItemsLayoutBox.getLayoutItems(wnd.getId()).indexOf(item);
            }
        }

        if (index != -1)
            return (VisualItem) ItemsLayoutBox.getLayoutItems(wnd.getId()).get(index);
        else
            return null;
    }

    public void createWindow(String title, int msaa, int w, int h) {

        if (!glfwInit()) {
            System.out.println("Can not init GLFW");
        }
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        window = (int) glfwCreateWindow(w, h, title, NULL, NULL);

        if (window == NULL) {
            System.out.println("GLFW window can not be initialized.");
            glfwTerminate();
        }
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

    }

    private void createShaderProgram(String vs, String fs) {

        String v_code = readFileAsString(vs);
        System.out.println(v_code.toString());
        String f_code = readFileAsString(fs);
        System.out.println(f_code.toString());

        programPrimitives = glCreateProgram();
        int vertex = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertex, v_code);
        glAttachShader(programPrimitives, vertex);

        int fragment = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragment, f_code);
        glAttachShader(programPrimitives, fragment);

        // now link the program
        glLinkProgram(programPrimitives);

        if (programPrimitives == 0) {
            System.out.println("program shader fail");
            return;
        }
    }

    private String readFileAsString(String filename) {
        StringBuilder code = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while ((line = reader.readLine()) != null) {
                code.append(line);
                code.append('\n');
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("unable to load shader from file [" + filename + "]", e);
        }

        return code.toString();
    }

    public void run() {

        mainLoop();
    }

    private void mainLoop() {
        gVAO = glGenVertexArrays();
        glBindVertexArray(gVAO);

        glUseProgram(programPrimitives);

        while (!glfwWindowShouldClose(window)) {
            //TODO this is important!
            glfwPollEvents();
            render();
        }
        System.out.println("close");
        glDeleteVertexArrays(gVAO);
        glDeleteProgram(programPrimitives);
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        //glfwTerminate();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT);

        for (BaseItem item : ItemsLayoutBox.getLayoutItems(wnd.getId())) {
            if (item instanceof ItemText) {
                glDisable(GL13.GL_MULTISAMPLE);
                draText(item);
                glEnable(GL13.GL_MULTISAMPLE);
            }
            else drawShell(item);
        }

        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    void draText(BaseItem item) {
        int vertexbuffer = glGenBuffers();

        float[] data = ((ItemText)item).getCoordinates();
        float[] colorData = ((ItemText)item).getColors();

        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
        glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        int colorbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorbuffer);
        glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

        // draw
        glDrawArrays(GL_POINTS, 0, data.length / 3);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Clear VBO and shader
        glDeleteBuffers(vertexbuffer);
        glDeleteBuffers(colorbuffer);
    }

    void drawShell(BaseItem shell) {
        //Vertex
        List<float[]> crd_array = shell.makeTriangles();

        float vertexData[] = new float[crd_array.size() * 3];
        for (int i = 0; i < vertexData.length / 3; i++) {
            vertexData[i * 3 + 0] = crd_array.get(i)[0];
            vertexData[i * 3 + 1] = crd_array.get(i)[1];
            vertexData[i * 3 + 2] = crd_array.get(i)[2];
        }
        int vertexbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
        glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        //Color
        float[] argb = {
                (float)shell.getBackground().getRed()/255.0f,
                (float)shell.getBackground().getGreen()/255.0f,
                (float)shell.getBackground().getBlue()/255.0f,
                (float)shell.getBackground().getAlpha()/255.0f};

        float colorData[] = new float[crd_array.size() * 4];
        for (int i = 0; i < colorData.length / 4; i++) {
            colorData[i * 4 + 0] = argb[0];
            colorData[i * 4 + 1] = argb[1];
            colorData[i * 4 + 2] = argb[2];
            colorData[i * 4 + 3] = argb[3];
        }
        int colorbuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorbuffer);
        glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

        // draw
        glDrawArrays(GL_TRIANGLES, 0, crd_array.size());

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Clear VBO and shader
        glDeleteBuffers(vertexbuffer);
        glDeleteBuffers(colorbuffer);

        //clear array
        crd_array.clear();
    }
}

class Pointer {
    public int x = 0;
    public int y = 0;
}
