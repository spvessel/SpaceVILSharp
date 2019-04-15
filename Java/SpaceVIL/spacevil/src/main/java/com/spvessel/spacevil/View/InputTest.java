package com.spvessel.spacevil.View;

import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.TextArea;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.*;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputTest extends ActiveWindow {
    @Override
    public void initWindow() {
        isBorderHidden = true;
        setSize(700, 550);
        setWindowName("InputTest");
        setWindowTitle("InputTest");

        setMinSize(50, 100);
        setBackground(45, 45, 45);
        setPadding(2, 2, 2, 2);

        TitleBar title = new TitleBar("Input Test");
        // title.setAlignment(ItemAlignment.BOTTOM, ItemAlignment.LEFT);
        // title.direction = HorizontalDirection.FROM_RIGHT_TO_LEFT;
        addItem(title);

        VerticalStack layout = new VerticalStack();
        ///////////////////////////////////////////////////////////////////////
        layout.setHeight(100);
        // layout.setHeightPolicy(SizePolicy.FIXED);
        // layout.setAlignment(ItemAlignment.BOTTOM);
        ///////////////////////////////////////////////////////////////////////

        layout.setMargin(0, 30, 0, 30);
        layout.setBackground(70, 70, 70);
        layout.setSpacing(6, 30);
        layout.setPadding(2, 2, 2, 2);
        addItem(layout);

        PasswordLine password = new PasswordLine();
        password.setSubstrateText("Enter a password...");
        password.setTextAlignment(ItemAlignment.RIGHT);

        TextEdit te = new TextEdit();
        // te.setText("TextZaranee");
        te.setTextAlignment(ItemAlignment.RIGHT);
        // te.getSelectionArea().setBackground(Color.green);
        te.setSubstrateText("Write some text");
        // te.setMargin(0,0,150,0);
        // te.setFontSize(10);
        te.setWidth(300);
        te.setWidthPolicy(SizePolicy.EXPAND);

        TextArea tb = new TextArea();
        tb.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        tb.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        tb.setText("123qwe sdfsdf;l!k(sdfsdf)sdfsdf_ sdfsdfs_dff+gh");
        tb.setWidth(300);
        tb.setHeight(300);
        tb.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        tb.eventKeyPress.add((sender, args) -> {
            if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.S) {
                System.out.println(args.mods.size() + " mods: " + args.mods + " code: " + args.key);
            }
        });
        // tb.setPadding(15, 0, 15, 0);
        // tb.setMargin(new Indents(50, 30, 30, 30));
        // tb.setTextMargin(new Indents(50, 30, 30, 30));
        // tb.onTextChanged.add(() -> System.out.println("text changed"));

        SpinItem sp = new SpinItem();
        sp.setParameters(1, -5.5, 7, 0.51);

        layout.addItem(password);
        layout.addItem(te);
        layout.addItem(tb);
        tb.setStyle(Style.getTextAreaStyle());

        ButtonCore bc = new ButtonCore("pizdec");
        bc.setSize(150, 30);

        // tb.setEditable(false);

        layout.addItem(bc);

        layout.addItem(sp);

        Label tl = new Label();
        tl.setBackground(255, 255, 255, 100);
        // tl.setFontSize(17);
        tl.setForeground(Color.WHITE);
        tl.setTextAlignment(ItemAlignment.TOP);
        // System.out.println(tl.getTextWidth());
        tl.setText("sdasdasd");

        layout.addItem(tl);

        bc.eventMouseClick.add((sender, args) -> {
            // String s = " qwerty\n// tb.setTextMargin(new Indents(50, 30, 30, 30));//
            // tb.setTextMargin(new Indents(50, 30, 30, 30));";
            // String text = ""; // tb.getText();
            // // s = tb.getText();
            // tb.setText(text + s);
            // System.out.println((s.toCharArray()[0] == " ".charAt(0)) + " " +
            // s.toCharArray()[0]);
            // System.out.println((s.toCharArray()[1] == " ".charAt(0)) + " " +
            // s.toCharArray()[1]);
            // System.out.println((s.toCharArray()[2] == " ".charAt(0)) + " " +
            // s.toCharArray()[2]);
            // System.out.println((s.toCharArray()[3] == " ".charAt(0)) + " " +
            // s.toCharArray()[3]);
            // System.out.println((s.toCharArray()[4] == " ".charAt(0)) + " " +
            // s.toCharArray()[4]);

            // tb.pasteText("12345678");
            // te.pasteText("text edit text");
            // this.setWindowTitle(te.getSelectedText());
            // tb.setFocused(false);
            tb.setText("package com.spvessel.spacevil;\n" +

                    "        // HBar\n" + "        hScrollBar.isFocusable = false;\n"
                    + "        hScrollBar.setVisible(true);\n"
                    + "        hScrollBar.setItemName(getItemName() + \"_\" + hScrollBar.getItemName());\n" + "\n"
                    + "        // Area\n"
                    + "        // _area.setItemName(getItemName() + \"_\" + _area.getItemName());\n"
                    + "        // _area.setSpacing(0, 5);\n" + "    }\n" + "\n" + "    public TextArea(String text) {\n"
                    + "        this();\n" + "        setText(text);\n" + "    }\n" + "\n"
                    + "    private long v_size = 0;\n" + "    private long h_size = 0;\n" + "\n"
                    + "    private void updateVListArea() {\n"
                    + "        // ve EventCommonMethod onTextChanged = new EventCommonMethod()");
            // tb.rewindText();
            // tb.setEditable(!tb.isEditable());
            // tb.setEditable(false);
            // tb.clear();
            // tb.setFocus();
            // tb.setEditable(true);
        });

        Label label = new Label("1234567890");
//        System.out.println(label.getTextWidth());
        layout.addItem(label);

        eventDrop.add((sender, args) -> {

            if (args.count > 0) {
                System.out.println(args.item.getItemName());
                String line = null;
                String FilePath = args.paths.get(0);
                StringBuilder sb = new StringBuilder();
                try {
                    InputStreamReader stream = new InputStreamReader(new FileInputStream(FilePath), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(stream);
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tb.setText(sb.toString());
            }
        });
    }
}