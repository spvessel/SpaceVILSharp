package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;
import com.spvessel.spacevil.Common.CommonService;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TextEncrypt extends Prototype implements ITextEditable, IDraggable {
    private static int count = 0;

    private String _pwd = "";
    private String _hideSign;
    private TextLine _textObject;
    private TextLine _substrateText;

    private Rectangle _cursor;
    private int _cursorPosition = 0;

    private Rectangle _selectedArea;
    private boolean _isEditable = true;

    private int _cursorXMax = SpaceVILConstants.sizeMaxValue;

    private int _selectFrom = -1;
    private int _selectTo = -1;
    private boolean _isSelect = false;
    private boolean _justSelected = false;

    private List<KeyCode> _cursorControlKeys;
    // private List<KeyCode> InsteadKeyMods;

    private Lock textInputLock = new ReentrantLock();

    TextEncrypt() {
        setItemName("TextEncrypt_" + count);
        count++;

        _hideSign = "\u25CF";

        _textObject = new TextLine();
        _textObject.setRecountable(true);
        _cursor = new Rectangle();
        _selectedArea = new Rectangle();

        _substrateText = new TextLine();

        eventKeyPress.add(this::onKeyPress);
        eventTextInput.add(this::onTextInput);
        eventMousePress.add(this::onMousePressed);
        eventMouseDrag.add(this::onDragging);
        eventMouseDoubleClick.add(this::onMouseDoubleClick);

        _cursorControlKeys = new LinkedList<>(Arrays.asList(KeyCode.Left, KeyCode.Right, KeyCode.End, KeyCode.Home));
        // InsteadKeyMods = new LinkedList<>(Arrays.asList(KeyCode.LEFTSHIFT, KeyCode.RIGHTSHIFT, KeyCode.LEFTCONTROL,
        //         KeyCode.RIGHTCONTROL, KeyCode.LEFTALT, KeyCode.RIGHTALT, KeyCode.LEFTSUPER, KeyCode.RIGHTSUPER));

        // setStyle(DefaultsService.getDefaultStyle(TextEncrypt.class));
        // _textObject.setTextAlignment(new
        // LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER)));

        setCursor(EmbeddedCursor.IBeam);
    }
    
    @Override
    public void setFocused(boolean value) {
        super.setFocused(value);
        if (isFocused() && _isEditable) {
            _cursor.setVisible(true);
        } else {
            _cursor.setVisible(false);
        }
    } 

    private void onMouseDoubleClick(IItem sender, MouseArgs args) {
        textInputLock.lock();
        try {
            if (args.button == MouseButton.ButtonLeft) {
                selectAll();
            }
        } finally {
            textInputLock.unlock();
        }
    }

    private void onMousePressed(IItem sender, MouseArgs args) {
        textInputLock.lock();
        try {
            if (args.button == MouseButton.ButtonLeft) {
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

    private void onDragging(IItem sender, MouseArgs args) {
        textInputLock.lock();
        try {
            if (args.button == MouseButton.ButtonLeft) {
                replaceCursorAccordingCoord(args.position.getX());

                if (!_isSelect) {
                    _isSelect = true;
                    _selectFrom = _cursorPosition;
                } else {
                    _selectTo = _cursorPosition;
                    makeSelectedArea();
                }
            }
        } finally {
            textInputLock.unlock();
        }
    }

    private void replaceCursorAccordingCoord(int realPos) {
        int w = getTextWidth();
        if (_textObject.getTextAlignment().contains(ItemAlignment.Right) && (w < _cursorXMax)) {
            realPos -= getX() + (getWidth() - w) - getPadding().right - _textObject.getMargin().right
                    - _cursor.getWidth();
        } else {
            realPos -= getX() + getPadding().left + _textObject.getMargin().left;
        }

        _cursorPosition = coordXToPos(realPos);
        replaceCursor();
    }

    private int coordXToPos(int coordX) {
        int pos = 0;

        List<Integer> lineLetPos = _textObject.getLetPosArray();
        if (lineLetPos == null) {
            return pos;
        }

        for (int i = 0; i < lineLetPos.size(); i++) {
            if (lineLetPos.get(i) + getLineXShift() <= coordX + 3) {
                pos = i + 1;
            } else {
                break;
            }
        }

        return pos;
    }

    private void onKeyPress(Object sender, KeyArgs args) {
        if (!_isEditable) {
            return;
        }
        textInputLock.lock();
        try {

            if (!_isSelect && _justSelected) {
                cancelJustSelected();
            }

            boolean isCursorControlKey = _cursorControlKeys.contains(args.key);
            boolean hasShift = args.mods.contains(KeyMods.Shift);
            boolean hasControl = args.mods.contains(CommonService.getOsControlMod());

            if (!args.mods.contains(KeyMods.No)) {
                //Only one available shortcut
                if (hasControl && args.mods.size() == 1) {
                    if (args.key == KeyCode.A || args.key == KeyCode.a) {
                        selectAll();
                    }
                }

                if (isCursorControlKey) {
                    if (!_isSelect) {
                        if (hasShift) {
                            if ((args.mods.size() == 1) || ((args.mods.size() == 2) && hasControl)) {
                                _isSelect = true;
                                _selectFrom = _cursorPosition;
                            }
                        }
                    } else { //_isSelect
                        if ((args.mods.size() == 1) && hasControl) {
                            unselectText();
                            cancelJustSelected();
                        }
                    }

                }

                // control + delete/backspace
                if (hasControl && (args.mods.size() == 1)) {
                    if (!_isSelect) {
                        if (args.key == KeyCode.Backspace) { //remove to left
                            _selectFrom = _cursorPosition;
                            _selectTo = 0;
                            cutText();
                        } else if (args.key == KeyCode.Delete) { //remove to right
                            _selectFrom = _cursorPosition;
                            _selectTo = getText().length();
                            cutText();                            
                        }
                    } else if (_isSelect && ((args.key == KeyCode.Backspace) || (args.key == KeyCode.Delete))) {
                        cutText();
                    }
                }

                // alt, super ?
            } else {
                if (args.key == KeyCode.Backspace || args.key == KeyCode.Delete) {
                    if (_isSelect) {
                        cutText();
                    } else {
                        if (args.key == KeyCode.Backspace && _cursorPosition > 0) { // backspace
                            StringBuilder sb = new StringBuilder(getText());
                            setText(sb.deleteCharAt(_cursorPosition - 1).toString());
                            _cursorPosition--;
                            replaceCursor();
                        }
                        if (args.key == KeyCode.Delete && _cursorPosition < getText().length()) { // delete
                            StringBuilder sb = new StringBuilder(getText());
                            setText(sb.deleteCharAt(_cursorPosition).toString());
                            replaceCursor();
                        }
                    }
                } else if (_isSelect) { //??? && !InsteadKeyMods.contains(args.key)) {
                    unselectText();
                }
            }

            if (isCursorControlKey) {
                if (!args.mods.contains(KeyMods.Alt) && !args.mods.contains(KeyMods.Super)) {
                    if (args.key == KeyCode.Left && _cursorPosition > 0) { // arrow left
                        if (hasControl) {
                            _cursorPosition = 0;
                            replaceCursor();
                        } else if (!_justSelected) {
                            _cursorPosition--;
                            replaceCursor();
                        }
                    }
                    if (args.key == KeyCode.Right && _cursorPosition < getText().length()) { // arrow right
                        if (hasControl) {
                            _cursorPosition = getText().length();
                            replaceCursor();
                        }

                        if (!_justSelected) {
                            _cursorPosition++;
                            replaceCursor();
                        }
                    }
                    if (args.key == KeyCode.End) { // end
                        _cursorPosition = getText().length();
                        replaceCursor();
                    }
                    if (args.key == KeyCode.Home) { // home
                        _cursorPosition = 0;
                        replaceCursor();
                    }
                }
            }

            if (_isSelect) {
                if (_selectTo != _cursorPosition) {
                    _selectTo = _cursorPosition;
                    makeSelectedArea();
                }
            }
        } finally {
            textInputLock.unlock();
        }
    }

    private int cursorPosToCoord(int cPos, boolean isx) {
        int coord = 0;
        if (_textObject.getLetPosArray() == null) {
            return coord;
        }

        if (cPos > 0) {
            coord = _textObject.getLetPosArray().get(cPos - 1);
            if ((getTextWidth() >= _cursorXMax) || !_textObject.getTextAlignment().contains(ItemAlignment.Right)) {
                coord += _cursor.getWidth();
            }
        }

        if (isx) {
            if (getLineXShift() + coord < 0) {
                _textObject.setLineXShift(-coord);
            }
            if (getLineXShift() + coord > _cursorXMax) {
                _textObject.setLineXShift(_cursorXMax - coord);
            }
        }

        return getLineXShift() + coord;
    }

    private void replaceCursor() {
        int len = getText().length();

        if (_cursorPosition > len) {
            _cursorPosition = len;
        }
        int pos = cursorPosToCoord(_cursorPosition, true);

        int w = getTextWidth();

        if (_textObject.getTextAlignment().contains(ItemAlignment.Right) && (w < _cursorXMax)) {
            int xcp = getX() + getWidth() - w + pos - getPadding().right // - _cursor.getWidth()
                    - _textObject.getMargin().right - _cursor.getWidth();
            if (_cursorPosition == 0) {
                xcp -= _cursor.getWidth();
            }
            _cursor.setX(xcp);
        } else {
            int cnt = getX() + getPadding().left + pos + _textObject.getMargin().left;
            _cursor.setX(cnt);
        }
    }

    private void onTextInput(IItem sender, TextInputArgs args) {
        if (!_isEditable) {
            return;
        }
        textInputLock.lock();
        try {
            byte[] input = ByteBuffer.allocate(4).putInt(args.character).array();
            String str = new String(input, Charset.forName("UTF-32"));

            if (_isSelect || _justSelected) {
                unselectText();
                cutText();
            }
            if (_justSelected) {
                cancelJustSelected();
            }

            StringBuilder sb = new StringBuilder(getText());
            setText(sb.insert(_cursorPosition, str).toString());

            _cursorPosition++;
            replaceCursor();
        } finally {
            textInputLock.unlock();
        }
    }   

    private boolean _needShow = false;

    void showPassword(boolean _isHidden) {
        if (_isHidden == _needShow) {
            return;
        }
        this._needShow = _isHidden;
        setText(_pwd);
        makeSelectedArea();
        replaceCursor();
        getHandler().setFocusedItem(this);
    }

    private void setText(String text) {
        textInputLock.lock();
        try {
            if (_substrateText.isVisible()) {
                _substrateText.setVisible(false);
            }
            if (text == null || text.equals("")) {
                _substrateText.setVisible(true);
            }

            _pwd = text;
            if (_needShow) {
                _textObject.setItemText(text);
            } else {
                StringBuilder txt = new StringBuilder();
                for (char item : text.toCharArray()) {
                    txt.append(_hideSign);
                }
                _textObject.setItemText(txt.toString());
            }

            _textObject.checkXShift(_cursorXMax);
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

    boolean isEditable() {
        return _isEditable;
    }

    void setEditable(boolean value) {

        if (_isEditable == value) {
            return;
        }
        _isEditable = value;

        if (_isEditable) {
            _cursor.setVisible(true);
        } else {
            _cursor.setVisible(false);
        }
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        _cursorXMax = getWidth() - _cursor.getWidth() - getPadding().left - getPadding().right
                - _textObject.getMargin().left - _textObject.getMargin().right; // _cursorXMin;// ;
        _textObject.setAllowWidth(_cursorXMax);
        _textObject.checkXShift(_cursorXMax); // _textObject.setLineXShift();

        _substrateText.setAllowWidth(_cursorXMax);
        _substrateText.checkXShift(_cursorXMax);

        replaceCursor();
        if (_textObject.getTextAlignment().contains(ItemAlignment.Right)) {
            makeSelectedArea();
        }
    }

    public void initElements() {
        // adding
        addItems(_substrateText, _selectedArea, _textObject, _cursor);

        _textObject.setCursorWidth(_cursor.getWidth());
        _substrateText.setCursorWidth(_cursor.getWidth());
    }

    int getTextWidth() {
        return _textObject.getWidth();
    }

    int getTextHeight() {
        return _textObject.getHeight();
    }

    private void makeSelectedArea() {
        makeSelectedArea(_selectFrom, _selectTo);
    }

    private void makeSelectedArea(int fromPt, int toPt) {
        if (fromPt == -1) {
            fromPt = 0;
        }
        if (toPt == -1) {
            toPt = 0;
        }
        fromPt = cursorPosToCoord(fromPt, false);
        toPt = cursorPosToCoord(toPt, false);

        if (fromPt == toPt) {
            _selectedArea.setWidth(0);
            return;
        }
        int fromReal = Math.min(fromPt, toPt);
        int toReal = Math.max(fromPt, toPt);

        if (fromReal < 0) {
            fromReal = 0;
        }
        if (toReal > _cursorXMax) {
            toReal = _cursorXMax;
        }

        int width = toReal - fromReal + 1;

        int w = getTextWidth();
        if (_textObject.getTextAlignment().contains(ItemAlignment.Right) && (w < _cursorXMax)) {
            _selectedArea.setX(getX() + getWidth() - w + fromReal - getPadding().right - _textObject.getMargin().right
                    - _cursor.getWidth());
        } else {
            _selectedArea.setX(getX() + getPadding().left + fromReal + _textObject.getMargin().left);
        }
        _selectedArea.setWidth(width);
    }

    private void unselectText() {
        _isSelect = false;
        _justSelected = true;
        makeSelectedArea(_cursorPosition, _cursorPosition);
    }

    private void cancelJustSelected() {
        _selectFrom = -1;
        _selectTo = -1;
        _justSelected = false;
    }

    private String cutText()
    {
        if (!_isEditable) {
            return "";
        }
        textInputLock.lock();
        try {
            if (_selectFrom == -1) {
                _selectFrom = 0;
            }
            if (_selectTo == -1) {
                _selectTo = 0;
            }
            String str = getSelectedText();
            if (_selectFrom == _selectTo) {
                return str;
            }
            int fromReal = Math.min(_selectFrom, _selectTo);
            int toReal = Math.max(_selectFrom, _selectTo);
            StringBuilder sb = new StringBuilder(getText());
            setText(sb.delete(fromReal, toReal).toString()); // - fromReal
            _cursorPosition = fromReal;
            replaceCursor();
            if (_isSelect) {
                unselectText();
            }
            cancelJustSelected();
            return str;
        } finally {
            textInputLock.unlock();
        }
    }

    private String getSelectedText()
    {
        textInputLock.lock();
        try {
            if (_selectFrom == -1) {
                _selectFrom = 0;
            }
            if (_selectTo == -1) {
                _selectTo = 0;
            }
            if (_selectFrom == _selectTo) {
                return "";
            }
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

    private void selectAll() {
        _selectFrom = 0;
        _cursorPosition = getText().length();
        _selectTo = _cursorPosition;
        replaceCursor();

        _isSelect = true;
        makeSelectedArea();
    }

    private int getLineXShift() {
        return _textObject.getLineXShift();
    }
    
    void setSubstrateText(String substrateText) {
        _substrateText.setItemText(substrateText);
    }

    //decorations----------------------------------------------------------------------------
    void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(BaseItemStatics.composeFlags(alignment)); //Arrays.asList(alignment));
    }

    void setTextAlignment(List<ItemAlignment> alignment) {
        // Ignore all changes
        List<ItemAlignment> ial = new LinkedList<>();
        if (alignment.contains(ItemAlignment.Right)) {
            ial.add(ItemAlignment.Right);
            ial.add(ItemAlignment.VCenter);
        } else {
            ial.add(ItemAlignment.Left);
            ial.add(ItemAlignment.VCenter);
        }
        _textObject.setTextAlignment(ial);
        _substrateText.setTextAlignment(ial);
    }

    List<ItemAlignment> getTextAlignment() {
        return _textObject.getTextAlignment();
    }

    void setTextMargin(Indents margin) {
        _textObject.setMargin(margin);
        _substrateText.setMargin(margin);
    }

    Indents getTextMargin() {
        return _textObject.getMargin();
    }

    void setFont(Font font) {
        _textObject.setFont(font);
        _substrateText.setFont(
                FontService.changeFontFamily(font.getFamily(), _substrateText.getFont()));
    }

    void setFontSize(int size) {
        _textObject.setFontSize(size);
    }

    void setFontStyle(int style) {
        _textObject.setFontStyle(style);
    }

    void setFontFamily(String font_family) {
        _textObject.setFontFamily(font_family);
        _substrateText.setFontFamily(font_family);
    }

    Font getFont() {
        return _textObject.getFont();
    }
    
    void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    // void setForeground(int r, int g, int b) {
    //     _textObject.setForeground(r, g, b);
    // }

    // void setForeground(int r, int g, int b, int a) {
    //     _textObject.setForeground(r, g, b, a);
    // }

    // void setForeground(float r, float g, float b) {
    //     _textObject.setForeground(r, g, b);
    // }

    // void setForeground(float r, float g, float b, float a) {
    //     _textObject.setForeground(r, g, b, a);
    // }

    Color getForeground() {
        return _textObject.getForeground();
    }

    void setSubstrateFontSize(int size) {
        _substrateText.setFontSize(size);
    }

    void setSubstrateFontStyle(int style) {
        _substrateText.setFontStyle(style);
    }

    void setSubstrateForeground(Color foreground) {
        _substrateText.setForeground(foreground);
    }

    // void setSubstrateForeground(int r, int g, int b) {
    //     _substrateText.setForeground(r, g, b);
    // }

    // void setSubstrateForeground(int r, int g, int b, int a) {
    //     _substrateText.setForeground(r, g, b, a);
    // }

    // void setSubstrateForeground(float r, float g, float b) {
    //     _substrateText.setForeground(r, g, b);
    // }

    // void setSubstrateForeground(float r, float g, float b, float a) {
    //     _substrateText.setForeground(r, g, b, a);
    // }

    Color getSubstrateForeground() {
        return _substrateText.getForeground();
    }

    String getSubstrateText() {
        return _substrateText.getItemText();
    }
    
    // style
    @Override
    public void setStyle(Style style) {
        if (style == null) {
            return;
        }
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
            _substrateText.setFont(inner_style.font);
            _substrateText.setForeground(inner_style.foreground);
        }
    }
}
