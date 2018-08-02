using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class Grid : VisualItem, IGrid
    {
        static int count = 0;

        public Grid()
        {
            SetItemName("Grid_" + count);
            SetBackground(Color.Transparent);
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            count++;
        }

        public Grid(int rows, int columns) : this()
        {
            _row_count = rows;
            _column_count = columns;
            InitCells();
        }

        //rows and counts
        private List<Cell> _cells;
        private void InitCells()
        {
            _cells = new List<Cell>();
            for (int i = 0; i < _row_count; i++)
            {
                for (int j = 0; j < _column_count; j++)
                {
                    _cells.Add(new Cell(this, i, j));
                }
            }
        }
        private int _row_count = 1;
        public void SetRowCount(int capacity)
        {
            _row_count = capacity;
        }
        public int GetRowCount()
        {
            return _row_count;
        }
        private int _column_count = 1;
        public void SetColumnCount(int capacity)
        {
            _column_count = capacity;
        }
        public int GetColumnCount()
        {
            return _column_count;
        }

        public Cell GetCell(int row, int column)
        {
            Cell cell = null;
            try
            {
                cell = _cells[column + row * _column_count];
            }
            catch (Exception ex)
            {
                Console.WriteLine("Cells row and colums out of range.\n" + ex.ToString());
                return cell;
            }
            return cell;
        }
        public List<Cell> GetAllCells()
        {
            return _cells;
        }

        //overrides
        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }
        public override void AddItem(BaseItem item)
        {
            //ignore if it is out of space, add in free cell, attach row and collumn numbers
            base.AddItem(item);
            UpdateLayout();
        }
        public void InsertItem(BaseItem item, int row, int column)
        {
            AddItem(item);
            _cells[row + column * _row_count].SetItem(item);
            UpdateLayout();
        }
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateLayout();
        }
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateLayout();
        }
        public override void SetX(int _x)
        {
            base.SetX(_x);
            UpdateLayout();
        }
        public override void SetY(int _y)
        {
            base.SetY(_y);
            UpdateLayout();
        }

        //Update Layout
        public void UpdateLayout()
        {
            /*
             * Алгоритм:
             * 1. найти максимумы по ширине в столбцах
             * 2. запомнить размеры по ширине каждой ячейки
             * 3. найти все максимумы по высоте в строках
             * 4. запомнить размеры по высоте каждой ячейки
             * 5. цикл по каждой ячейки
             * 6. установть размеры ячейки
             * 7. разместить элемент в ячейке
             * 8. повторить цикл с пункта 5 пока есть ячейки
             * 9. готово, пить кофе
             */

            /*
             * Учесть
             * У клетки минимальную/максимальную длину/ширину
             * Выравнивание элемента
             * Политику растягивания размеров
             */
            if (GetItems().Count == 0)
                return;

            //1, 2
            Int32[] columns_width = GetColumnsWidth();
            //3, 4
            Int32[] rows_height = GetRowsHeight();
            /*foreach (var item in rows_height)
            {
                Console.WriteLine(item + " ");
            }*/
            //5
            int x_offset = 0;
            int y_offset = 0;
            for (int r = 0; r < _row_count; r++)
            {
                int index = 0;
                for (int c = 0; c < _column_count; c++)
                {
                    index = r + c * _row_count;

                    BaseItem item = _cells[index].GetItem();
                    if (item == null)
                        continue;

                    _cells[index].SetRow(r);
                    _cells[index].SetColumn(c);

                    //6
                    _cells[index].SetWidth(columns_width[c]);
                    if (item.GetWidthPolicy().HasFlag(SizePolicy.Expand))
                        item.SetWidth(columns_width[c] - item.GetMargin().Left - item.GetMargin().Right);

                    _cells[index].SetHeight(rows_height[r]);
                    if (item.GetHeightPolicy().HasFlag(SizePolicy.Expand))
                        item.SetHeight(rows_height[r] - item.GetMargin().Top - item.GetMargin().Bottom);
                    //7

                    _cells[index].SetX(GetX() + x_offset);
                    _cells[index].SetY(GetY() + y_offset);
                    _cells[index].UpdateBehavior();

                    x_offset += _cells[index].GetWidth() + GetSpacing().Horizontal;
                }
                y_offset += _cells[index].GetHeight() + GetSpacing().Vertical;
                x_offset = 0;
            }
        }

        private Int32[] GetRowsHeight()
        {
            Int32[] rows_height = new Int32[_row_count];
            List<int[]> list_height = new List<int[]>();

            int total_space = GetHeight() - GetPadding().Top - GetPadding().Bottom;
            int free_space = total_space;
            int prefer_height = (total_space - GetSpacing().Vertical * (_row_count - 1)) / _row_count;
            int count = _row_count;

            for (int r = 0; r < _row_count; r++)
            {
                for (int c = 0; c < _column_count; c++)
                {
                    BaseItem item = _cells[r + c * _row_count].GetItem();
                    if (item == null || !item.IsVisible)
                    {
                        list_height.Add(new int[2] { r, -1 });
                        count--;
                        if (count == 0)
                            count++;
                        prefer_height = (free_space - GetSpacing().Vertical * count) / count;
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
            for (int r = 0; r < _row_count; r++)
            {
                int max = -10;
                for (int c = 0; c < _column_count; c++)
                {
                    if (list_height[c + r * _column_count][1] > max)
                        max = list_height[c + r * _column_count][1];
                }
                m_height.Add(new int[2] { r, max });
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
                    prefer_height = (free_space - GetSpacing().Vertical * count) / count;
                }
            }

            m_height.Sort((x, y) => x[0].CompareTo(y[0]));

            for (int i = 0; i < rows_height.Length; i++)
                rows_height[i] = m_height[i][1];

            return rows_height;
        }

        private Int32[] GetColumnsWidth()
        {
            Int32[] columns_width = new Int32[_column_count];
            List<int[]> list_width = new List<int[]>();

            int total_space = GetWidth() - GetPadding().Left - GetPadding().Right;
            int free_space = total_space;
            int prefer_width = (total_space - GetSpacing().Horizontal * (_column_count - 1)) / _column_count;
            int count = _column_count;

            for (int c = 0; c < _column_count; c++)
            {
                for (int r = 0; r < _row_count; r++)
                {
                    BaseItem item = _cells[r + c * _row_count].GetItem();
                    if (item == null || !item.IsVisible)
                    {
                        list_width.Add(new int[2] { c, -1 });
                        count--;
                        if (count == 0)
                            count++;
                        prefer_width = (free_space - GetSpacing().Horizontal * count) / count;
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
            for (int c = 0; c < _column_count; c++)
            {
                int max = -1;
                for (int r = 0; r < _row_count; r++)
                {
                    if (list_width[r + c * _row_count][1] > max)
                        max = list_width[r + c * _row_count][1];
                }
                m_width.Add(new int[2] { c, max });
            }
            m_width.Sort((x, y) => y[1].CompareTo(x[1]));
            foreach (var pair in m_width)
            {
                if (pair[1] == 0)
                    pair[1] = prefer_width;
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
                    prefer_width = (free_space - GetSpacing().Horizontal * count) / count;
                }
            }

            m_width.Sort((x, y) => x[0].CompareTo(y[0]));

            for (int i = 0; i < columns_width.Length; i++)
                columns_width[i] = m_width[i][1];

            return columns_width;
        }
    }
}
