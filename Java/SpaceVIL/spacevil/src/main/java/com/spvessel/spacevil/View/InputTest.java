package com.spvessel.spacevil.View;

import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.MSAA;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;
import com.spvessel.spacevil.TextArea;
import com.spvessel.spacevil.*;

public class InputTest extends ActiveWindow {
    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout("InputTest", "InputTest", 250, 400, true);
        // Handler.setAntiAliasingQuality(MSAA.MSAA_8X);
        setHandler(Handler);

        Handler.setMinSize(50, 100);
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
        password.setSubstrateText("Enter a password...");

        TextEdit te = new TextEdit();
        // te.setText("TextZaranee");
        te.setTextAlignment(ItemAlignment.RIGHT);
        //te.getSelectionArea().setBackground(Color.green);
        te.setSubstrateText("Write some text");

        TextArea tb = new TextArea();
        tb.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        tb.setHScrollBarVisible(ScrollBarVisibility.ALWAYS);
        tb.setText("123qwe");
//        tb.setPadding(15, 0, 15, 0);
        // tb.setMargin(new Indents(50, 30, 30, 30));
        // tb.setTextMargin(new Indents(50, 30, 30, 30));

        SpinItem sp = new SpinItem();
        sp.setParameters(1, -5.5, 7, 0.51);

        layout.addItem(password);
        layout.addItem(te);
        layout.addItem(tb);

        ButtonCore bc = new ButtonCore("pizdec");
        bc.setSize(150,30);
        bc.eventMouseClick.add((sender, args) -> {
            String text = tb.getText();
            tb.setText(text + "\n456\n123");
        });

        layout.addItem(bc);

        layout.addItem(sp);
    }
}