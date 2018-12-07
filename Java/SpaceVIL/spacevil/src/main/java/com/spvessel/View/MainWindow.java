package com.spvessel.View;

import com.spvessel.*;
import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.InterfaceMouseMethodState;
import com.spvessel.Decorations.CornerRadius;
import com.spvessel.Decorations.ItemState;
import com.spvessel.Flags.ItemStateType;
import com.spvessel.Flags.SizePolicy;


import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainWindow extends ActiveWindow {

    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "MainWindow", "MainWindow", 800, 200, true);
        setHandler(Handler);

        Handler.setMinSize(500, 100);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);


        BufferedImage iBig = null;
        BufferedImage iSmall = null;
        try {
            iBig = ImageIO.read(new File("D:\\icon_big.png"));
            iSmall = ImageIO.read(new File("D:\\icon_small.png"));
        } catch (IOException e) {
        }
        if (iBig != null && iSmall != null)
            Handler.setIcon(iBig, iSmall);

        TitleBar title = new TitleBar("Main King Window - JAVA");
        Handler.addItem(title);

        Grid grid = new Grid(1, 7);
        grid.setMargin(0, 30, 0, 0);
        grid.setPadding(6, 6, 6, 6);
        grid.setBackground(70, 70, 70);
        grid.setSpacing(6, 6);
        Handler.addItem(grid);

        Font font = DefaultsService.getDefaultFont(Font.PLAIN, 16);
        // Font font = DefaultsService.getDefaultFont(18);

        ButtonCore btn_layout = new ButtonCore("Layout");
        btn_layout.setShadow(5, 3, 3, new Color(0, 0, 0, 140));
        btn_layout.setFont(font);
        btn_layout.setToolTip("Show Layout window.");
        btn_layout.setBackground(255, 151, 153);
        btn_layout.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        btn_layout.setBorderRadius(new CornerRadius(6));
        ItemState state = new ItemState(new Color(255, 255, 255, 80));
        state.border.setThickness(10);
        state.border.setFill(new Color(255, 255, 255, 200));
        state.border.setRadius(new CornerRadius(12, 12, 6, 6));
        btn_layout.addItemState(ItemStateType.HOVERED, state);
        InterfaceMouseMethodState layout_click = (sender, args) -> WindowLayoutBox.tryShow("LayoutsTest");
        btn_layout.eventMouseClick.add(layout_click);

        ButtonCore btn_settings = new ButtonCore("Settings");
        btn_settings.setFont(font);
        btn_settings.setToolTip("Show Settings window.");
        btn_settings.setBackground(255, 181, 111);
        btn_settings.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        InterfaceMouseMethodState settings_click = (sender, args) -> WindowLayoutBox.tryShow("SettingsTest");
        btn_settings.eventMouseClick.add(settings_click);

        ButtonCore btn_label = new ButtonCore("Label");
        btn_label.setFont(font);
        btn_label.setToolTip("Show Label window.");
        btn_label.setBackground(111, 181, 255);
        btn_label.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        InterfaceMouseMethodState btn_action_click = (sender, args) -> {
            MessageBox ms = new MessageBox("Send result?", "Message:");
            ms.show();
            System.out.println(ms.getResult());
        };
        btn_label.eventMouseClick.add(btn_action_click);

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
        btn_input.eventMouseClick.add((sender, args) -> {
            // System.out.println(WindowLayoutBox.getListOfWindows().length);
            WindowLayoutBox.tryShow("InputTest");
        });

        grid.addItems(btn_layout, btn_settings, btn_label, btn_flow, btn_input, btn_image, btn_complex);

    }
}
