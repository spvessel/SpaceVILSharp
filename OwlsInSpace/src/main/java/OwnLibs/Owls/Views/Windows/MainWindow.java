package OwnLibs.Owls.Views.Windows;

import OwnLibs.Owls.Views.Items.*;
import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Common.DisplayService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.Scale;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;
import com.spvessel.spacevil.Common.*;

import OwnLibs.Owls.ElementsFactory;

import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.MenuItem;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MainWindow extends ActiveWindow {
    public TitleBar titleBar;

    public ImagedButton newFileBtn;
    public ImagedButton newFolderBtn;
    public ImagedButton importBtn;
    public ImagedButton saveBtn;
    public ImagedButton deleteBtn;
    public ImagedButton searchBtn;
    public ImagedButton refreshBtn;
    public ImagedButton editBtn;
    public ImagedButton settingsBtn;
    public ImagedButton backBtn;
    public ImagedButton filePreferencesBtn;

    public TreeView filesTree;
    public TabView workTabArea;

    public HorizontalStack keyWordsStack;

//    ContextMenu treeContextMenu;
    public ImagedMenuItem miTreeDelete;
    public ImagedMenuItem miKWDelete;
    public ImagedMenuItem miKWUse;
    public ImagedMenuItem miRename;
    public ImagedMenuItem miAddExist;
    public ImagedMenuItem miNewFile;
    public ImagedMenuItem miNewFolder;

    public ContextMenu keyWordsContextMenu;
    public ContextMenu filePreferencesContextMenu;
    public ImagedMenuItem miAddKeyWords;
    public ImagedMenuItem miShowFilePreferences;
    public ImagedMenuItem miShowAttachedContent;

    public ListBox kwResultsListBox;
    public Label kwResultLabel2;

    public AttachSideArea attachedContentSideArea;
    public HomePage homePage;
    public HomeTab homeTab;

    public ContextMenu contextTextActions;
    public ImagedMenuItem copy;
    public ImagedMenuItem paste;
    public ImagedMenuItem cut;
    public ImagedMenuItem goUp;
    public ImagedMenuItem goDown;
    public ImagedMenuItem wrap;

    @Override
    public void initWindow() {

        // right scaling
        int windowWidth = 1300, windowHeight = 800;
        Scale displayScale = DisplayService.getDisplayDpiScale();
        int displayWidth = (int) (DisplayService.getDisplayWidth() * displayScale.getXScale());
        int displayHeight = (int) (DisplayService.getDisplayHeight() * displayScale.getYScale());
        if (windowWidth * displayScale.getXScale() > displayWidth)
            windowWidth = (int) (windowWidth * 0.68);
        if (windowHeight * displayScale.getYScale() > displayHeight)
            windowHeight = (int) (windowHeight * 0.74);

        setParameters("OwlWindow", "OwlWindow", windowWidth, windowHeight, false);
        setMinSize(400, 400);
        setPadding(2, 2, 2, 2);
        isCentered = true;

        // setAntiAliasingQuality(MSAA.MSAA_8X);

        BufferedImage iBig = null;
        BufferedImage iSmall = null;
        try {
            iBig = ImageIO.read(MainWindow.class.getResourceAsStream("/images/iconbig.png"));
            iSmall = ImageIO.read(MainWindow.class.getResourceAsStream("/images/iconsmall.png"));
        } catch (IOException e) {
            System.out.println("load icons fail");
        }
        if (iBig != null && iSmall != null)
            setIcon(iBig, iSmall);

        titleBar = new TitleBar("Owls. Your own libs");

        VerticalStack mainVStack = new VerticalStack();
        mainVStack.setMargin(0, 30, 0, 0);
        mainVStack.setSpacing(0, 1);

        addItems(titleBar, mainVStack);
        titleBar.setIcon(iSmall, 24, 24);
        titleBar.setTextMargin(new Indents(10, 0, 0, 0));

        // functional buttons panel-----------------------------------------------------------------------------
        HorizontalStack functionalBtnsStack = new HorizontalStack();
        functionalBtnsStack.setPadding(0, 0, 6, 0);
        functionalBtnsStack.setHeightPolicy(SizePolicy.FIXED);
        functionalBtnsStack.setHeight(40);
        // functionalBtnsStack.setSpacing(2, 0);
        // functionalBtnsStack.setBackground(new Color(0x404040));
        functionalBtnsStack.setBackground(new Color(55, 55, 55));

        newFileBtn = new ImagedButton(DefaultsService.getDefaultImage(EmbeddedImage.FILE, EmbeddedImageSize.SIZE_32X32), "New file");
//                newFileBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
//                newFileBtn.setToolTip("New file");
//                ElementsFactory.setButtonImage(newFileBtn,
//                                DefaultsService.getDefaultImage(EmbeddedImage.FILE, EmbeddedImageSize.SIZE_32X32));

        newFolderBtn = new ImagedButton(DefaultsService.getDefaultImage(EmbeddedImage.FOLDER_PLUS,
                EmbeddedImageSize.SIZE_32X32), "New folder");
//                newFolderBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
//                newFolderBtn.setToolTip("New folder");
//                ElementsFactory.setButtonImage(newFolderBtn, DefaultsService.getDefaultImage(EmbeddedImage.FOLDER_PLUS,
//                                EmbeddedImageSize.SIZE_32X32));

        importBtn = new ImagedButton(DefaultsService.getDefaultImage(EmbeddedImage.IMPORT, EmbeddedImageSize.SIZE_32X32), "Import file");
//                importBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
//                importBtn.setToolTip("Import file");
//                ElementsFactory.setButtonImage(importBtn,
//                                DefaultsService.getDefaultImage(EmbeddedImage.IMPORT, EmbeddedImageSize.SIZE_32X32));

        saveBtn = new ImagedButton(DefaultsService.getDefaultImage(EmbeddedImage.DISKETTE, EmbeddedImageSize.SIZE_32X32), "Save file");
//                saveBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
//                saveBtn.setToolTip("Save file");
//                ElementsFactory.setButtonImage(saveBtn,
//                                DefaultsService.getDefaultImage(EmbeddedImage.DISKETTE, EmbeddedImageSize.SIZE_32X32));

        deleteBtn = new ImagedButton(DefaultsService.getDefaultImage(EmbeddedImage.RECYCLE_BIN, EmbeddedImageSize.SIZE_32X32), "Delete");
//                deleteBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
//                deleteBtn.setToolTip("Delete");
//                ElementsFactory.setButtonImage(deleteBtn, DefaultsService.getDefaultImage(EmbeddedImage.RECYCLE_BIN,
//                                EmbeddedImageSize.SIZE_32X32));

        searchBtn = new ImagedButton(DefaultsService.getDefaultImage(EmbeddedImage.LOUPE, EmbeddedImageSize.SIZE_32X32), "Search");
//                searchBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
//                searchBtn.setToolTip("Search");
//                ElementsFactory.setButtonImage(searchBtn,
//                                DefaultsService.getDefaultImage(EmbeddedImage.LOUPE, EmbeddedImageSize.SIZE_32X32));

        refreshBtn = new ImagedButton(DefaultsService.getDefaultImage(EmbeddedImage.REFRESH, EmbeddedImageSize.SIZE_32X32), "Refresh");
//                refreshBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
//                refreshBtn.setToolTip("Refresh");
//                ElementsFactory.setButtonImage(refreshBtn,
//                                DefaultsService.getDefaultImage(EmbeddedImage.REFRESH, EmbeddedImageSize.SIZE_32X32));

        editBtn = new ImagedButton(DefaultsService.getDefaultImage(EmbeddedImage.PENCIL, EmbeddedImageSize.SIZE_32X32), true, "Edit");
//                editBtn = (ButtonToggle) ElementsFactory.getFunctionalButton(true);
        editBtn.addItemState(ItemStateType.TOGGLED, new ItemState(new Color(0x595959)));
//                editBtn.setToolTip("Edit");
//                ElementsFactory.setButtonImage(editBtn,
//                                DefaultsService.getDefaultImage(EmbeddedImage.PENCIL, EmbeddedImageSize.SIZE_32X32));

        settingsBtn = new ImagedButton(DefaultsService.getDefaultImage(EmbeddedImage.GEAR, EmbeddedImageSize.SIZE_32X32), "Settings");
//                settingsBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
//                settingsBtn.setToolTip("Settings");
//                ElementsFactory.setButtonImage(settingsBtn,
//                                DefaultsService.getDefaultImage(EmbeddedImage.GEAR, EmbeddedImageSize.SIZE_32X32));

        backBtn = new ImagedButton(DefaultsService.getDefaultImage(EmbeddedImage.ARROW_LEFT, EmbeddedImageSize.SIZE_32X32), "Back");
//                backBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
//                backBtn.setToolTip("Back");
        backBtn.setHeightPolicy(SizePolicy.FIXED);
        backBtn.setHeight(30);
//                ElementsFactory.setButtonImage(backBtn, DefaultsService.getDefaultImage(EmbeddedImage.ARROW_LEFT,
//                        EmbeddedImageSize.SIZE_32X32));

        mainVStack.addItems(functionalBtnsStack);
        functionalBtnsStack.addItems(newFileBtn, newFolderBtn, importBtn, ElementsFactory.getVerticalDivider(),
                saveBtn, deleteBtn, ElementsFactory.getVerticalDivider(), searchBtn, refreshBtn,
                ElementsFactory.getVerticalDivider(), editBtn, settingsBtn);
        functionalBtnsStack.setShadow(1, 0, 1, Color.BLACK);

        // -------------------------------------------------------------------------------------------------------------

        // Vertical Split
        // Area-------------------------------------------------------------------------------------------
        VerticalSplitArea vSplitArea = new VerticalSplitArea();
        vSplitArea.SetSplitThickness(4);

        mainVStack.addItem(vSplitArea);

        Style vsaStyle = vSplitArea.getCoreStyle();
        Style shStyle = vsaStyle.getInnerStyle("splitholder");
        if (shStyle == null)
            shStyle = new Style();
        // shStyle.background = new Color(0x404040);
        shStyle.background = new Color(50, 50, 50);
        vsaStyle.removeInnerStyle("splitholder");
        vsaStyle.addInnerStyle("splitholder", shStyle);
        vSplitArea.setStyle(vsaStyle);

        vSplitArea.setSplitPosition(300);

        // treeStack = new VerticalStack();
        Frame leftArea = new Frame();
        leftArea.setPadding(0, 0, 0, 0);
        filesTree = new TreeView();
        vSplitArea.assignLeftItem(leftArea); // treeStack);
        filesTree.setVScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
        // filesTree.vScrollBar.setStyle(Style.getSimpleVerticalScrollBarStyle());
        filesTree.vScrollBar.setStyle(ElementsFactory.getVScrollBarStyle());
        filesTree.setHScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
        filesTree.hScrollBar.setStyle(ElementsFactory.getHScrollBarStyle());
        filesTree.disableMenu(true);
        filesTree.menu.setDrawable(false);
        filesTree.setBackground(65, 65, 65);

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
        workTabArea.setTabPolicy(SizePolicy.EXPAND);
        homePage = new HomePage();

        // codeVStack.addItems(kwFrame, codeArea);
        codeVStack.addItems(kwFrame, workTabArea);

        homeTab = new HomeTab();

        workTabArea.addTab(homeTab);
        workTabArea.addItemToTab(homeTab, homePage);
        // workTabArea.updateLayout();

        // codeArea.setStyle(ElementsFactory.getTextAreaStyle());
        // codeArea.setEditable(false);
        // codeArea.setVScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
        // codeArea.setHScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
        // codeArea.disableMenu(true);
        // codeArea.menu.setDrawable(false);

        attachedContentSideArea = new AttachSideArea(this, Side.RIGHT);
        filePreferencesContextMenu = new ContextMenu(this);
        filePreferencesContextMenu.setItemName("FilePreferenceContextMenu");
        filePreferencesContextMenu.activeButton = MouseButton.BUTTON_LEFT;
        filePreferencesContextMenu.setBorderThickness(1);
        filePreferencesContextMenu.setBorderFill(32, 32, 32);
        filePreferencesContextMenu.setBackground(60, 60, 60);
        filePreferencesContextMenu.setShadow(5, 0, 0, new Color(0, 0, 0, 210));



        miAddKeyWords = new ImagedMenuItem("Add tags", "/images/add.png");
        miShowAttachedContent = new ImagedMenuItem("Show attached", "/images/attach.png");
        miShowFilePreferences = new ImagedMenuItem("Preferences", "/images/preferences.png");

//        miAddKeyWords = new MenuItem("Add tags");
//        miShowAttachedContent = new MenuItem("Show attached");
//        miShowFilePreferences = new MenuItem("Preferences");
//
//        BufferedImage addIcon = null;
//        BufferedImage attachIcon = null;
//        BufferedImage preferencesIcon = null;
//        try {
//            addIcon = ImageIO.read(MainWindow.class.getResourceAsStream("/images/add.png"));
//            attachIcon = ImageIO.read(MainWindow.class.getResourceAsStream("/images/attach.png"));
//            preferencesIcon = ImageIO.read(MainWindow.class.getResourceAsStream("/images/preferences.png"));
//        } catch (IOException e) {
//            System.out.println("load icons fail");
//        }
//        ElementsFactory.setMenuItemImage(miAddKeyWords, addIcon);
//        ElementsFactory.setMenuItemImage(miShowAttachedContent, attachIcon);
//        ElementsFactory.setMenuItemImage(miShowFilePreferences, preferencesIcon);



        filePreferencesContextMenu.addItems(miAddKeyWords, miShowAttachedContent, miShowFilePreferences);

        filePreferencesBtn = new ImagedButton(DefaultsService.getDefaultImage(EmbeddedImage.LINES, EmbeddedImageSize.SIZE_32X32), "");
//                filePreferencesBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
        filePreferencesBtn.setPadding(4, 4, 4, 4);
//                // ElementsFactory.setButtonImage(filePreferenceBtn, "/images/add.png");
//                ElementsFactory.setButtonImage(filePreferencesBtn,
//                                DefaultsService.getDefaultImage(EmbeddedImage.LINES, EmbeddedImageSize.SIZE_32X32));

        keyWordsStack.setMargin(5, 0, filePreferencesBtn.getWidth() + 3, 0);
        keyWordsStack.setSpacing(5, 0);

        kwFrame.addItems(keyWordsStack, filePreferencesBtn);
        filePreferencesBtn.setAlignment(ItemAlignment.RIGHT);


        ContextMenu treeContextMenu = new ContextMenu(this);
        treeContextMenu.setItemName("TreeContextMenu");
        treeContextMenu.activeButton = MouseButton.BUTTON_RIGHT;
        treeContextMenu.setBorderThickness(1);
        treeContextMenu.setBorderFill(32, 32, 32);
        treeContextMenu.setBackground(60, 60, 60);
        treeContextMenu.setShadow(5, 0, 0, new Color(0, 0, 0, 210));
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

        keyWordsContextMenu.setShadow(5, 0, 0, new Color(0, 0, 0, 210));

        miKWDelete = new ImagedMenuItem("Remove", DefaultsService.getDefaultImage(EmbeddedImage.RECYCLE_BIN,
                EmbeddedImageSize.SIZE_32X32));
//        miKWDelete = new MenuItem("Remove");
//        ElementsFactory.setMenuItemImage(miKWDelete, DefaultsService.getDefaultImage(EmbeddedImage.RECYCLE_BIN,
//                EmbeddedImageSize.SIZE_32X32));

        miKWUse = new ImagedMenuItem("Use", DefaultsService.getDefaultImage(EmbeddedImage.LOUPE,
                EmbeddedImageSize.SIZE_32X32));
//        miKWUse = new MenuItem("Use");
//        ElementsFactory.setMenuItemImage(miKWUse,
//                DefaultsService.getDefaultImage(EmbeddedImage.LOUPE, EmbeddedImageSize.SIZE_32X32));


        miTreeDelete = new ImagedMenuItem("Delete", DefaultsService
                .getDefaultImage(EmbeddedImage.RECYCLE_BIN, EmbeddedImageSize.SIZE_32X32));
//        miTreeDelete = new MenuItem("Delete");
//        ElementsFactory.setMenuItemImage(miTreeDelete, DefaultsService
//                .getDefaultImage(EmbeddedImage.RECYCLE_BIN, EmbeddedImageSize.SIZE_32X32));

        miRename = new ImagedMenuItem("Rename", DefaultsService.getDefaultImage(EmbeddedImage.PENCIL,
                EmbeddedImageSize.SIZE_32X32));
//        miRename = new MenuItem("Rename");
//        ElementsFactory.setMenuItemImage(miRename,
//                DefaultsService.getDefaultImage(EmbeddedImage.PENCIL, EmbeddedImageSize.SIZE_32X32));

        miAddExist = new ImagedMenuItem("Add existing", DefaultsService.getDefaultImage(EmbeddedImage.IMPORT,
                EmbeddedImageSize.SIZE_32X32));
//        miAddExist = new MenuItem("Add existing");
//        ElementsFactory.setMenuItemImage(miAddExist,
//                DefaultsService.getDefaultImage(EmbeddedImage.IMPORT, EmbeddedImageSize.SIZE_32X32));

        miNewFile = new ImagedMenuItem("New file", DefaultsService.getDefaultImage(EmbeddedImage.FILE,
                EmbeddedImageSize.SIZE_32X32));
//        miNewFile = new MenuItem("New file");
//        ElementsFactory.setMenuItemImage(miNewFile,
//                DefaultsService.getDefaultImage(EmbeddedImage.FILE, EmbeddedImageSize.SIZE_32X32));

        miNewFolder = new ImagedMenuItem("New folder", DefaultsService.getDefaultImage(EmbeddedImage.FOLDER_PLUS,
                EmbeddedImageSize.SIZE_32X32));
//        miNewFolder = new MenuItem("New folder");
//        ElementsFactory.setMenuItemImage(miNewFolder, DefaultsService.getDefaultImage(EmbeddedImage.FOLDER_PLUS,
//                EmbeddedImageSize.SIZE_32X32));


//        ElementsFactory.getMenuStyle().setStyle(miTreeDelete, miKWDelete, miKWUse, miRename, miAddExist,
//                miNewFile, miNewFolder, miAddKeyWords, miShowAttachedContent, miShowFilePreferences);

        treeContextMenu.addItems(miTreeDelete, miRename, miAddExist, miNewFile, miNewFolder);
        keyWordsContextMenu.addItems(miKWDelete, miKWUse);




//        filesTree.eventMouseClick.add((sender, args) -> {
//            treeContextMenu.show(sender, args);
//        });
        filesTree.eventMouseClick.add(treeContextMenu::show);

        // inputDialog = new InputDialog("title", "action", "text");

        // MessageBox textInputDialog = new DialogItem();
        kwResultsListBox = new ListBox();
        leftArea.addItems(filesTree, kwResultsListBox);
//        ButtonCore btn = new ButtonCore();
//        btn.setSize(30, 30);
//        btn.setBackground(20, 20, 20, 0);

        // backBtn.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);

        Label kwResultLabel1 = new Label("Search results by keywords:");
        // label1.setForeground(Color.WHITE);
        kwResultLabel1.setHeightPolicy(SizePolicy.FIXED);
        kwResultLabel1.setHeight(20);
        kwResultLabel1.setFontSize(15);
        kwResultLabel1.setFontStyle(Font.BOLD);
        kwResultLabel1.setMargin(5, 0, 0, 0);

        // kwResultLabel2 = new Label("by key words:");
        kwResultLabel2 = new Label();
        kwResultLabel2.setHeightPolicy(SizePolicy.FIXED);
        kwResultLabel2.setHeight(20);
        kwResultLabel2.setFontSize(15);
        kwResultLabel2.setFontStyle(Font.ITALIC);
        kwResultLabel2.setMargin(15, 0, 0, 0);

        kwResultsListBox.addItems(backBtn, kwResultLabel1, kwResultLabel2);

        ElementsFactory.removeListBoxSelection(backBtn, kwResultsListBox);
        ElementsFactory.removeListBoxSelection(kwResultLabel1, kwResultsListBox);
        ElementsFactory.removeListBoxSelection(kwResultLabel2, kwResultsListBox);


        kwResultsListBox.setVisible(false);
        kwResultsListBox.setHScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
        kwResultsListBox.setVScrollBarPolicy(VisibilityPolicy.AS_NEEDED);

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

        copy = new ImagedMenuItem("Copy", "/images/copy.png", new Color(12, 180, 105));
        paste = new ImagedMenuItem("Paste", "/images/paste.png", new Color(173, 139, 186));
        cut = new ImagedMenuItem("Cut", "/images/cut.png", new Color(200, 116, 116));
        goUp = new ImagedMenuItem("Go up", "/images/goup.png", new Color(10, 168, 232));
        goDown = new ImagedMenuItem("Go down", "/images/godown.png", new Color(10, 168, 232));
        wrap = new ImagedMenuItem("Unwrap text", "/images/wrap.png", new Color(150, 150, 150));

//        copy = new MenuItem("Copy");
//        paste = new MenuItem("Paste");
//        cut = new MenuItem("Cut");
//        goUp = new MenuItem("Go up");
//        goDown = new MenuItem("Go down");
//        wrap = new MenuItem("Unwrap text");
//
//        BufferedImage copyIcon = null;
//        BufferedImage pasteIcon = null;
//        BufferedImage cutIcon = null;
//        BufferedImage goupIcon = null;
//        BufferedImage godownIcon = null;
//        BufferedImage wrapIcon = null;
//        try {
//            copyIcon = ImageIO.read(MainWindow.class.getResourceAsStream("/images/copy.png"));
//            pasteIcon = ImageIO.read(MainWindow.class.getResourceAsStream("/images/paste.png"));
//            cutIcon = ImageIO.read(MainWindow.class.getResourceAsStream("/images/cut.png"));
//            goupIcon = ImageIO.read(MainWindow.class.getResourceAsStream("/images/goup.png"));
//            godownIcon = ImageIO.read(MainWindow.class.getResourceAsStream("/images/godown.png"));
//            wrapIcon = ImageIO.read(MainWindow.class.getResourceAsStream("/images/wrap.png"));
//        } catch (IOException e) {
//            System.out.println("load icons fail");
//        }
//        ElementsFactory.setMenuItemImage(copy, copyIcon, new Color(12, 180, 105));
//        ElementsFactory.setMenuItemImage(paste, pasteIcon, new Color(173, 139, 186));
//        ElementsFactory.setMenuItemImage(cut, cutIcon, new Color(200, 116, 116));
//        ElementsFactory.setMenuItemImage(goUp, goupIcon, new Color(10, 168, 232));
//        ElementsFactory.setMenuItemImage(goDown, godownIcon, new Color(10, 168, 232));
//        ElementsFactory.setMenuItemImage(wrap, wrapIcon, new Color(150, 150, 150));



//        ElementsFactory.getMenuStyle().setStyle(copy, paste, cut, wrap, goUp, goDown);
        contextTextActions = new ContextMenu(this, copy, paste, cut, wrap, goUp, goDown);
        contextTextActions.setBorderThickness(1);
        contextTextActions.setBorderFill(32, 32, 32);
        contextTextActions.setBackground(60, 60, 60);
        contextTextActions.setShadow(5, 0, 0, new Color(0, 0, 0, 210));

    }

    public void setTitle(String title) {
        titleBar.setText(title);
    }
}
