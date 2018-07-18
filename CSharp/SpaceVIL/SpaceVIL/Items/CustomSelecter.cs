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

        public void SetRectangles(List<PointF> points)
        {
            List<float[]> triangles = new List<float[]>();
            if (points != null) { 
                for (int i = 0; i < points.Count / 2; i++)
                {
                    PointF p1 = points[i * 4 + 0];
                    PointF p2 = points[i * 4 + 1];
                    triangles.AddRange(GraphicsMathService.GetRectangle((int)(p2.X - p1.X), (int)(p2.Y - p1.Y), (int)p1.X, (int)p2.Y));
                }
            }
            SetTriangles(triangles);
        }
    }
}
