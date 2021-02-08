using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    internal class PixMapData
    {
        private List<float> pixels;
        private List<float> colors;
        private List<int> letEndPos;
        private float alpha;

        public PixMapData(List<float> pixels, List<float> colors, List<int> letEndPos) //float alpha)
        {
            this.pixels = pixels;
            this.colors = colors;
            this.letEndPos = letEndPos;
            if (letEndPos.Count > 0)
                alpha = letEndPos[letEndPos.Count - 1];
            else alpha = 0;
        }

        public List<float> GetPixels()
        {
            return pixels;
        }

        public List<float> GetColors()
        {
            return colors;
        }

        public List<int> GetEndPositions()
        {
            return letEndPos;
        }

        public float GetAlpha() {
            return alpha;
        }
    }
}
