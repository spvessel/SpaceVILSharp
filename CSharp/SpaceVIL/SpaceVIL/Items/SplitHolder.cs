using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class SplitHolder : Prototype, IDraggable
    {
        private static int count = 0;
        private Orientation _orientation;
        private int _spacerSize = 6;

        /// <summary>
        /// Constructs a SplitHolder with orientation (HORIZONTAL or VERTICAL)
        /// </summary>
        public SplitHolder(Orientation orientation)
        {
            _orientation = orientation;
            SetItemName("SplitHolder_" + count);
            count++;
            IsFocusable = false;
            MakeHolderShape();
        }

        /// <summary>
        /// SplitHolder size (height for the HORIZONTAL orientation,
        /// width for the VERTICAL orientation)
        /// </summary>
        public void SetSpacerSize(int spSize)
        {
            if (_spacerSize != spSize)
            {
                _spacerSize = spSize;
                MakeHolderShape();
            }
        }
        public int GetHolderSize()
        {
            return _spacerSize;
        }

        private void MakeHolderShape()
        {
            switch (_orientation)
            {
                case Orientation.Vertical:
                    SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
                    SetWidth(_spacerSize);
                    SetMinWidth(_spacerSize);
                    break;

                case Orientation.Horizontal:
                    SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
                    SetHeight(_spacerSize);
                    SetMinHeight(_spacerSize);
                    break;
            }
        }

        /// <returns> Orientation of the SplitHolder (Horizontal or Vertical) </returns>
        public Orientation GetOrientation()
        {
            return _orientation;
        }

        /// <summary>
        /// Set style of the SplitHolder
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            SetBackground(style.Background);
        }
    }
}
