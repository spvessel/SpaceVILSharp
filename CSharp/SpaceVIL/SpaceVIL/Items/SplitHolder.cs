using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class SplitHolder : VisualItem, IDraggable
    {
        private static int count = 0;
        private Orientation _orientation;
        private int _spacerSize = 6;

        public SplitHolder(Orientation orientation)
        {
            _orientation = orientation;
            SetItemName("SplitHolder_" + count);
            //SetBackground(Color.Transparent);
            count++;
            MakeHolderShape();
        }

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

        public Orientation GetOrientation()
        {
            return _orientation;
        }
    }
}
