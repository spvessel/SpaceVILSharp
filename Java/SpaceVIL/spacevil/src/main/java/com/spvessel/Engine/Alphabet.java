package com.spvessel.Engine;

import com.spvessel.Common.ContourService;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.sound.sampled.SourceDataLine;

public class Alphabet {
    // Alphabet
    // class Alphabet {
    Font font;
    Map<Character, Letter> letters;
    // internal Dictionary<char, ModifyLetter> newLetters;
    int alphMinY = Integer.MAX_VALUE;
    int alphMaxY = Integer.MIN_VALUE;
    int alphHeight = 0;
    int lineSpacer;
    Letter bugLetter;

    Alphabet(Font font) {
        this.font = font;
        letters = new HashMap<>();

        makeBugLetter();
        fillABC();
        fillSpecLetters();
    }

    private void fillSpecLetters() {
        char specLet = " ".charAt(0);
        Letter letter = new Letter(" ");
        letters.put(specLet, letter);
        letter.width = letters.get("-".charAt(0)).width;
        letter.height = 0;
        lineSpacer = letter.width;

        specLet = "\t".charAt(0);
        Letter letter1 = new Letter("\t");
        letters.put(specLet, letter1);
        letter1.width = letter.width * 4;
        letter1.height = 0;

        /*
         * //!Может стоит вернуть это, если новые модификации текста будут работать
         * нормально specLet = "\r"[0]; Letter letter2 = new Letter("\t", null);
         * letters.Add(specLet, letter2); letter2.width = 0; letter2.height = 0;
         */
    }

    private int updateSpecX0(Letter letter, int x0) {
        return x0 + 2; // for " " and "\t"
    }

    private void addLetter(char c) {
        Letter letter = makeLetter(Character.toString(c));
        letters.put(c, letter);
        alphMinY = (alphMinY > letter.minY) ? letter.minY : alphMinY;
        alphMaxY = (alphMaxY < letter.minY + letter.height - 1) ? letter.minY + letter.height - 1 : alphMaxY;
        alphHeight = Math.abs(alphMaxY - alphMinY + 1);
        //if (font.getSize() == 18 && (alphMaxY == (letter.minY + letter.height - 1))) System.out.println(letter.name + " " + alphMaxY);
    }

    public List<ModifyLetter> makeTextNew(String text) {
        List<ModifyLetter> letList = new LinkedList<>();

        double err = 0.15; // переехало из буквы
        int x0 = 0;

        Letter currLet;
        Letter prevLet = null;
        for (char c : text.toCharArray()) {
            if (!letters.containsKey(c))
                addLetter(c);

            currLet = letters.get(c);

            if (currLet.isSpec) {
                x0 = updateSpecX0(currLet, x0);
            } else {
                if (prevLet != null) {
                    // int ly0 = prevLet.minY;
                    // int ly1 = ly0 + prevLet.height - 1;
                    // int ry0 = currLet.minY;
                    // int ry1 = ry0 + currLet.height - 1;

                    // boolean b1 = false, b2 = false;
                    // for (int i = Math.max(ly0, ry0); i < Math.min(ly1, ry1); i++) {
                    //     // if (prevLet.alphas[prevLet.width - 1, i - ly0] > err && currLet.alphas[0, i -
                    //     // ry0] > err)
                    //     if (prevLet.rightArr[1][i - ly0] > err && currLet.leftArr[0][i - ry0] > err) {
                    //         b1 = true;
                    //         break;
                    //     }
                    // }

                    // if (b1)
                    //     x0++;
                    // else {
                    //     for (int i = Math.max(ly0, ry0); i < Math.min(ly1, ry1); i++) {
                    //         // if (prevLet.alphas[prevLet.width - 2, i - ly0] > err && currLet.alphas[0, i -
                    //         // ry0] > err)
                    //         if (prevLet.rightArr[0][i - ly0] > err && currLet.leftArr[0][i - ry0] > err) {
                    //             b2 = true;
                    //             break;
                    //         }
                    //         // if (prevLet.alphas[prevLet.width - 1, i - ly0] > err && currLet.alphas[1, i -
                    //         // ry0] > err)
                    //         if (prevLet.rightArr[1][i - ly0] > err && currLet.leftArr[1][i - ry0] > err) {
                    //             b2 = true;
                    //             break;
                    //         }
                    //     }

                    //     //if (!b2)
                    //     //    x0--;
                    // }
                    x0++;
                }
            }

            letList.add(new ModifyLetter(currLet, x0));

            x0 += currLet.width;
            prevLet = currLet;
        }

        return letList;
    }

    private void fillABC() {
        String str = "aj"; // bcdefghijklmnopqrstuvwxyz";
        str += str.toUpperCase();
        str += "-"; // ".,?!1234567890-+=_"; //

        char[] defLetters = str.toCharArray();
        for (char c : defLetters)
            addLetter(c);
    }

    void addMoreLetters() {
        String str = "abcdefghijklmnopqrstuvwxyz";
        str += str.toUpperCase();
        str += "-.,?!1234567890-+=_";

        char[] defLetters = str.toCharArray();
        for (char c : defLetters)
            if (!letters.containsKey(c))
                addLetter(c);
    }

    private Letter makeLetter(String let) {
        /*
         * From C# // font = DefaultsService.GetDefaultFont(); GraphicsPath shape = new
         * GraphicsPath(); StringFormat format = StringFormat.GenericDefault; float
         * emSize = font.getSize(); shape.addString(let, font.getFamily(), (int)
         * font.getStyle(), emSize, new Point2D(0f, 0f), format); try { return new
         * Letter(let, shape); } catch (Exception e) {
         * System.out.println("Bug letter exception"); return bugLetter; }
         */

        // FontRenderContext context = new FontRenderContext(null, false, false);

        // GeneralPath shape = new GeneralPath();
        // TextLayout layout = new TextLayout(let, font, context);

        // AffineTransform transform = AffineTransform.getTranslateInstance(0, 0);

        // Shape outline = layout.getOutline(transform);
        // shape.append(outline, true);
        // try {
        //     return new Letter(let, shape);
        // } catch (Exception e) {
        //     System.out.println("Bug letter exception");
        //     return bugLetter;
        // }

        //Font font = DefaultsService.getDefaultFont(Font.BOLD, 30);
        //String message = "Make a Chance!";

        BufferedImage bi = new BufferedImage(font.getSize() * 2, font.getSize()*2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = bi.createGraphics();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        ig2.setRenderingHints(rh);
        ig2.setFont(font);
        FontMetrics metrics = ig2.getFontMetrics();
        //int stringWidth = metrics.stringWidth(let);
        int stringHeight = metrics.getAscent();
        ig2.setPaint(Color.white);
        ig2.drawString(let, 0, stringHeight);
        
        try {
            return new Letter(let, bi);
        } catch (Exception e) {
            System.out.println("Bug letter exception");
            return bugLetter;
        }
    }

    private void makeBugLetter() {
        bugLetter = new Letter("bug");
        bugLetter.width = (int) (font.getSize() / 3f);// lineSpacer;
        bugLetter.height = (int) (font.getSize() * 2 / 3f);// Math.Abs(maxY - minY + 1);
        bugLetter.minY = (int) (font.getSize() / 3f);// minY;
        bugLetter.isSpec = false;
        float[][] arr = new float[bugLetter.width][bugLetter.height];
        //List<Float> col = new LinkedList<>();
        //List<Float> pix = new LinkedList<>();
        for (int i = 0; i < bugLetter.width; i++) {
            arr[i][0] = 1;
            // col.add(1f);
            // pix.add((float) bugLetter.minX + i);
            // pix.add((float) bugLetter.minY);
            // pix.add(0f);
            arr[i][bugLetter.height - 1] = 1;
            // col.add(1f);
            // pix.add((float) bugLetter.minX + i);
            // pix.add((float) bugLetter.minY + bugLetter.height - 1);
            // pix.add(0f);
        }
        for (int i = 1; i < bugLetter.height - 1; i++) {
            arr[0][i] = 1;
            // col.add(1f);
            // pix.add((float) bugLetter.minX);
            // pix.add((float) bugLetter.minY + i);
            // pix.add(0f);
            arr[bugLetter.width - 1][i] = 1;
            // col.add(1f);
            // pix.add((float) bugLetter.minX + bugLetter.width - 1);
            // pix.add((float) bugLetter.minY + i);
            // pix.add(0f);

        }

        // bugLetter.alphas = arr;
        // bugLetter.col.addAll(col);
        // for (Float var : col) {
        //     bugLetter.col.add(var);
        // }
        // // bugLetter.pix.addAll(pix);
        // for (Float var : pix) {
        //     bugLetter.pix.add(var);
        // }

        bugLetter.twoDimToOnedim(arr);
    }
    // }

    class Letter {
        String name;
        int width;
        int height;
        int minY = 0;
        int minX = 0;
        boolean isSpec = false;
        float[][] leftArr;
        float[][] rightArr;
        //List<Float> col;
        //List<Float> pix;
        byte[] arr;

        public Letter(String name, Shape shape) {// GraphicsPath shape) {
            // System.out.println("make letter " + name);
            //col = new LinkedList<>();
            //pix = new LinkedList<>();

            this.name = name;
            if (shape != null)
                makeLetterArrays(shape);
            else
                isSpec = true;
        }

        public Letter(String name, BufferedImage bi) {
            this.name = name;
            bitmapToArray(bi);
        }

        public Letter(String name) {
            // for bug letter
            this.name = name;
            isSpec = true;
        }

        void twoDimToOnedim(float[][] twoDim) {
            arr = new byte[(this.width * this.height) * 4];
            // System.out.println(name + " width " + this.width + " " + this.height);
            int i = 0;
            for (int xx = 0; xx < width; xx++) {
                for (int yy = 0; yy < height; yy++) {
                    arr[i] = (byte)255;
                    i++;
                    arr[i] = (byte)255;
                    i++;
                    arr[i] = (byte)255;
                    i++;
                    arr[i] = (byte) (twoDim[xx][yy] * 255);
                    i++;
                }
            }
        }

        private void makeLetterArrays(Shape shape) {
            Rectangle2D rec = shape.getBounds2D();
            int x0 = (int) Math.floor(rec.getMinX());
            int x1 = (int) Math.ceil(rec.getMaxX());
            int y0 = (int) Math.floor(rec.getMinY());
            int y1 = (int) Math.ceil(rec.getMaxY());

            int height, width;
            height = (y1 - y0 + 1);
            width = (x1 - x0 + 1);

            double[][] alph = new double[width][height];

            minX = x0;
            minY = y0;
            //maxY = y1;

            int boolInd;

            for (int dd = y0; dd <= y1; dd++) {
                boolInd = (dd - y0);

                for (int d = x0; d <= x1; d++) {
                    if (shape.contains(d, dd)) {
                        alph[d - x0][boolInd] = 1;
                        /*
                         * coords.add((float) (d - minX)); coords.add((float) dd); coords.add(0.0f);
                         * 
                         * alphas.add(1.0f);
                         */

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

                        alph[d - x0][boolInd] = inter; // TODO: значения здесь можно усилить
                        /*
                         * coords.add((float) (d - minX)); coords.add((float) dd); coords.add(0.0f);
                         * 
                         * if (inter < 1.0f) inter *= 1.25; alphas.add((float) (inter));
                         * 
                         * if (!(shape.contains(d - 0.5, dd - 0.5) || shape.contains(d + 0.5, dd - 0.5)
                         * || shape.contains(d - 0.5, dd + 0.5) || shape.contains(d + 0.5, dd + 0.5) ||
                         * shape.contains(d - 0.25, dd - 0.25) || shape.contains(d + 0.25, dd - 0.25) ||
                         * shape.contains(d - 0.25, dd + 0.25) || shape.contains(d + 0.25, dd + 0.25)))
                         * { System.out.println(name + " it happens " + inter); }
                         */
                    }
                }

            }

            // --------------------------------------------------------------------------------------

            int x0shift = 0;
            boolean isBraked = false;
            while (x0shift < width) {
                for (int i = 0; i < height; i++) {
                    if (alph[x0shift][i] > 0) {
                        isBraked = true;
                        break;
                    }
                }
                if (isBraked)
                    break;
                x0shift++;
            }

            int x1shift = width - 1;
            isBraked = false;
            while (x1shift >= 0) {
                for (int i = 0; i < height; i++) {
                    if (alph[x1shift][i] > 0)
                        isBraked = true;
                }
                if (isBraked)
                    break;
                x1shift--;
            }

            int y0shift = 0;
            isBraked = false;
            while (y0shift < height) {
                for (int i = 0; i < width; i++) {
                    if (alph[i][y0shift] > 0)
                        isBraked = true;
                }
                if (isBraked)
                    break;
                y0shift++;
            }

            int y1shift = height - 1;
            isBraked = false;
            while (y1shift >= 0) {
                for (int i = 0; i < width; i++) {
                    if (alph[i][y1shift] > 0)
                        isBraked = true;
                }
                if (isBraked)
                    break;
                y1shift--;
            }

            minX = 0;// x0 + x0shift;
            minY = y0 + y0shift;

            this.height = y1shift - y0shift + 1;
            this.width = x1shift - x0shift + 1;

            // --------------------------------------------------------------------------------------

            arr = new byte[(this.width * this.height) * 4];
            // System.out.println(name + " width " + this.width + " " + this.height);
            int i = 0;
            for (int xx = x0shift; xx <= x1shift; xx++) {
                for (int yy = y0shift; yy <= y1shift; yy++) {
                    arr[i] = (byte)255;
                    i++;
                    arr[i] = (byte)255;
                    i++;
                    arr[i] = (byte)255;
                    i++;
                    arr[i] = (byte) (alph[xx][yy] * 255);
                    i++;
                }
            }
            /*
            for (int xx = x0shift; xx <= x1shift; xx++) {
                for (int yy = y0shift; yy <= y1shift; yy++) {

                    if (alph[xx][yy] != 0) {
                        col.add((float) alph[xx][yy]);
                        pix.add((float) xx - x0shift);
                        pix.add((float) yy - y0shift);
                        pix.add(0f);
                    }

                }
            }
            */
            leftArr = new float[2][height];
            rightArr = new float[2][height];
            for (int yy = y0shift; yy <= y1shift; yy++) {
                int xx = x0shift;
                leftArr[0][yy - y0shift] = (float) alph[xx][yy];
                if (xx + 1 < width)
                    leftArr[1][yy - y0shift] = (float) alph[xx + 1][yy];
                xx = x1shift;
                if (xx - 1 >= 0)
                    rightArr[0][yy - y0shift] = (float) alph[xx - 1][yy];
                rightArr[1][yy - y0shift] = (float) alph[xx][yy];

            }

        }

        /*
         * From C# private void makeLetterArrays(GraphicsPath shape) { Rectangle2D rec =
         * shape.getBounds2D(); //int x0 = (int)Math.Floor(rec.Left); //int x1 =
         * (int)Math.Ceiling(rec.Right); int y0;// = (int)Math.Floor(rec.Top); //int y1
         * = (int)Math.Ceiling(rec.Bottom);
         * 
         * //LogService.Log().LogOne(rec.Top, "Let " + name);
         * 
         * ContourService.CrossOut crossOut = ContourService.crossContours(shape);
         * double[][] alph = crossOut._arr; y0 = crossOut._minY;
         * 
         * height = alph.length;// y1 - y0 + 1; width = alph[0].length;// x1 - x0 + 1;
         * int x0shift = 0; boolean isBraked = false; while (x0shift < width) { for (int
         * i = 0; i < height; i++) { if (alph[x0shift][i] > 0) { isBraked = true; break;
         * } } if (isBraked) break; x0shift++; }
         * 
         * int x1shift = width - 1; isBraked = false; while (x1shift >= 0) { for (int i
         * = 0; i < height; i++) { if (alph[x1shift][i] > 0) isBraked = true; } if
         * (isBraked) break; x1shift--; }
         * 
         * int y0shift = 0; isBraked = false; while (y0shift < height) { for (int i = 0;
         * i < width; i++) { if (alph[i][y0shift] > 0) isBraked = true; } if (isBraked)
         * break; y0shift++; }
         * 
         * int y1shift = height - 1; isBraked = false; while (y1shift >= 0) { for (int i
         * = 0; i < width; i++) { if (alph[i][y1shift] > 0) isBraked = true; } if
         * (isBraked) break; y1shift--; }
         * 
         * minX = 0;// x0 + x0shift; minY = y0 + y0shift;
         * 
         * height = y1shift - y0shift + 1; width = x1shift - x0shift + 1;
         * 
         * //---------------------------------------------------------------------------
         * -----------
         * 
         * for (int xx = x0shift; xx <= x1shift; xx++) { for (int yy = y0shift; yy <=
         * y1shift; yy++) { if (alph[xx][yy] != 0) { col.add((float) alph[xx][yy]);
         * pix.add((float) xx - x0shift); pix.add((float) yy - y0shift); pix.add(0f); }
         * 
         * } }
         * 
         * leftArr = new float[2][height]; rightArr = new float[2][height]; for (int yy
         * = y0shift; yy <= y1shift; yy++) { int xx = x0shift; leftArr[0][yy - y0shift]
         * = (float) alph[xx][yy]; if (xx + 1 < width) leftArr[1][yy - y0shift] =
         * (float) alph[xx + 1][yy]; xx = x1shift; if (xx - 1 >= 0) rightArr[0][yy -
         * y0shift] = (float) alph[xx - 1][yy]; rightArr[1][yy - y0shift] = (float)
         * alph[xx][yy];
         * 
         * } }
         */

         private void bitmapToArray(BufferedImage bi) {
            int x0shift = 0;
            boolean isBraked = false;
            byte[] bytes;
            while (x0shift < bi.getWidth()) {
                for (int i = 0; i < bi.getHeight(); i++) {
                    bytes = ByteBuffer.allocate(4).putInt(bi.getRGB(x0shift, i)).array();                
                    if (bytes[0] != 0) {
                        isBraked = true;
                        break;
                    }
                }
                if (isBraked)
                    break;
                x0shift++;
            }

            int x1shift = bi.getWidth() - 1;
            isBraked = false;
            while (x1shift >= 0) {
                for (int i = 0; i < bi.getHeight(); i++) {
                    bytes = ByteBuffer.allocate(4).putInt(bi.getRGB(x1shift, i)).array();
                    if (bytes[0] != 0)
                        isBraked = true;
                }
                if (isBraked)
                    break;
                x1shift--;
            }

            int y0shift = 0;
            isBraked = false;
            while (y0shift < bi.getHeight()) {
                for (int i = 0; i < bi.getWidth(); i++) {
                    bytes = ByteBuffer.allocate(4).putInt(bi.getRGB(i, y0shift)).array();
                    if (bytes[0] != 0)
                        isBraked = true;
                }
                if (isBraked)
                    break;
                y0shift++;
            }

            int y1shift = bi.getHeight() - 1;
            isBraked = false;
            while (y1shift >= 0) {
                for (int i = 0; i < bi.getWidth(); i++) {
                    bytes = ByteBuffer.allocate(4).putInt(bi.getRGB(i, y1shift)).array();
                    if (bytes[0] != 0)
                        isBraked = true;
                }
                if (isBraked)
                    break;
                y1shift--;
            }

            minX = 0;// x0 + x0shift;
            minY = y0shift;

            this.height = y1shift - y0shift + 1;
            this.width = x1shift - x0shift + 1;
            //System.out.println(name + " height " + y1shift + " " + y0shift);
            // --------------------------------------------------------------------------------------

            arr = new byte[(this.width * this.height) * 4];
            int i = 0;
            for (int xx = x0shift; xx <= x1shift; xx++) {
                for (int yy = y0shift; yy <= y1shift; yy++) {
                    bytes = ByteBuffer.allocate(4).putInt(bi.getRGB(xx, yy)).array();
                    arr[i] = bytes[1]; //(byte) 255;
                    i++;
                    arr[i] = bytes[2]; //(byte) 255;
                    i++;
                    arr[i] = bytes[3]; //(byte) 255;
                    i++;
                    arr[i] = bytes[0]; //(byte) (alph[xx][yy] * 255);
                    i++;
                }
            }

            leftArr = new float[2][this.height];
            rightArr = new float[2][this.height];
            for (int yy = y0shift; yy <= y1shift; yy++) {
                int xx = x0shift;
                bytes = ByteBuffer.allocate(4).putInt(bi.getRGB(xx, yy)).array();
                leftArr[0][yy - y0shift] = (float) (bytes[0] * 1f / 255f);
                if (xx + 1 < this.width) {
                    bytes = ByteBuffer.allocate(4).putInt(bi.getRGB(xx + 1, yy)).array();
                    leftArr[1][yy - y0shift] = (float) (bytes[0] * 1f / 255f);
                }
                xx = x1shift;
                if (xx - 1 >= 0) {
                    bytes = ByteBuffer.allocate(4).putInt(bi.getRGB(xx - 1, yy)).array();
                    rightArr[0][yy - y0shift] = (float) (bytes[0] * 1f / 255f);
                }
                bytes = ByteBuffer.allocate(4).putInt(bi.getRGB(xx, yy)).array();
                rightArr[1][yy - y0shift] = (float) (bytes[0] * 1f / 255f);

            }
         }
    }

    public class ModifyLetter {
        String name;
        public int xBeg = 0;
        public int yBeg = 0;
        public int width = 0;
        public int height = 0;
        public int xShift = 0;
        private Letter _letter;
        boolean isSpec;

        ModifyLetter(Letter let, int xShift) {
            this.xShift = xShift;
            name = let.name;
            width = let.width;
            height = let.height;
            yBeg = let.minY;
            xBeg = let.minX;
            isSpec = let.isSpec;
            _letter = let;
        }
        /*
        public List<Float> getCol() {
            return _letter.col;
        }

        public List<Float> getPix() {
            return _letter.pix;
        }
        */
        public byte[] getArr() {
            return _letter.arr;
        }
    }
}
