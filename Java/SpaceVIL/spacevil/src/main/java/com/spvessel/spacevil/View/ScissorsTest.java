package com.spvessel.spacevil.View;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.VisibilityPolicy;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.MenuItem;
import com.spvessel.spacevil.TextArea;

import java.awt.*;

public class ScissorsTest extends ActiveWindow {
    @Override
    public void initWindow() {
        isBorderHidden = true;
        setSize(900, 900);
        setWindowName("ScissorsTest");
        setWindowTitle("ScissorsTest");

        setMinSize(300, 100);
        setBackground(45, 45, 45);
        setPadding(2, 2, 2, 2);
        isCentered = true;

        TitleBar title = new TitleBar("SpaceVIL Builder Tool");

        VerticalStack layout = new VerticalStack();
        layout.setMargin(0, 30, 0, 0);
        layout.setPadding(6, 15, 6, 6);
        layout.setBackground(70, 70, 70);
        layout.setSpacing(6, 15);

        HorizontalStack version = new HorizontalStack();
        version.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        version.setSize(595, 30);
        version.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        version.setSpacing(5, 0);

        HorizontalStack platform = new HorizontalStack();
        platform.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        platform.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        platform.setSize(450, 30);
        platform.setSpacing(5, 0);

        ButtonCore settings = new ButtonCore();
        settings.setSize(30, 30);
        settings.setBorderRadius(15);
        settings.eventMouseClick.add((sender, args) -> {
            // Settings sets = new Settings();
            // sets.onCloseDialog.add(() -> {
            //     // actions
            // });
            // sets.show(Handler);
        });

        Style v_style = Style.getTextEditStyle();
        v_style.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.RIGHT);
        v_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        v_style.setSize(50, 30);

        TextEdit v_major = new TextEdit();
        v_major.setText("0");
        v_major.setStyle(v_style);
        TextEdit v_middle = new TextEdit();
        v_middle.setText("3");
        v_middle.setStyle(v_style);
        TextEdit v_minor = new TextEdit();
        v_minor.setText("1");
        v_minor.setStyle(v_style);
        TextEdit v_extend = new TextEdit();
        v_extend.setText("5");
        v_extend.setStyle(v_style);

        ComboBox phase = new ComboBox();
        phase.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        phase.setWidth(150);

        ComboBox month = new ComboBox();
        month.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        month.setWidth(150);

        RadioButton java = new RadioButton("JVM / Gradle platform");
        java.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        java.setWidth(170);
        java.setChecked(true);
        RadioButton net = new RadioButton(".Net platform");
        net.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        net.setWidth(120);
        ComboBox net_version = new ComboBox();
        net_version.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        net_version.setWidth(150);

        ButtonCore startBtn = new ButtonCore("Start");
        startBtn.setWidth(150);
        startBtn.setHeight(30);
        startBtn.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        startBtn.setAlignment(ItemAlignment.HCENTER);
        startBtn.setMargin(0, 15, 0, 0);

        TextArea textServiceInfo = new TextArea();
        textServiceInfo.setVScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
        textServiceInfo.setHScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
        textServiceInfo.setEditable(false);
        textServiceInfo.setFont(DefaultsService.getDefaultFont(12));

        // adding
        addItems(title, layout);
        layout.addItems(version, platform, startBtn, textServiceInfo);
        version.addItems(settings, v_major, v_middle, v_minor, v_extend, phase, month);
        platform.addItems(java, net, net_version);

        // post actions
        phase.addItems(getMenuItem("ALPHA"), getMenuItem("BETA"), getMenuItem("RELEASE"));
        phase.setCurrentIndex(0);
        month.addItems(getMenuItem("January"), getMenuItem("February"), getMenuItem("March"), getMenuItem("April"),
                getMenuItem("May"), getMenuItem("June"), getMenuItem("July"), getMenuItem("August"),
                getMenuItem("September"), getMenuItem("October"), getMenuItem("November"), getMenuItem("December"));
        month.setCurrentIndex(0);
        net_version.addItems(getMenuItem(".Net Standard"), getMenuItem(".Net Core"));
        net_version.setCurrentIndex(0);
    }

    private MenuItem getMenuItem(String name) {
        return new MenuItem(name);
    }
}
