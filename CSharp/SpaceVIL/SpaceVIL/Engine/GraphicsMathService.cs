using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;

namespace SpaceVIL
{
    public struct CornerRadius
    {
        public float leftTop;
        public float rightTop;
        public float leftBottom;
        public float rightBottom;

        public CornerRadius(float lt, float rt, float lb, float rb)
        {
            leftTop = lt;
            rightTop = rt;
            leftBottom = lb;
            rightBottom = rb;
        }
    }

    internal class BorderSection
    {
        internal float thickness = 0;
        internal float x1;
        internal float y1;
        internal float x2;
        internal float y2;
        internal float nx;
        internal float ny;

        public BorderSection(float x1, float y1, float x2, float y2, float x3, float y3)
        {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;

            checkNormDir(x3, y3);
        }

        private void checkNormDir(float x3, float y3)
        {
            nx = y1 - y2;
            ny = x2 - x1;

            float k = Math.Sign((x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1));
            /*
            if (nx == 0 || ny == 0) {
                System.out.println();
                System.out.println("nx " + nx + " ny " + ny);
                System.out.println("x1 " + x1 + " y1 " + y1);
                System.out.println("x2 " + x2 + " y2 " + y2);
                System.out.println("k " + k);
            }
            */
            if (k != 0)
            {
                nx *= k;
                ny *= k;
            }

            float d = (float)Math.Sqrt(Math.Pow(nx, 2.0) + Math.Pow(ny, 2.0));
            if (d != 0)
            {
                nx /= d;
                ny /= d;
            }
            /*
            if (nx == 0 || ny == 0) {

                System.out.println("after nx " + nx + " ny " + ny);
                System.out.println();
            }
            */
        }
    }

    public static class GraphicsMathService
    {
        public static Color MixColors(params Color[] m_colors)
        {
            if (m_colors.Length == 0)
                return Color.White;
            if (m_colors.Length == 1)
                return m_colors[0];

            float r = 0, g = 0, b = 0, a = 255.0f;
            foreach (Color item in m_colors)
            {
                if (item.A == 255) //exchange
                {
                    r = item.R;
                    g = item.G;
                    b = item.B;
                }
                else //mixing
                {
                    r = r * (1.0f - item.A / 255.0f) + item.R * (item.A / 255.0f);
                    g = g * (1.0f - item.A / 255.0f) + item.G * (item.A / 255.0f);
                    b = b * (1.0f - item.A / 255.0f) + item.B * (item.A / 255.0f);
                }
            }
            return Color.FromArgb((int)a, (int)r, (int)g, (int)b);
        }
        static internal List<float[]> GetRectangle(float w = 100, float h = 100, float x = 0, float y = 0)
        {
            return new List<float[]>
            {
                new float[] { x, y, 0.0f },
                new float[] { x, y + h, 0.0f },
                new float[] { x + w, y + h, 0.0f },

                new float[] { x + w, y + h, 0.0f },
                new float[] { x + w, y, 0.0f },
                new float[] { x, y, 0.0f }
            };
        }

        static internal List<float[]> GetRoundSquare(CornerRadius cornerRadius, float width = 100, float height = 100,
            int x = 0, int y = 0)
        {

            if (cornerRadius.leftTop < 0)
                cornerRadius.leftTop = 0;
            if (cornerRadius.rightTop < 0)
                cornerRadius.rightTop = 0;
            if (cornerRadius.leftBottom < 0)
                cornerRadius.leftBottom = 0;
            if (cornerRadius.rightBottom < 0)
                cornerRadius.rightBottom = 0;


            List<float[]> triangles = new List<float[]>();
            //Начало координат в левом углу

            //1
            triangles.AddRange(RectToTri(new PointF(cornerRadius.leftTop + x, 0.0f + y), new PointF(width / 2f + x, height / 2f + y)));
            triangles.AddRange(RectToTri(new PointF(0.0f + x, cornerRadius.leftTop + y), new PointF(cornerRadius.leftTop + x, height / 2f + y)));

            //2
            triangles.AddRange(RectToTri(new PointF(width / 2f + x, 0.0f + y), new PointF(width - cornerRadius.rightTop + x, height / 2f + y)));
            triangles.AddRange(RectToTri(new PointF(width - cornerRadius.rightTop + x, cornerRadius.rightTop + y), new PointF(width + x, height / 2f + y)));

            //3
            triangles.AddRange(RectToTri(new PointF(cornerRadius.leftBottom + x, height / 2f + y), new PointF(width / 2f + x, height + y)));
            triangles.AddRange(RectToTri(new PointF(0.0f + x, height / 2f + y), new PointF(cornerRadius.leftBottom + x, height - cornerRadius.leftBottom + y)));

            //4
            triangles.AddRange(RectToTri(new PointF(width / 2f + x, height / 2f + y), new PointF(width - cornerRadius.rightBottom + x, height + y)));
            triangles.AddRange(RectToTri(new PointF(width - cornerRadius.rightBottom + x, height / 2f + y), new PointF(width + x, height - cornerRadius.rightBottom + y)));


            //if (radius < 1)
            //    return triangles;

            float x0, y0;

            if (cornerRadius.rightBottom >= 1)
            {
                x0 = width - cornerRadius.rightBottom + x;
                y0 = height - cornerRadius.rightBottom + y;
                triangles.AddRange(CountCircleSector(0, 90, x0, y0, cornerRadius.rightBottom));
            }

            if (cornerRadius.rightTop >= 1)
            {
                x0 = width - cornerRadius.rightTop + x;
                y0 = cornerRadius.rightTop + y;
                triangles.AddRange(CountCircleSector(270, 360, x0, y0, cornerRadius.rightTop));
            }

            if (cornerRadius.leftTop >= 1)
            {
                x0 = cornerRadius.leftTop + x;
                y0 = cornerRadius.leftTop + y;
                triangles.AddRange(CountCircleSector(180, 270, x0, y0, cornerRadius.leftTop));
            }

            if (cornerRadius.leftBottom >= 1)
            {
                x0 = cornerRadius.leftBottom + x;
                y0 = height - cornerRadius.leftBottom + y;
                triangles.AddRange(CountCircleSector(90, 180, x0, y0, cornerRadius.leftBottom));
            }

            return triangles;
        }

        static internal List<float[]> RectToTri(PointF leftTop, PointF rightBottom)
        {
            //Начало координат в левом верхнем углу
            List<float[]> tri = new List<float[]>();

            tri.Add(new float[] { leftTop.X, leftTop.Y, 0.0f });
            tri.Add(new float[] { leftTop.X, rightBottom.Y, 0.0f });
            tri.Add(new float[] { rightBottom.X, rightBottom.Y, 0.0f });

            tri.Add(new float[] { rightBottom.X, rightBottom.Y, 0.0f });
            tri.Add(new float[] { rightBottom.X, leftTop.Y, 0.0f });
            tri.Add(new float[] { leftTop.X, leftTop.Y, 0.0f });

            return tri;
        }

        static internal List<float[]> GetRoundSquare(float width = 100, float height = 100, float radius = 0.0f, int x = 0, int y = 0)
        {
            if (width <= 0 || height <= 0)
                return null;

            if (radius < 0)
                radius = 0;

            List<float[]> triangles = new List<float[]>();
            //Начало координат в углу

            triangles.Add(new float[] { radius + x, height + y, 0.0f });
            triangles.Add(new float[] { width - radius + x, y, 0.0f });
            triangles.Add(new float[] { radius + x, y, 0.0f });
            //1

            triangles.Add(new float[] { radius + x, height + y, 0.0f });
            triangles.Add(new float[] { width - radius + x, height + y, 0.0f });
            triangles.Add(new float[] { width - radius + x, y, 0.0f });
            //2

            triangles.Add(new float[] { width - radius + x, height - radius + y, 0.0f });
            triangles.Add(new float[] { width + x, radius + y, 0.0f });
            triangles.Add(new float[] { width - radius + x, radius + y, 0.0f });
            //3

            triangles.Add(new float[] { width - radius + x, height - radius + y, 0.0f });
            triangles.Add(new float[] { width + x, height - radius + y, 0.0f });
            triangles.Add(new float[] { width + x, radius + y, 0.0f });
            //4

            triangles.Add(new float[] { x, height - radius + y, 0.0f });
            triangles.Add(new float[] { radius + x, radius + y, 0.0f });
            triangles.Add(new float[] { x, radius + y, 0.0f });
            //5

            triangles.Add(new float[] { x, height - radius + y, 0.0f });
            triangles.Add(new float[] { radius + x, height - radius + y, 0.0f });
            triangles.Add(new float[] { radius + x, radius + y, 0.0f });
            //6

            if (radius < 1)
                return triangles;

            float x0, y0;
            x0 = width - radius + x;
            y0 = height - radius + y;
            triangles.AddRange(CountCircleSector(0, 90, x0, y0, radius)); //7

            x0 = width - radius + x;
            y0 = radius + y;
            triangles.AddRange(CountCircleSector(270, 360, x0, y0, radius)); //8

            x0 = radius + x;
            y0 = radius + y;
            triangles.AddRange(CountCircleSector(180, 270, x0, y0, radius)); //9

            x0 = radius + x;
            y0 = height - radius + y;
            triangles.AddRange(CountCircleSector(90, 180, x0, y0, radius)); //10

            return triangles;
        }

        static private List<float[]> CountCircleSector(int alph1, int alph2, float x0, float y0, float radius)
        {
            List<float[]> circleSect = new List<float[]>();
            float x1, y1, x2, y2;
            x1 = radius * (float)Math.Cos(alph1 * Math.PI / 180.0f) + x0;
            y1 = radius * (float)Math.Sin(alph1 * Math.PI / 180.0f) + y0;

            for (int alf = alph1 + 1; alf <= alph2; alf += 5)
            { //Шаг можно сделать больше 1 градуса, нужны тестирования
                x2 = radius * (float)Math.Cos(alf * Math.PI / 180.0f) + x0;
                y2 = radius * (float)Math.Sin(alf * Math.PI / 180.0f) + y0;
                circleSect.Add(new float[] { x0, y0, 0.0f });
                circleSect.Add(new float[] { x2, y2, 0.0f });
                circleSect.Add(new float[] { x1, y1, 0.0f }); //против часовой стрелки
                x1 = x2;
                y1 = y2;
            }
            x2 = radius * (float)Math.Cos(alph2 * Math.PI / 180.0f) + x0;
            y2 = radius * (float)Math.Sin(alph2 * Math.PI / 180.0f) + y0;
            circleSect.Add(new float[] { x0, y0, 0.0f });
            circleSect.Add(new float[] { x2, y2, 0.0f });
            circleSect.Add(new float[] { x1, y1, 0.0f });

            return circleSect;
        }
        static internal List<float[]> ToGL(IBaseItem item, WindowLayout handler) //where TLayout : VisualItem
        {
            if (item.GetTriangles() == null)
                return null;
            List<float[]> result = new List<float[]>();

            foreach (var vector in item.GetTriangles())
            {
                float x = (vector[0] / (float)handler.GetWidth()) * 2.0f - 1.0f;
                float y = (vector[1] / (float)handler.GetHeight() * 2.0f - 1.0f) * (-1.0f);
                result.Add(new float[] { x, y, vector[2] });
            }
            return result;
        }
        static internal List<float[]> ToGL(List<float[]> triangles, WindowLayout handler) //where TLayout : VisualItem
        {
            List<float[]> result = new List<float[]>();

            foreach (var vector in triangles)
            {
                float x = (vector[0] / (float)handler.GetWidth()) * 2.0f - 1.0f;
                float y = (vector[1] / (float)handler.GetHeight() * 2.0f - 1.0f) * (-1.0f);
                result.Add(new float[] { x, y, vector[2] });
            }
            return result;
        }

        static public List<float[]> GetStar(float R = 100, float r = 50, int n = 5)
        {
            float x_center = r;
            float y_center = r;

            List<float[]> triangles = new List<float[]>();

            float alpha = 0.0f;
            for (int i = 0; i < n; i++)
            {
                triangles.Add(new float[]
                {
                    x_center,
                    y_center,
                    0.0f
                });

                triangles.Add(new float[]
                {
                    (float)(x_center + r / 2 * Math.Cos(alpha * Math.PI / 180.0f)),
                    (float)(y_center - r / 2 * Math.Sin(alpha * Math.PI / 180.0f)),
                    0.0f
                });

                alpha = alpha + 360.0f / n;

                triangles.Add(new float[]
                {
                    (float)(x_center + r / 2 * Math.Cos(alpha * Math.PI / 180.0f)),
                    (float)(y_center - r / 2 * Math.Sin(alpha * Math.PI / 180.0f)),
                    0.0f
                });
            }

            alpha = 0.0f;
            int count = 1;
            for (int i = 1; i < n * 2 + 2; i++)
            {
                if ((i % 2) != 0) //При выполнении условия четности следующие формулы
                {
                    triangles.Add(new float[]
                    {
                        (float)(x_center + r / 2 * Math.Cos(alpha * Math.PI / 180.0f)),
                        (float)(y_center - r / 2 * Math.Sin(alpha * Math.PI / 180.0f)),
                        0.0f
                    });
                    if (count % 3 == 0)
                    {
                        triangles.Add(new float[]
                        {
                            (float)(x_center + r / 2 * Math.Cos(alpha * Math.PI / 180.0f)),
                            (float)(y_center - r / 2 * Math.Sin(alpha * Math.PI / 180.0f)),
                            0.0f
                        });
                        count = 1;
                    }
                }
                else //При невыполнении условия четности следующие формулы
                {
                    triangles.Add(new float[]
                    {
                        (float)(x_center + R / 2 * Math.Cos(alpha * Math.PI / 180.0f)),
                        (float)(y_center - R / 2 * Math.Sin(alpha * Math.PI / 180.0f)),
                        0.0f
                    });
                    if (count % 3 == 0)
                    {
                        triangles.Add(new float[]
                        {
                            (float)(x_center + R / 2 * Math.Cos(alpha * Math.PI / 180.0f)),
                            (float)(y_center - R / 2 * Math.Sin(alpha * Math.PI / 180.0f)),
                            0.0f
                        });
                        count = 1;
                    }
                }
                alpha = alpha + 180.0f / n;
                count++;
            }
            triangles.RemoveAt(triangles.Count - 1);
            return triangles;
        }
        static public List<float[]> GetRegularPolygon(float r = 100, int n = 6)
        {
            float x_center = r;
            float y_center = r;

            List<float[]> triangles = new List<float[]>();

            float alpha = 0;
            for (int i = 0; i < n; i++)
            {
                triangles.Add(new float[]
                {
                    x_center,
                    y_center,
                    0.0f
                });

                triangles.Add(new float[]
                {
                    (float)(x_center + r / 2 * Math.Cos(alpha * Math.PI / 180.0f)),
                    (float)(y_center - r / 2 * Math.Sin(alpha * Math.PI / 180.0f)),
                    0.0f
                });

                alpha = alpha + 360.0f / n;

                triangles.Add(new float[]
                {
                    (float)(x_center + r / 2 * Math.Cos(alpha * Math.PI / 180.0f)),
                    (float)(y_center - r / 2 * Math.Sin(alpha * Math.PI / 180.0f)),
                    0.0f
                });
            }

            return triangles;
        }
        static public List<float[]> GetEllipse(float r = 100, int n = 32)
        {
            float x_center = r;
            float y_center = r;

            List<float[]> triangles = new List<float[]>();

            float alpha = 0;
            for (int i = 0; i < n; i++)
            {
                triangles.Add(new float[]
                {
                    x_center,
                    y_center,
                    0.0f
                });

                triangles.Add(new float[]
                {
                    (float)(x_center + r * Math.Cos(alpha * Math.PI / 180.0f)),
                    (float)(y_center - r * Math.Sin(alpha * Math.PI / 180.0f)),
                    0.0f
                });

                alpha = alpha + 360.0f / n;

                triangles.Add(new float[]
                {
                    (float)(x_center + r * Math.Cos(alpha * Math.PI / 180.0f)),
                    (float)(y_center - r * Math.Sin(alpha * Math.PI / 180.0f)),
                    0.0f
                });
            }

            return triangles;
        }
        static public List<float[]> GetEllipse(float w, float h, int x = 0, int y = 0, int n = 32)
        {
            float rX = w / 2;
            float rY = h / 2;
            float x_center = x + rX;
            float y_center = y + rY;
            List<float[]> triangles = new List<float[]>();

            float alpha = 0;
            for (int i = 0; i < n; i++)
            {
                triangles.Add(new float[]
                {
                    x_center,
                    y_center,
                    0.0f
                });

                triangles.Add(new float[]
                {
                    (float)(x_center + rX * Math.Cos(alpha * Math.PI / 180.0f)),
                    (float)(y_center - rY * Math.Sin(alpha * Math.PI / 180.0f)),
                    0.0f
                });

                alpha = alpha + 360.0f / n;

                triangles.Add(new float[]
                {
                    (float)(x_center + rX * Math.Cos(alpha * Math.PI / 180.0f)),
                    (float)(y_center - rY * Math.Sin(alpha * Math.PI / 180.0f)),
                    0.0f
                });
            }

            return triangles;
        }
        static public List<float[]> GetTriangle(float w = 100, float h = 100, int x = 0, int y = 0, int a = 0)
        {
            float x0 = x + w / 2;
            float y0 = y + h / 2;

            List<float[]> figure = new List<float[]>();

            figure.Add(new float[] { x + w / 2, y, 0.0f });
            figure.Add(new float[] { x, y + h, 0.0f });
            figure.Add(new float[] { x + w, y + h, 0.0f });

            foreach (var crd in figure)
            {
                float x_crd = crd[0];
                float y_crd = crd[1];
                crd[0] = x0 + (x_crd - x0) * (float)Math.Cos(a * Math.PI / 180.0f) - (y_crd - y0) * (float)Math.Sin(a * Math.PI / 180.0f);
                crd[1] = y0 + (y_crd - y0) * (float)Math.Cos(a * Math.PI / 180.0f) + (x_crd - x0) * (float)Math.Sin(a * Math.PI / 180.0f);
            }
            return figure;
        }
        static public List<float[]> GetLine(float lenght, float thichness, int alpha)
        {
            List<float[]> figure = new List<float[]>();
            figure.Add(new float[] { 0, 0, 0.0f });
            figure.Add(new float[] { 0, thichness, 0.0f });
            figure.Add(new float[] { 0 + lenght, thichness, 0.0f });

            figure.Add(new float[] { lenght, thichness, 0.0f });
            figure.Add(new float[] { lenght, 0, 0.0f });
            figure.Add(new float[] { 0, 0, 0.0f });

            //rotate
            float x0 = lenght / 2;
            float y0 = thichness / 2;
            foreach (var crd in figure)
            {
                float x_crd = crd[0];
                float y_crd = crd[1];
                crd[0] = x0 + (x_crd - x0) * (float)Math.Cos(alpha * Math.PI / 180.0f) - (y_crd - y0) * (float)Math.Sin(alpha * Math.PI / 180.0f);
                crd[1] = y0 + (y_crd - y0) * (float)Math.Cos(alpha * Math.PI / 180.0f) + (x_crd - x0) * (float)Math.Sin(alpha * Math.PI / 180.0f);
            }

            return figure;
        }
        static public List<float[]> GetCross(float w, float h, float thickness, int alpha)
        {
            List<float[]> figure = new List<float[]>();

            float x0 = (w - thickness) / 2;
            float y0 = (h - thickness) / 2;

            //center
            figure.Add(new float[] { x0, y0, 0.0f });
            figure.Add(new float[] { x0, y0 + thickness, 0.0f });
            figure.Add(new float[] { x0 + thickness, y0 + thickness, 0.0f });

            figure.Add(new float[] { x0 + thickness, y0 + thickness, 0.0f });
            figure.Add(new float[] { x0 + thickness, y0, 0.0f });
            figure.Add(new float[] { x0, y0, 0.0f });

            //top
            figure.Add(new float[] { x0, 0, 0.0f });
            figure.Add(new float[] { x0, y0, 0.0f });
            figure.Add(new float[] { x0 + thickness, y0, 0.0f });

            figure.Add(new float[] { x0 + thickness, y0, 0.0f });
            figure.Add(new float[] { x0 + thickness, 0, 0.0f });
            figure.Add(new float[] { x0, 0, 0.0f });

            //bottom
            figure.Add(new float[] { x0, y0 + thickness, 0.0f });
            figure.Add(new float[] { x0, h, 0.0f });
            figure.Add(new float[] { x0 + thickness, h, 0.0f });

            figure.Add(new float[] { x0 + thickness, h, 0.0f });
            figure.Add(new float[] { x0 + thickness, y0 + thickness, 0.0f });
            figure.Add(new float[] { x0, y0 + thickness, 0.0f });

            //left
            figure.Add(new float[] { 0, y0, 0.0f });
            figure.Add(new float[] { 0, y0 + thickness, 0.0f });
            figure.Add(new float[] { x0, y0 + thickness, 0.0f });

            figure.Add(new float[] { x0, y0 + thickness, 0.0f });
            figure.Add(new float[] { x0, y0, 0.0f });
            figure.Add(new float[] { 0, y0, 0.0f });

            //right
            figure.Add(new float[] { x0 + thickness, y0, 0.0f });
            figure.Add(new float[] { x0 + thickness, y0 + thickness, 0.0f });
            figure.Add(new float[] { h, y0 + thickness, 0.0f });

            figure.Add(new float[] { h, y0 + thickness, 0.0f });
            figure.Add(new float[] { h, y0, 0.0f });
            figure.Add(new float[] { x0 + thickness, y0, 0.0f });

            //rotate
            x0 = w / 2;
            y0 = h / 2;
            foreach (var crd in figure)
            {
                float x_crd = crd[0];
                float y_crd = crd[1];
                crd[0] = x0 + (x_crd - x0) * (float)Math.Cos(alpha * Math.PI / 180.0f) - (y_crd - y0) * (float)Math.Sin(alpha * Math.PI / 180.0f);
                crd[1] = y0 + (y_crd - y0) * (float)Math.Cos(alpha * Math.PI / 180.0f) + (x_crd - x0) * (float)Math.Sin(alpha * Math.PI / 180.0f);
            }
            return figure;
        }

        public static List<float> GetRectBorder(int w, int h)
        {
            return GetRectBorderIgnoreTop(w, h, 0, 0);
        }

        public static List<float> GetRectBorderIgnoreTop(int w, int h, int ignoreFrom, int ignoreWidth)
        {
            List<float> borderCoords = new List<float>();

            ignoreFrom = (ignoreFrom > w - 1) ? w - 1 : ignoreFrom;
            borderCoords.AddRange(ParallLineToArray(0, ignoreFrom, 0, LineDir.Horiz));
            borderCoords.AddRange(ParallLineToArray(ignoreFrom + ignoreWidth, w - 1, 0, LineDir.Horiz));
            borderCoords.AddRange(ParallLineToArray(0, w - 1, h - 1, LineDir.Horiz));
            borderCoords.AddRange(ParallLineToArray(1, h - 2, 0, LineDir.Vert));
            borderCoords.AddRange(ParallLineToArray(1, h - 2, w - 1, LineDir.Vert));

            return borderCoords;
        }

        private static List<float> ParallLineToArray(int min, int max, int permanent, LineDir dir)
        {
            List<float> arr = new List<float>();

            for (int i = min; i <= max; i++)
            {
                if (dir.Equals(LineDir.Horiz))
                {
                    arr.Add(i);
                    arr.Add(permanent);
                }
                else
                { //Vert
                    arr.Add(permanent);
                    arr.Add(i);
                }

                arr.Add(0);
            }

            return arr;
        }

        enum LineDir
        {
            Horiz,
            Vert
        }

        // internal static byte[] PointsToTexture(TextItem text)
        // {
        //     float[] _points = text.Shape();
        //     float[] _colors = text.GetColors();

        //     if (_colors == null || _points == null)
        //         return null;

        //     int _w = text.GetWidth();
        //     int _h = text.GetHeight();

        //     byte[] _bitmap = new byte[_w * _h * 4];
        //     for (int i = 0; i < _bitmap.Length; i++)
        //     {
        //         _bitmap[i] = 0;
        //     }

        //     int color_index = 0;

        //     for (int i = 0; i < _points.Length; i += 3)
        //     {
        //         int r = (int)_points[i + 1];
        //         int c = (int)_points[i];
        //         int pos = (r + c * _h) * 4;
        //         if (pos + 3 > _bitmap.Length)
        //             break;

        //         _bitmap[pos + 0] = (byte)(_colors[color_index + 0] * 255);
        //         _bitmap[pos + 1] = (byte)(_colors[color_index + 1] * 255);
        //         _bitmap[pos + 2] = (byte)(_colors[color_index + 2] * 255);
        //         _bitmap[pos + 3] = (byte)(_colors[color_index + 3] * 255);

        //         color_index += 4;
        //     }

        //     color_index = 0;
        //     return _bitmap;
        // }

        public static List<float[]> MoveShape(List<float[]> shape, float x, float y)
        {
            if (shape.Count == 0)
                return null;

            //clone triangles
            List<float[]> result = new List<float[]>();
            for (int i = 0; i < shape.Count; i++)
            {
                result.Add(new float[] { shape.ElementAt(i)[0], shape.ElementAt(i)[1], shape.ElementAt(i)[2] });
            }
            //to the left top corner
            foreach (var point in result)
            {
                point[0] += x;
                point[1] += y;
            }

            return result;
        }

        public static List<float[]> GetFolderIconShape(float w = 20.0f, float h = 15.0f, float x = 0, float y = 0)
        {
            List<float[]> triangles = new List<float[]>();
            triangles.AddRange(GraphicsMathService.GetRectangle(w / 3, h));
            triangles.AddRange(GraphicsMathService.GetRectangle(2 * w / 3, h - h / 4, w / 3, h / 4));
            triangles.AddRange(GraphicsMathService.GetRectangle(w / 4, h / 8, w / 3 + 2));
            return triangles;
        }

        public static List<float[]> GetRoundSquareBorder(float width, float height, float radius, float thickness, int x, int y)
        {
            if (radius < 0)
                radius = 0;

            List<BorderSection> border = new List<BorderSection>();
            // Начало координат в углу

            border.Add(new BorderSection(width - radius + x, y, radius + x, y, radius + x, height + y));
            //triangles.add(new float[] { radius + x, height + y, 0.0f });
            //    triangles.add(new float[] { width - radius + x, y, 0.0f });
            //    triangles.add(new float[] { radius + x, y, 0.0f });

            border.Add(new BorderSection(width - radius + x, height + y, radius + x, height + y, width - radius + x, y));
            //    triangles.add(new float[] { radius + x, height + y, 0.0f });
            //    triangles.add(new float[] { width - radius + x, height + y, 0.0f });
            //triangles.add(new float[] { width - radius + x, y, 0.0f });

            border.Add(new BorderSection(width + x, height - radius + y, width + x, radius + y, width - radius + x, height - radius + y));
            //triangles.add(new float[] { width - radius + x, height - radius + y, 0.0f });
            //    triangles.add(new float[] { width + x, height - radius + y, 0.0f });
            //    triangles.add(new float[] { width + x, radius + y, 0.0f });

            border.Add(new BorderSection(x, height - radius + y, x, radius + y, radius + x, radius + y));
            //    triangles.add(new float[] { x, height - radius + y, 0.0f });
            //triangles.add(new float[] { radius + x, radius + y, 0.0f });
            //    triangles.add(new float[] { x, radius + y, 0.0f });

            if (radius < 1)
                return MakeBorder(border, thickness);

            List<float[]> tmpList;
            float x0, y0;
            x0 = width - radius + x;
            y0 = height - radius + y;
            tmpList = CountCircleSector(0, 90, x0, y0, radius);

            x0 = width - radius + x;
            y0 = radius + y;
            tmpList.AddRange(CountCircleSector(270, 360, x0, y0, radius));

            x0 = radius + x;
            y0 = radius + y;
            tmpList.AddRange(CountCircleSector(180, 270, x0, y0, radius));

            x0 = radius + x;
            y0 = height - radius + y;
            tmpList.AddRange(CountCircleSector(90, 180, x0, y0, radius));

            for (int i = 0; i < tmpList.Count / 3; i++)
            {
                border.Add(new BorderSection(tmpList.ElementAt(i * 3 + 1)[0], tmpList.ElementAt(i * 3 + 1)[1], tmpList.ElementAt(i * 3 + 2)[0], tmpList.ElementAt(i * 3 + 2)[1], tmpList.ElementAt(i * 3)[0], tmpList.ElementAt(i * 3)[1]));
            }

            return MakeBorder(border, thickness);
        }

        static internal List<float[]> GetRoundSquareBorder(CornerRadius cornerRadius, float width, float height, 
            float thickness, int x, int y)
        {

            if (cornerRadius.leftTop < 0)
                cornerRadius.leftTop = 0;
            if (cornerRadius.rightTop < 0)
                cornerRadius.rightTop = 0;
            if (cornerRadius.leftBottom < 0)
                cornerRadius.leftBottom = 0;
            if (cornerRadius.rightBottom < 0)
                cornerRadius.rightBottom = 0;

            List<BorderSection> border = new List<BorderSection>();
            //Начало координат в левом углу

            //1
            border.Add(new BorderSection(cornerRadius.leftTop + x, 0.0f + y, width / 2f + x, 0.0f + y, width / 2f + x, height / 2f + y));
            //triangles.AddRange(RectToTri(new PointF(cornerRadius.leftTop, 0.0f), new PointF(width / 2f, height / 2f)));
            border.Add(new BorderSection(0.0f + x, cornerRadius.leftTop + y, 0.0f + x, height / 2f + y, cornerRadius.leftTop + x, height / 2f + y));
            //triangles.AddRange(RectToTri(new PointF(0.0f, cornerRadius.leftTop), new PointF(cornerRadius.leftTop, height / 2f)));

            //2
            //triangles.AddRange(RectToTri(new PointF(width / 2f, 0.0f), new PointF(width - cornerRadius.rightTop, height / 2f)));
            border.Add(new BorderSection(width / 2f + x, 0.0f + y, width - cornerRadius.rightTop + x, 0.0f + y, width - cornerRadius.rightTop + x, height / 2f + y));
            //triangles.AddRange(RectToTri(new PointF(width - cornerRadius.rightTop, cornerRadius.rightTop), new PointF(width, height / 2f)));
            border.Add(new BorderSection(width + x, height / 2f + y, width + x, cornerRadius.rightTop + y, width - cornerRadius.rightTop + x, cornerRadius.rightTop + y));

            //3
            //triangles.AddRange(RectToTri(new PointF(cornerRadius.leftBottom, height / 2f), new PointF(width / 2f, height)));
            border.Add(new BorderSection(width / 2f + x, height + y, cornerRadius.leftBottom + x, height + y, cornerRadius.leftBottom + x, height / 2f + y));
            //triangles.AddRange(RectToTri(new PointF(0.0f, height / 2f), new PointF(cornerRadius.leftBottom, height - cornerRadius.leftBottom)));
            border.Add(new BorderSection(0.0f + x, height / 2f + y, 0.0f + x, height - cornerRadius.leftBottom + y, cornerRadius.leftBottom + x, height - cornerRadius.leftBottom + y));

            //4
            //triangles.AddRange(RectToTri(new PointF(width / 2f, height / 2f), new PointF(width - cornerRadius.rightBottom, height)));
            border.Add(new BorderSection(width - cornerRadius.rightBottom + x, height + y, width / 2f + x, height + y, width / 2f + x, height / 2f + y));
            //triangles.AddRange(RectToTri(new PointF(width - cornerRadius.rightBottom, height / 2f), new PointF(width, height - cornerRadius.rightBottom)));
            border.Add(new BorderSection(width + x, height - cornerRadius.rightBottom + y, width + x, height / 2f + y, width - cornerRadius.rightBottom + x, height / 2f + y));


            //if (radius < 1)
            //    return triangles;

            List<float[]> tmpList = new List<float[]>();
            float x0, y0;

            if (cornerRadius.rightBottom >= 1)
            {
                x0 = width - cornerRadius.rightBottom + x;
                y0 = height - cornerRadius.rightBottom + y;
                tmpList.AddRange(CountCircleSector(0, 90, x0, y0, cornerRadius.rightBottom));
            }

            if (cornerRadius.rightTop >= 1)
            {
                x0 = width - cornerRadius.rightTop + x;
                y0 = cornerRadius.rightTop + y;
                tmpList.AddRange(CountCircleSector(270, 360, x0, y0, cornerRadius.rightTop));
            }

            if (cornerRadius.leftTop >= 1)
            {
                x0 = cornerRadius.leftTop + x;
                y0 = cornerRadius.leftTop + y;
                tmpList.AddRange(CountCircleSector(180, 270, x0, y0, cornerRadius.leftTop));
            }

            if (cornerRadius.leftBottom >= 1)
            {
                x0 = cornerRadius.leftBottom + x;
                y0 = height - cornerRadius.leftBottom + y;
                tmpList.AddRange(CountCircleSector(90, 180, x0, y0, cornerRadius.leftBottom));
            }

            for (int i = 0; i < tmpList.Count / 3; i++)
            {
                border.Add(new BorderSection(tmpList.ElementAt(i * 3 + 1)[0], tmpList.ElementAt(i * 3 + 1)[1], tmpList.ElementAt(i * 3 + 2)[0], tmpList.ElementAt(i * 3 + 2)[1], tmpList.ElementAt(i * 3)[0], tmpList.ElementAt(i * 3)[1]));
            }

            return MakeBorder(border, thickness);
        }

        private static List<float[]> MakeBorder(List<BorderSection> borders, float thickness)
        {
            List<float[]> borderTris = new List<float[]>();

            float bw = thickness; //border width
            float x3, y3, x4, y4;
            float x1, y1, x2, y2;
            for (int i = 0; i < borders.Count; i++)
            {
                x1 = borders.ElementAt(i).x1;
                x2 = borders.ElementAt(i).x2;
                y1 = borders.ElementAt(i).y1;
                y2 = borders.ElementAt(i).y2;

                x3 = x1 + borders.ElementAt(i).nx * bw;
                y3 = y1 + borders.ElementAt(i).ny * bw;
                x4 = x2 + borders.ElementAt(i).nx * bw;
                y4 = y2 + borders.ElementAt(i).ny * bw;

                borderTris.AddRange(WherePoint(x1, y1, x2, y2, x4, y4));
                borderTris.AddRange(WherePoint(x1, y1, x4, y4, x3, y3));
            }

            return borderTris;
        }

        private static List<float[]> WherePoint(float x1, float y1, float x2, float y2, float x3, float y3)
        {
            List<float[]> clockwise = new List<float[]>();
            float f = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
            if (f < 0)
            {
                clockwise.Add(new float[] { x1, y1, 0 });
                clockwise.Add(new float[] { x2, y2, 0 });
                clockwise.Add(new float[] { x3, y3, 0 });
            }
            else
            {
                clockwise.Add(new float[] { x2, y2, 0 });
                clockwise.Add(new float[] { x1, y1, 0 });
                clockwise.Add(new float[] { x3, y3, 0 });
            }
            return clockwise;
        }
    }
}
