using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class VerticalSlider : VisualItem, IScrollable
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
            float offset = ((float)GetHeight() - Handler.GetHeight()) / (_max_value - _min_value) * _current_value;
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

        public VerticalSlider()
        {
            SetHeightPolicy(SizePolicy.Expand);
            SetWidth(25);
            SetBackground(Color.Transparent);
            SetItemName("VerticalSlider_" + count);
            EventMouseClick += OnTrackClick;
            count++;

            //Track
            Track.SetBackground(Color.FromArgb(255, 100, 100, 100));
            Track.SetAlignment(ItemAlignment.HCenter);
            Track.SetHeightPolicy(SizePolicy.Expand);
            Track.SetWidthPolicy(SizePolicy.Fixed);
            Track.SetWidth(5);

            //Handler
            Handler.Orientation = Orientation.Vertical;
            Handler.SetBackground(Color.FromArgb(255, 255, 181, 111));
            Handler.SetHeight(10);
            Handler.SetWidthPolicy(SizePolicy.Expand);
            Handler.SetHeightPolicy(SizePolicy.Fixed);
            Handler.SetAlignment(ItemAlignment.Top);
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

        public void OnDropHandler(object sender, MouseArgs args)//что-то с тобой не так
        {
            //иногда число NAN 
            float result = (float)(Handler.GetY() - GetY()) * (_max_value - _min_value) / ((float)GetHeight() - Handler.GetHeight());
            if (!Single.IsNaN(result))
                SetCurrentValue(result);
        }

        public virtual void OnTrackClick(object sender, MouseArgs args)
        {
            //Compute CurrentValue
            if (_mouse_ptr.IsSet())
            {
                SetCurrentValue(
                    (float)(_mouse_ptr.Y - GetY() - Handler.GetHeight() / 2)
                    * (_max_value - _min_value)
                    / ((float)GetHeight() - Handler.GetHeight()));
            }
        }

        public override void SetY(int _y)
        {
            base.SetY(_y);
            UpdateHandler();
        }

        public void InvokeScrollUp(MouseArgs args)
        {
            (GetParent() as IScrollable)?.InvokeScrollUp(args);
        }

        public void InvokeScrollDown(MouseArgs args)
        {
            (GetParent() as IScrollable)?.InvokeScrollDown(args);
        }
    }
}
