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
        List<BaseItem> items = new List<BaseItem>();
        protected internal List<BaseItem> Items
		{
			get { return items; }
			set { items = value; }
		}
        List<BaseItem> float_items = new List<BaseItem>();
        protected internal List<BaseItem> FloatItems
        {
            get { return float_items; }
            set { float_items = value; }
        }
    }
}
