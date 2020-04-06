using System;

namespace SpaceVIL.Core
{
    /// <summary>
    /// Scale is a class that describes the scaling factors along the X and Y axes.
    /// </summary>
    public class Scale
    {
        private float _x = 1f;
        private float _y = 1f;
        /// <summary>
        /// Default Scale constructor. All scaling factors are 1.
        /// </summary>
        public Scale() { }
        /// <summary>
        /// Constructs Scale with specified scaling factors.
        /// </summary>
        /// <param name="xScale">Scaling factor by X axis.</param>
        /// <param name="yScale">Scaling factor by Y axis.</param>
        public Scale(float xScale, float yScale)
        {
            SetScale(xScale, yScale);
        }
        /// <summary>
        /// Setting scaling factors.
        /// </summary>
        /// <param name="xScale">Scaling factor by X axis.</param>
        /// <param name="yScale">Scaling factor by Y axis.</param>
        public void SetScale(float xScale, float yScale)
        {
            if (xScale <= 0 || yScale <= 0)
                return;
            _x = xScale;
            _y = yScale;
        }
        /// <summary>
        /// Getting scaling factor by X axis.
        /// </summary>
        /// <returns>Scaling factor by X axis.</returns>
        public float GetXScale()
        {
            return _x;
        }
        /// <summary>
        /// Getting scaling factor by Y axis.
        /// </summary>
        /// <returns>Scaling factor by Y axis.</returns>
        public float GetYScale()
        {
            return _y;
        }

        public override string ToString()
        {
            return "XScale: " + _x.ToString(".0##") + " YScale: " + _y.ToString(".0##");
        }
    }
}