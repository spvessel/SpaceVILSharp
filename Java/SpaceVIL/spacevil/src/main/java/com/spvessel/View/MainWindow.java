package com.spvessel.View;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.InterfaceMouseMethodState;
import com.spvessel.Engine.GraphicsMathService;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.SizePolicy;
import com.spvessel.Items.ButtonCore;
import com.spvessel.Items.ButtonToggle;
import com.spvessel.Items.Ellipse;
import com.spvessel.Items.TextLine;
import com.spvessel.Items.Triangle;
import com.spvessel.Windows.ActiveWindow;
import com.spvessel.Windows.WindowLayout;
import com.spvessel.Windows.WindowLayoutBox;

import com.spvessel.Items.*;

public class MainWindow extends ActiveWindow {

    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "MainWindow", "MainWindow", 800, 200, true);
        setHandler(Handler);

        Handler.setMinSize(100, 100);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

        TitleBar title = new TitleBar("Main King Window");
        Handler.addItem(title);

        Grid grid = new Grid(1, 6);
        grid.setMargin(0, 30, 0, 0);
        grid.setPadding(6, 6, 6, 6);
        grid.setBackground(70, 70, 70);
        grid.setSpacing(6, 6);
        Handler.addItem(grid);

        Font font = new Font("Ubuntu", Font.PLAIN, 18);

        ButtonCore btn_layout = new ButtonCore("Layout");
        btn_layout.setFont(font);
        btn_layout.setToolTip("Show Layout window.");
        btn_layout.setBackground(255, 151, 153);
        btn_layout.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        InterfaceMouseMethodState layout_click = (sender, args) -> WindowLayoutBox.tryShow("LayoutsTest");
        btn_layout.eventMouseClick.add(layout_click);

        ButtonCore btn_label = new ButtonCore("Label");
        btn_label.setFont(font);
        btn_label.setToolTip("Show Label window.");
        btn_label.setBackground(111, 181, 255);
        btn_label.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);

        ButtonCore btn_flow = new ButtonCore("Flow");
        btn_flow.setFont(font);
        btn_flow.setToolTip("Show Flow window.");
        btn_flow.setBackground(193, 142, 221);
        btn_flow.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        InterfaceMouseMethodState flow_click = (sender, args) -> WindowLayoutBox.tryShow("FlowTest");
        btn_flow.eventMouseClick.add(flow_click);
        
        ButtonCore btn_complex = new ButtonCore("Complex");
        btn_complex.setFont(font);
        btn_complex.setToolTip("Show Complex window.");
        btn_complex.setBackground(114, 153, 211);
        btn_complex.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        InterfaceMouseMethodState complex_click = (sender, args) -> WindowLayoutBox.tryShow("ComplexTest");
        btn_complex.eventMouseClick.add(complex_click);

        ButtonCore btn_image = new ButtonCore("Image");
        btn_image.setFont(font);
        btn_image.setToolTip("Show Image window.");
        btn_image.setBackground(238, 174, 128);
        btn_image.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        InterfaceMouseMethodState img_click = (sender, args) -> WindowLayoutBox.tryShow("ImageTest");
        btn_image.eventMouseClick.add(img_click);

        ButtonCore btn_input = new ButtonCore("Input");
        btn_input.setFont(font);
        btn_input.setToolTip("Show Input window.");
        btn_input.setBackground(121, 223, 152);
        btn_input.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);

        grid.addItems(btn_layout, btn_label, btn_flow, btn_input, btn_image, btn_complex);
    }
}