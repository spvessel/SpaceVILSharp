package com.spvessel.spacevil;

import java.util.LinkedList;
import java.util.List;
import java.awt.image.BufferedImage;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Core.DropArgs;
import com.spvessel.spacevil.Core.Geometry;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Core.Scale;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.OSType;
import com.spvessel.spacevil.Flags.Side;

import com.spvessel.spacevil.internal.Wrapper.*;

final class WindowProcessor {
    private GlfwWrapper glfw = null;

    private Geometry _geometryForRestore = new Geometry();
    private Position _positionForRestore = new Position();
    private long _monitor;
    private GLFWVidMode _vid;
    private CommonProcessor _commonProcessor;

    WindowProcessor(CommonProcessor processor) {
        glfw = GlfwWrapper.get();
        _commonProcessor = processor;
    }

    void setWindowSize(int width, int height, Scale scale) {
        if (WindowsBox.getCurrentFocusedWindow() != _commonProcessor.window)
            glfw.FocusWindow(_commonProcessor.window.getGLWID());

        if (_commonProcessor.window.isKeepAspectRatio) {
            float currentW = width;
            float currentH = height;

            float ratioW = _commonProcessor.window.ratioW;
            float ratioH = _commonProcessor.window.ratioH;

            float xScale = (currentW / ratioW);
            float yScale = (currentH / ratioH);

            float ratio = 0;

            List<Side> handlerContainerSides = _commonProcessor.rootContainer.getSides();

            if (handlerContainerSides.contains(Side.Left) || handlerContainerSides.contains(Side.Right))
                ratio = xScale;
            else
                ratio = yScale;

            width = (int) (ratioW * ratio);
            height = (int) (ratioH * ratio);
        }

        if (CommonService.getOSType() != OSType.Mac) {
            if (width > _commonProcessor.window.getMaxWidth())
                width = _commonProcessor.window.getMaxWidth();

            if (height > _commonProcessor.window.getMaxHeight())
                height = _commonProcessor.window.getMaxHeight();

            glfw.SetWindowSize(_commonProcessor.handler.getWindowId(), (int) (width * scale.getXScale()),
                    (int) (height * scale.getYScale()));
        } else {
            glfw.SetWindowSize(_commonProcessor.handler.getWindowId(), width, height);
        }

        _commonProcessor.events.setEvent(InputEventType.WindowResize);
    }

    void setWindowPos(int x, int y) {
        glfw.SetWindowPos(_commonProcessor.handler.getWindowId(), x, y);
        _commonProcessor.events.setEvent(InputEventType.WindowMove);
    }

    void drop(long window, int count, String[] paths) {
        DropArgs dargs = new DropArgs();
        dargs.count = count;
        dargs.paths = new LinkedList<>();
        dargs.item = _commonProcessor.hoveredItem;

        for (int i = 0; i < count; i++) {
            dargs.paths.add(paths[i]);
        }
        _commonProcessor.manager.assignActionsForSender(InputEventType.WindowDrop, dargs,
                _commonProcessor.rootContainer, _commonProcessor.underFocusedItems, false);
    }

    void minimizeWindow() {
        _commonProcessor.inputLocker = true;
        _commonProcessor.events.setEvent(InputEventType.WindowMinimize);
        if (!_commonProcessor.window.isMinimized()) {
            glfw.IconifyWindow(_commonProcessor.handler.getWindowId());
            _commonProcessor.window.setMinimized(true);
        } else {
            glfw.RestoreWindow(_commonProcessor.handler.getWindowId());
            _commonProcessor.window.setMinimized(false);
        }
        _commonProcessor.inputLocker = false;
    }

    void maximizeWindow(Scale scale) {
        _commonProcessor.inputLocker = true;

        if (_commonProcessor.window.isMaximized) {
            glfw.RestoreWindow(_commonProcessor.handler.getWindowId());
            _commonProcessor.events.setEvent(InputEventType.WindowRestore);
            _commonProcessor.window.isMaximized = false;
        } else {
            glfw.MaximizeWindow(_commonProcessor.handler.getWindowId());
            _commonProcessor.events.setEvent(InputEventType.WindowMaximize);
            _commonProcessor.window.isMaximized = true;
        }

        if (CommonService.getOSType() != OSType.Mac) {
            int width = _commonProcessor.window.getWidth();
            int height = _commonProcessor.window.getHeight();
            glfw.SetWindowSize(_commonProcessor.handler.getWindowId(), (int) (width * scale.getXScale()),
                    (int) (height * scale.getYScale()));
        }

        _commonProcessor.inputLocker = false;
    }

    void fullScreenWindow() {
        _commonProcessor.inputLocker = true;
        _monitor = glfw.GetPrimaryMonitor();
        if (!_commonProcessor.window.isFullScreen) {
            _vid = glfw.GetVideoMode(_monitor);
            _geometryForRestore.setSize(_commonProcessor.window.getWidth(), _commonProcessor.window.getHeight());
            _positionForRestore.setPosition(_commonProcessor.window.getX(), _commonProcessor.window.getY());
            glfw.SetWindowMonitor(_commonProcessor.handler.getWindowId(), _monitor, 0, 0, _vid.width, _vid.height,
                    _vid.refreshRate);
            _commonProcessor.window.setWidthDirect(_vid.width);
            _commonProcessor.window.setHeightDirect(_vid.height);
            _commonProcessor.window.isFullScreen = true;
        } else {
            glfw.SetWindowMonitor(_commonProcessor.handler.getWindowId(), 0, _positionForRestore.getX(),
                    _positionForRestore.getY(), _geometryForRestore.getWidth(), _geometryForRestore.getHeight(), 0);
            _commonProcessor.window.setWidthDirect(_geometryForRestore.getWidth());
            _commonProcessor.window.setHeightDirect(_geometryForRestore.getHeight());
            _commonProcessor.window.isFullScreen = false;
        }
        _commonProcessor.inputLocker = false;
    }

    void focus(long wnd, boolean value) {
        if (!_commonProcessor.handler.focusable)
            return;

        _commonProcessor.events.resetAllEvents();
        _commonProcessor.toolTip.initTimer(false);

        if (value) {
            if (_commonProcessor.handler.focusable) {
                WindowsBox.setCurrentFocusedWindow(_commonProcessor.window);
                _commonProcessor.window.setFocus(value);
                _commonProcessor.handler.focused = value;
            }
        } else {
            if (_commonProcessor.window.isDialog) {
                _commonProcessor.window.setFocus(true);
                _commonProcessor.handler.focused = true;
            } else {
                _commonProcessor.window.setFocus(value);
                _commonProcessor.handler.focused = value;

                if (_commonProcessor.window.isOutsideClickClosable) {
                    _commonProcessor.resetItems();
                    _commonProcessor.window.close();
                }
            }
        }
    }

    private byte[] createByteImage(BufferedImage image) {
        // List<Byte> bmp = new LinkedList<Byte>();
        // for (int j = 0; j < image.getHeight(); j++) {
        // for (int i = 0; i < image.getWidth(); i++) {
        // byte[] bytes = ByteBuffer.allocate(4).putInt(image.getRGB(i, j)).array();
        // bmp.add(bytes[1]);
        // bmp.add(bytes[2]);
        // bmp.add(bytes[3]);
        // bmp.add(bytes[0]);
        // }
        // }
        // ByteBuffer result = BufferUtils.createByteBuffer(bmp.size());
        // for (byte var : bmp) {
        // result.put(var);
        // }
        // result.rewind();
        return VramTexture.getByteBuffer(image);
    }

    void applyIcon(BufferedImage iconBig, BufferedImage iconSmall) {
        if ((iconBig == null) || (iconSmall == null))
            return;
        GLFWImage[] gb = new GLFWImage[2];

        GLFWImage s = new GLFWImage();
        s.width = iconSmall.getWidth();
        s.height = iconSmall.getHeight();
        s.pixels = createByteImage(iconSmall);

        GLFWImage b = new GLFWImage();
        b.width = iconBig.getWidth();
        b.height = iconBig.getHeight();
        b.pixels = createByteImage(iconBig);

        gb[0] = s;
        gb[1] = b;

        glfw.SetWindowIcon(_commonProcessor.handler.getWindowId(), 2, gb);
    }
}