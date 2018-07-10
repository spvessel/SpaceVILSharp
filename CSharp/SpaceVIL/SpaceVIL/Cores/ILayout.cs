using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    internal interface IHLayout
    {
        void UpdateLayout();
    }
    internal interface IVLayout
    {
        void UpdateLayout();
    }
    interface IGrid
    {
        void UpdateLayout();
    }
    interface IFlow
    {
        void UpdateLayout();
    }
}
