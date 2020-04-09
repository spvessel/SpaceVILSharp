using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// Border is a class that decorates item's shape with border.
    /// </summary>
    public class Border
    {
        private CornerRadius _borderRadius;
        
        /// <summary>
        /// Getting border radiuses.
        /// </summary>
        /// <returns>Border radiuses as SpaceVIL.Decorations.CornerRadius.</returns>
        public CornerRadius GetRadius()
        {
            return _borderRadius;
        }

        /// <summary>
        /// Setting radius of the border's corners.
        /// </summary>
        /// <param name="value">Radiuses of the border's corners as SpaceVIL.Decorations.CornerRadius.</param>
        public void SetRadius(CornerRadius value)
        {
            _borderRadius = value;
        }

        private Color _border_color;

        /// <summary>
        /// Getting the border color oa an item's shape.
        /// </summary>
        /// <returns>Border color as System.Drawing.Color.</returns>
        public Color GetFill()
        {
            return _border_color;
        }

        /// <summary>
        /// Setting the border color of an item's shape.
        /// </summary>
        /// <param name="fill">Border color as System.Drawing.Color.</param>
        public void SetFill(Color fill)
        {
            _border_color = fill;
        }

        private int border_thickness;

        /// <summary>
        /// Getting border thickness of an item's shape.
        /// </summary>
        /// <returns>Border thickness.</returns>
        public int GetThickness()
        {
            return border_thickness;
        }

        /// <summary>
        /// Setting border thickness of an item's shape.
        /// </summary>
        /// <param name="thickness">Border thickness.</param>
        public void SetThickness(int thickness)
        {
            border_thickness = thickness;
        }

        /// <summary>
        /// Propery that defines if border is visible
        /// </summary>
        public bool IsVisible = false;

        /// <summary>
        /// Default Border constructor.
        /// </summary>
        public Border()
        {
            SetFill(Color.Transparent);
            SetThickness(-1);
            SetRadius(new CornerRadius());
        }

        /// <summary>
        /// Constructs a Border  with specified color, radius and thickness.
        /// </summary>
        /// <param name="fill">Border color as System.Drawing.Color.</param>
        /// <param name="radius">Radiuses of the border's corners as SpaceVIL.Decorations.CornerRadius.</param>
        /// <param name="thickness">Border thickness.</param>
        public Border(Color fill, CornerRadius radius, int thickness)
        {
            SetFill(fill); // (Color.Transparent);
            SetRadius(radius);
            SetThickness(thickness);
        }
    }
}
