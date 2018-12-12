using System;
using System.Collections.Generic;

namespace SpaceVIL.Decorations
{
    public class CustomFigure
    {
        private List<float[]> _figure;

        /// <returns> CustomFigure points list </returns>
        public List<float[]> GetFigure()
        {
            return _figure;
        }
        private bool _is_fixed = false;

        /// <summary>
        /// Is CustomFigure fixed
        /// </summary>
        public bool IsFixed()
        {
            return _is_fixed;
        }

        /// <summary>
        /// Constructs a CustomFigure
        /// </summary>
        /// <param name="isFixed"> is CustomFigure fixed </param>
        /// <param name="triangles"> Triangles list of the CustomFigure's shape </param>
        public CustomFigure(bool isFixed, List<float[]> triangles)
        {
            _is_fixed = isFixed;
            _figure = new List<float[]>(triangles);
        }

        /// <returns> new CustomFugure points list changed according to the new position (x, y) </returns>
        public List<float[]> UpdatePosition(int _x, int _y)
        {
            List<float[]> result = new List<float[]>();
            foreach (var item in _figure)
            {
                result.Add( new float[] {item[0] + _x, item[1] + _y, 0.0f });
            }
            return result;
        }
    }
}