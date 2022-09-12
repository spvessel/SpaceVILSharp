using SpaceVIL;
using SpaceVIL.Core;

namespace View.Components
{
    public class ContainersView: Frame
    {
        public override void InitElements()
        {
            TabView layoutTabs = new TabView();
            layoutTabs.SetTabPolicy(SizePolicy.Expand);

            Tab frameTab = new Tab("Frame");
            frameTab.SetDraggable(false);

            Tab stackTab = new Tab("Stacks");
            Tab gridTab = new Tab("Grid");
            Tab wrapGridTab = new Tab("WrapGrid");
            Tab listTab = new Tab("List");
            Tab treeTab = new Tab("Tree");

            AddItem(layoutTabs);
            layoutTabs.AddTabs(frameTab, stackTab, gridTab, wrapGridTab, listTab, treeTab);

            // Frame
            Frame frame = new Frame();
            frame.SetMargin(30, 30, 30, 30);
            layoutTabs.AddItemToTab(frameTab, frame);
            fillFrame(frame);

            // Stacks
            HorizontalStack commonStack = new HorizontalStack();
            commonStack.SetMargin(30, 30, 30, 30);
            commonStack.SetSpacing(20, 0);
            layoutTabs.AddItemToTab(stackTab, commonStack);
            VerticalStack vStack = new VerticalStack();
            vStack.SetSpacing(0, 10);
            HorizontalStack hStack = new HorizontalStack();
            hStack.SetSpacing(10, 0);
            commonStack.AddItems(vStack, hStack);
            fillContainer(vStack, 5, SizePolicy.Expand);
            fillContainer(hStack, 5, SizePolicy.Expand);

            // Grid
            Grid grid = new Grid(3, 4);
            grid.SetSpacing(20, 20);
            grid.SetMargin(30, 30, 30, 30);
            layoutTabs.AddItemToTab(gridTab, grid);
            fillGrid(grid);

            // WrapGrid
            HorizontalStack wrapGridContainer = new HorizontalStack();
            wrapGridContainer.SetMargin(30, 30, 30, 30);
            wrapGridContainer.SetSpacing(20, 0);
            layoutTabs.AddItemToTab(wrapGridTab, wrapGridContainer);
            WrapGrid vWrapGrid = new WrapGrid(100, 70, Orientation.Horizontal);
            vWrapGrid.SetStretch(true);
            vWrapGrid.GetArea().SetSpacing(10, 10);
            WrapGrid hWrapGrid = new WrapGrid(70, 100, Orientation.Vertical);
            hWrapGrid.GetArea().SetSpacing(10, 10);
            wrapGridContainer.AddItems(vWrapGrid, hWrapGrid);
            fillContainer(vWrapGrid, 20, SizePolicy.Expand);
            fillContainer(hWrapGrid, 20, SizePolicy.Expand);

            // ListBox
            ListBox listBox = new ListBox();
            layoutTabs.AddItemToTab(listTab, listBox);
            fillContainer(listBox, 50, SizePolicy.Fixed);

            // TreeView
            TreeView treeView = new TreeView();
            treeView.SetMargin(10, 30, 5, 5);
            layoutTabs.AddItemToTab(treeTab, treeView);
            treeView.SetRootVisible(true);
            fillTreeView(treeView);
        }

        private void fillFrame(Frame frame)
        {
            Prototype leftTopItem = ComponentsFactory.MakeFixedLabel("LeftTop", 200, 40);
            leftTopItem.SetAlignment(ItemAlignment.Left, ItemAlignment.Top);

            Prototype centerTopItem = ComponentsFactory.MakeFixedLabel("CenterTop", 200, 40);
            centerTopItem.SetAlignment(ItemAlignment.HCenter, ItemAlignment.Top);

            Prototype rightTopItem = ComponentsFactory.MakeFixedLabel("RightTop", 200, 40);
            rightTopItem.SetAlignment(ItemAlignment.Right, ItemAlignment.Top);

            Prototype leftCenterItem = ComponentsFactory.MakeFixedLabel("LeftCenter", 200, 40);
            leftCenterItem.SetAlignment(ItemAlignment.Left, ItemAlignment.VCenter);

            Prototype centerItem = ComponentsFactory.MakeFixedLabel("Center", 200, 40);
            centerItem.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

            Prototype rightCenterItem = ComponentsFactory.MakeFixedLabel("RifgtCenter", 200, 40);
            rightCenterItem.SetAlignment(ItemAlignment.Right, ItemAlignment.VCenter);

            Prototype leftBottomItem = ComponentsFactory.MakeFixedLabel("LeftBottom", 200, 40);
            leftBottomItem.SetAlignment(ItemAlignment.Left, ItemAlignment.Bottom);

            Prototype centerBottomItem = ComponentsFactory.MakeFixedLabel("CenterBottom", 200, 40);
            centerBottomItem.SetAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);

            Prototype rightBottomItem = ComponentsFactory.MakeFixedLabel("RightBottom", 200, 40);
            rightBottomItem.SetAlignment(ItemAlignment.Right, ItemAlignment.Bottom);

            frame.AddItems(leftTopItem, centerTopItem, rightTopItem, leftCenterItem, centerItem, rightCenterItem,
                    leftBottomItem, centerBottomItem, rightBottomItem);
        }

        private void fillGrid(Grid grid)
        {
            for (int i = 0; i < grid.GetColumnCount() * grid.GetRowCount(); i++)
            {
                grid.AddItem(ComponentsFactory.MakeExpandedLabel("" + (i + 1)));
            }
        }

        private void fillContainer(Prototype container, int count, SizePolicy policy)
        {
            for (int i = 0; i < count; i++)
            {
                if (policy == SizePolicy.Expand)
                {
                    container.AddItem(ComponentsFactory.MakeExpandedLabel("" + (i + 1)));
                } else {
                    container.AddItem(ComponentsFactory.MakeFixedLabel("" + (i + 1), 200, 50));
                }
            }
        }

        private void fillTreeView(TreeView tree)
        {
            for (int i = 0; i < 10; i++)
            {
                TreeItem branch = new TreeItem(TreeItemType.Branch, "Branch " + (i + 1));
                tree.AddItem(branch);
                for (int j = 0; j < 3; j++)
                {
                    TreeItem leaf = new TreeItem(TreeItemType.Leaf, "Leaf " + (j + 1));
                    branch.AddItem(leaf);
                }
            }
        }
    }
}