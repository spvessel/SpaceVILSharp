package com.spvessel.Items;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.*;
import com.spvessel.Flags.KeyMods;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PasswordLine extends VisualItem implements InterfaceTextEditable, InterfaceDraggable {
    static int count = 0;

    private ButtonToggle _show_pwd_btn;
    private String _pwd = "";
    private String _hide_sign;
    private TextLine _text_object;
    private Rectangle _cursor;
    private int _cursor_position = 0;
    //private int _saveCurs = 0;
    private Rectangle _selectedArea;
    private boolean _isEditable = true;

    //private int _cursorXMin = 0;
    private int _cursorXMax = int.MaxValue;

    private int _selectFrom = -1;
    private int _selectTo = -1;
    private boolean _isSelect = false;
    private boolean _justSelected = false;

    private final int BackspaceCode = 14;
    private final int DeleteCode = 339;
    private final int LeftArrowCode = 331;
    private final int RightArrowCode = 333;
    private final int EndCode = 335;
    private final int HomeCode = 327;
    private final int ACode = 30;

    private List<Integer> ShiftValCodes;

    public PasswordLine() {
        setItemName("PasswordLine_" + count);

        // _hide_sign = Encoding.UTF32.getString(BitConverter.getBytes(0x23fa)); //big point
        _hide_sign = Encoding.UTF32.getString(BitConverter.getBytes(0x25CF)); //big point
        _text_object = new TextLine();
        _show_pwd_btn = new ButtonToggle();
        _show_pwd_btn.setItemName(getItemName() + "_marker");
        _cursor = new Rectangle();
        _selectedArea = new Rectangle();
        count++;

        EventKeyPress += OnKeyPress;
        EventTextInput += OnTextInput;
        EventMousePressed += OnMousePressed;
        EventMouseDrag += OnDragging;

        ShiftValCodes = new LinkedList<>(Arrays.asList(LeftArrowCode,RightArrowCode,EndCode,HomeCode));

        setStyle(DefaultsService.getDefaultStyle(typeof(SpaceVIL.PasswordLine)));
        _text_object.setTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
    }

    protected void onMousePressed(Object sender, MouseArgs args) {
        //_saveCurs = _cursor_position;
        replaceCursorAccordingCoord(_mouse_ptr.X);
        if (_isSelect)
            unselectText();
    }

    protected void onDragging(Object sender, MouseArgs args) {
        replaceCursorAccordingCoord(_mouse_ptr.X);

        if (!_isSelect) {
            _isSelect = true;
            _selectFrom = _cursor_position;
        } else {
            _selectTo = _cursor_position;
            makeSelectedArea(_selectFrom, _selectTo);
        }
    }

    private void replaceCursorAccordingCoord(int realPos) {
        realPos -= getX() + getPadding().left;

        _cursor_position = coordXToPos(realPos);
        replaceCursor();
    }

    private int coordXToPos(int coordX) {
        int pos = 0;

        List<Integer> lineLetPos = _text_object.getLetPosArray();
        if (lineLetPos == null) return pos;

        for (int i = 0; i < lineLetPos.size(); i++) {
            if (lineLetPos.get(i) + getLineXShift() <= coordX + 3)
                pos = i + 1;
            else break;
        }

        return pos;
    }

    private void showPassword(InterfaceItem sender) {
        setText(_pwd);
        replaceCursor();
        getHandler().setFocusedItem(this);
    }

    protected void onKeyPress(Object sender, KeyArgs args) {
        //Console.WriteLine(_cursor_position);
        if (!_isEditable) return;

        if (!_isSelect && _justSelected) {
            _selectFrom = -1;// 0;
            _selectTo = -1;// 0;
            _justSelected = false;
        }

        if (args.mods != 0) {
            switch (args.mods) {
                case KeyMods.SHIFT:
                    if (ShiftValCodes.contains(args.scancode)) {
                        if (!_isSelect) {
                            _isSelect = true;
                            _selectFrom = _cursor_position;
                        }
                    }

                    break;

                case KeyMods.CONTROL:
                    if (args.scancode == ACode) {
                        _selectFrom = 0;
                        _cursor_position = getText().length();
                        replaceCursor();

                        _isSelect = true;
                    }
                    break;

                //alt, super ?
            }
        } else {
            if (args.scancode == BackspaceCode || args.scancode == DeleteCode) {
                if (_isSelect)
                    cutText();
                else {
                    if (args.scancode == BackspaceCode && _cursor_position > 0)//backspace
                    {
                        StringBuilder sb = new StringBuilder(getText());
                        setText(sb.deleteCharAt(_cursor_position - 1).toString());
                        _cursor_position--;
                        replaceCursor();
                    }
                    if (args.scancode == DeleteCode && _cursor_position < getText().length())//delete
                    {
                        StringBuilder sb = new StringBuilder(getText());
                        setText(sb.deleteCharAt(_cursor_position).toString());
                        replaceCursor();
                    }
                }
            } else if (_isSelect)
                unselectText();
        }

        if (args.scancode == LeftArrowCode && _cursor_position > 0)//arrow left
        {
            if (!_justSelected) {
                _cursor_position--;
                replaceCursor();
            }
        }
        if (args.scancode == RightArrowCode && _cursor_position < getText().length())//arrow right
        {
            if (!_justSelected) {
                _cursor_position++;
                replaceCursor();
            }
        }
        if (args.scancode == EndCode)//end
        {
            _cursor_position = getText().length();
            replaceCursor();
        }
        if (args.scancode == HomeCode)//home
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
    }

    private int cursorPosToCoord(int cPos) {
        int coord = 0;
        if (_text_object.getLetPosArray() == null) return coord;
        int letCount = _text_object.getLetPosArray().size();

        if (cPos > 0)
            coord = _text_object.getLetPosArray().get(cPos - 1);

        if (getLineXShift() + coord < 0) //_cursorXMin)
            _text_object.setLineXShift(-coord); // _lineXShift + _text_object.getLetWidth(_cursor_position));
        if (getLineXShift() + coord > _cursorXMax)
            _text_object.setLineXShift(_cursorXMax - coord); // _lineXShift - _text_object.getLetWidth(_cursor_position - 1));

        return getLineXShift() + coord;
    }

    private void replaceCursor() {
        int pos = cursorPosToCoord(_cursor_position);
        _cursor.setX(getX() + getPadding().left + pos);
    }

    protected void onTextInput(Object sender, TextInputArgs args) {
        if (!_isEditable) return;
        byte[] input = BitConverter.getBytes(args.Character);
        String str = Encoding.UTF32.getString(input);

        if (_isSelect) unselectText();
        if (_justSelected) cutText();

        StringBuilder sb = new StringBuilder(getText());
        setText(sb.insert(_cursor_position, str).toString());

        _cursor_position++;
        replaceCursor();
    }

    @Override
    public void setFocused(boolean value) {
        super.setFocused(value);
        if (isFocused() && _isEditable)
            _cursor.isVisible = true;
        else
            _cursor.IsVisible = false;
    }


    public void setTextAlignment(ItemAlignment alignment) {
        //Ignore all changes
        //_text_object.setTextAlignment(alignment);
    }

    public void setFont(Font font) {
        _text_object.setFont(font);
    }

    public Font getFont() {
        return _text_object.getFont();
    }

    private void setText(String text) {
        _pwd = text;
        if (_show_pwd_btn.IsToggled) {
            //setText(text);
            _text_object.setItemText(text);
        } else {
            StringBuilder txt = new StringBuilder();
            foreach(var item in text)
            {
                txt.Append(_hide_sign);
            }
            _text_object.setItemText(txt.ToString());
            //setText(txt.ToString());
        }

        //_text_object.setItemText(text);
        _text_object.CheckXShift(_cursorXMax);
    }

    private String getText() {
        return _pwd;// _text_object.getItemText();
    }

    public String getPassword() {
        return _pwd;
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

    public virtual boolean
    IsEditable

    {
        get {
        return _isEditable;
    }
        set
        {
            if (_isEditable == value)
                return;
            _isEditable = value;

            if (_isEditable)
                _cursor.IsVisible = true;
            else
                _cursor.IsVisible = false;
        }
    }

    public override

    void setWidth(int width) {
        base.setWidth(width);
        _cursorXMax = getWidth() - _cursor.getWidth() - getPadding().Left - getPadding().Right -
                _show_pwd_btn.getWidth(); //_cursorXMin;// ;
        _text_object.setAllowWidth(_cursorXMax);
        _text_object.CheckXShift(_cursorXMax); //_text_object.setLineXShift();
    }

    public override

    void initElements() {
        // _cursor.IsVisible = false;
        //adding
        _show_pwd_btn.IsPassEvents = false;
        _show_pwd_btn.EventToggle += (sender, args) =>showPassword(sender);
        AddItems(_selectedArea, _text_object, _cursor, _show_pwd_btn);
        // getHandler().setFocusedItem(this);

        //_cursorXMin = getPadding().Left;
        _cursorXMax = getWidth() - _cursor.getWidth() - getPadding().Left - getPadding().Right -
                _show_pwd_btn.getWidth(); //_cursorXMin;// ;
        _text_object.setAllowWidth(_cursorXMax);
        _text_object.setLineXShift();
    }

    public int getTextWidth() {
        return _text_object.getWidth();
    }

    public int getTextHeight() {
        return _text_object.getHeight();
    }

    private void makeSelectedArea(int fromPt, int toPt) {
        fromPt = cursorPosToCoord(fromPt);
        toPt = cursorPosToCoord(toPt);

        if (fromPt == toPt) {
            _selectedArea.setWidth(0);
            return;
        }
        int fromReal = Math.Min(fromPt, toPt);
        int toReal = Math.Max(fromPt, toPt);
        int width = toReal - fromReal + 1;
        _selectedArea.setX(getX() + getPadding().Left + fromReal);
        _selectedArea.setWidth(width);
    }

    private void unselectText() {
        _isSelect = false;
        _justSelected = true;
        makeSelectedArea(0, 0);
    }

    private int nearestPosToCursor(double xPos) {
        List<Integer> endPos = _text_object.getLetPosArray();
        int pos = endPos.OrderBy(x = > Math.Abs(x - xPos)).First();
        return pos;
    }

    void setCursorPosition(double newPos) {
        _cursor_position = nearestPosToCursor(newPos);
    }

    private String cutText() //������ �� ����������, ������, �����������, ��� �����
    {
        if (!_isEditable) return "";
        string str = getSelectedText();
        if (_selectFrom == _selectTo) return str;
        int fromReal = Math.Min(_selectFrom, _selectTo);
        int toReal = Math.Max(_selectFrom, _selectTo);
        setText(getText().Remove(fromReal, toReal - fromReal));
        _cursor_position = fromReal;
        replaceCursor();
        unselectText();
        _justSelected = false;
        return str;
    }

    private String getSelectedText() //������ �� ����������, ������, �����������, ��� �����
    {
        if (_selectFrom == _selectTo) return "";
        string text = getText();
        int fromReal = Math.Min(_selectFrom, _selectTo);
        int toReal = Math.Max(_selectFrom, _selectTo);
        string selectedText = text.Substring(fromReal, toReal - fromReal);
        return selectedText;
    }

    public void clear() {
        setText("");
    }

//style
    public override

    void setStyle(Style style) {
        if (style == null)
            return;
        base.setStyle(style);
        setForeground(style.Foreground);
        setFont(style.Font);
        setTextAlignment(style.TextAlignment);

        Style inner_style = style.getInnerStyle("selection");
        if (inner_style != null) {
            _selectedArea.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("cursor");
        if (inner_style != null) {
            _cursor.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("showmarker");
        if (inner_style != null) {
            _show_pwd_btn.setStyle(inner_style);
        }
    }

    private int getLineXShift() {
        return _text_object.getLineXShift();
    }

}
