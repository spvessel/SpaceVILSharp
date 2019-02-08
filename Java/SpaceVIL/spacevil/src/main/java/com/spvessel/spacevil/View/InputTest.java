package com.spvessel.spacevil.View;

import com.spvessel.spacevil.Flags.HorizontalDirection;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.MSAA;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.TextArea;
import com.spvessel.spacevil.*;

public class InputTest extends ActiveWindow {
    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout("InputTest", "InputTest", 700, 400, true);
        // Handler.setAntiAliasingQuality(MSAA.MSAA_8X);
        setHandler(Handler);

        Handler.setMinSize(50, 100);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

        TitleBar title = new TitleBar("Input Test");
        title.setAlignment(ItemAlignment.BOTTOM, ItemAlignment.LEFT);
        title.direction = HorizontalDirection.FROM_RIGHT_TO_LEFT;
        Handler.addItem(title);

        VerticalStack layout = new VerticalStack();
        ///////////////////////////////////////////////////////////////////////
        layout.setHeight(100);
        layout.setHeightPolicy(SizePolicy.FIXED);
        layout.setAlignment(ItemAlignment.BOTTOM);
        ///////////////////////////////////////////////////////////////////////

        layout.setMargin(0, 330, 0, 30);
        layout.setBackground(70, 70, 70);
        layout.setSpacing(6, 30);
        layout.setPadding(2, 2, 2, 2);
        Handler.addItem(layout);

        PasswordLine password = new PasswordLine();
        password.setSubstrateText("Enter a password...");

        TextEdit te = new TextEdit();
        // te.setText("TextZaranee");
        te.setTextAlignment(ItemAlignment.RIGHT);
        // te.getSelectionArea().setBackground(Color.green);
        te.setSubstrateText("Write some text");
        // te.setMargin(0,0,150,0);
        // te.setFontSize(10);
        te.setWidth(300);
        te.setWidthPolicy(SizePolicy.FIXED);

        TextArea tb = new TextArea();
        tb.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        tb.setHScrollBarVisible(ScrollBarVisibility.ALWAYS);
        tb.setText("123qwe");
        tb.setWidth(300);
        tb.setWidthPolicy(SizePolicy.FIXED);
        // tb.setPadding(15, 0, 15, 0);
        // tb.setMargin(new Indents(50, 30, 30, 30));
        // tb.setTextMargin(new Indents(50, 30, 30, 30));

        SpinItem sp = new SpinItem();
        sp.setParameters(1, -5.5, 7, 0.51);

        // layout.addItem(password);
        layout.addItem(te);
        // layout.addItem(tb);

        ButtonCore bc = new ButtonCore("pizdec");
        bc.setSize(150, 30);
        bc.eventMouseClick.add((sender, args) -> {
            String s = "    qwerty\n// tb.setTextMargin(new Indents(50, 30, 30, 30));// tb.setTextMargin(new Indents(50, 30, 30, 30));";
            String text = ""; // tb.getText();
            // s = tb.getText();
            tb.setText(text + s);
            System.out.println((s.toCharArray()[0] == " ".charAt(0)) + " " + s.toCharArray()[0]);
            System.out.println((s.toCharArray()[1] == " ".charAt(0)) + " " + s.toCharArray()[1]);
            System.out.println((s.toCharArray()[2] == " ".charAt(0)) + " " + s.toCharArray()[2]);
            System.out.println((s.toCharArray()[3] == " ".charAt(0)) + " " + s.toCharArray()[3]);
            System.out.println((s.toCharArray()[4] == " ".charAt(0)) + " " + s.toCharArray()[4]);
        });

        // layout.addItem(bc);

        // layout.addItem(sp);
    }
}