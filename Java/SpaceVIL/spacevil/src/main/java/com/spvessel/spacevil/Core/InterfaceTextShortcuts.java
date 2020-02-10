package com.spvessel.spacevil.Core;

public interface InterfaceTextShortcuts {
    public void pasteText(String pasteStr);

    public String getSelectedText();

    public String cutText();

    public void undo();

    public void redo();

    public void selectAll();
}