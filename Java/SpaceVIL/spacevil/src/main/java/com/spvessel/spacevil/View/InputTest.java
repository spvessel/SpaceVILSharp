package com.spvessel.spacevil.View;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Decorations.Effects;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Decorations.SubtractFigure;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.SizePolicy;

public class InputTest extends ActiveWindow {
    Label _infoOutput;

    @Override
    public void initWindow() {
        setSize(800, 800);
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

        // layout.setMargin(0, 30, 0, 30);
        layout.setBackground(70, 70, 70);
        layout.setSpacing(6, 30);
        layout.setPadding(2, 2, 2, 2);
        addItem(layout);

         PasswordLine password = new PasswordLine();
         password.setSubstrateText("Enter a password...");
        // password.setTextAlignment(ItemAlignment.RIGHT);

        TextEdit te = new TextEdit();
        layout.addItem(te);
        // te.setText("TextZaranee");
//         te.setTextAlignment(ItemAlignment.RIGHT);
        // te.getSelectionArea().setBackground(Color.green);
//        te.setEditable(false);
        te.setText("Write123 45678 90-= qwert yu iop [] asdfghj kl;' zxcv bnm,./"); //Substrate
        // te.setMargin(0,0,150,0);
        // te.setFontSize(10);
//        te.setWidth(300);
//        te.setWidthPolicy(SizePolicy.EXPAND);

        TextArea tb = new TextArea();

        // TextView tb = new TextView();
        // tb.setScrollStepFactor(3f);
        // tb.setMargin(2, 2, 2, 27);
        // tb.setEditable(false);
        //        tb.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        //        tb.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        // tb.setText("123\nqwe sdfsdqwe qweqwe qewqqweq wqeq f;l!\nk(sdfsdf) sdf\nsdqeq eqeqw eqwf_ sdfs\ndfs_d ff+gh");
        //        tb.setPadding(10,0,10,0);
        //        tb.setWidth(30);
        // tb.setHeight(50);
        // tb.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);

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

                SpinItem sp = new SpinItem();
                sp.setParameters(1, -5.5, 7, 0.51);

        layout.addItems(password, sp);
//        layout.addItem(te);

        layout.addItem(tb);
        tb.setStyle(Style.getTextAreaStyle());
        tb.setText(getBigText());
        tb.setWrapText(true);

        // ButtonCore bc = new ButtonCore("pizdec");
        // bc.setSize(150, 30);
        // ButtonCore bc1 = new ButtonCore("orNot");
        // bc1.setSize(150, 30);
        // ButtonCore bc2 = new ButtonCore("getWhole");
        // bc2.setSize(150, 30);
        // bc2.setWidthPolicy(SizePolicy.EXPAND);

        // layout.addItems(bc, bc1, bc2); //, sp);

        //        Label tl = new Label();
        //        tl.setBackground(255, 255, 255, 100);
        //        tl.setForeground(Color.WHITE);
        //        tl.setTextAlignment(ItemAlignment.TOP);
        //        tl.setText("sdasdasd");
        //
        //        layout.addItem(tl);

        //         bc.eventMouseClick.add((sender, args) -> {
        //             tb.setText("Welcome on the channel spvessel.\n"
        //                     + "In this video I’ll show first steps for using framework SpaceVIL. You will find detailed instructions on the site spvessel.com or in my github repository. All links at the end of the video.\n"
        //                     + "At first I’ll show you how it works for C# with .NET Standard on Windows OS. I’ll make a simple window and tell you about some features. Then I’ll show the same things on Linux Ubuntu OS\n"
        //                     + "dfggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg"
        //                     + "Welcome on the channel spvessel.\n"
        //                     + "In this video I’ll show first steps for using framework SpaceVIL. You will find detailed instructions on the site spvessel.com or in my github repository. All links at the end of the video.\n"
        //                     + "At first I’ll show you how it works for C# with .NET Standard on Windows OS. I’ll make a simple window and tell you about some features. Then I’ll show the same things on Linux Ubuntu OS\n"
        //                     + "Welcome on the channel spvessel.\n"
        //                     + "In this video I’ll show first steps for using framework SpaceVIL. You will find detailed instructions on the site spvessel.com or in my github repository. All links at the end of the video.\n"
        //                     + "At first I’ll show you how it works for C# with .NET Standard on Windows OS. I’ll make a simple window and tell you about some features. Then I’ll show the same things on Linux Ubuntu OS\n"
        //                     + "Welcome on the channel spvessel.\n"
        //                     + "In this video I’ll show first steps for using framework SpaceVIL. You will find detailed instructions on the site spvessel.com or in my github repository. All links at the end of the video.\n"
        //                     + "At first I’ll show you how it works for C# with .NET Standard on Windows OS. I’ll make a simple window and tell you about some features. Then I’ll show the same things on Linux Ubuntu OS\n"
        //                     + "Welcome on the channel spvessel.\n"
        //                     + "In this video I’ll show first steps for using framework SpaceVIL. You will find detailed instructions on the site spvessel.com or in my github repository. All links at the end of the video.\n"
        //                     + "At first I’ll show you how it works for C# with .NET Standard on Windows OS. I’ll make a simple window and tell you about some features. Then I’ll show the same things on Linux Ubuntu OS\n"
        //                     + "Welcome on the channel spvessel.\n"
        //                     + "In this video I’ll show first steps for using framework SpaceVIL. You will find detailed instructions on the site spvessel.com or in my github repository. All links at the end of the video.\n"
        //                     + "At first I’ll show you how it works for C# with .NET Standard on Windows OS. I’ll make a simple window and tell you about some features. Then I’ll show the same things on Linux Ubuntu OS\n");

        //             //            System.out.print(getX() + " ");
        //             //            setHeight(getHeight() + 10);
        //             //            System.out.println(getX());
        //         });

        //         bc1.eventMouseClick.add((sender, args) -> {
        //             System.out.println("first");
        //             // bc1.eventMouseClick.cancel();
        //         });

        //         bc1.eventMouseClick.add((sender, args) -> {
        //             System.out.println("second");
        //         });

        //         bc2.eventMousePress.add((sender, args) -> {
        //             System.out.println("pressed");
        //         });
        //         bc2.eventMouseClick.add((sender, args) -> {
        //             //            System.out.print(getX() + " ");
        //             //            setHeight(getHeight() - 10);
        //             //            System.out.println(getX());
        // //            String text = tb.getText();
        // //            System.out.println(text);
        //             //            System.out.println(tb.isWrapText());
        //             System.out.println("click");
        //         });
        //         bc2.eventMouseDoubleClick.add((sender, args) -> {
        //             System.out.println("double click");
        //         });

        //        Label label = new Label("1234567890");
        //        layout.addItem(label);

        // eventDrop.add((sender, args) -> {

        //     if (args.count > 0) {
        //         System.out.println(args.item.getItemName());
        //         String line = null;
        //         String FilePath = args.paths.get(0);
        //         StringBuilder sb = new StringBuilder();
        //         try {
        //             InputStreamReader stream = new InputStreamReader(new FileInputStream(FilePath), "UTF-8");
        //             BufferedReader bufferedReader = new BufferedReader(stream);
        //             while ((line = bufferedReader.readLine()) != null) {
        //                 sb.append(line + "\n");
        //             }
        //             bufferedReader.close();
        //         } catch (Exception e) {
        //             e.printStackTrace();
        //         }
        //         tb.setText(sb.toString());
        //     }
        // });

        // tb.setWrapText(true);
        //        tb.setText(getBigText());
        // tb.setStyle(getTextAreaStyle());
        // tb.setWrapText(true);
        // tb.rewindText();
        tb.setFocus();

        // tb.setBackground(0, 0, 0);

        // tb.eventKeyRelease.add((sender, args) -> {
        //     if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1) {
        //         if (args.key == KeyCode.EQUAL) {
        //             int fontSize = tb.getFont().getSize();
        //             fontSize++;
        //             System.out.println(fontSize);
        //             if (fontSize > 32)
        //                 return;
        //             tb.setFontSize(fontSize);
        //         }
        //         if (args.key == KeyCode.MINUS) {
        //             int fontSize = tb.getFont().getSize();
        //             fontSize--;
        //             System.out.println(fontSize);
        //             if (fontSize < 10)
        //                 return;
        //             tb.setFontSize(fontSize);
        //         }
        //     }
        // });

        // Effects.addEffect(bc2, getStencilEffect(bc2));
        // SubtractFigure effect1 = new SubtractFigure(
        //         new CustomFigure(true, GraphicsMathService.getEllipse(bc2.getHeight() + 10, bc2.getHeight() + 10, 0, 0, 32)));
        // effect1.setSizeScale(0.2f, 1f);
        // effect1.setPositionOffset(-bc2.getHeight() / 2, 0);
        // effect1.setAlignment(ItemAlignment.VCENTER);
        // Effects.addEffect(bc2, effect1);

        // SubtractFigure effect2 = new SubtractFigure(
        //         new CustomFigure(true, GraphicsMathService.getEllipse(bc2.getHeight() + 10, bc2.getHeight() + 10, 0, 0, 32)));
        // effect2.setSizeScale(0.2f, 1f);
        // effect2.setPositionOffset(bc2.getHeight() / 2, 0);
        // effect2.setAlignment(ItemAlignment.RIGHT, ItemAlignment.VCENTER);
        // Effects.addEffect(bc2, effect2);

        tb.eventKeyPress.add((sender, args) -> {
            if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.EQUAL) {
                int fontSize = tb.getFont().getSize();
                fontSize++;
                if (fontSize > 32)
                    return;
                tb.setFontSize(fontSize);
            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.MINUS) {
                int fontSize = tb.getFont().getSize();
                fontSize--;
                if (fontSize < 10)
                    return;
                tb.setFontSize(fontSize);
            }
        });

    }

    private static int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    private String getBigText() {
        return "ajfh gajhdif uahwoieh foiawoe ifisdfghaoisiuehgi ouaoesijfoaiehfouia shueighaoweigh1"
                + "ajfhgajhdif uahwoiehfoiaw oeifis dfghaoi siuehgiouaoesi jfoaiehfouiashu eighaoweigh2 "
                + "ajfhg ajhdifuahwoiehf oiawoei fisdfghaois iuehgiouaoesijfo aiehfouiashueighaow eigh3 "
                + "ajfhgajh difuahwoiehfoiawoe ifisdfghaoisiueh giouaoesijfoaiehf ouiashueighaoweigh4 "
                + "ajfhgajhdifuahwoieh foiawoeifisdfgh aoisiuehgioua oesijf oaiehfoui ashueighaoweigh5\n"
                + "ajfhgajhdi fuahwoiehfoiawoeifis dfghaoisiuehgioua oesijfoaiehfouiashue ighaoweigh6\n"
                + "ajfhgajhdifu ahwoiehfoiawoeifisd fgh aoisiuehgiouaoesijfoaiehf ouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeif isdfghaoisiueh giouaoesij foaiehfouiashueighaoweigh\n"
                + "ajfhgajhdif uahwoiehfoiawoei  fisdfghaoisiuehgi ouaoesijfoaiehf ouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoe ifisdfghaoisiue hgiouaoesijfoaie hfouiashue ighaoweigh\n"
                + "ajfhgajhdi fuahwoi ehfoiawoeifi sdfghaoisiuehgiouaoesij foaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdf ghaoi iuehgiouaoes ijfoaiehfouias hueighaoweigh\n"
                + "ajfhgajhdi fuahwoiehfoiawoeifisdfghaoisiue hgiouaoesijfoaiehfou iashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdf ghaoisiuehgiouaoe sijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoieh foiawoeifis dfghaoisiuehgiouaoesijfoaieh fouiash ueighaoweigh\n"
                + "ajfhg ajhdifuahw oiehfoiawoeifi sdfghaoi siuehgiouaoesijfoai ehfouias hueighaoweigh\n"
                + "ajfhgajhdifuah woiehfoiawoei fisdfghaoisiuehgio uaoesijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoie hfoiawoeifisdfghaoisiu ehgiouaoesijfoaiehfouiashueig haoweigh\n"
                + "ajfhgajhd ifuahwoiehfoiawoeifisdfg haois iuehgiouaoesi jfoaiehfou iashueighaoweigh\n"
                + "ajfhgajh difuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijfoaieh fouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfo ia woeifisdfgha oisiuehgiou aoesijfoaiehfoui ashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiaw oeifisdfghao isiuehgiouao esijfoai ehfouias hueighaoweigh\n"
                + "ajfhga jhdifuahwoiehfoiaw oeifisdfgh aoisiuehgi ouaoesijf oaiehfouias ueighaoweigh\n"
                + "ajfhgajhdifuahwoieh foiawoeifisdfghaoisiuehgiouaoes ijfoaiehfouia shueighaoweigh\n"
                + "ajfhgajhdifuah woiehfoiawoeifis dfghaoisiue hgioua oesijfoaiehfouiash ueighaoweigh\n"
                + "ajfhgajhdifuah woiehfoiawoeif isdfghaoisi uehgiouaoesijf oaiehfouiashu eighaoweigh\n"
                + "ajfhgaj hdifuahwoieh foiawoeifi sdfghaoisiuehgio uaoesijf oaiehfouiashue ighaoweigh\n"
                + "ajfhgajhd ifuahwoi ehfoiawoeifisdfgh aoisiuehgiouaoesijf oaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoia woeifisdf ghaoisiuehgiou aoesijfoaiehfoui ashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoia woeifisdfghaoisiuehgi ouaoesijfoai ehfouiashueighaoweigh\n"
                + "ajfhgajhdi fuahwoiehfoiawoeifisdf ghaoisiuehgi ouaoesijfoaie hfouiashueighaow eigh\n"
                + "ajfhgajhdifuahwoie hfoiawoeifisdfghaoisiue hgiouaoesijfo aiehfouia shueighaoweigh\n"
                + "ajfhgajhdifua hwoi ehfoiawoeifisdfghaoisiueh giouaoesijfoaie hfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoeifisdfghaoisiuehgiouaoesijf oaiehfouiashueighaoweigh\n"
                + "ajfhgaj hdifuahwoi ehfoiawoeifisd fghaoisiuehgiouaoe sijfoaiehfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwoiehfoiawoe ifisdfghaois iuehgiou aoesijfoaie hfouiashueighaoweigh\n"
                + "ajfhgajhdifuahwo iehfoiawo eifisdfghaoi iuehgiouaoesijfoaie hfouiashueighaoweigh\n"
                + "ajfhga jhdifuahwoieh foiawoeifisdfghaois iuehgiouaoesijfo aiehfouiashuei ghaoweigh\n" + "";
    }

    public static Style getTextAreaStyle() {
        Style style = new Style();
        style.background = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont(14);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style text_style = Style.getTextBlockStyle();
        text_style.font = DefaultsService.getDefaultFont(14);
        text_style.foreground = new Color(180, 180, 180);
        text_style.getInnerStyle("selection").background = new Color(255, 255, 255, 25);
        text_style.getInnerStyle("cursor").background = new Color(0, 162, 232);
        text_style.getInnerStyle("selection").setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        style.addInnerStyle("textedit", text_style);

        Style vsb_style = Style.getSimpleVerticalScrollBarStyle();
        vsb_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.TOP));
        vsb_style.width = 10;
        vsb_style.setPadding(0, 0, 0, 0);
        vsb_style.getInnerStyle("slider").setBackground(60, 60, 60);
        vsb_style.getInnerStyle("slider").addItemState(ItemStateType.HOVERED, new ItemState(new Color(0, 0, 0, 40)));
        vsb_style.getInnerStyle("slider").borderRadius = new CornerRadius(5);
        vsb_style.getInnerStyle("slider").setPadding(0, 2, 0, 2);
        vsb_style.getInnerStyle("slider").getInnerStyle("handler").setMargin(2, 0, 2, 0);
        vsb_style.getInnerStyle("slider").getInnerStyle("handler")
                .setShadow(new Shadow(8, 0, 0, new Color(0, 0, 0, 255)));
        vsb_style.getInnerStyle("slider").getInnerStyle("handler").isShadowDrop = true;
        style.addInnerStyle("vscrollbar", vsb_style);

        Style hsb_style = Style.getSimpleHorizontalScrollBarStyle();
        hsb_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.BOTTOM));
        hsb_style.height = 10;
        hsb_style.setMargin(0, 0, 0, 5);
        hsb_style.getInnerStyle("slider").setBackground(60, 60, 60);
        hsb_style.getInnerStyle("slider").addItemState(ItemStateType.HOVERED, new ItemState(new Color(0, 0, 0, 30)));
        hsb_style.getInnerStyle("slider").borderRadius = new CornerRadius(5);
        hsb_style.getInnerStyle("slider").setPadding(2, 0, 2, 0);
        hsb_style.getInnerStyle("slider").getInnerStyle("handler").setMargin(0, 2, 0, 2);
        style.addInnerStyle("hscrollbar", hsb_style);

        Style menu_style = new Style();
        menu_style.background = new Color(50, 50, 50);
        menu_style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        menu_style.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        style.addInnerStyle("menu", menu_style);

        return style;
    }

    private SubtractFigure getStencilEffect(InterfaceBaseItem item) {
        List<float[]> triangles = new LinkedList<>();
        float side = item.getHeight() / 4f;
        // 1
        triangles.add(new float[] { 0, 0 });
        triangles.add(new float[] { 0, side });
        triangles.add(new float[] { side, 0 });
        // 2
        triangles.add(new float[] { item.getWidth() - side, 0 });
        triangles.add(new float[] { item.getWidth(), side });
        triangles.add(new float[] { item.getWidth(), 0 });
        // 3
        triangles.add(new float[] { 0, item.getHeight() - side });
        triangles.add(new float[] { 0, item.getHeight() });
        triangles.add(new float[] { side, item.getHeight() });
        //  4
        triangles.add(new float[] { item.getWidth(), item.getHeight() - side });
        triangles.add(new float[] { item.getWidth() - side, item.getHeight() });
        triangles.add(new float[] { item.getWidth(), item.getHeight() });

        triangles.addAll(GraphicsMathService.getEllipse(10, 10, 5, (int) (item.getHeight() / 2f - 5.0f), 12));
        triangles.addAll(
                GraphicsMathService.getEllipse(10, 10, item.getWidth() - 15, (int) (item.getHeight() / 2f - 5.0f), 12));
        Figure figure = new Figure(false, triangles);

        SubtractFigure effect = new SubtractFigure(figure);
        return effect;
    }
}