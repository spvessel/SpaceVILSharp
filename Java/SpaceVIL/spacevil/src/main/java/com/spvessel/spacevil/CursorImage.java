package com.spvessel.spacevil;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Flags.EmbeddedCursor;

public final class CursorImage {
    private long _cursor;

    long getCursor() {
        return _cursor;
    }

    private ByteBuffer _bitmap;

    public CursorImage(int type) {
        switch (type) {
        case EmbeddedCursor.ARROW:
            _cursor = CommonService.cursorArrow;
            break;
        case EmbeddedCursor.IBEAM:
            _cursor = CommonService.cursorInput;
            break;
        case EmbeddedCursor.CROSSHAIR:
            _cursor = CommonService.cursorResizeAll;
            break;
        case EmbeddedCursor.HAND:
            _cursor = CommonService.cursorHand;
            break;
        case EmbeddedCursor.RESIZE_X:
            _cursor = CommonService.cursorResizeH;
            break;
        case EmbeddedCursor.RESIZE_Y:
            _cursor = CommonService.cursorResizeV;
            break;
        default:
            _cursor = CommonService.cursorArrow;
            break;
        }
    }

    public CursorImage(BufferedImage bitmap) {
        if (bitmap == null)
            return;

        _bitmap = getImagePixels(bitmap);
        createCursor();
    }

    public CursorImage(BufferedImage bitmap, int width, int height) {
        if (bitmap == null)
            return;
        BufferedImage scaled = GraphicsMathService.scaleBitmap(bitmap, width, height);
        _bitmap = getImagePixels(scaled);
        scaled.flush();
        createCursor();
    }

    private void createCursor() {
        GLFWImage imageBuffer = GLFWImage.malloc();
        imageBuffer.set(getCursorWidth(), getCursorHeight(), _bitmap);
        _cursor = GLFW.glfwCreateCursor(imageBuffer, 0, 0);
    }

    private int _imageWidth;
    private int _imageHeight;

    public int getCursorWidth() {
        return _imageWidth;
    }

    public int getCursorHeight() {
        return _imageHeight;
    }

    public void setImage(BufferedImage image) {
        if (image == null)
            return;
        _bitmap = getImagePixels(image);
        createCursor();
    }

    private ByteBuffer getImagePixels(BufferedImage bitmap) {
        if (bitmap == null)
            return null;

        _imageWidth = bitmap.getWidth();
        _imageHeight = bitmap.getHeight();
        List<Byte> _map = new LinkedList<Byte>();
        for (int j = 0; j < bitmap.getHeight(); j++) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                byte[] bytes = ByteBuffer.allocate(4).putInt(bitmap.getRGB(i, j)).array();
                _map.add(bytes[1]);
                _map.add(bytes[2]);
                _map.add(bytes[3]);
                _map.add(bytes[0]);
            }
        }
        ByteBuffer result = BufferUtils.createByteBuffer(_map.size());
        int index = 0;
        for (byte var : _map) {
            result.put(index, var);
            index++;
        }
        result.rewind();
        return result;
    }
}