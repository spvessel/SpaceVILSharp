package com.spvessel.spacevil;

import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Core.DropArgs;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.Side;

import java.awt.image.BufferedImage;
import java.nio.*;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;

final class WindowProcessor {
    private CommonProcessor _commonProcessor;

    WindowProcessor(CommonProcessor processor) {
        _commonProcessor = processor;
    }

    void setWindowSize(int width, int height) {
        if (WindowsBox.getCurrentFocusedWindow() != _commonProcessor.window)
            glfwFocusWindow(_commonProcessor.window.getGLWID());

        if (_commonProcessor.window.isKeepAspectRatio) {
            float currentW = width;
            float currentH = height;

            float ratioW = _commonProcessor.window.ratioW;
            float ratioH = _commonProcessor.window.ratioH;

            float xScale = (currentW / ratioW);
            float yScale = (currentH / ratioH);

            float scale = 0;

            List<Side> handlerContainerSides = _commonProcessor.rootContainer.getSides();

            if (handlerContainerSides.contains(Side.LEFT) || handlerContainerSides.contains(Side.RIGHT))
                scale = xScale;
            else
                scale = yScale;

            width = (int) (ratioW * scale);
            height = (int) (ratioH * scale);
        }
        glfwSetWindowSize(_commonProcessor.handler.getWindowId(), width, height);
        _commonProcessor.events.setEvent(InputEventType.WINDOW_RESIZE);
    }

    void setWindowPos(int x, int y) {
        glfwSetWindowPos(_commonProcessor.handler.getWindowId(), x, y);
        _commonProcessor.events.setEvent(InputEventType.WINDOW_MOVE);
    }

    void drop(long window, int count, long paths) {
        DropArgs dargs = new DropArgs();
        dargs.count = count;
        dargs.paths = new LinkedList<>();
        dargs.item = _commonProcessor.hoveredItem;

        for (int i = 0; i < count; i++) {
            String str = GLFWDropCallback.getName(paths, i);
            dargs.paths.add(str);
        }
        _commonProcessor.manager.assignActionsForSender(InputEventType.WINDOW_DROP, dargs, _commonProcessor.rootContainer,
                _commonProcessor.underFocusedItems, false);
    }

    void minimizeWindow() {
        _commonProcessor.inputLocker = true;
        _commonProcessor.events.setEvent(InputEventType.WINDOW_MINIMIZE);
        glfwIconifyWindow(_commonProcessor.handler.getWindowId());
        _commonProcessor.inputLocker = false;
    }

    void maximizeWindow() {
        _commonProcessor.inputLocker = true;
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        _commonProcessor.events.setEvent(InputEventType.WINDOW_RESTORE);

        if (_commonProcessor.window.isMaximized) {
            glfwRestoreWindow(_commonProcessor.handler.getWindowId());
            _commonProcessor.window.isMaximized = false;
            glfwGetWindowSize(_commonProcessor.handler.getWindowId(), w, h);
            _commonProcessor.window.setWidth(w.get(0));
            _commonProcessor.window.setHeight(h.get(0));
        } else {
            glfwMaximizeWindow(_commonProcessor.handler.getWindowId());
            _commonProcessor.window.isMaximized = true;
            glfwGetWindowSize(_commonProcessor.handler.getWindowId(), w, h);
            _commonProcessor.window.setWidth(w.get(0));
            _commonProcessor.window.setHeight(h.get(0));
        }
        _commonProcessor.inputLocker = false;
    }

    void focus(long wnd, boolean value) {
        _commonProcessor.events.resetAllEvents();
        _commonProcessor.toolTip.initTimer(false);
        _commonProcessor.window.isFocused = value;
        if (value) {
            if (_commonProcessor.handler.focusable) {
                WindowsBox.setCurrentFocusedWindow(_commonProcessor.window);
                _commonProcessor.handler.focused = value;
            }
        } else {
            if (_commonProcessor.window.isDialog) {
                _commonProcessor.handler.focused = true;
            } else {
                _commonProcessor.handler.focused = value;
                if (_commonProcessor.window.isOutsideClickClosable) {
                    _commonProcessor.resetItems();
                    _commonProcessor.window.close();
                }
            }
        }
    }

    private ByteBuffer createByteImage(BufferedImage image) {
        List<Byte> bmp = new LinkedList<Byte>();
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                byte[] bytes = ByteBuffer.allocate(4).putInt(image.getRGB(i, j)).array();
                bmp.add(bytes[1]);
                bmp.add(bytes[2]);
                bmp.add(bytes[3]);
                bmp.add(bytes[0]);
            }
        }
        ByteBuffer result = BufferUtils.createByteBuffer(bmp.size());
        int index = 0;
        for (byte var : bmp) {
            result.put(index, var);
            index++;
        }
        result.rewind();
        return result;
    }

    void applyIcon(BufferedImage iconBig, BufferedImage iconSmall) {
        GLFWImage.Buffer gb = GLFWImage.malloc(2);
        GLFWImage s = GLFWImage.malloc();
        s.set(iconSmall.getWidth(), iconSmall.getHeight(), createByteImage(iconSmall));
        GLFWImage b = GLFWImage.malloc();
        b.set(iconBig.getWidth(), iconBig.getHeight(), createByteImage(iconBig));
        gb.put(0, s);
        gb.put(1, b);

        glfwSetWindowIcon(_commonProcessor.handler.getWindowId(), gb);

        gb.free();
        s.free();
        b.free();
    }
}