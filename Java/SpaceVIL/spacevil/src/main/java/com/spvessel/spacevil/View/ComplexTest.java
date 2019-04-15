package com.spvessel.spacevil.View;

import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Flags.*;
import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.TreeItemType;
import com.spvessel.spacevil.Common.DefaultsService;

import java.awt.*;
import java.util.*;

public class ComplexTest extends ActiveWindow {

    TreeView treeview;
    Grid block;
    WrapGrid wrap;
    int count = 0;

    @Override
    public void initWindow() {
        isBorderHidden = true;
        setSize(950, 700);
        setWindowName("ComplexTest");
        setWindowTitle("ComplexTest");

        Style style = Style.getWrapGridStyle();
        Style wrap_style = style.getInnerStyle("area");
        Style wrapper_style = wrap_style.getInnerStyle("selection");
        wrapper_style.addItemState(ItemStateType.HOVERED, new ItemState(new Color(0, 255, 0, 150)));
        wrapper_style.borderRadius = new CornerRadius(6);
        DefaultsService.getDefaultTheme().replaceDefaultItemStyle(WrapGrid.class, style);

        setMinSize(150, 100);
        setBackground(45, 45, 45);
        setPadding(2, 2, 2, 2);

        treeview = new TreeView();
        block = new Grid(4, 4);
        wrap = new WrapGrid(100, 100, Orientation.VERTICAL);
        wrap.setStretch(true);

        TitleBar title = new TitleBar("ComplexTest");
        addItem(title);

        HorizontalStack toolbar = new HorizontalStack();
        toolbar.setBackground(51, 51, 51);
        toolbar.setItemName("toolbar");
        toolbar.setSpacing(6, 0);
        toolbar.setMargin(0, 30, 0, 0);
        toolbar.setHeight(40);
        toolbar.setPadding(10, 0, 0, 0);
        toolbar.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        addItem(toolbar);

        treeview.setItemName("TreeView Fucker!");
        treeview.setMinWidth(100);

        ButtonCore b1 = getButton("b1", 26, 30, SizePolicy.FIXED);
        b1.eventMouseClick.add((sender, args) -> {
            treeview.setRootVisible(!treeview.isRootVisible());
        });
        ButtonCore b2 = getButton("b2", 26, 30, SizePolicy.FIXED);
        b2.eventMouseClick.add((sender, args) -> {
            TreeItem ti = (TreeItem) treeview.getSelectedItem();
            if (ti == null)
                treeview.addItem(getTreeBranch());
            else
                ti.addItem(getTreeBranch());
        });
        ButtonCore b3 = getButton("b3", 26, 30, SizePolicy.FIXED);
        b3.eventMouseClick.add((sender, args) -> {
            TreeItem ti = (TreeItem) treeview.getSelectedItem();
            if (ti == null)
                treeview.addItem(getTreeLeaf());
            else
                ti.addItem(getTreeLeaf());
        });
        ButtonCore b4 = getButton("b4", 26, 30, SizePolicy.FIXED);
        b4.eventMouseClick.add((sender, args) -> {
            // TreeItem ti = (TreeItem) treeview.getSelectedItem();
            // treeview.sortBrunch(ti);
            TreeItem ti = treeview.getRootItem();
            treeview.removeItem(ti);
            TreeItem ti1 = new TreeItem(TreeItemType.BRANCH, "newRoot");
            treeview.addItem(ti1);
        });
        ButtonCore b5 = getButton("b5", 26, 30, SizePolicy.FIXED);
        b5.eventMouseClick.add((sender, args) -> {
            // block.setFormat(block.getRowCount() + 1, block.getColumnCount() + 1);
            TreeItem ti = (TreeItem) treeview.getSelectedItem();
            if (ti != null)
                ti.setText("text");
        });
        ButtonCore b6 = getButton("b6", 26, 30, SizePolicy.FIXED);
        b6.eventMouseClick.add((sender, args) -> {
            // fillBlocks();
            wrap.addItem(getButton("Wrap" + count++, 100, 100, SizePolicy.EXPAND));
        });
        toolbar.addItems(b1, b2, b3, b4, b5, b6);

        Style btn_style = Style.getButtonCoreStyle();
        btn_style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        btn_style.setStyle(b1, b2, b5, b6);

        // HorizontalSplitArea split_area = new HorizontalSplitArea();
        VerticalSplitArea split_area = new VerticalSplitArea();
        split_area.setMargin(0, 72, 0, 0);
        split_area.setSplitPosition(300);
        addItem(split_area);

        split_area.assignLeftItem(treeview);

        ImageItem img = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.IMPORT, EmbeddedImageSize.SIZE_32X32), false);
        img.keepAspectRatio(true);
        img.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        split_area.assignRightItem(wrap);

        for (int i = 0; i < 5; i++) {
            wrap.addItem(getButton("Wrap" + count++, 100, 100, SizePolicy.EXPAND));
        }

        // TabView tabs = new TabView();
        // tabs.setMinWidth(300);
        // split_area.assignRightItem(tabs);
        // tabs.addTab("WrapArea");
        // tabs.addTab("Grid");

        // tabs.addItemToTab("WrapArea", wrap);
        // tabs.addItemToTab("Grid", block);

        // block.insertItem(getButton("Button1", 100, 100, SizePolicy.EXPAND), 0, 0);
        // block.insertItem(getButton("Button2", 100, 100, SizePolicy.EXPAND), 0, 1);
        // block.insertItem(getButton("Button3", 100, 100, SizePolicy.EXPAND), 0, 2);
        // block.insertItem(getButton("Button4", 100, 100, SizePolicy.EXPAND), 0, 3);
        // block.insertItem(getButton("Button5", 100, 100, SizePolicy.EXPAND), 0, 4);
        // block.insertItem(getButton("Button6", 100, 100, SizePolicy.EXPAND), 0, 5);
        // block.insertItem(getButton("Button7", 100, 100, SizePolicy.EXPAND), 0, 6);
        // block.insertItem(getButton("Button8", 100, 100, SizePolicy.EXPAND), 0, 7);

        // getWindow().eventKeyPress.add((sender, args) -> {
        // System.out.println("FocusedItem: " +
        // getHandler().getFocusedItem().getItemName());
        // });

        wrap.eventScrollUp.add((sender, args) -> {
            if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1) {
                wrap.setCellSize(wrap.getCellWidth() + 10, wrap.getCellHeight() + 10);
            }
        });
        wrap.eventScrollDown.add((sender, args) -> {
            if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1) {
                wrap.setCellSize(wrap.getCellWidth() - 10, wrap.getCellHeight() - 10);
            }
        });
    }

    private void fillBlocks() {
        for (int i = 0; i < 4; i++) {
            ButtonCore btn = getButton("Block", 100, 100, SizePolicy.EXPAND);
            btn.eventMouseClick.add((sender, args) -> btn.getParent().removeItem(btn));
            block.addItem(btn);
        }
    }

    private ButtonCore getButton(String name, int w, int h, SizePolicy policy) {
        // Style
        Style style = new Style();
        style.background = new Color(255, 181, 111);
        style.foreground = new Color(0, 0, 0);
        style.borderRadius = new CornerRadius(6);
        style.font = DefaultsService.getDefaultFont();// new Font("Courier New", Font.PLAIN, 16);
        style.width = w;
        style.minWidth = 30;
        // style.maxWidth = 100;
        style.widthPolicy = policy;
        style.height = h;
        style.minHeight = 30;
        // style.maxHeight = 100;
        style.heightPolicy = policy;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.HCENTER));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.HCENTER));
        style.margin = new Indents(5, 5, 5, 5);
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 30);
        style.addItemState(ItemStateType.HOVERED, hovered);

        ItemState pressed = new ItemState(new Color(30, 0, 0, 60));
        pressed.border.setFill(new Color(255, 255, 255));
        pressed.border.setThickness(1);
        style.addItemState(ItemStateType.PRESSED, pressed);

        ButtonCore btn = new ButtonCore(name);
        btn.setSize(w, h);
        btn.setSizePolicy(policy, policy);
        btn.setItemName(name);
        btn.setStyle(style);
        btn.setShadow(10, 3, 3, new Color(0, 0, 0, 160));
        return btn;
    }

    private int count1 = 0;
    private int count2 = 0;

    private TreeItem getTreeBranch() {
        TreeItem item = new TreeItem(TreeItemType.BRANCH, "branch" + count1);
        count1++;
        item.setFont(new Font("Arial Black", Font.PLAIN, 10));
        item.setFontSize(15);
        // item.setText(item.getItemName());
        return item;
    }

    private TreeItem getTreeLeaf() {
        TreeItem item = new TreeItem(TreeItemType.LEAF, "leaf" + count2);
        count2++;
        item.setFontSize(15);
        // item.setText(item.getItemName());
        return item;
    }
}
