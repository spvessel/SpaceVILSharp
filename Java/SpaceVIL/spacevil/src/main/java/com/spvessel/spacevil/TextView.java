package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TextView extends Prototype implements InterfaceDraggable, InterfaceTextShortcuts, InterfaceTextWrap {

    private static int count = 0;
    private Point _cursorPosition = new Point(0, 0);
    private CustomSelector _selectedArea;

    private TextureStorage _textureStorage;

    private Point _selectFrom = new Point(-1, 0);
    private Point _selectTo = new Point(-1, 0);
    private boolean _isSelect = false;

    public TextView() {
        setItemName("TextView_" + count);
        count++;

        _textureStorage = new TextureStorage();

        _selectedArea = new CustomSelector();

        eventMousePress.add(this::onMousePressed);
        eventMouseDrag.add(this::onDragging);
        eventKeyPress.add(this::onKeyPress);
        eventKeyRelease.add(this::onKeyRelease);
        eventMouseDoubleClick.add(this::onDoubleClick);

        setStyle(DefaultsService.getDefaultStyle(TextView.class));
    }

    private long _startTime = 0;
    private boolean _isDoubleClick = false;

    private void onDoubleClick(Object sender, MouseArgs args) {
        _textureStorage.textInputLock.lock();
        try {
            if (args.button == MouseButton.BUTTON_LEFT) {
                replaceCursorAccordingCoord(new Point(args.position.getX(), args.position.getY()));
                if (_isSelect) {
                    unselectText();
                }
                int[] wordBounds = _textureStorage.findWordBounds(_cursorPosition);

                if (wordBounds[0] != wordBounds[1]) {
                    _isSelect = true;
                    _selectFrom = new Point(wordBounds[0], _cursorPosition.y);
                    _selectTo = new Point(wordBounds[1], _cursorPosition.y);
                    _cursorPosition = new Point(_selectTo);
                    makeSelectedArea();
                }

                _startTime = System.nanoTime();
                _isDoubleClick = true;
            } else {
                _isDoubleClick = false;
            }
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    private void onMousePressed(Object sender, MouseArgs args) {
        _textureStorage.textInputLock.lock();
        try {
            if (args.button == MouseButton.BUTTON_LEFT) {
                replaceCursorAccordingCoord(new Point(args.position.getX(), args.position.getY()));
                if (_isSelect) {
                    unselectText();
                }

                if (_isDoubleClick && (System.nanoTime() - _startTime) / 1000000 < 500) { //Select line on triple click
                    _isSelect = true;
                    _selectFrom = new Point(0, _cursorPosition.y);
                    _selectTo = new Point(getLettersCountInLine(_cursorPosition.y), _cursorPosition.y);
                    _cursorPosition = new Point(_selectTo);
                    makeSelectedArea();
                }
            }
            _isDoubleClick = false;
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    private void onDragging(Object sender, MouseArgs args) {
        _textureStorage.textInputLock.lock();
        try {
            if (args.button == MouseButton.BUTTON_LEFT) {
                replaceCursorAccordingCoord(new Point(args.position.getX(), args.position.getY()));
                if (!_isSelect) {
                    _isSelect = true;
                    _selectFrom = new Point(_cursorPosition);
                } else {
                    _selectTo = new Point(_cursorPosition);
                    makeSelectedArea();
                }
            }
            _isDoubleClick = false;
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    private void replaceCursorAccordingCoord(Point realPos) {
        _cursorPosition = _textureStorage.replaceCursorAccordingCoord(realPos);
    }

    private void onKeyRelease(Object sender, KeyArgs args) {
    }

    private void onKeyPress(Object sender, KeyArgs args) {
        TextShortcutProcessor.processShortcut(this, args); //ctrl + c & ctrl + a processor only
        if (args.key == KeyCode.ESCAPE && _isSelect) {
            unselectText();
        }
    }

    public String getText() {
        return _textureStorage.getWholeText();
    }

    public void setText(String text) {
        _textureStorage.textInputLock.lock();
        try {
            _cursorPosition = _textureStorage.setText(text);
            changeHeightAccordingToText();
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    int getTextWidth() {
        return _textureStorage.getWidth();
    }

    int getTextHeight() {
        return _textureStorage.getTextHeight();
    }

    @Override
    public void initElements() {
        addItems(_selectedArea, _textureStorage);
        _textureStorage.initLines(2); //_cursor.getWidth());
        if (isWrapText()) {
            reorganizeText();
        }
    }

    private int getLettersCountInLine(int lineNum) {
        return _textureStorage.getLettersCountInLine(lineNum);
    }

    private void makeSelectedArea() {
        makeSelectedArea(_selectFrom, _selectTo);
    }

    private void makeSelectedArea(Point from, Point to) {
        if (from.x == to.x && from.y == to.y) {
            _selectedArea.setRectangles(null);
            return;
        }

        List<Point> selectionRectangles;

        Point fromReal, toReal;
        List<Point> listPt = realFromTo(from, to);
        fromReal = listPt.get(0);
        toReal = listPt.get(1);

        selectionRectangles = _textureStorage.selectedArrays(fromReal, toReal);

        _selectedArea.setRectangles(selectionRectangles);
    }

    private List<Point> realFromTo(Point from, Point to) {
        List<Point> ans = new LinkedList<>();
        Point fromReal, toReal;
        if (from.y == to.y) {
            if (from.x < to.x) {
                fromReal = from;
                toReal = to;
            } else {
                fromReal = to;
                toReal = from;
            }
        } else {
            if (from.y < to.y) {
                fromReal = from;
                toReal = to;
            } else {
                fromReal = to;
                toReal = from;
            }
        }

        ans.add(fromReal);
        ans.add(toReal);
        return ans;
    }

    private String privGetSelectedText() {
        _textureStorage.textInputLock.lock();
        try {
            if (_selectFrom.x == -1 || _selectTo.x == -1) {
                return "";
            }
            _selectFrom = _textureStorage.checkLineFits(_selectFrom);
            _selectTo = _textureStorage.checkLineFits(_selectTo);
            if (_selectFrom.x == _selectTo.x && _selectFrom.y == _selectTo.y) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            List<Point> listPt = realFromTo(_selectFrom, _selectTo);
            Point fromReal = listPt.get(0);
            Point toReal = listPt.get(1);

            StringBuilder stmp;
            if (fromReal.y == toReal.y) {
                stmp = new StringBuilder(_textureStorage.getTextInLine(fromReal.y));
                sb.append(stmp.substring(fromReal.x, toReal.x)); // - fromReal.x
                return sb.toString();
            }

            _textureStorage.getSelectedText(sb, fromReal, toReal);

            return sb.toString();
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    public String getSelectedText() {
        return privGetSelectedText();
    }

    private void unselectText() {
        _isSelect = false;
        _selectFrom.x = -1;
        _selectFrom.y = 0;
        _selectTo.x = -1;
        _selectTo.y = 0;
        makeSelectedArea(new Point(_cursorPosition), new Point(_cursorPosition));
    }

    public final void selectAll() {
        _textureStorage.textInputLock.lock();
        try {
            _selectFrom.x = 0;
            _selectFrom.y = 0;
            _cursorPosition.y = _textureStorage.getLinesCount() - 1;
            _cursorPosition.x = getLettersCountInLine(_cursorPosition.y);
            _selectTo = new Point(_cursorPosition);
            _isSelect = true;
            makeSelectedArea();
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    @Override
    public void setWidth(int width) {
        if (getWidth() == width) {
            return;
        }

        Point tmpCursor = new Point(_cursorPosition);
        Point fromTmp = new Point(_selectFrom);
        Point toTmp = new Point(_selectTo);

        tmpCursor = _textureStorage.wrapCursorPosToReal(_cursorPosition);
        if (_isSelect) {
            fromTmp = _textureStorage.wrapCursorPosToReal(_selectFrom);
            toTmp = _textureStorage.wrapCursorPosToReal(_selectTo);
        }

        super.setWidth(width);
        _textureStorage.updateBlockWidth(2); //_cursor.getWidth());
        reorganizeText();

        _cursorPosition = _textureStorage.realCursorPosToWrap(tmpCursor);
        if (_isSelect) {
            fromTmp = _textureStorage.realCursorPosToWrap(fromTmp);
            toTmp = _textureStorage.realCursorPosToWrap(toTmp);
            _selectFrom = fromTmp;
            _selectTo = toTmp;
            makeSelectedArea();
        }

        changeHeightAccordingToText();
    }

    @Override
    public void setHeight(int height) {
        if (getHeight() == height) {
            return;
        }
        super.setHeight(height);
        _textureStorage.updateBlockHeight();
    }

    private void changeHeightAccordingToText() {
        if (getHeightPolicy() == SizePolicy.EXPAND)
            return;
        int textHeight = getTextHeight();
        setHeight(textHeight);
    }

    //Wrap Text Stuff---------------------------------------------------------------------------------------------------

    public boolean isWrapText() {
        return true;
    }

    //Something changed (text is always wrapped)
    private void reorganizeText() {
        _textureStorage.textInputLock.lock();
        try {
            _textureStorage.rewrapText();
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    //Decorations-------------------------------------------------------------------------------------------------------

    public void setLineSpacer(int lineSpacer) {
        _textureStorage.setLineSpacer(lineSpacer);
    }

    public int getLineSpacer() {
        return _textureStorage.getLineSpacer();
    }

    public void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(Arrays.asList(alignment));
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        // Ignore all changes for yet
    }

    public void setTextMargin(Indents margin) {
        _textureStorage.setTextMargin(margin);
    }

    public Indents getTextMargin() {
        return _textureStorage.getTextMargin();
    }

    public void setFont(Font font) {
        _textureStorage.setFont(font);
    }

    public Font getFont() {
        return _textureStorage.getFont();
    }

    public void setForeground(Color color) {
        _textureStorage.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        setForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    public void setForeground(int r, int g, int b, int a) {
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    public void setForeground(float r, float g, float b) {
        setForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    public void setForeground(float r, float g, float b, float a) {
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    public Color getForeground() {
        return _textureStorage.getForeground();
    }

    //Style
    @Override
    public void setStyle(Style style) {
        if (style == null) {
            return;
        }
        super.setStyle(style);
        setForeground(style.foreground);
        setFont(style.font);
        _textureStorage.setLineContainerAlignment(style.textAlignment);

        Style inner_style = style.getInnerStyle("selection");
        if (inner_style != null) {
            _selectedArea.setStyle(inner_style);
        }
    }

    //Shortcut methods disable------------------------------------------------------------------------------------------
    @Override
    public void pasteText(String pasteStr) {

    }

    @Override
    public String cutText() {
        return null;
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}
