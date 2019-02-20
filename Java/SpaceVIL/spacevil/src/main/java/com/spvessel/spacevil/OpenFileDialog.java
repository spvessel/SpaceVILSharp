package com.spvessel.spacevil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;
import com.spvessel.spacevil.Flags.SizePolicy;

public class OpenFileDialog extends OpenDialog {
    private ListBox fileList;
    private TextEdit addressLine;
    private ButtonToggle isShowHiddenBtn;

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
        toolbar.setPadding(6, 0, 0, 0);

        Style styleBtn = Style.getButtonCoreStyle();
        styleBtn.setSize(24, 24);
        styleBtn.borderRadius = new CornerRadius();
        ButtonCore btnBackward = new ButtonCore("<");
        ButtonCore btnCreate = new ButtonCore("+");
        ButtonCore btnRename = new ButtonCore("R");
        ButtonCore btnRefresh = new ButtonCore("@");
        btnRefresh.eventMouseClick.add((sender, args) -> refreshFolder());
        ButtonCore btnView = new ButtonCore("=");
        isShowHiddenBtn = new ButtonToggle("H");
        isShowHiddenBtn.setSize(24, 24);
        isShowHiddenBtn.setBorderRadius(0);
        styleBtn.setStyle(btnBackward, btnCreate, btnRename, btnRefresh, btnView);

        // AdresssLine
        addressLine = new TextEdit("D:\\");
        addressLine.setFontSize(14);
        addressLine.setForeground(210, 210, 210);
        addressLine.setBackground(50, 50, 50);
        addressLine.setHeight(24);

        // ListBox --> contect menu
        fileList = new ListBox();

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
        cancelBtn.eventMouseClick.add((sender, args) -> {
            close();
        });

        window.addItems(layout);
        layout.addItems(toolbar, addressLine, fileList, controlPanel);
        toolbar.addItems(btnBackward, btnCreate, btnRename, btnRefresh, btnView, isShowHiddenBtn);
        controlPanel.addItems(openBtn, cancelBtn);

        fileList.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        fileList.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);

        btnBackward.eventMouseClick.add((sender, args) -> {
            String path = addressLine.getText();
            int last = path.lastIndexOf("\\");
            if (last != -1) {
                path = path.substring(0, last);
                addressLine.setText(path);
                refreshFolder();
            }
        });
        refreshFolder();
    }

    private void showFolder(String path) {
        clearListBox();

        File fileFolder = new File(path);
        File[] files = fileFolder.listFiles();
        if (fileList == null)
            return;

        BufferedImage imgFile = null;
        BufferedImage imgFolder = null;

        // try {
        // imgFile =
        // ImageIO.read(OpenFileDialog.class.getResourceAsStream("D:\\icon_small.png"));
        // imgFolder =
        // ImageIO.read(OpenFileDialog.class.getResourceAsStream("D:\\ibig.png"));
        // } catch (IOException e) {
        // System.out.println("icons exception");
        // }

        // Maybe need some sorting

        for (File f : files) {
            if (!isShowHiddenBtn.isToggled() && f.isHidden())
                continue;
            SelectionItem fi;
            if (f.isDirectory()) { // directory
                fi = new SelectionItem();
                fi.setText(f.getName());
                fi.setBackground(255, 255, 255, 80);
                fileList.addItem(fi);
                fi.eventMouseDoubleClick.add((sender, args) -> {
                    String name = addressLine.getText();
                    if (name.endsWith("\\"))
                        addressLine.setText(name + fi.getText());
                    else
                        addressLine.setText(name + "\\" + fi.getText());
                    refreshFolder();
                });
            }
        }

        for (File f : files) {
            if (!isShowHiddenBtn.isToggled() && f.isHidden())
                continue;
            SelectionItem fi;
            if (!f.isDirectory()) { // directory
                fi = new SelectionItem();
                fi.setText(f.getName());
                fileList.addItem(fi);
            }
        }

    }

    public void refreshFolder() {
        String path = addressLine.getText(); // need some check
        showFolder(path);
    }

    private void clearListBox() {
        // List<InterfaceBaseItem> allInner = fileList.getListContent();
        // for (InterfaceBaseItem item : allInner)
        // fileList.removeItem(item);
        // fileList.unselect();
        fileList.setListContent(new LinkedList<InterfaceBaseItem>());
    }
}