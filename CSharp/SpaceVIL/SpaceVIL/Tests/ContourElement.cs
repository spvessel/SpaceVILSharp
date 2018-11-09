// using System;
// using System.Collections.Generic;
// using System.Linq;
// using System.Text;
// using System.Threading.Tasks;
// using System.Drawing;

// namespace SpaceVIL
// {
//     class ContourElement : VisualItem, IPixelDrawable
//     {
//         private static int count = 0;
//         private float[] _coords;
//         private float[] _colors;
//         private Color _color;
//         private List<float> _coordsList;
//         private List<float> _colorsList;

//         public ContourElement() {
//             SetItemName("ContourElement" + count);
//             count++;
//             SetBackground(Color.Transparent);
//         }

//         private List<List<PointF>> MakeContourFigure() {
//             List<List<PointF>> shapeList = new List<List<PointF>>();
            
//             List<PointF> tmpShape = new List<PointF>();
//             tmpShape.Add(new PointF(20f, 20f));
//             tmpShape.Add(new PointF(50f, 20f));
//             tmpShape.Add(new PointF(70f, 40f));
//             tmpShape.Add(new PointF(70f, 60f));
//             tmpShape.Add(new PointF(50f, 80f));
//             tmpShape.Add(new PointF(20f, 80f));
//             tmpShape.Add(new PointF(0f, 60f));
//             tmpShape.Add(new PointF(0f, 40f));
//             shapeList.Add(new List<PointF>(tmpShape));
            
//             tmpShape = new List<PointF>();
//             tmpShape.Add(new PointF(40f, 10f));
//             tmpShape.Add(new PointF(40f, 30f));
//             tmpShape.Add(new PointF(50f, 40f));
//             tmpShape.Add(new PointF(80f, 40f));
//             tmpShape.Add(new PointF(90f, 30f));
//             tmpShape.Add(new PointF(90f, 10f));
//             tmpShape.Add(new PointF(80f, 0f));
//             tmpShape.Add(new PointF(50f, 0f));
//             shapeList.Add(new List<PointF>(tmpShape));
            
//             tmpShape = new List<PointF>();
//             tmpShape.Add(new PointF(20f, 40f));
//             tmpShape.Add(new PointF(10f, 50f));
//             tmpShape.Add(new PointF(10f, 60f));
//             tmpShape.Add(new PointF(20f, 65f));
//             tmpShape.Add(new PointF(30f, 65f));
//             tmpShape.Add(new PointF(35f, 60f));
//             tmpShape.Add(new PointF(35f, 50f));
//             tmpShape.Add(new PointF(30f, 40f));
//             shapeList.Add(new List<PointF>(tmpShape));
            
//             tmpShape = new List<PointF>();
//             tmpShape.Add(new PointF(50f, 60f));
//             tmpShape.Add(new PointF(53f, 55f));
//             tmpShape.Add(new PointF(57f, 55f));
//             tmpShape.Add(new PointF(60f, 60f));
//             tmpShape.Add(new PointF(60f, 65f));
//             tmpShape.Add(new PointF(57f, 69f));
//             tmpShape.Add(new PointF(53f, 69f));
//             tmpShape.Add(new PointF(50f, 65f));
//             shapeList.Add(new List<PointF>(tmpShape));
            

//             return shapeList;
//         }

//         public float[] GetCoords()
//         {
//             _coordsList = new List<float>();
//             _colorsList = new List<float>();
            
//             //double[,] alph = ContourService.CrossContours(MakeContourFigure());
//             CrossOut crossOut = ContourService.CrossContours(MakeContourFigure());
//             double[,] alph = crossOut._arr;

//             for (int i = 0; i < alph.GetLength(0); i++)
//             {
//                 for (int j = 0; j < alph.GetLength(1); j++)
//                 {
//                     if (alph[i, j] > 0)
//                     {
//                         if (alph[i, j] != 0)
//                         {
//                             alph[i, j] = (alph[i, j] < 1) ? alph[i, j] + 0.15f : alph[i, j];
//                             alph[i, j] = (alph[i, j] > 1) ? 1 : alph[i, j];
//                         }

//                         _coordsList.Add(i + 0);
//                         _coordsList.Add(j + 0);
//                         _coordsList.Add(0);

//                         _colorsList.Add((float)alph[i, j]);
//                     }
//                 }
//             }

//             UpdateCoords(_coordsList);
//             return _coords;
//         }

//         public float[] GetColors()
//         {
//             UpdateColor();
//             return _colors;
//         }

//         private void UpdateCoords(List<float> coordList)
//         { //ToGl
//             _coords = new float[coordList.Count];
//             float f;
//             float x0 = GetX();
//             float y0 = GetY();
//             float windowH = GetHandler().GetHeight() / 2f;
//             float windowW = GetHandler().GetWidth() / 2f;

//             float xmin = Int32.MaxValue;
//             float xmax = Int32.MinValue;
//             float ymin = Int32.MaxValue;
//             float ymax = Int32.MinValue;

//             for (int i = 0; i < coordList.Count; i += 3)
//             {
//                 f = coordList[i];
//                 xmin = (xmin > f) ? f : xmin;
//                 xmax = (xmax < f) ? f : xmax;
//                 f += x0;
//                 f = f / windowW - 1.0f;
//                 _coords[i] = f;

//                 f = coordList[i + 1];
//                 ymin = (ymin > f) ? f : ymin;
//                 ymax = (ymax < f) ? f : ymax;
//                 f += y0;
//                 f = -(f / windowH - 1.0f);
//                 _coords[i + 1] = f;

//                 f = coordList[i + 2];
//                 _coords[i + 2] = f;
//             }
//         }

//         private void UpdateColor()
//         {
//             _colors = new float[(_colorsList.Count) * 4];

//             for (int i = 0; i < _colorsList.Count; i++)
//             {
//                 _colors[i * 4 + 0] = _color.R;
//                 _colors[i * 4 + 1] = _color.G;
//                 _colors[i * 4 + 2] = _color.B;
//                 _colors[i * 4 + 3] = _colorsList[i];
//             }
//         }

//         public override void SetBackground(Color color)
//         {
//             _color = color;
//         }
//     }
// }
