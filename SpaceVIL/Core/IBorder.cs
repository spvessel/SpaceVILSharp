using System.Drawing;
using SpaceVIL.Decorations;

namespace SpaceVIL.Core
{
    /// <summary>
    /// IBorder is an interface for creating classes that decorates item's shape with border.
    /// </summary>
    public interface IBorder : IEffect
    {
        /// <summary>
        /// Getting border radiuses.
        /// </summary>
        /// <returns>Border radiuses as SpaceVIL.Decorations.CornerRadius.</returns>
        CornerRadius GetRadius();

        /// <summary>
        /// Setting radius of the border's corners.
        /// </summary>
        /// <param name="value">Radiuses of the border's corners as SpaceVIL.Decorations.CornerRadius.</param>
        void SetRadius(CornerRadius value);

        /// <summary>
        /// Getting the border color oa an item's shape.
        /// </summary>
        /// <returns>Border color as System.Drawing.Color.</returns>
        Color GetColor();

        /// <summary>
        /// Setting the border color of an item's shape.
        /// </summary>
        /// <param name="color">Border color as System.Drawing.Color.</param>
        void SetColor(Color color);

        /// <summary>
        /// Getting border thickness of an item's shape.
        /// </summary>
        /// <returns>Border thickness.</returns>
        int GetThickness();

        /// <summary>
        /// Setting border thickness of an item's shape.
        /// </summary>
        /// <param name="thickness">Border thickness.</param>
        void SetThickness(int thickness);
    }
}