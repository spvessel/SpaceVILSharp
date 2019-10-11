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
    Label _infoOutput;

    @Override
    public void initWindow() {
        setSize(700, 550);
        setWindowName("InputTest");
        setWindowTitle("InputTest");

        setMinSize(50, 100);
        setBackground(45, 45, 45);
        setPadding(0, 0, 0, 0);

        _infoOutput = new Label("0 lines : 0 characters");
        _infoOutput.setHeight(25);
        _infoOutput.setHeightPolicy(SizePolicy.FIXED);
        _infoOutput.setBackground(80, 80, 80);
        _infoOutput.setForeground(210, 210, 210);
        _infoOutput.setFontSize(18);
        _infoOutput.setFontStyle(Font.BOLD);
        _infoOutput.setAlignment(ItemAlignment.BOTTOM);
        _infoOutput.setTextAlignment(ItemAlignment.HCENTER, ItemAlignment.BOTTOM);
        _infoOutput.setPadding(0, 0, 0, 3);
        addItem(_infoOutput);

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

        VerticalStack layout = new VerticalStack();
        ///////////////////////////////////////////////////////////////////////
        //        layout.setHeight(100);
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

//        TextArea tb = new TextArea();
        TextView tb = new TextView();
        tb.setMargin(2, 2, 2, 27);
        // tb.setEditable(false);
//        tb.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
//        tb.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        tb.setText("123\nqwe sdfsdqwe qweqwe qewqqweq wqeq f;l!\nk(sdfsdf) sdf\nsdqeq eqeqw eqwf_ sdfs\ndfs_d ff+gh");
//        tb.setPadding(10,0,10,0);
//        tb.setWidth(30);
        tb.setHeight(50);
        tb.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);

        //        tb.eventKeyPress.add((sender, args) -> {
        //            if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.S) {
        //                System.out.println(args.mods.size() + " mods: " + args.mods + " code: " + args.key);
        //                tb.appendText("appended text line...\n");
        //            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.R) {
        //                System.out.println(args.mods.size() + " mods: " + args.mods + " code: " + args.key);
        //                tb.setText("SetText...");
        //            }
        //        });
        // tb.setPadding(15, 0, 15, 0);
        // tb.setMargin(new Indents(50, 30, 30, 30));
        // tb.setTextMargin(new Indents(50, 30, 30, 30));
        // tb.onTextChanged.add(() -> System.out.println("text changed"));

        //        SpinItem sp = new SpinItem();
        //        sp.setParameters(1, -5.5, 7, 0.51);

        //        layout.addItem(password);
        //        layout.addItem(te);

        layout.addItem(tb);
//        tb.setStyle(Style.getTextAreaStyle());

        ButtonCore bc = new ButtonCore("pizdec");
        bc.setSize(150, 30);
        ButtonCore bc1 = new ButtonCore("orNot");
        bc1.setSize(150, 30);
        ButtonCore bc2 = new ButtonCore("getWhole");
        bc2.setSize(150, 30);

        layout.addItems(bc, bc1, bc2); //, sp);

        //        Label tl = new Label();
        //        tl.setBackground(255, 255, 255, 100);
        //        tl.setForeground(Color.WHITE);
        //        tl.setTextAlignment(ItemAlignment.TOP);
        //        tl.setText("sdasdasd");
        //
        //        layout.addItem(tl);

        tb.setWrapText(false);
        bc.eventMouseClick.add((sender, args) -> {
            tb.setText("Welcome on the channel spvessel.\n"
                    + "In this video I’ll show first steps for using framework SpaceVIL. You will find detailed instructions on the site spvessel.com or in my github repository. All links at the end of the video.\n"
                    + "At first I’ll show you how it works for C# with .NET Standard on Windows OS. I’ll make a simple window and tell you about some features. Then I’ll show the same things on Linux Ubuntu OS\n"
                    + "dfggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                    + "Welcome on the channel spvessel.\n"
                    + "In this video I’ll show first steps for using framework SpaceVIL. You will find detailed instructions on the site spvessel.com or in my github repository. All links at the end of the video.\n"
                    + "At first I’ll show you how it works for C# with .NET Standard on Windows OS. I’ll make a simple window and tell you about some features. Then I’ll show the same things on Linux Ubuntu OS\n"
                    + "Welcome on the channel spvessel.\n"
                    + "In this video I’ll show first steps for using framework SpaceVIL. You will find detailed instructions on the site spvessel.com or in my github repository. All links at the end of the video.\n"
                    + "At first I’ll show you how it works for C# with .NET Standard on Windows OS. I’ll make a simple window and tell you about some features. Then I’ll show the same things on Linux Ubuntu OS\n"
                    + "Welcome on the channel spvessel.\n"
                    + "In this video I’ll show first steps for using framework SpaceVIL. You will find detailed instructions on the site spvessel.com or in my github repository. All links at the end of the video.\n"
                    + "At first I’ll show you how it works for C# with .NET Standard on Windows OS. I’ll make a simple window and tell you about some features. Then I’ll show the same things on Linux Ubuntu OS\n"
                    + "Welcome on the channel spvessel.\n"
                    + "In this video I’ll show first steps for using framework SpaceVIL. You will find detailed instructions on the site spvessel.com or in my github repository. All links at the end of the video.\n"
                    + "At first I’ll show you how it works for C# with .NET Standard on Windows OS. I’ll make a simple window and tell you about some features. Then I’ll show the same things on Linux Ubuntu OS\n"
                    + "Welcome on the channel spvessel.\n"
                    + "In this video I’ll show first steps for using framework SpaceVIL. You will find detailed instructions on the site spvessel.com or in my github repository. All links at the end of the video.\n"
                    + "At first I’ll show you how it works for C# with .NET Standard on Windows OS. I’ll make a simple window and tell you about some features. Then I’ll show the same things on Linux Ubuntu OS\n");

            //            System.out.print(getX() + " ");
            //            setHeight(getHeight() + 10);
            //            System.out.println(getX());
        });

        bc1.eventMouseClick.add((sender, args) -> {
            //            System.out.print(getX() + " ");
            //            setHeight(getHeight() - 10);
            //            System.out.println(getX());
            tb.setWrapText(!tb.isWrapText());
            //            System.out.println(tb.isWrapText());
        });

        bc2.eventMouseClick.add((sender, args) -> {
            //            System.out.print(getX() + " ");
            //            setHeight(getHeight() - 10);
            //            System.out.println(getX());
            String text = tb.getText();
            System.out.println(text);
            //            System.out.println(tb.isWrapText());
        });

        //        Label label = new Label("1234567890");
        //        layout.addItem(label);

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

        // tb.setText(getBigText());
        // tb.rewindText();
        tb.setWrapText(true);

        tb.eventKeyRelease.add((sender, args) -> {
            if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1) {
                if (args.key == KeyCode.EQUAL) {
                    int fontSize = tb.getFont().getSize();
                    fontSize++;
                    System.out.println(fontSize);
                    if (fontSize > 32)
                        return;
                    tb.setFontSize(fontSize);
                }
                if (args.key == KeyCode.MINUS) {
                    int fontSize = tb.getFont().getSize();
                    fontSize--;
                    System.out.println(fontSize);
                    if (fontSize < 10)
                    return;
                    tb.setFontSize(fontSize);
                }
            }
        });
    }

    private static int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    private String getBigText() {
        return "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaiehfouiashueighaoweigh\n" + "";
    }
}