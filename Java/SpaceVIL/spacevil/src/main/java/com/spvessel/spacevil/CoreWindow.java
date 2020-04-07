package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Core.Area;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.EventCommonMethodState;
import com.spvessel.spacevil.Core.EventDropMethodState;
import com.spvessel.spacevil.Core.EventInputTextMethodState;
import com.spvessel.spacevil.Core.EventKeyMethodState;
import com.spvessel.spacevil.Core.EventMouseMethodState;
import com.spvessel.spacevil.Core.Geometry;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Core.Scale;
import com.spvessel.spacevil.Core.Size;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Exceptions.SpaceVILException;
import com.spvessel.spacevil.Flags.MSAA;
import com.spvessel.spacevil.Flags.OSType;
import com.spvessel.spacevil.Flags.RedrawFrequency;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * CoreWindow is an abstract class containing an implementation of common functionality for a window.
 */
public abstract class CoreWindow {
    private static int count = 0;
    private UUID windowUUID;

    /**
     * Constructs a CoreWindow
     */
    public CoreWindow() {
        windowUUID = UUID.randomUUID();
        setWindowName("Window_" + count);
        setWindowTitle("Window_" + count);
        setDefaults();
        count++;
        setHandler(new WindowLayout(this));
        setSize(300, 300);
        setMinSize(150, 100);
    }

    /**
     * Setting basic window attributes.
     * @param name Window name.
     * @param title Title text.
     */
    public void setParameters(String name, String title) {
        setWindowName(name);
        setWindowTitle(title);
    }

    /**
     * Setting basic window attributes
     * @param name Window name.
     * @param title Title text.
     * @param width Window width in pixels.
     * @param height Window height  in pixels.
     */
    public void setParameters(String name, String title, int width, int height) {
        setWindowName(name);
        setWindowTitle(title);
        setSize(width, height);
    }

    /**
     * Setting basic window attributes.
     * @param name Window name.
     * @param title Title text.
     * @param width Window width in pixels.
     * @param height Window height  in pixels.
     * @param isBorder A flag that shows/hides native window border decoration.
     */
    public void setParameters(String name, String title, int width, int height, boolean isBorder) {
        setWindowName(name);
        setWindowTitle(title);
        setSize(width, height);
        isBorderHidden = !isBorder;
    }

    private WindowLayout windowLayout;

    /**
     * Parent item for the CoreWindow
     */
    WindowLayout getLayout() {
        return windowLayout;
    }

    /**
     * Parent item for the CoreWindow
     */
    void setHandler(WindowLayout wl) {
        if (wl == null) {
            throw new SpaceVILException("Window handler can't be null");
        }
        windowLayout = wl;
        wl.setCoreWindow();
    }

    /**
     * Show the CoreWindow
     */
    public void show() {
        windowLayout.show();
    }

    /**
     * Close the CoreWindow
     */
    public void close() {
        windowLayout.close();
    }

    /**
     * This abstract method should provide the initial window attributes, content, events. 
     */
    abstract public void initWindow();

    /**
     * @return Count of all CoreWindows
     */
    public int getCount() {
        return count;
    }

    /**
     * @return CoreWindow unique ID
     */
    public UUID getWindowGuid() {
        return windowUUID;
    }

    // ------------------------------------------------------------------------------------------
    /**
     * Setting window background color.
     * @param color System.Drawing.Color.FromARGB(alpha, red, green, blue)
     */
    public void setBackground(Color color) {
        windowLayout.getContainer().setBackground(color);
    }

    /**
     * Setting window background color.
     * @param r Red bits of a color. Range: (0 - 255)
     * @param g Green bits of a color. Range: (0 - 255)
     * @param b Blue bits of a color. Range: (0 - 255)
     */
    public void setBackground(int r, int g, int b) {
        windowLayout.getContainer().setBackground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting window background color.
     * @param r Red bits of a color. Range: (0 - 255)
     * @param g Green bits of a color. Range: (0 - 255)
     * @param b Blue bits of a color. Range: (0 - 255)
     * @param a Alpha bits of a color. Range: (0 - 255)
     */
    public void setBackground(int r, int g, int b, int a) {
        windowLayout.getContainer().setBackground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Setting window background color.
     * @param r Red (0.0f - 1.0f)
     * @param g Green (0.0f - 1.0f)
     * @param b Blue (0.0f - 1.0f)
     */
    public void setBackground(float r, float g, float b) {
        windowLayout.getContainer().setBackground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting window background color.
     * @param r Red (0.0f - 1.0f)
     * @param g Green (0.0f - 1.0f)
     * @param b Blue (0.0f - 1.0f)
     * @param a Alpha (0.0f - 1.0f)
     */
    public void setBackground(float r, float g, float b, float a) {
        windowLayout.getContainer().setBackground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Getting window background color.
     * @return Returns background color as System.Drawing.Color
     */
    public Color getBackground() {
        return windowLayout.getContainer().getBackground();
    }

    /**
     * Setting padding indents. Padding is the space that’s inside the element between the element and the border.
     * @param padding com.spvessel.spacevil.Decorations.Indents(int left, int top, int right, int bottom)
     */
    public void setPadding(Indents padding) {
        windowLayout.getContainer().setPadding(padding);
    }

    /**
     * Setting padding indents. Padding is the space that’s inside the element between the element and the border.
     * @param left Left indent.
     * @param top Top indent.
     * @param right Right indent.
     * @param bottom Bottom indent.
     */
    public void setPadding(int left, int top, int right, int bottom) {
        windowLayout.getContainer().setPadding(left, top, right, bottom);
    }

    /**
     * Getting items as a list of IBaseItem items.
     * @return Returns a list of contained items in the window.
     */
    public List<InterfaceBaseItem> getItems() {
        return windowLayout.getContainer().getItems();
    }

    /**
     * Adding an item to the window.
     * @param item An instance of any IBaseItem class.
     */
    public void addItem(InterfaceBaseItem item) {
        windowLayout.getContainer().addItem(item);
    }

    /**
     * Allows to add multiple items to the window.
     * @param items An instance of any IBaseItem class.
     */
    public void addItems(InterfaceBaseItem... items) {
        for (InterfaceBaseItem item : items) {
            windowLayout.getContainer().addItem(item);
        }
    }

    /**
     * Allows you to insert an item at a specified position.
     * @param item An instance of any IBaseItem class.
     * @param index Index of position.
     */
    public void insertItem(InterfaceBaseItem item, int index) {
        windowLayout.getContainer().insertItem(item, index);
    }

    /**
     * Removing a specified item.
     * @param item An instance of any IBaseItem class.
     * @return True: if the window cantained the specified item and it was successfully removed.
     * False: if the window did not cantain the specified item.
     */
    public boolean removeItem(InterfaceBaseItem item) {
        return windowLayout.getContainer().removeItem(item);
    }

    /**
     * Removing all containing items in the window.
     */
    public void clear() {
        windowLayout.getContainer().clear();
    }

    private String _name;

    /**
     * Setting the window name. The window name is the string ID of the window and may differ from the window title.
     * @param value Window name.
     */
    public void setWindowName(String value) {
        _name = value;
        if (windowLayout != null) {
            windowLayout.getContainer().setItemName(_name);
        }
    }

    /**
     * Getting the window name.
     * @return Window name.
     */
    public String getWindowName() {
        return _name;
    }

    private String _title;

    /**
     * Setting the window title text.
     * @param title Title text.
     */
    public void setWindowTitle(String title) {
        _title = title;
    }

    /**
     * Getting the title text.
     * @return Title text.
     */
    public String getWindowTitle() {
        return _title;
    }

    // geometry
    private Geometry _itemGeometry = new Geometry();

    void setWidthDirect(int width) {
        _itemGeometry.setWidth(width);
        windowLayout.getContainer().setWidth(width);
    }

    /**
     * Setting the window width.
     * @param width Width in pixels.
     */
    public void setWidth(int width) {
        _itemGeometry.setWidth(width);
        windowLayout.getContainer().setWidth(width);
        if (windowLayout.isGLWIDValid()) {
            windowLayout.updateSize();
        }
    }

    void setHeightDirect(int height) {
        _itemGeometry.setHeight(height);
        windowLayout.getContainer().setHeight(height);
    }

    /**
     * Setting the window height.
     * @param height Height in pixels.
     */
    public void setHeight(int height) {
        _itemGeometry.setHeight(height);
        windowLayout.getContainer().setHeight(height);
        if (windowLayout.isGLWIDValid()) {
            windowLayout.updateSize();
        }
    }

    /**
     * Setting the window size in pixels: width and height.
     * @param width Width in pixels.
     * @param height Height in pixels.
     */
    public void setSize(int width, int height) {
        _itemGeometry.setWidth(width);
        _itemGeometry.setHeight(height);
        windowLayout.getContainer().setWidth(_itemGeometry.getWidth());
        windowLayout.getContainer().setHeight(_itemGeometry.getHeight());

        if (windowLayout.isGLWIDValid()) {
            windowLayout.updateSize();
        }
    }

    /**
     * Setting the window minimum width.
     * @param width Minimum width in pixels.
     */
    public void setMinWidth(int width) {
        _itemGeometry.setMinWidth(width);
        if (windowLayout.getContainer() != null) {
            windowLayout.getContainer().setMinWidth(width);
        }
    }

    /**
     * Setting the window minimum height.
     * @param height Minimum height in pixels.
     */
    public void setMinHeight(int height) {
        _itemGeometry.setMinHeight(height);
        if (windowLayout.getContainer() != null) {
            windowLayout.getContainer().setMinHeight(height);
        }
    }

    /**
     * Setting the minimum window size in pixels: width and height.
     * @param width Minimum width in pixels.
     * @param height Minimum height in pixels.
     */
    public void setMinSize(int width, int height) {
        setMinWidth(width);
        setMinHeight(height);
    }

    /**
     * Setting the window maximum width.
     * @param width Maximum width in pixels.
     */
    public void setMaxWidth(int width) {
        _itemGeometry.setMaxWidth(width);
        if (windowLayout.getContainer() != null) {
            windowLayout.getContainer().setMaxWidth(width);
        }
    }

    /**
     * Setting the window maximum height.
     * @param height Maximum height in pixels.
     */
    public void setMaxHeight(int height) {
        _itemGeometry.setMaxHeight(height);
        if (windowLayout.getContainer() != null) {
            windowLayout.getContainer().setMaxHeight(height);
        }
    }

    /**
     * Setting the maximum window size in pixels: width and height.
     * @param width Maximum width in pixels.
     * @param height Maximum height in pixels.
     */
    public void setMaxSize(int width, int height) {
        setMaxWidth(width);
        setMaxHeight(height);
    }

    /**
     * Getting the current minimum window width.
     * @return Current minimum width in pixels.
     */
    public int getMinWidth() {
        return _itemGeometry.getMinWidth();
    }

    /**
     * Getting the current minimum window width.
     * @return Current minimum width in pixels.
     */
    public int getWidth() {
        return _itemGeometry.getWidth();
    }

    /**
     * Getting the current maximum window width.
     * @return Current maximum width in pixels.
     */
    public int getMaxWidth() {
        return _itemGeometry.getMaxWidth();
    }

    /**
     * Getting the current minimum window height.
     * @return Current minimum height in pixels.
     */
    public int getMinHeight() {
        return _itemGeometry.getMinHeight();
    }

    /**
     * Getting the current window height.
     * @return Current height in pixels.
     */
    public int getHeight() {
        return _itemGeometry.getHeight();
    }

    /**
     * Getting the current maximum window height.
     * @return Current maximum height in pixels.
     */
    public int getMaxHeight() {
        return _itemGeometry.getMaxHeight();
    }

    /**
     * Getting the current window size.
     * @return Current window size as com.spvessel.spacevil.Core.Size.
     */
    public Size getSize() {
        return _itemGeometry.getSize();
    }

    // position
    private Position _itemPosition = new Position(50, 50);

    void setXDirect(int x) {
        _itemPosition.setX(x);
    }

    /**
     * Setting the window x-coordinate (the left-top window corner). Relocating the window at specified x-coordinate.
     * @param x X-Coordinate.
     */
    public void setX(int x) {
        setXDirect(x);
        if (windowLayout.isGLWIDValid()) {
            windowLayout.updatePosition();
        }
    }

    /**
     * Getting the current window x-coordinate.
     * @return Current x-coordinate.
     */
    public int getX() {
        return _itemPosition.getX();
    }

    void setYDirect(int y) {
        _itemPosition.setY(y);
    }

    /**
     * Setting the window y-coordinate (the left-top window corner). Relocating the window at specified y-coordinate.
     * @param y Y-Coordinate.
     */
    public void setY(int y) {
        setYDirect(y);
        if (windowLayout.isGLWIDValid()) {
            windowLayout.updatePosition();
        }
    }

    /**
     * Getting the current window y-coordinate.
     * @return Current y-coordinate.
     */
    public int getY() {
        return _itemPosition.getY();
    }

    /**
     * Setting the window x-coordinate and y-coordinate (the left-top window corner). Relocating the window at specified coordinates.
     * @param x X-Coordinate.
     * @param y Y-Coordinate.
     */
    public void setPosition(int x, int y) {
        _itemPosition.setPosition(x, y);

        if (windowLayout.isGLWIDValid()) {
            windowLayout.updatePosition();
        }
    }

    /**
     * Setting the window x-coordinate and y-coordinate (the left-top window corner). Relocating the window at specified coordinates.
     * @param position X-coordinate and Y-coordinate provided as com.spvessel.spacevil.Core.Position
     */
    public void setPosition(Position position) {
        _itemPosition.setPosition(position.getX(), position.getY());

        if (windowLayout.isGLWIDValid()) {
            windowLayout.updatePosition();
        }
    }

    /**
     * Getting the current window position.
     * @return X-coordinate and Y-coordinate provided as com.spvessel.spacevil.Core.Position
     */
    public Position getPosition() {
        return _itemPosition;
    }

    private void setDefaults() {
        isDialog = false;
        isClosed = true;
        isHidden = false;
        isResizable = true;
        isAlwaysOnTop = false;
        isBorderHidden = false;
        isCentered = false;
        isFocusable = true;
        isOutsideClickClosable = false;
        isMaximized = false;
        isTransparent = false;
        isFullScreen = false;
    }

    /**
     * <p>A flag that determines whether the current window is dialog or not.
     * <p>True: window is dialog. False: window is NOT dialog.
     * <p>Default: False.
     */
    public boolean isDialog;

    /**
     * <p>A flag that determines whether the current window is in closed state or not.
     * <p>True: window is closed. False: window is opened.
     * <p>>Default: True.
     */
    public boolean isClosed;

    /**
     * <p>A flag that determines whether the current window is in hidden state or not.
     * <p>True: window is hidden. False: window is unhidden.
     * <p>Default: False.
     */
    public boolean isHidden;

    /**
     * <p>A flag that determines whether the current window can be resize or not.
     * <p>True: window is resizable. False: window is NOT resizable.
     * <p>Default: True.
     */
    public boolean isResizable;

    /**
     * <p>A flag that determines whether the current window is always on top of all other windows or not.
     * <p>True: window is on top. False: window is NOT on top.
     * <p>Default: False.
     */
    public boolean isAlwaysOnTop;

    /**
     * <p>A flag that shows/hides native the current window border decoration.
     * <p>True: native window border is HIDDEN. False: native window border is SHOWN.
     * <p>Default: False.
     */
    public boolean isBorderHidden;

    /**
     * <p>A flag that determines whether the current window will first appear in the center of the screen or not.
     * <p>True: window is centered. False: window is NOT centered.
     * <p>Default: True.
     */
    public boolean isCentered;

    /**
     * <p>A flag that determines whether the current window can be in focused state or not.
     * <p>True: window is focusable. False: window is NOT focusable.
     * <p>Default: True.
     */
    public boolean isFocusable;

    /**
     * <p>A flag that determines whether the current window can be closed if the mouse is clicked outside of the current window or not.
     * <p>True: window can be closed if the mouse is clicked outside. False: window can NOT be closed if the mouse is clicked outside.
     * <p>Default: False.
     */
    public boolean isOutsideClickClosable;

    /**
     * <p>A flag that determines whether the current window will first appear maximized or not.
     * <p>True: window will first appear maximized. False: window will NOT first appear maximized.
     * <p>Default: False.
     */
    public boolean isMaximized;

    /**
     * <p>A flag that determines whether the current window can be transparent or not.
     * <p>True: window can be transparent. False: window can NOT be transparent.
     * <p>Default: False.
     */
    public boolean isTransparent;

    /**
     * <p>A flag that determines whether the current window will first appear in fullscreen mode or not.
     * <p>True: window will first appear in fullscreen mode. False: window will NOT first appear in fullscreen mode.
     * <p>Default: False.
     */
    boolean isFullScreen;

    MSAA _msaa = MSAA.MSAA_4X;
    
    /**
     * Setting the anti aliasing quality (off, x2, x4, x8).
     * Default: MSAA.MSAA4x
     * @param msaa com.spvessel.spacevil.Flags.MSAA anti aliasing quality.
     */
    public void setAntiAliasingQuality(MSAA msaa) {
        _msaa = msaa;
    }
    
    void setFocusable(boolean value) {
        windowLayout.setFocusable(value);
    }
    
    private boolean _isFocused;
    
    /**
     * Lets to know if the current window is focused or not.
     * @return True: if the current window is focused. False: if the current window is unfocused.
     */
    public boolean isFocused() {
        return _isFocused;
    }
    
    /**
     * Lets to manage focus state ot the current window.
     * @param value True: if you want the window to be focused. False: if you want the window to be unfocused.
     */
    public void setFocus(Boolean value) {
        if (_isFocused == value) {
            return;
        }
        _isFocused = value;
        if (value) {
            windowLayout.setFocus();
        }
    }
    
    /**
     * Sets the window focused.
     */
    public void setWindowFocused() {
        windowLayout.setFocus();
    }
    
    /**
     * Sets the window minimized.
     */
    public void minimize() {
        windowLayout.minimize();
    }

    /**
     * Sets the window maximized.
     */
    public void maximize() {
        if (CommonService.getOSType() != OSType.MAC)
            windowLayout.maximize();
        else
            macOSMaximize();
    }

    private Area _savedArea = new Area();

    private void macOSMaximize() {
        if (!isMaximized) {
            _savedArea.setAttr(getX(), getY(), getWidth(), getHeight());
            Area area = getWorkArea();
            setPosition(area.getX(), area.getY());
            setSize(area.getWidth(), area.getHeight());
            isMaximized = true;
        } else {
            setPosition(_savedArea.getX(), _savedArea.getY());
            setSize(_savedArea.getWidth(), _savedArea.getHeight());
            isMaximized = false;
        }
    }

    /**
     * Toggles the window to full screen mode or windowed mode.
     */
    public void toggleFullScreen() {
        windowLayout.toggleFullScreen();
    }

    /**
     * Getting the current focused item in the current window.
     * @return com.spvessel.spacevil.Prototype (abstract class for interactive items).
     */
    public Prototype getFocusedItem() {
        return windowLayout.getFocusedItem();
    }

    /**
     * Setting the specified item to the focused state.
     * @param item Any item that can be focused and extends of com.spvessel.spacevil.Prototype (abstract class for interactive items).
     */
    public void setFocusedItem(Prototype item) {
        windowLayout.setFocusedItem(item);
    }

    // public void setFocus() {
    //     windowLayout.getContainer().setFocus();
    // }

    void resetItems() {
        windowLayout.resetItems();
    }

    /**
     * Returns focus to the root item of the window.
     */
    public void resetFocus() {
        windowLayout.resetFocus();
    }

    /**
     * Sets the icons of the current window.
     * @param icon_big Task bar icon.
     * @param icon_small Title bar icon.
     */
    public void setIcon(BufferedImage icon_big, BufferedImage icon_small) {
        windowLayout.setIcon(icon_big, icon_small);
    }

    /**
     * Hides of unhides the current window.
     * @param value True: if you want to hide the window. False: if you want tu unhide the window.
     */
    public void setHidden(Boolean value) {
        windowLayout.setHidden(value);
        isHidden = value;
    }

    /**
     * Lets to set the rendering frequency.
     * Default: com.spvessel.spacevil.Flags.RedrawFrequency.Low
     * @param value Rendering frequency as com.spvessel.spacevil.Flags.RedrawFrequency.
     */
    public void setRenderFrequency(RedrawFrequency value) {
        WindowManager.setRenderFrequency(value);
    }

    /**
     * Getting the current rendering frequency.
     * @return Rendering frequency as com.spvessel.spacevil.Flags.RedrawFrequency
     */
    public RedrawFrequency getRenderFrequency() {
        return WindowManager.getRenderFrequency();
    }
    
    /**
     * Lets to describe the actions when the window starts.
     */
    public EventCommonMethod eventOnStart = new EventCommonMethod();
    
    /**
     * Lets to describe the actions when closing the window.
     */
    public EventCommonMethod eventClose = new EventCommonMethod();
    
    /**
     * Lets to describe the actions when you drag&drop files/folders to the current window.
     */
    public EventDropMethodState eventDrop = new EventDropMethodState();

    void release() {
        eventClose.clear();
        eventMinimize.clear();
        eventHide.clear();
        freeEvents();
    }
    
    void setWindow(WContainer window) {
        windowLayout.setWindow(window);
    }
    
    int ratioW = -1;
    int ratioH = -1;
    boolean isKeepAspectRatio = false;

    /**
     * Lets to set aspect ratio.
     * @param w Width value.
     * @param h Height value.
     */
    public void setAspectRatio(int w, int h) {
        isKeepAspectRatio = true;
        ratioW = w;
        ratioH = h;
    }
    
    private EventCommonMethod eventMinimize = new EventCommonMethod();
    private EventCommonMethod eventHide = new EventCommonMethod();
    
    EventCommonMethodState eventFocusGet = new EventCommonMethodState();
    EventCommonMethodState eventFocusLost = new EventCommonMethodState();

    /**
     * ATTENTION!
     * Not implemented yet.
     */
    public EventCommonMethodState eventResize = new EventCommonMethodState();

    /**
     * ATTENTION!
     * Not implemented yet.
     */
    public EventCommonMethodState eventDestroy = new EventCommonMethodState();

    /**
     * Lets to describe the actions when mouse cursor hovers the root item of the window.
     */
    public EventMouseMethodState eventMouseHover = new EventMouseMethodState();

    /**
     * Lets to describe the actions when mouse cursor leaves the root item of the window.
     */
    public EventMouseMethodState eventMouseLeave = new EventMouseMethodState();

    /**
     * Lets to describe the actions when the root item of the window was clicked.
     */
    public EventMouseMethodState eventMouseClick = new EventMouseMethodState();

    /**
     * Lets to describe the actions when the root item of the window was double clicked.
     */
    public EventMouseMethodState eventMouseDoubleClick = new EventMouseMethodState();

    /**
     * Lets to describe the actions when the root item of the window was pressed.
     */
    public EventMouseMethodState eventMousePress = new EventMouseMethodState();

    /**
     * Lets to describe the actions when the mouse button was pressed and moved inside the root item of the window.
     */
    public EventMouseMethodState eventMouseDrag = new EventMouseMethodState();

    /**
     * Lets to describe the actions when the mouse button was released after dragging.
     */
    public EventMouseMethodState eventMouseDrop = new EventMouseMethodState();

    /**
     * Lets to describe the actions when mouse wheel scrolls up.
     */
    public EventMouseMethodState eventScrollUp = new EventMouseMethodState();

    /**
     * Lets to describe the actions when mouse wheel scrolls down.
     */
    public EventMouseMethodState eventScrollDown = new EventMouseMethodState();

    /**
     * Lets to describe the actions when a keyboard key was pressed.
     */
    public EventKeyMethodState eventKeyPress = new EventKeyMethodState();

    /**
     * Lets to describe the actions when a keyboard key was released.
     */
    public EventKeyMethodState eventKeyRelease = new EventKeyMethodState();

    /**
     * Lets to describe the actions when you type text.
     */
    public EventInputTextMethodState eventTextInput = new EventInputTextMethodState();

    private void freeEvents() {
        eventFocusGet.clear();
        eventFocusLost.clear();
        eventResize.clear();
        eventDestroy.clear();

        eventMouseHover.clear();
        eventMouseClick.clear();
        eventMouseDoubleClick.clear();
        eventMousePress.clear();
        eventMouseDrag.clear();
        eventMouseDrop.clear();
        eventScrollUp.clear();
        eventScrollDown.clear();

        eventKeyPress.clear();
        eventKeyRelease.clear();

        eventTextInput.clear();
    }

    /**
     * Setting the border of the root item of the window.
     * @param border Border as com.spvessel.spacevil.Decorations.Border
     */
    public void setBorder(Border border) {
        if (windowLayout.getContainer() != null) {
            windowLayout.getContainer().setBorder(border);
        }
    }

    /**
     * Setting the color of the window border.
     * @param fill Color as java.awt.Color
     */
    public void setBorderFill(Color fill) {
        if (windowLayout.getContainer() != null) {
            windowLayout.getContainer().setBorderFill(fill);
        }
    }

    /**
     * Setting the color of the window border.
     * @param r Red (0 - 255)
     * @param g Green (0 - 255)
     * @param b Blue (0 - 255)
     */
    public void setBorderFill(int r, int g, int b) {
        if (windowLayout.getContainer() != null) {
            windowLayout.getContainer().setBorderFill(r, g, b);
        }
    }

    /**
     * Setting the color of the window border.
     * @param r Red (0 - 255)
     * @param g Green (0 - 255)
     * @param b Blue (0 - 255)
     * @param a Alpha (0 - 255)
     */
    public void setBorderFill(int r, int g, int b, int a) {
        if (windowLayout.getContainer() != null) {
            windowLayout.getContainer().setBorderFill(r, g, b, a);
        }
    }

    /**
     * Setting the color of the window border.
     * @param r Red (0.0f - 1.0f)
     * @param g Green (0.0f - 1.0f)
     * @param b Blue (0.0f - 1.0f)
     */
    public void setBorderFill(float r, float g, float b) {
        if (windowLayout.getContainer() != null) {
            windowLayout.getContainer().setBorderFill(r, g, b);
        }
    }

    /**
     * Setting the color of the window border.
     * @param r Red (0.0f - 1.0f)
     * @param g Green (0.0f - 1.0f)
     * @param b Blue (0.0f - 1.0f)
     * @param a Alpha (0.0f - 1.0f)
     */
    public void setBorderFill(float r, float g, float b, float a) {
        if (windowLayout.getContainer() != null) {
            windowLayout.getContainer().setBorderFill(r, g, b, a);
        }
    }

    /**
     * Setting the corner radii of the window border.
     * @param radius Corner radii as com.spvessel.spacevil.Decorations.CornerRadius
     */
    public void setBorderRadius(CornerRadius radius) {
        if (windowLayout.getContainer() != null) {
            windowLayout.getContainer().setBorderRadius(radius);
        }
    }

    /**
     * Setting the common corner radius of the window border.
     * @param radius The corner radius.
     */
    public void setBorderRadius(int radius) {
        if (windowLayout.getContainer() != null) {
            windowLayout.getContainer().setBorderRadius(new CornerRadius(radius));
        }
    }

    /**
     * Setting the window border thickness.
     * @param thickness The border thickness.
     */
    public void setBorderThickness(int thickness) {
        if (windowLayout.getContainer() != null) {
            windowLayout.getContainer().setBorderThickness(thickness);
        }
    }

    /**
     * Getting the current window border corner radii.
     * @return Corner radii as com.spvessel.spacevil.Decorations.CornerRadius
     */
    public CornerRadius getBorderRadius() {
        if (windowLayout.getContainer() != null) {
            return windowLayout.getContainer().getBorderRadius();
        }
        return null;
    }

    /**
     * Getting the current window border thickness.
     * @return The current thickness.
     */
    public int getBorderThickness() {
        if (windowLayout.getContainer() != null) {
            return windowLayout.getContainer().getBorderThickness();
        }
        return 0;
    }

    /**
     * Getting the current window border color.
     * @return The border color as java.awt.Color
     */
    public Color getBorderFill() {
        if (windowLayout.getContainer() != null) {
            return windowLayout.getContainer().getBorderFill();
        }
        return new Color(0, 0, 0, 0);
    }

    /**
     * Getting the GLFW ID of the window.
     * @return ID of the window.
     */
    public long getGLWID() {
        return getLayout().getGLWID();
    }

    boolean initEngine() {
        return windowLayout.initEngine();
    }

    void updateScene() {
        windowLayout.updateScene();
    }

    void dispose() {
        windowLayout.dispose();
    }

    CoreWindow getPairForCurrentWindow() {
        return windowLayout.getPairForCurrentWindow();
    }

    /**
     * Setting the dimmer color of the window. The dimmer appears when the current window opens a dialog window.
     * @param color The dimmer color as java.awt.Color
     */
    public void setShadeColor(Color color) {
        windowLayout.setShadeColor(color);
    }

    /**
     * Setting the dimmer color of the window. The dimmer appears when the current window opens a dialog window.
     * @param r Red (0 - 255)
     * @param g Green (0 - 255)
     * @param b Blue (0 - 255)
     */
    public void setShadeColor(int r, int g, int b) {
        windowLayout.setShadeColor(r, g, b);
    }

    /**
     * Setting the dimmer color of the window. The dimmer appears when the current window opens a dialog window.
     * @param r Red (0 - 255)
     * @param g Green (0 - 255)
     * @param b Blue (0 - 255)
     * @param a Alpha (0 - 255)
     */
    public void setShadeColor(int r, int g, int b, int a) {
        windowLayout.setShadeColor(r, g, b, a);
    }

    /**
     * Setting the dimmer color of the window. The dimmer appears when the current window opens a dialog window.
     * @param r Red (0.0f - 1.0f)
     * @param g Green (0.0f - 1.0f)
     * @param b Blue (0.0f - 1.0f)
     */
    public void setShadeColor(float r, float g, float b) {
        windowLayout.setShadeColor(r, g, b);
    }

    /**
     * Setting the dimmer color of the window. The dimmer appears when the current window opens a dialog window.
     * @param r Red (0.0f - 1.0f)
     * @param g Green (0.0f - 1.0f)
     * @param b Blue (0.0f - 1.0f)
     * @param a Alpha (0.0f - 1.0f)
     */
    public void setShadeColor(float r, float g, float b, float a) {
        windowLayout.setShadeColor(r, g, b, a);
    }

    /**
     * Getting the current dimmer color.
     * @return The dimmer color as java.awt.Color
     */
    public Color getShadeColor() {
        return windowLayout.getShadeColor();
    }

    <T> void freeVRAMResource(T resource) {
        getLayout().freeVRAMResource(resource);
    }

    /**
     * Getting the area of a primary monitor. The work area not occupied by global task bars or menu bars.
     * @return Work area as com.spvessel.spacevil.Core.Area
     */
    public Area getWorkArea() {
        long monitor = GLFW.glfwGetPrimaryMonitor();
        if (monitor != NULL) {
            IntBuffer x = BufferUtils.createIntBuffer(1);
            IntBuffer y = BufferUtils.createIntBuffer(1);
            IntBuffer w = BufferUtils.createIntBuffer(1);
            IntBuffer h = BufferUtils.createIntBuffer(1);
            GLFW.glfwGetMonitorWorkarea(monitor, x, y, w, h);
            return new Area(x.get(0), y.get(0), w.get(0), h.get(0));
        }
        return null;
    }

    private Scale _windowScale = new Scale();

    /**
     * Get DPI scale for the current window.
     * @return DPI scale as com.spvessel.spacevil.Core.Scale
     */
    public Scale getDpiScale()
    {
        return _windowScale;
    }
    
    void setWindowScale(float x, float y)
    {
        _windowScale.setScale(x, y);
    }
}