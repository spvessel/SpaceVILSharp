package com.spvessel.spacevil.Core;

/**
 * An interface that describes floating independent type of items.
 * <p> Notice: All floating items render above all others items.
 */
public interface IFloating {
    /**
     * Method that describes how to display an item.
     */
    public void show();

    /**
     * Method that describes how to display an element, depending on the sender and mouse arguments.
     * @param sender Sender as com.spvessel.spacevil.Core.IItem.
     * @param args Mouse arguments as com.spvessel.spacevil.Core.MouseArgs.
     */
    public void show(IItem sender, MouseArgs args);

    /**
     * Method that describes how to hide an item.
     */
    public void hide();

    /**
     * Method that describes how to display an element, depending on mouse arguments.
     * @param args Mouse arguments as com.spvessel.spacevil.Core.MouseArgs.
     */
    public void hide(MouseArgs args);

    /**
     * Method for getting boolean value of item's behavior when mouse click occurs outside the item.
     * @return True: an item will become invisible if mouse click occurs outside the item.
     * False: an item will stay visible if mouse click occurs outside the item.
     */
    public boolean isOutsideClickClosable();

    /**
     * Method for setting boolean value of item's behavior when mouse click occurs outside the item.
     * @param value True: an item should become invisible if mouse click occurs outside the item.
     * False: an item should stay visible if mouse click occurs outside the item.
     */
    public void setOutsideClickClosable(boolean value);
}