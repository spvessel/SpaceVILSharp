package com.spvessel.Cores;

public interface InterfaceFloating
    {
        void show(InterfaceItem sender, MouseArgs args);
        void hide();
        Boolean isOutsideClickClosable();
        void setOutsideClickClosable(Boolean value);
    }