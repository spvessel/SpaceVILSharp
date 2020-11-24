using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using System.Threading;
// using RadialMenu;

namespace View
{
    class FlowTest : ActiveWindow
    {
        override public void InitWindow()
        {
            // SetRenderFrequency(RedrawFrequency.Ultra);
            SetParameters(name: nameof(FlowTest), title: nameof(FlowTest));
            //window's attr
            SetWindowTitle(nameof(FlowTest));

            SetWidth(860);
            SetMinWidth(500);
            SetHeight(850);
            SetMinHeight(500);
            SetMaxSize(1000, 1000);

            SetPadding(2, 2, 2, 2);
            SetBackground(Color.FromArgb(255, 45, 45, 45));
            IsBorderHidden = true;
            IsCentered = true;
            // IsAlwaysOnTop = true;

            Bitmap image = new Bitmap("D:\\icon.png");
            Bitmap image2 = new Bitmap("D:\\icon.png");
            SetIcon(image, image2);

            EventKeyRelease += (sender, args) =>
            {
                if (args.Key == KeyCode.F11)
                    ToggleFullScreen();
            };

            //DragAnchor
            TitleBar title = new TitleBar(nameof(FlowTest));
            title.SetIcon(image, 20, 20);
            this.AddItem(title);

            //ToolBar
            VerticalStack layout = new VerticalStack();
            layout.SetAlignment(ItemAlignment.Top | ItemAlignment.HCenter);
            layout.SetMargin(0, title.GetHeight(), 0, 0);
            layout.SetPadding(6, 6, 6, 6);
            layout.SetSpacing(vertical: 10);
            layout.SetBackground(255, 255, 255, 20);

            //adding toolbar
            this.AddItem(layout);

            //Frame
            HorizontalStack toolbar = new HorizontalStack();
            toolbar.SetBackground(Color.FromArgb(255, 51, 51, 51));
            toolbar.SetItemName(nameof(toolbar));
            toolbar.SetHeight(40);
            toolbar.SetPadding(10, 0, 10, 0);
            toolbar.SetSpacing(20);
            toolbar.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            toolbar.SetContentAlignment(ItemAlignment.HCenter);
            layout.AddItem(toolbar);

            FreeArea flow = new FreeArea();
            flow.SetBackground(Color.Transparent);
            flow.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);

            //btn add_at_begin
            ButtonCore btn1 = new ButtonCore();
            // btn1.SetShadow(15, 5, 5, Color.Black);

            // Effects.AddEffect(btn1, new Shadow(10, new SpaceVIL.Core.Size(30, 30), Color.Yellow));
            Effects.AddEffect(btn1, new Shadow(10, new Position(5, 5), Color.Black));
            btn1.SetBackground(13, 176, 255);
            btn1.SetItemName(nameof(btn1));
            btn1.SetBorderRadius(new CornerRadius(6));
            btn1.SetWidth(30);
            btn1.SetHeight(30);
            btn1.SetMargin(10, 0, 10, 0);
            btn1.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            btn1.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            // btn1.SetCustomFigure(new CustomFigure(false, GraphicsMathService.GetTriangle(30, 30, 0, 0, 180)));
            btn1.SetHoverRule(ItemHoverRule.Strict);

            btn1.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            // RadialMenuItem rm = new RadialMenuItem(this);
            // rm.AddItems(
            //     new ButtonCore("1"),
            //     new ButtonCore("2"),
            //     new ButtonCore("3"),
            //     new ButtonCore("4"),
            //     new ButtonCore("5")
            // );

            // SideArea sa = new SideArea(this, Side.Left);
            OpenEntryBox opb = new OpenEntryBox("Open File:", FileSystemEntryType.File, OpenDialogType.Save);
            btn1.EventMouseClick += (sender, args) =>
            {
                Console.WriteLine(btn1.GetItemName());

                //1.
                // OpenEntryDialog opd = new OpenEntryDialog("Save File:", FileSystemEntryType.File, OpenDialogType.Save);
                // opd.AddFilterExtensions("Text files (*.txt);*.txt", "Images (*.png, *.bmp, *.jpg) ; *.png, *.bmp, *.jpg");
                // opd.OnCloseDialog += () =>
                // {
                //     Console.WriteLine(opd.GetResult());
                // };
                // // opd.SetDefaultPath("D:\\");
                // opd.Show(this);

                //2.
                opb.AddFilterExtensions("Text files (*.txt);*.txt", "Images (*.png, *.bmp, *.jpg) ; *.png, *.bmp, *.jpg");
                opb.OnCloseDialog += () =>
                {
                    Console.WriteLine("Result? " + opb.GetResult());
                };
                opb.SetDefaultPath("D:\\");
                opb.Show();
            };

            //btn add_at_center
            ButtonCore btn2 = new ButtonCore();
            btn2.SetBackground(121, 223, 152);
            btn2.SetItemName(nameof(btn2));
            btn2.SetBorderRadius(new CornerRadius(6));
            btn2.SetWidth(30);
            btn2.SetHeight(30);
            btn2.SetMargin(10, 0, 10, 0);
            btn2.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            btn2.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            btn2.SetHoverRule(ItemHoverRule.Strict);
            btn2.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            // InputBox ib = new InputBox("Enter password", "Enter");
            btn2.EventMouseClick += (sender, args) =>
            {
                //1.
                // MessageItem msg = new MessageItem("Choose one of this:", "Message:");
                // ButtonCore btnDontSave = new ButtonCore("Do not save");
                // msg.AddUserButton(btnDontSave, 2); //id must be > 1
                // btnDontSave.EventMouseClick += (s, a) =>
                // {
                //     Console.WriteLine("btnDontSave is chosen");
                // };
                // msg.OnCloseDialog += (() =>
                // {
                //     Console.WriteLine(msg.GetResult() + " " + msg.GetUserButtonResult());
                // });
                // msg.Show(this);

                //2.
                // Hold();
                // MyDialogBox msg = new MyDialogBox();
                // msg.ShowAndWait(this);
                // Console.WriteLine(msg.GetResult());

                //3.
                // NoteBlock block = NoteBlock.GetNoteBlock();
                // block.SetPosition(100, 100);
                // flow.AddItem(block);

                //4.
                // ib.OnCloseDialog += () =>
                // {
                //     Console.WriteLine(ib.GetResult());
                // };
                // ib.Show();

                //4.4
                // InputDialog ib = new InputDialog("Enter password", "Enter");
                // ib.OnCloseDialog += () =>
                // {
                //     Console.WriteLine(ib.GetResult());
                // };
                // ib.Show(this);

                //5.
                // MessageBox msg = new MessageBox("Message", "Message");
                // msg.OnCloseDialog += () =>
                // {
                //     Console.WriteLine(msg.GetResult());
                // };
                // msg.Show();
            };
            //btn add_at_end
            ButtonCore btn3 = new ButtonCore();

            btn3.SetBackground(238, 174, 128);
            btn3.SetItemName(nameof(btn3));
            btn3.SetBorderRadius(new CornerRadius(6));
            btn3.SetWidth(30);
            btn3.SetHeight(30);
            btn3.SetMargin(10, 0, 10, 0);
            btn3.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            btn3.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            btn3.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });

            SideArea side = new SideArea(this, Side.Left);
            btn3.EventMouseClick += (sender, args) =>
            {
                side.Show();
            };



            ButtonCore btn4 = new ButtonCore();
            btn4.SetBackground(255, 60, 150);
            btn4.SetItemName(nameof(btn3));
            btn4.SetBorderRadius(new CornerRadius(6));
            btn4.SetWidth(30);
            btn4.SetHeight(30);
            btn4.SetMargin(10, 0, 50, 0);
            btn4.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            btn4.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            btn4.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            btn4.EventMouseClick += (sender, args) =>
            {
                for (int i = 0; i < 1; i++)
                {
                    for (int j = 0; j < 1; j++)
                    {
                        ResizableItem frame = new ResizableItem();
                        frame.SetBorder(new Border(Color.Gray, new CornerRadius(), 2));
                        frame.SetPadding(5, 5, 5, 5);
                        frame.SetBackground(100, 100, 100);
                        frame.SetSize(200, 200);
                        frame.SetPosition(10 + i * 210, 10 + j * 210);
                        Effects.AddEffect(frame, new Shadow(10, new Position(100, 100), Color.FromArgb(0, 255, 0)));
                        // frame.SetShadow(10, 5, 5, Color.Red);
                        flow.AddItem(frame);

                        // Graph graph = GetGraph();
                        // graph.SetPadding(5, 5, 5, 5);
                        // frame.AddItem(graph);

                        // OpenGLLayer ogl = new OpenGLLayer();
                        // ogl.SetMargin(0, 30, 0, 0);
                        // frame.AddItem(ogl);
                    }
                }

                // LoadingScreen screen = new LoadingScreen();
                // screen.Show(this);

                // Task task = new Task(() =>
                // {
                //     for (int i = 1; i <= 100; i++)
                //     {
                //         screen.SetValue(i);
                //         Thread.Sleep(50);
                //     }
                //     screen.SetToClose();
                // });
                // task.Start();
            };


            //adding buttons
            toolbar.AddItems(btn1, btn2, btn3, btn4);

            layout.AddItem(flow);

            // ResizableItem ri = new ResizableItem();
            // ri.SetBorder(new Border(Color.Gray, new CornerRadius(), 2));
            // ri.SetPadding(5, 5, 5, 5);
            // ri.SetBackground(100, 100, 100);
            // ri.SetSize(200, 200);
            // ri.SetPosition(10 + 1 * 210, 10 + 1 * 210);
            // flow.AddItem(ri);
            // OpenGLLayer ogl1 = new OpenGLLayer();
            // ogl1.SetMargin(0, 30, 0, 0);
            // ri.AddItem(ogl1);

            // ResizableItem rect1 = new ResizableItem();
            // rect1.SetBackground(255, 0, 0);
            // rect1.SetWidth(100);
            // rect1.SetHeight(100);
            // rect1.SetX(100);
            // rect1.SetY(100);
            // rect1.SetShadow(10, 0, 0, Color.Black);
            // rect1.SetShadowExtension(2, 2);

            // ResizableItem rect2 = new ResizableItem();
            // rect2.SetBackground(255, 255, 0);
            // rect2.SetWidth(100);
            // rect2.SetHeight(100);
            // rect2.SetX(150);
            // rect2.SetY(300);
            // rect2.SetShadow(10, 0, 0, Color.Black);
            // rect2.SetShadowExtension(2, 2);

            // ResizableItem rect3 = new ResizableItem();
            // rect3.SetBackground(255, 0, 255);
            // rect3.SetWidth(100);
            // rect3.SetHeight(100);
            // rect3.SetX(400);
            // rect3.SetY(200);
            // rect3.SetShadow(10, 0, 0, Color.Black);
            // rect3.SetShadowExtension(2, 2);
            // rect3.ExcludeSides(Side.Left);
            // flow.AddItems(rect1, rect2, rect3);

            MenuItem restore = new MenuItem("Build Tool");
            MenuItem x_plus = new MenuItem("X += 100");
            MenuItem y_plus = new MenuItem("Y += 100");
            MenuItem addition = new MenuItem("Addition");

            // context
            ContextMenu _context_menu = new ContextMenu(this);// new ContextMenu(this, restore, x_plus, y_plus, addition);
            _context_menu.SetItemName("Base");
            _context_menu.AddItems(restore, x_plus, y_plus, addition);

            ContextMenu addition_menu = new ContextMenu(this);
            addition_menu.SetItemName("Addition");
            addition_menu.SetSize(110, 94);
            MenuItem x_minus = new MenuItem("X -= 100");
            MenuItem y_minus = new MenuItem("Y -= 100");
            MenuItem ex_addition = new MenuItem("addition");
            addition_menu.AddItems(x_minus, y_minus, ex_addition);

            addition.AssignContextMenu(addition_menu);

            ContextMenu ex_menu = new ContextMenu(this
            , new MenuItem("Ex1"), new MenuItem("Ex2")
            );
            ex_menu.SetItemName("ExMenu");
            // ex_menu.SetSize(110, 64);
            ex_addition.AssignContextMenu(ex_menu);
            flow.EventMouseClick += (sender, args) =>
            {
                _context_menu.Show(sender, args);
            };

            // Dictionary<GeometryEventType, List<IEventUpdate>> listeners = GetLayout().GetContainer().GetCore().eventManager.listeners;
            // foreach (var item in listeners[GeometryEventType.ResizeHeight])
            // {
            //     Console.WriteLine((item as IBaseItem).GetItemName());
            // }

            EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.V)
                    SpaceVIL.Common.CommonService.SetClipboardString("SetClipBoardString");
                if (args.Key == KeyCode.C)
                    Console.WriteLine(SpaceVIL.Common.CommonService.GetClipboardString());
                if (args.Key == KeyCode.F)
                    Console.WriteLine(WindowsBox.GetCurrentFocusedWindow().GetWindowName());
            };
        }

        private Graph GetGraph()
        {
            Graph graph_points = new Graph();
            graph_points.SetPointColor(Color.FromArgb(10, 255, 10));
            graph_points.SetPointThickness(10.0f);
            graph_points.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            graph_points.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            // List<float[]> crd = new List<float[]>();
            // crd.add(new float[3] { 100.0f, 0.0f, 0.0f });
            // crd.add(new float[3] { 50.0f, 100.0f, 0.0f });
            // crd.add(new float[3] { 150.0f, 100.0f, 0.0f });
            // graph_points.setPointsCoord(crd);
            graph_points.SetPoints(GraphicsMathService.GetRoundSquare(300, 300, 0, 0, 0));
            // graph_points.setPointsCoord(GraphicsMathService.getTriangle(100, 100, 0,
            // 0,
            // 0));
            // graph_points.setWidth(300);
            // graph_points.setHeight(300);
            // graph_points.setX(200);
            // graph_points.setY(200);
            //
            // graph_points.setShapePointer(GraphicsMathService.getTriangle(graph_points.getPointThickness(),
            // graph_points.getPointThickness()));
            //
            // graph_points.SetShapePointer(GraphicsMathService.GetCross(graph_points.GetPointThickness(),
            //         graph_points.GetPointThickness(), 2, 45));
            //
            // graph_points.setShapePointer(GraphicsMathService.getStar(graph_points.getPointThickness(),
            // graph_points.getPointThickness() / 2.0f));
            return graph_points;
        }
    }
}
