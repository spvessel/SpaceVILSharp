using System.Collections.Generic;
using System.Text;
using SpaceVIL;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using View.Decorations;

namespace View.Components {

    // Interface IDraggable and IMovable disables passage mouse click event
    public class EventItem : BlankItem {

        private Dictionary<InputEventType, IInputEventArgs> currentEvents = new Dictionary<InputEventType, IInputEventArgs>();
        private Label eventStateInfo = null;

        public EventItem(string name)
        {
            SetItemName(name);
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ButtonCore)));
            SetMargin(50, 50, 50, 50);
            SetPadding(10, 10, 10, 10);
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            SetBackground(Palette.GreenLight);
            SetBorderRadius(2);
            AddItemState(ItemStateType.Pressed, new ItemState(Palette.DarkGlass));
            Effects().Add(new Shadow(10, new Position(0, 3), new Size(0, 10), Palette.BlackShadow));

            eventStateInfo = new Label(name, false);
        }

        public override void InitElements()
        {
            EventMousePress += (sender, args) => {
                currentEvents.Remove(InputEventType.MouseRelease);
                currentEvents.TryAdd(InputEventType.MousePress, args);
                UpdateEventInfo();
            };
            EventMouseClick += (sender, args) => {
                currentEvents.Remove(InputEventType.MouseDoubleClick);
                currentEvents.Remove(InputEventType.MousePress);
                currentEvents.TryAdd(InputEventType.MouseRelease, args);
                UpdateEventInfo();
            };
            EventMouseDoubleClick += (sender, args) => {
                currentEvents.Remove(InputEventType.MouseRelease);
                currentEvents.Remove(InputEventType.MousePress);
                currentEvents.TryAdd(InputEventType.MouseDoubleClick, args);
                UpdateEventInfo();
            };
            EventMouseHover += (sender, args) => {
                currentEvents.Remove(InputEventType.MouseLeave);
                currentEvents.TryAdd(InputEventType.MouseHover, args);
                UpdateEventInfo();
            };
            EventMouseLeave += (sender, args) => {
                currentEvents.Remove(InputEventType.MouseHover);
                currentEvents.TryAdd(InputEventType.MouseLeave, args);
                UpdateEventInfo();
            };
            EventMouseMove += (sender, args) => {
                currentEvents.TryAdd(InputEventType.MouseMove, args);
                UpdateEventInfo();
            };
            EventScrollUp += (sender, args) => {
                currentEvents.TryAdd(InputEventType.MouseScroll, args);
                UpdateEventInfo();
            };
            EventScrollDown += (sender, args) => {
                MouseArgs margs = new MouseArgs();
                margs.ScrollValue = new ScrollValue();
                margs.ScrollValue.DY = args.ScrollValue.DY * (-1);
                currentEvents.TryAdd(InputEventType.MouseScroll, margs);
                UpdateEventInfo();
            };
            EventKeyPress += (sender, args) => {
                currentEvents.Remove(InputEventType.KeyRelease);
                currentEvents.TryAdd(InputEventType.KeyPress, args);
                UpdateEventInfo();
            };
            EventKeyRelease += (sender, args) => {
                currentEvents.Remove(InputEventType.KeyPress);
                currentEvents.TryAdd(InputEventType.KeyRelease, args);
                UpdateEventInfo();
            };

            eventStateInfo.SetFontSize(16);
            eventStateInfo.SetForeground(Palette.Black);
            eventStateInfo.SetTextAlignment(ItemAlignment.Left, ItemAlignment.Bottom);
            AddItem(eventStateInfo);
        }

        private void UpdateEventInfo()
        {
            StringBuilder info = new StringBuilder(GetItemName());
            if (currentEvents.ContainsKey(InputEventType.MousePress))
            {
                info.Append(" MP");
            }
            if (currentEvents.ContainsKey(InputEventType.MouseRelease))
            {
                info.Append(" MC");
            }
            if (currentEvents.ContainsKey(InputEventType.MouseDoubleClick))
            {
                info.Append(" MDC");
            }
            if (currentEvents.ContainsKey(InputEventType.MouseHover))
            {
                info.Append(" MH");
            }
            if (currentEvents.ContainsKey(InputEventType.MouseLeave))
            {
                info.Append(" ML");
            }
            if (currentEvents.ContainsKey(InputEventType.MouseMove))
            {
                Position pos = ((MouseArgs) currentEvents[InputEventType.MouseMove]).Position;
                info.Append(" MM:" + pos.GetX() + "," + pos.GetY());
            }
            if (currentEvents.ContainsKey(InputEventType.MouseScroll))
            {
                ScrollValue scroll = ((MouseArgs) currentEvents[InputEventType.MouseScroll]).ScrollValue;
                info.Append(" MS:" + scroll.DX + "," + scroll.DY);
            }
            if (currentEvents.ContainsKey(InputEventType.KeyPress))
            {
                info.Append(" KP");
            }
            if (currentEvents.ContainsKey(InputEventType.KeyRelease))
            {
                info.Append(" KR");
            }
            eventStateInfo.SetText(info);
        }

        public override void Clear()
        {
            currentEvents.Clear();
            UpdateEventInfo();
        }
    }
}
