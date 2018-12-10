package com.spvessel.spacevil.Core;

public interface InterfaceFloating
    {
        void show(InterfaceItem sender, MouseArgs args);
        void hide();
        boolean isOutsideClickClosable();
        void setOutsideClickClosable(boolean value);
        // boolean isLockOutside();
        // void setLockOutside(boolean value);
    }