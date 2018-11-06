package com.spvessel;

interface InterfacePopUp {
    int getTimeOut();

    void setTimeOut(int milliseconds);

    void execute(WindowLayout wnd, String message);
}