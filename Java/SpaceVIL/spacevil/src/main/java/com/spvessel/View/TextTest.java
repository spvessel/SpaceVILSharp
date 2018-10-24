package com.spvessel.View;

import com.spvessel.Windows.*;
import com.spvessel.Items.*;

import java.awt.Color;
import java.awt.Font;

import com.spvessel.Flags.*;

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
        label.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        label.setTextAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        label.setFontSize(150);
        
        label.setForeground(Color.WHITE);
        
        Handler.addItem(label);
    }
}