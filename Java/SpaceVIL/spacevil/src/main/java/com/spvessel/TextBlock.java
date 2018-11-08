package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.*;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.KeyCode;
import com.spvessel.Flags.KeyMods;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TextBlock extends VisualItem
        implements InterfaceTextEditable, InterfaceDraggable, InterfaceTextShortcuts {

    public EventCommonMethod textChanged = new EventCommonMethod();
    private static int count = 0;
    private Rectangle _cursor;
    private Point _cursor_position = new Point(0, 0);
    private CustomSelector _selectedArea;

    private TextureStorage _textureStorage;

    private boolean _isEditable = true;

    private Point _selectFrom = new Point(-1, 0);
    private Point _selectTo = new Point(-1, 0);
    private boolean _isSelect = false;
    private boolean _justSelected = false;

    private Color _blockForeground;

    private List<KeyCode> ShiftValCodes;
    private List<KeyCode> InsteadKeyMods;

    private int scrollXStep = 15;

    public TextBlock() {
        setItemName("TextBlock_" + count);
        count++;

        _textureStorage = new TextureStorage();

        _cursor = new Rectangle();
        _selectedArea = new CustomSelector();

        setStyle(DefaultsService.getDefaultStyle(TextBlock.class));

        eventMousePressed.add(this::onMousePressed);
        eventMouseDrag.add(this::onDragging);
        eventKeyPress.add(this::onKeyPress);
        eventKeyRelease.add(this::onKeyRelease);
        eventTextInput.add(this::onTextInput);
        eventScrollUp.add(this::onScrollUp);
        eventScrollDown.add(this::onScrollDown);

        ShiftValCodes = new LinkedList<>(
                Arrays.asList(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.END, KeyCode.HOME, KeyCode.UP, KeyCode.DOWN));
        InsteadKeyMods = new LinkedList<>(Arrays.asList(KeyCode.LEFTSHIFT, KeyCode.RIGHTSHIFT, KeyCode.LEFTCONTROL,
                KeyCode.RIGHTCONTROL, KeyCode.LEFTALT, KeyCode.RIGHTALT, KeyCode.LEFTSUPER, KeyCode.RIGHTSUPER));

        // int[] output = te.getFontDims();
        // _minLineSpacer = output[0];
        // //_minFontY = output[1];
        // //_maxFontY = output[2];
        // _lineHeight = output[2]; //Math.abs(_maxFontY - _minFontY + 1);
        // if (_lineSpacer < _minLineSpacer)
        // _lineSpacer = _minLineSpacer;

        _cursor.setHeight(_textureStorage.getCursorHeight());
    }

    protected void onMousePressed(Object sender, MouseArgs args) {
        replaceCursorAccordingCoord(new Point(args.position.getX(), args.position.getY()));
        if (_isSelect)
            unselectText();
    }

    protected void onDragging(Object sender, MouseArgs args) {
        replaceCursorAccordingCoord(new Point(args.position.getX(), args.position.getY()));
        if (!_isSelect) {
            _isSelect = true;
            _selectFrom = new Point(_cursor_position);
        } else {
            _selectTo = new Point(_cursor_position);
            makeSelectedArea(_selectFrom, _selectTo);
        }
    }

    protected int getScrollYStep() {
        return _textureStorage.getScrollStep();
    }

    protected int getScrollXStep() {
        return scrollXStep;
    }

    protected int getScrollYOffset() {
        return _textureStorage.getScrollYOffset();
    }

    protected void setScrollYOffset(int offset) {
        int oldOff = _textureStorage.getScrollYOffset();
        _textureStorage.setScrollYOffset(offset);
        int diff = offset - oldOff;
        _selectedArea.shiftAreaY(diff);
        _cursor.setY(_cursor.getY() + diff);
    }

    protected int getScrollXOffset() {
        return _textureStorage.getScrollXOffset();
    }

    protected void setScrollXOffset(int offset) {
        int oldOff = _textureStorage.getScrollXOffset();
        _textureStorage.setScrollXOffset(offset);
        int diff = offset - oldOff;
        _selectedArea.shiftAreaX(diff);
        _cursor.setX(_cursor.getX() + diff);
    }

    protected void onScrollUp(Object sender, MouseArgs args) {
        int curPos = _cursor.getY();
        _cursor.setY(_textureStorage.scrollBlockUp(_cursor.getY()));
        curPos = _cursor.getY() - curPos;
        _selectedArea.shiftAreaY(curPos);
        // replaceCursor();
    }

    protected void onScrollDown(Object sender, MouseArgs args) {
        int curPos = _cursor.getY();
        _cursor.setY(_textureStorage.scrollBlockDown(_cursor.getY()));
        curPos = _cursor.getY() - curPos;
        _selectedArea.shiftAreaY(curPos);
        // replaceCursor();
    }

    // public void invokeScrollUp(MouseArgs args) {
    // eventScrollUp.execute(this, args);
    // }

    // public void invokeScrollDown(MouseArgs args) {
    // eventScrollDown.execute(this, args);
    // }

    private void replaceCursorAccordingCoord(Point realPos) {
        // Вроде бы и без этого норм, но пусть пока будет
        // 
        //
        //
        // if (_linesList != null)
        // realPos.y -= (int)_linesList[0].getLineTopCoord(); //???????!!!!!!

        // Console.WriteLine(_lineSpacer);
        /*
         * int lineNumb = _textureStorage.findLineNumb(realPos.y);
         * 
         * realPos.x -= getX() + getPadding().left + _textureStorage.textMargin().left;
         * 
         * _cursor_position.y = lineNumb; _cursor_position.x = coordXToPos(realPos.x,
         * lineNumb);
         */
        _cursor_position = _textureStorage.replaceCursorAccordingCoord(realPos);
        replaceCursor();
    }

    /*
     * private int coordXToPos(int coordX, int lineNumb) { return
     * _textureStorage.coordXToPos(coordX, lineNumb); }
     */
    protected void onKeyRelease(Object sender, KeyArgs args) {
        // if (args.key == KeyCode.v/* 0x2F*/ && args.mods == KeyMods.CONTROL)
        // pasteText(CommonService.ClipboardTextStorage);
    }

    protected void onKeyPress(Object sender, KeyArgs args) {
        _textureStorage.textInputLock.lock();
        try {
            if (!_isEditable) {
                if (args.mods.equals(KeyMods.CONTROL) && (args.key == KeyCode.A || args.key == KeyCode.a)) {
                    _selectFrom.x = 0;
                    _selectFrom.y = 0;
                    _cursor_position.y = _textureStorage.getCount() - 1;
                    _cursor_position.x = getLineLetCount(_cursor_position.y);
                    _selectTo = new Point(_cursor_position);
                    replaceCursor();
                    _isSelect = true;
                    makeSelectedArea(_selectFrom, _selectTo);
                }
                return;
            }

            if (!_isSelect && _justSelected) {
                _selectFrom.x = -1;// 0;
                _selectFrom.y = 0;
                _selectTo.x = -1;// 0;
                _selectTo.y = 0;
                _justSelected = false;
            }

            if (args.mods != KeyMods.NO) {
                // Выделение не сбрасывается, проверяются сочетания
                // 
                //
                //
                switch (args.mods) {
                case SHIFT:
                    if (ShiftValCodes.contains(args.key)) {
                        // System.out.println(args.mods + " " + args.key + " " + _isSelect);
                        if (!_isSelect) {
                            _isSelect = true;
                            _selectFrom = new Point(_cursor_position);
                        }
                    }

                    break;

                case CONTROL:
                    if (args.key == KeyCode.A || args.key == KeyCode.a) {
                        _selectFrom.x = 0;
                        _selectFrom.y = 0;
                        _cursor_position.y = _textureStorage.getCount() - 1;
                        _cursor_position.x = getLineLetCount(_cursor_position.y);
                        replaceCursor();

                        _isSelect = true;
                    }
                    break;

                // alt, super ?
                }
            } else {
                if (args.key == KeyCode.BACKSPACE || args.key == KeyCode.DELETE || args.key == KeyCode.ENTER) {
                    if (_isSelect)
                        privCutText();
                    else {
                        _cursor_position = checkLineFits(_cursor_position);
                        if (args.key == KeyCode.BACKSPACE) // backspace
                        {
                            if (_cursor_position.x > 0) {
                                StringBuilder sb = new StringBuilder(_textureStorage.getTextInLine(_cursor_position.y));
                                setTextInLine(sb.deleteCharAt(_cursor_position.x - 1).toString());
                                _cursor_position.x--;
                            } else if (_cursor_position.y > 0) {
                                _cursor_position.y--;
                                _cursor_position.x = getLineLetCount(_cursor_position.y);
                                _textureStorage.combineLines(_cursor_position.y);
                            }
                            replaceCursor();
                        }
                        if (args.key == KeyCode.DELETE) // delete
                        {
                            if (_cursor_position.x < getLineLetCount(_cursor_position.y)) {
                                StringBuilder sb = new StringBuilder(_textureStorage.getTextInLine(_cursor_position.y));
                                setTextInLine(sb.deleteCharAt(_cursor_position.x).toString());
                            } else if (_cursor_position.y < _textureStorage.getCount() - 1) {
                                _textureStorage.combineLines(_cursor_position.y);
                            }
                        }
                    }

                } else if (_isSelect && !InsteadKeyMods.contains(args.key)) {
                    unselectText();
                    // _justSelected = true;
                }
            }

            if (args.key == KeyCode.LEFT) // arrow left
            {
                _cursor_position = checkLineFits(_cursor_position);
                if (!_justSelected) {
                    if (_cursor_position.x > 0)
                        _cursor_position.x--;
                    else if (_cursor_position.y > 0) {
                        _cursor_position.y--;
                        _cursor_position.x = getLineLetCount(_cursor_position.y);
                    }
                    replaceCursor();
                }
            }
            if (args.key == KeyCode.RIGHT) // arrow right
            {
                if (!_justSelected) {
                    if (_cursor_position.x < getLineLetCount(_cursor_position.y))
                        _cursor_position.x++;
                    else if (_cursor_position.y < _textureStorage.getCount() - 1) {
                        _cursor_position.y++;
                        _cursor_position.x = 0;
                    }
                    replaceCursor();
                }

            }
            if (args.key == KeyCode.UP) // arrow up
            {
                if (!_justSelected) {
                    if (_cursor_position.y > 0)
                        _cursor_position.y--;
                    // ?????
                    replaceCursor();
                }

            }
            if (args.key == KeyCode.DOWN) // arrow down
            {
                if (!_justSelected) {
                    if (_cursor_position.y < _textureStorage.getCount() - 1)
                        _cursor_position.y++;
                    // ?????
                    replaceCursor();
                }

            }

            if (args.key == KeyCode.END) // end
            {
                _cursor_position.x = getLineLetCount(_cursor_position.y);
                replaceCursor();
            }
            if (args.key == KeyCode.HOME) // home
            {
                _cursor_position.x = 0;
                replaceCursor();
            }

            if (args.key == KeyCode.ENTER || args.key == KeyCode.NUMPADENTER) // enter
            {
                _textureStorage.breakLine(_cursor_position);
                _cursor_position.y++;
                _cursor_position.x = 0;

                replaceCursor();
                //textChanged.execute();
            }

            if (_isSelect) {
                if (!_selectTo.equals(_cursor_position)) {
                    _selectTo = new Point(_cursor_position);
                    makeSelectedArea(_selectFrom, _selectTo);
                }
            }
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    public void onTextInput(Object sender, TextInputArgs args) {
        if (!_isEditable)
            return;
        _textureStorage.textInputLock.lock();
        try {
            // byte[] input = BitConverter.getBytes(args.Character);
            // String str = Encoding.UTF32.getString(input);
            byte[] input = ByteBuffer.allocate(4).putInt(args.character).array();
            String str = new String(input, Charset.forName("UTF-32")); // StandardCharsets.UTF_8);

            if (_isSelect) {
                unselectText();
                privCutText();
            }
            if (_justSelected) _justSelected = false;
                
            _cursor_position = checkLineFits(_cursor_position);

            StringBuilder sb = new StringBuilder(_textureStorage.getTextInLine(_cursor_position.y));
            setTextInLine(sb.insert(_cursor_position.x, str).toString());
            _cursor_position.x++;
            replaceCursor();
            //textChanged.execute();
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    private Point checkLineFits(Point checkPoint) {
        Point outPt = new Point();
        // ??? check line count
        outPt.y = checkPoint.y;
        if (outPt.y == -1)
            outPt.y = 0;
        outPt.x = checkPoint.x;
        if (outPt.x == -1)
            outPt.x = 0;

        outPt.x = _textureStorage.checkLineWidth(outPt.x, checkPoint);

        return outPt;
    }

    private Point cursorPosToCoord(Point cPos0) {
        Point cPos = checkLineFits(cPos0);
        return _textureStorage.cupsorPosToCoord(cPos);
    }

    private void replaceCursor() {
        Point pos = addXYShifts(0, 0, _cursor_position);
        _cursor.setX(pos.x);
        _cursor.setY(pos.y - getLineSpacer() / 2 + 1);// - 3);
        textChanged.execute();
    }

    void setLineSpacer(int lineSpacer) {
        _textureStorage.setLineSpacer(lineSpacer);
        _cursor.setHeight(_textureStorage.getCursorHeight()); // + 6);
    }

    public int getLineSpacer() {
        return _textureStorage.getLineSpacer();
    }

    String getText() {
        return _textureStorage.getWholeText();
    }

    public void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(Arrays.asList(alignment));
        // Ignore all changes
        /*
         * _blockAlignment = alignment; if (_linesList == null) return; foreach
         * (TextLine te in _linesList) te.setTextAlignment(alignment);
         */
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        // Ignore all changes
    }

    public void setTextMargin(Indents margin) {
        _textureStorage.setTextMargin(margin);
    }

    public void setFont(Font font) {
        /*
         * if (!font.equals(_elementFont)) { _elementFont = font; if (_elementFont ==
         * null) return; int[] output = FontEngine.getSpacerDims(font); _minLineSpacer =
         * output[0]; //_minFontY = output[1]; //_maxFontY = output[2]; _lineHeight =
         * output[2]; //Math.abs(_maxFontY - _minFontY); if (_lineSpacer <
         * _minLineSpacer) _lineSpacer = _minLineSpacer;
         * 
         * if (_linesList == null) return; for (TextLine te : _linesList)
         * te.setFont(font);
         * 
         * _cursor.setHeight(_lineHeight + _lineSpacer); // + 6); }
         */
        _textureStorage.setFont(font);
        _cursor.setHeight(_textureStorage.getCursorHeight());
    }

    public Font getFont() {
        return _textureStorage.getFont();
    }

    public void setText(String text) {
        _textureStorage.textInputLock.lock();
        try {
            _cursor_position = _textureStorage.setText(text, _cursor_position);
            replaceCursor();
            //textChanged.execute();
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    private void setTextInLine(String text) {
        // _linesList.get(_cursor_position.y).setItemText(text);
        _textureStorage.setTextInLine(text, _cursor_position.y);
    }

    public int getTextWidth() {
        /*
         * int w = 0, w0; if (_linesList == null) return w; for (TextLine te :
         * _linesList) { w0 = te.getWidth(); w = (w < w0) ? w0 : w; } return w;
         */
        return _textureStorage.getWidth();
    }

    public int getTextHeight() {
        return _textureStorage.getTextHeight();
    }

    /*
     * protected void UpdateData(UpdateType updateType) { //foreach (TextEdit te in
     * _linesList) //te.Up }
     */
    public void setForeground(Color color) {
        _textureStorage.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        setForeground(new Color(r, g, b, 255));
    }

    public void setForeground(int r, int g, int b, int a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        setForeground(new Color(r, g, b, a));
    }

    public void setForeground(float r, float g, float b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        setForeground(new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), 255));
    }

    public void setForeground(float r, float g, float b, float a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        setForeground(new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f)));
    }

    public Color getForeground() {
        // if (_linesList == null) return Color.White; //?????
        // return _linesList[0].getForeground();
        return _blockForeground;
    }

    public boolean isEditable() {
        return _isEditable;
    }

    public void setEditable(boolean value) {
        if (_isEditable == value)
            return;
        _isEditable = value;

        if (_isEditable)
            _cursor.setVisible(true);
        else
            _cursor.setVisible(false);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        _textureStorage.setBlockWidth(width, _cursor.getWidth());
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        _textureStorage.setBlockHeight(height);
    }

    @Override
    public void initElements() {
        _cursor.setHeight(_textureStorage.getCursorHeight());
        addItems(_selectedArea, _textureStorage, _cursor);
        _textureStorage.initLines(_cursor.getWidth());
    }

    /*
     * private void UpdateLinesData(UpdateType updateType) { foreach (TextLine tl in
     * _linesList) tl.UpdateData(updateType); }
     */

    /*
     * private void RemoveAllLines() { foreach (TextLine tl in _linesList)
     * RemoveItem(tl); }
     */

    @Override
    public void setFocused(boolean value) {
        super.setFocused(value);
        if (isFocused() && _isEditable)
            _cursor.setVisible(true);
        else
            _cursor.setVisible(false);
    }

    private int getLineLetCount(int lineNum) {
        return _textureStorage.getLineLetCount(lineNum);
    }

    private void makeSelectedArea(Point from, Point to) {
        if (from.x == to.x && from.y == to.y) {
            _selectedArea.setRectangles(null);
            return;
        }

        List<Point> selectionRectangles = new LinkedList<>();

        Point fromReal, toReal;
        List<Point> listPt = realFromTo(from, to);
        fromReal = listPt.get(0);
        toReal = listPt.get(1);
        Point tmp = new Point();
        int lsp = getLineSpacer();
        if (from.y == to.y) {
            selectionRectangles.add(addXYShifts(0, -_cursor.getHeight() - lsp / 2 + 1, fromReal, false));
            selectionRectangles.add(addXYShifts(0, -lsp / 2 + 1, toReal, false));
            _selectedArea.setRectangles(selectionRectangles);
            return;
        }

        selectionRectangles.add(addXYShifts(0, -_cursor.getHeight() - lsp / 2 + 1, fromReal, false));
        tmp.x = getLineLetCount(fromReal.y);
        tmp.y = fromReal.y;
        selectionRectangles.add(addXYShifts(0, -lsp / 2 + 1, tmp, false));
        tmp.x = 0;
        tmp.y = toReal.y;
        selectionRectangles.add(addXYShifts(0, -_cursor.getHeight() - lsp / 2 + 1, tmp, false));
        selectionRectangles.add(addXYShifts(0, -lsp / 2 + 1, toReal, false));

        for (int i = fromReal.y + 1; i < toReal.y; i++) {
            tmp.x = 0;
            tmp.y = i;
            selectionRectangles.add(addXYShifts(0, -_cursor.getHeight() - lsp / 2 + 1, tmp, false));
            tmp.x = getLineLetCount(i);
            tmp.y = i;
            selectionRectangles.add(addXYShifts(0, -lsp / 2 + 1, tmp, false));
        }

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

    private Point addXYShifts(int xShift, int yShift, Point point) {
        return addXYShifts(xShift, yShift, point, true);
    }

    private Point addXYShifts(int xShift, int yShift, Point point, boolean isx) {
        Point outPoint = _textureStorage.addXYShifts(xShift, yShift, cursorPosToCoord(point), isx);
        /*
         * int offset = _cursorXMax / 3;
         * 
         * if (globalXShift + outPoint.x < 0) { globalXShift = -outPoint.x; globalXShift
         * += offset; if (globalXShift > 0) globalXShift = 0; updLinesXShift(); } if
         * (globalXShift + outPoint.x > _cursorXMax) { globalXShift = _cursorXMax -
         * outPoint.x; globalXShift -= offset; updLinesXShift(); } if (outPoint.y +
         * globalYShift < 0) { globalYShift = -outPoint.y; updLinesYShift();
         * 
         * } if (outPoint.y + _lineHeight + globalYShift > _cursorYMax) { globalYShift =
         * _cursorYMax - outPoint.y - _lineHeight; updLinesYShift(); }
         * 
         * outPoint.x += globalXShift; outPoint.y += globalYShift;
         */
        outPoint.x += /* getX() + getPadding().left + _linesList.get(0).getMargin().left */ +xShift;
        outPoint.y += /* getY() + getPadding().top + _linesList.get(0).getMargin().top */ +yShift;

        // Console.WriteLine(outPoint.x + " " + outPoint.y + " " + globalYShift);
        return outPoint;
    }

    private String privGetSelectedText() {
        _selectFrom = checkLineFits(_selectFrom);
        _selectTo = checkLineFits(_selectTo);
        if (_selectFrom.x == _selectTo.x && _selectFrom.y == _selectTo.y)
            return "";
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

        if (fromReal.x >= getLineLetCount(fromReal.y))
            sb.append("\n");
        else {
            stmp = new StringBuilder(_textureStorage.getTextInLine(fromReal.y));
            sb.append(stmp.substring(fromReal.x) + "\n");
        }
        for (int i = fromReal.y + 1; i < toReal.y; i++) {
            stmp = new StringBuilder(_textureStorage.getTextInLine(i));
            sb.append(stmp + "\n");
        }

        stmp = new StringBuilder(_textureStorage.getTextInLine(toReal.y));
        sb.append(stmp.substring(0, toReal.x));

        return sb.toString();
    }

    public String getSelectedText() {
        return privGetSelectedText();
    }

    private void privPasteText(String pasteStr) {
        if (!_isEditable)
            return;
        _textureStorage.textInputLock.lock();
        try {
            if (_isSelect)
                privCutText();
            if (pasteStr == null || pasteStr.equals(""))
                return;

            _cursor_position = checkLineFits(_cursor_position);
            _cursor_position = _textureStorage.pasteText(pasteStr, _cursor_position);

            replaceCursor();
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    public void pasteText(String pasteStr) {
        privPasteText(pasteStr);
    }

    private String privCutText() {
        if (!_isEditable)
            return "";
        _textureStorage.textInputLock.lock();
        try {
            String str = privGetSelectedText();
            _selectFrom = checkLineFits(_selectFrom);
            _selectTo = checkLineFits(_selectTo);
            if (_selectFrom.x == _selectTo.x && _selectFrom.y == _selectTo.y)
                return "";
            List<Point> listPt = realFromTo(_selectFrom, _selectTo);
            Point fromReal = listPt.get(0);
            Point toReal = listPt.get(1);

            _textureStorage.cutText(fromReal, toReal);

            _cursor_position = fromReal;
            replaceCursor();
            if (_isSelect)
                unselectText();
            _justSelected = false;
            return str;
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    public String cutText() {
        return privCutText();
    }

    private void unselectText() {
        _isSelect = false;
        _justSelected = true;
        makeSelectedArea(new Point(0, 0), new Point(0, 0));
    }

    public void clear() {
        _textureStorage.clear();
        _cursor_position.x = 0;
        _cursor_position.y = 0;
    }

    // style
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        setForeground(style.foreground);
        setFont(style.font);
        // setTextAlignment(style.TextAlignment);
        _textureStorage.setLineContainerAlignment(style.textAlignment);

        Style inner_style = style.getInnerStyle("selection");
        // System.out.println(inner_style.alignment);
        if (inner_style != null) {
            _selectedArea.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("cursor");
        if (inner_style != null) {
            _cursor.setStyle(inner_style);
        }
    }

    @Override
    public List<BaseItem> getItems() {
        List<BaseItem> list = super.getItems();
        return new LinkedList<BaseItem>(Arrays.asList(list.get(0), list.get(1), list.get(2)));
    }

    @Override
    public void removeItem(BaseItem item) {
        if (item.equals(_cursor)) {
            while (super.getItems().size() > 0) {
                super.removeItem(super.getItems().get(0));
            }
            return;
        }
        super.removeItem(item);
    }
}