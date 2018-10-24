package com.spvessel.Items;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.*;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.Style;
import com.spvessel.Engine.FontEngine;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.KeyCode;
import com.spvessel.Flags.KeyMods;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TextBlock extends VisualItem
        implements InterfaceTextEditable, InterfaceDraggable, InterfaceTextShortcuts, InterfaceScrollable {
    private static int count = 0;
    private String _wholeText = "";
    private Rectangle _cursor;
    private Point _cursor_position = new Point(0, 0);
    private CustomSelector _selectedArea;
    private List<TextLine> _linesList;
    private boolean _isEditable = true;

    private Point _selectFrom = new Point(-1, 0);
    private Point _selectTo = new Point(-1, 0);
    private boolean _isSelect = false;
    private boolean _justSelected = false;

    private int _minLineSpacer;
    //private int _minFontY;
    //private int _maxFontY;
    private int _lineHeight;

    private Font _elementFont;
    private int _lineSpacer;
    private List<ItemAlignment> _blockAlignment = new LinkedList<>(
            Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
    private Color _blockForeground;

    /*
     * private final KeyCode BackspaceCode = KeyCode.BACKSPACE;// 14; private final
     * KeyCode DeleteCode = KeyCode.DELETE;// 339; private final KeyCode
     * LeftArrowCode = KeyCode.LEFT;// 331; private final KeyCode RightArrowCode =
     * KeyCode.RIGHT;// 333; private final KeyCode UpArrowCode = KeyCode.UP;// 328;
     * private final KeyCode DownArrowCode = KeyCode.DOWN;// 336; private final
     * KeyCode EndCode = KeyCode.END;// 335; private final KeyCode HomeCode =
     * KeyCode.HOME;// 327; private final KeyCode ACode = KeyCode.A;// 30; private
     * final KeyCode EnterCode = KeyCode.ENTER;// 28;
     */

    private List<KeyCode> ShiftValCodes;
    private List<KeyCode> InsteadKeyMods;

    private Lock textInputLock = new ReentrantLock();

    private int globalXShift = 0;
    private int globalYShift = 0;
    // private int _cursorXMin = 0;
    private int _cursorXMax = Integer.MAX_VALUE;
    // private int _cursorYMin = 0;
    private int _cursorYMax = Integer.MAX_VALUE;

    public TextBlock() {
        setItemName("TextBlock_" + count);
        // setBackground(180, 180, 180);
        // setForeground(Color.Black);
        // setPadding(5, 0, 5, 0);
        count++;

        _linesList = new LinkedList<>();
        TextLine te = new TextLine();
        if (getForeground() != null)
            te.setForeground(getForeground());
        te.setTextAlignment(_blockAlignment);
        if (_elementFont != null)
            te.setFont(_elementFont);
        _linesList.add(te);

        _cursor = new Rectangle();
        _selectedArea = new CustomSelector();
        // _selectedArea.setBackground(111, 181, 255);
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.TextBlock"));
        setStyle(DefaultsService.getDefaultStyle(com.spvessel.Items.TextBlock.class));

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

        int[] output = te.getFontDims();
        _minLineSpacer = output[0];
        //_minFontY = output[1];
        //_maxFontY = output[2];
        _lineHeight = output[2]; //Math.abs(_maxFontY - _minFontY + 1);
        if (_lineSpacer < _minLineSpacer)
            _lineSpacer = _minLineSpacer;

        _cursor.setHeight(_lineHeight + _lineSpacer); // + 6);
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

    protected void onScrollUp(Object sender, MouseArgs args) {
        int h = getTextHeight();
        if (h < _cursorYMax)
            return;
        if (globalYShift >= 0)
            return;

        int curCoord = _cursor.getY() - globalYShift;

        globalYShift += getLineY(1);
        if (globalYShift > 0)
            globalYShift = 0;

        updLinesYShift();
        _cursor.setY(curCoord + globalYShift);
        // replaceCursor();
    }

    protected void onScrollDown(Object sender, MouseArgs args) {
        int h = getTextHeight();
        if (h < _cursorYMax)
            return;
        if (h + globalYShift <= _cursorYMax)
            return;

        int curCoord = _cursor.getY() - globalYShift;

        globalYShift -= getLineY(1);
        if (h + globalYShift < _cursorYMax)
            globalYShift = _cursorYMax - h;

        updLinesYShift();
        _cursor.setY(curCoord + globalYShift);
        // replaceCursor();
    }

    public void invokeScrollUp(MouseArgs args) {
        eventScrollUp.execute(this, args);
    }

    public void invokeScrollDown(MouseArgs args) {
        eventScrollDown.execute(this, args);
    }

    private void replaceCursorAccordingCoord(Point realPos) {
        // Вроде бы и без этого норм, но пусть пока будет
        // if (_linesList != null)
        // realPos.y -= (int)_linesList[0].getLineTopCoord(); //???????!!!!!!

        // Console.WriteLine(_lineSpacer);

        realPos.y -= getY() + getPadding().top + globalYShift + _linesList.get(0).getMargin().top;
        int lineNumb = realPos.y / getLineY(1);// (_lineHeight + _lineSpacer);
        if (lineNumb >= _linesList.size())
            lineNumb = _linesList.size() - 1;
        if (lineNumb < 0)
            lineNumb = 0;

        realPos.x -= getX() + getPadding().left + _linesList.get(0).getMargin().left;

        _cursor_position.y = lineNumb;
        _cursor_position.x = coordXToPos(realPos.x, lineNumb);
        replaceCursor();
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

    protected void onKeyRelease(Object sender, KeyArgs args) {
        // if (args.key == KeyCode.v/* 0x2F*/ && args.mods == KeyMods.CONTROL)
        // pasteText(CommonService.ClipboardTextStorage);
    }

    protected void onKeyPress(Object sender, KeyArgs args) {
        textInputLock.lock();
        try {

            //for (int i = 0; i < _linesList.size(); i++) {
                // System.out.print("line " + i + " ");
                // int[] tmp = _linesList.get(i).getAll(0);
                // if (tmp != null)
                // System.out.println("line " + i + " shifts " + " " + tmp[0] + " " +
                // _linesList.get(i).getTextAlignment());
                // System.out.println("x,y " + _linesList.get(0).getParent().getX() + " " +
                // _linesList.get(0).getParent().getY());
            //}

            if (!_isEditable) {
                if (args.mods.equals(KeyMods.CONTROL) && (args.key == KeyCode.A || args.key == KeyCode.a)) {
                    _selectFrom.x = 0;
                    _selectFrom.y = 0;
                    _cursor_position.y = _linesList.size() - 1;
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
                        _cursor_position.y = _linesList.size() - 1;
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
                                StringBuilder sb = new StringBuilder(_linesList.get(_cursor_position.y).getItemText());
                                setTextInLine(sb.deleteCharAt(_cursor_position.x - 1).toString());
                                _cursor_position.x--;
                            } else if (_cursor_position.y > 0) {
                                _cursor_position.y--;
                                _cursor_position.x = getLineLetCount(_cursor_position.y);
                                combineLines(_cursor_position.y);
                            }
                            replaceCursor();
                        }
                        if (args.key == KeyCode.DELETE) // delete
                        {
                            if (_cursor_position.x < getLineLetCount(_cursor_position.y)) {
                                StringBuilder sb = new StringBuilder(_linesList.get(_cursor_position.y).getItemText());
                                setTextInLine(sb.deleteCharAt(_cursor_position.x).toString());
                            } else if (_cursor_position.y < _linesList.size() - 1) {
                                combineLines(_cursor_position.y);
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
                    else if (_cursor_position.y < _linesList.size() - 1) {
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
                    if (_cursor_position.y < _linesList.size() - 1)
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

            if (args.key == KeyCode.ENTER) // enter
            {
                breakLine();
                _cursor_position.y++;
                _cursor_position.x = 0;

                replaceCursor();
            }

            if (_isSelect) {
                if (!_selectTo.equals(_cursor_position)) {
                    _selectTo = new Point(_cursor_position);
                    makeSelectedArea(_selectFrom, _selectTo);
                }
            }
        } finally {
            textInputLock.unlock();
        }
    }

    public void onTextInput(Object sender, TextInputArgs args) {
        if (!_isEditable)
            return;
        textInputLock.lock();
        try {
            // byte[] input = BitConverter.getBytes(args.Character);
            // String str = Encoding.UTF32.getString(input);
            byte[] input = ByteBuffer.allocate(4).putInt(args.character).array();
            String str = new String(input, Charset.forName("UTF-32")); // StandardCharsets.UTF_8);

            if (_isSelect)
                unselectText();// cutText();
            if (_justSelected)
                cutText();
            _cursor_position = checkLineFits(_cursor_position);

            StringBuilder sb = new StringBuilder(_linesList.get(_cursor_position.y).getItemText());
            setTextInLine(sb.insert(_cursor_position.x, str).toString());
            _cursor_position.x++;
            replaceCursor();
        } finally {
            textInputLock.unlock();
        }
    }

    private Point checkLineFits(Point checkPoint) {
        Point outPt = new Point();
        // ??? check line count
        outPt.y = checkPoint.y;
        if (outPt.y == -1)
            outPt.y = 0;
        int letCount = getLineLetCount(checkPoint.y);
        outPt.x = checkPoint.x;
        if (outPt.x == -1)
            outPt.x = 0;
        if (checkPoint.x > letCount)
            outPt.x = letCount;

        return outPt;
    }

    private Point cursorPosToCoord(Point cPos0) {
        Point coord = new Point(0, 0);
        // return coord;
        Point cPos = checkLineFits(cPos0);

        int letCount = getLineLetCount(cPos.y);
        coord.y = getLineY(cPos.y);

        if (letCount == 0) {
            coord.x = 0;
            return coord;
        }
        if (cPos.x == 0) {
            coord.x = 0;
            return coord;
        } else {
            if (!(cPos.x == 0 && cPos.y == 0)) // ???
            {
                coord.x = _linesList.get(cPos.y).getLetPosArray().get(cPos.x - 1);
            }
        }

        return coord;
    }

    private void replaceCursor() {
        Point pos = addXYShifts(0, 0, _cursor_position);
        _cursor.setX(pos.x);
        _cursor.setY(pos.y - _lineSpacer / 2 + 1);// - 3);
    }

    void setLineSpacer(int lineSpacer) {
        if (lineSpacer < _minLineSpacer)
            lineSpacer = _minLineSpacer;

        if (lineSpacer != _lineSpacer) {
            _lineSpacer = lineSpacer;

            if (_linesList == null)
                return;

            updLinesYShift();
            /*
             * for (int i = 0; i < _linesList.Count; i++) {
             * _linesList[i].setLineYShift((_lineHeight + _lineSpacer) * i); }
             */
        }

        _cursor.setHeight(_lineHeight + _lineSpacer); // + 6);
    }

    public int getLineSpacer() {
        return _lineSpacer;
    }

    String getWholeText() {
        StringBuilder sb = new StringBuilder();
        if (_linesList == null)
            return "";
        if (_linesList.size() == 1) {
            sb.append(_linesList.get(0).getText());
        } else {
            for (TextLine te : _linesList) {
                sb.append(te.getText());
                sb.append("\n");
            }
            sb.delete(sb.length() - 3, sb.length() - 1); // Remove(sb.Length - 3, 2);
        }
        _wholeText = sb.toString();
        return _wholeText;
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
    
    private Indents _text_margin = new Indents();
    public void setTextMargin(Indents margin) {
        _text_margin = margin;
        for (TextLine var : _linesList) {
            var.setMargin(margin);
        } 
    }

    public void setFont(Font font) {
        if (!font.equals(_elementFont)) {
            _elementFont = font;
            if (_elementFont == null)
                return;
            int[] output = FontEngine.getSpacerDims(font);
            _minLineSpacer = output[0];
            //_minFontY = output[1];
            //_maxFontY = output[2];
            _lineHeight = output[2]; //Math.abs(_maxFontY - _minFontY);
            if (_lineSpacer < _minLineSpacer)
                _lineSpacer = _minLineSpacer;

            if (_linesList == null)
                return;
            for (TextLine te : _linesList)
                te.setFont(font);

            _cursor.setHeight(_lineHeight + _lineSpacer); // + 6);
        }
    }

    public Font getFont() {
        return _elementFont;
    }

    public void setText(String text) {
        textInputLock.lock();
        try {
            if (text.equals("") || text == null)
                clear();
            else if (!text.equals(getWholeText())) {
                splitAndMakeLines(text);
            }
            // globalXShift = _cursorXMax; ???
            // updLinesXShift(); ???
        } finally {
            textInputLock.unlock();
        }
    }

    private void setTextInLine(String text) {
        _linesList.get(_cursor_position.y).setItemText(text);
        // _linesList[_cursor_position.y].UpdateData(UpdateType.Critical); //Doing in
        // TextItem

        // int test0 = _linesList[_cursor_position.y].getLineXShift();
        // _linesList[_cursor_position.y].CheckXShift(_cursorXMax);
        // int test1 = _linesList[_cursor_position.y].getLineXShift();
        // if (test0 != test1)
        // {
        // globalXShift = test1;
        // updLinesXShift();
        // }
    }

    public int getTextWidth() {
        int w = 0, w0;
        if (_linesList == null)
            return w;
        for (TextLine te : _linesList) {
            w0 = te.getWidth();
            w = (w < w0) ? w0 : w;
        }
        return w;
    }

    public int getTextHeight() {
        // return _lineHeight * _linesList.Count + _lineSpacer * (_linesList.Count - 1);
        return getLineY(_linesList.size());
    }

    private void splitAndMakeLines(String text) {
        clear();
        // removeLines(0, _linesList.Count - 1);
        // _linesList = new List<TextLine>();

        _wholeText = text;

        String[] line = text.split("\n");
        int inc = 0;
        String s;

        _linesList.get(0).setItemText(line[0]);

        for (int i = 1; i < line.length; i++) {
            s = line[i].replaceAll("\r", "");
            addNewLine(s, inc);
            inc++;
        }

        _cursor_position.y = line.length - 1;
        _cursor_position.x = getLineLetCount(_cursor_position.y);
        replaceCursor();
    }

    /*
     * internal void UpdateData(UpdateType updateType) { //foreach (TextEdit te in
     * _linesList) //te.Up }
     */
    public void setForeground(Color color) {
        if (_linesList != null && !color.equals(getForeground())) {
            _blockForeground = color;
            for (TextLine te : _linesList)
                te.setForeground(color);
        }
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
        _cursorXMax = getWidth() - _cursor.getWidth() - getPadding().left - getPadding().right
                - _linesList.get(0).getMargin().left - _linesList.get(0).getMargin().right; // _cursorXMin;// ;
        setAllowWidth();
        updLinesXShift();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        _cursorYMax = getHeight() - getPadding().top - getPadding().bottom - _linesList.get(0).getMargin().top
                - _linesList.get(0).getMargin().bottom; // _cursorYMin;
        setAllowHeight();
        updLinesYShift();
    }

    @Override
    public void initElements() {
        _cursor.setHeight(_lineHeight + _lineSpacer);
        addItems(_selectedArea, _cursor);
        addAllLines();

        _cursorXMax = getWidth() - _cursor.getWidth() - getPadding().left - getPadding().right - _linesList.get(0).getMargin().left
                - _linesList.get(0).getMargin().right; // _cursorXMin;// ;
        _cursorYMax = getHeight() - getPadding().top - getPadding().bottom - _linesList.get(0).getMargin().top - _linesList.get(0).getMargin().bottom; // _cursorYMin;
        setAllowWidth();
        setAllowHeight();
        updLinesXShift();
        updLinesYShift();
    }

    private void setLineContainerAlignment(List<ItemAlignment> alignment) {
        for (TextLine tl : _linesList)
            tl.setAlignment(alignment);
    }

    /*
     * private void UpdateLinesData(UpdateType updateType) { foreach (TextLine tl in
     * _linesList) tl.UpdateData(updateType); }
     */
    private void addAllLines() {
        removeItem(_cursor);
        for (TextLine tl : _linesList)
            addItem(tl);
        addItem(_cursor);
    }

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
        if (lineNum >= _linesList.size())
            return 0;
        else {
            return _linesList.get(lineNum).getItemText().length();
        }
    }

    private void makeSelectedArea(Point from, Point to) {
        if (from.x == to.x && from.y == to.y) {
            _selectedArea.setRectangles(null);
            return;
        }

        List<Point> selectionRectangles = new LinkedList<>();
        /// !!!Добавить высоту строк
        Point fromReal, toReal;
        List<Point> listPt = realFromTo(from, to);
        fromReal = listPt.get(0);
        toReal = listPt.get(1);

        // System.out.println(from + " " + to + " " + fromReal + " " + toReal);

        Point tmp = new Point();
        if (from.y == to.y) {
            selectionRectangles.add(addXYShifts(0, -_cursor.getHeight() - _lineSpacer / 2 + 1, fromReal));
            selectionRectangles.add(addXYShifts(0, -_lineSpacer / 2 + 1, toReal));
            _selectedArea.setRectangles(selectionRectangles);
            return;
        }

        selectionRectangles.add(addXYShifts(0, -_cursor.getHeight() - _lineSpacer / 2 + 1, fromReal));
        tmp.x = getLineLetCount(fromReal.y);
        tmp.y = fromReal.y;
        selectionRectangles.add(addXYShifts(0, -_lineSpacer / 2 + 1, tmp));
        tmp.x = 0;
        tmp.y = toReal.y;
        selectionRectangles.add(addXYShifts(0, -_cursor.getHeight() - _lineSpacer / 2 + 1, tmp));
        selectionRectangles.add(addXYShifts(0, -_lineSpacer / 2 + 1, toReal));

        for (int i = fromReal.y + 1; i < toReal.y; i++) {
            tmp.x = 0;
            tmp.y = i;
            selectionRectangles.add(addXYShifts(0, -_cursor.getHeight() - _lineSpacer / 2 + 1, tmp));
            tmp.x = getLineLetCount(i);
            tmp.y = i;
            selectionRectangles.add(addXYShifts(0, -_lineSpacer / 2 + 1, tmp));
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
        Point outPoint = cursorPosToCoord(point);
        int offset = _cursorXMax / 3;

        if (globalXShift + outPoint.x < 0) {
            globalXShift = -outPoint.x;
            globalXShift += offset;
            if (globalXShift > 0)
                globalXShift = 0;
            updLinesXShift();
        }
        if (globalXShift + outPoint.x > _cursorXMax) {
            globalXShift = _cursorXMax - outPoint.x;
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

        outPoint.x += globalXShift;
        outPoint.y += globalYShift;

        outPoint.x += getX() + getPadding().left + _linesList.get(0).getMargin().left + xShift;
        outPoint.y += getY() + getPadding().top + _linesList.get(0).getMargin().top + yShift;

        // Console.WriteLine(outPoint.x + " " + outPoint.y + " " + globalYShift);
        return outPoint;
    }

    public String getSelectedText() {
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
            stmp = new StringBuilder(_linesList.get(fromReal.y).getItemText());
            sb.append(stmp.substring(fromReal.x, toReal.x)); // - fromReal.x
            return sb.toString();
        }

        if (fromReal.x >= getLineLetCount(fromReal.y))
            sb.append("\n");
        else {
            stmp = new StringBuilder(_linesList.get(fromReal.y).getItemText());
            sb.append(stmp.substring(fromReal.x) + "\n");
        }
        for (int i = fromReal.y + 1; i < toReal.y; i++) {
            stmp = new StringBuilder(_linesList.get(i).getItemText());
            sb.append(stmp + "\n");
        }

        stmp = new StringBuilder(_linesList.get(toReal.y).getItemText());
        sb.append(stmp.substring(0, toReal.x));

        return sb.toString();
    }

    public void pasteText(String pasteStr) {
        if (!_isEditable)
            return;
        textInputLock.lock();
        try {
            if (_isSelect)
                cutText();
            if (pasteStr == null || pasteStr.equals(""))
                return;

            _cursor_position = checkLineFits(_cursor_position);
            String textBeg = new StringBuilder(_linesList.get(_cursor_position.y).getItemText()).substring(0,
                    _cursor_position.x);
            String textEnd = "";
            if (_cursor_position.x < getLineLetCount(_cursor_position.y))
                textEnd = new StringBuilder(_linesList.get(_cursor_position.y).getItemText())
                        .substring(_cursor_position.x);

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
                // Console.WriteLine(line[line.Length - 1].Length + " " + (_cursor_position.y +
                // line.Length - 1));

            }
            // Console.WriteLine(_cursor_position.x + " " + _cursor_position.y);

            replaceCursor();
        } finally {
            textInputLock.unlock();
        }
    }

    public String cutText() {
        if (!_isEditable)
            return "";
        textInputLock.lock();
        try {
            String str = getSelectedText();
            _selectFrom = checkLineFits(_selectFrom);
            _selectTo = checkLineFits(_selectTo);
            if (_selectFrom.x == _selectTo.x && _selectFrom.y == _selectTo.y)
                return "";
            List<Point> listPt = realFromTo(_selectFrom, _selectTo);
            Point fromReal = listPt.get(0);
            Point toReal = listPt.get(1);

            if (fromReal.y == toReal.y) {
                StringBuilder sb = new StringBuilder(_linesList.get(toReal.y).getItemText());
                _linesList.get(toReal.y).setItemText(sb.delete(fromReal.x, toReal.x).toString()); // - fromReal.x
            } else {
                removeLines(fromReal.y + 1, toReal.y - 1);
                StringBuilder sb = new StringBuilder(_linesList.get(fromReal.y).getItemText());
                _linesList.get(fromReal.y).setItemText(sb.substring(0, fromReal.x));
                sb = new StringBuilder(_linesList.get(fromReal.y + 1).getItemText());
                _linesList.get(fromReal.y + 1).setItemText(sb.substring(toReal.x));
                combineLines(fromReal.y);
            }

            _cursor_position = fromReal;
            replaceCursor();
            if (_isSelect)
                unselectText();
            _justSelected = false;
            // Console.WriteLine(str);
            return str;
        } finally {
            textInputLock.unlock();
        }
    }

    private void unselectText() {
        _isSelect = false;
        _justSelected = true;
        makeSelectedArea(new Point(0, 0), new Point(0, 0));
    }

    private void addNewLine(String text, int lineNum) {
        removeItem(_cursor);
        TextLine te = new TextLine();
        te.setForeground(getForeground());
        te.setTextAlignment(_blockAlignment);
        te.setMargin(_text_margin);
        if (_elementFont != null)
            te.setFont(_elementFont);
        addItem(te);
        // text.TrimEnd('\r');
        te.setItemText(text);
        /*
         * te.setLineYShift((_lineHeight + _lineSpacer) * lineNum); for (int i =
         * lineNum; i < _linesList.Count; i++) _linesList[i].setLineYShift((_lineHeight
         * + _lineSpacer) * (i + 1));
         */
        _linesList.add(lineNum, te);

        updLinesYShift(); // Пока обновляются все, но в принципе, нужно только под lineNum

        addItem(_cursor);
    }

    private void breakLine() {
        String newText;
        if (_cursor_position.x >= getLineLetCount(_cursor_position.y))
            newText = "";
        else {
            TextLine tl = _linesList.get(_cursor_position.y);
            StringBuilder text = new StringBuilder(tl.getItemText());
            tl.setItemText(text.substring(0, _cursor_position.x));
            newText = text.substring(_cursor_position.x);
        }
        addNewLine(newText, _cursor_position.y + 1);
    }

    private void combineLines(int topLineY) {
        String text = _linesList.get(topLineY).getItemText();
        text += _linesList.get(topLineY + 1).getItemText();
        _linesList.get(topLineY).setItemText(text);

        removeLines(topLineY + 1, topLineY + 1);
        /*
         * _linesList[topLineY + 1].setItemText(""); _linesList.RemoveAt(topLineY + 1);
         * for (int i = topLineY + 1; i < _linesList.Count; i++) {
         * _linesList[i].setLineYShift((_lineHeight + _lineSpacer) * i); }
         */
    }

    private void removeLines(int fromLine, int toLine) {
        int inc = fromLine;
        while (inc <= toLine) {
            // _linesList[fromLine].setItemText("");
            removeItem(_linesList.get(fromLine));
            _linesList.remove(fromLine);
            inc++;
        }

        updLinesYShift(); // Пока обновляются все, но в принципе, нужно только под fromLine
        /*
         * for (int i = fromLine; i < _linesList.Count; i++) {
         * _linesList[i].setLineYShift((_lineHeight + _lineSpacer) * i); // +
         * _lineSpacer); }
         */
    }

    public void clear() {
        _linesList.get(0).setItemText("");
        removeLines(1, _linesList.size() - 1);
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
        setLineContainerAlignment(style.textAlignment);

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

    private int getLineXShift(int n) {
        if (_linesList.size() > n)
            return _linesList.get(n).getLineXShift();
        else
            return 0;
    }

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
        return (_lineHeight + _lineSpacer) * num;// _lineSpacer / 2 +
    }

}