using System;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// HorizontalSlider is the basic implementation of a user interface slider (horizontal version).
    /// <para/> Contains track, handler.
    /// <para/> Supports all events including drag and drop 
    /// (internal handler (SpaceVIL.ScrollHandler) supports drag and drop events).
    /// </summary>
    public class HorizontalSlider : Prototype
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

        #region Values definition
        //Values
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
        /// <param name="value">Slider value</param>
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
        /// <summary>
        /// Getting the current slider value.
        /// </summary>
        /// <returns>Slider value.</returns>
        public float GetCurrentValue()
        {
            return _currentValue;
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
                    / (_maxValue - _minValue) * (_currentValue - _minValue);
            Handler.SetOffset((int)offset + GetPadding().Left + Handler.GetMargin().Left);
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
        #endregion///////////

        /// <summary>
        /// Default HorizontalSlider constructor.
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
        /// Initializing all elements in the HorizontalSlider.
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
            // float value = (_max_value - _min_value) / _step ;
            float result = (float)(Handler.GetX() - GetX()) * (_maxValue - _minValue)
                    / ((float)GetWidth() - GetSumOfHorizontalIndents() - Handler.GetWidth()) + _minValue;

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
                    * (_maxValue - _minValue)
                    / ((float)GetWidth() - GetSumOfHorizontalIndents() - Handler.GetWidth()));
            _dragging = false;
        }

        /// <summary>
        /// Setting X coordinate of the left-top corner of the HorizontalSlider.
        /// </summary>
        /// <param name="x">X position of the left-top corner.</param>
        public override void SetX(int x)
        {
            base.SetX(x);
            UpdateHandler();
        }

        /// <summary>
        /// Seting style of the HorizontalSlider.
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
