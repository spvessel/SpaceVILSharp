using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// Label is is the basic implementation of a user interface non-editable text item. 
    /// Label has multiline text support.
    /// <para/> Supports all events except drag and drop.
    /// <para/> By default, Label is stretched to all available space in the container.
    /// </summary>
    public class Label : Prototype, IVLayout
    {
        static int count = 0;
        private List<TextLine> _textObjects;
        private bool _init = false;

        /// <summary>
        /// Property to enable or disable mouse events (hover, click, press, scroll).
        /// <para/> True: Label can receive mouse events. False: cannot receive mouse events.
        /// <para/> Default: True.
        /// </summary>
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
        /// Default Label constructor. 
        /// </summary>
        public Label()
        {
            SetItemName("Label_" + count);
            count++;
            _textObjects = new List<TextLine>();
            _textObjects.Add(new TextLine());

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.Label)));
            IsFocusable = false;
        }

        /// <summary>
        /// Constructs a Label with text.
        /// </summary>
        /// <param name="text">Label text.</param>
        public Label(String text) : this()
        {
            SetText(text);
        }
        /// <summary>
        /// Constructs a Label with text and with the ability 
        /// to enable or disable mouse events.
        /// </summary>
        /// <param name="text">Label text.</param>
        /// <param name="hover">True: Label can receive mouse events. 
        /// False: cannot receive mouse events.</param>
        public Label(String text, bool hover) : this(text)
        {
            SetText(text);
            IsHover = hover;
        }

        /// <summary>
        /// Setting alignment of Label text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            foreach (TextLine tl in _textObjects)
            {
                tl.SetTextAlignment(alignment);
            }
            UpdateLayout();
        }
        /// <summary>
        /// Setting alignment of an Label text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            // ItemAlignment common = alignment.ElementAt(0);
            // if (alignment.Length > 1)
            // {
            //     for (int i = 1; i < alignment.Length; i++)
            //     {
            //         common |= alignment.ElementAt(i);
            //     }
            // }
            // SetTextAlignment(common);
            SetTextAlignment(BaseItemStatics.ComposeFlags(alignment));
        }
        /// <summary>
        /// Getting alignment of a Label text. 
        /// </summary>
        /// <returns>Text alignment as SpaceVIL.Core.ItemAlignment.</returns>
        public ItemAlignment GetTextAlignment()
        {
            return _textObjects[0].GetTextAlignment();
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to Label.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            foreach (TextLine tl in _textObjects)
            {
                tl.SetMargin(margin);
            }
            UpdateLayout();
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to Label.
        /// </summary>
        /// <param name="left">Indent on the left.</param>
        /// <param name="top">Indent on the top.</param>
        /// <param name="right">Indent on the right.</param>
        /// <param name="bottom">Indent on the bottom.</param>
        public void SetTextMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            SetTextMargin(new Indents(left, top, right, bottom));
        }
        /// <summary>
        /// Getting indents of the text.
        /// </summary>
        /// <returns>Indents as SpaceVIL.Decorations.Indents.</returns>
        public Indents GetTextMargin()
        {
            return _textObjects[0].GetMargin();
        }

        /// <summary>
        /// Setting font of the text.
        /// </summary>
        /// <param name="font">Font as System.Drawing.Font.</param>
        public void SetFont(Font font)
        {
            foreach (TextLine tl in _textObjects)
            {
                tl.SetFont(font);
            }
            UpdateLayout();
        }
        /// <summary>
        /// Setting font size of the text.
        /// </summary>
        /// <param name="size">New size of the font.</param>
        public void SetFontSize(int size)
        {
            foreach (TextLine tl in _textObjects)
            {
                tl.SetFontSize(size);
            }
            UpdateLayout();
        }
        /// <summary>
        /// Setting font style of the text.
        /// </summary>
        /// <param name="style">New font style as System.Drawing.FontStyle.</param>
        public void SetFontStyle(FontStyle style)
        {
            foreach (TextLine tl in _textObjects)
            {
                tl.SetFontStyle(style);
            }
        }
        /// <summary>
        /// Setting new font family of the text.
        /// </summary>
        /// <param name="fontFamily">New font family as System.Drawing.FontFamily.</param>
        public void SetFontFamily(FontFamily fontFamily)
        {
            foreach (TextLine tl in _textObjects)
            {
                tl.SetFontFamily(fontFamily);
            }
        }
        /// <summary>
        /// Getting the current font of the text.
        /// </summary>
        /// <returns>Font as System.Drawing.Font.</returns>
        public Font GetFont()
        {
            return _textObjects[0].GetFont();
        }

        /// <summary>
        /// Setting the text.
        /// </summary>
        /// <param name="text">Text as System.String.</param>
        public void SetText(String text)
        {
            if (text == null)
            {
                text = "";
            }

            if (_textObjects.Count > 1)
            {
                while (_textObjects.Count > 1)
                {
                    if (_init)
                    {
                        RemoveItem(_textObjects[1]);
                    }
                    _textObjects.RemoveAt(1);
                }
            }

            string[] line = text.Split('\n');
            string s;

            s = line[0].TrimEnd('\r');
            _textObjects[0].SetItemText(s);

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

                _textObjects.Insert(inc, te);
            }

            SetForeground(GetForeground());
            SetTextAlignment(_textObjects[0].GetTextAlignment());
            SetTextMargin(_textObjects[0].GetMargin());
            SetFont(GetFont());
        }

        /// <summary>
        /// Setting the text.
        /// </summary>
        /// <param name="text">Text as System.Object.</param>
        public virtual void SetText(object text)
        {
            SetText(text.ToString());
        }

        /// <summary>
        /// Getting the current text of the Label.
        /// </summary>
        /// <returns>Text as System.String.</returns>
        public String GetText()
        {
            StringBuilder sb = new StringBuilder();
            if (_textObjects == null)
            {
                return "";
            }
            if (_textObjects.Count == 1)
            {
                sb.Append(_textObjects[0].GetText());
            }
            else
            {
                for (int i = 0; i < _textObjects.Count - 1; i++)
                {
                    sb.Append(_textObjects[i].GetText());
                    sb.Append("\n");
                }
                sb.Append(_textObjects[_textObjects.Count - 1].GetText());
            }
            return sb.ToString();
        }

        /// <summary>
        /// Setting text color of a Label.
        /// </summary>
        /// <param name="color">Text color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            foreach (TextLine tl in _textObjects)
            {
                tl.SetForeground(color);
            }
        }
        /// <summary>
        /// Setting text color of a Label in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            foreach (TextLine tl in _textObjects)
            {
                tl.SetForeground(r, g, b);
            }
        }
        /// <summary>
        /// Setting text color of a Label in byte RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        /// <param name="a">Alpha color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b, int a)
        {
            foreach (TextLine tl in _textObjects)
            {
                tl.SetForeground(r, g, b, a);
            }
        }
        /// <summary>
        /// Setting text color of a Label in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            foreach (TextLine tl in _textObjects)
            {
                tl.SetForeground(r, g, b);
            }
        }
        /// <summary>
        /// Setting text color of a Label in float RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha color component. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b, float a)
        {
            foreach (TextLine tl in _textObjects)
            {
                tl.SetForeground(r, g, b, a);
            }
        }
        /// <summary>
        /// Getting current text color.
        /// </summary>
        /// <returns>Text color as System.Drawing.Color.</returns>
        public Color GetForeground()
        {
            return _textObjects[0].GetForeground();
        }
        /// <summary>
        /// Getting the text width (useful when you need resize Label by text width).
        /// </summary>
        /// <returns>Text width.</returns>
        public int GetTextWidth()
        {
            int wdt = _textObjects[0].GetWidth();
            for (int i = 1; i < _textObjects.Count; i++)
            {
                int w = _textObjects[i].GetWidth();
                if (w > wdt)
                {
                    wdt = w;
                }
            }
            return wdt;
        }
        /// <summary>
        /// Getting the text height (useful when you need resize Label by text height).
        /// </summary>
        /// <returns>Text height.</returns>
        public int GetTextHeight()
        {
            return GetLineY(_textObjects.Count);
        }

        private int GetLineY(int num)
        {
            int minLineSpacer = _textObjects[0].GetFontDims().lineSpacer; //[0];
            int lineHeight = _textObjects[0].GetHeight();
            return (lineHeight + minLineSpacer) * num;
        }
        /// <summary>
        /// Setting Label width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width"> Width of the Label. </param>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            TextLine txtObj = _textObjects[0];
            int _cursorXMax = GetWidth() - GetPadding().Left - GetPadding().Right - txtObj.GetMargin().Left
                    - txtObj.GetMargin().Right;

            foreach (TextLine tl in _textObjects)
            {
                tl.SetAllowWidth(_cursorXMax);
                tl.CheckXShift(_cursorXMax); // ???
            }
        }

        /// <summary>
        /// Initializing all elements in the Label.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            foreach (TextLine tl in _textObjects)
            {
                AddItem(tl);
            }
            _init = true;
        }

        /// <summary>
        /// Setting style of the Label.
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
            {
                return;
            }
            base.SetStyle(style);

            SetFont(style.Font);
            SetForeground(style.Foreground);
            SetTextAlignment(style.TextAlignment);
        }
        /// <summary>
        /// Updating all children positions (implementation of SpaceVIL.Core.IVLayout).
        /// </summary>
        public void UpdateLayout()
        {
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
            foreach (TextLine tl in _textObjects)
            {
                tl.SetLineYShift(GetLineY(inc) + globalYShift);
                inc++;
            }
        }
    }
}
