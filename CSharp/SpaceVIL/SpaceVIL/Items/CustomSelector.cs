using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class CustomSelecter : Primitive
    {
        static int count = 0;
        public CustomSelecter()
            : base(name: "CustomSelecter_" + count)
        {
            count++;
        }

        public override List<float[]> MakeShape()
        {
            //SetTriangles(GraphicsMathService.GetRectangle(GetWidth(), GetHeight(), GetX(), GetY()));
            return GraphicsMathService.ToGL(this as BaseItem, GetHandler());
        }

        public void SetRectangles(List<Point> points)
        {
            List<float[]> triangles = new List<float[]>();
            int w1 = 0, w2 = 0;
            int h1 = 0, h2 = 0;
            if (points != null) {
                w1 = points[0].X;
                w2 = w1;
                h1 = points[0].Y;
                h2 = h1;
                for (int i = 0; i < points.Count / 2; i++)
                {
                    Point p1 = points[i * 2 + 0];
                    Point p2 = points[i * 2 + 1];
                    triangles.AddRange(GraphicsMathService.GetRectangle((p2.X - p1.X), (p2.Y - p1.Y), p1.X, p2.Y));
                    w1 = (p1.X < w1) ? p1.X : w1;
                    w2 = (p2.X > w2) ? p2.X : w2;
                    h1 = (p1.Y < h1) ? p1.Y : h1;
                    h2 = (p2.Y > h2) ? p2.Y : h2;
                }
            }
            SetTriangles(triangles);
            SetWidth(w2 - w1);
            SetHeight(h2 - h1);

            //Console.WriteLine(w1 + " " + h1 + " " + w2 + " " + h2);
        }
    }
}
