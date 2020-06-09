using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// ToolTip is a static class for SpaceVIL.ToolTipItem managing.
    /// <para/> Every window has its own ToolTipItem.
    /// </summary>
    public static class ToolTip
    {
        private static ToolTipItem GetToolTip(CoreWindow window)
        {
            return window.GetLayout().GetToolTip();
        }

        /// <summary>
        /// Setting style for ToolTipItem of the specified window. 
        /// <para/> Every window has its own ToolTipItem.
        /// </summary>
        /// <param name="window">Window as SpaceVIL.CoreWindow.</param>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public static void SetStyle(CoreWindow window, Style style)
        {
            if (window == null)
            {
                return;
            }
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetStyle(style);
        }

        /// <summary>
        /// Setting waiting time in milliseconds after which ToolTipItem appears.
        /// <para/> Every window has its own ToolTipItem.
        /// <para/>Default: 500 milliseconds (0.5 seconds).
        /// </summary>
        /// <param name="window">Window as SpaceVIL.CoreWindow.</param>
        /// <param name="milliseconds">Waiting time in milliseconds.</param>
        public static void SetTimeOut(CoreWindow window, int milliseconds)
        {
            if (window == null)
            {
                return;
            }
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetTimeOut(milliseconds);
        }

        /// <summary>
        /// Getting current waiting time in milliseconds after which ToolTipItem appears.
        /// <para/> Every window has its own ToolTipItem.
        /// </summary>
        /// <param name="window">Window as SpaceVIL.CoreWindow.</param>
        /// <returns>Current waiting time in milliseconds.</returns>
        public static int GetTimeOut(CoreWindow window)
        {
            if (window == null)
            {
                return -1;
            }
            ToolTipItem toolTip = GetToolTip(window);
            return toolTip.GetTimeOut();
        }

        /// <summary>
        /// Setting background color of ToolTipItem.
        /// <para/> Every window has its own ToolTipItem.
        /// </summary>
        /// <param name="window">Window as SpaceVIL.CoreWindow.</param>
        /// <param name="color">Background color as System.Drawing.Color.</param>
        public static void SetBackground(CoreWindow window, Color color)
        {
            if (window == null)
            {
                return;
            }
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetBackground(color);
        }

        /// <summary>
        /// Setting text color of ToolTipItem.
        /// <para/> Every window has its own ToolTipItem.
        /// </summary>
        /// <param name="window">Window as SpaceVIL.CoreWindow.</param>
        /// <param name="color">Text color as System.Drawing.Color.</param>
        public static void SetForeground(CoreWindow window, Color color)
        {
            if (window == null)
            {
                return;
            }
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetForeground(color);
        }

        /// <summary>
        /// Setting text font of ToolTipItem.
        /// <para/> Every window has its own ToolTipItem.
        /// </summary>
        /// <param name="window">Window as SpaceVIL.CoreWindow.</param>
        /// <param name="font">Font as System.Drawing.Font.</param>
        public static void SetFont(CoreWindow window, Font font)
        {
            if (window == null)
            {
                return;
            }
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetFont(font);
        }

        /// <summary>
        /// Setting border for ToolTipItem.
        /// <para/> Every window has its own ToolTipItem.
        /// </summary>
        /// <param name="window">Window as SpaceVIL.CoreWindow.</param>
        /// <param name="border">Border as SpaceVIL.Decorations.Border.</param>
        public static void SetBorder(CoreWindow window, Border border)
        {
            if (window == null)
            {
                return;
            }
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetBorder(border);
        }

        /// <summary>
        /// Setting shadow for ToolTipItem.
        /// <para/> Every window has its own ToolTipItem.
        /// </summary>
        /// <param name="window">Window as SpaceVIL.CoreWindow.</param>
        /// <param name="shadow">Shadow as SpaceVIL.Decorations.Shadow.</param>
        public static void SetShadow(CoreWindow window, Shadow shadow)
        {
            if (window == null)
            {
                return;
            }
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetShadow(shadow.GetRadius(), shadow.GetXOffset(), shadow.GetYOffset(), shadow.GetColor());
        }

        /// <summary>
        /// Setting ToolTipItem shadow visibility. 
        /// <para/> Every window has its own ToolTipItem.
        /// <para/> True: shadow is visible. False: shadow is invisible.
        /// </summary>
        /// <param name="window">Window as SpaceVIL.CoreWindow.</param>
        /// <param name="value">True: shadow is visible. 
        /// False: shadow is invisible.</param>
        public static void SetShadowDrop(CoreWindow window, bool value)
        {
            if (window == null)
            {
                return;
            }
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.SetShadowDrop(value);
        }

        /// <summary>
        /// Adding item to the ToolTipItem for decoration or extension.
        /// <para/> Every window has its own ToolTipItem.
        /// </summary>
        /// <param name="window">Window as SpaceVIL.CoreWindow.</param>
        /// <param name="items">Sequence of items as SpaceVIL.Core.IBaseItem.</param>
        public static void AddItems(CoreWindow window, params IBaseItem[] items)
        {
            if (window == null)
            {
                return;
            }
            ToolTipItem toolTip = GetToolTip(window);
            toolTip.AddItems(items);
        }
    }
}