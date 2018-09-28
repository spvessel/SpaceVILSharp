package com.spvessel.Cores;

import com.spvessel.Windows.*;

interface InterfacePopUp {
    int getTimeOut();

    void setTimeOut(int milliseconds);

    void execute(WindowLayout wnd, String message);
}