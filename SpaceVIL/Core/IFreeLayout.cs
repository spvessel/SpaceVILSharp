namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that defines free type of a container.
    /// </summary>
    public interface IFreeLayout
    {
        /// <summary>
        /// Method for describing how to update the size and position of children within a container.
        /// </summary>
        void UpdateLayout();
    }
}
