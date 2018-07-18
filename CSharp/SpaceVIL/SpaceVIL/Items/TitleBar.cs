using System;
using System.Drawing;

namespace SpaceVIL
{
    class TitleBar : WindowAnchor
    {
        static int count = 0;
        private TextLine _text_object;
        private ImageItem _icon;
        public ImageItem GetIcon()
        {
            return _icon;
        }
        private ButtonCore _close;
        private ButtonCore _minimize;

        public TitleBar()
        {
            SetItemName("TitleBar_" + count);
            SetHeight(30);
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            SetBackground(Color.FromArgb(255, 45, 45, 45));
            SetAlignment(ItemAlignment.Top);
            SetPadding(20, 0, 10, 0);
            EventMouseClick += EmptyEvent;
            count++;

            _text_object = new TextLine();
            _minimize = new ButtonCore();
            _close = new ButtonCore();
            _icon = new ImageItem();
        }
        public TitleBar(String text = "") : this()
        {
            SetText(text);
        }
        
        public void SetIcon(Image icon)
        {
            _icon.SetImage(icon);
        }
        public void SetIcon(String url)
        {
            _icon.SetImageUrl(url);
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
        internal void SetFontSize(int size)
        {
            _text_object.SetFontSize(size);
        }
        internal void SetFontStyle(FontStyle style)
        {
            _text_object.SetFontStyle(style);
        }
        internal void SetFontFamily(FontFamily font_family)
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
        public Color GetForeground()
        {
            return _text_object.GetForeground();
        }

        public override void InitElements()
        {
            //text
            _text_object.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            _text_object.SetMargin(50, 0, 30, 8);
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
            _close.EventMouseClick += (sender) =>
            {
                GetHandler().Close();
            };

            //_minimize
            _minimize.SetBackground(100, 100, 100);
            _minimize.SetAlignment(ItemAlignment.Right | ItemAlignment.Bottom);
            _minimize.IsCustom = new CustomFigure(true, GraphicsMathService.GetRectangle(15, 2, 0, 13));
            _minimize.SetSize(15, 15);
            _minimize.SetMargin(0, 0, 30, 8);
            _minimize.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            _minimize.EventMouseClick += (sender) =>
            {
                GetHandler().Minimize();
            };

            //icon
            _icon.SetBackground(Color.Transparent);
            _icon.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            _icon.SetSize(40, 20);
            _icon.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Left);//????

            //adding
            AddItems(_icon, _text_object, _minimize, _close);

            //update text data
            _text_object.UpdateData(UpdateType.Critical);
        }
    }
}
