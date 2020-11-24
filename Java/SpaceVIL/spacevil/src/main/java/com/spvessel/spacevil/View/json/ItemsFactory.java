package com.spvessel.spacevil.View.json;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;

public class ItemsFactory {
    public static Object getObject(String name) {
        switch (name.charAt(0)) {
            case 'B': {
                if (name.equals("BlankItem")) {
                    return getBlankItem();
                } else if (name.equals("ButtonCore")) {
                    return getButtonCore();
                } else if (name.equals("ButtonToggle")) {
                    return getButtonToggle();
                }
                break;
            }
            case 'C': {
                if (name.equals("CheckBox")) {
                    return getCheckBox();
                } else if (name.equals("ComboBox")) {
                    return getComboBox();
                } else if (name.equals("ComboBoxDropDown")) {
                    return getComboBoxDropDown();
                    // } else if (name.equals("ContextMenu")) {
                    //     return getContextMenu();
                } else if (name.equals("CustomShape")) {
                    return getCustomShape();
                }
                break;
            }
            case 'E': {
                if (name.equals("Ellipse")) {
                    return getEllipse();
                }
                break;
            }
            case 'F': {
                // } else if (name.equals("FileSystemEntry")) {
                //     return getFileSystemEntry();
                // } else if (name.equals("FloatItem")) {
                //     return getFloatItem();
                if (name.equals("Frame")) {
                    return getFrame();
                } else if (name.equals("FreeArea")) {
                    return getFreeArea();
                }
                break;
            }
            case 'G': {
                if (name.equals("Graph")) {
                    return getGraph();
                } else if (name.equals("Grid")) {
                    return getGrid();
                }
                break;
            }
            case 'H': {
                if (name.equals("HorizontalScrollBar")) {
                    return getHorizontalScrollBar();
                } else if (name.equals("HorizontalSlider")) {
                    return getHorizontalSlider();
                } else if (name.equals("HorizontalSplitArea")) {
                    return getHorizontalSplitArea();
                } else if (name.equals("HorizontalStack")) {
                    return getHorizontalStack();
                }
                break;
            }
            case 'I': {
                if (name.equals("ImageItem")) {
                    return getImageItem();
                } else if (name.equals("Indicator")) {
                    return getIndicator();
                    // } else if (name.equals("InputBox")) {
                    //     return getInputBox();
                    // } else if (name.equals("InputDialog")) {
                    //     return getInputDialog();
                }
                break;
            }
            case 'L': {
                if (name.equals("Label")) {
                    return getLabel();
                } else if (name.equals("LinesContainer")) {
                    return getLinesContainer();
                } else if (name.equals("ListArea")) {
                    return getListArea();
                } else if (name.equals("ListBox")) {
                    return getListBox();
                } else if (name.equals("LoadingScreen")) {
                    return getLoadingScreen();
                }
                break;
            }
            case 'M': {
                if (name.equals("MenuItem")) {
                    return getMenuItem();
                } else if (name.equals("MessageBox")) {
                    return getMessageBox();
                } else if (name.equals("MessageItem")) {
                    return getMessageItem();
                }
                break;
            }
//            case 'O':  {
//                if (name.equals("OpenEntryBox")) {
//                    return getOpenEntryBox();
//                } else if (name.equals("OpenEntryDialog")) {
//                    return getOpenEntryDialog();
//                }
//                break;
//            }

            case 'P': {
                if (name.equals("PasswordLine")) {
                    return getPasswordLine();
                } else if (name.equals("PointsContainer")) {
                    return getPointsContainer();
                    // } else if (name.equals("PopUpMessage")) {
                    //     return getPopUpMessage();
                } else if (name.equals("ProgressBar")) {
                    return getProgressBar();
                }
                break;
            }
            case 'R': {
                if (name.equals("RadioButton")) {
                    return getRadioButton();
                } else if (name.equals("Rectangle")) {
                    return getRectangle();
                } else if (name.equals("ResizableItem")) {
                    return getResizableItem();
                }
                break;
            }
            case 'S': {
                if (name.equals("ScrollHandler")) {
                    return getScrollHandler();
                    // } else if (name.equals("SelectionItem")) {
                    //     return getSelectionItem();
                    // } else if (name.equals("SideArea")) {
                    //     return getSideArea();
                } else if (name.equals("SpinItem")) {
                    return getSpinItem();
                    // } else if (name.equals("SplitHolder")) {
                    //     return getSplitHolder();
                } else if (name.equals("Style") || name.equals("InnerStyle")) { //???
                    return getStyle();
                }
                break;
            }
            case 'T': {
                if (name.equals("Tab")) {
                    return getTab();
                } else if (name.equals("TabView")) {
                    return getTabView();
                } else if (name.equals("TextArea")) {
                    return getTextArea();
                } else if (name.equals("TextEdit")) {
                    return getTextEdit();
                } else if (name.equals("TextView")) {
                    return getTextView();
                } else if (name.equals("TitleBar")) {
                    return getTitleBar();
                    // } else if (name.equals("TreeItem")) {
                    //     return getTreeItem();
                } else if (name.equals("TreeView")) {
                    return getTreeView();
                } else if (name.equals("Triangle")) {
                    return getTriangle();
                }
                break;
            }
            case 'V': {
                if (name.equals("VerticalScrollBar")) {
                    return getVerticalScrollBar();
                } else if (name.equals("VerticalSlider")) {
                    return getVerticalSlider();
                } else if (name.equals("VerticalSplitArea")) {
                    return getVerticalSplitArea();
                } else if (name.equals("VerticalStack")) {
                    return getVerticalStack();
                }
                break;

//            case 'W':
//                if (name.equals("WrapArea")) {
//                    return getWrapArea();
//                } else if (name.equals("WrapGrid")) {
//                    return getWrapGrid();
//                }
//                break;
            }
        }

        return null;
    }

    public static BlankItem getBlankItem() {
        return new BlankItem();
    }

    public static ButtonCore getButtonCore() {
        return new ButtonCore();
    }

    public static ButtonToggle getButtonToggle() {
        return new ButtonToggle();
    }

    public static CheckBox getCheckBox() {
        return new CheckBox();
    }

    public static ComboBox getComboBox() {
        return new ComboBox();
    }

    public static ComboBoxDropDown getComboBoxDropDown() {
        return new ComboBoxDropDown();
    }

    //TODO
    public static ContextMenu getContextMenu(CoreWindow handler) {
        return new ContextMenu(handler);
    }

    //???
    public static CustomShape getCustomShape() {
        return new CustomShape();
    }

    //???
    public static Ellipse getEllipse() {
        return new Ellipse();
    }

    //TODO
    public static FileSystemEntry getFileSystemEntry(FileSystemEntryType type, String text) {
        return new FileSystemEntry(type, text);
    }

    //TODO
    public static FloatItem getFloatItem(CoreWindow handler) {
        return new FloatItem(handler);
    }

    public static Frame getFrame() {
        return new Frame();
    }

    public static FreeArea getFreeArea() {
        return new FreeArea();
    }

    public static Graph getGraph() {
        return new Graph();
    }

    public static Grid getGrid() {
        return new Grid(1, 1); //assume, that I'll set row/colCount later
    }

    public static HorizontalScrollBar getHorizontalScrollBar() {
        return new HorizontalScrollBar();
    }

    public static HorizontalSlider getHorizontalSlider() {
        return new HorizontalSlider();
    }

    public static HorizontalSplitArea getHorizontalSplitArea() {
        return new HorizontalSplitArea();
    }

    public static HorizontalStack getHorizontalStack() {
        return new HorizontalStack();
    }

    public static ImageItem getImageItem() {
        return new ImageItem();
    }

    public static Indicator getIndicator() {
        return new Indicator();
    }

    //TODO
    public static InputBox getInputBox(String title, String actionName, String defaultText) {
        return new InputBox(title, actionName, defaultText);
    }

    //TODO
    public static InputDialog getInputDialog(String title, String actionName, String defaultText) {
        return new InputDialog(title, actionName, defaultText);
    }

    public static Label getLabel() {
        return new Label();
    }

    public static LinesContainer getLinesContainer() {
        return new LinesContainer();
    }

    public static ListArea getListArea() {
        return new ListArea();
    }

    public static ListBox getListBox() {
        return new ListBox();
    }

    public static LoadingScreen getLoadingScreen() {
        return new LoadingScreen();
    }

    public static MenuItem getMenuItem() {
        return new MenuItem();
    }

    public static MessageBox getMessageBox() {
        return new MessageBox();
    }

    public static MessageItem getMessageItem() {
        return new MessageItem();
    }

    //TODO
    public static OpenEntryBox getOpenEntryBox(String title, FileSystemEntryType entryType, OpenDialogType dialogType) {
        return new OpenEntryBox(title, entryType, dialogType);
    }

    //TODO
    public static OpenEntryDialog getOpenEntryDialog(String title, FileSystemEntryType entryType, OpenDialogType dialogType) {
        return new OpenEntryDialog(title, entryType, dialogType);
    }

    public static PasswordLine getPasswordLine() {
        return new PasswordLine();
    }

    public static PointsContainer getPointsContainer() {
        return new PointsContainer();
    }

    //TODO
    public static PopUpMessage getPopUpMessage(String message) {
        return new PopUpMessage(message);
    }

    public static ProgressBar getProgressBar() {
        return new ProgressBar();
    }

    public static RadioButton getRadioButton() {
        return new RadioButton();
    }

    public static Rectangle getRectangle() {
        return new Rectangle();
    }

    public static ResizableItem getResizableItem() {
        return new ResizableItem();
    }

    public static ScrollHandler getScrollHandler() {
        return new ScrollHandler();
    }

    //TODO
    public static SelectionItem getSelectionItem(InterfaceBaseItem content) {
        return new SelectionItem(content);
    }

    //TODO
    public static SideArea getSideArea(CoreWindow handler, Side attachSide) {
        return new SideArea(handler, attachSide);
    }

    public static SpinItem getSpinItem() {
        return new SpinItem();
    }

    //TODO
    public static SplitHolder getSplitHolder(Orientation orientation) {
        return new SplitHolder(orientation);
    }

    public static Style getStyle() {
        return Style.getDefaultCommonStyle();
    }

    public static Tab getTab() {
        return new Tab();
    }

    public static TabView getTabView() {
        return new TabView();
    }

    public static TextArea getTextArea() {
        return new TextArea();
    }

    public static TextEdit getTextEdit() {
        return new TextEdit();
    }

    public static TextView getTextView() {
        return new TextView();
    }

    public static TitleBar getTitleBar() {
        return new TitleBar();
    }

    //TODO
    public static TreeItem getTreeItem(TreeItemType type) {
        return new TreeItem(type);
    }

    public static TreeView getTreeView() {
        return new TreeView();
    }

    public static Triangle getTriangle() {
        return new Triangle();
    }

    public static VerticalScrollBar getVerticalScrollBar() {
        return new VerticalScrollBar();
    }

    public static VerticalSlider getVerticalSlider() {
        return new VerticalSlider();
    }

    public static VerticalSplitArea getVerticalSplitArea() {
        return new VerticalSplitArea();
    }

    public static VerticalStack getVerticalStack() {
        return new VerticalStack();
    }

    //TODO
    public static WrapArea getWrapArea(int cellWidth, int cellHeight, Orientation orientation) {
        return new WrapArea(cellWidth, cellHeight, orientation);
    }

    //TODO
    public static WrapGrid getWrapGrid(int cellWidth, int cellHeight, Orientation orientation) {
        return new WrapGrid(cellWidth, cellHeight, orientation);
    }

}
