using System.Collections.Generic;
using System.Linq;
using System;

namespace SpaceVIL
{
    internal class ItemsLayout
    {
        private Guid _id;
        protected internal Guid Id
		{
			get { return  _id; }
			set { _id = value; }
		}
        protected internal ItemsLayout(Guid layoutId)
        {
            Id = layoutId;
        }
        List<IItem> items = new List<IItem>();
        protected internal List<IItem> Items
		{
			get { return items; }
			set { items = value; }
		}
    }
}
