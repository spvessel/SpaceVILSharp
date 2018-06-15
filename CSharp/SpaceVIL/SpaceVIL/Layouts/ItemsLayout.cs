using System.Collections.Generic;
using System.Linq;
using System;

namespace SpaceVIL
{
    internal class ItemsLayout
    {
        private int _id;
        protected internal int Id
		{
			get { return  _id; }
			set { _id = value; }
		}
        protected internal ItemsLayout(int layoutId)
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
