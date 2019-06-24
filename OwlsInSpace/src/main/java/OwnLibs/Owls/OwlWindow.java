package OwnLibs.Owls;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.MenuItem;
import com.spvessel.spacevil.TextArea;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OwlWindow extends ActiveWindow {
    TitleBar titleBar;

    ButtonCore newFileBtn;
    ButtonCore newFolderBtn;
    ButtonCore importBtn;
    ButtonCore saveBtn;
    ButtonCore deleteBtn;
    ButtonCore searchBtn;
    ButtonToggle editBtn;
    ButtonCore backBtn;

    TreeView filesTree;
    // TextArea codeArea;
    TabView workTabArea;

    ButtonCore addKeyWordsBtn;
    HorizontalStack keyWordsStack;

    ContextMenu treeContextMenu;
    MenuItem miTreeDelete;
    MenuItem miKWDelete;
    MenuItem miKWUse;
    MenuItem miRename;
    MenuItem miAddExist;
    MenuItem miNewFile;
    MenuItem miNewFolder;

    ContextMenu keyWordsContextMenu;

    ListBox kwResultsListBox;
    Label kwResultLabel1;
    Label kwResultLabel2;

    @Override
    public void initWindow() {
        setParameters("OwlWindow", "OwlWindow", 1300, 800, false);
        setMinSize(400, 400);
        setPadding(2, 2, 2, 2);
        isCentered = true;

        BufferedImage iBig = null;
        BufferedImage iSmall = null;
        try {
            iBig = ImageIO.read(OwlWindow.class.getResourceAsStream("/images/iconbig.png"));
            iSmall = ImageIO.read(OwlWindow.class.getResourceAsStream("/images/iconsmall.png"));
        } catch (IOException e) {
            System.out.println("load icons fail");
        }
        if (iBig != null && iSmall != null)
            setIcon(iBig, iSmall);

        titleBar = new TitleBar("Owls. Your own libs");

        VerticalStack mainVStack = new VerticalStack();
        mainVStack.setMargin(0, 30, 0, 0);

        addItems(titleBar, mainVStack);
        titleBar.setIcon(iSmall, 24, 24);
        titleBar.setTextMargin(new Indents(10, 0, 0, 0));

        // functional buttons
        // panel--------------------------------------------------------------------------------------
        HorizontalStack functionalBtnsStack = new HorizontalStack();
        functionalBtnsStack.setPadding(0, 0, 6, 0);
        functionalBtnsStack.setHeightPolicy(SizePolicy.FIXED);
        functionalBtnsStack.setHeight(40);
        functionalBtnsStack.setBackground(new Color(0x404040));

        newFileBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
        newFileBtn.setToolTip("New file");
        newFolderBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
        newFolderBtn.setToolTip("New folder");
        importBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
        importBtn.setToolTip("Import file");
        saveBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
        saveBtn.setToolTip("Save file");
        deleteBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
        deleteBtn.setToolTip("Delete");
        searchBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
        searchBtn.setToolTip("Search");
        editBtn = (ButtonToggle) ElementsFactory.getFunctionalButton(true);
        editBtn.addItemState(ItemStateType.TOGGLED, new ItemState(new Color(0x595959)));
        editBtn.setToolTip("Edit");
        backBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
        backBtn.setToolTip("Back");
        backBtn.setHeightPolicy(SizePolicy.FIXED);
        backBtn.setHeight(30);

        mainVStack.addItems(functionalBtnsStack);
        functionalBtnsStack.addItems(newFileBtn, newFolderBtn, importBtn, saveBtn, deleteBtn, searchBtn, editBtn);

        ElementsFactory.setButtonImage(newFileBtn,
                DefaultsService.getDefaultImage(EmbeddedImage.FILE, EmbeddedImageSize.SIZE_32X32));
        ElementsFactory.setButtonImage(newFolderBtn,
                DefaultsService.getDefaultImage(EmbeddedImage.FOLDER_PLUS, EmbeddedImageSize.SIZE_32X32));
        ElementsFactory.setButtonImage(importBtn,
                DefaultsService.getDefaultImage(EmbeddedImage.IMPORT, EmbeddedImageSize.SIZE_32X32));
        ElementsFactory.setButtonImage(saveBtn,
                DefaultsService.getDefaultImage(EmbeddedImage.DISKETTE, EmbeddedImageSize.SIZE_32X32));
        ElementsFactory.setButtonImage(deleteBtn,
                DefaultsService.getDefaultImage(EmbeddedImage.RECYCLE_BIN, EmbeddedImageSize.SIZE_32X32));
        ElementsFactory.setButtonImage(searchBtn,
                DefaultsService.getDefaultImage(EmbeddedImage.LOUPE, EmbeddedImageSize.SIZE_32X32));
        ElementsFactory.setButtonImage(editBtn,
                DefaultsService.getDefaultImage(EmbeddedImage.PENCIL, EmbeddedImageSize.SIZE_32X32));

        // --------------------------------------------------------------------------------------------------------------

        // Vertical Split
        // Area-------------------------------------------------------------------------------------------
        VerticalSplitArea vSplitArea = new VerticalSplitArea();
        vSplitArea.SetSplitThickness(4);

        mainVStack.addItem(vSplitArea);

        Style vsaStyle = vSplitArea.getCoreStyle();
        Style shStyle = vsaStyle.getInnerStyle("splitholder");
        if (shStyle == null)
            shStyle = new Style();
        shStyle.background = new Color(0x404040);
        vsaStyle.removeInnerStyle("splitholder");
        vsaStyle.addInnerStyle("splitholder", shStyle);
        vSplitArea.setStyle(vsaStyle);

        vSplitArea.setSplitPosition(300);

        // treeStack = new VerticalStack();
        Frame leftArea = new Frame();
        leftArea.setPadding(0, 0, 0, 0);
        filesTree = new TreeView();
        vSplitArea.assignLeftItem(leftArea); // treeStack);
        filesTree.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        filesTree.vScrollBar.setStyle(Style.getSimpleVerticalScrollBarStyle());
        filesTree.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        filesTree.hScrollBar.setStyle(Style.getSimpleHorizontalScrollBarStyle());
        filesTree.setBackground(new Color(0x505050));
        filesTree.disableMenu(true);
        filesTree.menu.setDrawable(false);

        // treeStack.addItems(backBtn, filesTree);

        // filesTree.addItem(ElementsFactory.getTreeItem("branch1",
        // TreeItemType.BRANCH));
        // filesTree.addItem(ElementsFactory.getTreeItem("leaf1", TreeItemType.LEAF));

        VerticalStack codeVStack = new VerticalStack();
        vSplitArea.assignRightItem(codeVStack);
        codeVStack.setSpacing(0, 3);
        codeVStack.setBackground(new Color(0x404040));

        Frame kwFrame = new Frame();
        kwFrame.setHeightPolicy(SizePolicy.FIXED);
        kwFrame.setHeight(30);
        kwFrame.setBackground(new Color(0x505050));
        kwFrame.setPadding(0, 0, 0, 0);

        keyWordsStack = new HorizontalStack();
        keyWordsStack.setHeightPolicy(SizePolicy.FIXED);
        keyWordsStack.setHeight(32);

        // keyWordsStack.setBackground(new Color(0x646464));

        // codeArea = new TextArea();
        workTabArea = new TabView();

        // codeVStack.addItems(kwFrame, codeArea);
        codeVStack.addItems(kwFrame, workTabArea);

        // codeArea.setStyle(ElementsFactory.getTextAreaStyle());
        // codeArea.setEditable(false);
        // codeArea.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        // codeArea.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        // codeArea.disableMenu(true);
        // codeArea.menu.setDrawable(false);

        addKeyWordsBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
        addKeyWordsBtn.setPadding(0, 0, 0, 0);
        keyWordsStack.setMargin(5, 0, addKeyWordsBtn.getWidth() + 3, 0);
        keyWordsStack.setSpacing(5, 0);

        kwFrame.addItems(keyWordsStack, addKeyWordsBtn);
        addKeyWordsBtn.setAlignment(ItemAlignment.RIGHT);

        ElementsFactory.setButtonImage(addKeyWordsBtn, "/images/add.png");

        treeContextMenu = new ContextMenu(this);
        treeContextMenu.setItemName("TreeContextMenu");
        treeContextMenu.activeButton = MouseButton.BUTTON_RIGHT;
        treeContextMenu.setBorderThickness(1);
        treeContextMenu.setBorderFill(32, 32, 32);
        treeContextMenu.setBackground(60, 60, 60);
        treeContextMenu.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
        // treeContextMenu.setBorderRadius(5);
        // treeContextMenu.itemList.setSelectionVisible(false);
        // treeContextMenu.setReturnFocus(filesTree.getArea());

        keyWordsContextMenu = new ContextMenu(this);
        keyWordsContextMenu.setItemName("TreeContextMenu");
        keyWordsContextMenu.activeButton = MouseButton.BUTTON_RIGHT;
        keyWordsContextMenu.setBorderThickness(1);
        keyWordsContextMenu.setBorderFill(32, 32, 32);
        keyWordsContextMenu.setBackground(60, 60, 60);
        keyWordsContextMenu.itemList.setSelectionVisible(false);

        keyWordsContextMenu.setShadow(5, 2, 2, new Color(0, 0, 0, 180));

        miKWDelete = new MenuItem("Remove");
        miKWUse = new MenuItem("Use");

        miTreeDelete = new MenuItem("Delete");
        miRename = new MenuItem("Rename");
        miAddExist = new MenuItem("Add existing");
        miNewFile = new MenuItem("New file");
        miNewFolder = new MenuItem("New folder");

        ElementsFactory.getMenuStyle().setStyle(miTreeDelete, miKWDelete, miKWUse, miRename, miAddExist, miNewFile,
                miNewFolder);

        treeContextMenu.addItems(miTreeDelete, miRename, miAddExist, miNewFile, miNewFolder);
        keyWordsContextMenu.addItems(miKWDelete, miKWUse);

        ElementsFactory.setMenuItemImage(miTreeDelete,
                DefaultsService.getDefaultImage(EmbeddedImage.RECYCLE_BIN, EmbeddedImageSize.SIZE_32X32));
        ElementsFactory.setMenuItemImage(miKWDelete,
                DefaultsService.getDefaultImage(EmbeddedImage.RECYCLE_BIN, EmbeddedImageSize.SIZE_32X32));
        ElementsFactory.setMenuItemImage(miKWUse,
                DefaultsService.getDefaultImage(EmbeddedImage.LOUPE, EmbeddedImageSize.SIZE_32X32));
        ElementsFactory.setMenuItemImage(miRename,
                DefaultsService.getDefaultImage(EmbeddedImage.PENCIL, EmbeddedImageSize.SIZE_32X32));
        ElementsFactory.setMenuItemImage(miAddExist,
                DefaultsService.getDefaultImage(EmbeddedImage.IMPORT, EmbeddedImageSize.SIZE_32X32));
        ElementsFactory.setMenuItemImage(miNewFile,
                DefaultsService.getDefaultImage(EmbeddedImage.FILE, EmbeddedImageSize.SIZE_32X32));
        ElementsFactory.setMenuItemImage(miNewFolder,
                DefaultsService.getDefaultImage(EmbeddedImage.FOLDER_PLUS, EmbeddedImageSize.SIZE_32X32));

        filesTree.eventMouseClick.add((sender, args) -> {
            treeContextMenu.show(sender, args);
        });

        // inputDialog = new InputDialog("title", "action", "text");

        // MessageBox textInputDialog = new DialogItem();
        kwResultsListBox = new ListBox();
        leftArea.addItems(filesTree, kwResultsListBox);
        ButtonCore btn = new ButtonCore();
        btn.setSize(30, 30);
        btn.setBackground(20, 20, 20, 0);

        // backBtn.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);

        kwResultLabel1 = new Label("Search results by keywords:");
        // label1.setForeground(Color.WHITE);
        kwResultLabel1.setHeightPolicy(SizePolicy.FIXED);
        kwResultLabel1.setHeight(20);
        kwResultLabel1.setMargin(5, 0, 0, 0);
        kwResultLabel1.setFontSize(15);
        kwResultLabel1.setFontStyle(Font.BOLD);
        // kwResultLabel2 = new Label("by key words:");
        kwResultLabel2 = new Label();
        // label2.setForeground(Color.WHITE);
        kwResultLabel2.setHeightPolicy(SizePolicy.FIXED);
        kwResultLabel2.setHeight(20);
        kwResultLabel2.setFontStyle(Font.ITALIC);
        kwResultLabel2.setFontSize(15);
        kwResultLabel2.setMargin(15, 0, 0, 0);

        kwResultsListBox.addItems(backBtn, kwResultLabel1, kwResultLabel2);

        ElementsFactory.removeListBoxSelection(backBtn, kwResultsListBox);
        ElementsFactory.removeListBoxSelection(kwResultLabel1, kwResultsListBox);
        ElementsFactory.removeListBoxSelection(kwResultLabel2, kwResultsListBox);

        ElementsFactory.setButtonImage(backBtn,
                DefaultsService.getDefaultImage(EmbeddedImage.ARROW_LEFT, EmbeddedImageSize.SIZE_32X32));
        kwResultsListBox.setVisible(false);
        kwResultsListBox.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        kwResultsListBox.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);

        kwResultsListBox.eventMouseDoubleClick.add((s, a) -> {
            InterfaceBaseItem selected = kwResultsListBox.getSelectedItem();
            if (selected instanceof HorizontalStack)
                ((HorizontalStack) selected).eventMouseDoubleClick.execute(s, a);
        });

        filesTree.getArea().setFocus();

        // addKeyWords.eventMousePress.add((sender, args) -> {
        // filesTree.setVisible(!filesTree.isVisible());
        // kwResultsListBox.setVisible(!kwResultsListBox.isVisible());
        // kwResultsListBox.getArea().updateLayout();
        //// System.out.println(backBtn.getWidth() + " " + backBtn.getHeight() +
        // backBtn.getX() + " " + backBtn.getY());
        //
        //// KeyWordItem bi = ElementsFactory.getKeyWord("123");
        //// keyWordsStack.addItem(bi);
        //// backBtn.setVisible(false);
        //// treeStack.updateLayout();
        //// alertDialog.show(Handler);
        //// ((Prototype)alertDialog.window.getItems().get(1)).addItem(bc);
        //// inputDialog.show(Handler);
        // });

    }

    void setTitle(String title) {
        titleBar.setText(title);
    }
}
