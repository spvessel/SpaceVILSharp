package com.spvessel.spacevil.Common;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static org.lwjgl.glfw.GLFW.*;

import com.spvessel.spacevil.Flags.OSType;

public final class CommonService {

    private CommonService() {
    }

    public static String ClipboardTextStorage = "";
    public final static Lock GlobalLocker = new ReentrantLock();

    // os type
    private static OSType _os_type = OSType.WINDOWS;

    public static OSType getOSType() {
        return _os_type;
    }

    // cursors
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

        // cursors
        cursorArrow = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        cursorInput = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        cursorHand = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        cursorResizeH = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        cursorResizeV = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        cursorResizeAll = glfwCreateStandardCursor(GLFW_CROSSHAIR_CURSOR);

        return true;
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
}