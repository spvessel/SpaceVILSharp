using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Text;

namespace SpaceVIL
{
    static internal class FontEngine
    {
        static String _preloadDefFile = "./somefile.dat";

        static Dictionary<Font, Alphabet> fonts = new Dictionary<Font, Alphabet>();

        static FontEngine()
        {

        }

        internal static List<ModifyLetter> GetPixMap(string text, Font font)
        {
            //return FontReview.getTextArrays(text, font);

            // Font biggerFont = font;
            // float cof = 1f;

            // if (font.Size < 20) {
            //     biggerFont = new Font(font.FontFamily, 50, font.Style);
            //     cof = font.Size * 1f / 50f;
            // }

            if (!fonts.ContainsKey(font))
            {
                fonts.Add(font, new Alphabet(font));
            }
            return fonts[font].MakeTextNew(text); //, cof);
        }

        internal static int[] GetSpacerDims(Font font)
        {
            if (font == null)
                return new int[] { 0, 0, 0 };
            //return FontReview.getDims();

            // Font biggerFont = font;
            // float cof = 1f;
            // if (font.Size < 20) {
            //     biggerFont = new Font(font.FontFamily, 50, font.Style);
            //     cof = font.Size * 1f / 50f;
            // }

            if (!fonts.ContainsKey(font))
            {
                fonts.Add(font, new Alphabet(font));
            }
            Alphabet a = fonts[font];
            return new int[] { a.lineSpacer, a.alphMinY, a.alphHeight }; //(int)(a.lineSpacer * cof), (int)(a.alphMinY * cof), (int)(a.alphHeight * cof), a.alphHeight, a.alphMinY };
        }

        internal static bool SavePreloadFont(Font font)
        {
            if (!fonts.ContainsKey(font))
            {
                fonts.Add(font, new Alphabet(font));
            }

            fonts[font].AddMoarLetters(); //Заполнить весь алфавит
            //Сохранить в файл или куда там
            return true;
        }

        //Alphabet
        private class Alphabet
        {
            internal Font font;
            internal Dictionary<char, Letter> letters;
            internal int alphMinY = Int32.MaxValue;
            internal int alphMaxY = Int32.MinValue;
            internal int alphHeight = 0;
            internal int lineSpacer;
            internal Letter bugLetter;

            public Alphabet(Font font)
            {
                this.font = font;
                letters = new Dictionary<char, Letter>();
                //Console.WriteLine("Make " + font.FontFamily + " " + font.Size);
                MakeBugLetter();
                FillABC();
                FillSpecLetters();
            }

            private void FillSpecLetters()
            {
                char specLet = " "[0];
                Letter letter = new Letter(" ");
                letters.Add(specLet, letter);
                letter.width = letters["-"[0]].width;
                letter.height = 0;
                lineSpacer = (int)letter.width;

                specLet = "\t"[0];
                Letter letter1 = new Letter("\t");
                letters.Add(specLet, letter1);
                letter1.width = letter.width * 4;
                letter1.height = 0;
                /*
                //!Может стоит вернуть это, если новые модификации текста будут работать нормально
                specLet = "\r"[0];
                Letter letter2 = new Letter("\t", null);
                letters.Add(specLet, letter2);
                letter2.width = 0;
                letter2.height = 0;
                */
            }

            private int UpdateSpecX0(Letter letter, int x0)
            {
                return x0 + 2; //for " " and "\t"
            }

            private void AddLetter(char c)
            {
                //Console.WriteLine(font.Name + " " + font.Size + " Add letter " + c);
                Letter letter = MakeLetter(char.ToString(c));
                letters.Add(c, letter);
                alphMinY = (alphMinY > letter.minY) ? letter.minY : alphMinY;
                alphMaxY = (alphMaxY < letter.minY + letter.height - 1) ? letter.minY + letter.height - 1 : alphMaxY;
                alphHeight = Math.Abs(alphMaxY - alphMinY + 1);
            }
            /*
            internal PixMapData MakeText(String text)
            {
                double err = 0.15; //переехало из буквы
                double x0 = 0;

                List<float> pix = new List<float>();
                List<float> col = new List<float>();
                List<int> letEndPos = new List<int>();

                Letter currLet;
                Letter prevLet = null;
                foreach (char c in text.ToCharArray())
                {
                    if (!letters.ContainsKey(c)) AddLetter(c);

                    currLet = letters[c];

                    if (currLet.isSpec)
                    {
                        x0 = UpdateSpecX0(currLet, x0);
                    }
                    else
                    {
                        if (prevLet != null)
                        {
                            int ly0 = prevLet.minY;
                            int ly1 = ly0 + prevLet.height - 1;
                            int ry0 = currLet.minY;
                            int ry1 = ry0 + currLet.height - 1;

                            bool b1 = false, b2 = false;
                            for (int i = Math.Max(ly0, ry0); i < Math.Min(ly1, ry1); i++)
                            {
                                if (prevLet.alphas[prevLet.width - 1, i - ly0] > err && currLet.alphas[0, i - ry0] > err)
                                {
                                    b1 = true;
                                    break;
                                }
                            }

                            if (b1) x0++;
                            else
                            {
                                for (int i = Math.Max(ly0, ry0); i < Math.Min(ly1, ry1); i++)
                                {
                                    if (prevLet.alphas[prevLet.width - 2, i - ly0] > err && currLet.alphas[0, i - ry0] > err)
                                    {
                                        b2 = true;
                                        break;
                                    }
                                    if (prevLet.alphas[prevLet.width - 1, i - ly0] > err && currLet.alphas[1, i - ry0] > err)
                                    {
                                        b2 = true;
                                        break;
                                    }
                                }

                                if (!b2) x0--;
                            }
                        }


                        //col.AddRange(currLet.GetAlphas());
                        //pix.AddRange(AddShift((float)x0, currLet.GetCoords()));

                        //Вместо AddShift

                        int xBeg = currLet.minX;
                        int yBeg = currLet.minY;
                        for (int ix = 0; ix < currLet.width; ix++)
                        {
                            for (int iy = 0; iy < currLet.height; iy++)
                            {
                                if (currLet.alphas[ix, iy] > 0)
                                {
                                    col.Add((float)currLet.alphas[ix, iy]);
                                    pix.Add(ix + xBeg + (float)x0);
                                    pix.Add(iy + yBeg);
                                    pix.Add(0);
                                }
                            }
                        }

                    }

                    x0 += currLet.width;
                    prevLet = currLet;
                    letEndPos.Add((int)x0);
                }

                return new PixMapData(pix, col, letEndPos);// (float)x0);
            }
            */

            internal List<ModifyLetter> MakeTextNew(String text) //, float cof)
            {
                //Console.WriteLine(text);
                List<ModifyLetter> letList = new List<ModifyLetter>();


                double err = 0.25; //переехало из буквы
                int x0 = 0;

                Letter currLet;
                Letter prevLet = null;
                foreach (char c in text.ToCharArray())
                {
                    if (!letters.ContainsKey(c)) AddLetter(c);

                    currLet = letters[c];

                    if (currLet.isSpec)
                    {
                        x0 = UpdateSpecX0(currLet, x0);
                    }
                    else
                    {
                        
                        // if (prevLet != null)
                        // {
                        //     int ly0 = prevLet.minY;
                        //     int ly1 = ly0 + prevLet.height - 1;
                        //     int ry0 = currLet.minY;
                        //     int ry1 = ry0 + currLet.height - 1;

                        //     bool b1 = false, b2 = false;
                        //     for (int i = Math.Max(ly0, ry0); i < Math.Min(ly1, ry1); i++)
                        //     {
                        //         //if (prevLet.alphas[prevLet.width - 1, i - ly0] > err && currLet.alphas[0, i - ry0] > err)
                        //         if (prevLet.rightArr[1, i - ly0] > err && currLet.leftArr[0, i - ry0] > err)
                        //         {
                        //             b1 = true;
                        //             break;
                        //         }
                        //     }

                        //     if (b1) x0++;
                        //     else
                        //     {
                        //         for (int i = Math.Max(ly0, ry0); i < Math.Min(ly1, ry1); i++)
                        //         {
                        //             //if (prevLet.alphas[prevLet.width - 2, i - ly0] > err && currLet.alphas[0, i - ry0] > err)
                        //             if (prevLet.rightArr[0, i - ly0] > err && currLet.leftArr[0, i - ry0] > err)
                        //             {
                        //                 b2 = true;
                        //                 break;
                        //             }
                        //             //if (prevLet.alphas[prevLet.width - 1, i - ly0] > err && currLet.alphas[1, i - ry0] > err)
                        //             if (prevLet.rightArr[1, i - ly0] > err && currLet.leftArr[1, i - ry0] > err)
                        //             {
                        //                 b2 = true;
                        //                 break;
                        //             }
                        //         }

                        //         if (!b2) x0--;
                        //     }
                        // }
                         
                        //  x0++;
                         x0++;
                    }

                    letList.Add(new ModifyLetter(currLet, x0)); //, cof));

                    x0 += currLet.width;
                    prevLet = currLet;
                    //letEndPos.Add(x0);
                }

                return letList;
            }

            private void FillABC()
            {
                String str = "aj"; // bcdefghijklmnopqrstuvwxyz";
                str += str.ToUpper();
                str += "-"; //".,?!1234567890-+=_"; //


                char[] defLetters = str.ToCharArray();
                foreach (char c in defLetters)
                    AddLetter(c);
            }

            internal void AddMoarLetters()
            {
                String str = "abcdefghijklmnopqrstuvwxyz";
                str += str.ToUpper();
                str += "-.,?!1234567890-+=_";


                char[] defLetters = str.ToCharArray();
                foreach (char c in defLetters)
                    if (!letters.ContainsKey(c))
                        AddLetter(c);
            }

            private Letter MakeLetter(String let)
            {
                // GraphicsPath shape = new GraphicsPath();
                // StringFormat format = StringFormat.GenericTypographic;
                // float emSize = font.Height * font.FontFamily.GetCellAscent(font.Style) / font.FontFamily.GetEmHeight(font.Style);
                // // float emSize = font.Size;
                // shape.AddString(let, font.FontFamily, (int)font.Style, emSize, new PointF(0f, 0f), format);
                // try
                // {
                //     return new Letter(let, shape);
                // }
                // catch (Exception)
                // {
                //     Console.WriteLine("Bug letter exception");
                //     return bugLetter;
                // }


                Bitmap bm = new Bitmap((int)(font.Size * 2), (int)(font.Size * 2));
                Graphics g = Graphics.FromImage(bm);
                g.SmoothingMode = SmoothingMode.HighQuality;
                // g.InterpolationMode = InterpolationMode.HighQualityBilinear;
                // g.PixelOffsetMode = PixelOffsetMode.HighQuality; // or PixelOffsetMode.Half
                g.TextRenderingHint = TextRenderingHint.AntiAlias;
                g.DrawString(let, font, Brushes.White, new PointF(0f, 0f));
                g.Dispose();

                try
                {
                    return new Letter(let, bm);
                }
                catch (Exception)
                {
                    Console.WriteLine("Bug letter exception");
                    return bugLetter;
                }

            }
            /*
            private List<float> AddShift(float x0, List<float> coord)
            {
                List<float> outCoord = new List<float>();
                float f;
                for (int i = 0; i < coord.Count(); i += 3)
                {
                    f = coord[i] + x0;

                    outCoord.Add(f);
                    f = coord[i + 1];

                    outCoord.Add(f);
                    f = coord[i + 2];
                    outCoord.Add(f);
                }

                return outCoord;
            }
            */

            private void MakeBugLetter()
            {
                bugLetter = new Letter("bug");
                bugLetter.width = (int)(font.Size / 3f);// lineSpacer;
                bugLetter.height = (int)(font.Size * 2 / 3f);// Math.Abs(maxY - minY + 1);
                bugLetter.minY = (int)(font.Size / 3f);// minY;
                bugLetter.isSpec = false;
                float[,] arr = new float[bugLetter.width, bugLetter.height];
                //List<float> col = new List<float>();
                //List<float> pix = new List<float>();
                for (int i = 0; i < bugLetter.width; i++)
                {
                    arr[i, 0] = 1;
                    // col.Add(1);
                    // pix.Add(bugLetter.minX + i);
                    // pix.Add(bugLetter.minY);
                    // pix.Add(0f);
                    arr[i, bugLetter.height - 1] = 1;
                    // col.Add(1);
                    // pix.Add(bugLetter.minX + i);
                    // pix.Add(bugLetter.minY + bugLetter.height - 1);
                    // pix.Add(0f);
                }
                for (int i = 1; i < bugLetter.height - 1; i++)
                {
                    arr[0, i] = 1;
                    // col.Add(1);
                    // pix.Add(bugLetter.minX);
                    // pix.Add(bugLetter.minY + i);
                    // pix.Add(0f);
                    arr[bugLetter.width - 1, i] = 1;
                    // col.Add(1);
                    // pix.Add(bugLetter.minX + bugLetter.width - 1);
                    // pix.Add(bugLetter.minY + i);
                    // pix.Add(0f);
                }

                //bugLetter.alphas = arr;
                //bugLetter.col.AddRange(col);
                //bugLetter.pix.AddRange(pix);

                bugLetter.TwoDimToOne(arr);
            }
        }

        internal class Letter
        {
            internal String name;
            internal int width;
            internal int height;
            //internal double[,] alphas;
            internal int minY = 0;
            internal int minX = 0;
            internal bool isSpec = false;
            internal float[,] leftArr;
            internal float[,] rightArr;
            //internal List<float> col;
            //internal List<float> pix;
            internal byte[] arr;

            public Letter(String name)
            {
                //for bug letter
                this.name = name;
                isSpec = true;
            }

            public Letter(String name, GraphicsPath shape)
            {
                //col = new List<float>();
                //pix = new List<float>();
                //Console.WriteLine("Creating letter " + name);
                this.name = name;
                if (shape != null)
                    MakeLetterArrays(shape);
                else isSpec = true;
                //Console.Write(name + " ");
                //LogService.Log().LogArr(new int[] {minY, height}, "Let " + name);
            }

            public Letter(String name, Bitmap bm)
            {
                this.name = name;
                BitmapToArrays(bm);
            }

            internal void TwoDimToOne(float[,] twoDim)
            {
                arr = new byte[(this.width * this.height) * 4];
                // System.out.println(name + " width " + this.width + " " + this.height);
                int i = 0;
                for (int xx = 0; xx < width; xx++)
                {
                    for (int yy = 0; yy < height; yy++)
                    {
                        arr[i] = (byte)255;
                        i++;
                        arr[i] = (byte)255;
                        i++;
                        arr[i] = (byte)255;
                        i++;
                        arr[i] = (byte)(twoDim[xx, yy] * 255);
                        i++;
                    }
                }
            }

            private void MakeLetterArrays(GraphicsPath shape)
            {
                RectangleF rec = shape.GetBounds();
                //int x0 = (int)Math.Floor(rec.Left);
                //int x1 = (int)Math.Ceiling(rec.Right);
                int y0;// = (int)Math.Floor(rec.Top);
                //int y1 = (int)Math.Ceiling(rec.Bottom);

                //LogService.Log().LogOne(rec.Top, "Let " + name);
                CrossOut crossOut = ContourService.CrossContours(shape);
                double[,] alph = crossOut._arr;
                y0 = crossOut._minY;

                height = alph.GetLength(1);// y1 - y0 + 1;
                width = alph.GetLength(0);// x1 - x0 + 1;
                int x0shift = 0;
                bool isBraked = false;
                while (x0shift < width)
                {
                    for (int d = 0; d < height; d++)
                    {
                        if (alph[x0shift, d] > 0)
                        {
                            isBraked = true;
                            break;
                        }
                    }
                    if (isBraked) break;
                    x0shift++;
                }

                int x1shift = width - 1;
                isBraked = false;
                while (x1shift >= 0)
                {
                    for (int d = 0; d < height; d++)
                    {
                        if (alph[x1shift, d] > 0) isBraked = true;
                    }
                    if (isBraked) break;
                    x1shift--;
                }

                int y0shift = 0;
                isBraked = false;
                while (y0shift < height)
                {
                    for (int d = 0; d < width; d++)
                    {
                        if (alph[d, y0shift] > 0) isBraked = true;
                    }
                    if (isBraked) break;
                    y0shift++;
                }

                int y1shift = height - 1;
                isBraked = false;
                while (y1shift >= 0)
                {
                    for (int d = 0; d < width; d++)
                    {
                        if (alph[d, y1shift] > 0) isBraked = true;
                    }
                    if (isBraked) break;
                    y1shift--;
                }

                minX = 0;// x0 + x0shift;
                minY = y0 + y0shift;

                height = y1shift - y0shift + 1;
                width = x1shift - x0shift + 1;

                //--------------------------------------------------------------------------------------

                arr = new byte[(this.width * this.height) * 4];
                // System.out.println(name + " width " + this.width + " " + this.height);
                int i = 0;
                for (int xx = x0shift; xx <= x1shift; xx++)
                {
                    for (int yy = y0shift; yy <= y1shift; yy++)
                    {
                        arr[i] = (byte)255;
                        i++;
                        arr[i] = (byte)255;
                        i++;
                        arr[i] = (byte)255;
                        i++;
                        // if (alph[xx, yy] < 1)
                        //     arr[i] = (byte) (0 * 255);
                        // else
                        arr[i] = (byte)(alph[xx, yy] * 255);
                        i++;
                    }
                }

                /*
                for (int xx = x0shift; xx <= x1shift; xx++)
                {
                    for (int yy = y0shift; yy <= y1shift; yy++)
                    {
                        if (alph[xx, yy] != 0)
                        {
                            col.Add((float)alph[xx, yy]);
                            pix.Add(xx - x0shift);
                            pix.Add(yy - y0shift);
                            pix.Add(0f);
                        }

                    }
                }
                */
                leftArr = new float[2, height];
                rightArr = new float[2, height];
                for (int yy = y0shift; yy <= y1shift; yy++)
                {
                    int xx = x0shift;
                    leftArr[0, yy - y0shift] = (float)alph[xx, yy];
                    if (xx + 1 < width) leftArr[1, yy - y0shift] = (float)alph[xx + 1, yy];
                    xx = x1shift;
                    if (xx - 1 >= 0) rightArr[0, yy - y0shift] = (float)alph[xx - 1, yy];
                    rightArr[1, yy - y0shift] = (float)alph[xx, yy];

                }

                //--------------------------------------------------------------------------------------
                /*
                alphas = new double[width, height];
                for (int xx = x0shift; xx <= x1shift; xx++)
                {
                    for (int yy = y0shift; yy <= y1shift; yy++)
                    {
                        // if (alph[xx, yy] != 0.0f)
                        // {
                        //     if (alph[xx, yy] < 0.21f)
                        //         alph[xx, yy] = 0.0f;
                        //     //alph[xx, yy] = (alph[xx, yy] < 1) ? alph[xx, yy] + 0.15f : alph[xx, yy];
                        //     //alph[xx, yy] = (alph[xx, yy] > 1) ? 1 : alph[xx, yy];
                        // }
                        
                        alphas[xx - x0shift, yy - y0shift] = alph[xx, yy];

                    }
                }
                */
            }

            private void BitmapToArrays(Bitmap bm)
            {

                int x0shift = 0;
                bool isBraked = false;
                while (x0shift < bm.Width)
                {
                    for (int d = 0; d < bm.Height; d++)
                    {
                        if (bm.GetPixel(x0shift, d).A > 0)
                        {
                            isBraked = true;
                            break;
                        }
                    }
                    if (isBraked) break;
                    x0shift++;
                }

                int x1shift = bm.Width - 1;
                isBraked = false;
                while (x1shift >= 0)
                {
                    for (int d = 0; d < bm.Height; d++)
                    {
                        if (bm.GetPixel(x1shift, d).A > 0) isBraked = true;
                    }
                    if (isBraked) break;
                    x1shift--;
                }

                int y0shift = 0;
                isBraked = false;
                while (y0shift < bm.Height)
                {
                    for (int d = 0; d < bm.Width; d++)
                    {
                        if (bm.GetPixel(d, y0shift).A > 0) isBraked = true;
                    }
                    if (isBraked) break;
                    y0shift++;
                }

                int y1shift = bm.Height - 1;
                isBraked = false;
                while (y1shift >= 0)
                {
                    for (int d = 0; d < bm.Width; d++)
                    {
                        if (bm.GetPixel(d, y1shift).A > 0) isBraked = true;
                    }
                    if (isBraked) break;
                    y1shift--;
                }

                minX = 0;// x0 + x0shift;
                minY = y0shift;

                height = y1shift - y0shift + 1;
                width = x1shift - x0shift + 1;
                //--------------------------------------------------------------------------------------
                arr = new byte[(this.width * this.height) * 4];
                // System.out.println(name + " width " + this.width + " " + this.height);
                int i = 0;
                for (int xx = x0shift; xx <= x1shift; xx++)
                {
                    for (int yy = y0shift; yy <= y1shift; yy++)
                    {
                        arr[i] = bm.GetPixel(xx, yy).R;
                        i++;
                        arr[i] = bm.GetPixel(xx, yy).G;
                        i++;
                        arr[i] = bm.GetPixel(xx, yy).B;
                        i++;
                        arr[i] = bm.GetPixel(xx, yy).A;
                        i++;
                    }
                }

                leftArr = new float[2, height];
                rightArr = new float[2, height];
                for (int yy = y0shift; yy <= y1shift; yy++)
                {
                    int xx = x0shift;
                    leftArr[0, yy - y0shift] = (float) (bm.GetPixel(xx, yy).A / 255f);
                    if (xx + 1 < width) leftArr[1, yy - y0shift] = (float) (bm.GetPixel(xx, yy).A / 255f);
                    xx = x1shift;
                    if (xx - 1 >= 0) rightArr[0, yy - y0shift] = (float) (bm.GetPixel(xx, yy).A / 255f);
                    rightArr[1, yy - y0shift] = (float) (bm.GetPixel(xx, yy).A / 255f);

                }
                //--------------------------------------------------------------------------------------
            }

            #region OldThings
            /*
            private void MakeLetterArraysOld(GraphicsPath shape)
            {
                RectangleF rec = shape.GetBounds();
                int x0 = (int)Math.Floor(rec.Left);
                int x1 = (int)Math.Ceiling(rec.Right);
                int y0 = (int)Math.Floor(rec.Top);
                int y1 = (int)Math.Ceiling(rec.Bottom);
                
                GraphicsPathIterator myPathIterator = new GraphicsPathIterator(shape);
                // Rewind the iterator.
                myPathIterator.Rewind();
                // Create the GraphicsPath section.
                GraphicsPath myPathSection = new GraphicsPath();
                int subpathPoints;
                bool IsClosed2;

                double[,] alph = new double[x1 - x0 + 1, y1 - y0 + 1];

                Dictionary<int, List<InOutCoord>> crossY = new Dictionary<int, List<InOutCoord>>();
                Dictionary<int, List<InOutCoord>> crossX = new Dictionary<int, List<InOutCoord>>();

                double xx1, xx2, yy1, yy2;

                for (int i = 0; i < myPathIterator.SubpathCount; i++)
                {
                    subpathPoints = myPathIterator.NextSubpath(myPathSection, out IsClosed2);

                    List<PointF> subpathList = new List<PointF>(); //Список всех точек замкнутой фигуры

                    float xcurr = myPathSection.PathPoints[myPathSection.PathPoints.Length - 1].X;
                    float ycurr = myPathSection.PathPoints[myPathSection.PathPoints.Length - 1].Y;
                    float xprev, yprev, xnext, ynext;
                    for (int j = 0; j < myPathSection.PathPoints.Length - 1; j++)
                    {
                        xprev = xcurr;
                        yprev = ycurr;
                        xcurr = myPathSection.PathPoints[j].X;
                        ycurr = myPathSection.PathPoints[j].Y;
                        xnext = myPathSection.PathPoints[j + 1].X;
                        ynext = myPathSection.PathPoints[j + 1].Y;

                        if (xprev == xcurr && xcurr == xnext) continue;

                        if (yprev == ycurr && ycurr == ynext) continue;

                        if ((Math.Truncate(xcurr) == xcurr) && (Math.Sign(xprev - xcurr) == Math.Sign(xnext - xcurr)))
                        {
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

                    if (!(xprev == xcurr && xcurr == xnext) && !(yprev == ycurr && ycurr == ynext))
                    {
                        if ((Math.Truncate(xcurr) == xcurr) && (Math.Sign(xprev - xcurr) == Math.Sign(xnext - xcurr)))
                        {
                            subpathList.Add(new PointF(xcurr - 0.1f, ycurr));
                            subpathList.Add(new PointF(xcurr + 0.1f, ycurr));
                            //continue;
                        }
                        else if ((Math.Truncate(ycurr) == ycurr) && (Math.Sign(yprev - ycurr) == Math.Sign(ynext - ycurr)))
                        {
                            subpathList.Add(new PointF(xcurr, ycurr - 0.1f));
                            subpathList.Add(new PointF(xcurr, ycurr + 0.1f));
                            //continue;
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

                        bool isIn;
                        if (yy2 - yy1 < 0) isIn = true; //Долбаный перевернутый Y
                        else isIn = false;

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

                                if (!crossY.ContainsKey(yinc)) crossY.Add(yinc, new List<InOutCoord>() { new InOutCoord(isIn, (float)xtmp) });
                                else crossY[yinc].Add(new InOutCoord(isIn, (float)xtmp));
                            }
                        }
                        else if (Math.Truncate(yy1) == yy1)
                        {
                            int yinc = (int)yy1;
                            if (!crossY.ContainsKey(yinc)) crossY.Add(yinc, new List<InOutCoord>() { });
                            
                            if (yy1 == yy2)
                            {
                                crossY[yinc].Add(new InOutCoord(true, (float)Math.Min(xx1, xx2)));
                                crossY[yinc].Add(new InOutCoord(false, (float)Math.Max(xx1, xx2)));
                            }
                            else crossY[yinc].Add(new InOutCoord(isIn, (float)xx1));
                        }

                        if (xx2 - xx1 > 0) isIn = true;
                        else isIn = false;

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

                                if (!crossX.ContainsKey(xinc)) crossX.Add(xinc, new List<InOutCoord>() { new InOutCoord(isIn, (float)ytmp) });
                                else crossX[xinc].Add(new InOutCoord(isIn, (float)ytmp));
                            }
                        }
                        else if (Math.Truncate(xx1) == xx1)
                        {
                            int xinc = (int)xx1;
                            if (!crossX.ContainsKey(xinc)) crossX.Add(xinc, new List<InOutCoord>() { });
                            
                            if (xx1 == xx2)
                            {
                                crossX[xinc].Add(new InOutCoord(true, (float)Math.Min(yy1, yy2)));
                                crossX[xinc].Add(new InOutCoord(false, (float)Math.Max(yy1, yy2)));
                            }
                            else crossX[xinc].Add(new InOutCoord(isIn, (float)yy1));
                        }
                    }

                }


                List<InOutCoord> tmpYList;
                List<InOutCoord> tmpList;
                foreach (int ykey in crossY.Keys)
                {
                    tmpList = new List<InOutCoord>();
                    tmpYList = new List<InOutCoord>();
                    tmpList.AddRange(crossY[ykey]);
                    
                    tmpList.Sort((x, y) => x._coord.CompareTo(y._coord));

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
                    inc = 0;
                    
                    for (int i = 0; i < tmpYList.Count; i+=2) {
                        for (int j = (int)Math.Ceiling(tmpYList[i]._coord); j <= (int)Math.Truncate(tmpYList[i + 1]._coord); j++) {
                            alph[j - x0, ykey - y0] = 1;
                        }
                    }
                    
                    for (int i = 0; i < tmpYList.Count; i ++)
                    {
                        int xhalf;
                        double diff;

                        if (tmpYList[i]._isIn)
                        {
                            xhalf = (int)Math.Truncate(tmpYList[i]._coord);
                            diff = tmpYList[i]._coord - xhalf;
                            if (diff < 0.5) alph[xhalf - x0, ykey - y0] = (alph[xhalf - x0, ykey - y0] + (0.5 - diff)); // /2.0
                        }
                        else { 
                            xhalf = (int)Math.Truncate(tmpYList[i]._coord) + 1;
                            diff = xhalf - tmpYList[i]._coord;
                            if (diff < 0.5) alph[xhalf - x0, ykey - y0] = (alph[xhalf - x0, ykey - y0] + (0.5 - diff)); // /2.0
                        }
                    }
                    
                }


                List<InOutCoord> tmpXList;
                foreach (int xkey in crossX.Keys)
                {
                    tmpList = new List<InOutCoord>();
                    tmpXList = new List<InOutCoord>();
                    tmpList.AddRange(crossX[xkey]);
                    tmpList.Sort((x, y) => x._coord.CompareTo(y._coord));

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
                    
                    for (int i = 0; i < tmpXList.Count; i++)
                    {
                        int yhalf;
                        double diff;

                        if (tmpXList[i]._isIn)
                        {
                            yhalf = (int)Math.Round(tmpXList[i]._coord);
                            diff = Math.Abs(tmpXList[i]._coord - yhalf);
                            if (diff < 0.5) alph[xkey - x0, yhalf - y0] = (alph[xkey - x0, yhalf - y0] + (0.5 - diff)); // /2.0
                        }
                        else
                        {
                            yhalf = (int)Math.Truncate(tmpXList[i]._coord) + 1;
                            diff = yhalf - tmpXList[i]._coord;
                            if (diff < 0.5) alph[xkey - x0, yhalf - y0] = (alph[xkey - x0, yhalf - y0] + (0.5 - diff)); // /2.0
                        }
                    }
                    
                }


                height = y1 - y0 + 1;
                width = x1 - x0 + 1;
                int x0shift = 0;
                bool isBraked = false;
                while (x0shift < width)
                {
                    for (int i = 0; i < height; i++)
                    {
                        if (alph[x0shift, i] > 0)
                        {
                            isBraked = true;
                            break;
                        }
                    }
                    if (isBraked) break;
                    x0shift++;
                }

                int x1shift = width - 1;
                isBraked = false;
                while (x1shift >= 0)
                {
                    for (int i = 0; i < height; i++)
                    {
                        if (alph[x1shift, i] > 0) isBraked = true;
                    }
                    if (isBraked) break;
                    x1shift--;
                }

                int y0shift = 0;
                isBraked = false;
                while (y0shift < height)
                {
                    for (int i = 0; i < width; i++)
                    {
                        if (alph[i, y0shift] > 0) isBraked = true;
                    }
                    if (isBraked) break;
                    y0shift++;
                }

                int y1shift = height - 1;
                isBraked = false;
                while (y1shift >= 0)
                {
                    for (int i = 0; i < width; i++)
                    {
                        if (alph[i, y1shift] > 0) isBraked = true;
                    }
                    if (isBraked) break;
                    y1shift--;
                }

                minX = 0;// x0 + x0shift;
                minY = y0 + y0shift;

                height = y1shift - y0shift + 1;
                width = x1shift - x0shift + 1;

                alphas = new double[width, height];
                for (int xx = x0shift; xx <= x1shift; xx++)
                {
                    for (int yy = y0shift; yy <= y1shift; yy++)
                    {
                        if (alph[xx, yy] != 0)
                        {
                            alph[xx, yy] = (alph[xx, yy] < 1) ? alph[xx, yy] + 0.05f : alph[xx, yy];
                            alph[xx, yy] = (alph[xx, yy] > 1) ? 1 : alph[xx, yy];
                        }
                        alphas[xx - x0shift, yy - y0shift] = alph[xx, yy];

                    }
                }
            }
            */
        }
        /*
        private class InOutCoord
        {
            public bool _isIn;
            public float _coord;

            public InOutCoord(bool isIn, float coord)
            {
                _isIn = isIn;
                _coord = coord;
            }
        }
        */
        #endregion

        internal class ModifyLetter
        {
            internal String name;
            internal int xBeg = 0;
            internal int yBeg = 0;
            internal int width = 0;
            internal int height = 0;
            internal int xShift = 0;
            private Letter _letter;
            internal bool isSpec;
            //internal int somethUnReal = 0;

            internal ModifyLetter(Letter let, int xShift) //, float cof)
            {
                this.xShift = xShift;
                name = let.name;
                width = let.width;
                height = let.height;
                yBeg = let.minY;
                xBeg = let.minX;
                isSpec = let.isSpec;
                _letter = let;
                //somethUnReal = (int)((xShift + let.width + let.minX) * cof);
            }
            /*
            internal List<float> GetCol() {
                return _letter.col;
            }

            internal List<float> GetPix() {
                return _letter.pix;
            }
            */
            internal byte[] getArr()
            {
                return _letter.arr;
            }
        }
    }
}
