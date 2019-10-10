using System.Drawing;

namespace SpaceVIL.Core
{
    public interface IImageItem
    {
        Bitmap GetImage();
        int GetImageWidth();
        int GetImageHeight();
        bool IsColorOverlay();
        Color GetColorOverlay();
        float GetRotationAngle();
        RectangleBounds GetRectangleBounds();
    }
}
