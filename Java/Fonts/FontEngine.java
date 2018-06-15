package CommonVesselGUI;

import CommonVesselGUI.Cores.PixMapData;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

class FontEngine {
    static Map<Font, Alphabet> fonts = new HashMap<>();

    //Нужен вариант с дефолтным фонтом
    static PixMapData getPixMap(String text, Font font) {
        if (!fonts.containsKey(font)) {
            fonts.put(font, new Alphabet(font));
        }

        return fonts.get(font).makeText(text);
    }

    static int[] getSpacerDims(Font font) {
        if (!fonts.containsKey(font)) {
            fonts.put(font, new Alphabet(font));
        }
        Alphabet a = fonts.get(font);
        return new int[]{a.lineSpacer, a.minY, a.maxY};
    }

    static private class Alphabet {
        private Font font;
        private Map<Character, Letter> letters;
        private int minY = Integer.MAX_VALUE;
        private int maxY = Integer.MIN_VALUE;
        private int lineSpacer;

        Alphabet(Font font) {
            this.font = font;
            letters = new HashMap<>();

            fillABC();
            fillSpecLetters();
        }

        private void fillSpecLetters() {
            char specLet = " ".charAt(0);
            Letter letter = new Letter(" ", null);
            letters.put(specLet, letter);
            letter.width = letters.get("-".charAt(0)).width;
            letter.height = 0;
            lineSpacer = (int) letter.width;

            specLet = "\t".charAt(0);
            Letter letter1 = new Letter("\t", null);
            letters.put(specLet, letter1);
            letter1.width = letter.width * 4;
            letter1.height = 0;
            /*
            specLet = "\n".charAt(0);
            Letter letter2 = new Letter("\n", null);
            letters.put(specLet, letter2);
            letter2.width = 0;
            */
            //updateLineSpacer(); //Здесь это вроде не нужно
        }
        /*
        private void updateLineSpacer() {
            letters.get("\n".charAt(0)).height = Math.abs(minY) + Math.abs(maxY) + lineSpacer;
        }
        */
        private double updateSpecX0(Letter letter, double x0) {
            /*if (letter.name.equals("\n"))
                return 0;
            else*/
                return x0 + 2; //for " " and "\t"
        }
        /*
        private double updateSpecY0(Letter letter, double y0, int addSpace) {
            if (letter.name.equals("\n"))
                return y0 + letter.height + addSpace;
            return y0;
        }
        */
        private void addLetter(char c) {
            Letter letter = makeLetter(Character.toString(c));
            letters.put(c, letter);
            minY = (minY > letter.minY) ? letter.minY : minY;
            maxY = (maxY < letter.maxY) ? letter.maxY : maxY;
        }

        private PixMapData makeText(String text) {
            //updateLineSpacer();

            double x0 = 0;
            //double y0 = 0;

            //float textMaxX = Integer.MIN_VALUE;
            //float textMinY = Integer.MAX_VALUE;
            //float textMaxY = Integer.MIN_VALUE;

            List<Float> pix = new LinkedList<>();
            List<Float> col = new LinkedList<>();

            Letter currLet;
            Letter prevLet = null;
            for (char c : text.toCharArray()) {
                if (!letters.containsKey(c)) addLetter(c); //letters.put(c, makeLetter(Character.toString(c)));

                currLet = letters.get(c);

                if (currLet.isSpec) {
                    x0 = updateSpecX0(currLet, x0);
                    //y0 = updateSpecY0(currLet, y0, addSpace);
                } else {
                    if (prevLet != null) {
                        int ly0 = prevLet.minY;
                        int ly1 = prevLet.maxY;
                        int ry0 = currLet.minY;
                        int ry1 = currLet.maxY;

                        boolean b1 = false, b2 = false;
                        for (int i = Math.max(ly0, ry0); i < Math.min(ly1, ry1); i++) {
                            if (prevLet.rightArr0[i - ly0] && currLet.leftArr0[i - ry0]) {
                                b1 = true;
                                break;
                            }
                        }

                        if (b1) x0++;
                        else {
                            for (int i = Math.max(ly0, ry0); i < Math.min(ly1, ry1); i++) {
                                if (prevLet.rightArr1[i - ly0] && currLet.leftArr0[i - ry0]) {
                                    b2 = true;
                                    break;
                                }
                                if (prevLet.rightArr0[i - ly0] && currLet.leftArr1[i - ry0]) {
                                    b2 = true;
                                    break;
                                }
                            }

                            if (!b2) x0--; // && !(currLet.name.equals(" ") || prevLet.name.equals(" "))
                        }
                    }

                    col.addAll(currLet.getAlphas());
                    pix.addAll(addShift((float) x0, currLet.getCoords()));

                    //textMinY = (textMinY > currLet.minY + y0) ? (float)(currLet.minY + y0) : textMinY;
                    //textMaxY = (textMaxY < currLet.maxY + y0) ? (float)(currLet.maxY + y0) : textMaxY;
                }

                x0 += currLet.width;
                //textMaxX = (textMaxX < x0) ? (float)x0 : textMaxX;

                prevLet = currLet;
            }

            return new PixMapData(pix, col, (float)x0);
        }

        private void fillABC() {
            String str = "abcdefghijklmnopqrstuvwxyz";
            str += str.toUpperCase();
            str += ".,?!1234567890-+=_";


            char[] defLetters = str.toCharArray();
            for (char c : defLetters)
                addLetter(c); //letters.put(c, makeLetter(Character.toString(c)));
        }

        private Letter makeLetter(String let) {
            FontRenderContext context = new FontRenderContext(null, false, false);

            GeneralPath shape = new GeneralPath();
            TextLayout layout = new TextLayout(let, font, context);

            AffineTransform transform = AffineTransform.getTranslateInstance(0, 0);

            Shape outline = layout.getOutline(transform);
            shape.append(outline, true);

            return new Letter(let, shape);
        }

        private List<Float> addShift(float x0, List<Float> coord) {
            List<Float> outCoord = new LinkedList<>();
            float f;
            for (int i = 0; i < coord.size(); i += 3) {
                f = coord.get(i) + x0;

                outCoord.add(f);
                f = coord.get(i + 1);

                outCoord.add(f);
                f = coord.get(i + 2);
                outCoord.add(f);
            }

            return outCoord;
        }

    }

    static private class Letter {
        private String name;
        private double width;
        private double height;
        private List<Float> coords;
        private List<Float> alphas;
        private double left = 0;
        private double right = 0;
        private int minY = 0;
        private int maxY = 0;
        private boolean[] leftArr0;
        private boolean[] leftArr1;
        private boolean[] rightArr0;
        private boolean[] rightArr1;
        private double err = 0.3;
        private boolean isSpec;

        Letter(String name, Shape shape) {
            this.name = name;
            coords = new LinkedList<>();
            alphas = new LinkedList<>();
            if (shape != null)
                makeLetterArrays(shape);
            else isSpec = true;
        }

        private void makeLetterArrays(Shape shape) {
            double minX;
            Rectangle2D rec = shape.getBounds2D();
            double x0 = Math.floor(rec.getMinX());
            double x1 = Math.ceil(rec.getMaxX());
            double y0 = Math.floor(rec.getMinY());
            double y1 = Math.ceil(rec.getMaxY());

            int height;
            height = (int) (y1 - y0 + 1);

            leftArr0 = new boolean[height];
            leftArr1 = new boolean[height];
            rightArr0 = new boolean[height];
            rightArr1 = new boolean[height];

            boolean[] leftTmpArray = new boolean[height];
            boolean[] rightTmpArray = new boolean[height];

            minX = x0;
            minY = (int) y0;
            maxY = (int) y1;

            double tmpLeft = 0;
            double tmpRight = 0;
            int boolInd;

            for (double dd = y0; dd <= y1; dd++) {
                boolInd = (int) (dd - y0);

                for (double d = x0; d <= x1; d++) {
                    if (shape.contains(d, dd)) {
                        coords.add((float) (d - minX));
                        coords.add((float) dd);
                        coords.add(0.0f);

                        //colors.add(0.0f);
                        //colors.add(0.0f);
                        //colors.add(0.0f);
                        alphas.add(1.0f);

                        if (d == x0) {
                            left = 1;
                            leftArr0[boolInd] = true;
                            if (boolInd - 1 >= 0) leftArr0[boolInd - 1] = true;
                            if (boolInd + 1 <= y1 - y0) leftArr0[boolInd + 1] = true;
                        }
                        if (d == x0 + 1) {
                            tmpLeft = 1;
                            leftArr1[boolInd] = true;
                            if (boolInd - 1 >= 0) leftArr1[boolInd - 1] = true;
                            if (boolInd + 1 <= y1 - y0) leftArr1[boolInd + 1] = true;
                        }
                        if (d == x1) {
                            right = 1;
                            rightArr0[boolInd] = true;
                        }
                        if (d == x1 - 1) {
                            tmpRight = 1;
                            rightArr1[boolInd] = true;
                        }
                        if (d == x0 + 2) {
                            leftTmpArray[boolInd] = true;
                            if (boolInd - 1 >= 0) leftTmpArray[boolInd - 1] = true;
                            if (boolInd + 1 <= y1 - y0) leftTmpArray[boolInd + 1] = true;
                        }
                        if (d == x1 - 2) {
                            rightTmpArray[boolInd] = true;
                        }

                    } else if (shape.intersects(d - 0.5, dd - 0.5, 1, 1)) {

                        double inter;

                        int count = 0;
                        int countTmp = 0;

                        for (double xx = -0.5; xx <= 0.5; xx += 0.1) {
                            for (double yy = -0.5; yy <= 0.5; yy += 0.1) {
                                countTmp++;
                                if (shape.contains(d + xx, dd + yy))
                                    count++;
                            }
                        }

                        inter = count * 1.0 / countTmp;
                        coords.add((float) (d - minX));
                        coords.add((float) dd);
                        coords.add(0.0f);

                        //colors.add(0f);
                        //colors.add(0f);
                        //colors.add(0f);
                        if (inter < 1.0f) inter *= 1.25;
                        alphas.add((float) (inter));

                        if (d == x0) {
                            left = (left < inter) ? inter : left;
                            if (inter >= err) {
                                leftArr0[boolInd] = true;
                                if (boolInd - 1 >= 0) leftArr0[boolInd - 1] = true;
                                if (boolInd + 1 <= y1 - y0) leftArr0[boolInd + 1] = true;
                            }
                        }
                        if (d == x0 + 1) {
                            tmpLeft = (tmpLeft < inter) ? inter : tmpLeft;
                            if (inter >= err) {
                                leftArr1[boolInd] = true;
                                if (boolInd - 1 >= 0) leftArr1[boolInd - 1] = true;
                                if (boolInd + 1 <= y1 - y0) leftArr1[boolInd + 1] = true;
                            }
                        }
                        if (d == x1) {
                            right = (right < inter) ? inter : right;
                            if (inter >= err)
                                rightArr0[boolInd] = true;
                        }
                        if (d == x1 - 1) {
                            tmpRight = (tmpRight < inter) ? inter : tmpRight;
                            if (inter >= err)
                                rightArr1[boolInd] = true;
                        }
                        if ((d == x0 + 2) && (inter >= err)) {

                            leftTmpArray[boolInd] = true;
                            if (boolInd - 1 >= 0) leftTmpArray[boolInd - 1] = true;

                            if (boolInd + 1 <= y1 - y0) leftTmpArray[boolInd + 1] = true;
                        }
                        if ((d == x1 - 2) && (inter >= err)) {
                            rightTmpArray[boolInd] = true;
                        }
                    }
                }

            }

            if (left == 0) {
                left = tmpLeft;
                //if (tmpLeft == 0) System.out.println("Something is wrong");
                width = -x0 - 1;

                leftArr0 = leftArr1;
                leftArr1 = leftTmpArray;

                for (int i = 0; i < coords.size() / 3; i++) {
                    coords.set(i * 3, coords.get(i * 3) - 1);
                }
            } else {
                width = -x0;
            }
            if (right == 0) {
                right = tmpRight;
                //if (tmpRight == 0) System.out.println("Something is wrong");
                width += x1 - 1;

                rightArr0 = rightArr1;
                rightArr1 = rightTmpArray;
            } else {
                width += x1;
            }

            width++;
            height = Math.abs(maxY - minY);
        }

        List<Float> getCoords() {
            return coords;
        }

        List<Float> getAlphas() {
            return alphas;
        }
    }


}
