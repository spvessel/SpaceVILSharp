using System;
using System.Drawing;
using System.Collections.Generic;
using System.Linq;
using SpaceVIL.Core;

namespace SpaceVIL
{
    public class PointsContainer : Primitive, IPoints
    {
        static int count = 0;

        /// <summary>
        /// Constructs a PointsContainer
        /// </summary>
        public PointsContainer() : base(name: "PointsContainer_" + count)
        {
            SetBackground(Color.Transparent);
            count++;
        }

        float _thickness = 1.0f;

        /// <summary>
        /// Points thickness
        /// </summary>
        public void SetPointThickness(float thickness)
        {
            _thickness = thickness;
        }
        public float GetPointThickness()
        {
            return _thickness;
        }

        Color _color = Color.White;

        /// <summary>
        /// Points color
        /// </summary>
        public void SetPointColor(Color color)
        {
            _color = color;
        }
        public Color GetPointColor()
        {
            return _color;
        }

        List<float[]> _shape_pointer;

        /// <summary>
        /// Shape of the points
        /// </summary>
        public void SetShapePointer(List<float[]> shape)
        {
            if (shape == null)
                return;
            _shape_pointer = shape;
        }
        public List<float[]> GetShapePointer()
        {
            if (_shape_pointer == null)
                _shape_pointer = GraphicsMathService.GetEllipse(GetPointThickness() / 2.0f, 16);
            return _shape_pointer;
        }

        /// <summary>
        /// List of the points coordinates
        /// </summary>
        public void SetPointsCoord(List<float[]> coord)
        {
            List<float[]> tmp = new List<float[]>(coord);
            SetTriangles(tmp);
        }

        /// <summary>
        /// Make shape according to triangles list assigned with setTriangles
        /// </summary>
        public override void MakeShape()
        {
            if (GetTriangles() != null)
            {
                SetTriangles(UpdateShape());
            }
        }

        public List<float[]> GetPoints()
        {
            if (GetTriangles() == null || GetTriangles().Count < 2)
                return null;
            return GetTriangles();
        }
    }
}