package com.spvessel.spacevil;

import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceFreeLayout;
import com.spvessel.spacevil.Core.InterfaceHLayout;
import com.spvessel.spacevil.Core.InterfaceVLayout;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

final class BaseItemStatics {
    private BaseItemStatics() {
    }

    static void updateAllLayout(InterfaceBaseItem item) {
        Prototype parent = item.getParent();
        if (parent == null) {
            return;
        }

        boolean hLayout = parent instanceof InterfaceHLayout;
        boolean vLayout = parent instanceof InterfaceVLayout;
        boolean grid = parent instanceof InterfaceFreeLayout;

        if (!hLayout && !vLayout && !grid)
            updateBehavior(item);

        if (hLayout)
            ((InterfaceHLayout) parent).updateLayout();
        if (vLayout)
            ((InterfaceVLayout) parent).updateLayout();
        if (grid)
            ((InterfaceFreeLayout) parent).updateLayout();
    }

    static void updateHLayout(InterfaceBaseItem item) {
        Prototype parent = item.getParent();
        if (parent == null) {
            return;
        }

        if (item.getWidthPolicy() == SizePolicy.FIXED) {
            boolean hLayout = parent instanceof InterfaceHLayout;
            boolean grid = parent instanceof InterfaceFreeLayout;

            if (!hLayout && !grid)
                updateBehavior(item);

            if (hLayout)
                ((InterfaceHLayout) parent).updateLayout();
            if (grid)
                ((InterfaceFreeLayout) parent).updateLayout();
        }
    }

    static void updateVLayout(InterfaceBaseItem item) {
        Prototype parent = item.getParent();
        if (parent == null) {
            return;
        }
        if (item.getHeightPolicy() == SizePolicy.FIXED) {
            boolean vLayout = parent instanceof InterfaceVLayout;
            boolean grid = parent instanceof InterfaceFreeLayout;

            if (!vLayout && !grid)
                updateBehavior(item);

            if (vLayout)
                ((InterfaceVLayout) parent).updateLayout();
            if (grid)
                ((InterfaceFreeLayout) parent).updateLayout();
        }
    }

    static void castToUpdateBehavior(InterfaceBaseItem item) {
        if (item instanceof Prototype)
            updateBehavior(((Prototype) item).getCore());
        else
            updateBehavior(item);
    }

    static void castToUpdateGeometry(InterfaceBaseItem item) {
        if (item instanceof Prototype)
            updateGeometry(((Prototype) item).getCore());
        else
            updateGeometry(item);
    }

    static void updateGeometry(InterfaceBaseItem item) {
        updateGeometryAttr(item, GeometryEventType.RESIZE_WIDTH, 0);
        updateGeometryAttr(item, GeometryEventType.RESIZE_HEIGHT, 0);
        updateGeometryAttr(item, GeometryEventType.MOVED_X, 0);
        updateGeometryAttr(item, GeometryEventType.MOVED_Y, 0);
    }

    static void updateBehavior(InterfaceBaseItem item) {
        Prototype parent = item.getParent();
        if (parent == null)
            return;

        if (item instanceof VisualItem) {
            updatePrototypeBehavior(item, parent);
        } else {
            updateBaseItemBehavior(item, parent);
        }
    }

    static void updatePrototypeBehavior(InterfaceBaseItem item, Prototype parent) {
        Prototype prt = ((VisualItem) item).prototype;
        updateBaseItemBehavior(prt, parent);
    }

    static void updateBaseItemBehavior(InterfaceBaseItem item, Prototype parent) {
        List<ItemAlignment> alignment = item.getAlignment();
        Indents itemMargin = item.getMargin();

        if (alignment.contains(ItemAlignment.LEFT)) {
            item.setX(parent.getX() + parent.getPadding().left + itemMargin.left);
        }
        if (alignment.contains(ItemAlignment.RIGHT)) {
            item.setX(
                    parent.getX() + parent.getWidth() - item.getWidth() - parent.getPadding().right - itemMargin.right);
        }
        if (alignment.contains(ItemAlignment.TOP)) {
            item.setY(parent.getY() + parent.getPadding().top + itemMargin.top);
        }
        if (alignment.contains(ItemAlignment.BOTTOM)) {
            item.setY(parent.getY() + parent.getHeight() - item.getHeight() - parent.getPadding().bottom
                    - itemMargin.bottom);
        }
        if (alignment.contains(ItemAlignment.HCENTER)) {
            item.setX(parent.getX() + (parent.getWidth() - item.getWidth()) / 2 + itemMargin.left - itemMargin.right);
        }
        if (alignment.contains(ItemAlignment.VCENTER)) {
            item.setY(parent.getY() + (parent.getHeight() - item.getHeight()) / 2 + itemMargin.top - itemMargin.bottom);
        }
    }

    static void updateGeometryAttr(InterfaceBaseItem item, GeometryEventType type, int value) {
        Prototype parent = item.getParent();
        if (parent == null)
            return;

        if (item instanceof VisualItem) {
            updatePrototypeGeometryAttr(item, parent, type, value);
        } else {
            updateBaseItemGeometryAttr(item, parent, type, value);
        }
    }

    static void updateBaseItemGeometryAttr(InterfaceBaseItem item, Prototype parent, GeometryEventType type,
            int value) {
        item.setConfines();
        switch (type) {
        case MOVED_X:
            item.setX(item.getX() + value);
            break;

        case MOVED_Y:
            item.setY(item.getY() + value);
            break;

        case RESIZE_WIDTH:
            if (item.getWidthPolicy() == SizePolicy.FIXED) {
                if (item.getAlignment().contains(ItemAlignment.RIGHT)) {
                    item.setX(parent.getX() + parent.getWidth() - item.getWidth() - parent.getPadding().right
                            - item.getMargin().right);//
                }
                if (item.getAlignment().contains(ItemAlignment.HCENTER)) {
                    item.setX(parent.getX() + (parent.getWidth() - item.getWidth()) / 2 + item.getMargin().left
                            - item.getMargin().right);
                }
            } else if (item.getWidthPolicy() == SizePolicy.EXPAND) {
                int prefered = parent.getWidth() - parent.getPadding().left - parent.getPadding().right
                        - item.getMargin().right - item.getMargin().left;//
                prefered = (prefered > item.getMaxWidth()) ? item.getMaxWidth() : prefered;
                prefered = (prefered < item.getMinWidth()) ? item.getMinWidth() : prefered;
                item.setWidth(prefered);

                if (prefered + parent.getPadding().left + parent.getPadding().right + item.getMargin().right
                        + item.getMargin().left == parent.getWidth())//
                {
                    item.setX(parent.getX() + parent.getPadding().left + item.getMargin().left);//
                } else if (prefered + parent.getPadding().left + parent.getPadding().right + item.getMargin().right
                        + item.getMargin().left < parent.getWidth())//
                {
                    if (item.getAlignment().contains(ItemAlignment.RIGHT)) {
                        item.setX(parent.getX() + parent.getWidth() - item.getWidth() - parent.getPadding().right
                                - item.getMargin().right);//
                    }
                    if (item.getAlignment().contains(ItemAlignment.HCENTER)) {
                        item.setX(parent.getX() + (parent.getWidth() - item.getWidth()) / 2 + item.getMargin().left);//
                    }
                } else if (prefered + parent.getPadding().left + parent.getPadding().right + item.getMargin().right
                        + item.getMargin().left > parent.getWidth())//
                {
                    // никогда не должен зайти
                    item.setX(parent.getX() + parent.getPadding().left + item.getMargin().left);//
                    prefered = parent.getWidth() - parent.getPadding().left - parent.getPadding().right
                            - item.getMargin().left - item.getMargin().right;//
                    item.setWidth(prefered);
                }
            }
            break;

        case RESIZE_HEIGHT:
            if (item.getHeightPolicy() == SizePolicy.FIXED) {
                if (item.getAlignment().contains(ItemAlignment.BOTTOM)) {
                    item.setY(parent.getY() + parent.getHeight() - item.getHeight() - parent.getPadding().bottom
                            - item.getMargin().bottom);//
                }
                if (item.getAlignment().contains(ItemAlignment.VCENTER)) {
                    item.setY(parent.getY() + (parent.getHeight() - item.getHeight()) / 2 + item.getMargin().top
                            - item.getMargin().bottom);
                }
            } else if (item.getHeightPolicy() == SizePolicy.EXPAND) {
                int prefered = parent.getHeight() - parent.getPadding().top - parent.getPadding().bottom
                        - item.getMargin().bottom - item.getMargin().top;//
                prefered = (prefered > item.getMaxHeight()) ? item.getMaxHeight() : prefered;
                prefered = (prefered < item.getMinHeight()) ? item.getMinHeight() : prefered;
                item.setHeight(prefered);

                if (prefered + parent.getPadding().top + parent.getPadding().bottom + item.getMargin().bottom
                        + item.getMargin().top == parent.getHeight())//
                {
                    item.setY(parent.getY() + parent.getPadding().top + item.getMargin().top);//
                } else if (prefered + parent.getPadding().top + parent.getPadding().bottom + item.getMargin().bottom
                        + item.getMargin().top < parent.getHeight())//
                {
                    if (item.getAlignment().contains(ItemAlignment.BOTTOM)) {
                        item.setY(parent.getY() + parent.getHeight() - item.getHeight() - parent.getPadding().bottom
                                - item.getMargin().bottom);//
                    }
                    if (item.getAlignment().contains(ItemAlignment.VCENTER)) {
                        item.setY(parent.getY() + (parent.getHeight() - item.getHeight()) / 2 + item.getMargin().top);//
                    }
                } else if (prefered + parent.getPadding().top + parent.getPadding().bottom + item.getMargin().bottom
                        + item.getMargin().top > parent.getHeight())//
                {
                    // никогда не должен зайти
                    item.setY(parent.getY() + parent.getPadding().top + item.getMargin().top);//
                    prefered = parent.getHeight() - parent.getPadding().top - parent.getPadding().bottom
                            - item.getMargin().top - item.getMargin().bottom;//
                    item.setHeight(prefered);
                }
            }
            break;

        default:
            break;
        }
    }

    static void updatePrototypeGeometryAttr(InterfaceBaseItem item, Prototype parent, GeometryEventType type,
            int value) {
        Prototype prt = ((VisualItem) item).prototype;
        updateBaseItemGeometryAttr(prt, parent, type, value);
    }

    static List<float[]> updateShape(InterfaceBaseItem item) {
        if (item == null)
            return null;
        return updateShape(item.getTriangles(), item.getWidth(), item.getHeight());
    }

    static List<float[]> updateShape(List<float[]> triangles, int w, int h) {
        if (triangles == null || triangles.size() == 0) {
            return null;
        }

        // clone triangles
        List<float[]> result = new LinkedList<>();

        for (int i = 0; i < triangles.size(); i++) {
            result.add(new float[] { triangles.get(i)[0], triangles.get(i)[1] });
        }

        // max and min
        Float maxX = result.stream().map(i -> i[0]).max(Float::compare).get();
        Float maxY = result.stream().map(i -> i[1]).max(Float::compare).get();
        Float minX = result.stream().map(i -> i[0]).min(Float::compare).get();
        Float minY = result.stream().map(i -> i[1]).min(Float::compare).get();

        // to the left top corner
        for (float[] point : result) {
            point[0] = (point[0] - minX) * w / (maxX - minX);// + item.getX();
            point[1] = (point[1] - minY) * h / (maxY - minY);// + item.getY();
        }

        return result;
    }
}