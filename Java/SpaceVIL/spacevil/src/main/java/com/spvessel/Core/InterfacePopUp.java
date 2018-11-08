package com.spvessel.Core;

import com.spvessel.WindowLayout;

interface InterfacePopUp {
    int getTimeOut();

    void setTimeOut(int milliseconds);

    void execute(WindowLayout wnd, String message);
}