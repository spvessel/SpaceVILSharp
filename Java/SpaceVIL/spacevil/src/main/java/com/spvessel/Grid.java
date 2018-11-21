package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.Cell;
import com.spvessel.Core.InterfaceBaseItem;
import com.spvessel.Core.InterfaceGrid;
import com.spvessel.Flags.SizePolicy;

import java.util.List;
import java.util.LinkedList;

public class Grid extends Prototype implements InterfaceGrid {
    static int count = 0;

    private Grid() {
        setItemName("Grid_" + count);
        count++;

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.Grid"));
        setStyle(DefaultsService.getDefaultStyle(Grid.class));
        isFocusable = false;
    }

    public Grid(int rows, int columns) {
        this();
        _row_count = rows;
        _column_count = columns;
        initCells();
    }

    // rows and counts
    private List<Cell> _cells;

    void initCells() {
        _cells = new LinkedList<Cell>();
        for (int i = 0; i < _row_count; i++) {
            for (int j = 0; j < _column_count; j++) {
                _cells.add(new Cell(this, i, j));
            }
        }
    }

    public void setFormat(int rows, int columns) {
        if (rows == _row_count && columns == _column_count)
            return;

        _row_count = rows;
        _column_count = columns;
        rearrangeCells();
    }

    private void rearrangeCells() {
        if (_cells == null) {
            initCells();
            return;
        }

        List<InterfaceBaseItem> items = new LinkedList<>();
        for (Cell cell : _cells)
            items.add(cell.getItem());
        initCells();
        int index = 0;
        for (InterfaceBaseItem item : items) {
            _cells.get(index).setItem(item);
            index++;
            if (_cells.size() == index)
                break;
        }
        updateLayout();
    }

    private int _row_count = 1;

    public void setRowCount(int capacity) {
        if (capacity != _row_count)
            _row_count = capacity;
        rearrangeCells();
    }

    public int getRowCount() {
        return _row_count;
    }

    private int _column_count = 1;

    public void setColumnCount(int capacity) {
        if (capacity != _column_count)
            _column_count = capacity;
        // Need to initCells REFACTOR!
        rearrangeCells();
    }

    public int getColumnCount() {
        return _column_count;
    }

    public Cell getCell(int row, int column) {
        Cell cell = null;
        try {
            cell = _cells.get(column + row * _column_count);
        } catch (Exception ex) {
            System.out.println("Cells row and colums out of range.\n" + ex.toString());
            return cell;
        }
        return cell;
    }

    public List<Cell> getAllCells() {
        return _cells;
    }

    // overrides
    @Override
    public boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    @Override
    public void removeItem(InterfaceBaseItem item) {
        super.removeItem(item);
        for (Cell link : _cells) {
            if (link.getItem() == item)
                link.setItem(null);
        }
        // UpdateLayout();
    }

    @Override
    public void addItem(InterfaceBaseItem item) {
        // ignore if it is out of space, add in free cell, attach row and collumn
        // numbers
        for (Cell cell : _cells) {
            if (cell.getItem() == null) {
                super.addItem(item);
                cell.setItem(item);
                updateLayout();
                return;
            }
        }
    }

    public void insertItem(InterfaceBaseItem item, int row, int column) {
        super.addItem(item);
        // _cells[row + column * _row_count].setItem(item);
        // Console.WriteLine(column + row * _column_count);
        _cells.get(column + row * _column_count).setItem(item);
        updateLayout();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    @Override
    public void setX(int _x) {
        super.setX(_x);
        updateLayout();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updateLayout();
    }

    @Override
    public void setY(int _y) {
        super.setY(_y);
        updateLayout();
    }

    // TMP
    private int[] colWidth;
    private int[] rowHeight;

    protected int[] getColWidth() {
        return colWidth;
    }

    protected int[] getRowHeight() {
        return rowHeight;
    }

    // update Layout
    public void updateLayout() {

        if (getItems().size() == 0)
            return;

        int[] columns_width = getColumnsWidth();
        colWidth = columns_width;

        int[] rows_height = getRowsHeight();
        rowHeight = rows_height;

        int x_offset = 0;
        int y_offset = 0;
        for (int r = 0; r < _row_count; r++) {
            int index = 0;
            for (int c = 0; c < _column_count; c++) {
                index = c + r * _column_count;

                InterfaceBaseItem item = _cells.get(index).getItem();

                _cells.get(index).setRow(r);
                _cells.get(index).setColumn(c);

                // 6
                _cells.get(index).setWidth(columns_width[c]);
                _cells.get(index).setHeight(rows_height[r]);
                _cells.get(index).setX(getX() + getPadding().left + x_offset);
                _cells.get(index).setY(getY() + getPadding().top + y_offset);

                if (item != null) {
                    if (item.getWidthPolicy() == SizePolicy.EXPAND)
                        item.setWidth(columns_width[c] - item.getMargin().left - item.getMargin().right);

                    if (item.getHeightPolicy() == SizePolicy.EXPAND)
                        item.setHeight(rows_height[r] - item.getMargin().top - item.getMargin().bottom);
                    item.setConfines();
                }

                // 7
                _cells.get(index).updateBehavior();
                x_offset += _cells.get(index).getWidth() + getSpacing().horizontal;
            }
            y_offset += _cells.get(index).getHeight() + getSpacing().vertical;
            x_offset = 0;
        }

    }

    private int[] getRowsHeight() {
        int[] rows_height = new int[_row_count];
        List<int[]> list_height = new LinkedList<int[]>();

        int total_space = getHeight() - getPadding().top - getPadding().bottom;
        int free_space = total_space;
        int prefer_height = (total_space - getSpacing().vertical * (_row_count - 1)) / _row_count;
        int count = _row_count;

        for (int r = 0; r < _row_count; r++) {
            for (int c = 0; c < _column_count; c++) {
                InterfaceBaseItem item = _cells.get(c + r * _column_count).getItem();

                if (item == null || !item.isVisible() || !item.isDrawable()) {
                    list_height.add(new int[] { r, -1 });
                    continue;
                }

                if (item.getHeightPolicy() == SizePolicy.FIXED) {
                    list_height.add(new int[] { r, item.getHeight() + item.getMargin().top + item.getMargin().bottom });
                } else {
                    list_height.add(new int[] { r, 0 });
                }
            }
        }
        ///////////
        List<Integer[]> m_height = new LinkedList<Integer[]>();
        for (int r = 0; r < _row_count; r++) {
            int max = -10;
            for (int c = 0; c < _column_count; c++) {
                if (list_height.get(c + r * _column_count)[1] > max)
                    max = list_height.get(c + r * _column_count)[1];
            }
            m_height.add(new Integer[] { r, max });
            if (max == -1) {
                count--;
                if (count == 0)
                    count++;
                prefer_height = (free_space - getSpacing().vertical * (count - 1)) / count;
            }
        }

        m_height.sort((li1, li2) -> li2[1].compareTo(li1[1]));

        for (Integer[] pair : m_height) {
            if (pair[1] == 0)
                pair[1] = prefer_height;
            else if (pair[1] < 0) {
                pair[1] = 0;
            } else {
                free_space -= pair[1];
                count--;
                if (count == 0)
                    count++;
                prefer_height = (free_space - getSpacing().vertical * (count - 1)) / count;
            }
        }

        m_height.sort((li1, li2) -> li1[0].compareTo(li2[0]));

        for (int i = 0; i < _row_count; i++)
            rows_height[i] = m_height.get(i)[1];

        return rows_height;
    }

    private int[] getColumnsWidth() {
        int[] columns_width = new int[_column_count];
        List<int[]> list_width = new LinkedList<int[]>();

        int total_space = getWidth() - getPadding().left - getPadding().right;
        int free_space = total_space;
        int prefer_width = (total_space - getSpacing().horizontal * (_column_count - 1)) / _column_count;
        int count = _column_count;

        for (int c = 0; c < _column_count; c++) {
            for (int r = 0; r < _row_count; r++) {
                InterfaceBaseItem item = _cells.get(c + r * _column_count).getItem();
                if (item == null || !item.isVisible() || !item.isDrawable()) {
                    list_width.add(new int[] { c, -1 });
                    continue;
                }

                if (item.getWidthPolicy() == SizePolicy.FIXED) {
                    list_width.add(new int[] { c, item.getWidth() + item.getMargin().left + item.getMargin().right });
                } else {
                    list_width.add(new int[] { c, 0 });
                }
            }
        }
        //////////
        List<Integer[]> m_width = new LinkedList<Integer[]>();
        for (int c = 0; c < _column_count; c++) {
            int max = -10;
            for (int r = 0; r < _row_count; r++) {
                if (list_width.get(r + c * _row_count)[1] > max)
                    max = list_width.get(r + c * _row_count)[1];
            }
            m_width.add(new Integer[] { c, max });
            if (max == -1) {
                count--;
                if (count == 0)
                    count++;
                prefer_width = (free_space - getSpacing().horizontal * (count - 1)) / count;
            }
        }

        m_width.sort((li1, li2) -> li2[1].compareTo(li1[1]));

        for (Integer[] pair : m_width) {
            if (pair[1] == 0)
                pair[1] = prefer_width;
            else if (pair[1] < 0) {
                pair[1] = 0;
            } else {
                free_space -= pair[1];
                count--;
                if (count == 0)
                    count++;
                prefer_width = (free_space - getSpacing().horizontal * (count - 1)) / count;
            }
        }

        m_width.sort((li1, li2) -> li1[0].compareTo(li2[0]));

        for (int i = 0; i < _column_count; i++)
            columns_width[i] = m_width.get(i)[1];

        return columns_width;
    }
}