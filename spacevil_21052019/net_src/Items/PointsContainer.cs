using System;
using System.Drawing;
using System.Collections.Generic;
using System.Linq;
using SpaceVIL.Core;

namespace SpaceVIL
{
    public class PointsContainer : Primitive, IPoints, ILine
    {
        // PrimitiveType _shape
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
            List<float[]> tmp = new List<float[]>();
            foreach (var item in coord)
            {
                tmp.Add(item);
            }
            SetTriangles(tmp);
        }

        private List<float[]> UpdateCrd()
        {
            //clone triangles
            List<float[]> result = new List<float[]>();
            for (int i = 0; i < GetTriangles().Count; i++)
                result.Add(new float[] { GetTriangles().ElementAt(i)[0], GetTriangles().ElementAt(i)[1], GetTriangles().ElementAt(i)[2] });
            
            //to the left top corner
            foreach (var item in result)
            {
                item[0] = (item[0]) + GetX();
                item[1] = (item[1]) + GetY();
            }

            return result;
        }

        /// <summary>
        /// Make shape according to triangles list assigned with setTriangles
        /// </summary>
        public override List<float[]> MakeShape()
        {
            if (GetTriangles() == null || GetTriangles().Count < 2)
                return null;

            return UpdateShape();
        }

        float _line_thickness = 1.0f;

        /// <summary>
        /// Thickness of the line between the points
        /// </summary>
        public void SetLineThickness(float thickness)
        {
            _line_thickness = thickness;
        }
        public float GetLineThickness()
        {
            return _line_thickness;
        }

        Color _line_color = Color.Blue;

        /// <summary>
        /// The line color
        /// </summary>
        public void SetLineColor(Color color)
        {
            _line_color = color;
        }
        public Color GetLineColor()
        {
            return _line_color;
        }
    }
}