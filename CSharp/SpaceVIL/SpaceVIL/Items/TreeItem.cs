using System;
using System.Drawing;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Common;
using SpaceVIL.Decorations;
using System.Threading;

namespace SpaceVIL
{
    /// <summary>
    /// TreeItem is designed to be a node for SpaceVIL.TreeView (branch-leaf type of container).
    /// <para/> Can be as leaf node or as branch node. 
    /// Branch node can contains another branches and leafs. 
    /// Leaf node cannot contains any nodes.
    /// <para/> Contains text, icon, indicator (branch only).
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class TreeItem : Prototype
    {
        private Object Locker = new Object();

        private List<TreeItem> _listInners;

        /// <summary>
        /// Getting all contained nodes in this TreeItem.
        /// </summary>
        /// <returns>Contained nodes as List&lt;SpaceVIL.TreeItem&gt;</returns>
        public List<TreeItem> GetChildren()
        {
            return _listInners;
        }

        /// <summary>
        /// Removing the specified node from TreeItem.
        /// </summary>
        /// <param name="child">Node as SpaceVIL.TreeItem.</param>
        public void RemoveChild(TreeItem child)
        {
            if (_listInners.Contains(child))
            {
                _listInners.Remove(child);
                child.RemoveChildren();
                _treeViewContainer.RemoveItem(child);
            }
        }

        /// <summary>
        /// Removing all contained nodes in this TreeItem.
        /// </summary>
        public void RemoveChildren()
        {
            Monitor.Enter(Locker);
            try
            {
                foreach (var item in _listInners)
                {
                    item.RemoveChildren();
                    _treeViewContainer.RemoveItem(item);
                }
                _listInners.Clear();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - RemoveChildren");
                Console.WriteLine(ex.StackTrace);
            }
            finally
            {
                Monitor.Exit(Locker);
            }
        }

        private TreeItem _parentBranch;

        /// <summary>
        /// Getting the parent branch node that contains this TreeItem.
        /// </summary>
        /// <returns>Parent branch node as SpaceVIL.TreeItem.</returns>
        public TreeItem GetParentBranch()
        {
            return _parentBranch;
        }

        internal TreeView _treeViewContainer;
        internal int _nestingLevel = 0;
        private int _indentSize = 20;

        /// <summary>
        /// Getting indent size (X axis) of the current 
        /// TreeItem relative to its parent branch.
        /// </summary>
        /// <returns>Indent size (X axis).</returns>
        public int GetIndentSize()
        {
            return _indentSize;
        }

        /// <summary>
        /// Setting indent size (X axis) for the current 
        /// TreeItem relative to its parent branch.
        /// </summary>
        /// <param name="size">Indent size (X axis).</param>
        public void SetIndentSize(int size)
        {
            _indentSize = size;
            ResetIndents();
        }

        private bool _isRoot = false;

        /// <summary>
        /// Returns True if this TreeItem is root (head) otherwise returns False.
        /// </summary>
        /// <returns>True: if this TreeItem is root (head).
        /// False: if this TreeItem is not root (head).</returns>
        public bool IsRoot()
        {
            return _isRoot;
        }
        internal void SetRoot(bool value)
        {
            _isRoot = value;
        }
        internal UInt32 Index = 0;
        private TreeItemType _nodeType;

        /// <summary>
        /// Getting node type.
        /// <para/> Can be TreeItemType.Leaf or TreeItemType.Brunch.
        /// </summary>
        /// <returns>Node type as SpaceVIL.Core.TreeItemType.</returns>
        public TreeItemType GetItemType()
        {
            return _nodeType;
        }

        static int count = 0;

        private Label _textLabel;

        private ButtonToggle _indicator;

        /// <summary>
        /// Getting the branch node indicator of TreeItem.
        /// </summary>
        /// <returns>Branch node indicator.</returns>
        public ButtonToggle GetIndicator()
        {
            return _indicator;
        }

        private CustomShape _iconShape;

        /// <summary>
        /// Constructs TreeItem with specified type of node.
        /// </summary>
        /// <param name="type">Node type as SpaceVIL.Core.TreeItemType.</param>
        public TreeItem(TreeItemType type)
        {
            _nodeType = type;
            SetItemName(type.ToString().ToLower() + "_v" + count);
            count++;

            _listInners = new List<TreeItem>();
            _indicator = new ButtonToggle();
            _indicator.SetItemName("Indicator_" + count);
            _textLabel = new Label();
            _textLabel.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            _iconShape = new CustomShape();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TreeItem)));
            EventKeyPress += OnKeyPress;
        }

        /// <summary>
        /// Constructs TreeItem with specified type of node and text.
        /// </summary>
        /// <param name="type">Node type as SpaceVIL.Core.TreeItemType.</param>
        /// <param name="text">Text of TreeItem.</param>
        public TreeItem(TreeItemType type, String text = "") : this(type)
        {
            SetText(text);
        }

        private void OnKeyPress(IItem sender, KeyArgs args)
        {
            if (args.Key == KeyCode.Enter)
                _indicator.EventToggle.Invoke(sender, new MouseArgs());

            // else if (args.Key == KeyCode.Space)
            //     AddItem(new TreeItem(TreeItemType.Branch, "new branch " + count));
            // else if (args.Key == KeyCode.Equal)
            // {
            //     TreeItem tr = new TreeItem(TreeItemType.Leaf, "new leaf " + count);
            //     tr.EventMouseDoubleClick += (s, a)=>{
            //         Console.WriteLine(tr.GetItemName() + " click");
            //     };
            //     AddItem(tr);
            // }

            // else if (args.Key == KeyCode.Delete)
            //     GetParentBranch().RemoveChild(this);
        }

        internal void ResetIndents()
        {
            Int32 level = _nestingLevel;
            if (!_treeViewContainer._root.IsVisible())
                level--;

            SetPadding(2 + _indentSize * level, 0, 0, 0);
            int width = GetPadding().Left;
            foreach (IBaseItem item in GetItems())
            {
                width += item.GetWidth() + item.GetMargin().Left + item.GetMargin().Right + GetSpacing().Horizontal;
            }

            int newMinWidth = width - GetSpacing().Horizontal;
            if (newMinWidth > _treeViewContainer.MaxWrapperWidth)
            {
                _treeViewContainer.MaxWrapperWidth = newMinWidth;
                _treeViewContainer.RefreshWrapperWidth();
            }
            else
            {
                newMinWidth = _treeViewContainer.MaxWrapperWidth;
            }

            SetMinWidth(newMinWidth);
            _treeViewContainer.GetWrapper(this).SetMinWidth(newMinWidth);
        }

        /// <summary>
        /// Initializing all elements in the TreeItem. 
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            _textLabel.IsFocusable = false;
            switch (_nodeType)
            {
                case TreeItemType.Leaf:
                    _iconShape.SetMargin(2, 0, 0, 0);
                    base.AddItem(_iconShape);
                    base.AddItem(_textLabel);
                    break;

                case TreeItemType.Branch:
                    base.AddItems(_indicator);
                    base.AddItem(_iconShape);
                    base.AddItem(_textLabel);

                    _indicator.EventToggle += (sender, args) => OnToggleHide(_indicator.IsToggled());
                    _indicator.IsFocusable = false;
                    EventMouseDoubleClick += (sender, args) =>
                    {
                        if (args.Button == MouseButton.ButtonLeft)
                            _indicator.EventToggle.Invoke(sender, args);
                    };
                    break;

                default:
                    base.AddItem(_textLabel);
                    break;
            }
        }

        internal void OnToggleHide(bool value) // refactor
        {
            foreach (var item in _listInners)
            {
                if (value)
                {
                    if (_indicator.IsToggled())
                    {
                        _treeViewContainer.GetWrapper(item).SetVisible(true);
                        item.SetVisible(true);
                        item.OnToggleHide(value);
                    }
                }
                else
                {
                    _treeViewContainer.GetWrapper(item).SetVisible(false);
                    item.SetVisible(false);
                    item.OnToggleHide(value);
                }
            }
            //update layout
            _treeViewContainer.UpdateElements();
        }

        private void AddTreeItem(TreeItem item)
        {
            item._parentBranch = this;
            item._treeViewContainer = _treeViewContainer;
            item._nestingLevel = _nestingLevel + 1;
            if (!_indicator.IsToggled())
                item.SetVisible(false);

            List<TreeItem> neighbors = GetChildren();
            int ind = -1;

            for (int i = 0; i < neighbors.Count; i++)
            {
                int compareResult = _treeViewContainer.CompareInAlphabet(neighbors[i], item);
                if (compareResult > 0)
                    break;
                ind = i;
            }

            _listInners.Insert(ind + 1, item);
            if (ind == -1)
                _treeViewContainer.RefreshTree(this, item);
            else
                _treeViewContainer.RefreshTree(_listInners[ind], item);

            // OnToggleHide(true);
        }

        /// <summary>
        /// Adding item into the TreeItem.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            TreeItem tmp = item as TreeItem;
            if (tmp != null)
                AddTreeItem(tmp);
            else
                base.AddItem(item);
        }

        /// <summary>
        /// Setting item width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width"> Width of the item. </param>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateLayout();
        }

        /// <summary>
        /// Setting X coordinate of the left-top corner of the TreeItem.
        /// </summary>
        /// <param name="x">X position of the left-top corner.</param>
        public override void SetX(int x)
        {
            base.SetX(x);
            UpdateLayout();
        }

        protected virtual void UpdateLayout()
        {
            _textLabel.SetWidth(_textLabel.GetTextWidth() + 5);
            //update self width
            int offset = 0;
            int startX = GetX() + GetPadding().Left;

            foreach (var child in GetItems())
            {
                child.SetX(startX + offset + child.GetMargin().Left);
                if (child.GetWidthPolicy() == SizePolicy.Expand)
                {
                    child.SetWidth(GetWidth() - offset);
                }
                offset += child.GetWidth() + GetSpacing().Horizontal;
            }
        }

        /// <summary>
        /// Setting alignment of TreeItem text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textLabel.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Setting alignment of TreeItem text. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Text alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            _textLabel.SetTextAlignment(alignment);
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to TreeItem.
        /// </summary>
        /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
        public void SetTextMargin(Indents margin)
        {
            _textLabel.SetMargin(margin);
        }

        /// <summary>
        /// Setting indents for the text to offset text relative to TreeItem.
        /// </summary>
        /// <param name="left">Indent on the left.</param>
        /// <param name="top">Indent on the top.</param>
        /// <param name="right">Indent on the right.</param>
        /// <param name="bottom">Indent on the bottom.</param>
        public void SetTextMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _textLabel.SetMargin(left, top, right, bottom);
        }

        /// <summary>
        /// Getting indents of the text.
        /// </summary>
        /// <returns>Indents as SpaceVIL.Decorations.Indents.</returns>
        public Indents GetTextMargin()
        {
            return _textLabel.GetMargin();
        }

        /// <summary>
        /// Setting font of the text.
        /// </summary>
        /// <param name="font">Font as System.Drawing.Font.</param>
        public void SetFont(Font font)
        {
            _textLabel.SetFont(font);
        }

        /// <summary>
        /// Setting font size of the text.
        /// </summary>
        /// <param name="size">New size of the font.</param>
        public void SetFontSize(int size)
        {
            _textLabel.SetFontSize(size);
        }

        /// <summary>
        /// Setting font style of the text.
        /// </summary>
        /// <param name="style">New font style as System.Drawing.FontStyle.</param>
        public void SetFontStyle(FontStyle style)
        {
            _textLabel.SetFontStyle(style);
        }

        /// <summary>
        /// Setting new font family of the text.
        /// </summary>
        /// <param name="fontFamily">New font family as System.Drawing.FontFamily.</param>
        public void SetFontFamily(FontFamily fontFamily)
        {
            _textLabel.SetFontFamily(fontFamily);
        }

        /// <summary>
        /// Getting the current font of the text.
        /// </summary>
        /// <returns>Font as System.Drawing.Font.</returns>
        public Font GetFont()
        {
            return _textLabel.GetFont();
        }

        /// <summary>
        /// Setting the text.
        /// </summary>
        /// <param name="text">Text as System.String.</param>
        public virtual void SetText(String text)
        {
            _textLabel.SetText(text);
            UpdateLayout();
        }

        /// <summary>
        /// Getting the current text of the TreeItem.
        /// </summary>
        /// <returns>Text as System.String.</returns>
        public virtual String GetText()
        {
            return _textLabel.GetText();
        }

        /// <summary>
        /// Getting the text width (useful when you need resize TreeItem by text width).
        /// </summary>
        /// <returns>Text width.</returns>
        public int GetTextWidth()
        {
            return _textLabel.GetWidth();
        }

        /// <summary>
        /// Getting the text height (useful when you need resize TreeItem by text height).
        /// </summary>
        /// <returns>Text height.</returns>
        public int GetTextHeight()
        {
            return _textLabel.GetHeight();
        }

        /// <summary>
        /// Setting text color of a TreeItem.
        /// </summary>
        /// <param name="color">Text color as System.Drawing.Color.</param>
        public void SetForeground(Color color)
        {
            _textLabel.SetForeground(color);
        }

        /// <summary>
        /// Setting text color of a TreeItem in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b)
        {
            _textLabel.SetForeground(r, g, b);
        }

        /// <summary>
        /// Setting text color of a TreeItem in byte RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        /// <param name="a">Alpha color component. Range: (0 - 255)</param>
        public void SetForeground(int r, int g, int b, int a)
        {
            _textLabel.SetForeground(r, g, b, a);
        }

        /// <summary>
        /// Setting text color of a TreeItem in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b)
        {
            _textLabel.SetForeground(r, g, b);
        }

        /// <summary>
        /// Setting text color of a TreeItem in float RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha color component. Range: (0.0f - 1.0f)</param>
        public void SetForeground(float r, float g, float b, float a)
        {
            _textLabel.SetForeground(r, g, b, a);
        }

        /// <summary>
        /// Getting current text color.
        /// </summary>
        /// <returns>Text color as System.Drawing.Color.</returns>
        public Color GetForeground()
        {
            return _textLabel.GetForeground();
        }

        /// <summary>
        /// Setting style of the TreeItem.
        /// <para/> Inner styles: "indicator", "branchicon", "leaficon".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            SetForeground(style.Foreground);
            SetFont(style.Font);
            SetTextAlignment(style.TextAlignment);

            //additional
            Style innerStyle = style.GetInnerStyle("indicator");
            if (innerStyle != null)
            {
                _indicator.SetStyle(innerStyle);
            }
            if (_nodeType == TreeItemType.Branch)
                innerStyle = style.GetInnerStyle("branchicon");
            else
                innerStyle = style.GetInnerStyle("leaficon");

            if (innerStyle != null)
            {
                _iconShape.SetStyle(innerStyle);
            }
        }

        /// <summary>
        /// Shows or hides content (contained nodes) of this TreeItem.
        /// </summary>
        /// <param name="value">True: if you want to show content.
        /// False: if you want to hide content.</param>
        public void SetExpanded(bool value)
        {
            if (_nodeType.Equals(TreeItemType.Branch))
            {
                _indicator.SetToggled(value);
                OnToggleHide(value);
            }
        }
    }
}