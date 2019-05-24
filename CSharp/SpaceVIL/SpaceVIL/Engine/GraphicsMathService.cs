using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    internal class BorderSection
    {
        internal float thickness = 0;
        internal float x1;
        internal float y1;
        internal float x2;
        internal float y2;
        internal float nx;
        internal float ny;

        internal BorderSection(float x1, float y1, float x2, float y2, float x3, float y3)
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
        }
    }

    /// <summary>
    /// Class with some functions for constructing custom figures
    /// </summary>
    public static class GraphicsMathService
    {
        /// <summary>
        /// Mix two or more colors
        /// </summary>
        public static Color MixColors(params Color[] m_colors)
        {
            if (m_colors.Length == 0)
                return Color.White;
            if (m_colors.Length == 1)
                return m_colors[0];

            float r = 0, g = 0, b = 0, a = 0.0f;

            foreach (Color item in m_colors)
            {
                if (item == null || item.A == 0)
                    continue;

                if (item.A == 255) //exchange
                {
                    r = item.R;
                    g = item.G;
                    b = item.B;
                    a = 255.0f;
                }
                else //mixing
                {
                    float alpha = item.A / 255.0f;
                    float notAlpha = 1.0f - alpha;
                    if (r == 0.0f)
                        r = item.R;
                    else
                        r = r * notAlpha + item.R * alpha;

                    if (g == 0.0f)
                        g = item.G;
                    else
                        g = g * notAlpha + item.G * alpha;

                    if (b == 0.0f)
                        b = item.B;
                    else
                        b = b * notAlpha + item.B * alpha;

                    if (a == 0.0f)
                        a = item.A;
                    else if (a < 255.0f)
                        a = a * notAlpha + item.A * alpha;
                }
            }
            return Color.FromArgb((int)a, (int)r, (int)g, (int)b);
        }

        public static Color CloneColor(Color color)
        {
            return Color.FromArgb(color.A, color.R, color.G, color.B);
        }

        /// <summary>
        /// Make rectangle as two triangles by its width, height and top left corner position (x, y)
        /// </summary>
        static public List<float[]> GetRectangle(float w = 100, float h = 100, float x = 0, float y = 0)
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

        /// <summary>
        /// Make a rectangle with roundness corners
        /// </summary>
        /// <param name="cornerRadius"> radius values for all corners </param>
        /// <param name="width"> rectangle width (default = 100) </param>
        /// <param name="height"> rectangle height (default = 100) </param>
        /// <param name="x"> X position (left top corner) of the result object (default = 0) </param>
        /// <param name="y"> Y position (left top corner) of the result object (default = 0) </param>
        /// <returns> Points list of the rectangle with roundness corners </returns>
        static public List<float[]> GetRoundSquare(CornerRadius cornerRadius, float width = 100, float height = 100, int x = 0, int y = 0)
        {
            if (width <= 0 || height <= 0)
                return null;

            if (cornerRadius == null)
                cornerRadius = new CornerRadius();
            else
            {
                if (cornerRadius.LeftTop < 0)
                    cornerRadius.LeftTop = 0;
                if (cornerRadius.RightTop < 0)
                    cornerRadius.RightTop = 0;
                if (cornerRadius.LeftBottom < 0)
                    cornerRadius.LeftBottom = 0;
                if (cornerRadius.RightBottom < 0)
                    cornerRadius.RightBottom = 0;
            }


            List<float[]> triangles = new List<float[]>();
            //Начало координат в левом углу

            //1

            triangles.AddRange(RectToTri(new PointF(cornerRadius.LeftTop + x, 0.0f + y), new PointF(width / 2f + x, height / 2f + y)));
            triangles.AddRange(RectToTri(new PointF(0.0f + x, cornerRadius.LeftTop + y), new PointF(cornerRadius.LeftTop + x, height / 2f + y)));

            //2
            triangles.AddRange(RectToTri(new PointF(width / 2f + x, 0.0f + y), new PointF(width - cornerRadius.RightTop + x, height / 2f + y)));
            triangles.AddRange(RectToTri(new PointF(width - cornerRadius.RightTop + x, cornerRadius.RightTop + y), new PointF(width + x, height / 2f + y)));

            //3
            triangles.AddRange(RectToTri(new PointF(cornerRadius.LeftBottom + x, height / 2f + y), new PointF(width / 2f + x, height + y)));
            triangles.AddRange(RectToTri(new PointF(0.0f + x, height / 2f + y), new PointF(cornerRadius.LeftBottom + x, height - cornerRadius.LeftBottom + y)));

            //4
            triangles.AddRange(RectToTri(new PointF(width / 2f + x, height / 2f + y), new PointF(width - cornerRadius.RightBottom + x, height + y)));
            triangles.AddRange(RectToTri(new PointF(width - cornerRadius.RightBottom + x, height / 2f + y), new PointF(width + x, height - cornerRadius.RightBottom + y)));

            float x0, y0;
            int quadrantInd;

            if (cornerRadius.RightBottom >= 1)
            {
                x0 = width - cornerRadius.RightBottom + x;
                y0 = height - cornerRadius.RightBottom + y;
                quadrantInd = 1;
                triangles.AddRange(CountCircleSector(quadrantInd, x0, y0, cornerRadius.RightBottom));
            }

            if (cornerRadius.RightTop >= 1)
            {
                x0 = width - cornerRadius.RightTop + x;
                y0 = cornerRadius.RightTop + y;
                quadrantInd = 4;
                triangles.AddRange(CountCircleSector(quadrantInd, x0, y0, cornerRadius.RightTop));
            }

            if (cornerRadius.LeftTop >= 1)
            {
                x0 = cornerRadius.LeftTop + x;
                y0 = cornerRadius.LeftTop + y;
                quadrantInd = 3;
                triangles.AddRange(CountCircleSector(quadrantInd, x0, y0, cornerRadius.LeftTop));
            }

            if (cornerRadius.LeftBottom >= 1)
            {
                x0 = cornerRadius.LeftBottom + x;
                y0 = height - cornerRadius.LeftBottom + y;
                quadrantInd = 2;
                triangles.AddRange(CountCircleSector(quadrantInd, x0, y0, cornerRadius.LeftBottom));
            }

            return triangles;
        }

        private static List<float[]> RectToTri(PointF leftTop, PointF rightBottom)
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

        /// <summary>
        /// Make a rectangle with roundness corners
        /// </summary>
        /// <param name="width"> rectangle width (default = 100) </param>
        /// <param name="height"> rectangle height (default = 100) </param>
        /// <param name="radius"> same radius value for each corner (default = 0) </param>
        /// <param name="x"> X position (left top corner) of the result object (default = 0) </param>
        /// <param name="y"> Y position (left top corner) of the result object (default = 0) </param>
        /// <returns> Points list of the rectangle with roundness corners </returns>
        static public List<float[]> GetRoundSquare(float width = 100, float height = 100, float radius = 0.0f, int x = 0, int y = 0)
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
            int quadrantInd;
            x0 = width - radius + x;
            y0 = height - radius + y;
            quadrantInd = 1;
            triangles.AddRange(CountCircleSector(quadrantInd, x0, y0, radius)); //7

            x0 = width - radius + x;
            y0 = radius + y;
            quadrantInd = 4;
            triangles.AddRange(CountCircleSector(quadrantInd, x0, y0, radius)); //8

            x0 = radius + x;
            y0 = radius + y;
            quadrantInd = 3;
            triangles.AddRange(CountCircleSector(quadrantInd, x0, y0, radius)); //9

            x0 = radius + x;
            y0 = height - radius + y;
            quadrantInd = 2;
            triangles.AddRange(CountCircleSector(quadrantInd, x0, y0, radius)); //10

            return triangles;
        }


        static private List<float[]> CountCircleSector(int quadrantInd, float x0, float y0, float radius)
        {
            int[,] quadrantAngles = new int[4, 2] { {0, 90}, {90, 180}, {180, 270}, {270, 360} };

            int firstAngle = quadrantAngles[quadrantInd - 1, 0];
            int lastAngle = quadrantAngles[quadrantInd - 1, 1];

            List<float[]> circleSect = new List<float[]>();
            float x1, y1, x2, y2;
            x1 = radius * (float)CosGrad(firstAngle) + x0;
            y1 = radius * (float)SinGrad(firstAngle) + y0;

            for (int alf = firstAngle + 1; alf <= lastAngle; alf += 5)
            { //Шаг можно сделать больше 1 градуса, нужны тестирования
                x2 = radius * (float)CosGrad(alf) + x0;
                y2 = radius * (float)SinGrad(alf) + y0;
                circleSect.Add(new float[] { x0, y0, 0.0f });
                circleSect.Add(new float[] { x2, y2, 0.0f });
                circleSect.Add(new float[] { x1, y1, 0.0f }); //против часовой стрелки
                x1 = x2;
                y1 = y2;
            }
            x2 = radius * (float)CosGrad(lastAngle) + x0;
            y2 = radius * (float)SinGrad(lastAngle) + y0;
            circleSect.Add(new float[] { x0, y0, 0.0f });
            circleSect.Add(new float[] { x2, y2, 0.0f });
            circleSect.Add(new float[] { x1, y1, 0.0f });

            return circleSect;
        }
        static internal List<float[]> ToGL(IBaseItem item, CoreWindow handler) //where TLayout : VisualItem
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
        static internal List<float[]> ToGL(List<float[]> triangles, CoreWindow handler) //where TLayout : VisualItem
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

        /// <summary>
        /// Make a star figure
        /// </summary>
        /// <param name="R"> Circumscribed circle radius </param>
        /// <param name="r"> Incircle radius </param>
        /// <param name="n"> vertices count </param>
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
                    (float)(x_center + r / 2 * CosGrad(alpha)),
                    (float)(y_center - r / 2 * SinGrad(alpha)),
                    0.0f
                });

                alpha = alpha + 360.0f / n;

                triangles.Add(new float[]
                {
                    (float)(x_center + r / 2 * CosGrad(alpha)),
                    (float)(y_center - r / 2 * SinGrad(alpha)),
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
                        (float)(x_center + r / 2 * CosGrad(alpha)),
                        (float)(y_center - r / 2 * SinGrad(alpha)),
                        0.0f
                    });
                    if (count % 3 == 0)
                    {
                        triangles.Add(new float[]
                        {
                            (float)(x_center + r / 2 * CosGrad(alpha)),
                            (float)(y_center - r / 2 * SinGrad(alpha)),
                            0.0f
                        });
                        count = 1;
                    }
                }
                else //При невыполнении условия четности следующие формулы
                {
                    triangles.Add(new float[]
                    {
                        (float)(x_center + R / 2 * CosGrad(alpha)),
                        (float)(y_center - R / 2 * SinGrad(alpha)),
                        0.0f
                    });
                    if (count % 3 == 0)
                    {
                        triangles.Add(new float[]
                        {
                            (float)(x_center + R / 2 * CosGrad(alpha)),
                            (float)(y_center - R / 2 * SinGrad(alpha)),
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

        /// <summary>
        /// Make a regular polygon
        /// </summary>
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
                    (float)(x_center + r / 2 * CosGrad(alpha)),
                    (float)(y_center - r / 2 * SinGrad(alpha)),
                    0.0f
                });

                alpha = alpha + 360.0f / n;

                triangles.Add(new float[]
                {
                    (float)(x_center + r / 2 * CosGrad(alpha)),
                    (float)(y_center - r / 2 * SinGrad(alpha)),
                    0.0f
                });
            }

            return triangles;
        }

        /// <summary>
        /// Make an ellipse with two equal radii (i. e. circle)
        /// </summary>
        /// <param name="n"> points count on the ellipse border (default = 32) </param>
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
                    (float)(x_center + r * CosGrad(alpha)),
                    (float)(y_center - r * SinGrad(alpha)),
                    0.0f
                });

                alpha = alpha + 360.0f / n;

                triangles.Add(new float[]
                {
                    (float)(x_center + r * CosGrad(alpha)),
                    (float)(y_center - r * SinGrad(alpha)),
                    0.0f
                });
            }

            return triangles;
        }

        /// <summary>
        /// Make an ellipse
        /// </summary>
        /// <param name="w"> ellipse width </param>
        /// <param name="h"> ellipse height </param>
        /// <param name="x"> X position of the left top corner (ellipse center in x + w/2) (default = 0) </param>
        /// <param name="y"> Y position of the left top corner (ellipse center in y + h/2) (default = 0) </param>
        /// <param name="n"> points count on the ellipse border (default = 32) </param>
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
                    (float)(x_center + rX * CosGrad(alpha)),
                    (float)(y_center - rY * SinGrad(alpha)),
                    0.0f
                });

                alpha = alpha + 360.0f / n;

                triangles.Add(new float[]
                {
                    (float)(x_center + rX * CosGrad(alpha)),
                    (float)(y_center - rY * SinGrad(alpha)),
                    0.0f
                });
            }

            return triangles;
        }

        /// <summary>
        /// Make a triangle with corners in (x + w/2, y), (x, y + h), (x + w, y + h), rotated on angle degrees
        /// </summary>
        /// <param name="angle"> rotation angle for the triangle in degrees (default = 0) </param>
        static public List<float[]> GetTriangle(float w = 100, float h = 100, int x = 0, int y = 0, int angle = 0)
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
                crd[0] = x0 + (x_crd - x0) * (float)CosGrad(angle) - (y_crd - y0) * (float)SinGrad(angle);
                crd[1] = y0 + (y_crd - y0) * (float)CosGrad(angle) + (x_crd - x0) * (float)SinGrad(angle);
            }
            return figure;
        }

        internal static List<float[]> GetLine(float lenght, float thichness, int alpha)
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
                crd[0] = x0 + (x_crd - x0) * (float)CosGrad(alpha) - (y_crd - y0) * (float)SinGrad(alpha);
                crd[1] = y0 + (y_crd - y0) * (float)CosGrad(alpha) + (x_crd - x0) * (float)SinGrad(alpha);
            }

            return figure;
        }

        /// <summary>
        /// Make cross figure
        /// </summary>
        /// <param name="w"> cross width </param>
        /// <param name="h"> cross height </param>
        /// <param name="thickness"> cross parts thickness </param>
        /// <param name="alpha"> cross rotation angle in degrees </param>
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
                crd[0] = x0 + (x_crd - x0) * (float)CosGrad(alpha) - (y_crd - y0) * (float)SinGrad(alpha);
                crd[1] = y0 + (y_crd - y0) * (float)CosGrad(alpha) + (x_crd - x0) * (float)SinGrad(alpha);
            }
            return figure;
        }

        static public List<float[]> RotateShape(float w, float h, float angle, List<float[]> triangles)
        {
            if (triangles == null)
                return null;
            //rotate
            float x0 = w / 2.0f;
            float y0 = h / 2.0f;
            foreach (var crd in triangles)
            {
                float x_crd = crd[0];
                float y_crd = crd[1];
                crd[0] = x0 + (x_crd - x0) * (float)CosGrad(angle) - (y_crd - y0) * (float)SinGrad(angle);
                crd[1] = y0 + (y_crd - y0) * (float)CosGrad(angle) + (x_crd - x0) * (float)SinGrad(angle);
            }
            return triangles;
        }
        static public List<float[]> GetArrow(float w, float h, float thickness, int alpha)
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

            //left
            figure.Add(new float[] { 0, y0, 0.0f });
            figure.Add(new float[] { 0, y0 + thickness, 0.0f });
            figure.Add(new float[] { x0, y0 + thickness, 0.0f });

            figure.Add(new float[] { x0, y0 + thickness, 0.0f });
            figure.Add(new float[] { x0, y0, 0.0f });
            figure.Add(new float[] { 0, y0, 0.0f });

            //rotate
            x0 = w / 2;
            y0 = h / 2;
            foreach (var crd in figure)
            {
                float x_crd = crd[0];
                float y_crd = crd[1];
                crd[0] = x0 + (x_crd - x0) * (float)CosGrad(alpha) - (y_crd - y0) * (float)SinGrad(alpha);
                crd[1] = y0 + (y_crd - y0) * (float)CosGrad(alpha) + (x_crd - x0) * (float)SinGrad(alpha);
            }
            return figure;
        }

        internal static List<float> GetRectBorder(int w, int h)
        {
            return GetRectBorderIgnoreTop(w, h, 0, 0);
        }

        internal static List<float> GetRectBorderIgnoreTop(int w, int h, int ignoreFrom, int ignoreWidth)
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
                if (dir == LineDir.Horiz)
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

        /// <summary>
        /// Move shape by X or/and Y direction
        /// </summary>
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

        /// <summary>
        /// Make folder icon shape as three rectangles
        /// </summary>
        public static List<float[]> GetFolderIconShape(float w = 20.0f, float h = 15.0f, float x = 0, float y = 0)
        {
            List<float[]> triangles = new List<float[]>();
            triangles.AddRange(GraphicsMathService.GetRectangle(w / 3, h));
            triangles.AddRange(GraphicsMathService.GetRectangle(2 * w / 3, h - h / 4, w / 3, h / 4));
            triangles.AddRange(GraphicsMathService.GetRectangle(w / 4, h / 8, w / 3 + 2));
            return triangles;
        }

        /// <summary>
        /// Make a rectangle border with roundness corners
        /// </summary>
        /// <param name="width"> rectangle border width </param>
        /// <param name="height"> rectangle border height </param>
        /// <param name="radius"> same radius value for each corner </param>
        /// <param name="thickness"> border thickness </param>
        /// <param name="x"> X position (left top corner) of the result object </param>
        /// <param name="y"> Y position (left top corner) of the result object </param>
        /// <returns> Points list of the rectangle border with roundness corners </returns>
        public static List<float[]> GetRoundSquareBorder(float width, float height, float radius, float thickness, int x, int y)
        {
            if (radius < 0)
                radius = 0;

            List<BorderSection> border = new List<BorderSection>();
            // Начало координат в углу

            border.Add(new BorderSection(width - radius + x, y, radius + x, y, width / 2f + x, height + 1 + y)); //radius + x, height + y));
            //triangles.add(new float[] { radius + x, height + y, 0.0f });
            //    triangles.add(new float[] { width - radius + x, y, 0.0f });
            //    triangles.add(new float[] { radius + x, y, 0.0f });

            border.Add(new BorderSection(width - radius + x, height + y, radius + x, height + y, width / 2f + x, y - 1)); //width - radius + x, y));
            //    triangles.add(new float[] { radius + x, height + y, 0.0f });
            //    triangles.add(new float[] { width - radius + x, height + y, 0.0f });
            //triangles.add(new float[] { width - radius + x, y, 0.0f });

            border.Add(new BorderSection(width + x, height - radius + y, width + x, radius + y, x - 1, height / 2f + y)); //width - radius + x, height - radius + y));
            //triangles.add(new float[] { width - radius + x, height - radius + y, 0.0f });
            //    triangles.add(new float[] { width + x, height - radius + y, 0.0f });
            //    triangles.add(new float[] { width + x, radius + y, 0.0f });

            border.Add(new BorderSection(x, height - radius + y, x, radius + y, width + 1 + x, height / 2f + y)); //radius + x, radius + y));
            //    triangles.add(new float[] { x, height - radius + y, 0.0f });
            //triangles.add(new float[] { radius + x, radius + y, 0.0f });
            //    triangles.add(new float[] { x, radius + y, 0.0f });

            if (radius < 1)
                return MakeBorder(border, thickness);

            List<float[]> tmpList;
            float x0, y0;
            int quadrantInd;
            x0 = width - radius + x;
            y0 = height - radius + y;
            quadrantInd = 1;
            tmpList = CountCircleSector(quadrantInd, x0, y0, radius);

            x0 = width - radius + x;
            y0 = radius + y;
            quadrantInd = 4;
            tmpList.AddRange(CountCircleSector(quadrantInd, x0, y0, radius));

            x0 = radius + x;
            y0 = radius + y;
            quadrantInd = 3;
            tmpList.AddRange(CountCircleSector(quadrantInd, x0, y0, radius));

            x0 = radius + x;
            y0 = height - radius + y;
            quadrantInd = 2;
            tmpList.AddRange(CountCircleSector(quadrantInd, x0, y0, radius));

            for (int i = 0; i < tmpList.Count / 3; i++)
            {
                border.Add(new BorderSection(tmpList.ElementAt(i * 3 + 1)[0], tmpList.ElementAt(i * 3 + 1)[1], tmpList.ElementAt(i * 3 + 2)[0], tmpList.ElementAt(i * 3 + 2)[1], tmpList.ElementAt(i * 3)[0], tmpList.ElementAt(i * 3)[1]));
            }

            return MakeBorder(border, thickness);
        }

        /// <summary>
        /// Make a rectangle border with roundness corners
        /// </summary>
        /// <param name="cornerRadius"> radius values for all corners </param>
        /// <param name="width"> rectangle border width </param>
        /// <param name="height"> rectangle border height </param>
        /// <param name="thickness"> border thickness </param>
        /// <param name="x"> X position (left top corner) of the result object </param>
        /// <param name="y"> Y position (left top corner) of the result object </param>
        /// <returns> Points list of the rectangle border with roundness corners </returns>
        static public List<float[]> GetRoundSquareBorder(CornerRadius cornerRadius, float width, float height, float thickness, int x, int y)
        {
            if (width <= 0 || height <= 0)
                return null;

            if (cornerRadius == null)
                cornerRadius = new CornerRadius();
            else
            {
                if (cornerRadius.LeftTop < 0)
                    cornerRadius.LeftTop = 0;
                if (cornerRadius.RightTop < 0)
                    cornerRadius.RightTop = 0;
                if (cornerRadius.LeftBottom < 0)
                    cornerRadius.LeftBottom = 0;
                if (cornerRadius.RightBottom < 0)
                    cornerRadius.RightBottom = 0;
            }

            List<BorderSection> border = new List<BorderSection>();
            //Начало координат в левом углу

            //1
            border.Add(new BorderSection(cornerRadius.LeftTop + x, 0.0f + y, width / 2f + x, 0.0f + y, width / 2f + x, height + y + 1));
            border.Add(new BorderSection(0.0f + x, cornerRadius.LeftTop + y, 0.0f + x, height / 2f + y, width + 1 + x, height / 2f + y));

            //2
            border.Add(new BorderSection(width / 2f + x, 0.0f + y, width - cornerRadius.RightTop + x, 0.0f + y, width / 2f + x, height + y + 1));
            border.Add(new BorderSection(width + x, height / 2f + y, width + x, cornerRadius.RightTop + y, x - 1, height / 2f + y));

            //3
            border.Add(new BorderSection(width / 2f + x, height + y, cornerRadius.LeftBottom + x, height + y, width / 2f, y - 1));
            border.Add(new BorderSection(0.0f + x, height / 2f + y, 0.0f + x, height - cornerRadius.LeftBottom + y, width + 1 + x, height / 2f + y));

            //4
            border.Add(new BorderSection(width - cornerRadius.RightBottom + x, height + y, width / 2f + x, height + y, width / 2f, y - 1));
            border.Add(new BorderSection(width + x, height - cornerRadius.RightBottom + y, width + x, height / 2f + y, x - 1, height / 2f + y));

            List<float[]> tmpList = new List<float[]>();
            float x0, y0;
            int quadrantInd;

            if (cornerRadius.RightBottom >= 1)
            {
                x0 = width - cornerRadius.RightBottom + x;
                y0 = height - cornerRadius.RightBottom + y;
                quadrantInd = 1;
                tmpList.AddRange(CountCircleSector(quadrantInd, x0, y0, cornerRadius.RightBottom));
            }

            if (cornerRadius.RightTop >= 1)
            {
                x0 = width - cornerRadius.RightTop + x;
                y0 = cornerRadius.RightTop + y;
                quadrantInd = 4;
                tmpList.AddRange(CountCircleSector(quadrantInd, x0, y0, cornerRadius.RightTop));
            }

            if (cornerRadius.LeftTop >= 1)
            {
                x0 = cornerRadius.LeftTop + x;
                y0 = cornerRadius.LeftTop + y;
                quadrantInd = 3;
                tmpList.AddRange(CountCircleSector(quadrantInd, x0, y0, cornerRadius.LeftTop));
            }

            if (cornerRadius.LeftBottom >= 1)
            {
                x0 = cornerRadius.LeftBottom + x;
                y0 = height - cornerRadius.LeftBottom + y;
                quadrantInd = 2;
                tmpList.AddRange(CountCircleSector(quadrantInd, x0, y0, cornerRadius.LeftBottom));
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

        public static Bitmap ScaleBitmap(Bitmap img, int w, int h)
        {
            float boundW = w;
            float boundH = h;

            var ratioX = (boundW / img.Width);
            var ratioY = (boundH / img.Height);
            float ratio = ratioX < ratioY ? ratioX : ratioY;

            int resW = (int)(img.Width * ratio);
            int resH = (int)(img.Height * ratio);

            var bmp = new Bitmap(resW, resH);
            var graphic = Graphics.FromImage(bmp);
            graphic.DrawImage(img, new System.Drawing.Rectangle(0, 0, resW, resH));
            graphic.Dispose();

            // Console.WriteLine(resW + " " + resH);

            return bmp;
        }

        public static Color ColorTransform(int r, int g, int b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            return Color.FromArgb(255, r, g, b);
        }

        public static Color ColorTransform(int r, int g, int b, int a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            return Color.FromArgb(a, r, g, b);
        }

        public static Color ColorTransform(float r, float g, float b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            return Color.FromArgb(255, (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f));
        }

        public static Color ColorTransform(float r, float g, float b, float a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            return Color.FromArgb((int)(a * 255.0f), (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f));
        }

        public static Font ChangeFontSize(int size, Font oldFont)
        {
            return new Font(oldFont.FontFamily, size, oldFont.Style);
        }

        public static Font ChangeFontStyle(FontStyle style, Font oldFont)
        {
            return new Font(oldFont.FontFamily, oldFont.Size, style);
        }

        public static Font ChangeFontFamily(FontFamily fontFamily, Font oldFont)
        {
            return new Font(fontFamily, oldFont.Size, oldFont.Style);
        }

        private static double Grad2Radian(double angleGrad) {
            return (angleGrad * Math.PI / 180.0f);
        }

        private static double CosGrad(double angleGrad) {
            return Math.Cos(Grad2Radian(angleGrad));
        }

        private static double SinGrad(double angleGrad) {
            return Math.Sin(Grad2Radian(angleGrad));
        }
    }
}
