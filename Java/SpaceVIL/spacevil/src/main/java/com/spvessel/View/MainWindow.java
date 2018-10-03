package com.spvessel.View;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.SizePolicy;
import com.spvessel.Items.ButtonCore;
import com.spvessel.Items.TextLine;
import com.spvessel.Windows.ActiveWindow;
import com.spvessel.Windows.WindowLayout;
import com.spvessel.Windows.WindowLayoutBox;

import java.awt.*;

public class MainWindow extends ActiveWindow {
    int count = 0;

    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "MainWindow", "MainWindow", 800, 200, false);
        setHandler(Handler);

        Handler.setMinSize(500, 100);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

        TextLine tl = new TextLine("Add a Member!", DefaultsService.getDefaultFont(Font.PLAIN, 45));
        tl.setForeground(210, 210, 210);
        tl.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        tl.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        tl.setTextAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        Handler.addItem(tl);

        ButtonCore btn = new ButtonCore();
        btn.setSize(200, 50);
        btn.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
        Handler.addItem(btn);

        ButtonCore btn2 = new ButtonCore();
        btn2.setSize(200, 50);
        btn2.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        Handler.addItem(btn2);
        btn.eventMouseClick = (sender, args) -> {
            boolean ok = WindowLayoutBox.tryShow("FlowTest");
            System.out.println(ok);
        };
        btn2.eventMouseClick = (sender, args) -> {
            tl.setItemText("Make a Chance! " + count);
            count++;
        };
    }
}