using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Reflection;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using Contact;
using System.Diagnostics;

namespace View
{
    class LayoutsTest : ActiveWindow
    {
        override public void InitWindow()
        {
            SetParameters(nameof(LayoutsTest), nameof(LayoutsTest));
            this.SetWidth(400);
            this.SetMinWidth(200);
            this.SetHeight(600);
            this.SetMinHeight(200);
            this.SetWindowTitle("Window");
            this.SetPadding(2, 2, 2, 2);
            this.SetBackground(Color.FromArgb(255, 45, 45, 45));
            this.IsBorderHidden = true;

            //DragAnchor
            TitleBar title = new TitleBar(nameof(LayoutsTest));
            this.AddItem(title);

            //Bar
            VerticalStack btn_bar = new VerticalStack();
            btn_bar.SetBackground(Color.FromArgb(255, 45, 45, 45));
            btn_bar.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            btn_bar.SetItemName("ButtonBar");
            btn_bar.SetWidthPolicy(SizePolicy.Expand);
            btn_bar.SetHeightPolicy(SizePolicy.Expand);
            btn_bar.SetSpacing(10, 10);
            btn_bar.SetMargin(0, 30, 0, 0);
            this.AddItem(btn_bar);

            //Frame
            Frame frame = new Frame();
            frame.SetBackground(Color.FromArgb(255, 70, 70, 70));
            frame.SetItemName("Frame");
            frame.SetHeight(40);
            frame.SetPadding(20, 0, 20, 0);
            frame.SetWidthPolicy(SizePolicy.Expand);
            frame.SetHeightPolicy(SizePolicy.Fixed);
            btn_bar.AddItem(frame);

            ListBox listbox = new ListBox();
            // VerticalStack listbox = new VerticalStack();
            listbox.SetMargin(9, 9, 9, 9);
            listbox.SetBackground(Color.FromArgb(255, 70, 70, 70));
            listbox.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            //listbox.SetHScrollBarPolicy(ScrollBarVisibility.Never);
            btn_bar.AddItem(listbox);

            var list = new HashSet<IBaseItem>();

            //button
            ButtonCore up = new ButtonCore();
            up.SetBackground(Color.FromArgb(255, 181, 255, 111));
            up.SetItemName("Up");
            up.SetText("Contact");
            up.SetForeground(Color.Black);
            up.SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.Bottom);
            up.SetFont(new Font(new FontFamily("Courier New"), 14, FontStyle.Regular));
            up.SetWidth(80);
            up.SetHeight(20);
            up.SetWidthPolicy(SizePolicy.Fixed);
            up.SetHeightPolicy(SizePolicy.Fixed);
            up.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            up.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            up.SetBorderRadius(new CornerRadius(10));
            up.EventMouseClick += (sender, args) =>
            {
                listbox.AddItem(new Contact.VisualContact());
                // list.Add(new Contact.VisualContact());
            };
            frame.AddItem(up);

            ButtonCore down = new ButtonCore();
            down.SetBackground(Color.FromArgb(255, 181, 111, 255));
            down.SetItemName("Down");
            down.SetText("Show");
            down.SetForeground(Color.Black);
            down.SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.Bottom);
            down.SetFont(new Font(new FontFamily("Courier New"), 14, FontStyle.Regular));
            down.SetWidth(80);
            down.SetHeight(20);
            down.SetWidthPolicy(SizePolicy.Fixed);
            down.SetHeightPolicy(SizePolicy.Fixed);
            down.SetAlignment(ItemAlignment.Right | ItemAlignment.VCenter);
            down.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            down.SetBorderRadius(new CornerRadius(10));
            down.EventMouseClick += (sender, args) =>
            {
                //PrintService.PrintList(listbox.GetItems());
                /*Type myType = Type.GetType(typeof(SpaceVIL.RadioButton).ToString());
                var field = myType.GetField("count", BindingFlags.NonPublic | BindingFlags.Static);
                Console.WriteLine((int)field.GetValue(null));*/
                //listbox.SetHScrollBarVisible(ScrollBarVisibility.Never);
                //listbox.SetVScrollBarVisible(ScrollBarVisibility.Never);
                listbox.Clear();
                // list.Clear();
                GC.Collect();
                GC.WaitForPendingFinalizers();
                GC.Collect();
            };
            frame.AddItem(down);

            ButtonCore btn = new ButtonCore();
            btn.SetBackground(Color.FromArgb(255, 255, 181, 111));
            btn.SetItemName("Add");
            btn.SetText("Radio");
            btn.SetFont(new Font(new FontFamily("Courier New"), 14, FontStyle.Regular));
            btn.SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.Bottom);
            btn.SetForeground(Color.Black);
            btn.SetWidth(80);
            btn.SetHeight(20);
            btn.SetWidthPolicy(SizePolicy.Fixed);
            btn.SetHeightPolicy(SizePolicy.Fixed);
            btn.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            btn.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            btn.SetBorderRadius(new CornerRadius(10));

            btn.EventMouseClick += (sender, args) =>
            {
                // RadioButton radio = new RadioButton();
                // radio.SetText("Another radio button for testing.");
                // radio.SetFont(new Font(new FontFamily("Courier New"), 16, FontStyle.Regular));
                // radio.SetAlignment(ItemAlignment.Top | ItemAlignment.Left);
                // radio.SetHeightPolicy(SizePolicy.Fixed);
                // radio.SetWidthPolicy(SizePolicy.Expand);
                // radio.SetHeight(20);

                // listbox.AddItem(radio);
                // Console.WriteLine("Action");
                listbox.Clear();
                List<IBaseItem> content = new List<IBaseItem>();
                for (int i = 0; i < 100; i++)
                    content.Add(new VisualContact());
                Stopwatch stopWatch = new Stopwatch();
                stopWatch.Start();
                listbox.SetListContent(content);

                // for (int i = 0; i < 10000; i++)
                // {
                //     listbox.AddItem(new VisualContact());
                // }

                stopWatch.Stop();
                TimeSpan ts = stopWatch.Elapsed;
                Console.WriteLine(stopWatch.Elapsed.TotalMilliseconds);
                GC.Collect();

                // int index = 0;
                // foreach (var item in ItemsLayoutBox.GetLayoutItems(GetWindowGuid()))
                // {
                //     if (item is SelectionItem)
                //         index++;
                // }
                // Console.WriteLine(index);

                // list.Clear();
                // for (int i = 0; i < 100000; i++)
                // {
                //     list.Add(new VisualContact());
                // }
                // GC.Collect();

                // listbox.GetArea().PrintListeners();
                // listbox.PrintListeners();

            };
            frame.AddItem(btn);
            // for (int i = 0; i < 10000; i++)
            // {
            //     listbox.AddItem(new VisualContact());
            // }

            EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.Delete)
                {
                    listbox.GetParent().RemoveItem(listbox);
                    GC.Collect();
                }
            };

            // for (int i = 0; i < 8; i++)
            // {
            //     btn.EventMouseClick.Invoke(btn, null);
            // }
            // listbox.GetParent().RemoveItem(listbox);
            // GC.Collect();
        }
    }

}
