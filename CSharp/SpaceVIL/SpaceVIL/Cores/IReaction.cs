using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    interface IReaction
    {
        void InvokePoolEvents();
        void EmptyEvent(IItem sender);
    }
}
