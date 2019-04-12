package com.spvessel.spacevil.View;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.awt.Color;

public class TextTest extends ActiveWindow {
    @Override
    public void initWindow() {
        isBorderHidden = true;
        setSize(800, 400);
        setWindowName("TextTest");
        setWindowTitle("TextTest");

        setMinSize(500, 100);
        setBackground(45, 45, 45);
        setPadding(2, 2, 2, 2);

        TitleBar title = new TitleBar("Text Test");
        addItem(title);

        Label label = new Label("Helloj odd Y!");
        label.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        label.setTextAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        label.setFontSize(150);
        
        label.setForeground(Color.WHITE);
        
        addItem(label);
    }
}