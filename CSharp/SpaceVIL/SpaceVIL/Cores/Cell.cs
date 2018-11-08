using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public sealed class Cell : Geometry, IPosition
    {
        private Grid _parent = null;
        public Grid GetParentGrid()
        {
            return _parent;
        }
        internal Cell(Grid grid)
        {
            _parent = grid;
        }
        internal Cell(Grid grid, int row, int column) : this(grid)
        {
            _row_index = row;
            _column_index = column;
        }
        internal Cell(Grid grid, int row, int column, int width, int height) : this(grid, row, column)
        {
            SetSize(width, height);
        }
        internal Cell(Grid grid, int row, int column, int width, int height, IBaseItem item) : this(grid, row, column, width, height)
        {
            _item_link = item;
        }

        //Indecies
        private int _row_index = 0;
        public int GetRow()
        {
            return _row_index;
        }
        public void SetRow(int index)
        {
            _row_index = index;
        }
        private int _column_index = 0;
        public int GetColumn()
        {
            return _column_index;
        }
        public void SetColumn(int index)
        {
            _column_index = index;
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

        private IBaseItem _item_link = null;
        public IBaseItem GetItem()
        {
            return _item_link;
        }
        public void SetItem(IBaseItem item)
        {
            _item_link = item;
        }

        internal void UpdateBehavior()
        {
            if (GetParentGrid() == null || _item_link == null)
                return;

            ItemAlignment alignment = _item_link.GetAlignment();

            if (alignment.HasFlag(ItemAlignment.Left))
            {
                _item_link.SetX(GetX() + _item_link.GetMargin().Left);//
            }
            if (alignment.HasFlag(ItemAlignment.Right))
            {
                _item_link.SetX(GetX() + GetWidth() - _item_link.GetWidth() - _item_link.GetMargin().Right);//
            }
            if (alignment.HasFlag(ItemAlignment.Top))
            {
                _item_link.SetY(GetY() + _item_link.GetMargin().Top);//
            }
            if (alignment.HasFlag(ItemAlignment.Bottom))
            {
                _item_link.SetY(GetY() + GetHeight() - _item_link.GetHeight() - _item_link.GetMargin().Bottom);//
            }
            if (alignment.HasFlag(ItemAlignment.HCenter))
            {
                _item_link.SetX(GetX() + (GetWidth() - _item_link.GetWidth()) / 2 + _item_link.GetMargin().Left - _item_link.GetMargin().Right);//
            }
            if (alignment.HasFlag(ItemAlignment.VCenter))
            {
                _item_link.SetY(GetY() + (GetHeight() - _item_link.GetHeight()) / 2 + _item_link.GetMargin().Top - _item_link.GetMargin().Bottom);//
            }
        }

        public void PrintCellInfo()
        {
            Console.WriteLine(
                "X: " + _item_link.GetX() + "\n" +
                "Y: " + _item_link.GetY() + "\n" +
                "Row: " + _row_index + "\n" +
                "Column: " + _column_index + "\n" +
                "Width: " + GetWidth() + "\n" +
                "ItemW: " + _item_link.GetWidth() + "\n" +
                "Height: " + GetHeight() + "\n" +
                "ItemH: " + _item_link.GetHeight() + "\n" +
                "Item: " + _item_link?.GetItemName() + " type: " + _item_link?.ToString()
                );
        }
    }
}
