using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL;
using System.Threading;


namespace View
{
    #region  LabelTest
    //LabelTest
    partial class LabelTest
    {
        WindowLayout wnd_handler;
        public WindowLayout Handler
        {
            get
            {
                return wnd_handler;
            }
            set
            {
                wnd_handler = value;
            }
        }
        private void InitWindow()
        {
            wnd_handler = new WindowLayout(name: nameof(LabelTest));
            wnd_handler.SetWidth(500);
            wnd_handler.SetMinWidth(500);
            wnd_handler.SetHeight(500);
            wnd_handler.SetMinHeight(500);
            wnd_handler.SetWindowTitle(nameof(LabelTest));
            WindowLayoutBox.InitWindow(wnd_handler);
            Handler.SetBackground(Color.FromArgb(255, 76, 76, 76));

            //ToolBar
            VerticalStack layout = new VerticalStack();
            layout.SetPadding(3, 3, 3, 3);
            layout.SetSpacing(vertical: 5);
            layout.SetBackground(255, 255, 255, 20);

            //adding toolbar
            wnd_handler.AddItem(layout);

            //Frame
            HorizontalStack toolbar = new HorizontalStack();
            toolbar.SetBackground(Color.FromArgb(255, 60, 60, 60));
            toolbar.SetItemName(nameof(toolbar));
            toolbar.SetHeight(40);
            toolbar.SetPadding(10);
            toolbar.SetSpacing(10);
            toolbar.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            layout.AddItem(toolbar);

            //Frame
            Frame frame = new Frame();
            frame.SetBackground(Color.FromArgb(255, 51, 51, 51));
            frame.SetItemName("Container");
            frame.SetPadding(15, 15, 15, 15);
            frame.SetWidthPolicy(SizePolicy.Expand);
            frame.SetHeightPolicy(SizePolicy.Expand);
            layout.AddItem(frame);
            /*
            //ProgressBar
            ProgressBar progb = new ProgressBar();
            progb.SetWidthPolicy(SizePolicy.Expand);
            //progb.SetHeightPolicy(SizePolicy.Expand);
            progb.SetMinValue(35);
            progb.SetMaxValue(78);
            layout.AddItem(progb);
            
            //BorderShape
            /*BorderElement be = new BorderElement();
            be.SetBackground(Color.Green);
            be.SetWidthPolicy(SizePolicy.Expand);
            be.SetHeight(50);
            layout.AddItem(be);
            */

            //ContourElement
            ContourElement ce = new ContourElement();
            ce.SetBackground(Color.Green);
            ce.SetHeight(150);
            layout.AddItem(ce);
            
            //Label
            Label label = new Label("вапоэопр");
            label.SetFont(new Font(new FontFamily("Times New Roman"), 16, FontStyle.Regular));
            //label.SetFont(new Font(new FontFamily("Panforte Pro"), 16, FontStyle.Regular));
            label.SetForeground(Color.FromArgb(255, 210, 210, 210));
            label.Border.Radius = 10;
            label.SetBackground(Color.FromArgb(10, 255, 255, 255));
            label.SetHeight(50);
            label.SetAlignment(ItemAlignment.VCenter);
            label.SetTextAlignment(ItemAlignment.VCenter | ItemAlignment.HCenter);
            label.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            label.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(60, 255, 255, 255)
            });

            frame.AddItem(label);

            //style
            Style btn_style = new Style();
            btn_style.Background = Color.FromArgb(255, 13, 176, 255);
            btn_style.BorderRadius = 6;
            btn_style.Width = 30;
            btn_style.Height = 30;
            btn_style.WidthPolicy = SizePolicy.Fixed;
            btn_style.HeightPolicy = SizePolicy.Fixed;
            btn_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            ItemState brighter = new ItemState();
            brighter.Background = Color.FromArgb(125, 255, 255, 255);
            btn_style.ItemStates.Add(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });

            //btn add_at_begin
            ButtonCore show_info = new ButtonCore();
            show_info.SetItemName(nameof(show_info));
            show_info.SetStyle(btn_style);
            show_info.EventMouseClick += (sender) =>
            {
                /*
                Console.WriteLine(label.GetText());
                label.SetForeground(Color.FromArgb(255, 210, 210, 210));
                label.SetFont(new Font(new FontFamily("Calibri"), 30, FontStyle.Italic));

                progb.SetCurrentValue(33);
                */
            };
            toolbar.AddItem(show_info);

            ButtonCore change_color = new ButtonCore();
            change_color.SetItemName(nameof(show_info));
            change_color.SetStyle(btn_style);
            change_color.EventMouseClick += (sender) =>
            {
                
                label.SetForeground(Color.FromArgb(255, 50, 255, 80));
                Console.WriteLine(label.GetForeground());

                //progb.SetCurrentValue(55);
                
            };
            toolbar.AddItem(change_color);

            ButtonCore change_font = new ButtonCore();
            change_font.SetItemName(nameof(show_info));
            change_font.SetStyle(btn_style);
            change_font.EventMouseClick += (sender) =>
            {
                /*
                label.SetFont(new Font(new FontFamily("Open Sans Light"), 30, FontStyle.Bold));
                Console.WriteLine(label.GetFont());

                        for (int i = 35; i <= 78; i++)
                        {
                            progb.SetCurrentValue(i);
                            Thread.Sleep(50);
                        }
*/
            };
            toolbar.AddItem(change_font);
        }

        public void Show()
        {
            wnd_handler.Show();
        }
    }
    #endregion
}
