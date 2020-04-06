using System;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// TextArea is a basic implementation of a user interface editable text area. 
    /// <para/> Contains text area, scroll bars, menu button, navigation context menu.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class TextArea : Prototype
    {
        static int count = 0;
        private Grid _grid = new Grid(2, 2);
        private TextBlock _area = new TextBlock();
        /// <summary>
        /// Interactive item to show the navigation context menu.
        /// </summary>
        public BlankItem Menu = new BlankItem();
        private bool _isMenuDisabled = false;

        /// <summary>
        /// Setting the navigation context menu to disable or enable.
        /// </summary>
        /// <param name="value">True: if you want to disable navigation context menu. 
        /// False: if you want to enable navigation context menu. </param>
        public void SetDisableMenu(bool value)
        {
            _isMenuDisabled = value;
        }

        private ContextMenu _menu;

        /// <summary>
        /// Returns True if TextArea is editable otherwise returns False.
        /// </summary>
        /// <returns>True: if TextArea is editable.
        /// True: if TextArea is non-editable.</returns>
        public bool IsEditable()
        {
            return _area.IsEditable;
        }

        /// <summary>
        /// Setting TextArea text field be editable or be non-editable.
        /// </summary>
        /// <param name="value">True: if you want TextArea be editable.
        /// True: if you want TextArea be non-editable.</param>
        public void SetEditable(bool value)
        {
            _area.IsEditable = value;
        }
        /// <summary>
        /// Vertical scroll bar of TextArea.
        /// </summary>
        public VerticalScrollBar VScrollBar = new VerticalScrollBar();
        /// <summary>
        /// Horizontal scroll bar of TextArea.
        /// </summary>
        public HorizontalScrollBar HScrollBar = new HorizontalScrollBar();
        private VisibilityPolicy _vScrollBarPolicy = VisibilityPolicy.AsNeeded;
        private VisibilityPolicy _hScrollBarPolicy = VisibilityPolicy.AsNeeded;

        /// <summary>
        /// Getting vertical scroll bar visibility policy.
        /// </summary>
        /// <returns>Visibility policy as SpaceVIL.Core.VisibilityPolicy.</returns>
        public VisibilityPolicy GetVScrollBarPolicy()
        {
            return _vScrollBarPolicy;
        }
        /// <summary>
        /// Setting vertical scroll bar visibility policy.
        /// <para/> Default: SpaceVIL.Core.VisibilityPolicy.AsNeeded.
        /// </summary>
        /// <param name="policy">Visibility policy as SpaceVIL.Core.VisibilityPolicy.</param>
        public void SetVScrollBarPolicy(VisibilityPolicy policy)
        {
            _vScrollBarPolicy = policy;

            if (policy == VisibilityPolicy.Never)
            {
                VScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == VisibilityPolicy.AsNeeded)
            {
                VScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == VisibilityPolicy.Always)
            {
                VScrollBar.SetDrawable(true);
                if (!HScrollBar.IsDrawable())
                    Menu.SetVisible(false);
                else
                    Menu.SetVisible(true);
            }

            _grid.UpdateLayout();
            UpdateHorizontalSlider();
            HScrollBar.Slider.UpdateHandler();
        }

        /// <summary>
        /// Getting horizontal scroll bar visibility policy.
        /// </summary>
        /// <returns>Visibility policy as SpaceVIL.Core.VisibilityPolicy.</returns>
        public VisibilityPolicy GetHScrollBarPolicy()
        {
            return _hScrollBarPolicy;
        }
        /// <summary>
        /// Setting horizontal scroll bar visibility policy.
        /// <para/> Default: SpaceVIL.Core.VisibilityPolicy.AsNeeded.
        /// </summary>
        /// <param name="policy">Visibility policy as SpaceVIL.Core.VisibilityPolicy.</param>
        public void SetHScrollBarPolicy(VisibilityPolicy policy)
        {
            _hScrollBarPolicy = policy;

            if (policy == VisibilityPolicy.Never)
            {
                HScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == VisibilityPolicy.AsNeeded)
            {
                HScrollBar.SetDrawable(false);
                Menu.SetVisible(false);
            }
            else if (policy == VisibilityPolicy.Always)
            {
                HScrollBar.SetDrawable(true);
                if (!VScrollBar.IsDrawable())
                    Menu.SetVisible(false);
                else
                    Menu.SetVisible(true);
            }

            _grid.UpdateLayout();
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
        }

        /// <summary>
        /// Default TextArea constructor.
        /// </summary>
        public TextArea()
        {
            SetItemName("TextArea_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TextArea)));

            //VBar
            VScrollBar.SetDrawable(true);
            VScrollBar.SetItemName(GetItemName() + "_" + VScrollBar.GetItemName());

            //HBar
            HScrollBar.SetDrawable(true);
            HScrollBar.SetItemName(GetItemName() + "_" + HScrollBar.GetItemName());

            EventMouseClick += (sender, args) =>
            {
                _area.SetFocus();
            };
        }
        /// <summary>
        /// Constructs TextArea with the specified text.
        /// </summary>
        /// <param name="text">Text for TextArea.</param>
        public TextArea(String text) : this()
        {
            SetText(text);
        }

        private Int64 vSize = 0;
        private Int64 hSize = 0;

        private void UpdateVListArea()
        {
            //vertical slider
            float vValue = VScrollBar.Slider.GetCurrentValue();
            int vOffSet = (int)Math.Round((float)(vSize * vValue) / 100.0f);
            _area.SetScrollYOffset(-vOffSet);
        }

        private void UpdateHListArea()
        {
            //horizontal slider
            float hValue = HScrollBar.Slider.GetCurrentValue();
            int hOffSet = (int)Math.Round((float)(hSize * hValue) / 100.0f);
            _area.SetScrollXOffset(-hOffSet);
        }

        private void UpdateVerticalSlider()//vertical slider
        {
            int visibleArea = _area.GetHeight() - _area.GetPadding().Top - _area.GetPadding().Bottom;
            if (visibleArea < 0)
                visibleArea = 0;
            int total = _area.GetTextHeight();

            int totalInvisibleSize = total - visibleArea;
            if (total <= visibleArea)
            {
                VScrollBar.Slider.Handler.SetHeight(0);
                VScrollBar.Slider.SetStep(VScrollBar.Slider.GetMaxValue());
                vSize = 0;
                VScrollBar.Slider.SetCurrentValue(0);
                if (GetVScrollBarPolicy() == VisibilityPolicy.AsNeeded)
                {
                    VScrollBar.SetDrawable(false);
                    Menu.SetVisible(false);
                    _grid.UpdateLayout();
                }
                return;
            }
            if (GetVScrollBarPolicy() == VisibilityPolicy.AsNeeded)
            {
                VScrollBar.SetDrawable(true);
                if (!HScrollBar.IsDrawable())
                    Menu.SetVisible(false);
                else
                    Menu.SetVisible(true);
                _grid.UpdateLayout();
            }
            vSize = totalInvisibleSize;

            if (totalInvisibleSize > 0)
            {
                float sizeHandler = (float)(visibleArea)
                    / (float)total * 100.0f;
                sizeHandler = (float)VScrollBar.Slider.GetHeight() / 100.0f * sizeHandler;
                //size of handler
                VScrollBar.Slider.Handler.SetHeight((int)sizeHandler);
            }
            //step of slider
            float stepCount = (float)totalInvisibleSize / (float)_area.GetScrollYStep();
            VScrollBar.Slider.SetStep((VScrollBar.Slider.GetMaxValue() - VScrollBar.Slider.GetMinValue()) / stepCount);
            VScrollBar.Slider.SetCurrentValue((100.0f / totalInvisibleSize) * Math.Abs(_area.GetScrollYOffset()));
        }
        private void UpdateHorizontalSlider()//horizontal slider
        {
            int visibleArea = _area.GetWidth() - _area.GetPadding().Left - _area.GetPadding().Right - 2 * _area.GetCursorWidth();
            if (visibleArea < 0)
                visibleArea = 0;
            int total = _area.GetTextWidth();

            int totalInvisibleSize = total - visibleArea;
            if (total <= visibleArea)
            {
                HScrollBar.Slider.Handler.SetWidth(0);
                HScrollBar.Slider.SetStep(HScrollBar.Slider.GetMaxValue());
                hSize = 0;
                HScrollBar.Slider.SetCurrentValue(0);
                if (GetHScrollBarPolicy() == VisibilityPolicy.AsNeeded)
                {
                    HScrollBar.SetDrawable(false);
                    Menu.SetVisible(false);
                    _grid.UpdateLayout();
                }
                return;
            }
            if (GetHScrollBarPolicy() == VisibilityPolicy.AsNeeded)
            {
                HScrollBar.SetDrawable(true);
                if (!VScrollBar.IsDrawable())
                    Menu.SetVisible(false);
                else
                    Menu.SetVisible(true);
                _grid.UpdateLayout();
            }
            hSize = totalInvisibleSize;

            if (totalInvisibleSize > 0)
            {
                float sizeHandler = (float)(visibleArea)
                    / (float)total * 100.0f;
                sizeHandler = (float)HScrollBar.Slider.GetWidth() / 100.0f * sizeHandler;
                //size of handler
                HScrollBar.Slider.Handler.SetWidth((int)sizeHandler);
            }
            //step of slider
            float stepCount = (float)totalInvisibleSize / (float)_area.GetScrollXStep();
            HScrollBar.Slider.SetStep((HScrollBar.Slider.GetMaxValue() - HScrollBar.Slider.GetMinValue()) / stepCount);
            HScrollBar.Slider.SetCurrentValue((100.0f / totalInvisibleSize) * Math.Abs(_area.GetScrollXOffset()));
        }

        /// <summary>
        /// Setting item width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width"> Width of the item. </param>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateHorizontalSlider();
            HScrollBar.Slider.UpdateHandler();
        }

        /// <summary>
        /// Setting item height. If the value is greater/less than the maximum/minimum 
        /// value of the height, then the height becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="height"> Height of the item. </param>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
        }

        private void UpdateElements()
        {
            UpdateVerticalSlider();
            VScrollBar.Slider.UpdateHandler();
            UpdateHorizontalSlider();
            HScrollBar.Slider.UpdateHandler();
        }
        /// <summary>
        /// Event that is invoked when text is changed.
        /// </summary>
        public EventCommonMethod OnTextChanged;

        /// <summary>
        /// Initializing all elements in the TextArea.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            //Adding
            base.AddItem(_grid);
            _grid.InsertItem(_area, 0, 0);
            _grid.InsertItem(VScrollBar, 0, 1);
            _grid.InsertItem(HScrollBar, 1, 0);
            _grid.InsertItem(Menu, 1, 1);

            //Events Connections
            EventScrollUp += VScrollBar.EventScrollUp.Invoke;
            EventScrollDown += VScrollBar.EventScrollDown.Invoke;
            _area.CursorChanged += UpdateElements;
            _area.TextChanged += () => OnTextChanged?.Invoke();

            VScrollBar.Slider.EventValueChanged += (sender) => { UpdateVListArea(); };
            HScrollBar.Slider.EventValueChanged += (sender) => { UpdateHListArea(); };

            // create menu
            if (!_isMenuDisabled)
            {
                _menu = new ContextMenu(GetHandler());
                _menu.SetBackground(60, 60, 60);
                _menu.SetPassEvents(false);

                Color menuItemCForeground = Color.FromArgb(210, 210, 210);

                MenuItem goUp = new MenuItem("Go up");
                goUp.SetForeground(menuItemCForeground);
                goUp.EventMouseClick += ((sender, args) =>
                {
                    _area.SetScrollYOffset(0);
                    UpdateElements();
                    _area.SetFocus();
                });

                MenuItem goDown = new MenuItem("Go down");
                goDown.SetForeground(menuItemCForeground);
                goDown.EventMouseClick += ((sender, args) =>
                {
                    _area.SetScrollYOffset(-_area.GetTextHeight());
                    UpdateElements();
                    _area.SetFocus();
                });

                MenuItem goUpLeft = new MenuItem("Go up and left");
                goUpLeft.SetForeground(menuItemCForeground);
                goUpLeft.EventMouseClick += ((sender, args) =>
                {
                    _area.SetScrollYOffset(0);
                    _area.SetScrollXOffset(0);
                    UpdateElements();
                    _area.SetFocus();
                });

                MenuItem goDownRight = new MenuItem("Go down and right");
                goDownRight.SetForeground(menuItemCForeground);
                goDownRight.EventMouseClick += ((sender, args) =>
                {
                    _area.SetScrollYOffset(-_area.GetTextHeight());
                    _area.SetScrollXOffset(-_area.GetTextWidth());
                    UpdateElements();
                    _area.SetFocus();
                });
                _menu.AddItems(goUpLeft, goDownRight, goUp, goDown);
                Menu.EventMouseClick += (sender, args) =>
                {
                    if (!_isMenuDisabled)
                        _menu.Show(sender, args);
                };
                _menu.ActiveButton = MouseButton.ButtonLeft;
                _menu.SetShadow(10, 0, 0, Color.Black);
            }

            UpdateElements();
        }
        /// <summary>
        /// Setting the text.
        /// </summary>
        /// <param name="text">Text as System.String.</param>
        public void SetText(String text)
        {
            _area.SetText(text);
            UpdateElements();
        }
        /// <summary>
        /// Getting the current text of the TextArea.
        /// </summary>
        /// <returns>Text as System.String.</returns>
        public String GetText()
        {
            return _area.GetText();
        }

        /// <summary>
        /// Setting style of the TextArea.
        /// <para/> Inner styles: "vscrollbar", "hscrollbar", "textedit", "menu".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);

            Style innerStyle = style.GetInnerStyle("vscrollbar");
            if (innerStyle != null)
            {
                VScrollBar.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("hscrollbar");
            if (innerStyle != null)
            {
                HScrollBar.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("textedit");
            if (innerStyle != null)
            {
                _area.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("menu");
            if (innerStyle != null)
            {
                Menu.SetStyle(innerStyle);
            }
        }

        /// <summary>
        /// Setting text color of a TextArea.
        /// </summary>
        /// <param name="color">Color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _area.SetForeground(color);
        }
        /// <summary>
        /// Setting text color of a TextArea in byte RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b));
        }
        /// <summary>
        /// Setting text color of a TextArea in byte RGBA format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        /// <param name="a">Alpha bits of a color. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b, int a)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b, a));
        }
        /// <summary>
        /// Setting text color of a TextArea in float RGB format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b));
        }
        /// <summary>
        /// Setting text color of a TextArea in float RGBA format.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue bits of a color. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha bits of a color. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b, float a)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b, a));
        }
        /// <summary>
        /// Getting current text color.
        /// </summary>
        /// <returns>Text color as System.Drawing.Color.</returns>
        public Color GetForeground()
        {
            return _area.GetForeground();
        }

        /// <summary>
        /// Setting indent between lines in TextArea.
        /// </summary>
        /// <param name="lineSpacer">Indent between lines.</param>
        public void SetLineSpacer(int lineSpacer)
        {
            _area.SetLineSpacer(lineSpacer);
        }
        /// <summary>
        /// Setting current indent between lines in TextArea.
        /// </summary>
        /// <returns>Indent between lines.</returns>
        public int GetLineSpacer()
        {
            return _area.GetLineSpacer();
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to this TextArea.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            _area.SetTextMargin(margin);
        }
        /// <summary>
        /// Setting indents for the text to offset text relative to TextArea.
        /// </summary>
        /// <param name="left">Indent on the left.</param>
        /// <param name="top">Indent on the top.</param>
        /// <param name="right">Indent on the right.</param>
        /// <param name="bottom">Indent on the bottom.</param>
        public void SetTextMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _area.SetTextMargin(new Indents(left, top, right, bottom));
        }
        /// <summary>
        /// Getting indents of the text.
        /// </summary>
        /// <returns>Indents as SpaceVIL.Decorations.Indents.</returns>
        public Indents GetTextMargin()
        {
            return _area.GetTextMargin();
        }

        /// <summary>
        /// Setting font of the text.
        /// </summary>
        /// <param name="font">Font as System.Drawing.Font.</param>
        public void SetFont(Font font)
        {
            _area.SetFont(font);
        }
        /// <summary>
        /// Setting font size of the text.
        /// </summary>
        /// <param name="size">New size of the font.</param>
        public void SetFontSize(int size)
        {
            Font oldFont = GetFont();
            if (oldFont.Size != size)
            {
                Font newFont = GraphicsMathService.ChangeFontSize(size, oldFont); //new Font(oldFont.FontFamily, size, oldFont.Style);
                SetFont(newFont);
            }
        }
        /// <summary>
        /// Setting font style of the text.
        /// </summary>
        /// <param name="style">New font style as System.Drawing.FontStyle.</param>
        public void SetFontStyle(FontStyle style)
        {
            Font oldFont = GetFont();
            if (oldFont.Style != style)
            {
                Font newFont = GraphicsMathService.ChangeFontStyle(style, oldFont); //new Font(oldFont.FontFamily, oldFont.Size, style);
                SetFont(newFont);
            }
        }
        /// <summary>
        /// Setting new font family of the text.
        /// </summary>
        /// <param name="fontFamily">New font family as System.Drawing.FontFamily.</param>
        public void SetFontFamily(FontFamily font_family)
        {
            if (font_family == null)
                return;
            Font oldFont = GetFont();
            if (oldFont.FontFamily != font_family)
            {
                Font newFont = GraphicsMathService.ChangeFontFamily(font_family, oldFont); //new Font(font_family, oldFont.Size, oldFont.Style);
                SetFont(newFont);
            }
        }
        /// <summary>
        /// Getting the current font of the text.
        /// </summary>
        /// <returns>Font as System.Drawing.Font.</returns>
        public Font GetFont()
        {
            return _area.GetFont();
        }

        /// <summary>
        /// Getting the text width.
        /// </summary>
        /// <returns>Text width.</returns>
        public int GetTextWidth()
        {
            return _area.GetWidth();
        }

        /// <summary>
        /// Getting the text height (useful when you need resize item by text height).
        /// </summary>
        /// <returns>Text height.</returns>
        public int GetTextHeight()
        {
            return _area.GetTextHeight();
        }

        /// <summary>
        /// Set TextArea focused/unfocused
        /// </summary>
        public override void SetFocus()
        {
            _area.SetFocus();
        }

        /// <summary>
        /// Setting focus on TextArea if it is focusable.
        /// </summary>
        public override void Clear()
        {
            _area.ClearText();
        }
        /// <summary>
        /// Adding the specified text to the end of the existing text.
        /// </summary>
        /// <param name="text">Text for adding.</param>
        public void AppendText(String text)
        {
            _area.AppendText(text);
        }
        /// <summary>
        /// Paste the specified text at the current position of the text cursor 
        /// (or replace the specified text at the current starting position of 
        /// the selected text).
        /// </summary>
        /// <param name="text">Text to insert.</param>
        public void PasteText(String text)
        {
            _area.PasteText(text);
        }
        /// <summary>
        /// Cuts the current selected text and save it to the clipboard buffer.
        /// </summary>
        /// <returns>Selected text.</returns>
        public string CutText()
        {
            return _area.CutText();
        }
        /// <summary>
        /// Getting the current selected text.
        /// </summary>
        /// <returns>Current selected text</returns>
        public string GetSelectedText()
        {
            return _area.GetSelectedText();
        }

        /// <summary>
        /// Moves text cursor to the text beginning.
        /// </summary>
        public void RewindText()
        {
            _area.RewindText();
        }
        /// <summary>
        /// Returns True if TextArea wraps the contained text to the 
        /// width of the TextArea otherwise returns False.
        /// </summary>
        /// <returns>True: if TextArea wraps the contained text to the width of the TextArea. 
        /// False: if TextArea does not wraps the contained text.</returns>
        public bool IsWrapText()
        {
            return _area.IsWrapText();
        }
        /// <summary>
        /// Setting TextArea mode that wraps (or not wraps) input text to the width of the TextArea.
        /// </summary>
        /// <param name="value">True: if you want to TextArea wraps the contained text to the width of the TextArea. 
        /// False: if you want to TextArea does not wraps the contained text.</param>
        public void SetWrapText(bool value)
        {
            _area.SetWrapText(value);
            UpdateHorizontalSlider();
        }
        /// <summary>
        /// Setting scroll step factor. The scroll factor determines how many lines are scrolled 
        /// in a single scroll request (using a button or mouse wheel).
        /// <para/> Default: 1.0
        /// </summary>
        /// <param name="value">Scroll step factor</param>
        public void SetScrollStepFactor(float value)
        {
            _area.SetScrollStepFactor(value);
        }
    }
}