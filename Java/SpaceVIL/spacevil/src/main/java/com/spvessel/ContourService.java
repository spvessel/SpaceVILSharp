package com.spvessel;

import java.awt.geom.Point2D;
import java.util.*;

public final class ContourService {
      /*
    public static CrossOut crossContours(List<List<Point2D>> innerList) {
        List<Contour> contoursList = new LinkedList<>();
        for (int i = 0; i < innerList.size(); i++) {
            contoursList.add(analizeCotour(innerList.get(i)));
        }

        return makeCrossArray(contoursList);
    }

    public static CrossOut crossContours(GraphicsPath shape) {
        shape.Flatten();
        List<Contour> contoursList = new LinkedList<>();

        GraphicsPathIterator myPathIterator = new GraphicsPathIterator(shape);
        // Rewind the iterator.
        myPathIterator.Rewind();
        // Create the GraphicsPath section.
        GraphicsPath myPathSection = new GraphicsPath();
        int subpathPoints;
        boolean IsClosed2;
        //Console.WriteLine(myPathIterator.Count);

        for (int i = 0; i < myPathIterator.SubpathCount; i++) {
            subpathPoints = myPathIterator.NextSubpath(myPathSection, out IsClosed2);
            List<Point2D> pathSection = new LinkedList<>();
            for (int j = 0; j < myPathSection.PathPoints.Length; j++) {
                pathSection.add(new Point2D(myPathSection.PathPoints[j].X, myPathSection.PathPoints[j].Y));
            }
            contoursList.add(analizeCotour(pathSection));
        }

        return makeCrossArray(contoursList);
    }

    public static double[][] crossContoursAntiAl(GraphicsPath shape) {
        List<Contour> contoursList = new LinkedList<>();

        GraphicsPathIterator myPathIterator = new GraphicsPathIterator(shape);
        // Rewind the iterator.
        myPathIterator.Rewind();
        // Create the GraphicsPath section.
        GraphicsPath myPathSection = new GraphicsPath();
        int subpathPoints;
        bool IsClosed2;

        for (int i = 0; i < myPathIterator.SubpathCount; i++) {
            subpathPoints = myPathIterator.NextSubpath(myPathSection, out IsClosed2);
            List<Point2D> pathSection = new LinkedList<>();
            for (int j = 0; j < myPathSection.PathPoints.Length; j++) {
                pathSection.add(new Point2D(myPathSection.PathPoints[j].X, myPathSection.PathPoints[j].Y));
            }
            contoursList.add(analizeCotour(pathSection));
        }

        return makeCrossArrayAntiAl(contoursList);
    }

    private static Contour analizeCotour(List<Point2D> pathSection) {
        List<Point2D> contour = new LinkedList<>();
        double xx1, xx2, yy1, yy2;
        Map<Integer, List<InOutCoord>> crossY = new HashMap<>();
        Map<Integer, List<InOutCoord>> crossX = new HashMap<>();
        double xcurr = pathSection.get(pathSection.size() - 1).getX();
        double ycurr = pathSection.get(pathSection.size() - 1).getY();
        double xprev, yprev, xnext, ynext;
        boolean clockWise;
        for (int j = 0; j < pathSection.size() - 1; j++) {
            xprev = xcurr;
            yprev = ycurr;
            xcurr = pathSection.get(j).getX();
            ycurr = pathSection.get(j).getY();
            xnext = pathSection.get(j + 1).getX();
            ynext = pathSection.get(j + 1).getY();

            if (xprev == xcurr && xcurr == xnext) continue;

            if (yprev == ycurr && ycurr == ynext) continue;

            if (((int)xcurr == xcurr) && (Math.signum(xprev - xcurr) == Math.signum(xnext - xcurr))) {
                contour.add(new Point2D.Double(xcurr - 0.1f, ycurr));
                contour.add(new Point2D.Double(xcurr + 0.1f, ycurr));
                continue;
            }

            if (((int)ycurr == ycurr) && (Math.signum(yprev - ycurr) == Math.signum(ynext - ycurr))) {
                contour.add(new Point2D.Double(xcurr, ycurr - 0.1f));
                contour.add(new Point2D.Double(xcurr, ycurr + 0.1f));
                continue;
            }

            contour.add(new Point2D.Double(xcurr, ycurr));
        }

        xprev = pathSection.get(pathSection.size() - 2).getX();
        yprev = pathSection.get(pathSection.size() - 2).getY();
        xcurr = pathSection.get(pathSection.size() - 1).getX();
        ycurr = pathSection.get(pathSection.size() - 1).getY();
        xnext = pathSection.get(0).getX();
        ynext = pathSection.get(0).getY();

        if (!(xprev == xcurr && xcurr == xnext) && !(yprev == ycurr && ycurr == ynext)) {
            if (((int)xcurr == xcurr) && (Math.signum(xprev - xcurr) == Math.signum(xnext - xcurr))) {
                contour.add(new Point2D.Double(xcurr - 0.1f, ycurr));
                contour.add(new Point2D.Double(xcurr + 0.1f, ycurr));
                //continue;
            } else if (((int)ycurr == ycurr) && (Math.signum(yprev - ycurr) == Math.signum(ynext - ycurr))) {
                contour.add(new Point2D.Double(xcurr, ycurr - 0.1f));
                contour.add(new Point2D.Double(xcurr, ycurr + 0.1f));
                //continue;
            } else contour.add(new Point2D.Double(xcurr, ycurr));
        }

        contour.add(contour.get(0));
        clockWise = isClockwise(contour);//Console.WriteLine("Contour check " + clockWise);

        for (int j = 0; j < contour.size() - 1; j++) {
            yy1 = contour.get(j).getY();
            yy2 = contour.get(j + 1).getY();
            xx1 = contour.get(j).getX();
            xx2 = contour.get(j + 1).getX();

            boolean isIn;
            if (yy2 - yy1 < 0) isIn = clockWise; // true; //Долбаный перевернутый Y
            else isIn = !clockWise; //false;

            if (Math.abs((int)yy2 - (int)yy1) >= 1) {
                int ybeg, yend;

                if (yy1 < yy2) {
                    ybeg = (int) Math.ceil(yy1);
                    yend = (int) Math.ceil(yy2);
                } else {
                    yend = (int) yy1 + 1;
                    ybeg = (int) yy2 + 1;
                }

                double xtmp;

                for (int yinc = ybeg; yinc < yend; yinc++) {
                    xtmp = (yinc - yy1) * (xx2 - xx1) / (yy2 - yy1) + xx1;

                    if (!crossY.containsKey(yinc)) crossY.put(yinc, new LinkedList<InOutCoord>()
                            { new InOutCoord(isIn, (float)xtmp, clockWise) });
                    else crossY.get(yinc).add(new InOutCoord(isIn, (float) xtmp, clockWise));
                }
            } else if ((int)yy1 == yy1) {
                int yinc = (int) yy1;
                if (!crossY.containsKey(yinc)) crossY.put(yinc, new LinkedList<>());

                if (yy1 == yy2) {
                    crossY.get(yinc).add(new InOutCoord(true, (float) Math.min(xx1, xx2), clockWise));
                    crossY.get(yinc).add(new InOutCoord(false, (float) Math.max(xx1, xx2), clockWise));
                } else crossY.get(yinc).add(new InOutCoord(isIn, (float) xx1, clockWise));
            }

            if (xx2 - xx1 > 0) isIn = clockWise; //true;
            else isIn = !clockWise; //false;

            if (Math.abs((int)xx2 - (int)xx1) >= 1) {

                int xbeg, xend;

                if (xx1 < xx2) {
                    xbeg = (int) Math.ceil(xx1);
                    xend = (int) Math.ceil(xx2);
                } else {
                    xend = (int) xx1 + 1;
                    xbeg = (int) xx2 + 1;
                }

                double ytmp;

                for (int xinc = xbeg; xinc < xend; xinc++) {
                    ytmp = (xinc - xx1) * (yy2 - yy1) / (xx2 - xx1) + yy1;

                    if (!crossX.containsKey(xinc)) crossX.put(xinc, new LinkedList<InOutCoord>()
                            { new InOutCoord(isIn, (float)ytmp,clockWise) });
                    else crossX.get(xinc).add(new InOutCoord(isIn, (float) ytmp, clockWise));
                }
            } else if ((int)xx1 == xx1) {
                int xinc = (int) xx1;
                if (!crossX.containsKey(xinc)) crossX.put(xinc, new LinkedList<>());

                if (xx1 == xx2) {
                    crossX.get(xinc).add(new InOutCoord(true, (float) Math.min(yy1, yy2), clockWise));
                    crossX.get(xinc).add(new InOutCoord(false, (float) Math.max(yy1, yy2), clockWise));
                } else crossX.get(xinc).add(new InOutCoord(isIn, (float) yy1, clockWise));
            }
        }

        return new Contour(crossX, crossY, clockWise);
    }

    private static CrossOut makeCrossArray(List<Contour> contours) {
        Map<Integer, List<InOutCoord>> _globalCrossX = new HashMap<>();
        Map<Integer, List<InOutCoord>> _globalCrossY = new HashMap<>();

        int x0 = Integer.MAX_VALUE;// = _globalCrossX.Keys.Min() - 1; //Это с запасом, но должно хватить
        int y0 = Integer.MAX_VALUE;// = _globalCrossY.Keys.Min() - 1;
        int x1 = Integer.MIN_VALUE;// = _globalCrossX.Keys.Max() + 1;
        int y1 = Integer.MIN_VALUE;// = _globalCrossY.Keys.Max() + 1;

        for (int i = 0; i < contours.size(); i++) {
            for (int xkey : contours.get(i)._crossX.keySet()) {
                if (!_globalCrossX.containsKey(xkey)) _globalCrossX.put(xkey, new LinkedList<>());
                _globalCrossX.get(xkey).addAll(contours.get(i)._crossX.get(xkey));
            }

            for (int ykey : contours.get(i)._crossY.keySet()) {
                if (!_globalCrossY.containsKey(ykey)) _globalCrossY.put(ykey, new LinkedList<>());
                _globalCrossY.get(ykey).addAll(contours.get(i)._crossY.get(ykey));
            }

            x0 = (x0 > contours.get(i).minX) ? contours.get(i).minX : x0;
            x1 = (x1 < contours.get(i).maxX) ? contours.get(i).maxX : x1;
            y0 = (y0 > contours.get(i).minY) ? contours.get(i).minY : y0;
            y1 = (y1 < contours.get(i).maxY) ? contours.get(i).maxY : y1;
        }

        double[][] alph = new double[x1 - x0 + 1][y1 - y0 + 1];
        int isInside;
        int add;
        int incCoord;
        double diff;

        for (int ykey : _globalCrossY.keySet()) {
            _globalCrossY.get(ykey).Sort((x, y) =>x._coord.CompareTo(y._coord));
            isInside = 0;
            incCoord = (int) (_globalCrossY.get(ykey).get(0)._coord);

            for (int i = 0; i < _globalCrossY.get(ykey).size(); i++) {
                add = (_globalCrossY.get(ykey).get(i)._isIn == _globalCrossY.get(ykey).get(i)._clockwiae) ? 1 : -1;

                if (isInside != 0) //Был внутри
                {
                    while (incCoord <= _globalCrossY.get(ykey).get(i)._coord) {
                        alph[incCoord - x0][ykey - y0] = 1;
                        incCoord++;
                    }

                    isInside += add;
                    diff = incCoord - _globalCrossY.get(ykey).get(i)._coord;
                    if (isInside == 0 && diff > 0) //Стал снаружи
                    {
                        if (alph[incCoord - x0][ykey - y0] < 1 - diff - 0.3)
                            alph[incCoord - x0][ykey - y0] = (1 - diff - 0.3) * 3f / 4;
                    }
                } else {

                    while (incCoord < _globalCrossY.get(ykey).get(i)._coord) {
                        incCoord++;
                    }

                    isInside += add;
                    diff = incCoord - _globalCrossY.get(ykey).get(i)._coord;
                    if (isInside != 0 && diff > 0) {
                        if ((incCoord != x0) && (alph[incCoord - 1 - x0][ykey - y0] < diff - 0.3))
                            alph[incCoord - 1 - x0][ykey - y0] = (diff - 0.3) * 3f / 4;
                    }
                }
            }
        }

        for (int xkey : _globalCrossX.keySet()) {
            _globalCrossX.get(xkey).Sort((x, y) =>x._coord.CompareTo(y._coord));
            isInside = 0;
            for (int i = 0; i < _globalCrossX.get(xkey).size(); i++) {
                add = (_globalCrossX.get(xkey).get(i)._isIn == _globalCrossX.get(xkey).get(i)._clockwiae) ? 1 : -1;

                incCoord = (int) (_globalCrossX.get(xkey).get(i)._coord);
                if (incCoord != _globalCrossX.get(xkey).get(i)._coord) {
                    incCoord++;
                    diff = incCoord - _globalCrossX.get(xkey).get(i)._coord;

                    if (isInside != 0 && isInside + add == 0) //Точка выхода
                    {
                        if (alph[xkey - x0][incCoord - y0] < 1 - diff - 0.3)
                            alph[xkey - x0][incCoord - y0] = (1 - diff - 0.3) * 3f / 4;
                        //if (diff < 0.5 && diff > 0) alph[xkey - x0, incCoord - y0] = (alph[xkey - x0, incCoord - y0] + (0.5 - diff)); // /2.0

                    } else if (isInside == 0 && isInside + add != 0) //Точка входа
                    {
                        if (alph[xkey - x0][incCoord - 1 - y0] < diff - 0.3)
                            alph[xkey - x0][incCoord - 1 - y0] = (diff - 0.3) * 3f / 4;
                        //diff = Math.Abs(_globalCrossX[xkey][i]._coord - incCoord);
                        //if (diff < 0.5 && diff > 0) alph[xkey - x0, incCoord - y0] = (alph[xkey - x0, incCoord - y0] + (0.5 - diff)); // /2.0
                    }
                }
                isInside += add;
            }
        }

        return new CrossOut(alph, x0, y0);
    }


    private static double[][] makeCrossArrayAntiAl(List<Contour> contours) {
        Map<Integer, List<InOutCoord>> _globalCrossX = new HashMap<>();
        Map<Integer, List<InOutCoord>> _globalCrossY = new HashMap<>();

        int x0 = Integer.MAX_VALUE;// = _globalCrossX.Keys.Min() - 1; //Это с запасом, но должно хватить
        int y0 = Integer.MAX_VALUE;// = _globalCrossY.Keys.Min() - 1;
        int x1 = Integer.MIN_VALUE;// = _globalCrossX.Keys.Max() + 1;
        int y1 = Integer.MIN_VALUE;// = _globalCrossY.Keys.Max() + 1;

        for (int i = 0; i < contours.size(); i++) {
            for (int xkey : contours.get(i)._crossX.keySet()) {
                if (!_globalCrossX.containsKey(xkey)) _globalCrossX.put(xkey, new LinkedList<>());
                _globalCrossX.get(xkey).addAll(contours.get(i)._crossX.get(xkey));
            }

            for (int ykey : contours.get(i)._crossY.keySet()) {
                if (!_globalCrossY.containsKey(ykey)) _globalCrossY.put(ykey, new LinkedList<>());
                _globalCrossY.get(ykey).addAll(contours.get(i)._crossY.get(ykey));
            }

            x0 = (x0 > contours.get(i).minX) ? contours.get(i).minX : x0;
            x1 = (x1 < contours.get(i).maxX) ? contours.get(i).maxX : x1;
            y0 = (y0 > contours.get(i).minY) ? contours.get(i).minY : y0;
            y1 = (y1 < contours.get(i).maxY) ? contours.get(i).maxY : y1;
        }

        double[][] alph = new double[x1 - x0 + 1][y1 - y0 + 1];
        int isInside;
        int add;
        int incCoord;
        double diff;

        for (int ykey : _globalCrossY.keySet()) {
            _globalCrossY.get(ykey).Sort((x, y) =>x._coord.CompareTo(y._coord));
            isInside = 0;
            incCoord = (int) (_globalCrossY.get(ykey).get(0)._coord);
            for (int i = 0; i < _globalCrossY.get(ykey).size(); i++) {
                add = (_globalCrossY.get(ykey).get(i)._isIn == _globalCrossY.get(ykey).get(i)._clockwiae) ? 1 : -1;
                if (isInside != 0) //Был внутри
                {
                    while (incCoord <= _globalCrossY.get(ykey).get(i)._coord) {
                        alph[incCoord - x0][ykey - y0] = 1;
                        incCoord++;
                    }

                    isInside += add;
                    diff = incCoord - _globalCrossY.get(ykey).get(i)._coord;
                    if (isInside == 0 && diff > 0 && diff < 1) //Стал снаружи, т.е. точка выхода
                    {
                        if (alph[incCoord - x0][ykey - y0] > 0)
                            alph[incCoord - x0][ykey - y0] = 200 + Math.round((1 - diff) * 100);
                        //if (diff < 0.5) alph[incCoord - x0, ykey - y0] = (alph[incCoord - x0, ykey - y0] + (0.5 - diff)); // /2.0
                    }

                } else {
                    while (incCoord < _globalCrossY.get(ykey).get(i)._coord) {
                        incCoord++;
                    }

                    isInside += add;
                    diff = incCoord - _globalCrossY.get(ykey).get(i)._coord;
                    if (isInside != 0 && diff > 0 && diff < 1) //точка входа
                    {
                        alph[incCoord - 1 - x0][ykey - y0] = 100 + Math.round(diff * 100);
                        //diff = 1 - diff;
                        //if (diff < 0.5) alph[incCoord - 1 - x0, ykey - y0] = (alph[incCoord - 1 - x0, ykey - y0] + (0.5 - diff)); // /2.0
                    }
                }
            }
        }

        for (int xkey : _globalCrossX.keySet()) {
            _globalCrossX.get(xkey).Sort((x, y) =>x._coord.CompareTo(y._coord));
            isInside = 0;
            for (int i = 0; i < _globalCrossX.get(xkey).size(); i++) {
                add = (_globalCrossX.get(xkey).get(i)._isIn == _globalCrossX.get(xkey).get(i)._clockwiae) ? 1 : -1;

                if (isInside != 0 && isInside + add == 0) //Точка выхода
                {
                    incCoord = (int) (_globalCrossX.get(xkey).get(i)._coord) + 1;
                    if (alph[xkey - x0][incCoord - y0] < 100) {
                        diff = incCoord - _globalCrossX.get(xkey).get(i)._coord;
                        if (diff < 0.5 && diff > 0) alph[xkey - x0][incCoord - y0] = (alph[xkey - x0][incCoord - y0]
                                + (0.5 - diff)); // /2.0
                    }
                } else if (isInside == 0 && isInside + add != 0) //Точка входа
                {
                    incCoord = Math.round(_globalCrossX.get(xkey).get(i)._coord);
                    if (alph[xkey - x0][incCoord - y0] < 100) {
                        diff = Math.abs(_globalCrossX.get(xkey).get(i)._coord - incCoord);
                        if (diff < 0.5 && diff > 0) alph[xkey - x0][incCoord - y0] = (alph[xkey - x0][incCoord - y0]
                                + (0.5 - diff)); // /2.0
                    }
                }

                isInside += add;
            }
        }

        return alph;
    }


    private static boolean isClockwise(List<Point2D> pointsList) {
        float sum = 0;
        double x1, x2, y1, y2;
        x1 = pointsList.get(0).getX();
        y1 = pointsList.get(0).getY();
        for (int i = 0; i < pointsList.size() - 1; i++) {
            x2 = pointsList.get(i + 1).getX();
            y2 = pointsList.get(i + 1).getY();

            sum += (x2 - x1) * (y1 + y2);
            x1 = x2;
            y1 = y2;
        }
        x2 = pointsList.get(0).getX();
        y2 = pointsList.get(0).getY();

        sum += (x2 - x1) * (y1 + y2);

        //if (sum == 0) System.out.println("Something is wrong with clockwise");
        return (sum < 0);
    }

    private class Contour {
        boolean _clockwise;
        Map<Integer, List<InOutCoord>> _crossX;
        Map<Integer, List<InOutCoord>> _crossY;
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        Contour(Map<Integer, List<InOutCoord>> crossX, Map<Integer, List<InOutCoord>> crossY, boolean clockwise) {
            _clockwise = clockwise;
            formalizeCross(crossX, crossY);
        }

        private void formalizeCross(Map<Integer, List<InOutCoord>> crossX,
                                    Map<Integer, List<InOutCoord>> crossY) {

            _crossX = new HashMap<>();
            _crossY = new HashMap<>();

            List<InOutCoord> tmpYList;
            List<InOutCoord> tmpList;

            for (int ykey : crossY.keySet()) {
                tmpList = new LinkedList<>();
                tmpYList = new LinkedList<>();
                tmpList.addAll(crossY.get(ykey));

                tmpList.Sort((x, y) =>x._coord.CompareTo(y._coord));
                //minX = (minX > (int)Math.Truncate(tmpList[0]._coord)) ? (int)Math.Truncate(tmpList[0]._coord) : minX;
                //maxX = (maxX < (int)Math.Truncate(tmpList[tmpList.Count - 1]._coord) + 1) ? (int)Math.Truncate(tmpList[tmpList.Count - 1]._coord) + 1 : maxX;

                int inc = 0;
                while (inc < tmpList.size()) {
                    tmpYList.add(tmpList.get(inc)); //(tmpList[inc]._isIn == true)
                    inc++;
                    while (inc < tmpList.size() && tmpList.get(inc)._isIn) inc++;

                    if (inc < tmpList.size()) {
                        while (inc < tmpList.size() && tmpList.get(inc)._isIn == false) inc++;
                        tmpYList.add(tmpList.get(inc - 1));
                    }
                }

                _crossY.put(ykey, new LinkedList<>(tmpYList));
            }

            List<InOutCoord> tmpXList;
            for (int xkey : crossX.keySet()) {
                tmpList = new LinkedList<>();
                tmpXList = new LinkedList<>();
                tmpList.addAll(crossX.get(xkey));
                tmpList.Sort((x, y) =>x._coord.CompareTo(y._coord));

                //minY = (minY > (int)Math.Truncate(tmpList[0]._coord)) ? (int)Math.Truncate(tmpList[0]._coord) : minY;
                //maxY = (maxY < (int)Math.Truncate(tmpList[tmpList.Count - 1]._coord) + 1) ? (int)Math.Truncate(tmpList[tmpList.Count - 1]._coord) + 1 : maxY;

                int inc = 0;
                while (inc < tmpList.size()) {
                    tmpXList.add(tmpList.get(inc)); //(tmpList[inc]._isIn == true)
                    inc++;
                    while (inc < tmpList.size() && tmpList.get(inc)._isIn) inc++;

                    if (inc < tmpList.size()) {
                        while (inc < tmpList.size() && tmpList.get(inc)._isIn == false) inc++;
                        tmpXList.add(tmpList.get(inc - 1));
                    }
                }

                _crossX.put(xkey, new LinkedList<>(tmpXList));
            }

            if (_crossY.size() != 0) {
                minY = Collections.min(_crossY.keySet()) - 1;
                maxY = Collections.max(_crossY.keySet()) + 1;
                //minY = (minY > _crossY.Keys.Min() - 1) ? _crossY.Keys.Min() - 1 : minY;
                //maxY = (maxY < _crossY.Keys.Max() + 1) ? _crossY.Keys.Max() + 1 : maxY;
            } else {
                if (_crossX.size() == 0) {
                    minX = 0;
                    maxX = 0;
                    minY = 0;
                    maxY = 0;
                }
            }

            if (_crossX.size() != 0) {
                minX = Collections.min(_crossX.keySet()) - 1;
                maxX = Collections.max(_crossX.keySet()) + 1;
                //minX = (minX > _crossX.Keys.Min() - 1) ? _crossX.Keys.Min() - 1 : minX; //Это с запасом, но должно хватить
                //maxX = (maxX < _crossX.Keys.Max() + 1) ? _crossX.Keys.Max() + 1 : maxX;
            }
        }

    }

    private class InOutCoord {
        boolean _isIn;
        float _coord;
        boolean _clockwiae;

        InOutCoord(boolean isIn, float coord, boolean clockwise) {
            _isIn = isIn;
            _coord = coord;
            _clockwiae = clockwise;
        }
    }

    public class CrossOut {
        public double[][] _arr;
        public int _minY;
        public int _minX;

        public CrossOut(double[][] arr, int minX, int minY) {
            _arr = arr;
            _minY = minY;
            _minX = minX;
        }
    }
    */
}
