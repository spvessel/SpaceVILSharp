using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class Indicator : VisualItem
    {
        internal class CustomToggle : ButtonToggle
        {
            protected internal override bool GetHoverVerification(float xpos, float ypos)
            {
                return false;
            }
        }

        private static int count = 0;

        private CustomToggle _marker;
        public ButtonToggle GetIndicatorMarker()
        {
            return _marker;
        }

        public Indicator()
        {
            SetItemName("Indicator_" + count);
            count++;

            _marker = new CustomToggle();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.Indicator)));
        }

        public override void InitElements()
        {
            //marker
            _marker.SetItemName(GetItemName() + "_marker");
            AddItem(_marker);
        }

        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            Style marker_style = style.GetInnerStyle("marker");
            if(marker_style != null)
            {
                _marker.SetStyle(marker_style);
            }
        }
    }
}
