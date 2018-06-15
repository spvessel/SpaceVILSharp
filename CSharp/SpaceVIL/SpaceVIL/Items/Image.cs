using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    class Image : VisualItem, IImage
    {
        static int count = 0;

        public Image()
        {
            SetItemName("Image" + count);
            count++;
        }

        public override void InvokePoolEvents()
        {
        }

        //IImage implements
        public String GetImageUrl()
        {
            return null;
        }

        public float[] GetPixMapImage()
        {
            return null;
        }

        public void SetImageUrl(String url)
        {
            
        }
    }
}
