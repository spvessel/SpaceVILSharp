package com.spvessel.spacevil.Common;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.spvessel.spacevil.CoreWindow;
import com.spvessel.spacevil.CursorImage;
import com.spvessel.spacevil.Common.DisplayService;
import com.spvessel.spacevil.WindowsBox;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.OSType;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;

/**
 * DisplayService is static class providing methods to getting basic information 
 * about the SpaceVIL framework, OS attributes, 
 * initializing the SpaceVIL framework and working with clipboard.
 */
public final class CommonService {

    private CommonService() {
    }

    private static String _version = "0.3.1-ALPHA - January 2019";

    /**
     * Getting basic information about SpaceVIL such as version, date, platform and OS.
     * @return The information as java.lang.String.
     */
    public static String getSpaceVILInfo() {

        String info = "SpaceVIL version: " + _version + "\n" + "Platform: JVM \n" + "OS type: "
                + ((getOSType() == OSType.WINDOWS) ? "Windows" : (getOSType() == OSType.MAC) ? "Mac OS X" : "Linux")
                + "\n";

        return info;
    }

    private static String ClipboardTextStorage = "";
    public final static Lock GlobalLocker = new ReentrantLock();

    // os type
    private static OSType _os_type = OSType.WINDOWS;

    public static OSType getOSType() {
        return _os_type;
    }

    // cursors
    public static CursorImage currentCursor = null;
    public static long cursorArrow;
    public static long cursorInput;
    public static long cursorHand;
    public static long cursorResizeH;
    public static long cursorResizeV;
    public static long cursorResizeAll;

    /**
     * Initializing the mandatory SpaceVIL common components (GLFW, default values and etc.).
     * @return True: if initialization is successful. False: if initialization is unsuccessful.
     */
    public static boolean initSpaceVILComponents() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (isWindows(OS)) {
            _os_type = OSType.WINDOWS;
        } else if (isMac(OS)) {
            _os_type = OSType.MAC;
            _controlRight = KeyCode.RIGHTSUPER;
            _controlLeft = KeyCode.LEFTSUPER;
            _controlMod = KeyMods.SUPER;
        } else if (isUnix(OS)) {
            _os_type = OSType.LINUX;
        } else {
            System.out.println("Your Operating System is not support. Abort.");
            return false;
        }

        if (!glfwInit()) {
            System.out.println("Init SpaceVIL Framework fail. Abort.");
            return false;
        }

        System.setProperty("java.awt.headless", "true");
        System.setProperty("org.lwjgl.opengl.Display.enableHighDPI", "false");

        // cursors
        cursorArrow = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        cursorInput = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        cursorHand = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        cursorResizeH = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        cursorResizeV = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        cursorResizeAll = glfwCreateStandardCursor(GLFW_CROSSHAIR_CURSOR);

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        DisplayService.setDisplaySize(vidmode.width(), vidmode.height());

        long monitor = glfwGetPrimaryMonitor();
        FloatBuffer x = BufferUtils.createFloatBuffer(1);
        FloatBuffer y = BufferUtils.createFloatBuffer(1);
        glfwGetMonitorContentScale(monitor, x, y);
        DisplayService.setDisplayScale(x.get(0), y.get(0));

        // System.out.println(DisplayService.getDisplayDpiScale());
        
        DefaultsService.initImages();
        DefaultsService.initDefaultTheme();

        // runCG();

        return true;
    }

    private static void runCG(int ms) {
        Thread freeMemoryThread = new Thread(() -> {
            Runtime rt = Runtime.getRuntime();
            while (true) {
                try {
                    Thread.sleep(ms);
                } catch (Exception e) {
                }
                rt.gc();
            }
        });
        freeMemoryThread.setDaemon(true);
        freeMemoryThread.start();
    }

    private static boolean isWindows(String OS) {
        return (OS.indexOf("win") >= 0);
    }

    private static boolean isMac(String OS) {
        return (OS.indexOf("mac") >= 0);
    }

    private static boolean isUnix(String OS) {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
    }

    /**
     * Getting stored text in clipboard.
     * @return The text as java.lang.String.
     */
    public static String getClipboardString() {
        CoreWindow window = WindowsBox.getCurrentFocusedWindow();
        if (window == null) {
            return "";
        }
        long id = window.getGLWID();
        if (id == NULL) {
            return "";
        }
        return glfwGetClipboardString(id);
    }

    /**
     * Placing the specified text to the clipboard.
     * @param text The text as java.lang.String
     */
    public static void setClipboardString(String text) {
        CoreWindow window = WindowsBox.getCurrentFocusedWindow();
        if (window == null) {
            return;
        }
        long id = window.getGLWID();
        if (id == NULL) {
            return;
        }
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        ByteBuffer bb = BufferUtils.createByteBuffer(bytes.length + 1);
        bb.put(bytes);
        bb.put(bytes.length, (byte) 0);
        bb.rewind();
        glfwSetClipboardString(id, bb);
    }

    private static KeyCode _controlRight = KeyCode.RIGHTCONTROL;
    private static KeyCode _controlLeft = KeyCode.LEFTCONTROL;
    private static KeyMods _controlMod = KeyMods.CONTROL;

    /**
     * Getting the default right "control" key KeyCode of current OS (for Mac OS - Command key)
     * @return The keyboard key as com.spvessel.spacevil.Flags.KeyCode
     */
    public static KeyCode getOsControlKeyRight() {
        return _controlRight;
    }

    /**
     * Getting the default left "control" key KeyCode of current OS (for Mac OS - Command key)
     * @return The keyboard key as com.spvessel.spacevil.Flags.KeyCode
     */
    public static KeyCode getOsControlKeyLeft() {
        return _controlLeft;
    }

    /**
     * Getting the default "control" modifier KeyCode of current OS (for Mac OS - Command key)
     * @return The keyboard modifier as com.spvessel.spacevil.Flags.KeyCode
     */
    public static KeyMods getOsControlMod() {
        return _controlMod;
    }
}