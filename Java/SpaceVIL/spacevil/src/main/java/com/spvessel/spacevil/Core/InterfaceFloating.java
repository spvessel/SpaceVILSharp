package com.spvessel.spacevil.Core;

public interface InterfaceFloating {
    public void show();

    public void show(InterfaceItem sender, MouseArgs args);

    public void hide();

    public void hide(MouseArgs args);

    public boolean isOutsideClickClosable();

    public void setOutsideClickClosable(boolean value);
}