using System.Drawing;

namespace SpaceVIL.Core
{
    /// <summary>
    /// IShadow is an interface for creating classes that decorates item's shape with shadow.
    /// </summary>
    public interface IShadow
    {
        /// <summary>
        /// Getting the shadow blur raduis.
        /// </summary>
        /// <returns>The blur radius of the shadow. Min value: 0. Max value: 10.</returns>
        int GetRadius();

        /// <summary>
        /// Getting the offset of the shadow relative to the position of the item.
        /// </summary>
        /// <returns>Shadow offset as SpaceVIL.Core.Position.</returns>
        Position GetOffset();

        /// <summary>
        /// Getting shadow color.
        /// </summary>
        /// <returns>Returns the shadow color as System.Drawing.Color.</returns>
        Color GetColor();

        /// <summary>
        /// Getting shadow drop flag.
        /// </summary>
        /// <returns>True: allow shadow dropping. False: not allow shadow dropping.</returns>
        bool IsDrop();

        /// <summary>
        /// Setting shadow drop flag.
        /// </summary>
        /// <returns>True: allow shadow dropping. False: not allow shadow dropping.</returns>
        void SetDrop(bool value);

        /// <summary>
        /// Getting the values of shadow extensions in pixels.
        /// </summary>
        /// <returns>The values of shadow extensions as SpaceVIL.Core.Size</returns>
        Size GetExtension();
    }
}