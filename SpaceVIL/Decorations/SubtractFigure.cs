using System.Collections.Generic;
using SpaceVIL.Core;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// SubtractFigure is visual effect for applying to item's shape. Implements SpaceVIL.Core.ISubtractFigure and SpaceVIL.Core.IEffect.
    /// <para/> This visual effect cuts specified shape from original item's shape.
    /// </summary>
    public class SubtractFigure : ISubtractFigure
    {
        /// <summary>
        /// Constructs subtract effect with specified shape.
        /// </summary>
        /// <param name="figure">Figure for subtraction as SpaceVIL.Decoratons.Figure.</param>
        public SubtractFigure(Figure figure)
        {
            SetSubtractFigure(figure);
        }

        private Figure _figure = null;

        /// <summary>
        /// Setting shape for subtraction.
        /// </summary>
        /// <param name="figure">Figure for subtraction as SpaceVIL.Decoratons.Figure.</param>
        public void SetSubtractFigure(Figure figure)
        {
            _figure = figure;
        }

        /// <summary>
        /// Getting the current figure for subtraction.
        /// </summary>
        /// <returns>Figure for subtraction as SpaceVIL.Decoratons.Figure.</returns>
        public Figure GetSubtractFigure()
        {
            return _figure;
        }

        /// <summary>
        /// Getting the effect name. 
        /// </summary>
        /// <returns>Returns name SubtractEffect as System.String.</returns>
        public string GetEffectName()
        {
            return this.GetType().ToString();
        }

        private int _x = 0;
        private int _y = 0;

        /// <summary>
        /// Setting shape's shift by X, Y axis.
        /// </summary>
        /// <param name="x">X axis shift.</param>
        /// <param name="y">Y axis shift.</param>
        public void SetPositionOffset(int x, int y)
        {
            _x = x;
            _y = y;
        }

        private float _widthScale = 1.0f;
        private float _heightScale = 1.0f;

        /// <summary>
        /// Setting shape's scaling factors for width and height.
        /// </summary>
        /// <param name="wScale">Scaling factor for width.</param>
        /// <param name="hScale">Scaling factor for height.</param>
        public void SetSizeScale(float wScale, float hScale)
        {
            _widthScale = wScale;
            _heightScale = hScale;
        }

        /// <summary>
        /// Getting shape's shift by X-axis.
        /// </summary>
        /// <returns>X axis shift.</returns>
        public int GetXOffset()
        {
            return _x;
        }

        /// <summary>
        /// Getting shape's shift by Y-axis.
        /// </summary>
        /// <returns>Y axis shift.</returns>
        public int GetYOffset()
        {
            return _y;
        }

        /// <summary>
        /// Getting width scaling.
        /// </summary>
        /// <returns>Width scaling.</returns>
        public float GetWidthScale()
        {
            return _widthScale;
        }

        /// <summary>
        /// Getting height scaling.
        /// </summary>
        /// <returns>Height scaling.</returns>
        public float GetHeightScale()
        {
            return _heightScale;
        }

        private ItemAlignment _alignment;

        /// <summary>
        /// Getting shape's allignment within the item.
        /// </summary>
        /// <returns>Alignment as SpaceVIL.Core.ItemAlignment.</returns>
        public ItemAlignment GetAlignment()
        {
            return _alignment;
        }

        /// <summary>
        /// Setting shape's allignment within the item.
        /// </summary>
        /// <param name="alignments">Alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetAlignment(params ItemAlignment[] alignments)
        {
            ItemAlignment alignment = 0;
            for (int i = 0; i < alignments.Length; i++)
            {
                alignment |= alignments[i];
            }

            if (alignment.HasFlag(ItemAlignment.Left) && alignment.HasFlag(ItemAlignment.Right))
            {
                alignment &= ~ItemAlignment.Right;
            }
            if (alignment.HasFlag(ItemAlignment.Top) && alignment.HasFlag(ItemAlignment.Bottom))
            {
                alignment &= ~ItemAlignment.Bottom;
            }

            if (alignment.HasFlag(ItemAlignment.HCenter))
            {
                if (alignment.HasFlag(ItemAlignment.Left) || alignment.HasFlag(ItemAlignment.Right))
                {
                    alignment &= ~(ItemAlignment.Left | ItemAlignment.Right);
                }
            }
            if (alignment.HasFlag(ItemAlignment.VCenter))
            {
                if (alignment.HasFlag(ItemAlignment.Top) || alignment.HasFlag(ItemAlignment.Bottom))
                {
                    alignment &= ~(ItemAlignment.Top | ItemAlignment.Bottom);
                }
            }
            _alignment = alignment;
        }

        private bool _isApplied = true;

        /// <summary>
        /// Returns True if the effect is applied, false otherwise.
        /// </summary>
        /// <returns>True: if effect is applied. False: if shadow is not applied.</returns>
        public bool IsApplied()
        {
            return _isApplied;
        }

        /// <summary>
        /// Determines whether the effect should be applied or not.
        /// </summary>
        /// <param name="value">True: if the effect is to be applied. 
        /// False: if effect is not to be applied.</param>
        public void SetApplied(bool value)
        {
            _isApplied = value;
        }
    }
}