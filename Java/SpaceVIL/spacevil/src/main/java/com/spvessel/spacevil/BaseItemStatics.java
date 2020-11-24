package com.spvessel.spacevil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IFreeLayout;
import com.spvessel.spacevil.Core.IHLayout;
import com.spvessel.spacevil.Core.IVLayout;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

final class BaseItemStatics {
    private BaseItemStatics() {
    }

    static void updateAllLayout(IBaseItem item) {
        Prototype parent = item.getParent();
        if (parent == null) {
            return;
        }

        boolean hLayout = parent instanceof IHLayout;
        boolean vLayout = parent instanceof IVLayout;
        boolean grid = parent instanceof IFreeLayout;

        if (!hLayout && !vLayout && !grid) {
            updateBehavior(item);
        }

        if (hLayout) {
            ((IHLayout) parent).updateLayout();
        }
        if (vLayout) {
            ((IVLayout) parent).updateLayout();
        }
        if (grid) {
            ((IFreeLayout) parent).updateLayout();
        }
    }

    static void updateHLayout(IBaseItem item) {
        Prototype parent = item.getParent();
        if (parent == null) {
            return;
        }

        if (item.getWidthPolicy() == SizePolicy.Fixed) {
            boolean hLayout = parent instanceof IHLayout;
            boolean grid = parent instanceof IFreeLayout;

            if (!hLayout && !grid) {
                updateBehavior(item);
            }

            if (hLayout) {
                ((IHLayout) parent).updateLayout();
            }
            if (grid) {
                ((IFreeLayout) parent).updateLayout();
            }
        }
    }

    static void updateVLayout(IBaseItem item) {
        Prototype parent = item.getParent();
        if (parent == null) {
            return;
        }
        if (item.getHeightPolicy() == SizePolicy.Fixed) {
            boolean vLayout = parent instanceof IVLayout;
            boolean grid = parent instanceof IFreeLayout;

            if (!vLayout && !grid) {
                updateBehavior(item);
            }

            if (vLayout) {
                ((IVLayout) parent).updateLayout();
            }
            if (grid) {
                ((IFreeLayout) parent).updateLayout();
            }
        }
    }

    static void castToUpdateBehavior(IBaseItem item) {
        if (item instanceof Prototype) {
            updateBehavior(((Prototype) item).getCore());
        } else {
            updateBehavior(item);
        }
    }

    static void castToUpdateGeometry(IBaseItem item) {
        if (item instanceof Prototype) {
            updateGeometry(((Prototype) item).getCore());
        } else {
            updateGeometry(item);
        }
    }

    static void updateGeometry(IBaseItem item) {
        updateGeometryAttr(item, GeometryEventType.ResizeWidth, 0);
        updateGeometryAttr(item, GeometryEventType.ResizeHeight, 0);
        updateGeometryAttr(item, GeometryEventType.MovedX, 0);
        updateGeometryAttr(item, GeometryEventType.MovedY, 0);
    }

    static void updateBehavior(IBaseItem item) {
        Prototype parent = item.getParent();
        if (parent == null) {
            return;
        }

        if (item instanceof VisualItem) {
            updatePrototypeBehavior(item, parent);
        } else {
            updateBaseItemBehavior(item, parent);
        }
    }

    static void updatePrototypeBehavior(IBaseItem item, Prototype parent) {
        Prototype prt = ((VisualItem) item).prototype;
        updateBaseItemBehavior(prt, parent);
    }

    static void updateBaseItemBehavior(IBaseItem item, Prototype parent) {
        List<ItemAlignment> alignment = item.getAlignment();
        Indents itemMargin = item.getMargin();

        if (alignment.contains(ItemAlignment.Left)) {
            item.setX(parent.getX() + parent.getPadding().left + itemMargin.left);
        }
        if (alignment.contains(ItemAlignment.Right)) {
            item.setX(
                    parent.getX() + parent.getWidth() - item.getWidth() - parent.getPadding().right - itemMargin.right);
        }
        if (alignment.contains(ItemAlignment.Top)) {
            item.setY(parent.getY() + parent.getPadding().top + itemMargin.top);
        }
        if (alignment.contains(ItemAlignment.Bottom)) {
            item.setY(parent.getY() + parent.getHeight() - item.getHeight() - parent.getPadding().bottom
                    - itemMargin.bottom);
        }
        if (alignment.contains(ItemAlignment.HCenter)) {
            item.setX(parent.getX() + (parent.getWidth() - item.getWidth()) / 2 + itemMargin.left - itemMargin.right);
        }
        if (alignment.contains(ItemAlignment.VCenter)) {
            item.setY(parent.getY() + (parent.getHeight() - item.getHeight()) / 2 + itemMargin.top - itemMargin.bottom);
        }
    }

    static void updateGeometryAttr(IBaseItem item, GeometryEventType type, int value) {
        Prototype parent = item.getParent();
        if (parent == null) {
            return;
        }

        if (item instanceof VisualItem) {
            updatePrototypeGeometryAttr(item, parent, type, value);
        } else {
            updateBaseItemGeometryAttr(item, parent, type, value);
        }
    }

    static void updateBaseItemGeometryAttr(IBaseItem item, Prototype parent, GeometryEventType type,
            int value) {
        item.setConfines();
        switch (type) {
        case MovedX:
            item.setX(item.getX() + value);
            break;

        case MovedY:
            item.setY(item.getY() + value);
            break;

        case ResizeWidth:
            if (item.getWidthPolicy() == SizePolicy.Fixed) {
                if (item.getAlignment().contains(ItemAlignment.Right)) {
                    item.setX(parent.getX() + parent.getWidth() - item.getWidth() - parent.getPadding().right
                            - item.getMargin().right);//
                }
                if (item.getAlignment().contains(ItemAlignment.HCenter)) {
                    item.setX(parent.getX() + (parent.getWidth() - item.getWidth()) / 2 + item.getMargin().left
                            - item.getMargin().right);
                }
            } else if (item.getWidthPolicy() == SizePolicy.Expand) {
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
                    if (item.getAlignment().contains(ItemAlignment.Right)) {
                        item.setX(parent.getX() + parent.getWidth() - item.getWidth() - parent.getPadding().right
                                - item.getMargin().right);//
                    }
                    if (item.getAlignment().contains(ItemAlignment.HCenter)) {
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

        case ResizeHeight:
            if (item.getHeightPolicy() == SizePolicy.Fixed) {
                if (item.getAlignment().contains(ItemAlignment.Bottom)) {
                    item.setY(parent.getY() + parent.getHeight() - item.getHeight() - parent.getPadding().bottom
                            - item.getMargin().bottom);//
                }
                if (item.getAlignment().contains(ItemAlignment.VCenter)) {
                    item.setY(parent.getY() + (parent.getHeight() - item.getHeight()) / 2 + item.getMargin().top
                            - item.getMargin().bottom);
                }
            } else if (item.getHeightPolicy() == SizePolicy.Expand) {
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
                    if (item.getAlignment().contains(ItemAlignment.Bottom)) {
                        item.setY(parent.getY() + parent.getHeight() - item.getHeight() - parent.getPadding().bottom
                                - item.getMargin().bottom);//
                    }
                    if (item.getAlignment().contains(ItemAlignment.VCenter)) {
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

    static void updatePrototypeGeometryAttr(IBaseItem item, Prototype parent, GeometryEventType type,
            int value) {
        Prototype prt = ((VisualItem) item).prototype;
        updateBaseItemGeometryAttr(prt, parent, type, value);
    }

    static List<float[]> updateShape(IBaseItem item) {
        if (item == null) {
            return null;
        }
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

    static <T> List<T> composeFlags(T[] flagsArray) {
        return Arrays.asList(flagsArray);
    }
}