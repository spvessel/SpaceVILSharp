using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
namespace SpaceVIL
{
    public class PasswordLine : VisualItem, ITextEditable, IDraggable
    {
        static int count = 0;

        private ButtonToggle _show_pwd_btn;
        private string _pwd = String.Empty;
        private string _hide_sign;
        private TextLine _text_object;
        private Rectangle _cursor;
        private int _cursor_position = 0;
        //private int _saveCurs = 0;
        private Rectangle _selectedArea;

        private int _selectFrom = -1;
        private int _selectTo = -1;
        private bool _isSelect = false;
        private bool _justSelected = false;

        private const int BackspaceCode = 14;
        private const int DeleteCode = 339;
        private const int LeftArrowCode = 331;
        private const int RightArrowCode = 333;
        private const int EndCode = 335;
        private const int HomeCode = 327;
        private const int ACode = 30;

        private List<int> ShiftValCodes;

        public PasswordLine()
        {
            _hide_sign = Encoding.UTF32.GetString(BitConverter.GetBytes(0x23fa)); //big point
            _text_object = new TextLine();
            _show_pwd_btn = new ButtonToggle();

            _cursor = new Rectangle();
            _selectedArea = new Rectangle();
            _selectedArea.SetBackground(111, 181, 255);

            SetItemName("PasswordLine_" + count);
            SetBackground(180, 180, 180);
            SetForeground(Color.Black);
            SetPadding(5, 0, 5, 0);
            count++;
            EventKeyPress += OnKeyPress;
            EventTextInput += OnTextInput;
            EventMousePressed += OnMousePressed;
            EventMouseDrag += OnDragging;

            ShiftValCodes = new List<int>() {LeftArrowCode, RightArrowCode, EndCode,
                HomeCode};
        }

        protected virtual void OnMousePressed(object sender, MouseArgs args)
        {
            //_saveCurs = _cursor_position;
            ReplaceCursorAccordingCoord(_mouse_ptr.X);
            if (_isSelect)
                UnselectText();
        }

        protected virtual void OnDragging(object sender, MouseArgs args)
        {
            ReplaceCursorAccordingCoord(_mouse_ptr.X);

            if (!_isSelect)
            {
                _isSelect = true;
                _selectFrom = _cursor_position;
            }
            else
            {
                _selectTo = _cursor_position;
                MakeSelectedArea(_selectFrom, _selectTo);
            }
        }

        private void ReplaceCursorAccordingCoord(int realPos)
        {
            realPos -= GetX() + GetPadding().Left;

            _cursor_position = CoordXToPos(realPos);
            ReplaceCursor();
        }

        private int CoordXToPos(int coordX)
        {
            int pos = 0;

            List<int> lineLetPos = _text_object.GetLetPosArray();
            if (lineLetPos == null) return pos;

            for (int i = 0; i < lineLetPos.Count; i++)
            {
                if (lineLetPos[i] <= coordX + 3)
                    pos = i + 1;
                else break;
            }

            return pos;
        }

        private void ShowPassword(IItem sender)
        {
            SetText(_pwd);
            ReplaceCursor();
            GetHandler().SetFocusedItem(this);
        }
        protected virtual void OnKeyPress(object sender, KeyArgs args)
        {
            //Console.WriteLine(_cursor_position);
            if (!_isSelect && _justSelected)
            {
                _selectFrom = -1;// 0;
                _selectTo = -1;// 0;
                _justSelected = false;
            }

            if (args.Mods != 0)
            {
                //Выделение не сбрасывается, проверяются сочетания
                switch (args.Mods)
                {
                    case KeyMods.Shift:
                        if (ShiftValCodes.Contains(args.Scancode))
                        {
                            if (!_isSelect)
                            {
                                _isSelect = true;
                                _selectFrom = _cursor_position;
                            }
                        }

                        break;

                    case KeyMods.Control:
                        if (args.Scancode == ACode)
                        {
                            _selectFrom = 0;
                            _cursor_position = GetText().Length;
                            ReplaceCursor();

                            _isSelect = true;
                        }
                        break;

                        //alt, super ?
                }
            }
            else
            {
                if (args.Scancode == BackspaceCode || args.Scancode == DeleteCode)
                {
                    if (_isSelect)
                        CutText();
                    else
                    {
                        if (args.Scancode == BackspaceCode && _cursor_position > 0)//backspace
                        {
                            SetText(GetText().Remove(_cursor_position - 1, 1));
                            _cursor_position--;
                            ReplaceCursor();
                        }
                        if (args.Scancode == DeleteCode && _cursor_position < GetText().Length)//delete
                        {
                            SetText(GetText().Remove(_cursor_position, 1));
                        }
                    }
                }
                else
                    if (_isSelect)
                    UnselectText();
            }

            if (args.Scancode == LeftArrowCode && _cursor_position > 0)//arrow left
            {
                if (!_justSelected) _cursor_position--;
                ReplaceCursor();
            }
            if (args.Scancode == RightArrowCode && _cursor_position < GetText().Length)//arrow right
            {
                if (!_justSelected) _cursor_position++;
                ReplaceCursor();
            }
            if (args.Scancode == EndCode)//end
            {
                _cursor_position = GetText().Length;
                ReplaceCursor();
            }
            if (args.Scancode == HomeCode)//home
            {
                _cursor_position = 0;
                ReplaceCursor();
            }

            if (_isSelect)
            {
                if (_selectTo != _cursor_position)
                {
                    _selectTo = _cursor_position;
                    MakeSelectedArea(_selectFrom, _selectTo);
                }
            }
        }

        private int CursorPosToCoord(int cPos)
        {
            int coord = 0;
            if (_text_object.GetLetPosArray() == null) return coord;
            int letCount = _text_object.GetLetPosArray().Count;

            if (cPos > 0)
                coord = _text_object.GetLetPosArray()[cPos - 1];
            return coord;
        }

        private void ReplaceCursor()
        {
            int pos = CursorPosToCoord(_cursor_position);
            _cursor.SetX(GetX() + GetPadding().Left + pos);
        }

        protected virtual void OnTextInput(object sender, TextInputArgs args)
        {
            byte[] input = BitConverter.GetBytes(args.Character);
            string str = Encoding.UTF32.GetString(input);

            if (_isSelect) UnselectText();
            if (_justSelected) CutText();

            SetText(GetText().Insert(_cursor_position, str));

            _cursor_position++;
            ReplaceCursor();
        }

        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }
        public void SetFont(Font font)
        {
            _text_object.SetFont(font);
        }
        public Font GetFont()
        {
            return _text_object.GetFont();
        }
        private void SetText(String text)
        {
            _pwd = text;
            if (_show_pwd_btn.IsToggled)
            {
                //SetText(text);
                _text_object.SetItemText(text);
            }
            else
            {
                StringBuilder txt = new StringBuilder();
                foreach (var item in text)
                {
                    txt.Append(_hide_sign);
                }
                _text_object.SetItemText(txt.ToString());
                //SetText(txt.ToString());
            }

            //_text_object.SetItemText(text);
        }
        private String GetText()
        {
            return _pwd;// _text_object.GetItemText();
        }
        public String GetPassword()
        {
            return _pwd;
        }
        public void SetForeground(Color color)
        {
            _text_object.SetForeground(color);
        }
        public void SetForeground(int r, int g, int b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        public void SetForeground(float r, float g, float b)
        {
            _text_object.SetForeground(r, g, b);
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            _text_object.SetForeground(r, g, b, a);
        }
        public Color GetForeground()
        {
            return _text_object.GetForeground();
        }

        public override void InitElements()
        {
            //_show_pwd_btn
            _show_pwd_btn = new ButtonToggle();
            _show_pwd_btn.SetItemName(GetItemName() + "_marker");
            _show_pwd_btn.SetBackground(Color.FromArgb(255, 120, 120, 120));
            _show_pwd_btn.SetWidth(16);
            _show_pwd_btn.SetHeight(16);
            _show_pwd_btn.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            _show_pwd_btn.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Right);
            _show_pwd_btn.AddItemState(ItemStateType.Toggled, new ItemState()
            {
                Background = Color.FromArgb(255, 80, 80, 80)
            });
            _show_pwd_btn.Border.Radius = 4;
            _show_pwd_btn.EventToggle += (sender, args) => ShowPassword(sender);
            //text
            _text_object.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);

            //cursor
            _cursor.SetBackground(0, 0, 0);
            _cursor.SetWidth(2);
            _cursor.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand); //???

            //selectedArea
            //_selectedArea.SetMargin(0, 5, 0, 5); //???
            _selectedArea.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);

            //adding
            AddItems(_selectedArea, _text_object, _cursor);
            AddItem(_show_pwd_btn);

            //update text data
            //_text_object.UpdateData(UpdateType.Critical);
            GetHandler().SetFocusedItem(this);
        }

        //style
        public override void SetStyle(Style style)
        {
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
        }

        public int GetTextWidth()
        {
            return _text_object.GetWidth();
        }

        public int GetTextHeight()
        {
            return _text_object.GetHeight();
        }

        private void MakeSelectedArea(int fromPt, int toPt)
        {
            fromPt = CursorPosToCoord(fromPt);
            toPt = CursorPosToCoord(toPt);

            if (fromPt == toPt)
            {
                _selectedArea.SetWidth(0);
                return;
            }
            int fromReal = Math.Min(fromPt, toPt);
            int toReal = Math.Max(fromPt, toPt);
            int width = toReal - fromReal + 1;
            _selectedArea.SetX(GetX() + GetPadding().Left + fromReal);
            _selectedArea.SetWidth(width);
        }

        private void UnselectText()
        {
            _isSelect = false;
            _justSelected = true;
            MakeSelectedArea(0, 0);
        }

        private int NearestPosToCursor(double xPos)
        {
            List<int> endPos = _text_object.GetLetPosArray();
            int pos = endPos.OrderBy(x => Math.Abs(x - xPos)).First();
            return pos;
        }

        internal void SetCursorPosition(double newPos)
        {
            _cursor_position = NearestPosToCursor(newPos);
        }

        private string CutText() //Наружу не передается, пароль, секретность, все такое
        {
            string str = GetSelectedText();
            if (_selectFrom == _selectTo) return str;
            int fromReal = Math.Min(_selectFrom, _selectTo);
            int toReal = Math.Max(_selectFrom, _selectTo);
            SetText(GetText().Remove(fromReal, toReal - fromReal));
            _cursor_position = fromReal;
            ReplaceCursor();
            UnselectText();
            _justSelected = false;
            return str;
        }

        private string GetSelectedText() //Наружу не передается, пароль, секретность, все такое
        {
            if (_selectFrom == _selectTo) return "";
            string text = GetText();
            int fromReal = Math.Min(_selectFrom, _selectTo);
            int toReal = Math.Max(_selectFrom, _selectTo);
            string selectedText = text.Substring(fromReal, toReal - fromReal);
            return selectedText;
        }

        public void Clear()
        {
            SetText("");
        }
    }
}