using System.Drawing;

namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that discribes such type of items that are images (for texturing).
    /// </summary>
    public interface IImageItem
    {
        /// <summary>
        /// Method for describing how to get a bitmap in the form of System.Drawing.Bitmap.
        /// </summary>
        /// <returns>Image as System.Drawing.Bitmap</returns>
        Bitmap GetImage();
        /// <summary>
        /// Method for describing how to get an image width.
        /// </summary>
        /// <returns>Image width.</returns>
        int GetImageWidth();
        /// <summary>
        /// Method for describing how to get an image height.
        /// </summary>
        /// <returns>Image height.</returns>
        int GetImageHeight();
        /// <summary>
        /// Method for getting color overlay (useful in images that have alpha channel).
        /// </summary>
        /// <returns>Color overlay as System.Drawing.Color.</returns>
        Color GetColorOverlay();
        /// <summary>
        /// Method for getting color overlay status.
        /// </summary>
        /// <returns>True: if color overlay is using. False: if color overlay is not using.</returns>
        bool IsColorOverlay();
        /// <summary>
        /// Method for getting rotation angle of an image.
        /// </summary>
        /// <returns>Rotation angle.</returns>
        float GetRotationAngle();
        /// <summary>
        /// Method for getting bounds for an image (for example: to keep aspect ratio).
        /// </summary>
        /// <returns>Bounds as SpaceVIL.Core.RectangleBounds.</returns>
        Area GetAreaBounds();
    }
}
