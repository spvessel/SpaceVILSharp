using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;

namespace SpaceVIL
{
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
        static internal List<float[]> GetRectangle(int w = 100, int h = 100, int x = 0, int y = 0)
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
        static internal List<float[]> GetRoundSquare(float width = 100, float height = 100, float radius = 0.0f, int x = 0, int y = 0)
        {
            if (radius < 0)
                radius = 0;

            List<float[]> triangles = new List<float[]>();
            //Начало координат предполагается посередине прямоугольника.
            //Если хочется сделать в левом нижнем углу, то ко всем иксовым координатам нужно прибавить width/2,
            //а ко всем игрековым прибавить height/2

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
        static internal List<float[]> ToGL(BaseItem item, WindowLayout handler) //where TLayout : VisualItem
        {
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
        static public List<float[]> GetStar(float R = 100, float r = 50, int n = 6)
        {
            float x_center = R;
            float y_center = R;

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
        static public List<float[]> GetEllipse(float r = 100)
        {
            int n = 180;
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
        static public List<float[]> GetEllipse(float w, float h, int x = 0, int y = 0)
        {
            int n = 180;
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
    }
}
