using System.Drawing;
using System.Collections.Generic;

namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that discribes such type of items 
    /// that are points (for drawing graphs).
    /// </summary>
    public interface IPoints
    {
        /// <summary>
        /// Setting thickness of points.
        /// </summary>
        /// <param name="thickness">Point thickness.</param>
        void SetPointThickness(float thickness);
        /// <summary>
        /// Getting points thickness.
        /// </summary>
        /// <returns>Point thickness.</returns>
        float GetPointThickness();
        /// <summary>
        /// Setting points color.
        /// </summary>
        /// <param name="color">Points color.</param>
        void SetPointColor(Color color);
        /// <summary>
        /// Getting points color.
        /// </summary>
        /// <returns>Points color.</returns>
        Color GetPointColor();
        /// <summary>
        /// Setting custom shape for points (if one want to use other shape than circle).
        /// </summary>
        /// <param name="shape">Points list of the shape as List of float[2] array.</param>
        void SetShapePointer(List<float[]> shape);
        /// <summary>
        /// Getting current shape of points. Default: circle shape.
        /// </summary>
        /// <returns>Points list of the shape as List of float[2] array.</returns>
        List<float[]> GetShapePointer();
        /// <summary>
        /// Getting points coordinates.
        /// </summary>
        /// <returns>Points list as List of float[2] array.</returns>
        List<float[]> GetPoints();
        /// <summary>
        /// Setting points coordinates.
        /// </summary>
        /// <param name="coord">Points list as List of float[2] array.</param>
        void SetPoints(List<float[]> coord);
    }
}