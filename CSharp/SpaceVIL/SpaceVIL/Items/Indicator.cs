using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class Indicator : VisualItem
    {
        private static int count = 0;

        private ButtonToggle _indicator;
        public ButtonToggle GetIndicatorMarker()
        {
            return _indicator;
        }

        public Indicator()
        {
            SetItemName("Indicator" + count);
            SetWidth(20);
            SetHeight(20);
            SetPadding(4,4,4,4);
            SetWidthPolicy(SizePolicy.Fixed);
            SetHeightPolicy(SizePolicy.Fixed);
            SetBackground(Color.FromArgb(255, 32, 32, 32));
            count++;
        }

        public override void InitElements()
        {
            //marker
            _indicator = new ButtonToggle();
            _indicator.SetItemName(GetItemName() + "_marker");
            _indicator.SetBackground(Color.FromArgb(255, 32, 32, 32));
            _indicator.SetWidthPolicy(SizePolicy.Expand);
            _indicator.SetHeightPolicy(SizePolicy.Expand);
            _indicator.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            _indicator.AddItemState(true, ItemStateType.Toggled, new ItemState()
            {
                Background = Color.FromArgb(255, 255, 181, 111)
            });
            AddItem(_indicator);
        }
    }
}
