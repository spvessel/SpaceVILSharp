package com.spvessel.View;

import com.spvessel.Engine.GraphicsMathService;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.SizePolicy;
import com.spvessel.Items.*;
import com.spvessel.Items.Frame;
import com.spvessel.Windows.ActiveWindow;
import com.spvessel.Windows.WindowLayout;

import java.awt.*;
import java.util.List;

public class LabelTest extends ActiveWindow {
    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "LabelTest", "LabelTest");
        Handler.setWidth(500);
        Handler.setMinWidth(200);
        Handler.setHeight(500);
        Handler.setMinHeight(200);
        Handler.setWindowTitle("LabelTest");
        Handler.setPadding(2, 2, 2, 2);
        Handler.setBackground(new Color(45, 45, 45, 255));
        Handler.isBorderHidden = true;

        //DragAnchor
        TitleBar title = new TitleBar("LabelTest");
        Handler.addItem(title);

        //ToolBar
        VerticalStack layout = new VerticalStack();
        layout.setMargin(0, 30, 0, 0);
        layout.setSpacing(0, 5);
        layout.setBackground(255, 255, 255, 20);

        //adding toolbar
        Handler.addItem(layout);

        //Frame
        HorizontalStack toolbar = new HorizontalStack();
        toolbar.setBackground(new Color(60, 60, 60, 255));
        toolbar.setItemName("toolbar");
        toolbar.setHeight(40);
        //toolbar.setPadding(10);
        //toolbar.setSpacing(10);
        toolbar.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        layout.addItem(toolbar);

        //Frame
        Frame frame = new Frame();
        frame.setBackground(new Color(51, 51, 51, 255));
        frame.setItemName("Container");
        frame.setPadding(15, 15, 15, 15);
        frame.setWidthPolicy(SizePolicy.EXPAND);
        frame.setHeightPolicy(SizePolicy.EXPAND);
        layout.addItem(frame);
    }
}
