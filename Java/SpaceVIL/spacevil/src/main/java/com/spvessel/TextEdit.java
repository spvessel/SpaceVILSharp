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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TextEdit extends Prototype
        implements InterfaceTextEditable, InterfaceTextShortcuts, InterfaceDraggable, InterfaceScrollable {
    static int count = 0;
    private TextLine _text_object;
    private Rectangle _cursor;
    private int _cursor_position = 0;
    private Rectangle _selectedArea;
    private boolean _isEditable = true;

    // private int _cursorXMin = 0;
    private int _cursorXMax = Integer.MAX_VALUE;
    // private int _lineXShift = 0;

    public Rectangle getSelectionArea() {
        return _selectedArea;
    }

    private int _selectFrom = -1;
    private int _selectTo = -1;
    private boolean _isSelect = false;
    private boolean _justSelected = false;
    /*
     * private final KeyCode BackspaceCode = KeyCode.BACKSPACE;// 14; private final
     * KeyCode DeleteCode = KeyCode.DELETE;// 339; private final KeyCode
     * LeftArrowCode = KeyCode.LEFT;// 331; private final KeyCode RightArrowCode =
     * KeyCode.RIGHT;// 333; private final KeyCode EndCode = KeyCode.END;// 335;
     * private final KeyCode HomeCode = KeyCode.HOME;// 327; private final KeyCode
     * ACode = KeyCode.A;// 30;
     */
    private List<KeyCode> ShiftValCodes;
    private List<KeyCode> InsteadKeyMods;

    private Lock textInputLock = new ReentrantLock();

    private int scrollStep = 15;

    public TextEdit() {
        _text_object = new TextLine();
        _cursor = new Rectangle();
        _selectedArea = new Rectangle();

        setItemName("TextEdit_" + count);
        count++;

        eventMousePress.add(this::onMousePressed);
        eventMouseDrag.add(this::onDragging);
        eventKeyPress.add(this::onKeyPress);
        eventKeyRelease.add(this::onKeyRelease);
        eventTextInput.add(this::onTextInput);
        eventScrollUp.add(this::onScrollUp);
        eventScrollDown.add(this::onScrollDown);
        eventMouseDoubleClick.add(this::onMouseDoubleClick);

        ShiftValCodes = new LinkedList<>(Arrays.asList(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.END, KeyCode.HOME));
        InsteadKeyMods = new LinkedList<>(Arrays.asList(KeyCode.LEFTSHIFT, KeyCode.RIGHTSHIFT, KeyCode.LEFTCONTROL,
                KeyCode.RIGHTCONTROL, KeyCode.LEFTALT, KeyCode.RIGHTALT, KeyCode.LEFTSUPER, KeyCode.RIGHTSUPER));

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.TextEdit"));
        setStyle(DefaultsService.getDefaultStyle(TextEdit.class));
    }

    private void onMouseDoubleClick(Object sender, MouseArgs args) {
        selectAll();
    }

    private void onMousePressed(Object sender, MouseArgs args) {
        textInputLock.lock();
        try {
            replaceCursorAccordingCoord(args.position.getX());
            if (_isSelect)
                unselectText();
        } finally {
            textInputLock.unlock();
        }
    }

    private void onDragging(Object sender, MouseArgs args) {
        textInputLock.lock();
        try {
            replaceCursorAccordingCoord(args.position.getX());

            if (!_isSelect) {
                _isSelect = true;
                _selectFrom = _cursor_position;
            } else {
                _selectTo = _cursor_position;
                makeSelectedArea(_selectFrom, _selectTo);
            }
        } finally {
            textInputLock.unlock();
        }
    }

    private void onScrollUp(Object sender, MouseArgs args) {
        int w = getTextWidth();

        if (w < _cursorXMax)
            return;
        int sh = getLineXShift();
        if (sh >= 0)
            return;

        int curPos = _cursor.getX();
        int curCoord = curPos - sh;

        sh += scrollStep;
        if (sh > 0)
            sh = 0;

        _text_object.setLineXShift(sh);
        _cursor.setX(curCoord + sh);

        curPos = _cursor.getX() - curPos;
        _selectedArea.setX(_selectedArea.getX() + curPos);
    }

    private void onScrollDown(Object sender, MouseArgs args) {
        int w = getTextWidth();

        if (w < _cursorXMax)
            return;
        int sh = getLineXShift();
        if (w + sh <= _cursorXMax)
            return;

        int curPos = _cursor.getX();
        int curCoord = curPos - sh;

        sh -= scrollStep;
        if (w + sh < _cursorXMax)
            sh = _cursorXMax - w;

        _text_object.setLineXShift(sh);
        _cursor.setX(curCoord + sh);

        curPos = _cursor.getX() - curPos;
        _selectedArea.setX(_selectedArea.getX() + curPos);
    }

    public void invokeScrollUp(MouseArgs args) {
        eventScrollUp.execute(this, args);
    }

    public void invokeScrollDown(MouseArgs args) {
        eventScrollDown.execute(this, args);
    }

    private void replaceCursorAccordingCoord(int realPos) {
        int w = getTextWidth();
        if (_text_object.getTextAlignment().contains(ItemAlignment.RIGHT) && (w < _cursorXMax))
            realPos -= getX() + (getWidth() - w) - getPadding().right - _text_object.getMargin().right;
        else
            realPos -= getX() + getPadding().left + _text_object.getMargin().left;

        _cursor_position = coordXToPos(realPos);
        replaceCursor();
    }

    private int coordXToPos(int coordX) {
        int pos = 0;

        List<Integer> lineLetPos = _text_object.getLetPosArray();
        if (lineLetPos == null)
            return pos;

        for (int i = 0; i < lineLetPos.size(); i++) {
            if (lineLetPos.get(i) + getLineXShift() <= coordX + 3)
                pos = i + 1;
            else
                break;
        }

        return pos;
    }

    private void onKeyRelease(InterfaceItem sender, KeyArgs args) {

    }

    private void onKeyPress(InterfaceItem sender, KeyArgs args) {
        textInputLock.lock();
        try {
            if (!_isEditable) {
                if (args.mods.equals(KeyMods.CONTROL) && (args.key == KeyCode.A || args.key == KeyCode.a)) {
                    selectAll();
                }
                return;
            }

            if (!_isSelect && _justSelected) {
                //_selectFrom = -1;// 0;
                //_selectTo = -1;// 0;
                //_justSelected = false;
                cancelJustSelected();
            }
            if (args.mods != KeyMods.NO) {
                // Выделение не сбрасывается, проверяются сочетания
                // 
                switch (args.mods) {
                    case SHIFT:
                        if (ShiftValCodes.contains(args.key)) {
                            if (!_isSelect) {
                                _isSelect = true;
                                _selectFrom = _cursor_position;
                            }
                        }

                        break;

                    case CONTROL:
                        if (args.key == KeyCode.A || args.key == KeyCode.a) {
                            _selectFrom = 0;
                            _cursor_position = privGetText().length();
                            replaceCursor();

                            _isSelect = true;
                        }
                        break;

                    // alt, super ?
                }
            } else {
                if (args.key == KeyCode.BACKSPACE || args.key == KeyCode.DELETE) {
                    if (_isSelect) {
                        privCutText();
                    } else {
                        if (args.key == KeyCode.BACKSPACE && _cursor_position > 0) // backspace
                        {
                            StringBuilder sb = new StringBuilder(privGetText());
                            _cursor_position--;
                            privSetText(sb.deleteCharAt(_cursor_position).toString());
                            //replaceCursor();
                        }
                        if (args.key == KeyCode.DELETE && _cursor_position < privGetText().length()) // delete
                        {
                            StringBuilder sb = new StringBuilder(privGetText());
                            privSetText(sb.deleteCharAt(_cursor_position).toString());
                            //replaceCursor();
                        }
                    }
                } else if (_isSelect && !InsteadKeyMods.contains(args.key))
                    unselectText();
            }

            if (args.key == KeyCode.LEFT && _cursor_position > 0) // arrow left
            {
                if (!_justSelected) {
                    _cursor_position--;
                    replaceCursor();
                }
            }
            if (args.key == KeyCode.RIGHT && _cursor_position < privGetText().length()) // arrow right
            {
                if (!_justSelected) {
                    _cursor_position++;
                    replaceCursor();
                }
            }
            if (args.key == KeyCode.END) // end
            {
                _cursor_position = privGetText().length();
                replaceCursor();
            }
            if (args.key == KeyCode.HOME) // home
            {
                _cursor_position = 0;
                replaceCursor();
            }

            if (_isSelect) {
                if (_selectTo != _cursor_position) {
                    _selectTo = _cursor_position;
                    makeSelectedArea(_selectFrom, _selectTo);
                }
            }
        } finally {
            textInputLock.unlock();
        }
    }

    private int cursorPosToCoord(int cPos) {
        int coord = 0;
        if (_text_object.getLetPosArray() == null)
            return coord;

        if (cPos > 0)
            coord = _text_object.getLetPosArray().get(cPos - 1);

        if (getLineXShift() + coord < 0) {
            _text_object.setLineXShift(-coord);
        }
        if (getLineXShift() + coord > _cursorXMax)
            _text_object.setLineXShift(_cursorXMax - coord);

        return getLineXShift() + coord;
    }

    private void replaceCursor() {
        int len = privGetText().length();

        if (_cursor_position > len) {
            _cursor_position = len;
            //replaceCursor();
        }
        int pos = cursorPosToCoord(_cursor_position);

        int w = getTextWidth();

        if (_text_object.getTextAlignment().contains(ItemAlignment.RIGHT) && (w < _cursorXMax)) {
            _cursor.setX(getX() + getWidth() - w + pos - getPadding().right // - _cursor.getWidth()
                    - _text_object.getMargin().right);
        } else
            _cursor.setX(getX() + getPadding().left + pos + _text_object.getMargin().left);
    }

    private void onTextInput(Object sender, TextInputArgs args) {
        if (!_isEditable)
            return;
        textInputLock.lock();
        try {
            byte[] input = ByteBuffer.allocate(4).putInt(args.character).array();
            String str = new String(input, Charset.forName("UTF-32"));

            if (_isSelect || _justSelected) {
                unselectText();// privCutText();
                privCutText();
            }
            if (_justSelected) cancelJustSelected(); //_justSelected = false;


            StringBuilder sb = new StringBuilder(privGetText());
            _cursor_position++;
            privSetText(sb.insert(_cursor_position - 1, str).toString());
            //replaceCursor();
        } finally {
            textInputLock.unlock();
        }
    }

    @Override
    public void setFocused(boolean value) {
        super.setFocused(value);
        if (isFocused() && _isEditable)
            _cursor.setVisible(true);
        else
            _cursor.setVisible(false);
    }

    public void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(Arrays.asList(alignment));
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        List<ItemAlignment> ial = new LinkedList<>();
        if (alignment.contains(ItemAlignment.RIGHT)) {
            ial.add(ItemAlignment.RIGHT);
            ial.add(ItemAlignment.VCENTER);
        } else {
            ial.add(ItemAlignment.LEFT);
            ial.add(ItemAlignment.VCENTER);
        }
        _text_object.setTextAlignment(ial);
    }

    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    public void setFont(Font font) {
        _text_object.setFont(font);
    }

    public void setFontSize(int size) {
        _text_object.setFontSize(size);
    }

    public void setFontStyle(int style) {
        _text_object.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        _text_object.setFontFamily(font_family);
    }

    public Font getFont() {
        return _text_object.getFont();
    }

    private void privSetText(String text) {
        textInputLock.lock();
        try {
            // _text_object.setLineXShift(_lineXShift, getWidth());
            _text_object.setItemText(text);
            _text_object.checkXShift(_cursorXMax);
            // _text_object.UpdateData(UpdateType.Critical); //Doing in the _text_object

            replaceCursor();
        } finally {
            textInputLock.unlock();
        }
    }

    public void setText(String text) {
        if (_isSelect || _justSelected) {
            unselectText();
            cancelJustSelected();
        }
        privSetText(text);
    }

    private String privGetText() {
        return _text_object.getItemText();
    }

    public String getText() {
        return privGetText();
    }

    public void setForeground(Color color) {
        _text_object.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        _text_object.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        _text_object.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        _text_object.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        _text_object.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _text_object.getForeground();
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
                - _text_object.getMargin().left - _text_object.getMargin().right;
        _text_object.setAllowWidth(_cursorXMax);
        _text_object.checkXShift(_cursorXMax);
        replaceCursor();
    }

    @Override
    public void initElements() {
        addItems(_selectedArea, _text_object, _cursor);

        _cursorXMax = getWidth() - _cursor.getWidth() - getPadding().left - getPadding().right
                - _text_object.getMargin().left - _text_object.getMargin().right;
        _text_object.setAllowWidth(_cursorXMax);
        _text_object.setLineXShift();

        int scctp = _text_object.getFontDims()[0];
        if (scctp > scrollStep)
            scrollStep = scctp;
    }

    public int getTextWidth() {
        return _text_object.getWidth();
    }

    public int getTextHeight() {
        return _text_object.getHeight();
    }

    private void makeSelectedArea(int fromPt, int toPt) {
        if (fromPt == -1)
            fromPt = 0;
        if (toPt == -1)
            toPt = 0;
        fromPt = cursorPosToCoord(fromPt);
        toPt = cursorPosToCoord(toPt);

        if (fromPt == toPt) {
            _selectedArea.setWidth(0);
            return;
        }
        int fromReal = Math.min(fromPt, toPt);
        int toReal = Math.max(fromPt, toPt);
        int width = toReal - fromReal + 1;

        int w = getTextWidth();
        if (_text_object.getTextAlignment().contains(ItemAlignment.RIGHT) && (w < _cursorXMax))
            _selectedArea
                    .setX(getX() + getWidth() - w + fromReal - getPadding().right - _text_object.getMargin().right);
        else
            _selectedArea.setX(getX() + getPadding().left + fromReal + _text_object.getMargin().left);
        _selectedArea.setWidth(width);
    }

    private String privGetSelectedText() {
        textInputLock.lock();
        try {
            if (_selectFrom == -1)
                _selectFrom = 0;
            if (_selectTo == -1)
                _selectTo = 0;
            if (_selectFrom == _selectTo)
                return "";
            String text = privGetText();
            int fromReal = Math.min(_selectFrom, _selectTo);
            int toReal = Math.max(_selectFrom, _selectTo);
            if (fromReal < 0)
                return "";
            String selectedText = text.substring(fromReal, toReal); // - fromReal
            return selectedText;
        } finally {
            textInputLock.unlock();
        }
    }

    public String getSelectedText() {
        return privGetSelectedText();
    }

    private void privPasteText(String pasteStr) {
        if (!_isEditable)
            return;
        textInputLock.lock();
        try {
            if (_isSelect)
                privCutText();
            String text = privGetText();
            String newText = text.substring(0, _cursor_position) + pasteStr + text.substring(_cursor_position);
            _cursor_position += pasteStr.length();
            privSetText(newText);
            //replaceCursor();
        } finally {
            textInputLock.unlock();
        }
    }

    public void pasteText(String pasteStr) {
        if (pasteStr != null)
            privPasteText(pasteStr);
    }

    private String privCutText() {
        if (!_isEditable)
            return "";
        textInputLock.lock();
        try {
            if (_selectFrom == -1)
                _selectFrom = 0;
            if (_selectTo == -1)
                _selectTo = 0;
            String str = privGetSelectedText();
            if (_selectFrom == _selectTo)
                return str;
            int fromReal = Math.min(_selectFrom, _selectTo);
            int toReal = Math.max(_selectFrom, _selectTo);
            StringBuilder sb = new StringBuilder(privGetText());
            _cursor_position = fromReal;
            privSetText(sb.delete(fromReal, toReal).toString()); // - fromReal
            replaceCursor();
            if (_isSelect)
                unselectText();
            cancelJustSelected(); //_justSelected = false;
            return str;
        } finally {
            textInputLock.unlock();
        }
    }

    public String cutText() {
        return privCutText();
    }

    private void unselectText() {
        _isSelect = false;
        _justSelected = true;
        makeSelectedArea(_cursor_position, _cursor_position);
    }

    private void cancelJustSelected() {
        _selectFrom = -1;// 0;
        _selectTo = -1;// 0;
        _justSelected = false;
    }

    /*
     * internal void ShowCursor(bool isShow) { if (isShow) _cursor.setWidth(2); else
     * _cursor.setWidth(0); }
     */
//    private int nearestPosToCursor(double xPos) {
//        List<Integer> endPos = _text_object.getLetPosArray();
//        int pos = (int) endPos.stream().map(x -> Math.abs(x - xPos)).sorted().toArray()[0];
//        return pos;
//    }

//    void setCursorPosition(double newPos) {
//        _cursor_position = nearestPosToCursor(newPos);
//    }

    public void clear() {
        setText("");
    }

    // style
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);

        Style inner_style = style.getInnerStyle("selection");
        if (inner_style != null) {
            _selectedArea.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("cursor");
        if (inner_style != null) {
            _cursor.setStyle(inner_style);
        }
    }

    private int getLineXShift() {
        return _text_object.getLineXShift();
    }

    boolean isBeginning() {
        return (_cursor_position == 0);
    }

    void selectAll() {
        textInputLock.lock();
        try {
            _selectFrom = 0;
            _cursor_position = privGetText().length();
            _selectTo = _cursor_position;
            replaceCursor();
            _isSelect = true;
            makeSelectedArea(_selectFrom, _selectTo);
        } finally {
            textInputLock.unlock();
        }
    }

    public void undo() {
        _text_object.undo();
        replaceCursor();
    }

    public void redo() {
        _text_object.redo();
        replaceCursor();
    }
}
