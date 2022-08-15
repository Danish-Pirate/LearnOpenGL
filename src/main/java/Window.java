import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final String title;
    private int height;
    private int width;
    private static Window window;
    private long windowID;
    private Level level;

    private Window() {
        level = new Level();
        title = "OpenGL";
        width = 1280;
        height = 720;
    }

    public void run() {
        init();
        loop();
        cleanup();
    }

    public static Window getWindow() {
        if (window == null)
            window = new Window();
        return window;
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("GLFW could not be initialized");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        windowID = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowID == NULL)
            throw new IllegalStateException("Could not create GLFW window");

        glfwSetCursorPosCallback(windowID, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(windowID, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(windowID, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(windowID, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(windowID, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        glfwMakeContextCurrent(windowID);
        glfwSwapInterval(1);
        glfwShowWindow(windowID);
        GL.createCapabilities();

        //glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        level.init();

    }

    private void loop() {
        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(windowID)) {
            glfwPollEvents();
            glClearColor(1, 1, 1, 1);
            glClear(GL_COLOR_BUFFER_BIT );
            glClear(GL_DEPTH_BUFFER_BIT);
            level.update();
            glfwSwapBuffers(windowID);

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    private void cleanup() {
        glfwFreeCallbacks(windowID);
        glfwDestroyWindow(windowID);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void setWidth(int newWidth) {
        getWindow().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        getWindow().height = newHeight;
    }

    public static int getHeight() {
        return getWindow().height;
    }

    public static int getWidth() {
        return getWindow().width;
    }
}

