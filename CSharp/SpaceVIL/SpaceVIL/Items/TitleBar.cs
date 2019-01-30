using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class TitleBar : WindowAnchor
    {
        static int count = 0;
        private HorizontalStack _layout;
        public HorizontalDirection Direction = HorizontalDirection.FromLeftToRight;
        private Label _text_object;
        private ImageItem _icon;
        public ImageItem GetIcon()
        {
            return _icon;
        }
        private ButtonCore _close;
        public ButtonCore GetCloseButton()
        {
            return _close;
        }
        private ButtonCore _minimize;
        public ButtonCore GetMinimizeButton()
        {
            return _minimize;
        }
        private ButtonCore _maximize;
        public ButtonCore GetMaximizeButton()
        {
            return _maximize;
        }

        public TitleBar()
        {
            SetItemName("TitleBar_" + count);
            count++;

            _layout = new HorizontalStack();
            _text_object = new Label();
            _minimize = new ButtonCore();
            _minimize.IsFocusable = false;
            _maximize = new ButtonCore();
            _maximize.IsFocusable = false;
            _close = new ButtonCore();
            _close.IsFocusable = false;
            _icon = new ImageItem();
            _icon.IsFocusable = false;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TitleBar)));
        }
        public TitleBar(String text = "") : this()
        {
            SetText(text);
        }

        public void SetIcon(Image icon, int width, int height)
        {
            _icon.SetSize(width, height);
            _icon.SetImage(icon);
            _icon.SetVisible(true);
        }
        public void SetIcon(String url, int width, int height)
        {
            _icon.SetSize(width, height);
            _icon.SetImageUrl(url);
            _icon.SetVisible(true);
        }

        //text init
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }
        public void SetTextMargin(Indents margin)
        {
            _text_object.SetMargin(margin);
        }
        public void SetFont(Font font)
        {
            _text_object.SetFont(font);
        }
        public void SetFontSize(int size)
        {
            _text_object.SetFontSize(size);
        }
        public void SetFontStyle(FontStyle style)
        {
            _text_object.SetFontStyle(style);
        }
        public void SetFontFamily(FontFamily font_family)
        {
            _text_object.SetFontFamily(font_family);
        }
        public Font GetFont()
        {
            return _text_object.GetFont();
        }
        public void SetText(String text)
        {
            _text_object.SetText(text);
        }
        public String GetText()
        {
            return _text_object.GetText();
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
            AddItem(_layout);

            //text
            // SetFont(new Font(new FontFamily("Open Sans Light"), 16, FontStyle.Bold));

            //_close
            _close.EventMouseClick += (sender, args) =>
            {
                GetHandler().Close();
            };

            //_minimize
            _minimize.EventMouseClick += (sender, args) =>
            {
                GetHandler().Minimize();
            };

            //_maximize
            if (Common.CommonService.GetOSType() != OSType.Mac)
            {
                _maximize.EventMouseClick += (sender, args) =>
                {
                    GetHandler().Maximize();
                };
            }
            else
            {
                _maximize.SetVisible(false);
                _maximize.SetDrawable(false);
            }

            //adding
            switch (Direction)
            {
                case HorizontalDirection.FromLeftToRight:
                    _layout.AddItems(_icon, _text_object, _minimize, _maximize, _close);
                    break;
                case HorizontalDirection.FromRightToLeft:
                    _text_object.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
                    _layout.AddItems(_close, _minimize, _maximize, _icon, _text_object);
                    break;
                default:
                    _layout.AddItems(_icon, _text_object, _minimize, _maximize, _close);
                    break;
            }
        }

        //style
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;

            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);
            _layout.SetSpacing(style.Spacing);

            Style inner_style = style.GetInnerStyle("closebutton");
            if (inner_style != null)
            {
                _close.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("minimizebutton");
            if (inner_style != null)
            {
                _minimize.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("maximizebutton");
            if (inner_style != null)
            {
                _maximize.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("title");
            if (inner_style != null)
            {
                SetTextMargin(inner_style.Margin);
            }

            //icon
            _icon.SetVisible(false);
            _icon.SetBackground(Color.Transparent);
            _icon.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            _icon.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Left);
        }
    }
}
