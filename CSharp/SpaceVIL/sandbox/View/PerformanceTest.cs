using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace View
{
    public class PerformanceTest : ActiveWindow
    {

        public override void InitWindow()
        {
            SetParameters("PerformanceTest", "Performance Net", 800, 800, true);
            IsCentered = true;
            // SetRenderFrequency(RedrawFrequency.Ultra);
            SetBackground(24, 24, 24);
            SetPadding(2, 30, 2, 2);

            EventKeyRelease += (sender, args) =>
            {
                if (args.Key == KeyCode.Space)
                    Console.WriteLine(ItemsLayoutBox.GetListOfItemsNames(this).Count);
            };

            EventOnStart += () =>
            {
                // WrapTest();
                // StackTest();
                // ShadowTest();
                GridTest();
            };
        }

        private IBaseItem GetButton(int index)
        {
            ButtonCore btn = new ButtonCore();
            btn.SetItemName("Button_" + index);
            btn.SetBackground(Color.White);
            btn.SetSize(8, 8);
            // btn.RemoveAllItemStates();
            // ItemState state = new ItemState(Color.Red);
            // state.Border = new Border(Color.Transparent, new CornerRadius(4), 2);
            // btn.AddItemState(ItemStateType.Hovered, state);
            // btn.SetBorderRadius(3);
            btn.AddItemState(ItemStateType.Hovered, new ItemState(Color.Red));
            btn.EventMouseClick += (sender, args) =>
            {
                Console.WriteLine(btn.GetItemName());
            };

            // btn.SetCustomFigure(new CustomFigure(false, GraphicsMathService.GetRectangle(8, 8, 0, 0)));
            return btn;
        }

        private void AddManyItems(WrapGrid container, int count)
        {
            var content = new List<IBaseItem>();
            for (int i = 0; i < count; i++)
            {
                content.Add(GetButton(i + 1));
            }
            container.SetListContent(content);
        }

        private void WrapTest()
        {
            WrapGrid wrapGrid = new WrapGrid(8, 10, Orientation.Horizontal);
            wrapGrid.GetArea().SetSpacing(2, 1);
            wrapGrid.SetBackground(Color.Transparent);
            AddItems(wrapGrid);
            AddManyItems(wrapGrid, 10000);
        }

        private void StackTest()
        {
            VerticalStack vStack = new VerticalStack();
            vStack.SetSpacing(0, 2);
            AddItems(vStack);
            int index = 0;
            for (int i = 0; i < 50; i++)
            {
                HorizontalStack h = new HorizontalStack();
                h.SetHeightPolicy(SizePolicy.Fixed);
                h.SetHeight(8);
                h.SetSpacing(2, 0);
                vStack.AddItem(h);
                for (int j = 0; j < 70; j++)
                {
                    h.AddItem(GetButton(index++));
                }
            }
        }

        private void GridTest()
        {
            Grid grid = new Grid(100,50);
            grid.SetSpacing(2, 2);
            AddItems(grid);
            int index = 0;
            for (int i = 0; i < 100; i++)
            {
                for (int j = 0; j < 50; j++)
                {
                    grid.AddItem(GetButton(index++));
                }
            }
        }

        private void ShadowTest()
        {
            SetBackground(200, 200, 200);
            VerticalStack v = v = new VerticalStack();
            v.SetSpacing(0, 0);
            AddItems(v);
            int index = 0;
            for (int i = 0; i < 32; i++)
            {
                HorizontalStack h = new HorizontalStack();
                h.SetHeightPolicy(SizePolicy.Fixed);
                h.SetHeight(22);
                h.SetSpacing(6, 0);
                h.SetContentAlignment(ItemAlignment.HCenter);
                v.AddItem(h);
                for (int j = 0; j < 32; j++)
                {
                    IBaseItem btn = GetButton(++index);
                    // btn.SetBackground(Color.FromArgb(100, 255, 255, 255));
                    btn.SetSize(14, 14);
                    Effects.AddEffect(btn, new Shadow(5, new Position(3, 2), new SpaceVIL.Core.Size(10, 10), Color.Black));
                    h.AddItem(btn);
                }
            }
        }
    }
}
