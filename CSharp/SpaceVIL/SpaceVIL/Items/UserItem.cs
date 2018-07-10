using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    abstract class UserItem : VisualItem, IUserItem
    {
        abstract public void Init();
    }
}
