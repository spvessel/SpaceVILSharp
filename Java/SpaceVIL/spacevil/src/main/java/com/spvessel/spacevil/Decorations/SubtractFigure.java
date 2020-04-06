package com.spvessel.spacevil.Decorations;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Core.InterfaceEffect;
import com.spvessel.spacevil.Core.InterfaceSubtractFigure;
import com.spvessel.spacevil.Flags.ItemAlignment;

public class SubtractFigure implements InterfaceSubtractFigure, InterfaceEffect {

    public SubtractFigure(Figure figure) {
        setSubtractFigure(figure);
    }

    private Figure _figure = null;

    @Override
    public void setSubtractFigure(Figure figure) {
        _figure = figure;
    }

    @Override
    public Figure getSubtractFigure() {
        return _figure;
    }

    @Override
    public String getEffectName() {
        return this.getClass().getName();
    }

    private int _x = 0;
    private int _y = 0;

    @Override
    public void setPositionOffset(int x, int y) {
        _x = x;
        _y = y;
    }

    private float _widthScale = 1.0f;
    private float _heightScale = 1.0f;

    @Override
    public void setSizeScale(float wScale, float hScale) {
        _widthScale = wScale;
        _heightScale = hScale;
    }

    @Override
    public int getXOffset() {
        return _x;
    }

    @Override
    public int getYOffset() {
        return _y;
    }

    @Override
    public float getWidthScale() {
        return _widthScale;
    }

    @Override
    public float getHeightScale() {
        return _heightScale;
    }

    private List<ItemAlignment> _alignment = new LinkedList<>();

    @Override
    public List<ItemAlignment> getAlignment() {
        return _alignment;
    }

    @Override
    public void setAlignment(ItemAlignment... alignments) {
        List<ItemAlignment> alignmentList = Arrays.asList(alignments);
        if (_alignment != null && _alignment.equals(alignmentList))
            return;

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
            if (var.equals(ItemAlignment.LEFT))
                _left = var;
            if (var.equals(ItemAlignment.RIGHT))
                _right = var;
            if (var.equals(ItemAlignment.TOP))
                _top = var;
            if (var.equals(ItemAlignment.BOTTOM))
                _bottom = var;
            if (var.equals(ItemAlignment.HCENTER))
                _hcenter = var;
            if (var.equals(ItemAlignment.VCENTER))
                _vcenter = var;
        }

        if (_left != null && _right != null) {
            alignmentList.remove(ItemAlignment.RIGHT);
        }

        if (_top != null && _bottom != null) {
            alignmentList.remove(ItemAlignment.BOTTOM);
        }
        if (_hcenter != null) {
            if (alignmentList.contains(ItemAlignment.LEFT))
                alignmentList.remove(ItemAlignment.LEFT);
            if (alignmentList.contains(ItemAlignment.RIGHT))
                alignmentList.remove(ItemAlignment.RIGHT);
        }
        if (_vcenter != null) {
            if (alignmentList.contains(ItemAlignment.TOP))
                alignmentList.remove(ItemAlignment.TOP);
            if (alignmentList.contains(ItemAlignment.BOTTOM))
                alignmentList.remove(ItemAlignment.BOTTOM);
        }

        _alignment = alignmentList;
    }
}
