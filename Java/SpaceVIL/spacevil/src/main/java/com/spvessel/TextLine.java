package com.spvessel;

import com.spvessel.Core.InterfaceTextContainer;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ItemAlignment;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TextLine extends TextItem implements InterfaceTextContainer {
    private static int count = 0;

    private TextPrinter textPrt = new TextPrinter();
    private boolean flagBB = false;

    // private int _minLineSpacer;
    // private int _minFontY;
    // private int _maxFontY;
    // private List<float> _coordArray; //private List<List<float>> _coordArray;
    private int _lineWidth = 0; // private float[] _lineWidth;
    private List<Integer> _letEndPos;
    private int _lineYShift = 0;
    private int _lineXShift = 0;
    private int _parentAllowWidth = Integer.MAX_VALUE;
    private int _parentAllowHeight = Integer.MAX_VALUE;

    private List<Alphabet.ModifyLetter> _letters = new LinkedList<>();
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
    public TextLine() {
        count++;
    }

    public TextLine(String text, Font font) {
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
        } finally {
            textLock.unlock();
        }
    }

    public TextPrinter getLetTextures() {
        textLock.lock();
        try {
            if (flagBB) {
                int[] fontDims = getFontDims();
                int height = fontDims[2];
                if (getHeight() != height)
                    super.setHeight(height);

                // List<TextPrinter> letTexturesList = new LinkedList<>();
                if (_letters.size() == 0)
                    return null;
                if (_lineYShift - fontDims[1] + height < 0 || _lineYShift - fontDims[1] > _parentAllowHeight)
                    return null;

                int bb_h = getHeight();
                int bb_w = getWidth();

                ByteBuffer cacheBB = BufferUtils.createByteBuffer(bb_h * bb_w * 4);

                int xFirstBeg = _letters.get(0).xBeg + _letters.get(0).xShift;
                // _xpos = (int) alignShiftX + _lineXShift + getParent().getX() + xFirstBeg;
                // _ypos = (int) alignShiftY + _lineYShift + getParent().getY();

                for (Alphabet.ModifyLetter modL : _letters) {
                    // int x = modL.xBeg + (int) alignShiftX + _lineXShift + modL.xShift;
                    // int y = (modL.yBeg - _minFontY) + (int) alignShiftY + _lineYShift;

                    // x += getParent().getX();
                    // y += getParent().getY();
                    // if (x < 0)
                    // continue;
                    // if (x > _parentAllowWidth)
                    // break;

                    byte[] bitmap = modL.getArr();
                    if (bitmap == null) {
                        continue;
                    }

                    // int w = letTx.letWidth;
                    // int h = letTx.letHeight;

                    // x -= xpos;
                    // y -= _ypos;

                    int offset = (modL.yBeg - fontDims[1]) * 4 * bb_w + (modL.xBeg + modL.xShift - xFirstBeg) * 4;
                    // if((modL.yBeg - fontDims[1]) > fontDims[2]) {
                    // System.out.println("modL." + fontDims[1] + " " + modL.yBeg);
                    // }
                    // System.err.println(bb_w + " " + bb_h + " " + (modL.width + modL.xBeg +
                    // modL.xShift - xFirstBeg) + " "
                    // + (modL.height + modL.yBeg - _minFontY) + " " + offset);

                    for (int j = 0; j < modL.height; j++) {
                        for (int i = 0; i < modL.width; i++) {
                            //System.out.print(cacheBB.get(3 + offset + j * 4 + i * (bb_h * 4)) + " ");
                            int b1 = bitmap[3 + j * 4 + i * (modL.height * 4)] & 0xFF;
                            int b2 = cacheBB.get(3 + offset + i * 4 + j * (bb_w * 4)) & 0xFF;
                            if (b1 < b2)
                                continue;

                            cacheBB.put(0 + offset + i * 4 + j * (bb_w * 4), bitmap[0 + j * 4 + i * (modL.height * 4)]);
                            cacheBB.put(1 + offset + i * 4 + j * (bb_w * 4), bitmap[1 + j * 4 + i * (modL.height * 4)]);
                            cacheBB.put(2 + offset + i * 4 + j * (bb_w * 4), bitmap[2 + j * 4 + i * (modL.height * 4)]);
                            cacheBB.put(3 + offset + i * 4 + j * (bb_w * 4), bitmap[3 + j * 4 + i * (modL.height * 4)]);
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

        // Horizontal
        if (alignments.contains(ItemAlignment.LEFT)) {
            alignShiftX = getParent().getPadding().left + getMargin().left;
        } else if (alignments.contains(ItemAlignment.RIGHT) && (_lineWidth < _parentAllowWidth))
            alignShiftX = getParent().getWidth() - _lineWidth - getParent().getPadding().right - getMargin().right;

        else if (alignments.contains(ItemAlignment.HCENTER) && (_lineWidth < _parentAllowWidth))
            alignShiftX = ((getParent().getWidth() - getParent().getPadding().left - getParent().getPadding().right
                    + getMargin().left - getMargin().right) - _lineWidth) / 2f;

        // Vertical
        if (alignments.contains(ItemAlignment.TOP)) {
            // System.out.println(getMargin().top);
            alignShiftY = getParent().getPadding().top + getMargin().top;
        } else if (alignments.contains(ItemAlignment.BOTTOM))
            alignShiftY = getParent().getHeight() - height - getParent().getPadding().bottom - getMargin().bottom;

        else if (alignments.contains(ItemAlignment.VCENTER))
            alignShiftY = ((getParent().getHeight() - getParent().getPadding().bottom - getParent().getPadding().top)
                    - height) / 2f - getMargin().bottom + getMargin().top;

        int xFirstBeg = _letters.get(0).xBeg + _letters.get(0).xShift;
        textPrt.xTextureShift = (int) alignShiftX + _lineXShift + getParent().getX() + xFirstBeg;
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
    }

    int getLineYShift() {
        return _lineYShift;
    }

    void setLineXShift(int sp) {
        // if (_lineXShift == sp) return;
        _lineXShift = sp;
        // updateCoords();
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
        _parentAllowWidth = allowWidth;
    }

    void setAllowHeight(int allowHeight) {
        _parentAllowHeight = allowHeight;
    }
}