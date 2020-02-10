package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.CoreWindow;

public interface InterfacePopUp {
    public int getTimeOut();

    public void setTimeOut(int milliseconds);

    public void execute(CoreWindow wnd, String message);
}