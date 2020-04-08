package com.spvessel.spacevil.Decorations;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Core.InterfaceEffect;
import com.spvessel.spacevil.Core.InterfaceSubtractFigure;
import com.spvessel.spacevil.Flags.ItemAlignment;

/**
 * SubtractFigure is visual effect for applying to item's shape. 
 * Implements com.spvessel.spacevil.Core.InterfaceSubtractFigure and com.spvessel.spacevil.Core.InterfaceEffect.
 * <p> This visual effect cuts specified shape from original item's shape.
 */
public class SubtractFigure implements InterfaceSubtractFigure, InterfaceEffect {
    /**
     * Constructs subtract effect with specified shape.
     * @param figure Figure for subtraction as com.spvessel.spacevil.Decorations.Figure.
     */
    public SubtractFigure(Figure figure) {
        setSubtractFigure(figure);
    }

    private Figure _figure = null;

    /**
     * Setting shape for subtraction.
     * @param figure Figure for subtraction as com.spvessel.spacevil.Decorations.Figure.
     */
    public void setSubtractFigure(Figure figure) {
        _figure = figure;
    }

    /**
     * Getting the current figure for subtraction.
     * @return Figure for subtraction as com.spvessel.spacevil.Decorations.Figure.
     */
    public Figure getSubtractFigure() {
        return _figure;
    }

    /**
     * Getting the effect name. 
     * @return Returns name SubtractEffect as java.lang.String.
     */
    public String getEffectName() {
        return this.getClass().getName();
    }

    private int _x = 0;
    private int _y = 0;

    /**
     * Setting shape's shift by X, Y axis.
     * @param x X axis shift.
     * @param y Y axis shift.
     */
    public void setPositionOffset(int x, int y) {
        _x = x;
        _y = y;
    }

    private float _widthScale = 1.0f;
    private float _heightScale = 1.0f;

    /**
     * Setting shape's scaling factors for width and height.
     * @param wScale Scaling factor for width.
     * @param hScale Scaling factor for height.
     */
    public void setSizeScale(float wScale, float hScale) {
        _widthScale = wScale;
        _heightScale = hScale;
    }

    /**
     * Getting shape's shift by X-axis.
     * @return X axis shift.
     */
    public int getXOffset() {
        return _x;
    }

    /**
     * Getting shape's shift by Y-axis.
     * @return Y axis shift.
     */
    public int getYOffset() {
        return _y;
    }

    /**
     * Getting width scaling.
     * @return Width scaling.
     */
    public float getWidthScale() {
        return _widthScale;
    }

    /**
     * Getting height scaling.
     * @return Height scaling.
     */
    public float getHeightScale() {
        return _heightScale;
    }

    private List<ItemAlignment> _alignment = new LinkedList<>();

    /**
     * Getting shape's allignment within the item.
     * @return Alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public List<ItemAlignment> getAlignment() {
        return _alignment;
    }

    /**
     * Setting shape's allignment within the item.
     * @param alignments Alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setAlignment(ItemAlignment... alignments) {
        List<ItemAlignment> alignmentList = Arrays.asList(alignments);
        if (_alignment != null && _alignment.equals(alignmentList)) {
            return;
        }

        if (alignmentList == null) {
            _alignment = new LinkedList<>();
            return;
        }

        ItemAlignment _left = null;
        ItemAlignment _right = null;
        ItemAlignment _top = null;
        ItemAlignment _bottom = null;
        ItemAlignment _hcenter = null;
        ItemAlignment _vcenter = null;

        for (ItemAlignment var : alignmentList) {
            if (var.equals(ItemAlignment.LEFT)) {
                _left = var;
            }
            if (var.equals(ItemAlignment.RIGHT)) {
                _right = var;
            }
            if (var.equals(ItemAlignment.TOP)) {
                _top = var;
            }
            if (var.equals(ItemAlignment.BOTTOM)) {
                _bottom = var;
            }
            if (var.equals(ItemAlignment.HCENTER)) {
                _hcenter = var;
            }
            if (var.equals(ItemAlignment.VCENTER)) {
                _vcenter = var;
            }
        }

        if (_left != null && _right != null) {
            alignmentList.remove(ItemAlignment.RIGHT);
        }

        if (_top != null && _bottom != null) {
            alignmentList.remove(ItemAlignment.BOTTOM);
        }
        if (_hcenter != null) {
//            if (alignmentList.contains(ItemAlignment.LEFT))
                alignmentList.remove(ItemAlignment.LEFT);
//            if (alignmentList.contains(ItemAlignment.RIGHT))
                alignmentList.remove(ItemAlignment.RIGHT);
        }
        if (_vcenter != null) {
//            if (alignmentList.contains(ItemAlignment.TOP))
                alignmentList.remove(ItemAlignment.TOP);
//            if (alignmentList.contains(ItemAlignment.BOTTOM))
                alignmentList.remove(ItemAlignment.BOTTOM);
        }

        _alignment = alignmentList;
    }
}
