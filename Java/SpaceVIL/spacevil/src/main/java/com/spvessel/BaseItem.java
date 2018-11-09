package com.spvessel;

import com.spvessel.Core.*;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.GeometryEventType;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.ItemRule;
import com.spvessel.Flags.SizePolicy;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseItem implements InterfaceBaseItem {

    protected int _confines_x_0 = 0;
    protected int _confines_x_1 = 0;
    protected int _confines_y_0 = 0;
    protected int _confines_y_1 = 0;

    private WindowLayout _handler;

    public void setHandler(WindowLayout handler) {
        _handler = handler;
    }

    public WindowLayout getHandler() {
        return _handler;
    }

    // parent
    private Prototype _parent = null;

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

    protected void addChildren(InterfaceBaseItem item) {
        if (item.getParent() != null)
            item.getParent().removeItem(item);

        item.setParent(((VisualItem) this)._main);

        // refactor events verification
        if (item.getParent() instanceof InterfaceVLayout) {
            addEventListener(GeometryEventType.RESIZE_WIDTH, item);
            addEventListener(GeometryEventType.MOVED_X, item);
            castAndUpdate(item);
            return;
        }
        if (item.getParent() instanceof InterfaceHLayout) {
            addEventListener(GeometryEventType.RESIZE_HEIGHT, item);
            addEventListener(GeometryEventType.MOVED_Y, item);
            castAndUpdate(item);
            return;
        }
        if (item.getParent() instanceof InterfaceGrid) {
            return;
        }

        addEventListener(GeometryEventType.RESIZE_WIDTH, item);
        addEventListener(GeometryEventType.RESIZE_HEIGHT, item);
        addEventListener(GeometryEventType.MOVED_X, item);
        addEventListener(GeometryEventType.MOVED_Y, item);
        castAndUpdate(item);
    }

    protected void addEventListener(GeometryEventType type, InterfaceBaseItem listener) {
    }

    protected void removeEventListener(GeometryEventType type, InterfaceBaseItem listener) {
    }

    public void removeItemFromListeners() {
        getParent().removeEventListener(GeometryEventType.RESIZE_WIDTH, this);
        getParent().removeEventListener(GeometryEventType.RESIZE_HEIGHT, this);
        getParent().removeEventListener(GeometryEventType.MOVED_X, this);
        getParent().removeEventListener(GeometryEventType.MOVED_Y, this);
    }

    public void initElements() {
        // do nothing
    }

    private Item _item = new Item();

    private Indents _margin = new Indents();

    public Indents getMargin() {
        return _margin;
    }

    public void setMargin(Indents margin) {
        _margin = margin;
    }

    public void setMargin(int left, int top, int right, int bottom) {
        _margin = new Indents(left, top, right, bottom);
    }

    public List<float[]> getTriangles() {
        return _item.getTriangles();
    }

    public void setTriangles(List<float[]> triangles) {
        _item.setTriangles(triangles);
    }

    public List<float[]> makeShape() {
        return _item.makeShape();
    }

    protected List<float[]> updateShape() {
        if (getTriangles().size() == 0)
            return null;

        // clone triangles
        List<float[]> result = new LinkedList<float[]>();

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

    public void setBackground(Color color) {
        _item.setBackground(color);
    }

    public void setBackground(int r, int g, int b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        _item.setBackground(new Color(r, g, b, 255));
    }

    public void setBackground(int r, int g, int b, int a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        _item.setBackground(new Color(r, g, b, a));
    }

    public void setBackground(float r, float g, float b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        _item.setBackground(new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), 255));
    }

    public void setBackground(float r, float g, float b, float a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        _item.setBackground(new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), 255));
    }

    public Color getBackground() {
        return _item.getBackground();
    }

    public void setItemName(String name) {
        _item.setItemName(name);
    }

    public String getItemName() {
        return _item.getItemName();
    }

    private boolean _drawable = true;

    public boolean isDrawable() {
        return _drawable;
    }

    public void setDrawable(boolean value) {
        if (_drawable == value)
            return;
        _drawable = value;
    }

    private boolean _visible = true;

    public boolean isVisible() {
        return _visible;
    }

    public void setVisible(boolean value) {
        if (_visible == value)
            return;
        _visible = value;
    }

    protected void updateInnersDrawable(boolean value) {
    }

    // geometry
    private Geometry _itemGeometry = new Geometry();

    public void setMinWidth(int width) {
        _itemGeometry.setMinWidth(width);
    }

    public void setWidth(int width) {
        _itemGeometry.setWidth(width);
    }

    public void setMaxWidth(int width) {
        _itemGeometry.setMaxWidth(width);
    }

    public void setMinHeight(int height) {
        _itemGeometry.setMinHeight(height);
    }

    public void setHeight(int height) {
        _itemGeometry.setHeight(height);
    }

    public void setMaxHeight(int height) {
        _itemGeometry.setMaxHeight(height);
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

    public int getMinHeight() {
        return _itemGeometry.getMinHeight();
    }

    public int getHeight() {
        return _itemGeometry.getHeight();
    }

    public int getMaxHeight() {
        return _itemGeometry.getMaxHeight();
    }

    public void setSize(int width, int height) {
        _itemGeometry.setWidth(width);
        _itemGeometry.setHeight(height);
    }

    public void setMinSize(int width, int height) {
        _itemGeometry.setMinWidth(width);
        _itemGeometry.setMinHeight(height);
    }

    public void setMaxSize(int width, int height) {
        _itemGeometry.setMaxWidth(width);
        _itemGeometry.setMaxHeight(height);
    }

    public int[] getSize() {
        return _itemGeometry.getSize();
    }

    // behavior
    private Behavior _itemBehavior = new Behavior();

    public void setAlignment(ItemAlignment... alignment) {
        _itemBehavior.setAlignment(alignment);
        updateBehavior();
    }

    public void setAlignment(List<ItemAlignment> alignment) {
        _itemBehavior.setAlignment(alignment);
        updateBehavior();
    }

    protected void updateBehavior() {
        if (getParent() == null)
            return;

        if (this instanceof VisualItem) {
            protoUpdateBehavior();
            return;
        }

        List<ItemAlignment> alignment = getAlignment();

        if (alignment.contains(ItemAlignment.LEFT)) {
            setX(getParent().getX() + getParent().getPadding().left + getMargin().left);//
        }
        if (alignment.contains(ItemAlignment.RIGHT)) {
            setX(getParent().getX() + getParent().getWidth() - getWidth() - getParent().getPadding().right
                    - getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.TOP)) {
            setY(getParent().getY() + getParent().getPadding().top + getMargin().top);//
        }
        if (alignment.contains(ItemAlignment.BOTTOM)) {
            setY(getParent().getY() + getParent().getHeight() - getHeight() - getParent().getPadding().bottom
                    - getMargin().bottom);//
        }
        if (alignment.contains(ItemAlignment.HCENTER)) {
            setX(getParent().getX() + (getParent().getWidth() - getWidth()) / 2 + getMargin().left - getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.VCENTER)) {
            setY(getParent().getY() + (getParent().getHeight() - getHeight()) / 2 + getMargin().top
                    - getMargin().bottom);//
        }
    }

    private void protoUpdateBehavior() {
        Prototype prt = ((VisualItem) this)._main;

        List<ItemAlignment> alignment = prt.getAlignment();

        if (alignment.contains(ItemAlignment.LEFT)) {
            prt.setX(prt.getParent().getX() + prt.getParent().getPadding().left + prt.getMargin().left);//
        }
        if (alignment.contains(ItemAlignment.RIGHT)) {
            prt.setX(prt.getParent().getX() + prt.getParent().getWidth() - prt.getWidth()
                    - prt.getParent().getPadding().right - prt.getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.TOP)) {
            prt.setY(prt.getParent().getY() + prt.getParent().getPadding().top + prt.getMargin().top);//
        }
        if (alignment.contains(ItemAlignment.BOTTOM)) {
            prt.setY(prt.getParent().getY() + prt.getParent().getHeight() - prt.getHeight()
                    - prt.getParent().getPadding().bottom - prt.getMargin().bottom);//
        }
        if (alignment.contains(ItemAlignment.HCENTER)) {
            prt.setX(prt.getParent().getX() + (prt.getParent().getWidth() - prt.getWidth()) / 2 + prt.getMargin().left
                    - prt.getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.VCENTER)) {
            prt.setY(prt.getParent().getY() + (prt.getParent().getHeight() - prt.getHeight()) / 2 + prt.getMargin().top
                    - prt.getMargin().bottom);//
        }
    }

    public List<ItemAlignment> getAlignment() {
        return _itemBehavior.getAlignment();
    }

    public void setSizePolicy(SizePolicy width, SizePolicy height) {
        setWidthPolicy(width);
        setHeightPolicy(height);
    }

    public void setWidthPolicy(SizePolicy policy) {
        if (_itemBehavior.getWidthPolicy() != policy) {
            _itemBehavior.setWidthPolicy(policy);
        }
    }

    public SizePolicy getWidthPolicy() {
        return _itemBehavior.getWidthPolicy();
    }

    public void setHeightPolicy(SizePolicy policy) {
        if (_itemBehavior.getHeightPolicy() != policy) {
            _itemBehavior.setHeightPolicy(policy);
        }
    }

    public SizePolicy getHeightPolicy() {
        return _itemBehavior.getHeightPolicy();
    }

    // position
    private Position _itemPosition = new Position();

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

    protected boolean IsOutConfines() {
        if (getX() >= _confines_x_1 || getX() + getWidth() <= _confines_x_0 || getY() >= _confines_y_1
                || getY() + getHeight() <= _confines_y_0)
            return true;
        return false;
    }

    public void update(GeometryEventType type, int value) {
        if (this instanceof VisualItem) {
            protoUpdate(type, value);
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
                    setX(getParent().getX() + getParent().getWidth() - getWidth() - getParent().getPadding().right
                            - getMargin().right);//
                }
                if (getAlignment().contains(ItemAlignment.HCENTER)) {
                    setX(getParent().getX() + (getParent().getWidth() - getWidth()) / 2 + getMargin().left
                            - getMargin().right);
                }
            } else if (getWidthPolicy() == SizePolicy.EXPAND) {
                int prefered = getParent().getWidth() - getParent().getPadding().left - getParent().getPadding().right
                        - getMargin().right - getMargin().left;//
                prefered = (prefered > getMaxWidth()) ? getMaxWidth() : prefered;
                prefered = (prefered < getMinWidth()) ? getMinWidth() : prefered;
                setWidth(prefered);

                if (prefered + getParent().getPadding().left + getParent().getPadding().right + getMargin().right
                        + getMargin().left == getParent().getWidth())//
                {
                    setX(getParent().getX() + getParent().getPadding().left + getMargin().left);//
                } else if (prefered + getParent().getPadding().left + getParent().getPadding().right + getMargin().right
                        + getMargin().left < getParent().getWidth())//
                {
                    if (getAlignment().contains(ItemAlignment.RIGHT)) {
                        setX(getParent().getX() + getParent().getWidth() - getWidth() - getParent().getPadding().right
                                - getMargin().right);//
                    }
                    if (getAlignment().contains(ItemAlignment.HCENTER)) {
                        setX(getParent().getX() + (getParent().getWidth() - getWidth()) / 2 + getMargin().left);//
                    }
                } else if (prefered + getParent().getPadding().left + getParent().getPadding().right + getMargin().right
                        + getMargin().left > getParent().getWidth())//
                {
                    // никогда не должен зайти
                    setX(getParent().getX() + getParent().getPadding().left + getMargin().left);//
                    prefered = getParent().getWidth() - getParent().getPadding().left - getParent().getPadding().right
                            - getMargin().left - getMargin().right;//
                    setWidth(prefered);
                }
            }
            break;

        case RESIZE_HEIGHT:
            if (getHeightPolicy() == SizePolicy.FIXED) {
                if (getAlignment().contains(ItemAlignment.BOTTOM)) {
                    setY(getParent().getY() + getParent().getHeight() - getHeight() - getParent().getPadding().bottom
                            - getMargin().bottom);//
                }
                if (getAlignment().contains(ItemAlignment.VCENTER)) {
                    setY(getParent().getY() + (getParent().getHeight() - getHeight()) / 2 + getMargin().top
                            - getMargin().bottom);
                }
            } else if (getHeightPolicy() == SizePolicy.EXPAND) {
                int prefered = getParent().getHeight() - getParent().getPadding().top - getParent().getPadding().bottom
                        - getMargin().bottom - getMargin().top;//
                prefered = (prefered > getMaxHeight()) ? getMaxHeight() : prefered;
                prefered = (prefered < getMinHeight()) ? getMinHeight() : prefered;
                setHeight(prefered);

                if (prefered + getParent().getPadding().top + getParent().getPadding().bottom + getMargin().bottom
                        + getMargin().top == getParent().getHeight())//
                {
                    setY(getParent().getY() + getParent().getPadding().top + getMargin().top);//
                } else if (prefered + getParent().getPadding().top + getParent().getPadding().bottom
                        + getMargin().bottom + getMargin().top < getParent().getHeight())//
                {
                    if (getAlignment().contains(ItemAlignment.BOTTOM)) {
                        setY(getParent().getY() + getParent().getHeight() - getHeight()
                                - getParent().getPadding().bottom - getMargin().bottom);//
                    }
                    if (getAlignment().contains(ItemAlignment.VCENTER)) {
                        setY(getParent().getY() + (getParent().getHeight() - getHeight()) / 2 + getMargin().top);//
                    }
                } else if (prefered + getParent().getPadding().top + getParent().getPadding().bottom
                        + getMargin().bottom + getMargin().top > getParent().getHeight())//
                {
                    // никогда не должен зайти
                    setY(getParent().getY() + getParent().getPadding().top + getMargin().top);//
                    prefered = getParent().getHeight() - getParent().getPadding().top - getParent().getPadding().bottom
                            - getMargin().top - getMargin().bottom;//
                    setHeight(prefered);
                }
            }
            break;

        default:
            break;
        }
    }

    private void protoUpdate(GeometryEventType type, int value) {
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
                    prt.setX(prt.getParent().getX() + prt.getParent().getWidth() - prt.getWidth()
                            - prt.getParent().getPadding().right - prt.getMargin().right);//
                }
                if (prt.getAlignment().contains(ItemAlignment.HCENTER)) {
                    prt.setX(prt.getParent().getX() + (prt.getParent().getWidth() - prt.getWidth()) / 2
                            + prt.getMargin().left - prt.getMargin().right);
                }
            } else if (prt.getWidthPolicy() == SizePolicy.EXPAND) {
                int prefered = prt.getParent().getWidth() - prt.getParent().getPadding().left
                        - prt.getParent().getPadding().right - prt.getMargin().right - prt.getMargin().left;//
                prefered = (prefered > prt.getMaxWidth()) ? prt.getMaxWidth() : prefered;
                prefered = (prefered < prt.getMinWidth()) ? prt.getMinWidth() : prefered;
                prt.setWidth(prefered);

                if (prefered + prt.getParent().getPadding().left + prt.getParent().getPadding().right
                        + prt.getMargin().right + prt.getMargin().left == prt.getParent().getWidth())//
                {
                    prt.setX(prt.getParent().getX() + prt.getParent().getPadding().left + prt.getMargin().left);//
                } else if (prefered + prt.getParent().getPadding().left + prt.getParent().getPadding().right
                        + prt.getMargin().right + prt.getMargin().left < prt.getParent().getWidth())//
                {
                    if (prt.getAlignment().contains(ItemAlignment.RIGHT)) {
                        prt.setX(prt.getParent().getX() + prt.getParent().getWidth() - prt.getWidth()
                                - prt.getParent().getPadding().right - prt.getMargin().right);//
                    }
                    if (prt.getAlignment().contains(ItemAlignment.HCENTER)) {
                        prt.setX(prt.getParent().getX() + (prt.getParent().getWidth() - prt.getWidth()) / 2
                                + prt.getMargin().left);//
                    }
                } else if (prefered + prt.getParent().getPadding().left + prt.getParent().getPadding().right
                        + prt.getMargin().right + prt.getMargin().left > prt.getParent().getWidth())//
                {
                    // никогда не должен зайти
                    prt.setX(prt.getParent().getX() + prt.getParent().getPadding().left + prt.getMargin().left);//
                    prefered = prt.getParent().getWidth() - prt.getParent().getPadding().left
                            - prt.getParent().getPadding().right - prt.getMargin().left - prt.getMargin().right;//
                    prt.setWidth(prefered);
                }
            }
            break;

        case RESIZE_HEIGHT:
            if (prt.getHeightPolicy() == SizePolicy.FIXED) {
                if (prt.getAlignment().contains(ItemAlignment.BOTTOM)) {
                    prt.setY(prt.getParent().getY() + prt.getParent().getHeight() - prt.getHeight()
                            - prt.getParent().getPadding().bottom - prt.getMargin().bottom);//
                }
                if (prt.getAlignment().contains(ItemAlignment.VCENTER)) {
                    prt.setY(prt.getParent().getY() + (prt.getParent().getHeight() - prt.getHeight()) / 2
                            + prt.getMargin().top - prt.getMargin().bottom);
                }
            } else if (prt.getHeightPolicy() == SizePolicy.EXPAND) {
                int prefered = prt.getParent().getHeight() - prt.getParent().getPadding().top
                        - prt.getParent().getPadding().bottom - prt.getMargin().bottom - prt.getMargin().top;//
                prefered = (prefered > prt.getMaxHeight()) ? prt.getMaxHeight() : prefered;
                prefered = (prefered < prt.getMinHeight()) ? prt.getMinHeight() : prefered;
                prt.setHeight(prefered);

                if (prefered + prt.getParent().getPadding().top + prt.getParent().getPadding().bottom
                        + prt.getMargin().bottom + prt.getMargin().top == prt.getParent().getHeight())//
                {
                    prt.setY(prt.getParent().getY() + prt.getParent().getPadding().top + prt.getMargin().top);//
                } else if (prefered + prt.getParent().getPadding().top + prt.getParent().getPadding().bottom
                        + prt.getMargin().bottom + prt.getMargin().top < prt.getParent().getHeight())//
                {
                    if (prt.getAlignment().contains(ItemAlignment.BOTTOM)) {
                        prt.setY(prt.getParent().getY() + prt.getParent().getHeight() - prt.getHeight()
                                - prt.getParent().getPadding().bottom - prt.getMargin().bottom);//
                    }
                    if (prt.getAlignment().contains(ItemAlignment.VCENTER)) {
                        prt.setY(prt.getParent().getY() + (prt.getParent().getHeight() - prt.getHeight()) / 2
                                + prt.getMargin().top);//
                    }
                } else if (prefered + prt.getParent().getPadding().top + prt.getParent().getPadding().bottom
                        + prt.getMargin().bottom + prt.getMargin().top > prt.getParent().getHeight())//
                {
                    // никогда не должен зайти
                    prt.setY(prt.getParent().getY() + prt.getParent().getPadding().top + prt.getMargin().top);//
                    prefered = prt.getParent().getHeight() - prt.getParent().getPadding().top
                            - prt.getParent().getPadding().bottom - prt.getMargin().top - prt.getMargin().bottom;//
                    prt.setHeight(prefered);
                }
            }
            break;

        default:
            break;
        }
    }

    public void updateGeometry() {
        update(GeometryEventType.RESIZE_WIDTH, 0);
        update(GeometryEventType.RESIZE_HEIGHT, 0);
        update(GeometryEventType.MOVED_X, 0);
        update(GeometryEventType.MOVED_Y, 0);
    }

    public void setStyle(Style style) {
    }

    public abstract Style getCoreStyle();

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

    public boolean isShadowDrop() {
        return _is_shadow_drop;
    }

    public void setShadowDrop(boolean value) {
        _is_shadow_drop = value;
    }

    private float _shadow_radius = 1.0f;

    public void setShadowRadius(float radius) {
        _shadow_radius = radius;
    }

    public float getShadowRadius() {
        return _shadow_radius;
    }

    private Color _shadow_color = new Color(0, 0, 0);

    public Color getShadowColor() {
        return _shadow_color;
    }

    public void setShadowColor(Color color) {
        _shadow_color = color;
    }

    private Position _shadow_pos = new Position();

    public Position getShadowPos() {
        return _shadow_pos;
    }

    public void setShadow(float radius, int x, int y, Color color) {
        _is_shadow_drop = true;
        _shadow_radius = radius;
        _shadow_color = color;
        _shadow_pos.setX(x);
        _shadow_pos.setY(y);
    }

    // update
    public void setConfines() {
        _confines_x_0 = getParent().getX() + getParent().getPadding().left;
        _confines_x_1 = getParent().getX() + getParent().getWidth() - getParent().getPadding().right;
        _confines_y_0 = getParent().getY() + getParent().getPadding().top;
        _confines_y_1 = getParent().getY() + getParent().getHeight() - getParent().getPadding().bottom;
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
}