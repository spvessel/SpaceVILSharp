﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Drawing.Drawing2D;

namespace SpaceVIL.Tests
{
    static class FontReview
    {
        internal static PixMapData getTextArrays(string text, Font font) {
            GraphicsPath shape = new GraphicsPath();
            StringFormat format = StringFormat.GenericDefault;
            shape.AddString(text, font.FontFamily, (int)font.Style, font.Size, new PointF(0f, 0f), format);

            return likeFontEngine(shape);
        }

        internal static int[] getDims() {
            return new int[] { 3, minY, maxY };
        }

        static int minY;
        static int maxY;

        private static PixMapData likeFontEngine(GraphicsPath shape) {
            List<float> pix = new List<float>();
            List<float> col = new List<float>();

            RectangleF rec = shape.GetBounds();
            int x0 = (int)Math.Floor(rec.Left);
            int x1 = (int)Math.Ceiling(rec.Right);
            int y0 = (int)Math.Floor(rec.Top);
            int y1 = (int)Math.Ceiling(rec.Bottom);

            minY = y0;
            maxY = y1;

            GraphicsPathIterator myPathIterator = new GraphicsPathIterator(shape);
            // Rewind the iterator.
            myPathIterator.Rewind();
            // Create the GraphicsPath section.
            GraphicsPath myPathSection = new GraphicsPath();
            int subpathPoints;
            bool IsClosed2;

            double[,] alph = new double[x1 - x0 + 1, y1 - y0 + 1];

            Dictionary<int, List<double>> crossY = new Dictionary<int, List<double>>();
            Dictionary<int, List<double>> crossX = new Dictionary<int, List<double>>();

            double xx1, xx2, yy1, yy2;

            for (int i = 0; i < myPathIterator.SubpathCount; i++)
            {
                subpathPoints = myPathIterator.NextSubpath(myPathSection, out IsClosed2);

                List<PointF> subpathList = new List<PointF>();

                float xcurr = myPathSection.PathPoints[myPathSection.PathPoints.Length - 1].X;
                float ycurr = myPathSection.PathPoints[myPathSection.PathPoints.Length - 1].Y;
                float xprev, yprev, xnext, ynext;
                for (int j = 0; j < myPathSection.PathPoints.Length - 1; j++) {
                    xprev = xcurr;
                    yprev = ycurr;
                    xcurr = myPathSection.PathPoints[j].X;
                    ycurr = myPathSection.PathPoints[j].Y;
                    xnext = myPathSection.PathPoints[j + 1].X;
                    ynext = myPathSection.PathPoints[j + 1].Y;

                    if (xprev == xcurr && xcurr == xnext) continue;

                    if (yprev == ycurr && ycurr == ynext) continue;

                    if ((Math.Truncate(xcurr) == xcurr) && (Math.Sign(xprev - xcurr) == Math.Sign(xnext - xcurr))) {
                        subpathList.Add(new PointF(xcurr - 0.1f, ycurr));
                        subpathList.Add(new PointF(xcurr + 0.1f, ycurr));
                        continue;
                    }

                    if ((Math.Truncate(ycurr) == ycurr) && (Math.Sign(yprev - ycurr) == Math.Sign(ynext - ycurr)))
                    {
                        subpathList.Add(new PointF(xcurr, ycurr - 0.1f));
                        subpathList.Add(new PointF(xcurr, ycurr + 0.1f));
                        continue;
                    }

                    subpathList.Add(new PointF(xcurr, ycurr));
                }

                xprev = myPathSection.PathPoints[myPathSection.PathPoints.Length - 2].X; yprev = myPathSection.PathPoints[myPathSection.PathPoints.Length - 2].Y;
                xcurr = myPathSection.PathPoints[myPathSection.PathPoints.Length - 1].X; ycurr = myPathSection.PathPoints[myPathSection.PathPoints.Length - 1].Y;
                xnext = myPathSection.PathPoints[0].X; ynext = myPathSection.PathPoints[0].Y;

                if (!(xprev == xcurr && xcurr == xnext) && !(yprev == ycurr && ycurr == ynext)) {
                    if ((Math.Truncate(xcurr) == xcurr) && (Math.Sign(xprev - xcurr) == Math.Sign(xnext - xcurr)))
                    {
                        subpathList.Add(new PointF(xcurr - 0.1f, ycurr));
                        subpathList.Add(new PointF(xcurr + 0.1f, ycurr));
                        continue;
                    }
                    else if ((Math.Truncate(ycurr) == ycurr) && (Math.Sign(yprev - ycurr) == Math.Sign(ynext - ycurr)))
                    {
                        subpathList.Add(new PointF(xcurr, ycurr - 0.1f));
                        subpathList.Add(new PointF(xcurr, ycurr + 0.1f));
                        continue;
                    }
                    else subpathList.Add(new PointF(xcurr, ycurr));
                }

                subpathList.Add(subpathList[0]);

                for (int j = 0; j < subpathList.Count - 1; j++)
                {
                    yy1 = subpathList[j].Y;
                    yy2 = subpathList[j + 1].Y;
                    xx1 = subpathList[j].X;
                    xx2 = subpathList[j + 1].X;

                    if (Math.Abs(Math.Truncate(yy2) - Math.Truncate(yy1)) >= 1)
                    {
                        int ybeg, yend;
                        //double yb = Math.Min(yy1, yy2);

                        if (yy1 < yy2)
                        {
                            //ybeg = (int)Math.Truncate(yy1);
                            //ybeg = (ybeg == yy1) ? ybeg : ybeg + 1;
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

                            if (!crossY.ContainsKey(yinc)) crossY.Add(yinc, new List<double>() { xtmp });
                            else crossY[yinc].Add(xtmp);

                            alph[(int)Math.Round(xtmp) - x0, yinc - y0] = 1;
                        }
                    }
                    else if (Math.Truncate(yy1) == yy1) {
                        int yinc = (int) yy1;
                        if (!crossY.ContainsKey(yinc)) crossY.Add(yinc, new List<double>() { xx1 });
                        else crossY[yinc].Add(xx1);
                    }

                    if (Math.Abs(Math.Truncate(xx2) - Math.Truncate(xx1)) >= 1)
                    {

                        int xbeg, xend;
                        //double xb = Math.Min(xx1, xx2);

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

                            if (!crossX.ContainsKey(xinc)) crossX.Add(xinc, new List<double>() { ytmp });
                            else crossX[xinc].Add(ytmp);

                            alph[xinc - x0, (int)Math.Round(ytmp) - y0] = 1;
                        }
                    }
                    else if (Math.Truncate(xx1) == xx1)
                    {
                        int xinc = (int)xx1;
                        if (!crossX.ContainsKey(xinc)) crossX.Add(xinc, new List<double>() { yy1 });
                        else crossX[xinc].Add(yy1);
                    }

                    Console.WriteLine("(" + Math.Round(xx1, 2) + ", " + Math.Round(xx2, 2) + ") (" + Math.Round(yy1, 2) + ", " + Math.Round(yy2, 2) + ")");
                }

            }

            Console.WriteLine();
            Console.WriteLine();
            Console.WriteLine();

            List<double> tmpYList;
            foreach (int ykey in crossY.Keys)
            {
                tmpYList = new List<double>();
                tmpYList.AddRange(crossY[ykey]);
                tmpYList.Sort();
                Console.Write("ykey = " + ykey + ", " + tmpYList.Count + " ");
                foreach (double d in tmpYList)
                    Console.Write(Math.Round(d, 2) + " ");
                Console.WriteLine();
            }

            Console.WriteLine();
            Console.WriteLine();
            Console.WriteLine();

            List<double> tmpXList;
            foreach (int xkey in crossX.Keys)
            {
                tmpXList = new List<double>();
                tmpXList.AddRange(crossX[xkey]);
                tmpXList.Sort();
                Console.Write("xkey = " + xkey + ", " + tmpXList.Count + " ");
                foreach (double d in tmpXList)
                    Console.Write(Math.Round(d, 2) + " ");
                Console.WriteLine();
            }

            /*
            List<double> tmpYList;
            foreach (int ykey in crossY.Keys)
            {
                int inc = 0;
                tmpYList = new List<double>();
                tmpYList.AddRange(crossY[ykey]);
                tmpYList.Sort();

                foreach (double d in crossY[ykey])
                    Console.Write(d + " ");
                Console.WriteLine();

                tmpYList = tmpYList.Distinct().ToList(); //!!! Возможно, после исправления дистинкт больше не нужен


                for (int xinc = x0; xinc <= x1; xinc++)
                {
                    while ((inc < tmpYList.Count) && (xinc > tmpYList[inc]))
                    {
                        inc++;
                    }

                    if (inc >= tmpYList.Count) continue;
                    if (xinc == tmpYList[inc])
                    {
                        alph[xinc - x0, ykey - y0] = 1;

                    }
                    else if (inc % 2 == 1)
                    {
                        alph[xinc - x0, ykey - y0] = 1;
                    }

                }

                if (tmpYList.Count > 1)
                {
                    for (int i = 0; i < tmpYList.Count; i += 2)
                    {
                        int xhalf = (int)Math.Truncate(tmpYList[i]);
                        double diff = tmpYList[i] - xhalf;
                        if (diff < 0.5) alph[xhalf - x0, ykey - y0] = (alph[xhalf - x0, ykey - y0] + (0.5 - diff)); // /2.0
                        xhalf = (int)Math.Truncate(tmpYList[i + 1]) + 1;
                        diff = xhalf - tmpYList[i + 1];
                        if (diff < 0.5) alph[xhalf - x0, ykey - y0] = (alph[xhalf - x0, ykey - y0] + (0.5 - diff)); // /2.0
                    }
                }
            }


            List<double> tmpXList;
            foreach (int xkey in crossX.Keys)
            {
                tmpXList = new List<double>();
                tmpXList.AddRange(crossX[xkey]);
                tmpXList.Sort();
                tmpXList = tmpXList.Distinct().ToList(); //Скорее всего не нужен уже

                //if (tmpXList.Count == 3) {
                //foreach (double d in crossX[xkey])
                //    Console.Write(d + " ");
                //Console.WriteLine();
                //}
                //if (tmpXList.Count % 2 == 1) Console.WriteLine(name + " " + tmpXList.Count);

                if (tmpXList.Count > 1)
                {
                    for (int i = 0; i < tmpXList.Count; i += 2)
                    {
                        int yhalf = (int)Math.Round(tmpXList[i]);
                        double diff = Math.Abs(tmpXList[i] - yhalf);
                        if (diff < 0.5) alph[xkey - x0, yhalf - y0] = (alph[xkey - x0, yhalf - y0] + (0.5 - diff)); // /2.0
                        yhalf = (int)Math.Truncate(tmpXList[i + 1]) + 1;
                        diff = yhalf - tmpXList[i + 1];
                        if (diff < 0.5) alph[xkey - x0, yhalf - y0] = (alph[xkey - x0, yhalf - y0] + (0.5 - diff)); // /2.0
                    }
                }
                else
                {
                    int yhalf = (int)Math.Round(tmpXList[0]);
                    double diff = Math.Abs(tmpXList[0] - yhalf);
                    if (alph[xkey - x0, yhalf - y0] < 1) alph[xkey - x0, yhalf - y0] = (alph[xkey - x0, yhalf - y0] + (0.5 - diff)); // /2.0
                }
            }
            */
            for (int i = x0; i <= x1; i++) {
                for (int j = y0; j <= y1; j++) {
                    if (alph[i - x0, j - y0] > 0) {
                        pix.Add(i);
                        pix.Add(j);
                        pix.Add(0);

                        col.Add((float)alph[i - x0, j - y0]);
                    }
                }
            }

            return new PixMapData(pix, col, 0);
        }



        static internal PixMapData MakeNewText(String text, Font font)
        {
            //double x0 = 0;
            List<float> pix = new List<float>();
            List<float> col = new List<float>();


            GraphicsPath shape = new GraphicsPath();
            StringFormat format = StringFormat.GenericDefault;
            shape.AddString(text, font.FontFamily, (int)font.Style, font.Size, new PointF(0f, 0f), format);

            /*
            for (int i = 0; i < shape.PathPoints.Length; i++)
            {
                PointF pt = shape.PathPoints[i];
                pix.Add(pt.X);
                pix.Add(pt.Y);
                pix.Add(0);

                col.Add(1);

            //Console.WriteLine(pt.X + " " + pt.Y);
            }
            */

            //pix.Add(shape.PathPoints[0].X);
            //pix.Add(shape.PathPoints[0].Y);
            //pix.Add(0);

            //col.Add(1);

            //Console.WriteLine(shape.PathPoints.Length);


            GraphicsPathIterator myPathIterator = new
                GraphicsPathIterator(shape);

            // Rewind the iterator.
            myPathIterator.Rewind();

            // Create the GraphicsPath section.
            GraphicsPath myPathSection = new GraphicsPath();

            // Iterate to the 3rd subpath and list the number of points therein

            // to the screen.
            int subpathPoints;
            bool IsClosed2;

            // Iterate to the third subpath.
            //Console.WriteLine(myPathIterator.SubpathCount);
            /*
            subpathPoints = myPathIterator.NextSubpath(
                myPathSection, out IsClosed2);
            Console.WriteLine(subpathPoints + " " + IsClosed2 + " " + myPathSection.PathPoints[0].X + " " + myPathSection.PathPoints[0].Y);
            subpathPoints = myPathIterator.NextSubpath(
                myPathSection, out IsClosed2);
            Console.WriteLine(subpathPoints + " " + IsClosed2 + " " + myPathSection.PathPoints[0].X + " " + myPathSection.PathPoints[0].Y);
            subpathPoints = myPathIterator.NextSubpath(
                myPathSection, out IsClosed2);
            Console.WriteLine(subpathPoints + " " + IsClosed2 + " " + myPathSection.PathPoints[0].X + " " + myPathSection.PathPoints[0].Y);
            subpathPoints = myPathIterator.NextSubpath(
                myPathSection, out IsClosed2);
            Console.WriteLine(subpathPoints + " " + IsClosed2 + " " + myPathSection.PathPoints[0].X + " " + myPathSection.PathPoints[0].Y);
            */

            RectangleF rec = shape.GetBounds();
            int x0 = (int)Math.Floor(rec.Left);
            int x1 = (int)Math.Ceiling(rec.Right);
            int y0 = (int)Math.Floor(rec.Top);
            int y1 = (int)Math.Ceiling(rec.Bottom);
            double[,] alph = new double[(int)(x1 - x0 + 1), (int)(y1 - y0 + 1)];

            Dictionary<int, List<double>> crossY = new Dictionary<int, List<double>>();
            Dictionary<int, List<double>> crossX = new Dictionary<int, List<double>>();
            //List<Point> moarPoints = new List<Point>();
            double xx1, xx2, yy1, yy2;

            for (int i = 0; i < myPathIterator.SubpathCount; i++)
            {
                subpathPoints = myPathIterator.NextSubpath(myPathSection, out IsClosed2);

                for (int j = 0; j < myPathSection.PathPoints.Length - 1; j++)
                {
                    yy1 = myPathSection.PathPoints[j].Y;
                    yy2 = myPathSection.PathPoints[j + 1].Y;
                    xx1 = myPathSection.PathPoints[j].X;
                    xx2 = myPathSection.PathPoints[j + 1].X;

                    if (Math.Abs(Math.Truncate(yy2) - Math.Truncate(yy1)) >= 1)
                    {
                        double yb = Math.Min(yy1, yy2);
                        int ybeg = (int)Math.Truncate(yb);
                        ybeg = (ybeg == yb) ? ybeg : ybeg + 1;
                        int yend = (int)Math.Ceiling(Math.Max(yy1, yy2));

                        double xtmp;

                        for (int yinc = ybeg; yinc < yend; yinc++)
                        {
                            xtmp = (yinc - yy1) * (xx2 - xx1) / (yy2 - yy1) + xx1;
                            if (!crossY.ContainsKey(yinc)) crossY.Add(yinc, new List<double>() { xtmp });
                            else crossY[yinc].Add(xtmp);
                        }
                    }
                    /*else {
                        if (Math.Abs(Math.Truncate(xx2) - Math.Truncate(xx1)) >= 1)
                        {
                            double xb = Math.Min(xx1, xx2);
                            int xbeg = (int)Math.Truncate(xb);
                            xbeg = (xbeg == xb) ? xbeg : xbeg + 1;
                            int xend = (int)Math.Ceiling(Math.Max(xx1, xx2));

                            int yr1 = (int)Math.Round(Math.Min(yy1, yy2));
                            int yr2 = (int)Math.Round(Math.Max(yy1, yy2));

                            double xmiddle = xend;
                            if (yr1 != yr2) xmiddle = (xend - xbeg) * 1.0 / 2.0;

                            for (int xinc = xbeg; xinc < xend; xinc++)
                            {
                                if (xinc < xmiddle)
                                    moarPoints.Add(new Point(xinc, yr1));
                                else moarPoints.Add(new Point(xinc, yr2));
                            }
                        }
                        else {
                            moarPoints.Add(new Point((int)Math.Round(xx1), (int)Math.Round(yy1)));
                            moarPoints.Add(new Point((int)Math.Round(xx2), (int)Math.Round(yy2)));
                        }
                    }*/

                    if (Math.Abs(Math.Truncate(xx2) - Math.Truncate(xx1)) >= 1)
                    {
                        double xb = Math.Min(xx1, xx2);
                        int xbeg = (int)Math.Truncate(xb);
                        xbeg = (xbeg == xb) ? xbeg : xbeg + 1;
                        int xend = (int)Math.Ceiling(Math.Max(xx1, xx2));

                        double ytmp;

                        for (int xinc = xbeg; xinc < xend; xinc++)
                        {
                            ytmp = (xinc - xx1) * (yy2 - yy1) / (xx2 - xx1) + yy1;

                            if (!crossX.ContainsKey(xinc)) crossX.Add(xinc, new List<double>() { ytmp });
                            else crossX[xinc].Add(ytmp);
                        }
                    }
                }

                yy1 = myPathSection.PathPoints[0].Y;
                yy2 = myPathSection.PathPoints[myPathSection.PathPoints.Length - 1].Y;
                xx1 = myPathSection.PathPoints[0].X;
                xx2 = myPathSection.PathPoints[myPathSection.PathPoints.Length - 1].X;
                if (Math.Abs(Math.Truncate(yy2) - Math.Truncate(yy1)) >= 1)
                {
                    double yb = Math.Min(yy1, yy2);
                    int ybeg = (int)Math.Truncate(yb);
                    ybeg = (ybeg == yb) ? ybeg : ybeg + 1;
                    int yend = (int)Math.Ceiling(Math.Max(yy1, yy2));

                    double xtmp;

                    for (int yinc = ybeg; yinc < yend; yinc++)
                    {
                        xtmp = (yinc - yy1) * (xx2 - xx1) / (yy2 - yy1) + xx1;
                        if (!crossY.ContainsKey(yinc)) crossY.Add(yinc, new List<double>() { xtmp });
                        else crossY[yinc].Add(xtmp);
                    }
                }

                if (Math.Abs(Math.Truncate(xx2) - Math.Truncate(xx1)) >= 1)
                {
                    double xb = Math.Min(xx1, xx2);
                    int xbeg = (int)Math.Truncate(xb);
                    xbeg = (xbeg == xb) ? xbeg : xbeg + 1;
                    int xend = (int)Math.Ceiling(Math.Max(xx1, xx2));

                    double ytmp;

                    for (int xinc = xbeg; xinc < xend; xinc++)
                    {
                        ytmp = (xinc - xx1) * (yy2 - yy1) / (xx2 - xx1) + yy1;

                        if (!crossX.ContainsKey(xinc)) crossX.Add(xinc, new List<double>() { ytmp });
                        else crossX[xinc].Add(ytmp);
                    }
                }

                /*else
                {
                    if (Math.Abs(Math.Truncate(xx2) - Math.Truncate(xx1)) >= 1)
                    {
                        double xb = Math.Min(xx1, xx2);
                        int xbeg = (int)Math.Truncate(xb);
                        xbeg = (xbeg == xb) ? xbeg : xbeg + 1;
                        int xend = (int)Math.Ceiling(Math.Max(xx1, xx2));

                        int yr1 = (int)Math.Round(Math.Min(yy1, yy2));
                        int yr2 = (int)Math.Round(Math.Max(yy1, yy2));

                        double xmiddle = xend;
                        if (yr1 != yr2) xmiddle = (xend - xbeg) * 1.0 / 2.0;

                        for (int xinc = xbeg; xinc < xend; xinc++)
                        {
                            if (xinc < xmiddle)
                                moarPoints.Add(new Point(xinc, yr1));
                            else moarPoints.Add(new Point(xinc, yr2));
                        }
                    }
                }*/
            }


            //Console.WriteLine(x0 + " " + x1);
            for (int yinc = y0; yinc <= y1; yinc++)
            {
                int inc = 0;

                if (!crossY.ContainsKey(yinc)) continue;

                crossY[yinc].Sort();
                //!!! Возможно, после исправления дистинкт больше не нужен
                crossY[yinc] = crossY[yinc].Distinct().ToList();

                //foreach (double d in cross[yinc])
                //    Console.Write(d + " ");
                //Console.WriteLine();

                for (int xinc = x0; xinc <= x1; xinc++)
                {
                    while ((inc < crossY[yinc].Count) && (xinc > crossY[yinc][inc]))
                    {
                        inc++;
                    }

                    if (inc >= crossY[yinc].Count) continue;
                    if (xinc == crossY[yinc][inc])
                    {
                        alph[xinc - x0, yinc - y0] = 1;
                        //pix.Add(xinc);
                        //pix.Add(yinc);
                        //pix.Add(0);
                        //col.Add((float)alph[xinc - x0, yinc - y0]);

                    }
                    else if (inc % 2 == 1)
                    {
                        alph[xinc - x0, yinc - y0] = 1;
                        //pix.Add(xinc);
                        //pix.Add(yinc);
                        //pix.Add(0);
                        //col.Add((float)alph[xinc - x0, yinc - y0]);
                    }

                }

                for (int i = 0; i < crossY[yinc].Count; i += 2)
                {
                    int xhalf = (int)Math.Truncate(crossY[yinc][i]);
                    double diff = crossY[yinc][i] - xhalf;
                    if (diff < 0.5) alph[xhalf - x0, yinc - y0] = (alph[xhalf - x0, yinc - y0] + (0.5 - diff)); // /2.0
                    xhalf = (int)Math.Truncate(crossY[yinc][i + 1]) + 1;
                    diff = xhalf - crossY[yinc][i + 1];
                    if (diff < 0.5) alph[xhalf - x0, yinc - y0] = (alph[xhalf - x0, yinc - y0] + (0.5 - diff)); // /2.0
                }
            }


            List<double> tmpXList;
            foreach (int xkey in crossX.Keys)
            {
                tmpXList = new List<double>();
                tmpXList.AddRange(crossX[xkey]);
                tmpXList.Sort();
                tmpXList = tmpXList.Distinct().ToList(); //Скорее всего не нужен уже
                for (int i = 0; i < tmpXList.Count; i += 2)
                {
                    int yhalf = (int)Math.Truncate(tmpXList[i]);
                    double diff = tmpXList[i] - yhalf;
                    if (diff < 0.5) alph[xkey - x0, yhalf - y0] = (alph[xkey - x0, yhalf - y0] + (0.5 - diff)); // /2.0
                    yhalf = (int)Math.Truncate(tmpXList[i + 1]) + 1;
                    diff = yhalf - tmpXList[i + 1];
                    if (diff < 0.5) alph[xkey - x0, yhalf - y0] = (alph[xkey - x0, yhalf - y0] + (0.5 - diff)); // /2.0
                }
            }



            /*
            foreach (Point pt in moarPoints) {
                if (alph[pt.X - x0, pt.Y - y0] < 1) alph[pt.X - x0, pt.Y - y0] = (alph[pt.X - x0, pt.Y - y0] + 0.9) / 2.0;
            }
            */
            for (int yinc = y0; yinc <= y1; yinc++)
            {
                for (int xinc = x0; xinc <= x1; xinc++)
                {/*
                        if (alph[xinc - x0, yinc - y0] < 1)
                        {
                            float inc = 9;
                            int incb = 0;

                            if (alph[xinc - x0 - 1, yinc - y0 - 1] == 1) incb++;
                            if (alph[xinc - x0 - 1, yinc - y0] == 1) incb++;
                            if (alph[xinc - x0 - 1, yinc - y0 + 1] == 1) incb++;
                            if (alph[xinc - x0, yinc - y0 - 1] == 1) incb++;
                            if (alph[xinc - x0, yinc - y0 + 1] == 1) incb++;
                            if (alph[xinc - x0 + 1, yinc - y0 - 1] == 1) incb++;
                            if (alph[xinc - x0 + 1, yinc - y0] == 1) incb++;
                            if (alph[xinc - x0 + 1, yinc - y0 + 1] == 1) incb++;

                            alph[xinc - x0, yinc - y0] = incb * 1.0 / inc;
                            
                        }*/

                    if (alph[xinc - x0, yinc - y0] > 0)
                    {
                        pix.Add(xinc);
                        pix.Add(yinc);
                        pix.Add(0);

                        //if (alph[xinc - x0, yinc - y0] < 1) Console.Write((float)alph[xinc - x0, yinc - y0] + " ");
                        col.Add((float)alph[xinc - x0, yinc - y0]);
                    }
                }
            }

            return new PixMapData(pix, col, (float)x0);
        }
    }
}
