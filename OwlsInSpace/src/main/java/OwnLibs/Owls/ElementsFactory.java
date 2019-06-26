package OwnLibs.Owls;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;

import OwnLibs.Owls.Views.Items.KeyWordItem;
import OwnLibs.Owls.Views.Items.OwlsTab;
import OwnLibs.Owls.Views.Windows.OwlWindow;

import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.MenuItem;
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
            button = new ButtonToggle();
        else
            button = new ButtonCore();

        Style funcBtnStyle = Style.getButtonCoreStyle();
        funcBtnStyle.borderRadius = new CornerRadius();
        funcBtnStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        funcBtnStyle.width = 30;
        funcBtnStyle.setBackground(0, 0, 0, 0);
        funcBtnStyle.addItemState(ItemStateType.HOVERED, new ItemState(new Color(0x595959)));
        funcBtnStyle.setPadding(2, 2, 2, 2);

        button.setStyle(funcBtnStyle);
        return button;
    }

    public static Prototype setButtonImage(Prototype button, String imgPath) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(OwlWindow.class.getResourceAsStream(imgPath));
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
        style.background = new Color(0x555555);
        style.foreground = new Color(210, 210, 210);
        style.font = DefaultsService.getDefaultFont(12);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style text_style = Style.getTextBlockStyle();
        text_style.font = DefaultsService.getDefaultFont(14);
        text_style.background = new Color(0x555555);
        text_style.foreground = new Color(200, 200, 200);
        text_style.getInnerStyle("selection").background = new Color(255, 255, 255, 40);
        text_style.getInnerStyle("cursor").background = new Color(0, 162, 232);
        text_style.getInnerStyle("selection").setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        style.addInnerStyle("textedit", text_style);

        Style vsb_style = Style.getSimpleVerticalScrollBarStyle();
        vsb_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.TOP));
        style.addInnerStyle("vscrollbar", vsb_style);

        Style hsb_style = Style.getSimpleHorizontalScrollBarStyle();
        hsb_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.BOTTOM));
        style.addInnerStyle("hscrollbar", hsb_style);

        Style menu_style = new Style();
        menu_style.background = new Color(50, 50, 50);
        menu_style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        menu_style.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        style.addInnerStyle("menu", menu_style);

        return style;
    }

    public static TextArea getTextArea() {
        TextArea tArea = new TextArea();
        tArea.setStyle(ElementsFactory.getTextAreaStyle());
        tArea.setEditable(false);
        tArea.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        tArea.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
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
        return tArea;
    }

    public static OwlsTab getNewTab(String name) {
        OwlsTab tab = new OwlsTab(name);
        tab.setToolTip(name);
        tab.setWidthPolicy(SizePolicy.EXPAND);
        tab.setFontSize(12);
        tab.setBorderRadius(0);
        tab.setClosable(true);
        return tab;
    }
}
