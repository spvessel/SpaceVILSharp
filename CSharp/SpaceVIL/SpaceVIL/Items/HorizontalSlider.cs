using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    class HorizontalSlider : VisualItem, IScrollable
    {
        static int count = 0;

        public Rectangle Track = new Rectangle();
        public ScrollHandler Handler = new ScrollHandler();

        #region Values definition
        //Values
        private float _step = 1;
        public void SetStep(float value)
        {
            _step = value;
        }
        public float GetStep()
        {
            return _step;
        }

        private float _current_value = 0;
        public void SetCurrentValue(float value)
        {
            _current_value = value;

            if (_current_value < _min_value)
                _current_value = _min_value;
            if (_current_value > _max_value)
                _current_value = _max_value;

            float offset = _current_value 
                * ((float)GetWidth() - Handler.GetWidth())
                / (_max_value - _min_value) 
                + GetX();

            Handler.SetOffset((int)offset + Handler.GetWidth() / 2);
        }
        public float GetCurrentValue()
        {
            return _current_value;
        }

        private float _min_value = 0;
        public void SetMinValue(float value)
        {
            _min_value = value;
        }
        public float GetMinValue()
        {
            return _min_value;
        }

        private float _max_value = 100;
        public void SetMaxValue(float value)
        {
            _max_value = value;
        }
        public float GetMaxValue()
        {
            return _max_value;
        }
        #endregion

        public HorizontalSlider()
        {
            SetWidthPolicy(SizePolicy.Expand);
            SetHeight(25);
            SetBackground(Color.Transparent);
            SetItemName("HorizontalSlider" + count);
            EventMouseClick += OnTrackClick;
            EventMouseHover += (sender) => IsMouseHover = !IsMouseHover;
            count++;

            //Track
            Track.SetBackground(Color.FromArgb(255, 100, 100, 100));
            Track.SetAlignment(ItemAlignment.VCenter);
            Track.SetWidthPolicy(SizePolicy.Expand);
            Track.SetHeightPolicy(SizePolicy.Fixed);
            Track.SetHeight(5);

            //Handler
            Handler.Orientation = Orientation.Horizontal;
            Handler.SetBackground(Color.FromArgb(255, 255, 181, 111));
            Handler.SetWidth(10);
            Handler.SetWidthPolicy(SizePolicy.Fixed);
            Handler.SetHeightPolicy(SizePolicy.Expand);
            Handler.SetAlignment(ItemAlignment.Left);
            Handler.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
        }

        public override void InitElements()
        {
            //Adding
            AddItems(Track, Handler);

            //Event connections
            Handler.EventMouseDrop += OnDropHandler;
        }

        public override void InvokePoolEvents()
        {
            if (EventMouseClick != null) EventMouseClick.Invoke(this);
        }

        public void OnDropHandler(object sender)
        {
            SetCurrentValue(
                (float)(Handler.GetX() - GetX()) 
                * (_max_value - _min_value) 
                / ((float)GetWidth() - Handler.GetWidth()));
        }

        public virtual void OnTrackClick(object sender)
        {
            //Compute CurrentValue
            if (_mouse_ptr.IsSet())
            {
                SetCurrentValue(
                    (float)(_mouse_ptr.X - GetX() - Handler.GetWidth() / 2) 
                    * (_max_value - _min_value) 
                    / ((float)GetWidth() - Handler.GetWidth()));
                Handler.SetOffset(_mouse_ptr.X);
            }
        }

        public override void SetX(int _x)
        {
            base.SetX(_x);
            float offset = _current_value 
                * (float)GetWidth() 
                / (_max_value - _min_value) 
                + GetX();
            Handler.SetOffset((int)offset);
        }

        public void InvokeScrollUp()
        {
            (GetParent() as IScrollable)?.InvokeScrollUp();
        }

        public void InvokeScrollDown()
        {
            (GetParent() as IScrollable)?.InvokeScrollDown();
        }
    }
}
