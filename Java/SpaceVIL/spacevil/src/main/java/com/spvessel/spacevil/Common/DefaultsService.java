package com.spvessel.spacevil.Common;

import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Decorations.ThemeStyle;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public final class DefaultsService {

    private static ThemeStyle _default_theme; // = ThemeStyle.GetInstance();
    private static DefaultFont _default_font = DefaultFont.getInstance();;

    private DefaultsService() {
        // _default_font = DefaultFont.getInstance();
    }

    public static ThemeStyle getDefaultTheme() {
        if (_default_theme == null)
            _default_theme = new ThemeStyle();
        return _default_theme;
    }

    public static void setDefaultTheme(ThemeStyle theme) {
        _default_theme = theme;
    }

    public static Style getDefaultStyle(Class<?> type) {
        if (_default_theme == null)
            _default_theme = new ThemeStyle();
        return _default_theme.getThemeStyle(type);
    }

    public static void setDefaultFont(Font font) {
        _default_font.setDefaultFont(font);
    }

    public static Font getDefaultFont() {
        return _default_font.getDefaultFont();
    }

    public static Font getDefaultFont(int size) {
        return _default_font.getDefaultFont(size);
    }

    public static Font getDefaultFont(int style, int size) {
        return _default_font.getDefaultFont(style, size);
    }

    private static Map<EmbeddedImage, BufferedImage> images_32 = new HashMap<>();
    private static Map<EmbeddedImage, BufferedImage> images_64 = new HashMap<>();

    static void initImages() {
        ///////////////32
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

        //////////////////64
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

    public static BufferedImage getDefaultImage(EmbeddedImage image, EmbeddedImageSize size) {
        if (size == EmbeddedImageSize.SIZE_32X32) {
            return images_32.get(image);

        } else if (size == EmbeddedImageSize.SIZE_64X64) {
            return images_64.get(image);
        }
        return null;
    }
}
