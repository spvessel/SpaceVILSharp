package com.spvessel.spacevil.Core;

/**
 * An interface that defines text items that can wrap text relative to its width 
 * and describes its attributes.
 */
public interface ITextWrap {
    /**
     * Method for getting wrap status of the text item.
     * @return True: if text is wrapped in width by its container. 
     * False: if container does not wraps the contained text.
     */
    public boolean isWrapText();
}
