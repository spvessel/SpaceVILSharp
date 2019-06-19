using System;
using System.Collections.Generic;
using SpaceVIL.Core;

using static OpenGL.OpenGLConstants;

#if MAC
using static OpenGL.UnixGL;
#elif WINDOWS
using static OpenGL.WindowsGL;
#elif LINUX
using static OpenGL.UnixGL;
#else
using static OpenGL.WindowsGL;
#endif

namespace SpaceVIL
{
    internal class StencilProcessor
    {
        private CommonProcessor _commonProcessor;
        private Dictionary<IBaseItem, int[]> _bounds;

        internal StencilProcessor(CommonProcessor processor)
        {
            _commonProcessor = processor;
            _bounds = new Dictionary<IBaseItem, int[]>();
        }

        internal bool Process(IBaseItem shell)
        {
            Prototype parent = shell.GetParent();
            if (parent != null && _bounds.ContainsKey(parent))
            {
                int[] shape = _bounds[parent];

                if (shape == null)
                    return false;

                glEnable(GL_SCISSOR_TEST);
                glScissor(shape[0], shape[1], shape[2], shape[3]);

                if (!_bounds.ContainsKey(shell))
                {
                    int x = shell.GetX();
                    int y = _commonProcessor.Window.GetHeight() - (shell.GetY() + shell.GetHeight());
                    int w = shell.GetWidth();
                    int h = shell.GetHeight();
                    int x1 = x + w;
                    int y1 = y + h;

                    if (x < shape[0])
                    {
                        x = shape[0];
                        w = x1 - x;
                    }
                    if (y < shape[1])
                    {
                        y = shape[1];
                        h = y1 - y;
                    }

                    if (x + w > shape[0] + shape[2])
                    {
                        w = shape[0] + shape[2] - x;
                    }

                    if (y + h > shape[1] + shape[3])
                        h = shape[1] + shape[3] - y;

                    _bounds.Add(shell, new int[] { x, y, w, h });
                }
                return true;
            }
            return LazyStencil(shell);
        }

        private void SetConfines(IBaseItem shell, int[] parentConfines)
        {
            shell.SetConfines(
                            parentConfines[0],
                            parentConfines[1],
                            parentConfines[2],
                            parentConfines[3]
                        );

            Prototype root = shell as Prototype;
            if (root != null)
            {
                List<IBaseItem> root_items = root.GetItems();
                foreach (var item in root_items)
                    SetConfines(item, shell.GetConfines());
            }
        }

        private void SetScissorRectangle(IBaseItem rect)
        {
            Prototype parent = rect.GetParent();
            if (parent == null)
                return;

            int x = parent.GetX();
            int y = _commonProcessor.Window.GetHeight() - (parent.GetY() + parent.GetHeight());
            int w = parent.GetWidth();
            int h = parent.GetHeight();
            float scale = _commonProcessor.Window.GetDpiScale()[0];
            x = (int)((float)x * scale);
            y = (int)((float)y * scale);
            w = (int)((float)w * scale);
            h = (int)((float)h * scale);

            glEnable(GL_SCISSOR_TEST);
            glScissor(x, y, w, h);

            if (!_bounds.ContainsKey(rect))
                _bounds.Add(rect, new int[] { x, y, w, h });

            parent.SetConfines(
                parent.GetX() + parent.GetPadding().Left,
                parent.GetX() + parent.GetWidth() - parent.GetPadding().Right,
                parent.GetY() + parent.GetPadding().Top,
                parent.GetY() + parent.GetHeight() - parent.GetPadding().Bottom
            );
            SetConfines(rect, parent.GetConfines());
        }

        private bool LazyStencil(IBaseItem shell)
        {
            var outside = new Dictionary<ItemAlignment, Int32[]>();
            Prototype parent = shell.GetParent();
            if (parent != null)
            {
                //bottom
                if (parent.GetY() + parent.GetHeight() < shell.GetY() + shell.GetHeight())
                {
                    int y = parent.GetY() + parent.GetHeight() - parent.GetPadding().Bottom;
                    int h = shell.GetHeight();
                    outside.Add(ItemAlignment.Bottom, new int[] { y, h });
                }
                //top
                if (parent.GetY() + parent.GetPadding().Top > shell.GetY())
                {
                    int y = shell.GetY();
                    int h = parent.GetY() + parent.GetPadding().Top - shell.GetY();
                    outside.Add(ItemAlignment.Top, new int[] { y, h });
                }
                //right
                if (parent.GetX() + parent.GetWidth() - parent.GetPadding().Right <
                    shell.GetX() + shell.GetWidth())
                {
                    int x = parent.GetX() + parent.GetWidth() - parent.GetPadding().Right;
                    int w = shell.GetWidth();
                    outside.Add(ItemAlignment.Right, new int[] { x, w });
                }
                //left
                if (parent.GetX() + parent.GetPadding().Left > shell.GetX())
                {
                    int x = shell.GetX();
                    int w = parent.GetX() + parent.GetPadding().Left - shell.GetX();
                    outside.Add(ItemAlignment.Left, new int[] { x, w });
                }

                if (outside.Count > 0)
                {
                    SetScissorRectangle(shell);
                    return true;
                }
            }
            return false;
        }

        internal void ClearBounds()
        {
            _bounds.Clear();
        }
    }
}