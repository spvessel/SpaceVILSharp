package com.spvessel.spacevil.Common;

import com.spvessel.spacevil.CoreWindow;
import com.spvessel.spacevil.CursorImage;
import com.spvessel.spacevil.WindowsBox;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.OSType;
import com.spvessel.spacevil.internal.Wrapper.GlfwWrapper;
import com.spvessel.spacevil.internal.Wrapper.GLFWVidMode;

/**
 * DisplayService is static class providing methods to getting basic information
 * about the SpaceVIL framework, OS attributes, initializing the SpaceVIL
 * framework and working with clipboard.
 */
public final class CommonService {

    private static GlfwWrapper glfw = null;

    private CommonService() {
    }

    private static String _version = "0.3.1-ALPHA - January 2019";

    /**
     * Getting basic information about SpaceVIL such as version, date, platform and
     * OS.
     * 
     * @return The information as java.lang.String.
     */
    public static String getSpaceVILInfo() {

        String info = "SpaceVIL version: " + _version + "\n" + "Platform: JVM \n" + "OS type: "
                + ((getOSType() == OSType.Windows) ? "Windows" : (getOSType() == OSType.Mac) ? "Mac OS X" : "Linux")
                + "\n";

        return info;
    }

    // os type
    private static OSType _osType = OSType.Windows;

    public static OSType getOSType() {
        return _osType;
    }

    // cursors
    public static CursorImage currentCursor = null;

    /**
     * Initializing the mandatory SpaceVIL common components (GLFW, default values
     * and etc.).
     * 
     * @return True: if initialization is successful. False: if initialization is
     *         unsuccessful.
     */
    public static boolean initSpaceVILComponents() {
        System.setProperty("java.awt.headless", "true");

        String OS = System.getProperty("os.name").toLowerCase();
        if (isWindows(OS)) {
            _osType = OSType.Windows;
        } else if (isMac(OS)) {
            _osType = OSType.Mac;
            _controlRight = KeyCode.RightSuper;
            _controlLeft = KeyCode.LeftSuper;
            _controlMod = KeyMods.Super;
        } else if (isUnix(OS)) {
            _osType = OSType.Linux;
        } else {
            System.out.println("Your Operating System is not support. Abort.");
            return false;
        }

        NativeLibrary.extractEmbeddedLibrary();

        glfw = GlfwWrapper.get();

        try {
            if (glfw.Init() == 0) {
                System.out.println("Init SpaceVIL Framework fail. Abort.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Can not load/find library: "
                    + ((getOSType() == OSType.Linux || getOSType() == OSType.Mac) ? "lib" : "") + "glfw"
                    + ((getOSType() == OSType.Linux) ? ".so" : ((getOSType() == OSType.Mac) ? ".dylib" : ".dll"))
                    + ". Failed to init GLFW. Abort function.");
            return false;
        }

        long monitor = glfw.GetPrimaryMonitor();
        GLFWVidMode vidmode = glfw.GetVideoMode(monitor);
        DisplayService.setDisplaySize(vidmode.width, vidmode.height);

        float[] contentScale = glfw.GetMonitorContentScale(monitor);
        DisplayService.setDisplayScale(contentScale[0], contentScale[1]);

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
     * 
     * @return The text as java.lang.String.
     */
    public static String getClipboardString() {
        try {
            CoreWindow window = WindowsBox.getCurrentFocusedWindow();
            if (window == null) {
                return null;
            }
            long id = window.getGLWID();
            if (id == 0) {
                return null;
            }
            return glfw.GetClipboardString(id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Placing the specified text to the clipboard.
     * 
     * @param text The text as java.lang.String
     */
    public static void setClipboardString(String text) {
        CoreWindow window = WindowsBox.getCurrentFocusedWindow();
        if (window == null) {
            return;
        }
        long id = window.getGLWID();
        if (id == 0) {
            return;
        }
        glfw.SetClipboardString(id, text);
    }

    private static KeyCode _controlRight = KeyCode.RightControl;
    private static KeyCode _controlLeft = KeyCode.LeftControl;
    private static KeyMods _controlMod = KeyMods.Control;

    /**
     * Getting the default right "control" key KeyCode of current OS (for Mac OS -
     * Command key)
     * 
     * @return The keyboard key as com.spvessel.spacevil.Flags.KeyCode
     */
    public static KeyCode getOsControlKeyRight() {
        return _controlRight;
    }

    /**
     * Getting the default left "control" key KeyCode of current OS (for Mac OS -
     * Command key)
     * 
     * @return The keyboard key as com.spvessel.spacevil.Flags.KeyCode
     */
    public static KeyCode getOsControlKeyLeft() {
        return _controlLeft;
    }

    /**
     * Getting the default "control" modifier KeyCode of current OS (for Mac OS -
     * Command key)
     * 
     * @return The keyboard modifier as com.spvessel.spacevil.Flags.KeyCode
     */
    public static KeyMods getOsControlMod() {
        return _controlMod;
    }
}