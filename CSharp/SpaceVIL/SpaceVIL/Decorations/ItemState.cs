using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class ItemState
    {
        public bool Value = true;
        public Color Background; // = Color.LightGray;
        // public Color Foreground = Color.Black;
        // public string Text = null;
        // public string ImageUri = null;
        // public bool IsVisible = true;
        public Border Border = new Border();

        public CustomFigure Shape = null;

        public ItemState()
        {

        }

        public ItemState(Color background)
        {
            this.Background = background;
        }
    }
}
