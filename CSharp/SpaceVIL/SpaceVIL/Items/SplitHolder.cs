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
        public SplitHolder()
        {
            SetItemName("SplitHolder_" + count);
            count++;
        }

        public void SetOrientation(Orientation or) {
            _orientation = or;
        }
    }
}
