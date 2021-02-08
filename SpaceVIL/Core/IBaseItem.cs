using SpaceVIL.Decorations;

namespace SpaceVIL.Core
{
    /// <summary>
    /// The main interface of SpaceVIL environment. Contains all the necessary methods for rendering objects and interacting with them.
    /// <para/> Implementations: BaseItem, Prototype, Primitive.
    /// </summary>
    public interface IBaseItem : IItem, ISize, IPosition, IEventUpdate, IBehavior
    {
        /// <summary>
        /// Setting the window to which the item will belong.
        /// </summary>
        /// <param name="handler">Window as SpaceVIL.CoreWindow.</param>
        void SetHandler(CoreWindow handler);

        /// <summary>
        /// Getting the window to which the item will belong.
        /// </summary>
        /// <returns>Window as SpaceVIL.CoreWindow.</returns>
        CoreWindow GetHandler();

        /// <summary>
        /// Setting the parent of the item.
        /// </summary>
        /// <param name="parent">Parent as SpaceVIL.Prototype 
        /// (Prototype is container and can contains children).</param>
        void SetParent(Prototype parent);

        /// <summary>
        /// Getting the parent of the item.
        /// </summary>
        /// <returns>Parent as SpaceVIL.Prototype 
        /// (Prototype is container and can contains children).</returns>
        Prototype GetParent();

        /// <summary>
        /// Setting the confines of the item relative to its parent's size and position.
        /// <para/> Example: items can be partially (or completely) outside the container (example: ListBox), 
        /// in which case the part that is outside the container should not be visible and should not interact with the user.
        /// </summary>
        void SetConfines();

        /// <summary>
        /// Setting the confines of the item relative to specified bounds.
        /// <para/> Example: items can be partially (or completely) outside the container (example: ListBox), 
        /// in which case the part that is outside the container should not be visible and should not interact with the user.
        /// </summary>
        /// <param name="x0">Left X begin position.</param>
        /// <param name="x1">Right X end position.</param>
        /// <param name="y0">Top Y begin position.</param>
        /// <param name="y1">Bottom Y end position.</param>
        void SetConfines(int x0, int x1, int y0, int y1);

        /// <summary>
        /// Setting the indents of an item to offset itself relative to its container.
        /// </summary>
        /// <param name="margin">Margin as SpaceVIL.Decorations.Indents.</param>
        void SetMargin(Indents margin);

        /// <summary>
        /// Setting the indents of an item to offset itself relative to its container.
        /// </summary>
        /// <param name="left">Indent on the left.</param>
        /// <param name="top">Indent on the top.</param>
        /// <param name="right">Indent on the right.</param>
        /// <param name="bottom">Indent on the bottom.</param>
        void SetMargin(int left = 0, int top = 0, int right = 0, int bottom = 0);

        /// <summary>
        /// Getting the indents of an item to offset itself relative to its container.
        /// </summary>
        /// <returns>Margin as SpaceVIL.Decorations.Indents.</returns>
        Indents GetMargin();

        /// <summary>
        /// Initializing children if this IBaseItem is container (SpaceVIL.Prototype).
        /// </summary>
        void InitElements();

        /// <summary>
        /// Setting a style that describes the appearance of an item.
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        void SetStyle(Style style);

        /// <summary>
        /// Getting the core (only appearance properties without inner styles) style of an item.
        /// </summary>
        /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
        Style GetCoreStyle();

        /// <summary>
        /// Getting the drawable (visibility) status of an item. This property used in 
        /// conjunction with the IsVisible() property.
        /// <para/> Explanation: an item can be visible and invisible, in some cases 
        /// the item can be located outside the container (example: SpaceVIL.ListBox), 
        /// and it must be invisible so as not to waste CPU / GPU resources, but in some 
        /// cases you must control the visibility of elements that are inside container 
        /// and should be invisible (example: SpaceVIL.TreeView).
        /// </summary>
        /// <returns>True: if item is drawable (visible). False: if item is not drawable (invisible).</returns>
        bool IsDrawable();

        /// <summary>
        /// Setting the drawable (visibility) status of an item. This property used in 
        /// conjunction with the IsVisible() property.
        /// <para/> Explanation: an item can be visible and invisible, in some cases 
        /// the item can be located outside the container (example: SpaceVIL.ListBox), 
        /// and it must be invisible so as not to waste CPU / GPU resources, but in some 
        /// cases you must control the visibility of elements that are inside container 
        /// and should be invisible (example: SpaceVIL.TreeView).
        /// </summary>
        /// <param name="value">True: if item should be drawable (visible). 
        /// False: if item should not be drawable (invisible).</param>
        void SetDrawable(bool value);

        /// <summary>
        /// Getting the visibility status of an item. This property may used in 
        /// conjunction with the IsDrawable() property.
        /// </summary>
        /// <returns>True: if item is visible. False: if item is invisible.</returns>
        bool IsVisible();

        /// <summary>
        /// Setting the visibility status of an item. This property may used in 
        /// conjunction with the IsDrawable() property.
        /// </summary>
        /// <param name="value">True: if item should be visible. 
        /// False: if item should be invisible.</param>
        void SetVisible(bool value);

        /// <summary>
        /// Method to describe disposing item's resources if the item was removed.
        /// </summary>
        void Release();

        /// <summary>
        /// Gettting access to manage visual effects of the item.
        /// </summary>
        /// <returns>Implementation of an SpaceVIL.Core.IAppearanceExtension interface.</returns>
        IAppearanceExtension Effects();
    }
}
