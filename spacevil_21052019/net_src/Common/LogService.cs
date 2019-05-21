using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Drawing;
using SpaceVIL.Decorations;
using SpaceVIL.Core;

namespace SpaceVIL.Common
{
    internal class LogService
    {
        private static LogService _instance;
        private static object logLocker = new object();
        static bool isLogging = true;
        public static string logPath = @".\LogNote.txt";
        private static StreamWriter fstream;

        private LogService() { }

        public static LogService Log()
        {
            lock (logLocker)
            {
                if (_instance == null)
                {
                    _instance = new LogService();
                    try
                    {
                        fstream = new StreamWriter(logPath, false, Encoding.Default);
                        fstream.Close();
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine("Unable to comply\n" + ex);
                    }
                }
                return _instance;
            }
        }

        public void LogText(string text)
        {
            if (!isLogging) return;
            StringBuilder outText = new StringBuilder(GetTime());
            outText.AppendLine(text);
            AddText(outText.ToString());
        }

        public void LogOne<T>(T par, string describe = "", bool flat = true)
        {
            if (!isLogging) return;

            StringBuilder outText = new StringBuilder(GetTime());
            //outText.AppendLine();
            outText.Append(describe + ": ");
            outText.AppendLine(par.ToString());
            outText.AppendLine();
            AddText(outText.ToString());
        }

        public void LogList<T>(List<T> list, string describe = "List", bool flat = true)
        {
            if (!isLogging) return;

            StringBuilder outText = new StringBuilder(GetTime());
            //outText.AppendLine();
            outText.AppendLine(describe);

            foreach (T o in list)
            {
                if (flat)
                {
                    outText.Append(o.ToString() + " ");
                }
                else
                {
                    outText.AppendLine("    " + o.ToString());
                }
            }
            outText.AppendLine();

            AddText(outText.ToString());
        }

        public void LogArr<T>(T[] list, string describe = "Array", bool flat = true)
        {
            if (!isLogging) return;

            StringBuilder outText = new StringBuilder(GetTime());
            //outText.AppendLine();
            outText.AppendLine(describe);

            foreach (T o in list)
            {
                if (flat)
                {
                    outText.Append(o.ToString() + " ");
                }
                else
                {
                    outText.AppendLine("    " + o.ToString());
                }
            }
            outText.AppendLine();

            AddText(outText.ToString());
        }

        public void LogTwoDimArr<T>(T[,] list, string describe = "TwoDim Array")
        {
            if (!isLogging) return;

            StringBuilder outText = new StringBuilder(GetTime());
            //outText.AppendLine();
            outText.AppendLine(describe);

            for (int i = 0; i < list.GetLength(0); i++)
            {
                for (int j = 0; j < list.GetLength(1); j++)
                    outText.Append(list[i, j].ToString() + " ");
                
                outText.AppendLine();
            }
            outText.AppendLine();

            AddText(outText.ToString());
        }

        public void LogListOfArr<T>(List<T[]> list, string describe = "List of arrays")
        {
            if (!isLogging) return;

            StringBuilder outText = new StringBuilder(GetTime());
            //outText.AppendLine();
            outText.AppendLine(describe);

            foreach (T[] o in list)
            {
                outText.Append("    ");
                for (int i = 0; i < o.Length; i++)
                {
                    outText.Append(o[i].ToString() + " ");
                }
                outText.AppendLine();
            }
            outText.AppendLine();

            AddText(outText.ToString());
        }

        public void LogException(Exception ex, IBaseItem item)
        {
            if (!isLogging) return;

            StringBuilder outText = new StringBuilder(GetTime());
            //outText.AppendLine();
            outText.AppendLine("Item name: " + item.GetItemName() +
                ", Handler: " + item.GetHandler().GetWindowName());
            outText.AppendLine("Exception: " + ex.ToString());

            AddText(outText.ToString());
        }

        public void LogIBaseItem(IBaseItem item, LogProps props)
        {
            if (!isLogging) return;

            StringBuilder outText = new StringBuilder(GetTime());
            //outText.AppendLine();
            outText.AppendLine("Item name: " + item.GetItemName() +
                ", Handler: " + item.GetHandler().GetWindowName());

            if (props.HasFlag(LogProps.Geometry))
            {
                outText.AppendLine("Geometry:");
                outText.AppendLine("    Width " + item.GetWidth() +
                    " (minWidth " + item.GetMinWidth() + ", maxWidth " +
                    item.GetMaxWidth() + ")");

                outText.AppendLine("    Height " + item.GetHeight() +
                    " (minHeight " + item.GetMinHeight() + ", maxHeight " +
                    item.GetMaxHeight() + ")");

                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Position))
            {
                outText.AppendLine("Position: (" + item.GetX() + ", " + item.GetY() + ")");
                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Behavior))
            {
                ItemAlignment ia = item.GetAlignment();
                outText.Append("Alignment: ");
                if (ia.HasFlag(ItemAlignment.Left)) outText.Append("Left, ");
                if (ia.HasFlag(ItemAlignment.Right)) outText.Append("Right, ");
                if (ia.HasFlag(ItemAlignment.Top)) outText.Append("Top ");
                if (ia.HasFlag(ItemAlignment.Bottom)) outText.Append("Bottom ");
                if (ia.HasFlag(ItemAlignment.HCenter)) outText.Append("HCenter ");
                if (ia.HasFlag(ItemAlignment.VCenter)) outText.Append("VCenter ");

                outText.AppendLine();
                outText.AppendLine();

                SizePolicy sp = item.GetWidthPolicy();
                outText.Append("WidthPolicy: ");
                if (sp.HasFlag(SizePolicy.Expand)) outText.AppendLine("Expand ");
                if (sp.HasFlag(SizePolicy.Fixed)) outText.AppendLine("Fixed ");
                // if (sp.HasFlag(SizePolicy.Ignored)) outText.AppendLine("Ignored ");
                // if (sp.HasFlag(SizePolicy.Stretch)) outText.AppendLine("Stretch ");

                sp = item.GetHeightPolicy();
                outText.Append("HeightPolicy: ");
                if (sp.HasFlag(SizePolicy.Expand)) outText.AppendLine("Expand ");
                if (sp.HasFlag(SizePolicy.Fixed)) outText.AppendLine("Fixed ");
                // if (sp.HasFlag(SizePolicy.Ignored)) outText.AppendLine("Ignored ");
                // if (sp.HasFlag(SizePolicy.Stretch)) outText.AppendLine("Stretch ");

                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Margin))
            {
                Indents im = item.GetMargin();
                outText.AppendLine("Margin: Left = " + im.Left + ", Right = " +
                    im.Right + ", Top = " + im.Top + ", Bottom = " + im.Bottom);
                outText.AppendLine();
            }

            outText.Append(CheckColNShape(item, props));
            /*
            if (props.HasFlag(LogProps.Color))
            {
                Color ic = item.GetBackground();
                outText.Append("Background(a,r,g,b): (" + ic.A + ", " + ic.R + ", " +
                     ic.G + ", " + ic.B + ")\n");
                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Shape))
            {
                List<float[]> tri = item.GetTriangles();
                outText.Append("Triangles: \n");
                foreach (float[] f in tri)
                {
                    outText.Append("    " + f[0] + " " + f[1] + " " + f[2] + "\n");
                }
                outText.AppendLine();
            }
            */

            if (props.HasFlag(LogProps.Parent))
            {
                outText.AppendLine("Parent: " + item.GetParent().GetItemName());
            }

            AddText(outText.ToString());
        }

        public void LogPrototype(Prototype item, LogProps props)
        {
            if (!isLogging) return;

            LogIBaseItem(item, props);
            StringBuilder outText = new StringBuilder();
            if (props.HasFlag(LogProps.Spacing))
            {
                Spacing spi = item.GetSpacing();
                outText.AppendLine("Spacing: Horizontal = " + spi.Horizontal +
                    ", Vertical = " + spi.Vertical);
                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Padding))
            {
                Indents pdi = item.GetPadding();
                outText.AppendLine("Padding: Left = " + pdi.Left + ", Top = " + pdi.Top +
                    ", Right = " + pdi.Right + ", Bottom = " + pdi.Bottom);
                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Events))
            {
                outText.AppendLine("Events:");

                if (item.EventFocusGet != null) outText.AppendLine("   EventFocusGet");
                if (item.EventFocusLost != null) outText.AppendLine("   EventFocusLost");
                if (item.EventResize != null) outText.AppendLine("   EventResized");
                if (item.EventDestroy != null) outText.AppendLine("   EventDestroyed");
                if (item.EventMouseHover != null) outText.AppendLine("   EventMouseHover");
                if (item.EventMouseClick != null) outText.AppendLine("   EventMouseClick");
                if (item.EventMousePress != null) outText.AppendLine("   EventMousePressed");
                if (item.EventMouseDoubleClick != null) outText.AppendLine("   EventMouseDoubleClick");
                if (item.EventMouseDrag != null) outText.AppendLine("   EventMouseDrag");
                if (item.EventMouseDrop != null) outText.AppendLine("   EventMouseDrop");
                if (item.EventScrollUp != null) outText.AppendLine("   EventScrollUp");
                if (item.EventScrollDown != null) outText.AppendLine("   EventScrollDown");
                if (item.EventKeyPress != null) outText.AppendLine("   EventKeyPress");
                if (item.EventKeyRelease != null) outText.AppendLine("   EventKeyRelease");
                if (item.EventTextInput != null) outText.AppendLine("   EventTextInput");

                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Children))
            {
                outText.AppendLine("Children:");
                List<IBaseItem> chi = item.GetItems();
                foreach (IBaseItem bi in chi)
                {
                    outText.AppendLine("    " + bi.GetItemName());
                }
                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.IsFocused))
            {
                outText.AppendLine("IsFocused: " + item.IsFocused());
            }


            AddText(outText.ToString());
        }

        public void LogEvent(IItem sender, string describe = "EventCommon from", LogProps props = LogProps.None)
        {
            if (!isLogging) return;
            StringBuilder outText = new StringBuilder(GetTime());
            //outText.AppendLine();
            outText.AppendLine(describe + " " + sender.GetItemName());


            outText.Append(CheckColNShape(sender, props));
            AddText(outText.ToString());
        }

        public void LogEvent(IItem sender, MouseArgs args, string describe = "EventMouse from", LogProps props = LogProps.None)
        {
            if (!isLogging) return;
            LogEvent(sender, describe, props);

            StringBuilder outText = new StringBuilder();
            outText.AppendLine("Button: " + args.Button + ", State: " + args.State +
                ", Mods: " + args.Mods + ", Pos: (" + args.Position.GetX() + ", " +
                args.Position.GetY() + ")");

            outText.AppendLine();
            AddText(outText.ToString());
        }

        public void LogEvent(IItem sender, KeyArgs args, string describe = "EventKey from", LogProps props = LogProps.None)
        {
            if (!isLogging) return;
            LogEvent(sender, describe, props);

            StringBuilder outText = new StringBuilder();
            outText.AppendLine("Key: " + args.Key + ", Scancode: " + args.Scancode + ", State: " +
                args.State + ", Mods: " + args.Mods);

            outText.AppendLine();
            AddText(outText.ToString());
        }

        public void LogEvent(IItem sender, TextInputArgs args, string describe = "EventTextInput from", LogProps props = LogProps.None)
        {
            if (!isLogging) return;
            LogEvent(sender, describe, props);

            StringBuilder outText = new StringBuilder();
            outText.AppendLine("Char: " + args.Character + ", Mods: " + args.Mods);

            outText.AppendLine();
            AddText(outText.ToString());
        }

        public void LogWindow(CoreWindow window, LogProps props)
        {
            if (!isLogging) return;

            StringBuilder outText = new StringBuilder(GetTime());
            //outText.AppendLine();
            outText.AppendLine("Window name: " + window.GetWindowName() +
                ", Title: " + window.GetWindowTitle());
            outText.AppendLine();

            if (props.HasFlag(LogProps.Geometry))
            {
                outText.AppendLine("Geometry:");
                outText.AppendLine("    Width " + window.GetWidth() +
                    " (minWidth " + window.GetMinWidth() + ", maxWidth " +
                    window.GetMaxWidth() + ")");

                outText.AppendLine("    Height " + window.GetHeight() +
                    " (minHeight " + window.GetMinHeight() + ", maxHeight " +
                    window.GetMaxHeight() + ")");


                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Position))
            {
                outText.AppendLine("Position: (" + window.GetX() + ", " + window.GetY() + ")");
                outText.AppendLine();
            }
            /*
            if (props.HasFlag(LogProps.Parent))
            {
                outText.AppendLine("Parent: " + window.GetWindow().GetParent().GetItemName());
            }
            */
            if (props.HasFlag(LogProps.Padding))
            {
                Indents pdi = window.GetLayout().GetContainer().GetPadding();
                outText.AppendLine("Padding: Left = " + pdi.Left + ", Top = " + pdi.Top +
                    ", Right = " + pdi.Right + ", Bottom = " + pdi.Bottom);
                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Events))
            {
                outText.AppendLine("Events:");

                // if (window.EventFocusGet != null) outText.AppendLine("   EventFocusGet");
                // if (window.EventFocusLost != null) outText.AppendLine("   EventFocusLost");
                if (window.EventResize != null) outText.AppendLine("   EventResized");
                if (window.EventDestroy != null) outText.AppendLine("   EventDestroyed");
                if (window.EventMouseHover != null) outText.AppendLine("   EventMouseHover");
                if (window.EventMouseClick != null) outText.AppendLine("   EventMouseClick");
                if (window.EventMousePress != null) outText.AppendLine("   EventMousePressed");
                if (window.EventMouseDoubleClick != null) outText.AppendLine("   EventMouseDoubleClick");
                if (window.EventMouseDrag != null) outText.AppendLine("   EventMouseDrag");
                if (window.EventMouseDrop != null) outText.AppendLine("   EventMouseDrop");
                if (window.EventScrollUp != null) outText.AppendLine("   EventScrollUp");
                if (window.EventScrollDown != null) outText.AppendLine("   EventScrollDown");
                if (window.EventKeyPress != null) outText.AppendLine("   EventKeyPress");
                if (window.EventKeyRelease != null) outText.AppendLine("   EventKeyRelease");
                if (window.EventTextInput != null) outText.AppendLine("   EventTextInput");

                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Children))
            {
                outText.AppendLine("Children:");
                List<IBaseItem> chi = window.GetItems();
                foreach (IBaseItem bi in chi)
                {
                    outText.AppendLine("    " + bi.GetItemName());
                }
                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.IsFocused))
            {
                outText.AppendLine("IsFocused: " + window.IsFocused);
                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Color))
            {
                Color ic = window.GetBackground();
                outText.AppendLine("Background(a,r,g,b): (" + ic.A + ", " + ic.R + ", " +
                     ic.G + ", " + ic.B + ")");
                outText.AppendLine();
            }

            AddText(outText.ToString());
        }


        public void EndLogging()
        {
            //fstream.Flush();
            fstream.Close();
        }

        private StringBuilder CheckColNShape(IItem item, LogProps props)
        {
            StringBuilder outText = new StringBuilder();

            if (props.HasFlag(LogProps.Color))
            {
                Color ic = item.GetBackground();
                outText.AppendLine("Background(a,r,g,b): (" + ic.A + ", " + ic.R + ", " +
                     ic.G + ", " + ic.B + ")");
                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Shape))
            {
                List<float[]> tri = item.GetTriangles();
                outText.AppendLine("Triangles:");
                foreach (float[] f in tri)
                {
                    outText.AppendLine("    " + f[0] + " " + f[1] + " " + f[2]);
                }
                outText.AppendLine();
            }

            return outText;
        }

        private string GetTime()
        {
            DateTime now = DateTime.Now;
            StringBuilder str = new StringBuilder();
            str.AppendLine("/////////////////////////////////////////////////////////////");
            str.AppendLine(now.ToString("hh:mm:ss"));
            return str.ToString();
        }

        private void AddText(string text)
        {
            fstream = new StreamWriter(logPath, true, Encoding.Default);
            fstream.WriteLine(text);
            fstream.Close();
        }
    }

    [Flags]
    public enum LogProps
    {
        None = 0x00,
        Geometry = 0x01,
        Behavior = 0x02,
        Position = 0x04,
        Margin = 0x08,

        Color = 0x10,
        Shape = 0x20,
        Events = 0x40,

        Parent = 0x80,
        Children = 0x100,
        Family = 0xC0, //Parent + Children

        IsFocused = 0x200,

        Spacing = 0x400,
        Padding = 0x800,
        AllGeometry = 0xC0F, //Geometry + Position + Behavior + Margin + Spacing + Padding

        AllProps = 0xFFF,
    }
}
