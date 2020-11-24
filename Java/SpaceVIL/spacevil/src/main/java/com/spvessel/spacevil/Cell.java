package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.Geometry;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IFreeLayout;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.util.List;

/**
 * Cell is sealed class representing cell in com.spvessel.spacevil.Grid. 
 * You cannot create instances of Cell class, only to get it for proccessing 
 * from any instance of com.spvessel.spacevil.Grid class.
 */
public final class Cell extends Geometry {
    private IFreeLayout _parent = null;

    /**
     * Getting com.spvessel.spacevil.Core.IFreeLayout (usualy com.spvessel.spacevil.Grid) 
     * instance to which this Cell belongs.
     * @return Container with cells as com.spvessel.spacevil.Core.IFreeLayout (usualy com.spvessel.spacevil.Grid).
     */
    public IFreeLayout getParentGrid() {
        return _parent;
    }

    Cell(IFreeLayout grid) {
        _parent = grid;
    }

    Cell(IFreeLayout grid, int row, int column)
    {
        this(grid);
        _rowIndex = row;
        _columnIndex = column;
    }

    Cell(IFreeLayout grid, int row, int column, IBaseItem item) {
        this(grid, row, column);
        _itemLink = item;
    }

    // Indecies
    private int _rowIndex = 0;

    /**
     * Getting Cell row number.
     * @return Row number.
     */
    public int getRow() {
        return _rowIndex;
    }

    void setRow(int index) {
        _rowIndex = index;
    }

    private int _columnIndex = 0;

    /**
     * Getting Cell column number.
     * @return Column number.
     */
    public int getColumn() {
        return _columnIndex;
    }

    void setColumn(int index) {
        _columnIndex = index;
    }

    // Position
    private int _x = 0;
    private int _y = 0;

    void setX(int x) {
        _x = x;
    }

    /**
     * Getting X position of the Cell.
     * @return X position.
     */
    public int getX() {
        return _x;
    }

    void setY(int y) {
        _y = y;
    }

    /**
     * Getting Y position of the Cell.
     * @return Y position.
     */
    public int getY() {
        return _y;
    }

    private IBaseItem _itemLink = null;

    /**
     * Getting contained item in the Cell
     * @return Item as com.spvessel.spacevil.Core.IBaseItem
     */
    public IBaseItem getItem() {
        return _itemLink;
    }

    void setItem(IBaseItem item) {
        _itemLink = item;
    }

    void updateBehavior() {
        if (getParentGrid() == null || _itemLink == null)
            return;

        List<ItemAlignment> alignment = _itemLink.getAlignment();

        if (alignment.contains(ItemAlignment.Left)) {
            _itemLink.setX(getX() + _itemLink.getMargin().left);//
        }
        if (alignment.contains(ItemAlignment.Right)) {
            _itemLink.setX(getX() + getWidth() - _itemLink.getWidth() - _itemLink.getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.Top)) {
            _itemLink.setY(getY() + _itemLink.getMargin().top);//
        }
        if (alignment.contains(ItemAlignment.Bottom)) {
            _itemLink.setY(getY() + getHeight() - _itemLink.getHeight() - _itemLink.getMargin().bottom);//
        }
        if (alignment.contains(ItemAlignment.HCenter)) {
            _itemLink.setX(getX() + (getWidth() - _itemLink.getWidth()) / 2 + _itemLink.getMargin().left
                    - _itemLink.getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.VCenter)) {
            _itemLink.setY(getY() + (getHeight() - _itemLink.getHeight()) / 2 + _itemLink.getMargin().top
                    - _itemLink.getMargin().bottom);//
        }
    }

    void printCellInfo() {
        System.out.println("X: " + _itemLink.getX() + "\n" + "Y: " + _itemLink.getY() + "\n" + "Row: " + _rowIndex
                + "\n" + "Column: " + _columnIndex + "\n" + "Width: " + getWidth() + "\n" + "ItemW: "
                + _itemLink.getWidth() + "\n" + "Height: " + getHeight() + "\n" + "ItemH: " + _itemLink.getHeight()
                + "\n");
    }

}