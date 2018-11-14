using System;
using System.Collections.Generic;

namespace SpaceVIL.Decorations
{
    public class CustomFigure
    {
        private List<float[]> _figure;
        public List<float[]> GetFigure()
        {
            return _figure;
        }
        private bool _is_fixed = false;
        public bool IsFixed()
        {
            return _is_fixed;
        }
        public CustomFigure(bool isFixed, List<float[]> triangles)
        {
            _is_fixed = isFixed;
            _figure = new List<float[]>(triangles);
        }
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