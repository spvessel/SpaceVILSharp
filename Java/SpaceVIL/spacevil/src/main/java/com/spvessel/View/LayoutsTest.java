package com.spvessel.View;

import com.spvessel.*;
import com.spvessel.Core.InterfaceMouseMethodState;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.ScrollBarVisibility;
import com.spvessel.Flags.SizePolicy;

public class LayoutsTest extends ActiveWindow {
    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "LayoutsTest", "LayoutsTest", 1000, 1000, true);
        setHandler(Handler);
        Handler.setMinSize(250, 200);
        Handler.setPadding(2, 2, 2, 2);
        Handler.setBackground(45, 45, 45);

        // DragAnchor
        TitleBar title = new TitleBar("LayoutsTest");
        Handler.addItem(title);

        // Bar
        VerticalStack btn_bar = new VerticalStack();
        btn_bar.setBackground(45, 45, 45);
        btn_bar.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        btn_bar.setItemName("ButtonBar");
        btn_bar.setWidthPolicy(SizePolicy.EXPAND);
        btn_bar.setHeightPolicy(SizePolicy.EXPAND);
        btn_bar.setSpacing(10, 10);
        btn_bar.setMargin(0, 30, 0, 0);
        Handler.addItem(btn_bar);

        // Frame
        HorizontalStack frame = new HorizontalStack();
        frame.setSpacing(10, 0);
        frame.setBackground(70, 70, 70);
        frame.setItemName("Frame");
        frame.setHeight(40);
        frame.setPadding(20, 0, 20, 0);
        frame.setWidthPolicy(SizePolicy.EXPAND);
        frame.setHeightPolicy(SizePolicy.FIXED);
        btn_bar.addItem(frame);

        Grid grid = new Grid(2, 2);
        // HorizontalStack grid = new HorizontalStack();
        grid.setPadding(25, 25, 25, 25);
        grid.setSpacing(25, 25);
        btn_bar.addItem(grid);

        ListBox listbox_left_1 = new ListBox();
        listbox_left_1.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        listbox_left_1.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        listbox_left_1.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        grid.insertItem(listbox_left_1, 0, 0);
        ListBox listbox_left_2 = new ListBox();
        listbox_left_2.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        grid.insertItem(listbox_left_2, 1, 0);

        ListBox listbox_right_1 = new ListBox();
        listbox_right_1.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        grid.insertItem(listbox_right_1, 0, 1);
        ListBox listbox_right_2 = new ListBox();
        listbox_right_2.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        grid.insertItem(listbox_right_2, 1, 1);

        // button
        ButtonCore left = new ButtonCore();
        left.setBackground(181, 255, 111);
        left.setItemName("Left");
        left.setText("Left");
        left.setWidth(60);
        left.setHeight(25);
        InterfaceMouseMethodState left_click = (sender, args) -> {
            VisualContact contact_1 = new VisualContact();
            contact_1.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
            VisualContact contact_2 = new VisualContact();
            contact_2.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
            listbox_left_1.addItem(contact_1);
            listbox_left_2.addItem(contact_2);
        };
        left.eventMouseClick.add(left_click);
        frame.addItem(left);

        ButtonCore right = new ButtonCore();
        right.setBackground(255, 181, 111);
        right.setItemName("Right");
        right.setText("Right");
        right.setWidth(60);
        right.setHeight(25);
        InterfaceMouseMethodState right_click = (sender, args) -> {
            VisualContact contact_1 = new VisualContact();
            contact_1.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
            VisualContact contact_2 = new VisualContact();
            contact_2.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
            listbox_right_1.addItem(contact_1);
            listbox_right_2.addItem(contact_2);
        };
        right.eventMouseClick.add(right_click);
        frame.addItem(right);

        ButtonCore all = new ButtonCore();
        all.setBackground(181, 111, 255);
        all.setItemName("Down");
        all.setText("All");
        all.setWidth(60);
        all.setHeight(25);
        InterfaceMouseMethodState all_click = (sender, args) -> {
            RadioButton radio = new RadioButton();
            // CheckBox radio = new CheckBox();
            radio.setText("Another radio button for testing.");
            radio.setHeight(20);
            InterfaceMouseMethodState r_click = (s, a) -> {
                int y_p = radio.getParent().getY();
                int y_c = radio.getY();
                System.out.println(radio.getItemName() + " " + y_p + " " + y_c);
            };
            radio.eventMouseClick.add(r_click);
            listbox_left_1.addItem(radio);
        };
        all.eventMouseClick.add(all_click);
        frame.addItem(all);
    }
}