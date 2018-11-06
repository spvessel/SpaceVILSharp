package com.spvessel;

import java.util.List;

public interface InterfaceBehavior{
    
    void setAlignment(ItemAlignment... alignment);
    List<ItemAlignment> getAlignment();

    void setWidthPolicy(SizePolicy policy);
    SizePolicy getWidthPolicy();


    void setHeightPolicy(SizePolicy policy);
    SizePolicy getHeightPolicy();
}
