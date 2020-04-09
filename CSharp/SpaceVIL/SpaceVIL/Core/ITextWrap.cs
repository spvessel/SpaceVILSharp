namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that defines text items that can wrap text relative to its width 
    /// and describes its attributes.
    /// </summary>
    public interface ITextWrap
    {
        /// <summary>
        /// Method for getting wrap status of the text item.
        /// </summary>
        /// <returns>True: if text is wrapped in width by its container. 
        /// False: if container does not wraps the contained text.</returns>
        bool IsWrapText();
    }
}