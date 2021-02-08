using System;
using System.Drawing;
using SpaceVIL.Core;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// Shadow is visual effect for applying to item's shape. Implements SpaceVIL.Core.IShadow and SpaceVIL.Core.IEffect.
    /// <para/> This visual effect drops shadow under item's shape.
    /// </summary>
    public sealed class Shadow : IShadow
    {
        /// <summary>
        /// Getting the effect name. 
        /// </summary>
        /// <returns>Returns name Shadow effect as System.String.</returns>
        public string GetEffectName()
        {
            return this.GetType().ToString();
        }

        private int _radius = 5;
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

        private Position _offset = new Position();

        /// <summary>
        /// Setting X shift of the shadow.
        /// </summary>
        /// <param name="value">Shift by X-axis.</param>
        public void SetXOffset(int value)
        {
            _offset.SetX(value);
        }

        /// <summary>
        /// Setting Y shift of the shadow.
        /// </summary>
        /// <param name="value">Shift by Y-axis.</param>
        public void SetYOffset(int value)
        {
            _offset.SetY(value);
        }

        /// <summary>
        /// Getting the offset of the shadow relative to the position of the item.
        /// </summary>
        /// <returns>Shadow offset as SpaceVIL.Core.Position.</returns>
        public Position GetOffset()
        {
            return _offset;
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

        private bool _isApplied;

        /// <summary>
        /// Setting drop shadow status.
        /// </summary>
        /// <param name="value">True: allow shadow dropping. False: not allow shadow dropping.</param>
        public void SetApplied(bool value)
        {
            _isApplied = value;
        }

        /// <summary>
        /// Getting shadow drop status.
        /// </summary>
        /// <returns>True: allow shadow dropping. False: not allow shadow dropping.</returns>
        public bool IsApplied()
        {
            return _isApplied;
        }

        private Core.Size _extension = new Core.Size();

        /// <summary>
        /// Getting the values of shadow extensions in pixels.
        /// </summary>
        /// <returns>The values of shadow extensions as SpaceVIL.Core.Size</returns>
        public Core.Size GetExtension()
        {
            return _extension;
        }

        /// <summary>
        /// Default Shadow class constructor. Allow shadow dropping.
        /// </summary>
        public Shadow()
        {
            _isApplied = true;
        }

        /// <summary>
        /// Shadow class constructor with specified blur radius. Allow shadow dropping.
        /// </summary>
        /// <param name="radius">A blur radius of the shadow.</param>
        public Shadow(int radius) : this()
        {
            _radius = radius;
        }

        /// <summary>
        /// Shadow class constructor with specified blur radius, shadow color. Allow shadow dropping.
        /// </summary>
        /// <param name="radius">A blur radius of the shadow.</param>
        /// <param name="color">A shadow color as System.Drawing.Color.</param>
        public Shadow(int radius, Color color) : this()
        {
            _radius = radius;
            _color = Color.FromArgb(color.A, color.R, color.G, color.B);
        }

        /// <summary>
        /// Shadow class constructor with specified blur radius, axis shifts, shadow color. Allow shadow dropping.
        /// </summary>
        /// <param name="radius">A blur radius of the shadow.</param>
        /// <param name="offset">Shift of the shadow.</param>
        /// <param name="color">A shadow color as System.Drawing.Color.</param>
        public Shadow(int radius, Position offset, Color color) : this()
        {
            _radius = radius;
            _offset.SetPosition(offset.GetX(), offset.GetY());
            _color = Color.FromArgb(color.A, color.R, color.G, color.B);
        }

        /// <summary>
        /// Shadow class constructor with specified blur radius, size extensions, shadow color. Allow shadow dropping.
        /// </summary>
        /// <param name="radius">A blur radius of the shadow.</param>
        /// <param name="extension">>Size extension of the shadow.</param>
        /// <param name="color">A shadow color as System.Drawing.Color.</param>
        public Shadow(int radius, Core.Size extension, Color color) : this()
        {
            _radius = radius;
            _extension.SetSize(extension.GetWidth(), extension.GetHeight());
            _color = Color.FromArgb(color.A, color.R, color.G, color.B);
        }

        /// <summary>
        /// Shadow class constructor with specified blur radius, axis shifts, size extensions and shadow color. Allow shadow dropping.
        /// </summary>
        /// <param name="radius">A blur radius of the shadow.</param>
        /// <param name="offset">Shift of the shadow.</param>
        /// <param name="extension">Size extension of the shadow.</param>
        /// <param name="color">A shadow color as System.Drawing.Color.</param>
        public Shadow(int radius, Position offset, Core.Size extension, Color color) : this()
        {
            _radius = radius;
            _offset.SetPosition(offset.GetX(), offset.GetY());
            _extension.SetSize(extension.GetWidth(), extension.GetHeight());
            _color = Color.FromArgb(color.A, color.R, color.G, color.B);
        }

        /// <summary>
        /// Clones current Shadow class instance.
        /// </summary>
        /// <returns>Copy of current Shadow.</returns>
        public Shadow Clone()
        {
            Shadow clone = new Shadow(
                GetRadius(),
                new Position(GetOffset().GetX(), GetOffset().GetY()),
                new Core.Size(GetExtension().GetWidth(), GetExtension().GetHeight()),
                Color.FromArgb(_color.ToArgb())
            );
            clone.SetApplied(IsApplied());

            return clone;
        }
    }
}