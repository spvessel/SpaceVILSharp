package com.spvessel.Cores;

import java.util.List;
import com.spvessel.Flags.*;

public interface InterfaceBehavior{
    
    void setAlignment(ItemAlignment... alignment);
    List<ItemAlignment> getAlignment();

    void setWidthPolicy(SizePolicy policy);
    SizePolicy getWidthPolicy();


    void setHeightPolicy(SizePolicy policy);
    SizePolicy getHeightPolicy();
}
