using System;
using System.Drawing;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// Class that is the shadow of an item.
    /// </summary>
    public sealed class Shadow
    {
        private int _radius = 0;
        private int _maxAvailableRadius = 10;

        /// <summary>
        /// Setting the specified blur radius of the shadow.
        /// <para/>Default: 0.
        /// </summary>
        /// <param name="value">Blur radius of the shadow. Min value: 0. Max value: 10.</param>
        public void SetRadius(int value)
        {
            if (value < 0 || value > _maxAvailableRadius)
            {
                return;
            }
            _radius = value;
        }

        /// <summary>
        /// Getting the shadow blur raduis.
        /// </summary>
        /// <returns>The blur radius of the shadow. Min value: 0. Max value: 10.</returns>
        public int GetRadius()
        {
            return _radius;
        }

        private int _x = 0;

        /// <summary>
        /// Setting X shift of the shadow.
        /// </summary>
        /// <param name="value">Shift by X-axis.</param>
        public void SetXOffset(int value)
        {
            _x = value;
        }

        /// <summary>
        /// Getting X shift of the shadow.
        /// </summary>
        /// <returns>Shift by X-axis.</returns>
        public int GetXOffset()
        {
            return _x;
        }

        private int _y = 0;

        /// <summary>
        /// Setting Y shift of the shadow.
        /// </summary>
        /// <param name="value">Shift by Y-axis.</param>
        public void SetYOffset(int value)
        {
            _y = value;
        }
        
        /// <summary>
        /// Setting Y shift of the shadow.
        /// </summary>
        /// <returns>Shift by Y-axis.</returns>
        public int GetYOffset()
        {
            return _y;
        }

        private Color _color = Color.Black;

        /// <summary>
        /// Setting shadow color.
        /// </summary>
        /// <param name="color">Shadow color as System.Drawing.Color.</param>
        public void SetColor(Color color)
        {
            _color = Color.FromArgb(color.A, color.R, color.G, color.B);
        }

        /// <summary>
        /// Setting shadow color in RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public void SetColor(int r, int g, int b)
        {
            _color = GraphicsMathService.ColorTransform(r, g, b);
        }

        /// <summary>
        /// Setting shadow color in byte RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        /// <param name="a">Alpha color component. Range: (0 - 255)</param>
        public void SetColor(int r, int g, int b, int a)
        {
            _color = GraphicsMathService.ColorTransform(r, g, b, a);
        }

        /// <summary>
        /// Setting shadow color in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public void SetColor(float r, float g, float b)
        {
            _color = GraphicsMathService.ColorTransform(r, g, b);
        }

        /// <summary>
        /// Setting shadow color in float RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha color component. Range: (0.0f - 1.0f)</param>
        public void SetColor(float r, float g, float b, float a)
        {
            _color = GraphicsMathService.ColorTransform(r, g, b, a);
        }

        /// <summary>
        /// Getting shadow color.
        /// </summary>
        /// <returns>Returns the shadow color as System.Drawing.Color.</returns>
        public Color GetColor()
        {
            return Color.FromArgb(_color.A, _color.R, _color.G, _color.B);
        }

        private bool _isDrop;

        /// <summary>
        /// Setting drop shadow flag.
        /// </summary>
        /// <param name="value">True: allow shadow dropping. False: not allow shadow dropping.</param>
        public void SetDrop(bool value)
        {
            _isDrop = value;
        }

        /// <summary>
        /// Getting shadow drop flag.
        /// </summary>
        /// <returns>True: allow shadow dropping. False: not allow shadow dropping.</returns>
        public bool IsDrop()
        {
            return _isDrop;
        }

        /// <summary>
        /// Default Shadow class constructor. Allow shadow dropping.
        /// </summary>
        public Shadow()
        {
            _isDrop = true;
        }
        
        /// <summary>
        /// Shadow class constructor with specified blur radius, axis shifts, shadow color. Allow shadow dropping.
        /// </summary>
        /// <param name="radius">A blur radius of the shadow.</param>
        /// <param name="x">X shift of the shadow.</param>
        /// <param name="y">Y shift of the shadow.</param>
        /// <param name="color">A shadow color as System.Drawing.Color.</param>
        public Shadow(int radius, int x, int y, Color color) : this()
        {
            _radius = radius;
            _x = x;
            _y = y;
            _color = Color.FromArgb(color.A, color.R, color.G, color.B);
        }
    }
}