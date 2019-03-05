package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class TextBlock extends Prototype
        implements InterfaceTextEditable, InterfaceDraggable, InterfaceTextShortcuts, InterfaceGrid {

    EventCommonMethod textChanged = new EventCommonMethod();
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

    private List<KeyCode> ShiftValCodes;
    private List<KeyCode> InsteadKeyMods;

    private int scrollXStep = 30;

    TextBlock() {
        setItemName("TextBlock_" + count);
        count++;

        _textureStorage = new TextureStorage();

        _cursor = new Rectangle();
        _selectedArea = new CustomSelector();

        // setStyle(DefaultsService.getDefaultStyle(TextBlock.class));

        eventMousePress.add(this::onMousePressed);
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

        _cursor.setHeight(_textureStorage.getCursorHeight());

        undoQueue = new ArrayDeque<>();
        redoQueue = new ArrayDeque<>();
        undoQueue.addFirst(new TextBlockState(getText(), new Point(_cursor_position)));
    }

    private void onMousePressed(Object sender, MouseArgs args) {
        _textureStorage.textInputLock.lock();
        try {
            replaceCursorAccordingCoord(new Point(args.position.getX(), args.position.getY()));
            if (_isSelect)
                unselectText();
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    private void onDragging(Object sender, MouseArgs args) {
        _textureStorage.textInputLock.lock();
        try {
            replaceCursorAccordingCoord(new Point(args.position.getX(), args.position.getY()));
            if (!_isSelect) {
                _isSelect = true;
                _selectFrom = new Point(_cursor_position);
            } else {
                _selectTo = new Point(_cursor_position);
                makeSelectedArea(_selectFrom, _selectTo);
            }
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    int getScrollYStep() {
        return _textureStorage.getScrollStep();
    }

    int getScrollXStep() {
        return scrollXStep;
    }

    int getScrollYOffset() {
        return _textureStorage.getScrollYOffset();
    }

    void setScrollYOffset(int offset) {
        int oldOff = _textureStorage.getScrollYOffset();
        _textureStorage.setScrollYOffset(offset);
        int diff = offset - oldOff;
        _selectedArea.shiftAreaY(diff);
        _cursor.setY(_cursor.getY() + diff);
    }

    int getScrollXOffset() {
        return _textureStorage.getScrollXOffset();
    }

    void setScrollXOffset(int offset) {
        int oldOff = _textureStorage.getScrollXOffset();
        _textureStorage.setScrollXOffset(offset);
        int diff = offset - oldOff;
        _selectedArea.shiftAreaX(diff);
        _cursor.setX(_cursor.getX() + diff);
    }

    private void onScrollUp(Object sender, MouseArgs args) {
        int curPos = _cursor.getY();
        _cursor.setY(_textureStorage.scrollBlockUp(_cursor.getY()));
        curPos = _cursor.getY() - curPos;
        _selectedArea.shiftAreaY(curPos);
        // replaceCursor();
    }

    private void onScrollDown(Object sender, MouseArgs args) {
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
        _cursor_position = _textureStorage.replaceCursorAccordingCoord(realPos);
        replaceCursor();
    }

    private void onKeyRelease(Object sender, KeyArgs args) {
        // if (args.key == KeyCode.v/* 0x2F*/ && args.mods == KeyMods.CONTROL)
        // pasteText(CommonService.ClipboardTextStorage);
    }

    private void onKeyPress(Object sender, KeyArgs args) {
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
                cancelJustSelected();
            }

            if (args.mods != KeyMods.NO) {
                // Выделение не сбрасывается, проверяются сочетания
                switch (args.mods) {
                    case SHIFT:
                        if (ShiftValCodes.contains(args.key)) {
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
                        cutText();
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
                                undoStuff();
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
                                undoStuff();
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
                // textChanged.execute();
                undoStuff();
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

    private void onTextInput(Object sender, TextInputArgs args) {
        if (!_isEditable)
            return;
        _textureStorage.textInputLock.lock();
        try {
            ignoreSetInLine = true;

            // byte[] input = BitConverter.getBytes(args.Character);
            // String str = Encoding.UTF32.getString(input);
            byte[] input = ByteBuffer.allocate(4).putInt(args.character).array();
            String str = new String(input, Charset.forName("UTF-32")); // StandardCharsets.UTF_8);

            if (_isSelect || _justSelected) {
                unselectText();
                privCutText();
            }
            if (_justSelected)
                cancelJustSelected(); // _justSelected = false;

            _cursor_position = checkLineFits(_cursor_position);

            StringBuilder sb = new StringBuilder(_textureStorage.getTextInLine(_cursor_position.y));
            setTextInLine(sb.insert(_cursor_position.x, str).toString());
            _cursor_position.x++;
            replaceCursor();

            undoStuff();

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

    int getLineSpacer() {
        return _textureStorage.getLineSpacer();
    }

    String getText() {
        return _textureStorage.getWholeText();
    }

    void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(Arrays.asList(alignment));
    }

    void setTextAlignment(List<ItemAlignment> alignment) {
        // Ignore all changes for yet
    }

    void setTextMargin(Indents margin) {
        _textureStorage.setTextMargin(margin);
    }

    Indents getTextMargin() {
        return _textureStorage.getTextMargin();
    }

    void setFont(Font font) {
        _textureStorage.setFont(font);
        _cursor.setHeight(_textureStorage.getCursorHeight());
    }

    Font getFont() {
        return _textureStorage.getFont();
    }

    void setText(String text) {
        _textureStorage.textInputLock.lock();
        try {
            if (_isSelect)
                unselectText();
            if (_justSelected)
                cancelJustSelected();

            _cursor_position = _textureStorage.setText(text, _cursor_position);
            replaceCursor();

            undoStuff();
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    private void setTextInLine(String text) {
        _textureStorage.setTextInLine(text, _cursor_position.y);

        if (!ignoreSetInLine)
            undoStuff();
        else ignoreSetInLine = false;
    }

    int getTextWidth() {
        return _textureStorage.getWidth();
    }

    int getTextHeight() {
        return _textureStorage.getTextHeight();
    }

    void setForeground(Color color) {
        _textureStorage.setForeground(color);
    }
    void setForeground(int r, int g, int b) {
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
    void setForeground(int r, int g, int b, int a) {
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
    void setForeground(float r, float g, float b) {
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
    void setForeground(float r, float g, float b, float a) {
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

    Color getForeground() {
        return _textureStorage.getForeground();
    }

    boolean isEditable() {
        return _isEditable;
    }

    void setEditable(boolean value) {
        if (_isEditable == value)
            return;
        _isEditable = value;

        if (_isEditable)
            _cursor.setVisible(true);
        else
            _cursor.setVisible(false);
    }

    @Override
    public void initElements() {
        _cursor.setHeight(_textureStorage.getCursorHeight());
        addItems(_selectedArea, _textureStorage, _cursor);
        _textureStorage.initLines(_cursor.getWidth());
    }

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

        outPoint.x += /* getX() + getPadding().left + _linesList.get(0).getMargin().left */ +xShift;
        outPoint.y += /* getY() + getPadding().top + _linesList.get(0).getMargin().top */ +yShift;

        return outPoint;
    }

    private String privGetSelectedText() {
        _textureStorage.textInputLock.lock();
        try {
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
        } finally {
            _textureStorage.textInputLock.unlock();
        }
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

            undoStuff();
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    public void pasteText(String pasteStr) {
        if (pasteStr != null)
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

            _cursor_position = new Point(fromReal);
            replaceCursor();
            if (_isSelect)
                unselectText();
            cancelJustSelected();
            return str;
        } finally {
            _textureStorage.textInputLock.unlock();
        }
    }

    public String cutText() {
        String ans = privCutText();
        undoStuff();
        return ans;
    }

    private void unselectText() {
        _isSelect = false;
        _justSelected = true;
        makeSelectedArea(new Point(0, 0), new Point(0, 0));
    }

    private void cancelJustSelected() {
        _selectFrom.x = -1;// 0;
        _selectFrom.y = 0;
        _selectTo.x = -1;// 0;
        _selectTo.y = 0;
        _justSelected = false;
    }

    void clear() {
        _textureStorage.clear();
        _cursor_position.x = 0;
        _cursor_position.y = 0;
        if (_isSelect)
            unselectText();
        if (_justSelected)
            cancelJustSelected();

        undoStuff();
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
        if (inner_style != null) {
            _selectedArea.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("cursor");
        if (inner_style != null) {
            _cursor.setStyle(inner_style);
        }
    }

    @Override
    public List<InterfaceBaseItem> getItems() {
        List<InterfaceBaseItem> list = super.getItems();
        return new LinkedList<>(Arrays.asList(list.get(0), list.get(1), list.get(2)));
    }

    @Override
    public void removeItem(InterfaceBaseItem item) {
        if (item.equals(_cursor)) {
            while (super.getItems().size() > 0) {
                super.removeItem(super.getItems().get(0));
            }
            return;
        }
        super.removeItem(item);
    }

    private ArrayDeque<TextBlockState> undoQueue;
    private ArrayDeque<TextBlockState> redoQueue;
    private boolean nothingFlag = false;
    private int queueCapacity = 100;
    private boolean ignoreSetInLine = false;

    public void redo() {
        if (redoQueue.size() == 0)
            return;

        TextBlockState tmpText = redoQueue.pollFirst();
        if (tmpText != null) {
            nothingFlag = true;

            setText(tmpText.textState);
            _cursor_position = new Point(tmpText.cursorStateX, tmpText.cursorStateY);
            undoQueue.peekFirst().cursorStateX = _cursor_position.x;
            undoQueue.peekFirst().cursorStateY = _cursor_position.y;
            replaceCursor();

//            _selectFrom = new Point(tmpText.fromSelectState);
//            _selectTo = new Point(tmpText.toSelectState);
//            makeSelectedArea(_selectFrom, _selectTo);
            // _textureStorage.redo();
        }
    }

    public void undo() {
        if (undoQueue.size() == 1)
            return;

//        undoStuff();

        TextBlockState tmpText = undoQueue.pollFirst();
        if (tmpText != null) {
            if (redoQueue.size() > queueCapacity)
                redoQueue.pollLast();
            redoQueue.addFirst(new TextBlockState(tmpText.textState, new Point(tmpText.cursorStateX, tmpText.cursorStateY)));
//            tmpText = null;
            tmpText = undoQueue.pollFirst();
            if (tmpText != null) {
                nothingFlag = true;

                setText(tmpText.textState);
                _cursor_position = new Point(tmpText.cursorStateX, tmpText.cursorStateY);
                undoQueue.peekFirst().cursorStateX = _cursor_position.x;
                undoQueue.peekFirst().cursorStateY = _cursor_position.y;
                replaceCursor();

//                _selectFrom = new Point(tmpText.fromSelectState);
//                _selectTo = new Point(tmpText.toSelectState);
//                makeSelectedArea(_selectFrom, _selectTo);
                // _textureStorage.undo();
            }
        }
    }

    private void undoStuff() {
        if (!nothingFlag) {
//                TextBlockState tbs = new TextBlockState(getText(), _cursor_position);
//                redoQueue.addFirst(tbs);
            redoQueue = new ArrayDeque<>();
        } else {
            nothingFlag = false;
        }

        if (undoQueue.size() > queueCapacity)
            undoQueue.pollLast();
        TextBlockState tbs = new TextBlockState(getText(), new Point(_cursor_position));
//        if (_isSelect) {
//            tbs.fromSelectState = new Point(_selectFrom);
//            tbs.toSelectState = new Point(_selectTo);
//        }
        undoQueue.addFirst(tbs);
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
    public void setX(int _x) {
        if (getX() != _x) {
            super.setX(_x);
            updateLayout();
        }
    }

    @Override
    public void setY(int _y) {
        if (getY() != _y) {
            super.setY(_y);
            updateLayout();
        }
    }

    public void updateLayout() {
        if (_textureStorage.getParent() == null)
            return;
        // ReplaceCursor();
        Point pos = addXYShifts(0, 0, _cursor_position);
        _cursor.setX(pos.x);
        _cursor.setY(pos.y - getLineSpacer() / 2 + 1);// - 3);
        makeSelectedArea(_selectFrom, _selectTo);

    }

    // private class TextCursor : Rectangle {
    // Point _cursor_position = new Point(0, 0);
    // TextCursor(int height) {
    // SetItemName("TextCursor_" + count);
    // SetHeight(height);
    // }

    // }

    int getCursorWidth() {
        return _cursor.getWidth();
    }

    void appendText(String text) {
        unselectText();
        cancelJustSelected();
        int lineNum = _textureStorage.getCount() - 1;
        _cursor_position = new Point(getLineLetCount(lineNum), lineNum);
        pasteText(text);
    }

    private class TextBlockState {
        String textState;
        int cursorStateX;
        int cursorStateY;
//        Point fromSelectState;
//        Point toSelectState;
        TextBlockState() {
        }
        TextBlockState(String textState, Point cursorState) {
            this.textState = textState;
            this.cursorStateX = cursorState.x;
            this.cursorStateY = cursorState.y;
//            fromSelectState = new Point(0, 0);
//            toSelectState = new Point(0, 0);
        }
    }
}