using System.Collections.Generic;
using SpaceVIL;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using View.Decorations;
using FontStyle = System.Drawing.FontStyle;

namespace View.Components
{
    public class PerformanceView : VerticalStack
    {
        public override void InitElements()
        {
            SetPadding(2, 2, 2, 2);

            TabView layoutTabs = new TabView();
            layoutTabs.SetTabPolicy(SizePolicy.Expand);

            Tab shadowTest = new Tab("ShadowTest");
            Tab wrapTest = new Tab("WrapTest");
            Tab wrapWrapTest = new Tab("WrapWrapTest");
            Tab stackTest = new Tab("StackTest");
            Tab gridTest = new Tab("GridTest");

            AddItem(layoutTabs);
            layoutTabs.AddTabs(shadowTest, wrapTest, wrapWrapTest, stackTest, gridTest);

            layoutTabs.AddItemToTab(shadowTest, new ShadowTest(this));
            layoutTabs.AddItemToTab(wrapTest, new WrapTest(this));
            layoutTabs.AddItemToTab(wrapWrapTest, new WrapWrapTest(this));
            layoutTabs.AddItemToTab(stackTest, new ListTest(this));
            layoutTabs.AddItemToTab(gridTest, new GridTest(this));
        }

        internal BlankItem GetBlankItem(int index)
        {
            BlankItem blank = new BlankItem();
            blank.SetItemName("Button_" + index);
            blank.SetBackground(Palette.White);
            blank.SetSize(8, 8);
            blank.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            blank.AddItemState(ItemStateType.Hovered, new ItemState(Palette.Pink));
            blank.AddItemState(ItemStateType.Pressed, new ItemState(Palette.DarkGlass));
            blank.SetMargin(1, 1, 1, 1);
            
            return blank;
        }

        internal void AddManyItems(WrapGrid container, int count)
        {
            var content = new List<IBaseItem>();
            for (int i = 0; i < count; i++)
            {
                content.Add(GetBlankItem(i + 1));
            }
            container.SetListContent(content);
        }
    }

    class WrapTest : Frame
    {
        private PerformanceView _view = null;
        internal WrapTest(PerformanceView view)
        {
            _view = view;
        }

        public override void InitElements()
        {
            WrapGrid wrapGrid = new WrapGrid(9, 9, Orientation.Horizontal);
            wrapGrid.GetArea().SetSpacing(0, 0);
            wrapGrid.SetBackground(Palette.Transparent);
            AddItems(wrapGrid);
            _view.AddManyItems(wrapGrid, 1000);
        }
    }

    class WrapWrapTest : Frame
    {
        private PerformanceView _view = null;
        internal WrapWrapTest(PerformanceView view)
        {
            _view = view;
        }

        public override void InitElements()
        {
            WrapGrid wrapGrid = new WrapGrid(250, 250, Orientation.Horizontal);
            wrapGrid.GetArea().SetSpacing(0, 0);
            wrapGrid.SetMargin(0, 0, 0, 30);
            AddItems(wrapGrid);
            for (int i = 0; i < 8; i++)
            {
                WrapGrid w = new WrapGrid(10, 10, Orientation.Horizontal);
                w.GetArea().SetSpacing(0, 0);
                w.SetMargin(2, 2, 2, 2);
                w.SetBackground(Palette.Black);
                wrapGrid.AddItem(w);
                _view.AddManyItems(w, 1000);
            }
        }
    }

    class ListTest : Frame {
        private PerformanceView _view = null;
        internal ListTest(PerformanceView view)
        {
            _view = view;
        }
        public override void InitElements()
        {
            SetBackground(200, 200, 200);
            ListBox list = new ListBox();
            list.SetSpacing(0, 0);
            AddItems(list);
            int index = 0;
            for (int i = 0; i < 1000; i++)
            {
                IBaseItem blank = _view.GetBlankItem(++index);
                blank.SetSize(14, 14);
                blank.Effects().Add(new Shadow(5, new Position(3, 2), new Size(10, 10), Palette.BlackShadow));
                list.AddItem(blank);
            }
        }
    }

    class GridTest : Frame
    {
        private PerformanceView _view = null;
        internal GridTest(PerformanceView view)
        {
            _view = view;
        }
        public override void InitElements()
        {
            Grid grid = new Grid(20, 50);
            grid.SetSpacing(2, 2);
            AddItems(grid);
            int index = 0;
            for (int i = 0; i < 1000; i++)
            {
                grid.InsertItem(_view.GetBlankItem(index++), i);
            }
        }
    }

    class ShadowTest : Frame {
        private PerformanceView _view = null;
        Label infoOutput = null;
        internal ShadowTest(PerformanceView view)
        {
            _view = view;
        }

        public override void InitElements()
        {
            SetBackground(200, 200, 200);

            infoOutput = new Label("No button pressed");
            infoOutput.SetHeight(25);
            infoOutput.SetHeightPolicy(SizePolicy.Fixed);
            infoOutput.SetBackground(80, 80, 80);
            infoOutput.SetForeground(210, 210, 210);
            infoOutput.SetFontSize(18);
            infoOutput.SetFontStyle(FontStyle.Bold);
            infoOutput.SetAlignment(ItemAlignment.Bottom);
            infoOutput.SetTextAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);
            infoOutput.SetPadding(0, 0, 0, 3);
            
            VerticalStack layout = new VerticalStack();
            layout.SetSpacing(0, 0);
            AddItems(layout);
            layout.AddItem(infoOutput);

            int index = 0;
            for (int i = 0; i < 32; i++)
            {
                HorizontalStack stack = new HorizontalStack();
                stack.SetHeightPolicy(SizePolicy.Fixed);
                stack.SetHeight(22);
                stack.SetSpacing(6, 0);
                stack.SetContentAlignment(ItemAlignment.HCenter);
                layout.AddItem(stack);
                for (int j = 0; j < 32; j++)
                {
                    BlankItem blank = _view.GetBlankItem(++index);
                    blank.EventMouseClick += (sender, args) => {
                        infoOutput.SetText(blank.GetItemName() + " was pressed");
                    };
                    // set size & shadow
                    blank.SetSize(14, 14);
                    blank.Effects().Add(new Shadow(5, new Position(3, 2), new Size(10, 10), Palette.BlackShadow));
                    stack.AddItem(blank);
                }
            }
        }
    }
}