using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Drawing;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace View
{
    class LabelTest : ActiveWindow
    {
        override public void InitWindow()
        {
            SetParameters(nameof(LabelTest), nameof(LabelTest));
            this.SetWidth(500);
            this.SetMinWidth(500);
            this.SetHeight(500);
            this.SetMinHeight(500);
            this.SetWindowTitle(nameof(LabelTest));
            this.SetPadding(2, 2, 2, 2);
            this.SetBackground(Color.FromArgb(255, 45, 45, 45));
            this.IsBorderHidden = true;

            //DragAnchor
            TitleBar title = new TitleBar(nameof(LabelTest));
            this.AddItem(title);

            //ToolBar
            VerticalStack layout = new VerticalStack();
            layout.SetPadding(3, 3, 3, 3);
            layout.SetMargin(0, 30, 0, 0);
            layout.SetSpacing(vertical: 5);
            layout.SetBackground(255, 255, 255, 20);

            //adding toolbar
            this.AddItem(layout);

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

            //ProgressBar
            ProgressBar progb = new ProgressBar();
            progb.SetWidthPolicy(SizePolicy.Expand);
            //progb.SetHeightPolicy(SizePolicy.Expand);
            progb.SetMinValue(35);
            progb.SetMaxValue(78);
            layout.AddItem(progb);
            /*
            //BorderShape
            /*BorderElement be = new BorderElement();
            be.SetBackground(Color.Green);
            be.SetWidthPolicy(SizePolicy.Expand);
            be.SetHeight(50);
            layout.AddItem(be);
            */

            Label shadow = new Label("Hello world 12345!");
            shadow.SetFont(new Font(new FontFamily("Courier New"), 30, FontStyle.Regular));
            shadow.SetForeground(Color.Black);
            shadow.SetBorderRadius(new CornerRadius(10));
            //shadow.SetBackground(Color.White);
            shadow.SetHeight(50);
            shadow.SetMargin(2, 2, 0, 0);
            shadow.SetAlignment(ItemAlignment.VCenter | ItemAlignment.HCenter);
            shadow.SetTextAlignment(ItemAlignment.VCenter | ItemAlignment.HCenter);
            shadow.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);

            //Label
            Label label = new Label("Hello world 12345!");
            label.SetFont(new Font(new FontFamily("Courier New"), 30, FontStyle.Regular));
            label.SetForeground(Color.FromArgb(255, 210, 210, 210));
            label.SetBorderRadius(new CornerRadius(10));
            label.SetHeight(50);
            label.SetAlignment(ItemAlignment.VCenter | ItemAlignment.HCenter);
            label.SetTextAlignment(ItemAlignment.VCenter | ItemAlignment.HCenter);
            label.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);

            frame.AddItems(shadow, label);

            //style
            Style btn_style = new Style();
            btn_style.Background = Color.FromArgb(255, 13, 176, 255);
            btn_style.Border.SetRadius(new CornerRadius(6));
            btn_style.Width = 30;
            btn_style.Height = 30;
            btn_style.WidthPolicy = SizePolicy.Fixed;
            btn_style.HeightPolicy = SizePolicy.Fixed;
            btn_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            ItemState brighter = new ItemState();
            brighter.Background = Color.FromArgb(125, 255, 255, 255);
            /*
            btn_style.ItemStates.Add(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            */
            //btn add_at_begin
            ButtonCore show_info = new ButtonCore();
            show_info.SetItemName(nameof(show_info));
            show_info.SetStyle(btn_style);
            show_info.EventMouseClick += (sender, args) =>
            {

                Console.WriteLine(label.GetText());
                label.SetForeground(Color.FromArgb(255, 210, 210, 210));
                label.SetFont(new Font(new FontFamily("Courier New"), 30, FontStyle.Italic));

                progb.SetCurrentValue(33);

            };
            toolbar.AddItem(show_info);

            ButtonCore change_color = new ButtonCore();
            change_color.SetItemName(nameof(show_info));
            change_color.SetStyle(btn_style);
            change_color.EventMouseClick += (sender, args) =>
            {

                label.SetForeground(Color.FromArgb(255, 50, 255, 80));
                Console.WriteLine(label.GetForeground());

                progb.SetCurrentValue(55);

            };
            toolbar.AddItem(change_color);

            ButtonCore change_font = new ButtonCore();
            change_font.SetItemName(nameof(show_info));
            change_font.SetStyle(btn_style);
            change_font.EventMouseClick += (sender, args) =>
            {
                //shadow.IsVisible = !shadow.IsVisible;
                //label.SetFont(new Font(new FontFamily("Open Sans Light"), 30, FontStyle.Bold));
                /*Thread tr = new Thread(() =>
                {
                    for (int i = 35; i <= 78; i++)
                    {
                        progb.SetCurrentValue(i);
                        Thread.Sleep(50);
                    }
                });
                tr.Start();*/
            };
            toolbar.AddItem(change_font);
        }
    }
}
