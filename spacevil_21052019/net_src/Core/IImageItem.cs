using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    public interface IImageItem
    {
        byte[] GetPixMapImage();
        int GetImageWidth();
        int GetImageHeight();
        bool IsColorOverlay();
        Color GetColorOverlay();
        float GetRotationAngle();
        RectangleBounds GetRectangleBounds();
    }
}
