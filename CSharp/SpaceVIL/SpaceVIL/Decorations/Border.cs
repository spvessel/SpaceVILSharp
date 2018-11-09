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
        //     get { return _border_radius; }
        //     set { _border_radius = value; }
        // }

        private CornerRadius _border_radius;
        public CornerRadius Radius
        {
            get { return _border_radius; }
            set
            {
                _border_radius = value;
            }
        }
        private Color _border_color;
        public Color Fill
        {
            get { return _border_color; }
            set
            {
                _border_color = value;
            }
        }
        private int border_thickness;
        public int Thickness
        {
            get { return border_thickness; }
            set
            {
                border_thickness = value;
            }
        }

        public bool IsVisible = false;
        public Border()
        {
            Fill = Color.Transparent;
            Thickness = -1;
        }
        public Border(Color fill, CornerRadius radius, int thickness)
        {
            Fill = fill;
            Radius = radius;
            Thickness = thickness;
        }
    }
}
