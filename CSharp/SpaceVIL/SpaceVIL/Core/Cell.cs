using SpaceVIL.Core;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace SpaceVIL
{
    public sealed class Cell : Geometry, IPosition
    {
        private IGrid _parent = null;

        /// <returns> parent grid of the cell </returns>
        public IGrid GetParentGrid()
        {
            return _parent;
        }
        internal Cell(IGrid grid)
        {
            _parent = grid;
        }
        internal Cell(IGrid grid, int row, int column) : this(grid)
        {
            _row_index = row;
            _column_index = column;
        }
        internal Cell(IGrid grid, int row, int column, IBaseItem item) : this(grid, row, column)
        {
            _itemLink = item;
        }

        //Indecies
        private int _row_index = 0;

        /// <returns> cell row number </returns>
        public int GetRow()
        {
            return _row_index;
        }

        /// <param name="index"> cell row number </param>
        public void SetRow(int index)
        {
            _row_index = index;
        }

        private int _column_index = 0;

        /// <returns> cell column number </returns>
        public int GetColumn()
        {
            return _column_index;
        }

        /// <param name="index"> cell column number </param>
        public void SetColumn(int index)
        {
            _column_index = index;
        }

        //Position
        private int _x = 0;
        private int _y = 0;

        /// <summary>
        /// X position of the cell
        /// </summary>
        public void SetX(int x)
        {
            _x = x;
        }
        public int GetX()
        {
            return _x;
        }

        /// <summary>
        /// Y position of the cell
        /// </summary>
        public void SetY(int y)
        {
            _y = y;
        }
        public int GetY()
        {
            return _y;
        }

        private IBaseItem _itemLink = null;

        /// <returns> cell item </returns>
        public IBaseItem GetItem()
        {
            return _itemLink;
        }

        /// <param name="item"> Set item into cell </param>
        public void SetItem(IBaseItem item)
        {
            _itemLink = item;
        }

        internal void UpdateBehavior()
        {
            if (GetParentGrid() == null || _itemLink == null)
                return;

            ItemAlignment alignment = _itemLink.GetAlignment();

            if (alignment.HasFlag(ItemAlignment.Left))
            {
                _itemLink.SetX(GetX() + _itemLink.GetMargin().Left);//
            }
            if (alignment.HasFlag(ItemAlignment.Right))
            {
                _itemLink.SetX(GetX() + GetWidth() - _itemLink.GetWidth() - _itemLink.GetMargin().Right);//
            }
            if (alignment.HasFlag(ItemAlignment.Top))
            {
                _itemLink.SetY(GetY() + _itemLink.GetMargin().Top);//
            }
            if (alignment.HasFlag(ItemAlignment.Bottom))
            {
                _itemLink.SetY(GetY() + GetHeight() - _itemLink.GetHeight() - _itemLink.GetMargin().Bottom);//
            }
            if (alignment.HasFlag(ItemAlignment.HCenter))
            {
                _itemLink.SetX(GetX() + (GetWidth() - _itemLink.GetWidth()) / 2 + _itemLink.GetMargin().Left - _itemLink.GetMargin().Right);//
            }
            if (alignment.HasFlag(ItemAlignment.VCenter))
            {
                _itemLink.SetY(GetY() + (GetHeight() - _itemLink.GetHeight()) / 2 + _itemLink.GetMargin().Top - _itemLink.GetMargin().Bottom);//
            }
        }

        internal void PrintCellInfo()
        {
            Console.WriteLine(
                "X: " + _itemLink.GetX() + "\n" +
                "Y: " + _itemLink.GetY() + "\n" +
                "Row: " + _row_index + "\n" +
                "Column: " + _column_index + "\n" +
                "Width: " + GetWidth() + "\n" +
                "ItemW: " + _itemLink.GetWidth() + "\n" +
                "Height: " + GetHeight() + "\n" +
                "ItemH: " + _itemLink.GetHeight() + "\n" +
                "Item: " + _itemLink?.GetItemName() + " type: " + _itemLink?.ToString()
                );
        }
    }
}
