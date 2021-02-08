using System.Collections.Generic;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// HorizontalStack is a class that represents a line type container (horizontal version). 
    /// HorizontalStack groups items one after another using content alignment, margins, paddings, 
    /// sizes and size policies.
    /// HorizontalStack implements SpaceVIL.Core.IHLayout.
    /// <para/> By default ability to get focus is disabled.
    /// <para/> HorizontalStack cannot receive any events, 
    /// so HorizontalStack is always in the SpaceVIL.Core.ItemStateType.Base state.
    /// </summary>
    public class HorizontalStack : Prototype, IHLayout
    {
        static int count = 0;

        private ItemAlignment _contentAlignment = ItemAlignment.Left;
        /// <summary>
        /// Setting content alignment within HorizontalStack area. 
        /// <para/> Supports only: ItemAlignment.Left, ItemAlignment.HCenter, ItemAlignment.Right.
        /// <para/> Default: ItemAlignment.Left.
        /// </summary>
        /// <param name="alignment">Content alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetContentAlignment(ItemAlignment alignment)
        {
            if (alignment == ItemAlignment.Left || alignment == ItemAlignment.HCenter || alignment == ItemAlignment.Right)
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
        /// Default HorizontalStack constructor.
        /// </summary>
        public HorizontalStack()
        {
            SetItemName("HorizontalStack_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.HorizontalStack)));
            IsFocusable = false;
        }

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }

        /// <summary>
        /// Adding item to the HorizontalStack. 
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public override void AddItem(IBaseItem item)
        {
            base.AddItem(item);
            UpdateLayout();
        }
        /// <summary>
        /// Inserting item to the HorizontalStack container. 
        /// If the count of container elements is less than the index, 
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
        /// Removing the specified item from the HorizontalStack container.
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
        /// Setting HorizontalStack width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width">Width of the HorizontalStack.</param>
        public override void SetWidth(int width)
        {
            base.SetWidth(width);
            UpdateLayout();
        }

        /// <summary>
        /// Setting X coordinate of the left-top corner of the HorizontalStack.
        /// </summary>
        /// <param name="x">X position of the left-top corner.</param>
        public override void SetX(int x)
        {
            base.SetX(x);
            UpdateLayout();
        }

        /// <summary>
        /// Updating all children positions (implementation of SpaceVIL.Core.IHLayout).
        /// </summary>
        public void UpdateLayout()
        {
            int total_space = GetWidth() - GetPadding().Left - GetPadding().Right;
            int free_space = total_space;
            int fixed_count = 0;
            int expanded_count = 0;

            List<int> maxWidthExpands = new List<int>();

            foreach (var child in GetItems())
            {
                if (child.IsVisible())
                {
                    if (child.GetWidthPolicy() == SizePolicy.Fixed)
                    {
                        fixed_count++;
                        free_space -= (child.GetWidth() + child.GetMargin().Left + child.GetMargin().Right);//
                    }
                    else
                    {
                        if (child.GetMaxWidth() < total_space)
                        {
                            maxWidthExpands.Add(child.GetMaxWidth() + child.GetMargin().Right + child.GetMargin().Left);
                        }
                        expanded_count++;
                    }
                }
            }
            free_space -= GetSpacing().Horizontal * ((expanded_count + fixed_count) - 1);

            int width_for_expanded = 1;
            // if (expanded_count > 0 && free_space > expanded_count)
            //     width_for_expanded = free_space / expanded_count;
            maxWidthExpands.Sort();

            while (true)
            {
                if (expanded_count == 0)
                    break;

                if (free_space > expanded_count)
                    width_for_expanded = free_space / expanded_count;

                if (width_for_expanded <= 1 || maxWidthExpands.Count == 0)
                {
                    //                    width_for_expanded = 1;
                    break;
                }

                if (width_for_expanded > maxWidthExpands[0])
                {
                    while (maxWidthExpands.Count > 0 && width_for_expanded > maxWidthExpands[0])
                    {
                        free_space -= maxWidthExpands[0];
                        maxWidthExpands.RemoveAt(0);
                        expanded_count--;
                    }
                }
                else
                {
                    break;
                }
                //                width_for_expanded = widthForExpand(free_space, expanded_count);
            }

            int offset = 0;
            int startX = GetX() + GetPadding().Left;
            bool isFirstExpand = false;
            int diff = (free_space - width_for_expanded * expanded_count);
            if (expanded_count != 0 && diff > 0)
            {
                isFirstExpand = true;
            }

            if (expanded_count > 0 || _contentAlignment.Equals(ItemAlignment.Left))
            {
                foreach (var child in GetItems())
                {
                    if (child.IsVisible())
                    {
                        child.SetX(startX + offset + child.GetMargin().Left);//

                        if (child.GetWidthPolicy() == SizePolicy.Expand)
                        {
                            if (width_for_expanded - child.GetMargin().Left - child.GetMargin().Right < child.GetMaxWidth())
                            {
                                child.SetWidth(width_for_expanded - child.GetMargin().Left - child.GetMargin().Right);
                            }
                            else
                            {
                                // expanded_count--;
                                // free_space -= (child.GetWidth() + child.GetMargin().Left + child.GetMargin().Right);//
                                // width_for_expanded = 1;
                                // if (expanded_count > 0 && free_space > expanded_count)
                                //     width_for_expanded = free_space / expanded_count;
                                child.SetWidth(child.GetMaxWidth());
                            }

                            if (isFirstExpand)
                            {
                                if (child.GetWidth() + diff < child.GetMaxWidth())
                                {
                                    child.SetWidth(child.GetWidth() + diff);
                                    isFirstExpand = false;
                                }
                            }
                        }
                        offset += child.GetWidth() + GetSpacing().Horizontal + child.GetMargin().Left + child.GetMargin().Right;//
                    }
                    //refactor
                    child.SetConfines();
                }
            }
            else
            {
                //for content alignment right
                if (_contentAlignment.Equals(ItemAlignment.Right))
                {
                    foreach (var child in GetItems())
                    {
                        if (child.IsVisible())
                        {
                            child.SetX(startX + offset + child.GetMargin().Left + free_space);
                            offset += child.GetWidth() + GetSpacing().Horizontal + child.GetMargin().Left + child.GetMargin().Right;//

                            if (child.GetWidthPolicy() == SizePolicy.Expand)
                                child.SetWidth(child.GetMaxWidth());
                        }
                        child.SetConfines();
                    }
                }
                //for content alignment hcenter
                else if (_contentAlignment.Equals(ItemAlignment.HCenter))
                {
                    foreach (var child in GetItems())
                    {
                        if (child.IsVisible())
                        {
                            child.SetX(startX + offset + child.GetMargin().Left + free_space / 2);
                            offset += child.GetWidth() + GetSpacing().Horizontal + child.GetMargin().Left + child.GetMargin().Right;//

                            if (child.GetWidthPolicy() == SizePolicy.Expand)
                                child.SetWidth(child.GetMaxWidth());
                        }
                        child.SetConfines();
                    }
                }
            }
        }
    }
}
