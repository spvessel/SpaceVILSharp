package com.spvessel.spacevil;

import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Core.DropArgs;
import com.spvessel.spacevil.Core.Geometry;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Core.Scale;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.Side;

import java.awt.image.BufferedImage;
import java.nio.*;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;

final class WindowProcessor {
    private Geometry _geometryForRestore = new Geometry();
    private Position _positionForRestore = new Position();
    private long _monitor;
    private GLFWVidMode _vid;
    private CommonProcessor _commonProcessor;

    WindowProcessor(CommonProcessor processor) {
        _commonProcessor = processor;
    }

    void setWindowSize(int width, int height, Scale scale) {
        if (WindowsBox.getCurrentFocusedWindow() != _commonProcessor.window)
            glfwFocusWindow(_commonProcessor.window.getGLWID());

        if (_commonProcessor.window.isKeepAspectRatio) {
            float currentW = width;
            float currentH = height;

            float ratioW = _commonProcessor.window.ratioW;
            float ratioH = _commonProcessor.window.ratioH;

            float xScale = (currentW / ratioW);
            float yScale = (currentH / ratioH);

            float ratio = 0;

            List<Side> handlerContainerSides = _commonProcessor.rootContainer.getSides();

            if (handlerContainerSides.contains(Side.LEFT) || handlerContainerSides.contains(Side.RIGHT))
                ratio = xScale;
            else
                ratio = yScale;

            width = (int) (ratioW * ratio);
            height = (int) (ratioH * ratio);
        }

        if (width > _commonProcessor.window.getMaxWidth())
            width = _commonProcessor.window.getMaxWidth();

        if (height > _commonProcessor.window.getMaxHeight())
            height = _commonProcessor.window.getMaxHeight();

        glfwSetWindowSize(_commonProcessor.handler.getWindowId(), (int) (width * scale.getX()),
                (int) (height * scale.getY()));

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
        _commonProcessor.manager.assignActionsForSender(InputEventType.WINDOW_DROP, dargs,
                _commonProcessor.rootContainer, _commonProcessor.underFocusedItems, false);
    }

    void minimizeWindow() {
        _commonProcessor.inputLocker = true;
        _commonProcessor.events.setEvent(InputEventType.WINDOW_MINIMIZE);
        glfwIconifyWindow(_commonProcessor.handler.getWindowId());
        _commonProcessor.inputLocker = false;
    }

    void maximizeWindow(Scale scale) {
        _commonProcessor.inputLocker = true;

        if (_commonProcessor.window.isMaximized) {
            glfwRestoreWindow(_commonProcessor.handler.getWindowId());
            _commonProcessor.events.setEvent(InputEventType.WINDOW_RESTORE);
            _commonProcessor.window.isMaximized = false;
        } else {
            glfwMaximizeWindow(_commonProcessor.handler.getWindowId());
            _commonProcessor.events.setEvent(InputEventType.WINDOW_MAXIMIZE);
            _commonProcessor.window.isMaximized = true;
        }

        int width = _commonProcessor.window.getWidth();
        int height = _commonProcessor.window.getHeight();
        
        glfwSetWindowSize(_commonProcessor.handler.getWindowId(), (int) (width * scale.getX()),
                (int) (height * scale.getY()));

        _commonProcessor.inputLocker = false;
    }

    void fullScreenWindow() {
        _commonProcessor.inputLocker = true;
        _monitor = glfwGetPrimaryMonitor();
        if (!_commonProcessor.window.isFullScreen) {
            _vid = glfwGetVideoMode(_monitor);
            _geometryForRestore.setSize(_commonProcessor.window.getWidth(), _commonProcessor.window.getHeight());
            _positionForRestore.setPosition(_commonProcessor.window.getX(), _commonProcessor.window.getY());
            glfwSetWindowMonitor(_commonProcessor.handler.getWindowId(), _monitor, 0, 0, _vid.width(), _vid.height(),
                    _vid.refreshRate());
            _commonProcessor.window.setWidthDirect(_vid.width());
            _commonProcessor.window.setHeightDirect(_vid.height());
            _commonProcessor.window.isFullScreen = true;
        } else {
            glfwSetWindowMonitor(_commonProcessor.handler.getWindowId(), NULL, _positionForRestore.getX(),
                    _positionForRestore.getY(), _geometryForRestore.getWidth(), _geometryForRestore.getHeight(), 0);
            _commonProcessor.window.setWidthDirect(_geometryForRestore.getWidth());
            _commonProcessor.window.setHeightDirect(_geometryForRestore.getHeight());
            _commonProcessor.window.isFullScreen = false;
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
        // List<Byte> bmp = new LinkedList<Byte>();
        // for (int j = 0; j < image.getHeight(); j++) {
        //     for (int i = 0; i < image.getWidth(); i++) {
        //         byte[] bytes = ByteBuffer.allocate(4).putInt(image.getRGB(i, j)).array();
        //         bmp.add(bytes[1]);
        //         bmp.add(bytes[2]);
        //         bmp.add(bytes[3]);
        //         bmp.add(bytes[0]);
        //     }
        // }
        // ByteBuffer result = BufferUtils.createByteBuffer(bmp.size());
        // for (byte var : bmp) {
        //     result.put(var);
        // }
        // result.rewind();
        return VramTexture.getByteBuffer(image);
    }

    void applyIcon(BufferedImage iconBig, BufferedImage iconSmall) {
        if ((iconBig == null) || (iconSmall == null))
            return;
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