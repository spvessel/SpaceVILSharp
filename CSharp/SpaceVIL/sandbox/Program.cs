using System;
using System.Drawing;
using SpaceVIL;
using System.Diagnostics;
using System.Threading;
using View;
using SpaceVIL.Common;
using SpaceVIL.Decorations;
using SpaceVIL.Core;

namespace Program
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine(SpaceVIL.Common.CommonService.GetSpaceVILInfo());

            WindowManager.SetRenderType(RenderType.Always);
            // WindowManager.SetRenderFrequency(RedrawFrequency.Ultra);
            WindowManager.EnableVSync(0);

            // DefaultsService.GetDefaultStyle(typeof(SpaceVIL.ButtonCore)).BorderRadius = new CornerRadius(8);
            // DefaultsService.GetDefaultTheme().ReplaceDefaultItemStyle(typeof(SpaceVIL.ToolTipItem), GetNewToolTipStyle());
            // Bitmap cursor = new Bitmap("D:\\icon_small.png");
            // DefaultsService.SetDefaultCursor(new CursorImage(cursor, 20, 20));

            // MessageBox msg = new MessageBox("IsRunning", "OutputTitle");
            // msg.GetCancelButton().SetVisible(false);
            // msg.Show();
            // msg.OnCloseDialog += () =>
            // {
            //     WindowManager.AppExit();
            // };
            // DefaultsService.GetDefaultTheme().ReplaceDefaultItemStyle(typeof(TextEdit), GetTextEditStyle());
            RenderService.EnableMonitoring(BenchmarkIndicator.Framerate | BenchmarkIndicator.Frametime);

            MainWindow mw = new MainWindow();
            // mw.SetPosition(500, 500);
            Settings sets = new Settings();
            LayoutsTest lt = new LayoutsTest();
            // SplitAreaTest sa = new SplitAreaTest();
            FlowTest flow = new FlowTest();
            // // FlowTest flow2 = new FlowTest();
            ImageTest im = new ImageTest();
            InputTest it = new InputTest();
            // OpenGLTest ogllt = new OpenGLTest();
            // Containers con = new Containers();
            // PerformanceTest pt = new PerformanceTest();
            // EventTest et = new EventTest();
            WindowManager.StartWith(
                mw
                // sets
                // lt
                // flow
                // im
                // it
                // con
                // pt
                // ogllt
                // et
                );
        }
        public static readonly Color Hover = Color.FromArgb(100, 255, 255, 255);
        public static readonly Color KeyBindFocused = Color.FromArgb(138, 220, 255);

        private static Style GetTextEditStyle()
        {
            Style style = Style.GetTextEditStyle();
            Style text = style.GetInnerStyle("text");
            text.AddItemState(ItemStateType.Hovered, new ItemState(Hover));
            text.AddItemState(ItemStateType.Focused, new ItemState(KeyBindFocused));
            return style;
        }

        internal static Style GetNewToolTipStyle()
        {
            Style style = Style.GetToolTipStyle();
            style.Border.SetColor(Color.FromArgb(210, 210, 210));
            style.Border.SetRadius(new CornerRadius(4, 0, 0, 4));
            style.Border.SetThickness(1);
            style.Background = Color.FromArgb(32, 32, 32);
            style.Foreground = Color.FromArgb(210, 210, 210);
            style.SetShadow(new Shadow(10, new Position(10, 10), Color.FromArgb(255, 0, 0)));
            Style textStyle = style.GetInnerStyle("text");
            if (textStyle != null)
            {
                textStyle.SetMargin(30, 50, 0, 50);
                textStyle.SetTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            }
            return style;
        }
    }
}
