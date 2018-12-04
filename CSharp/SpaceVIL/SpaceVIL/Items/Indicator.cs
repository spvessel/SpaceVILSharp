using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class Indicator : Prototype
    {
        internal class CustomToggle : ButtonToggle
        {
            internal override bool GetHoverVerification(float xpos, float ypos)
            {
                return false;
            }
        }

        private static int count = 0;

        private CustomToggle _marker;

        /// <returns> IndicationMarker ButtonToggle type for styling </returns>
        public ButtonToggle GetIndicatorMarker()
        {
            return _marker;
        }

        /// <summary>
        /// Constructs an Indicator
        /// </summary>
        public Indicator()
        {
            SetItemName("Indicator_" + count);
            count++;

            _marker = new CustomToggle();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.Indicator)));
        }

        /// <summary>
        /// Initialization and adding of all elements in the Indicator
        /// </summary>
        public override void InitElements()
        {
            //marker
            _marker.SetItemName(GetItemName() + "_marker");
            AddItem(_marker);
        }

        /// <summary>
        /// Set style of the Indicator
        /// </summary>
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
