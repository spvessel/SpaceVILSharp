using System;
using System.Drawing;

namespace SpaceVIL
{
    public sealed class Shadow
    {
        private int _radius = 0;
        private int _maxAvailableRadius = 10;

        public void SetRadius(int value)
        {
            if (value < 0 || value > _maxAvailableRadius)
                return;
            _radius = value;
        }
        public int GetRadius()
        {
            return _radius;
        }
        
        private int _x = 0;
        public void SetXOffset(int value)
        {
            _x = value;
        }
        public int GetXOffset()
        {
            return _x;
        }
        
        private int _y = 0;
        public void SetYOffset(int value)
        {
            _y = value;
        }
        public int GetYOffset()
        {
            return _y;
        }
        
        private Color _color = Color.Black;
        public void SetColor(Color color)
        {
            _color = Color.FromArgb(color.A, color.R, color.G, color.B);
        }
        public void SetColor(int r, int g, int b)
        {
            _color = GraphicsMathService.ColorTransform(r, g, b);
        }
        public void SetColor(int r, int g, int b, int a)
        {
            _color = GraphicsMathService.ColorTransform(r, g, b, a);
        }
        public void SetColor(float r, float g, float b)
        {
            _color = GraphicsMathService.ColorTransform(r, g, b);
        }
        public void SetColor(float r, float g, float b, float a)
        {
            _color = GraphicsMathService.ColorTransform(r, g, b, a);
        }
        public Color GetColor()
        {
            return Color.FromArgb(_color.A, _color.R, _color.G, _color.B);
        }

        private bool _isDrop;
        public void SetDrop(bool value)
        {
            _isDrop = value;
        }
        public bool IsDrop()
        {
            return _isDrop;
        }

        public Shadow()
        {
            _isDrop = true;
        }
        public Shadow(int radius, int x, int y, Color color) : this()
        {
            _radius = radius;
            _x = x;
            _y = y;
            _color = Color.FromArgb(color.A, color.R, color.G, color.B);
        }
    }
}