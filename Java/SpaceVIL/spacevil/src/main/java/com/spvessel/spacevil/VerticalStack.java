package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceVLayout;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

public class VerticalStack extends Prototype implements InterfaceVLayout {
    private static int count = 0;

    private ItemAlignment _contentAlignment = ItemAlignment.TOP;

    public void setContentAlignment(ItemAlignment alignment) {
        if (alignment == ItemAlignment.TOP || alignment == ItemAlignment.VCENTER || alignment == ItemAlignment.BOTTOM)
            _contentAlignment = alignment;
    }

    public ItemAlignment getContentAlignment() {
        return _contentAlignment;
    }

    /**
     * Constructs a VerticalStack
     */
    public VerticalStack() {
        setItemName("verticalStack_" + count);
        count++;
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.VerticalStack"));
        setStyle(DefaultsService.getDefaultStyle(VerticalStack.class));
        isFocusable = false;
    }

    // overrides
    @Override
    protected boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    /**
     * Add item to the VerticalStack
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        super.addItem(item);
        updateLayout();
    }

    @Override
    public void insertItem(InterfaceBaseItem item, int index) {
        super.insertItem(item, index);
        updateLayout();
    }

    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        boolean result = super.removeItem(item);
        if (result)
            updateLayout();
        return result;
    }

    /**
     * Set height of the VerticalStack
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updateLayout();
    }

    /**
     * Set Y position of the VerticalStack
     */
    @Override
    public void setY(int _y) {
        super.setY(_y);
        updateLayout();
    }

    /**
     * Update all children and VerticalStack sizes and positions according to
     * confines
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
                        if (height_for_expanded - child.getMargin().top - child.getMargin().bottom < child.getMaxHeight()) {
                            child.setHeight(height_for_expanded - child.getMargin().top - child.getMargin().bottom);
                        } else {
                            // expanded_count--;
                            // free_space -= (child.getHeight() + child.getMargin().top + child.getMargin().bottom);
                            // height_for_expanded = 1;
                            // if (expanded_count > 0 && free_space > expanded_count)
                            //     height_for_expanded = free_space / expanded_count;
                            child.setHeight(child.getMaxHeight()); //?
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