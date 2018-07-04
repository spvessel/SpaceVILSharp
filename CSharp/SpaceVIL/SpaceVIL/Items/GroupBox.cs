using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class GroupBox : Frame
    {
        private static int count = 0;
        private Label _text;

        public GroupBox()
        {
            SetItemName("GroupBox_" + count);
            count++;
            SetBackground(Color.Transparent);

            //text
            _text = new Label();
            _text.SetItemName(GetItemName() + "_text");
            _text.SetBackground(255, 255, 255, 20);
            _text.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            _text.SetAlignment(ItemAlignment.VCenter);
            _text.SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            _text.SetPadding(10);
        }

        public override void InitElements()
        {
            AddItem(_text);
        }
    }
}
