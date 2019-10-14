using System.Collections.Generic;
using SpaceVIL.Core;

namespace SpaceVIL.Decorations
{
    public class SubtractFigure : ISubtractFigure, IEffect
    {
        public SubtractFigure(CustomFigure figure)
        {
            SetSubtractFigure(figure);
        }

        private CustomFigure _figure = null;
        private float _widthScale = 1.0f;
        private float _heightScale = 1.0f;
        private int _x = 0;
        private int _y = 0;
        private ItemAlignment _alignment;

        public ItemAlignment GetAlignment()
        {
            return _alignment;
        }

        public string GetEffectName()
        {
            return this.GetType().ToString();
        }

        public float GetHeightScale()
        {
            return _heightScale;
        }

        public CustomFigure GetSubtractFigure()
        {
            return _figure;
        }

        public float GetWidthScale()
        {
            return _widthScale;
        }

        public int GetXOffset()
        {
            return _x;
        }

        public int GetYOffset()
        {
            return _y;
        }

        public void SetAlignment(params ItemAlignment[] alignments)
        {
            ItemAlignment alignment = 0;
            for (int i = 0; i < alignments.Length; i++)
            {
                alignment |= alignments[i];
            }

            if (alignment.HasFlag(ItemAlignment.Left) && alignment.HasFlag(ItemAlignment.Right))
                alignment &= ~ItemAlignment.Right;
            if (alignment.HasFlag(ItemAlignment.Top) && alignment.HasFlag(ItemAlignment.Bottom))
                alignment &= ~ItemAlignment.Bottom;

            if (alignment.HasFlag(ItemAlignment.HCenter))
            {
                if (alignment.HasFlag(ItemAlignment.Left) || alignment.HasFlag(ItemAlignment.Right))
                    alignment &= ~(ItemAlignment.Left | ItemAlignment.Right);
            }
            if (alignment.HasFlag(ItemAlignment.VCenter))
            {
                if (alignment.HasFlag(ItemAlignment.Top) || alignment.HasFlag(ItemAlignment.Bottom))
                    alignment &= ~(ItemAlignment.Top | ItemAlignment.Bottom);
            }
            _alignment = alignment;
        }

        public void SetPositionOffset(int x, int y)
        {
            _x = x;
            _y = y;
        }

        public void SetSizeScale(float wScale, float hScale)
        {
            _widthScale = wScale;
            _heightScale = hScale;
        }

        public void SetSubtractFigure(CustomFigure figure)
        {
            _figure = figure;
        }
    }
}