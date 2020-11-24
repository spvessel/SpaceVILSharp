using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// SideArea is a container designed to show when it is needed, 
    /// and the rest of the time SideArea is hidden. SideArea is a floating 
    /// item (see SpaceVIL.Core.IFloating and  enum SpaceVIL.Core.LayoutType)
    /// Always attached to one of the four sides of window. 
    /// <para/> Contains close button and resizable area. 
    /// <para/> Supports all events except drag and drop.
    /// <para/> Notice: All floating items render above all others items.
    /// <para/> SideArea does not pass any input events and invisible by default.
    /// </summary>
    public class SideArea : Prototype, IFloating
    {
        static int count = 0;
        private bool _init = false;

        private readonly Color _shadowColor = Color.FromArgb(150, 0, 0, 0);
        private readonly int _shadowRadius = 5;
        private readonly int _shadowIndent = 3;

        private Shadow _shadowLeftArea = null;
        private Shadow _shadowTopArea = null;
        private Shadow _shadowRightArea = null;
        private Shadow _shadowBottomArea = null;

        private ButtonCore _close;
        /// <summary>
        /// Resizable container area of SideArea.
        /// </summary>
        public ResizableItem Window;

        private Side _attachSide = Side.Left;
        /// <summary>
        /// Getting the side of the window which SideArea is attached. 
        /// <para/> Default: Side.Left.
        /// </summary>
        /// <returns>Side of the window as SpaceVIL.Core.Side.</returns>
        public Side GetAttachSide()
        {
            return _attachSide;
        }
        /// <summary>
        /// Setting the side of the window which SideArea will be attached.
        /// <para/> Default: Side.Left.
        /// </summary>
        /// <param name="side">Side of the window as SpaceVIL.Core.Side.</param>
        public void SetAttachSide(Side side)
        {
            if (_attachSide == side)
                return;

            _attachSide = side;
            ApplyAttach();
        }

        private void ApplyAttach()
        {
            Window.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            Window.SetAlignment(ItemAlignment.Top, ItemAlignment.Left);
            Window.IsXResizable = false;
            Window.IsYResizable = false;
            Window.ClearExcludedSides();
            Effects.ClearEffects(Window, EffectType.Shadow);
            
            switch (_attachSide)
            {
                case Side.Left:
                    Window.IsXResizable = true;
                    Window.SetWidthPolicy(SizePolicy.Fixed);
                    Window.SetWidth(_size);
                    Window.ExcludeSides(Side.Left, Side.Bottom, Side.Top);
                    Window.SetAlignment(ItemAlignment.Left);
                    Effects.AddEffect(Window, _shadowLeftArea);
                    break;

                case Side.Top:
                    Window.IsYResizable = true;
                    Window.SetHeightPolicy(SizePolicy.Fixed);
                    Window.SetHeight(_size);
                    Window.ExcludeSides(Side.Left, Side.Right, Side.Top);
                    Window.SetAlignment(ItemAlignment.Top);
                    Effects.AddEffect(Window, _shadowTopArea);
                    break;

                case Side.Right:
                    Window.IsXResizable = true;
                    Window.SetWidthPolicy(SizePolicy.Fixed);
                    Window.SetWidth(_size);
                    Window.ExcludeSides(Side.Right, Side.Bottom, Side.Top);
                    Window.SetAlignment(ItemAlignment.Right);
                    Effects.AddEffect(Window, _shadowRightArea);
                    break;

                case Side.Bottom:
                    Window.IsYResizable = true;
                    Window.SetHeightPolicy(SizePolicy.Fixed);
                    Window.SetHeight(_size);
                    Window.ExcludeSides(Side.Left, Side.Right, Side.Bottom);
                    Window.SetAlignment(ItemAlignment.Bottom);
                    Effects.AddEffect(Window, _shadowBottomArea);
                    break;

                default:
                    Window.SetWidth(_size);
                    Window.SetAlignment(ItemAlignment.Left);
                    Effects.AddEffect(Window, _shadowLeftArea);
                    break;
            }
        }

        private int _size = 300;

        /// <summary>
        /// Getting actual size of SideArea.
        /// <para/> If SideArea is attached to Side.Left or Side.Right, 
        /// then this value is the width of the area, otherwise, the height.
        /// <para/> Default: 300.
        /// </summary>
        /// <returns>Actual size of SideArea.</returns>
        public int GetAreaSize()
        {
            return _size;
        }

        /// <summary>
        /// Setting actual size of SideArea.
        /// <para/> If SideArea is attached to Side.Left or Side.Right, 
        /// then this value is the width of the area, otherwise, the height.
        /// <para/> Default: 300.
        /// </summary>
        /// <param name="size">Actual size of SideArea.</param>
        public void SetAreaSize(int size)
        {
            if (size == _size)
                return;
            _size = size;
            ApplyAttach();
        }
        /// <summary>
        /// Constructs SideArea with the specified side and the specified window 
        /// for attachment.
        /// (see SpaceVIL.CoreWindow, SpaceVIL.ActiveWindow, SpaceVIL.DialogWindow).
        /// <para/> SideArea does not pass any input events and invisible by default.
        /// </summary>
        /// <param name="handler">Window for attaching SideArea.</param>
        /// <param name="attachSide">Side of the window as SpaceVIL.Core.Side.</param>
        public SideArea(CoreWindow handler, Side attachSide)
        {
            ItemsLayoutBox.AddItem(handler, this, LayoutType.Floating);

            _shadowLeftArea = new Shadow(_shadowRadius, new Position(_shadowIndent, 0), _shadowColor);
            _shadowTopArea = new Shadow(_shadowRadius, new Position(0, _shadowIndent), _shadowColor);
            _shadowRightArea = new Shadow(_shadowRadius, new Position(-_shadowIndent, 0), _shadowColor);
            _shadowBottomArea = new Shadow(_shadowRadius, new Position(0, -_shadowIndent), _shadowColor);

            SetItemName("SideArea_" + count++);
            _close = new ButtonCore();
            Window = new ResizableItem();
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.SideArea)));
            _attachSide = attachSide;
            ApplyAttach();
            EventMouseClick += (sender, args) =>
            {
                Hide();
            };
            SetVisible(false);
            SetPassEvents(false);

            EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.Escape)
                    Hide();
            };
        }

        /// <summary>
        /// Initializing all elements in the SideArea.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            base.AddItem(Window);
            Window.AddItem(_close);
            Window.SetPassEvents(false);
            Window.IsXFloating = false;
            Window.IsYFloating = false;

            _close.EventMouseClick += (sender, args) =>
            {
                Hide();
            };

            _init = true;
        }
        /// <summary>
        /// Adding item into the SideArea.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            Window.AddItem(item);
        }

        /// <summary>
        /// Inserting item to the SideArea. 
        /// If the count of container elements is less than the index, then the element is added to the end of the list.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <param name="index">Index of insertion.</param>
        public override void InsertItem(IBaseItem item, int index)
        {
            Window.InsertItem(item, index);
        }

        /// <summary>
        /// Removing the specified item from SideArea.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public override bool RemoveItem(IBaseItem item)
        {
            return Window.RemoveItem(item);
        }

        /// <summary>
        /// Setting SideArea width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width"> Width of the SideArea. </param>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
        }

        /// <summary>
        /// Setting SideArea height. If the value is greater/less than the maximum/minimum 
        /// value of the height, then the height becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="height"> Height of the SideArea. </param>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
        }

        /// <summary>
        /// Shows the SideArea at the proper position.
        /// </summary>
        public virtual void Show()
        {
            if (!_init)
                InitElements();
            SetVisible(true);
            SetFocus();
        }

        /// <summary>
        /// Shows the SideArea at the proper position. This method do exactly 
        /// as Show() method without arguments.
        /// </summary>
        /// <param name="sender"> The item from which the show request is sent. </param>
        /// <param name="args"> Mouse click arguments (cursor position, mouse button,
        /// mouse button press/release, etc.). </param>
        public virtual void Show(IItem sender, MouseArgs args)
        {
            Show();
        }

        /// <summary>
        /// Hide the SideArea without destroying.
        /// </summary>
        public virtual void Hide()
        {
            SetVisible(false);
        }

        /// <summary>
        /// Hide the SideArea without destroying with using specified mouse arguments.
        /// This method do exactly as Hide() method without arguments.
        /// </summary>
        /// <param name="args">Arguments as SpaceVIL.Core.MouseArgs.</param>
        public void Hide(MouseArgs args)
        {
            Hide();
        }

        /// <summary>
        /// Setting style of the SideArea.
        /// <para/> Inner styles: "window", "closebutton".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            Style innerStyle = style.GetInnerStyle("window");
            if (innerStyle != null)
            {
                Window.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("closebutton");
            if (innerStyle != null)
            {
                _close.SetStyle(innerStyle);
            }
        }

        private bool _ouside = false;

        /// <summary>
        /// Returns True if SideArea (see SpaceVIL.Core.IFloating)
        /// should closes when mouse click outside the area of SideArea otherwise returns False.
        /// </summary>
        /// <returns>True: if SideArea closes when mouse click outside the area.
        /// False: if SideArea stays opened when mouse click outside the area.</returns>
        public bool IsOutsideClickClosable()
        {
            return _ouside;
        }

        /// <summary>
        /// Setting boolean value of item's behavior when mouse click occurs outside the SideArea.
        /// </summary>
        /// <param name="value">True: SideArea should become invisible if mouse click occurs outside the item.
        /// False: an item should stay visible if mouse click occurs outside the item.</param>
        public void SetOutsideClickClosable(bool value)
        {
            _ouside = value;
        }
    }
}