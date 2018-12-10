package com.spacevil;

import com.spacevil.Core.InterfaceBehavior;
import com.spacevil.Flags.ItemAlignment;
import com.spacevil.Flags.SizePolicy;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class Behavior implements InterfaceBehavior {

    private List<ItemAlignment> _alignment = new LinkedList<>();

    public List<ItemAlignment> getAlignment() {
        return _alignment;
    }

    public void setAlignment(List<ItemAlignment> alignment) {
        if (_alignment.equals(alignment))
            return;

        if (alignment == null) {
            _alignment = new LinkedList<>();
            return;
        }

        ItemAlignment _left = null;
        ItemAlignment _right = null;
        ItemAlignment _top = null;
        ItemAlignment _bottom = null;
        ItemAlignment _hcenter = null;
        ItemAlignment _vcenter = null;

        for (ItemAlignment var : alignment) {
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
            alignment.remove(ItemAlignment.RIGHT);
        }

        if (_top != null && _bottom != null) {
            alignment.remove(ItemAlignment.BOTTOM);
        }
        if (_hcenter != null) {
            if (alignment.contains(ItemAlignment.LEFT))
                alignment.remove(ItemAlignment.LEFT);
            if (alignment.contains(ItemAlignment.RIGHT))
                alignment.remove(ItemAlignment.RIGHT);
        }
        if (_vcenter != null) {
            if (alignment.contains(ItemAlignment.TOP))
                alignment.remove(ItemAlignment.TOP);
            if (alignment.contains(ItemAlignment.BOTTOM))
                alignment.remove(ItemAlignment.BOTTOM);
        }

        _alignment = alignment;
    }
    public void setAlignment(ItemAlignment... alignment) {
        List<ItemAlignment> list = Arrays.stream(alignment).collect(Collectors.toList());
        setAlignment(list);
    }

    public SizePolicy _w_policy = SizePolicy.FIXED;

    public void setWidthPolicy(SizePolicy policy) {
        _w_policy = policy;
    }

    public SizePolicy getWidthPolicy() {
        return _w_policy;
    }

    public SizePolicy _h_policy = SizePolicy.FIXED;

    public void setHeightPolicy(SizePolicy policy) {
        _h_policy = policy;
    }

    public SizePolicy getHeightPolicy() {
        return _h_policy;
    }
}