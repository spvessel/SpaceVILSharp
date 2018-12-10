package com.spvessel.spacevil.View;

import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;
import com.spvessel.spacevil.TextArea;
import com.spvessel.spacevil.*;

public class InputTest extends ActiveWindow {
    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "InputTest", "InputTest", 800, 400, true);
        setHandler(Handler);

        Handler.setMinSize(500, 100);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

        TitleBar title = new TitleBar("Input Test");
        Handler.addItem(title);

        VerticalStack layout = new VerticalStack();
        layout.setMargin(0, 30, 0, 0);
        layout.setBackground(70, 70, 70);
        layout.setSpacing(6, 30);
        layout.setPadding(2, 2, 2, 2);
        Handler.addItem(layout);

        PasswordLine password = new PasswordLine();

        TextEdit te = new TextEdit();
        te.setTextAlignment(ItemAlignment.RIGHT);
        //te.getSelectionArea().setBackground(Color.green);

        TextArea tb = new TextArea();
        tb.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        tb.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);

        // tb.setMargin(new Indents(50, 30, 30, 30));
        // tb.setTextMargin(new Indents(50, 30, 30, 30));

        SpinItem sp = new SpinItem();
        sp.setParameters(1, -5.5, 7, 0.51);

        layout.addItem(password);
        layout.addItem(te);
        layout.addItem(tb);
        layout.addItem(sp);
    }
}