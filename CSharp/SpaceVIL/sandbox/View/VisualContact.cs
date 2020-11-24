using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SpaceVIL;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace Contact
{
    class VisualContact : Prototype
    {
        public static int _count = 0;
        // private Int32[] _array;
        public VisualContact()
        {
            //self view attr
            SetItemName("VC_" + _count);
            SetBackground(80, 80, 80);
            SetSize(300, 60);
            //SetMinSize(600, 0);
            SetHeightPolicy(SizePolicy.Fixed);
            SetWidthPolicy(SizePolicy.Fixed);
            SetBorderRadius(new CornerRadius(5));
            AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(15, 255, 255, 255)
            });
            SetPadding(10, 0, 5, 0);
            _count++;
            // SetShadow(10, 5, 5, Color.Black);
            // _array = new Int32[1024 * 1024 * 128];
            // for (int i = 0; i < _array.Length; i++)
            // {
            //     _array[i] = 12;
            // }
        }

        public override void InitElements()
        {

            //contact image border
            Ellipse border = new Ellipse();
            border.SetBackground(Color.FromArgb(100, 255, 180, 100));
            border.SetHeight(45);
            border.SetHeightPolicy(SizePolicy.Fixed);
            border.SetWidth(45);
            border.SetWidthPolicy(SizePolicy.Fixed);
            border.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Left);
            border.SetTriangles(GraphicsMathService.GetEllipse(50));

            //contact name
            Label name = new Label(GetItemName() + " contact");
            name.SetFont(new Font(new FontFamily("Courier New"), 14, FontStyle.Regular));
            name.SetTextAlignment(ItemAlignment.VCenter | ItemAlignment.Left);
            name.SetBackground(Color.FromArgb(32, 255, 255, 255));
            name.SetHeight(30);
            name.SetHeightPolicy(SizePolicy.Fixed);
            name.SetWidthPolicy(SizePolicy.Expand);
            name.SetMargin(60, 0, 30, 5);
            name.SetPadding(20, 0, 0, 0);
            name.SetAlignment(ItemAlignment.Bottom | ItemAlignment.Left);
            name.SetBorderRadius(new CornerRadius(10));

            //contact close
            ButtonCore close = new ButtonCore();
            close.SetBackground(Color.FromArgb(255, 40, 40, 40));
            close.SetWidth(14);
            close.SetWidthPolicy(SizePolicy.Fixed);
            close.SetHeight(14);
            close.SetHeightPolicy(SizePolicy.Fixed);
            close.SetAlignment(ItemAlignment.Top | ItemAlignment.Right);
            close.SetMargin(0, 5, 0, 0);
            close.SetCustomFigure(new Figure(false, GraphicsMathService.GetCross(14, 14, 5, 45)));
            close.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            // close.EventMouseClick += (sender, args) =>
            // {
            //     DisposeSelf();
            // };

            //adding
            AddItem(border);
            AddItem(name);
            AddItem(close);

            
        }

        public void DisposeSelf()
        {
            // while (GetItems().Count > 0)
            // {
            //     RemoveItem(GetItems().ElementAt(0));
            // }
            GetParent().RemoveItem(this);
        }
    }
}
