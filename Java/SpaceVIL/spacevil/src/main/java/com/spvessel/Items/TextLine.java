package com.spvessel.Items;

import com.spvessel.Decorations.Style;
import com.spvessel.Engine.Alphabet;
import com.spvessel.Engine.FontEngine;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Cores.InterfaceTextContainer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class TextLine extends TextItem implements InterfaceTextContainer {
    private static int count = 0;

    private int _minLineSpacer;
    private int _minFontY;
    private int _maxFontY;
    // private List<float> _coordArray; //private List<List<float>> _coordArray;
    private int _lineWidth = 0; // private float[] _lineWidth;
    private List<Integer> _letEndPos;
    private int _lineYShift = 0;
    private int _lineXShift = 0;
    private int _parentAllowWidth = Integer.MAX_VALUE;
    private int _parentAllowHeight = Integer.MAX_VALUE;

    private List<Alphabet.ModifyLetter> _letters;

    private List<Float> px0;
    private List<ItemAlignment> _needCheckAlignment = new LinkedList<>();
    private int _needCheckPaddingRight;
    private int _needCheckPaddingBottom;
    private int _needCheckWidth;
    private int _needCheckHeight;
    private int _needCheckX;
    private int _needCheckY;

    public TextLine() {
        count++;
    }

    public TextLine(String text, Font font) {
        super(text, font, "TextLine_" + count);
        count++;
        getFontDims();
        updateData();
    }

    public void createText() {
        //long time = System.nanoTime();
        String text = getItemText();
        Font font = getFont();

        _letters = FontEngine.getPixMap(text, font);
        //long time0 = System.nanoTime();
        //System.out.println("get pix map " + (time0 - time) / 100000f);

        _letEndPos = new LinkedList<>();

        if (_letters.size() > 0)
            _lineWidth = _letters.get(_letters.size() - 1).xShift + _letters.get(_letters.size() - 1).width
                    + _letters.get(_letters.size() - 1).xBeg; // xBeg не обязательно, т.к. везде 0, но вдруг
                                                              // 

        super.setWidth(_lineWidth);
        super.setHeight(Math.abs(_maxFontY - _minFontY));


        px0 = new LinkedList<>();
        List<Float> cl0 = new LinkedList<>();
        List<Float> tmpPx;
        float xc, yc;
        for (Alphabet.ModifyLetter modL : _letters) {
            _letEndPos.add(modL.xBeg + modL.xShift + modL.width);
            //px0.addAll(modL.getPix());
            tmpPx = modL.getPix();
            for (int i = 0; i < tmpPx.size() / 3; i++) {
                xc = modL.xBeg + modL.xShift;
                yc = - _minFontY + modL.yBeg;
                px0.add(tmpPx.get(i * 3) + xc);
                px0.add(tmpPx.get(i * 3 + 1) + yc);
                px0.add(tmpPx.get(i * 3 + 2));
            }

            cl0.addAll(modL.getCol());
        }

        //long time1 = System.nanoTime();
        //System.out.println("new arrays " + (time1 - time0) / 100000f);

        addAllShifts();

        //long time2 = System.nanoTime();
        //System.out.println("Add all shifts " + (time2 - time1) / 100000f);
        //System.out.println();

        setAlphas(cl0);
    }

    @Override
    public void setWidth(int width) {
        setAllowWidth(width);
    }

    @Override
    public void setHeight(int height) {
        setAllowHeight(height);
    }

    private void addAllShifts() {
        //long time0 = System.nanoTime();

        if (getParent() == null)
            return;

        if (_letters == null)
            return;

        List<Float> outRealCoords = new LinkedList<>(px0);
        //List<Float> alphas = new LinkedList<>();

        List<ItemAlignment> alignments = getTextAlignment();
        float alignShiftX = 1;
        float alignShiftY = 0;

        float height = Math.abs(_maxFontY - _minFontY);

        if (_lineYShift - _minFontY + height < 0 || _lineYShift - _minFontY > _parentAllowHeight) {
            //setAlphas(alphas);
            setRealCoords(outRealCoords);
            return;
        }

        // Horizontal
        if (alignments.contains(ItemAlignment.RIGHT) && (_lineWidth < _parentAllowWidth))
            alignShiftX = getParent().getWidth() - _lineWidth - getParent().getPadding().right;

        else if (alignments.contains(ItemAlignment.HCENTER) && (_lineWidth < _parentAllowWidth))
            alignShiftX = (getParent().getWidth() - _lineWidth) / 2f;

        // Vertical
        if (alignments.contains(ItemAlignment.BOTTOM))
            alignShiftY = getParent().getHeight() - height - getParent().getPadding().bottom;

        else if (alignments.contains(ItemAlignment.VCENTER))
            alignShiftY = (getParent().getHeight() - height) / 2f;

        //List<Float> tmpList;
        float xCoord, yCoord;

        //long time1 = System.nanoTime();
        //System.out.println("Prepare is end " + (time1 - time0) / 100000f);

        for (int j = 0; j < px0.size() / 3; j++) {
            //tmpList = modL.getPix();
            xCoord = alignShiftX + _lineXShift + px0.get(j * 3);
            yCoord = alignShiftY + _lineYShift + px0.get(j * 3 + 1);

            if (xCoord < 0)
                continue;
            if (xCoord > _parentAllowWidth)
                break;

            //alphas.addAll(modL.getCol());

            outRealCoords.set(j * 3, xCoord);
            outRealCoords.set(j * 3 + 1, yCoord);
            outRealCoords.set(j * 3 + 2, px0.get(j * 3 + 2));

        }

        //long time2 = System.nanoTime();
        //System.out.println("Add alphas and colors " + (time2 - time1) / 100000f);
        //System.out.println("shifts");
        //setAlphas(alphas);
        setRealCoords(outRealCoords);

        _needCheckAlignment = alignments;
        _needCheckPaddingRight = getParent().getPadding().right;
        _needCheckPaddingBottom = getParent().getPadding().bottom;
        _needCheckWidth = getParent().getWidth();
        _needCheckHeight = getParent().getHeight();
        _needCheckX = getParent().getX();
        _needCheckY = getParent().getY();
    }

    private void oldaddAllShifts() {
        //long time0 = System.nanoTime();

        if (getParent() == null)
            return;

        if (_letters == null)
            return;

        List<Float> outRealCoords = new LinkedList<>();
        List<Float> alphas = new LinkedList<>();

        List<ItemAlignment> alignments = getTextAlignment();
        float alignShiftX = 1;
        float alignShiftY = 0;

        float height = Math.abs(_maxFontY - _minFontY);

        if (_lineYShift - _minFontY + height < 0 || _lineYShift - _minFontY > _parentAllowHeight) {
            setAlphas(alphas);
            setRealCoords(outRealCoords);
            return;
        }

        // Horizontal
        if (alignments.contains(ItemAlignment.RIGHT) && (_lineWidth < _parentAllowWidth))
            alignShiftX = getParent().getWidth() - _lineWidth - getParent().getPadding().right;

        else if (alignments.contains(ItemAlignment.HCENTER) && (_lineWidth < _parentAllowWidth))
            alignShiftX = (getParent().getWidth() - _lineWidth) / 2f;

        // Vertical
        if (alignments.contains(ItemAlignment.BOTTOM))
            alignShiftY = getParent().getHeight() - height - getParent().getPadding().bottom;

        else if (alignments.contains(ItemAlignment.VCENTER))
            alignShiftY = (getParent().getHeight() - height) / 2f;

        List<Float> tmpList;
        float xCoord, yCoord;

        //long time1 = System.nanoTime();
        //System.out.println("Prepare is end " + (time1 - time0) / 100000f);

        for (Alphabet.ModifyLetter modL : _letters) {
            tmpList = modL.getPix();
            xCoord = alignShiftX + modL.xBeg + modL.xShift + _lineXShift;
            yCoord = alignShiftY + _lineYShift - _minFontY + modL.yBeg;

            if (xCoord + modL.width < 0)
                continue;
            if (xCoord > _parentAllowWidth)
                break;

            alphas.addAll(modL.getCol());
            //for (Float var : modL.getCol())
            //    alphas.add(var);


            for (int j = 0; j < tmpList.size() / 3; j++) {
                outRealCoords.add(tmpList.get(j * 3) + xCoord);
                outRealCoords.add(tmpList.get(j * 3 + 1) + yCoord);
                outRealCoords.add(tmpList.get(j * 3 + 2));
            }
        }

        //long time2 = System.nanoTime();
        //System.out.println("Add alphas and colors " + (time2 - time1) / 100000f);
        //System.out.println();
        setAlphas(alphas);
        setRealCoords(outRealCoords);
    }

    @Override
    public void updateData() {
        if (getFont() == null)
            return;

        int[] output = FontEngine.getSpacerDims(getFont());
        _minLineSpacer = output[0];
        _minFontY = output[1];
        _maxFontY = output[2];
        createText();
    }

    @Override
    protected void updateCoords() {
        addAllShifts();
    }

    public TextItem getText() {
        return this;
    }

    @Override
    public float[] shape() {
        if (needCoordUpd())
            addAllShifts();
        return super.shape();
    }

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

    public List<Integer> getLetPosArray() {
        return _letEndPos;
    }

    public int getLetWidth(int count) {
        if (_letters == null)
            return 0;
        if ((count < 0) || (count >= _letters.size()))
            return 0;

        return _letters.get(count).width;
    }

    public void setLineYShift(int sp) {
        _lineYShift = sp;
        updateCoords();
    }

    public int getLineYShift() {
        return _lineYShift;
    }

    public void setLineXShift(int sp) {
        // if (_lineXShift == sp) return;
        _lineXShift = sp;
        updateCoords();
    }

    public int getLineXShift() {
        return _lineXShift;
    }

    public float getLineTopCoord() {
        float lineTopCoord = 0;
        List<ItemAlignment> alignments = getTextAlignment();
        float height = Math.abs(_maxFontY - _minFontY);
        if (alignments.contains(ItemAlignment.BOTTOM))
            lineTopCoord = getParent().getHeight() - height;

        else if (alignments.contains(ItemAlignment.VCENTER))
            lineTopCoord = (getParent().getHeight() - height) / 2f;

        lineTopCoord += _lineYShift - _minFontY;

        return lineTopCoord;
    }

    public int[] getFontDims() {
        int[] output = FontEngine.getSpacerDims(getFont());
        _minLineSpacer = output[0];
        _minFontY = output[1];
        _maxFontY = output[2];
        return output;
    }

    @Override
    public void setStyle(Style style) {
        setAlignment(style.alignment);
        setTextAlignment(style.textAlignment);
        setMargin(style.margin);
        setSizePolicy(style.widthPolicy, style.heightPolicy);
    }

    public void setLineXShift() {
        setLineXShift(_lineXShift);
    }

    public void checkXShift(int _cursorXMax) {
        if (getLetPosArray() == null || getLetPosArray().size() == 0)
            return;
        int s = getLetPosArray().get(getLetPosArray().size() - 1) - _cursorXMax;
        if (s <= 0)
            setLineXShift(0);
        else if ((s > 0) && (s + _lineXShift < 0)) {
            setLineXShift(-s);
        }
    }

    public void setLineYShift() {
        setLineYShift(_lineYShift);
    }

    public void setAllowWidth(int allowWidth) {
        _parentAllowWidth = allowWidth;
    }

    public void setAllowHeight(int allowHeight) {
        _parentAllowHeight = allowHeight;
    }

}