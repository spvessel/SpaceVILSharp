package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.Prototype;

public abstract class AbstractVerticalLayout extends Prototype implements InterfaceLayout {
    private boolean _isUpdating = false;

    public final void updateLayout() {
        if (_isUpdating)
            return;
        _isUpdating = true;
        onUpdate();
        _isUpdating = false;
    }

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