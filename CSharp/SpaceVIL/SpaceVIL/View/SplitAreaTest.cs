 using System;
 using System.Collections.Generic;
 using System.Linq;
 using System.Text;
 using System.Threading.Tasks;
 using System.Drawing;
 using SpaceVIL;

 namespace View
 {
     class SplitAreaTest : ActiveWindow
     {
         override public void InitWindow()
         {
             Handler = new WindowLayout(this, nameof(SplitAreaTest), nameof(SplitAreaTest), 500, 500, true);
             Handler.SetMinWidth(50);
             Handler.SetMinHeight(50);
             Handler.SetPadding(2, 2, 2, 2);
             Handler.SetBackground(Color.FromArgb(255, 45, 45, 45));

             //DragAnchor
             TitleBar title = new TitleBar(nameof(SplitAreaTest));
             Handler.AddItem(title);

             //SplitArea
             VerticalSplitArea splitArea = new VerticalSplitArea();
             splitArea.SetMargin(0, 30, 0, 0);
             Handler.AddItem(splitArea);

             Style style = new Style();
             style.Background = Color.FromArgb(255, 13, 176, 255);
             style.Foreground = Color.Black;
             style.BorderRadius = 6;
             style.Font = new Font(new FontFamily("Courier New"), 20, FontStyle.Regular);
             style.WidthPolicy = SizePolicy.Expand;
             style.HeightPolicy = SizePolicy.Expand;
             style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
             ItemState brighter = new ItemState();
             brighter.Background = Color.FromArgb(125, 255, 255, 255);
             style.ItemStates.Add(ItemStateType.Hovered, new ItemState()
             {
                 Background = Color.FromArgb(60, 255, 255, 255)
             });

             ButtonCore _button1 = new ButtonCore("btn1");
             ButtonCore _button2 = new ButtonCore("btn2");
             _button1.SetToolTip("Show LayoutTest window.");
             _button1.SetStyle(style);
             _button1.SetItemName("Layout");
             _button1.SetBackground(Color.FromArgb(255, 255, 181, 111));
             _button1.SetMinWidth(30);

             _button2.SetToolTip("Show LayoutTest window.");
             _button2.SetStyle(style);
             _button2.SetItemName("Layout");
             _button2.SetBackground(Color.FromArgb(255, 255, 181, 111));
             _button2.SetMinWidth(30);

             splitArea.AssignLeftItem(_button2);
             splitArea.AssignRightItem(_button1);

             /*
             Console.WriteLine(LogProps.AllGeometry.HasFlag(LogProps.Geometry) + " " +
                 LogProps.AllGeometry.HasFlag(LogProps.Behavior) + " " +
                 LogProps.AllGeometry.HasFlag(LogProps.Position) + " " +
                 LogProps.AllGeometry.HasFlag(LogProps.Margin) + " " +
                 LogProps.AllGeometry.HasFlag(LogProps.Spacing) + " " +
                 LogProps.AllGeometry.HasFlag(LogProps.Color));
                 */
             //LogService.Log().LogVisualItem(_button1, LogProps.AllGeometry);
             //LogService.Log().LogWindow(Handler, LogProps.AllProps);
          
         }
     }
 }
