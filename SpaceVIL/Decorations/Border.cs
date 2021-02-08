using System.Drawing;
using SpaceVIL.Core;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// Border is a class that decorates item's shape with border.
    /// </summary>
    public class Border : IBorder
    {
        /// <summary>
        /// Getting the effect name. 
        /// </summary>
        /// <returns>Returns name Shadow effect as System.String.</returns>
        public string GetEffectName()
        {
            return this.GetType().ToString();
        }

        private CornerRadius _radius;

        /// <summary>
        /// Getting border radiuses.
        /// </summary>
        /// <returns>Border radiuses as SpaceVIL.Decorations.CornerRadius.</returns>
        public CornerRadius GetRadius()
        {
            return _radius;
        }

        /// <summary>
        /// Setting radius of the border's corners.
        /// </summary>
        /// <param name="value">Radiuses of the border's corners as SpaceVIL.Decorations.CornerRadius.</param>
        public void SetRadius(CornerRadius value)
        {
            _radius = value;
        }

        private Color _color;

        /// <summary>
        /// Getting the border color oa an item's shape.
        /// </summary>
        /// <returns>Border color as System.Drawing.Color.</returns>
        public Color GetColor()
        {
            return _color;
        }

        /// <summary>
        /// Setting the border color of an item's shape.
        /// </summary>
        /// <param name="color">Border color as System.Drawing.Color.</param>
        public void SetColor(Color color)
        {
            _color = color;
        }

        private int _thickness;

        /// <summary>
        /// Getting border thickness of an item's shape.
        /// </summary>
        /// <returns>Border thickness.</returns>
        public int GetThickness()
        {
            return _thickness;
        }

        /// <summary>
        /// Setting border thickness of an item's shape.
        /// </summary>
        /// <param name="thickness">Border thickness.</param>
        public void SetThickness(int thickness)
        {
            _thickness = thickness;
        }

        private bool _isApplied = true;

        /// <summary>
        /// Getting the visibility status of a border.
        /// </summary>
        /// <returns>True: if border is visible. False: if border is invisible.</returns>
        public bool IsApplied()
        {
            return _isApplied;
        }

        /// <summary>
        /// Setting the visibility status of a border.
        /// </summary>
        /// <param name="value">True: if border should be visible. 
        /// False: if border should be invisible.</param>
        public void SetApplied(bool value)
        {
            _isApplied = value;
        }

        /// <summary>
        /// Default Border constructor.
        /// </summary>
        public Border()
        {
            SetColor(Color.Transparent);
            SetThickness(-1);
            SetRadius(new CornerRadius());
        }

        /// <summary>
        /// Constructs a Border  with specified color, radius and thickness.
        /// </summary>
        /// <param name="color">Border color as System.Drawing.Color.</param>
        /// <param name="radius">Radiuses of the border's corners as SpaceVIL.Decorations.CornerRadius.</param>
        /// <param name="thickness">Border thickness.</param>
        public Border(Color color, CornerRadius radius, int thickness)
        {
            SetColor(color);
            SetRadius(radius);
            SetThickness(thickness);
        }

        /// <summary>
        /// Clones current Border class instance.
        /// </summary>
        /// <returns>Copy of current Border.</returns>
        public Border Clone()
        {
            Border clone = new Border(
                Color.FromArgb(_color.ToArgb()),
                new CornerRadius(_radius),
                _thickness
            );

            return clone;
        }
    }
}
