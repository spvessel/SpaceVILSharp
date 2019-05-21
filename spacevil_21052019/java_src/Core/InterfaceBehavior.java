package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.List;

public interface InterfaceBehavior{
    
    void setAlignment(ItemAlignment... alignment);
    List<ItemAlignment> getAlignment();

    void setWidthPolicy(SizePolicy policy);
    SizePolicy getWidthPolicy();


    void setHeightPolicy(SizePolicy policy);
    SizePolicy getHeightPolicy();
}
