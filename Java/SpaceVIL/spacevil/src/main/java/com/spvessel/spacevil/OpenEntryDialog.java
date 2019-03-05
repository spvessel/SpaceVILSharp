package com.spvessel.spacevil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.FileSystemEntryType;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;

public class OpenEntryDialog extends OpenDialog {
    private String _result = null;

    public String getResult() {
        return _result;
    }

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

    public void setDefaultPath(String path) {
        _addressLine.setText(path);
    }

    public enum OpenDialogType {
        OPEN, SAVE
    }

    private FileSystemEntryType _entryType = FileSystemEntryType.FILE;
    private OpenDialogType _dialogType = OpenDialogType.OPEN;

    public OpenEntryDialog(String title, FileSystemEntryType entryType, OpenDialogType dialogType) {
        setTitle(title);
        _entryType = entryType;
        _dialogType = dialogType;
        _filesTrack.add(System.getProperty("user.home"));

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
        _btnOpen = new ButtonCore("Open");
        _btnCancel = new ButtonCore("Cancel");

        setStyle(DefaultsService.getDefaultStyle(OpenEntryDialog.class));
    }

    public OpenEntryDialog(String title, FileSystemEntryType entryType) {
        this(title, entryType, OpenDialogType.OPEN);
    }

    public OpenEntryDialog(String title) {
        this(title, FileSystemEntryType.FILE);
    }

    @Override
    public void initElements() {
        // important!
        super.initElements();
        if (_entryType == FileSystemEntryType.DIRECTORY) {
            _fileName.setVisible(false);
            _btnFilter.setVisible(false);
            _filterText.setVisible(false);
        } else {
            if (_dialogType == OpenDialogType.SAVE)
                _fileName.setText("new_file");
            _filterList = new ContextMenu(getHandler());
            _filterList.activeButton = MouseButton.BUTTON_LEFT;
            addFilterExtensions("All files (*.*);*.*");
        }
        _addNewList = new ContextMenu(getHandler(), getEntryMenuItem("Directory"), getEntryMenuItem("File"));
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

        window.addItems(_layout);
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

        _fileList.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        _fileList.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
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
            FileSystemEntry selected = ((FileSystemEntry) _fileList.getSelectionItem());
            InputDialog input = new InputDialog("Rename:", "OK", selected.getText());
            input.selectAll();
            input.onCloseDialog.add(() -> {
                String result = input.getResult();
                if (result == null)
                    return;
                File file = new File(
                        _addressLine.getText() + "\\" + ((FileSystemEntry) _fileList.getSelectionItem()).getText());
                File newFile = new File(_addressLine.getText() + "\\" + result);
                if (file.renameTo(newFile)) {
                    selected.setText(result);
                } else {
                    PopUpMessage popError = new PopUpMessage(
                            (selected.getEntryType() == FileSystemEntryType.DIRECTORY) ? "Can not rename directory."
                                    : "Can not rename file.");
                    popError.show(getHandler());
                }
            });
            input.show(getHandler());
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
        if (_fileList == null)
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

        if (_entryType.equals(FileSystemEntryType.FILE)) {
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
        int ind = name.lastIndexOf("\\");
        int firstInd = name.indexOf("\\");
        if (name.endsWith("\\")) {
            if (ind <= 1 || name.substring(ind - 1, ind).equals(":"))
                return;
            name = name.substring(0, name.length() - 1);
            System.out.println(name);
            ind = name.lastIndexOf("\\");
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
        if (name.endsWith("\\") || name.endsWith("/"))
            _addressLine.setText(name + fse.getText());
        else
            _addressLine.setText(name + "\\" + fse.getText());

        _filesTrack.push(_addressLine.getText());
        refreshFolder();
    }

    private void setNameLine(FileSystemEntry fse) {
        if (_fileName.getText().equals(fse.getText()))
            return;
        _fileName.setText(fse.getText());
    }

    public final void refreshFolder() {
        String path = _addressLine.getText(); // need some check
        showFolder(path);
        _fileList.getArea().setFocus();
    }

    private void clearListBox() {
        _fileList.getArea().removeAllItems();
    }

    private Map<String, String[]> _extensionFilter = new LinkedHashMap<>();

    // "All files (*.*);*.*"
    public void addFilterExtensions(String... exts) {
        for (int i = 0; i < exts.length; i++) {
            String[] line = exts[i].split(";");
            String key = line[0];
            String regex = line[1].toLowerCase().replaceAll("[!*]", "");
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

                if (entry.getText().equals("File")) {
                    File file = new File(_addressLine.getText() + "\\" + result);
                    if (!file.exists()) {
                        try {
                            if (file.createNewFile())
                                refreshFolder();
                        } catch (Exception e) {
                            PopUpMessage popError = new PopUpMessage("Failed to create file.");
                            popError.show(getHandler());
                        }
                    } else {
                        PopUpMessage popError = new PopUpMessage("Failed to create file.");
                        popError.show(getHandler());
                    }
                } else {
                    File dir = new File(_addressLine.getText() + "\\" + result);
                    if (!dir.exists()) {
                        if (dir.mkdir()) {
                            refreshFolder();
                        } else {
                            PopUpMessage popError = new PopUpMessage("Failed to create directory.");
                            popError.show(getHandler());
                        }
                    }
                }

            });
            input.show(getHandler());
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
        FileSystemEntry selection = ((FileSystemEntry) _fileList.getSelectionItem());
        if (_dialogType == OpenDialogType.OPEN) {
            if (_entryType == FileSystemEntryType.FILE) {
                if (selection == null || selection.getEntryType() == FileSystemEntryType.DIRECTORY) {
                    PopUpMessage popError = new PopUpMessage("Choose file first.");
                    popError.show(getHandler());
                    return;
                }
                _result = _addressLine.getText() + "\\" + selection.getText();
            } else if (_entryType == FileSystemEntryType.DIRECTORY) {
                _result = _addressLine.getText();
                if (selection != null)
                    _result += "\\" + selection.getText();
            }
        } else if (_dialogType == OpenDialogType.SAVE)
            _result = _addressLine.getText() + "\\" + _fileName.getText();
        close();
    }

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        // toolbar
        Style inner_style = style.getInnerStyle("window");
        if (inner_style != null)
            window.setStyle(inner_style);
        // layout
        inner_style = style.getInnerStyle("layout");
        if (inner_style != null)
            _layout.setStyle(inner_style);
        // toolbar
        inner_style = style.getInnerStyle("toolbar");
        if (inner_style != null)
            _toolbar.setStyle(inner_style);
        // buttoncore
        inner_style = style.getInnerStyle("toolbarbutton");
        if (inner_style != null) {
            _btnBackward.setStyle(inner_style);
            _btnUpward.setStyle(inner_style);
            _btnHome.setStyle(inner_style);
            _btnUser.setStyle(inner_style);
            _btnCreate.setStyle(inner_style);
            _btnRename.setStyle(inner_style);
            _btnRefresh.setStyle(inner_style);
        }
        // buttontogle
        inner_style = style.getInnerStyle("buttonhidden");
        if (inner_style != null)
            _btnShowHidden.setStyle(inner_style);
        // addressline
        inner_style = style.getInnerStyle("addressline");
        if (inner_style != null)
            _addressLine.setStyle(inner_style);
        // filename
        inner_style = style.getInnerStyle("filenameline");
        if (inner_style != null)
            _fileName.setStyle(inner_style);
        // listbox
        inner_style = style.getInnerStyle("list");
        if (inner_style != null)
            _fileList.setStyle(inner_style);
        // controlpanel
        inner_style = style.getInnerStyle("controlpanel");
        if (inner_style != null)
            _controlPanel.setStyle(inner_style);
        // ok, cancel
        inner_style = style.getInnerStyle("okbutton");
        if (inner_style != null)
            _btnOpen.setStyle(inner_style);
        inner_style = style.getInnerStyle("cancelbutton");
        if (inner_style != null)
            _btnCancel.setStyle(inner_style);
        inner_style = style.getInnerStyle("filter");
        if (inner_style != null)
            _btnFilter.setStyle(inner_style);
        inner_style = style.getInnerStyle("filtertext");
        if (inner_style != null) {
            _filterText.setStyle(inner_style);
            updateFilterText();
        }
        inner_style = style.getInnerStyle("divider");
        if (inner_style != null)
            _dividerStyle = inner_style;
    }
}