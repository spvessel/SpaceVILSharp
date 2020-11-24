package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Core.ITextShortcuts;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;

class TextShortcutProcessor {

    static void processShortcut(ITextShortcuts textItem, KeyArgs args) {
        int modsCount = args.mods.size();

        if (modsCount == 1) {

            //copy
            if (args.mods.contains(CommonService.getOsControlMod()) && args.key == KeyCode.C) {
                String copy_str = textItem.getSelectedText();
//                glfwSetClipboardString(_handler.getWindowId(), copy_str);
                CommonService.setClipboardString(copy_str);
                return;
            }

            //undo
            if (args.mods.contains(CommonService.getOsControlMod()) && args.key == KeyCode.Z) {
                textItem.undo();
                return;
            }

            //redo
            if (args.mods.contains(CommonService.getOsControlMod()) && args.key == KeyCode.Y) {
                textItem.redo();
                return;
            }

            //select all
            if (args.mods.contains(CommonService.getOsControlMod())
                    && (args.key == KeyCode.A || args.key == KeyCode.a)) {
                textItem.selectAll();
                return;
            }

            //editable only but has inner checks------------------------------------------------------------------------

            //insert/paste
            if ((args.mods.contains(CommonService.getOsControlMod()) && args.key == KeyCode.V)
                    || (args.mods.contains(KeyMods.Shift) && args.key == KeyCode.Insert)) {
                String paste_str = "";
//                paste_str = glfwGetClipboardString(_handler.getWindowId());
                paste_str = CommonService.getClipboardString();
                textItem.pasteText(paste_str);
                return;
            }

            //cut
            if (args.mods.contains(CommonService.getOsControlMod()) && args.key == KeyCode.X) {
                String cut_str = textItem.cutText();
//                glfwSetClipboardString(_handler.getWindowId(), cut_str);
                if (cut_str == null || cut_str.equals(""))
                    return;
                    
                CommonService.setClipboardString(cut_str);
                return;
            }

        }

    }

}
