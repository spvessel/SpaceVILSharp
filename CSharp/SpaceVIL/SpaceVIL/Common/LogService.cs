using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Drawing;

namespace SpaceVIL
{
    class LogService
    {
        private static LogService _instance;
        static bool isLogging = false;
        //private static StreamWriter fstream = new StreamWriter(@".\Log\note.txt", FileMode.Create);

        private LogService() { }

        public static LogService Log() {
            if (_instance == null)
            {
                _instance = new LogService();
            }
            return _instance;
        }


        public void WriteBase(BaseItem item, LogProps props) {
            StringBuilder outText = new StringBuilder("Item " + item.GetItemName() + 
                ", Handler " + item.GetHandler().GetWindowName() + ":\n");

            if (props.HasFlag(LogProps.Geometry)) {
                outText.Append("Geometry:\n");
                outText.Append("    Width " + item.GetWidth() +
                    " (minWidth " + item.GetMinWidth() + ", maxWidth " + 
                    item.GetMaxWidth() + ")\n");
                outText.Append("    Height " + item.GetHeight() +
                    " (minHeight " + item.GetMinHeight() + ", maxHeight " +
                    item.GetMaxHeight() + ")\n");
                
                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Position)) {
                outText.Append("Position: (" + item.GetX() + ", " + item.GetY() + ")\n");
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

                outText.AppendLine();
                outText.AppendLine();

                SizePolicy sp = item.GetWidthPolicy();
                outText.Append("WidthPolicy: ");
                if (sp.HasFlag(SizePolicy.Expand)) outText.Append("Expand \n");
                if (sp.HasFlag(SizePolicy.Fixed)) outText.Append("Fixed \n");
                if (sp.HasFlag(SizePolicy.Ignored)) outText.Append("Ignored \n");
                if (sp.HasFlag(SizePolicy.Stretch)) outText.Append("Stretch \n");

                sp = item.GetHeightPolicy();
                outText.Append("HeightPolicy: ");
                if (sp.HasFlag(SizePolicy.Expand)) outText.Append("Expand \n");
                if (sp.HasFlag(SizePolicy.Fixed)) outText.Append("Fixed \n");
                if (sp.HasFlag(SizePolicy.Ignored)) outText.Append("Ignored \n");
                if (sp.HasFlag(SizePolicy.Stretch)) outText.Append("Stretch \n");

                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Margin))
            {
                Margin im = item.GetMargin();
                outText.Append("Margin: Left = " + im.Left + ", Right = " + 
                    im.Right + ", Top = " + im.Top + ", Bottom = " + im.Bottom);

                outText.AppendLine();
                outText.AppendLine();
            }

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

            if (props.HasFlag(LogProps.Parent))
            {
                outText.Append("Parent: " + item.GetParent().GetItemName() + "\n");
            }

            AddText(outText.ToString());
        }

        public void WriteVisual(VisualItem item, LogProps props) {
            WriteBase(item, props);
            StringBuilder outText = new StringBuilder();

            if (props.HasFlag(LogProps.Events))
            {
                outText.Append(": ");
                outText.AppendLine();
            }

            if (props.HasFlag(LogProps.Children))
            {
                List<BaseItem> chi = item.GetItems();
                //outText.Append();
            }

            AddText(outText.ToString());
        }

        void EndLogging() {
            //fstream.Close();
        }





        private void AddText(string text) {
            Console.WriteLine(text);
        }
    }

    [Flags]
    public enum LogProps
    {
        Geometry = 0x01,
        Behavior = 0x02,
        Position = 0x04,
        Margin = 0x08,
        Color = 0x10,
        Shape = 0x20,
        Events = 0x40,
        Parent = 0x80,
        Children = 0x100
    }
}
