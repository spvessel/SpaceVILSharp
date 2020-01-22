package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.spvessel.spacevil.Common.DisplayService;
import com.spvessel.spacevil.Core.InterfaceTextContainer;
import com.spvessel.spacevil.Core.InterfaceTextImage;
import com.spvessel.spacevil.Core.InterfaceTextWrap;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;

final class TextureStorage extends Primitive implements InterfaceTextContainer {
    private static int count = 0;
    private List<TextLine> _linesList;
    private List<ItemAlignment> _blockAlignment = new LinkedList<>(
            Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
    private List<Integer> _lineBreakes;
    private Font _elementFont;
    private int _lineSpacer;
    private int _minLineSpacer;
    private int _lineHeight;

    private int globalXShift = 0;
    private int globalYShift = 0;
    private int _cursorXMax = SpaceVILConstants.sizeMaxValue;
    private int _cursorYMax = SpaceVILConstants.sizeMaxValue;

    Lock textInputLock = new ReentrantLock();

    TextureStorage() {
        super("TextureStorage_" + count);

        _linesList = new ArrayList<>();
        _lineBreakes = new ArrayList<>();
        TextLine te = new TextLine();
        te.setRecountable(true);
        if (getForeground() != null)
            te.setForeground(getForeground());
        te.setTextAlignment(_blockAlignment);
        if (_elementFont != null)
            te.setFont(_elementFont);
        _linesList.add(te);

        getDims();

        _isUpdateTextureNeed = true;
    }

    int scrollBlockUp(int cursorY) {
        int h = getTextHeight();
        int curCoord = cursorY;

        if (h < _cursorYMax && globalYShift >= 0) {
            return curCoord;
        }

        curCoord -= globalYShift;

        globalYShift += getLineY(1);
        if (globalYShift > 0) {
            globalYShift = 0;
        }

        updLinesYShift();
        return (curCoord + globalYShift);
    }

    int scrollBlockDown(int cursorY) {
        int h = getTextHeight();
        int curCoord = cursorY;

        if (h < _cursorYMax && h + globalYShift <= _cursorYMax) {
            return curCoord;
        }

        curCoord -= globalYShift;

        globalYShift -= getLineY(1);
        if (h + globalYShift < _cursorYMax) {
            globalYShift = _cursorYMax - h;
        }

        updLinesYShift();
        return (curCoord + globalYShift);
    }

    int getCursorHeight() {
        return (_lineHeight + _lineSpacer);
    }

    void updateBlockWidth(int curWidth) {
        cursorWidth = curWidth;
        Prototype parent = getParent();
        if (parent == null) {
            return;
        }

        Indents textMargin = getTextMargin();
        Indents textPadding = parent.getPadding();
        int cursorChanges = _cursorXMax;
        _cursorXMax = parent.getWidth() - cursorWidth * 2 - textPadding.left - textPadding.right - textMargin.left
                - textMargin.right;
        cursorChanges -= _cursorXMax;

        setAllowWidth((cursorChanges != 0)); // <- updLinesXShift();
    }

    void updateBlockHeight() {
        Prototype parent = getParent();
        if (parent == null) {
            return;
        }

        Indents textMargin = getTextMargin();
        Indents textPadding = parent.getPadding();
        int cursorChanges = _cursorYMax;
        _cursorYMax = parent.getHeight() - textPadding.top - textPadding.bottom - textMargin.top - textMargin.bottom;
        cursorChanges -= _cursorYMax;

        setAllowHeight((cursorChanges != 0)); // <- updLinesYShift();
    }

    private int cursorWidth = 0;

    void initLines(int curWidth) {
        Prototype parent = getParent();
        if (parent == null) {
            return;
        }

        cursorWidth = curWidth;
        Indents textMargin = getTextMargin();
        Indents textPadding = parent.getPadding();

        _cursorXMax = parent.getWidth() - cursorWidth - textPadding.left - textPadding.right - textMargin.left
                - textMargin.right;

        _cursorYMax = parent.getHeight() - textPadding.top - textPadding.bottom - textMargin.top - textMargin.bottom;

        addAllLines();
        setAllowWidth(true); // <- updLinesXShift();
        setAllowHeight(true); // <- updLinesYShift();
    }

    void setLineContainerAlignment(List<ItemAlignment> alignment) {
        for (TextLine tl : _linesList) {
            tl.setAlignment(alignment);
        }
    }

    private void addAllLines() {
        for (TextLine tl : _linesList) {
            tl.setParent(getParent());
        }
    }

    private TextLine getTextLine(int lineNum) {
        return _linesList.get(lineNum);
    }

    int getLettersCountInLine(int lineNum) {
        if (lineNum >= _linesList.size()) { //возможно это плохо или очень плохо
            return 0;
        } else {
            return getTextInLine(lineNum).length(); //_linesList.get(lineNum).getItemText().length();
        }
    }

    private int checkLineWidth(int xpt, Point checkPoint) {
        int outPtX = xpt;
        int letCount = getLettersCountInLine(checkPoint.y);
        if (checkPoint.x > letCount) {
            outPtX = letCount;
        }
        return outPtX;
    }

    private boolean checkIsWrap() {
        Prototype parent = getParent();
        if (parent != null) {
            return (((InterfaceTextWrap) parent).isWrapText());
        }
        return false;
    }

    private void addNewLine(String text, int lineNum) {
        addNewLine(text, lineNum, true);
    }

    private void addNewLine(String text, int lineNum, boolean isRealLine) {
        TextLine te = new TextLine();
        te.setForeground(getForeground());
        te.setTextAlignment(_blockAlignment);
        te.setMargin(getTextMargin());
        te.setAllowWidth(_cursorXMax);
        te.setAllowHeight(_cursorYMax);
        te.setLineYShift(getLineY(lineNum) + globalYShift);
        te.setLineXShift(globalXShift);

        if (_elementFont != null) {
            te.setFont(_elementFont);
        }

        te.setParent(getParent());

        // text.TrimEnd('\r');
        te.setItemText(text);
        te.setRecountable(true);
        _linesList.add(lineNum, te);

        if (checkIsWrap()) {
            int prevLineVal = 0;
            if (lineNum - 1 >= 0 && lineNum - 1 < _lineBreakes.size()) {
                prevLineVal = _lineBreakes.get(lineNum - 1);
            }

            if (isRealLine) {
                _lineBreakes.add(lineNum, prevLineVal + 1);
                for (int i = lineNum + 1; i < _lineBreakes.size(); i++) {
                    _lineBreakes.set(i, _lineBreakes.get(i) + 1);
                }
            } else {
                _lineBreakes.add(lineNum, prevLineVal);
            }

            wrapLine(lineNum);
        }

        updLinesYShift(); // Пока обновляются все, но в принципе, нужно только под lineNum

        checkWidth(); // <- _isUpdateTextureNeed = true;
    }

    //    void breakLine(Point _cursorPosition) {
    //        breakLine(_cursorPosition, true);
    //    }

    void breakLine(Point _cursorPosition) { //, boolean isRealBreak) {
        String newText = "";
        int lineNum = _cursorPosition.y + 1;
        addNewLine(newText, lineNum, true); //isRealBreak); // <- checkWidth(); //Есть в addNewLine

        //        if (_cursorPosition.x >= getLettersCountInLine(_cursorPosition.y)) {
        //            newText = "";
        //        } else {
        if (_cursorPosition.x < getLettersCountInLine(_cursorPosition.y)) {
            String text = getTextInLine(_cursorPosition.y);
            setTextInLine(text.substring(0, _cursorPosition.x), _cursorPosition);
            newText = text.substring(_cursorPosition.x);
        }
        //        int lineNum = _cursorPosition.y + 1;

        //        addNewLine(newText, lineNum, isRealBreak); // <- checkWidth(); //Есть в addNewLine
        String changedText = getTextInLine(lineNum);
        if (changedText != null && !changedText.equals("")) {
            newText += changedText;
        }
        setTextInLine(newText, new Point(0, lineNum));
    }

    void clear() {
        _lineBreakes = new ArrayList<>();
        _lineBreakes.add(0);
        setTextInLine("", new Point(0, 0)); //_linesList.get(0).setItemText("");
        removeLines(1, _linesList.size() - 1);

        checkWidth();
    }

    private int coordXToPos(int coordX, int lineNumb) {
        int pos = 0;

        List<Integer> lineLetPos = getTextLine(lineNumb).getLetPosArray(); //_linesList.get(lineNumb).getLetPosArray();
        if (lineLetPos == null) {
            return pos;
        }

        for (int i = 0; i < lineLetPos.size(); i++) {
            if (lineLetPos.get(i) + globalXShift <= coordX + 3) {
                pos = i + 1;
            } else {
                break;
            }
        }

        return pos;
    }

    Point checkLineFits(Point checkPoint) {
        Point outPt = new Point();
        outPt.y = checkPoint.y;
        // ??? check line count
        if (outPt.y >= getLinesCount()) {
            outPt.y = getLinesCount() - 1;
        }

        if (outPt.y == -1) {
            outPt.y = 0;
        }
        outPt.x = checkPoint.x;
        if (outPt.x == -1) {
            outPt.x = 0;
        }

        outPt.x = checkLineWidth(outPt.x, checkPoint);

        return outPt;
    }

    private Point cursorPosToCoord(Point cPos0) {
        Point cPos = checkLineFits(cPos0); //??? moved to the replaceCursor 
        Point coord = new Point(0, 0);
        coord.y = getLineY(cPos.y);

        int letCount = getLettersCountInLine(cPos.y);
        if (letCount == 0) {
            coord.x = 0;
            return coord;
        }
        if (cPos.x == 0) {
            coord.x = 0;
            return coord;
        } else {
            coord.x = getLetPosInLine(cPos.y, cPos.x - 1) + cursorWidth;
        }
        return coord;
    }

    private int getLetPosInLine(int cPosY, int cPosX) {
        return getTextLine(cPosY).getLetPosArray().get(cPosX); //_linesList.get(cPosY).getLetPosArray().get(cPosX);
    }

    private Point cursorPosToCoordAndGlobalShifts(Point cursorPos) {
        Point coord = cursorPosToCoord(cursorPos);
        return sumPoints(coord, new Point(globalXShift, globalYShift));
    }

    private Point sumPoints(Point cursorPos, Point adderPoint) {
        Point coord = new Point(cursorPos);
        coord.x += adderPoint.x;
        coord.y += adderPoint.y;
        return coord;
    }

    Point replaceCursorAccordingCoord(Point realPos) {
        Prototype parent = getParent();
        realPos.y -= parent.getY() + parent.getPadding().top + globalYShift + getTextMargin().top;
        int lineNumb = realPos.y / getLineY(1);
        if (lineNumb >= getLinesCount()) {
            lineNumb = getLinesCount() - 1;
        }
        if (lineNumb < 0) {
            lineNumb = 0;
        }

        realPos.x -= parent.getX() + parent.getPadding().left + getTextMargin().left;

        Point outPt = new Point(0, 0);
        outPt.y = lineNumb;
        outPt.x = coordXToPos(realPos.x, lineNumb);
        return outPt;
    }

    private void combineLines(Point combinePos) { //int topLineY) {
        if (combinePos.y >= getLinesCount() - 1) {
            return;
        }
        String text = getTextInLine(combinePos.y); //_linesList.get(topLineY).getItemText();
        text += getTextInLine(combinePos.y + 1); //_linesList.get(topLineY + 1).getItemText();

        if (checkIsWrap()) {
            int lineNum = combinePos.y + 1;
            int currentLineVal = _lineBreakes.get(lineNum);
            int prevLineVal = (lineNum > 0) ? _lineBreakes.get(lineNum - 1) : -1;
            int nextLineVal = (lineNum < _lineBreakes.size() - 1) ? _lineBreakes.get(lineNum + 1) : currentLineVal;

            if (prevLineVal != currentLineVal && currentLineVal == nextLineVal) {
                for (int i = lineNum; i < _lineBreakes.size(); i++) {
                    _lineBreakes.set(i, _lineBreakes.get(i) - 1);
                }
            }
        }

        removeLines(combinePos.y + 1, combinePos.y + 1);
        setTextInLine(text, combinePos); //_linesList.get(topLineY).setItemText(text);

        checkWidth();
    }

    void combineLinesOrRemoveLetter(Point combinePos, KeyCode keyCode) {
        if (!checkIsWrap()) {
            combineLines(combinePos);
            return;
        }

        //line is not last is checked before call
        int currentLineVal = _lineBreakes.get(combinePos.y);
        int nextLineVal = (combinePos.y < _lineBreakes.size() - 1) ? _lineBreakes.get(combinePos.y + 1)
                : currentLineVal + 1;

        //???
        String currentText = getTextInLine(combinePos.y);
        String nextText = getTextInLine(combinePos.y + 1);
        if (currentLineVal != nextLineVal || currentText.length() == 0 || nextText.length() == 0) {
            combineLines(combinePos);
            return;
        }

        if (keyCode == KeyCode.BACKSPACE) {
            combinePos.x--;
            setTextInLine(currentText.substring(0, currentText.length() - 1), combinePos); //new Point(combinePos));
        } else if (keyCode == KeyCode.DELETE) {
            combinePos.x = 0;
            combinePos.y++;
            setTextInLine(nextText.substring(1), combinePos); //new Point(combinePos));
        }
    }

    private void removeLines(int fromLine, int toLine) {
        int inc = fromLine;
        boolean isWrapped = checkIsWrap();
        while (inc <= toLine) {
            getTextLine(fromLine).setParent(null); //_linesList.get(fromLine).setParent(null);
            _linesList.remove(fromLine);

            if (isWrapped) {
                removeLineBreakes(fromLine);
            }

            inc++;
        }

        updLinesYShift(); // Пока обновляются все, но в принципе, нужно только под fromLine
    }

    private void removeLineBreakes(int lineNum) {
        if (lineNum >= _lineBreakes.size()) {
            return;
        }

        int lineVal = _lineBreakes.get(lineNum);
        int prevLineVal = (lineNum > 0) ? _lineBreakes.get(lineNum - 1) : -1;
        int nextLineVal = (lineNum < _lineBreakes.size() - 1) ? _lineBreakes.get(lineNum + 1) : lineVal;

        _lineBreakes.remove(lineNum);
        if (lineVal != prevLineVal && lineVal != nextLineVal) {
            for (int i = lineNum; i < _lineBreakes.size(); i++) {
                _lineBreakes.set(i, _lineBreakes.get(i) - 1);
            }
        }
    }

    private void updLinesYShift() {
        int inc = 0;
        for (TextLine line : _linesList) {
            line.setLineYShift(getLineY(inc) + globalYShift);
            inc++;
        }
        _isUpdateTextureNeed = true;
    }

    private void updLinesXShift() {
        for (TextLine line : _linesList) {
            line.setLineXShift(globalXShift);
        }
        _isUpdateTextureNeed = true;
    }

    private void setAllowHeight(boolean isCursorChanged) {
        if (isCursorChanged) {
            for (TextLine line : _linesList) {
                line.setAllowHeight(_cursorYMax);
            }
        }
        updLinesYShift();
    }

    private void setAllowWidth(boolean isCursorChanged) {
        if (isCursorChanged) {
            for (TextLine line : _linesList) {
                line.setAllowWidth(_cursorXMax);
            }
        }
        updLinesXShift();
    }

    private int getLineY(int num) {
        return (_lineHeight + _lineSpacer) * num;
    }

    private int getLineY(float num) {
        return (int) ((_lineHeight + _lineSpacer) * num);
    }

    int getLinesCount() {
        return _linesList.size();
    }

    void setTextMargin(Indents margin) {
        for (TextLine var : _linesList) {
            var.setMargin(margin);
        }
        updateLayout();
    }

    Indents getTextMargin() {
        return getTextLine(0).getMargin(); //_linesList.get(0).getMargin();
    }

    private void getDims() { //int[]
        Alphabet.FontDimensions fontDims = getTextLine(0).getFontDims(); //int[] output = getTextLine(0).getFontDims(); //_linesList.get(0).getFontDims();
        _minLineSpacer = fontDims.lineSpacer; //output[0];
        _lineHeight = fontDims.height; //output[2];
        //        if (_lineSpacer < _minLineSpacer)
        //            _lineSpacer = _minLineSpacer;
        setLineSpacer(_minLineSpacer);

//        return output;
    }

    void setFont(Font font) {
        if (font == null) {
            return;
        }
        if (!font.equals(_elementFont)) {
            _elementFont = font;

            if (_linesList == null) {
                return;
            }
            for (TextLine te : _linesList) {
                te.setFont(font);
            }

            getDims(); // <- setLineSpacer <- updLinesYShift
            updateLayout();
        }
    }

    Font getFont() {
        return _elementFont;
    }

    void setTextInLine(String text, Point cursorPos) { //int lineY) {
        getTextLine(cursorPos.y).setItemText(text); //_linesList.get(lineY).setItemText(text);
        if (checkIsWrap()) {
            wrapLine(cursorPos.y);
            Point newPos = findNewPosition(cursorPos);
            cursorPos.x = newPos.x;
            cursorPos.y = newPos.y;
        }
        checkWidth(); // <- _isUpdateTextureNeed = true;
    }

    String getTextInLine(int lineNum) {
        return getTextLine(lineNum).getItemText(); //_linesList.get(lineNum).getItemText();
    }

    int getTextHeight() {
        return getLineY(_linesList.size());
    }

    private String _wholeText = "";

    String getWholeText() {
        StringBuilder sb = new StringBuilder();
        if (_linesList == null) {
            return "";
        }
        if (_linesList.size() == 1) { //TODO Кажется, else покрывает if, проверить
            sb.append(getTextInLine(0)); //_linesList.get(0).getText());
        } else {
            if (checkIsWrap() && (_lineBreakes.size() == _linesList.size())) {
                makeTextAccordingToBreaks(sb);
            } else {
                makeUnwrapText(sb);
            }
        }
        _wholeText = sb.toString();
        return _wholeText;
    }

    void getSelectedText(StringBuilder sb, Point fromPt, Point toPt) {
        if (checkIsWrap()) {
            makeTextAccordingToBreaks(sb, fromPt, toPt);
        } else {
            makeUnwrapText(sb, fromPt, toPt);
        }
    }

    private void makeUnwrapText(StringBuilder sb) {
        makeUnwrapText(sb, new Point(0, 0),
                new Point(getLettersCountInLine(_linesList.size() - 1), _linesList.size() - 1));
    }

    private void makeUnwrapText(StringBuilder sb, Point fromPt, Point toPt) {
        if (fromPt.x >= getLettersCountInLine(fromPt.y)) {
            sb.append("\n");
        } else {
            String textFirst = getTextInLine(fromPt.y);
            sb.append(textFirst.substring(fromPt.x));
            sb.append("\n");
        }
        for (int i = fromPt.y + 1; i < toPt.y; i++) {
            sb.append(getTextInLine(i));
            sb.append("\n");
        }

        String textLast = getTextInLine(toPt.y).substring(0, toPt.x);
        sb.append(textLast);
    }

    private void makeTextAccordingToBreaks(StringBuilder sb) {
        makeTextAccordingToBreaks(sb, new Point(0, 0),
                new Point(getLettersCountInLine(_linesList.size() - 1), _linesList.size() - 1));
    }

    private void makeTextAccordingToBreaks(StringBuilder sb, Point fromPt, Point toPt) {
        int currentVal = _lineBreakes.get(fromPt.y);
        int nextVal = (fromPt.y < toPt.y) ? _lineBreakes.get(fromPt.y + 1) : currentVal;
        String textFirst = "";

        if (fromPt.x < getLettersCountInLine(fromPt.y)) {
            textFirst = getTextInLine(fromPt.y).substring(fromPt.x);
        }

        sb.append(textFirst); //_linesList.get(i).getText());
        if (currentVal != nextVal) {
            sb.append("\n");
        }
        currentVal = nextVal;

        for (int i = fromPt.y + 1; i < toPt.y; i++) { //0; i < _linesList.size() - 1; i++) {
            nextVal = _lineBreakes.get(i + 1);
            sb.append(getTextInLine(i)); //_linesList.get(i).getText());
            if (currentVal != nextVal) {
                sb.append("\n");
            }
            currentVal = nextVal;
        }
        String textLast = getTextInLine(toPt.y).substring(0, toPt.x);
        sb.append(textLast); //_linesList.get(_linesList.size() - 1).getText());
    }

    Point setText(String text) {
        Point curPos = new Point(0, 0);
        if (text == null || text.equals("")) {
            clear();
        } else { //if (!text.equals(getWholeText())) {
            curPos = splitAndMakeLines(text); // <- checkWidth <- _isUpdateTextureNeed = true;
        }
        return curPos;
    }

    void setLineSpacer(int lineSpacer) {
        if (lineSpacer < _minLineSpacer) {
            lineSpacer = _minLineSpacer;
        }

        if (lineSpacer != _lineSpacer) {
            _lineSpacer = lineSpacer;

            if (_linesList == null) {
                return;
            }

            updLinesYShift();
        }
    }

    int getLineSpacer() {
        return _lineSpacer;
    }

    private Point splitAndMakeLines(String text) {
        clear();

        _wholeText = text;

        String[] line = text.split("\n", -1);
        //        int inc = 0;
        String s;

        s = line[0].replaceAll("\r", "");
        setTextInLine(s, new Point(line[0].length(), 0)); //0); //_linesList.get(0).setItemText(s);

        for (int i = 1; i < line.length; i++) {
            s = line[i].replaceAll("\r", "");
            addNewLine(s, _linesList.size());
        }

        Point curPos = new Point(0, 0);
        curPos.y = _linesList.size() - 1; //line.length - 1;
        curPos.x = getLettersCountInLine(curPos.y);

        checkWidth();
        return curPos;
    }

    void cutText(Point fromReal, Point toReal) {
        if (fromReal.y == toReal.y) {
            StringBuilder sb = new StringBuilder(getTextInLine(toReal.y)); //_linesList.get(toReal.y).getItemText());
            //            _linesList.get(toReal.y).setItemText(sb.delete(fromReal.x, toReal.x).toString());
            setTextInLine(sb.delete(fromReal.x, toReal.x).toString(), toReal); //.y);

        } else {
            StringBuilder firstLinePartText = new StringBuilder(getTextInLine(fromReal.y).substring(0, fromReal.x));
            StringBuilder lastLinePartText = new StringBuilder(getTextInLine(toReal.y).substring(toReal.x));
            removeLines(fromReal.y + 1, toReal.y);
            setTextInLine(firstLinePartText.append(lastLinePartText).toString(), fromReal); //.y);

            //            removeLines(fromReal.y + 1, toReal.y - 1);
            //            StringBuilder sb = new StringBuilder(getTextInLine(fromReal.y)); //_linesList.get(fromReal.y).getItemText());
            ////            _linesList.get(fromReal.y).setItemText(sb.substring(0, fromReal.x));
            //            setTextInLine(sb.substring(0, fromReal.x), fromReal.y);
            //            sb = new StringBuilder(getTextInLine(fromReal.y + 1)); //_linesList.get(fromReal.y + 1).getItemText());
            ////            _linesList.get(fromReal.y + 1).setItemText(sb.substring(toReal.x));
            //            setTextInLine(sb.substring(toReal.x), fromReal.y + 1);
            //            combineLines(fromReal.y);
        }

        checkWidth();
    }

    Point pasteText(String pasteStr, Point _cursor_position) {
        String textInLine = getTextInLine(_cursor_position.y); //_linesList.get(_cursor_position.y).getItemText();
        String textBeg = textInLine.substring(0, _cursor_position.x); //new StringBuilder()
        String textEnd = "";
        if (_cursor_position.x < getLettersCountInLine(_cursor_position.y)) {
            textEnd = textInLine.substring(_cursor_position.x); //new StringBuilder()
        }

        String[] line = pasteStr.split("\n", -1);
        for (int i = 0; i < line.length; i++) {
            line[i] = line[i].replaceAll("\r", ""); // .TrimEnd('\r');
        }

        if (line.length == 1) {
            //            _linesList.get(_cursor_position.y).setItemText(textBeg + line[0] + textEnd);
            _cursor_position.x += pasteStr.length();
            setTextInLine(textBeg + line[0] + textEnd, _cursor_position); //_cursor_position.y);
        } else {
            ////            _linesList.get(_cursor_position.y).setItemText(textBeg + line[0]);
            //            setTextInLine(textBeg + line[0], _cursor_position.y);
            //            int ind = _cursor_position.y + 1;
            //            for (int i = 1; i < line.length - 1; i++) {
            //                addNewLine(line[i], ind);
            //                ind++;
            //            }
            //
            //            addNewLine(line[line.length - 1] + textEnd, ind);
            //
            //            _cursor_position.x = line[line.length - 1].length();
            //            _cursor_position.y += line.length - 1;

            //------------------------------------------------------------------------------
            breakLine(_cursor_position);
            int beforeSize = _linesList.size();
            setTextInLine(textBeg + line[0], new Point(0, _cursor_position.y)); //_cursor_position.y); //textBeg = getTextInLine(_cursor_position.y);
            int nextLineNumb = _cursor_position.y;
            int afterSize = _linesList.size();
            nextLineNumb += (afterSize - beforeSize) + 1;
            for (int i = 1; i < line.length - 1; i++) {
                beforeSize = afterSize;
                addNewLine(line[i], nextLineNumb);
                afterSize = _linesList.size();
                nextLineNumb += (afterSize - beforeSize);
            }

            _cursor_position = new Point(line[line.length - 1].length(), nextLineNumb);
            setTextInLine(line[line.length - 1] + textEnd, _cursor_position); //nextLineNumb);
            //            _cursor_position = findNewPosition(new Point(line[line.length - 1].length(), nextLineNumb));

            //            int lastLineLength = line[line.length - 1].length();
            //            int linesInc = nextLineNumb;
            //            while (lastLineLength > 0 && linesInc < _linesList.size()) {
            //                int lineLength = getTextInLine(linesInc).length();
            //                if (lineLength < lastLineLength) {
            //                    lastLineLength -= lineLength;
            //                    linesInc++;
            //                } else {
            //                    _cursor_position.x = lastLineLength; // - 1;
            //                    break;
            //                }
            //            }
            //
            //            _cursor_position.y = linesInc; //nextLineNumb + (afterSize - beforeSize); // - 1;
            //
        }

        checkWidth();
        return _cursor_position;
    }

    private Point findNewPosition(Point oldCoord) {
        int linesInc = oldCoord.y;
        while (oldCoord.x >= 0 && linesInc < _linesList.size()) {
            int lineLength = getTextInLine(linesInc).length();
            if (lineLength < oldCoord.x) {
                oldCoord.x -= lineLength;
                linesInc++;
            } else {
                return new Point(oldCoord.x, linesInc); //_cursor_position.x = lastLineLength; // - 1;
            }
        }

        return new Point(0, oldCoord.y);
    }

    Point addXYShifts(Point point) {
        Point outPoint = cursorPosToCoord(point);
        Prototype parent = getParent();
        if (parent == null)
            return new Point(0, 0);
        int offset = _cursorXMax / 3;

        if (globalXShift + outPoint.x < 0) {
            globalXShift = -outPoint.x;
            globalXShift += offset;
            if (globalXShift > 0) {
                globalXShift = 0;
            }
            updLinesXShift();
        }
        if (globalXShift + outPoint.x > _cursorXMax) {
            globalXShift = _cursorXMax - outPoint.x;

            if (outPoint.x < getWidth()) {
                globalXShift -= offset;
            }

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

        outPoint.x += globalXShift;
        outPoint.y += globalYShift;

        outPoint.x += parent.getX() + parent.getPadding().left + getTextMargin().left;
        outPoint.y += parent.getY() + parent.getPadding().top + getTextMargin().top;

        return outPoint;
    }

    private boolean checkPoints(Point point) {
        return (!(point.y >= _linesList.size()));
    }

    List<Point> selectedArrays(Point fromPt, Point toPt) {
        if (!checkPoints(fromPt)) {
            return null;
        }
        if (!checkPoints(toPt)) {
            return null;
        }

        int cursorHeight = getCursorHeight();
        List<Point> selectionRectangles = new LinkedList<>();
        Prototype parent = getParent();
        Point adderPt = new Point();
        adderPt.x = parent.getX() + parent.getPadding().left + getTextMargin().left;
        adderPt.y = parent.getY() + parent.getPadding().top + getTextMargin().top;

        Point tmp = new Point();
        Point xy1;
        Point xy2;
        int lsp = getLineSpacer();

        if (fromPt.y == toPt.y) {
            if (getTextLine(fromPt.y).getTexture() == null) { //_linesList.get(fromPt.y).getLetTextures() == null)
                return null;
            }

            xy1 = cursorPosToCoordAndGlobalShifts(fromPt);
            xy2 = cursorPosToCoordAndGlobalShifts(toPt);

            if (xy2.x < 0 || xy1.x > _cursorXMax) {
                return null;
            }

            if (xy1.x < 0) {
                xy1.x = 0;
            }

            if (xy2.x > _cursorXMax) {
                xy2.x = _cursorXMax;
            }

            xy1 = sumPoints(xy1, adderPt);
            xy2 = sumPoints(xy2, adderPt);
            selectionRectangles.add(new Point(xy1.x, xy1.y - cursorHeight - lsp / 2 + 1));
            selectionRectangles.add(new Point(xy2.x, xy2.y - lsp / 2 + 1));

            return selectionRectangles;
        }

        xy1 = cursorPosToCoordAndGlobalShifts(fromPt);
        tmp.x = getLettersCountInLine(fromPt.y);
        tmp.y = fromPt.y;
        xy2 = cursorPosToCoordAndGlobalShifts(tmp);

        if (getTextLine(fromPt.y).getTexture() != null) { //_linesList.get(fromPt.y).getLetTextures() != null) {
            if (xy2.x >= 0 && xy1.x <= _cursorXMax) {
                if (xy1.x < 0) {
                    xy1.x = 0;
                }

                if (xy2.x > _cursorXMax) {
                    xy2.x = _cursorXMax;
                }

                xy1 = sumPoints(xy1, adderPt);
                xy2 = sumPoints(xy2, adderPt);

                selectionRectangles.add(new Point(xy1.x, xy1.y - cursorHeight - lsp / 2 + 1));
                selectionRectangles.add(new Point(xy2.x, xy2.y - lsp / 2 + 1));
            }
        }

        tmp.x = 0;
        tmp.y = toPt.y;
        xy1 = cursorPosToCoordAndGlobalShifts(tmp);
        xy2 = cursorPosToCoordAndGlobalShifts(toPt);

        if (getTextLine(toPt.y).getTexture() != null) { //_linesList.get(toPt.y).getLetTextures() != null) {
            if (xy2.x >= 0 && xy1.x <= _cursorXMax) {
                if (xy1.x < 0) {
                    xy1.x = 0;
                }

                if (xy2.x > _cursorXMax) {
                    xy2.x = _cursorXMax;
                }

                xy1 = sumPoints(xy1, adderPt);
                xy2 = sumPoints(xy2, adderPt);
                selectionRectangles.add(new Point(xy1.x, xy1.y - cursorHeight - lsp / 2 + 1));
                selectionRectangles.add(new Point(xy2.x, xy2.y - lsp / 2 + 1));
            }
        }

        for (int i = fromPt.y + 1; i < toPt.y; i++) {
            tmp.x = 0;
            tmp.y = i;
            xy1 = cursorPosToCoordAndGlobalShifts(tmp);

            tmp.x = getLettersCountInLine(i);
            tmp.y = i;
            xy2 = cursorPosToCoordAndGlobalShifts(tmp);

            if (getTextLine(i).getTexture() != null) { //_linesList.get(i).getLetTextures() != null) {
                if (xy2.x >= 0 && xy1.x <= _cursorXMax) {
                    if (xy1.x < 0) {
                        xy1.x = 0;
                    }

                    if (xy2.x > _cursorXMax) {
                        xy2.x = _cursorXMax;
                    }

                    xy1 = sumPoints(xy1, adderPt);
                    xy2 = sumPoints(xy2, adderPt);
                    selectionRectangles.add(new Point(xy1.x, xy1.y - cursorHeight - lsp / 2 + 1));
                    selectionRectangles.add(new Point(xy2.x, xy2.y - lsp / 2 + 1));
                }
            }
        }

        return selectionRectangles;
    }

    private Color _foreground = Color.BLACK;

    void setForeground(Color foreground) {
        if (foreground == null || _linesList == null) {
            return;
        }
        if (!foreground.equals(getForeground())) {
            _foreground = foreground;
            for (TextLine te : _linesList) {
                te.setForeground(foreground); // Вроде бы это больше не нужно
            }
        }
    }

    public Color getForeground() {
        return _foreground;
    }

    private boolean _isUpdateTextureNeed = true;
    private TextPrinter _blockTexture = null;
    private int _firstVisibleLineNumb = -1;

    public InterfaceTextImage getTexture() {
        textInputLock.lock();
        try {
            Prototype parent = getParent();
            if (parent == null) {
                return null;
            }

            if (_isUpdateTextureNeed) {
                float _screenScale = 1;
                CoreWindow wLayout = getHandler();
                if (wLayout == null) { // || wLayout.getDpiScale() == null) {
                    _screenScale = 1;
                } else {
                    _screenScale = DisplayService.getDisplayDpiScale().getX();// wLayout.getDpiScale().getX();
                    if (_screenScale == 0) { //!= 1)
                        _screenScale = 1;
                    }
                    // makeBigArr();
                }

                List<InterfaceTextImage> tpLines = new LinkedList<>();
                int w = 0, h = 0, bigWidth = 0;
                int lineHeigh = (int) (getLineY(1) * _screenScale);
                int visibleHeight = 0;
                _firstVisibleLineNumb = -1; // int startNumb = -1;
                int inc = -1;
                
                for (TextLine tl : _linesList) {
                    inc++;
                    InterfaceTextImage tmp = tl.getTexture();
                    tpLines.add(tmp);
                    h += lineHeigh;// tmp.HeightTexture;
                    w = (w > tl.getWidth()) ? w : tl.getWidth();
                    if (tmp == null) {
                        continue;
                    }
                    //                if (_screenScale != 1) {
                    //                    int bw = 0;
                    //                    if (tmp != null)
                    int bw = tmp.getWidth();
                    bigWidth = (bigWidth > bw) ? bigWidth : bw;
                    //                }

                    //w = (w > tmp.widthTexture) ? w : tmp.widthTexture;
                    visibleHeight += lineHeigh;
                    if (_firstVisibleLineNumb == -1) { //startNumb == -1) {
                        _firstVisibleLineNumb = inc; //startNumb = inc;
                        //                    _firstVisibleLineNumb = startNumb;
                    }
                }
                //w += _cursorXMax / 3;
                setWidth(w);
                setHeight((int) ((float) h / _screenScale));

                //            if (_screenScale != 1) {
                // setWidth((int)(bigWidth * 1f / _screenScale));
                w = bigWidth;
                //            }

                byte[] bigByte = new byte[visibleHeight * w * 4];
                int bigOff = 0;

                for (InterfaceTextImage tptmp : tpLines) {
                    if (tptmp == null) {
                        continue;
                    }

                    if (tptmp.getBytes() == null) {
                        bigOff += lineHeigh * w * 4;
                        continue;
                    }

                    for (int p = 0; p < 4; p++) {
                        for (int j = 0; j < tptmp.getHeight(); j++) {
                            for (int i = 0; i < tptmp.getWidth(); i++) {
                                // bigByte.put(bigOff + p + i * 4 + j * (w * 4),
                                //         tptmp.texture.get(p + i * 4 + j * (tptmp.widthTexture * 4)));
                                bigByte[bigOff + p + i * 4 + j * (w * 4)] = tptmp.getBytes()[p + i * 4
                                        + j * (tptmp.getWidth() * 4)];
                            }

                            for (int i = tptmp.getWidth(); i < w; i++) {
                                // bigByte.put(bigOff + p + i * 4 + j * (w * 4), (byte) 0);
                                bigByte[bigOff + p + i * 4 + j * (w * 4)] = 0;
                            }
                        }

                        for (int j = tptmp.getHeight(); j < lineHeigh; j++) {
                            for (int i = 0; i < w; i++) {
                                // bigByte.put(bigOff + p + i * 4 + j * (w * 4), (byte) 0);
                                bigByte[bigOff + p + i * 4 + j * (w * 4)] = 0;
                            }
                        }
                    }
                    bigOff += lineHeigh * w * 4;
                }
                _blockTexture = new TextPrinter(bigByte); //TextPrinter tpout = new TextPrinter(bigByte);
                _blockTexture.setSize(w, visibleHeight);
                // _blockTexture.widthTexture = w; //tpout.widthTexture = w;
                // _blockTexture.heightTexture = visibleHeight; //h; //tpout.heightTexture = visibleHeight; //h;
                //            tpout.xTextureShift = parent.getPadding().left + getTextMargin().left + parent.getX() + cursorWidth;
                //            tpout.yTextureShift = parent.getPadding().top + getTextMargin().top + parent.getY();
                //
                ////            if (tpLines.size() == 0 || tpLines.get(0) == null) {
                ////                tpout.xTextureShift = parent.getPadding().left + getTextMargin().left + parent.getX();
                ////                tpout.yTextureShift = parent.getPadding().top + getTextMargin().top + parent.getY();
                ////
                ////                tpout.xTextureShift += 0;//_linesList.get(0).getLineXShift();
                ////                tpout.yTextureShift += 0;//_linesList.get(0).getLineYShift();
                ////            } else {
                ////                tpout.xTextureShift = parent.getPadding().left + getTextMargin().left + parent.getX();//tpLines.get(0).xTextureShift;
                ////                tpout.yTextureShift = parent.getPadding().top + getTextMargin().top + parent.getY(); //tpLines.get(0).yTextureShift;
                ////            }
                //
                //            if (startNumb > -1)
                //                tpout.yTextureShift += _linesList.get(startNumb).getLineYShift();

                _isUpdateTextureNeed = false;
                ItemsRefreshManager.setRefreshText(this);
                // _blockTexture = tpout;

            }
            updateCoords(parent);
            return _blockTexture; //tpout;
        } finally {
            textInputLock.unlock();
        }
    }

    private void updateCoords(Prototype parent) {
        _blockTexture.setPosition(parent.getPadding().left + getTextMargin().left + parent.getX() + cursorWidth,
                parent.getPadding().top + getTextMargin().top + parent.getY());
        // _blockTexture.xTextureShift = parent.getPadding().left + getTextMargin().left + parent.getX() + cursorWidth;
        // _blockTexture.yTextureShift = parent.getPadding().top + getTextMargin().top + parent.getY();

        if (_firstVisibleLineNumb > -1) {
            _blockTexture.setYOffset(_blockTexture.getYOffset() + getTextLine(_firstVisibleLineNumb).getLineYShift());
            // _blockTexture.yTextureShift += getTextLine(_firstVisibleLineNumb).getLineYShift(); //_linesList.get(_firstVisibleLineNumb).getLineYShift();
        }
    }

    private float _stepFactor = 1.0f;

    void setScrollStepFactor(float value) {
        _stepFactor = value;
    }

    int getScrollStep() {
        // return getLineY(1);
        return getLineY(_stepFactor);
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
            h += lineHeigh;
            w = (w > tl.getWidth()) ? w : tl.getWidth();
        }

        setWidth(w);
        setHeight(h);
        _isUpdateTextureNeed = true;
    }

    private Pattern patternWordBounds = Pattern.compile("\\W|_", Pattern.UNICODE_CHARACTER_CLASS);

    int[] findWordBounds(Point cursorPosition) {
        //С положение курсора должно быть все в порядке, не нужно проверять вроде бы
        String lineText = getTextInLine(cursorPosition.y);
        int index = cursorPosition.x;

        String testString = lineText.substring(index);
        Matcher matcher = patternWordBounds.matcher(testString);

        int begPt = 0;
        int endPt = getLettersCountInLine(cursorPosition.y);

        if (matcher.find()) {
            endPt = index + matcher.start();
        }

        testString = lineText.substring(0, index);
        matcher = patternWordBounds.matcher(testString);

        while (matcher.find()) {
            begPt = matcher.start() + 1;
        }

        return new int[] { begPt, endPt };
    }

    //Wrap Text Stuff---------------------------------------------------------------------------------------------------

    private void wrapLine(int lineNum) {
        TextLine textLine = getTextLine(lineNum); //_linesList.get(lineNum);

        if (textLine.getWidth() == _cursorXMax) {
            return;
        }

        boolean isLinesChanged;

        if (lineNum > 0) {
            isLinesChanged = hasThisAndNextLineCombination(lineNum - 1);
            if (isLinesChanged) {
                return;
            }
        }

        if (lineNum < _lineBreakes.size() - 1) {
            isLinesChanged = hasThisAndNextLineCombination(lineNum);
            if (isLinesChanged) {
                return;
            }
        }


//        if (lineNum > 0) { //check if I can move something to the previous line
//            int prevLineVal = _lineBreakes.get(lineNum - 1);
//            if (lineVal == prevLineVal) {
//                TextLine prevTextLine = getTextLine(lineNum - 1);
//                String prevText = prevTextLine.getText();
//
//                if (prevTextLine.getWidth() < _cursorXMax) {
//                    if (prevText.endsWith(" ")) {
//                        int firstSpaceInd = textInLine.indexOf(" ");
//                        if (firstSpaceInd > 0 && letPosArr.size() != 0) {
//                            if (prevTextLine.getWidth() + letPosArr.get(firstSpaceInd - 1) < _cursorXMax) {
//                                combineLines(new Point(prevText.length(), lineNum - 1));
//                                return;
//                            }
//                        }
//                    } else if (!textInLine.startsWith(" ")) {
//
//                    }
//                }
//
//
//                if (textInLine.startsWith(" ") && !prevText.endsWith(" ")) {
//                    //move space to previous line directly, without checking
//
//                }
//            }
//        }
//
//        if (textLine.getWidth() < _cursorXMax) { // add something from the next line
//            if (lineVal == nextLineVal) {
//                TextLine nextTextLine = getTextLine(lineNum + 1);
//                int nextLet = nextTextLine.getLetPosArray().get(0); //check needed
//                if (textLine.getWidth() + nextLet < _cursorXMax) {
//                    combineLines(new Point(textInLine.length(), lineNum));
//                }
//            }
//            return;
//        }

        if (textLine.getWidth() < _cursorXMax) {
            return;
        }

        int lineVal = _lineBreakes.get(lineNum);
        int nextLineVal = (lineNum < _lineBreakes.size() - 1) ? _lineBreakes.get(lineNum + 1) : lineVal + 1;
        String textInLine = textLine.getText();

        List<Integer> letPosArr = textLine.getLetPosArray();

        if (letPosArr.size() == 0) {
            return;
        }

        List<Integer> listSpace = new ArrayList<>();
        List<Integer> listPos = new ArrayList<>();

        int ind = 0;
        int pos = textInLine.indexOf(" ", ind);

        while (pos >= 0) {
            listSpace.add(letPosArr.get(pos));
            listPos.add(pos);
            ind = pos + 1;
            pos = textInLine.indexOf(" ", ind);
        }

        Point breakPos;
        int splitPos = 0;
        if (listSpace.size() == 0) { //one long word
            splitPos = letPosArr.size() - 1; //letPosArr.get(letPosArr.size() - 1);
        } else {
            splitPos = binarySearch(0, listSpace.size() - 1, listSpace, _cursorXMax);

            splitPos = listPos.get(splitPos) + 1; //After space
            if (splitPos >= letPosArr.size()) {
                splitPos = letPosArr.size() - 1;
            }
        }

        if (letPosArr.get(splitPos) > _cursorXMax) {
            //one long word
            splitPos = binarySearch(0, letPosArr.size() - 1, letPosArr, _cursorXMax);
        }

        if (splitPos == letPosArr.size() - 1 || splitPos == 0) {
            return; //or it will be splitted with an empty line
        }

        breakPos = new Point(splitPos, lineNum);

        if (lineVal == nextLineVal) {
            String text = getTextInLine(breakPos.y);
            StringBuilder newText = new StringBuilder(text.substring(breakPos.x));
            newText.append(getTextInLine(breakPos.y + 1));
            textLine.setItemText(text.substring(0, breakPos.x)); // setTextInLine(text.substring(0, breakPos.x), new Point(breakPos.x, breakPos.y));
            setTextInLine(newText.toString(), new Point(newText.length(), breakPos.y + 1));
        } else {
            //            breakLine(breakPos, false);
            String newText = "";
            if (breakPos.x < letPosArr.size()) {
                String text = getTextInLine(breakPos.y);
                textLine.setItemText(text.substring(0, breakPos.x)); //setTextInLine(text.substring(0, breakPos.x), breakPos);
                newText = text.substring(breakPos.x);
            }

            addNewLine(newText, breakPos.y + 1, false);
        }
    }

    private boolean hasThisAndNextLineCombination(int lineNum) {
        //checking before call
        int currentLineVal = _lineBreakes.get(lineNum);
        int nextLineVal = _lineBreakes.get(lineNum + 1);
        if (currentLineVal == nextLineVal) {
            TextLine currentLine = getTextLine(lineNum );
            String currentText = currentLine.getText();
            TextLine nextLine = getTextLine(lineNum + 1);
            String nextText = nextLine.getText();

            if (currentLine.getWidth() < _cursorXMax) {
                List<Integer> nextLineLetPosArray = nextLine.getLetPosArray();

                if (nextLineLetPosArray.size() != 0) {
                    if (currentText.endsWith(" ")) {
                        int firstSpaceInd = nextText.indexOf(" ");
                        if (firstSpaceInd > 0 && firstSpaceInd + 1 < nextLineLetPosArray.size()) {
                            if (currentLine.getWidth() + nextLineLetPosArray.get(firstSpaceInd + 1) < _cursorXMax) {
                                combineLines(new Point(currentText.length(), lineNum));
                                return true;
                            }
                        }
                    } else if (!nextText.startsWith(" ")) {
                        if (nextLineLetPosArray.size() >= 3) {
                            if (currentLine.getWidth() + nextLineLetPosArray.get(2) < _cursorXMax) {
                                combineLines(new Point(currentText.length(), lineNum));
                                return true;
                            }
                        }
                    }
                }
            }

            //Пока плохо работает из-за положения курсора
//            if (nextText.startsWith(" ") && !currentText.endsWith(" ")) {
//                //move space to previous line directly, without checking
//                currentLine.setItemText(currentText + " ");
//                nextLine.setItemText(nextText.substring(1));
//            }
        }
        return false;
    }

    private int binarySearch(int fromInd, int toInd, List<Integer> searchingList, int testValue) {
        while (toInd > fromInd) {
            int midInd = (toInd + fromInd) / 2;

            if (searchingList.get(midInd) == testValue) {
                return midInd;
            }

            if (searchingList.get(midInd) > testValue) {
                toInd = midInd - 1;
            } else {
                fromInd = midInd + 1;
            }
        }

        if (searchingList.get(fromInd) > testValue && fromInd > 0) {
            fromInd--;
        }

        return fromInd;
    }

    void rewrapText() {
        setText(getWholeText());
    }

    private void updateLayout() {
        if (!checkIsWrap()) {
            return;
        }
        textInputLock.lock();
        try {
            rewrapText();
        } finally {
            textInputLock.unlock();
        }
    }

    Point wrapCursorPosToReal(Point wrapPos) {
        //Convert wrap cursor position to the real position
        if (_lineBreakes.size() != _linesList.size()) {
            return wrapPos;
        }
        Point realPos = new Point(0, 0);
        int lineNum = findLineBegInBreakLines(wrapPos.y);
        int lineRealLength = wrapPos.x;
        for (int i = lineNum; i < wrapPos.y; i++) {
            lineRealLength += getTextInLine(i).length();
        }

        realPos.x = lineRealLength;
        realPos.y = _lineBreakes.get(wrapPos.y);

        return realPos;
    }

    Point realCursorPosToWrap(Point realPos) {
        //Convert real cursor position to the wrap position
        if (_lineBreakes.size() != _linesList.size()) {
            return realPos;
        }
        Point wrapPos = new Point(0, 0);

        int lineBeg = _lineBreakes.get(realPos.y);
        if (lineBeg != realPos.y) { //which means less
            lineBeg = binarySearch(lineBeg, _lineBreakes.size() - 1, _lineBreakes, realPos.y);
        }
        lineBeg = findLineBegInBreakLines(lineBeg);

        int lineRealLength = realPos.x;
        int linesCount = getLinesCount();
        while (lineBeg < linesCount - 1) {
            int len = getTextInLine(lineBeg).length();
            if (len >= lineRealLength) {
                break;
            }
            lineRealLength -= len;
            lineBeg++;
        }

        wrapPos.x = lineRealLength;
        wrapPos.y = lineBeg;

        return wrapPos;
    }

    private int findLineBegInBreakLines(int wrapLineNum) {
        int lineBeg = wrapLineNum;
        int lineVal = _lineBreakes.get(lineBeg);
        if (lineVal == 0) {
            return 0;
        }

        while (lineBeg > 0 && _lineBreakes.get(lineBeg - 1) == lineVal) {
            lineBeg--;
            lineVal = _lineBreakes.get(lineBeg);
        }

        return lineBeg;
    }
}