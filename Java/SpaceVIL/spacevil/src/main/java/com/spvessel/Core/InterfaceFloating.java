package com.spvessel.Core;

public interface InterfaceFloating
    {
        void show(InterfaceItem sender, MouseArgs args);
        void hide();
        boolean getOutsideClickClosable();
        void setOutsideClickClosable(boolean value);
    }