package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.IBehavior;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.LinkedList;
import java.util.List;

class Behavior implements IBehavior {

    private List<ItemAlignment> _alignment = new LinkedList<>();

    public List<ItemAlignment> getAlignment() {
        return _alignment;
    }

    public void setAlignment(List<ItemAlignment> alignment) {
        if (_alignment != null && _alignment.equals(alignment)) {
            return;
        }

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
            if (var.equals(ItemAlignment.Left)) {
                _left = var;
            }
            if (var.equals(ItemAlignment.Right)) {
                _right = var;
            }
            if (var.equals(ItemAlignment.Top)) {
                _top = var;
            }
            if (var.equals(ItemAlignment.Bottom)) {
                _bottom = var;
            }
            if (var.equals(ItemAlignment.HCenter)) {
                _hcenter = var;
            }
            if (var.equals(ItemAlignment.VCenter)) {
                _vcenter = var;
            }
        }

        if (_left != null && _right != null) {
            alignment.remove(ItemAlignment.Right);
        }

        if (_top != null && _bottom != null) {
            alignment.remove(ItemAlignment.Bottom);
        }
        if (_hcenter != null) {
            if (alignment.contains(ItemAlignment.Left)) {
                alignment.remove(ItemAlignment.Left);
            }
            if (alignment.contains(ItemAlignment.Right)) {
                alignment.remove(ItemAlignment.Right);
            }
        }
        if (_vcenter != null) {
            if (alignment.contains(ItemAlignment.Top)) {
                alignment.remove(ItemAlignment.Top);
            }
            if (alignment.contains(ItemAlignment.Bottom)) {
                alignment.remove(ItemAlignment.Bottom);
            }
        }

        _alignment = alignment;
    }
    public void setAlignment(ItemAlignment... alignment) {
        setAlignment(BaseItemStatics.composeFlags(alignment)); //Arrays.asList(alignment));
    }

    public SizePolicy _w_policy = SizePolicy.Fixed;

    public void setWidthPolicy(SizePolicy policy) {
        _w_policy = policy;
    }

    public SizePolicy getWidthPolicy() {
        return _w_policy;
    }

    public SizePolicy _h_policy = SizePolicy.Fixed;

    public void setHeightPolicy(SizePolicy policy) {
        _h_policy = policy;
    }

    public SizePolicy getHeightPolicy() {
        return _h_policy;
    }
}