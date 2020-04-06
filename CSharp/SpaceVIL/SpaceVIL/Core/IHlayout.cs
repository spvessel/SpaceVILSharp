namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that defines horizontal type of a container.
    /// </summary>
    public interface IHLayout
    {
        /// <summary>
        /// Method for describing how to update the size and position of children within a container.
        /// </summary>
        void UpdateLayout();
    }
}