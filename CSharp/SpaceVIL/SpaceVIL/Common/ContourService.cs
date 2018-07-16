using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Drawing.Drawing2D;

namespace SpaceVIL
{
    static class ContourService
    {
        public static double[,] CrossContours(List<List<PointF>> innerList) {
            List<Contour> contoursList = new List<Contour>();
            for (int i = 0; i < innerList.Count; i++) {
                contoursList.Add(AnalizeCotour(innerList[i]));
            }

            return MakeCrossArray(contoursList);
        }

        public static double[,] CrossContours(GraphicsPath shape)
        {
            List<Contour> contoursList = new List<Contour>();

            GraphicsPathIterator myPathIterator = new GraphicsPathIterator(shape);
            // Rewind the iterator.
            myPathIterator.Rewind();
            // Create the GraphicsPath section.
            GraphicsPath myPathSection = new GraphicsPath();
            int subpathPoints;
            bool IsClosed2;

            for (int i = 0; i < myPathIterator.SubpathCount; i++)
            {
                subpathPoints = myPathIterator.NextSubpath(myPathSection, out IsClosed2);
                List<PointF> pathSection = new List<PointF>();
                for (int j = 0; j < myPathSection.PathPoints.Length; j++) {
                    pathSection.Add(new PointF(myPathSection.PathPoints[j].X, myPathSection.PathPoints[j].Y));
                }
                contoursList.Add(AnalizeCotour(pathSection));
            }

            return MakeCrossArray(contoursList);
        }

        internal static double[,] CrossContoursAntiAl(GraphicsPath shape)
        {
            List<Contour> contoursList = new List<Contour>();

            GraphicsPathIterator myPathIterator = new GraphicsPathIterator(shape);
            // Rewind the iterator.
            myPathIterator.Rewind();
            // Create the GraphicsPath section.
            GraphicsPath myPathSection = new GraphicsPath();
            int subpathPoints;
            bool IsClosed2;

            for (int i = 0; i < myPathIterator.SubpathCount; i++)
            {
                subpathPoints = myPathIterator.NextSubpath(myPathSection, out IsClosed2);
                List<PointF> pathSection = new List<PointF>();
                for (int j = 0; j < myPathSection.PathPoints.Length; j++)
                {
                    pathSection.Add(new PointF(myPathSection.PathPoints[j].X, myPathSection.PathPoints[j].Y));
                }
                contoursList.Add(AnalizeCotour(pathSection));
            }

            return MakeCrossArrayAntiAl(contoursList);
        }

        private static Contour AnalizeCotour(List<PointF> pathSection) {
            List<PointF> contour = new List<PointF>();
            double xx1, xx2, yy1, yy2;
            Dictionary<int, List<InOutCoord>> crossY = new Dictionary<int, List<InOutCoord>>();
            Dictionary<int, List<InOutCoord>> crossX = new Dictionary<int, List<InOutCoord>>();
            float xcurr = pathSection[pathSection.Count - 1].X;
            float ycurr = pathSection[pathSection.Count - 1].Y;
            float xprev, yprev, xnext, ynext;
            bool clockWise;
            for (int j = 0; j < pathSection.Count - 1; j++)
            {
                xprev = xcurr;
                yprev = ycurr;
                xcurr = pathSection[j].X;
                ycurr = pathSection[j].Y;
                xnext = pathSection[j + 1].X;
                ynext = pathSection[j + 1].Y;

                if (xprev == xcurr && xcurr == xnext) continue;

                if (yprev == ycurr && ycurr == ynext) continue;

                if ((Math.Truncate(xcurr) == xcurr) && (Math.Sign(xprev - xcurr) == Math.Sign(xnext - xcurr)))
                {
                    contour.Add(new PointF(xcurr - 0.1f, ycurr));
                    contour.Add(new PointF(xcurr + 0.1f, ycurr));
                    continue;
                }

                if ((Math.Truncate(ycurr) == ycurr) && (Math.Sign(yprev - ycurr) == Math.Sign(ynext - ycurr)))
                {
                    contour.Add(new PointF(xcurr, ycurr - 0.1f));
                    contour.Add(new PointF(xcurr, ycurr + 0.1f));
                    continue;
                }

                contour.Add(new PointF(xcurr, ycurr));
            }

            xprev = pathSection[pathSection.Count - 2].X; yprev = pathSection[pathSection.Count - 2].Y;
            xcurr = pathSection[pathSection.Count - 1].X; ycurr = pathSection[pathSection.Count - 1].Y;
            xnext = pathSection[0].X; ynext = pathSection[0].Y;

            if (!(xprev == xcurr && xcurr == xnext) && !(yprev == ycurr && ycurr == ynext))
            {
                if ((Math.Truncate(xcurr) == xcurr) && (Math.Sign(xprev - xcurr) == Math.Sign(xnext - xcurr)))
                {
                    contour.Add(new PointF(xcurr - 0.1f, ycurr));
                    contour.Add(new PointF(xcurr + 0.1f, ycurr));
                    //continue;
                }
                else if ((Math.Truncate(ycurr) == ycurr) && (Math.Sign(yprev - ycurr) == Math.Sign(ynext - ycurr)))
                {
                    contour.Add(new PointF(xcurr, ycurr - 0.1f));
                    contour.Add(new PointF(xcurr, ycurr + 0.1f));
                    //continue;
                }
                else contour.Add(new PointF(xcurr, ycurr));
            }

            contour.Add(contour[0]);
            clockWise = IsClockwise(contour);//Console.WriteLine("Contour check " + clockWise);

            for (int j = 0; j < contour.Count - 1; j++)
            {
                yy1 = contour[j].Y;
                yy2 = contour[j + 1].Y;
                xx1 = contour[j].X;
                xx2 = contour[j + 1].X;

                bool isIn;
                if (yy2 - yy1 < 0) isIn = clockWise; // true; //Долбаный перевернутый Y
                else isIn = !clockWise; //false;

                if (Math.Abs(Math.Truncate(yy2) - Math.Truncate(yy1)) >= 1)
                {
                    int ybeg, yend;

                    if (yy1 < yy2)
                    {
                        ybeg = (int)Math.Ceiling(yy1);
                        yend = (int)Math.Ceiling(yy2);
                    }
                    else
                    {
                        yend = (int)Math.Truncate(yy1) + 1;
                        ybeg = (int)Math.Truncate(yy2) + 1;
                    }

                    double xtmp;

                    for (int yinc = ybeg; yinc < yend; yinc++)
                    {
                        xtmp = (yinc - yy1) * (xx2 - xx1) / (yy2 - yy1) + xx1;

                        if (!crossY.ContainsKey(yinc)) crossY.Add(yinc, new List<InOutCoord>() { new InOutCoord(isIn, (float)xtmp, clockWise) });
                        else crossY[yinc].Add(new InOutCoord(isIn, (float)xtmp, clockWise));
                    }
                }
                else if (Math.Truncate(yy1) == yy1)
                {
                    int yinc = (int)yy1;
                    if (!crossY.ContainsKey(yinc)) crossY.Add(yinc, new List<InOutCoord>() { });

                    if (yy1 == yy2)
                    {
                        crossY[yinc].Add(new InOutCoord(true, (float)Math.Min(xx1, xx2), clockWise));
                        crossY[yinc].Add(new InOutCoord(false, (float)Math.Max(xx1, xx2), clockWise));
                    }
                    else crossY[yinc].Add(new InOutCoord(isIn, (float)xx1, clockWise));
                }

                if (xx2 - xx1 > 0) isIn = clockWise; //true;
                else isIn = !clockWise; //false;

                if (Math.Abs(Math.Truncate(xx2) - Math.Truncate(xx1)) >= 1)
                {

                    int xbeg, xend;

                    if (xx1 < xx2)
                    {
                        xbeg = (int)Math.Ceiling(xx1);
                        xend = (int)Math.Ceiling(xx2);
                    }
                    else
                    {
                        xend = (int)Math.Truncate(xx1) + 1;
                        xbeg = (int)Math.Truncate(xx2) + 1;
                    }

                    double ytmp;

                    for (int xinc = xbeg; xinc < xend; xinc++)
                    {
                        ytmp = (xinc - xx1) * (yy2 - yy1) / (xx2 - xx1) + yy1;

                        if (!crossX.ContainsKey(xinc)) crossX.Add(xinc, new List<InOutCoord>() { new InOutCoord(isIn, (float)ytmp, clockWise) });
                        else crossX[xinc].Add(new InOutCoord(isIn, (float)ytmp, clockWise));
                    }
                }
                else if (Math.Truncate(xx1) == xx1)
                {
                    int xinc = (int)xx1;
                    if (!crossX.ContainsKey(xinc)) crossX.Add(xinc, new List<InOutCoord>() { });

                    if (xx1 == xx2)
                    {
                        crossX[xinc].Add(new InOutCoord(true, (float)Math.Min(yy1, yy2), clockWise));
                        crossX[xinc].Add(new InOutCoord(false, (float)Math.Max(yy1, yy2), clockWise));
                    }
                    else crossX[xinc].Add(new InOutCoord(isIn, (float)yy1, clockWise));
                }
            }

            return new Contour(crossX, crossY, clockWise);
        }

        private static double[,] MakeCrossArray(List<Contour> contours) {
            Dictionary<int, List<InOutCoord>> _globalCrossX = new Dictionary<int, List<InOutCoord>>();
            Dictionary<int, List<InOutCoord>> _globalCrossY = new Dictionary<int, List<InOutCoord>>();

            int x0 = int.MaxValue;// = _globalCrossX.Keys.Min() - 1; //Это с запасом, но должно хватить
            int y0 = int.MaxValue;// = _globalCrossY.Keys.Min() - 1;
            int x1 = int.MinValue;// = _globalCrossX.Keys.Max() + 1;
            int y1 = int.MinValue;// = _globalCrossY.Keys.Max() + 1;

            for (int i = 0; i < contours.Count; i++) {
                foreach (int xkey in contours[i]._crossX.Keys) {
                    if (!_globalCrossX.ContainsKey(xkey)) _globalCrossX.Add(xkey, new List<InOutCoord>() { });
                    _globalCrossX[xkey].AddRange(contours[i]._crossX[xkey]);
                }

                foreach (int ykey in contours[i]._crossY.Keys) {
                    if(!_globalCrossY.ContainsKey(ykey)) _globalCrossY.Add(ykey, new List<InOutCoord>() { });
                    _globalCrossY[ykey].AddRange(contours[i]._crossY[ykey]);
                }

                x0 = (x0 > contours[i].minX) ? contours[i].minX : x0;
                x1 = (x1 < contours[i].maxX) ? contours[i].maxX : x1;
                y0 = (y0 > contours[i].minY) ? contours[i].minY : y0;
                y1 = (y1 < contours[i].maxY) ? contours[i].maxY : y1;
            }

            //Console.WriteLine(x0 + " " + x1 + " " + y0 + " " + y1);
            //Console.WriteLine("Old " + _globalCrossX.Keys.Min() + " " + _globalCrossX.Keys.Max() + " " + (_globalCrossY.Keys.Min()) + " " + (_globalCrossY.Keys.Max()));

            double[,] alph = new double[x1 - x0 + 1, y1 - y0 + 1];
            int isInside;
            int add;
            int incCoord;
            double diff;

            foreach (int ykey in _globalCrossY.Keys) {
                _globalCrossY[ykey].Sort((x, y) => x._coord.CompareTo(y._coord));
                isInside = 0;
                incCoord = (int)Math.Truncate(_globalCrossY[ykey][0]._coord);
                for (int i = 0; i < _globalCrossY[ykey].Count; i++) {
                    add = (_globalCrossY[ykey][i]._isIn == _globalCrossY[ykey][i]._clockwiae) ? 1 : -1;
                    //Console.Write(isInside + add + " ");
                    if (isInside != 0) //Был внутри
                    {
                        while (incCoord <= _globalCrossY[ykey][i]._coord)
                        {
                            alph[incCoord - x0, ykey - y0] = 1;
                            incCoord++;
                        }

                        isInside += add;
                        diff = incCoord - _globalCrossY[ykey][i]._coord;
                        if (isInside == 0 && diff > 0) //Стал снаружи
                        {
                            if (diff < 0.5) alph[incCoord - x0, ykey - y0] = (alph[incCoord - x0, ykey - y0] + (0.5 - diff)); // /2.0
                        }
                        
                    }
                    else {
                        
                        while (incCoord < _globalCrossY[ykey][i]._coord)
                        {
                            incCoord++;
                        }
                        
                        isInside += add;
                        diff = incCoord - _globalCrossY[ykey][i]._coord;
                        if (isInside != 0 && diff > 0) {
                            diff = 1 - diff;
                            if (diff < 0.5) alph[incCoord - 1 - x0, ykey - y0] = (alph[incCoord - 1 - x0, ykey - y0] + (0.5 - diff)); // /2.0
                        }
                    }
                }

                /*
                //Console.WriteLine(_globalCrossY[ykey].Count);
                if (ykey == 20) {
                    for (int i = 0; i < _globalCrossY[ykey].Count; i++) {
                        Console.Write("(" + _globalCrossY[ykey][i]._coord + " , " +
                            _globalCrossY[ykey][i]._isIn + ") ");
                    }
                    Console.WriteLine();
                }
                */
            }

            foreach (int xkey in _globalCrossX.Keys) {
                _globalCrossX[xkey].Sort((x, y) => x._coord.CompareTo(y._coord));
                isInside = 0;
                for (int i = 0; i < _globalCrossX[xkey].Count; i++) {
                    add = (_globalCrossX[xkey][i]._isIn == _globalCrossX[xkey][i]._clockwiae) ? 1 : -1;

                    if (isInside != 0 && isInside + add == 0) //Точка выхода
                    {
                        incCoord = (int)Math.Truncate(_globalCrossX[xkey][i]._coord) + 1;
                        diff = incCoord - _globalCrossX[xkey][i]._coord;
                        if (diff < 0.5 && diff > 0) alph[xkey - x0, incCoord - y0] = (alph[xkey - x0, incCoord - y0] + (0.5 - diff)); // /2.0
                    }
                    else if (isInside == 0 && isInside + add != 0) //Точка входа
                    {
                        incCoord = (int)Math.Round(_globalCrossX[xkey][i]._coord);
                        diff = Math.Abs(_globalCrossX[xkey][i]._coord - incCoord);
                        if (diff < 0.5 && diff > 0) alph[xkey - x0, incCoord - y0] = (alph[xkey - x0, incCoord - y0] + (0.5 - diff)); // /2.0
                    }

                    isInside += add;
                }
            }

            return alph;
        }


        private static double[,] MakeCrossArrayAntiAl(List<Contour> contours)
        {
            Dictionary<int, List<InOutCoord>> _globalCrossX = new Dictionary<int, List<InOutCoord>>();
            Dictionary<int, List<InOutCoord>> _globalCrossY = new Dictionary<int, List<InOutCoord>>();

            int x0 = int.MaxValue;// = _globalCrossX.Keys.Min() - 1; //Это с запасом, но должно хватить
            int y0 = int.MaxValue;// = _globalCrossY.Keys.Min() - 1;
            int x1 = int.MinValue;// = _globalCrossX.Keys.Max() + 1;
            int y1 = int.MinValue;// = _globalCrossY.Keys.Max() + 1;

            for (int i = 0; i < contours.Count; i++)
            {
                foreach (int xkey in contours[i]._crossX.Keys)
                {
                    if (!_globalCrossX.ContainsKey(xkey)) _globalCrossX.Add(xkey, new List<InOutCoord>() { });
                    _globalCrossX[xkey].AddRange(contours[i]._crossX[xkey]);
                }

                foreach (int ykey in contours[i]._crossY.Keys)
                {
                    if (!_globalCrossY.ContainsKey(ykey)) _globalCrossY.Add(ykey, new List<InOutCoord>() { });
                    _globalCrossY[ykey].AddRange(contours[i]._crossY[ykey]);
                }

                x0 = (x0 > contours[i].minX) ? contours[i].minX : x0;
                x1 = (x1 < contours[i].maxX) ? contours[i].maxX : x1;
                y0 = (y0 > contours[i].minY) ? contours[i].minY : y0;
                y1 = (y1 < contours[i].maxY) ? contours[i].maxY : y1;
            }

            double[,] alph = new double[x1 - x0 + 1, y1 - y0 + 1];
            int isInside;
            int add;
            int incCoord;
            double diff;

            foreach (int ykey in _globalCrossY.Keys)
            {
                _globalCrossY[ykey].Sort((x, y) => x._coord.CompareTo(y._coord));
                isInside = 0;
                incCoord = (int)Math.Truncate(_globalCrossY[ykey][0]._coord);
                for (int i = 0; i < _globalCrossY[ykey].Count; i++)
                {
                    add = (_globalCrossY[ykey][i]._isIn == _globalCrossY[ykey][i]._clockwiae) ? 1 : -1;
                    if (isInside != 0) //Был внутри
                    {
                        while (incCoord <= _globalCrossY[ykey][i]._coord)
                        {
                            alph[incCoord - x0, ykey - y0] = 1;
                            incCoord++;
                        }

                        isInside += add;
                        diff = incCoord - _globalCrossY[ykey][i]._coord;
                        if (isInside == 0 && diff > 0 && diff < 1) //Стал снаружи, т.е. точка выхода
                        {
                            if (alph[incCoord - x0, ykey - y0] > 0)
                            alph[incCoord - x0, ykey - y0] = 200 + Math.Round((1 - diff)*100);
                            //if (diff < 0.5) alph[incCoord - x0, ykey - y0] = (alph[incCoord - x0, ykey - y0] + (0.5 - diff)); // /2.0
                        }

                    }
                    else
                    {

                        while (incCoord < _globalCrossY[ykey][i]._coord)
                        {
                            incCoord++;
                        }

                        isInside += add;
                        diff = incCoord - _globalCrossY[ykey][i]._coord;
                        if (isInside != 0 && diff > 0 && diff < 1) //точка входа
                        {
                            alph[incCoord - 1 - x0, ykey - y0] = 100 + Math.Round(diff * 100);
                            //diff = 1 - diff;
                            //if (diff < 0.5) alph[incCoord - 1 - x0, ykey - y0] = (alph[incCoord - 1 - x0, ykey - y0] + (0.5 - diff)); // /2.0
                        }
                    }
                }
            }

            foreach (int xkey in _globalCrossX.Keys)
            {
                _globalCrossX[xkey].Sort((x, y) => x._coord.CompareTo(y._coord));
                isInside = 0;
                for (int i = 0; i < _globalCrossX[xkey].Count; i++)
                {
                    add = (_globalCrossX[xkey][i]._isIn == _globalCrossX[xkey][i]._clockwiae) ? 1 : -1;

                    if (isInside != 0 && isInside + add == 0) //Точка выхода
                    {
                        incCoord = (int)Math.Truncate(_globalCrossX[xkey][i]._coord) + 1;
                        if (alph[xkey - x0, incCoord - y0] < 100)
                        {
                            diff = incCoord - _globalCrossX[xkey][i]._coord;
                            if (diff < 0.5 && diff > 0) alph[xkey - x0, incCoord - y0] = (alph[xkey - x0, incCoord - y0] + (0.5 - diff)); // /2.0
                        }
                    }
                    else if (isInside == 0 && isInside + add != 0) //Точка входа
                    {
                        incCoord = (int)Math.Round(_globalCrossX[xkey][i]._coord);
                        if (alph[xkey - x0, incCoord - y0] < 100) { 
                            diff = Math.Abs(_globalCrossX[xkey][i]._coord - incCoord);
                            if (diff < 0.5 && diff > 0) alph[xkey - x0, incCoord - y0] = (alph[xkey - x0, incCoord - y0] + (0.5 - diff)); // /2.0
                        }
                    }

                    isInside += add;
                }
            }

            return alph;
        }


        private static bool IsClockwise(List<PointF> pointsList) {
            float sum = 0;
            float x1, x2, y1, y2;
            x1 = pointsList[0].X;
            y1 = pointsList[0].Y;
            for (int i = 0; i < pointsList.Count - 1; i++) {
                x2 = pointsList[i+1].X;
                y2 = pointsList[i+1].Y;

                sum += (x2 - x1) * (y1 + y2);
                x1 = x2;
                y1 = y2;
            }
            x2 = pointsList[0].X;
            y2 = pointsList[0].Y;

            sum += (x2 - x1) * (y1 + y2);

            if (sum == 0) Console.WriteLine("Something is wrong with clockwise");
            return (sum < 0);
        }

        private class Contour
        {
            public bool _clockwise;
            public Dictionary<int, List<InOutCoord>> _crossX;
            public Dictionary<int, List<InOutCoord>> _crossY;
            public int minX = int.MaxValue;
            public int maxX = int.MinValue;
            public int minY = int.MaxValue;
            public int maxY = int.MinValue;

            public Contour(Dictionary<int, List<InOutCoord>> crossX,
                Dictionary<int, List<InOutCoord>> crossY, bool clockwise) {
                _clockwise = clockwise;
                FormalizeCross(crossX, crossY);
            }

            private void FormalizeCross(Dictionary<int, List<InOutCoord>> crossX,
                Dictionary<int, List<InOutCoord>> crossY) {

                _crossX = new Dictionary<int, List<InOutCoord>>();
                _crossY = new Dictionary<int, List<InOutCoord>>();

                List<InOutCoord> tmpYList;
                List<InOutCoord> tmpList;

                foreach (int ykey in crossY.Keys)
                {
                    tmpList = new List<InOutCoord>();
                    tmpYList = new List<InOutCoord>();
                    tmpList.AddRange(crossY[ykey]);
                    //tmpList.Distinct();
                    tmpList.Sort((x, y) => x._coord.CompareTo(y._coord));
                    minX = (minX > (int)Math.Truncate(tmpList[0]._coord)) ? (int)Math.Truncate(tmpList[0]._coord) : minX;
                    maxX = (maxX < (int)Math.Truncate(tmpList[tmpList.Count - 1]._coord) + 1) ? (int)Math.Truncate(tmpList[tmpList.Count - 1]._coord) + 1 : maxX;

                    int inc = 0;
                    while (inc < tmpList.Count)
                    {
                        tmpYList.Add(tmpList[inc]); //(tmpList[inc]._isIn == true)
                        inc++;
                        while (inc < tmpList.Count && tmpList[inc]._isIn) inc++;

                        if (inc < tmpList.Count)
                        {
                            while (inc < tmpList.Count && tmpList[inc]._isIn == false) inc++;
                            tmpYList.Add(tmpList[inc - 1]);
                        }
                    }

                    _crossY.Add(ykey, new List<InOutCoord>(tmpYList));
                }

                List<InOutCoord> tmpXList;
                foreach (int xkey in crossX.Keys)
                {
                    tmpList = new List<InOutCoord>();
                    tmpXList = new List<InOutCoord>();
                    tmpList.AddRange(crossX[xkey]);
                    tmpList.Sort((x, y) => x._coord.CompareTo(y._coord));

                    minY = (minY > (int)Math.Truncate(tmpList[0]._coord)) ? (int)Math.Truncate(tmpList[0]._coord) : minY;
                    maxY = (maxY < (int)Math.Truncate(tmpList[tmpList.Count - 1]._coord) + 1) ? (int)Math.Truncate(tmpList[tmpList.Count - 1]._coord) + 1 : maxY;

                    int inc = 0;
                    while (inc < tmpList.Count)
                    {
                        tmpXList.Add(tmpList[inc]); //(tmpList[inc]._isIn == true)
                        inc++;
                        while (inc < tmpList.Count && tmpList[inc]._isIn) inc++;

                        if (inc < tmpList.Count)
                        {
                            while (inc < tmpList.Count && tmpList[inc]._isIn == false) inc++;
                            tmpXList.Add(tmpList[inc - 1]);
                        }
                    }

                    _crossX.Add(xkey, new List<InOutCoord>(tmpXList));
                }

                if (_crossY.Count != 0)
                {
                    minY = (minY > _crossY.Keys.Min() - 1) ? _crossY.Keys.Min() - 1 : minY;
                    maxY = (maxY < _crossY.Keys.Max() + 1) ? _crossY.Keys.Max() + 1 : maxY;
                }
                else
                {
                    if (_crossX.Count == 0)
                    {
                        minX = 0; maxX = 0; minY = 0; maxY = 0;
                    }
                }

                if (_crossX.Count != 0) {
                    minX = (minX > _crossX.Keys.Min() - 1) ? _crossX.Keys.Min() - 1 : minX; //Это с запасом, но должно хватить
                    maxX = (maxX < _crossX.Keys.Max() + 1) ? _crossX.Keys.Max() + 1 : maxX;
                }
            }

        }

        private class InOutCoord
        {
            public bool _isIn;
            public float _coord;
            public bool _clockwiae;

            public InOutCoord(bool isIn, float coord, bool clockwise)
            {
                _isIn = isIn;
                _coord = coord;
                _clockwiae = clockwise;
            }
        }
    }
}
