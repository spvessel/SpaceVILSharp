using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    abstract class Scrollable : IScrollable
    {
        public EventMouseMethodState EventScrollUp = null;
        public EventMouseMethodState EventScrollDown = null;

        abstract public void InvokeScrollUp(MouseArgs args);
        abstract public void InvokeScrollDown(MouseArgs args);
    }
}