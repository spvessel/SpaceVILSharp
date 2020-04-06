package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceTextContainer;
import com.spvessel.spacevil.Core.InterfaceTextImage;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.SpaceVILConstants;
import com.spvessel.spacevil.Common.DisplayService;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TextLine extends TextItem implements InterfaceTextContainer {
    private static int count = 0;

    private TextPrinter textPrt = new TextPrinter();
    private boolean _isUpdateNeed = false;

    private List<Integer> _letEndPos;
    private int _lineYShift = 0;
    private int _lineXShift = 0;
    private int _parentAllowWidth = SpaceVILConstants.sizeMaxValue;
    private int _parentAllowHeight = SpaceVILConstants.sizeMaxValue;
    private int _bigWidth = 0;
    private float _screenScale = 1;

    private List<Alphabet.ModifyLetter> _letters = new LinkedList<>();
    private List<Alphabet.ModifyLetter> _bigLetters = new LinkedList<>();

    private boolean _isRecountable = false;

    TextLine() {
        super();
        count++;
        updateData();
    }

    TextLine(String text, Font font) {
        super(text, font, "TextLine_" + count);
        count++;
        updateData();
    }

    private Lock textLock = new ReentrantLock();
    private boolean afterCreate = false;

    private void createText() {
        textLock.lock();
        try {
            afterCreate = true;
            int _lineWidth = 0;
            String text = getItemText();
            Font font = getFont();

            _letters = FontEngine.getModifyLetters(text, font);
            _letEndPos = new LinkedList<>();

            if (_letters.size() > 0) {
                _lineWidth = _letters.get(_letters.size() - 1).xShift + _letters.get(_letters.size() - 1).width
                        + _letters.get(_letters.size() - 1).xBeg; // xBeg не обязательно, т.к. везде 0, но вдруг
            }

            Alphabet.FontDimensions fontDims = getFontDims(); //int[] fontDims = getFontDims();
            super.setWidth(_lineWidth);
            super.setHeight(fontDims.height); //fontDims[2]);

            for (Alphabet.ModifyLetter modL : _letters) {
                _letEndPos.add(modL.xBeg + modL.xShift + modL.width);
            }

            CoreWindow wLayout = (getParent() == null) ? null : getParent().getHandler();
            if (wLayout == null) { // || wLayout.getDpiScale() == null) {
                _screenScale = 1; //0;
            } else {
                _screenScale = DisplayService.getDisplayDpiScale().getXScale();// wLayout.getDpiScale().getX();
                if (_screenScale != 1) {
                    makeBigArr();
                }
            }

        } finally {
            textLock.unlock();
        }
    }

//    private boolean isBigExist = false;

    private void makeBigArr() {
        if (getFont() == null) {
            return;
        }
        // Font fontBig = getFont().deriveFont(getFont().getStyle(), (int) (getFont().getSize() * _screenScale) 
        // new Font(getFont().getName(), getFont().getStyle(), (int) (getFont().getSize() * _screenScale));
               Font fontBig = GraphicsMathService.changeFontSize((int) (getFont().getSize() * _screenScale), getFont());

        _bigLetters = FontEngine.getModifyLetters(getItemText(), fontBig);
        // int[] output = FontEngine.getFontDims(fontBig);
        // _bigHeight = output[2];
        _bigWidth = 0;
        if (_bigLetters.size() > 0) {
            _bigWidth = _bigLetters.get(_bigLetters.size() - 1).xShift + _bigLetters.get(_bigLetters.size() - 1).width
                    + _bigLetters.get(_bigLetters.size() - 1).xBeg; // xBeg не обязательно, т.к. везде 0, но вдруг
            super.setWidth((int) ((float) _bigWidth / _screenScale));
        }

        // _bigMinY = output[1];

        // if (_screenScale != 0) 
        {
            _letEndPos = new LinkedList<>();
            for (Alphabet.ModifyLetter modL : _bigLetters) {
                _letEndPos.add((int) ((float) (modL.xBeg + modL.xShift + modL.width) / _screenScale));
            }
        }

//        isBigExist = true;
    }

    public InterfaceTextImage getTexture() {
        textLock.lock();
        try {
            Alphabet.FontDimensions fontDims = getFontDims(); //int[] fontDims = getFontDims();
            int height = fontDims.height; //[2];
            if (getHeight() != height) {
                super.setHeight(height);
            }

            Prototype parent = getParent();
            if (parent == null) { //Обязательно
                return null;
            }

            CoreWindow wLayout = parent.getHandler();
            if (wLayout != null) { // && wLayout.getDpiScale() != null) {
                float scl = DisplayService.getDisplayDpiScale().getXScale();// wLayout.getDpiScale().getX();
                if (scl != _screenScale) { //} && !isBigExist) { //Это при допущении, что скейл меняется только один раз!
                    if (_screenScale != 0 || scl != 1) {
                        //Возможно может возникнуть проблема при переходе от большего к меньшему
                        _screenScale = scl;
                        makeBigArr();
                    }
                }
            }

            if (_isRecountable) {
                if (_lineYShift - fontDims.minY + height < 0 || _lineYShift - fontDims.minY > _parentAllowHeight) { //(_lineYShift - fontDims[1] + height < 0 || _lineYShift - fontDims[1] > _parentAllowHeight) {
                    return null;
                }
            }
            if (_letters.size() == 0) {
                return new TextPrinter(); //null;
            }
            if (_isUpdateNeed && (_isRecountable || afterCreate)) {
                afterCreate = false;
                int bb_h = getHeight();
                int bb_w = getWidth();

                if (_parentAllowWidth > 0 && _isRecountable) {
                    bb_w = bb_w > _parentAllowWidth ? _parentAllowWidth : bb_w;
                }

                // ByteBuffer cacheBB = BufferUtils.createByteBuffer(bb_h * bb_w * 4);
                byte[] cacheBB = new byte[bb_h * bb_w * 4];

                int xFirstBeg = _letters.get(0).xBeg + _letters.get(0).xShift;

                if (_screenScale != 1) { // 0 && _screenScale != 1) {
                    Font fontBig = new Font(getFont().getName(), getFont().getStyle(),
                            (int) (getFont().getSize() * _screenScale));
                    // Font fontBig = GraphicsMathService.changeFontSize((int) (getFont().getSize() * _screenScale), getFont());

                    Alphabet.FontDimensions bigFontDims = FontEngine.getFontDims(fontBig); //int[] output = FontEngine.getFontDims(fontBig);
                    bb_h = bigFontDims.height; //output[2];
                    bb_w = _bigWidth;
                    if (_isRecountable) {
                        bb_w = _bigWidth > (int) (_parentAllowWidth * _screenScale)
                                ? (int) (_parentAllowWidth * _screenScale)
                                : _bigWidth;
                    }

                    int bigMinY = bigFontDims.minY; //output[1];
                    cacheBB = makeSomeBig(bb_h, bb_w, bigMinY, 0, _letters.size() - 1);
                } else {
                    for (Alphabet.ModifyLetter modL : _letters) {
                        int widthFrom = 0;
                        int widthTo = modL.width;

                        if (_isRecountable) {
                            if (modL.xBeg + modL.xShift + modL.width + _lineXShift < 0) { //До разрешенной области
                                continue;
                            }
                            if (modL.xBeg + modL.xShift + _lineXShift <= 0) {
                                widthFrom = Math.abs(modL.xBeg + modL.xShift + _lineXShift);
                            }

                            xFirstBeg = -_lineXShift;

                            if (modL.xBeg + modL.xShift - xFirstBeg > _parentAllowWidth) { //После разрешенной области + _lineXShift
                                break;
                            }
                            if (modL.xBeg + modL.xShift + modL.width - xFirstBeg >= _parentAllowWidth) { // + _lineXShift
                                widthTo = _parentAllowWidth - (modL.xBeg + modL.xShift + widthFrom - xFirstBeg); // + _lineXShift
                            }
                        }

                        byte[] bitmap = modL.getArr();
                        if (bitmap == null) { //?spec let
                            continue;
                        }

                        int offset = (modL.yBeg - fontDims.minY) * 4 * bb_w //(modL.yBeg - fontDims[1]) * 4 * bb_w
                                + (modL.xBeg + modL.xShift + widthFrom - xFirstBeg) * 4;

                        for (int j = 0; j < modL.height; j++) {
                            for (int i = widthFrom; i < widthTo; i++) {
                                int b1 = bitmap[3 + j * 4 + i * (modL.height * 4)] & 0xFF;
                                // int b2 = cacheBB.get(3 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)) & 0xFF;
                                int b2 = cacheBB[3 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)] & 0xFF;
                                if (b1 < b2) {
                                    continue;
                                }

                                // cacheBB.put(0 + offset + (i - widthFrom) * 4 + j * (bb_w * 4), bitmap[0 + j * 4 + i * (modL.height * 4)]);
                                // cacheBB.put(1 + offset + (i - widthFrom) * 4 + j * (bb_w * 4), bitmap[1 + j * 4 + i * (modL.height * 4)]);
                                // cacheBB.put(2 + offset + (i - widthFrom) * 4 + j * (bb_w * 4), bitmap[2 + j * 4 + i * (modL.height * 4)]);
                                // cacheBB.put(3 + offset + (i - widthFrom) * 4 + j * (bb_w * 4), bitmap[3 + j * 4 + i * (modL.height * 4)]);
                                cacheBB[0 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)] = bitmap[0 + j * 4
                                        + i * (modL.height * 4)];
                                cacheBB[1 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)] = bitmap[1 + j * 4
                                        + i * (modL.height * 4)];
                                cacheBB[2 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)] = bitmap[2 + j * 4
                                        + i * (modL.height * 4)];
                                cacheBB[3 + offset + (i - widthFrom) * 4 + j * (bb_w * 4)] = bitmap[3 + j * 4
                                        + i * (modL.height * 4)];
                            }
                        }
                    }
                }
                // cacheBB.rewind();
                _isUpdateNeed = false;
                textPrt = new TextPrinter(cacheBB);
                textPrt.setSize(bb_w, bb_h);
                ItemsRefreshManager.setRefreshText(this);
            }
            updateCoords(parent);
            return textPrt;
        } finally {
            textLock.unlock();
        }
    }

    /*
    private int[] findFirstLast(List<Alphabet.ModifyLetter> letList, int winWidth) { //, float someShift) {
        int firstInd = 0, lastInd = 0;
        int someShift = (int) (_lineXShift * _screenScale);
        boolean isFirstFound = false;
    
        for (int ii = 0; ii < letList.size(); ii++) {
            Alphabet.ModifyLetter modL = letList.get(ii);
    
            if (modL.xBeg + modL.xShift + modL.width + someShift < 0) { // До разрешенной области
                continue;
            }
    
            if (!isFirstFound) {
                firstInd = ii;
                isFirstFound = true;
            }
    
            if (modL.xBeg + modL.xShift + modL.width + someShift >= winWidth) {
                lastInd = ii;
                break;
            }
        }
    
        if (lastInd == 0) lastInd = letList.size() - 1;
    
        Alphabet.ModifyLetter letFirst = letList.get(firstInd);
        Alphabet.ModifyLetter letLast = letList.get(lastInd);
    
        int visWidth = letLast.xShift + letLast.width + letLast.xBeg - letFirst.xBeg - letFirst.xShift;
    
        return new int[]{firstInd, lastInd, visWidth}; //, outShift};
    }
    */

    private byte[] makeSomeBig(int hgt, int wdt, int bigMinY, int firstInd, int lastInd) {
        // ByteBuffer outCache = BufferUtils.createByteBuffer(hgt * wdt * 4);
        byte[] outCache = new byte[hgt * wdt * 4];
        int someShift = (int) (_lineXShift * _screenScale);
        int parWidth = (int) (_parentAllowWidth * _screenScale);

        int xFirstBeg = _bigLetters.get(firstInd).xBeg + _bigLetters.get(firstInd).xShift;

        for (int ii = firstInd; ii <= lastInd; ii++) {
            Alphabet.ModifyLetter bigLet = _bigLetters.get(ii);

            //ignore at first
            int widthFrom = 0;
            int widthTo = bigLet.width;

            if (_isRecountable) {
                if (bigLet.xBeg + bigLet.xShift + bigLet.width + someShift < 0) { // До разрешенной области
                    continue;
                }
                if (bigLet.xBeg + bigLet.xShift + someShift <= 0) {
                    widthFrom = Math.abs(bigLet.xBeg + bigLet.xShift + someShift);
                }

                xFirstBeg = -someShift;

                if (bigLet.xBeg + bigLet.xShift - xFirstBeg > parWidth) { // После разрешенной области + _lineXShift
                    break;
                }
                if (bigLet.xBeg + bigLet.xShift + bigLet.width - xFirstBeg >= parWidth) {
                    widthTo = parWidth - (bigLet.xBeg + bigLet.xShift + widthFrom - xFirstBeg);
                }
            }

            byte[] bitmap = bigLet.getArr();
            if (bitmap == null) {
                continue;
            }

            int offset = (bigLet.yBeg - bigMinY) * 4 * wdt + (bigLet.xBeg + bigLet.xShift + widthFrom - xFirstBeg) * 4;

            for (int j = 0; j < bigLet.height; j++) {
                for (int i = widthFrom; i < widthTo; i++) {
                    int b1 = bitmap[3 + j * 4 + i * (bigLet.height * 4)] & 0xFF;
                    // int b2 = outCache.get(3 + offset + (i - widthFrom) * 4 + j * (wdt * 4)) & 0xFF;
                    int b2 = outCache[3 + offset + (i - widthFrom) * 4 + j * (wdt * 4)] & 0xFF;
                    if (b1 < b2) {
                        continue;
                    }

                    // outCache.put(0 + offset + (i - widthFrom) * 4 + j * (wdt * 4),
                    //         bitmap[0 + j * 4 + i * (bigLet.height * 4)]);
                    // outCache.put(1 + offset + (i - widthFrom) * 4 + j * (wdt * 4),
                    //         bitmap[1 + j * 4 + i * (bigLet.height * 4)]);
                    // outCache.put(2 + offset + (i - widthFrom) * 4 + j * (wdt * 4),
                    //         bitmap[2 + j * 4 + i * (bigLet.height * 4)]);
                    // outCache.put(3 + offset + (i - widthFrom) * 4 + j * (wdt * 4),
                    //         bitmap[3 + j * 4 + i * (bigLet.height * 4)]);
                    outCache[0 + offset + (i - widthFrom) * 4 + j * (wdt * 4)] = bitmap[0 + j * 4
                            + i * (bigLet.height * 4)];
                    outCache[1 + offset + (i - widthFrom) * 4 + j * (wdt * 4)] = bitmap[1 + j * 4
                            + i * (bigLet.height * 4)];
                    outCache[2 + offset + (i - widthFrom) * 4 + j * (wdt * 4)] = bitmap[2 + j * 4
                            + i * (bigLet.height * 4)];
                    outCache[3 + offset + (i - widthFrom) * 4 + j * (wdt * 4)] = bitmap[3 + j * 4
                            + i * (bigLet.height * 4)];
                }
            }
        }

        return outCache;
    }

    @Override
    public void setWidth(int width) {
        setAllowWidth(width);
    }

    @Override
    public void setHeight(int height) {
        setAllowHeight(height);
    }

    @Override
    public void updateData() {
        textLock.lock();
        try {
            if (getFont() == null) {
                return;
            }
            createText();
            _isUpdateNeed = true;
        } finally {
            textLock.unlock();
        }
    }

    private void updateCoords(Prototype parent) {
        if (_letters.size() == 0)
            return;
        Alphabet.FontDimensions fontDims = getFontDims(); // int[] fontDims = getFontDims();
        int height = fontDims.height; //fontDims[2];

        List<ItemAlignment> alignments = getTextAlignment();
        float alignShiftX = 1;
        float alignShiftY = 0;

        int _lineWidth = getWidth();
        // Horizontal
        if (alignments.contains(ItemAlignment.LEFT) || (_lineWidth >= _parentAllowWidth)) {
            alignShiftX = parent.getPadding().left + getMargin().left + cursorWidth;
        } else if (alignments.contains(ItemAlignment.RIGHT) && (_lineWidth < _parentAllowWidth)) {
            alignShiftX = parent.getWidth() - _lineWidth - parent.getPadding().right - getMargin().right - cursorWidth;
        } else if (alignments.contains(ItemAlignment.HCENTER) && (_lineWidth < _parentAllowWidth)) {
            // alignShiftX = ((parent.getWidth() - parent.getPadding().left - parent.getPadding().right
            //         + getMargin().left - getMargin().right) - _lineWidth) / 2f;
            alignShiftX = (_parentAllowWidth - _lineWidth) / 2f + parent.getPadding().left + getMargin().left
                    + cursorWidth; //(parent.getWidth() - _lineWidth) / 2f + parent.getPadding().left;
        }
        // Vertical
        if (alignments.contains(ItemAlignment.TOP)) {
            alignShiftY = parent.getPadding().top + getMargin().top;
        } else if (alignments.contains(ItemAlignment.BOTTOM)) {
            alignShiftY = parent.getHeight() - height - parent.getPadding().bottom - getMargin().bottom;
        } else if (alignments.contains(ItemAlignment.VCENTER)) {
            // alignShiftY = ((parent.getHeight() - parent.getPadding().bottom - parent.getPadding().top)
            //         - height) / 2f - getMargin().bottom + getMargin().top;
            alignShiftY = (parent.getHeight() - height) / 2f + parent.getPadding().top;
        }

        int xFirstBeg = _letters.get(0).xBeg + _letters.get(0).xShift;
        textPrt.setPosition((int) alignShiftX + parent.getX() + xFirstBeg,
                (int) alignShiftY + _lineYShift + parent.getY());

        if (!_isRecountable) {
            textPrt.setXOffset(textPrt.getXOffset() + _lineXShift);
        }
    }

    String getText() {
        return getItemText();
    }

    List<Integer> getLetPosArray() {
        if (_isUpdateNeed) {
            updateData();
        }
        return _letEndPos;
    }

    int getLetWidth(int count) {
        if (_letters == null) {
            return 0;
        }
        if ((count < 0) || (count >= _letters.size())) {
            return 0;
        }

        return _letters.get(count).width;
    }

    void setLineYShift(int sp) {
        _lineYShift = sp;
        // updateCoords();
        _isUpdateNeed = true;
    }

    int getLineYShift() {
        return _lineYShift;
    }

    void setLineXShift(int sp) {
        // if (_lineXShift == sp) return;
        _lineXShift = sp;
        // updateCoords();
        _isUpdateNeed = true;

    }

    int getLineXShift() {
        return _lineXShift;
    }

    //    float getLineTopCoord() {
    //        float lineTopCoord = 0;
    //        List<ItemAlignment> alignments = getTextAlignment();
    //        int[] fontDims = getFontDims();
    //        float height = fontDims[2];
    //        if (alignments.contains(ItemAlignment.BOTTOM))
    //            lineTopCoord = getParent().getHeight() - height;
    //
    //        else if (alignments.contains(ItemAlignment.VCENTER))
    //            lineTopCoord = (getParent().getHeight() - height) / 2f;
    //
    //        lineTopCoord += _lineYShift - fontDims[1];
    //
    //        return lineTopCoord;
    //    }

    Alphabet.FontDimensions getFontDims() {
        return FontEngine.getFontDims(getFont());
    }

    @Override
    public void setStyle(Style style) {
        setAlignment(style.alignment);
        setTextAlignment(style.textAlignment);
        setMargin(style.margin);
        setSizePolicy(style.widthPolicy, style.heightPolicy);
    }

    //    void setLineXShift() {
    //        setLineXShift(_lineXShift);
    //    }

    void checkXShift(int _cursorXMax) {
        if (getLetPosArray() == null || getLetPosArray().size() == 0) {
            return;
        }
        int s = getLetPosArray().get(getLetPosArray().size() - 1) - _cursorXMax;
        if (s <= 0) {
            setLineXShift(0);
        } else if (s + _lineXShift < 0) { //&& (s > 0)
            setLineXShift(-s);
        }
    }

    //    void setLineYShift() {
    //        setLineYShift(_lineYShift);
    //    }

    void setAllowWidth(int allowWidth) {
        if (_parentAllowWidth != allowWidth) {
            _isUpdateNeed = true;
        }
        _parentAllowWidth = allowWidth;
    }

    void setAllowHeight(int allowHeight) {
        if (_parentAllowHeight != allowHeight) {
            _isUpdateNeed = true;
        }
        _parentAllowHeight = allowHeight;
    }

    private int cursorWidth = 0;

    void setCursorWidth(int cwidth) {
        if (cursorWidth != cwidth) {
            _isUpdateNeed = true;
        }
        cursorWidth = cwidth;
    }

    void setRecountable(boolean isRecountable) {
        _isRecountable = isRecountable;
    }
}