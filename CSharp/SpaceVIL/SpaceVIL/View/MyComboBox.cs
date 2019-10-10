using System;
using System.Drawing;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace View
{
    public class MyComboBox : SpaceVIL.ComboBox
    {
        ImageItem img = null;
        public MyComboBox(Bitmap image, params MenuItem[] items) : base(items)
        {
            img = new ImageItem(image);
            SetTextMargin(30, 0, 0, 0);
        }
        public override void InitElements()
        {
            base.InitElements();
            // img attr
            img.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            img.SetSize(20, 20);
            img.SetAlignment(ItemAlignment.Left, ItemAlignment.VCenter);
            img.SetMargin(10, 0, 0, 0);
            img.KeepAspectRatio(true);

            // add image
            AddItem(img);
        }
    }
}