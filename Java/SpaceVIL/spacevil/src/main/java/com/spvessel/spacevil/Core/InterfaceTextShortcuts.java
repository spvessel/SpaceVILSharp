package com.spvessel.spacevil.Core;

/**
 * An interface that defines items that can receive common keyboard shortcuts 
 * (copy, paste, cut and etc.) and describes its attributes.
 */
public interface InterfaceTextShortcuts {
    /**
     * Method for pasting text.
     * @param pasteStr Text for pasting.
     */
    public void pasteText(String pasteStr);

    /**
     * Method for getting selected text.
     * @return Selected text.
     */
    public String getSelectedText();

    /**
     * Method for cutting selected text.
     * @return Cutted text.
     */
    public String cutText();

    /**
     * Method for undo last change.
     */
    public void undo();

    /**
     * Method for redo last undo action.
     */
    public void redo();

    /**
     * Method for selecting all text in the item.
     */
    public void selectAll();
}