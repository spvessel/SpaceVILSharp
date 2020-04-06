
namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that describes the attributes of the position of a shape.
    /// <para/> This interface is part of SpaceVIL.Core.IBaseItem.
    /// </summary>
    public interface IPosition
    {
        /// <summary>
        /// Method for setting X coordinate of the left-top corner of a shape.
        /// </summary>
        /// <param name="x">X coordinate.</param>
        void SetX(int x);
        /// <summary>
        /// Method for getting X coordinate of the left-top corner of a shape.
        /// </summary>
        /// <returns>X coordinate.</returns>
        int GetX();
        /// <summary>
        /// Method for setting Y coordinate of the left-top corner of a shape.
        /// </summary>
        /// <param name="y">Y coordinate.</param>
        void SetY(int y);
        /// <summary>
        /// Method for getting Y coordinate of the left-top corner of a shape.
        /// </summary>
        /// <returns>Y coordinate.</returns>
        int GetY();
    }
}
