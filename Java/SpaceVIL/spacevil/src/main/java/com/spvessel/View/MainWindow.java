package com.spvessel.View;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.SizePolicy;
import com.spvessel.Items.ButtonCore;
import com.spvessel.Items.TextLine;
import com.spvessel.Windows.ActiveWindow;
import com.spvessel.Windows.WindowLayout;

import java.awt.*;

public class MainWindow extends ActiveWindow {
    @Override
    public void initWindow()
    {
        WindowLayout Handler = new WindowLayout(this, "MainWindow", "MainWindow", 800, 200, false);
        setHandler(Handler);

        Handler.setMinSize(500, 100);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

        TextLine tl = new TextLine("Add a Member!", DefaultsService.getDefaultFont(Font.PLAIN,16));
        tl.setForeground(255,255,255);
        tl.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        tl.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        tl.setTextAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        Handler.addItem(tl);

        ButtonCore btn = new ButtonCore();
        btn.setSize(200, 50);
        btn.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
//        Handler.addItem(btn);

        ButtonCore btn2 = new ButtonCore();
        btn2.setSize(200, 50);
        btn2.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
//        Handler.addItem(btn2);

        btn.eventMouseClick = (sender, args) -> System.out.println(btn.getItemName());
        btn2.eventMouseClick = (sender, args) -> System.out.println("another button pressed");
    }
}