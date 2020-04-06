namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that defines vertical type of a container.
    /// </summary>
    public interface IVLayout
    {
        /// <summary>
        /// Method for describing how to update the size and position of children within a container.
        /// </summary>
        void UpdateLayout();
    }
}