package com.spacevil.View;

import com.spacevil.*;

import java.awt.Color;
import java.awt.Font;

public class SettingsTest extends ActiveWindow
{
        // public settingsTest(String name) : base(name) { }
        MessageBox ms = new MessageBox("Set Button text to True?", "Message:");

    public SettingsTest() {
    }

    @Override
    public void initWindow()
        {
            WindowLayout Handler = new WindowLayout(this, "SettingsTest", "SettingsTest", 600, 550, true);
            setHandler(Handler);
            Handler.setMinSize(600, 550);
            Handler.setBackground(45, 45, 45);
            Handler.setPadding(2, 2, 2, 2);
            int count = 10;

            //Titlebar
            TitleBar title = new TitleBar("Settings Test");
            Handler.addItem(title);

            //Frame
            Frame frame = new Frame();
            frame.setBackground(new Color(60, 60, 60));
            frame.setItemName("Container");
            frame.setMargin(0, 30, 0, 0);
            frame.setPadding(30, 50, 30, 50);
            frame.setWidthPolicy(com.spacevil.Flags.SizePolicy.EXPAND);
            frame.setHeightPolicy(com.spacevil.Flags.SizePolicy.EXPAND);
            Handler.addItem(frame);

            CustomShape random_shape = new CustomShape();
            random_shape.setBackground(new Color(200, 51, 220));
            random_shape.setWidth(50);
            random_shape.setHeight(50);
            random_shape.setWidthPolicy(com.spacevil.Flags.SizePolicy.FIXED);
            random_shape.setHeightPolicy(com.spacevil.Flags.SizePolicy.FIXED);
            frame.addItem(random_shape);
            random_shape.setTriangles(GraphicsMathService.getStar(250, 125, count));

            VerticalStack stack = new VerticalStack();
            stack.setItemName("SettingsTest_VStack");
            stack.setBackground(new Color(51, 51, 51));
            stack.setPadding(20, 20, 20, 20);
            stack.setSpacing(10, 10);
            stack.setWidth(300);
            stack.setWidthPolicy(com.spacevil.Flags.SizePolicy.FIXED);
            stack.setHeightPolicy(com.spacevil.Flags.SizePolicy.EXPAND);
            stack.setAlignment(com.spacevil.Flags.ItemAlignment.HCENTER);
            frame.addItem(stack);

            CheckBox chb = new CheckBox();
            chb.setItemName("SelfDestructor");
            chb.setText("Self destructor.");
            chb.setWidth(200);
            chb.setMinWidth(20);
            chb.setHeight(50);
            chb.setMinHeight(20);
            chb.setWidthPolicy(com.spacevil.Flags.SizePolicy.FIXED);
            chb.setHeightPolicy(com.spacevil.Flags.SizePolicy.FIXED);
            chb.setAlignment(com.spacevil.Flags.ItemAlignment.HCENTER);
            chb.eventMouseClick.add( (sender, args) ->
            {
                chb.setHeight(chb.getHeight() - 5);
                chb.setWidth(chb.getWidth() - 10);
                System.out.println(chb.getWidth());
            });
            stack.addItem(chb);

            ButtonCore btn1 = new ButtonCore();
            stack.addItem(btn1);
            btn1.setBackground(new Color(46, 204, 112));
            btn1.setItemName("PrintWindows");
            btn1.setText("Print all windows.");
            btn1.setForeground(new Color(0,0,0));
            btn1.setWidth(200);
            btn1.setMinWidth(200);
            btn1.setHeight(50);
            btn1.setMinHeight(50);
            btn1.setAlignment(com.spacevil.Flags.ItemAlignment.HCENTER);
            btn1.setWidthPolicy(com.spacevil.Flags.SizePolicy.FIXED);
            btn1.eventMouseClick.add( (sender, args) ->
            {
                System.out.println(btn1.getItemName());
                WindowLayoutBox.tryShow("FlowTest");
            });

            ButtonCore btn2 = new ButtonCore();
            btn2.setBackground(new Color(46, 112, 204));
            btn2.setItemName("ShowMessage");
            btn2.setText("Close settingsTest");
            btn2.setFontStyle(Font.ITALIC);
            btn2.setForeground(new Color(0, 0, 0));
            btn2.setWidth(200);
            btn2.setMinWidth(200);
            btn2.setHeight(50);
            btn2.setMinHeight(50);
            btn2.setWidthPolicy(com.spacevil.Flags.SizePolicy.EXPAND);
            btn2.setHeightPolicy(com.spacevil.Flags.SizePolicy.EXPAND);
            btn2.eventMouseClick.add( (sender, args) ->
            {
                ms.show();
                btn2.setText(String.valueOf(ms.getResult()));
            });
            stack.addItem(btn2);
        }
}