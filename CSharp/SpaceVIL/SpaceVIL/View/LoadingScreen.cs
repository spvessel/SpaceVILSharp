using System;
using System.Drawing;
using System.Threading;
using SpaceVIL;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace ItemsOnTest
{
    class LoadingScreen : Prototype
    {
        static int count = 0;
        ImageItem _loadIcon;
        Label _percent;
        public LoadingScreen()
        {
            SetItemName("LoadingScreen_" + count++);
            SetPassEvents(false);
            _loadIcon = new ImageItem();
            _percent = new Label("0%");
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.Frame)));
            SetBackground(0, 0, 0, 150);
        }

        public override void InitElements()
        {
            var image = new Bitmap("D:\\Images\\load_circle.png");
            _loadIcon.SetImage(image);
            _loadIcon.IsHover = false;
            _loadIcon.SetMaxSize(64, 64);
            base.AddItems(_loadIcon, _percent);
            _percent.SetTextAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
            _percent.SetFontSize(14);
            _percent.SetFontStyle(FontStyle.Bold);
        }

        WindowLayout _handler = null;

        public void Show(WindowLayout handler)
        {
            _handler = handler;
            _handler.AddItem(this);
            _handler.SetFocusedItem(this);
            Thread thread = new Thread(() =>
            {
                int perc = 0;
                int duration = 360 * 6;
                while (duration > 0)
                {
                    _loadIcon.SetRotationAngle(duration);
                    _percent.SetText((int)(perc / 21.6f) + "%");
                    duration--;
                    perc++;
                    Thread.Sleep(2);
                }
                _percent.SetText("100%");
                Thread.Sleep(1000);
                Close();
            });
            thread.Start();
        }

        public void Close()
        {
            _handler.GetWindow().RemoveItem(this);
        }
    }
}