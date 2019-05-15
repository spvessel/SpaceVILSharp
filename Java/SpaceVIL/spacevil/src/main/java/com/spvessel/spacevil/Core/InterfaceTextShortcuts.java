package com.spvessel.spacevil.Core;

public interface InterfaceTextShortcuts {
    void pasteText(String pasteStr);

    String getSelectedText();

    String cutText();

    void undo();

    void redo();

    void selectAll();
}