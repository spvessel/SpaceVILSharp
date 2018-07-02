using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    class UserItem : VisualItem, IUserItem
    {
        public static int count = 0;
        public UserItem()
        {
            SetItemName("UserItem" + count);
            count++;
        }

        public virtual void Init()
        {

        }
    }
}
