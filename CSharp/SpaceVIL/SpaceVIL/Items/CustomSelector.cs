using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    internal class CustomSelector : Primitive
    {
        private static int count = 0;

        /// <summary>
        /// Constructs a CustomSelector
        /// </summary>
        internal CustomSelector() : base(name: "CustomSelector_" + count)
        {
            count++;
        }

        /// <summary>
        /// Make CustomSelector's rectangles with left top and right bottom points
        /// </summary>
        internal void SetRectangles(List<SpaceVIL.Core.Point> points)
        {
            List<float[]> triangles = new List<float[]>();
            int w1 = 0, w2 = 0;
            int h1 = 0, h2 = 0;
            int rectX = 0, rectY = 0;
            if (points != null && points.Count > 0)
            {
                w1 = points[0].X;
                w2 = w1;
                h1 = points[0].Y;
                h2 = h1;
                rectX = points[0].X;
                rectY = points[1].Y;
                for (int i = 0; i < points.Count / 2; i++)
                {
                    SpaceVIL.Core.Point p1 = points[i * 2 + 0];
                    SpaceVIL.Core.Point p2 = points[i * 2 + 1];
                    triangles.AddRange(
                        GraphicsMathService.GetRectangle((p2.X - p1.X), (p2.Y - p1.Y), p1.X - rectX, p2.Y - rectY));
                    w1 = (p1.X < w1) ? p1.X : w1;
                    w2 = (p2.X > w2) ? p2.X : w2;
                    h1 = (p1.Y < h1) ? p1.Y : h1;
                    h2 = (p2.Y > h2) ? p2.Y : h2;
                }
            }
            SetX(rectX);
            SetY(rectY);
            SetWidth(w2 - w1);
            SetHeight(h2 - h1);
            SetTriangles(triangles);
            ItemsRefreshManager.SetRefreshShape(this);
        }

        /// <summary>
        /// Shift selector on Y direction
        /// </summary>
        internal void ShiftAreaY(int yShift) {
            SetY(yShift);
            List<float[]> triangles = GetTriangles();
            if (triangles == null || triangles.Count == 0)
                return;
            
            foreach (float[] xyz in triangles) {
                xyz[1] += yShift;
            }
            SetTriangles(triangles);
        }

        /// <summary>
        /// Shift selector on X direction
        /// </summary>
        internal void ShiftAreaX(int xShift)
        {
            SetX(xShift);
            List<float[]> triangles = GetTriangles();
            if (triangles == null || triangles.Count == 0)
                return;

            foreach (float[] xyz in triangles)
            {
                xyz[0] += xShift;
            }
            SetTriangles(triangles);
        }
    }
}
