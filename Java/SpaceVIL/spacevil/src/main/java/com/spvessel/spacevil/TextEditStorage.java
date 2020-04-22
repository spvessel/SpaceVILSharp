package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TextEditStorage extends Prototype implements InterfaceTextEditable, InterfaceTextShortcuts, InterfaceDraggable {
    static int count = 0;
    private TextLine _textObject;
    private TextLine _substrateText;
    private Rectangle _cursor;
    private int _cursorPosition = 0;
    private Rectangle _selectedArea;
    private boolean _isEditable = true;

    private int _cursorXMax = SpaceVILConstants.sizeMaxValue;

    Rectangle getSelectionArea() {
        return _selectedArea;
    }

    private int _selectFrom = -1;
    private int _selectTo = -1;
    private boolean _isSelect = false;
    private boolean _justSelected = false;

    private List<KeyCode> ShiftValCodes;
    private List<KeyCode> InsteadKeyMods;

    private Lock textInputLock = new ReentrantLock();

    private int scrollStep = 15;

    TextEditStorage() {
        _textObject = new TextLine();
        _textObject.setRecountable(true);
        _cursor = new Rectangle();
        _selectedArea = new Rectangle();

        _substrateText = new TextLine();

        setItemName("TextEditStorage_" + count);
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

        undoQueue = new ArrayDeque<>();
        redoQueue = new ArrayDeque<>();
        undoQueue.addFirst(new TextEditState(getText(), _cursorPosition));

        setCursor(EmbeddedCursor.IBEAM);
    }

    @Override
    protected void setFocused(boolean value) {
        super.setFocused(value);
        if (isFocused() && _isEditable) {
            _cursor.setVisible(true);
        } else {
            _cursor.setVisible(false);
        }
    }

    private void onMouseDoubleClick(Object sender, MouseArgs args) {
        if (args.button == MouseButton.BUTTON_LEFT) {
            selectAll();
        }
    }

    private void onMousePressed(Object sender, MouseArgs args) {
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

    private void onDragging(Object sender, MouseArgs args) {
        textInputLock.lock();
        try {
            if (args.button == MouseButton.BUTTON_LEFT) {
                replaceCursorAccordingCoord(args.position.getX());

                if (!_isSelect) {
                    _isSelect = true;
                    _selectFrom = _cursorPosition;
                } else {
                    _selectTo = _cursorPosition;
                    makeSelectedArea(); //_selectFrom, _selectTo);
                }
            }
        } finally {
            textInputLock.unlock();
        }
    }

    private void onScrollUp(Object sender, MouseArgs args) {
        int w = getTextWidth();

        if (w < _cursorXMax) {
            return;
        }
        int sh = getLineXShift();
        if (sh >= 0) {
            return;
        }

        int curPos = _cursor.getX();
        int curCoord = curPos - sh;

        sh += scrollStep;
        if (sh > 0) {
            sh = 0;
        }

        _textObject.setLineXShift(sh);
        _cursor.setX(curCoord + sh);

        if (_justSelected) {
            cancelJustSelected();
        }
        makeSelectedArea(); //_selectFrom, _selectTo);
    }

    private void onScrollDown(Object sender, MouseArgs args) {
        int w = getTextWidth();

        if (w < _cursorXMax) {
            return;
        }
        int sh = getLineXShift();
        if (w + sh <= _cursorXMax) {
            return;
        }

        int curPos = _cursor.getX();
        int curCoord = curPos - sh;

        sh -= scrollStep;
        if (w + sh < _cursorXMax) {
            sh = _cursorXMax - w;
        }

        _textObject.setLineXShift(sh);
        _cursor.setX(curCoord + sh);

        if (_justSelected) {
            cancelJustSelected();
        }
        makeSelectedArea(); //_selectFrom, _selectTo);
    }

    private void replaceCursorAccordingCoord(int realPos) {
        int w = getTextWidth();
        if (_textObject.getTextAlignment().contains(ItemAlignment.RIGHT) && (w < _cursorXMax)) {
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

    private void onKeyRelease(InterfaceItem sender, KeyArgs args) {

    }

    private void onKeyPress(InterfaceItem sender, KeyArgs args) {
        textInputLock.lock();
        try {
//            if (args == null)
//                return;

            TextShortcutProcessor.processShortcut(this, args);

            if (!_isEditable) {
                return;
            }

            if (!_isSelect && _justSelected) {
                cancelJustSelected();
            }
            if (!args.mods.contains(KeyMods.NO)) {
                // Выделение не сбрасывается, проверяются сочетания
                if (args.mods.contains(KeyMods.SHIFT) && args.mods.size() == 1) {
                    if (ShiftValCodes.contains(args.key)) {
                        if (!_isSelect) {
                            _isSelect = true;
                            _selectFrom = _cursorPosition;
                        }
                    }
                }

                // alt, super ?
            } else {
                if (args.key == KeyCode.BACKSPACE || args.key == KeyCode.DELETE) {
                    if (_isSelect) {
                        privCutText();
                    } else {
                        if (args.key == KeyCode.BACKSPACE && _cursorPosition > 0) // backspace
                        {
                            StringBuilder sb = new StringBuilder(privGetText());
                            _cursorPosition--;
                            privSetText(sb.deleteCharAt(_cursorPosition).toString());
                            // replaceCursor();
                        }
                        if (args.key == KeyCode.DELETE && _cursorPosition < privGetText().length()) // delete
                        {
                            StringBuilder sb = new StringBuilder(privGetText());
                            privSetText(sb.deleteCharAt(_cursorPosition).toString());
                            // replaceCursor();
                        }
                    }
                } else if (_isSelect && !InsteadKeyMods.contains(args.key)) {
                    unselectText();
                    // cancelJustSelected();
                }
            }

            if (args.key == KeyCode.LEFT && _cursorPosition > 0) // arrow left
            {
                if (!_justSelected) {
                    _cursorPosition--;
                    replaceCursor();
                }
            }
            if (args.key == KeyCode.RIGHT && _cursorPosition < privGetText().length()) // arrow right
            {
                if (!_justSelected) {
                    _cursorPosition++;
                    replaceCursor();
                }
            }
            if (args.key == KeyCode.END) // end
            {
                _cursorPosition = privGetText().length();
                replaceCursor();
            }
            if (args.key == KeyCode.HOME) // home
            {
                _cursorPosition = 0;
                replaceCursor();
            }

            if (_isSelect) {
                if (_selectTo != _cursorPosition) {
                    _selectTo = _cursorPosition;
                    makeSelectedArea(); //_selectFrom, _selectTo);
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
            if ((getTextWidth() >= _cursorXMax) || !_textObject.getTextAlignment().contains(ItemAlignment.RIGHT)) {
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
        int len = privGetText().length();

        if (_cursorPosition > len) {
            _cursorPosition = len;
            // replaceCursor();
        }
        int pos = cursorPosToCoord(_cursorPosition, true);

        int w = getTextWidth();

        if (_textObject.getTextAlignment().contains(ItemAlignment.RIGHT) && (w < _cursorXMax)) {
            int xcp = getX() + getWidth() - w + pos - getPadding().right // - _cursor.getWidth()
                    - _textObject.getMargin().right - _cursor.getWidth();
            if (_cursorPosition == 0) {
                xcp -= _cursor.getWidth();
            }
            _cursor.setX(xcp);
        } else {
            int cnt = getX() + getPadding().left + pos + _textObject.getMargin().left;
            // if (_cursorPosition > 0)
            // cnt += _cursor.getWidth();
            _cursor.setX(cnt);
        }
    }

    private void onTextInput(Object sender, TextInputArgs args) {
        if (!_isEditable) {
            return;
        }
        textInputLock.lock();
        try {
            byte[] input = ByteBuffer.allocate(4).putInt(args.character).array();
            String str = new String(input, Charset.forName("UTF-32"));

            if (_isSelect || _justSelected) {
                unselectText();// privCutText();
                privCutText();
            }
            if (_justSelected) {
                cancelJustSelected();
            }

            StringBuilder sb = new StringBuilder(privGetText());
            _cursorPosition++;
            privSetText(sb.insert(_cursorPosition - 1, str).toString());
            // replaceCursor();
        } finally {
            textInputLock.unlock();
        }
    }

    private void privSetText(String text) {
        textInputLock.lock();
        try {
            if (_substrateText.isVisible()) {
                _substrateText.setVisible(false);
            }
            if (text == null || text.equals("")) {
                _substrateText.setVisible(true);
            }
            // _textObject.setLineXShift(_lineXShift, getWidth());
            _textObject.setItemText(text);
            _textObject.checkXShift(_cursorXMax);
            // _textObject.UpdateData(UpdateType.Critical); //Doing in the _textObject

            // _cursorPosition = privGetText().length();
            replaceCursor();

            if (!nothingFlag) {
                redoQueue = new ArrayDeque<>();
            } else {
                nothingFlag = false;
            }
            if (undoQueue.size() > queueCapacity) {
                undoQueue.pollLast();
            }
            undoQueue.addFirst(new TextEditState(getText(), _cursorPosition));
        } finally {
            textInputLock.unlock();
        }
    }

    void setText(String text) {
        if (_isSelect || _justSelected) {
            unselectText();
            cancelJustSelected();
        }
        privSetText(text);
        _cursorPosition = privGetText().length();
        replaceCursor();
    }

    String getText() {
        return privGetText();
    }

    private String privGetText() {
        return _textObject.getItemText();
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
                - _textObject.getMargin().left - _textObject.getMargin().right;
        _textObject.setAllowWidth(_cursorXMax);
        _textObject.checkXShift(_cursorXMax);

        _substrateText.setAllowWidth(_cursorXMax);
        _substrateText.checkXShift(_cursorXMax);

        replaceCursor();
        if (_textObject.getTextAlignment().contains(ItemAlignment.RIGHT)) {
            makeSelectedArea(); //_selectFrom, _selectTo);
        }
    }

    /**
     * Initialization and adding of all elements in the TextEdit
     */
    @Override
    public void initElements() {
        addItems(_substrateText, _selectedArea, _textObject, _cursor);

        int scctp = _textObject.getFontDims().lineSpacer; //[0];
        if (scctp > scrollStep) {
            scrollStep = scctp;
        }

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
        if (_textObject.getTextAlignment().contains(ItemAlignment.RIGHT) && (w < _cursorXMax)) {
            _selectedArea.setX(getX() + getWidth() - w + fromReal - getPadding().right - _textObject.getMargin().right
                    - _cursor.getWidth());
        } else {
            _selectedArea.setX(getX() + getPadding().left + fromReal + _textObject.getMargin().left);
        }
        _selectedArea.setWidth(width);
    }

    private String privGetSelectedText() {
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
            String text = privGetText();
            int fromReal = Math.min(_selectFrom, _selectTo);
            int toReal = Math.max(_selectFrom, _selectTo);
            if (fromReal < 0) {
                return "";
            }
            String selectedText = text.substring(fromReal, toReal); // - fromReal
            return selectedText;
        } finally {
            textInputLock.unlock();
        }
    }

    @Override
    public String getSelectedText() {
        return privGetSelectedText();
    }

    private void privPasteText(String pasteStr) {
        if (!_isEditable) {
            return;
        }
        textInputLock.lock();
        try {
            if (_isSelect) {
                privCutText();
            }

            if ((pasteStr == null) || pasteStr.equals("")) {
                return;
            }

            String text = privGetText();
            String newText = text.substring(0, _cursorPosition) + pasteStr + text.substring(_cursorPosition);
            _cursorPosition += pasteStr.length();
            privSetText(newText);
            // replaceCursor();
        } finally {
            textInputLock.unlock();
        }
    }

    @Override
    public void pasteText(String pasteStr) {
        privPasteText(pasteStr);
    }

    private String privCutText() {
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
            String str = privGetSelectedText();
            if (_selectFrom == _selectTo) {
                return str;
            }
            int fromReal = Math.min(_selectFrom, _selectTo);
            int toReal = Math.max(_selectFrom, _selectTo);
            StringBuilder sb = new StringBuilder(privGetText());
            _cursorPosition = fromReal;
            privSetText(sb.delete(fromReal, toReal).toString()); // - fromReal
            replaceCursor();
            if (_isSelect) {
                unselectText();
            }
            cancelJustSelected(); // _justSelected = false;
            return str;
        } finally {
            textInputLock.unlock();
        }
    }

    @Override
    public String cutText() {
        return privCutText();
    }

    private void unselectText() {
        _isSelect = false;
        _justSelected = true;
        makeSelectedArea(_cursorPosition, _cursorPosition);
    }

    private void cancelJustSelected() {
        _selectFrom = -1;// 0;
        _selectTo = -1;// 0;
        _justSelected = false;
    }

    @Override
    public void clear() {
        setText("");
    }

    private int getLineXShift() {
        return _textObject.getLineXShift();
    }

    boolean isBeginning() {
        return (_cursorPosition == 0);
    }

    @Override
    public final void selectAll() {
        textInputLock.lock();
        try {
            _selectFrom = 0;
            _cursorPosition = privGetText().length();
            _selectTo = _cursorPosition;
            replaceCursor();
            _isSelect = true;
            makeSelectedArea(); //_selectFrom, _selectTo);
        } finally {
            textInputLock.unlock();
        }
    }

    private int queueCapacity = SpaceVILConstants.textUndoCapacity;
    private boolean nothingFlag = false;

    @Override
    public void undo() {
        undoAction();
    }

    private ArrayDeque<TextEditState> undoQueue;

    private void undoAction() {
        if (undoQueue.size() == 1) {
            return;
        }

        TextEditState tmpText = undoQueue.pollFirst();
        if (tmpText != null) {
            if (redoQueue.size() > queueCapacity) {
                redoQueue.pollLast();
            }
            redoQueue.addFirst(new TextEditState(tmpText.textState, tmpText.cursorState));

            tmpText = undoQueue.pollFirst();
            if (tmpText != null) {
                nothingFlag = true;

                privSetText(tmpText.textState);
                _cursorPosition = tmpText.cursorState;
                undoQueue.peekFirst().cursorState = _cursorPosition;
                replaceCursor();
            }
        }
    }

    @Override
    public void redo() {
        redoAction();
    }

    private ArrayDeque<TextEditState> redoQueue;

    private void redoAction() {
        if (redoQueue.size() == 0) {
            return;
        }

        TextEditState tmpText = redoQueue.pollFirst();
        if (tmpText != null) {
            nothingFlag = true;

            privSetText(tmpText.textState);
            _cursorPosition = tmpText.cursorState;
            undoQueue.peekFirst().cursorState = _cursorPosition;
            replaceCursor();
        }
    }

    void setSubstrateText(String substrateText) {
        _substrateText.setItemText(substrateText);
        // _substrateText.checkXShift(_cursorXMax);
    }

    String getSubstrateText() {
        return _substrateText.getItemText();
    }

    void appendText(String text) {
        unselectText();
        cancelJustSelected();
        _cursorPosition = privGetText().length();
        pasteText(text);
    }

    private class TextEditState {
        String textState;
        int cursorState;

        TextEditState(String textState, int cursorState) {
            this.textState = textState;
            this.cursorState = cursorState;
        }
    }

    //decorations-------------------------------------------------------------------------------------------------------

    void setTextAlignment(List<ItemAlignment> alignment) {
        List<ItemAlignment> ial = new LinkedList<>();
        if (alignment.contains(ItemAlignment.RIGHT)) {
            ial.add(ItemAlignment.RIGHT);
            ial.add(ItemAlignment.VCENTER);
        } else {
            ial.add(ItemAlignment.LEFT);
            ial.add(ItemAlignment.VCENTER);
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

    Color getSubstrateForeground() {
        return _substrateText.getForeground();
    }

    @Override
    public void setStyle(Style style) {
        if (style == null) {
            return;
        }
        super.setStyle(style);
        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);

        Style innerStyle = style.getInnerStyle("selection");
        System.out.println(getItemName() + " " + (innerStyle == null));
        if (innerStyle != null) {
            _selectedArea.setStyle(innerStyle);
        }
        innerStyle = style.getInnerStyle("cursor");
        if (innerStyle != null) {
            _cursor.setStyle(innerStyle);
        }
        innerStyle = style.getInnerStyle("substrate");
        if (innerStyle != null) {
            _substrateText.setFont(innerStyle.font);
            _substrateText.setForeground(innerStyle.foreground);
        }
    }

}
