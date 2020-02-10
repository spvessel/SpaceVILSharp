package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.List;

public interface InterfaceBehavior{
    
    public void setAlignment(ItemAlignment... alignment);
    public List<ItemAlignment> getAlignment();

    public void setWidthPolicy(SizePolicy policy);
    public SizePolicy getWidthPolicy();

    public void setHeightPolicy(SizePolicy policy);
    public SizePolicy getHeightPolicy();
}
