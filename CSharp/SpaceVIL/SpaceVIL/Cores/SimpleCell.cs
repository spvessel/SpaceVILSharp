﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    internal class SimpleCell : Geometry, IPosition
    {
        internal SimpleCell()
        {
        }
        
        //Position
        private int _x = 0;
        private int _y = 0;
        public void SetX(int x)
        {
            _x = x;
        }
        public int GetX()
        {
            return _x;
        }
        public void SetY(int y)
        {
            _y = y;
        }
        public int GetY()
        {
            return _y;
        }

        private BaseItem _item_link = null;
        public BaseItem GetItem()
        {
            return _item_link;
        }
        public void SetItem(BaseItem item)
        {
            _item_link = item;
        }

    }
}