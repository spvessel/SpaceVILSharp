package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.WindowLayout;

interface InterfacePopUp {
    int getTimeOut();

    void setTimeOut(int milliseconds);

    void execute(WindowLayout wnd, String message);
}