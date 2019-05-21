package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.Geometry;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfacePosition;
import com.spvessel.spacevil.Grid;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.util.List;

public final class Cell extends Geometry implements InterfacePosition {
    private Grid _parent = null;

    /**
     * @return parent grid of the cell
     */
    public Grid getParentGrid() {
        return _parent;
    }

    Cell(Grid grid, int row, int column) {
        _parent = grid;
        _row_index = row;
        _column_index = column;
    }

    // Indecies
    private int _row_index = 0;

    /**
     * @return cell row number
     */
    public int getRow() {
        return _row_index;
    }

    /**
     * @param index cell row number
     */
    public void setRow(int index) {
        _row_index = index;
    }

    private int _column_index = 0;

    /**
     * @return cell column number
     */
    public int getColumn() {
        return _column_index;
    }

    /**
     * @param index cell column number
     */
    public void setColumn(int index) {
        _column_index = index;
    }

    // Position
    private int _x = 0;
    private int _y = 0;

    /**
     * X position of the cell
     */
    public void setX(int x) {
        _x = x;
    }
    public int getX() {
        return _x;
    }

    /**
     * Y position of the cell
     */
    public void setY(int y) {
        _y = y;
    }
    public int getY() {
        return _y;
    }

    private InterfaceBaseItem _item_link = null;

    /**
     * @return cell item
     */
    public InterfaceBaseItem getItem() {
        return _item_link;
    }

    /**
     * Set item into cell
     */
    public void setItem(InterfaceBaseItem item) {
        _item_link = item;
    }

    void updateBehavior() {
        if (getParentGrid() == null || _item_link == null)
            return;

        List<ItemAlignment> alignment = _item_link.getAlignment();

        if (alignment.contains(ItemAlignment.LEFT)) {
            _item_link.setX(getX() + _item_link.getMargin().left);//
        }
        if (alignment.contains(ItemAlignment.RIGHT)) {
            _item_link.setX(getX() + getWidth() - _item_link.getWidth() - _item_link.getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.TOP)) {
            _item_link.setY(getY() + _item_link.getMargin().top);//
        }
        if (alignment.contains(ItemAlignment.BOTTOM)) {
            _item_link.setY(getY() + getHeight() - _item_link.getHeight() - _item_link.getMargin().bottom);//
        }
        if (alignment.contains(ItemAlignment.HCENTER)) {
            _item_link.setX(getX() + (getWidth() - _item_link.getWidth()) / 2 + _item_link.getMargin().left
                    - _item_link.getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.VCENTER)) {
            _item_link.setY(getY() + (getHeight() - _item_link.getHeight()) / 2 + _item_link.getMargin().top
                    - _item_link.getMargin().bottom);//
        }
    }

    void printCellInfo() {
        System.out.println("X: " + _item_link.getX() + "\n" + "Y: " + _item_link.getY() + "\n" + "Row: " + _row_index
                + "\n" + "Column: " + _column_index + "\n" + "Width: " + getWidth() + "\n" + "ItemW: "
                + _item_link.getWidth() + "\n" + "Height: " + getHeight() + "\n" + "ItemH: " + _item_link.getHeight()
                + "\n");
    }

}