package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceHLayout;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HorizontalStack extends Prototype implements InterfaceHLayout {
    private static int count = 0;

    private ItemAlignment _contentAlignment = ItemAlignment.LEFT;

    public void setContentAlignment(ItemAlignment alignment) {
        if (alignment == ItemAlignment.LEFT || alignment == ItemAlignment.HCENTER || alignment == ItemAlignment.RIGHT)
            _contentAlignment = alignment;
    }

    public ItemAlignment getContentAlignment() {
        return _contentAlignment;
    }

    /**
     * Constructs a HorizontalStack
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
     * Add item to the HorizontalStack
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
     * Set width of the HorizontalStack
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    /**
     * Set X position of the HorizontalStack
     */
    @Override
    public void setX(int _x) {
        super.setX(_x);
        updateLayout();
    }

    // @Override
    // public void setY(int _y) {
    // super.setY(_y);
    // updateLayout();
    // }

    /**
     * Update all children and HorizontalStack sizes and positions according to
     * confines
     */
    public void updateLayout() {
        int total_space = getWidth() - getPadding().left - getPadding().right;
        int free_space = total_space;
        int fixed_count = 0;
        int expanded_count = 0;

        List<Integer> maxWidthExpands = new LinkedList<>();

        for (InterfaceBaseItem child : getItems()) {
            if (child.isVisible()) {
                if (child.getWidthPolicy() == SizePolicy.FIXED) {
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
//        if (expanded_count > 0 && free_space > expanded_count)
//            width_for_expanded = free_space / expanded_count;
        Collections.sort(maxWidthExpands);

        while (true) {
            if (expanded_count == 0)
                break;

            if (free_space > expanded_count)
                width_for_expanded = free_space / expanded_count;

            if (width_for_expanded <= 1 || maxWidthExpands.size() == 0) {
//                width_for_expanded = 1;
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
//            width_for_expanded = widthForExpand(free_space, expanded_count);
        }


        int offset = 0;
        int startX = getX() + getPadding().left;
        boolean isFirstExpand = false;
        int diff = (free_space - width_for_expanded * expanded_count);
        if (expanded_count != 0 && diff > 0) {
            isFirstExpand = true;
        } //else if (diff < 0) {
//            System.out.println("Something is wrong with diff " + diff + " " + free_space + " " + total_space);
//        }

//        if (expanded_count == 0)
//            System.out.println(getParent().getItemName() + " " + _contentAlignment);

        if (expanded_count > 0 || _contentAlignment.equals(ItemAlignment.LEFT)) {
            for (InterfaceBaseItem child : getItems()) {
                if (child.isVisible()) {
                    child.setX(startX + offset + child.getMargin().left);//
                    if (child.getWidthPolicy() == SizePolicy.EXPAND) {
                        if (width_for_expanded - child.getMargin().left - child.getMargin().right < child.getMaxWidth()) {
                            child.setWidth(width_for_expanded - child.getMargin().left - child.getMargin().right);
                        }
                        else {
//                            expanded_count--;
//                            free_space -= (child.getWidth() + child.getMargin().left + child.getMargin().right);//
//                            width_for_expanded = 1;
//                            if (expanded_count > 0 && free_space > expanded_count)
//                                width_for_expanded = free_space / expanded_count;
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
            if (_contentAlignment.equals(ItemAlignment.RIGHT)) {
                for (InterfaceBaseItem child : getItems()) {
                    if (child.isVisible()) {
                        child.setX(startX + offset + child.getMargin().left + free_space);//
                        offset += child.getWidth() + getSpacing().horizontal + child.getMargin().left
                                + child.getMargin().right;//
//                        System.out.print(child.getWidth() + ", " + child.getMaxWidth() + " | ");

                        if (child.getWidthPolicy() == SizePolicy.EXPAND)
                            child.setWidth(child.getMaxWidth());
                    }
                    child.setConfines();
                }
//                System.out.println();
            } else if (_contentAlignment.equals(ItemAlignment.HCENTER)) {
                for (InterfaceBaseItem child : getItems()) {
                    if (child.isVisible()) {
                        child.setX(startX + offset + child.getMargin().left + free_space / 2);//
                        offset += child.getWidth() + getSpacing().horizontal + child.getMargin().left
                                + child.getMargin().right;//

                        if (child.getWidthPolicy() == SizePolicy.EXPAND)
                            child.setWidth(child.getMaxWidth());
                    }
                    child.setConfines();
                }
            }
        }
    }
}