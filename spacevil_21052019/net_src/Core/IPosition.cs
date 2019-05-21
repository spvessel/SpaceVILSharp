using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    public interface IPosition
    {
        void SetX(int x);
        int GetX();
        void SetY(int y);
        int GetY();
        //void UpdateX(ItemAlignment alignment, IAncestor ancestor, Padding padding, int width);
        //void UpdateY(ItemAlignment alignment, IAncestor ancestor, Padding padding, int height);
    }
}
