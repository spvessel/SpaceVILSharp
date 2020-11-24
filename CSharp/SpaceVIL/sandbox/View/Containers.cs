using System;
using System.Drawing;
using System.Threading;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using SpaceVIL.Common;

namespace View
{
    class Containers : ActiveWindow
    {
        TreeView treeview = new TreeView();
        Grid block = new Grid(2, 2);

        TabView tabs = new TabView();
        Tab tab1 = new Tab("FirstLeftTab");
        Tab tab2 = new Tab("List1");
        Tab tab3 = new Tab("List2");
        TitleBar title = new TitleBar(nameof(Containers));

        public override void InitWindow()
        {
            SetParameters(nameof(Containers), nameof(Containers), 700, 500, true);
            this.SetMinSize(400, 400);
            this.IsCentered = true;

            // title.SetShadow(5, 0, 3, Color.FromArgb(150, 0, 0, 0));
            // this.AddItem(title);

            Frame cc = new Frame();
            // cc.SetMargin(0, title.GetHeight() + 10, 0, 0);
            cc.SetBackground(50, 50, 50);
            this.AddItem(cc);

            cc.AddItem(tabs);

            // tab1.SetDraggable(false);
            // tab1.SetMaxWidth(30);
            tabs.AddTabs(tab1, tab2, tab3,
                new Tab("TabForTest1"),
                new Tab("TabForTest2")//, 
                                      // new Tab("TabForTest3"), 
                                      // new Tab("TabForTest4"),
                                      // new Tab("TabForTest5")
                );
            // tabs.SetContentPolicy(SizePolicy.Expand);
            tabs.SetTabPolicy(SizePolicy.Fixed);

            EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.Menu)
                    foreach (Tab tab in tabs.GetTabs())
                        tab.SetClosable(!tab.IsClosable());
                if (args.Key == KeyCode.Delete)
                {
                    ClearAllTabs();
                }
                if (args.Key == KeyCode.Enter)
                {
                    if (tabs != null)
                    {
                        Console.WriteLine(tabs.GetTabs().Count);
                    }
                }
            };

            Grid grid = new Grid(3, 4);
            grid.SetBackground(255, 255, 255, 20);
            grid.SetSpacing(20, 20);
            grid.SetMargin(20, 30, 20, 30);
            tabs.AddItemToTabByName("FirstLeftTab", grid);
            // Grid subgrid = new Grid(1, 2);
            // grid.InsertItem(subgrid, 1, 1);

            // subgrid.AddItems(new VerticalScrollBar(), new VerticalScrollBar());
            for (int i = 0; i < 12; i++)
            {
                ButtonCore h_btn = GetButton("Button №" + i, 150, 50, SizePolicy.Expand);
                h_btn.SetMaxSize(200, 100);
                h_btn.SetBackground(121, 223, 152);
                grid.AddItem(h_btn);
            }

            tab1.EventOnClose += () =>
            {
                Console.WriteLine("close");
            };

            // BlankItem b1 = GetBlankItem("Blank1");
            // // BlankItem b2 = GetBlankItem("Blank2");
            // // BlankItem b3 = GetBlankItem("Blank3");

            // cc.AddItem(b1);
            // b1.AddItem(b2);
            // b2.AddItem(b3);

            // b3.SetFocus();

            // b3.SetPassEvents(false);
            // b2.SetPassEvents(false);
            // b2.SetPassEvents(true, InputEventType.KeyPress, InputEventType.KeyRelease);

            EventMouseClick += (sender, args) =>
            {
                // Console.WriteLine(sender.GetItemName() + " EventMouseClick");
            };
            EventMouseDoubleClick += (sender, args) =>
            {
                // Console.WriteLine(sender.GetItemName() + " EventMouseDoubleClick");
            };
            EventKeyPress += (sender, args) =>
            {
                if (args.Key == KeyCode.R)
                {
                    // ItemsRefreshManager.PrintSizeOfShapes();
                }
                // Console.WriteLine(sender.GetItemName() + " EventKeyPress");
            };
            EventKeyRelease += (sender, args) =>
            {
                // Console.WriteLine(sender.GetItemName() + " EventKeyRelease");
            };
            EventMouseHover += (sender, args) =>
            {
                // Console.WriteLine(sender.GetItemName() + " EventMouseHover");
            };


            // cc.AddItem(tabs);
            // // tabs.AddTab("Common");
            // // tabs.AddTab("Stack");
            // tabs.AddTabs(tab1, tab2, tab3);
            // tab1.SetWidthPolicy(SizePolicy.Expand);

            // tabs.GetTabs()[0].SetWidthPolicy(SizePolicy.Expand);

            // EventKeyPress += (sender, args) =>
            // {
            //     if (args.Key == KeyCode.Menu)
            //         foreach (Tab tab in tabs.GetTabs())
            //             tab.SetClosable(!tab.IsClosable());

            //     if (args.Key == KeyCode.Delete)
            //         ClearAllTabs();
            //     if (args.Key == KeyCode.Enter)
            //         if (tabs != null)
            //             Console.WriteLine(tabs.GetTabs().Count);
            // };

            // tabs.AddTab("Split");
            // tabs.AddTab("Free");
            // tabs.AddTab("Items");

            // //Common
            // ButtonCore b1 = GetButton("Item Left-Top", 150, 50, SizePolicy.Fixed);
            // b1.SetBackground(121, 223, 152);
            // b1.SetAlignment(ItemAlignment.Left | ItemAlignment.Top);
            // b1.SetMargin(20, 20, 0, 0);
            // tabs.AddItemToTab("Common", b1);
            // ButtonCore b2 = GetButton("Item Right-Bottom", 150, 50, SizePolicy.Fixed);
            // b2.SetBackground(238, 174, 128);
            // b2.SetAlignment(ItemAlignment.Right | ItemAlignment.Bottom);
            // b2.SetMargin(0, 0, 20, 20);
            // tabs.AddItemToTab("Common", b2);
            // ButtonCore b3 = GetButton("Item Center", 150, 50, SizePolicy.Expand);
            // b3.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            // b3.SetMargin(170, 70, 170, 70);
            // tabs.AddItemToTab("Common", b3);

            // //Stack
            // HorizontalStack h = new HorizontalStack();
            // h.SetBackground(255, 255, 255, 20);
            // h.SetMargin(20, 20, 20, 20);
            // h.SetHeight(60);
            // h.SetHeightPolicy(SizePolicy.Fixed);
            // tabs.AddItemToTab("Stack", h);
            // VerticalStack v = new VerticalStack();
            // v.SetBackground(255, 255, 255, 20);
            // v.SetMargin(20, 100, 20, 20);
            // v.SetWidth(190);
            // v.SetWidthPolicy(SizePolicy.Fixed);
            // tabs.AddItemToTab("Stack", v);

            // for (int i = 0; i < 5; i++)
            // {
            //     ButtonCore h_btn = GetButton("Item H" + i, 150, 50, SizePolicy.Fixed);
            //     h_btn.SetBackground(121, 223, 152);
            //     h.AddItem(h_btn);
            //     ButtonCore v_btn = GetButton("Item V" + i, 150, 50, SizePolicy.Fixed);
            //     v.AddItem(v_btn);
            // }

            // //Grid
            // Grid grid = new Grid(3, 4);
            // grid.SetBackground(255, 255, 255, 20);
            // grid.SetSpacing(2, 2);
            // grid.SetMargin(20, 30, 20, 30);
            // tabs.AddItemToTabByName("Gridadofigjhopaijgpaijgpoiajhsogijhaosighj1", grid);
            // Grid subgrid = new Grid(1, 2);
            // grid.InsertItem(subgrid, 1, 1);
            // HorizontalSlider slider = new HorizontalSlider();
            // slider.SetBackground(0, 0, 0, 255);
            // subgrid.AddItems(new VerticalScrollBar(), new VerticalScrollBar());
            // for (int i = 0; i < 11; i++)
            // {
            //     ButtonCore h_btn = GetButton("Cell" + i, 150, 50, SizePolicy.Expand);
            //     h_btn.SetMaxSize(200, 100);
            //     h_btn.SetBackground(121, 223, 152);
            //     grid.AddItem(h_btn);
            // }

            // //List
            // TreeView treeview = new TreeView();
            // treeview.SetWidth(300);
            // treeview.SetWidthPolicy(SizePolicy.Fixed);
            // treeview.SetHScrollBarVisible(ScrollBarVisibility.AsNeeded);
            // tabs.AddItemToTab("List", treeview);
            // treeview.SetRootVisibility(true);
            // for (int i = 0; i < 5; i++)
            // {
            //     TreeItem item = GetTreeBranch();
            //     treeview.AddItem(item);
            //     item.AddItem(GetTreeLeaf());
            //     item.AddItem(GetTreeLeaf());
            // }

            // ListBox listbox = new ListBox();
            // // listbox.GetArea().SetSpacing(0, 10);
            // listbox.SetMargin(305, 0, 0, 0);
            // tabs.AddItemToTab("List", listbox);
            // for (int i = 0; i < 100; i++)
            // {
            //     CheckBox chb = new CheckBox();
            //     // chb.SetBackground(Color.Transparent);
            //     chb.SetText("Item CheckBox" + i);
            //     // chb.SetMinWidth(500);
            //     chb.SetForeground(Color.FromArgb(255, 210, 210, 210));
            //     listbox.AddItem(chb);
            // }

            // //Split
            // Frame v1 = new Frame();
            // Frame h1 = new Frame();
            // Frame h2 = new Frame();
            // VerticalSplitArea vsplit = new VerticalSplitArea();
            // vsplit.SetSplitThickness(3);
            // vsplit.SetSplitHolderPosition(100);
            // tabs.AddItemToTab("Split", vsplit);

            // HorizontalSplitArea hsplit = new HorizontalSplitArea();
            // hsplit.SetSplitThickness(3);

            // vsplit.AssignLeftItem(v1);
            // ButtonCore v_btn_1 = GetButton("Item Left Area", 150, 100, SizePolicy.Expand);
            // v_btn_1.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            // v1.AddItem(v_btn_1);
            // vsplit.AssignRightItem(hsplit);

            // hsplit.AssignTopItem(h1);
            // hsplit.AssignBottomItem(h2);
            // ButtonCore h_btn_1 = GetButton("Item Top Area", 150, 50, SizePolicy.Expand);
            // h_btn_1.SetWidthPolicy(SizePolicy.Fixed);
            // ButtonCore h_btn_2 = GetButton("Item Bottom Area", 150, 50, SizePolicy.Expand);
            // h_btn_2.SetWidthPolicy(SizePolicy.Fixed);

            // h1.AddItem(h_btn_1);
            // h2.AddItem(h_btn_2);


            // //Free
            // HorizontalStack toolbar = new HorizontalStack();
            // toolbar.SetBackground(Color.FromArgb(255, 51, 51, 51));
            // toolbar.SetHeight(40);
            // toolbar.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            // toolbar.SetMargin(20, 20, 20, 20);
            // toolbar.SetBorderRadius(new CornerRadius(6, 6, 0, 0));
            // tabs.AddItemToTab("Free", toolbar);

            // ButtonCore btn_1 = GetButton("T", 30, 30, SizePolicy.Fixed);
            // btn_1.SetMargin(5, 0, 0, 0);
            // btn_1.SetBackground(111, 181, 255);

            // ButtonCore btn_2 = GetButton("R", 30, 30, SizePolicy.Fixed);
            // btn_2.SetMargin(5, 0, 0, 0);
            // btn_2.SetBackground(111, 181, 255);

            // ButtonCore btn_3 = GetButton("E", 30, 30, SizePolicy.Fixed);
            // btn_3.SetMargin(5, 0, 0, 0);
            // btn_3.SetBackground(111, 181, 255);

            // ButtonCore btn_4 = GetButton("C", 30, 30, SizePolicy.Fixed);
            // btn_4.SetMargin(5, 0, 0, 0);
            // btn_4.SetBackground(111, 181, 255);

            // ButtonCore btn_5 = GetButton("Graph", 65, 30, SizePolicy.Fixed);
            // btn_5.SetMargin(5, 0, 0, 0);
            // btn_5.SetBackground(111, 181, 255);

            // toolbar.AddItems(
            //     btn_1,
            //     btn_2,
            //     btn_3,
            //     btn_4,
            //     btn_5
            // );

            // FreeArea flow = new FreeArea();
            // flow.SetBackground(255, 255, 255, 20);
            // flow.SetMargin(20, 60, 20, 20);
            // flow.SetBorderRadius(new CornerRadius(0, 0, 6, 6));
            // tabs.AddItemToTab("Free", flow);

            // btn_1.EventMouseClick += (sender, args) =>
            // {
            //     ResizableItem tr = new ResizableItem();
            //     tr.SetSize(100, 100);
            //     tr.SetBackground(121, 223, 152);
            //     tr.SetPosition(100, 100);
            //     tr.SetCustomFigure(new CustomFigure(false, GraphicsMathService.GetTriangle(100, 100, 0)));
            //     tr.SetBorderFill(Color.FromArgb(30, 255, 255, 255));
            //     tr.SetBorderThickness(1);
            //     tr.SetShadow(5, 2, 2, Color.FromArgb(150, 0, 0, 0));
            //     flow.AddItem(tr);
            // };
            // btn_2.EventMouseClick += (sender, args) =>
            // {
            //     ResizableItem tr = new ResizableItem();
            //     tr.SetSize(100, 100);
            //     tr.SetBackground(238, 174, 128);
            //     tr.SetPosition(100, 100);
            //     tr.SetCustomFigure(new CustomFigure(false, GraphicsMathService.GetRectangle(100, 100)));
            //     tr.SetBorderFill(Color.FromArgb(30, 255, 255, 255));
            //     tr.SetBorderThickness(1);
            //     tr.SetShadow(5, 2, 2, Color.FromArgb(150, 0, 0, 0));
            //     flow.AddItem(tr);
            // };
            // btn_3.EventMouseClick += (sender, args) =>
            // {
            //     ResizableItem tr = new ResizableItem();
            //     tr.SetSize(100, 100);
            //     tr.SetBackground(193, 142, 221);
            //     tr.SetPosition(100, 100);
            //     tr.SetCustomFigure(new CustomFigure(false, GraphicsMathService.GetEllipse(100, 100)));
            //     tr.SetBorderFill(Color.FromArgb(30, 255, 255, 255));
            //     tr.SetBorderThickness(1);
            //     tr.SetShadow(5, 2, 2, Color.FromArgb(150, 0, 0, 0));
            //     flow.AddItem(tr);
            // };
            // btn_4.EventMouseClick += (sender, args) =>
            // {
            //     ResizableItem tr = new ResizableItem();
            //     tr.SetSize(100, 75);
            //     tr.SetBackground(114, 153, 211);
            //     tr.SetPosition(100, 100);
            //     tr.SetCustomFigure(new CustomFigure(false, GraphicsMathService.GetStar(100, 75, 13)));
            //     tr.SetBorderFill(Color.FromArgb(30, 255, 255, 255));
            //     tr.SetBorderThickness(1);
            //     tr.SetShadow(5, 2, 2, Color.FromArgb(150, 0, 0, 0));
            //     flow.AddItem(tr);
            // };
            // btn_5.EventMouseClick += (sender, args) =>
            // {
            //     ResizableItem frame = new ResizableItem();
            //     frame.SetBackground(Color.Transparent);
            //     frame.SetPadding(10, 10, 10, 10);
            //     frame.SetBackground(Color.Transparent);
            //     frame.SetSize(150, 150);
            //     frame.SetPosition(100, 100);
            //     frame.SetBorderFill(Color.FromArgb(150, 255, 255, 255));
            //     frame.SetBorderThickness(1);
            //     flow.AddItem(frame);

            //     PointsContainer graph = GetPointsContainer();
            //     frame.AddItem(graph);
            // };

            // HorizontalStack hs1 = new HorizontalStack();
            // hs1.SetSpacing(20, 5);
            // hs1.SetMargin(40, 40, 40, 40);
            // tabs.AddItemToTab("Items", hs1);
            // VerticalStack c1 = new VerticalStack();
            // c1.SetSpacing(0, 5);
            // VerticalStack c2 = new VerticalStack();
            // c2.SetSpacing(0, 5);
            // c2.SetPadding(0, 55, 0, 0);
            // VerticalStack c3 = new VerticalStack();
            // c3.SetSpacing(0, 5);
            // hs1.AddItems(c1, c2, c3);
            // //Items
            // Label items = GetLabel("ITEMS:");
            // items.SetBackground(255, 255, 255, 50);
            // items.SetForeground(Color.White);
            // items.SetMargin(0, 0, 0, 20);
            // items.SetFontStyle(FontStyle.Bold);
            // c1.AddItem(items);
            // c1.AddItem(GetLabel("1. SpaceVIL.ButtonCore"));
            // c1.AddItem(GetLabel("2. SpaceVIL.ButtonToggle"));
            // c1.AddItem(GetLabel("3. SpaceVIL.CheckBox"));
            // c1.AddItem(GetLabel("4. SpaceVIL.ComboBox"));
            // c1.AddItem(GetLabel("5. SpaceVIL.ContextMenu"));
            // c1.AddItem(GetLabel("6. SpaceVIL.FloatItem"));
            // c1.AddItem(GetLabel("7. SpaceVIL.HorizontalScrollBar"));
            // c1.AddItem(GetLabel("8. SpaceVIL.HorizontalSlider"));
            // c1.AddItem(GetLabel("9. SpaceVIL.ImageItem"));
            // c1.AddItem(GetLabel("10. SpaceVIL.Indicator"));
            // c1.AddItem(GetLabel("11. SpaceVIL.Label"));
            // c1.AddItem(GetLabel("12. SpaceVIL.MenuItem"));
            // c1.AddItem(GetLabel("13. SpaceVIL.MessageBox"));
            // c2.AddItem(GetLabel("14. SpaceVIL.PasswordLine"));
            // c2.AddItem(GetLabel("15. SpaceVIL.PointsContainer"));
            // c2.AddItem(GetLabel("16. SpaceVIL.PopUpMessage"));
            // c2.AddItem(GetLabel("17. SpaceVIL.ProgressBar"));
            // c2.AddItem(GetLabel("18. SpaceVIL.RadioButton"));
            // c2.AddItem(GetLabel("19. SpaceVIL.SpinItem"));
            // c2.AddItem(GetLabel("20. SpaceVIL.TextArea"));
            // c2.AddItem(GetLabel("21. SpaceVIL.TextEdit"));
            // c2.AddItem(GetLabel("22. SpaceVIL.TitleBar"));
            // c2.AddItem(GetLabel("23. SpaceVIL.TreeItem"));
            // c2.AddItem(GetLabel("24. SpaceVIL.VerticalScrollBar"));
            // c2.AddItem(GetLabel("25. SpaceVIL.VerticalSlider"));
            // c2.AddItem(GetLabel("26. SpaceVIL.WindowAnchor"));

            // //primitives
            // Label primitives = GetLabel("PRIMITIVES:");
            // primitives.SetBackground(255, 255, 255, 50);
            // primitives.SetForeground(Color.White);
            // primitives.SetMargin(0, 0, 0, 20);
            // primitives.SetFontStyle(FontStyle.Bold);
            // c3.AddItem(primitives);
            // c3.AddItem(GetLabel("1. SpaceVIL.CustomShape"));
            // c3.AddItem(GetLabel("2. SpaceVIL.Ellipse"));
            // c3.AddItem(GetLabel("3 SpaceVIL.Rectangle"));
            // c3.AddItem(GetLabel("4. SpaceVIL.Triangle"));

            // //containers
            // Label containers = GetLabel("CONTAINERS:");
            // containers.SetBackground(255, 255, 255, 50);
            // containers.SetForeground(Color.White);
            // containers.SetMargin(0, 35, 0, 20);
            // containers.SetFontStyle(FontStyle.Bold);
            // c3.AddItem(containers);
            // c3.AddItem(GetLabel("1. SpaceVIL.Frame"));
            // c3.AddItem(GetLabel("2. SpaceVIL.FreeArea"));
            // c3.AddItem(GetLabel("3. SpaceVIL.Grid"));
            // c3.AddItem(GetLabel("4. SpaceVIL.HorizontalSplitArea"));
            // c3.AddItem(GetLabel("5. SpaceVIL.HorizontalStack"));
            // c3.AddItem(GetLabel("6. SpaceVIL.ListBox"));
            // c3.AddItem(GetLabel("7. SpaceVIL.TabView"));
            // c3.AddItem(GetLabel("8. SpaceVIL.TreeView"));
            // c3.AddItem(GetLabel("9. SpaceVIL.VerticalSplitArea"));
            // c3.AddItem(GetLabel("10. SpaceVIL.VerticalStack"));
        }

        private void ClearAllTabs()
        {
            tabs.RemoveAllTabs();
            tabs.GetParent().RemoveItem(tabs);
            // title.RemoveItem(tabs);
            tab1 = null;
            tab2 = null;
            tab3 = null;
        }

        private BlankItem GetBlankItem(String name)
        {
            BlankItem blank = new BlankItem();
            blank.SetItemName(name);
            blank.SetStyle(Style.GetFrameStyle());
            blank.SetMargin(50, 50, 50, 50);
            blank.SetBorder(new Border(Color.FromArgb(0, 162, 232), new CornerRadius(10), 3));
            blank.SetBackground(255, 255, 255, 50);
            blank.EventMouseClick += (sender, args) =>
            {
                Console.WriteLine(blank.GetItemName() + " EventMouseClick");
            };
            blank.EventMouseDoubleClick += (sender, args) =>
            {
                Console.WriteLine(blank.GetItemName() + " EventMouseDoubleClick");
            };
            blank.EventKeyPress += (sender, args) =>
            {
                Console.WriteLine(blank.GetItemName() + " EventKeyPress");
            };
            blank.EventKeyRelease += (sender, args) =>
            {
                Console.WriteLine(blank.GetItemName() + " EventKeyRelease");
            };
            // blank.EventMouseHover += (sender, args) =>
            // {
            //     Console.WriteLine(blank.GetItemName() + " EventMouseHover");
            // };
            return blank;
        }

        private ButtonCore GetButton(String name, int w = 0, int h = 0, SizePolicy policy = SizePolicy.Expand)
        {
            //Style
            Style style = new Style();
            style.Background = Color.FromArgb(255, 111, 181, 255);
            style.Foreground = Color.Black;
            style.Border.SetRadius(new CornerRadius(3));
            style.Font = DefaultsService.GetDefaultFont(16);
            style.Width = w;
            style.MinWidth = 30;
            style.MinHeight = 30;
            style.WidthPolicy = policy;
            style.Height = h;
            style.HeightPolicy = policy;
            style.Alignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            style.TextAlignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            ItemState hovered = new ItemState(Color.FromArgb(60, 255, 255, 255));
            style.AddItemState(ItemStateType.Hovered, hovered);
            ItemState pressed = new ItemState(Color.FromArgb(30, 0, 0, 60));
            pressed.Border.SetColor(Color.FromArgb(255, 255, 255, 255));
            pressed.Border.SetThickness(1);
            style.AddItemState(ItemStateType.Pressed, pressed);
            // style.SetMargin(20, 20, 20, 20);

            ButtonCore btn = new ButtonCore(name);
            btn.SetStyle(style);
            btn.SetSize(w, h);
            btn.SetSizePolicy(policy, policy);
            btn.SetItemName(name);
            Effects.AddEffect(btn, new Shadow(5, new Position(0, 3), Color.FromArgb(150, 0, 0, 0)));

            btn.EventMouseLeave += (sender, ags) =>
            {
                btn.SetBackground(121, 223, 152);
                btn.SetText("Leave");
            };
            btn.EventMouseHover += (sender, ags) =>
            {
                btn.SetText("Hover");
                btn.SetBackground(10, 162, 232);
            };
            return btn;
        }
        private Label GetLabel(String text)
        {
            Label lb = new Label(text);
            lb.SetHeight(30);
            lb.SetHeightPolicy(SizePolicy.Fixed);
            lb.SetBackground(255, 255, 255, 20);
            lb.SetForeground(220, 220, 220);
            lb.SetFont(DefaultsService.GetDefaultFont(16));
            lb.SetPadding(10);
            return lb;
        }
        private TreeItem GetTreeBranch()
        {
            TreeItem item = new TreeItem(TreeItemType.Branch);
            item.SetText(item.GetItemName());
            return item;
        }
        private TreeItem GetTreeLeaf()
        {
            TreeItem item = new TreeItem(TreeItemType.Leaf);
            item.SetText(item.GetItemName());
            return item;
        }
        private PointsContainer GetPointsContainer()
        {
            PointsContainer graph_points = new PointsContainer();
            graph_points.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            graph_points.SetPointColor(Color.FromArgb(255, 10, 255, 10));
            graph_points.SetPointThickness(10.0f);
            // List<float[]> crd = new List<float[]>();
            // crd.Add(new float[3] { 100.0f, 0.0f, 0.0f });
            // crd.Add(new float[3] { 50.0f, 100.0f, 0.0f });
            // crd.Add(new float[3] { 150.0f, 100.0f, 0.0f });
            // graph_points.SetPointsCoord(crd);
            graph_points.SetPoints(GraphicsMathService.GetRoundSquare(300, 300, 150));
            // graph_points.SetPointsCoord(GraphicsMathService.GetTriangle(100, 100));
            // graph_points.SetShapePointer(GraphicsMathService.GetTriangle(graph_points.GetPointThickness(), graph_points.GetPointThickness()));
            // graph_points.SetWidth(300);
            // graph_points.SetHeight(300);
            // graph_points.SetX(200);
            // graph_points.SetY(200);
            // graph_points.SetShapePointer(GraphicsMathService.GetTriangle(graph_points.GetPointThickness(), graph_points.GetPointThickness()));
            graph_points.SetPointShape(GraphicsMathService.GetCross(graph_points.GetPointThickness(), graph_points.GetPointThickness(), 2, 45));
            // graph_points.SetShapePointer(GraphicsMathService.GetStar(graph_points.GetPointThickness(), graph_points.GetPointThickness() / 2.0f));
            return graph_points;
        }
    }
}