using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public static class ToolTip
    {
        private static ToolTipItem GetToolTip(CoreWindow window)
        {
            return window.GetLayout().GetToolTip();
        }

        public static void SetStyle(CoreWindow window, Style style)
        {
            if (window == null)
                return;
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetStyle(style);
        }

        public static void SetTimeOut(CoreWindow window, int ms)
        {
            if (window == null)
                return;
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetTimeOut(ms);
        }

        public static int GetTimeOut(CoreWindow window)
        {
            if (window == null)
                return -1;
            ToolTipItem toolTip = GetToolTip(window);
            return toolTip.GetTimeOut();
        }

        public static void SetBackground(CoreWindow window, Color color)
        {
            if (window == null)
                return;
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetBackground(color);
        }

        public static void SetForeground(CoreWindow window, Color color)
        {
            if (window == null)
                return;
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetForeground(color);
        }

        public static void SetFont(CoreWindow window, Font font)
        {
            if (window == null)
                return;
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetFont(font);
        }

        public static void SetBorder(CoreWindow window, Border border)
        {
            if (window == null)
                return;
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetBorder(border);
        }

        public static void SetShadow(CoreWindow window, Shadow shadow)
        {
            if (window == null)
                return;
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetShadow(shadow.GetRadius(), shadow.GetXOffset(), shadow.GetYOffset(), shadow.GetColor());
        }

        public static void SetShadowDrop(CoreWindow window, bool value)
        {
            if (window == null)
                return;
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetShadowDrop(value);
        }

        public static void AddItems(CoreWindow window, params IBaseItem[] items)
        {
            if (window == null)
                return;
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.AddItems(items);
        }
    }
}