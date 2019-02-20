package com.spvessel.spacevil;

import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;
import com.spvessel.spacevil.Flags.SizePolicy;

public class OpenFileDialog extends OpenDialog {
    public OpenFileDialog() {

    }

    @Override
    public void initElements() {
        // important!
        super.initElements();

        // elements
        VerticalStack layout = new VerticalStack();
        layout.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        layout.setMargin(0, 30, 0, 0);
        layout.setPadding(6, 6, 6, 6);
        layout.setSpacing(0, 2);
        layout.setBackground(255, 255, 255, 20);

        // Create, View, Backward, Refresh, Rename
        HorizontalStack toolbar = new HorizontalStack();
        toolbar.setHeightPolicy(SizePolicy.FIXED);
        toolbar.setHeight(30);
        toolbar.setBackground(45, 45, 45);
        toolbar.setSpacing(3, 0);
        toolbar.setPadding(6,0,0,0);

        Style styleBtn = Style.getButtonCoreStyle();
        styleBtn.setSize(24, 24);
        styleBtn.borderRadius = new CornerRadius();
        ButtonCore btnBackward = new ButtonCore("<");
        ButtonCore btnCreate = new ButtonCore("+");
        ButtonCore btnRename = new ButtonCore("R");
        ButtonCore btnRefresh = new ButtonCore("@");
        ButtonCore btnView = new ButtonCore("=");
        styleBtn.setStyle(btnBackward, btnCreate, btnRename, btnRefresh, btnView);

        // AdresssLine
        TextEdit addressLine = new TextEdit();
        addressLine.setFontSize(14);
        addressLine.setForeground(210, 210, 210);
        addressLine.setBackground(50, 50, 50);
        addressLine.setHeight(24);

        // ListBox --> contect menu
        ListBox fileList = new ListBox();
        
        // SelectionItem: icon, text etc --> context menu
        
        // Open, Cancel
        Frame controlPanel = new Frame();
        controlPanel.setHeightPolicy(SizePolicy.FIXED);
        controlPanel.setHeight(40);
        controlPanel.setBackground(45, 45, 45);
        controlPanel.setPadding(6, 6, 6, 6);
        ButtonCore openBtn = new ButtonCore("Open");
        openBtn.setSize(100, 30);
        openBtn.setAlignment(ItemAlignment.VCENTER, ItemAlignment.RIGHT);
        openBtn.setMargin(0, 0, 105, 0);
        openBtn.setBorderRadius(0);
        ButtonCore cancelBtn = new ButtonCore("Cancel");
        cancelBtn.setSize(100, 30);
        cancelBtn.setAlignment(ItemAlignment.VCENTER, ItemAlignment.RIGHT);
        cancelBtn.setBorderRadius(0);
        
        window.addItems(layout);
        layout.addItems(toolbar, addressLine, fileList, controlPanel);
        toolbar.addItems(btnBackward, btnCreate, btnRename, btnRefresh, btnView);
        controlPanel.addItems(openBtn, cancelBtn);
        
        fileList.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        fileList.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
    }
}