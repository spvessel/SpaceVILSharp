package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceVLayout;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

/**
 * VerticalStack is a class that represents a line type container (vertical
 * version). VerticalStack groups items one after another using content
 * alignment, margins, paddings, sizes and size policies. VerticalStack
 * implements com.spvessel.spacevil.Core.InterfaceVLayout. By default ability to
 * get focus is disabled.
 * <p>
 * VerticalStack cannot receive any events, so VerticalStack is always in the
 * com.spvessel.spacevil.Flags.ItemStateType.BASE state.
 */
public class VerticalStack extends Prototype implements InterfaceVLayout {
    private static int count = 0;

    private ItemAlignment _contentAlignment = ItemAlignment.TOP;

    /**
     * Setting content alignment within VerticalStack area.
     * <p>
     * Supports only: ItemAlignment.TOP, ItemAlignment.VCENTER,
     * ItemAlignment.BOTTOM.
     * <p>
     * Default: ItemAlignment.TOP.
     * 
     * @param alignment Content alignment as
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setContentAlignment(ItemAlignment alignment) {
        if (alignment == ItemAlignment.TOP || alignment == ItemAlignment.VCENTER || alignment == ItemAlignment.BOTTOM)
            _contentAlignment = alignment;
    }

    /**
     * Getting current content alignment.
     * <p>
     * Can be: ItemAlignment.TOP, ItemAlignment.VCENTER, ItemAlignment.BOTTOM.
     * 
     * @return Content alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public ItemAlignment getContentAlignment() {
        return _contentAlignment;
    }

    /**
     * Default VerticalStack constructor.
     */
    public VerticalStack() {
        setItemName("verticalStack_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle(VerticalStack.class));
        isFocusable = false;
    }

    @Override
    protected boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    /**
     * Adding item to the VerticalStack.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        super.addItem(item);
        updateLayout();
    }

    /**
     * Inserting item to the VerticalStack container. If the number of container
     * elements is less than the index, then the element is added to the end of the
     * list.
     * 
     * @param item  Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @param index Index of insertion.
     */
    @Override
    public void insertItem(InterfaceBaseItem item, int index) {
        super.insertItem(item, index);
        updateLayout();
    }

    /**
     * Removing the specified item from the VerticalStack container.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        boolean result = super.removeItem(item);
        if (result)
            updateLayout();
        return result;
    }

    /**
     * Setting VerticalStack height. If the value is greater/less than the
     * maximum/minimum value of the height, then the height becomes equal to the
     * maximum/minimum value.
     * 
     * @param height Height of the VerticalStack.
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updateLayout();
    }

    /**
     * Setting Y coordinate of the left-top corner of the VerticalStack.
     * 
     * @param y Y position of the left-top corner.
     */
    @Override
    public void setY(int y) {
        super.setY(y);
        updateLayout();
    }

    /**
     * Updating all children positions (implementation of
     * com.spvessel.spacevil.Core.InterfaceVLayout).
     */
    public void updateLayout() {
        int total_space = getHeight() - getPadding().top - getPadding().bottom;
        int free_space = total_space;
        int fixed_count = 0;
        int expanded_count = 0;

        List<Integer> maxHeightExpands = new LinkedList<>();

        for (InterfaceBaseItem child : getItems()) {
            if (child.isVisible()) {
                if (child.getHeightPolicy() == SizePolicy.FIXED) {
                    fixed_count++;
                    free_space -= (child.getHeight() + child.getMargin().top + child.getMargin().bottom);//
                } else {
                    if (child.getMaxHeight() < total_space) {
                        maxHeightExpands.add(child.getMaxHeight() + child.getMargin().top + child.getMargin().bottom);
                    }
                    expanded_count++;
                }
            }
        }
        free_space -= getSpacing().vertical * ((expanded_count + fixed_count) - 1);

        int height_for_expanded = 1;
        // if (expanded_count > 0 && free_space > expanded_count)
        // height_for_expanded = free_space / expanded_count;

        Collections.sort(maxHeightExpands);

        while (true) {
            if (expanded_count == 0)
                break;

            if (free_space > expanded_count)
                height_for_expanded = free_space / expanded_count;

            if (height_for_expanded <= 1 || maxHeightExpands.size() == 0) {
                break;
            }

            if (height_for_expanded > maxHeightExpands.get(0)) {
                while (maxHeightExpands.size() > 0 && height_for_expanded > maxHeightExpands.get(0)) {
                    free_space -= maxHeightExpands.get(0);
                    maxHeightExpands.remove(0);
                    expanded_count--;
                }
            } else {
                break;
            }
        }

        int offset = 0;
        int startY = getY() + getPadding().top;
        boolean isFirstExpand = false;
        int diff = (free_space - height_for_expanded * expanded_count);
        if (expanded_count != 0 && diff > 0) {
            isFirstExpand = true;
        }

        if (expanded_count > 0 || _contentAlignment.equals(ItemAlignment.TOP)) {
            for (InterfaceBaseItem child : getItems()) {
                if (child.isVisible()) {
                    child.setY(startY + offset + child.getMargin().top);//
                    if (child.getHeightPolicy() == SizePolicy.EXPAND) {
                        if (height_for_expanded - child.getMargin().top - child.getMargin().bottom < child
                                .getMaxHeight()) {
                            child.setHeight(height_for_expanded - child.getMargin().top - child.getMargin().bottom);
                        } else {
                            // expanded_count--;
                            // free_space -= (child.getHeight() + child.getMargin().top +
                            // child.getMargin().bottom);
                            // height_for_expanded = 1;
                            // if (expanded_count > 0 && free_space > expanded_count)
                            // height_for_expanded = free_space / expanded_count;
                            child.setHeight(child.getMaxHeight()); // ?
                        }

                        if (isFirstExpand) {
                            if (child.getHeight() + diff < child.getMaxHeight()) {
                                child.setHeight(child.getHeight() + diff);
                                isFirstExpand = false;
                            }
                        }
                    }
                    offset += child.getHeight() + getSpacing().vertical + child.getMargin().top
                            + child.getMargin().bottom;//
                }
                // refactor
                child.setConfines();
            }
        } else {
            if (_contentAlignment.equals(ItemAlignment.BOTTOM)) {
                for (InterfaceBaseItem child : getItems()) {
                    if (child.isVisible()) {
                        child.setY(startY + offset + child.getMargin().top + free_space);//
                        offset += child.getHeight() + getSpacing().vertical + child.getMargin().top
                                + child.getMargin().bottom;//

                        if (child.getHeightPolicy() == SizePolicy.EXPAND)
                            child.setHeight(child.getMaxHeight());
                    }
                    child.setConfines();
                }
            } else if (_contentAlignment.equals(ItemAlignment.VCENTER)) {
                for (InterfaceBaseItem child : getItems()) {
                    if (child.isVisible()) {
                        child.setY(startY + offset + child.getMargin().top + free_space / 2);//
                        offset += child.getHeight() + getSpacing().vertical + child.getMargin().top
                                + child.getMargin().bottom;//

                        if (child.getHeightPolicy() == SizePolicy.EXPAND)
                            child.setHeight(child.getMaxHeight());
                    }
                    child.setConfines();
                }
            }
        }

    }
}