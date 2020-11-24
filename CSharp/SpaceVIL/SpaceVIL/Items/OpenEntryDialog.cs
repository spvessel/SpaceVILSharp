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
    /// <summary>
    /// OpenEntryDialog is user interface element for browsing file system 
    /// to select a file or folder to open or save.
    /// Support create/rename/delete files and folders, navigate shortcuts, file filtering.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class OpenEntryDialog : OpenDialog
    {
        private String _result = null;
        /// <summary>
        /// Getting full path of selected fyle system entry.
        /// </summary>
        /// <returns>Full path of selected fyle system entry.</returns>
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

        /// <summary>
        /// Setting the default path that will be opened when OpenEntryDialog is shown.
        /// </summary>
        /// <param name="path">Default path to open.</param>
        public void SetDefaultPath(String path)
        {
            _addressLine.SetText(path);
        }

        private FileSystemEntryType _entryType = FileSystemEntryType.File;
        private OpenDialogType _dialogType = OpenDialogType.Open;

        /// <summary>
        /// Constructs OpenEntryDialog with title text, entry type and dialog type. 
        /// <para/> Entry type can be FileSystemEntryType.File or FileSystemEntryType.Directory. 
        /// <para/> Dialog type can be OpenDialogType.Open or OpenDialogType.Save.
        /// </summary>
        /// <param name="title">Title text.</param>
        /// <param name="entryType">Entry type as SpaceVIL.Core.FileSystemEntryType.</param>
        /// <param name="dialogType">Dialog type as SpaceVIL.Core.OpenDialogType.</param>
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
            {
                _btnOpen = new ButtonCore("Save");
            }
            else
            {
                _btnOpen = new ButtonCore("Open");
            }

            _btnCancel = new ButtonCore("Cancel");

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.OpenEntryDialog)));
        }
        /// <summary>
        /// Constructs OpenEntryDialog with title text, entry type. Dialog type is OpenDialogType.Open.
        /// <para/> Entry type can be FileSystemEntryType.File or FileSystemEntryType.Directory. 
        /// </summary>
        /// <param name="title">Title text.</param>
        /// <param name="entryType">Entry type as SpaceVIL.Core.FileSystemEntryType.</param>
        public OpenEntryDialog(String title, FileSystemEntryType entryType) : this(title, entryType, OpenDialogType.Open) { }
        
        /// <summary>
        /// Constructs OpenEntryDialog with title text. Entry type is FileSystemEntryType.File. 
        /// Dialog type is OpenDialogType.Open.
        /// </summary>
        /// <param name="title">Title text.</param>
        public OpenEntryDialog(String title) : this(title, FileSystemEntryType.File) { }
        
        /// <summary>
        /// Initializing all elements in the OpenEntryDialog.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
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
                {
                    _fileName.SetText("new_file");
                }
                _filterList = new ContextMenu(GetHandler());
                _filterList.ActiveButton = MouseButton.ButtonLeft;
                AddFilterExtensions("All files (*.*);*.*");
            }
            _addNewList = new ContextMenu(GetHandler(), GetEntryMenuItem("Directory"), GetEntryMenuItem("File"));
            _addNewList.ActiveButton = MouseButton.ButtonLeft;

            _folder = DefaultsService.GetDefaultImage(EmbeddedImage.Folder, EmbeddedImageSize.Size64x64);
            _file = DefaultsService.GetDefaultImage(EmbeddedImage.File, EmbeddedImageSize.Size64x64);
            ImageItem backward = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.ArrowLeft, EmbeddedImageSize.Size64x64), false);
            ImageItem upward = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.ArrowUp, EmbeddedImageSize.Size64x64), false);
            ImageItem home = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Home, EmbeddedImageSize.Size64x64), false);
            ImageItem user = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.User, EmbeddedImageSize.Size64x64), false);
            ImageItem create = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.FolderPlus, EmbeddedImageSize.Size64x64), false);
            ImageItem rename = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Pencil, EmbeddedImageSize.Size64x64), false);
            ImageItem refresh = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Refresh, EmbeddedImageSize.Size64x64), false);
            ImageItem hidden = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Eye, EmbeddedImageSize.Size64x64), false);
            ImageItem filter = new ImageItem(DefaultsService.GetDefaultImage(EmbeddedImage.Filter, EmbeddedImageSize.Size64x64), false);
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

            _fileList.SetHScrollBarPolicy(VisibilityPolicy.AsNeeded);
            _fileList.SetVScrollBarPolicy(VisibilityPolicy.AsNeeded);
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
                    {
                        return;
                    }
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
            {
                FillFilterList();
            }
            RefreshFolder();
        }

        private void ShowFolder(String path)
        {
            ClearListBox();

            DirectoryInfo d = new DirectoryInfo(path); //Assuming Test is your Folder
            DirectoryInfo[] dirs = d.GetDirectories();
            FileInfo[] files = d.GetFiles();

            if (_fileList == null)
            {
                return;
            }

            // Maybe need some sorting
            if (dirs != null)
            {
                foreach (DirectoryInfo dir in dirs)
                {
                    if (!_btnShowHidden.IsToggled() && dir.Attributes.HasFlag(FileAttributes.Hidden))
                    {
                        continue;
                    }

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
                    {
                        continue;
                    }

                    if (CheckExtensionFilter(f))
                    {
                        continue;
                    }

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
                        {
                            Open();
                        }
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
                {
                    return;
                }
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
            {
                _addressLine.SetText(name + fse.GetText());
            }
            else
            {
                _addressLine.SetText(name + Path.DirectorySeparatorChar + fse.GetText());
            }

            _filesTrack.Push(_addressLine.GetText());
            RefreshFolder();
        }

        private void SetNameLine(FileSystemEntry fse)
        {
            if (_fileName.GetText().Equals(fse.GetText()))
            {
                return;
            }
            _fileName.SetText(fse.GetText());
        }
        /// <summary>
        /// Refresh opened folder.
        /// </summary>
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

        /// <summary>
        /// Adding file filter extensions. 
        /// <para/> Rule: "filter name (*.ext1, *.ext2, *.extN) ; *.ext1, *.ext2, *.extN&quot;
        /// <para/> Example 1: "Text files (*.txt) ; *.txt&quot;
        /// <para/> Example 2: "Images (*.png, *.bmp, *.jpg) ; *.png, *.bmp, *.jpg&quot;
        /// </summary>
        /// <param name="exts"> File filter extensions. </param>
        public void AddFilterExtensions(params String[] exts)
        {
            for (int i = 0; i < exts.Length; i++)
            {
                String[] line = exts[i].Split(';');
                String key = line[0];
                String regex = line[1].ToLower().Replace("*", "").Replace(" ", "");
                if (_extensionFilter.ContainsKey(key))
                {
                    _extensionFilter[key] = regex.Split(',');
                }
                else
                {
                    _extensionFilter.Add(key, regex.Split(','));
                }
            }
        }

        private bool CheckExtensionFilter(FileInfo f)
        {
            if (_extensionFilter.Count == 0)
            {
                return false;
            }

            String name = f.Name.ToLower();
            foreach (String item in _extensionFilter[_filterText.GetText()])
            {
                if (name.EndsWith(item) || item.Equals("."))
                {
                    return false;
                }
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
                    {
                        return;
                    }

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
                    {
                        _result += Path.DirectorySeparatorChar + selection.GetText();
                    }
                }
            }
            else if (_dialogType == OpenDialogType.Save)
            {
                _result = _addressLine.GetText() + Path.DirectorySeparatorChar + _fileName.GetText();
            }
            Close();
        }
        /// <summary>
        /// Shows OpenEntryDialog and attaches it to the specified window 
        /// (see SpaceVIL.CoreWindow, SpaceVIL.ActiveWindow, SpaceVIL.DialogWindow).
        /// </summary>
        /// <param name="handler">Window for attaching OpenEntryDialog.</param>
        public override void Show(CoreWindow handler)
        {
            base.Show(handler);
            _fileList.GetArea().SetFocus();
        }
        /// <summary>
        /// Setting style of the OpenEntryDialog.
        /// <para/> Inner styles: "window", "layout", "toolbar", "toolbarbutton",
        /// "buttonhidden", "addressline", "filenameline", "list", "controlpanel", 
        /// "okbutton", "cancelbutton", "filter", "filtertext", "divider".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
            {
                return;
            }
            base.SetStyle(style);

            // toolbar
            Style innerStyle = style.GetInnerStyle("window");
            if (innerStyle != null)
            {
                Window.SetStyle(innerStyle);
            }

            // layout
            innerStyle = style.GetInnerStyle("layout");
            if (innerStyle != null)
            {
                _layout.SetStyle(innerStyle);
            }
            
            // toolbar
            innerStyle = style.GetInnerStyle("toolbar");
            if (innerStyle != null)
            {
                _toolbar.SetStyle(innerStyle);
            }
            
            // buttoncore
            innerStyle = style.GetInnerStyle("toolbarbutton");
            if (innerStyle != null)
            {
                _btnBackward.SetStyle(innerStyle);
                _btnHome.SetStyle(innerStyle);
                _btnUser.SetStyle(innerStyle);
                _btnCreate.SetStyle(innerStyle);
                _btnRename.SetStyle(innerStyle);
                _btnRefresh.SetStyle(innerStyle);
                _btnUpward.SetStyle(innerStyle);
            }

            // buttontogle
            innerStyle = style.GetInnerStyle("buttonhidden");
            if (innerStyle != null)
            {
                _btnShowHidden.SetStyle(innerStyle);
            }

            // addressline
            innerStyle = style.GetInnerStyle("addressline");
            if (innerStyle != null)
            {
                _addressLine.SetStyle(innerStyle);
            }
            
            // filename
            innerStyle = style.GetInnerStyle("filenameline");
            if (innerStyle != null)
            {
                _fileName.SetStyle(innerStyle);
            }
            
            // listbox
            innerStyle = style.GetInnerStyle("list");
            if (innerStyle != null)
            {
                _fileList.SetStyle(innerStyle);
            }
            
            // controlpanel
            innerStyle = style.GetInnerStyle("controlpanel");
            if (innerStyle != null)
            {
                _controlPanel.SetStyle(innerStyle);
            }

            // ok, cancel
            innerStyle = style.GetInnerStyle("okbutton");
            if (innerStyle != null)
            {
                _btnOpen.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("cancelbutton");
            if (innerStyle != null)
            {
                _btnCancel.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("filter");
            if (innerStyle != null)
            {
                _btnFilter.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("filtertext");
            if (innerStyle != null)
            {
                _filterText.SetStyle(innerStyle);
                UpdateFilterText();
            }
            innerStyle = style.GetInnerStyle("divider");
            if (innerStyle != null)
            {
                _dividerStyle = innerStyle;
            }
        }
    }
}