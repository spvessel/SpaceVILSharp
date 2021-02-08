using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using SpaceVIL.Common;
using System.Threading;

namespace View
{
    class InputTest : ActiveWindow
    {
        override public void InitWindow()
        {
            SetParameters(name: nameof(InputTest), title: nameof(InputTest));
            this.SetWidth(300);
            // this.SetMinWidth(200);
            this.SetHeight(500);
            // this.SetMinHeight(200);
            this.SetWindowTitle(nameof(InputTest));
            this.SetPadding(2, 2, 2, 2);
            this.SetBackground(Color.FromArgb(255, 32, 32, 32));

            EventKeyPress += (sender, args) =>
            {
                // if (args.Key == KeyCode.C)
                //     Console.WriteLine(SpaceVIL.Common.CommonService.GetClipboardString());
                // if (args.Key == KeyCode.F)
                //     Console.WriteLine(WindowsBox.GetCurrentFocusedWindow().GetWindowName());
            };
            // this.IsBorderHidden = true;

            //DragAnchor
            // TitleBar title = new TitleBar(nameof(InputTest));
            // this.AddItem(title);

            //ToolBar
            VerticalStack layout = new VerticalStack();
            layout.SetMargin(0, 30, 0, 30);
            layout.SetSpacing(vertical: 5);
            layout.SetBackground(255, 255, 255, 20);

            //adding toolbar
            // this.AddItem(layout);

            //Frame
            // HorizontalStack toolbar = new HorizontalStack();
            // toolbar.SetBackground(Color.FromArgb(255, 60, 60, 60));
            // toolbar.SetItemName(nameof(toolbar));
            // toolbar.SetHeight(40);
            // toolbar.SetPadding(10);
            // toolbar.SetSpacing(10);
            // toolbar.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            // layout.AddItem(toolbar);

            // //Frame
            VerticalStack frame = new VerticalStack();
            frame.SetBackground(Color.FromArgb(255, 51, 51, 51));
            frame.SetItemName("Container");
            frame.SetPadding(15, 15, 15, 15);
            frame.SetWidthPolicy(SizePolicy.Expand);
            frame.SetHeightPolicy(SizePolicy.Expand);
            frame.SetSpacing(0, 10);
            AddItem(frame);



            // Grid listbox = new Grid(6, 1);
            // listbox.SetSpacing(0, 5);
            // listbox.SetHeight(500);
            // //listbox.SetBackground(Color.FromArgb(255, 70, 70, 70));
            // listbox.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            // //listbox.SetSelectionVisibility(false);
            // //listbox.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            // frame.AddItem(listbox);

            PasswordLine _editLines = new PasswordLine();
            // _editLines.SetFont(new Font(new FontFamily("Arial"), 16, FontStyle.Regular));
            // _editLines.SetBackground(Color.Transparent);
            // _editLines.SetAlignment(ItemAlignment.Top);
            // _editLines.SetHeight(50);
            // _editLines.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            // _editLines.SetSpacing(0, 5);
            // _editLines.SetPadding(2, 2, 2, 2);
            // _editLines.SetForeground(Color.White);
            // _editLines.SetTextAlignment(ItemAlignment.Right);
            frame.AddItem(_editLines);

            SpinItem spinItem = new SpinItem();
            frame.AddItem(spinItem);

            TextEdit textEdit = new TextEdit();
            textEdit.SetTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            frame.AddItem(textEdit);

            TextArea _editBlock = new TextArea();
            _editBlock.SetScrollStepFactor(3);
            // _editBlock.SetWrapText(true);

            // TextView _editBlock = new TextView();
            // _editBlock.SetMargin(9, 9, 9, 9);
            // _editBlock.SetEditable(false);
            // _editBlock.SetFont(new Font(new FontFamily("Arial"), 16, FontStyle.Regular));
            //_editLines.SetBackground(Color.Transparent);
            // _editBlock.SetAlignment(ItemAlignment.Top);
            // _editBlock.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            // //_editBlock.SetSpacing(0, 5);
            // _editBlock.SetPadding(2, 2, 2, 2);
            // _editBlock.SetHeight(500);
            // //_editLines.SetForeground(Color.White);
            // //_editLines.SetTextAlignment(ItemAlignment.Right);
            // //frame.AddItem(_editLines);
            // // listbox.AddItem(_editBlock);
            // _editBlock.SetStyle(Style.GetTextAreaStyle());
            //_editBlock.IsFocusable = true;
            // _editBlock.OnTextChanged += () => { Console.WriteLine("Text changed"); };
            // _editBlock.EventKeyPress += (sender, args) =>
            // {
            //     if (args.Key == KeyCode.S && args.Mods == KeyMods.Control)
            //         _editBlock.AppendText("appended text line...\n");
            //     else if (args.Key == KeyCode.R && args.Mods == KeyMods.Control)
            //         _editBlock.SetText("SetText...");
            // };

            // SpinItem sp = new SpinItem();
            // sp.SetParameters(1, -5.5, 7, 0.51);
            // sp.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            // sp.SetHeight(50);
            // listbox.AddItem(sp);

            // //style
            // Style btn_style = new Style();
            // btn_style.Background = Color.FromArgb(255, 13, 176, 255);
            // btn_style.BorderRadius = new CornerRadius(6);
            // btn_style.Width = 30;
            // btn_style.Height = 30;
            // btn_style.WidthPolicy = SizePolicy.Fixed;
            // btn_style.HeightPolicy = SizePolicy.Fixed;
            // btn_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            // ItemState brighter = new ItemState();
            // brighter.Background = Color.FromArgb(125, 255, 255, 255);
            // /*
            // btn_style.ItemStates.Add(ItemStateType.Hovered, new ItemState()
            // {
            //     Background = Color.FromArgb(125, 255, 255, 255)
            // });
            // */
            // //btn add_at_begin
            // ButtonCore show_info = new ButtonCore();
            // show_info.SetToolTip("info");
            // show_info.SetItemName(nameof(show_info));
            // show_info.SetStyle(btn_style);
            // show_info.EventMouseClick += (sender, args) =>
            // {
            //     //Console.WriteLine(pwd.GetPassword());
            //     //_editLines.SetText("123\ner");
            //     //_editLines.IsEditable = false;
            //     //_editLines.ShowPassword();
            // };
            // toolbar.AddItem(show_info);

            // ButtonCore change_color = new ButtonCore();
            // change_color.SetToolTip("Clear");
            // change_color.SetItemName(nameof(change_color));
            // change_color.SetStyle(btn_style);
            // change_color.EventMouseClick += (sender, args) =>
            // {
            //     _editLines.Clear();
            // };
            // toolbar.AddItem(change_color);

            // ButtonCore change_font = new ButtonCore();
            // change_font.SetItemName(nameof(change_font));
            // change_font.SetStyle(btn_style);
            // change_font.EventMouseClick += (sender, args) =>
            // {
            //     //if (_editLines.IsEditable())
            //     //    _editLines.SetEditable(false);
            //     //else
            //     //    _editLines.SetEditable(true);
            //     // _editLines.AppendText("123");
            //     _editBlock.AppendText("qwerty");

            // };
            // toolbar.AddItem(change_font);

            // Label tl = new Label("\na\na\na\na\n\na\na\na\n");
            // // layout.AddItem(tl);
            // tl.SetBackground(255, 255, 255, 100);
            // tl.SetItemName("InputTestLabel");
            // tl.SetText("qwertyu\niopasd\nfghjklz\nxcvbnm1\n234567890");
            // tl.SetFontSize(17);
            // tl.SetForeground(Color.White);
            // tl.SetTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            // tl.SetTextAlignment(ItemAlignment.HCenter, ItemAlignment.Top);
            // tl.SetMargin(20, 20, 20, 20);
            // // List<int> nullist = null;
            // // List<int> list = new List<int>(nullist);
            // Console.WriteLine("here ");

            // _editBlock.AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(30, 255, 255, 255)));

            // _editBlock.SetHeight(100);
            // _editBlock.SetHeightPolicy(SizePolicy.Fixed);

            // _editBlock.SetMargin(10, 30, 10, 10);
            // _editBlock.SetPadding(5, 0, 5, 0);

            // _editBlock.SetBorderThickness(1);
            // _editBlock.SetBorderFill(Color.Gray);

            // _editBlock.SetForeground(Color.Black);
            // _editBlock.SetFontSize(15);

            // layout.AddItems(
            // _editLines, 
            // _editBlock);

            // _editBlock.SetText("きっかけが何だっ");

            frame.AddItem(_editBlock);


            // _editBlock.EventKeyRelease += (sender, args) =>
            // {
            //     if (args.Mods == KeyMods.Control)
            //     {
            //         if (args.Key == KeyCode.Equal)
            //         {
            //             int fontSize = (int)_editBlock.GetFont().Size;
            //             fontSize++;
            //             Console.WriteLine(fontSize);
            //             if (fontSize > 32)
            //                 return;
            //             _editBlock.SetFontSize(fontSize);
            //         }
            //         if (args.Key == KeyCode.Minus)
            //         {
            //             int fontSize = (int)_editBlock.GetFont().Size;
            //             fontSize--;
            //             Console.WriteLine(fontSize);
            //             if (fontSize < 10)
            //                 return;
            //             _editBlock.SetFontSize(fontSize);
            //         }
            //     }
            // };
            _editBlock.SetWrapText(true);
            // _editBlock.SetStyle(getTextAreaStyle());
            _editBlock.SetText(GetBigText());

            // _editBlock.RewindText();
            _editBlock.SetFocus();
            // _editBlock.SetMargin(0, 0, 0, 40);

            // ButtonCore btn = new ButtonCore("Wrap Text");
            // btn.SetSize(100, 30);
            // btn.SetAlignment(ItemAlignment.Bottom, ItemAlignment.Left);
            // btn.SetMargin(5, 0, 0, 5);
            // btn.EventMouseClick += (sender, args) =>
            // {
            //     _editBlock.SetWrapText(!_editBlock.IsWrapText());
            //     if (_editBlock.IsWrapText())
            //     {
            //         // btn.EventMouseClick.EndInvoke();
            //     }
            // };
            // btn.EventMouseClick += (sender, args) =>
            // {
            //     Console.WriteLine("continue with print this text.");
            // };
            // AddItem(btn);
            // Console.WriteLine("window " + GetWidth() + "; area " + _editBlock.GetWidth() + "; text " + _editBlock.GetTextWidth());

            // Console.WriteLine("text width " + _editBlock.GetTextWidth());

            EventOnStart += () =>
            {
                // _editBlock.SetText(GetBigText());
                // SetWidth(GetWidth() + 1);
            };

            _editBlock.EventKeyPress += ((sender, args) =>
            {
                if (args.Mods == KeyMods.Control && args.Key == KeyCode.Equal)
                {
                    int fontSize = (int)_editBlock.GetFont().Size;
                    fontSize++;
                    if (fontSize > 32)
                        return;
                    _editBlock.SetFontSize(fontSize);
                }
                else if (args.Mods == KeyMods.Control && args.Key == KeyCode.Minus)
                {
                    int fontSize = (int)_editBlock.GetFont().Size;
                    fontSize--;
                    if (fontSize < 10)
                        return;
                    _editBlock.SetFontSize(fontSize);
                }
            });
        }

        private String GetBigText()
        {
            return "ajfh gajhdif uahwoieh foiawoe ifisdfghaoisiuehgi ouaoesijfoaiehfouia shueighaoweigh1"
                    + "ajfhgajhdif uahwoiehfoiaw oeifis dfghaoi siuehgiouaoesi jfoaiehfouiashu eighaoweigh2 "
                    + "ajfhg ajhdifuahwoiehf oiawoei fisdfghaois iuehgiouaoesijfo aiehfouiashueighaow eigh3 "
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh4 "
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh5\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh6\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                    + "ajfhgaj hdifuahwoiehfo iawoeifisdfghaoisiue hgiouaoesijf oaiehfouias hueighaoweigh\n" + "";
        }

        public static Style GetTextAreaStyle()
        {
            Style style = new Style();
            style.Background = Color.FromArgb(70, 70, 70);
            style.Font = DefaultsService.GetDefaultFont(14);
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            Style text_style = Style.GetTextBlockStyle();
            text_style.Font = DefaultsService.GetDefaultFont(14);
            text_style.Foreground = Color.FromArgb(210, 210, 210);
            text_style.GetInnerStyle("selection").Background = Color.FromArgb(25, 255, 255, 255);
            text_style.GetInnerStyle("cursor").Background = Color.FromArgb(10, 162, 232);
            text_style.GetInnerStyle("selection").SetAlignment(ItemAlignment.Left, ItemAlignment.Top);
            style.AddInnerStyle("textedit", text_style);

            Style vsb_style = Style.GetSimpleVerticalScrollBarStyle();
            vsb_style.Alignment = ItemAlignment.Right | ItemAlignment.Top;
            vsb_style.Width = 10;
            vsb_style.SetPadding(0, 0, 0, 0);
            vsb_style.GetInnerStyle("slider").SetBackground(60, 60, 60);
            vsb_style.GetInnerStyle("slider").AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(40, 0, 0, 0)));
            vsb_style.GetInnerStyle("slider").Border.SetRadius(new CornerRadius(5));
            vsb_style.GetInnerStyle("slider").SetPadding(0, 2, 0, 2);
            vsb_style.GetInnerStyle("slider").GetInnerStyle("handler").SetMargin(2, 0, 2, 0);
            vsb_style.GetInnerStyle("slider").GetInnerStyle("handler")
                    .SetShadow(new Shadow(8, Color.FromArgb(0, 0, 0)));
            style.AddInnerStyle("vscrollbar", vsb_style);

            Style hsb_style = Style.GetSimpleHorizontalScrollBarStyle();
            hsb_style.Alignment = ItemAlignment.Left | ItemAlignment.Bottom;
            hsb_style.Height = 10;
            hsb_style.SetMargin(0, 0, 0, 5);
            hsb_style.GetInnerStyle("slider").SetBackground(60, 60, 60);
            hsb_style.GetInnerStyle("slider").AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(30, 0, 0, 0)));
            hsb_style.GetInnerStyle("slider").Border.SetRadius(new CornerRadius(5));
            hsb_style.GetInnerStyle("slider").SetPadding(2, 0, 2, 0);
            hsb_style.GetInnerStyle("slider").GetInnerStyle("handler").SetMargin(0, 2, 0, 2);
            style.AddInnerStyle("hscrollbar", hsb_style);

            Style menu_style = new Style();
            menu_style.Background = Color.FromArgb(50, 50, 50);
            menu_style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            menu_style.SetAlignment(ItemAlignment.Right, ItemAlignment.Bottom);
            style.AddInnerStyle("menu", menu_style);

            return style;
        }
    }
}
