package com.spvessel.spacevil.View;

import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.TextArea;
import com.spvessel.spacevil.Common.CommonService;
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

        eventKeyPress.add((sender, args) -> {
            // if (args.key == KeyCode.V)
            // CommonService.setClipboardString("SetClipBoardString");
            // if (args.key == KeyCode.C)
            // System.out.println(CommonService.getClipboardString());
            // if (args.key == KeyCode.F)
            // System.out.println(WindowsBox.getCurrentFocusedWindow().getWindowName());
            if (args.key == KeyCode.F11)
                toggleFullScreen();
        });

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
        // tb.setEditable(false);
        tb.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        tb.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        tb.setText("123\nqwe sdfsdf;l!\nk(sdfsdf)sdf\nsdf_ sdfs\ndfs_dff+gh");
        tb.setWidth(300);
        tb.setHeight(500);
        tb.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        tb.eventKeyPress.add((sender, args) -> {
            if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.S) {
                System.out.println(args.mods.size() + " mods: " + args.mods + " code: " + args.key);
                tb.appendText("appended text line...\n");
            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.R) {
                System.out.println(args.mods.size() + " mods: " + args.mods + " code: " + args.key);
                tb.setText("SetText...");
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
        ButtonCore bc1 = new ButtonCore("orNot");
        bc1.setSize(150, 30);

        // tb.setEditable(false);

        layout.addItems(bc, bc1, sp);

        Label tl = new Label();
        tl.setBackground(255, 255, 255, 100);
        // tl.setFontSize(17);
        tl.setForeground(Color.WHITE);
        tl.setTextAlignment(ItemAlignment.TOP);
        // System.out.println(tl.getTextWidth());
        tl.setText("sdasdasd");

        layout.addItem(tl);

        bc.eventMouseClick.add((sender, args) -> {
            System.out.print(getX() + " ");
            setHeight(getHeight() + 10);
            System.out.println(getX());
        });

        bc1.eventMouseClick.add((sender, args) -> {
            System.out.print(getX() + " ");
            setHeight(getHeight() - 10);
            System.out.println(getX());
        });

        Label label = new Label("1234567890");
        // System.out.println(label.getTextWidth());
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