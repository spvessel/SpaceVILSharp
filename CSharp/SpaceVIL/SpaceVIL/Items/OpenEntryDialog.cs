using System;
using System.Collections.Generic;
using System.Linq;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using System.IO;

namespace SpaceVIL
{
    public class OpenEntryDialog : OpenDialog
    {
        private String _result = null;

        public String GetResult()
        {
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

        private Stack<String> _filesTrack = new Stack<String>();
        private Bitmap _folder = null;
        private Bitmap _file = null;

        public void SetDefaultPath(String path)
        {
            _addressLine.SetText(path);
        }

        private FileSystemEntryType _entryType = FileSystemEntryType.File;
        private OpenDialogType _dialogType = OpenDialogType.Open;

        public OpenEntryDialog(String title, FileSystemEntryType entryType, OpenDialogType dialogType)
        {
            SetTitle(title);
            _entryType = entryType;
            _dialogType = dialogType;
            String userHomePath = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile);
            _filesTrack.Push(userHomePath);

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
            _addressLine = new TextEdit(userHomePath);
            _fileList = new ListBox();
            _fileName = new TextEdit();
            _controlPanel = new Frame();
            
            if (dialogType == OpenDialogType.Save)
                _btnOpen = new ButtonCore("Save");
            else
                _btnOpen = new ButtonCore("Open");

            _btnCancel = new ButtonCore("Cancel");

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.OpenEntryDialog)));
        }

        public OpenEntryDialog(String title, FileSystemEntryType entryType) : this(title, entryType, OpenDialogType.Open) { }
        public OpenEntryDialog(String title) : this(title, FileSystemEntryType.File) { }

        public override void InitElements()
        {
            // important!
            base.InitElements();
            if (_entryType == FileSystemEntryType.Directory)
            {
                _fileName.SetVisible(false);
                _btnFilter.SetVisible(false);
                _filterText.SetVisible(false);
            }
            else
            {
                if (_dialogType == OpenDialogType.Save)
                    _fileName.SetText("new_file");
                _filterList = new ContextMenu(GetHandler());
                _filterList.ActiveButton = MouseButton.ButtonLeft;
                AddFilterExtensions("All files (*.*);*.*");
            }
            _addNewList = new ContextMenu(GetHandler(), GetEntryMenuItem("Directory"), GetEntryMenuItem("File"));
            _addNewList.ActiveButton = MouseButton.ButtonLeft;

            _folder = DefaultsService.GetDefaultImage(EmbeddedImage.Folder, EmbeddedImageSize.Size32x32);
            _file = DefaultsService.GetDefaultImage(EmbeddedImage.File, EmbeddedImageSize.Size32x32);
            ImageItem backward = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.ArrowLeft, EmbeddedImageSize.Size32x32), false);
            ImageItem upward = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.ArrowUp, EmbeddedImageSize.Size32x32), false);
            ImageItem home = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Home, EmbeddedImageSize.Size32x32), false);
            ImageItem user = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.User, EmbeddedImageSize.Size32x32), false);
            ImageItem create = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.FolderPlus, EmbeddedImageSize.Size32x32), false);
            ImageItem rename = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Pencil, EmbeddedImageSize.Size32x32), false);
            ImageItem refresh = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Refresh, EmbeddedImageSize.Size32x32), false);
            ImageItem hidden = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Eye, EmbeddedImageSize.Size32x32), false);
            ImageItem filter = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Filter, EmbeddedImageSize.Size32x32), false);
            Style.GetFrameStyle().SetStyle(backward, upward, create, rename, refresh, hidden, user, home, filter);

            Window.AddItems(_layout);
            _layout.AddItems(_toolbar, _addressLine, _fileList, _fileName, _controlPanel);
            _toolbar.AddItems(_btnBackward, _btnUpward, GetDivider(), _btnHome, _btnUser, GetDivider(), _btnCreate, _btnRename,
                    GetDivider(), _btnRefresh, GetDivider(), _btnShowHidden, GetDivider(), _btnFilter, _filterText);
            _btnBackward.AddItem(backward);
            _btnUpward.AddItem(upward);
            _btnHome.AddItem(home);
            _btnUser.AddItem(user);
            _btnCreate.AddItem(create);
            _btnRename.AddItem(rename);
            _btnRefresh.AddItem(refresh);
            _btnShowHidden.AddItem(hidden);
            _btnFilter.AddItem(filter);
            _controlPanel.AddItems(_btnOpen, _btnCancel);

            _fileList.SetHScrollBarVisible(ScrollBarVisibility.AsNeeded);
            _fileList.SetVScrollBarVisible(ScrollBarVisibility.AsNeeded);
            _btnBackward.EventMouseClick += (sender, args) => { PathBackward(); };
            _btnUpward.EventMouseClick += (sender, args) => { PathUpward(); };
            _btnHome.EventMouseClick += (sender, args) =>
            {
                _addressLine.SetText(AppDomain.CurrentDomain.BaseDirectory);
                RefreshFolder();
            };
            _btnUser.EventMouseClick += (sender, args) =>
            {
                _addressLine.SetText(Environment.GetFolderPath(Environment.SpecialFolder.UserProfile));
                RefreshFolder();
            };
            _btnCreate.EventMouseClick += (sender, args) =>
            {
                _addNewList.Show(sender, args);
            };
            _btnRename.EventMouseClick += (sender, args) =>
            {
                FileSystemEntry selected = ((FileSystemEntry)_fileList.GetSelectedItem());
                InputDialog input = new InputDialog("Rename:", "OK", selected.GetText());
                input.SelectAll();
                input.OnCloseDialog += () =>
                {
                    String result = input.GetResult();
                    if (result == null)
                        return;
                    try
                    {
                        System.IO.File.Move(_addressLine.GetText() + Path.DirectorySeparatorChar + ((FileSystemEntry)_fileList.GetSelectedItem()).GetText(),
                             _addressLine.GetText() + Path.DirectorySeparatorChar + result);
                        selected.SetText(result);
                    }
                    catch (System.Exception)
                    {
                        PopUpMessage popError = new PopUpMessage((selected.GetEntryType() == FileSystemEntryType.Directory) ? "Can not rename directory." : "Can not rename file.");
                        popError.Show(GetHandler());
                    }
                };
                input.Show(GetHandler());
            };
            _btnRefresh.EventMouseClick += (sender, args) => { RefreshFolder(); };
            _btnFilter.EventMouseClick += (sender, args) =>
            {
                args.Position.SetPosition(_filterText.GetX(), _filterText.GetY() + _filterText.GetHeight());
                _filterList.Show(sender, args);
            };
            _btnShowHidden.EventToggle += (sender, args) => { RefreshFolder(); };
            _btnCancel.EventMouseClick += (sender, args) =>
            {
                _result = null;
                Close();
            };
            _btnOpen.EventMouseClick += (sender, args) =>
            {
                Open();
            };
            _addressLine.EventKeyRelease += (sender, args) => PathEditFinished(args);
            _fileList.GetArea().EventKeyRelease += (sender, args) =>
            {
                if (args.Key == KeyCode.Backspace)
                {
                    PathBackward();
                }
            };
            if (_filterList != null)
                FillFilterList();
            RefreshFolder();
        }

        private void ShowFolder(String path)
        {
            ClearListBox();

            DirectoryInfo d = new DirectoryInfo(path); //Assuming Test is your Folder
            DirectoryInfo[] dirs = d.GetDirectories();
            FileInfo[] files = d.GetFiles();

            if (_fileList == null)
                return;

            // Maybe need some sorting
            if (dirs != null)
            {
                foreach (DirectoryInfo dir in dirs)
                {
                    if (!_btnShowHidden.IsToggled() && dir.Attributes.HasFlag(FileAttributes.Hidden))
                        continue;

                    FileSystemEntry fi = new FileSystemEntry(FileSystemEntryType.Directory, dir.Name);
                    fi.SetIcon(new Bitmap(_folder), 16, 16);
                    fi.SetPassEvents(false, InputEventType.MouseDoubleClick);
                    _fileList.AddItem(fi);
                    fi.EventMouseDoubleClick += (sender, args) =>
                    {
                        SetAddressLine(fi);
                    };
                    fi.EventKeyRelease += (sender, args) =>
                    {
                        if (args.Key == KeyCode.Enter)
                        {
                            SetAddressLine(fi);
                        }
                    };
                }
            }

            if (_entryType == FileSystemEntryType.File && files != null)
            {
                foreach (FileInfo f in files)
                {
                    if (!_btnShowHidden.IsToggled() && f.Attributes.HasFlag(FileAttributes.Hidden))
                        continue;

                    if (CheckExtensionFilter(f))
                        continue;

                    FileSystemEntry fi = new FileSystemEntry(FileSystemEntryType.File, f.Name);
                    fi.SetIcon(new Bitmap(_file), 16, 16);
                    fi.SetText(f.Name);
                    _fileList.AddItem(fi);
                    fi.EventMouseClick += (sender, args) =>
                    {
                        SetNameLine(fi);
                    };
                    fi.EventMouseDoubleClick += (sender, args) =>
                    {
                        Open();
                    };
                    fi.EventKeyRelease += (sender, args) =>
                    {
                        if (args.Key == KeyCode.Enter)
                            Open();
                    };
                }
            }
        }

        private void PathEditFinished(KeyArgs args)
        {
            if (args.Key == KeyCode.Enter || args.Key == KeyCode.NumpadEnter)
            {
                String text = _addressLine.GetText();
                if (text == null || text.Equals(""))
                {
                    text = "./";
                    _addressLine.SetText(text);
                }
                _filesTrack.Push(text);
                RefreshFolder();
            }
        }

        private void PathBackward()
        {
            if (_filesTrack.Count > 1)
            {
                _filesTrack.Pop();
                _addressLine.SetText(_filesTrack.First());
                RefreshFolder();
            }
        }

        private void PathUpward()
        {
            String name = _addressLine.GetText();
            int ind = name.LastIndexOf(Path.DirectorySeparatorChar);
            int firstInd = name.IndexOf(Path.DirectorySeparatorChar);
            if (name.EndsWith(Path.DirectorySeparatorChar.ToString()))
            {
                if (ind <= 1 || name.Substring(ind - 1, 1).Equals(":"))
                    return;
                name = name.Substring(0, name.Length - 1);
                ind = name.LastIndexOf(Path.DirectorySeparatorChar);
            }

            if (ind == firstInd)
            {
                if (ind == -1)
                { //No such index
                    if (name.Contains("./"))
                    {
                        ind = name.IndexOf("/") + 1;
                    }
                    else
                    {
                        return;
                    }
                }
                else if (name.Substring(ind - 1, 1).Equals(":"))
                {
                    ind = ind + 1;
                }
            }

            name = name.Substring(0, ind);
            _addressLine.SetText(name);
            _filesTrack.Push(name);
            RefreshFolder();
        }

        private void SetAddressLine(FileSystemEntry fse)
        {
            String name = _addressLine.GetText();
            if (name.EndsWith(Path.DirectorySeparatorChar.ToString()) || name.EndsWith("/"))
                _addressLine.SetText(name + fse.GetText());
            else
                _addressLine.SetText(name + Path.DirectorySeparatorChar + fse.GetText());

            _filesTrack.Push(_addressLine.GetText());
            RefreshFolder();
        }

        private void SetNameLine(FileSystemEntry fse)
        {
            if (_fileName.GetText().Equals(fse.GetText()))
                return;
            _fileName.SetText(fse.GetText());
        }

        public void RefreshFolder()
        {
            String path = _addressLine.GetText(); // need some check
            ShowFolder(path);
            _fileList.GetArea().SetFocus();
        }

        private void ClearListBox()
        {
            _fileList.GetArea().Clear();
        }

        private Dictionary<String, String[]> _extensionFilter = new Dictionary<String, String[]>();

        // "All files (*.*);*.*"
        public void AddFilterExtensions(params String[] exts)
        {
            for (int i = 0; i < exts.Length; i++)
            {
                String[] line = exts[i].Split(';');
                String key = line[0];
                String regex = line[1].ToLower().Replace("*", "");
                if (_extensionFilter.ContainsKey(key))
                    _extensionFilter[key] = regex.Split(',');
                else
                    _extensionFilter.Add(key, regex.Split(','));
            }
        }

        private bool CheckExtensionFilter(FileInfo f)
        {
            if (_extensionFilter.Count == 0)
                return false;

            String name = f.Name.ToLower();
            foreach (String item in _extensionFilter[_filterText.GetText()])
            {
                if (name.EndsWith(item) || item.Equals("."))
                    return false;
            }
            return true;
        }

        private void UpdateFilterText()
        {
            _filterText.SetWidth(_filterText.GetTextWidth() + _filterText.GetPadding().Left + _filterText.GetPadding().Right);
        }

        private void FillFilterList()
        {
            _filterList.Clear();

            foreach (String entry in _extensionFilter.Keys)
            {
                _filterList.AddItem(GetFilterMenuItem(entry));
            }
            _filterText.SetText(_extensionFilter.Keys.First());
            UpdateFilterText();
        }

        private MenuItem GetFilterMenuItem(String name)
        {
            MenuItem drive = new MenuItem(name);
            drive.EventMouseClick += (sender, args) =>
            {
                _filterText.SetText(drive.GetText());
                UpdateFilterText();
                RefreshFolder();
            };
            return drive;
        }

        private MenuItem GetEntryMenuItem(String name)
        {
            MenuItem entry = new MenuItem(name);
            entry.EventMouseClick += (sender, args) =>
            {
                InputDialog input = new InputDialog("Add New " + entry.GetText() + ":", "Add", "new_" + entry.GetText());
                input.SelectAll();
                input.OnCloseDialog += () =>
                {
                    String result = input.GetResult();
                    if (result == null)
                        return;

                    if ("File".Equals(entry.GetText()))
                    {
                        if (!File.Exists(_addressLine.GetText() + Path.DirectorySeparatorChar + result))
                        {
                            try
                            {
                                File.Create(_addressLine.GetText() + Path.DirectorySeparatorChar + result);
                                RefreshFolder();
                            }
                            catch (System.Exception ex)
                            {
                                Console.WriteLine(ex.StackTrace);
                                PopUpMessage popError = new PopUpMessage("Failed to create file.");
                                popError.Show(GetHandler());
                            }
                        }
                        else
                        {
                            PopUpMessage popError = new PopUpMessage("File already exist.");
                            popError.Show(GetHandler());
                        }
                    }
                    else
                    {
                        if (!Directory.Exists(_addressLine.GetText() + Path.DirectorySeparatorChar + result))
                        {
                            try
                            {
                                Directory.CreateDirectory(_addressLine.GetText() + Path.DirectorySeparatorChar + result);
                                RefreshFolder();

                            }
                            catch (System.Exception)
                            {
                                PopUpMessage popError = new PopUpMessage("Failed to create directory.");
                                popError.Show(GetHandler());
                            }

                        }
                    }
                };
                input.Show(GetHandler());
            };
            return entry;
        }

        private Style _dividerStyle = null;

        private Rectangle GetDivider()
        {
            Rectangle d = new Rectangle();
            d.SetStyle(_dividerStyle);
            return d;
        }

        private void Open()
        {
            FileSystemEntry selection = ((FileSystemEntry)_fileList.GetSelectedItem());
            if (_dialogType == OpenDialogType.Open)
            {
                if (_entryType == FileSystemEntryType.File)
                {
                    if (selection == null || selection.GetEntryType() == FileSystemEntryType.Directory)
                    {
                        PopUpMessage popError = new PopUpMessage("Choose file first.");
                        popError.Show(GetHandler());
                        return;
                    }
                    _result = _addressLine.GetText() + Path.DirectorySeparatorChar + selection.GetText();
                }
                else if (_entryType == FileSystemEntryType.Directory)
                {
                    _result = _addressLine.GetText();
                    if (selection != null)
                        _result += Path.DirectorySeparatorChar + selection.GetText();
                }
            }
            else if (_dialogType == OpenDialogType.Save)
                _result = _addressLine.GetText() + Path.DirectorySeparatorChar + _fileName.GetText();
            Close();
        }

        public override void Show(CoreWindow handler)
        {
            base.Show(handler);
            _fileList.GetArea().SetFocus();
        }

        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            // toolbar
            Style inner_style = style.GetInnerStyle("window");
            if (inner_style != null)
                Window.SetStyle(inner_style);
            // layout
            inner_style = style.GetInnerStyle("layout");
            if (inner_style != null)
                _layout.SetStyle(inner_style);
            // toolbar
            inner_style = style.GetInnerStyle("toolbar");
            if (inner_style != null)
                _toolbar.SetStyle(inner_style);
            // buttoncore
            inner_style = style.GetInnerStyle("toolbarbutton");
            if (inner_style != null)
            {
                _btnBackward.SetStyle(inner_style);
                _btnHome.SetStyle(inner_style);
                _btnUser.SetStyle(inner_style);
                _btnCreate.SetStyle(inner_style);
                _btnRename.SetStyle(inner_style);
                _btnRefresh.SetStyle(inner_style);
                _btnUpward.SetStyle(inner_style);
            }
            // buttontogle
            inner_style = style.GetInnerStyle("buttonhidden");
            if (inner_style != null)
                _btnShowHidden.SetStyle(inner_style);
            // addressline
            inner_style = style.GetInnerStyle("addressline");
            if (inner_style != null)
                _addressLine.SetStyle(inner_style);
            // filename
            inner_style = style.GetInnerStyle("filenameline");
            if (inner_style != null)
                _fileName.SetStyle(inner_style);
            // listbox
            inner_style = style.GetInnerStyle("list");
            if (inner_style != null)
                _fileList.SetStyle(inner_style);
            // controlpanel
            inner_style = style.GetInnerStyle("controlpanel");
            if (inner_style != null)
                _controlPanel.SetStyle(inner_style);
            // ok, cancel
            inner_style = style.GetInnerStyle("okbutton");
            if (inner_style != null)
                _btnOpen.SetStyle(inner_style);
            inner_style = style.GetInnerStyle("cancelbutton");
            if (inner_style != null)
                _btnCancel.SetStyle(inner_style);
            inner_style = style.GetInnerStyle("filter");
            if (inner_style != null)
                _btnFilter.SetStyle(inner_style);
            inner_style = style.GetInnerStyle("filtertext");
            if (inner_style != null)
            {
                _filterText.SetStyle(inner_style);
                UpdateFilterText();
            }
            inner_style = style.GetInnerStyle("divider");
            if (inner_style != null)
                _dividerStyle = inner_style;
        }
    }
}