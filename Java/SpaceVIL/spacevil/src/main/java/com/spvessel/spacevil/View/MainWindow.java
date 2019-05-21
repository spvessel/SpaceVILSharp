package com.spvessel.spacevil.View;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Flags.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

public class MainWindow extends ActiveWindow {

    @Override
    public void initWindow() {
        isBorderHidden = true;
        setSize(820, 220);
        setWindowName("MainWindow");
        setWindowTitle("MainWindow");
        setMinSize(500, 100);

        // setAspectRatio(4, 1);

        // setPadding(10, 10, 10, 10);
        // setPadding(10, 10, 10, 10);
        // setBackground(new Color(0, 0, 0, 10));
        // setBorderRadius(10);
        // setAntiAliasingQuality(MSAA.MSAA_8X);
        // isTransparent = true;
        // setShadow(10, 0, 0, Color.black);

        // Handler.setAntiAliasingQuality(MSAA.MSAA_8X);
        // Handler.eventClose.clear();
        // Handler.eventClose.add(() -> {
        // MessageItem msg = new MessageItem("Close?", "Are You sure?");
        // msg.onCloseDialog.add(() -> {
        // if (msg.getResult())
        // Handler.close();
        // });
        // msg.show(Handler);
        // });
        // Handler.setBackground(45, 45, 45);
        // Handler.setPadding(2, 2, 2, 2);

        BufferedImage iBig = null;
        BufferedImage iSmall = null;
        try {
            // iBig = ImageIO.read(new
            // File("D:\\Source\\GitHub\\Game2048\\src\\main\\resources\\Game2048.png"));
            // iSmall = ImageIO.read(new
            // File("D:\\Source\\GitHub\\Game2048\\src\\main\\resources\\Game2048.png"));
            iSmall = ImageIO.read(new File("D:\\icon_small.png"));
        } catch (IOException e) {
            System.out.println("load icons fail");
        }
        // if (iBig != null && iSmall != null)
        // Handler.setIcon(iBig, iSmall);

        Frame layout = new Frame();
        layout.setBackground(70, 70, 70);
        layout.setBorderRadius(10);
        layout.setPadding(0, 0, 0, 0);
        layout.setShadow(8, 0, 0, new Color(0, 0, 0, 255));
        layout.setShadowExtension(6, 6);
        addItem(layout);

        TitleBar title = new TitleBar("Main King Window - JAVA");
        title.setAlignment(ItemAlignment.BOTTOM, ItemAlignment.LEFT);
        title.setBorderRadius(new CornerRadius(0, 0, 7, 7));
        title.direction = HorizontalDirection.FROM_RIGHT_TO_LEFT;
        // title.setIcon(iBig, 20, 20);
        layout.addItem(title);

        // Ellipse ellipse = new Ellipse();
        // ellipse.setBackground(Color.WHITE);
        // ellipse.setSize(30, 30);
        // // ellipse.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        // ellipse.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);

        // addItem(ellipse);

        Grid grid = new Grid(1, 7);
        grid.setBorderRadius(new CornerRadius(7, 7, 0, 0));
        grid.setMargin(0, 0, 0, 30);
        grid.setPadding(6, 6, 6, 6);
        grid.setBackground(70, 70, 70, 250);
        grid.setSpacing(6, 6);
        layout.addItem(grid);

        Font font = DefaultsService.getDefaultFont(Font.PLAIN, 16);
        // Font font = DefaultsService.getDefaultFont(18);

        ButtonCore btn_layout = new ButtonCore("Layout");
        btn_layout.setTextAlignment(ItemAlignment.BOTTOM, ItemAlignment.HCENTER);
        btn_layout.setShadow(5, 0, 0, new Color(255, 0, 255, 180));
        btn_layout.setShadowExtension(4, 4);
        btn_layout.setFont(font);
        btn_layout.setToolTip("Show Layout window.");
        btn_layout.setBackground(211, 120, 134);
        btn_layout.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        btn_layout.setBorderRadius(new CornerRadius(6));
        ItemState state = new ItemState(new Color(255, 255, 255, 80));
        state.border.setThickness(10);
        state.border.setFill(new Color(255, 255, 255, 150));
        state.border.setRadius(new CornerRadius(12, 12, 6, 6));
        btn_layout.addItemState(ItemStateType.HOVERED, state);
        InterfaceMouseMethodState layout_click = (sender, args) -> WindowsBox.tryShow("LayoutsTest");
        btn_layout.eventMouseClick.add(layout_click);

        ButtonCore btn_settings = new ButtonCore("Settings");
        btn_settings.setFont(font);
        btn_settings.setToolTip("Show Settings window.");
        btn_settings.setBackground(255, 181, 111);
        btn_settings.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        InterfaceMouseMethodState settings_click = (sender, args) -> WindowsBox.tryShow("SettingsTest");
        btn_settings.eventMouseClick.add(settings_click);

        ButtonCore btn_label = new ButtonCore("Label");
        btn_label.setFont(font);
        btn_label.setToolTip("Show Label window.");
        btn_label.setBackground(111, 181, 255);
        btn_label.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        InterfaceMouseMethodState btn_action_click = (sender, args) -> {
            setShadeColor(new Color( 100, 100, 100, 125));
            MessageBox ms = new MessageBox("Send result?", "Message:");
            ms.show();
            System.out.println(ms.getResult());
            // MessageItem ms = new MessageItem("Send result?", "Message:");
            // ms.show(this);
        };
        btn_label.eventMouseClick.add(btn_action_click);

        ButtonCore btn_flow = new ButtonCore("Flow");
        btn_flow.setItemName("Flow");
        btn_flow.setFont(font);
        btn_flow.setToolTip("Show Flow window.");
        btn_flow.setBackground(193, 142, 221);
        btn_flow.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        // btn_flow.setCursor(EmbeddedCursor.HAND);
        btn_flow.setCursor(iSmall, 10, 30);
        InterfaceMouseMethodState flow_click = (sender, args) -> WindowsBox.tryShow("FlowTest");
        btn_flow.eventMouseClick.add(flow_click);

        ButtonCore btn_complex = new ButtonCore("Complex");
        btn_complex.setFont(font);
        btn_complex.setToolTip("Show Complex window.");
        btn_complex.setBackground(114, 153, 211);
        btn_complex.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        InterfaceMouseMethodState complex_click = (sender, args) -> WindowsBox.tryShow("ComplexTest");
        btn_complex.eventMouseClick.add(complex_click);

        ButtonCore btn_image = new ButtonCore("Image");
        btn_image.setFont(font);
        btn_image.setToolTip("Show Image window.");
        btn_image.setBackground(238, 174, 128);
        btn_image.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        InterfaceMouseMethodState img_click = (sender, args) -> WindowsBox.tryShow("ImageTest");
        btn_image.eventMouseClick.add(img_click);

        ButtonCore btn_input = new ButtonCore("Input");
        btn_input.setFont(font);
        btn_input.setToolTip("Show Input window.");
        btn_input.setBackground(121, 223, 152);
        btn_input.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        btn_input.eventMouseClick.add((sender, args) -> {
            // System.out.println(WindowLayoutBox.getListOfWindows().length);
            WindowsBox.tryShow("InputTest");
        });

        grid.addItems(btn_layout, btn_settings, btn_label, btn_flow, btn_input, btn_image, btn_complex);

        MenuItem mi1 = new MenuItem("MenuItem 1");
        mi1.eventMouseClick.add((sender, args) -> {
            System.out.println("mi1 click");
        });
        MenuItem mi2 = new MenuItem("MenuItem 2");
        mi2.eventMouseClick.add((sender, args) -> {
            System.out.println("mi2 click");
        });
        MenuItem mi3 = new MenuItem("MenuItem 3");
        mi3.eventMouseClick.add((sender, args) -> {
            System.out.println("mi3 click");
        });
        MenuItem mi4 = new MenuItem("MenuItem 4");
        mi4.eventMouseClick.add((sender, args) -> {
            System.out.println("mi4 click");
        });
        ContextMenu menu = new ContextMenu(this, mi1, mi2, mi3, mi4);
        menu.setReturnFocus(btn_flow);

        eventMouseClick.add((sender, args) -> menu.show(sender, args));
        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.MENU) {
                MouseArgs margs = new MouseArgs();
                margs.button = MouseButton.BUTTON_RIGHT;
                menu.show(sender, margs);
            }

            if (args.key == KeyCode.V)
                CommonService.setClipboardString("SetClipBoardString");
            if (args.key == KeyCode.C)
                System.out.println(CommonService.getClipboardString());
            if (args.key == KeyCode.F)
                System.out.println(WindowsBox.getCurrentFocusedWindow().getWindowName());
        });

        btn_flow.setFocus();
        //
        //
        // List<List<Side>> testListSide = new LinkedList<>();
        // LinkedList test1 = new LinkedList();
        // test1.add(Side.TOP);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.BOTTOM);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.RIGHT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.BOTTOM);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.RIGHT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.BOTTOM);
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.BOTTOM);
        // test1.add(Side.RIGHT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.RIGHT);
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.BOTTOM);
        // test1.add(Side.RIGHT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.BOTTOM);
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.LEFT);
        // test1.add(Side.RIGHT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.BOTTOM);
        // test1.add(Side.RIGHT);
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // test1 = new LinkedList();
        // test1.add(Side.TOP);
        // test1.add(Side.BOTTOM);
        // test1.add(Side.RIGHT);
        // test1.add(Side.LEFT);
        // testListSide.add(test1);
        //
        // boolean oldExp = false, newExp = false;
        //
        // for (List<Side> currentTest : testListSide) {
        // oldExp = ((currentTest.contains(Side.RIGHT) &&
        // currentTest.contains(Side.TOP))
        // || (currentTest.contains(Side.RIGHT) && currentTest.contains(Side.BOTTOM))
        // || (currentTest.contains(Side.LEFT) && currentTest.contains(Side.TOP))
        // || (currentTest.contains(Side.LEFT) && currentTest.contains(Side.BOTTOM))
        // || currentTest.contains(Side.LEFT)
        // || currentTest.contains(Side.RIGHT));
        //
        // newExp = (currentTest.contains(Side.LEFT) ||
        // currentTest.contains(Side.RIGHT));
        //
        // System.out.print("test: " + currentTest);
        // if (oldExp == newExp)
        // System.out.println(" OK");
        // else
        // System.out.println(" FAIL");
        // }
        //
        //
    }
}
