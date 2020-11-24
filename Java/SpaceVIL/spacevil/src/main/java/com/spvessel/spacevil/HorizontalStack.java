package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IHLayout;

import java.util.List;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.Collections;
import java.util.LinkedList;

/**
 * HorizontalStack is a class that represents a line type container (horizontal
 * version). HorizontalStack groups items one after another using content
 * alignment, margins, paddings, sizes and size policies. HorizontalStack
 * implements com.spvessel.spacevil.Core.IHLayout.
 * <p>
 * By default ability to get focus is disabled.
 * <p>
 * HorizontalStack cannot receive any events, so HorizontalStack is always in
 * the com.spvessel.spacevil.Flags.ItemStateType.BASE state.
 */
public class HorizontalStack extends Prototype implements IHLayout {
    private static int count = 0;

    private ItemAlignment _contentAlignment = ItemAlignment.Left;

    /**
     * Setting content alignment within HorizontalStack area. Default:
     * ItemAlignment.LEFT.
     * <p>
     * Supports only: ItemAlignment.LEFT, ItemAlignment.HCENTER,
     * ItemAlignment.RIGHT.
     * 
     * @param alignment Content alignment as
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setContentAlignment(ItemAlignment alignment) {
        if (alignment == ItemAlignment.Left || alignment == ItemAlignment.HCenter || alignment == ItemAlignment.Right)
            _contentAlignment = alignment;
    }

    /**
     * Getting current content alignment.
     * <p>
     * Can be: ItemAlignment.LEFT, ItemAlignment.HCENTER, ItemAlignment.RIGHT.
     * 
     * @return Content alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public ItemAlignment getContentAlignment() {
        return _contentAlignment;
    }

    /**
     * Default HorizontalStack constructor.
     */
    public HorizontalStack() {
        setItemName("HorizontalStack_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle(HorizontalStack.class));
        isFocusable = false;
    }

    // overrides
    @Override
    protected boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    /**
     * Adding item to the HorizontalStack.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     */
    @Override
    public void addItem(IBaseItem item) {
        super.addItem(item);
        updateLayout();
    }

    /**
     * Inserting item to the HorizontalStack container. If the count of container
     * elements is less than the index, then the element is added to the end of the
     * list.
     * 
     * @param item  Item as com.spvessel.spacevil.Core.IBaseItem.
     * @param index Index of insertion.
     */
    @Override
    public void insertItem(IBaseItem item, int index) {
        super.insertItem(item, index);
        updateLayout();
    }

    /**
     * Removing the specified item from the HorizontalStack container.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(IBaseItem item) {
        boolean result = super.removeItem(item);
        if (result)
            updateLayout();
        return result;
    }

    /**
     * Setting HorizontalStack width. If the value is greater/less than the
     * maximum/minimum value of the width, then the width becomes equal to the
     * maximum/minimum value.
     * 
     * @param width Width of the HorizontalStack.
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    /**
     * Setting X coordinate of the left-top corner of the HorizontalStack.
     * 
     * @param x X position of the left-top corner.
     */
    @Override
    public void setX(int x) {
        super.setX(x);
        updateLayout();
    }

    /**
     * Updating all children positions (implementation of
     * com.spvessel.spacevil.Core.IHLayout).
     */
    public void updateLayout() {
        int total_space = getWidth() - getPadding().left - getPadding().right;
        int free_space = total_space;
        int fixed_count = 0;
        int expanded_count = 0;

        List<Integer> maxWidthExpands = new LinkedList<>();
        List<IBaseItem> itemList = getItems();

        for (IBaseItem child : itemList) {
            if (child.isVisible()) {
                if (child.getWidthPolicy() == SizePolicy.Fixed) {
                    fixed_count++;
                    free_space -= (child.getWidth() + child.getMargin().left + child.getMargin().right);//
                } else {
                    if (child.getMaxWidth() < total_space) {
                        maxWidthExpands.add(child.getMaxWidth() + child.getMargin().right + child.getMargin().left);
                    }
                    expanded_count++;
                }
            }
        }
        free_space -= getSpacing().horizontal * ((expanded_count + fixed_count) - 1);

        int width_for_expanded = 1;
        // if (expanded_count > 0 && free_space > expanded_count)
        // width_for_expanded = free_space / expanded_count;
        Collections.sort(maxWidthExpands);

        while (true) {
            if (expanded_count == 0)
                break;

            if (free_space > expanded_count)
                width_for_expanded = free_space / expanded_count;

            if (width_for_expanded <= 1 || maxWidthExpands.size() == 0) {
                // width_for_expanded = 1;
                break;
            }

            if (width_for_expanded > maxWidthExpands.get(0)) {
                while (maxWidthExpands.size() > 0 && width_for_expanded > maxWidthExpands.get(0)) {
                    free_space -= maxWidthExpands.get(0);
                    maxWidthExpands.remove(0);
                    expanded_count--;
                }
            } else {
                break;
            }
            // width_for_expanded = widthForExpand(free_space, expanded_count);
        }

        int offset = 0;
        int startX = getX() + getPadding().left;
        boolean isFirstExpand = false;
        int diff = (free_space - width_for_expanded * expanded_count);
        if (expanded_count != 0 && diff > 0) {
            isFirstExpand = true;
        } // else if (diff < 0) {
          // System.out.println("Something is wrong with diff " + diff + " " + free_space
          // + " " + total_space);
          // }

        // if (expanded_count == 0)
        // System.out.println(getParent().getItemName() + " " + _contentAlignment);

        if (expanded_count > 0 || _contentAlignment.equals(ItemAlignment.Left)) {
            for (IBaseItem child : itemList) {
                if (child.isVisible()) {
                    child.setX(startX + offset + child.getMargin().left);
                    if (child.getWidthPolicy() == SizePolicy.Expand) {
                        if (width_for_expanded - child.getMargin().left - child.getMargin().right < child
                                .getMaxWidth()) {
                            child.setWidth(width_for_expanded - child.getMargin().left - child.getMargin().right);
                        } else {
                            // expanded_count--;
                            // free_space -= (child.getWidth() + child.getMargin().left +
                            // child.getMargin().right);//
                            // width_for_expanded = 1;
                            // if (expanded_count > 0 && free_space > expanded_count)
                            // width_for_expanded = free_space / expanded_count;
                            child.setWidth(child.getMaxWidth());
                        }

                        if (isFirstExpand) {
                            if (child.getWidth() + diff < child.getMaxWidth()) {
                                child.setWidth(child.getWidth() + diff);
                                isFirstExpand = false;
                            }
                        }
                    }
                    offset += child.getWidth() + getSpacing().horizontal + child.getMargin().left
                            + child.getMargin().right;//
                }
                // refactor
                child.setConfines();
            }
        } else {
            if (_contentAlignment.equals(ItemAlignment.Right)) {
                for (IBaseItem child : itemList) {
                    if (child.isVisible()) {
                        child.setX(startX + offset + child.getMargin().left + free_space);//
                        offset += child.getWidth() + getSpacing().horizontal + child.getMargin().left
                                + child.getMargin().right;//
                        // System.out.print(child.getWidth() + ", " + child.getMaxWidth() + " | ");

                        if (child.getWidthPolicy() == SizePolicy.Expand)
                            child.setWidth(child.getMaxWidth());
                    }
                    child.setConfines();
                }
                // System.out.println();
            } else if (_contentAlignment.equals(ItemAlignment.HCenter)) {
                for (IBaseItem child : itemList) {
                    if (child.isVisible()) {
                        child.setX(startX + offset + child.getMargin().left + free_space / 2);//
                        offset += child.getWidth() + getSpacing().horizontal + child.getMargin().left
                                + child.getMargin().right;//

                        if (child.getWidthPolicy() == SizePolicy.Expand)
                            child.setWidth(child.getMaxWidth());
                    }
                    child.setConfines();
                }
            }
        }
    }
}