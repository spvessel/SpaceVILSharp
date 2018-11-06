package com.spvessel;

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
        
        TextArea tb = new TextArea();
        // tb.setMargin(new Indents(50, 30, 30, 30));
        // tb.setTextMargin(new Indents(50, 30, 30, 30));

        layout.addItem(password);
        layout.addItem(te);
        layout.addItem(tb);
    }
}