package com.spvessel.spacevil.View;

import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.TextArea;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class InputTest extends ActiveWindow {
    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout("InputTest", "InputTest", 700, 550, true);
        // Handler.setAntiAliasingQuality(MSAA.MSAA_8X);
        setHandler(Handler);

        Handler.setMinSize(50, 100);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

        TitleBar title = new TitleBar("Input Test");
        // title.setAlignment(ItemAlignment.BOTTOM, ItemAlignment.LEFT);
        // title.direction = HorizontalDirection.FROM_RIGHT_TO_LEFT;
        Handler.addItem(title);

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
        te.setWidthPolicy(SizePolicy.EXPAND);

        TextArea tb = new TextArea();
        tb.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        tb.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        tb.setText("123qwe");
        tb.setWidth(300);
        tb.setHeight(300);
        tb.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        // tb.setPadding(15, 0, 15, 0);
        // tb.setMargin(new Indents(50, 30, 30, 30));
        // tb.setTextMargin(new Indents(50, 30, 30, 30));
//        tb.onTextChanged.add(() -> System.out.println("text changed"));

        SpinItem sp = new SpinItem();
        sp.setParameters(1, -5.5, 7, 0.51);

        layout.addItem(password);
        layout.addItem(te);
        layout.addItem(tb);
        tb.setStyle(Style.getTextAreaStyle());

        ButtonCore bc = new ButtonCore("pizdec");
        bc.setSize(150, 30);
        //tb.setEditable(false);
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
//            tb.pasteText("12345678");
//            te.pasteText("text edit text");
//            Handler.setWindowTitle(te.getSelectedText());
            //tb.setFocused(false);
            tb.setText("package com.spvessel.spacevil;\n" +

                    "        // HBar\n" +
                    "        hScrollBar.isFocusable = false;\n" +
                    "        hScrollBar.setVisible(true);\n" +
                    "        hScrollBar.setItemName(getItemName() + \"_\" + hScrollBar.getItemName());\n" +
                    "\n" +
                    "        // Area\n" +
                    "        // _area.setItemName(getItemName() + \"_\" + _area.getItemName());\n" +
                    "        // _area.setSpacing(0, 5);\n" +
                    "    }\n" +
                    "\n" +
                    "    public TextArea(String text) {\n" +
                    "        this();\n" +
                    "        setText(text);\n" +
                    "    }\n" +
                    "\n" +
                    "    private long v_size = 0;\n" +
                    "    private long h_size = 0;\n" +
                    "\n" +
                    "    private void updateVListArea() {\n" +
                    "        // vertical slider\n" +
                    "        float v_value = vScrollBar.slider.getCurrentValue();\n" +
                    "        int v_offset = (int) Math.round((float) (v_size * v_value) / 100.0f);\n" +
                    "        _area.setScrollYOffset(-v_offset);\n" +
                    "    }\n" +
                    "\n" +
                    "    private void updateHListArea() {\n" +
                    "        // horizontal slider\n" +
                    "        float h_value = hScrollBar.slider.getCurrentValue();\n" +
                    "        int h_offset = (int) Math.round((float) (h_size * h_value) / 100.0f);\n" +
                    "        _area.setScrollXOffset(-h_offset);\n" +
                    "    }\n" +
                    "\n" +
                    "    private void updateVerticalSlider()// vertical slider\n" +
                    "    {\n" +
                    "        int visible_area = _area.getHeight() - _area.getPadding().top - _area.getPadding().bottom;\n" +
                    "        if (visible_area < 0)\n" +
                    "            visible_area = 0;\n" +
                    "        int total = _area.getTextHeight();\n" +
                    "\n" +
                    "        int total_invisible_size = total - visible_area;\n" +
                    "        if (total <= visible_area) {\n" +
                    "            vScrollBar.slider.handler.setHeight(0);\n" +
                    "            vScrollBar.slider.setStep(vScrollBar.slider.getMaxValue());\n" +
                    "            v_size = 0;\n" +
                    "            vScrollBar.slider.setCurrentValue(0);\n" +
                    "            if (getVScrollBarVisible() == ScrollBarVisibility.AS_NEEDED) {\n" +
                    "                vScrollBar.setDrawable(false);\n" +
                    "                menu.setVisible(false);\n" +
                    "                _grid.updateLayout();\n" +
                    "            }\n" +
                    "            return;\n" +
                    "        }\n" +
                    "        if (getVScrollBarVisible() == ScrollBarVisibility.AS_NEEDED) {\n" +
                    "            vScrollBar.setDrawable(true);\n" +
                    "            if (!hScrollBar.isDrawable())\n" +
                    "                menu.setVisible(false);\n" +
                    "            else\n" +
                    "                menu.setVisible(true);\n" +
                    "            _grid.updateLayout();\n" +
                    "        }\n" +
                    "        v_size = total_invisible_size;\n" +
                    "\n" +
                    "        if (total_invisible_size > 0) {\n" +
                    "            float size_handler = (float) (visible_area) / (float) total * 100.0f;\n" +
                    "            size_handler = (float) vScrollBar.slider.getHeight() / 100.0f * size_handler;\n" +
                    "            // size of handler\n" +
                    "            vScrollBar.slider.handler.setHeight((int) size_handler);\n" +
                    "        }\n" +
                    "        // step of slider\n" +
                    "        float step_count = (float) total_invisible_size / (float) _area.getScrollYStep();\n" +
                    "        vScrollBar.slider.setStep((vScrollBar.slider.getMaxValue() - vScrollBar.slider.getMinValue()) / step_count);\n" +
                    "        vScrollBar.slider.setCurrentValue((100.0f / total_invisible_size) * Math.abs(_area.getScrollYOffset()));\n" +
                    "    }\n" +
                    "\n" +
                    "    private void updateHorizontalSlider()// horizontal slider\n" +
                    "    {\n" +
                    "        int visible_area = _area.getWidth() - _area.getPadding().left - _area.getPadding().right - 2 * _area.getCursorWidth();\n" +
                    "        if (visible_area < 0)\n" +
                    "            visible_area = 0;\n" +
                    "        int total = _area.getTextWidth();\n" +
                    "\n" +
                    "        int total_invisible_size = total - visible_area;\n" +
                    "        if (total <= visible_area) {\n" +
                    "            hScrollBar.slider.handler.setWidth(0);\n" +
                    "            hScrollBar.slider.setStep(hScrollBar.slider.getMaxValue());\n" +
                    "            h_size = 0;\n" +
                    "            hScrollBar.slider.setCurrentValue(0);\n" +
                    "            if (getHScrollBarVisible() == ScrollBarVisibility.AS_NEEDED) {\n" +
                    "                hScrollBar.setDrawable(false);\n" +
                    "                menu.setVisible(false);\n" +
                    "                _grid.updateLayout();\n" +
                    "            }\n" +
                    "            return;\n" +
                    "        }\n" +
                    "        if (getHScrollBarVisible() == ScrollBarVisibility.AS_NEEDED) {\n" +
                    "            hScrollBar.setDrawable(true);\n" +
                    "            if (!vScrollBar.isDrawable())\n" +
                    "                menu.setVisible(false);\n" +
                    "            else\n" +
                    "                menu.setVisible(true);\n" +
                    "            _grid.updateLayout();\n" +
                    "        }\n" +
                    "        h_size = total_invisible_size;\n" +
                    "\n" +
                    "        if (total_invisible_size > 0) {\n" +
                    "            float size_handler = (float) (visible_area) / (float) total * 100.0f;\n" +
                    "            size_handler = (float) hScrollBar.slider.getWidth() / 100.0f * size_handler;\n" +
                    "            // size of handler\n" +
                    "            hScrollBar.slider.handler.setWidth((int) size_handler);\n" +
                    "        }\n" +
                    "        // step of slider\n" +
                    "        float step_count = (float) total_invisible_size / (float) _area.getScrollXStep();\n" +
                    "        hScrollBar.slider.setStep((hScrollBar.slider.getMaxValue() - hScrollBar.slider.getMinValue()) / step_count);\n" +
                    "        hScrollBar.slider.setCurrentValue((100.0f / total_invisible_size) * Math.abs(_area.getScrollXOffset()));\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * Set width of the TextArea\n" +
                    "     */\n" +
                    "    @Override\n" +
                    "    public void setWidth(int width) {\n" +
                    "        super.setWidth(width);\n" +
                    "        updateHorizontalSlider();\n" +
                    "        hScrollBar.slider.updateHandler();\n" +
                    "        // _area.setWidth(width);\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * Set height of the TextArea\n" +
                    "     */\n" +
                    "    @Override\n" +
                    "    public void setHeight(int height) {\n" +
                    "        super.setHeight(height);\n" +
                    "        updateVerticalSlider();\n" +
                    "        vScrollBar.slider.updateHandler();\n" +
                    "        // _area.setHeight(height);\n" +
                    "    }\n" +
                    "\n" +
                    "    private void updateElements() {\n" +
                    "        updateVerticalSlider();\n" +
                    "        vScrollBar.slider.updateHandler();\n" +
                    "        updateHorizontalSlider();\n" +
                    "        hScrollBar.slider.updateHandler();\n" +
                    "    }\n" +
                    "\n" +
                    "    public EventCommonMethod onTextChanged = new EventCommonMethod()");
//            tb.rewindText();

        });

        layout.addItem(bc);

        // layout.addItem(sp);

        Label tl = new Label();
        layout.addItem(tl);
        tl.setText("1qwertyuiopasdfghjkl\nzxcvbnm123\n4567890");
        tl.setBackground(255,255,255,100);
        tl.setFontSize(17);
        tl.setForeground(Color.WHITE);
        tl.setTextAlignment(ItemAlignment.BOTTOM);

        Handler.getWindow().eventDrop.add((sender, args) -> {
            if (args.count > 0) {
                System.out.println(args.item.getItemName());
                String line = null;
                String FilePath = args.paths.get(0);
                StringBuilder sb = new StringBuilder();
                try {
                    FileReader fileReader = new FileReader(FilePath);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
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