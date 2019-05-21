package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemRule;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseItem implements InterfaceBaseItem {

    int _confines_x_0 = 0;
    int _confines_x_1 = 0;
    int _confines_y_0 = 0;
    int _confines_y_1 = 0;

    private CoreWindow _coreWindow;

    /**
     * CoreWindow handler - window that contains the BaseItem
     */
    public void setHandler(CoreWindow handler) {
        _coreWindow = handler;
    }

    public CoreWindow getHandler() {
        return _coreWindow;
    }

    // parent
    private Prototype _parent = null;

    /**
     * BaseItem's parent item
     */
    public Prototype getParent() {
        return _parent;
    }

    public void setParent(Prototype parent) {
        _parent = parent;
    }

    private void castAndUpdate(InterfaceBaseItem item) {
        if (item instanceof Prototype)
            ((Prototype) item).getCore().updateBehavior();
        else
            ((BaseItem) item).updateBehavior();
    }

    void addChildren(InterfaceBaseItem item) {
        Prototype itemParent = item.getParent();
        if (itemParent != null)
            itemParent.removeItem(item);

        item.setParent(((VisualItem) this)._main);
        itemParent = item.getParent();

        // refactor events verification
        if (itemParent instanceof InterfaceVLayout) {
            addEventListener(GeometryEventType.RESIZE_WIDTH, item);
            addEventListener(GeometryEventType.MOVED_X, item);
            castAndUpdate(item);
            return;
        }
        if (itemParent instanceof InterfaceHLayout) {
            addEventListener(GeometryEventType.RESIZE_HEIGHT, item);
            addEventListener(GeometryEventType.MOVED_Y, item);
            castAndUpdate(item);
            return;
        }
        if (itemParent instanceof InterfaceGrid) {
            return;
        }
        if (itemParent instanceof InterfaceFree) {
            return;
        }

        addEventListener(GeometryEventType.RESIZE_WIDTH, item);
        addEventListener(GeometryEventType.RESIZE_HEIGHT, item);
        addEventListener(GeometryEventType.MOVED_X, item);
        addEventListener(GeometryEventType.MOVED_Y, item);
        castAndUpdate(item);
    }

    void addEventListener(GeometryEventType type, InterfaceBaseItem listener) {
    }

    void removeEventListener(GeometryEventType type, InterfaceBaseItem listener) {
    }

    /**
     * Item will not react on parent's changes
     */
    public void removeItemFromListeners() {
        Prototype parent = getParent();
        parent.removeEventListener(GeometryEventType.RESIZE_WIDTH, this);
        parent.removeEventListener(GeometryEventType.RESIZE_HEIGHT, this);
        parent.removeEventListener(GeometryEventType.MOVED_X, this);
        parent.removeEventListener(GeometryEventType.MOVED_Y, this);
    }

    /**
     * Initialization and adding of all elements in the BaseItem
     */
    public void initElements() {
        // do nothing
    }

    private Item _item = new Item();

    private Indents _margin = new Indents();

    /**
     * BaseItem margin
     */
    public Indents getMargin() {
        return _margin;
    }

    public void setMargin(Indents margin) {
        _margin = margin;
        updateGeometry();

        Prototype parent = getParent();
        if (parent == null) {
            return;
        }

        boolean hLayout = parent instanceof InterfaceHLayout;
        boolean vLayout = parent instanceof InterfaceVLayout;
        boolean grid = parent instanceof InterfaceGrid;

        if (!hLayout && !vLayout && !grid)
            updateBehavior();

        if (hLayout)
            ((InterfaceHLayout) parent).updateLayout();
        if (vLayout)
            ((InterfaceVLayout) parent).updateLayout();
        if (grid)
            ((InterfaceGrid) parent).updateLayout();
    }

    public void setMargin(int left, int top, int right, int bottom) {
        setMargin(new Indents(left, top, right, bottom));
        // _margin = new Indents(left, top, right, bottom);
        // updateGeometry();

        // Prototype parent = getParent();
        // if (parent == null) {
        // return;
        // }

        // boolean hLayout = parent instanceof InterfaceHLayout;
        // boolean vLayout = parent instanceof InterfaceVLayout;
        // boolean grid = parent instanceof InterfaceGrid;

        // if (!hLayout && !vLayout && !grid)
        // updateBehavior();

        // if (hLayout)
        // ((InterfaceHLayout) parent).updateLayout();
        // if (vLayout)
        // ((InterfaceVLayout) parent).updateLayout();
        // if (grid)
        // ((InterfaceGrid) parent).updateLayout();
    }

    /**
     * @return triangles list of the BaseItem's shape
     */
    public List<float[]> getTriangles() {
        return _item.getTriangles();
    }

    /**
     * Sets BaseItem's shape as triangles list
     */
    public void setTriangles(List<float[]> triangles) {
        _item.setTriangles(triangles);
    }

    /**
     * @return shape points list in GL coordinates, using triangles from
     *         getTriangles()
     */
    public List<float[]> makeShape() {
        return _item.makeShape();
    }

    List<float[]> updateShape() {
        if (getTriangles().size() == 0)
            return null;

        // clone triangles
        List<float[]> result = new LinkedList<>();

        for (int i = 0; i < getTriangles().size(); i++) {
            result.add(new float[] { getTriangles().get(i)[0], getTriangles().get(i)[1], getTriangles().get(i)[2] });
        }

        // List<float[]> result = getTriangles();
        // max and min
        Float maxX = result.stream().map(i -> i[0]).max(Float::compare).get();
        Float maxY = result.stream().map(i -> i[1]).max(Float::compare).get();
        Float minX = result.stream().map(i -> i[0]).min(Float::compare).get();
        Float minY = result.stream().map(i -> i[1]).min(Float::compare).get();

        // to the left top corner
        for (float[] item : result) {
            item[0] = (item[0] - minX) * getWidth() / (maxX - minX) + getX();
            item[1] = (item[1] - minY) * getHeight() / (maxY - minY) + getY();
        }

        return result;
    }

    /**
     * Background color of the BaseItem
     */
    public void setBackground(Color color) {
        _item.setBackground(color);
    }

    public void setBackground(int r, int g, int b) {
        _item.setBackground(GraphicsMathService.colorTransform(r, g, b));
    }

    public void setBackground(int r, int g, int b, int a) {
        _item.setBackground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    public void setBackground(float r, float g, float b) {
        _item.setBackground(GraphicsMathService.colorTransform(r, g, b));
    }

    public void setBackground(float r, float g, float b, float a) {
        _item.setBackground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    public Color getBackground() {
        return _item.getBackground();
    }

    /**
     * BaseItem's name
     */
    public void setItemName(String name) {
        _item.setItemName(name);
    }

    public String getItemName() {
        return _item.getItemName();
    }

    private boolean _drawable = true;

    /**
     * If BaseItem will drawn by engine
     */
    public boolean isDrawable() {
        return _drawable;
    }

    public void setDrawable(boolean value) {
        if (_drawable == value)
            return;
        _drawable = value;
    }

    private boolean _visible = true;

    /**
     * If BaseItem is visible
     */
    public boolean isVisible() {
        return _visible;
    }

    public void setVisible(boolean value) {
        if (_visible == value)
            return;
        _visible = value;
    }

    void updateInnersDrawable(boolean value) {
    }

    // geometry
    private Geometry _itemGeometry = new Geometry();

    /**
     * Width of the BaseItem
     */
    public void setMinWidth(int width) {
        _itemGeometry.setMinWidth(width);
    }

    public void setWidth(int width) {
        _itemGeometry.setWidth(width);
    }

    public void setMaxWidth(int width) {
        _itemGeometry.setMaxWidth(width);
    }

    public int getMinWidth() {
        return _itemGeometry.getMinWidth();
    }

    public int getWidth() {
        return _itemGeometry.getWidth();
    }

    public int getMaxWidth() {
        return _itemGeometry.getMaxWidth();
    }

    /**
     * Height of the BaseItem
     */
    public void setMinHeight(int height) {
        _itemGeometry.setMinHeight(height);
    }

    public void setHeight(int height) {
        _itemGeometry.setHeight(height);
    }

    public void setMaxHeight(int height) {
        _itemGeometry.setMaxHeight(height);
    }

    public int getMinHeight() {
        return _itemGeometry.getMinHeight();
    }

    public int getHeight() {
        return _itemGeometry.getHeight();
    }

    public int getMaxHeight() {
        return _itemGeometry.getMaxHeight();
    }

    /**
     * Size (width and height) of the BaseItem
     */
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void setMinSize(int width, int height) {
        setMinWidth(width);
        setMinHeight(height);
    }

    public void setMaxSize(int width, int height) {
        setMaxWidth(width);
        setMaxHeight(height);
    }

    public int[] getSize() {
        return _itemGeometry.getSize();
    }

    public int[] getMinSize() {
        return new int[] { _itemGeometry.getMinWidth(), _itemGeometry.getMinHeight() };
    }

    public int[] getMaxSize() {
        return new int[] { _itemGeometry.getMaxWidth(), _itemGeometry.getMaxHeight() };
    }

    // behavior
    private Behavior _itemBehavior = new Behavior();

    /**
     * BaseItem alignment
     */
    public void setAlignment(ItemAlignment... alignment) {
        setAlignment(Arrays.asList(alignment));
        // _itemBehavior.setAlignment(alignment);
        // updateGeometry();
        //
        // Prototype parent = getParent();
        // if (parent == null) {
        // return;
        // }
        //
        // boolean hLayout = parent instanceof InterfaceHLayout;
        // boolean vLayout = parent instanceof InterfaceVLayout;
        // boolean grid = parent instanceof InterfaceGrid;
        //
        // if (!hLayout && !vLayout && !grid)
        // updateBehavior();
        //
        // if (hLayout)
        // ((InterfaceHLayout) parent).updateLayout();
        // if (vLayout)
        // ((InterfaceVLayout) parent).updateLayout();
        // if (grid)
        // ((InterfaceGrid) parent).updateLayout();
    }

    public void setAlignment(List<ItemAlignment> alignment) {
        _itemBehavior.setAlignment(alignment);
        updateGeometry();

        Prototype parent = getParent();
        if (parent == null) {
            return;
        }

        boolean hLayout = parent instanceof InterfaceHLayout;
        boolean vLayout = parent instanceof InterfaceVLayout;
        boolean grid = parent instanceof InterfaceGrid;

        if (!hLayout && !vLayout && !grid)
            updateBehavior();

        if (hLayout)
            ((InterfaceHLayout) parent).updateLayout();
        if (vLayout)
            ((InterfaceVLayout) parent).updateLayout();
        if (grid)
            ((InterfaceGrid) parent).updateLayout();
    }

    public List<ItemAlignment> getAlignment() {
        return _itemBehavior.getAlignment();
    }

    void updateBehavior() {
        Prototype parent = getParent();
        if (parent == null)
            return;

        if (this instanceof VisualItem) {
            protoUpdateBehavior(parent);
            return;
        }

        List<ItemAlignment> alignment = getAlignment();

        if (alignment.contains(ItemAlignment.LEFT)) {
            setX(parent.getX() + parent.getPadding().left + getMargin().left);//
        }
        if (alignment.contains(ItemAlignment.RIGHT)) {
            setX(parent.getX() + parent.getWidth() - getWidth() - parent.getPadding().right - getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.TOP)) {
            setY(parent.getY() + parent.getPadding().top + getMargin().top);//
        }
        if (alignment.contains(ItemAlignment.BOTTOM)) {
            setY(parent.getY() + parent.getHeight() - getHeight() - parent.getPadding().bottom - getMargin().bottom);//
        }
        if (alignment.contains(ItemAlignment.HCENTER)) {
            setX(parent.getX() + (parent.getWidth() - getWidth()) / 2 + getMargin().left - getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.VCENTER)) {
            setY(parent.getY() + (parent.getHeight() - getHeight()) / 2 + getMargin().top - getMargin().bottom);//
        }
    }

    private void protoUpdateBehavior(Prototype parent) {
        Prototype prt = ((VisualItem) this)._main;

        List<ItemAlignment> alignment = prt.getAlignment();

        if (alignment.contains(ItemAlignment.LEFT)) {
            prt.setX(parent.getX() + parent.getPadding().left + prt.getMargin().left);//
        }
        if (alignment.contains(ItemAlignment.RIGHT)) {
            prt.setX(parent.getX() + parent.getWidth() - prt.getWidth() - parent.getPadding().right
                    - prt.getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.TOP)) {
            prt.setY(parent.getY() + parent.getPadding().top + prt.getMargin().top);//
        }
        if (alignment.contains(ItemAlignment.BOTTOM)) {
            prt.setY(parent.getY() + parent.getHeight() - prt.getHeight() - parent.getPadding().bottom
                    - prt.getMargin().bottom);//
        }
        if (alignment.contains(ItemAlignment.HCENTER)) {
            prt.setX(parent.getX() + (parent.getWidth() - prt.getWidth()) / 2 + prt.getMargin().left
                    - prt.getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.VCENTER)) {
            prt.setY(parent.getY() + (parent.getHeight() - prt.getHeight()) / 2 + prt.getMargin().top
                    - prt.getMargin().bottom);//
        }
    }

    /**
     * BaseItem size (width and height) policy - FIXED or EXPAND
     */
    public void setSizePolicy(SizePolicy width, SizePolicy height) {
        setWidthPolicy(width);
        setHeightPolicy(height);
    }

    public void setWidthPolicy(SizePolicy policy) {
        if (_itemBehavior.getWidthPolicy() != policy) {
            _itemBehavior.setWidthPolicy(policy);

            if (this instanceof VisualItem) {
                VisualItem vItem = (VisualItem) this;
                Prototype protoItem = vItem._main;

                if (protoItem instanceof InterfaceFloating) {
                    if (policy == SizePolicy.EXPAND)
                        ItemsLayoutBox.subscribeWindowSizeMonitoring(protoItem, GeometryEventType.RESIZE_WIDTH);
                    else
                        ItemsLayoutBox.unsubscribeWindowSizeMonitoring(protoItem, GeometryEventType.RESIZE_WIDTH);
                    updateGeometry();
                }
            }

            Prototype parent = getParent();
            if (parent == null) {
                return;
            }

            boolean hLayout = parent instanceof InterfaceHLayout;
            boolean vLayout = parent instanceof InterfaceVLayout;
            boolean grid = parent instanceof InterfaceGrid;

            if (!hLayout && !vLayout && !grid)
                updateBehavior();

            if (hLayout)
                ((InterfaceHLayout) parent).updateLayout();
            if (vLayout)
                ((InterfaceVLayout) parent).updateLayout();
            if (grid)
                ((InterfaceGrid) parent).updateLayout();
        }
    }

    public SizePolicy getWidthPolicy() {
        return _itemBehavior.getWidthPolicy();
    }

    public void setHeightPolicy(SizePolicy policy) {
        if (_itemBehavior.getHeightPolicy() != policy) {
            _itemBehavior.setHeightPolicy(policy);

            if (this instanceof VisualItem) {
                VisualItem vItem = (VisualItem) this;
                Prototype protoItem = vItem._main;

                if (protoItem instanceof InterfaceFloating) {
                    if (policy == SizePolicy.EXPAND)
                        ItemsLayoutBox.subscribeWindowSizeMonitoring(protoItem, GeometryEventType.RESIZE_HEIGHT);
                    else
                        ItemsLayoutBox.unsubscribeWindowSizeMonitoring(protoItem, GeometryEventType.RESIZE_HEIGHT);
                    updateGeometry();
                }
            }

            Prototype parent = getParent();
            if (parent == null) {
                return;
            }

            boolean hLayout = parent instanceof InterfaceHLayout;
            boolean vLayout = parent instanceof InterfaceVLayout;
            boolean grid = parent instanceof InterfaceGrid;

            if (!hLayout && !vLayout && !grid)
                updateBehavior();

            if (hLayout)
                ((InterfaceHLayout) parent).updateLayout();
            if (vLayout)
                ((InterfaceVLayout) parent).updateLayout();
            if (grid)
                ((InterfaceGrid) parent).updateLayout();
        }
    }

    public SizePolicy getHeightPolicy() {
        return _itemBehavior.getHeightPolicy();
    }

    // position
    private Position _itemPosition = new Position();

    /**
     * BaseItem (x, y) position
     */
    public void setX(int x) {
        _itemPosition.setX(x);
    }

    public int getX() {
        return _itemPosition.getX();
    }

    public void setY(int y) {
        _itemPosition.setY(y);
    }

    public int getY() {
        return _itemPosition.getY();
    }

    // protected boolean IsOutConfines() {
    // if (getX() >= _confines_x_1 || getX() + getWidth() <= _confines_x_0 || getY()
    // >= _confines_y_1
    // || getY() + getHeight() <= _confines_y_0)
    // return true;
    // return false;
    // }

    /**
     * Update BaseItem's state
     */
    public void update(GeometryEventType type, int value) {
        Prototype parent = getParent();

        if (parent == null)
            return;

        if (this instanceof VisualItem) {
            protoUpdate(type, value, parent);
            return;
        }

        setConfines();
        switch (type) {
        case MOVED_X:
            setX(getX() + value);
            break;

        case MOVED_Y:
            setY(getY() + value);
            break;

        case RESIZE_WIDTH:
            if (getWidthPolicy() == SizePolicy.FIXED) {
                if (getAlignment().contains(ItemAlignment.RIGHT)) {
                    setX(parent.getX() + parent.getWidth() - getWidth() - parent.getPadding().right
                            - getMargin().right);//
                }
                if (getAlignment().contains(ItemAlignment.HCENTER)) {
                    setX(parent.getX() + (parent.getWidth() - getWidth()) / 2 + getMargin().left - getMargin().right);
                }
            } else if (getWidthPolicy() == SizePolicy.EXPAND) {
                int prefered = parent.getWidth() - parent.getPadding().left - parent.getPadding().right
                        - getMargin().right - getMargin().left;//
                prefered = (prefered > getMaxWidth()) ? getMaxWidth() : prefered;
                prefered = (prefered < getMinWidth()) ? getMinWidth() : prefered;
                setWidth(prefered);

                if (prefered + parent.getPadding().left + parent.getPadding().right + getMargin().right
                        + getMargin().left == parent.getWidth())//
                {
                    setX(parent.getX() + parent.getPadding().left + getMargin().left);//
                } else if (prefered + parent.getPadding().left + parent.getPadding().right + getMargin().right
                        + getMargin().left < parent.getWidth())//
                {
                    if (getAlignment().contains(ItemAlignment.RIGHT)) {
                        setX(parent.getX() + parent.getWidth() - getWidth() - parent.getPadding().right
                                - getMargin().right);//
                    }
                    if (getAlignment().contains(ItemAlignment.HCENTER)) {
                        setX(parent.getX() + (parent.getWidth() - getWidth()) / 2 + getMargin().left);//
                    }
                } else if (prefered + parent.getPadding().left + parent.getPadding().right + getMargin().right
                        + getMargin().left > parent.getWidth())//
                {
                    // никогда не должен зайти
                    setX(parent.getX() + parent.getPadding().left + getMargin().left);//
                    prefered = parent.getWidth() - parent.getPadding().left - parent.getPadding().right
                            - getMargin().left - getMargin().right;//
                    setWidth(prefered);
                }
            }
            break;

        case RESIZE_HEIGHT:
            if (getHeightPolicy() == SizePolicy.FIXED) {
                if (getAlignment().contains(ItemAlignment.BOTTOM)) {
                    setY(parent.getY() + parent.getHeight() - getHeight() - parent.getPadding().bottom
                            - getMargin().bottom);//
                }
                if (getAlignment().contains(ItemAlignment.VCENTER)) {
                    setY(parent.getY() + (parent.getHeight() - getHeight()) / 2 + getMargin().top - getMargin().bottom);
                }
            } else if (getHeightPolicy() == SizePolicy.EXPAND) {
                int prefered = parent.getHeight() - parent.getPadding().top - parent.getPadding().bottom
                        - getMargin().bottom - getMargin().top;//
                prefered = (prefered > getMaxHeight()) ? getMaxHeight() : prefered;
                prefered = (prefered < getMinHeight()) ? getMinHeight() : prefered;
                setHeight(prefered);

                if (prefered + parent.getPadding().top + parent.getPadding().bottom + getMargin().bottom
                        + getMargin().top == parent.getHeight())//
                {
                    setY(parent.getY() + parent.getPadding().top + getMargin().top);//
                } else if (prefered + parent.getPadding().top + parent.getPadding().bottom + getMargin().bottom
                        + getMargin().top < parent.getHeight())//
                {
                    if (getAlignment().contains(ItemAlignment.BOTTOM)) {
                        setY(parent.getY() + parent.getHeight() - getHeight() - parent.getPadding().bottom
                                - getMargin().bottom);//
                    }
                    if (getAlignment().contains(ItemAlignment.VCENTER)) {
                        setY(parent.getY() + (parent.getHeight() - getHeight()) / 2 + getMargin().top);//
                    }
                } else if (prefered + parent.getPadding().top + parent.getPadding().bottom + getMargin().bottom
                        + getMargin().top > parent.getHeight())//
                {
                    // никогда не должен зайти
                    setY(parent.getY() + parent.getPadding().top + getMargin().top);//
                    prefered = parent.getHeight() - parent.getPadding().top - parent.getPadding().bottom
                            - getMargin().top - getMargin().bottom;//
                    setHeight(prefered);
                }
            }
            break;

        default:
            break;
        }
    }

    private void protoUpdate(GeometryEventType type, int value, Prototype parent) {
        Prototype prt = ((VisualItem) this)._main;

        prt.setConfines();
        switch (type) {
        case MOVED_X:
            prt.setX(getX() + value);
            break;

        case MOVED_Y:
            prt.setY(getY() + value);
            break;

        case RESIZE_WIDTH:
            if (prt.getWidthPolicy() == SizePolicy.FIXED) {
                if (prt.getAlignment().contains(ItemAlignment.RIGHT)) {
                    prt.setX(parent.getX() + parent.getWidth() - prt.getWidth() - parent.getPadding().right
                            - prt.getMargin().right);//
                }
                if (prt.getAlignment().contains(ItemAlignment.HCENTER)) {
                    prt.setX(parent.getX() + (parent.getWidth() - prt.getWidth()) / 2 + prt.getMargin().left
                            - prt.getMargin().right);
                }
            } else if (prt.getWidthPolicy() == SizePolicy.EXPAND) {
                int prefered = parent.getWidth() - parent.getPadding().left - parent.getPadding().right
                        - prt.getMargin().right - prt.getMargin().left;//
                prefered = (prefered > prt.getMaxWidth()) ? prt.getMaxWidth() : prefered;
                prefered = (prefered < prt.getMinWidth()) ? prt.getMinWidth() : prefered;
                prt.setWidth(prefered);

                if (prefered + parent.getPadding().left + parent.getPadding().right + prt.getMargin().right
                        + prt.getMargin().left == parent.getWidth())//
                {
                    prt.setX(parent.getX() + parent.getPadding().left + prt.getMargin().left);//
                } else if (prefered + parent.getPadding().left + parent.getPadding().right + prt.getMargin().right
                        + prt.getMargin().left < parent.getWidth())//
                {
                    if (prt.getAlignment().contains(ItemAlignment.RIGHT)) {
                        prt.setX(parent.getX() + parent.getWidth() - prt.getWidth() - parent.getPadding().right
                                - prt.getMargin().right);//
                    }
                    if (prt.getAlignment().contains(ItemAlignment.HCENTER)) {
                        prt.setX(parent.getX() + (parent.getWidth() - prt.getWidth()) / 2 + prt.getMargin().left);//
                    }
                } else if (prefered + parent.getPadding().left + parent.getPadding().right + prt.getMargin().right
                        + prt.getMargin().left > parent.getWidth())//
                {
                    // никогда не должен зайти
                    prt.setX(parent.getX() + parent.getPadding().left + prt.getMargin().left);//
                    prefered = parent.getWidth() - parent.getPadding().left - parent.getPadding().right
                            - prt.getMargin().left - prt.getMargin().right;//
                    prt.setWidth(prefered);
                }
            }
            break;

        case RESIZE_HEIGHT:
            if (prt.getHeightPolicy() == SizePolicy.FIXED) {
                if (prt.getAlignment().contains(ItemAlignment.BOTTOM)) {
                    prt.setY(parent.getY() + parent.getHeight() - prt.getHeight() - parent.getPadding().bottom
                            - prt.getMargin().bottom);//
                }
                if (prt.getAlignment().contains(ItemAlignment.VCENTER)) {
                    prt.setY(parent.getY() + (parent.getHeight() - prt.getHeight()) / 2 + prt.getMargin().top
                            - prt.getMargin().bottom);
                }
            } else if (prt.getHeightPolicy() == SizePolicy.EXPAND) {
                int prefered = parent.getHeight() - parent.getPadding().top - parent.getPadding().bottom
                        - prt.getMargin().bottom - prt.getMargin().top;//
                prefered = (prefered > prt.getMaxHeight()) ? prt.getMaxHeight() : prefered;
                prefered = (prefered < prt.getMinHeight()) ? prt.getMinHeight() : prefered;
                prt.setHeight(prefered);

                if (prefered + parent.getPadding().top + parent.getPadding().bottom + prt.getMargin().bottom
                        + prt.getMargin().top == parent.getHeight())//
                {
                    prt.setY(parent.getY() + parent.getPadding().top + prt.getMargin().top);//
                } else if (prefered + parent.getPadding().top + parent.getPadding().bottom + prt.getMargin().bottom
                        + prt.getMargin().top < parent.getHeight())//
                {
                    if (prt.getAlignment().contains(ItemAlignment.BOTTOM)) {
                        prt.setY(parent.getY() + parent.getHeight() - prt.getHeight() - parent.getPadding().bottom
                                - prt.getMargin().bottom);//
                    }
                    if (prt.getAlignment().contains(ItemAlignment.VCENTER)) {
                        prt.setY(parent.getY() + (parent.getHeight() - prt.getHeight()) / 2 + prt.getMargin().top);//
                    }
                } else if (prefered + parent.getPadding().top + parent.getPadding().bottom + prt.getMargin().bottom
                        + prt.getMargin().top > parent.getHeight())//
                {
                    // никогда не должен зайти
                    prt.setY(parent.getY() + parent.getPadding().top + prt.getMargin().top);//
                    prefered = parent.getHeight() - parent.getPadding().top - parent.getPadding().bottom
                            - prt.getMargin().top - prt.getMargin().bottom;//
                    prt.setHeight(prefered);
                }
            }
            break;

        default:
            break;
        }
    }

    void updateGeometry() {
        update(GeometryEventType.RESIZE_WIDTH, 0);
        update(GeometryEventType.RESIZE_HEIGHT, 0);
        update(GeometryEventType.MOVED_X, 0);
        update(GeometryEventType.MOVED_Y, 0);
    }

    /**
     * Style of the BaseItem
     */
    public void setStyle(Style style) {
    }

    public abstract Style getCoreStyle();

    /**
     * Check and set BaseItem default style
     */
    public void CheckDefaults() {
        // checking all attributes
        // setStyle(default theme)
        // foreach inners setStyle(from item default style)

        setDefaults();
    }

    public void setDefaults() {
    }

    public ItemRule HoverRule = ItemRule.LAZY;

    // shadow
    private boolean _is_shadow_drop = false;
    private int _shadow_radius = 1;
    private Color _shadow_color = new Color(0, 0, 0);
    private Position _shadow_pos = new Position();

    /**
     * BaseItem's shadow parameters. Is item has shadow
     */
    public boolean isShadowDrop() {
        return _is_shadow_drop;
    }

    public void setShadowDrop(boolean value) {
        _is_shadow_drop = value;
    }

    /**
     * BaseItem's shadow parameters. Shadow corners radius
     */
    public void setShadowRadius(int radius) {
        _shadow_radius = radius;
    }

    public int getShadowRadius() {
        return _shadow_radius;
    }

    /**
     * BaseItem's shadow parameters. Shadow color
     */
    public Color getShadowColor() {
        return _shadow_color;
    }

    public void setShadowColor(Color color) {
        _shadow_color = color;
    }

    /**
     * BaseItem's shadow parameters. Shadow position
     */
    public Position getShadowPos() {
        return _shadow_pos;
    }

    private int _xExtension = 0;
    private int _yExtension = 0;

    public int[] getShadowExtension() {
        return new int[] { _xExtension, _yExtension };
    }

    public void setShadowExtension(int wExtension, int hExtension) {
        _xExtension = wExtension;
        _yExtension = hExtension;
    }

    /**
     * Set BaseItem's shadow with parameters
     *
     * @param radius Shadow corners radius (same for all corners)
     * @param x      Shadow X position
     * @param y      Shadow Y position
     * @param color  Shadow color
     */
    public void setShadow(int radius, int x, int y, Color color) {
        _is_shadow_drop = true;
        _shadow_radius = radius;
        _shadow_color = color;
        _shadow_pos.setX(x);
        _shadow_pos.setY(y);
    }

    public void setShadow(Shadow shadow) {
        _is_shadow_drop = shadow.isDrop();
        _shadow_radius = shadow.getRadius();
        _shadow_color = shadow.getColor();
        _shadow_pos.setX(shadow.getXOffset());
        _shadow_pos.setY(shadow.getYOffset());
    }

    // update

    /**
     * BaseItem confines
     */
    public void setConfines() {
        Prototype parent = getParent();
        if (parent == null)
            return;
        _confines_x_0 = parent.getX() + parent.getPadding().left;
        _confines_x_1 = parent.getX() + parent.getWidth() - parent.getPadding().right;
        _confines_y_0 = parent.getY() + parent.getPadding().top;
        _confines_y_1 = parent.getY() + parent.getHeight() - parent.getPadding().bottom;
    }

    public void setConfines(int x0, int x1, int y0, int y1) {
        _confines_x_0 = x0;
        _confines_x_1 = x1;
        _confines_y_0 = y0;
        _confines_y_1 = y1;
    }

    public int[] getConfines() {
        return new int[] { _confines_x_0, _confines_x_1, _confines_y_0, _confines_y_1 };
    }

    public void release() {
    }
}