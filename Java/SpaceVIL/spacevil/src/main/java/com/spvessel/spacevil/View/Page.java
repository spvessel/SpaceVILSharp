package com.spvessel.spacevil.View;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.WindowLayout;
import com.spvessel.spacevil.Flags.ItemAlignment;

public class Page {
    public Page() {
    }
    
    public void PrintPage(ActiveWindow window)
    {
        WindowLayout Handler = new WindowLayout("PageTest", "PageTest", 1000, 800, true);
        window.setHandler(Handler);
        TitleBar title = new TitleBar("PageTest");
        VerticalStack layout = new VerticalStack();
        layout.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        layout.setMargin(0, title.getHeight(), 0, 0);
        layout.setPadding(6, 6, 6, 6);
        layout.setSpacing(0, 10);
        layout.setBackground(255, 255, 255, 20);
        Handler.addItem(layout);
        Handler.addItem(title);
    }
}