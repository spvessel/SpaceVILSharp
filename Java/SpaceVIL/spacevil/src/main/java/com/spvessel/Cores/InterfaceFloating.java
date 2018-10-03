package com.spvessel.Cores;

public interface InterfaceFloating
    {
        void show(InterfaceItem sender, MouseArgs args);
        void hide();
        boolean isOutsideClickClosable();
        void setOutsideClickClosable(boolean value);
    }