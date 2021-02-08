using System;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// Cell is sealed class representing cell in SpaceVIL.Grid.
    /// You cannot create instances of Cell class, only to get it for proccessing
    /// from any instance of SpaceVIL.Grid class.
    /// </summary>
    public sealed class Cell : Geometry
    {
        private IFreeLayout _parent = null;

        /// <summary>
        /// Getting SpaceVIL.Core.IFreeLayout (usualy SpaceVIL.Grid) 
        /// instance to which this Cell belongs.
        /// </summary>
        /// <returns>Container with cells as SpaceVIL.Core.IFreeLayout 
        /// (usualy SpaceVIL.Grid).</returns>
        public IFreeLayout GetParentGrid()
        {
            return _parent;
        }
        internal Cell(IFreeLayout grid)
        {
            _parent = grid;
        }
        internal Cell(IFreeLayout grid, int row, int column) : this(grid)
        {
            _rowIndex = row;
            _columnIndex = column;
        }
        internal Cell(IFreeLayout grid, int row, int column, IBaseItem item) : this(grid, row, column)
        {
            _itemLink = item;
        }

        //Indecies
        private int _rowIndex = 0;

        /// <summary>
        /// Getting Cell row number.
        /// </summary>
        /// <returns>Row number.</returns>
        public int GetRow()
        {
            return _rowIndex;
        }
        internal void SetRow(int index)
        {
            _rowIndex = index;
        }

        private int _columnIndex = 0;

        /// <summary>
        /// Getting Cell column number.
        /// </summary>
        /// <returns>Column number.</returns>
        public int GetColumn()
        {
            return _columnIndex;
        }
        internal void SetColumn(int index)
        {
            _columnIndex = index;
        }

        //Position
        private int _x = 0;
        private int _y = 0;

        internal void SetX(int x)
        {
            _x = x;
        }

        /// <summary>
        /// Getting X position of the Cell.
        /// </summary>
        /// <returns>X position.</returns>
        public int GetX()
        {
            return _x;
        }

        internal void SetY(int y)
        {
            _y = y;
        }
        
        /// <summary>
        /// Getting Y position of the Cell.
        /// </summary>
        /// <returns>Y position.</returns>
        public int GetY()
        {
            return _y;
        }

        private IBaseItem _itemLink = null;

        /// <summary>
        /// Getting contained item in the Cell
        /// </summary>
        /// <returns>Item as SpaceVIL.Core.IBaseItem</returns>
        public IBaseItem GetItem()
        {
            return _itemLink;
        }

        internal void SetItem(IBaseItem item)
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
                "Row: " + _rowIndex + "\n" +
                "Column: " + _columnIndex + "\n" +
                "Width: " + GetWidth() + "\n" +
                "ItemW: " + _itemLink.GetWidth() + "\n" +
                "Height: " + GetHeight() + "\n" +
                "ItemH: " + _itemLink.GetHeight() + "\n" +
                "Item: " + _itemLink?.GetItemName() + " type: " + _itemLink?.ToString()
                );
        }
    }
}
