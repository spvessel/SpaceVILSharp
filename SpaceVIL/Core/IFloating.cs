using System;
namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that describes floating independent type of items.
    /// <para/> Notice: All floating items render above all others items.
    /// </summary>
    public interface IFloating
    {
        /// <summary>
        /// Method that describes how to display an item.
        /// </summary>
        void Show();

        /// <summary>
        /// Method that describes how to display an element, depending on the sender and mouse arguments.
        /// </summary>
        /// <param name="sender">Sender as SpaceVIL.Core.IItem.</param>
        /// <param name="args">Mouse arguments as SpaceVIL.Core.MouseArgs.</param>
        void Show(IItem sender, MouseArgs args);

        /// <summary>
        /// Method that describes how to hide an item.
        /// </summary>
        void Hide();

        /// <summary>
        /// Method that describes how to display an element, depending on mouse arguments.
        /// </summary>
        /// <param name="args">Mouse arguments as SpaceVIL.Core.MouseArgs.</param>
        void Hide(MouseArgs args);

        /// <summary>
        /// Method for getting boolean value of item's behavior when mouse click occurs outside the item.
        /// </summary>
        /// <returns>True: an item will become invisible if mouse click occurs outside the item.
        /// False: an item will stay visible if mouse click occurs outside the item.</returns>
        bool IsOutsideClickClosable();
        
        /// <summary>
        /// Method for setting boolean value of item's behavior when mouse click occurs outside the item.
        /// </summary>
        /// <param name="value">True: an item should become invisible if mouse click occurs outside the item.
        /// False: an item should stay visible if mouse click occurs outside the item.</param>
        void SetOutsideClickClosable(bool value);
    }
}