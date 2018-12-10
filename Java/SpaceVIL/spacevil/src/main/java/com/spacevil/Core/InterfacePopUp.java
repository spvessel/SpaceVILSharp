package com.spacevil.Core;

import com.spacevil.WindowLayout;

interface InterfacePopUp {
    int getTimeOut();

    void setTimeOut(int milliseconds);

    void execute(WindowLayout wnd, String message);
}