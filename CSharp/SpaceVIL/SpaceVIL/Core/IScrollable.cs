using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    interface IScrollable
    {
        void InvokeScrollUp(MouseArgs args);
        void InvokeScrollDown(MouseArgs args);
    }
}
