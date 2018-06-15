using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SpaceVIL;
using System.Drawing;

namespace Contact
{
    class VisualContact : SpaceVIL.UserItem
    {
        public VisualContact()
        {
            //self view
            SetBackground(Color.Transparent);
            SetHeight(60);
            SetHeightPolicy(SizePolicy.Fixed);
            SetWidthPolicy(SizePolicy.Expand);
            Border.Radius = 5;
            AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            SetPadding(10, 10, 5, 10);
        }

        public override void Init()
        {

            //contact image border
            Ellipse border = new Ellipse();
            border.SetBackground(Color.FromArgb(100, 255, 180, 100));
            border.SetHeight(35);
            border.SetHeightPolicy(SizePolicy.Fixed);
            border.SetWidth(35);
            border.SetWidthPolicy(SizePolicy.Fixed);
            border.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Left);
            border.SetTriangles(GraphicsMathService.GetEllipse(50));

            //contact name
            Frame name = new Frame();
            name.SetBackground(Color.FromArgb(32, 255, 255, 255));
            name.SetHeight(20);
            name.SetHeightPolicy(SizePolicy.Fixed);
            name.SetWidthPolicy(SizePolicy.Expand);
            name.SetMargin(60, 0, 30, 0);
            name.SetAlignment(ItemAlignment.Bottom | ItemAlignment.Left);
            name.Border.Radius = 10;

            //contact close
            CustomShape close = new CustomShape();
            close.SetBackground(Color.FromArgb(255, 40, 40, 40));
            close.SetWidth(14);
            close.SetWidthPolicy(SizePolicy.Fixed);
            close.SetHeight(14);
            close.SetHeightPolicy(SizePolicy.Fixed);
            close.SetAlignment(ItemAlignment.Top | ItemAlignment.Right);
            close.SetTriangles(GraphicsMathService.GetCross(14, 14, 5, 45));

            //adding
            AddItem(border);
            AddItem(name);
            AddItem(close);
        }
    }
}
