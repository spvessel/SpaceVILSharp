using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class Border
    {
        // private CornerRadius _border_radius;
        // public CornerRadius Radius
        // {
        //     Get { return _border_radius; }
        //     Set { _border_radius = value; }
        // }

        private CornerRadius _border_radius;
        public CornerRadius GetRadius()
        {
            return _border_radius;
        }
        public void SetRadius(CornerRadius value)
        {
            _border_radius = value;
        }

        private Color _border_color;
        public Color GetFill()
        {
            return _border_color;
        }
        public void SetFill(Color fill)
        {
            _border_color = fill;
        }
        private int border_thickness;
        public int GetThickness()
        {
            return border_thickness;
        }
        public void SetThickness(int thickness)
        {
            border_thickness = thickness;
        }

        public bool IsVisible = false;
        public Border()
        {
            SetFill(Color.Transparent);
            SetThickness(-1);
        }
        public Border(Color fill, CornerRadius radius, int thickness)
        {
            SetFill(Color.Transparent);
            SetRadius(radius);
            SetThickness(thickness);
        }
    }
}
