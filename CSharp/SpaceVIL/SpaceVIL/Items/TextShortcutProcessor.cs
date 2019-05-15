using SpaceVIL.Common;
using SpaceVIL.Core;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Items
{
    internal static class TextShortcutProcessor
    {
        internal static void ProcessShortcut(ITextShortcuts textItem, KeyArgs args)
        {
            //copy
            if (args.Mods == KeyMods.Control && args.Key == KeyCode.C)
            {
                string copy_str = textItem.GetSelectedText();
                // Glfw.SetClipboardString(_handler.GetWindowId(), copy_str);
                CommonService.SetClipboardString(copy_str);
                return;
            }
            
            //undo
            if (args.Mods == KeyMods.Control && args.Key == KeyCode.Z)
            {
                textItem.Undo();
                return;
            }
            
            //redo
            if (args.Mods == KeyMods.Control && args.Key == KeyCode.Y)
            {
                textItem.Redo();
                return;
            }
            
            //select all
            if (args.Mods == KeyMods.Control && (args.Key == KeyCode.A || args.Key == KeyCode.a))
            {
                textItem.SelectAll();
                return;
            }

            //editable only but has inner checks------------------------------------------------------------------------

            //insert/paste
            if ((args.Mods == KeyMods.Control && args.Key == KeyCode.V) ||
                (args.Mods == KeyMods.Shift && args.Key == KeyCode.Insert))
            {
                string paste_str = "";
                // string paste_str = Glfw.GetClipboardString(_handler.GetWindowId());
                paste_str = CommonService.GetClipboardString();
                textItem.PasteText(paste_str);
                return;
            }
            
            //cut
            if (args.Mods == KeyMods.Control && args.Key == KeyCode.X)
            {
                string cut_str = textItem.CutText();
                // Glfw.SetClipboardString(_handler.GetWindowId(), cut_str);
                CommonService.SetClipboardString(cut_str);
                return;
            }
        }
    }
}
