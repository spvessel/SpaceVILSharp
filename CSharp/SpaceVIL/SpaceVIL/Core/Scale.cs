using System;

namespace SpaceVIL.Core
{
    public class Scale
    {
        private float _x = 1f;
        private float _y = 1f;

        public Scale() { }
        public Scale(float xScale, float yScale)
        {
            SetScale(xScale, yScale);
        }
        public void SetScale(float xScale, float yScale)
        {
            if (xScale <= 0 || yScale <= 0)
                return;
            _x = xScale;
            _y = yScale;
        }

        public float GetX()
        {
            return _x;
        }

        public float GetY()
        {
            return _y;
        }

        public override string ToString()
        {
            return "XScale: " + _x.ToString(".0##") + " YScale: " + _y.ToString(".0##");
        }
    }
}