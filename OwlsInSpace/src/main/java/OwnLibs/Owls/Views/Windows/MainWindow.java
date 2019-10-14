package OwnLibs.Owls.Views.Windows;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;

import OwnLibs.Owls.ElementsFactory;
import OwnLibs.Owls.Views.Items.AttachSideArea;
import OwnLibs.Owls.Views.Items.HomeTab;
import OwnLibs.Owls.Views.Items.HomePage;

import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.MenuItem;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MainWindow extends ActiveWindow {
        public TitleBar titleBar;

        public ButtonCore newFileBtn;
        public ButtonCore newFolderBtn;
        public ButtonCore importBtn;
        public ButtonCore saveBtn;
        public ButtonCore deleteBtn;
        public ButtonCore searchBtn;
        public ButtonToggle editBtn;
        public ButtonCore settingsBtn;
        public ButtonCore backBtn;

        public TreeView filesTree;
        public TabView workTabArea;

        public ButtonCore filePreferencesBtn;
        public HorizontalStack keyWordsStack;

        ContextMenu treeContextMenu;
        public MenuItem miTreeDelete;
        public MenuItem miKWDelete;
        public MenuItem miKWUse;
        public MenuItem miRename;
        public MenuItem miAddExist;
        public MenuItem miNewFile;
        public MenuItem miNewFolder;

        public ContextMenu keyWordsContextMenu;
        public ContextMenu filePreferencesContextMenu;
        public MenuItem miAddKeyWords;
        public MenuItem miShowFilePreferences;
        public MenuItem miShowAttachedContent;

        public ListBox kwResultsListBox;
        public Label kwResultLabel1;
        public Label kwResultLabel2;

        public AttachSideArea attachedContentSideArea;
        public HomePage homePage;
        public HomeTab homeTab;

        @Override
        public void initWindow() {
                setParameters("OwlWindow", "OwlWindow", 1300, 800, false);
                setMinSize(400, 400);
                setPadding(2, 2, 2, 2);
                isCentered = true;

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

                addItems(titleBar, mainVStack);
                titleBar.setIcon(iSmall, 24, 24);
                titleBar.setTextMargin(new Indents(10, 0, 0, 0));

                // functional buttons
                // panel--------------------------------------------------------------------------------------
                HorizontalStack functionalBtnsStack = new HorizontalStack();
                functionalBtnsStack.setPadding(0, 0, 6, 0);
                functionalBtnsStack.setHeightPolicy(SizePolicy.FIXED);
                functionalBtnsStack.setHeight(40);
                // functionalBtnsStack.setSpacing(2, 0);
                // functionalBtnsStack.setBackground(new Color(0x404040));
                functionalBtnsStack.setBackground(new Color(55, 55, 55));

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
                // editBtn.addItemState(ItemStateType.TOGGLED, new ItemState(new Color(0x595959)));
                editBtn.addItemState(ItemStateType.TOGGLED, new ItemState(new Color(24, 148, 188)));
                editBtn.setToolTip("Edit");
                settingsBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
                settingsBtn.setToolTip("Settings");
                backBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
                backBtn.setToolTip("Back");
                backBtn.setHeightPolicy(SizePolicy.FIXED);
                backBtn.setHeight(30);

                mainVStack.addItems(functionalBtnsStack);
                functionalBtnsStack.addItems(newFileBtn, newFolderBtn, importBtn, saveBtn, deleteBtn, searchBtn,
                                editBtn, settingsBtn);

                ElementsFactory.setButtonImage(newFileBtn,
                                DefaultsService.getDefaultImage(EmbeddedImage.FILE, EmbeddedImageSize.SIZE_32X32));
                ElementsFactory.setButtonImage(newFolderBtn, DefaultsService.getDefaultImage(EmbeddedImage.FOLDER_PLUS,
                                EmbeddedImageSize.SIZE_32X32));
                ElementsFactory.setButtonImage(importBtn,
                                DefaultsService.getDefaultImage(EmbeddedImage.IMPORT, EmbeddedImageSize.SIZE_32X32));
                ElementsFactory.setButtonImage(saveBtn,
                                DefaultsService.getDefaultImage(EmbeddedImage.DISKETTE, EmbeddedImageSize.SIZE_32X32));
                ElementsFactory.setButtonImage(deleteBtn, DefaultsService.getDefaultImage(EmbeddedImage.RECYCLE_BIN,
                                EmbeddedImageSize.SIZE_32X32));
                ElementsFactory.setButtonImage(searchBtn,
                                DefaultsService.getDefaultImage(EmbeddedImage.LOUPE, EmbeddedImageSize.SIZE_32X32));
                ElementsFactory.setButtonImage(editBtn,
                                DefaultsService.getDefaultImage(EmbeddedImage.PENCIL, EmbeddedImageSize.SIZE_32X32));
                ElementsFactory.setButtonImage(settingsBtn,
                                DefaultsService.getDefaultImage(EmbeddedImage.GEAR, EmbeddedImageSize.SIZE_32X32));

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
                filesTree.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
                // filesTree.vScrollBar.setStyle(Style.getSimpleVerticalScrollBarStyle());
                filesTree.vScrollBar.setStyle(ElementsFactory.getVScrollBarStyle());
                filesTree.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
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
                workTabArea.setContentPolicy(SizePolicy.EXPAND);
                homePage = new HomePage();

                // codeVStack.addItems(kwFrame, codeArea);
                codeVStack.addItems(kwFrame, workTabArea);

                homeTab = new HomeTab();

                workTabArea.addTab(homeTab);
                workTabArea.addItemToTab(homeTab, homePage);
                // workTabArea.updateLayout();

                // codeArea.setStyle(ElementsFactory.getTextAreaStyle());
                // codeArea.setEditable(false);
                // codeArea.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
                // codeArea.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
                // codeArea.disableMenu(true);
                // codeArea.menu.setDrawable(false);

                attachedContentSideArea = new AttachSideArea(this, Side.RIGHT);
                filePreferencesContextMenu = new ContextMenu(this);
                filePreferencesContextMenu.setItemName("FilePreferenceContextMenu");
                filePreferencesContextMenu.activeButton = MouseButton.BUTTON_LEFT;
                filePreferencesContextMenu.setBorderThickness(1);
                filePreferencesContextMenu.setBorderFill(32, 32, 32);
                filePreferencesContextMenu.setBackground(60, 60, 60);
                // filePreferencesContextMenu.setShadow(5, 2, 2, new Color(0, 0, 0, 180));

                miAddKeyWords = new MenuItem("Add tags");
                miShowAttachedContent = new MenuItem("Show attached");
                miShowFilePreferences = new MenuItem("Preferences");

                filePreferencesContextMenu.addItems(miAddKeyWords, miShowAttachedContent, miShowFilePreferences);

                filePreferencesBtn = (ButtonCore) ElementsFactory.getFunctionalButton(false);
                filePreferencesBtn.setPadding(4, 4, 4, 4);
                keyWordsStack.setMargin(5, 0, filePreferencesBtn.getWidth() + 3, 0);
                keyWordsStack.setSpacing(5, 0);

                kwFrame.addItems(keyWordsStack, filePreferencesBtn);
                filePreferencesBtn.setAlignment(ItemAlignment.RIGHT);

                // ElementsFactory.setButtonImage(filePreferenceBtn, "/images/add.png");
                ElementsFactory.setButtonImage(filePreferencesBtn,
                                DefaultsService.getDefaultImage(EmbeddedImage.LINES, EmbeddedImageSize.SIZE_32X32));

                treeContextMenu = new ContextMenu(this);
                treeContextMenu.setItemName("TreeContextMenu");
                treeContextMenu.activeButton = MouseButton.BUTTON_RIGHT;
                treeContextMenu.setBorderThickness(1);
                treeContextMenu.setBorderFill(32, 32, 32);
                treeContextMenu.setBackground(60, 60, 60);
                // treeContextMenu.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
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

                // keyWordsContextMenu.setShadow(5, 2, 2, new Color(0, 0, 0, 180));

                miKWDelete = new MenuItem("Remove");
                miKWUse = new MenuItem("Use");

                miTreeDelete = new MenuItem("Delete");
                miRename = new MenuItem("Rename");
                miAddExist = new MenuItem("Add existing");
                miNewFile = new MenuItem("New file");
                miNewFolder = new MenuItem("New folder");

                ElementsFactory.getMenuStyle().setStyle(miTreeDelete, miKWDelete, miKWUse, miRename, miAddExist,
                                miNewFile, miNewFolder, miAddKeyWords, miShowAttachedContent, miShowFilePreferences);

                treeContextMenu.addItems(miTreeDelete, miRename, miAddExist, miNewFile, miNewFolder);
                keyWordsContextMenu.addItems(miKWDelete, miKWUse);

                ElementsFactory.setMenuItemImage(miTreeDelete, DefaultsService
                                .getDefaultImage(EmbeddedImage.RECYCLE_BIN, EmbeddedImageSize.SIZE_32X32));
                ElementsFactory.setMenuItemImage(miKWDelete, DefaultsService.getDefaultImage(EmbeddedImage.RECYCLE_BIN,
                                EmbeddedImageSize.SIZE_32X32));
                ElementsFactory.setMenuItemImage(miKWUse,
                                DefaultsService.getDefaultImage(EmbeddedImage.LOUPE, EmbeddedImageSize.SIZE_32X32));
                ElementsFactory.setMenuItemImage(miRename,
                                DefaultsService.getDefaultImage(EmbeddedImage.PENCIL, EmbeddedImageSize.SIZE_32X32));
                ElementsFactory.setMenuItemImage(miAddExist,
                                DefaultsService.getDefaultImage(EmbeddedImage.IMPORT, EmbeddedImageSize.SIZE_32X32));
                ElementsFactory.setMenuItemImage(miNewFile,
                                DefaultsService.getDefaultImage(EmbeddedImage.FILE, EmbeddedImageSize.SIZE_32X32));
                ElementsFactory.setMenuItemImage(miNewFolder, DefaultsService.getDefaultImage(EmbeddedImage.FOLDER_PLUS,
                                EmbeddedImageSize.SIZE_32X32));

                BufferedImage addIcon = null;
                BufferedImage attachIcon = null;
                BufferedImage preferencesIcon = null;
                try {
                        addIcon = ImageIO.read(MainWindow.class.getResourceAsStream("/images/add.png"));
                        attachIcon = ImageIO.read(MainWindow.class.getResourceAsStream("/images/attach.png"));
                        preferencesIcon = ImageIO.read(MainWindow.class.getResourceAsStream("/images/preferences.png"));
                } catch (IOException e) {
                        System.out.println("load icons fail");
                }
                ElementsFactory.setMenuItemImage(miAddKeyWords, addIcon);
                ElementsFactory.setMenuItemImage(miShowAttachedContent, attachIcon);
                ElementsFactory.setMenuItemImage(miShowFilePreferences, preferencesIcon);

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

                ElementsFactory.setButtonImage(backBtn, DefaultsService.getDefaultImage(EmbeddedImage.ARROW_LEFT,
                                EmbeddedImageSize.SIZE_32X32));
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

        public void setTitle(String title) {
                titleBar.setText(title);
        }
}
