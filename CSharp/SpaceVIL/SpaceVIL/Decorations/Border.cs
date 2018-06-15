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
        private int _border_radius;
        public int Radius
		{
			get { return _border_radius; }
			set { _border_radius = value; }
		}
        private Color _border_color;
        public Color Fill
		{
			get { return _border_color; }
			set { _border_color = value; }
		}
        private float border_thickness;
        public float Thickness
		{
			get { return border_thickness; }
			set { border_thickness = value; }
		}

        public Border()
        {
            Fill = Color.Transparent;
            Thickness = 0;
            Radius = 0;
        }
    }
}
