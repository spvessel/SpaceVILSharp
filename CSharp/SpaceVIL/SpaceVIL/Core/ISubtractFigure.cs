using SpaceVIL.Decorations;

namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that describes visual effect which cuts specified shape from original item's shape.
    /// </summary>
    public interface ISubtractFigure
    {
        /// <summary>
        /// Method for setting shape for subtraction.
        /// </summary>
        /// <param name="figure">Figure for subtraction as SpaceVIL.Decoratons.Figure.</param>
        void SetSubtractFigure(Figure figure);

        /// <summary>
        /// Method for getting the current figure for subtraction.
        /// </summary>
        /// <returns>Figure for subtraction as SpaceVIL.Decoratons.Figure.</returns>
        Figure GetSubtractFigure();

        /// <summary>
        /// Method for setting shape's shift by X, Y axis.
        /// </summary>
        /// <param name="x">X axis shift.</param>
        /// <param name="y">Y axis shift.</param>
        void SetPositionOffset(int x, int y);

        /// <summary>
        /// Method for setting shape's scaling factors for width and height.
        /// </summary>
        /// <param name="wScale">Scaling factor for width.</param>
        /// <param name="hScale">Scaling factor for height.</param>
        void SetSizeScale(float wScale, float hScale);

        /// <summary>
        /// Method for getting shape's shift by X-axis.
        /// </summary>
        /// <returns>X axis shift.</returns>
        int GetXOffset();

        /// <summary>
        /// Method for getting shape's shift by Y-axis.
        /// </summary>
        /// <returns>Y axis shift.</returns>
        int GetYOffset();

        /// <summary>
        /// Method for getting width scaling.
        /// </summary>
        /// <returns>Width scaling.</returns>
        float GetWidthScale();

        /// <summary>
        /// Method for getting height scaling.
        /// </summary>
        /// <returns>Height scaling.</returns>
        float GetHeightScale();

        /// <summary>
        /// Method for setting shape's allignment within the item.
        /// </summary>
        /// <param name="alignments">Alignment as SpaceVIL.Core.ItemAlignment.</param>
        void SetAlignment(params ItemAlignment[] alignments);
        
        /// <summary>
        /// Method for getting shape's allignment within the item.
        /// </summary>
        /// <returns>Alignment as SpaceVIL.Core.ItemAlignment.</returns>
        ItemAlignment GetAlignment();
    }
}