using System.Drawing;
using System.Collections.Generic;

namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that discribes such type of items 
    /// that are adjacent points are considered lines (for drawing graphs).
    /// </summary>
    public interface ILine : IPosition
    {
        /// <summary>
        /// Setting thickness of lines.
        /// </summary>
        /// <param name="thickness">Line thickness.</param>
        void SetLineThickness(float thickness);
        /// <summary>
        /// Getting lines thickness.
        /// </summary>
        /// <returns>Lines thickness.</returns>
        float GetLineThickness();
        /// <summary>
        /// Setting lines color.
        /// </summary>
        /// <param name="color">Line color.</param>
        void SetLineColor(Color color);
        /// <summary>
        /// Getting lines color.
        /// </summary>
        /// <returns>Lines color.</returns>
        Color GetLineColor();
        /// <summary>
        /// Getting adjacent points are considered lines.
        /// </summary>
        /// <returns>Points list as List of float[2] array.</returns>
        List<float[]> GetPoints();
        /// <summary>
        /// Setting adjacent points are considered lines.
        /// </summary>
        /// <param name="coord">Points list as List of float[2] array.</param>
        void SetPoints(List<float[]> coord);
    }
}