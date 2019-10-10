package com.spvessel.spacevil.Core;

public interface InterfaceFloating {
    void show();

    void show(InterfaceItem sender, MouseArgs args);

    void hide();

    void hide(MouseArgs args);

    boolean isOutsideClickClosable();

    void setOutsideClickClosable(boolean value);
}