using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    class SplitHolder : VisualItem, IDraggable
    {
        private static int count = 0;
        private Orientation _orientation;
        private int _spacerSize = 6;
        //private Rectangle _holderShape;

        public SplitHolder(Orientation or)
        {
            _orientation = or;
            SetItemName("SplitHolder_" + count);
            count++;
            //_holderShape = new Rectangle();
            MakeHolderShape();
            // EventMouseClick += EmptyEvent;
            //EventMouseDrag += OnDragging;
            //EventMouseDrop += EmptyEvent;
        }

        public void SetSpacerSize(int spSize)
        {
            if (_spacerSize != spSize)
            {
                _spacerSize = spSize;
                MakeHolderShape();
            }
        }

        public int GetSpacerSize() {
            return _spacerSize;
        }

        private void MakeHolderShape() {
            switch (_orientation)
            {
                case Orientation.Vertical:

                    SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
                    SetHeight(_spacerSize);
                    break;

                case Orientation.Horizontal:
                    SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
                    SetWidth(_spacerSize);
                    break;
            }
        }
    }
}
