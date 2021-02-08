using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// FloatItem is a floating container for other items (see SpaceVIL.Core.IFloating). 
    /// Can be moved using drag and drop events. 
    /// <para/> Supports all events including drag and drop.
    /// <para/> Notice: All floating items render above all others items.
    /// </summary>
    public class FloatItem : Prototype, IFloating, IDraggable
    {
        /// <summary>
        /// Property for enabling/disabling drag and drop. 
        /// <para/> True: Drag and drop is enabled. False: Drag and drop is disabled.
        /// <para/> Default: True.
        /// </summary>
        public bool IsFloating = true;
        private int _storedOffset = 0;
        private bool _init = false;
        static int count = 0;
        private int _diffX = 0;
        private int _diffY = 0;
        private bool _ouside = false;

        /// <summary>
        /// Returns True if FloatItem (see SpaceVIL.Core.IFloating)
        /// should closes when mouse click outside the area of FloatItem otherwise returns False.
        /// </summary>
        /// <returns>True: if FloatItem closes when mouse click outside the area.
        /// False: if FloatItem stays opened when mouse click outside the area.</returns>
        public bool IsOutsideClickClosable()
        {
            return _ouside;
        }
        /// <summary>
        /// Setting boolean value of item's behavior when mouse click occurs outside the FloatItem.
        /// </summary>
        /// <param name="value">True: FloatItem should become invisible if mouse click occurs outside the item.
        /// False: an item should stay visible if mouse click occurs outside the item.</param>
        public void SetOutsideClickClosable(bool value)
        {
            _ouside = value;
        }

        /// <summary>
        /// Constructs a FloatItem and attaches it to the specified window 
        /// (see SpaceVIL.CoreWindow, SpaceVIL.ActiveWindow, SpaceVIL.DialogWindow).
        /// FloatItem invisible by default.
        /// </summary>
        /// <param name="handler">Window for attaching FloatItem.</param>
        public FloatItem(CoreWindow handler)
        {
            ItemsLayoutBox.AddItem(handler, this, LayoutType.Floating);
            SetVisible(false);

            SetItemName("FloatItem_" + count);
            SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            EventMouseHover += OnMousePress;
            EventMousePress += OnMousePress;
            EventMouseDrag += OnDragging;
            count++;
        }

        /// <summary>
        /// Initializing FloatItem. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            SetConfines();
            _init = true;
        }

        /// <summary>
        /// Shows the FloatItem at the proper position.
        /// </summary>
        /// <param name="sender"> The item from which the show request is sent. </param>
        /// <param name="args"> Mouse click arguments (cursor position, mouse button,
        /// mouse button press/release, etc.). </param>
        public void Show(IItem sender, MouseArgs args)
        {
            if (!_init)
                InitElements();
            if (GetX() == -GetWidth()) //refactor?
                SetX(_storedOffset);
            SetVisible(true);
        }
        /// <summary>
        /// Shows the FloatItem at the position (0, 0).
        /// </summary>
        public void Show()
        {
            if (!_init)
                InitElements();
            if (GetX() == -GetWidth()) //refactor?
                SetX(_storedOffset);
            SetVisible(true);
        }

        /// <summary>
        /// Hides the FloatItem without destroying.
        /// </summary>
        public void Hide()
        {
            _storedOffset = GetX();
            SetVisible(false);
            SetX(-GetWidth());
        }
        /// <summary>
        /// Hides the FloatItem without destroying.
        /// <para/> This method do exactly as Hide() method without arguments.
        /// </summary>
        /// <param name="args">Mouse click arguments (cursor position, mouse button,
        /// mouse button press/release, etc.).</param>
        public void Hide(MouseArgs args)
        {
            Hide();
        }

        private void OnMousePress(object sender, MouseArgs args)
        {
            _diffX = args.Position.GetX() - GetX();
            _diffY = args.Position.GetY() - GetY();
        }

        private void OnDragging(object sender, MouseArgs args)
        {
            if (!IsFloating)
                return;

            int offset_x;
            int offset_y;

            offset_x = args.Position.GetX() - _diffX;
            offset_y = args.Position.GetY() - _diffY;

            SetX(offset_x);
            SetY(offset_y);
            SetConfines();
        }

        /// <summary>
        /// Overridden method for setting confines according 
        /// to position and size of the FloatItem (see Prototype.SetConfines()).
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