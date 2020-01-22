using System;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// Part of HorizontalScrollBar
    /// </summary>
    public class HorizontalSlider : Prototype
    {
        static int count = 0;

        public Rectangle Track = new Rectangle();
        public ScrollHandler Handler = new ScrollHandler();

        #region Values definition
        //Values
        private float _step = 1.0f;

        /// <summary>
        /// HorizontalSlider moving step when HorizontalScrollBar arrows pressed
        /// </summary>
        public void SetStep(float value)
        {
            _step = value;
        }
        public float GetStep()
        {
            return _step;
        }

        public EventCommonMethodState EventValueChanged;
        public override void Release()
        {
            EventValueChanged = null;
        }

        private float _current_value = 0;
        public int Direction = 0;

        /// <summary>
        /// Position value of the HorizontalSlider
        /// </summary>
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

        public float GetCurrentValue()
        {
            return _current_value;
        }

        private int GetSumOfHorizontalIndents()
        {
            Indents marginHandler = Handler.GetMargin();
            Indents paddingSlider = GetPadding();
            int margin = marginHandler.Left + marginHandler.Right;
            int padding = paddingSlider.Left + paddingSlider.Right;
            return margin + padding;
        }

        internal void UpdateHandler()
        {
            float offset = ((float)GetWidth() - GetSumOfHorizontalIndents() - Handler.GetWidth())
                    / (_max_value - _min_value) * (_current_value - _min_value);
            Handler.SetOffset((int)offset + GetPadding().Left + Handler.GetMargin().Left);
        }

        private float _min_value = 0;

        /// <summary>
        /// Minimum value of the HorizontalSlider
        /// </summary>
        public void SetMinValue(float value)
        {
            _min_value = value;
        }
        public float GetMinValue()
        {
            return _min_value;
        }

        private float _max_value = 100;

        /// <summary>
        /// Maximum value of the HorizontalSlider
        /// </summary>
        public void SetMaxValue(float value)
        {
            _max_value = value;
        }
        public float GetMaxValue()
        {
            return _max_value;
        }
        #endregion///////////

        /// <summary>
        /// Constructs a HorizontalSlider
        /// </summary>
        public HorizontalSlider()
        {
            SetItemName("HorizontalSlider_" + count);
            EventMouseClick += OnTrackClick;
            count++;

            Handler.Orientation = Orientation.Horizontal;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.HorizontalSlider)));
        }

        /// <summary>
        /// Initialization and adding of all elements in the HorizontalSlider
        /// </summary>
        public override void InitElements()
        {
            //Adding
            AddItems(Track, Handler);

            //Event connections
            EventMouseDrop += OnDragHandler;
            Handler.EventMouseDrag += EventMouseDrop.Invoke;
        }

        private bool _dragging = false;

        protected virtual void OnDragHandler(object sender, MouseArgs args)//что-то с тобой не так
        {
            _dragging = true;
            //иногда число NAN 

            float result = (float)(Handler.GetX() - GetX()) * (_max_value - _min_value) 
                    / ((float)GetWidth() - GetSumOfHorizontalIndents() - Handler.GetWidth()) + _min_value;
            
            if (!Single.IsNaN(result))
            {
                SetCurrentValue(result);
            }
        }

        protected virtual void OnTrackClick(object sender, MouseArgs args)
        {
            //Compute CurrentValue
            if (!_dragging)
                SetCurrentValue(
                    (float)(args.Position.GetX() - GetX() - Handler.GetWidth() / 2)
                    * (_max_value - _min_value)
                    / ((float)GetWidth() - GetSumOfHorizontalIndents() - Handler.GetWidth()));
            _dragging = false;
        }

        /// <summary>
        /// Set X position of the HorizontalSlider
        /// </summary>
        public override void SetX(int _x)
        {
            base.SetX(_x);
            UpdateHandler();
        }

        /// <summary>
        /// Set style of the HorizontalSlider
        /// </summary>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);

            Style inner_style = style.GetInnerStyle("track");
            if (inner_style != null)
            {
                Track.SetStyle(inner_style);
            }
            inner_style = style.GetInnerStyle("handler");
            if (inner_style != null)
            {
                Handler.SetStyle(inner_style);
            }
        }
    }
}
