using System;
using System.Drawing;
using System.Collections.Generic;

namespace SpaceVIL
{
    public class PointsContainer : Primitive, IPoints, ILine
    {
        static int count = 0;
        public PointsContainer() : base(name: "PointsContainer_" + count)
        {
            SetBackground(Color.Transparent);
            count++;
        }

        float _thickness = 1.0f;
        public void SetPointThickness(float thickness)
        {
            _thickness = thickness;
        }
        public float GetPointThickness()
        {
            return _thickness;
        }
        Color _color = Color.White;
        public void SetPointColor(Color color)
        {
            _color = color;
        }
        public Color GetPointColor()
        {
            return _color;
        }
        List<float[]> _shape_pointer;
        public void SetShapePointer(List<float[]> shape)
        {
            if (shape == null)
                return;
            _shape_pointer = shape;
        }
        public List<float[]> GetShapePointer()
        {
            return _shape_pointer;
        }

        public void SetPointsCoord(List<float[]> coord)
        {
            List<float[]> tmp = new List<float[]>();
            foreach (var item in coord)
            {
                tmp.Add(item);
            }
            SetTriangles(tmp);
        }
        public override List<float[]> MakeShape()
        {
            if (GetTriangles() == null || GetTriangles().Count == 0)
                return null;

            return GraphicsMathService.ToGL(UpdateShape(), GetHandler());
        }

        float _line_thickness = 1.0f;
        public void SetLineThickness(float thickness)
        {
            _line_thickness = thickness;
        }
        public float GetLineThickness()
        {
            return _line_thickness;
        }
        Color _line_color = Color.Blue;
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