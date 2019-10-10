package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;
import com.spvessel.spacevil.SpaceVILConstants;
import com.spvessel.spacevil.Common.CommonService;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TextEncrypt extends Prototype implements InterfaceTextEditable, InterfaceDraggable {
    private static int count = 0;

    private String _pwd = "";
    private String _hide_sign;
    private TextLine _text_object;
    private TextLine _substrate_text;

    private Rectangle _cursor;
    private int _cursor_position = 0;

    private Rectangle _selectedArea;
    private boolean _isEditable = true;

    private int _cursorXMax = SpaceVILConstants.sizeMaxValue;

    private int _selectFrom = -1;
    private int _selectTo = -1;
    private boolean _isSelect = false;
    private boolean _justSelected = false;

    private List<KeyCode> ShiftValCodes;
    private List<KeyCode> InsteadKeyMods;

    private Lock textInputLock = new ReentrantLock();

    TextEncrypt() {
        setItemName("TextEncrypt_" + count);

        _hide_sign = "\u25CF";

        _text_object = new TextLine();
        _text_object.setRecountable(true);
        _substrate_text = new TextLine();

        _cursor = new Rectangle();
        _selectedArea = new Rectangle();
        count++;

        eventKeyPress.add(this::onKeyPress);
        eventTextInput.add(this::onTextInput);
        eventMousePress.add(this::onMousePressed);
        eventMouseDrag.add(this::onDragging);
        eventMouseDoubleClick.add(this::onMouseDoubleClick);

        ShiftValCodes = new LinkedList<>(Arrays.asList(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.END, KeyCode.HOME));
        InsteadKeyMods = new LinkedList<>(Arrays.asList(KeyCode.LEFTSHIFT, KeyCode.RIGHTSHIFT, KeyCode.LEFTCONTROL,
                KeyCode.RIGHTCONTROL, KeyCode.LEFTALT, KeyCode.RIGHTALT, KeyCode.LEFTSUPER, KeyCode.RIGHTSUPER));

        // setStyle(DefaultsService.getDefaultStyle(TextEncrypt.class));
        // _text_object.setTextAlignment(new
        // LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER)));

        setCursor(EmbeddedCursor.IBEAM);
    }

    private void onMouseDoubleClick(InterfaceItem sender, MouseArgs args) {
        textInputLock.lock();
        try {
            if (args.button == MouseButton.BUTTON_LEFT) {
                selectAll();
            }
        } finally {
            textInputLock.unlock();
        }
    }

    private void onMousePressed(InterfaceItem sender, MouseArgs args) {
        textInputLock.lock();
        try {
            if (args.button == MouseButton.BUTTON_LEFT) {
                replaceCursorAccordingCoord(args.position.getX());
                if (_isSelect) {
                    unselectText();
                    cancelJustSelected();
                }
            }
        } finally {
            textInputLock.unlock();
        }
    }

    private void onDragging(InterfaceItem sender, MouseArgs args) {
        textInputLock.lock();
        try {
            if (args.button == MouseButton.BUTTON_LEFT) {
                replaceCursorAccordingCoord(args.position.getX());

                if (!_isSelect) {
                    _isSelect = true;
                    _selectFrom = _cursor_position;
                } else {
                    _selectTo = _cursor_position;
                    makeSelectedArea(); //_selectFrom, _selectTo);
                }
            }
        } finally {
            textInputLock.unlock();
        }
    }

    private void replaceCursorAccordingCoord(int realPos) {
        int w = getTextWidth();
        if (_text_object.getTextAlignment().contains(ItemAlignment.RIGHT) && (w < _cursorXMax))
            realPos -= getX() + (getWidth() - w) - getPadding().right - _text_object.getMargin().right
                    - _cursor.getWidth();
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

    private void onKeyPress(Object sender, KeyArgs args) {
        if (!_isEditable)
            return;
        textInputLock.lock();
        try {

            if (!_isSelect && _justSelected) {
                cancelJustSelected();
            }

            if (!args.mods.contains(KeyMods.NO)) {
                if (args.mods.contains(KeyMods.SHIFT) && args.mods.size() == 1) {
                    if (ShiftValCodes.contains(args.key)) {
                        if (!_isSelect) {
                            _isSelect = true;
                            _selectFrom = _cursor_position;
                        }
                    }
                }

                if (args.mods.contains(CommonService.getOsControlMod()) && args.mods.size() == 1) {
                    if (args.key == KeyCode.A || args.key == KeyCode.a) {
                        selectAll();
                    }
                }
                // alt, super ?
            } else {
                if (args.key == KeyCode.BACKSPACE || args.key == KeyCode.DELETE) {
                    if (_isSelect)
                        cutText();
                    else {
                        if (args.key == KeyCode.BACKSPACE && _cursor_position > 0) // backspace
                        {
                            StringBuilder sb = new StringBuilder(getText());
                            setText(sb.deleteCharAt(_cursor_position - 1).toString());
                            _cursor_position--;
                            replaceCursor();
                        }
                        if (args.key == KeyCode.DELETE && _cursor_position < getText().length()) // delete
                        {
                            StringBuilder sb = new StringBuilder(getText());
                            setText(sb.deleteCharAt(_cursor_position).toString());
                            replaceCursor();
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
            if (args.key == KeyCode.RIGHT && _cursor_position < getText().length()) // arrow right
            {
                if (!_justSelected) {
                    _cursor_position++;
                    replaceCursor();
                }
            }
            if (args.key == KeyCode.END) // end
            {
                _cursor_position = getText().length();
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
                    makeSelectedArea(); //_selectFrom, _selectTo);
                }
            }
        } finally {
            textInputLock.unlock();
        }
    }

    private int cursorPosToCoord(int cPos, boolean isx) {
        int coord = 0;
        if (_text_object.getLetPosArray() == null)
            return coord;

        if (cPos > 0) {
            coord = _text_object.getLetPosArray().get(cPos - 1);
            if ((getTextWidth() >= _cursorXMax) || !_text_object.getTextAlignment().contains(ItemAlignment.RIGHT)) {
                coord += _cursor.getWidth();
            }
        }

        if (isx) {
            if (getLineXShift() + coord < 0) {
                _text_object.setLineXShift(-coord);
            }
            if (getLineXShift() + coord > _cursorXMax)
                _text_object.setLineXShift(_cursorXMax - coord);
        }

        return getLineXShift() + coord;
    }

    private void replaceCursor() {
        int len = getText().length();

        if (_cursor_position > len) {
            _cursor_position = len;
            // replaceCursor();
        }
        int pos = cursorPosToCoord(_cursor_position, true);

        int w = getTextWidth();

        if (_text_object.getTextAlignment().contains(ItemAlignment.RIGHT) && (w < _cursorXMax)) {
            int xcp = getX() + getWidth() - w + pos - getPadding().right // - _cursor.getWidth()
                    - _text_object.getMargin().right - _cursor.getWidth();
            if (_cursor_position == 0)
                xcp -= _cursor.getWidth();
            _cursor.setX(xcp);
        } else {
            int cnt = getX() + getPadding().left + pos + _text_object.getMargin().left;
            // if (_cursor_position > 0)
            // cnt += _cursor.getWidth();
            _cursor.setX(cnt);
        }
    }

    private void onTextInput(InterfaceItem sender, TextInputArgs args) {
        if (!_isEditable)
            return;
        textInputLock.lock();
        try {
            byte[] input = ByteBuffer.allocate(4).putInt(args.character).array(); // BitConverter.getBytes(args.character);
            String str = new String(input, Charset.forName("UTF-32"));// Charset.forName("UTF-32LE"));
            // //Encoding.UTF32.getString(input);

            if (_isSelect || _justSelected) {
                unselectText();
                cutText();
            }
            if (_justSelected)
                cancelJustSelected(); // _justSelected = false;

            StringBuilder sb = new StringBuilder(getText());
            setText(sb.insert(_cursor_position, str).toString());

            _cursor_position++;
            replaceCursor();
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

    void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(Arrays.asList(alignment));
    }

    void setTextAlignment(List<ItemAlignment> alignment) {
        // Ignore all changes
        List<ItemAlignment> ial = new LinkedList<>();
        if (alignment.contains(ItemAlignment.RIGHT)) {
            ial.add(ItemAlignment.RIGHT);
            ial.add(ItemAlignment.VCENTER);
        } else {
            ial.add(ItemAlignment.LEFT);
            ial.add(ItemAlignment.VCENTER);
        }
        _text_object.setTextAlignment(ial);
        _substrate_text.setTextAlignment(ial);
    }

    void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
        _substrate_text.setMargin(margin);
    }

    void setFont(Font font) {
        _text_object.setFont(font);
        _substrate_text.setFont(
            GraphicsMathService.changeFontFamily(font.getFamily(), _substrate_text.getFont()));
    }

    void setFontSize(int size) {
        _text_object.setFontSize(size);
    }

    void setFontStyle(int style) {
        _text_object.setFontStyle(style);
    }

    void setFontFamily(String font_family) {
        _text_object.setFontFamily(font_family);
        _substrate_text.setFontFamily(font_family);
    }

    Font getFont() {
        return _text_object.getFont();
    }

    private boolean _needShow = false;

    void showPassword(boolean _isHidden) {
        if (_isHidden == _needShow)
            return;
        this._needShow = _isHidden;
        setText(_pwd);
        makeSelectedArea(); //_selectFrom, _selectTo);
        replaceCursor();
        getHandler().setFocusedItem(this);
    }

    private void setText(String text) {
        textInputLock.lock();
        try {
            if (_substrate_text.isVisible())
                _substrate_text.setVisible(false);
            if (text == null || text.equals(""))
                _substrate_text.setVisible(true);

            _pwd = text;
            if (_needShow) {
                _text_object.setItemText(text);
            } else {
                StringBuilder txt = new StringBuilder();
                for (char item : text.toCharArray()) {
                    txt.append(_hide_sign);
                }
                _text_object.setItemText(txt.toString());
            }

            _text_object.checkXShift(_cursorXMax);
        } finally {
            textInputLock.unlock();
        }
    }

    private String getText() {
        return _pwd;
    }

    String getPassword() {
        return _pwd;
    }

    void setForeground(Color color) {
        _text_object.setForeground(color);
    }

    void setForeground(int r, int g, int b) {
        _text_object.setForeground(r, g, b);
    }

    void setForeground(int r, int g, int b, int a) {
        _text_object.setForeground(r, g, b, a);
    }

    void setForeground(float r, float g, float b) {
        _text_object.setForeground(r, g, b);
    }

    void setForeground(float r, float g, float b, float a) {
        _text_object.setForeground(r, g, b, a);
    }

    Color getForeground() {
        return _text_object.getForeground();
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
    public void setWidth(int width) {
        super.setWidth(width);
        _cursorXMax = getWidth() - _cursor.getWidth() - getPadding().left - getPadding().right
                - _text_object.getMargin().left - _text_object.getMargin().right; // _cursorXMin;// ;
        _text_object.setAllowWidth(_cursorXMax);
        _text_object.checkXShift(_cursorXMax); // _text_object.setLineXShift();

        _substrate_text.setAllowWidth(_cursorXMax);
        _substrate_text.checkXShift(_cursorXMax);

        replaceCursor();
        if (_text_object.getTextAlignment().contains(ItemAlignment.RIGHT))
            makeSelectedArea(); //_selectFrom, _selectTo);
    }

    public void initElements() {
        // adding
        addItems(_substrate_text, _selectedArea, _text_object, _cursor);
        // getHandler().setFocusedItem(this);

        // _cursorXMin = getPadding().Left;
        // _cursorXMax = getWidth() - _cursor.getWidth() - getPadding().left -
        // getPadding().right
        // - _text_object.getMargin().left - _text_object.getMargin().right; //
        // _cursorXMin;// ;
        // _text_object.setAllowWidth(_cursorXMax);
        // _text_object.setLineXShift();

        // _substrate_text.setAllowWidth(_cursorXMax);
        // _substrate_text.setLineXShift();

        _text_object.setCursorWidth(_cursor.getWidth());
        _substrate_text.setCursorWidth(_cursor.getWidth());
    }

    int getTextWidth() {
        return _text_object.getWidth();
    }

    int getTextHeight() {
        return _text_object.getHeight();
    }

    private void makeSelectedArea() {
        makeSelectedArea(_selectFrom, _selectTo);
    }

    private void makeSelectedArea(int fromPt, int toPt) {
        if (fromPt == -1)
            fromPt = 0;
        if (toPt == -1)
            toPt = 0;
        fromPt = cursorPosToCoord(fromPt, false);
        toPt = cursorPosToCoord(toPt, false);

        if (fromPt == toPt) {
            _selectedArea.setWidth(0);
            return;
        }
        int fromReal = Math.min(fromPt, toPt);
        int toReal = Math.max(fromPt, toPt);

        if (fromReal < 0)
            fromReal = 0;
        if (toReal > _cursorXMax)
            toReal = _cursorXMax;

        int width = toReal - fromReal + 1;

        int w = getTextWidth();
        if (_text_object.getTextAlignment().contains(ItemAlignment.RIGHT) && (w < _cursorXMax))
            _selectedArea.setX(getX() + getWidth() - w + fromReal - getPadding().right - _text_object.getMargin().right
                    - _cursor.getWidth());
        else
            _selectedArea.setX(getX() + getPadding().left + fromReal + _text_object.getMargin().left);
        _selectedArea.setWidth(width);
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

    private String cutText() // ������ �� ����������, ������, �����������, ��� �����
    {
        if (!_isEditable)
            return "";
        textInputLock.lock();
        try {
            if (_selectFrom == -1)
                _selectFrom = 0;
            if (_selectTo == -1)
                _selectTo = 0;
            String str = getSelectedText();
            if (_selectFrom == _selectTo)
                return str;
            int fromReal = Math.min(_selectFrom, _selectTo);
            int toReal = Math.max(_selectFrom, _selectTo);
            StringBuilder sb = new StringBuilder(getText());
            setText(sb.delete(fromReal, toReal).toString()); // - fromReal
            _cursor_position = fromReal;
            replaceCursor();
            if (_isSelect)
                unselectText();
            cancelJustSelected(); // _justSelected = false;
            return str;
        } finally {
            textInputLock.unlock();
        }
    }

    private String getSelectedText() // ������ �� ����������, ������, �����������, ��� �����
    {
        textInputLock.lock();
        try {
            if (_selectFrom == -1)
                _selectFrom = 0;
            if (_selectTo == -1)
                _selectTo = 0;
            if (_selectFrom == _selectTo)
                return "";
            String text = getText();
            int fromReal = Math.min(_selectFrom, _selectTo);
            int toReal = Math.max(_selectFrom, _selectTo);
            String selectedText = text.substring(fromReal, toReal); // - fromReal
            return selectedText;
        } finally {
            textInputLock.unlock();
        }
    }

    @Override
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
        inner_style = style.getInnerStyle("substrate");
        if (inner_style != null) {
            _substrate_text.setFont(inner_style.font);
            _substrate_text.setForeground(inner_style.foreground);
        }
    }

    private void selectAll() {
        _selectFrom = 0;
        _cursor_position = getText().length();
        _selectTo = _cursor_position;
        replaceCursor();

        _isSelect = true;
        makeSelectedArea(); //_selectFrom, _selectTo);
    }

    private int getLineXShift() {
        return _text_object.getLineXShift();
    }

    void setSubstrateText(String substrateText) {
        _substrate_text.setItemText(substrateText);
    }

    void setSubstrateFontSize(int size) {
        _substrate_text.setFontSize(size);
    }

    void setSubstrateFontStyle(int style) {
        _substrate_text.setFontStyle(style);
    }

    void setSubstrateForeground(Color foreground) {
        _substrate_text.setForeground(foreground);
    }

    void setSubstrateForeground(int r, int g, int b) {
        _substrate_text.setForeground(r, g, b);
    }

    void seSubstratetForeground(int r, int g, int b, int a) {
        _substrate_text.setForeground(r, g, b, a);
    }

    void setSubstrateForeground(float r, float g, float b) {
        _substrate_text.setForeground(r, g, b);
    }

    void setSubstrateForeground(float r, float g, float b, float a) {
        _substrate_text.setForeground(r, g, b, a);
    }

    Color getSubstrateForeground() {
        return _substrate_text.getForeground();
    }

    String getSubstrateText() {
        return _substrate_text.getItemText();
    }
}
