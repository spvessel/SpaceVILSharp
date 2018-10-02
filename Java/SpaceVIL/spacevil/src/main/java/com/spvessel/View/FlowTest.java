package com.spvessel.View;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.SizePolicy;
import com.spvessel.Items.ButtonCore;
import com.spvessel.Items.TextLine;
import com.spvessel.Windows.ActiveWindow;
import com.spvessel.Windows.WindowLayout;

import java.awt.*;

public class FlowTest extends ActiveWindow {
    int count = 0;

    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "FlowTest", "FlowTest", 800, 800, false);
        setHandler(Handler);

        Handler.setMinSize(500, 100);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

        TextLine tl = new TextLine("FreeArea in future!", DefaultsService.getDefaultFont(Font.PLAIN, 35));
        tl.setForeground(255, 255, 255);
        tl.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        tl.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        tl.setTextAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        Handler.addItem(tl);
    }
}