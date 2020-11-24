package com.spvessel.spacevil;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import com.spvessel.spacevil.internal.Wrapper.GlfwWrapper;
import com.spvessel.spacevil.internal.Wrapper.GLFWImage;

import com.spvessel.spacevil.Flags.EmbeddedCursor;

/**
 * Class CursorImage provides features for creating custom cursors. It can also
 * be used with several standards types of cursor images (Arrow, IBeam,
 * Crosshair, Hand and etc.).
 */
public final class CursorImage {
    private static boolean isDefaultCursorsInit = false;
    static long cursorArrow;
    static long cursorInput;
    static long cursorHand;
    static long cursorResizeH;
    static long cursorResizeV;
    static long cursorResizeAll;

    static GlfwWrapper glfw = null;

    static void initCursors() {
        if(glfw == null) {
            glfw = GlfwWrapper.get();
        }
        if (!isDefaultCursorsInit) {
            // cursors
            cursorArrow = glfw.CreateStandardCursor(EmbeddedCursor.Arrow.getValue());
            cursorInput = glfw.CreateStandardCursor(EmbeddedCursor.IBeam.getValue());
            cursorHand = glfw.CreateStandardCursor(EmbeddedCursor.Hand.getValue());
            cursorResizeH = glfw.CreateStandardCursor(EmbeddedCursor.ResizeX.getValue());
            cursorResizeV = glfw.CreateStandardCursor(EmbeddedCursor.ResizeY.getValue());
            cursorResizeAll = glfw.CreateStandardCursor(EmbeddedCursor.Crosshair.getValue());
            isDefaultCursorsInit = true;
        }
    }

    private long _cursor;

    long getCursor() {
        return _cursor;
    }

    private byte[] _bitmap;

    /**
     * Constructor for creating cursor with standards types of cursor images (Arrow,
     * IBeam, Crosshair, Hand and etc.).
     * 
     * @param type Cursor image as SpaceVIL.Core.EmbeddedCursor enum.
     */
    public CursorImage(EmbeddedCursor type) {
        switch (type) {
            case Arrow:
                _cursor = CursorImage.cursorArrow;
                break;
            case IBeam:
                _cursor = CursorImage.cursorInput;
                break;
            case Crosshair:
                _cursor = CursorImage.cursorResizeAll;
                break;
            case Hand:
                _cursor = CursorImage.cursorHand;
                break;
            case ResizeX:
                _cursor = CursorImage.cursorResizeH;
                break;
            case ResizeY:
                _cursor = CursorImage.cursorResizeV;
                break;
            default:
                _cursor = CursorImage.cursorArrow;
                break;
        }

        _imageHeight = 25;
        _imageWidth = 25;
    }

    /**
     * Constructor for creating cursor with custom bitmap image.
     * 
     * @param bitmap Cursor image as java.awt.image.BufferedImage.
     */
    public CursorImage(BufferedImage bitmap) {
        if (bitmap == null) {
            return;
        }

        _bitmap = getImagePixels(bitmap);
        createCursor();
    }

    /**
     * Constructor for creating cursor with custom bitmap image with the specified
     * size.
     * 
     * @param bitmap Cursor image as java.awt.image.BufferedImage.
     * @param width  Desired width.
     * @param height Desired height.
     */
    public CursorImage(BufferedImage bitmap, int width, int height) {
        if (bitmap == null) {
            return;
        }
        BufferedImage scaled = GraphicsMathService.scaleBitmap(bitmap, width, height);
        _bitmap = getImagePixels(scaled);
        scaled.flush();
        createCursor();
    }

    private void createCursor() {
        GLFWImage imageBuffer = new GLFWImage();
        imageBuffer.width = getCursorWidth();
        imageBuffer.height = getCursorHeight();
        imageBuffer.pixels = _bitmap;
        _cursor = glfw.CreateCursor(imageBuffer, 0, 0);
    }

    private int _imageWidth;
    private int _imageHeight;

    /**
     * Getting cursor image width.
     * 
     * @return The width of the image.
     */
    public int getCursorWidth() {
        return _imageWidth;
    }

    /**
     * Getting cursor image height.
     * 
     * @return The height of the image.
     */
    public int getCursorHeight() {
        return _imageHeight;
    }

    /**
     * Setting new image for cursor.
     * 
     * @param image Cursor image as java.awt.image.BufferedImage.
     */
    public void setImage(BufferedImage image) {
        if (image == null) {
            return;
        }
        _bitmap = getImagePixels(image);
        createCursor();
    }

    private byte[] getImagePixels(BufferedImage bitmap) {
        if (bitmap == null)
            return null;

        _imageWidth = bitmap.getWidth();
        _imageHeight = bitmap.getHeight();
        // List<Byte> _map = new LinkedList<Byte>();
        // for (int j = 0; j < bitmap.getHeight(); j++) {
        // for (int i = 0; i < bitmap.getWidth(); i++) {
        // byte[] bytes = ByteBuffer.allocate(4).putInt(bitmap.getRGB(i, j)).array();
        // _map.add(bytes[1]);
        // _map.add(bytes[2]);
        // _map.add(bytes[3]);
        // _map.add(bytes[0]);
        // }
        // }
        // ByteBuffer result = BufferUtils.createByteBuffer(_map.size());
        // int index = 0;
        // for (byte var : _map) {
        // result.put(index, var);
        // index++;
        // }
        // result.rewind();
        // return result;
        return VramTexture.getByteBuffer(bitmap);
    }
}