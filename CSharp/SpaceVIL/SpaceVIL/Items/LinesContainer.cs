using System.Drawing;
using System.Collections.Generic;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// LinesContainer is class for rendering lines in graph.
    /// </summary>
    public class LinesContainer : Primitive, ILines
    {
        static int count = 0;

        /// <summary>
        /// Default LinesContainer constructor.
        /// </summary>
        public LinesContainer() : base(name: "LinesContainer_" + count)
        {
            SetBackground(Color.Transparent);
            count++;
        }
        private float _lineThickness = 1.0f;
        /// <summary>
        /// Setting thickness of lines.
        /// </summary>
        /// <param name="thickness">Line thickness.</param>
        public void SetLineThickness(float thickness)
        {
            _lineThickness = thickness;
        }
        /// <summary>
        /// Getting lines thickness.
        /// </summary>
        /// <returns>Lines thickness.</returns>
        public float GetLineThickness()
        {
            return _lineThickness;
        }
        private Color _lineColor = Color.Blue;
        /// <summary>
        /// Setting lines color.
        /// <pata/> Default: Blue.
        /// </summary>
        /// <param name="color">Line color.</param>
        public void SetLineColor(Color color)
        {
            _lineColor = color;
        }
        /// <summary>
        /// Getting lines color.
        /// </summary>
        /// <returns>Lines color.</returns>
        public Color GetLineColor()
        {
            return _lineColor;
        }
        /// <summary>
        /// Setting points coordinates.
        /// </summary>
        /// <param name="coord">Points list as List of float[2] array.</param>
        public void SetPoints(List<float[]> coord)
        {
            List<float[]> tmp = new List<float[]>(coord);
            SetTriangles(tmp);
        }
        /// <summary>
        /// Getting points coordinates.
        /// </summary>
        /// <returns>Points list as List of float[2] array.</returns>
        public List<float[]> GetPoints()
        {
            if (GetTriangles() == null || GetTriangles().Count < 2)
                return null;
            return GetTriangles();
        }
        /// <summary>
        /// Overridden method for stretching the points position relative 
        /// to the current size of the item. 
        /// Use in conjunction with GetTriangles() and SetTriangles() methods.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void MakeShape()
        {
            if (GetTriangles() != null)
                SetTriangles(UpdateShape());
        }
    }
}