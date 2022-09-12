using SpaceVIL;
using SpaceVIL.Core;
using View.Decorations;

namespace View.Components
{
    public class EventsAndRoutingView : VerticalStack
    {
        private BlankItem a = null;
        private BlankItem b = null;
        private BlankItem c = null;

        public override void InitElements()
        {
            a = new EventItem("A:");
            b = new EventItem("B:");
            c = new EventItem("C:");

            ComboBox lockEventModes = new ComboBox(
                MakeOption("Pass ALL events", () => {
                    EnableEvents(true);
                }),
                MakeOption("Don't pass ALL events", () => {
                    EnableEvents(false);
                }),
                MakeOption("Pass only MousePress events", () => {
                    SetPassEvents(InputEventType.MousePress);
                }),
                MakeOption("Pass only MouseClick events", () => {
                    SetPassEvents(InputEventType.MouseRelease);
                }),
                MakeOption("Pass only MouseDoubleClick events", () => {
                    SetPassEvents(InputEventType.MouseDoubleClick);
                }),
                MakeOption("Pass only MouseHover events", () => {
                    SetPassEvents(InputEventType.MouseHover);
                }),
                MakeOption("Pass only MouseLeave events", () => {
                    SetPassEvents(InputEventType.MouseLeave);
                }),
                MakeOption("Pass only MouseMove events", () => {
                    SetPassEvents(InputEventType.MouseMove);
                }),
                MakeOption("Pass only MouseScroll events", () => {
                    SetPassEvents(InputEventType.MouseScroll);
                }),
                MakeOption("Pass only KeyPress events", () => {
                    SetPassEvents(InputEventType.KeyPress);
                }),
                MakeOption("Pass only KeyRelease events", () => {
                    SetPassEvents(InputEventType.KeyRelease);
                })
            );
            lockEventModes.SelectionChanged += () => {
                a.Clear();
                b.Clear();
                c.Clear();
            };
            lockEventModes.SetMaxSize(500, 35);
            lockEventModes.SetMargin(50, 0, 50, 0);
            lockEventModes.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

            Frame layout = new Frame();
            layout.SetBackground(Palette.WhiteGlass);
            layout.SetMargin(50, 50, 50, 50);
            
            AddItems(ComponentsFactory.MakeHeaderLabel("Events & Routing:"), lockEventModes, layout);
            layout.AddItems(c);
            c.AddItem(b);
            b.AddItem(a);

            lockEventModes.SetCurrentIndex(0);
        }

        private MenuItem MakeOption(string name, EventCommonMethod action)
        {
            MenuItem item =  new MenuItem(name);
            item.EventMouseClick += (sender, args) => {
                action.Invoke();
            };
            return item;
        }

        private void SetPassEvents(InputEventType e)
        {
            a.SetPassEvents(false);
            b.SetPassEvents(false);
            c.SetPassEvents(false);

            a.SetPassEvents(true, e);
            b.SetPassEvents(true, e);
            c.SetPassEvents(true, e);
        }

        private void EnableEvents(bool value)
        {
            a.SetPassEvents(value);
            b.SetPassEvents(value);
            c.SetPassEvents(value);
        }
    }
}