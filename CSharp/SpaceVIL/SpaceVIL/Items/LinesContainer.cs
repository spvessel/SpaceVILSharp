using System;
using System.Drawing;
using System.Collections.Generic;
using System.Linq;
using SpaceVIL.Core;

namespace SpaceVIL
{
    public class LinesContainer : Primitive, ILine
    {
        static int count = 0;

        /// <summary>
        /// Constructs a LinesContainer
        /// </summary>
        public LinesContainer() : base(name: "LinesContainer_" + count)
        {
            SetBackground(Color.Transparent);
            count++;
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
                SetTriangles(UpdateShape());
        }

        public List<float[]> GetPoints()
        {
            if (GetTriangles() == null || GetTriangles().Count < 2)
                return null;
            return GetTriangles();
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