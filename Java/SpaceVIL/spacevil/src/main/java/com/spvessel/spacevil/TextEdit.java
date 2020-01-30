package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

public class TextEdit extends Prototype { //implements InterfaceTextEditable, InterfaceTextShortcuts, InterfaceDraggable {
    static int count = 0;
    //    private TextLine _text_object;
    //    private TextLine _substrate_text;
    //    private Rectangle _cursor;
    //    private int _cursor_position = 0;
    //    private Rectangle _selectedArea;
    //    private boolean _isEditable = true;
    //
    //    private int _cursorXMax = SpaceVILConstants.sizeMaxValue;

    private TextEditStorage _textObject;

    /**
     * @return selection area Rectangle for styling
     */
    public Rectangle getSelectionArea() {
        //        return _selectedArea;
        return _textObject.getSelectionArea();
    }

    //    private int _selectFrom = -1;
    //    private int _selectTo = -1;
    //    private boolean _isSelect = false;
    //    private boolean _justSelected = false;
    //
    //    private List<KeyCode> ShiftValCodes;
    //    private List<KeyCode> InsteadKeyMods;
    //
    //    private Lock textInputLock = new ReentrantLock();
    //
    //    private int scrollStep = 15;

    /**
     * Constructs a TextEdit
     */
    public TextEdit() {
        //        _text_object = new TextLine();
        //        _text_object.setRecountable(true);
        //        _cursor = new Rectangle();
        //        _selectedArea = new Rectangle();
        //
        //        _substrate_text = new TextLine();

        _textObject = new TextEditStorage();

        setItemName("TextEdit_" + count);
        count++;

        //        eventMousePress.add(this::onMousePressed);
        //        eventMouseDrag.add(this::onDragging);
        //        eventKeyPress.add(this::onKeyPress);
        //        eventKeyRelease.add(this::onKeyRelease);
        //        eventTextInput.add(this::onTextInput);
        //        eventScrollUp.add(this::onScrollUp);
        //        eventScrollDown.add(this::onScrollDown);
        //        eventMouseDoubleClick.add(this::onMouseDoubleClick);
        //
        //        ShiftValCodes = new LinkedList<>(Arrays.asList(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.END, KeyCode.HOME));
        //        InsteadKeyMods = new LinkedList<>(Arrays.asList(KeyCode.LEFTSHIFT, KeyCode.RIGHTSHIFT, KeyCode.LEFTCONTROL,
        //                KeyCode.RIGHTCONTROL, KeyCode.LEFTALT, KeyCode.RIGHTALT, KeyCode.LEFTSUPER, KeyCode.RIGHTSUPER));

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.TextEdit"));
        setStyle(DefaultsService.getDefaultStyle(TextEdit.class));
        // _substrate_text.setFontStyle(Font.ITALIC);

        //        undoQueue = new ArrayDeque<>();
        //        redoQueue = new ArrayDeque<>();
        //        undoQueue.addFirst(new TextEditState(getText(), _cursor_position));
        //
        //        setCursor(EmbeddedCursor.IBEAM);
    }

    public TextEdit(String text) {
        this();
        setText(text);
    }

    //    private void onMouseDoubleClick(Object sender, MouseArgs args) {
    //        if (args.button == MouseButton.BUTTON_LEFT) {
    //            selectAll();
    //        }
    //    }
    //
    //    private void onMousePressed(Object sender, MouseArgs args) {
    //        textInputLock.lock();
    //        try {
    //            if (args.button == MouseButton.BUTTON_LEFT) {
    //                replaceCursorAccordingCoord(args.position.getX());
    //                if (_isSelect) {
    //                    unselectText();
    //                    cancelJustSelected();
    //                }
    //            }
    //        } finally {
    //            textInputLock.unlock();
    //        }
    //    }
    //
    //    private void onDragging(Object sender, MouseArgs args) {
    //        textInputLock.lock();
    //        try {
    //            if (args.button == MouseButton.BUTTON_LEFT) {
    //                replaceCursorAccordingCoord(args.position.getX());
    //
    //                if (!_isSelect) {
    //                    _isSelect = true;
    //                    _selectFrom = _cursor_position;
    //                } else {
    //                    _selectTo = _cursor_position;
    //                    makeSelectedArea(); //_selectFrom, _selectTo);
    //                }
    //            }
    //        } finally {
    //            textInputLock.unlock();
    //        }
    //    }
    //
    //    private void onScrollUp(Object sender, MouseArgs args) {
    //        int w = getTextWidth();
    //
    //        if (w < _cursorXMax) {
    //            return;
    //        }
    //        int sh = getLineXShift();
    //        if (sh >= 0) {
    //            return;
    //        }
    //
    //        int curPos = _cursor.getX();
    //        int curCoord = curPos - sh;
    //
    //        sh += scrollStep;
    //        if (sh > 0) {
    //            sh = 0;
    //        }
    //
    //        _text_object.setLineXShift(sh);
    //        _cursor.setX(curCoord + sh);
    //
    //        if (_justSelected) {
    //            cancelJustSelected();
    //        }
    //        makeSelectedArea(); //_selectFrom, _selectTo);
    //    }
    //
    //    private void onScrollDown(Object sender, MouseArgs args) {
    //        int w = getTextWidth();
    //
    //        if (w < _cursorXMax) {
    //            return;
    //        }
    //        int sh = getLineXShift();
    //        if (w + sh <= _cursorXMax) {
    //            return;
    //        }
    //
    //        int curPos = _cursor.getX();
    //        int curCoord = curPos - sh;
    //
    //        sh -= scrollStep;
    //        if (w + sh < _cursorXMax) {
    //            sh = _cursorXMax - w;
    //        }
    //
    //        _text_object.setLineXShift(sh);
    //        _cursor.setX(curCoord + sh);
    //
    //        if (_justSelected) {
    //            cancelJustSelected();
    //        }
    //        makeSelectedArea(); //_selectFrom, _selectTo);
    //    }
    //
    //    private void replaceCursorAccordingCoord(int realPos) {
    //        int w = getTextWidth();
    //        if (_text_object.getTextAlignment().contains(ItemAlignment.RIGHT) && (w < _cursorXMax)) {
    //            realPos -= getX() + (getWidth() - w) - getPadding().right - _text_object.getMargin().right
    //                    - _cursor.getWidth();
    //        } else {
    //            realPos -= getX() + getPadding().left + _text_object.getMargin().left;
    //        }
    //
    //        _cursor_position = coordXToPos(realPos);
    //        replaceCursor();
    //    }
    //
    //    private int coordXToPos(int coordX) {
    //        int pos = 0;
    //
    //        List<Integer> lineLetPos = _text_object.getLetPosArray();
    //        if (lineLetPos == null) {
    //            return pos;
    //        }
    //
    //        for (int i = 0; i < lineLetPos.size(); i++) {
    //            if (lineLetPos.get(i) + getLineXShift() <= coordX + 3) {
    //                pos = i + 1;
    //            } else {
    //                break;
    //            }
    //        }
    //
    //        return pos;
    //    }
    //
    //    private void onKeyRelease(InterfaceItem sender, KeyArgs args) {
    //
    //    }
    //
    //    private void onKeyPress(InterfaceItem sender, KeyArgs args) {
    //        textInputLock.lock();
    //        try {
    ////            if (args == null)
    ////                return;
    //
    //            TextShortcutProcessor.processShortcut(this, args);
    //
    //            if (!_isEditable) {
    //                return;
    //            }
    //
    //            if (!_isSelect && _justSelected) {
    //                cancelJustSelected();
    //            }
    //            if (!args.mods.contains(KeyMods.NO)) {
    //                // Выделение не сбрасывается, проверяются сочетания
    //                if (args.mods.contains(KeyMods.SHIFT) && args.mods.size() == 1) {
    //                    if (ShiftValCodes.contains(args.key)) {
    //                        if (!_isSelect) {
    //                            _isSelect = true;
    //                            _selectFrom = _cursor_position;
    //                        }
    //                    }
    //                }
    //
    //                // alt, super ?
    //            } else {
    //                if (args.key == KeyCode.BACKSPACE || args.key == KeyCode.DELETE) {
    //                    if (_isSelect) {
    //                        privCutText();
    //                    } else {
    //                        if (args.key == KeyCode.BACKSPACE && _cursor_position > 0) // backspace
    //                        {
    //                            StringBuilder sb = new StringBuilder(privGetText());
    //                            _cursor_position--;
    //                            privSetText(sb.deleteCharAt(_cursor_position).toString());
    //                            // replaceCursor();
    //                        }
    //                        if (args.key == KeyCode.DELETE && _cursor_position < privGetText().length()) // delete
    //                        {
    //                            StringBuilder sb = new StringBuilder(privGetText());
    //                            privSetText(sb.deleteCharAt(_cursor_position).toString());
    //                            // replaceCursor();
    //                        }
    //                    }
    //                } else if (_isSelect && !InsteadKeyMods.contains(args.key)) {
    //                    unselectText();
    //                    // cancelJustSelected();
    //                }
    //            }
    //
    //            if (args.key == KeyCode.LEFT && _cursor_position > 0) // arrow left
    //            {
    //                if (!_justSelected) {
    //                    _cursor_position--;
    //                    replaceCursor();
    //                }
    //            }
    //            if (args.key == KeyCode.RIGHT && _cursor_position < privGetText().length()) // arrow right
    //            {
    //                if (!_justSelected) {
    //                    _cursor_position++;
    //                    replaceCursor();
    //                }
    //            }
    //            if (args.key == KeyCode.END) // end
    //            {
    //                _cursor_position = privGetText().length();
    //                replaceCursor();
    //            }
    //            if (args.key == KeyCode.HOME) // home
    //            {
    //                _cursor_position = 0;
    //                replaceCursor();
    //            }
    //
    //            if (_isSelect) {
    //                if (_selectTo != _cursor_position) {
    //                    _selectTo = _cursor_position;
    //                    makeSelectedArea(); //_selectFrom, _selectTo);
    //                }
    //            }
    //        } finally {
    //            textInputLock.unlock();
    //        }
    //    }
    //
    //    private int cursorPosToCoord(int cPos, boolean isx) {
    //        int coord = 0;
    //        if (_text_object.getLetPosArray() == null) {
    //            return coord;
    //        }
    //
    //        if (cPos > 0) {
    //            coord = _text_object.getLetPosArray().get(cPos - 1);
    //            if ((getTextWidth() >= _cursorXMax) || !_text_object.getTextAlignment().contains(ItemAlignment.RIGHT)) {
    //                coord += _cursor.getWidth();
    //            }
    //        }
    //
    //        if (isx) {
    //            if (getLineXShift() + coord < 0) {
    //                _text_object.setLineXShift(-coord);
    //            }
    //            if (getLineXShift() + coord > _cursorXMax) {
    //                _text_object.setLineXShift(_cursorXMax - coord);
    //            }
    //        }
    //
    //        return getLineXShift() + coord;
    //    }
    //
    //    private void replaceCursor() {
    //        int len = privGetText().length();
    //
    //        if (_cursor_position > len) {
    //            _cursor_position = len;
    //            // replaceCursor();
    //        }
    //        int pos = cursorPosToCoord(_cursor_position, true);
    //
    //        int w = getTextWidth();
    //
    //        if (_text_object.getTextAlignment().contains(ItemAlignment.RIGHT) && (w < _cursorXMax)) {
    //            int xcp = getX() + getWidth() - w + pos - getPadding().right // - _cursor.getWidth()
    //                    - _text_object.getMargin().right - _cursor.getWidth();
    //            if (_cursor_position == 0) {
    //                xcp -= _cursor.getWidth();
    //            }
    //            _cursor.setX(xcp);
    //        } else {
    //            int cnt = getX() + getPadding().left + pos + _text_object.getMargin().left;
    //            // if (_cursor_position > 0)
    //            // cnt += _cursor.getWidth();
    //            _cursor.setX(cnt);
    //        }
    //    }
    //
    //    private void onTextInput(Object sender, TextInputArgs args) {
    //        if (!_isEditable) {
    //            return;
    //        }
    //        textInputLock.lock();
    //        try {
    //            byte[] input = ByteBuffer.allocate(4).putInt(args.character).array();
    //            String str = new String(input, Charset.forName("UTF-32"));
    //
    //            if (_isSelect || _justSelected) {
    //                unselectText();// privCutText();
    //                privCutText();
    //            }
    //            if (_justSelected) {
    //                cancelJustSelected();
    //            }
    //
    //            StringBuilder sb = new StringBuilder(privGetText());
    //            _cursor_position++;
    //            privSetText(sb.insert(_cursor_position - 1, str).toString());
    //            // replaceCursor();
    //        } finally {
    //            textInputLock.unlock();
    //        }
    //    }

    /**
     * Set TextEdit focused/unfocused
     */
    @Override
    public void setFocused(boolean value) {
        _textObject.setFocused(value);
        //        super.setFocused(value);
        //        if (isFocused() && _isEditable) {
        //            _cursor.setVisible(true);
        //        } else {
        //            _cursor.setVisible(false);
        //        }
    }

    /**
     * Text alignment in the TextEdit
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(Arrays.asList(alignment));
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textObject.setTextAlignment(alignment);
        //        List<ItemAlignment> ial = new LinkedList<>();
        //        if (alignment.contains(ItemAlignment.RIGHT)) {
        //            ial.add(ItemAlignment.RIGHT);
        //            ial.add(ItemAlignment.VCENTER);
        //        } else {
        //            ial.add(ItemAlignment.LEFT);
        //            ial.add(ItemAlignment.VCENTER);
        //        }
        //        _text_object.setTextAlignment(ial);
        //        _substrate_text.setTextAlignment(ial);
    }

    public List<ItemAlignment> getTextAlignment() {
        return _textObject.getTextAlignment();
    }

    /**
     * Text margin in the TextEdit
     */
    public void setTextMargin(Indents margin) {
        //        _text_object.setMargin(margin);
        //        _substrate_text.setMargin(margin);
        _textObject.setTextMargin(margin);
    }

    public Indents getTextMargin() {
        //        return _text_object.getMargin();
        return _textObject.getTextMargin();
    }

    /**
     * Text font in the TextEdit
     */
    public void setFont(Font font) {
        //        _text_object.setFont(font);
        //        _substrate_text.setFont(
        //            GraphicsMathService.changeFontFamily(font.getFamily(), _substrate_text.getFont())); //new Font(font.getFamily(), _substrate_text.getFont().getStyle(), _substrate_text.getFont().getSize()));
        _textObject.setFont(font);
    }

    public void setFontSize(int size) {
        //_text_object.setFontSize(size);
        _textObject.setFontSize(size);
    }

    public void setFontStyle(int style) {
        //_text_object.setFontStyle(style);
        _textObject.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        //        _text_object.setFontFamily(font_family);
        //        _substrate_text.setFontFamily(font_family);
        _textObject.setFontFamily(font_family);
    }

    public Font getFont() {
        //return _text_object.getFont();
        return _textObject.getFont();
    }

    //    private void privSetText(String text) {
    //        textInputLock.lock();
    //        try {
    //            if (_substrate_text.isVisible()) {
    //                _substrate_text.setVisible(false);
    //            }
    //            if (text == null || text.equals("")) {
    //                _substrate_text.setVisible(true);
    //            }
    //            // _text_object.setLineXShift(_lineXShift, getWidth());
    //            _text_object.setItemText(text);
    //            _text_object.checkXShift(_cursorXMax);
    //            // _text_object.UpdateData(UpdateType.Critical); //Doing in the _text_object
    //
    //            // _cursor_position = privGetText().length();
    //            replaceCursor();
    //
    //            if (!nothingFlag) {
    //                redoQueue = new ArrayDeque<>();
    //            } else {
    //                nothingFlag = false;
    //            }
    //            if (undoQueue.size() > queueCapacity) {
    //                undoQueue.pollLast();
    //            }
    //            undoQueue.addFirst(new TextEditState(getText(), _cursor_position));
    //        } finally {
    //            textInputLock.unlock();
    //        }
    //    }

    /**
     * Text in the TextEdit
     */
    public void setText(String text) {
        //        if (_isSelect || _justSelected) {
        //            unselectText();
        //            cancelJustSelected();
        //        }
        //        privSetText(text);
        //        _cursor_position = privGetText().length();
        //        replaceCursor();
        _textObject.setText(text);
    }

    public String getText() {
        //        return privGetText();
        return _textObject.getText();
    }

    //    private String privGetText() {
    //        return _text_object.getItemText();
    //    }

    /**
     * Text color in the TextEdit
     */
    public void setForeground(Color color) {
        //        _text_object.setForeground(color);
        _textObject.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        //        _text_object.setForeground(r, g, b);
        setForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    public void setForeground(int r, int g, int b, int a) {
        //        _text_object.setForeground(r, g, b, a);
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    public void setForeground(float r, float g, float b) {
        //        _text_object.setForeground(r, g, b);
        setForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    public void setForeground(float r, float g, float b, float a) {
        //        _text_object.setForeground(r, g, b, a);
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    public Color getForeground() {
        //        return _text_object.getForeground();
        return _textObject.getForeground();
    }

    /**
     * Returns if TextEdit editable or not
     */
    public boolean isEditable() {
        //        return _isEditable;
        return _textObject.isEditable();
    }

    /**
     * Set TextEdit editable true or false
     */
    public void setEditable(boolean value) {
        _textObject.setEditable(value);
        //        if (_isEditable == value) {
        //            return;
        //        }
        //        _isEditable = value;
        //
        //        if (_isEditable) {
        //            _cursor.setVisible(true);
        //        } else {
        //            _cursor.setVisible(false);
        //        }
    }

    //    /**
    //     * Set width of the TextEdit
    //     */
    //    @Override
    //    public void setWidth(int width) {
    //        super.setWidth(width);
    //        _cursorXMax = getWidth() - _cursor.getWidth() - getPadding().left - getPadding().right
    //                - _text_object.getMargin().left - _text_object.getMargin().right;
    //        _text_object.setAllowWidth(_cursorXMax);
    //        _text_object.checkXShift(_cursorXMax);
    //
    //        _substrate_text.setAllowWidth(_cursorXMax);
    //        _substrate_text.checkXShift(_cursorXMax);
    //
    //        replaceCursor();
    //        if (_text_object.getTextAlignment().contains(ItemAlignment.RIGHT)) {
    //            makeSelectedArea(); //_selectFrom, _selectTo);
    //        }
    //    }
    //
    /**
     * Initialization and adding of all elements in the TextEdit
     */
    @Override
    public void initElements() {
        //        addItems(_substrate_text, _selectedArea, _text_object, _cursor);
        //
        //        // _cursorXMax = getWidth() - _cursor.getWidth() - getPadding().left -
        //        // getPadding().right
        //        // - _text_object.getMargin().left - _text_object.getMargin().right;
        //        // _text_object.setAllowWidth(_cursorXMax);
        //        // _text_object.setLineXShift();
        //
        //        // _substrate_text.setAllowWidth(_cursorXMax);
        //        // _substrate_text.setLineXShift();
        //
        //        int scctp = _text_object.getFontDims().lineSpacer; //[0];
        //        if (scctp > scrollStep) {
        //            scrollStep = scctp;
        //        }
        //
        //        _text_object.setCursorWidth(_cursor.getWidth());
        //        _substrate_text.setCursorWidth(_cursor.getWidth());
        addItem(_textObject);
    }

    /**
     * Returns width of the whole text in the TextEdit (includes visible and
     * invisible parts of the text)
     */
    public int getTextWidth() {
        //return _text_object.getWidth();
        return _textObject.getWidth();
    }

    /**
     * Returns height of the whole text in the TextEdit (includes visible and
     * invisible parts of the text)
     */
    public int getTextHeight() {
        //return _text_object.getHeight();
        return _textObject.getHeight();
    }

    //    private void makeSelectedArea() {
    //        makeSelectedArea(_selectFrom, _selectTo);
    //    }
    //
    //    private void makeSelectedArea(int fromPt, int toPt) {
    //        if (fromPt == -1) {
    //            fromPt = 0;
    //        }
    //        if (toPt == -1) {
    //            toPt = 0;
    //        }
    //        fromPt = cursorPosToCoord(fromPt, false);
    //        toPt = cursorPosToCoord(toPt, false);
    //
    //        if (fromPt == toPt) {
    //            _selectedArea.setWidth(0);
    //            return;
    //        }
    //        int fromReal = Math.min(fromPt, toPt);
    //        int toReal = Math.max(fromPt, toPt);
    //
    //        if (fromReal < 0) {
    //            fromReal = 0;
    //        }
    //        if (toReal > _cursorXMax) {
    //            toReal = _cursorXMax;
    //        }
    //
    //        int width = toReal - fromReal + 1;
    //
    //        int w = getTextWidth();
    //        if (_text_object.getTextAlignment().contains(ItemAlignment.RIGHT) && (w < _cursorXMax)) {
    //            _selectedArea.setX(getX() + getWidth() - w + fromReal - getPadding().right - _text_object.getMargin().right
    //                    - _cursor.getWidth());
    //        } else {
    //            _selectedArea.setX(getX() + getPadding().left + fromReal + _text_object.getMargin().left);
    //        }
    //        _selectedArea.setWidth(width);
    //    }
    //
    //    private String privGetSelectedText() {
    //        textInputLock.lock();
    //        try {
    //            if (_selectFrom == -1) {
    //                _selectFrom = 0;
    //            }
    //            if (_selectTo == -1) {
    //                _selectTo = 0;
    //            }
    //            if (_selectFrom == _selectTo) {
    //                return "";
    //            }
    //            String text = privGetText();
    //            int fromReal = Math.min(_selectFrom, _selectTo);
    //            int toReal = Math.max(_selectFrom, _selectTo);
    //            if (fromReal < 0) {
    //                return "";
    //            }
    //            String selectedText = text.substring(fromReal, toReal); // - fromReal
    //            return selectedText;
    //        } finally {
    //            textInputLock.unlock();
    //        }
    //    }

    /**
     * @return selected part of the text
     */
    public String getSelectedText() {
        //return privGetSelectedText();
        return _textObject.getSelectedText();
    }

    //    private void privPasteText(String pasteStr) {
    //        if (!_isEditable) {
    //            return;
    //        }
    //        textInputLock.lock();
    //        try {
    //            if (_isSelect) {
    //                privCutText();
    //            }
    //            String text = privGetText();
    //            String newText = text.substring(0, _cursor_position) + pasteStr + text.substring(_cursor_position);
    //            _cursor_position += pasteStr.length();
    //            privSetText(newText);
    //            // replaceCursor();
    //        } finally {
    //            textInputLock.unlock();
    //        }
    //    }

    /**
     * Paste text
     */
    public void pasteText(String pasteStr) {
        //        if (pasteStr != null) {
        //            privPasteText(pasteStr);
        //        }
        _textObject.pasteText(pasteStr);
    }

    //    private String privCutText() {
    //        if (!_isEditable) {
    //            return "";
    //        }
    //        textInputLock.lock();
    //        try {
    //            if (_selectFrom == -1) {
    //                _selectFrom = 0;
    //            }
    //            if (_selectTo == -1) {
    //                _selectTo = 0;
    //            }
    //            String str = privGetSelectedText();
    //            if (_selectFrom == _selectTo) {
    //                return str;
    //            }
    //            int fromReal = Math.min(_selectFrom, _selectTo);
    //            int toReal = Math.max(_selectFrom, _selectTo);
    //            StringBuilder sb = new StringBuilder(privGetText());
    //            _cursor_position = fromReal;
    //            privSetText(sb.delete(fromReal, toReal).toString()); // - fromReal
    //            replaceCursor();
    //            if (_isSelect) {
    //                unselectText();
    //            }
    //            cancelJustSelected(); // _justSelected = false;
    //            return str;
    //        } finally {
    //            textInputLock.unlock();
    //        }
    //    }

    /**
     * Cuts selected part of the text and returns it. Is nothing is selected returns
     * empry string
     */
    public String cutText() {
        //        return privCutText();
        return _textObject.cutText();
    }

    //    private void unselectText() {
    //        _isSelect = false;
    //        _justSelected = true;
    //        makeSelectedArea(_cursor_position, _cursor_position);
    //    }
    //
    //    private void cancelJustSelected() {
    //        _selectFrom = -1;// 0;
    //        _selectTo = -1;// 0;
    //        _justSelected = false;
    //    }

    /**
     * Remove all text from the TextEdit
     */
    @Override
    public void clear() {
        //        setText("");
        _textObject.clear();
    }

    /**
     * Set style of the TextEdit
     */
    @Override
    public void setStyle(Style style) {
        super.setStyle(style);
        Style inner_style = style.getInnerStyle("text");
        if (inner_style != null) {
            _textObject.setStyle(inner_style);
        }
        //        if (style == null) {
        //            return;
        //        }
        //        super.setStyle(style);
        //        setForeground(style.foreground);
        //        setFont(style.font);
        //        setTextAlignment(style.textAlignment);
        //
        //        Style inner_style = style.getInnerStyle("selection");
        //        if (inner_style != null) {
        //            _selectedArea.setStyle(inner_style);
        //        }
        //        inner_style = style.getInnerStyle("cursor");
        //        if (inner_style != null) {
        //            _cursor.setStyle(inner_style);
        //        }
        //        inner_style = style.getInnerStyle("substrate");
        //        if (inner_style != null) {
        //            _substrate_text.setFont(inner_style.font);
        //            _substrate_text.setForeground(inner_style.foreground);
        //        }
    }

    //    private int getLineXShift() {
    //        return _text_object.getLineXShift();
    //    }

    boolean isBeginning() {
        //return (_cursor_position == 0);
        return _textObject.isBeginning();
    }

    public final void selectAll() {
        _textObject.selectAll();
        //        textInputLock.lock();
        //        try {
        //            _selectFrom = 0;
        //            _cursor_position = privGetText().length();
        //            _selectTo = _cursor_position;
        //            replaceCursor();
        //            _isSelect = true;
        //            makeSelectedArea(); //_selectFrom, _selectTo);
        //        } finally {
        //            textInputLock.unlock();
        //        }
    }
    //
    //    private int queueCapacity = SpaceVILConstants.textUndoCapacity;
    //    private boolean nothingFlag = false;

    //    /**
    //     * Undo last action
    //     */
    //    public void undo() {
    //        // _text_object.undo();
    //        undoAction();
    //        // replaceCursor();
    //    }

    //    private ArrayDeque<TextEditState> undoQueue;
    //
    //    private void undoAction() {
    //        if (undoQueue.size() == 1) {
    //            return;
    //        }
    //
    //        TextEditState tmpText = undoQueue.pollFirst();
    //        if (tmpText != null) {
    //            if (redoQueue.size() > queueCapacity) {
    //                redoQueue.pollLast();
    //            }
    //            redoQueue.addFirst(new TextEditState(tmpText.textState, tmpText.cursorState));
    //
    //            tmpText = undoQueue.pollFirst();
    //            if (tmpText != null) {
    //                nothingFlag = true;
    //
    //                privSetText(tmpText.textState);
    //                _cursor_position = tmpText.cursorState;
    //                undoQueue.peekFirst().cursorState = _cursor_position;
    //                replaceCursor();
    //            }
    //        }
    //    }

    //    /**
    //     * Redo last undo action
    //     */
    //    public void redo() {
    //        // _text_object.redo();
    //        redoAction();
    //        // replaceCursor();
    //    }

    //    private ArrayDeque<TextEditState> redoQueue;
    //
    //    private void redoAction() {
    //        if (redoQueue.size() == 0) {
    //            return;
    //        }
    //
    //        TextEditState tmpText = redoQueue.pollFirst();
    //        if (tmpText != null) {
    //            nothingFlag = true;
    //
    //            privSetText(tmpText.textState);
    //            _cursor_position = tmpText.cursorState;
    //            undoQueue.peekFirst().cursorState = _cursor_position;
    //            replaceCursor();
    //        }
    //    }

    public void setSubstrateText(String substrateText) {
        //        _substrate_text.setItemText(substrateText);
        _textObject.setSubstrateText(substrateText);
        // _substrate_text.checkXShift(_cursorXMax);
    }

    public void setSubstrateFontSize(int size) {
        //_substrate_text.setFontSize(size);
        _textObject.setSubstrateFontSize(size);
    }

    public void setSubstrateFontStyle(int style) {
        //_substrate_text.setFontStyle(style);
        _textObject.setSubstrateFontStyle(style);
    }

    public void setSubstrateForeground(Color foreground) {
        //_substrate_text.setForeground(foreground);
        _textObject.setSubstrateForeground(foreground);
    }

    public void setSubstrateForeground(int r, int g, int b) {
        //_substrate_text.setForeground(r, g, b);
        setSubstrateForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    public void seSubstratetForeground(int r, int g, int b, int a) {
        //_substrate_text.setForeground(r, g, b, a);
        setSubstrateForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    public void setSubstrateForeground(float r, float g, float b) {
        //_substrate_text.setForeground(r, g, b);
        setSubstrateForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    public void setSubstrateForeground(float r, float g, float b, float a) {
        //        _substrate_text.setForeground(r, g, b, a);
        setSubstrateForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    public Color getSubstrateForeground() {
        //return _substrate_text.getForeground();
        return _textObject.getSubstrateForeground();
    }

    public String getSubstrateText() {
        //return _substrate_text.getItemText();
        return _textObject.getSubstrateText();
    }

    public void appendText(String text) {
        _textObject.appendText(text);
        //        unselectText();
        //        cancelJustSelected();
        //        _cursor_position = privGetText().length();
        //        pasteText(text);
    }

    //    private class TextEditState {
    //        String textState;
    //        int cursorState;
    //
    //        TextEditState() {
    //        }
    //
    //        TextEditState(String textState, int cursorState) {
    //            this.textState = textState;
    //            this.cursorState = cursorState;
    //        }
    //    }
}
