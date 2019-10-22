using System.Drawing;

namespace SpaceVIL.Core
{
    public interface IImageItem
    {
        Bitmap GetImage();
        int GetImageWidth();
        int GetImageHeight();
        Color GetColorOverlay();
        bool IsColorOverlay();
        float GetRotationAngle();
        RectangleBounds GetRectangleBounds();
    }
}
