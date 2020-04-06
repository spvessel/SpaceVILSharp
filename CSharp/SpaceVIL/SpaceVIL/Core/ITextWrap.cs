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
        /// <returns></returns>
        bool IsWrapText();
    }
}