package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.CoreWindow;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;

/**
 * The main interface of SpaceVIL environment. Contains all the necessary
 * methods for rendering objects and interacting with them.
 * <p>
 * Implementations: BaseItem, Prototype, Primitive.
 */
public interface IBaseItem extends IItem, ISize, IPosition, IEventUpdate, IBehavior {

    /**
     * Setting the window to which the item will belong.
     * 
     * @param handler Window as com.spvessel.spacevil.CoreWindow.
     */
    public void setHandler(CoreWindow handler);

    /**
     * Getting the window to which the item belong.
     * 
     * @return Window as com.spvessel.spacevil.CoreWindow.
     */
    public CoreWindow getHandler();

    /**
     * Setting the parent of the item.
     * 
     * @param parent Parent as com.spvessel.spacevil.Prototype (Prototype is
     *               container and can contains children).
     */
    public void setParent(Prototype parent);

    /**
     * Getting the parent of the item.
     * 
     * @return Parent as com.spvessel.spacevil.Prototype (Prototype is container and
     *         can contains children).
     */
    public Prototype getParent();

    /**
     * Setting the confines of the item relative to its parent's size and position.
     * <p>
     * Example: items can be partially (or completely) outside the container
     * (example: ListBox), in which case the part that is outside the container
     * should not be visible and should not interact with the user.
     */
    public void setConfines();

    /**
     * Setting the confines of the item relative to specified bounds.
     * <p>
     * Example: items can be partially (or completely) outside the container
     * (example: ListBox), in which case the part that is outside the container
     * should not be visible and should not interact with the user.
     * 
     * @param x0 Left X begin position.
     * @param x1 Right X end position.
     * @param y0 Top Y begin position.
     * @param y1 Bottom Y end position.
     */
    public void setConfines(int x0, int x1, int y0, int y1);

    /**
     * Setting the indents of an item to offset itself relative to its container.
     * 
     * @param margin Margin as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setMargin(Indents margin);

    /**
     * Setting the indents of an item to offset itself relative to its container.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setMargin(int left, int top, int right, int bottom);

    /**
     * Getting the indents of an item to offset itself relative to its container.
     * 
     * @return Margin as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getMargin();

    /**
     * Initializing children if this BaseItem is container
     * (com.spvessel.spacevil.Prototype).
     */
    public void initElements();

    /**
     * Setting a style that describes the appearance of an item.
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    public void setStyle(Style style);

    /**
     * Getting the core (only appearance properties without inner styles) style of
     * an item.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public Style getCoreStyle();

    /**
     * Getting the drawable (visibility) status of an item. This property used in
     * conjunction with the isVisible() property.
     * <p>
     * Explanation: an item can be visible and invisible, in some cases the item can
     * be located outside the container (example: com.spvessel.spacevil.ListBox),
     * and it must be invisible so as not to waste CPU / GPU resources, but in some
     * cases you must control the visibility of elements that are inside container
     * and should be invisible (example: com.spvessel.spacevil.TreeView).
     * 
     * @return True: if item is drawable (visible). False: if item is not drawable
     *         (invisible).
     */
    public boolean isDrawable();

    /**
     * Setting the drawable (visibility) status of an item. This property used in
     * conjunction with the isVisible() property.
     * <p>
     * Explanation: an item can be visible and invisible, in some cases the item can
     * be located outside the container (example: com.spvessel.spacevil.ListBox),
     * and it must be invisible so as not to waste CPU / GPU resources, but in some
     * cases you must control the visibility of elements that are inside container
     * and should be invisible (example: com.spvessel.spacevil.TreeView).
     * 
     * @param value True: if item should be drawable (visible). False: if item
     *              should not be drawable (invisible).
     */
    public void setDrawable(boolean value);

    /**
     * Getting the visibility status of an item. This property may used in
     * conjunction with the isDrawable() property.
     * 
     * @return True: if item is visible. False: if item is invisible.
     */
    public boolean isVisible();

    /**
     * Setting the visibility status of an item. This property may used in
     * conjunction with the isDrawable() property.
     * 
     * @param value True: if item should be visible. False: if item should be
     *              invisible.
     */
    public void setVisible(boolean value);

    /**
     * Method to describe disposing item's resources if the item was removed.
     */
    public void release();

    /**
     * Gettting access to manage visual effects of the item.
     * 
     * @return Implementation of an com.spvessel.spacevil.Core.IAppearanceExtension
     *         interface.
     */
    public IAppearanceExtension effects();
}