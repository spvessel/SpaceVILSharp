using System.Threading;
using System.Drawing;
using SpaceVIL;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;

namespace View
{
    class MainWindow : ActiveWindow
    {
        public void OnButtonActionClick()
        {

        }

        override public void InitWindow()
        {
            SetParameters(nameof(MainWindow), nameof(MainWindow));
            this.SetWidth(500);
            this.SetMinWidth(500);
            this.SetHeight(300);
            this.SetMinHeight(300);
            this.SetWindowTitle(nameof(MainWindow));
            SetAntiAliasingQuality(MSAA.MSAA8x);

            this.IsBorderHidden = true;

            // WindowManager.EnableVSync(-2);
            // WindowManager.SetRenderFrequency(RedrawFrequency.Ultra);
            // WindowManager.SetRenderType(RenderType.Always);

            // this.SetPadding(5, 5, 5, 5);
            // this.SetBackground(Color.Transparent);
            // this.IsTransparent = true;

            this.SetPadding(8, 8, 8, 8);
            this.SetBackground(Color.FromArgb(200, 0, 162, 232));
            this.SetBorderRadius(10);
            // this.SetAntiAliasingQuality(MSAA.MSAA8x);
            this.IsTransparent = true;
            // IsOutsideClickClosable = true;

            // this.EventClose = null;
            // this.EventClose += () =>
            // {
            //     MessageItem msg = new MessageItem("Close?", "Are You sure?");
            //     msg.GetCancelButton().SetVisible(false);
            //     msg.OnCloseDialog += () =>
            //     {
            //         Console.WriteLine(msg.GetResult() + " " + msg.GetUserButtonResult());
            //         if (msg.GetResult())
            //             this.Close();
            //     };

            //     msg.Show(this);
            // };

            Bitmap image = new Bitmap("D:\\spimages.png");
            // Glfw3.Glfw.Image cursor;
            // cursor.Width = image.Width;
            // cursor.Height = image.Height;
            // List<byte> _map = new List<byte>();
            // for (int j = 0; j < image.Height; j++)
            // {
            //     for (int i = 0; i < image.Width; i++)
            //     {
            //         Color pixel = image.GetPixel(i, j);
            //         _map.Add(pixel.R);
            //         _map.Add(pixel.G);
            //         _map.Add(pixel.B);
            //         _map.Add(pixel.A);
            //     }
            // }
            // cursor.Pixels = _map.ToArray();
            // Glfw3.Glfw.Cursor userCursor = Glfw3.Glfw.CreateCursor(cursor, 0, 0);

            // Frame layout = new Frame();
            // layout.SetShadow(7, 3, 3, Color.FromArgb(255, 0, 0, 0));
            // layout.SetBackground(45, 45, 45, 255);
            // layout.SetPadding(5, 5, 5, 5);
            // layout.SetBorderRadius(10);
            // AddItem(layout);

            //DragAnchor
            // TitleBar title = new TitleBar("Main Window King");
            TitleBar title = new TitleBar("Owls. Your own libs");
            title.SetBorderRadius(new CornerRadius(7, 7, 0, 0));
            this.AddItem(title);

            //Frame
            Frame border = new Frame();
            border.SetBackground(Color.FromArgb(255, 71, 71, 71));
            border.SetItemName("Container");
            border.SetMargin(0, 30, 0, 0);
            border.SetWidthPolicy(SizePolicy.Expand);
            border.SetHeightPolicy(SizePolicy.Expand);
            border.SetBorderRadius(new CornerRadius(0, 0, 7, 7));
            this.AddItem(border);

            //Grid
            Grid grid = new Grid(2, 3);
            grid.SetSpacing(6, 6);
            grid.SetMargin(10, 10, 10, 10);
            border.AddItem(grid);

            //Style
            Style style = Style.GetButtonCoreStyle();
            style.Background = Color.FromArgb(255, 13, 176, 255);
            style.Foreground = Color.Black;
            style.Border.SetRadius(new CornerRadius(6));
            style.Font = DefaultsService.GetDefaultFont(FontStyle.Regular, 18);
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            style.TextAlignment = ItemAlignment.HCenter | ItemAlignment.VCenter;

            /*
            style.ItemStates.Add(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(60, 255, 255, 255)
            });
            */
            //Button Action
            ButtonCore btn_layout = new ButtonCore("Layout");
            // MyWidget btn_layout = new MyWidget();
            // btn_layout.SetCursor(EmbeddedCursor.Hand);
            btn_layout.SetCursor(image, 50, 50);
            btn_layout.SetToolTip("Show LayoutTest window.");
            btn_layout.SetItemName("Layout");
            btn_layout.SetStyle(style);
            // btn_layout.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            // btn_layout.SetSize(50, 50);
            // btn_layout.SetMargin(50, 50, 50, 50);
            btn_layout.SetToolTip("Show window Layout.\nIn there you can test ListBox.");
            // btn_layout.SetCustomFigure(new CustomFigure(false, GraphicsMathService.GetRoundSquare(new CornerRadius(0, 50, 50, 0))));
            btn_layout.SetBackground(Color.FromArgb(255, 255, 151, 153));
            btn_layout.Effects().Add(new Shadow(5, new Position(), new SpaceVIL.Core.Size(6, 6), Color.FromArgb(180, 255, 0, 255)));

            ItemState state = new ItemState(Color.FromArgb(40, 255, 255, 255));
            state.Border.SetThickness(2);
            state.Border.SetColor(Color.FromArgb(125, 255, 255, 255));
            state.Border.SetRadius(new CornerRadius(30, 12, 6, 6));

            btn_layout.AddItemState(ItemStateType.Hovered, state);

            btn_layout.EventMouseClick += (sender, args) =>
            {
                // WindowsBox.GetWindowInstance("LayoutsTest")?.Show();
                WindowsBox.GetWindowInstance("InputTest")?.Focus();
            };
            grid.InsertItem(btn_layout, 0, 0);

            //Button Settings
            ButtonCore btn_settings = new ButtonCore("Settings");
            btn_settings.SetItemName("Settings");
            btn_settings.SetToolTip("Show Settings window.");
            btn_settings.SetStyle(style);
            btn_settings.SetToolTip("Show window Settings");
            btn_settings.SetBackground(Color.FromArgb(255, 255, 181, 111));
            btn_settings.EventMouseClick += (sender, args) =>
            {
                WindowsBox.GetWindowInstance("Settings")?.Show();
            };
            ItemState hovered = new ItemState(Color.FromArgb(60, 255, 255, 255));
            hovered.Border = new Border(Color.White, new CornerRadius(50, 0, 0, 50), 5);
            btn_settings.AddItemState(ItemStateType.Hovered, hovered);
            btn_settings.Effects().Add(new Shadow(5, new Position(), new SpaceVIL.Core.Size(6, 6), Color.FromArgb(180, 255, 0, 255)));
            grid.InsertItem(btn_settings, 0, 1);

            //Button Inputs
            ButtonCore btn_input = new ButtonCore("Inputs");
            btn_input.SetItemName("Inputs");
            btn_input.SetToolTip("Show Input window.");
            btn_input.SetStyle(style);
            btn_input.SetBackground(121, 223, 152);
            btn_input.SetToolTip("Show window Input");
            btn_input.EventMouseClick += (sender, args) =>
            {
                WindowsBox.GetWindowInstance("InputTest")?.Show();
            };
            btn_input.SetCustomFigure(new Figure(false, GraphicsMathService.GetStar(100, 50, 10)));

            ItemState ellipseState = new ItemState(Color.FromArgb(10, 162, 232));
            ellipseState.Shape = new Figure(false, GraphicsMathService.GetEllipse(100, 32));
            btn_input.AddItemState(ItemStateType.Hovered, ellipseState);
            btn_input.Effects().Add(new Shadow(5, new Position(), new SpaceVIL.Core.Size(6, 6), Color.FromArgb(180, 255, 0, 255)));
            grid.InsertItem(btn_input, 0, 2);

            //LabelTest
            ButtonCore btn_label = new ButtonCore("Containers");
            btn_label.SetItemName("Containers");
            btn_label.SetToolTip("Show SplitAreaTest window.");
            btn_label.SetStyle(style);
            btn_label.SetBackground(111, 181, 255);
            btn_label.SetToolTip("Show window Label");
            btn_label.EventMouseClick += (sender, args) =>
            {
                WindowsBox.GetWindowInstance("Containers")?.Show();
            };
            btn_label.Effects().Add(new Shadow(5, new Position(), new SpaceVIL.Core.Size(6, 6), Color.FromArgb(180, 255, 0, 255)));
            grid.InsertItem(btn_label, 1, 0);

            //ImageTest
            ButtonCore btn_image = new ButtonCore("Image");
            btn_image.SetItemName("Image");
            btn_image.SetToolTip("Show ImageTest window.");
            btn_image.SetStyle(style);
            btn_image.SetBackground(238, 174, 128);
            btn_image.SetToolTip("Show window Image");
            btn_image.EventMouseClick += (sender, args) =>
            {
                WindowsBox.GetWindowInstance("ImageTest")?.Show();
            };
            btn_image.Effects().Add(new Shadow(5, new Position(), new SpaceVIL.Core.Size(6, 6), Color.FromArgb(180, 255, 0, 255)));
            grid.InsertItem(btn_image, 1, 1);

            //Flow
            ButtonCore btn_flow = new ButtonCore("Flow");
            btn_flow.SetItemName("Flow");
            btn_flow.SetToolTip("Show FlowTest window.");
            btn_flow.SetStyle(style);
            btn_flow.SetBackground(193, 142, 221);
            btn_flow.SetToolTip("Show window Flow");
            btn_flow.EventMouseClick += (sender, args) =>
            {
                Console.WriteLine("flow show");
                WindowsBox.GetWindowInstance("FlowTest")?.Show();
            };
            btn_flow.Effects().Add(new Shadow(5, new Position(), new SpaceVIL.Core.Size(6, 6), Color.FromArgb(180, 255, 0, 255)));
            grid.InsertItem(btn_flow, 1, 2);

            MenuItem mi1 = new MenuItem("Build Tool");
            mi1.EventMouseClick += (sender, args) => { Console.WriteLine("mi1 click"); };
            MenuItem mi2 = new MenuItem("MenuItem 2");
            mi2.EventMouseClick += (sender, args) => { Console.WriteLine("mi2 click"); };
            MenuItem mi3 = new MenuItem("MenuItem 3");
            mi3.EventMouseClick += (sender, args) => { Console.WriteLine("mi3 click"); };
            MenuItem mi4 = new MenuItem("MenuItem 4");
            ContextMenu menu = new ContextMenu(this, mi1, mi2, mi3, mi4);
            mi4.EventMouseClick += (sender, args) =>
            {
                Console.WriteLine("mi4 click");
                menu.AddItem(new MenuItem("New menuItem"));
            };
            menu.ReturnFocus = btn_flow;
            EventMouseClick += (sender, args) => menu.Show(sender, args);
            MessageBox msg = new MessageBox("Choose one of this:", "Message:");
            EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.Menu)
                {
                    // MouseArgs margs = new MouseArgs();
                    // margs.Button = MouseButton.ButtonRight;
                    // menu.Show(sender, margs);

                    // SetShadeColor(Color.FromArgb(255, 255, 1, 1));
                    ButtonCore btnDontSave = new ButtonCore("Do not save");
                    msg.AddUserButton(btnDontSave, 2); //id must be > 1
                    // ButtonCore btnDontSave2 = new ButtonCore("Do not save2");
                    // msg.AddUserButton(btnDontSave2, 3); //id must be > 1
                    btnDontSave.EventMouseClick += (s, a) =>
                    {
                        Console.WriteLine("btnDontSave is chosen");
                    };
                    msg.OnCloseDialog += (() =>
                    {
                        Console.WriteLine(msg.GetResult() + " " + msg.GetUserButtonResult());
                    });
                    msg.Show();
                    // Console.WriteLine(IsFocused());
                    // Focus();
                }

                if (args.Key == KeyCode.R)
                {
                    // ItemsRefreshManager.PrintSizeOfShapes();
                }

                if (args.Key == KeyCode.Right)
                {
                    Console.WriteLine("right " + mSkew);
                    // Glfw3.Glfw.SetCursorPos(GetGLWID(), mSkew, 20);
                    SetCursorPos(mSkew, 100);
                    mSkew += 50;
                }
                // if (args.Key == KeyCode.V)
                //     SpaceVIL.Common.CommonService.SetClipboardString("SetClipBoardString");
                // if (args.Key == KeyCode.C)
                //     Console.WriteLine(SpaceVIL.Common.CommonService.GetClipboardString());
                // if (args.Key == KeyCode.F)
                //     Console.WriteLine(WindowsBox.GetCurrentFocusedWindow().GetWindowName());
            };

            // btn_flow.SetFocus();

            EventKeyRelease += ((sender, args) =>
            {
                // System.out.println("root is focused");
                if (args.Key == KeyCode.Alpha1)
                {
                    WindowManager.SetRenderType(RenderType.IfNeeded);
                }
                if (args.Key == KeyCode.Alpha2)
                {
                    WindowManager.SetRenderType(RenderType.Periodic);
                }
                if (args.Key == KeyCode.Alpha3)
                {
                    WindowManager.SetRenderType(RenderType.Always);
                }
            });
            // WindowManager.EnableVSync(0);
            // WindowManager.SetRenderType(RenderType.Always);

            // ToolTip.SetStyle(this, Program.Program.GetNewToolTipStyle());
            // ToolTip.AddItems(this, GetDecor());


            // Effects.AddEffect(btn_flow, GetStencilEffect(btn_flow));

            SubtractFigure effect1 = new SubtractFigure(new Figure(true, GraphicsMathService.GetCross(20, 20, 4, 45)));
            // effect1.SetSizeScale(0.2f, 1f);
            effect1.SetPositionOffset(-10, 10);
            effect1.SetAlignment(ItemAlignment.Top | ItemAlignment.Right);
            btn_flow.Effects().Add(effect1);
            effect1.SetApplied(false);
            btn_flow.EventMouseHover += (sender, args) =>
            {
                effect1.SetApplied(true);
            };
            btn_flow.EventMouseLeave += (sender, args) =>
            {
                effect1.SetApplied(false);
            };

            // SubtractFigure effect2 = new SubtractFigure(
            //         new CustomFigure(true, GraphicsMathService.GetEllipse(btn_flow.GetHeight() + 10, btn_flow.GetHeight() + 10, 0, 0, 32)));
            // effect2.SetSizeScale(0.2f, 1f);
            // effect2.SetPositionOffset(btn_flow.GetHeight() / 2 + 5, 0);
            // effect2.SetAlignment(ItemAlignment.Right, ItemAlignment.VCenter);
            // Effects.AddEffect(btn_flow, effect2);
            foreach(var effect in btn_flow.Effects().Get(EffectType.Shadow))
            {
                (effect as Shadow).SetApplied(false);
            }

            EventOnStart += () =>
            {
                // Maximize();
            };

        }
        int mSkew = 0;
        private IBaseItem GetDecor()
        {
            // Ellipse ellipse = new Ellipse(12);
            // ellipse.setSize(8, 8);
            // ellipse.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
            // ellipse.setMargin(10, 0, 0, 0);
            // return ellipse;

            ImageItem image = new ImageItem(
                    DefaultsService.GetDefaultImage(EmbeddedImage.Eye, EmbeddedImageSize.Size32x32), false);
            image.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            image.SetSize(20, 20);
            image.SetAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            image.SetMargin(2, 0, 0, 0);
            image.KeepAspectRatio(true);

            return image;
        }

        private SubtractFigure GetStencilEffect(IBaseItem item)
        {
            List<float[]> triangles = new List<float[]>();
            float side = item.GetHeight() / 4f;
            // 1
            triangles.Add(new float[] { 0, 0 });
            triangles.Add(new float[] { 0, side });
            triangles.Add(new float[] { side, 0 });
            // 2
            triangles.Add(new float[] { item.GetWidth() - side, 0 });
            triangles.Add(new float[] { item.GetWidth(), side });
            triangles.Add(new float[] { item.GetWidth(), 0 });
            // 3
            triangles.Add(new float[] { 0, item.GetHeight() - side });
            triangles.Add(new float[] { 0, item.GetHeight() });
            triangles.Add(new float[] { side, item.GetHeight() });
            //  4
            triangles.Add(new float[] { item.GetWidth(), item.GetHeight() - side });
            triangles.Add(new float[] { item.GetWidth() - side, item.GetHeight() });
            triangles.Add(new float[] { item.GetWidth(), item.GetHeight() });

            triangles.AddRange(GraphicsMathService.GetEllipse(10, 10, 5, (int)(item.GetHeight() / 2f - 5.0f), 12));
            triangles.AddRange(
                    GraphicsMathService.GetEllipse(10, 10, item.GetWidth() - 15, (int)(item.GetHeight() / 2f - 5.0f), 12));
            Figure figure = new Figure(false, triangles);

            SubtractFigure effect = new SubtractFigure(figure);
            return effect;
        }

        [DllImport("user32.dll", SetLastError = true, CharSet = CharSet.Unicode)]
        public static extern bool SetCursorPos(int x, int y);
    }
}
