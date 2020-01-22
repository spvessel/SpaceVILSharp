package com.spvessel.spacevil.Common;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
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
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryUtil;

public final class CommonService {

    private CommonService() {
    }

    private static String _version = "0.3.1-ALPHA - January 2019";

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
        // System.setProperty("org.lwjgl.system.stackSize", Integer.toString(Integer.MAX_VALUE));
        // org.lwjgl.system.Configuration.STACK_SIZE = new Configuration<Integer>("org.lwjgl.system.stackSize", Integer.MAX_VALUE);

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
        System.out.println(DisplayService.getDisplayDpiScale());
        
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

    public static String getClipboardString() {
        CoreWindow window = WindowsBox.getCurrentFocusedWindow();
        if (window == null)
            return "";
        long id = window.getGLWID();
        if (id == NULL)
            return "";
        return glfwGetClipboardString(id);
    }

    public static void setClipboardString(String text) {
        CoreWindow window = WindowsBox.getCurrentFocusedWindow();
        if (window == null)
            return;
        long id = window.getGLWID();
        if (id == NULL)
            return;
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

    public static KeyCode getOsControlKeyRight() {
        return _controlRight;
    }

    public static KeyCode getOsControlKeyLeft() {
        return _controlLeft;
    }

    public static KeyMods getOsControlMod() {
        return _controlMod;
    }
}