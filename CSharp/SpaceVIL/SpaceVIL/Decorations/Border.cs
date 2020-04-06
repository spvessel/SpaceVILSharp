using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// Border of the item
    /// </summary>
    public class Border
    {
        private CornerRadius _border_radius;

        /// <returns> Radius of the border's corners </returns>
        public CornerRadius GetRadius()
        {
            return _border_radius;
        }

        /// <summary>
        /// Set radius of the border's corners
        /// </summary>
        public void SetRadius(CornerRadius value)
        {
            _border_radius = value;
        }

        private Color _border_color;

        /// <summary>
        /// Border color
        /// </summary>
        public Color GetFill()
        {
            return _border_color;
        }

        /// <summary>
        /// Border color
        /// </summary>
        public void SetFill(Color fill)
        {
            _border_color = fill;
        }

        private int border_thickness;

        /// <summary>
        /// Border thickness
        /// </summary>
        public int GetThickness()
        {
            return border_thickness;
        }

        /// <summary>
        /// Border thickness
        /// </summary>
        public void SetThickness(int thickness)
        {
            border_thickness = thickness;
        }

        /// <summary>
        /// Is border visible
        /// </summary>
        public bool IsVisible = false;

        /// <summary>
        /// Constructs a Border
        /// </summary>
        public Border()
        {
            SetFill(Color.Transparent);
            SetThickness(-1);
            SetRadius(new CornerRadius());
        }

        /// <summary>
        /// Constructs a Border with color, radius and thickness
        /// </summary>
        public Border(Color fill, CornerRadius radius, int thickness)
        {
            SetFill(fill); // (Color.Transparent);
            SetRadius(radius);
            SetThickness(thickness);
        }
    }
}
