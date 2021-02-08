namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that describes text image.
    /// </summary>
    public interface ITextImage
    {
        /// <summary>
        /// Method for getting bitmap image as byte array.
        /// </summary>
        /// <returns>Bitmap image as byte array.</returns>
        byte[] GetBytes();

        /// <summary>
        /// Method for getting empty status.
        /// </summary>
        /// <returns>True: implementation of the interface does not contain an image.
        /// False: implementation of the interface contains image.</returns>
        bool IsEmpty();

        /// <summary>
        /// Method for getting width of the image.
        /// </summary>
        /// <returns>Image width.</returns>
        int GetWidth();

        /// <summary>
        /// Method for getting height of the image.
        /// </summary>
        /// <returns>Image height.</returns>
        int GetHeight();

        /// <summary>
        /// Method for getting image offset by X axis.
        /// </summary>
        /// <returns>X axis offset.</returns>
        int GetXOffset();
        
        /// <summary>
        /// Method for getting image offset by Y axis.
        /// </summary>
        /// <returns>Y axis offset.</returns>
        int GetYOffset();
    }
}