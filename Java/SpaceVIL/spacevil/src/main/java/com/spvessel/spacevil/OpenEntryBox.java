package com.spvessel.spacevil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.FileSystemEntryType;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.OpenDialogType;
import com.spvessel.spacevil.Flags.VisibilityPolicy;

public class OpenEntryBox extends DialogWindow {

    private String _result = null;

    /**
     * Getting full path of selected fyle system entry.
     * 
     * @return Full path of selected fyle system entry.
     */
    public String getResult() {
        return _result;
    }

    private TitleBar _titleBar;
    private VerticalStack _layout;
    private HorizontalStack _toolbar;
    private ButtonCore _btnBackward;
    private ButtonCore _btnHome;
    private ButtonCore _btnUser;
    private ButtonCore _btnCreate;
    private ButtonCore _btnRename;
    private ButtonCore _btnRefresh;
    private ButtonCore _btnFilter;
    private ButtonCore _btnUpward;
    private Label _filterText;
    private ButtonToggle _btnShowHidden;
    private TextEdit _addressLine;
    private ListBox _fileList;
    private TextEdit _fileName;
    private Frame _controlPanel;
    private ButtonCore _btnOpen;
    private ButtonCore _btnCancel;
    private ContextMenu _filterList;
    private ContextMenu _addNewList;

    private Deque<String> _filesTrack = new ArrayDeque<>();
    private BufferedImage _folder = null;
    private BufferedImage _file = null;

    /**
     * Setting the default path that will be opened when OpenEntryBox is shown.
     * 
     * @param path Default path to open.
     */
    public void setDefaultPath(String path) {
        _addressLine.setText(path);
    }

    /**
     * Setting a title text of the dialog window.
     * 
     * @param title Title text.
     */
    public void setTitle(String title) {
        _titleBar.setText(title);
    }

    /**
     * Getting a title text of the dialog window.
     * 
     * @return Title text.
     */
    public String getTitle() {
        return _titleBar.getText();
    }

    private FileSystemEntryType _entryType = FileSystemEntryType.FILE;
    private OpenDialogType _dialogType = OpenDialogType.OPEN;

    /**
     * Constructs OpenEntryBox with title text, entry type and dialog type.
     * <p>
     * Entry type can be FileSystemEntryType.FILE or FileSystemEntryType.DIRECTORY.
     * <p>
     * Dialog type can be OpenDialogType.OPEN or OpenDialogType.SAVE.
     * 
     * @param title      Title text.
     * @param entryType  Entry type as
     *                   com.spvessel.spacevil.Flags.FileSystemEntryType.
     * @param dialogType Dialog type as com.spvessel.spacevil.Flags.OpenDialogType.
     */
    public OpenEntryBox(String title, FileSystemEntryType entryType, OpenDialogType dialogType) {
        setWindowName("OpenEntryBox:" + title);

        _entryType = entryType;
        _dialogType = dialogType;
        _filesTrack.add(System.getProperty("user.home"));

        _titleBar = new TitleBar(title);
        _layout = new VerticalStack();
        _toolbar = new HorizontalStack();
        _btnBackward = new ButtonCore();
        _btnHome = new ButtonCore();
        _btnUser = new ButtonCore();
        _btnCreate = new ButtonCore();
        _btnRename = new ButtonCore();
        _btnRefresh = new ButtonCore();
        _btnFilter = new ButtonCore();
        _btnUpward = new ButtonCore();
        _filterText = new Label();
        _btnShowHidden = new ButtonToggle();
        _addressLine = new TextEdit(System.getProperty("user.home"));
        _fileList = new ListBox();
        _fileName = new TextEdit();
        _controlPanel = new Frame();

        if (dialogType == OpenDialogType.SAVE)
            _btnOpen = new ButtonCore("Save");
        else
            _btnOpen = new ButtonCore("Open");

        _btnCancel = new ButtonCore("Cancel");

        setStyle(DefaultsService.getDefaultStyle(OpenEntryDialog.class));
    }

    /**
     * Constructs OpenEntryBox with title text, entry type. Dialog type is
     * OpenDialogType.OPEN.
     * <p>
     * Entry type can be FileSystemEntryType.FILE or FileSystemEntryType.DIRECTORY.
     * 
     * @param title     Title text.
     * @param entryType Entry type as
     *                  com.spvessel.spacevil.Flags.FileSystemEntryType.
     */
    public OpenEntryBox(String title, FileSystemEntryType entryType) {
        this(title, entryType, OpenDialogType.OPEN);
    }

    /**
     * Constructs OpenEntryBox with title text. Entry type is
     * FileSystemEntryType.FILE. Dialog type is OpenDialogType.OPEN.
     * 
     * @param title Title text.
     */
    public OpenEntryBox(String title) {
        this(title, FileSystemEntryType.FILE);
    }

    /**
     * Initializing all elements in the OpenEntryBox.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initWindow() {
        isBorderHidden = true;
        isAlwaysOnTop = true;

        _titleBar.getMaximizeButton().setVisible(false);

        // adding toolbar
        addItems(_titleBar);

        _titleBar.getCloseButton().eventMouseClick.clear();
        _titleBar.getCloseButton().eventMouseClick.add((sender, args) -> {
            close();
        });

        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.ESCAPE)
                close();
        });

        if (_entryType == FileSystemEntryType.DIRECTORY) {
            _fileName.setVisible(false);
            _btnFilter.setVisible(false);
            _filterText.setVisible(false);
        } else {
            if (_dialogType == OpenDialogType.SAVE)
                _fileName.setText("new_file");
            _filterList = new ContextMenu(this);
            _filterList.activeButton = MouseButton.BUTTON_LEFT;
            addFilterExtensions("All files (*.*);*.*");
        }
        _addNewList = new ContextMenu(this, getEntryMenuItem("Directory"), getEntryMenuItem("File"));
        _addNewList.activeButton = MouseButton.BUTTON_LEFT;

        _folder = DefaultsService.getDefaultImage(EmbeddedImage.FOLDER, EmbeddedImageSize.SIZE_32X32);
        _file = DefaultsService.getDefaultImage(EmbeddedImage.FILE, EmbeddedImageSize.SIZE_32X32);
        ImageItem backward = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.ARROW_LEFT, EmbeddedImageSize.SIZE_32X32), false);
        ImageItem upward = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.ARROW_UP, EmbeddedImageSize.SIZE_32X32), false);
        ImageItem home = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.HOME, EmbeddedImageSize.SIZE_32X32), false);
        ImageItem user = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.USER, EmbeddedImageSize.SIZE_32X32), false);
        ImageItem create = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.FOLDER_PLUS, EmbeddedImageSize.SIZE_32X32), false);
        ImageItem rename = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.PENCIL, EmbeddedImageSize.SIZE_32X32), false);
        ImageItem refresh = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.REFRESH, EmbeddedImageSize.SIZE_32X32), false);
        ImageItem hidden = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.EYE, EmbeddedImageSize.SIZE_32X32), false);
        ImageItem filter = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.FILTER, EmbeddedImageSize.SIZE_32X32), false);

        Style.getFrameStyle().setStyle(backward, upward, create, rename, refresh, hidden, user, home, filter);
        addItems(_layout);
        _layout.addItems(_toolbar, _addressLine, _fileList, _fileName, _controlPanel);
        _toolbar.addItems(_btnBackward, _btnUpward, getDivider(), _btnHome, _btnUser, getDivider(), _btnCreate,
                _btnRename, getDivider(), _btnRefresh, getDivider(), _btnShowHidden, getDivider(), _btnFilter,
                _filterText);
        _btnBackward.addItem(backward);
        _btnUpward.addItem(upward);
        _btnHome.addItem(home);
        _btnUser.addItem(user);
        _btnCreate.addItem(create);
        _btnRename.addItem(rename);
        _btnRefresh.addItem(refresh);
        _btnShowHidden.addItem(hidden);
        _btnFilter.addItem(filter);
        _controlPanel.addItems(_btnOpen, _btnCancel);

        _fileList.setHScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
        _fileList.setVScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
        _btnBackward.eventMouseClick.add((sender, args) -> pathBackward());
        _btnUpward.eventMouseClick.add((sender, args) -> pathUpward());
        _btnHome.eventMouseClick.add((sender, args) -> {
            _addressLine.setText(System.getProperty("user.dir"));
            refreshFolder();
        });
        _btnUser.eventMouseClick.add((sender, args) -> {
            _addressLine.setText(System.getProperty("user.home"));
            refreshFolder();
        });
        _btnCreate.eventMouseClick.add((sender, args) -> {
            _addNewList.show(sender, args);
        });
        _btnRename.eventMouseClick.add((sender, args) -> {
            FileSystemEntry selected = ((FileSystemEntry) _fileList.getSelectedItem());
            InputDialog input = new InputDialog("Rename:", "OK", selected.getText());
            input.selectAll();
            input.onCloseDialog.add(() -> {
                String result = input.getResult();
                if (result == null)
                    return;
                File file = new File(_addressLine.getText() + File.separator
                        + ((FileSystemEntry) _fileList.getSelectedItem()).getText());
                File newFile = new File(_addressLine.getText() + File.separator + result);
                if (file.renameTo(newFile)) {
                    selected.setText(result);
                } else {
                    PopUpMessage popError = new PopUpMessage(
                            (selected.getEntryType() == FileSystemEntryType.DIRECTORY) ? "Can not rename directory."
                                    : "Can not rename file.");
                    popError.show(this);
                }
            });
            input.show(this);
        });
        _btnRefresh.eventMouseClick.add((sender, args) -> refreshFolder());
        _btnFilter.eventMouseClick.add((sender, args) -> {
            args.position.setPosition(_filterText.getX(), _filterText.getY() + _filterText.getHeight());
            _filterList.show(sender, args);
        });
        _btnShowHidden.eventToggle.add((sender, args) -> refreshFolder());
        _btnCancel.eventMouseClick.add((sender, args) -> {
            _result = null;
            close();
        });
        _btnOpen.eventMouseClick.add((sender, args) -> {
            open();
        });
        _addressLine.eventKeyRelease.add((sender, args) -> pathEditFinished(args));
        _fileList.getArea().eventKeyRelease.add((sender, args) -> {
            if (args.key == KeyCode.BACKSPACE) {
                pathBackward();
            }
        });
        if (_filterList != null)
            fillFilterList();
        refreshFolder();
    }

    private void showFolder(String path) {
        clearListBox();

        File fileFolder = new File(path);
        File[] files = fileFolder.listFiles();
        if (_fileList == null || files == null)
            return;

        // Maybe need some sorting
        for (File f : files) {
            if (!_btnShowHidden.isToggled() && f.isHidden())
                continue;
            FileSystemEntry fi;
            if (f.isDirectory()) {
                fi = new FileSystemEntry(FileSystemEntryType.DIRECTORY, f.getName());
                fi.setIcon(_folder, 16, 16);
                _fileList.addItem(fi);
                fi.eventMouseDoubleClick.add((sender, args) -> {
                    setAddressLine(fi);
                });
                fi.eventKeyRelease.add((sender, args) -> {
                    if (args.key == KeyCode.ENTER) {
                        setAddressLine(fi);
                    }
                });
            }
        }

        if (_entryType == FileSystemEntryType.FILE) {
            for (File f : files) {
                if (!_btnShowHidden.isToggled() && f.isHidden())
                    continue;
                FileSystemEntry fi;
                if (!f.isDirectory()) {
                    if (checkExtensionFilter(f))
                        continue;

                    fi = new FileSystemEntry(FileSystemEntryType.FILE, f.getName());
                    fi.setIcon(_file, 16, 16);
                    fi.setText(f.getName());
                    _fileList.addItem(fi);
                    fi.eventMouseClick.add((sender, args) -> {
                        setNameLine(fi);
                    });
                    fi.eventMouseDoubleClick.add((sender, args) -> {
                        open();
                    });
                    fi.eventKeyRelease.add((sender, args) -> {
                        if (args.key == KeyCode.ENTER)
                            open();
                    });
                }
            }
        }
    }

    private void pathEditFinished(KeyArgs args) {
        if (args.key == KeyCode.ENTER || args.key == KeyCode.NUMPADENTER) {
            String text = _addressLine.getText();
            if (text == null || text.equals("")) {
                text = "./";
                _addressLine.setText(text);
            }
            _filesTrack.push(text);
            refreshFolder();
        }
    }

    private void pathBackward() {
        if (_filesTrack.size() > 1) {
            _filesTrack.pop();
            _addressLine.setText(_filesTrack.getFirst());
            refreshFolder();
        }
    }

    private void pathUpward() {
        String name = _addressLine.getText();
        int ind = name.lastIndexOf(File.separator);
        int firstInd = name.indexOf(File.separator);
        if (name.endsWith(File.separator)) {
            if (ind <= 1 || name.substring(ind - 1, ind).equals(":"))
                return;
            name = name.substring(0, name.length() - 1);
            System.out.println(name);
            ind = name.lastIndexOf(File.separator);
        }

        if (ind == firstInd) {
            if (ind == -1) { // No such index
                if (name.contains("./")) {
                    ind = name.indexOf("/") + 1;
                } else {
                    return;
                }
            } else if (name.substring(ind - 1, ind).equals(":")) {
                ind = ind + 1;
            }
        }

        name = name.substring(0, ind);
        _addressLine.setText(name);
        _filesTrack.push(name);
        refreshFolder();
    }

    private void setAddressLine(FileSystemEntry fse) {
        String name = _addressLine.getText();
        if (name.endsWith(File.separator) || name.endsWith("/"))
            _addressLine.setText(name + fse.getText());
        else
            _addressLine.setText(name + File.separator + fse.getText());

        _filesTrack.push(_addressLine.getText());
        refreshFolder();
    }

    private void setNameLine(FileSystemEntry fse) {
        if (_fileName.getText().equals(fse.getText()))
            return;
        _fileName.setText(fse.getText());
    }

    /**
     * Refresh opened folder.
     */
    public final void refreshFolder() {
        String path = _addressLine.getText(); // need some check
        showFolder(path);
        _fileList.getArea().setFocus();
    }

    private void clearListBox() {
        _fileList.getArea().clear();
    }

    private Map<String, String[]> _extensionFilter = new LinkedHashMap<>();

    /**
     * Adding file filter extensions.
     * <p>
     * Rule: "filter name (*.ext1, *.ext2, *.extN) ; *.ext1, *.ext2, *.extN&quot;
     * <p>
     * Example 1: "Text files (*.txt) ; *.txt&quot;
     * <p>
     * Example 2: "Images (*.png, *.bmp, *.jpg) ; *.png, *.bmp, *.jpg&quot;
     * 
     * @param exts File filter extensions.
     */
    public void addFilterExtensions(String... exts) {
        for (int i = 0; i < exts.length; i++) {
            String[] line = exts[i].split(";");
            String key = line[0];
            String regex = line[1].toLowerCase().replaceAll("[!*]", "").replaceAll("\\s", "");
            if (_extensionFilter.containsKey(key))
                _extensionFilter.replace(key, regex.split(","));
            else
                _extensionFilter.put(key, regex.split(","));
        }
    }

    private boolean checkExtensionFilter(File f) {
        if (_extensionFilter.size() == 0)
            return false;
        String name = f.getName().toLowerCase();
        for (String item : _extensionFilter.get(_filterText.getText())) {
            if (name.endsWith(item) || item.equals("."))
                return false;
        }
        return true;
    }

    private void updateFilterText() {
        _filterText
                .setWidth(_filterText.getTextWidth() + _filterText.getPadding().left + _filterText.getPadding().right);
    }

    private void fillFilterList() {
        _filterList.clear();
        for (Map.Entry<String, String[]> entry : _extensionFilter.entrySet()) {
            _filterList.addItem(getFilterMenuItem(entry.getKey()));
        }
        _filterText.setText(_extensionFilter.entrySet().iterator().next().getKey());
        updateFilterText();
    }

    private MenuItem getFilterMenuItem(String name) {
        MenuItem drive = new MenuItem(name);
        drive.eventMouseClick.add((sender, args) -> {
            _filterText.setText(drive.getText());
            updateFilterText();
            refreshFolder();
        });
        return drive;
    }

    private MenuItem getEntryMenuItem(String name) {
        MenuItem entry = new MenuItem(name);
        entry.eventMouseClick.add((sender, args) -> {
            InputDialog input = new InputDialog("Add New " + entry.getText() + ":", "Add", "new_" + entry.getText());
            input.selectAll();
            input.onCloseDialog.add(() -> {
                String result = input.getResult();
                if (result == null)
                    return;

                if ("File".equals(entry.getText())) {
                    File file = new File(_addressLine.getText() + File.separator + result);
                    if (!file.exists()) {
                        try {
                            if (file.createNewFile())
                                refreshFolder();
                        } catch (Exception e) {
                            PopUpMessage popError = new PopUpMessage("Failed to create file.");
                            popError.show(this);
                        }
                    } else {
                        PopUpMessage popError = new PopUpMessage("Failed to create file.");
                        popError.show(this);
                    }
                } else {
                    File dir = new File(_addressLine.getText() + File.separator + result);
                    if (!dir.exists()) {
                        if (dir.mkdir()) {
                            refreshFolder();
                        } else {
                            PopUpMessage popError = new PopUpMessage("Failed to create directory.");
                            popError.show(this);
                        }
                    }
                }

            });
            input.show(this);
        });
        return entry;
    }

    private Style _dividerStyle = null;

    private Rectangle getDivider() {
        Rectangle d = new Rectangle();
        d.setStyle(_dividerStyle);
        return d;
    }

    private void open() {
        FileSystemEntry selection = ((FileSystemEntry) _fileList.getSelectedItem());
        if (_dialogType == OpenDialogType.OPEN) {
            if (_entryType == FileSystemEntryType.FILE) {
                if (selection == null || selection.getEntryType() == FileSystemEntryType.DIRECTORY) {
                    PopUpMessage popError = new PopUpMessage("Choose file first.");
                    popError.show(this);
                    return;
                }
                _result = _addressLine.getText() + File.separator + selection.getText();
            } else if (_entryType == FileSystemEntryType.DIRECTORY) {
                _result = _addressLine.getText();
                if (selection != null)
                    _result += File.separator + selection.getText();
            }
        } else if (_dialogType == OpenDialogType.SAVE)
            _result = _addressLine.getText() + File.separator + _fileName.getText();

        close();
    }

    /**
     * Shows OpenEntryBox window.
     */
    @Override
    public void show() {
        super.show();
        _fileList.getArea().setFocus();
    }

    /**
     * Close OpenEntryBox window.
     */
    @Override
    public void close() {
        super.close();

        if (onCloseDialog != null) {
            onCloseDialog.execute();
        }
    }

    /**
     * Setting style of the OpenEntryBox.
     * <p>
     * Inner styles: "window", "layout", "toolbar", "toolbarbutton", "buttonhidden",
     * "addressline", "filenameline", "list", "controlpanel", "okbutton",
     * "cancelbutton", "filter", "filtertext", "divider".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    public void setStyle(Style style) {
        if (style == null)
            return;

        Style innerStyle = style.getInnerStyle("window");
        if (innerStyle != null) {
            getLayout().getContainer().setStyle(innerStyle);
            setMinSize(innerStyle.minWidth, innerStyle.minHeight);
            setSize(innerStyle.width, innerStyle.height);
        }

        // layout
        innerStyle = style.getInnerStyle("layout");
        if (innerStyle != null)
            _layout.setStyle(innerStyle);
            
        // toolbar
        innerStyle = style.getInnerStyle("toolbar");
        if (innerStyle != null)
            _toolbar.setStyle(innerStyle);
        // buttoncore
        innerStyle = style.getInnerStyle("toolbarbutton");
        if (innerStyle != null) {
            _btnBackward.setStyle(innerStyle);
            _btnUpward.setStyle(innerStyle);
            _btnHome.setStyle(innerStyle);
            _btnUser.setStyle(innerStyle);
            _btnCreate.setStyle(innerStyle);
            _btnRename.setStyle(innerStyle);
            _btnRefresh.setStyle(innerStyle);
        }
        // buttontogle
        innerStyle = style.getInnerStyle("buttonhidden");
        if (innerStyle != null)
            _btnShowHidden.setStyle(innerStyle);
        // addressline
        innerStyle = style.getInnerStyle("addressline");
        if (innerStyle != null)
            _addressLine.setStyle(innerStyle);
        // filename
        innerStyle = style.getInnerStyle("filenameline");
        if (innerStyle != null)
            _fileName.setStyle(innerStyle);
        // listbox
        innerStyle = style.getInnerStyle("list");
        if (innerStyle != null)
            _fileList.setStyle(innerStyle);
        // controlpanel
        innerStyle = style.getInnerStyle("controlpanel");
        if (innerStyle != null)
            _controlPanel.setStyle(innerStyle);
        // ok, cancel
        innerStyle = style.getInnerStyle("okbutton");
        if (innerStyle != null)
            _btnOpen.setStyle(innerStyle);
        innerStyle = style.getInnerStyle("cancelbutton");
        if (innerStyle != null)
            _btnCancel.setStyle(innerStyle);
        innerStyle = style.getInnerStyle("filter");
        if (innerStyle != null)
            _btnFilter.setStyle(innerStyle);
        innerStyle = style.getInnerStyle("filtertext");
        if (innerStyle != null) {
            _filterText.setStyle(innerStyle);
            updateFilterText();
        }
        innerStyle = style.getInnerStyle("divider");
        if (innerStyle != null)
            _dividerStyle = innerStyle;
    }
}