using System.Drawing;
using SpaceVIL;
using SpaceVIL.Common;
using SpaceVIL.Core;
using System.Collections.Generic;
using View.Decorations;
using View.Components;
using System.Diagnostics;

namespace View
{
    class MainWindow : ActiveWindow
    {
        override public void InitWindow()
        {
            SetParameters("Sandbox", "Sandbox");
            IsBorderHidden = true;
            IsCentered = true;
            SetAntiAliasingQuality(MSAA.MSAA8x);
            SetPreferredSize(1200, 800);
            SetBackground(32, 32, 32);
            Bitmap iBig = DefaultsService.GetDefaultImage(EmbeddedImage.Home, EmbeddedImageSize.Size64x64);
            Bitmap iSmall = DefaultsService.GetDefaultImage(EmbeddedImage.Home, EmbeddedImageSize.Size32x32);
            SetIcon(iBig, iSmall);
            ImageResources.LoadImages();

            Prototype layout = ComponentsFactory.MakeVerticalStack(2);
            Prototype title = ComponentsFactory.MakeTitleBar(GetWindowTitle(), iSmall);
            VerticalSplitArea splitArea = ComponentsFactory.MakeSplitArea(300);
            ListBox modes = ComponentsFactory.MakeList();
            Prototype viewArea = ComponentsFactory.MakeFrame();

            // adding
            AddItem(layout);
            layout.AddItems(title, splitArea);
            splitArea.SetLeftItem(modes);
            splitArea.SetRightItem(viewArea);

            SetView(viewArea, new WindowsAndDialogsView());

            modes.AddItems(
                ComponentsFactory.MakeActionButton("Windows & Dialogs", () => {
                    SetView(viewArea, new WindowsAndDialogsView());
                }),
                ComponentsFactory.MakeActionButton("Containers", () => {
                    SetView(viewArea, new ContainersView());
                }),
                ComponentsFactory.MakeActionButton("Clickables & Toggles", () => {
                    SetView(viewArea, new ClickablesAndTogglesView());
                }),
                ComponentsFactory.MakeActionButton("Text & Input", () => {
                    SetView(viewArea, new TextAndInputView());
                }),
                ComponentsFactory.MakeActionButton("Menus, Popups & Tooltips", () => {
                    SetView(viewArea, new MenusPopupsAndTooltipsView());
                }),
                ComponentsFactory.MakeActionButton("Sliders & ProgressBars", () => {
                    SetView(viewArea, new SlidersAndProgressBarsView());
                }),
                ComponentsFactory.MakeActionButton("Work with images", () => {
                    SetView(viewArea, new WorkWithImagesView());
                }),
                ComponentsFactory.MakeActionButton("Custom Elements", () => {
                    SetView(viewArea, new CustomElementsView());
                }),
                ComponentsFactory.MakeActionButton("Events & Routing", () => {
                    SetView(viewArea, new EventsAndRoutingView());
                }),
                ComponentsFactory.MakeActionButton("Effects", () => {
                    SetView(viewArea, new EffectsView());
                }),
                ComponentsFactory.MakeActionButton("Styling", () => {
                    SetView(viewArea, new StyleExampleView());
                }),
                ComponentsFactory.MakeActionButton("FreeArea", () => {
                    SetView(viewArea, new FreeAreaView());
                }),
                ComponentsFactory.MakeActionButton("JSon Layout", () => {
                    SetView(viewArea, new JSonLayoutView());
                }),
                ComponentsFactory.MakeActionButton("Performance", () => {
                    Stopwatch stopWatch = new Stopwatch();
                    stopWatch.Start();
                    SetView(viewArea, new PerformanceView());
                    stopWatch.Stop();
                    System.Console.WriteLine("Performance: " + stopWatch.ElapsedMilliseconds + " ms");
                })
            );
        }

        private void SetView(Prototype area, Prototype view)
        {
            List<IBaseItem> items = area.GetItems();
            if (items.Count > 0 && items[0].GetType() == view.GetType())
            {
                return;
            }
            area.Clear();
            area.AddItem(view);
        }

        private void SetPreferredSize(int width, int height)
        {
            // right scaling
            int windowWidth = width, windowHeight = height;
            Scale displayScale = DisplayService.GetDisplayDpiScale();
            int displayWidth = (int) (DisplayService.GetDisplayWidth() * displayScale.GetXScale());
            int displayHeight = (int) (DisplayService.GetDisplayHeight() * displayScale.GetYScale());
            if (windowWidth * displayScale.GetXScale() > displayWidth)
                windowWidth = (int) (windowWidth * 0.80);
            if (windowHeight * displayScale.GetYScale() > displayHeight)
                windowHeight = (int) (windowHeight * 0.80);

            SetSize(windowWidth, windowHeight);
        }
    }
}
