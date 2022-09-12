using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace View.json {
    public class ItemsFactory {
        public static object GetObject(string name)
        {
            switch (name[0]) {
                case 'B':
                {
                    if (name.Equals("BlankItem"))
                    {
                        return GetBlankItem();
                    }
                    else if (name.Equals("ButtonCore"))
                    {
                        return GetButtonCore();
                    }
                    else if (name.Equals("ButtonToggle"))
                    {
                        return GetButtonToggle();
                    }
                    break;
                }
                case 'C':
                {
                    if (name.Equals("CheckBox"))
                    {
                        return GetCheckBox();
                    }
                    else if (name.Equals("ComboBox"))
                    {
                        return GetComboBox();
                    }
                    else if (name.Equals("ComboBoxDropDown"))
                    {
                        return GetComboBoxDropDown();
                    }
                    else if (name.Equals("CustomShape"))
                    {
                        return GetCustomShape();
                    }
                    break;
                }
                case 'E':
                {
                    if (name.Equals("Ellipse"))
                    {
                        return GetEllipse();
                    }
                    break;
                }
                case 'F':
                {
                    if (name.Equals("Frame"))
                    {
                        return GetFrame();
                    }
                    else if (name.Equals("FreeArea"))
                    {
                        return GetFreeArea();
                    }
                    break;
                }
                case 'G':
                {
                    if (name.Equals("Graph"))
                    {
                        return GetGraph();
                    }
                    else if (name.Equals("Grid"))
                    {
                        return GetGrid();
                    }
                    break;
                }
                case 'H':
                {
                    if (name.Equals("HorizontalScrollBar"))
                    {
                        return GetHorizontalScrollBar();
                    }
                    else if (name.Equals("HorizontalSlider"))
                    {
                        return GetHorizontalSlider();
                    }
                    else if (name.Equals("HorizontalSplitArea"))
                    {
                        return GetHorizontalSplitArea();
                    }
                    else if (name.Equals("HorizontalStack"))
                    {
                        return GetHorizontalStack();
                    }
                    break;
                }
                case 'I':
                {
                    if (name.Equals("ImageItem"))
                    {
                        return GetImageItem();
                    }
                    else if (name.Equals("Indicator"))
                    {
                        return GetIndicator();
                    }
                    break;
                }
                case 'L':
                {
                    if (name.Equals("Label"))
                    {
                        return GetLabel();
                    }
                    else if (name.Equals("LinesContainer"))
                    {
                        return GetLinesContainer();
                    }
                    else if (name.Equals("ListArea"))
                    {
                        return GetListArea();
                    }
                    else if (name.Equals("ListBox"))
                    {
                        return GetListBox();
                    }
                    else if (name.Equals("LoadingScreen"))
                    {
                        return GetLoadingScreen();
                    }
                    break;
                }
                case 'M':
                {
                    if (name.Equals("MenuItem"))
                    {
                        return GetMenuItem();
                    }
                    else if (name.Equals("MessageBox"))
                    {
                        return GetMessageBox();
                    }
                    else if (name.Equals("MessageItem"))
                    {
                        return GetMessageItem();
                    }
                    break;
                }

                case 'P':
                {
                    if (name.Equals("PasswordLine"))
                    {
                        return GetPasswordLine();
                    }
                    else if (name.Equals("PointsContainer"))
                    {
                        return GetPointsContainer();
                    }
                    else if (name.Equals("ProgressBar"))
                    {
                        return GetProgressBar();
                    }
                    break;
                }
                case 'R':
                {
                    if (name.Equals("RadioButton"))
                    {
                        return GetRadioButton();
                    }
                    else if (name.Equals("Rectangle"))
                    {
                        return GetRectangle();
                    }
                    else if (name.Equals("ResizableItem"))
                    {
                        return GetResizableItem();
                    }
                    break;
                }
                case 'S':
                {
                    if (name.Equals("ScrollHandler"))
                    {
                        return GetScrollHandler();
                    }
                    else if (name.Equals("SpinItem"))
                    {
                        return GetSpinItem();
                    }
                    else if (name.Equals("Style") || name.Equals("InnerStyle")) //???
                    {
                        return GetStyle();
                    }
                    break;
                }
                case 'T':
                {
                    if (name.Equals("Tab"))
                    {
                        return GetTab();
                    }
                    else if (name.Equals("TabView"))
                    {
                        return GetTabView();
                    }
                    else if (name.Equals("TextArea"))
                    {
                        return GetTextArea();
                    }
                    else if (name.Equals("TextEdit"))
                    {
                        return GetTextEdit();
                    }
                    else if (name.Equals("TextView"))
                    {
                        return GetTextView();
                    }
                    else if (name.Equals("TitleBar"))
                    {
                        return GetTitleBar();
                    }
                    else if (name.Equals("TreeView"))
                    {
                        return GetTreeView();
                    }
                    else if (name.Equals("Triangle"))
                    {
                        return GetTriangle();
                    }
                    break;
                }
                case 'V':
                {
                    if (name.Equals("VerticalScrollBar"))
                    {
                        return GetVerticalScrollBar();
                    }
                    else if (name.Equals("VerticalSlider"))
                    {
                        return GetVerticalSlider();
                    }
                    else if (name.Equals("VerticalSplitArea"))
                    {
                        return GetVerticalSplitArea();
                    }
                    else if (name.Equals("VerticalStack"))
                    {
                        return GetVerticalStack();
                    }
                    break;
                }
            }
            return null;
        }

        public static BlankItem GetBlankItem()
        {
            return new BlankItem();
        }
    
        public static ButtonCore GetButtonCore()
        {
            return new ButtonCore();
        }
    
        public static ButtonToggle GetButtonToggle()
        {
            return new ButtonToggle();
        }
    
        public static CheckBox GetCheckBox()
        {
            return new CheckBox();
        }
    
        public static ComboBox GetComboBox()
        {
            return new ComboBox();
        }
    
        public static ComboBoxDropDown GetComboBoxDropDown()
        {
            return new ComboBoxDropDown();
        }
    
        //TODO
        public static ContextMenu GetContextMenu(CoreWindow handler)
        {
            return new ContextMenu(handler);
        }
    
        //???
        public static CustomShape GetCustomShape()
        {
            return new CustomShape();
        }
    
        //???
        public static Ellipse GetEllipse()
        {
            return new Ellipse();
        }
    
        //TODO
        public static FileSystemEntry GetFileSystemEntry(FileSystemEntryType type, string text)
        {
            return new FileSystemEntry(type, text);
        }
    
        //TODO
        public static FloatItem GetFloatItem(CoreWindow handler)
        {
            return new FloatItem(handler);
        }
    
        public static Frame GetFrame()
        {
            return new Frame();
        }
    
        public static FreeArea GetFreeArea()
        {
            return new FreeArea();
        }
    
        public static Graph GetGraph()
        {
            return new Graph();
        }
    
        public static Grid GetGrid()
        {
            return new Grid(1, 1); //assume, that I'll set row/colCount later
        }
    
        public static HorizontalScrollBar GetHorizontalScrollBar()
        {
            return new HorizontalScrollBar();
        }
    
        public static HorizontalSlider GetHorizontalSlider()
        {
            return new HorizontalSlider();
        }
    
        public static HorizontalSplitArea GetHorizontalSplitArea()
        {
            return new HorizontalSplitArea();
        }
    
        public static HorizontalStack GetHorizontalStack()
        {
            return new HorizontalStack();
        }
    
        public static ImageItem GetImageItem()
        {
            return new ImageItem();
        }
    
        public static Indicator GetIndicator()
        {
            return new Indicator();
        }
    
        //TODO
        public static InputBox GetInputBox(string title, string actionName, string defaultText)
        {
            return new InputBox(title, actionName, defaultText);
        }
    
        //TODO
        public static InputDialog GetInputDialog(string title, string actionName, string defaultText)
        {
            return new InputDialog(title, actionName, defaultText);
        }
    
        public static Label GetLabel()
        {
            return new Label();
        }
    
        public static LinesContainer GetLinesContainer()
        {
            return new LinesContainer();
        }
    
        public static ListArea GetListArea()
        {
            return new ListArea();
        }
    
        public static ListBox GetListBox()
        {
            return new ListBox();
        }
    
        public static LoadingScreen GetLoadingScreen()
        {
            return new LoadingScreen();
        }
    
        public static MenuItem GetMenuItem()
        {
            return new MenuItem();
        }
    
        public static MessageBox GetMessageBox()
        {
            return new MessageBox();
        }
    
        public static MessageItem GetMessageItem()
        {
            return new MessageItem();
        }
    
        //TODO
        public static OpenEntryBox GetOpenEntryBox(string title, FileSystemEntryType entryType, OpenDialogType dialogType)
        {
            return new OpenEntryBox(title, entryType, dialogType);
        }
    
        //TODO
        public static OpenEntryDialog GetOpenEntryDialog(string title, FileSystemEntryType entryType, OpenDialogType dialogType)
        {
            return new OpenEntryDialog(title, entryType, dialogType);
        }
    
        public static PasswordLine GetPasswordLine()
        {
            return new PasswordLine();
        }
    
        public static PointsContainer GetPointsContainer()
        {
            return new PointsContainer();
        }
    
        //TODO
        public static PopUpMessage GetPopUpMessage(string message)
        {
            return new PopUpMessage(message);
        }
    
        public static ProgressBar GetProgressBar()
        {
            return new ProgressBar();
        }
    
        public static RadioButton GetRadioButton()
        {
            return new RadioButton();
        }
    
        public static Rectangle GetRectangle()
        {
            return new Rectangle();
        }
    
        public static ResizableItem GetResizableItem()
        {
            return new ResizableItem();
        }
    
        public static ScrollHandler GetScrollHandler()
        {
            return new ScrollHandler();
        }
    
        //TODO
        public static SelectionItem GetSelectionItem(IBaseItem content)
        {
            return new SelectionItem(content);
        }
    
        //TODO
        public static SideArea GetSideArea(CoreWindow handler, Side attachSide)
        {
            return new SideArea(handler, attachSide);
        }
    
        public static SpinItem GetSpinItem()
        {
            return new SpinItem();
        }
    
        //TODO
        public static SplitHolder GetSplitHolder(Orientation orientation)
        {
            return new SplitHolder(orientation);
        }
    
        public static Style GetStyle()
        {
            return Style.GetDefaultCommonStyle();
        }
    
        public static Tab GetTab()
        {
            return new Tab();
        }
    
        public static TabView GetTabView()
        {
            return new TabView();
        }
    
        public static TextArea GetTextArea()
        {
            return new TextArea();
        }
    
        public static TextEdit GetTextEdit()
        {
            return new TextEdit();
        }
    
        public static TextView GetTextView()
        {
            return new TextView();
        }
    
        public static TitleBar GetTitleBar()
        {
            return new TitleBar();
        }
    
        //TODO
        public static TreeItem GetTreeItem(TreeItemType type)
        {
            return new TreeItem(type);
        }
    
        public static TreeView GetTreeView()
        {
            return new TreeView();
        }
    
        public static Triangle GetTriangle()
        {
            return new Triangle();
        }
    
        public static VerticalScrollBar GetVerticalScrollBar()
        {
            return new VerticalScrollBar();
        }
    
        public static VerticalSlider GetVerticalSlider()
        {
            return new VerticalSlider();
        }
    
        public static VerticalSplitArea GetVerticalSplitArea()
        {
            return new VerticalSplitArea();
        }
    
        public static VerticalStack GetVerticalStack()
        {
            return new VerticalStack();
        }
    
        //TODO
        public static WrapArea GetWrapArea(int cellWidth, int cellHeight, Orientation orientation)
        {
            return new WrapArea(cellWidth, cellHeight, orientation);
        }
    
        //TODO
        public static WrapGrid GetWrapGrid(int cellWidth, int cellHeight, Orientation orientation)
        {
            return new WrapGrid(cellWidth, cellHeight, orientation);
        }
    }
}