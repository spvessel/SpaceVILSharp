package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * TextView is designed to display non-editable text with the ability to select
 * and copy. TextView wraps contained text in the current width.
 * <p>
 * Supports all events including drag and drop.
 */
public class TextView extends Prototype implements InterfaceDraggable, InterfaceTextShortcuts, InterfaceTextWrap {

    private static int count = 0;
    private Point _cursorPosition = new Point(0, 0);
    private CustomSelector _selectedArea;
    private TextureStorage _textureStorage;
    private Point _selectFrom = new Point(-1, 0);
    private Point _selectTo = new Point(-1, 0);
    private boolean _isSelect = false;

    /**
     * Default TextView constructor.
     */
    public TextView() {
        setItemName("TextView_" + count);
        count++;

        _textureStorage = new TextureStorage();

        _selectedArea = new CustomSelector();

        eventMousePress.add(this::onMousePressed);
        eventMouseClick.add(this::onMouseClick);
        eventMouseDrag.add(this::onDragging);
        eventKeyPress.add(this::onKeyPress);
        eventKeyRelease.add(this::onKeyRelease);

        setStyle(DefaultsService.getDefaultStyle(TextView.class));
    }

    private long _startTime = 0;
    private boolean _isDoubleClick = false;
    private Point _previousClickPos = new Point();

    private void onMousePressed(Object sender, MouseArgs args) {
        // Set cursor and unselect only - common actions for click/double click
        _textureStorage.textInputLock.lock();
        try {
            if (args.button == MouseButton.BUTTON_LEFT) {
                replaceCursorAccordingCoord(new Point(args.position.getX(), args.position.getY()));
                if (_isSelect) {
                    unselectText();
                }
            }

        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    private void onMouseClick(Object sender, MouseArgs args) {
        _textureStorage.textInputLock.lock();
        try {
            if (args.button == MouseButton.BUTTON_LEFT) {
                // replaceCursorAccordingCoord(new Point(args.position.getX(),
                // args.position.getY()));
                // if (_isSelect) {
                // unselectText();
                // }
                //
                Point savePos = new Point(_cursorPosition);
                if (isPosSame()) {
                    if ((System.nanoTime() - _startTime) / 1000000 < 500) {

                        if (_isDoubleClick) { // && (System.nanoTime() - _startTime) / 1000000 < 500) { //Select line on
                                              // triple click
                            _isSelect = true;
                            _selectFrom = new Point(0, _cursorPosition.y);
                            _selectTo = new Point(getLettersCountInLine(_cursorPosition.y), _cursorPosition.y);
                            _cursorPosition = new Point(_selectTo);
                            makeSelectedArea();

                            _isDoubleClick = false;
                        } else { // if double click
                            int[] wordBounds = _textureStorage.findWordBounds(_cursorPosition);

                            if (wordBounds[0] != wordBounds[1]) {
                                _isSelect = true;
                                _selectFrom = new Point(wordBounds[0], _cursorPosition.y);
                                _selectTo = new Point(wordBounds[1], _cursorPosition.y);
                                _cursorPosition = new Point(_selectTo);
                                makeSelectedArea();
                            }

                            // _startTime = System.nanoTime();
                            _isDoubleClick = true;
                        }

                    } else {
                        _isDoubleClick = false;
                    }

                } else {
                    _isDoubleClick = false;
                    // _startTime = System.nanoTime();
                }

                _previousClickPos = savePos;
                _startTime = System.nanoTime();
            } else {
                _isDoubleClick = false;
            }

        } finally {
            _textureStorage.textInputLock.unlock();
        }

    }

    // private void onDoubleClick(Object sender, MouseArgs args) {
    // _textureStorage.textInputLock.lock();
    // try {
    // if (args.button == MouseButton.BUTTON_LEFT) {
    //// replaceCursorAccordingCoord(new Point(args.position.getX(),
    // args.position.getY()));
    //// if (_isSelect) {
    //// unselectText();
    //// }
    ////
    // Point savePos = new Point(_cursorPosition);
    // if (isPosSame()) {
    // int[] wordBounds = _textureStorage.findWordBounds(_cursorPosition);
    //
    // if (wordBounds[0] != wordBounds[1]) {
    // _isSelect = true;
    // _selectFrom = new Point(wordBounds[0], _cursorPosition.y);
    // _selectTo = new Point(wordBounds[1], _cursorPosition.y);
    // _cursorPosition = new Point(_selectTo);
    // makeSelectedArea();
    // }
    //
    // _startTime = System.nanoTime();
    // _isDoubleClick = true;
    // }
    // _previousClickPos = savePos;
    //
    // } else {
    // _isDoubleClick = false;
    // }
    // } finally {
    // _textureStorage.textInputLock.unlock();
    // }
    // }

    private boolean isPosSame() {
        Point pos1 = new Point(_cursorPosition);
        Point pos2 = new Point(_previousClickPos);
        int tol = 5;
        if (pos1.y != pos2.y) {
            return false;
        }
        return (pos1.x - tol <= pos2.x && pos2.x <= pos1.x + tol);
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
        TextShortcutProcessor.processShortcut(this, args); // ctrl + c & ctrl + a processor only
        if (args.key == KeyCode.ESCAPE && _isSelect) {
            unselectText();
        }
    }

    /**
     * Getting the current text of the TextView.
     * 
     * @return Text as java.lang.String.
     */
    public String getText() {
        return _textureStorage.getWholeText();
    }

    /**
     * Setting the text.
     * 
     * @param text Text as java.lang.String.
     */
    public final void setText(String text) {
        _textureStorage.textInputLock.lock();
        try {
            _cursorPosition = _textureStorage.setText(text);
            changeHeightAccordingToText();
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    /**
     * Setting the text.
     * 
     * @param text Text as java.lang.Object.
     */
    public void setText(Object text) {
        setText(text.toString());
    }

    int getTextWidth() {
        return _textureStorage.getWidth();
    }

    int getTextHeight() {
        return _textureStorage.getTextHeight();
    }

    /**
     * Initializing all elements in the TextView.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        addItems(_selectedArea, _textureStorage);
        _textureStorage.initLines(2); // _cursor.getWidth());
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

    /**
     * Getting the current selected text.
     * 
     * @return Current selected text.
     */
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

    /**
     * Selecting entire text of the TextView.
     */
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

    /**
     * Setting item width. If the value is greater/less than the maximum/minimum
     * value of the width, then the width becomes equal to the maximum/minimum
     * value.
     * 
     * @param width Width of the item.
     */
    @Override
    public void setWidth(int width) {
        if (getWidth() == width) {
            return;
        }

        updateBlockWidth(width);
    }

    private void updateBlockWidth(int width) {
        Point tmpCursor; // = new Point(_cursorPosition);
        Point fromTmp = new Point(_selectFrom);
        Point toTmp = new Point(_selectTo);

        tmpCursor = _textureStorage.wrapCursorPosToReal(_cursorPosition);
        if (_isSelect) {
            fromTmp = _textureStorage.wrapCursorPosToReal(_selectFrom);
            toTmp = _textureStorage.wrapCursorPosToReal(_selectTo);
        }

        super.setWidth(width);
        _textureStorage.updateBlockWidth(2); // _cursor.getWidth());
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

    /**
     * Setting item height. If the value is greater/less than the maximum/minimum
     * value of the height, then the height becomes equal to the maximum/minimum
     * value.
     * 
     * @param height Height of the item.
     */
    @Override
    public void setHeight(int height) {
        if (getHeight() == height) {
            return;
        }
        updateBlockHeight(height);
    }

    private void updateBlockHeight(int height) {
        super.setHeight(height);
        _textureStorage.updateBlockHeight();
    }

    private void changeHeightAccordingToText() {
        if (getHeightPolicy() == SizePolicy.EXPAND)
            return;
        int textHeight = getTextHeight();
        setHeight(textHeight);
    }

    // Wrap Text
    // Stuff---------------------------------------------------------------------------------------------------

    /**
     * Always returns True. TextView always wraps contained text.
     * <p>
     * com.spvessel.spacevil.Core.InterfaceTextWrap implementation.
     */
    public boolean isWrapText() {
        return true;
    }

    // Something changed (text is always wrapped)
    private void reorganizeText() {
        _textureStorage.textInputLock.lock();
        try {
            _textureStorage.rewrapText();
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    // Decorations-------------------------------------------------------------------------------------------------------

    /**
     * Setting indent between lines in TextView.
     * 
     * @param lineSpacer Indent between lines.
     */
    public void setLineSpacer(int lineSpacer) {
        _textureStorage.setLineSpacer(lineSpacer);
    }

    /**
     * Getting current indent between lines in TextView.
     * 
     * @return Indent between lines.
     */
    public int getLineSpacer() {
        return _textureStorage.getLineSpacer();
    }

    void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(BaseItemStatics.composeFlags(alignment)); //Arrays.asList(alignment));
    }

    void setTextAlignment(List<ItemAlignment> alignment) {
        // Ignore all changes for yet
    }

    /**
     * Setting indents for the text to offset text relative to this TextView.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _textureStorage.setTextMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to TextView.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setTextMargin(int left, int top, int right, int bottom) {
        _textureStorage.setTextMargin(new Indents(left, top, right, bottom));
    }

    /**
     * Getting indents of the text.
     * 
     * @return Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getTextMargin() {
        return _textureStorage.getTextMargin();
    }

    /**
     * Setting font of the text.
     * 
     * @param font Font as java.awt.Font.
     */
    public void setFont(Font font) {
        _textureStorage.setFont(font);
    }

    /**
     * Setting font size of the text.
     *
     * @param size New size of the font.
     */
    public void setFontSize(int size) {
        Font oldFont = getFont();
        if (oldFont.getSize() != size) {
            Font newFont = FontService.changeFontSize(size, oldFont);
            setFont(newFont);
        }
    }

    /**
     * Setting font style of the text.
     *
     * @param style New font style (from java.awt.Font package).
     */
    public void setFontStyle(int style) {
        Font oldFont = getFont();
        if (oldFont.getStyle() != style) {
            Font newFont = FontService.changeFontStyle(style, oldFont);
            setFont(newFont);
        }
    }

    /**
     * Setting new font family of the text.
     *
     * @param fontFamily New font family name.
     */
    public void setFontFamily(String fontFamily) {
        if (fontFamily == null)
            return;
        Font oldFont = getFont();
        if (!oldFont.getFamily().equals(fontFamily)) {
            Font newFont = FontService.changeFontFamily(fontFamily, oldFont);
            setFont(newFont);
        }
    }

    /**
     * Getting the current font of the text.
     * 
     * @return Font as java.awt.Font.
     */
    public Font getFont() {
        return _textureStorage.getFont();
    }

    /**
     * Setting text color of a TextView.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _textureStorage.setForeground(color);
    }

    /**
     * Setting text color of a TextView in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        setForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting background color of an item in byte RGBA format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b, int a) {
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Setting text color of a TextView in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        setForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting text color of a TextView in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Getting current text color.
     * 
     * @return Text color as as java.awt.Color.
     */
    public Color getForeground() {
        return _textureStorage.getForeground();
    }

    /**
     * Setting style of the TextView.
     * <p>
     * Inner styles: "selection".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
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

    // Shortcut methods
    // disable------------------------------------------------------------------------------------------

    /**
     * Do nothing. com.spvessel.spacevil.Core.InterfaceTextShortcuts implementation.
     */
    @Override
    public void pasteText(String pasteStr) {

    }

    /**
     * Do nothing. com.spvessel.spacevil.Core.InterfaceTextShortcuts implementation.
     */
    @Override
    public String cutText() {
        return "";
    }

    /**
     * Do nothing. com.spvessel.spacevil.Core.InterfaceTextShortcuts implementation.
     */
    @Override
    public void undo() {

    }

    /**
     * Do nothing. com.spvessel.spacevil.Core.InterfaceTextShortcuts implementation.
     */
    @Override
    public void redo() {

    }
}
