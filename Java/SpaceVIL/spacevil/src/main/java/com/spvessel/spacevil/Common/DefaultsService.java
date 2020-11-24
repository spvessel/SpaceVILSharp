package com.spvessel.spacevil.Common;

import com.spvessel.spacevil.CursorImage;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Decorations.ThemeStyle;
import com.spvessel.spacevil.Flags.EmbeddedCursor;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.OSType;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * DefaultsService is static class providing methods to getting SpaceVIL default
 * common values such as font, mouse cursor, icon images.
 */
public final class DefaultsService {
    private DefaultsService() {
        // _default_font = DefaultFont.getInstance();
    }

    static void initDefaultTheme() {
        _default_theme = new ThemeStyle();
    }

    private static Map<EmbeddedImage, BufferedImage> images_32 = new HashMap<>();

    private static Map<EmbeddedImage, BufferedImage> images_64 = new HashMap<>();

    private static CursorImage _defaultCursor = new CursorImage(EmbeddedCursor.Arrow);

    private static ThemeStyle _default_theme; // = ThemeStyle.GetInstance();

    private static DefaultFont _default_font = DefaultFont.getInstance();;

    /**
     * Setting default cursor image the current application.
     * 
     * @param cursor The mouse cursor image as com.spvessel.spacevil.CursorImage
     */
    public static void setDefaultCursor(final CursorImage cursor) {
        if (cursor == null) {
            return;
        }
        _defaultCursor = cursor;
    }

    /**
     * Getting the current default mouse cursor image.
     * 
     * @return The mouse cursor image as com.spvessel.spacevil.CursorImage
     */
    public static CursorImage getDefaultCursor() {
        return _defaultCursor;
    }

    /**
     * Getting the default theme.
     * 
     * @return The theme as The theme as
     *         com.spvessel.spacevil.Decorations.ThemeStyle
     */
    public static ThemeStyle getDefaultTheme() {
        if (_default_theme == null) {
            _default_theme = new ThemeStyle();
        }
        return _default_theme;
    }

    /**
     * Setting the default theme for the current application.
     * 
     * @param theme The theme as com.spvessel.spacevil.Decorations.ThemeStyle
     */
    public static void setDefaultTheme(final ThemeStyle theme) {
        _default_theme = theme;
    }

    /**
     * Getting the default item style from the current default theme by its type.
     * 
     * @param type Item type as java.lang.Class&lt;?&gt;.
     *             <p>
     *             Example: typeof(com.spvessel.spacevil.ButtonCore)
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getDefaultStyle(final Class<?> type) {
        if (_default_theme == null) {
            _default_theme = new ThemeStyle();
        }
        return _default_theme.getThemeStyle(type);
    }

    /**
     * Getting the current default font for the current application.
     * 
     * @return The current default font as java.awt.Font
     */
    public static Font getDefaultFont() {
        return _default_font.getDefaultFont();
    }

    /**
     * Getting the current default font with the specified font size for the current
     * application.
     * 
     * @param size A font size as integer value
     * @return The current default font with changed font size as java.awt.Font
     */
    public static Font getDefaultFont(final int size) {
        return _default_font.getDefaultFont(size);
    }

    /**
     * Getting the current default font with the specified font size and font style
     * for the current application.
     * 
     * @param style A font style as integer value
     * @param size  A font size as integer value
     * @return The current default font with changed font size and font style as
     *         java.awt.Font
     */
    public static Font getDefaultFont(final int style, final int size) {
        return _default_font.getDefaultFont(style, size);
    }

    /**
     * Setting the default font for the current application.
     * 
     * @param font A font as java.awt.Font
     */
    public static void setDefaultFont(final Font font) {
        _default_font.setDefaultFont(font);
    }

    static void initImages() {
        /////////////// 32
        InputStream stream = DefaultFont.class.getResourceAsStream("/images/add32.png");
        addImage(stream, images_32, EmbeddedImage.Add);
        stream = DefaultFont.class.getResourceAsStream("/images/arrowleft32.png");
        addImage(stream, images_32, EmbeddedImage.ArrowLeft);
        stream = DefaultFont.class.getResourceAsStream("/images/eye32.png");
        addImage(stream, images_32, EmbeddedImage.Eye);
        stream = DefaultFont.class.getResourceAsStream("/images/file32.png");
        addImage(stream, images_32, EmbeddedImage.File);
        stream = DefaultFont.class.getResourceAsStream("/images/folder32.png");
        addImage(stream, images_32, EmbeddedImage.Folder);
        stream = DefaultFont.class.getResourceAsStream("/images/folderplus32.png");
        addImage(stream, images_32, EmbeddedImage.ForderPlus);
        stream = DefaultFont.class.getResourceAsStream("/images/gear32.png");
        addImage(stream, images_32, EmbeddedImage.Gear);
        stream = DefaultFont.class.getResourceAsStream("/images/import32.png");
        addImage(stream, images_32, EmbeddedImage.Import);
        stream = DefaultFont.class.getResourceAsStream("/images/lines32.png");
        addImage(stream, images_32, EmbeddedImage.Lines);
        stream = DefaultFont.class.getResourceAsStream("/images/loupe32.png");
        addImage(stream, images_32, EmbeddedImage.Loupe);
        stream = DefaultFont.class.getResourceAsStream("/images/recyclebin32.png");
        addImage(stream, images_32, EmbeddedImage.RecycleBin);
        stream = DefaultFont.class.getResourceAsStream("/images/refresh32.png");
        addImage(stream, images_32, EmbeddedImage.Refresh);
        stream = DefaultFont.class.getResourceAsStream("/images/pencil32.png");
        addImage(stream, images_32, EmbeddedImage.Pencil);
        stream = DefaultFont.class.getResourceAsStream("/images/diskette32.png");
        addImage(stream, images_32, EmbeddedImage.Diskette);
        stream = DefaultFont.class.getResourceAsStream("/images/eraser32.png");
        addImage(stream, images_32, EmbeddedImage.Eraser);
        stream = DefaultFont.class.getResourceAsStream("/images/home32.png");
        addImage(stream, images_32, EmbeddedImage.Home);
        stream = DefaultFont.class.getResourceAsStream("/images/user32.png");
        addImage(stream, images_32, EmbeddedImage.User);
        stream = DefaultFont.class.getResourceAsStream("/images/drive32.png");
        addImage(stream, images_32, EmbeddedImage.Drive);
        stream = DefaultFont.class.getResourceAsStream("/images/filter32.png");
        addImage(stream, images_32, EmbeddedImage.Filter);
        stream = DefaultFont.class.getResourceAsStream("/images/arrowup32.png");
        addImage(stream, images_32, EmbeddedImage.ArrowUp);
        stream = DefaultFont.class.getResourceAsStream("/images/loadcircle32.png");
        addImage(stream, images_32, EmbeddedImage.LoadCircle);

        ////////////////// 64
        stream = DefaultFont.class.getResourceAsStream("/images/add64.png");
        addImage(stream, images_64, EmbeddedImage.Add);
        stream = DefaultFont.class.getResourceAsStream("/images/arrowleft64.png");
        addImage(stream, images_64, EmbeddedImage.ArrowLeft);
        stream = DefaultFont.class.getResourceAsStream("/images/eye64.png");
        addImage(stream, images_64, EmbeddedImage.Eye);
        stream = DefaultFont.class.getResourceAsStream("/images/file64.png");
        addImage(stream, images_64, EmbeddedImage.File);
        stream = DefaultFont.class.getResourceAsStream("/images/folder64.png");
        addImage(stream, images_64, EmbeddedImage.Folder);
        stream = DefaultFont.class.getResourceAsStream("/images/folderplus64.png");
        addImage(stream, images_64, EmbeddedImage.ForderPlus);
        stream = DefaultFont.class.getResourceAsStream("/images/gear64.png");
        addImage(stream, images_64, EmbeddedImage.Gear);
        stream = DefaultFont.class.getResourceAsStream("/images/import64.png");
        addImage(stream, images_64, EmbeddedImage.Import);
        stream = DefaultFont.class.getResourceAsStream("/images/lines64.png");
        addImage(stream, images_64, EmbeddedImage.Lines);
        stream = DefaultFont.class.getResourceAsStream("/images/loupe64.png");
        addImage(stream, images_64, EmbeddedImage.Loupe);
        stream = DefaultFont.class.getResourceAsStream("/images/recyclebin64.png");
        addImage(stream, images_64, EmbeddedImage.RecycleBin);
        stream = DefaultFont.class.getResourceAsStream("/images/refresh64.png");
        addImage(stream, images_64, EmbeddedImage.Refresh);
        stream = DefaultFont.class.getResourceAsStream("/images/pencil64.png");
        addImage(stream, images_64, EmbeddedImage.Pencil);
        stream = DefaultFont.class.getResourceAsStream("/images/diskette64.png");
        addImage(stream, images_64, EmbeddedImage.Diskette);
        stream = DefaultFont.class.getResourceAsStream("/images/eraser64.png");
        addImage(stream, images_64, EmbeddedImage.Eraser);
        stream = DefaultFont.class.getResourceAsStream("/images/home64.png");
        addImage(stream, images_64, EmbeddedImage.Home);
        stream = DefaultFont.class.getResourceAsStream("/images/user64.png");
        addImage(stream, images_64, EmbeddedImage.User);
        stream = DefaultFont.class.getResourceAsStream("/images/drive64.png");
        addImage(stream, images_64, EmbeddedImage.Drive);
        stream = DefaultFont.class.getResourceAsStream("/images/filter64.png");
        addImage(stream, images_64, EmbeddedImage.Filter);
        stream = DefaultFont.class.getResourceAsStream("/images/arrowup64.png");
        addImage(stream, images_64, EmbeddedImage.ArrowUp);
        stream = DefaultFont.class.getResourceAsStream("/images/loadcircle64.png");
        addImage(stream, images_64, EmbeddedImage.LoadCircle);
    }

    static void addImage(final InputStream stream, final Map<EmbeddedImage, BufferedImage> map,
            final EmbeddedImage key) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(stream);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        if (image != null)
            map.put(key, image);
    }

    /**
     * Getting the specified image by the type and size of the image, which is
     * stored in the SpaceVIL framework.
     * 
     * @param image An image type as com.spvessel.spacevil.Flags.EmbeddedImage
     *              <p>
     *              Example: com.spvessel.spacevil.Flags.EmbeddedImage.GEAR (to get
     *              gear icon)
     * @param size  An image size as com.spvessel.spacevil.Flags.EmbeddedImageSize
     *              (only 32x32 or 64x64)
     *              <p>
     *              Example:
     *              com.spvessel.spacevil.Flags.EmbeddedImageSize.SIZE_32X32 (to get
     *              an image in 32x32 pixels)
     * @return Copy of image icon as java.awt.image.BufferedImage
     */
    public static BufferedImage getDefaultImage(final EmbeddedImage image, final EmbeddedImageSize size) {
        if (size == EmbeddedImageSize.Size32x32) {
            return images_32.get(image);
        } else if (size == EmbeddedImageSize.Size64x64) {
            return images_64.get(image);
        }
        return null;
    }

    private static Map<Integer, KeyCode> _defaultLinuxKeyCodes = new HashMap<Integer, KeyCode>() {
        {
            put(65, KeyCode.Space);
            put(48, KeyCode.Apostrophe);
            put(59, KeyCode.Comma);
            put(20, KeyCode.Minus);
            put(60, KeyCode.Period);
            put(61, KeyCode.Slash);
            put(19, KeyCode.Alpha0);
            put(10, KeyCode.Alpha1);
            put(11, KeyCode.Alpha2);
            put(12, KeyCode.Alpha3);
            put(13, KeyCode.Alpha4);
            put(14, KeyCode.Alpha5);
            put(15, KeyCode.Alpha6);
            put(16, KeyCode.Alpha7);
            put(17, KeyCode.Alpha8);
            put(18, KeyCode.Alpha9);
            put(47, KeyCode.SemiColon);
            put(21, KeyCode.Equal);
            put(38, KeyCode.A);
            put(56, KeyCode.B);
            put(54, KeyCode.C);
            put(40, KeyCode.D);
            put(26, KeyCode.E);
            put(41, KeyCode.F);
            put(42, KeyCode.G);
            put(43, KeyCode.H);
            put(31, KeyCode.I);
            put(44, KeyCode.J);
            put(45, KeyCode.K);
            put(46, KeyCode.L);
            put(58, KeyCode.M);
            put(57, KeyCode.N);
            put(32, KeyCode.O);
            put(33, KeyCode.P);
            put(24, KeyCode.Q);
            put(27, KeyCode.R);
            put(39, KeyCode.S);
            put(28, KeyCode.T);
            put(30, KeyCode.U);
            put(55, KeyCode.V);
            put(25, KeyCode.W);
            put(53, KeyCode.X);
            put(29, KeyCode.Y);
            put(52, KeyCode.Z);
            put(34, KeyCode.LeftBracket);
            put(51, KeyCode.Backslash);
            put(35, KeyCode.RightBracket);
            put(49, KeyCode.GraveAccent);
            put(94, KeyCode.World1);
            put(9, KeyCode.Escape);
            put(36, KeyCode.Enter);
            put(23, KeyCode.Tab);
            put(22, KeyCode.Backspace);
            put(118, KeyCode.Insert);
            put(119, KeyCode.Delete);
            put(114, KeyCode.Right);
            put(113, KeyCode.Left);
            put(116, KeyCode.Down);
            put(111, KeyCode.Up);
            put(112, KeyCode.PageUp);
            put(117, KeyCode.PageDown);
            put(110, KeyCode.Home);
            put(115, KeyCode.End);
            put(66, KeyCode.CapsLock);
            put(78, KeyCode.ScrollLock);
            put(77, KeyCode.NumLock);
            put(218, KeyCode.PrintScreen);
            put(127, KeyCode.Pause);
            put(67, KeyCode.F1);
            put(68, KeyCode.F2);
            put(69, KeyCode.F3);
            put(70, KeyCode.F4);
            put(71, KeyCode.F5);
            put(72, KeyCode.F6);
            put(73, KeyCode.F7);
            put(74, KeyCode.F8);
            put(75, KeyCode.F9);
            put(76, KeyCode.F10);
            put(95, KeyCode.F11);
            put(96, KeyCode.F12);
            put(90, KeyCode.Numpad0);
            put(87, KeyCode.Numpad1);
            put(88, KeyCode.Numpad2);
            put(89, KeyCode.Numpad3);
            put(83, KeyCode.Numpad4);
            put(84, KeyCode.Numpad5);
            put(85, KeyCode.Numpad6);
            put(79, KeyCode.Numpad7);
            put(80, KeyCode.Numpad8);
            put(81, KeyCode.Numpad9);
            put(129, KeyCode.NumpadDecimal);
            put(106, KeyCode.NumpadDivide);
            put(63, KeyCode.NumpadMultiply);
            put(82, KeyCode.NumpadSubtract);
            put(86, KeyCode.NumpadAdd);
            put(104, KeyCode.NumpadEnter);
            put(125, KeyCode.NumpadEqual);
            put(50, KeyCode.LeftShift);
            put(37, KeyCode.LeftControl);
            put(64, KeyCode.LeftAlt);
            put(133, KeyCode.LeftSuper);
            put(62, KeyCode.RightShift);
            put(105, KeyCode.RightControl);
            put(203, KeyCode.RightAlt);
            put(134, KeyCode.RightSuper);
            put(135, KeyCode.Menu);
        }
    };

    private static Map<Integer, KeyCode> _defaultWindowsKeyCodes = new HashMap<Integer, KeyCode>() {
        {
            put(57, KeyCode.Space);
            put(40, KeyCode.Apostrophe);
            put(51, KeyCode.Comma);
            put(12, KeyCode.Minus);
            put(52, KeyCode.Period);
            put(53, KeyCode.Slash);
            put(11, KeyCode.Alpha0);
            put(2, KeyCode.Alpha1);
            put(3, KeyCode.Alpha2);
            put(4, KeyCode.Alpha3);
            put(5, KeyCode.Alpha4);
            put(6, KeyCode.Alpha5);
            put(7, KeyCode.Alpha6);
            put(8, KeyCode.Alpha7);
            put(9, KeyCode.Alpha8);
            put(10, KeyCode.Alpha9);
            put(39, KeyCode.SemiColon);
            put(13, KeyCode.Equal);
            put(30, KeyCode.A);
            put(48, KeyCode.B);
            put(46, KeyCode.C);
            put(32, KeyCode.D);
            put(18, KeyCode.E);
            put(33, KeyCode.F);
            put(34, KeyCode.G);
            put(35, KeyCode.H);
            put(23, KeyCode.I);
            put(36, KeyCode.J);
            put(37, KeyCode.K);
            put(38, KeyCode.L);
            put(50, KeyCode.M);
            put(49, KeyCode.N);
            put(24, KeyCode.O);
            put(25, KeyCode.P);
            put(16, KeyCode.Q);
            put(19, KeyCode.R);
            put(31, KeyCode.S);
            put(20, KeyCode.T);
            put(22, KeyCode.U);
            put(47, KeyCode.V);
            put(17, KeyCode.W);
            put(45, KeyCode.X);
            put(21, KeyCode.Y);
            put(44, KeyCode.Z);
            put(26, KeyCode.LeftBracket);
            put(43, KeyCode.Backslash);
            put(27, KeyCode.RightBracket);
            put(41, KeyCode.GraveAccent);
            put(86, KeyCode.World2);
            put(1, KeyCode.Escape);
            put(28, KeyCode.Enter);
            put(15, KeyCode.Tab);
            put(14, KeyCode.Backspace);
            put(338, KeyCode.Insert);
            put(339, KeyCode.Delete);
            put(333, KeyCode.Right);
            put(331, KeyCode.Left);
            put(336, KeyCode.Down);
            put(328, KeyCode.Up);
            put(329, KeyCode.PageUp);
            put(337, KeyCode.PageDown);
            put(327, KeyCode.Home);
            put(335, KeyCode.End);
            put(58, KeyCode.CapsLock);
            put(70, KeyCode.ScrollLock);
            put(325, KeyCode.NumLock);
            put(311, KeyCode.PrintScreen);
            put(326, KeyCode.Pause);
            put(59, KeyCode.F1);
            put(60, KeyCode.F2);
            put(61, KeyCode.F3);
            put(62, KeyCode.F4);
            put(63, KeyCode.F5);
            put(64, KeyCode.F6);
            put(65, KeyCode.F7);
            put(66, KeyCode.F8);
            put(67, KeyCode.F9);
            put(68, KeyCode.F10);
            put(87, KeyCode.F11);
            put(88, KeyCode.F12);
            put(100, KeyCode.F13);
            put(101, KeyCode.F14);
            put(102, KeyCode.F15);
            put(103, KeyCode.F16);
            put(104, KeyCode.F17);
            put(105, KeyCode.F18);
            put(106, KeyCode.F19);
            put(107, KeyCode.F20);
            put(108, KeyCode.F21);
            put(109, KeyCode.F22);
            put(110, KeyCode.F23);
            put(118, KeyCode.F24);
            put(82, KeyCode.Numpad0);
            put(79, KeyCode.Numpad1);
            put(80, KeyCode.Numpad2);
            put(81, KeyCode.Numpad3);
            put(75, KeyCode.Numpad4);
            put(76, KeyCode.Numpad5);
            put(77, KeyCode.Numpad6);
            put(71, KeyCode.Numpad7);
            put(72, KeyCode.Numpad8);
            put(73, KeyCode.Numpad9);
            put(83, KeyCode.NumpadDecimal);
            put(309, KeyCode.NumpadDivide);
            put(55, KeyCode.NumpadMultiply);
            put(74, KeyCode.NumpadSubtract);
            put(78, KeyCode.NumpadAdd);
            put(284, KeyCode.NumpadEnter);
            put(89, KeyCode.NumpadEqual);
            put(42, KeyCode.LeftShift);
            put(29, KeyCode.LeftControl);
            put(56, KeyCode.LeftAlt);
            put(347, KeyCode.LeftSuper);
            put(54, KeyCode.RightShift);
            put(285, KeyCode.RightControl);
            put(312, KeyCode.RightAlt);
            put(348, KeyCode.RightSuper);
            put(349, KeyCode.Menu);
        }
    };

    /**
     * Returns KeyCode of specified scancode of keyboard.
     * 
     * @param scancode Scancode of key.
     * @return The key as com.spvessel.spacevil.Flags.KeyCode.
     */
    public static KeyCode getKeyCodeByScancode(final int scancode) {
        if (CommonService.getOSType() == OSType.Linux) {
            if (_defaultLinuxKeyCodes.containsKey(scancode)) {
                return _defaultLinuxKeyCodes.get(scancode);
            }
        } else if (CommonService.getOSType() == OSType.Windows) {
            if (_defaultWindowsKeyCodes.containsKey(scancode)) {
                return _defaultWindowsKeyCodes.get(scancode);
            }
        }
        return KeyCode.Unknown;
    }
}
