package com.spvessel.buildtool;

import java.awt.Color;
import java.awt.Font;

import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.TextEdit;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

class Parameter extends Prototype {
    Label parName = new Label();
    TextEdit parField = new TextEdit();

    public String getText() {
        return parField.getText();
    }

    public String getName() {
        return parName.getText();
    }

    Parameter(String name, String par) {
        setMargin(6, 6, 6, 6);
        setBackground(new Color(0, 0, 0, 0));
        setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        setHeight(50);
        parName.setText(name);
        parField.setText(par);
    }

    @Override
    public void initElements() {
        parName.setHeightPolicy(SizePolicy.FIXED);
        parName.setHeight(20);
        parName.setMargin(15, 0, 0, 0);
        parName.setForeground(210, 210, 210);
        parName.setFontSize(14);
        parName.setFontStyle(Font.BOLD);
        parName.setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);

        parField.setBorderRadius(15);
        parField.setAlignment(ItemAlignment.BOTTOM, ItemAlignment.LEFT);
        parField.setMargin(0, 0, 0, 0);
        parField.setShadow(5, 2, 2, new Color(0, 0, 0, 180));

        addItems(parName, parField);
    }
}