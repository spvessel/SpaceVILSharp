package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.Prototype;

public abstract class AbstractFreeLayout extends Prototype implements InterfaceLayout {
    private boolean _isUpdating = false;

    public final void updateLayout() {
        if (_isUpdating)
            return;
        _isUpdating = true;
        onUpdate();
        _isUpdating = false;
    }

    // @Override
    // public void setWidth(int width) {
    //     super.setWidth(width);
    //     updateLayout();
    // }

    // @Override
    // public void setX(int _x) {
    //     super.setX(_x);
    //     updateLayout();
    // }

    // @Override
    // public void setHeight(int height) {
    //     super.setHeight(height);
    //     updateLayout();
    // }

    // @Override
    // public void setY(int _y) {
    //     super.setY(_y);
    //     updateLayout();
    // }

    public abstract void onUpdate();
}