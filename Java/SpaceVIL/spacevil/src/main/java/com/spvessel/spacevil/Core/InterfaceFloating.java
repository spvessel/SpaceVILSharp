package com.spvessel.spacevil.Core;

public interface InterfaceFloating
    {
        void show(InterfaceItem sender, MouseArgs args);
        void show();
        void hide();
        boolean isOutsideClickClosable();
        void setOutsideClickClosable(boolean value);
    }