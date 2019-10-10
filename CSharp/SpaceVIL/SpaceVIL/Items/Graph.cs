using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Core;

namespace SpaceVIL
{
    public class Graph : Prototype
    {
        private static int count = 0;

        PointsContainer points;
        LinesContainer lines;

        public bool IsHover = false;

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            if (IsHover)
                return base.GetHoverVerification(xpos, ypos);
            return false;
        }

        public Graph()
        {
            SetItemName("Graph_" + count);
            SetBackground(Color.Transparent);
            count++;

            points = new PointsContainer();
            lines = new LinesContainer();

            IsFocusable = false;
        }

        public Graph(bool hover) : this()
        {
            IsHover = hover;
        }

        public override void InitElements()
        {
            lines.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            points.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            AddItems(lines, points);
        }

        public void SetPointThickness(float thickness)
        {
            points.SetPointThickness(thickness);
        }

        public float GetPointThickness()
        {
            return points.GetPointThickness();
        }

        public void SetPointColor(Color color)
        {
            points.SetPointColor(color);
        }

        public Color GetPointColor()
        {
            return points.GetPointColor();
        }

        public void SetShapePointer(List<float[]> shape)
        {
            points.SetShapePointer(shape);
        }

        public List<float[]> GetShapePointer()
        {
            return points.GetShapePointer();
        }

        public void SetPointsCoord(List<float[]> coord)
        {
            points.SetPointsCoord(coord);
            lines.SetPointsCoord(coord);
        }

        public List<float[]> GetPointsCoord()
        {
            return points.GetTriangles();
        }

        public void SetLineThickness(float thickness)
        {
            lines.SetLineThickness(thickness);
        }

        public float GetLineThickness()
        {
            return lines.GetLineThickness();
        }

        public void SetLineColor(Color color)
        {
            lines.SetLineColor(color);
        }

        public Color GetLineColor()
        {
            return lines.GetLineColor();
        }
    }
}