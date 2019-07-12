package com.spvessel.spacevil.View;

import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.RedrawFrequency;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceMouseMethodState;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Flags.SizePolicy;

public class LayoutsTest extends ActiveWindow {
    @Override
    public void initWindow() {
        isBorderHidden = true;
        setSize(800, 1000);
        setWindowName("LayoutsTest");
        setWindowTitle("LayoutsTest");

        setMinSize(250, 200);
        setPadding(2, 2, 2, 2);
        setBackground(45, 45, 45);

        // setRenderFrequency(RedrawFrequency.ULTRA);

        // DragAnchor
        TitleBar title = new TitleBar("LayoutsTest");
        addItem(title);

        // Bar
        VerticalStack btn_bar = new VerticalStack();
        btn_bar.setBackground(45, 45, 45);
        btn_bar.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        btn_bar.setItemName("ButtonBar");
        btn_bar.setWidthPolicy(SizePolicy.EXPAND);
        btn_bar.setHeightPolicy(SizePolicy.EXPAND);
        btn_bar.setSpacing(10, 10);
        btn_bar.setMargin(0, 30, 0, 0);
        addItem(btn_bar);

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
        // listbox_left_1.setVisible(false);
        listbox_left_1.eventMouseClick.add((sender, args) -> {
            // System.out.println(listbox_left_1.getSelection() + " " +
            // listbox_left_1.getSelectionItem().getItemName());
        });
        // listbox_left_1.setSelectionVisibility(false);
        // listbox_left_1.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        // listbox_left_1.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
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

        // VisualContact visualContact = new VisualContact();
        // visualContact.eventMouseClick.add((sender, args) -> {
        //     System.out.println("visualContact");
        // });
        // listbox_left_1.addItem(visualContact);

        // button
        ButtonCore left = new ButtonCore();
        left.setBackground(181, 255, 111);
        left.setItemName("Left");
        left.setText("Left");
        left.setWidth(60);
        left.setHeight(25);
        InterfaceMouseMethodState left_click = (sender, args) -> {
            // VisualContact contact_1 = new VisualContact();
            // contact_1.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
            VisualContact contact_2 = new VisualContact();
            listbox_left_1.addItem(contact_2);
            // listbox_left_2.addItem(contact_2);
            // listbox_left_1.addItem(new Album("Album", "C:\\"));
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
            // VisualContact c_1 = new VisualContact();
            // VisualContact c_2 = new VisualContact();
            // VisualContact c_3 = new VisualContact();
            // VisualContact c_4 = new VisualContact();
            // VisualContact r_1 = new VisualContact();
            // VisualContact r_2 = new VisualContact();
            // VisualContact r_3 = new VisualContact();
            // VisualContact r_4 = new VisualContact();
            // listbox_right_1.setListContent(Arrays.asList(c_1, c_2, c_3, c_4));
            // listbox_right_2.setListContent(Arrays.asList(r_4, r_3, r_2, r_1));
            listbox_left_1.clear();
            // System.gc();
            // gc();
            Runtime r = Runtime.getRuntime();
            r.gc();
        };
        right.eventMouseClick.add(right_click);
        frame.addItem(right);

        ButtonCore all = new ButtonCore();
        all.setBackground(181, 111, 255);
        all.setItemName("Down");
        all.setText("All");
        all.setWidth(60);
        all.setHeight(25);
        Set<InterfaceBaseItem> list = new LinkedHashSet<>();
        InterfaceMouseMethodState all_click = (sender, args) -> {
            // listbox_left_1.setSelectionVisibility(!listbox_left_1.getSelectionVisibility());
            // RadioButton radio = new RadioButton();
            // // CheckBox radio = new CheckBox();
            // radio.setText("Another radio button for testing.");
            // radio.setHeight(20);
            // InterfaceMouseMethodState r_click = (s, a) -> {
            // int y_p = radio.getParent().getY();
            // int y_c = radio.getY();
            // System.out.println(radio.getItemName() + " " + y_p + " " + y_c);
            // };
            // radio.eventMouseClick.add(r_click);
            // listbox_left_1.addItem(radio);
            // listbox_left_1.setVisible(!listbox_left_1.isVisible());

            listbox_left_1.clear();
            // list.clear();
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
            listbox_left_1.addItem(new VisualContact());
            }
            System.out.println("Fuction run " + (System.currentTimeMillis() - startTime)
            + " ms");
            System.gc();

            // list.clear();
            // for (int i = 0; i < 1000; i++) {
            //     list.add(new VisualContact());
            // }
            // System.gc();
        };
        all.eventMouseClick.add(all_click);
        frame.addItem(all);
    }

    public static void gc() {
        Object obj = new Object();
        WeakReference ref = new WeakReference<Object>(obj);
        obj = null;
        while (ref.get() != null) {
            System.gc();
        }
    }
}