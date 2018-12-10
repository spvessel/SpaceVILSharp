package com.spacevil.View;

import com.spacevil.ActiveWindow;
import com.spacevil.Label;
import com.spacevil.TitleBar;
import com.spacevil.WindowLayout;

import java.awt.Color;

public class TextTest extends ActiveWindow {
    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "TextTest", "TextTest", 800, 400, true);
        setHandler(Handler);

        Handler.setMinSize(500, 100);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

        TitleBar title = new TitleBar("Text Test");
        Handler.addItem(title);

        Label label = new Label("Helloj odd Y!");
        label.setSizePolicy(com.spacevil.Flags.SizePolicy.EXPAND, com.spacevil.Flags.SizePolicy.EXPAND);
        label.setTextAlignment(com.spacevil.Flags.ItemAlignment.HCENTER, com.spacevil.Flags.ItemAlignment.VCENTER);
        label.setFontSize(150);
        
        label.setForeground(Color.WHITE);
        
        Handler.addItem(label);
    }
}