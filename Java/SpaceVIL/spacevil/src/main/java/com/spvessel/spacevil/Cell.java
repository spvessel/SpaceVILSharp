package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.Geometry;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceFreeLayout;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.util.List;

/**
 * Cell is sealed class representing cell in SpaceVIL.Grid. 
 * You cannot create instances of Cell class, only to get it for proccessing 
 * from any instance of SpaceVIL.Grid class.
 */
public final class Cell extends Geometry {
    private InterfaceFreeLayout _parent = null;

    /**
     * Getting SpaceVIL.Core.IFreeLayout (usualy SpaceVIL.Grid) 
     * instance to which this Cell belongs.
     * @return Container with cells as com.spvessel.spacevil.Core.InterfaceFreeLayout (usualy SpaceVIL.Grid).
     */
    public InterfaceFreeLayout getParentGrid() {
        return _parent;
    }

    Cell(InterfaceFreeLayout grid) {
        _parent = grid;
    }

    Cell(InterfaceFreeLayout grid, int row, int column)
    {
        this(grid);
        _rowIndex = row;
        _columnIndex = column;
    }

    Cell(InterfaceFreeLayout grid, int row, int column, InterfaceBaseItem item) {
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

    private InterfaceBaseItem _itemLink = null;

    /**
     * Getting contained item in the Cell
     * @return Item as com.spvessel.spacevil.Core.InterfaceBaseItem
     */
    public InterfaceBaseItem getItem() {
        return _itemLink;
    }

    void setItem(InterfaceBaseItem item) {
        _itemLink = item;
    }

    void updateBehavior() {
        if (getParentGrid() == null || _itemLink == null)
            return;

        List<ItemAlignment> alignment = _itemLink.getAlignment();

        if (alignment.contains(ItemAlignment.LEFT)) {
            _itemLink.setX(getX() + _itemLink.getMargin().left);//
        }
        if (alignment.contains(ItemAlignment.RIGHT)) {
            _itemLink.setX(getX() + getWidth() - _itemLink.getWidth() - _itemLink.getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.TOP)) {
            _itemLink.setY(getY() + _itemLink.getMargin().top);//
        }
        if (alignment.contains(ItemAlignment.BOTTOM)) {
            _itemLink.setY(getY() + getHeight() - _itemLink.getHeight() - _itemLink.getMargin().bottom);//
        }
        if (alignment.contains(ItemAlignment.HCENTER)) {
            _itemLink.setX(getX() + (getWidth() - _itemLink.getWidth()) / 2 + _itemLink.getMargin().left
                    - _itemLink.getMargin().right);//
        }
        if (alignment.contains(ItemAlignment.VCENTER)) {
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