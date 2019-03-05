package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Core.InterfaceTextContainer;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Flags.ItemAlignment;
import org.lwjgl.BufferUtils;

final class TextureStorage extends Primitive implements InterfaceTextContainer {
    private static int count = 0;
    private List<TextLine> _linesList;
    private List<ItemAlignment> _blockAlignment = new LinkedList<>(
            Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
    private Font _elementFont;
    private int _lineSpacer;
    private int _minLineSpacer;
    private int _lineHeight;

    private int globalXShift = 0;
    private int globalYShift = 0;
    private int _cursorXMax = Integer.MAX_VALUE;
    private int _cursorYMax = Integer.MAX_VALUE;

    Lock textInputLock = new ReentrantLock();

    TextureStorage() {
        super("TextureStorage_" + count);

        _linesList = new LinkedList<>();
        TextLine te = new TextLine();
        te.setRecountable(true);
        if (getForeground() != null)
            te.setForeground(getForeground());
        te.setTextAlignment(_blockAlignment);
        if (_elementFont != null)
            te.setFont(_elementFont);
        _linesList.add(te);

        getDims();
    }

    int scrollBlockUp(int cursorY) {
        int h = getTextHeight();
        int curCoord = cursorY;

        if (h < _cursorYMax && globalYShift >= 0)
            return curCoord;
        // if ()
        // return curCoord;

        curCoord -= globalYShift;

        globalYShift += getLineY(1);
        if (globalYShift > 0)
            globalYShift = 0;

        updLinesYShift();
        return (curCoord + globalYShift);
    }

    int scrollBlockDown(int cursorY) {
        int h = getTextHeight();
        int curCoord = cursorY;

        if (h < _cursorYMax && h + globalYShift <= _cursorYMax)
            return curCoord;
        // if () return curCoord;

        curCoord -= globalYShift;

        globalYShift -= getLineY(1);
        if (h + globalYShift < _cursorYMax)
            globalYShift = _cursorYMax - h;

        updLinesYShift();
        return (curCoord + globalYShift);
    }

    int getCursorHeight() {
        return (_lineHeight + _lineSpacer);
    }

    void setBlockWidth(int width, int curWidth) {
        cursorWidth = curWidth;
        if (getParent() == null)
            return;
        _cursorXMax = getParent().getWidth() - cursorWidth - getParent().getPadding().left - getParent().getPadding().right
                - getTextMargin().left - getTextMargin().right;

        setAllowWidth();
        updLinesXShift();
    }

    void setBlockHeight(int height) {
        if (getParent() == null)
            return;
        _cursorYMax = getParent().getHeight() - getParent().getPadding().top - getParent().getPadding().bottom
                - getTextMargin().top - getTextMargin().bottom;
        setAllowHeight();
        updLinesYShift();
    }

    private int cursorWidth = 0;
    void initLines(int curWidth) {
        cursorWidth = curWidth;
        Indents textMargin = getTextMargin();

        _cursorXMax = getParent().getWidth() - cursorWidth - getParent().getPadding().left - getParent().getPadding().right
                - textMargin.left - textMargin.right;

        _cursorYMax = getParent().getHeight() - getParent().getPadding().top - getParent().getPadding().bottom
                - textMargin.top - textMargin.bottom;

        addAllLines();
        setAllowWidth();
        setAllowHeight();
        updLinesXShift();
        updLinesYShift();
    }

    void setLineContainerAlignment(List<ItemAlignment> alignment) {
        for (TextLine tl : _linesList)
            tl.setAlignment(alignment);
    }

    private void addAllLines() {
        // removeItem(_cursor);
        for (TextLine tl : _linesList)
            getParent().addItem(tl);
        // addItem(_cursor);
    }

    int getLineLetCount(int lineNum) {
        if (lineNum >= _linesList.size())
            return 0;
        else {
            return _linesList.get(lineNum).getItemText().length();
        }
    }

    int checkLineWidth(int xpt, Point checkPoint) {
        int outPtX = xpt;
        int letCount = getLineLetCount(checkPoint.y);
        if (checkPoint.x > letCount)
            outPtX = letCount;
        return outPtX;
    }

    private int getLetPosInLine(int cPosY, int cPosX) {
        return _linesList.get(cPosY).getLetPosArray().get(cPosX);
    }

    private void addNewLine(String text, int lineNum) {
        // removeItem(_cursor);
        TextLine te = new TextLine();
        te.setForeground(getForeground());
        te.setTextAlignment(_blockAlignment);
        te.setMargin(getTextMargin());
        if (_elementFont != null)
            te.setFont(_elementFont);
        getParent().addItem(te);
        // text.TrimEnd('\r');
        te.setItemText(text);
        te.setRecountable(true);
        _linesList.add(lineNum, te);

        updLinesYShift(); // Пока обновляются все, но в принципе, нужно только под lineNum
        // addItem(_cursor);

        checkWidth();
    }

    void breakLine(Point _cursorPosition) {
        String newText;
        if (_cursorPosition.x >= getLineLetCount(_cursorPosition.y))
            newText = "";
        else {
            TextLine tl = _linesList.get(_cursorPosition.y);
            StringBuilder text = new StringBuilder(tl.getItemText());
            tl.setItemText(text.substring(0, _cursorPosition.x));
            newText = text.substring(_cursorPosition.x);
        }
        addNewLine(newText, _cursorPosition.y + 1);

        //checkWidth(); //Есть в addNewLine
    }

    void clear() {
        _linesList.get(0).setItemText("");
        removeLines(1, _linesList.size() - 1);

        checkWidth();
    }

    private int coordXToPos(int coordX, int lineNumb) {
        int pos = 0;

        List<Integer> lineLetPos = _linesList.get(lineNumb).getLetPosArray();
        if (lineLetPos == null)
            return pos;

        for (int i = 0; i < lineLetPos.size(); i++) {
            if (lineLetPos.get(i) + globalXShift <= coordX + 3)
                pos = i + 1;
            else
                break;
        }

        return pos;
    }

    Point cupsorPosToCoord(Point cPos) {
        Point coord = new Point(0, 0);
        coord.y = getLineY(cPos.y);

        int letCount = getLineLetCount(cPos.y);
        if (letCount == 0) {
            coord.x = 0;
            return coord;
        }
        if (cPos.x == 0) {
            coord.x = 0;
            return coord;
        } else {
//            if (!(cPos.x == 0 && cPos.y == 0)) {
                coord.x = getLetPosInLine(cPos.y, cPos.x - 1) + cursorWidth;
//            }
        }
        return coord;
    }

    Point replaceCursorAccordingCoord(Point realPos) {
        realPos.y -= getParent().getY() + getParent().getPadding().top + globalYShift + getTextMargin().top;
        int lineNumb = realPos.y / getLineY(1);
        if (lineNumb >= getCount())
            lineNumb = getCount() - 1;
        if (lineNumb < 0)
            lineNumb = 0;
        // return lineNumb;

        realPos.x -= getParent().getX() + getParent().getPadding().left + getTextMargin().left;

        Point outPt = new Point(0, 0);
        outPt.y = lineNumb;
        outPt.x = coordXToPos(realPos.x, lineNumb);
        return outPt;
    }

    void combineLines(int topLineY) {
        String text = _linesList.get(topLineY).getItemText();
        text += _linesList.get(topLineY + 1).getItemText();
        _linesList.get(topLineY).setItemText(text);

        removeLines(topLineY + 1, topLineY + 1);

        checkWidth();
    }

    private void removeLines(int fromLine, int toLine) {
        int inc = fromLine;
        while (inc <= toLine) {
            getParent().removeItem(_linesList.get(fromLine));
            _linesList.remove(fromLine);
            inc++;
        }

        updLinesYShift(); // Пока обновляются все, но в принципе, нужно только под fromLine
    }

    /*
     * private int getLineXShift(int n) { if (_linesList.size() > n) return
     * _linesList.get(n).getLineXShift(); else return 0; }
     */
    private void updLinesYShift() {
        int inc = 0;
        for (TextLine line : _linesList) {
            line.setLineYShift(getLineY(inc) + globalYShift);
            inc++;
        }

    }

    private void updLinesXShift() {
        for (TextLine line : _linesList) {
            line.setLineXShift(globalXShift);
        }
    }

    private void setAllowHeight() {
        for (TextLine line : _linesList) {
            line.setAllowHeight(_cursorYMax);
        }
    }

    private void setAllowWidth() {
        for (TextLine line : _linesList) {
            line.setAllowWidth(_cursorXMax);
        }
    }

    private int getLineY(int num) {
        return (_lineHeight + _lineSpacer) * num;
    }

    int getCount() {
        return _linesList.size();
    }

    void setTextMargin(Indents margin) {
        // _text_margin = margin;
        for (TextLine var : _linesList) {
            var.setMargin(margin);
        }
    }

    Indents getTextMargin() {
        return _linesList.get(0).getMargin();
    }

    private int[] getDims() {
        int[] output = _linesList.get(0).getFontDims();
        _minLineSpacer = output[0];
        _lineHeight = output[2];
        if (_lineSpacer < _minLineSpacer)
            _lineSpacer = _minLineSpacer;

        return output;
    }

    void setFont(Font font) {
        if (!font.equals(_elementFont)) {
            _elementFont = font;
            if (_elementFont == null)
                return;
            if (_linesList == null)
                return;
            for (TextLine te : _linesList)
                te.setFont(font);

            getDims();
        }
    }

    Font getFont() {
        return _elementFont;
    }

    void setTextInLine(String text, int lineY) {
        _linesList.get(lineY).setItemText(text);

        checkWidth();
    }

    String getTextInLine(int lineNum) {
        return _linesList.get(lineNum).getItemText();
    }

    int getTextHeight() {
        return getLineY(_linesList.size());
    }

    private String _wholeText = "";

    String getWholeText() {
        StringBuilder sb = new StringBuilder();
        if (_linesList == null)
            return "";
        if (_linesList.size() == 1) {
            sb.append(_linesList.get(0).getText());
        } else {
//            for (TextLine te : _linesList) {
//                sb.append(te.getText());
//                sb.append("\n");
//            }
//            sb.delete(sb.length() - 3, sb.length() - 1); // Remove(sb.Length - 3, 2);
            for (int i = 0; i < _linesList.size() - 1; i++) {
                sb.append(_linesList.get(i).getText());
                sb.append("\n");
            }
            sb.append(_linesList.get(_linesList.size() - 1).getText());
        }
        _wholeText = sb.toString();
        return _wholeText;
    }

    Point setText(String text, Point curPos) {
        if (text == null || text.equals(""))
            clear();
        else if (!text.equals(getWholeText())) {
            curPos = splitAndMakeLines(text, curPos);
        }
        return curPos;
    }

    void setLineSpacer(int lineSpacer) {
        if (lineSpacer < _minLineSpacer)
            lineSpacer = _minLineSpacer;

        if (lineSpacer != _lineSpacer) {
            _lineSpacer = lineSpacer;

            if (_linesList == null)
                return;

            updLinesYShift();
        }
    }

    int getLineSpacer() {
        return _lineSpacer;
    }

    private Point splitAndMakeLines(String text, Point curPos) {
        clear();

        _wholeText = text;

        String[] line = text.split("\n");
        int inc = 0;
        String s;

        s = line[0].replaceAll("\r", "");
        _linesList.get(0).setItemText(s);

        for (int i = 1; i < line.length; i++) {
            inc++;
            s = line[i].replaceAll("\r", "");
            addNewLine(s, inc);
        }

        curPos.y = line.length - 1;
        curPos.x = getLineLetCount(curPos.y);

        checkWidth();
        return curPos;
    }

    void cutText(Point fromReal, Point toReal) {
        if (fromReal.y == toReal.y) {
            StringBuilder sb = new StringBuilder(_linesList.get(toReal.y).getItemText());
            _linesList.get(toReal.y).setItemText(sb.delete(fromReal.x, toReal.x).toString());

        } else {
            removeLines(fromReal.y + 1, toReal.y - 1);
            StringBuilder sb = new StringBuilder(_linesList.get(fromReal.y).getItemText());
            _linesList.get(fromReal.y).setItemText(sb.substring(0, fromReal.x));
            sb = new StringBuilder(_linesList.get(fromReal.y + 1).getItemText());
            _linesList.get(fromReal.y + 1).setItemText(sb.substring(toReal.x));
            combineLines(fromReal.y);
        }

        checkWidth();
    }

    Point pasteText(String pasteStr, Point _cursor_position) {
        String textBeg = new StringBuilder(_linesList.get(_cursor_position.y).getItemText()).substring(0,
                _cursor_position.x);
        String textEnd = "";
        if (_cursor_position.x < getLineLetCount(_cursor_position.y))
            textEnd = new StringBuilder(_linesList.get(_cursor_position.y).getItemText()).substring(_cursor_position.x);

        String[] line = pasteStr.split("\n");
        for (int i = 0; i < line.length; i++) {
            line[i] = line[i].replaceAll("\r", ""); // .TrimEnd('\r');
        }

        if (line.length == 1) {
            _linesList.get(_cursor_position.y).setItemText(textBeg + line[0] + textEnd);
            _cursor_position.x += pasteStr.length();
        } else {
            _linesList.get(_cursor_position.y).setItemText(textBeg + line[0]);
            int ind = _cursor_position.y + 1;
            for (int i = 1; i < line.length - 1; i++) {
                addNewLine(line[i], ind);
                ind++;
            }

            addNewLine(line[line.length - 1] + textEnd, ind);

            _cursor_position.x = line[line.length - 1].length();
            _cursor_position.y += line.length - 1;
        }

        checkWidth();
        return _cursor_position;
    }

    Point addXYShifts(int xShift, int yShift, Point outPoint, boolean isx) {
        // Point outPoint = cursorPosToCoord(point);
        if (getParent() == null)
            return new Point(0, 0);
        int offset = _cursorXMax / 3;

        if (isx) {
            if (globalXShift + outPoint.x < 0) {
                globalXShift = -outPoint.x;
                globalXShift += offset;
                if (globalXShift > 0)
                    globalXShift = 0;
                updLinesXShift();
            }
            if (globalXShift + outPoint.x > _cursorXMax) {
                globalXShift = _cursorXMax - outPoint.x;
//                globalXShift -= offset;
//                if (globalXShift + outPoint.x < _cursorXMax) {
//                    globalXShift = _cursorXMax - outPoint.x;
//                    if (globalXShift > 0)
//                        globalXShift = 0;
//                }

                if (outPoint.x < getWidth())
                    globalXShift -= offset;

                updLinesXShift();
            }

            if (outPoint.y + globalYShift < 0) {
                globalYShift = -outPoint.y;
                updLinesYShift();

            }
            if (outPoint.y + _lineHeight + globalYShift > _cursorYMax) {
                globalYShift = _cursorYMax - outPoint.y - _lineHeight;
                updLinesYShift();
            }
        }

        outPoint.x += getParent().getX() + getParent().getPadding().left + globalXShift + getTextMargin().left;
        outPoint.y += getParent().getY() + getParent().getPadding().top + globalYShift + getTextMargin().top;

        // outPoint.x += getX() + getPadding().left + _linesList.get(0).getMargin().left + xShift;
        // outPoint.y += getY() + getPadding().top + _linesList.get(0).getMargin().top + yShift;

        return outPoint;
    }

    Color _foreground = Color.BLACK;

    void setForeground(Color foreground) {
        if (_linesList != null && !foreground.equals(getForeground())) {
            _foreground = foreground;
            for (TextLine te : _linesList)
                te.setForeground(foreground); // Вроде бы это больше не нужно
        }
    }

    public Color getForeground() {
        return _foreground;
    }

    public TextPrinter getLetTextures() {
        textInputLock.lock();
        try {
            float _screenScale = 1;
            WindowLayout wLayout = getHandler();
            if (wLayout == null || wLayout.getDpiScale() == null)
                _screenScale = 1;
            else {
                _screenScale = wLayout.getDpiScale()[0];
                if (_screenScale == 0) //!= 1)
                    _screenScale = 1;
                    // makeBigArr();
            }

            List<TextPrinter> tpLines = new LinkedList<>();
            int w = 0, h = 0, bigWidth = 0;
            int lineHeigh = (int)(getLineY(1) * _screenScale);
            int visibleHeight = 0;
            int startNumb = -1;
//            int endNumb = -1;
            int inc = -1;
            for (TextLine tl : _linesList) {
                inc++;

                TextPrinter tmp = tl.getLetTextures();
                tpLines.add(tmp);
                h += lineHeigh;// tmp.HeightTexture;
                w = (w > tl.getWidth()) ? w : tl.getWidth();
                if (tmp == null) {
//                    if (startNumb > -1 && endNumb == -1)
//                        endNumb = inc;
                    continue;
                }
//                if (_screenScale != 1) {
//                    int bw = 0;
//                    if (tmp != null)
                    int bw = tmp.widthTexture;
                    bigWidth = (bigWidth > bw) ? bigWidth : bw;
//                }

                //w = (w > tmp.widthTexture) ? w : tmp.widthTexture;
                visibleHeight += lineHeigh;
                if (startNumb == -1)
                    startNumb = inc;
            }
            //w += _cursorXMax / 3;
            setWidth(w);
            setHeight((int)((float)h / _screenScale));

//            if (_screenScale != 1) {
                // setWidth((int)(bigWidth * 1f / _screenScale));
                w = bigWidth;
//            }

            ByteBuffer bigByte = BufferUtils.createByteBuffer(visibleHeight * w * 4); //h
            int bigOff = 0;

            for (TextPrinter tptmp : tpLines) {
                if (tptmp == null) {
                    continue;
                }

                if (tptmp.texture == null) {
                    bigOff += lineHeigh * w * 4;
                    continue;
                }

                for (int p = 0; p < 4; p++) {
                    for (int j = 0; j < tptmp.heightTexture; j++) {
                        for (int i = 0; i < tptmp.widthTexture; i++) {
                            bigByte.put(bigOff + p + i * 4 + j * (w * 4),
                                    tptmp.texture.get(p + i * 4 + j * (tptmp.widthTexture * 4)));
                        }

                        for (int i = tptmp.widthTexture; i < w; i++) {
                            bigByte.put(bigOff + p + i * 4 + j * (w * 4), (byte) 0);
                        }
                    }

                    for (int j = tptmp.heightTexture; j < lineHeigh; j++) {
                        for (int i = 0; i < w; i++) {
                            bigByte.put(bigOff + p + i * 4 + j * (w * 4), (byte) 0);
                        }
                    }
                }
                bigOff += lineHeigh * w * 4;
            }
            TextPrinter tpout = new TextPrinter(bigByte);
            tpout.widthTexture = w;
            tpout.heightTexture = visibleHeight; //h;

            tpout.xTextureShift = getParent().getPadding().left + getTextMargin().left + getParent().getX() + cursorWidth;
            tpout.yTextureShift = getParent().getPadding().top + getTextMargin().top + getParent().getY();

//            if (tpLines.size() == 0 || tpLines.get(0) == null) {
//                tpout.xTextureShift = getParent().getPadding().left + getTextMargin().left + getParent().getX();
//                tpout.yTextureShift = getParent().getPadding().top + getTextMargin().top + getParent().getY();
//
//                tpout.xTextureShift += 0;//_linesList.get(0).getLineXShift();
//                tpout.yTextureShift += 0;//_linesList.get(0).getLineYShift();
//            } else {
//                tpout.xTextureShift = getParent().getPadding().left + getTextMargin().left + getParent().getX();//tpLines.get(0).xTextureShift;
//                tpout.yTextureShift = getParent().getPadding().top + getTextMargin().top + getParent().getY(); //tpLines.get(0).yTextureShift;
//            }

            if (startNumb > -1)
                tpout.yTextureShift += _linesList.get(startNumb).getLineYShift();

            return tpout;
        } finally {
            textInputLock.unlock();
        }
    }

    int getScrollStep() {
        return getLineY(1);
    }

    int getScrollYOffset() {
        return globalYShift;
    }

    void setScrollYOffset(int offset) {
        globalYShift = offset;
        updLinesYShift();
    }

    int getScrollXOffset() {
        return globalXShift;
    }

    void setScrollXOffset(int offset) {
        globalXShift = offset;
        updLinesXShift();
    }

    private void checkWidth() {
        int w = 0, h = 0;
        int lineHeigh = getLineY(1);

        for (TextLine tl : _linesList) {
//            TextPrinter tmp = tl.getLetTextures();
            h += lineHeigh;// tmp.HeightTexture;
            w = (w > tl.getWidth()) ? w : tl.getWidth();
//            if (tmp == null)
//                continue;
//            w = (w > tmp.widthTexture) ? w : tmp.widthTexture;
        }

        setWidth(w);
        setHeight(h);
    }
}