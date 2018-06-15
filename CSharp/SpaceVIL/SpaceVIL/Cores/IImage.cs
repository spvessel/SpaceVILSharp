using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    interface IImage
    {
        String GetImageUrl();
        void SetImageUrl(String url);
        float[] GetPixMapImage();
    }
}
