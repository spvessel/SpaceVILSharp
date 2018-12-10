package com.spacevil.Core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class Behavior implements InterfaceBehavior {

    private List<com.spacevil.Flags.ItemAlignment> _alignment = new LinkedList<>();

    public List<com.spacevil.Flags.ItemAlignment> getAlignment() {
        return _alignment;
    }

    public void setAlignment(List<com.spacevil.Flags.ItemAlignment> alignment) {
        if (_alignment.equals(alignment))
            return;

        if (alignment == null) {
            _alignment = new LinkedList<>();
            return;
        }

        com.spacevil.Flags.ItemAlignment _left = null;
        com.spacevil.Flags.ItemAlignment _right = null;
        com.spacevil.Flags.ItemAlignment _top = null;
        com.spacevil.Flags.ItemAlignment _bottom = null;
        com.spacevil.Flags.ItemAlignment _hcenter = null;
        com.spacevil.Flags.ItemAlignment _vcenter = null;

        for (com.spacevil.Flags.ItemAlignment var : alignment) {
            if (var.equals(com.spacevil.Flags.ItemAlignment.LEFT))
                _left = var;
            if (var.equals(com.spacevil.Flags.ItemAlignment.RIGHT))
                _right = var;
            if (var.equals(com.spacevil.Flags.ItemAlignment.TOP))
                _top = var;
            if (var.equals(com.spacevil.Flags.ItemAlignment.BOTTOM))
                _bottom = var;
            if (var.equals(com.spacevil.Flags.ItemAlignment.HCENTER))
                _hcenter = var;
            if (var.equals(com.spacevil.Flags.ItemAlignment.VCENTER))
                _vcenter = var;
        }

        if (_left != null && _right != null) {
            alignment.remove(com.spacevil.Flags.ItemAlignment.RIGHT);
        }

        if (_top != null && _bottom != null) {
            alignment.remove(com.spacevil.Flags.ItemAlignment.BOTTOM);
        }
        if (_hcenter != null) {
            if (alignment.contains(com.spacevil.Flags.ItemAlignment.LEFT))
                alignment.remove(com.spacevil.Flags.ItemAlignment.LEFT);
            if (alignment.contains(com.spacevil.Flags.ItemAlignment.RIGHT))
                alignment.remove(com.spacevil.Flags.ItemAlignment.RIGHT);
        }
        if (_vcenter != null) {
            if (alignment.contains(com.spacevil.Flags.ItemAlignment.TOP))
                alignment.remove(com.spacevil.Flags.ItemAlignment.TOP);
            if (alignment.contains(com.spacevil.Flags.ItemAlignment.BOTTOM))
                alignment.remove(com.spacevil.Flags.ItemAlignment.BOTTOM);
        }

        _alignment = alignment;
    }
    public void setAlignment(com.spacevil.Flags.ItemAlignment... alignment) {
        List<com.spacevil.Flags.ItemAlignment> list = Arrays.stream(alignment).collect(Collectors.toList());
        setAlignment(list);
    }

    public com.spacevil.Flags.SizePolicy _w_policy = com.spacevil.Flags.SizePolicy.FIXED;

    public void setWidthPolicy(com.spacevil.Flags.SizePolicy policy) {
        _w_policy = policy;
    }

    public com.spacevil.Flags.SizePolicy getWidthPolicy() {
        return _w_policy;
    }

    public com.spacevil.Flags.SizePolicy _h_policy = com.spacevil.Flags.SizePolicy.FIXED;

    public void setHeightPolicy(com.spacevil.Flags.SizePolicy policy) {
        _h_policy = policy;
    }

    public com.spacevil.Flags.SizePolicy getHeightPolicy() {
        return _h_policy;
    }
}