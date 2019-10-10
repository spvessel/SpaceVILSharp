package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Flags.ItemAlignment;

import static org.lwjgl.opengl.GL11.*;

final class StencilProcessor {

    private CommonProcessor _commonProcessor;
    private Map<InterfaceBaseItem, int[]> _bounds;

    StencilProcessor(CommonProcessor processor) {
        _commonProcessor = processor;
        _bounds = new HashMap<>();
    }

    boolean process(InterfaceBaseItem shell) {
        Prototype parent = shell.getParent();

        if (parent != null && _bounds.containsKey(parent)) {

            int[] shape = _bounds.get(parent);

            if (shape == null)
                return false;

            glEnable(GL_SCISSOR_TEST);
            glScissor(shape[0], shape[1], shape[2], shape[3]);

            if (!_bounds.containsKey(shell)) {
                int x = shell.getX();
                int y = _commonProcessor.window.getHeight() - (shell.getY() + shell.getHeight());
                int w = shell.getWidth();
                int h = shell.getHeight();

                int x1 = x + w;
                int y1 = y + h;

                if (x < shape[0]) {
                    x = shape[0];
                    w = x1 - x;
                }

                if (y < shape[1]) {
                    y = shape[1];
                    h = y1 - y;
                }

                if (x + w > shape[0] + shape[2]) {
                    w = shape[0] + shape[2] - x;
                }

                if (y + h > shape[1] + shape[3])
                    h = shape[1] + shape[3] - y;

                _bounds.put(shell, new int[] { x, y, w, h });
                // _bounds.put(shell, new int[] { shape[0], shape[1], shape[2], shape[3] });
            }
            return true;
        }
        return lazyStencil(shell);
    }

    private void setConfines(InterfaceBaseItem shell, int[] parentConfines) {
        shell.setConfines(parentConfines[0], parentConfines[1], parentConfines[2], parentConfines[3]);

        if (shell instanceof Prototype) {
            Prototype root = (Prototype) shell;
            List<InterfaceBaseItem> root_items = root.getItems();
            for (InterfaceBaseItem item : root_items) {
                setConfines(item, parentConfines);
            }
        }
    }

    private void setScissorRectangle(InterfaceBaseItem rect) {
        Prototype parent = rect.getParent();
        if (parent == null)
            return;

        int x = parent.getX();
        int y = _commonProcessor.window.getHeight() - (parent.getY() + parent.getHeight());
        int w = parent.getWidth();
        int h = parent.getHeight();

        float scale = _commonProcessor.window.getDpiScale()[0];
        x *= scale;
        y *= scale;
        w *= scale;
        h *= scale;

        glEnable(GL_SCISSOR_TEST);
        glScissor(x, y, w, h);

        if (!_bounds.containsKey(rect))
            _bounds.put(rect, new int[] { x, y, w, h });

        parent.setConfines(parent.getX() + parent.getPadding().left,
                parent.getX() + parent.getWidth() - parent.getPadding().right, parent.getY() + parent.getPadding().top,
                parent.getY() + parent.getHeight() - parent.getPadding().bottom);
        setConfines(rect, parent.getConfines());
    }

    private Boolean lazyStencil(InterfaceBaseItem shell) {
        Map<ItemAlignment, int[]> outside = new HashMap<ItemAlignment, int[]>();
        Prototype parent = shell.getParent();

        if (parent != null) {
            // bottom
            if (parent.getY() + parent.getHeight() < shell.getY() + shell.getHeight()) {
                int y = parent.getY() + parent.getHeight() - parent.getPadding().bottom;
                int h = shell.getHeight();
                outside.put(ItemAlignment.BOTTOM, new int[] { y, h });
            }
            // top
            if (parent.getY() + parent.getPadding().top > shell.getY()) {
                int y = shell.getY();
                int h = parent.getY() + parent.getPadding().top - shell.getY();
                outside.put(ItemAlignment.TOP, new int[] { y, h });
            }
            // right
            if (parent.getX() + parent.getWidth() - parent.getPadding().right < shell.getX() + shell.getWidth()) {
                int x = parent.getX() + parent.getWidth() - parent.getPadding().right;
                int w = shell.getWidth();
                outside.put(ItemAlignment.RIGHT, new int[] { x, w });
            }
            // left
            if (parent.getX() + parent.getPadding().left > shell.getX()) {
                int x = shell.getX();
                int w = parent.getX() + parent.getPadding().left - shell.getX();
                outside.put(ItemAlignment.LEFT, new int[] { x, w });
            }

            if (outside.size() > 0) {
                setScissorRectangle(shell);
                return true;
            }
        }
        return false;
    }

    void clearBounds() {
        _bounds.clear();
    }
}