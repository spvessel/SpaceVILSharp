package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceHLayout;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

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

        for (InterfaceBaseItem child : getItems()) {
            if (child.isVisible()) {
                if (child.getWidthPolicy() == SizePolicy.FIXED) {
                    fixed_count++;
                    free_space -= (child.getWidth() + child.getMargin().left + child.getMargin().right);//
                } else {
                    expanded_count++;
                }
            }
        }
        free_space -= getSpacing().horizontal * ((expanded_count + fixed_count) - 1);

        int width_for_expanded = 1;
        if (expanded_count > 0 && free_space > expanded_count)
            width_for_expanded = free_space / expanded_count;

        int offset = 0;
        int startX = getX() + getPadding().left;

        if (expanded_count > 0 || _contentAlignment.equals(ItemAlignment.LEFT)) {
            for (InterfaceBaseItem child : getItems()) {
                if (child.isVisible()) {
                    child.setX(startX + offset + child.getMargin().left);//
                    if (child.getWidthPolicy() == SizePolicy.EXPAND) {
                        if (width_for_expanded - child.getMargin().left - child.getMargin().right < child.getMaxWidth())
                            child.setWidth(width_for_expanded - child.getMargin().left - child.getMargin().right);
                        else {
                            expanded_count--;
                            free_space -= (child.getWidth() + child.getMargin().left + child.getMargin().right);//
                            width_for_expanded = 1;
                            if (expanded_count > 0 && free_space > expanded_count)
                                width_for_expanded = free_space / expanded_count;
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
                    }
                    child.setConfines();
                }
            } else if (_contentAlignment.equals(ItemAlignment.HCENTER)) {
                for (InterfaceBaseItem child : getItems()) {
                    if (child.isVisible()) {
                        child.setX(startX + offset + child.getMargin().left + free_space / 2);//
                        offset += child.getWidth() + getSpacing().horizontal + child.getMargin().left
                                + child.getMargin().right;//
                    }
                    child.setConfines();
                }
            }
        }
    }
}