using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Timers;
namespace SpaceVIL
{
    public class PopUpMessage : VisualItem, IPopUp
    {
        static int count = 0;
        private TextLine _text_object;
        private ButtonCore _btn_close;
        internal Timer _stop;
        private int _timeout = 5000;
        internal bool _holded = false;
        public void SetTimeOut(int milliseconds)
        {
            _timeout = milliseconds;
        }
        public int GetTimeOut()
        {
            return _timeout;
        }
        public PopUpMessage()
        {
            _btn_close = new ButtonCore();
            _text_object = new TextLine();
            EventMouseClick += EmptyEvent;
            SetItemName("PopUpMessage_" + count);
            SetBackground(32, 32, 32, 240);
            SetForeground(Color.White);
            SetPadding(5, 5, 5, 5);
            SetMargin(10, 10, 10, 10);
            SetAlignment(ItemAlignment.Bottom | ItemAlignment.HCenter);
            SetSize(300, 70);
            SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            Border.Radius = 10;
            count++;
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
            _btn_close.SetBackground(Color.FromArgb(255, 100, 100, 100));
            _btn_close.SetItemName("ClosePopUp");
            _btn_close.SetSize(10, 10);
            _btn_close.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            _btn_close.SetAlignment(ItemAlignment.Right | ItemAlignment.Top);
            _btn_close.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            _btn_close.IsCustom = new CustomFigure(false, GraphicsMathService.GetCross(10, 10, 3, 45));
            _btn_close.EventMouseClick += (sender) =>
            {
                RemoveSelf();
            };

            //text
            _text_object.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            _text_object.SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);

            //adding
            AddItems(_text_object, _btn_close);

            //update text data
            _text_object.UpdateData(UpdateType.Critical);
        }

        public void Execute(WindowLayout wnd, String message)
        {
            SetHandler(wnd);
            wnd.GetWindow().AddItem(this);
            SetText(message);
            InitTimer();
        }
        internal void InitTimer()
        {
            if (_stop != null)
                return;

            _stop = new System.Timers.Timer(_timeout);
            _stop.Elapsed += (sender, e) =>
            {
                lock (CommonService.engine_locker)
                    RemoveSelf();
            };
            _stop.Start();
        }

        private void RemoveSelf()
        {

            if (_stop != null)
            {
                _stop.Stop();
                _stop.Dispose();
                _stop = null;
            }
            GetParent().RemoveItem(this);
        }

        internal void HoldSelf(bool ok)
        {
            _holded = ok;
            if (_stop != null)
            {
                _stop.Stop();
                _stop.Dispose();
                _stop = null;
            }
        }
    }
}