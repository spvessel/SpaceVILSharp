using System;
using System.Drawing;
using System.Collections.Generic;
using System.Linq;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// PointsContainer is class for rendering points in graph. 
    /// </summary>
    public class PointsContainer : Primitive, IPoints
    {
        static int count = 0;

        /// <summary>
        /// Default PointsContainer constructor.
        /// </summary>
        public PointsContainer() : base(name: "PointsContainer_" + count)
        {
            SetBackground(Color.Transparent);
            count++;
        }
        private float _thickness = 1.0f;
        /// <summary>
        /// Setting thickness of points.
        /// </summary>
        /// <param name="thickness">Point thickness.</param>
        public void SetPointThickness(float thickness)
        {
            _thickness = thickness;
        }
        /// <summary>
        /// Getting points thickness.
        /// </summary>
        /// <returns>Point thickness.</returns>
        public float GetPointThickness()
        {
            return _thickness;
        }
        private Color _color = Color.White;
        /// <summary>
        /// Setting points color. Default: White.
        /// </summary>
        /// <param name="color">Points color.</param>
        public void SetPointColor(Color color)
        {
            _color = color;
        }
        /// <summary>
        /// Getting points color.
        /// </summary>
        /// <returns>Points color.</returns>
        public Color GetPointColor()
        {
            return _color;
        }
        private List<float[]> _pointShape;
        /// <summary>
        /// Setting custom shape for points (if one want to use other shape than circle).
        /// </summary>
        /// <param name="shape">Points list of the shape as List of float[2] array.</param>
        public void SetPointShape(List<float[]> shape)
        {
            if (shape == null)
                return;
            _pointShape = shape;
        }
        /// <summary>
        /// Getting current shape of points. Default: circle shape.
        /// </summary>
        /// <returns>Points list as List of float[2] array.</returns>
        public List<float[]> GetPointShape()
        {
            if (_pointShape == null)
                _pointShape = GraphicsMathService.GetEllipse(GetPointThickness() / 2.0f, 16);
            return _pointShape;
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
            {
                SetTriangles(UpdateShape());
            }
        }
    }
}