package com.spvessel.View;

import com.spvessel.Decorations.ItemState;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.SizePolicy;
import com.spvessel.Items.*;
import com.spvessel.Items.Frame;
import com.spvessel.Windows.ActiveWindow;
import com.spvessel.Windows.WindowLayout;
import sun.font.FontFamily;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;

public class InputTest extends ActiveWindow {
    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "InputTest", "InputTest");
        Handler.setWidth(500);
        Handler.setMinWidth(200);
        Handler.setHeight(500);
        Handler.setMinHeight(200);
        Handler.setWindowTitle("InputTest");
        Handler.setPadding(2, 2, 2, 2);
        Handler.setBackground(new Color(45, 45, 45, 255));
        Handler.isBorderHidden = true;

        //DragAnchor
        TitleBar title = new TitleBar("InputTest");
        Handler.addItem(title);

        //ToolBar
        VerticalStack layout = new VerticalStack();
        layout.setMargin(0, 30, 0, 0);
        layout.setSpacing(0, 5);
        layout.setBackground(255, 255, 255, 20);

        //adding toolbar
        Handler.addItem(layout);

        //Frame
        HorizontalStack toolbar = new HorizontalStack();
        toolbar.setBackground(new Color(60, 60, 60, 255));
        toolbar.setItemName("toolbar");
        toolbar.setHeight(40);
        //toolbar.setPadding(10);
        //toolbar.setSpacing(10);
        toolbar.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        layout.addItem(toolbar);

        //Frame
        Frame frame = new Frame();
        frame.setBackground(new Color(51, 51, 51, 255));
        frame.setItemName("Container");
        frame.setPadding(15, 15, 15, 15);
        frame.setWidthPolicy(SizePolicy.EXPAND);
        frame.setHeightPolicy(SizePolicy.EXPAND);
        layout.addItem(frame);


        ListBox listbox = new ListBox();
        listbox.setBackground(new Color(70, 70, 70, 255));
        listbox.setAlignment(new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER)));
        listbox.setSelectionVisibility(false);
        //listbox.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        frame.addItem(listbox);

        TextEdit _editLines = new TextEdit();
        _editLines.setFont(new Font("Arial", Font.PLAIN, 150));
        _editLines.setAlignment(ItemAlignment.TOP);
        _editLines.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        _editLines.setSpacing(0, 5);
        _editLines.setPadding(2, 2, 2, 2);
        //_editLines.setForeground(Color.White);
        //_editLines.setTextAlignment(ItemAlignment.RIGHT);
        //frame.AddItem(_editLines);
        listbox.addItem(_editLines);

        //style
        Style btn_style = new Style();
        btn_style.background = new Color(13, 176, 255, 255);
        btn_style.borderRadius = 6;
        btn_style.width = 30;
        btn_style.height = 30;
        btn_style.widthPolicy = SizePolicy.FIXED;
        btn_style.heightPolicy = SizePolicy.FIXED;
        btn_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        ItemState brighter = new ItemState();
        brighter.background = new Color(255, 255, 255, 125);
            /*
            btn_style.ItemStates.Add(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            */
        /*
        //btn add_at_begin
        ButtonCore show_info = new ButtonCore();
        show_info.setToolTip("info");
        show_info.setItemName(nameof(show_info));
        show_info.setStyle(btn_style);
        show_info.EventMouseClick += (sender, args) =>
        {
            //Console.WriteLine(pwd.GetPassword());
            //_editLines.setText("123\ner");
            //_editLines.IsEditable = false;
            //_editLines.ShowPassword();
        }
        ;
        toolbar.AddItem(show_info);

        ButtonCore change_color = new ButtonCore();
        change_color.setToolTip("Clear");
        change_color.setItemName(nameof(change_color));
        change_color.setStyle(btn_style);
        change_color.EventMouseClick += (sender, args) =>
        {
            _editLines.Clear();
        }
        ;
        toolbar.AddItem(change_color);

        ButtonCore change_font = new ButtonCore();
        change_font.setItemName(nameof(change_font));
        change_font.setStyle(btn_style);
        change_font.EventMouseClick += (sender, args) =>
        {
            if (_editLines.IsEditable)
                _editLines.IsEditable = false;
            else
                _editLines.IsEditable = true;
        }
        ;
        toolbar.AddItem(change_font);
        */
    }

}
