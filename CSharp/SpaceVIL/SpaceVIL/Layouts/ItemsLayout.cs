using System.Collections.Generic;
using System.Linq;
using System;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal sealed class ItemsLayout
    {
        private Guid _id;
        internal Guid Id
		{
			get { return  _id; }
			set { _id = value; }
		}
        internal ItemsLayout(Guid layoutId)
        {
            Id = layoutId;
        }
        List<IBaseItem> items = new List<IBaseItem>();
        internal List<IBaseItem> Items
		{
			get { return items; }
			set { items = value; }
		}
        List<IBaseItem> float_items = new List<IBaseItem>();
        internal List<IBaseItem> FloatItems
        {
            get { return float_items; }
            set { float_items = value; }
        }
    }
}
