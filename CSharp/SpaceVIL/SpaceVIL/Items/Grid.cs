﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using System.Threading;

namespace SpaceVIL
{
    public class Grid : Prototype, IFreeLayout
    {
        static int count = 0;

        /// <summary>
        /// Constructs a Grid
        /// </summary>
        private Grid()
        {
            SetItemName("Grid_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.Grid)));
            IsFocusable = false;
        }

        /// <summary>
        /// Constructs a Grid with the given rows and columns
        /// </summary>
        public Grid(int rows, int columns) : this()
        {
            _rowCount = rows;
            _columnCount = columns;
            InitCells();
        }

        //rows and counts
        private List<Cell> _cells;
        internal void InitCells()
        {
            _cells = new List<Cell>();
            for (int i = 0; i < _rowCount; i++)
            {
                for (int j = 0; j < _columnCount; j++)
                {
                    _cells.Add(new Cell(this, i, j));
                }
            }
        }
        public void SetFormat(int rows, int columns)
        {
            if (rows == _rowCount && columns == _columnCount)
                return;

            _rowCount = rows;
            _columnCount = columns;
            RearrangeCells();
        }
        private void RearrangeCells()
        {
            if (_cells == null)
            {
                InitCells();
                return;
            }

            List<IBaseItem> items = new List<IBaseItem>();
            foreach (var cell in _cells)
                items.Add(cell.GetItem());
            InitCells();
            int index = 0;
            foreach (var item in items)
            {
                _cells.ElementAt(index).SetItem(item);
                index++;
                if (_cells.Count == index)
                    break;
            }
            UpdateLayout();
        }
        private int _rowCount = 1;

        /// <summary>
        /// Set count of the rows
        /// </summary>
        public void SetRowCount(int capacity)
        {
            if (capacity != _rowCount)
                _rowCount = capacity;
            RearrangeCells();
        }
        public int GetRowCount()
        {
            return _rowCount;
        }
        private int _columnCount = 1;

        /// <summary>
        /// Set count of the columns
        /// </summary>
        public void SetColumnCount(int capacity)
        {
            if (capacity != _columnCount)
                _columnCount = capacity;
            //Need to InitCells REFACTOR!
            RearrangeCells();
        }
        public int GetColumnCount()
        {
            return _columnCount;
        }

        /// <summary>
        /// Returns the cell by row and column number
        /// </summary>
        public Cell GetCell(int row, int column)
        {
            Cell cell = null;
            try
            {
                cell = _cells[column + row * _columnCount];
            }
            catch (Exception ex)
            {
                Console.WriteLine("Cells row and colums out of range.\n" + ex.ToString());
                return cell;
            }
            return cell;
        }

        /// <returns> all cells list </returns>
        public List<Cell> GetAllCells()
        {
            return _cells;
        }

        public override bool RemoveItem(IBaseItem item)
        {
            bool baseBool = base.RemoveItem(item);
            foreach (var link in _cells)
            {
                if (link.GetItem() == item)
                    link.SetItem(null);
            }
            // UpdateLayout();
            return baseBool;
        }

        public void RemoveItem(int row, int column)
        {
            if (row == _rowCount || column == _columnCount)
                return;

            IBaseItem ibi = _cells[column + row * _columnCount].GetItem();
            if (ibi != null)
            {
                base.RemoveItem(ibi);
                _cells[column + row * _columnCount].SetItem(null);
            }
        }

        private Object Locker = new Object();
        public override void Clear()
        {
            Monitor.Enter(Locker);
            try
            {
                List<IBaseItem> list = GetItems();
                while (list.Count > 0)
                    RemoveItem(list.First());
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - Clear");
                Console.WriteLine(ex.StackTrace);
            }
            finally
            {
                Monitor.Exit(Locker);
            }
        }

        //overrides
        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }

        /// <summary>
        /// Add item to the Grid
        /// </summary>
        public override void AddItem(IBaseItem item)
        {
            //ignore if it is out of space, add in free cell, attach row and collumn numbers
            foreach (Cell cell in _cells)
            {
                if (cell.GetItem() == null)
                {
                    base.AddItem(item);
                    cell.SetItem(item);
                    UpdateLayout();
                    return;
                }
            }
        }

        /// <summary>
        /// Insert item to the Grid by row and column number
        /// </summary>
        public void InsertItem(IBaseItem item, int row, int column)
        {
            if (row == _rowCount || column == _columnCount)
                return;
            base.AddItem(item);
            //_cells[row + column * _rowCount].SetItem(item);

            RemoveItem(row, column);

            _cells[column + row * _columnCount].SetItem(item);
            UpdateLayout();
        }

        public override void InsertItem(IBaseItem item, int index)
        {
            if (_columnCount == 0)
                return;
            int row, column;
            row = index / _columnCount;
            column = index - row * _columnCount;
            InsertItem(item, row, column);
        }

        /// <summary>
        /// Set width of the Grid
        /// </summary>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateLayout();
        }

        /// <summary>
        /// Set height of the Grid
        /// </summary>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateLayout();
        }

        /// <summary>
        /// Set X position of the Grid
        /// </summary>
        public override void SetX(int _x)
        {
            base.SetX(_x);
            UpdateLayout();
        }

        /// <summary>
        /// Set Y position of the Grid
        /// </summary>
        public override void SetY(int _y)
        {
            base.SetY(_y);
            UpdateLayout();
        }

        //TMP
        private Int32[] colWidth;
        private Int32[] rowHeight;
        internal Int32[] GetColWidth()
        {
            return colWidth;
        }
        internal Int32[] GetRowHeight()
        {
            return rowHeight;
        }

        private bool _isUpdating = false;
        /// <summary>
        /// Update all children and grid sizes and positions
        /// according to confines
        /// </summary>
        //Update Layout
        public void UpdateLayout()
        {
            // Console.WriteLine("grid update");
            /*
             * Алгоритм:
             * 1. найти максимумы по ширине в столбцах
             * 2. запомнить размеры по ширине каждой ячейки
             * 3. найти все максимумы по высоте в строках
             * 4. запомнить размеры по высоте каждой ячейки
             * 5. цикл по каждой ячейке
             * 6. установть размеры ячейки
             * 7. разместить элемент в ячейке
             * 8. повторить цикл с пункта 5, пока есть ячейки
             * 9. готово, пить кофе
             */

            /*
             * Учесть
             * У клетки минимальную/максимальную длину/ширину
             * Выравнивание элемента
             * Политику растягивания размеров
             */
            List<IBaseItem> list = GetItems();
            if (list == null || list.Count == 0 || _isUpdating)
                return;
            _isUpdating = true;

            //1, 2
            Int32[] columns_width = GetColumnsWidth();
            colWidth = columns_width;

            //3, 4
            Int32[] rows_height = GetRowsHeight();
            rowHeight = rows_height;

            //5
            int x_offset = 0;
            int y_offset = 0;
            for (int r = 0; r < _rowCount; r++)
            {
                int index = 0;
                for (int c = 0; c < _columnCount; c++)
                {
                    // index = r + c * _rowCount;
                    index = c + r * _columnCount;

                    IBaseItem item = _cells[index].GetItem();
                    /*if (item == null)
                        continue;*/

                    _cells[index].SetRow(r);
                    _cells[index].SetColumn(c);

                    //6
                    _cells[index].SetWidth(columns_width[c]);
                    _cells[index].SetHeight(rows_height[r]);
                    _cells[index].SetX(GetX() + GetPadding().Left + x_offset);
                    _cells[index].SetY(GetY() + GetPadding().Top + y_offset);

                    if (item != null)
                    {
                        if (item.GetWidthPolicy().HasFlag(SizePolicy.Expand))
                            item.SetWidth(columns_width[c] - item.GetMargin().Left - item.GetMargin().Right);

                        if (item.GetHeightPolicy().HasFlag(SizePolicy.Expand))
                            item.SetHeight(rows_height[r] - item.GetMargin().Top - item.GetMargin().Bottom);
                        item.SetConfines();
                    }

                    //7
                    _cells[index].UpdateBehavior();

                    x_offset += _cells[index].GetWidth() + GetSpacing().Horizontal;
                }
                y_offset += _cells[index].GetHeight() + GetSpacing().Vertical;
                x_offset = 0;
            }
            _isUpdating = false;
        }

        private Int32[] GetRowsHeight()
        {
            Int32[] rows_height = new Int32[_rowCount];
            List<int[]> list_height = new List<int[]>();

            int total_space = GetHeight() - GetPadding().Top - GetPadding().Bottom;
            int free_space = total_space;
            int prefer_height = (total_space - GetSpacing().Vertical * (_rowCount - 1)) / _rowCount;
            int count = _rowCount;

            for (int r = 0; r < _rowCount; r++)
            {
                for (int c = 0; c < _columnCount; c++)
                {
                    IBaseItem item = _cells[c + r * _columnCount].GetItem();

                    if (item == null || !item.IsVisible() || !item.IsDrawable())
                    {
                        list_height.Add(new int[2] { r, -1 });
                        continue;
                    }

                    if (item.GetHeightPolicy() == SizePolicy.Fixed)
                    {
                        list_height.Add(new int[2] { r, item.GetHeight() + item.GetMargin().Top + item.GetMargin().Bottom });
                    }
                    else
                    {
                        list_height.Add(new int[2] { r, 0 });
                    }
                }
            }
            ///////////
            List<int[]> m_height = new List<int[]>();
            for (int r = 0; r < _rowCount; r++)
            {
                int max = -10;
                for (int c = 0; c < _columnCount; c++)
                {
                    if (list_height[c + r * _columnCount][1] > max)
                        max = list_height[c + r * _columnCount][1];
                }
                m_height.Add(new int[2] { r, max });
                if (max == -1)
                {
                    count--;
                    if (count == 0)
                        count++;
                    prefer_height = (free_space - GetSpacing().Vertical * (count - 1)) / count;
                }
            }

            m_height.Sort((x, y) => y[1].CompareTo(x[1]));

            foreach (var pair in m_height)
            {
                if (pair[1] == 0)
                    pair[1] = prefer_height;
                else if (pair[1] < 0)
                {
                    pair[1] = 0;
                }
                else
                {
                    free_space -= pair[1];
                    count--;
                    if (count == 0)
                        count++;
                    prefer_height = (free_space - GetSpacing().Vertical * (count - 1)) / count;
                }
            }

            m_height.Sort((x, y) => x[0].CompareTo(y[0]));

            for (int i = 0; i < rows_height.Length; i++)
                rows_height[i] = m_height[i][1];

            return rows_height;
        }

        private Int32[] GetColumnsWidth()
        {
            Int32[] columns_width = new Int32[_columnCount];
            List<int[]> list_width = new List<int[]>();

            int total_space = GetWidth() - GetPadding().Left - GetPadding().Right;
            int free_space = total_space;
            //int prefer_width = (int)Math.Round((float)(total_space - GetSpacing().Horizontal * (_columnCount - 1)) / (float)_columnCount, MidpointRounding.AwayFromZero);
            int prefer_width = (total_space - GetSpacing().Horizontal * (_columnCount - 1)) / _columnCount;
            int count = _columnCount;

            for (int c = 0; c < _columnCount; c++)
            {
                for (int r = 0; r < _rowCount; r++)
                {
                    IBaseItem item = _cells[c + r * _columnCount].GetItem();
                    if (item == null || !item.IsVisible() || !item.IsDrawable())
                    {
                        list_width.Add(new int[2] { c, -1 });
                        continue;
                    }

                    if (item.GetWidthPolicy() == SizePolicy.Fixed)
                    {
                        list_width.Add(new int[2] { c, item.GetWidth() + item.GetMargin().Left + item.GetMargin().Right });
                    }
                    else
                    {
                        list_width.Add(new int[2] { c, 0 });
                    }
                }
            }
            //////////
            List<int[]> m_width = new List<int[]>();
            for (int c = 0; c < _columnCount; c++)
            {
                int max = -10;
                for (int r = 0; r < _rowCount; r++)
                {
                    if (list_width[r + c * _rowCount][1] > max)
                        max = list_width[r + c * _rowCount][1];
                }
                m_width.Add(new int[2] { c, max });
                if (max == -1)
                {
                    count--;
                    if (count == 0)
                        count++;
                    prefer_width = (free_space - GetSpacing().Horizontal * (count - 1)) / count;
                }
            }

            m_width.Sort((x, y) => y[1].CompareTo(x[1]));

            foreach (var pair in m_width)
            {
                if (pair[1] == 0)
                    pair[1] = (int)prefer_width;
                else if (pair[1] < 0)
                {
                    pair[1] = 0;
                }
                else
                {
                    free_space -= pair[1];
                    count--;
                    if (count == 0)
                        count++;
                    prefer_width = (free_space - GetSpacing().Horizontal * (count - 1)) / count;
                }
            }

            m_width.Sort((x, y) => x[0].CompareTo(y[0]));

            for (int i = 0; i < columns_width.Length; i++)
                columns_width[i] = m_width[i][1];

            return columns_width;
        }
    }
}
