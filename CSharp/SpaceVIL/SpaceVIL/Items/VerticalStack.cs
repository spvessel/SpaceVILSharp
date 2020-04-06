using SpaceVIL.Common;
using SpaceVIL.Core;
using System.Collections.Generic;

namespace SpaceVIL
{
    /// <summary>
    /// VerticalStack is a class that represents a line type container (vertical version). 
    /// VerticalStack groups items one after another using content alignment, margins, paddings, 
    /// sizes and size policies.
    /// VerticalStack implements SpaceVIL.Core.IHLayout.
    /// By default ability to get focus is disabled.
    /// <para/> VerticalStack cannot receive any events, 
    /// so VerticalStack is always in the ItemState.Base state.
    /// </summary>
    public class VerticalStack : Prototype, IVLayout
    {
        static int count = 0;

        private ItemAlignment _contentAlignment = ItemAlignment.Top;
        /// <summary>
        /// Setting content alignment within VerticalStack area. Default: ItemAlignment.Left.
        /// <para/> Supports only: ItemAlignment.Left, ItemAlignment.HCenter, ItemAlignment.Right.
        /// </summary>
        /// <param name="alignment">Content alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetContentAlignment(ItemAlignment alignment)
        {
            if (alignment == ItemAlignment.Top || alignment == ItemAlignment.VCenter || alignment == ItemAlignment.Bottom)
                _contentAlignment = alignment;
        }
        /// <summary>
        /// Getting current content alignment.
        /// <para/> Can be: ItemAlignment.Left, ItemAlignment.HCenter, ItemAlignment.Right.
        /// </summary>
        /// <returns>Content alignment as SpaceVIL.Core.ItemAlignment.</returns>
        public ItemAlignment GetContentAlignment()
        {
            return _contentAlignment;
        }
        /// <summary>
        /// Default VerticalStack constructor.
        /// </summary>
        public VerticalStack()
        {
            SetItemName("VerticalStack_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.VerticalStack)));
            IsFocusable = false;

        }

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }
        /// <summary>
        /// Adding item to the VerticalStack. 
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            base.AddItem(item);
            UpdateLayout();
        }
        /// <summary>
        /// Inserting item to the VerticalStack container. 
        /// If the number of container elements is less than the index, 
        /// then the element is added to the end of the list.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <param name="index">Index of insertion.</param>
        public override void InsertItem(IBaseItem item, int index)
        {
            base.InsertItem(item, index);
            UpdateLayout();
        }
        /// <summary>
        /// Removing the specified item from the VerticalStack container.
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public override bool RemoveItem(IBaseItem item)
        {
            bool result = base.RemoveItem(item);
            if (result)
                UpdateLayout();
            return result;
        }
        /// <summary>
        /// Setting VerticalStack height. If the value is greater/less than the maximum/minimum 
        /// value of the height, then the height becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="height">Weight of the VerticalStack.</param>
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateLayout();
        }
        /// <summary>
        /// Setting Y coordinate of the left-top corner of the VerticalStack.
        /// </summary>
        /// <param name="y">Y position of the left-top corner.</param>
        public override void SetY(int y)
        {
            base.SetY(y);
            UpdateLayout();
        }
        /// <summary>
        /// Updating all children positions (implementation of SpaceVIL.Core.IFreeLayout).
        /// </summary>
        public void UpdateLayout()
        {
            int total_space = GetHeight() - GetPadding().Top - GetPadding().Bottom;
            int free_space = total_space;
            int fixed_count = 0;
            int expanded_count = 0;

            List<int> maxHeightExpands = new List<int>();

            foreach (var child in GetItems())
            {
                if (child.IsVisible())
                {
                    if (child.GetHeightPolicy() == SizePolicy.Fixed)
                    {
                        fixed_count++;
                        free_space -= (child.GetHeight() + child.GetMargin().Top + child.GetMargin().Bottom);//
                    }
                    else
                    {
                        if (child.GetMaxHeight() < total_space)
                        {
                            maxHeightExpands.Add(child.GetMaxHeight() + child.GetMargin().Top + child.GetMargin().Bottom);
                        }
                        expanded_count++;
                    }
                }
            }
            free_space -= GetSpacing().Vertical * ((expanded_count + fixed_count) - 1);

            int height_for_expanded = 1;
            // if (expanded_count > 0 && free_space > expanded_count)
            //     height_for_expanded = free_space / expanded_count;

            maxHeightExpands.Sort();

            while (true)
            {
                if (expanded_count == 0)
                    break;

                if (free_space > expanded_count)
                    height_for_expanded = free_space / expanded_count;

                if (height_for_expanded <= 1 || maxHeightExpands.Count == 0)
                {
                    break;
                }

                if (height_for_expanded > maxHeightExpands[0])
                {
                    while (maxHeightExpands.Count > 0 && height_for_expanded > maxHeightExpands[0])
                    {
                        free_space -= maxHeightExpands[0];
                        maxHeightExpands.RemoveAt(0);
                        expanded_count--;
                    }
                }
                else
                {
                    break;
                }
            }

            int offset = 0;
            int startY = GetY() + GetPadding().Top;
            bool isFirstExpand = false;
            int diff = (free_space - height_for_expanded * expanded_count);
            if (expanded_count != 0 && diff > 0)
            {
                isFirstExpand = true;
            }

            if (expanded_count > 0 || _contentAlignment.Equals(ItemAlignment.Top))
            {
                foreach (var child in GetItems())
                {
                    if (child.IsVisible())
                    {
                        child.SetY(startY + offset + child.GetMargin().Top);//
                        if (child.GetHeightPolicy() == SizePolicy.Expand)
                        {
                            if (height_for_expanded - child.GetMargin().Top - child.GetMargin().Bottom < child.GetMaxHeight())//
                            {
                                child.SetHeight(height_for_expanded - child.GetMargin().Top - child.GetMargin().Bottom);//
                            }
                            else
                            {
                                // expanded_count--;
                                // free_space -= (child.GetHeight() + child.GetMargin().Top + child.GetMargin().Bottom);
                                // height_for_expanded = 1;
                                // if (expanded_count > 0 && free_space > expanded_count)
                                //     height_for_expanded = free_space / expanded_count;
                                child.SetHeight(child.GetMaxHeight()); //?
                            }

                            if (isFirstExpand)
                            {
                                if (child.GetHeight() + diff < child.GetMaxHeight())
                                {
                                    child.SetHeight(child.GetHeight() + diff);
                                    isFirstExpand = false;
                                }
                            }
                        }
                        offset += child.GetHeight() + GetSpacing().Vertical + child.GetMargin().Top + child.GetMargin().Bottom;//
                    }
                    //refactor
                    child.SetConfines();
                }
            }
            else
            {
                //for content alignment right
                if (_contentAlignment.Equals(ItemAlignment.Bottom))
                {
                    foreach (var child in GetItems())
                    {
                        if (child.IsVisible())
                        {
                            child.SetY(startY + offset + child.GetMargin().Top + free_space);
                            offset += child.GetHeight() + GetSpacing().Vertical + child.GetMargin().Top + child.GetMargin().Bottom;//

                            if (child.GetHeightPolicy() == SizePolicy.Expand)
                                child.SetHeight(child.GetMaxHeight());
                        }
                        child.SetConfines();
                    }
                }
                //for content alignment hcenter
                else if (_contentAlignment.Equals(ItemAlignment.VCenter))
                {
                    foreach (var child in GetItems())
                    {
                        if (child.IsVisible())
                        {
                            child.SetY(startY + offset + child.GetMargin().Top + free_space / 2);
                            offset += child.GetHeight() + GetSpacing().Vertical + child.GetMargin().Top + child.GetMargin().Bottom;//

                            if (child.GetHeightPolicy() == SizePolicy.Expand)
                                child.SetHeight(child.GetMaxHeight());
                        }
                        child.SetConfines();
                    }
                }
            }
        }
    }
}
