using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class HorizontalSlider : VisualItem, IScrollable
    {
        static int count = 0;

        public Rectangle Track = new Rectangle();
        public ScrollHandler Handler = new ScrollHandler();

        #region Values definition
        //Values
        private float _step = 1.0f;
        public void SetStep(float value)
        {
            _step = value;
        }
        public float GetStep()
        {
            return _step;
        }
        public EventCommonMethodState EventValueChanged;
        private float _current_value = 0;
        public int Direction = 0;
        public void SetCurrentValue(float value)
        {
            //if (value == _current_value)
            //    return;

            if (_current_value > value)
                Direction = -1; //up
            else
                Direction = 1; //down

            _current_value = value;

            if (_current_value < _min_value)
                _current_value = _min_value;
            if (_current_value > _max_value)
                _current_value = _max_value;

            UpdateHandler(); //refactor!!

            if (EventValueChanged != null) EventValueChanged.Invoke(this);
        }

        internal void UpdateHandler()
        {
            float offset = ((float)GetWidth() - Handler.GetWidth()) / (_max_value - _min_value) * _current_value;
            Handler.SetOffset((int)offset);
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
        #endregion///////////

        public HorizontalSlider()
        {
            SetWidthPolicy(SizePolicy.Expand);
            SetHeight(25);
            SetBackground(Color.Transparent);
            SetItemName("HorizontalSlider_" + count);
            EventValueChanged += EmptyEvent;
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
            EventMouseDrop += OnDropHandler;
            Handler.EventMouseDrag += EventMouseDrop.Invoke;
        }

        public override void InvokePoolEvents()
        {
            if (EventMouseClick != null) EventMouseClick.Invoke(this);
        }

        public void OnDropHandler(object sender)//что-то с тобой не так
        {
            //иногда число NAN 
            float result = (float)(Handler.GetX() - GetX()) * (_max_value - _min_value) / ((float)GetWidth() - Handler.GetWidth());
            if (!Single.IsNaN(result))
                SetCurrentValue(result);
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
            }
        }

        public override void SetX(int _x)
        {
            base.SetX(_x);
            UpdateHandler();
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
