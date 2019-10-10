using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class Label : Prototype, IVLayout
    {
        static int count = 0;
        private List<TextLine> _text_objects;
        private bool _init = false;

        public bool IsHover = true;

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            if (IsHover)
            {
                return base.GetHoverVerification(xpos, ypos);
            }
            return false;
        }

        /// <summary>
        /// Constructs a Label
        /// </summary>
        public Label()
        {
            SetItemName("Label_" + count);
            count++;
            _text_objects = new List<TextLine>();
            _text_objects.Add(new TextLine());

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.Label)));
            IsFocusable = false;
        }

        /// <summary>
        /// Constructs a Label with text
        /// </summary>
        public Label(String text) : this()
        {
            SetText(text);
        }

        public Label(String text, bool hover) : this(text)
        {
            SetText(text);
            IsHover = hover;
        }

        /// <summary>
        /// Text alignment in the Label
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            foreach (TextLine tl in _text_objects)
            {
                tl.SetTextAlignment(alignment);
            }
            UpdateLayout();
        }
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            ItemAlignment common = alignment.ElementAt(0);
            if (alignment.Length > 1)
            {
                for (int i = 1; i < alignment.Length; i++)
                {
                    common |= alignment.ElementAt(i);
                }
            }
            SetTextAlignment(common);
        }
        public ItemAlignment GetTextAlignment()
        {
            return _text_objects[0].GetTextAlignment();
        }

        /// <summary>
        /// Text margin in the Label
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            foreach (TextLine tl in _text_objects)
            {
                tl.SetMargin(margin);
            }
            UpdateLayout();
        }
        public Indents GetTextMargin()
        {
            return _text_objects[0].GetMargin();
        }

        /// <summary>
        /// Text font parameters in the Label
        /// </summary>
        public void SetFont(Font font)
        {
            foreach (TextLine tl in _text_objects)
            {
                tl.SetFont(font);
            }
            UpdateLayout();
        }

        public void SetFontSize(int size)
        {
            foreach (TextLine tl in _text_objects)
            {
                tl.SetFontSize(size);
            }
            UpdateLayout();
        }

        public void SetFontStyle(FontStyle style)
        {
            foreach (TextLine tl in _text_objects)
            {
                tl.SetFontStyle(style);
            }
        }

        public void SetFontFamily(FontFamily font_family)
        {
            foreach (TextLine tl in _text_objects)
            {
                tl.SetFontFamily(font_family);
            }
        }

        public Font GetFont()
        {
            return _text_objects[0].GetFont();
        }

        // private string preInitText = "";

        /// <summary>
        /// Set text in the Label
        /// </summary>
        public void SetText(String text)
        {
            if (text == null)
            {
                text = "";
            }

            if (_text_objects.Count > 1)
            {
                while (_text_objects.Count > 1)
                {
                    if (_init)
                    {
                        RemoveItem(_text_objects[1]);
                    }
                    _text_objects.RemoveAt(1);
                }
            }

            string[] line = text.Split('\n');
            string s;

            s = line[0].TrimEnd('\r');
            _text_objects[0].SetItemText(s);

            int inc = 0;
            for (int i = 1; i < line.Length; i++)
            {
                inc++;
                s = line[i].TrimEnd('\r');

                TextLine te = new TextLine();
                if (_init)
                {
                    AddItem(te);
                }

                te.SetItemText(s);

                _text_objects.Insert(inc, te);
            }

            SetForeground(GetForeground());
            SetTextAlignment(_text_objects[0].GetTextAlignment());
            SetTextMargin(_text_objects[0].GetMargin());
            SetFont(GetFont());
        }

        public String GetText()
        {
            StringBuilder sb = new StringBuilder();
            if (_text_objects == null) return "";
            if (_text_objects.Count == 1)
            {
                sb.Append(_text_objects[0].GetText());
            }
            else
            {
                for (int i = 0; i < _text_objects.Count - 1; i++)
                {
                    sb.Append(_text_objects[i].GetText());
                    sb.Append("\n");
                }
                sb.Append(_text_objects[_text_objects.Count - 1].GetText());
            }
            return sb.ToString();
        }

        /// <summary>
        /// Text color in the Label
        /// </summary>
        public void SetForeground(Color color)
        {
            foreach (TextLine tl in _text_objects)
            {
                tl.SetForeground(color);
            }
        }
        public void SetForeground(int r, int g, int b)
        {
            foreach (TextLine tl in _text_objects)
            {
                tl.SetForeground(r, g, b);
            }
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            foreach (TextLine tl in _text_objects)
            {
                tl.SetForeground(r, g, b, a);
            }
        }
        public void SetForeground(float r, float g, float b)
        {
            foreach (TextLine tl in _text_objects)
            {
                tl.SetForeground(r, g, b);
            }
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            foreach (TextLine tl in _text_objects)
            {
                tl.SetForeground(r, g, b, a);
            }
        }
        public Color GetForeground()
        {
            return _text_objects[0].GetForeground();
        }

        /// <summary>
        /// Text width in the Label
        /// </summary>
        public int GetTextWidth()
        {
            int wdt = _text_objects[0].GetWidth();
            for (int i = 1; i < _text_objects.Count; i++)
            {
                int w = _text_objects[i].GetWidth();
                if (w > wdt)
                {
                    wdt = w;
                }
            }
            return wdt;
        }

        /// <summary>
        /// Text height in the Label
        /// </summary>
        public int GetTextHeight()
        {
            return GetLineY(_text_objects.Count);
        }

        private int GetLineY(int num)
        {
            int minLineSpacer = _text_objects[0].GetFontDims()[0];
            int lineHeight = _text_objects[0].GetHeight();
            return (lineHeight + minLineSpacer) * num;
        }

        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            TextLine txtObj = _text_objects[0];
            int _cursorXMax = GetWidth() - GetPadding().Left - GetPadding().Right - txtObj.GetMargin().Left
                    - txtObj.GetMargin().Right;

            foreach (TextLine tl in _text_objects)
            {
                tl.SetAllowWidth(_cursorXMax);
                tl.CheckXShift(_cursorXMax); // ???
            }
        }

        /// <summary>
        /// Initialization and adding of all elements in the Label
        /// </summary>
        public override void InitElements()
        {
            foreach (TextLine tl in _text_objects)
            {
                AddItem(tl);
            }
            _init = true;
        }

        //style
        /// <summary>
        /// Set style of the Label
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            SetFont(style.Font);
            SetForeground(style.Foreground);
            SetTextAlignment(style.TextAlignment);
        }

        public void UpdateLayout()
        {
            //UpdateLinesYShifts
            ItemAlignment alignment = GetTextAlignment();
            int globalYShift = 0;
            if (alignment.HasFlag(ItemAlignment.Bottom))
            {
                globalYShift = -(GetTextHeight() - GetLineY(1));
            }
            else if (alignment.HasFlag(ItemAlignment.VCenter))
            {
                globalYShift = -((GetTextHeight() - GetLineY(1)) / 2);
            }
            int inc = 0;
            foreach (TextLine tl in _text_objects)
            {
                tl.SetLineYShift(GetLineY(inc) + globalYShift);
                inc++;
            }
        }
    }
}
