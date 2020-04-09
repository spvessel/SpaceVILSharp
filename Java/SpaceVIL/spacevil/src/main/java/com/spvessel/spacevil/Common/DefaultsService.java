package com.spvessel.spacevil.Common;

import com.spvessel.spacevil.CursorImage;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Decorations.ThemeStyle;
import com.spvessel.spacevil.Flags.EmbeddedCursor;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * DefaultsService is static class providing methods to getting SpaceVIL default common values such as font, mouse cursor, icon images. 
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

    private static CursorImage _defaultCursor = new CursorImage(EmbeddedCursor.ARROW);

    private static ThemeStyle _default_theme; // = ThemeStyle.GetInstance();

    private static DefaultFont _default_font = DefaultFont.getInstance();;

    /**
     * Setting default cursor image the current application.
     * @param cursor The mouse cursor image as com.spvessel.spacevil.CursorImage
     */
    public static void setDefaultCursor(CursorImage cursor) {
        if (cursor == null) {
            return;
        }
        _defaultCursor = cursor;
    }

    /**
     * Getting the current default mouse cursor image.
     * @return The mouse cursor image as com.spvessel.spacevil.CursorImage
     */
    public static CursorImage getDefaultCursor() {
        return _defaultCursor;
    }

    /**
     * Getting the default theme.
     * @return The theme as The theme as com.spvessel.spacevil.Decorations.ThemeStyle
     */
    public static ThemeStyle getDefaultTheme() {
        if (_default_theme == null) {
            _default_theme = new ThemeStyle();
        }
        return _default_theme;
    }

    /**
     * Setting the default theme for the current application.
     * @param theme The theme as com.spvessel.spacevil.Decorations.ThemeStyle
     */
    public static void setDefaultTheme(ThemeStyle theme) {
        _default_theme = theme;
    }

    /**
     * Getting the default item style from the current default theme by its type.
     * @param type Item type as java.lang.Class&lt;?&gt;.
     * <p> Example: typeof(com.spvessel.spacevil.ButtonCore)
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getDefaultStyle(Class<?> type) {
        if (_default_theme == null) {
            _default_theme = new ThemeStyle();
        }
        return _default_theme.getThemeStyle(type);
    }
    
    /**
     * Getting the current default font for the current application.
     * @return The current default font as java.awt.Font
     */
    public static Font getDefaultFont() {
        return _default_font.getDefaultFont();
    }
    
    /**
     * Getting the current default font with the specified font size for the current application.
     * @param size A font size as integer value
     * @return The current default font with changed font size as java.awt.Font
     */
    public static Font getDefaultFont(int size) {
        return _default_font.getDefaultFont(size);
    }
    
    /**
     * Getting the current default font with the specified font size and font style for the current application.
     * @param style A font style as integer value
     * @param size A font size as integer value
     * @return The current default font with changed font size and font style as java.awt.Font
     */
    public static Font getDefaultFont(int style, int size) {
        return _default_font.getDefaultFont(style, size);
    }
    
    /**
     * Setting the default font for the current application.
     * @param font A font as java.awt.Font
     */
    public static void setDefaultFont(Font font) {
        _default_font.setDefaultFont(font);
    }
    

    static void initImages() {
        /////////////// 32
        InputStream stream = DefaultFont.class.getResourceAsStream("/images/add32.png");
        addImage(stream, images_32, EmbeddedImage.ADD);
        stream = DefaultFont.class.getResourceAsStream("/images/arrowleft32.png");
        addImage(stream, images_32, EmbeddedImage.ARROW_LEFT);
        stream = DefaultFont.class.getResourceAsStream("/images/eye32.png");
        addImage(stream, images_32, EmbeddedImage.EYE);
        stream = DefaultFont.class.getResourceAsStream("/images/file32.png");
        addImage(stream, images_32, EmbeddedImage.FILE);
        stream = DefaultFont.class.getResourceAsStream("/images/folder32.png");
        addImage(stream, images_32, EmbeddedImage.FOLDER);
        stream = DefaultFont.class.getResourceAsStream("/images/folderplus32.png");
        addImage(stream, images_32, EmbeddedImage.FOLDER_PLUS);
        stream = DefaultFont.class.getResourceAsStream("/images/gear32.png");
        addImage(stream, images_32, EmbeddedImage.GEAR);
        stream = DefaultFont.class.getResourceAsStream("/images/import32.png");
        addImage(stream, images_32, EmbeddedImage.IMPORT);
        stream = DefaultFont.class.getResourceAsStream("/images/lines32.png");
        addImage(stream, images_32, EmbeddedImage.LINES);
        stream = DefaultFont.class.getResourceAsStream("/images/loupe32.png");
        addImage(stream, images_32, EmbeddedImage.LOUPE);
        stream = DefaultFont.class.getResourceAsStream("/images/recyclebin32.png");
        addImage(stream, images_32, EmbeddedImage.RECYCLE_BIN);
        stream = DefaultFont.class.getResourceAsStream("/images/refresh32.png");
        addImage(stream, images_32, EmbeddedImage.REFRESH);
        stream = DefaultFont.class.getResourceAsStream("/images/pencil32.png");
        addImage(stream, images_32, EmbeddedImage.PENCIL);
        stream = DefaultFont.class.getResourceAsStream("/images/diskette32.png");
        addImage(stream, images_32, EmbeddedImage.DISKETTE);
        stream = DefaultFont.class.getResourceAsStream("/images/eraser32.png");
        addImage(stream, images_32, EmbeddedImage.ERASER);
        stream = DefaultFont.class.getResourceAsStream("/images/home32.png");
        addImage(stream, images_32, EmbeddedImage.HOME);
        stream = DefaultFont.class.getResourceAsStream("/images/user32.png");
        addImage(stream, images_32, EmbeddedImage.USER);
        stream = DefaultFont.class.getResourceAsStream("/images/drive32.png");
        addImage(stream, images_32, EmbeddedImage.DRIVE);
        stream = DefaultFont.class.getResourceAsStream("/images/filter32.png");
        addImage(stream, images_32, EmbeddedImage.FILTER);
        stream = DefaultFont.class.getResourceAsStream("/images/arrowup32.png");
        addImage(stream, images_32, EmbeddedImage.ARROW_UP);
        stream = DefaultFont.class.getResourceAsStream("/images/loadcircle32.png");
        addImage(stream, images_32, EmbeddedImage.LOAD_CIRCLE);

        ////////////////// 64
        stream = DefaultFont.class.getResourceAsStream("/images/add64.png");
        addImage(stream, images_64, EmbeddedImage.ADD);
        stream = DefaultFont.class.getResourceAsStream("/images/arrowleft64.png");
        addImage(stream, images_64, EmbeddedImage.ARROW_LEFT);
        stream = DefaultFont.class.getResourceAsStream("/images/eye64.png");
        addImage(stream, images_64, EmbeddedImage.EYE);
        stream = DefaultFont.class.getResourceAsStream("/images/file64.png");
        addImage(stream, images_64, EmbeddedImage.FILE);
        stream = DefaultFont.class.getResourceAsStream("/images/folder64.png");
        addImage(stream, images_64, EmbeddedImage.FOLDER);
        stream = DefaultFont.class.getResourceAsStream("/images/folderplus64.png");
        addImage(stream, images_64, EmbeddedImage.FOLDER_PLUS);
        stream = DefaultFont.class.getResourceAsStream("/images/gear64.png");
        addImage(stream, images_64, EmbeddedImage.GEAR);
        stream = DefaultFont.class.getResourceAsStream("/images/import64.png");
        addImage(stream, images_64, EmbeddedImage.IMPORT);
        stream = DefaultFont.class.getResourceAsStream("/images/lines64.png");
        addImage(stream, images_64, EmbeddedImage.LINES);
        stream = DefaultFont.class.getResourceAsStream("/images/loupe64.png");
        addImage(stream, images_64, EmbeddedImage.LOUPE);
        stream = DefaultFont.class.getResourceAsStream("/images/recyclebin64.png");
        addImage(stream, images_64, EmbeddedImage.RECYCLE_BIN);
        stream = DefaultFont.class.getResourceAsStream("/images/refresh64.png");
        addImage(stream, images_64, EmbeddedImage.REFRESH);
        stream = DefaultFont.class.getResourceAsStream("/images/pencil64.png");
        addImage(stream, images_64, EmbeddedImage.PENCIL);
        stream = DefaultFont.class.getResourceAsStream("/images/diskette64.png");
        addImage(stream, images_64, EmbeddedImage.DISKETTE);
        stream = DefaultFont.class.getResourceAsStream("/images/eraser64.png");
        addImage(stream, images_64, EmbeddedImage.ERASER);
        stream = DefaultFont.class.getResourceAsStream("/images/home64.png");
        addImage(stream, images_64, EmbeddedImage.HOME);
        stream = DefaultFont.class.getResourceAsStream("/images/user64.png");
        addImage(stream, images_64, EmbeddedImage.USER);
        stream = DefaultFont.class.getResourceAsStream("/images/drive64.png");
        addImage(stream, images_64, EmbeddedImage.DRIVE);
        stream = DefaultFont.class.getResourceAsStream("/images/filter64.png");
        addImage(stream, images_64, EmbeddedImage.FILTER);
        stream = DefaultFont.class.getResourceAsStream("/images/arrowup64.png");
        addImage(stream, images_64, EmbeddedImage.ARROW_UP);
        stream = DefaultFont.class.getResourceAsStream("/images/loadcircle64.png");
        addImage(stream, images_64, EmbeddedImage.LOAD_CIRCLE);
    }

    static void addImage(InputStream stream, Map<EmbeddedImage, BufferedImage> map, EmbeddedImage key) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (image != null)
            map.put(key, image);
    }

    /**
     * Getting the specified image by the type and size of the image, which is stored in the SpaceVIL framework.
     * @param image An image type as com.spvessel.spacevil.Flags.EmbeddedImage
     * <p> Example: com.spvessel.spacevil.Flags.EmbeddedImage.GEAR (to get gear icon)
     * @param size An image size as com.spvessel.spacevil.Flags.EmbeddedImageSize (only 32x32 or 64x64)
     * <p> Example: com.spvessel.spacevil.Flags.EmbeddedImageSize.SIZE_32X32 (to get an image in 32x32 pixels)
     * @return Copy of image icon as java.awt.image.BufferedImage
     */
    public static BufferedImage getDefaultImage(EmbeddedImage image, EmbeddedImageSize size) {
        if (size == EmbeddedImageSize.SIZE_32X32) {
            return images_32.get(image);
        } else if (size == EmbeddedImageSize.SIZE_64X64) {
            return images_64.get(image);
        }
        return null;
    }
}
