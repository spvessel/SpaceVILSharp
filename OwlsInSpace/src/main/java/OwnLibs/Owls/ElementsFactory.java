package OwnLibs.Owls;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;

import OwnLibs.Owls.Views.Items.KeyWordItem;
import OwnLibs.Owls.Views.Items.ToggledItem;
import OwnLibs.Owls.Views.Items.FileEntryTab;
import OwnLibs.Owls.Views.Windows.MainWindow;

import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.MenuItem;
import com.spvessel.spacevil.Rectangle;
import com.spvessel.spacevil.TextArea;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class ElementsFactory {
    static TreeItem getTreeItem(String name, TreeItemType tiType) {
        TreeItem treeItem = new TreeItem(tiType);
        treeItem.setFontSize(15);
        treeItem.setText(name);
        return treeItem;
    }

    public static Prototype getFunctionalButton(boolean isToggle) {
        Prototype button;
        if (isToggle)
            button = new ToggledItem();
        else
            button = new ButtonCore();

        Style funcBtnStyle = Style.getButtonCoreStyle();
        funcBtnStyle.borderRadius = new CornerRadius();
        funcBtnStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        funcBtnStyle.width = 30;
        funcBtnStyle.setBackground(0, 0, 0, 0);
        // funcBtnStyle.addItemState(ItemStateType.HOVERED, new ItemState(new Color(0x595959)));
        funcBtnStyle.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 20)));
        funcBtnStyle.setPadding(2, 2, 2, 2);

        button.setStyle(funcBtnStyle);
        return button;
    }

    public static Prototype setButtonImage(Prototype button, String imgPath) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(MainWindow.class.getResourceAsStream(imgPath));
        } catch (IOException e) {
            System.out.println("Icons loading exception: " + e);
        }

        ImageItem imgFile = new ImageItem(img, false);
        imgFile.keepAspectRatio(true);
        button.addItem(imgFile);
        return button;
    }

    public static Prototype setButtonImage(Prototype button, BufferedImage img) {
        ImageItem imgFile = new ImageItem(img, false);
        imgFile.setMargin(4, 4, 4, 4);
        imgFile.keepAspectRatio(true);
        button.addItem(imgFile);
        
        if (button instanceof ButtonToggle) {
            imgFile.setColorOverlay(new Color(209, 108, 108));
            button.setShadow(5, 0, 0, Color.black);
            button.setBackground(55, 55, 55);
            button.setShadowDrop(false);
        }
        return button;
    }

    public static MenuItem setMenuItemImage(MenuItem menuItem, BufferedImage img) {
        // Style imgBtnStyle = Style.getImageItemStyle();
        // imgBtnStyle.background = new Color(0, 0, 0, 0);
        // imgBtnStyle.setSize(16, 16);
        // imgBtnStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        // imgBtnStyle.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);

        ImageItem imgFile = new ImageItem(img, false);
        imgFile.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        imgFile.setSize(16, 16);
        imgFile.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        imgFile.keepAspectRatio(true);
        // imgFile.setStyle(imgBtnStyle);
        menuItem.addItem(imgFile);
        return menuItem;
    }

    public static MenuItem setMenuItemImage(MenuItem menuItem, BufferedImage img, Color overlay) {

        ImageItem imgFile = new ImageItem(img, false);
        imgFile.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        imgFile.setSize(16, 16);
        imgFile.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        imgFile.keepAspectRatio(true);
        imgFile.setColorOverlay(overlay);
        menuItem.addItem(imgFile);
        return menuItem;
    }

    public static KeyWordItem getKeyWord(String word) {
        KeyWordItem kwItem = new KeyWordItem(word);

        // kwButton.setText(word);
        // kwButton.setWidthPolicy(SizePolicy.EXPAND);
        return kwItem;
    }

    public static Style getMenuStyle() {
        Style style = Style.getMenuItemStyle();
        style.getInnerStyle("text").setMargin(25, 0, 0, 0);
        style.setForeground(210, 210, 210);
        style.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 30)));
        return style;
    }

    public static InterfaceBaseItem searchResultItem(String text, HorizontalStack hStack) {
        CustomShape dot = new CustomShape();
        Style leaf_icon_style = new Style();
        leaf_icon_style.background = new Color(129, 187, 133);
        leaf_icon_style.setSize(6, 6);
        leaf_icon_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        leaf_icon_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        leaf_icon_style.shape = GraphicsMathService.getEllipse(3, 16);
        leaf_icon_style.margin = new Indents(15, 0, 5, 0);
        dot.setStyle(leaf_icon_style);

        // hStack = new HorizontalStack();
        // hStack.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        // hStack.setHeight(30);
        Label label = new Label(text);
        label.setFontSize(13);
        hStack.addItems(dot, label);

        return hStack;
    }

    public static void removeListBoxSelection(InterfaceBaseItem item, ListBox listBox) {
        Style style = Style.getSelectionItemStyle();
        SelectionItem selection = (SelectionItem) listBox.getWrapper(item);
        style.setSize(selection.getWidth(), selection.getHeight());
        style.setMinSize(selection.getMinWidth(), selection.getMinHeight());
        style.getState(ItemStateType.TOGGLED).background = new Color(0, 0, 0, 0);
        selection.setStyle(style);
    }

    public static Style getTextAreaStyle() {
        Style style = new Style();
        style.background = new Color(75, 75, 75);
        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style text_style = Style.getTextBlockStyle();
        text_style.font = DefaultsService.getDefaultFont(16);
        text_style.foreground = new Color(190, 190, 190);
        text_style.getInnerStyle("selection").background = new Color(255, 255, 255, 25);
        text_style.getInnerStyle("cursor").background = new Color(0, 162, 232);
        text_style.getInnerStyle("selection").setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        style.addInnerStyle("textedit", text_style);

        style.addInnerStyle("vscrollbar", getVScrollBarStyle());
        style.addInnerStyle("hscrollbar", getHScrollBarStyle());

        Style menu_style = new Style();
        menu_style.background = new Color(50, 50, 50);
        menu_style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        menu_style.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        style.addInnerStyle("menu", menu_style);

        return style;
    }

    public static Style getVScrollBarStyle() {
        Style vsb_style = Style.getSimpleVerticalScrollBarStyle();
        vsb_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.TOP));
        vsb_style.width = 10;
        vsb_style.setMargin(0, 0, 5, 0);
        vsb_style.getInnerStyle("slider").setBackground(60, 60, 60);
        vsb_style.getInnerStyle("slider").addItemState(ItemStateType.HOVERED, new ItemState(new Color(0, 0, 0, 30)));
        vsb_style.getInnerStyle("slider").borderRadius = new CornerRadius(5);
        vsb_style.getInnerStyle("slider").setPadding(0, 2, 0, 2);
        vsb_style.getInnerStyle("slider").getInnerStyle("handler").setMargin(2, 0, 2, 0);
        // vsb_style.getInnerStyle("slider").getInnerStyle("handler")
        //         .setShadow(new Shadow(8, 0, 0, new Color(0, 0, 0, 255)));
        // vsb_style.getInnerStyle("slider").getInnerStyle("handler").isShadowDrop = true;
        return vsb_style;
    }

    public static Style getHScrollBarStyle() {
        Style hsb_style = Style.getSimpleHorizontalScrollBarStyle();
        hsb_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.BOTTOM));
        hsb_style.height = 10;
        hsb_style.setMargin(0, 0, 0, 5);
        hsb_style.getInnerStyle("slider").setBackground(60, 60, 60);
        hsb_style.getInnerStyle("slider").addItemState(ItemStateType.HOVERED, new ItemState(new Color(0, 0, 0, 30)));
        hsb_style.getInnerStyle("slider").borderRadius = new CornerRadius(5);
        hsb_style.getInnerStyle("slider").setPadding(2, 0, 2, 0);
        hsb_style.getInnerStyle("slider").getInnerStyle("handler").setMargin(0, 2, 0, 2);
        // vsb_style.getInnerStyle("slider").getInnerStyle("handler")
        //         .setShadow(new Shadow(8, 0, 0, new Color(0, 0, 0, 255)));
        // vsb_style.getInnerStyle("slider").getInnerStyle("handler").isShadowDrop = true;
        return hsb_style;
    }

    public static TextArea getTextArea() {
        TextArea tArea = new TextArea();
        tArea.setStyle(ElementsFactory.getTextAreaStyle());
        tArea.setWrapText(true);
        tArea.setEditable(false);
        tArea.setVScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
        tArea.setHScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
        tArea.disableMenu(true);
        tArea.menu.setDrawable(false);

        tArea.eventKeyPress.add((sender, args) -> {
            if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.EQUAL) {
                int fontSize = tArea.getFont().getSize();
                fontSize++;
                if (fontSize > 32)
                    return;
                tArea.setFontSize(fontSize);
            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.MINUS) {
                int fontSize = tArea.getFont().getSize();
                fontSize--;
                if (fontSize < 10)
                    return;
                tArea.setFontSize(fontSize);
            }
        });
        tArea.setScrollStepFactor(3);
        return tArea;
    }

    public static FileEntryTab getNewTab(String name) {
        FileEntryTab tab = new FileEntryTab(name);
        tab.setToolTip(name);
        // tab.setWidthPolicy(SizePolicy.EXPAND);
        tab.setFontSize(12);
        tab.setBorderRadius(0);
        tab.setClosable(true);
        return tab;
    }

    public static Style getHeaderLabelStyle() {
        Style style = Style.getLabelStyle();
        style.foreground = new Color(20, 180, 255);
        style.font = DefaultsService.getDefaultFont(Font.PLAIN, 16);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        style.height = 50;
        style.setMargin(0, 30, 0, 0);
        return style;
    }

    public static Style getCasualLabelStyle() {
        Style style = Style.getLabelStyle();
        style.font = DefaultsService.getDefaultFont(12);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        style.height = 25;
        return style;
    }

    public static Style getIndicatorStyle() {

        Style style = Style.getCheckBoxStyle();
        Style marker = style.getInnerStyle("indicator").getInnerStyle("marker");
        marker.addItemState(ItemStateType.TOGGLED, new ItemState(new Color(10, 162, 232)));

        return style;
    }

    public static InterfaceBaseItem getVerticalDivider() {
        Rectangle divider = new Rectangle();
        divider.setHeightPolicy(SizePolicy.EXPAND);
        divider.setWidth(1);
        divider.setMargin(5, 6, 5, 6);
        divider.setBackground(80, 80, 80);
        return divider;
    }
}
