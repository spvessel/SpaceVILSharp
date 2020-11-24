package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.Scale;
import com.spvessel.spacevil.Flags.ItemAlignment;

import com.spvessel.spacevil.internal.Wrapper.OpenGLWrapper;
import static com.spvessel.spacevil.internal.Wrapper.OpenGLWrapper.*;

final class StencilProcessor {

    private OpenGLWrapper gl = null;

    private CommonProcessor _commonProcessor;
    private Map<IBaseItem, int[]> _bounds;

    StencilProcessor(CommonProcessor processor) {
        gl = OpenGLWrapper.get();
        _commonProcessor = processor;
        _bounds = new HashMap<>();
    }

    private Scale _scale = new Scale();

    boolean process(IBaseItem shell, Scale scale) {

        _scale.setScale(scale.getXScale(), scale.getYScale());

        Prototype parent = shell.getParent();
        if (parent != null && _bounds.containsKey(parent)) {

            int[] shape = _bounds.get(parent);

            if (shape == null)
                return false;

            gl.Enable(GL_SCISSOR_TEST);
            gl.Scissor(shape[0], shape[1], shape[2], shape[3]);

            if (!_bounds.containsKey(shell)) {
                int x = shell.getX();
                int y = _commonProcessor.window.getHeight() - (shell.getY() + shell.getHeight());
                int w = shell.getWidth();
                int h = shell.getHeight();
                x *= _scale.getXScale();
                y *= _scale.getYScale();
                w *= _scale.getXScale();
                h *= _scale.getYScale();

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
            }
            return true;
        }
        return lazyStencil(shell);
    }

    private void setConfines(IBaseItem shell, int[] parentConfines) {
        shell.setConfines(parentConfines[0], parentConfines[1], parentConfines[2], parentConfines[3]);

        if (shell instanceof Prototype) {
            Prototype root = (Prototype) shell;
            List<IBaseItem> root_items = root.getItems();
            for (IBaseItem item : root_items) {
                setConfines(item, parentConfines);
            }
        }
    }

    private void setScissorRectangle(IBaseItem rect) {
        Prototype parent = rect.getParent();
        if (parent == null)
            return;

        int x = parent.getX();
        int y = _commonProcessor.window.getHeight() - (parent.getY() + parent.getHeight());
        int w = parent.getWidth();
        int h = parent.getHeight();
        x *= _scale.getXScale();
        y *= _scale.getYScale();
        w *= _scale.getXScale();
        h *= _scale.getYScale();

        gl.Enable(GL_SCISSOR_TEST);
        gl.Scissor(x, y, w, h);

        if (!_bounds.containsKey(rect))
            _bounds.put(rect, new int[] { x, y, w, h });

        parent.setConfines(parent.getX() + parent.getPadding().left,
                parent.getX() + parent.getWidth() - parent.getPadding().right, parent.getY() + parent.getPadding().top,
                parent.getY() + parent.getHeight() - parent.getPadding().bottom);
        setConfines(rect, parent.getConfines());
    }

    private Boolean lazyStencil(IBaseItem shell) {
        Map<ItemAlignment, int[]> outside = new HashMap<ItemAlignment, int[]>();
        Prototype parent = shell.getParent();

        if (parent != null) {
            // bottom
            if (parent.getY() + parent.getHeight() < shell.getY() + shell.getHeight()) {
                int y = parent.getY() + parent.getHeight() - parent.getPadding().bottom;
                int h = shell.getHeight();
                outside.put(ItemAlignment.Bottom, new int[] { y, h });
            }
            // top
            if (parent.getY() + parent.getPadding().top > shell.getY()) {
                int y = shell.getY();
                int h = parent.getY() + parent.getPadding().top - shell.getY();
                outside.put(ItemAlignment.Top, new int[] { y, h });
            }
            // right
            if (parent.getX() + parent.getWidth() - parent.getPadding().right < shell.getX() + shell.getWidth()) {
                int x = parent.getX() + parent.getWidth() - parent.getPadding().right;
                int w = shell.getWidth();
                outside.put(ItemAlignment.Right, new int[] { x, w });
            }
            // left
            if (parent.getX() + parent.getPadding().left > shell.getX()) {
                int x = shell.getX();
                int w = parent.getX() + parent.getPadding().left - shell.getX();
                outside.put(ItemAlignment.Left, new int[] { x, w });
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