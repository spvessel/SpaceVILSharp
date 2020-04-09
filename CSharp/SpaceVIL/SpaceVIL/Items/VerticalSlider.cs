using System;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// VerticalSlider is the basic implementation of a user interface slider (vertical version).
    /// <para/> Contains track, handler.
    /// <para/> Supports all events including drag and drop 
    /// (internal handler (SpaceVIL.ScrollHandler) supports drag and drop events).
    /// </summary>
    public class VerticalSlider : Prototype
    {
        static int count = 0;

        /// <summary>
        /// Slider track.
        /// </summary>
        public Rectangle Track = new Rectangle();

        /// <summary>
        /// Slider handler.
        /// </summary>
        public ScrollHandler Handler = new ScrollHandler();

        private float _step = 1.0f;

        /// <summary>
        /// Setting slider movement step.
        /// </summary>
        /// <param name="value">Slider step.</param>
        public void SetStep(float value)
        {
            _step = value;
        }

        /// <summary>
        /// Getting slider movement step.
        /// </summary>
        /// <returns>Slider step.</returns>
        public float GetStep()
        {
            return _step;
        }

        /// <summary>
        /// Event that is invoked when value of the slider is changed.
        /// </summary>
        public EventCommonMethodState EventValueChanged;

        /// <summary>
        /// Disposing all resources if the item was removed.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void Release()
        {
            EventValueChanged = null;
        }

        private float _currentValue = 0;

        bool _ignoreStep = true;

        /// <summary>
        /// Ignoring slider step (affects only on animation). 
        /// Set False if you want the slider to move strictly in steps.
        /// <para/> Default: True.
        /// </summary>
        /// <param name="value">True: if you want to ignore step. 
        /// False: if you do not want to ignore step. </param>
        public void SetIgnoreStep(bool value)
        {
            _ignoreStep = value;
        }

        /// <summary>
        /// Returns True if slider movement ignore steps otherwise returns False.
        /// </summary>
        /// <returns>True: if movement step is ignored. 
        /// False: if movement step is not ignored. </returns>
        public bool IsIgnoreStep()
        {
            return _ignoreStep;
        }

        /// <summary>
        /// Setting the current slider value. If the value is greater/less than the maximum/minimum 
        /// slider value, then the slider value becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="value">Slider value.</param>
        public void SetCurrentValue(float value)
        {
            _currentValue = value;

            if (!_ignoreStep)
                _currentValue = (float)Math.Round(_currentValue / _step, MidpointRounding.ToEven) * _step;

            if (_currentValue < _minValue)
                _currentValue = _minValue;
            if (_currentValue > _maxValue)
                _currentValue = _maxValue;

            UpdateHandler(); //refactor!!

            if (EventValueChanged != null) EventValueChanged.Invoke(this);
        }

        private Int32 GetSumOfVerticalIndents()
        {
            Indents marginHandler = Handler.GetMargin();
            Indents paddingSlider = GetPadding();
            int margin = marginHandler.Top + marginHandler.Bottom;
            int padding = paddingSlider.Top + paddingSlider.Bottom;
            return margin + padding;
        }

        internal void UpdateHandler()
        {
            float offset = ((float)GetHeight() - GetSumOfVerticalIndents() - Handler.GetHeight())
                    / (_maxValue - _minValue) * (_currentValue - _minValue);
            Handler.SetOffset((int)offset + GetPadding().Top + Handler.GetMargin().Top);
        }

        /// <summary>
        /// Getting the current slider value.
        /// </summary>
        /// <returns>Slider value.</returns>
        public float GetCurrentValue()
        {
            return _currentValue;
        }

        private float _minValue = 0;

        /// <summary>
        /// Setting the minimum slider value limit. 
        /// Slider value cannot be less than this limit.
        /// </summary>
        /// <param name="value">Minimum slider value limit.</param>
        public void SetMinValue(float value)
        {
            _minValue = value;
        }

        /// <summary>
        /// Getting the current minimum slider value limit.
        /// </summary>
        /// <returns>Minimum slider value limit.</returns>
        public float GetMinValue()
        {
            return _minValue;
        }

        private float _maxValue = 100;

        /// <summary>
        /// Setting the maximum slider value limit. 
        /// Slider value cannot be greater than this limit.
        /// </summary>
        /// <param name="value">Maximum slider value limit.</param>
        public void SetMaxValue(float value)
        {
            _maxValue = value;
        }

        /// <summary>
        /// Getting the current maximum slider value limit.
        /// </summary>
        /// <returns>Maximum slider value limit.</returns>
        public float GetMaxValue()
        {
            return _maxValue;
        }

        /// <summary>
        /// Default VerticalSlider constructor.
        /// </summary>
        public VerticalSlider()
        {
            SetItemName("VerticalSlider_" + count);
            EventMouseClick += OnTrackClick;
            count++;

            //Handler
            Handler.Orientation = Orientation.Vertical;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.VerticalSlider)));
        }

        /// <summary>
        /// Initializing all elements in the VerticalSlider.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
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
            float result = (float)(Handler.GetY() - GetY()) * (_maxValue - _minValue)
                    / ((float)GetHeight() - GetSumOfVerticalIndents() - Handler.GetHeight()) + _minValue;
            if (!Single.IsNaN(result))
                SetCurrentValue(result);
        }

        protected virtual void OnTrackClick(object sender, MouseArgs args)
        {
            //Compute CurrentValue
            if (!_dragging)
                SetCurrentValue(
                        (float)(args.Position.GetY() - GetY() - Handler.GetHeight() / 2)
                        * (_maxValue - _minValue)
                        / ((float)GetHeight() - GetSumOfVerticalIndents() - Handler.GetHeight()));
            _dragging = false;
        }

        /// <summary>
        /// Setting X coordinate of the left-top corner of the VerticalSlider.
        /// </summary>
        /// <param name="x">X position of the left-top corner.</param>
        public override void SetY(int y)
        {
            base.SetY(y);
            UpdateHandler();
        }

        /// <summary>
        /// Seting style of the VerticalSlider.
        /// <para/> Inner styles: "track", "handler".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
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
