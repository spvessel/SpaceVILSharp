package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceTextContainer;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TextLine extends TextItem implements InterfaceTextContainer {
    private static int count = 0;

    private TextPrinter textPrt = new TextPrinter();
    private boolean flagBB = false;

    // private int _minLineSpacer;
    // private int _minFontY;
    // private int _maxFontY;
    // private List<float> _coordArray; //private List<List<float>> _coordArray;
    private List<Integer> _letEndPos;
    private int _lineYShift = 0;
    private int _lineXShift = 0;
    private int _parentAllowWidth = Integer.MAX_VALUE;
    private int _parentAllowHeight = Integer.MAX_VALUE;
    private int _bigWidth = 0;
    // private int _bigHeight = 0;
    private float _screenScale = 1;
    // private int _bigMinY = 0;
    
    private List<Alphabet.ModifyLetter> _letters = new LinkedList<>();
    private List<Alphabet.ModifyLetter> _bigLetters = new LinkedList<>();
    // private List<TextPrinter> _letTexturesList;
    
    // private List<Float> px0;
    /*
    private List<ItemAlignment> _needCheckAlignment = new LinkedList<>();
    private int _needCheckPaddingRight;
    private int _needCheckPaddingBottom;
    private int _needCheckWidth;
    private int _needCheckHeight;
    private int _needCheckX;
    private int _needCheckY;
    */
    TextLine() {
        count++;
    }
    
    TextLine(String text, Font font) {
        super(text, font, "TextLine_" + count);
        count++;
        // getFontDims();
        updateData();
    }
    
    private Lock textLock = new ReentrantLock();
    
    private void createText() {
        // List<Float> cl0 = new LinkedList<>();
        textLock.lock();
        try {
            int _lineWidth = 0; // private float[] _lineWidth;
            // long time = System.nanoTime();
            String text = getItemText();
            Font font = getFont();

            _letters = FontEngine.getModifyLetters(text, font);

            _letEndPos = new LinkedList<>();
            _lineWidth = 0;
            if (_letters.size() > 0)
                _lineWidth = _letters.get(_letters.size() - 1).xShift + _letters.get(_letters.size() - 1).width
                        + _letters.get(_letters.size() - 1).xBeg; // xBeg не обязательно, т.к. везде 0, но вдруг

            int[] fontDims = getFontDims();
            super.setWidth(_lineWidth);
            super.setHeight(fontDims[2]);
            // px0 = new LinkedList<>();

            // List<Float> tmpPx;
            // float xc, yc;
            for (Alphabet.ModifyLetter modL : _letters) {
                _letEndPos.add(modL.xBeg + modL.xShift + modL.width);
                // // px0.addAll(modL.getPix());
                // tmpPx = modL.getPix();
                // for (int i = 0; i < tmpPx.size() / 3; i++) {
                // xc = modL.xBeg + modL.xShift;
                // yc = -_minFontY + modL.yBeg;
                // px0.add(tmpPx.get(i * 3) + xc);
                // px0.add(tmpPx.get(i * 3 + 1) + yc);
                // px0.add(tmpPx.get(i * 3 + 2));
                // }

                // cl0.addAll(modL.getCol());

                // _letTexturesList.add(new TextPrinter(modL.getArr()));
            }

            WindowLayout wLayout = getHandler();
            if (wLayout == null || wLayout.getDpiScale() == null)
                _screenScale = 0;
            else {
                _screenScale = wLayout.getDpiScale()[0];
                if (_screenScale != 1)
                    makeBigArr();
            }

            // System.out.println("scale: " + _screenScale);

            

        } finally {
            textLock.unlock();
        }
    }

    private boolean isBigExist = false;

    private void makeBigArr() { 
        Font fontBig = new Font(getFont().getName(), getFont().getStyle(), (int) (getFont().getSize() * _screenScale));

        _bigLetters = FontEngine.getModifyLetters(getItemText(), fontBig);
        // int[] output = FontEngine.getSpacerDims(fontBig);
        // _bigHeight = output[2];
        _bigWidth = 0;
        if (_bigLetters.size() > 0) {
            _bigWidth = _bigLetters.get(_bigLetters.size() - 1).xShift + _bigLetters.get(_bigLetters.size() - 1).width
                    + _bigLetters.get(_bigLetters.size() - 1).xBeg; // xBeg не обязательно, т.к. везде 0, но вдруг
            super.setWidth((int)((float)_bigWidth / _screenScale));
        }

        // _bigMinY = output[1];

        if (_screenScale != 0) {
            _letEndPos = new LinkedList<>();
            for (Alphabet.ModifyLetter modL : _bigLetters) {
                _letEndPos.add((int)((float)(modL.xBeg + modL.xShift + modL.width) / _screenScale));
            }
        }

        isBigExist = true;
    }

    // private int smokerShift = 0;

    public TextPrinter getLetTextures() {
        textLock.lock();
        try {
            WindowLayout wLayout = getHandler();
            if (wLayout != null && wLayout.getDpiScale() != null) {
                float scl = wLayout.getDpiScale()[0];
                if (scl != _screenScale && !isBigExist) { //Это при допущении, что скейл меняется только один раз!
                    _screenScale = scl;
                    makeBigArr();
                }
            }


            int[] fontDims = getFontDims();
            int height = fontDims[2];
            if (getHeight() != height) {
                super.setHeight(height);
            }

            // List<TextPrinter> letTexturesList = new LinkedList<>();
            if (_lineYShift - fontDims[1] + height < 0 || _lineYShift - fontDims[1] > _parentAllowHeight)
                return null;
            if (_letters.size() == 0) {
                return new TextPrinter(); //null;
            }
            if (flagBB)
            {
                int bb_h = getHeight();
                int bb_w = getWidth();
                // System.out.println("small " + bb_h + " " + bb_w);
                // if (_screenScale != 0 || _screenScale != 1) {
                //     bb_h = _bigHeight;
                //     bb_w = _bigWidth;
                // }

//                int firstVisLet = -1, inc = -1;
//                System.out.print(bb_w + " ");

                // float shiftPercent = Math.abs(_lineXShift) * 1f / (bb_w * 1f);

                if (_parentAllowWidth > 0)
                    bb_w = bb_w > _parentAllowWidth ? _parentAllowWidth : bb_w;
//                System.out.println(_parentAllowWidth + " " + bb_w);
                ByteBuffer cacheBB = BufferUtils.createByteBuffer(bb_h * bb_w * 4);

                // _xpos = (int) alignShiftX + _lineXShift + getParent().getX() + xFirstBeg;
                // _ypos = (int) alignShiftY + _lineYShift + getParent().getY();
                int xFirstBeg; // = _letters.get(0).xBeg + _letters.get(0).xShift;
//                System.out.println("first in line " + _letters.get(0).xShift + " " + _lineXShift + " " + xFirstBeg);
//                System.out.println(_letters.get(0).xBeg + " " + _letters.get(0).name);
//                int inc = 0;
                // smokerShift = 0;
                if (_screenScale != 0 && _screenScale != 1) {
                    Font fontBig = new Font(getFont().getName(), getFont().getStyle(),
                            (int) (getFont().getSize() * _screenScale));
                    int[] output = FontEngine.getSpacerDims(fontBig);
                    bb_h = output[2];
                    bb_w = _bigWidth > (int)(_parentAllowWidth * _screenScale) ? (int)(_parentAllowWidth * _screenScale) : _bigWidth;

                    // float badShift =  - bb_w * 1f * shiftPercent;
                    // int[] fli = new int[]{0, _letters.size() - 1, bb_w};
                    // if (_parentAllowWidth > 0 && bb_w > (int) (_parentAllowWidth * _screenScale)) {
                    //     fli = findFirstLast(_bigLetters, (int) (_parentAllowWidth * _screenScale)); //, badShift);
                    //     // bb_w = bb_w > (int) (_parentAllowWidth * _screenScale) ? (int) (_parentAllowWidth * _screenScale) : bb_w;
                    //     bb_w = fli[2];//_bigLetters.get(fli[1]).xShift + _bigLetters.get(fli[1]).width + _bigLetters.get(fli[1]).xBeg - _bigLetters.get(fli[0]).xBeg - _bigLetters.get(fli[0]).xShift;
                    //     // smokerShift = 0;//fli[3]; //_lineXShift + (int)((_bigLetters.get(fli[0]).xBeg + _bigLetters.get(fli[0]).xShift) * 1.0 / _screenScale);
                    // }
                    int bigMinY = output[1];
                    
                    // System.out.println("big " + fli[0] + " " + fli[1]);
                    // System.out.println("width " + bb_w + " shift " + smokerShift + " firstInd " + fli[0]);

                    cacheBB = makeSomeBig(bb_h, bb_w, bigMinY, 0, _letters.size() - 1); //, fli[0], fli[1]);
                }
                else {
                    for (Alphabet.ModifyLetter modL : _letters) {
//                for (int ii = 0; ii < _letters.size(); ii++) {
//                    Alphabet.ModifyLetter modL = _letters.get(ii);
//                    inc++;
                        // int x = modL.xBeg + (int) alignShiftX + _lineXShift + modL.xShift;
                        // int y = (modL.yBeg - _minFontY) + (int) alignShiftY + _lineYShift;

                        // x += getParent().getX();
                        // y += getParent().getY();
                        // if (x < 0)
                        // continue;
                        // if (x > _parentAllowWidth)
                        // break;

                        int widthFrom = 0;
                        int widthTo = modL.width;
//                    if (_letters.get(0).name.equals("7")) System.out.print(modL.name + " ");
                        if (modL.xBeg + modL.xShift + modL.width + _lineXShift < 0) { //До разрешенной области
//                        if (_letters.get(0).name.equals("7")) System.out.println();
                            continue;
                        }
                        if (modL.xBeg + modL.xShift + _lineXShift <= 0) //(firstVisLet == -1)
                        {
//                        firstVisLet = inc;
                            widthFrom = Math.abs(modL.xBeg + modL.xShift + _lineXShift);
                        }

                        xFirstBeg = -_lineXShift;

//                    if (inc == 0) {
//                        xFirstBeg = modL.xBeg + modL.xShift + widthFrom; //modL.xBeg + modL.xShift;
//                        inc++;
//                        if (modL.xBeg + modL.xShift + _lineXShift > 0)
//                            xFirstBeg += modL.xBeg + modL.xShift + _lineXShift;
//                        System.out.println((modL.xBeg + modL.xShift) + " " + _lineXShift + " " + xFirstBeg + " " + modL.name);
//                    }

                        if (modL.xBeg + modL.xShift - xFirstBeg > _parentAllowWidth) { //После разрешенной области + _lineXShift
//                        if (_letters.get(0).name.equals("7")) System.out.println();
                            break;
                        }
                        if (modL.xBeg + modL.xShift + modL.width - xFirstBeg >= _parentAllowWidth) { // + _lineXShift
                            widthTo = _parentAllowWidth - (modL.xBeg + modL.xShift + widthFrom - xFirstBeg); // + _lineXShift
                        }
//                    if (_letters.get(0).name.equals("7")) System.out.println("is visible");

                        byte[] bitmap = modL.getArr();
                        if (bitmap == null) { //?spec let
//                        if (modL.isSpec) {
//                            modL.height = 1;
//                            modL.yBeg = fontDims[1];
//                            bitmap = new byte[4 * modL.width * 1];
//                        } else {
                            continue;
//                        }
                        }

                        // int w = letTx.letWidth;
                        // int h = letTx.letHeight;

                        // x -= xpos;
                        // y -= _ypos;

                        int offset = (modL.yBeg - fontDims[1]) * 4 * bb_w + (modL.xBeg + modL.xShift + widthFrom - xFirstBeg) * 4;
//                    System.out.println("offset " + offset);
                        // if((modL.yBeg - fontDims[1]) > fontDims[2]) {
                        // System.out.println("modL." + fontDims[1] + " " + modL.yBeg);
                        // }
                        // System.err.println(bb_w + " " + bb_h + " " + (modL.width + modL.xBeg +
                        // modL.xShift - xFirstBeg) + " "
                        // + (modL.height + modL.yBeg - _minFontY) + " " + offset);
                        for (int j = 0; j < modL.height; j++) {
                            for (int i = widthFrom; i < widthTo; i++) {
                                int b1 = bitmap[3 + j * 4 + i * (modL.height * 4)] & 0xFF;
                                int b2 = cacheBB.get(3 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)) & 0xFF;
                                if (b1 < b2)
                                    continue;

                                cacheBB.put(0 + offset + (i - widthFrom) * 4 + j * (bb_w * 4), bitmap[0 + j * 4 + i * (modL.height * 4)]);
                                cacheBB.put(1 + offset + (i - widthFrom) * 4 + j * (bb_w * 4), bitmap[1 + j * 4 + i * (modL.height * 4)]);
                                cacheBB.put(2 + offset + (i - widthFrom) * 4 + j * (bb_w * 4), bitmap[2 + j * 4 + i * (modL.height * 4)]);
                                cacheBB.put(3 + offset + (i - widthFrom) * 4 + j * (bb_w * 4), bitmap[3 + j * 4 + i * (modL.height * 4)]);
                            }
                        }

                        // TextPrinter tp = new TextPrinter(modL.getArr());
                        // tp.xshift = x;
                        // tp.yshift = y;
                        // tp.letWidth = modL.width;
                        // tp.letHeight = modL.height;
                        // tp.yWinShift = y - (modL.yBeg - _minFontY);
                        // letTexturesList.add(tp);
                    }
                }
                cacheBB.rewind();
                flagBB = false;
                textPrt = new TextPrinter(cacheBB);
                textPrt.widthTexture = bb_w;
                textPrt.heightTexture = bb_h;

                // if (letTexturesList.size() > 0)
                // createAtlas(letTexturesList);
            }
            updateCoords();
            return textPrt;
        } finally {
            textLock.unlock();
        }
    }

    private int[] findFirstLast(List<Alphabet.ModifyLetter> letList, int winWidth) { //, float someShift) {
        int firstInd = 0, lastInd = 0;
        int someShift = (int) (_lineXShift * _screenScale);
        boolean isFirstFound = false;
        
        for (int ii = 0; ii < letList.size(); ii++) {
            Alphabet.ModifyLetter modL = letList.get(ii);
            // System.out.print(ii + " ");
            if (modL.xBeg + modL.xShift + modL.width + someShift < 0) { // До разрешенной области
                // System.out.println("Continue");
                continue;
            }
            // if (modL.xBeg + modL.xShift + someShift <= 0)
            // {
            //     firstInd = ii;
            // }
            if (!isFirstFound) {
                firstInd = ii;
                isFirstFound = true;
            }

            // if (modL.xBeg + modL.xShift + _lineXShift > _parentAllowWidth) { // После разрешенной области + _lineXShift
            // }
            if (modL.xBeg + modL.xShift + modL.width + someShift >= winWidth) {
                lastInd = ii;
                // System.out.println("last ind " + ii);
                break;
            }
        }

        if (lastInd == 0) lastInd = letList.size() - 1;

        Alphabet.ModifyLetter letFirst = letList.get(firstInd);
        Alphabet.ModifyLetter letLast = letList.get(lastInd);

        int visWidth = letLast.xShift + letLast.width + letLast.xBeg - letFirst.xBeg - letFirst.xShift;
        // int outShift = (int) ((someShift + (letFirst.xBeg + letFirst.xShift) * 1.0) / _screenScale);

        return new int[] {firstInd, lastInd, visWidth}; //, outShift};
    }

    private ByteBuffer makeSomeBig(int hgt, int wdt, int bigMinY, int firstInd, int lastInd) {
        ByteBuffer outCache = BufferUtils.createByteBuffer(hgt * wdt * 4);
        int someShift = (int ) (_lineXShift * _screenScale);
        int parWidth = (int)(_parentAllowWidth * _screenScale);

        int xFirstBeg = _bigLetters.get(firstInd).xBeg + _bigLetters.get(firstInd).xShift;
        //for (Alphabet.ModifyLetter modL : _letters) {
        for (int ii = firstInd; ii <= lastInd; ii++) { // for (int ii = 0; ii < _letters.size(); ii++) {
            // Alphabet.ModifyLetter smallLet = _letters.get(ii);
            Alphabet.ModifyLetter bigLet = _bigLetters.get(ii);

            //ignore at first
            int widthFrom = 0;
            int widthTo = bigLet.width;
            
            if (bigLet.xBeg + bigLet.xShift + bigLet.width + someShift < 0) { // До разрешенной области
                
                continue;
            }
            if (bigLet.xBeg + bigLet.xShift + someShift <= 0)
            {
                // continue;
                widthFrom = Math.abs(bigLet.xBeg + bigLet.xShift + someShift);
            }

            xFirstBeg = -someShift;

            if (bigLet.xBeg + bigLet.xShift - xFirstBeg > parWidth) { // После разрешенной области + _lineXShift
                break;
            }
            if (bigLet.xBeg + bigLet.xShift + bigLet.width - xFirstBeg >= parWidth) {
                // break;
                widthTo = parWidth - (bigLet.xBeg + bigLet.xShift + widthFrom - xFirstBeg);
            }
            

            byte[] bitmap = bigLet.getArr();
            if (bitmap == null) {
                continue;
            }

            int offset = (bigLet.yBeg - bigMinY) * 4 * wdt + (bigLet.xBeg + bigLet.xShift + widthFrom - xFirstBeg) * 4;
            // System.out.println(outCache.capacity() + " " + offset);
            for (int j = 0; j < bigLet.height; j++) {
                for (int i = widthFrom; i < widthTo; i++) {
                    int b1 = bitmap[3 + j * 4 + i * (bigLet.height * 4)] & 0xFF;
                    int b2 = outCache.get(3 + offset + (i - widthFrom) * 4 + j * (wdt * 4)) & 0xFF;
                    if (b1 < b2)
                        continue;

                    outCache.put(0 + offset + (i - widthFrom) * 4 + j * (wdt * 4),
                            bitmap[0 + j * 4 + i * (bigLet.height * 4)]);
                    outCache.put(1 + offset + (i - widthFrom) * 4 + j * (wdt * 4),
                            bitmap[1 + j * 4 + i * (bigLet.height * 4)]);
                    outCache.put(2 + offset + (i - widthFrom) * 4 + j * (wdt * 4),
                            bitmap[2 + j * 4 + i * (bigLet.height * 4)]);
                    outCache.put(3 + offset + (i - widthFrom) * 4 + j * (wdt * 4),
                            bitmap[3 + j * 4 + i * (bigLet.height * 4)]);
                }
            }

        }

        return outCache;
    }

    /*
     * int _xpos = 0; int _ypos = 0;
     * 
     * public int getXpos() { return _xpos; }
     * 
     * public int getYpos() { return _ypos; }
     * 
     * private void createAtlas(List<TextPrinter> lettTextures) { int bb_h =
     * getHeight(); int bb_w = getWidth(); int xpos = lettTextures.get(0).xshift;
     * _xpos = xpos; _ypos = lettTextures.get(0).yWinShift;
     * 
     * if (flagBB) { cacheBB = BufferUtils.createByteBuffer(bb_h * bb_w * 4);
     * 
     * for (TextPrinter letTx : lettTextures) { byte[] bitmap =
     * letTx._letterTexture; if (bitmap == null) { continue; } int x = letTx.xshift;
     * int y = letTx.yshift; int w = letTx.letWidth; int h = letTx.letHeight;
     * 
     * x -= xpos; y -= _ypos;
     * 
     * int offset = y * 4 + x * bb_h * 4;
     * 
     * for (int j = 0; j < h; j++) { for (int i = 0; i < w; i++) { cacheBB.put(0 +
     * (j + y) * 4 + (i + x) * (bb_h * 4), bitmap[0 + j * 4 + i * (h * 4)]);
     * cacheBB.put(1 + (j + y) * 4 + (i + x) * (bb_h * 4), bitmap[1 + j * 4 + i * (h
     * * 4)]); cacheBB.put(2 + (j + y) * 4 + (i + x) * (bb_h * 4), bitmap[2 + j * 4
     * + i * (h * 4)]); cacheBB.put(3 + (j + y) * 4 + (i + x) * (bb_h * 4), bitmap[3
     * + j * 4 + i * (h * 4)]); } } } cacheBB.rewind(); flagBB = false; } }
     */
    /*
     * public byte[] getArr(int index) { textLock.lock(); try { if (_letters.size()
     * == 0) return null; if (_lineYShift - _minFontY < 0 || _lineYShift - _minFontY
     * > _parentAllowHeight) return null; return _letters.get(index).getArr(); }
     * finally { textLock.unlock(); } }
     * 
     * public int[] getAll(int index) { textLock.lock(); try { if (_letters.size()
     * == 0) return null; List<ItemAlignment> alignments = getTextAlignment(); float
     * alignShiftX = 1; float alignShiftY = 0;
     * 
     * int height = Math.abs(_maxFontY - _minFontY); //
     * System.out.println(alignments);
     * 
     * // if (_lineYShift - _minFontY + height < 0 || _lineYShift - _minFontY > //
     * _parentAllowHeight) { // // setAlphas(alphas); //
     * setRealCoords(outRealCoords); // return; // }
     * 
     * // Horizontal if (alignments.contains(ItemAlignment.LEFT)) { alignShiftX =
     * getParent().getPadding().left + getMargin().left; } else if
     * (alignments.contains(ItemAlignment.RIGHT) && (_lineWidth <
     * _parentAllowWidth)) alignShiftX = getParent().getWidth() - _lineWidth -
     * getParent().getPadding().right - getMargin().right;
     * 
     * else if (alignments.contains(ItemAlignment.HCENTER) && (_lineWidth <
     * _parentAllowWidth)) alignShiftX = ((getParent().getWidth() -
     * getParent().getPadding().left - getParent().getPadding().right -
     * getMargin().left - getMargin().right) - _lineWidth) / 2f;
     * 
     * // Vertical if (alignments.contains(ItemAlignment.TOP)) { alignShiftY =
     * getParent().getPadding().top + getMargin().top; } else if
     * (alignments.contains(ItemAlignment.BOTTOM)) alignShiftY =
     * getParent().getHeight() - height - getParent().getPadding().bottom -
     * getMargin().bottom;
     * 
     * else if (alignments.contains(ItemAlignment.VCENTER)) alignShiftY =
     * ((getParent().getHeight() - getParent().getPadding().bottom -
     * getParent().getPadding().top - getMargin().bottom - getMargin().top) -
     * height) / 2f;
     * 
     * int x = _letters.get(index).xBeg + (int) alignShiftX + _lineXShift +
     * _letters.get(index).xShift; int y = (_letters.get(index).yBeg - _minFontY) +
     * (int) alignShiftY + _lineYShift;
     * 
     * x += getParent().getX(); y += getParent().getY();
     * 
     * return new int[] { x, y, _letters.get(index).width,
     * _letters.get(index).height }; } finally { textLock.unlock(); } }
     */

    @Override
    public void setWidth(int width) {
        setAllowWidth(width);
    }

    @Override
    public void setHeight(int height) {
        setAllowHeight(height);
    }

    // private void addAllShifts() {
    // textLock.lock();
    // try {
    // // long time0 = System.nanoTime();

    // if (getParent() == null)
    // return;

    // if (_letters == null)
    // return;

    // List<Float> outRealCoords = new LinkedList<>();
    // // List<Float> alphas = new LinkedList<>();

    // List<ItemAlignment> alignments = getTextAlignment();
    // float alignShiftX = 1;
    // float alignShiftY = 0;

    // float height = Math.abs(_maxFontY - _minFontY);

    // // if (getParent() instanceof TextBlock)
    // // System.out.println(getItemText() + " " + (_lineYShift - _minFontY +
    // height));
    // if (_lineYShift - _minFontY + height < 0 || _lineYShift - _minFontY >
    // _parentAllowHeight) {
    // // setAlphas(alphas);
    // setRealCoords(outRealCoords);
    // return;
    // }

    // outRealCoords = new LinkedList<>(px0);

    // if (alignments.contains(ItemAlignment.RIGHT) && (_lineWidth <
    // _parentAllowWidth))
    // alignShiftX = getParent().getWidth() - _lineWidth -
    // getParent().getPadding().right;

    // else if (alignments.contains(ItemAlignment.HCENTER) && (_lineWidth <
    // _parentAllowWidth))
    // alignShiftX = (getParent().getWidth() - _lineWidth) / 2f;

    // // Vertical
    // if (alignments.contains(ItemAlignment.BOTTOM))
    // alignShiftY = getParent().getHeight() - height -
    // getParent().getPadding().bottom;

    // else if (alignments.contains(ItemAlignment.VCENTER))
    // alignShiftY = (getParent().getHeight() - height) / 2f;

    // // List<Float> tmpList;
    // float xCoord, yCoord;

    // // long time1 = System.nanoTime();
    // // System.out.println("Prepare is end " + (time1 - time0) / 100000f);

    // for (int j = 0; j < px0.size() / 3; j++) {
    // // tmpList = modL.getPix();
    // xCoord = alignShiftX + _lineXShift + px0.get(j * 3);
    // yCoord = alignShiftY + _lineYShift + px0.get(j * 3 + 1);

    // if (xCoord < 0)
    // continue;
    // if (xCoord > _parentAllowWidth)
    // break;

    // // alphas.addAll(modL.getCol());

    // outRealCoords.set(j * 3, xCoord);
    // outRealCoords.set(j * 3 + 1, yCoord);
    // outRealCoords.set(j * 3 + 2, px0.get(j * 3 + 2));

    // }

    // // long time2 = System.nanoTime();
    // // System.out.println("Add alphas and colors " + (time2 - time1) / 100000f);
    // // System.out.println("shifts");
    // // setAlphas(alphas);
    // setRealCoords(outRealCoords);

    // _needCheckAlignment = alignments;
    // _needCheckPaddingRight = getParent().getPadding().right;
    // _needCheckPaddingBottom = getParent().getPadding().bottom;
    // _needCheckWidth = getParent().getWidth();
    // _needCheckHeight = getParent().getHeight();
    // _needCheckX = getParent().getX();
    // _needCheckY = getParent().getY();

    // } finally {
    // textLock.unlock();
    // }
    // }

    /*
     * private void oldaddAllShifts() { // long time0 = System.nanoTime();
     * 
     * if (getParent() == null) return;
     * 
     * if (_letters == null) return;
     * 
     * List<Float> outRealCoords = new LinkedList<>(); List<Float> alphas = new
     * LinkedList<>();
     * 
     * List<ItemAlignment> alignments = getTextAlignment(); float alignShiftX = 1;
     * float alignShiftY = 0;
     * 
     * float height = Math.abs(_maxFontY - _minFontY);
     * 
     * if (_lineYShift - _minFontY + height < 0 || _lineYShift - _minFontY >
     * _parentAllowHeight) { setAlphas(alphas); setRealCoords(outRealCoords);
     * return; }
     * 
     * // Horizontal if (alignments.contains(ItemAlignment.RIGHT) && (_lineWidth <
     * _parentAllowWidth)) alignShiftX = getParent().getWidth() - _lineWidth -
     * getParent().getPadding().right;
     * 
     * else if (alignments.contains(ItemAlignment.HCENTER) && (_lineWidth <
     * _parentAllowWidth)) alignShiftX = (getParent().getWidth() - _lineWidth) / 2f;
     * 
     * // Vertical if (alignments.contains(ItemAlignment.BOTTOM)) alignShiftY =
     * getParent().getHeight() - height - getParent().getPadding().bottom;
     * 
     * else if (alignments.contains(ItemAlignment.VCENTER)) alignShiftY =
     * (getParent().getHeight() - height) / 2f;
     * 
     * List<Float> tmpList; float xCoord, yCoord;
     * 
     * // long time1 = System.nanoTime(); // System.out.println("Prepare is end " +
     * (time1 - time0) / 100000f);
     * 
     * for (Alphabet.ModifyLetter modL : _letters) { tmpList = modL.getPix(); xCoord
     * = alignShiftX + modL.xBeg + modL.xShift + _lineXShift; yCoord = alignShiftY +
     * _lineYShift - _minFontY + modL.yBeg;
     * 
     * if (xCoord + modL.width < 0) continue; if (xCoord > _parentAllowWidth) break;
     * 
     * alphas.addAll(modL.getCol()); // for (Float var : modL.getCol()) //
     * alphas.add(var);
     * 
     * for (int j = 0; j < tmpList.size() / 3; j++) {
     * outRealCoords.add(tmpList.get(j * 3) + xCoord);
     * outRealCoords.add(tmpList.get(j * 3 + 1) + yCoord);
     * outRealCoords.add(tmpList.get(j * 3 + 2)); } }
     * 
     * // long time2 = System.nanoTime(); //
     * System.out.println("Add alphas and colors " + (time2 - time1) / 100000f); //
     * System.out.println(); setAlphas(alphas); setRealCoords(outRealCoords); }
     */

    @Override
    public void updateData() {
        textLock.lock();
        try {
            if (getFont() == null)
                return;
            /*
             * int[] output = FontEngine.getSpacerDims(getFont()); _minLineSpacer =
             * output[0]; _minFontY = output[1]; _maxFontY = output[2];
             */
            // getFontDims();
            createText();
            flagBB = true;
        } finally {
            textLock.unlock();
        }
    }

    private void updateCoords() {
        // addAllShifts();
        // if (_letters == null)
        //     return;

        if (_letters.size() == 0)
            return;
        int[] fontDims = getFontDims();
        int height = fontDims[2];

        List<ItemAlignment> alignments = getTextAlignment();
        float alignShiftX = 1;
        float alignShiftY = 0;

        int _lineWidth = getWidth();
        // Horizontal
        if (alignments.contains(ItemAlignment.LEFT) || (_lineWidth >= _parentAllowWidth)) {
            alignShiftX = getParent().getPadding().left + getMargin().left + cursorWidth;
        } else if (alignments.contains(ItemAlignment.RIGHT) && (_lineWidth < _parentAllowWidth))
            alignShiftX = getParent().getWidth() - _lineWidth - getParent().getPadding().right - getMargin().right - cursorWidth;

        else if (alignments.contains(ItemAlignment.HCENTER) && (_lineWidth < _parentAllowWidth))
            // alignShiftX = ((getParent().getWidth() - getParent().getPadding().left - getParent().getPadding().right
            //         + getMargin().left - getMargin().right) - _lineWidth) / 2f;
            alignShiftX = (_parentAllowWidth - _lineWidth) / 2f + getParent().getPadding().left + getMargin().left + cursorWidth; //(getParent().getWidth() - _lineWidth) / 2f + getParent().getPadding().left;

        // Vertical
        if (alignments.contains(ItemAlignment.TOP)) {
            // System.out.println(getMargin().top);
            alignShiftY = getParent().getPadding().top + getMargin().top;
        } else if (alignments.contains(ItemAlignment.BOTTOM))
            alignShiftY = getParent().getHeight() - height - getParent().getPadding().bottom - getMargin().bottom;

        else if (alignments.contains(ItemAlignment.VCENTER))
            // alignShiftY = ((getParent().getHeight() - getParent().getPadding().bottom - getParent().getPadding().top)
            //         - height) / 2f - getMargin().bottom + getMargin().top;
            alignShiftY = (getParent().getHeight() - height) / 2f + getParent().getPadding().top;

        int xFirstBeg = _letters.get(0).xBeg + _letters.get(0).xShift;
        textPrt.xTextureShift = (int) alignShiftX + getParent().getX() + xFirstBeg; // + _lineXShift
        textPrt.yTextureShift = (int) alignShiftY + _lineYShift + getParent().getY();
    }

    String getText() {
        return getItemText();
    }

    /*
    private boolean needCoordUpd() {
        if (_needCheckWidth != getParent().getWidth())
            return true;
        if (_needCheckHeight != getParent().getHeight())
            return true;
        if (_needCheckX != getParent().getX())
            return true;
        if (_needCheckY != getParent().getY())
            return true;
        if (!_needCheckAlignment.equals(getTextAlignment()))
            return true;
        if (_needCheckPaddingRight != getParent().getPadding().right)
            return true;
        return (_needCheckPaddingBottom != getParent().getPadding().bottom);
    }
    */
    List<Integer> getLetPosArray() {
        return _letEndPos;
    }

    int getLetWidth(int count) {
        if (_letters == null)
            return 0;
        if ((count < 0) || (count >= _letters.size()))
            return 0;

        return _letters.get(count).width;
    }

    void setLineYShift(int sp) {
        _lineYShift = sp;
        // updateCoords();
        flagBB = true;
    }

    int getLineYShift() {
        return _lineYShift;
    }

    void setLineXShift(int sp) {
        // if (_lineXShift == sp) return;
        _lineXShift = sp;
        // updateCoords();
        flagBB = true;
    }

    int getLineXShift() {
        return _lineXShift;
    }

    float getLineTopCoord() {
        float lineTopCoord = 0;
        List<ItemAlignment> alignments = getTextAlignment();
        int[] fontDims = getFontDims();
        float height = fontDims[2];
        if (alignments.contains(ItemAlignment.BOTTOM))
            lineTopCoord = getParent().getHeight() - height;

        else if (alignments.contains(ItemAlignment.VCENTER))
            lineTopCoord = (getParent().getHeight() - height) / 2f;

        lineTopCoord += _lineYShift - fontDims[1];

        return lineTopCoord;
    }

    int[] getFontDims() {
        int[] output = FontEngine.getSpacerDims(getFont());
        // _minLineSpacer = output[0];
        // _minFontY = output[1];
        // _maxFontY = output[2];
        return output;
    }

    @Override
    public void setStyle(Style style) {
        setAlignment(style.alignment);
        setTextAlignment(style.textAlignment);
        setMargin(style.margin);
        setSizePolicy(style.widthPolicy, style.heightPolicy);
    }

    void setLineXShift() {
        setLineXShift(_lineXShift);
    }

    void checkXShift(int _cursorXMax) {
        if (getLetPosArray() == null || getLetPosArray().size() == 0)
            return;
        int s = getLetPosArray().get(getLetPosArray().size() - 1) - _cursorXMax;
        if (s <= 0)
            setLineXShift(0);
        else if ((s > 0) && (s + _lineXShift < 0)) {
            setLineXShift(-s);
        }
    }

    void setLineYShift() {
        setLineYShift(_lineYShift);
    }

    void setAllowWidth(int allowWidth) {
        if (_parentAllowWidth != allowWidth)
            flagBB = true;
        _parentAllowWidth = allowWidth;
    }

    void setAllowHeight(int allowHeight) {
        if (_parentAllowHeight != allowHeight)
            flagBB = true;
        _parentAllowHeight = allowHeight;
    }

    private int cursorWidth = 0;
    void setCursorWidth(int cwidth) {
        if (cursorWidth != cwidth)
            flagBB = true;
        cursorWidth = cwidth;
    }
}