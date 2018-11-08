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
        List<IBaseItem> items = new List<IBaseItem>();
        protected internal List<IBaseItem> Items
		{
			get { return items; }
			set { items = value; }
		}
        List<IBaseItem> float_items = new List<IBaseItem>();
        protected internal List<IBaseItem> FloatItems
        {
            get { return float_items; }
            set { float_items = value; }
        }
    }
}
