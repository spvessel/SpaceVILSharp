using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    class PixMapData
    {
        private List<float> pixels;
        private List<float> colors;
        private float alpha;

        public PixMapData(List<float> pixels, List<float> colors, float alpha)
        {
            this.pixels = pixels;
            this.colors = colors;
            this.alpha = alpha;
        }

        public List<float> GetPixels()
        {
            return pixels;
        }

        public List<float> GetColors()
        {
            return colors;
        }

        public float GetAlpha()
        {
            return alpha;
        }
    }
}
