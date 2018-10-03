package com.spvessel.View;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.SizePolicy;
import com.spvessel.Items.ButtonCore;
import com.spvessel.Items.Frame;
import com.spvessel.Items.TextLine;
import com.spvessel.Items.TitleBar;
import com.spvessel.Items.VerticalSlider;
import com.spvessel.Windows.ActiveWindow;
import com.spvessel.Windows.WindowLayout;

import java.awt.*;

public class FlowTest extends ActiveWindow {
    int count = 0;

    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "FlowTest", "FlowTest", 800, 800, true);
        setHandler(Handler);

        Handler.setMinSize(500, 100);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

        TitleBar title = new TitleBar("FlowTest");
        Handler.addItem(title);

        Frame layout = new Frame();
        layout.setMargin(0, 30, 0, 0);
        layout.setPadding(6, 6, 6, 6);
        layout.setBackground(70, 70, 70);
        Handler.addItem(layout);

        VerticalSlider v_slider = new VerticalSlider();
        layout.addItem(v_slider);
    }
}