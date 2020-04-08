package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.List;

/**
 * An interface that describes the alignment of the item and size policy of the item.
 * <p> This interface is part of com.spvessel.spacevil.Core.InterfaceBaseItem.
 */
public interface InterfaceBehavior{
    /**
     * Setting an alignment of an item's shape relative to its container. 
     * Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
     * @param alignment Alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setAlignment(ItemAlignment... alignment);

    /**
     * Getting an alignment of an item's shape relative to its container. 
     * @return Alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public List<ItemAlignment> getAlignment();

    /**
     * Setting width policy of an item's shape. Can be Fixed (shape not changes its size) 
     * or Expand (shape is stretched to all available space).
     * @param policy Width policy as com.spvessel.spacevil.Flags.SizePolicy.
     */
    public void setWidthPolicy(SizePolicy policy);

    /**
     * Getting width policy of an item's shape.Can be Fixed (shape not changes its size) 
     * or Expand (shape is stretched to all available space).
     * @return Width policy as com.spvessel.spacevil.Flags.SizePolicy.
     */
    public SizePolicy getWidthPolicy();

    /**
     * Setting height policy of an item's shape. Can be Fixed (shape not changes its size) 
     * or Expand (shape is stretched to all available space).
     * @param policy Height policy as com.spvessel.spacevil.Flags.SizePolicy.
     */
    public void setHeightPolicy(SizePolicy policy);

    /**
     * Getting height policy of an item's shape.Can be Fixed (shape not changes its size) 
     * or Expand (shape is stretched to all available space).
     * @return Height policy as com.spvessel.spacevil.Flags.SizePolicy.
     */
    public SizePolicy getHeightPolicy();
}
