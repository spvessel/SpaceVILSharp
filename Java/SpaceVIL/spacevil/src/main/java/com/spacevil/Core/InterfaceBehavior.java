package com.spacevil.Core;

import java.util.List;

public interface InterfaceBehavior{
    
    void setAlignment(com.spacevil.Flags.ItemAlignment... alignment);
    List<com.spacevil.Flags.ItemAlignment> getAlignment();

    void setWidthPolicy(com.spacevil.Flags.SizePolicy policy);
    com.spacevil.Flags.SizePolicy getWidthPolicy();


    void setHeightPolicy(com.spacevil.Flags.SizePolicy policy);
    com.spacevil.Flags.SizePolicy getHeightPolicy();
}
