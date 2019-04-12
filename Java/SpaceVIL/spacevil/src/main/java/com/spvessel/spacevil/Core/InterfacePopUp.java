package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.CoreWindow;

interface InterfacePopUp {
    int getTimeOut();

    void setTimeOut(int milliseconds);

    void execute(CoreWindow wnd, String message);
}