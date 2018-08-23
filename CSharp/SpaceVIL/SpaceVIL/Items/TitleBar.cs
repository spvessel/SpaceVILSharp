using System;
using System.Drawing;

namespace SpaceVIL
{
    public enum HDirection
    {
        FromLeftToRight,
        FromRightToLeft,
    }

    public class TitleBar : WindowAnchor
    {
        static int count = 0;
        private HorizontalStack _layout;
        public HDirection Direction = HDirection.FromLeftToRight;
        private TextLine _text_object;
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
            SetHeight(30);
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            SetBackground(Color.FromArgb(255, 45, 45, 45));
            SetAlignment(ItemAlignment.Top);
            SetPadding(10, 0, 10, 0);
            count++;

            _layout = new HorizontalStack();
            _text_object = new TextLine();
            _minimize = new ButtonCore();
            _maximize = new ButtonCore();
            _close = new ButtonCore();
            _icon = new ImageItem();
        }
        public TitleBar(String text = "") : this()
        {
            SetText(text);
        }

        public void SetIcon(Image icon, int width, int height)
        {
            _icon.SetSize(width, height);
            _icon.SetImage(icon);
            _icon.IsVisible = false;
        }
        public void SetIcon(String url, int width, int height)
        {
            _icon.SetSize(width, height);
            _icon.SetImageUrl(url);
            _icon.IsVisible = false;
        }

        //text init
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _text_object.SetTextAlignment(alignment);
        }
        public void SetTextMargin(Margin margin)
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
            _text_object.SetItemText(text);
        }
        public String GetText()
        {
            return _text_object.GetItemText();
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
            _layout.SetSpacing(5);
            AddItem(_layout);

            //text
            _text_object.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            _text_object.SetMargin(0, 0, 0, 8);
            _text_object.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);

            SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            SetFont(new Font(new FontFamily("Open Sans Light"), 16, FontStyle.Bold));
            SetForeground(Color.FromArgb(255, 180, 180, 180));

            //_close
            _close.SetBackground(100, 100, 100);
            _close.SetSize(15, 15);
            _close.SetAlignment(ItemAlignment.Right | ItemAlignment.VCenter);
            _close.IsCustom = new CustomFigure(true, GraphicsMathService.GetCross(15, 15, 2, 45));
            _close.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(255, 186, 95, 97)
            });
            _close.EventMouseClick += (sender, args) =>
            {
                GetHandler().Close();
            };

            //_minimize
            _minimize.SetBackground(100, 100, 100);
            _minimize.SetAlignment(ItemAlignment.Left | ItemAlignment.Bottom);
            _minimize.IsCustom = new CustomFigure(true, GraphicsMathService.GetRectangle(15, 2, 0, 13));
            _minimize.SetSize(12, 15);
            _minimize.SetMargin(0, 0, 5, 9);
            _minimize.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            _minimize.EventMouseClick += (sender, args) =>
            {
                GetHandler().Minimize();
            };

            //_maximize
            _maximize.SetBackground(100, 100, 100);
            _maximize.SetAlignment(ItemAlignment.Left | ItemAlignment.Bottom);
            _maximize.SetSize(12, 12);
            _maximize.IsCustom = new CustomFigure(false, GraphicsMathService.GetRectangle());
            _maximize.SetMargin(0, 0, 0, 9);
            _maximize.SetPadding(2, 2, 2, 2);
            _maximize.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(40, 0, 255, 64)
            });
            _maximize.EventMouseClick += (sender, args) =>
            {
                GetHandler().Maximize();
            };
            Rectangle center = new Rectangle();
            center.SetBackground(GetBackground());
            center.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);


            //icon
            _icon.IsVisible = false;
            _icon.SetBackground(Color.Transparent);
            _icon.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            //_icon.SetSize(20, 20);
            _icon.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Left);

            //adding
            switch (Direction)
            {
                case HDirection.FromLeftToRight:
                    _layout.AddItems(_icon, _text_object, _minimize, _maximize, _close);
                    break;
                case HDirection.FromRightToLeft:
                    _layout.AddItems(_close, _maximize, _minimize, _icon, _text_object);
                    break;
                default:
                    _layout.AddItems(_icon, _text_object, _minimize, _maximize, _close);
                    break;
            }
            _maximize.AddItem(center);

            //update text data
            //_text_object.UpdateData(UpdateType.Critical);
        }
    }
}
