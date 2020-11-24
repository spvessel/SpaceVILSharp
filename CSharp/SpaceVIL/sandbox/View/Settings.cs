using System;
using System.Drawing;
using System.Threading;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
namespace View
{
    class Settings : ActiveWindow
    {
        TreeView treeview;

        override public void InitWindow()
        {
            // int count = 10;
            SetParameters("Settings", "Settings", 500, 500, false);
            this.SetMinSize(150, 100);
            // this.SetBackground(45, 45, 45);
            this.SetPadding(2, 2, 2, 2);

            TitleBar title = new TitleBar("ComplexTest");
            this.AddItem(title);

            HorizontalStack toolbar = new HorizontalStack();
            toolbar.SetBackground(51, 51, 51);
            toolbar.SetItemName("toolbar");
            toolbar.SetSpacing(6, 0);
            toolbar.SetMargin(0, 30, 0, 0);
            toolbar.SetHeight(40);
            toolbar.SetPadding(10, 0, 0, 0);
            toolbar.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            this.AddItem(toolbar);

            ButtonCore b1 = GetButton("b1", 26, 30, SizePolicy.Fixed);
            b1.EventMouseClick += (s, a) =>
            {
                treeview.SetRootVisible(!treeview.IsRootVisible());
            };
            ButtonCore b2 = GetButton("b2", 26, 30, SizePolicy.Fixed);
            b2.EventMouseClick += (s, a) =>
            {
                TreeItem ti = (TreeItem)treeview.GetSelectedItem();
                if (ti == null)
                    treeview.AddItem(getTreeBranch());
                else
                    ti.AddItem(getTreeBranch());
            };
            ButtonCore b3 = GetButton("b3", 26, 30, SizePolicy.Fixed);
            b3.EventMouseClick += (s, a) =>
            {
                TreeItem ti = (TreeItem)treeview.GetSelectedItem();
                if (ti == null)
                    treeview.AddItem(getTreeLeaf());
                else
                    ti.AddItem(getTreeLeaf());
            };
            ButtonCore b4 = GetButton("b4", 26, 30, SizePolicy.Fixed);
            b4.EventMouseClick += (s, a) =>
            {
                TreeItem ti = treeview.GetRootItem();
                treeview.RemoveItem(ti);
                TreeItem ti1 = new TreeItem(TreeItemType.Branch, "newRoot");
                treeview.AddItem(ti1);
            };
            ButtonCore b5 = GetButton("b5", 26, 30, SizePolicy.Fixed);
            b5.EventMouseClick += (s, a) =>
            {
                TreeItem ti = (TreeItem)treeview.GetSelectedItem();
                if (ti != null)
                    ti.SetText("text");
            };
            ButtonCore b6 = GetButton("b6", 26, 30, SizePolicy.Fixed);
            b6.EventMouseClick += (s, a) =>
            {
                toolbar.RemoveItem(b1);
            };

            toolbar.AddItems(b1, b2, b3, b4, b5, b6);

            VerticalSplitArea splitarea = new VerticalSplitArea();
            // HorizontalSplitArea splitarea = new HorizontalSplitArea();
            splitarea.SetMargin(0, 80, 0, 0);
            splitarea.SetSplitColor(Color.FromArgb(100, 100, 100));
            this.AddItem(splitarea);
            splitarea.SetSplitPosition(300);

            treeview = new TreeView();
            treeview.SetMinWidth(100);
            treeview.SetVScrollBarPolicy(VisibilityPolicy.AsNeeded);
            splitarea.AssignLeftItem(treeview);

            WrapGrid wrap = new WrapGrid(100, 100, Orientation.Horizontal);
            wrap.SetStretch(true);
            splitarea.AssignRightItem(wrap);

            int count = 0;
            for (int i = 0; i < 5; i++)
            {
                wrap.AddItem(GetButton("Wrap" + count++, 100, 100, SizePolicy.Expand));
            }

            wrap.EventScrollUp += (sender, args) =>
            {
                if (args.Mods == KeyMods.Control)
                {
                    wrap.SetCellSize(wrap.GetCellWidth() + 10, wrap.GetCellHeight() + 10);
                }
            };
            wrap.EventScrollDown += (sender, args) =>
            {
                if (args.Mods == KeyMods.Control)
                {
                    wrap.SetCellSize(wrap.GetCellWidth() - 10, wrap.GetCellHeight() - 10);
                }
            };
        }

        private ButtonCore GetButton(String name, int w, int h, SizePolicy policy)
        {
            // Style
            Style style = new Style();
            style.Background = (Color.FromArgb(255, 181, 111));
            style.Foreground = (Color.FromArgb(0, 0, 0));
            style.Border.SetRadius(new CornerRadius(6));
            style.Font = new Font(new FontFamily("Courier New"), 16, FontStyle.Regular);
            style.Width = w;
            style.MinWidth = 30;
            style.MaxWidth = 1000;
            style.WidthPolicy = policy;
            style.Height = h;
            style.MinHeight = 30;
            style.MaxHeight = 1000;
            style.HeightPolicy = policy;
            style.Alignment = ItemAlignment.VCenter | ItemAlignment.HCenter;
            style.TextAlignment = ItemAlignment.VCenter | ItemAlignment.HCenter;
            ItemState hovered = new ItemState();
            hovered.Background = Color.FromArgb(255, 255, 255, 60);
            style.AddItemState(ItemStateType.Hovered, hovered);
            style.Margin = new Indents(3, 3, 3, 3);

            ButtonCore btn = new ButtonCore(name);
            btn.SetSize(w, h);
            btn.SetSizePolicy(policy, policy);
            btn.SetItemName(name);
            btn.SetStyle(style);
            return btn;
        }

        private int count1 = 0;
        private int count2 = 0;

        private TreeItem getTreeBranch()
        {
            TreeItem item = new TreeItem(TreeItemType.Branch, "branch" + count1);
            count1++;
            item.SetFontSize(15);
            return item;
        }

        private TreeItem getTreeLeaf()
        {
            TreeItem item = new TreeItem(TreeItemType.Leaf, "leaf" + count2);
            count2++;
            item.SetFontSize(15);
            return item;
        }
    }
}
