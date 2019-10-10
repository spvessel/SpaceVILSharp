package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Core.InterfaceTextShortcuts;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;

//import static org.lwjgl.glfw.GLFW.glfwGetClipboardString;
//import static org.lwjgl.glfw.GLFW.glfwSetClipboardString;

class TextShortcutProcessor {

    static void processShortcut(InterfaceTextShortcuts textItem, KeyArgs args) {
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
            if (args.mods.contains(CommonService.getOsControlMod()) && (args.key == KeyCode.A || args.key == KeyCode.a)) {
                textItem.selectAll();
                return;
            }

            //editable only but has inner checks------------------------------------------------------------------------

            //insert/paste
            if ((args.mods.contains(CommonService.getOsControlMod()) && args.key == KeyCode.V) ||
                    (args.mods.contains(KeyMods.SHIFT) && args.key == KeyCode.INSERT)) {
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
                CommonService.setClipboardString(cut_str);
                return;
            }

        }

    }


}
