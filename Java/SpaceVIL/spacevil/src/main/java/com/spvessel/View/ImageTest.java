package com.spvessel.View;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.spvessel.Cores.InterfaceCommonMethodState;
import com.spvessel.Cores.InterfaceMouseMethodState;
import com.spvessel.Decorations.Indents;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.SizePolicy;
import com.spvessel.Items.ButtonCore;
import com.spvessel.Items.ComboBox;
import com.spvessel.Items.Frame;
import com.spvessel.Items.HorizontalSlider;
import com.spvessel.Items.HorizontalStack;
import com.spvessel.Items.ImageItem;
import com.spvessel.Items.MenuItem;
import com.spvessel.Items.ProgressBar;
import com.spvessel.Items.Rectangle;
import com.spvessel.Items.TitleBar;
import com.spvessel.Windows.ActiveWindow;
import com.spvessel.Windows.MessageBox;
import com.spvessel.Windows.WindowLayout;

public class ImageTest extends ActiveWindow {
    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "ImageTest", "ImageTest", 500, 500, true);
        setHandler(Handler);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

        // DragAnchor
        TitleBar title = new TitleBar("ImageTest");
        Handler.addItem(title);

        // Frame
        Frame frame = new Frame();
        frame.setBackground(60, 60, 60);
        frame.setItemName("Container");
        frame.setMargin(0, 30, 0, 0);
        frame.setWidthPolicy(SizePolicy.EXPAND);
        frame.setHeightPolicy(SizePolicy.EXPAND);
        Handler.addItem(frame);

        HorizontalStack h_stack = new HorizontalStack();
        h_stack.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        h_stack.setBackground(255, 255, 255, 10);
        h_stack.setHeight(30);
        h_stack.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
        h_stack.setSpacing(5, 0);

        frame.addItem(h_stack);

        Rectangle r1 = new Rectangle();
        r1.setBackground(255, 165, 0);
        r1.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        h_stack.addItem(r1);
        for (int i = 0; i < 4; i++) {
            Rectangle rect = new Rectangle();
            rect.setBackground(255, 255, 0);
            rect.setSize(30, 30);
            rect.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
            h_stack.addItem(rect);
        }

        ProgressBar pb = new ProgressBar();
        pb.setAlignment(ItemAlignment.BOTTOM, ItemAlignment.LEFT);
        pb.setMargin(25, 25, 25, 25);
        frame.addItem(pb);

        
        ButtonCore btn_action = new ButtonCore();
        btn_action.setBackground(100, 255, 150);
        btn_action.setText("Columnar");
        btn_action.setForeground(0, 0, 0);
        btn_action.setItemName("Action");
        btn_action.setWidth(256);
        btn_action.setHeight(128);
        btn_action.setWidthPolicy(SizePolicy.EXPAND);
        btn_action.setHeightPolicy(SizePolicy.FIXED);
        btn_action.setAlignment(ItemAlignment.HCENTER, ItemAlignment.TOP);
        btn_action.setMargin(new Indents(30, 45, 30, 45));
        btn_action.border.setRadius(10);
        
        btn_action.setTextMargin(new Indents(0, 45, 0, 0));
        // btn_action.setTextAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
        // btn_action.setFontSize(300);
        // btn_action.setForeground(100, 55, 55);


        InterfaceMouseMethodState btn_action_click = (sender, args) -> {
            MessageBox ms = new MessageBox("Send result?", "Message:");
            ms.show();
            System.out.println(ms.getResult());
        };
        btn_action.eventMouseClick.add(btn_action_click);
        frame.addItem(btn_action);

        // Image img1 = Image.FromFile("icon.png");
        // Image img1 = Image.FromFile("battery_full.png");
        // Image img1 = Image.FromFile("battery_full_small.png");
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("//home//rsedaikin//Documents//columnar.png"));
        } catch (IOException e) {
        }
        // Image img1 = Image.FromFile("spacevil_logo.png");
        // Image img1 = Image.FromFile("sample.png");
        // Image img1 = Image.FromFile("icon.jpg");

        ImageItem img = new ImageItem(image);
        img.setBackground(new Color(0, 0, 0, 0));
        img.setSize(256, 134);
        img.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        // img.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        img.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        btn_action.addItem(img);

        HorizontalSlider h_slider = new HorizontalSlider();
        h_slider.setAlignment(ItemAlignment.BOTTOM, ItemAlignment.LEFT);
        h_slider.setMargin(25, 25, 25, 100);
        InterfaceCommonMethodState valueChanged = (sender) -> pb.setCurrentValue((int) h_slider.getCurrentValue());
        h_slider.eventValueChanged.add(valueChanged);
        frame.addItem(h_slider);

        // ComboBox combo = new ComboBox();
        // combo.setMargin(25, 0, 25, 0);
        // frame.addItem(combo);

        // for (int i = 0; i < 5; i++) {
        //     MenuItem menu_item = new MenuItem("Custom item for selection #" + i);
        //     combo.addToList(menu_item);
        // }
        // combo.setCurrentIndex(0);
    }
}