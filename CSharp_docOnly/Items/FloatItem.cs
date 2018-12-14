using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    public class FloatItem : Prototype, IFloating, IDraggable //create abstract!!!!
    {
        private int _stored_offset = 0;
        private bool IsFloating = true;
        private bool _init = false;
        static int count = 0;
        private int _diff_x = 0;
        private int _diff_y = 0;

        private bool _ouside = false;

        /// <summary>
        /// Close the FloatItem it mouse click is outside (true or false)
        /// </summary>
        public bool IsOutsideClickClosable()
        {
            return _ouside;
        }
        public void SetOutsideClickClosable(bool value)
        {
            _ouside = value;
        }

        // private bool _lock_ouside = false;
        // public bool IsLockOutside()
        // {
        //     return _lock_ouside;
        // }
        // public void SetLockOutside(bool value)
        // {
        //     _lock_ouside = value;
        // }

        /// <summary>
        /// Constructs a FloatItem
        /// </summary>
        /// <param name="handler"> parent window for the FloatItem </param>
        public FloatItem(WindowLayout handler)
        {
            SetVisible(false);
            SetHandler(handler);
            SetItemName("FloatItem_" + count);
            SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            EventMouseHover += OnMousePress;
            EventMousePress += OnMousePress;
            EventMouseDrag += OnDragging;
            count++;

            lock (CommonService.GlobalLocker)
                ItemsLayoutBox.AddItem(GetHandler(), this, LayoutType.Floating);
        }

        /// <summary>
        /// Initialization and adding of all elements in the FloatItem
        /// </summary>
        public override void InitElements()
        {
            //fake tests
            SetConfines();
            _init = true;
        }

        /// <summary>
        /// Show the FloatItem
        /// </summary>
        /// <param name="sender"> the item from which the show request is sent </param>
        /// <param name="args"> mouse click arguments (cursor position, mouse button,
        /// mouse button press/release, etc.) </param>
        public void Show(IItem sender, MouseArgs args)
        {
            //LogService.Log().LogBaseItem(this, LogProps.AllGeometry);
            if (!_init)
                InitElements();
            if (GetX() == -GetWidth()) //refactor?
                SetX(_stored_offset);
            SetVisible(true);
        }

        /// <summary>
        /// Hide the FloatItem
        /// </summary>
        public void Hide()
        {
            _stored_offset = GetX();
            SetX(-GetWidth());
            SetVisible(false);
        }

        void OnMousePress(object sender, MouseArgs args)
        {
            _diff_x = args.Position.GetX() - GetX();
            _diff_y = args.Position.GetY() - GetY();
        }

        void OnDragging(object sender, MouseArgs args)
        {
            if (!IsFloating)
                return;

            int offset_x;
            int offset_y;

            offset_x = args.Position.GetX() - _diff_x;
            offset_y = args.Position.GetY() - _diff_y;

            SetX(offset_x);
            SetY(offset_y);
            SetConfines();
        }

        /// <summary>
        /// Set confines according to position and size of the FloatItem
        /// </summary>
        public override void SetConfines()
        {
            base.SetConfines(
                GetX(),
                GetX() + GetWidth(),
                GetY(),
                GetY() + GetHeight()
            );
        }
    }
}