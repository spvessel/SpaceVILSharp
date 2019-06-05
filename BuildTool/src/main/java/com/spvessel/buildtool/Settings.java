package com.spvessel.buildtool;

import java.awt.Color;
import java.util.Arrays;

import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.CoreWindow;
import com.spvessel.spacevil.DialogItem;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.VerticalScrollBar;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;
import com.spvessel.spacevil.Flags.SizePolicy;

class Settings extends DialogItem {
    private TitleBar titleBar = new TitleBar();

    @Override
    public void initElements() {
        super.initElements();
        //////////////////////////////////////////////////
        window.setSize(700, 800);
        window.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        window.setMargin(50, 50, 50, 50);

        VerticalStack layout = new VerticalStack();
        layout.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        layout.setMargin(0, 30, 0, 50);
        layout.setPadding(0, 6, 0, 6);
        layout.setSpacing(0, 10);

        ListBox setsList = new ListBox();
        setsList.getArea().setSpacing(0, 5);
        setsList.setSelectionVisible(false);
        setsList.setHScrollBarVisible(ScrollBarVisibility.NEVER);
        VerticalScrollBar vs = setsList.vScrollBar;
        vs.slider.handler.removeAllItemStates();
        vs.setArrowsVisible(false);
        vs.setBackground(0, 0, 0, 0);
        vs.setPadding(0, 2, 0, 2);
        vs.slider.track.setBackground(new Color(0, 0, 0, 0));
        vs.slider.handler.setBorderRadius(5);
        vs.slider.handler.setBackground(32, 32, 32, 255);
        vs.slider.handler.setMargin(3, 0, 3, 0);

        // adding toolbar
        titleBar.setBackground(50, 50, 50);
        titleBar.setShadow(5, 0, 3, new Color(0, 0, 0, 180));
        titleBar.getMaximizeButton().setVisible(false);
        titleBar.setText("Settings");

        // Apply
        ButtonCore Apply = new ButtonCore("Apply");
        Apply.setBackground(100, 255, 150);
        Apply.setSize(100, 30);
        Apply.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        Apply.setBorderRadius(15);
        Apply.setMargin(6, 6, 15, 15);
        Apply.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
        Apply.eventMouseClick.add((sender, args) -> {
            for (InterfaceBaseItem var : setsList.getListContent()) {
                Parameter par = (Parameter) var;
                if (par.getName().equals("Source Path JVM:")) {
                    Configs.SrcPathJVM = par.getText();
                } else if (par.getName().equals("Source Path .Net:")) {
                    Configs.SrcPathNet = par.getText();
                } else if (par.getName().equals("Template Path JVM:")) {
                    Configs.TemplatePathJVM = par.getText();
                } else if (par.getName().equals("Template Path .Net Core:")) {
                    Configs.TemplatePathNetCore = par.getText();
                } else if (par.getName().equals("Template Path .Net Standard:")) {
                    Configs.TemplatePathNetStandard = par.getText();
                } else if (par.getName().equals("Obfuscator Path JVM:")) {
                    Configs.ObfPathJVM = par.getText();
                } else if (par.getName().equals("Obfuscator Path .Net:")) {
                    Configs.ObfPathNet = par.getText();
                } else if (par.getName().equals("Release Path JVM:")) {
                    Configs.ReleasePathJVM = par.getText();
                } else if (par.getName().equals("Release Path .Net Core:")) {
                    Configs.ReleasePathNetCore = par.getText();
                } else if (par.getName().equals("Release Path .Net Standard:")) {
                    Configs.ReleasePathNetStandard = par.getText();
                } else if (par.getName().equals("Zip Directory:")) {
                    Configs.ZipDir = par.getText();
                } else if (par.getName().equals("AssemblyInfo .Net Standard Path:")) {
                    Configs.AssemblyInfoNetStandard = par.getText();
                } else if (par.getName().equals("AssemblyInfo .Net Core Path:")) {
                    Configs.AssemblyInfoNetCore = par.getText();
                } else if (par.getName().equals("Exclude Directories:")) {
                    Configs.ExcludeDirs = Arrays.asList(par.getText().split(";"));
                } else if (par.getName().equals("Exclude Files:")) {
                    Configs.ExcludeFiles = Arrays.asList(par.getText().split(";"));
                } else if (par.getName().equals("Valuable Extensions:")) {
                    Configs.ValExtensions = Arrays.asList(par.getText().split(";"));
                } else if (par.getName().equals("Zip JVM Name::")) {
                    Configs.ZipJVM = par.getText();
                } else if (par.getName().equals("Zip .Net Core Name:")) {
                    Configs.ZipNetCore = par.getText();
                } else if (par.getName().equals("Zip .Net Standard Name:")) {
                    Configs.ZipNetStandard = par.getText();
                } else if (par.getName().equals("Verificate JVM:")) {
                    Configs.VerificateJVM = par.getText();
                } else if (par.getName().equals("Verificate .Net Core:")) {
                    Configs.VerificateNetCore = par.getText();
                } else if (par.getName().equals("Verificate .Net Standard:")) {
                    Configs.VerificateNetStandard = par.getText();
                }
            }
            System.out.println(Configs.getCurrentConfig());
            Configs.saveConfig(Configs.ConfPath);
            close();
        });

        window.addItems(titleBar, layout, Apply);
        layout.addItem(setsList);

        setsList.addItems(new Parameter("Source Path JVM:", Configs.SrcPathJVM));
        setsList.addItems(new Parameter("Source Path .Net:", Configs.SrcPathNet));
        setsList.addItems(new Parameter("Template Path JVM:", Configs.TemplatePathJVM));
        setsList.addItems(new Parameter("Template Path .Net Core:", Configs.TemplatePathNetCore));
        setsList.addItems(new Parameter("Template Path .Net Standard:", Configs.TemplatePathNetStandard));
        setsList.addItems(new Parameter("Obfuscator Path JVM:", Configs.ObfPathJVM));
        setsList.addItems(new Parameter("Obfuscator Path .Net:", Configs.ObfPathNet));
        setsList.addItems(new Parameter("Release Path JVM:", Configs.ReleasePathJVM));
        setsList.addItems(new Parameter("Release Path .Net Core:", Configs.ReleasePathNetCore));
        setsList.addItems(new Parameter("Release Path .Net Standard:", Configs.ReleasePathNetStandard));

        setsList.addItems(new Parameter("Verificate JVM:", Configs.VerificateJVM));
        setsList.addItems(new Parameter("Verificate .Net Core:", Configs.VerificateNetCore));
        setsList.addItems(new Parameter("Verificate .Net Standard:", Configs.VerificateNetStandard));

        setsList.addItems(new Parameter("Zip Directory:", Configs.ZipDir));
        setsList.addItems(new Parameter("Zip JVM Name:", Configs.ZipJVM));
        setsList.addItems(new Parameter("Zip .Net Core Name:", Configs.ZipNetCore));
        setsList.addItems(new Parameter("Zip .Net Standard Name:", Configs.ZipNetStandard));
        setsList.addItems(new Parameter("AssemblyInfo .Net Standard Path:", Configs.AssemblyInfoNetStandard));
        setsList.addItems(new Parameter("AssemblyInfo .Net Core Path:", Configs.AssemblyInfoNetCore));
        setsList.addItems(new Parameter("Exclude Directories:", String.join(";", Configs.ExcludeDirs)));
        setsList.addItems(new Parameter("Exclude Files:", String.join(";", Configs.ExcludeFiles)));
        setsList.addItems(new Parameter("Valuable Extensions:", String.join(";", Configs.ValExtensions)));

        titleBar.getCloseButton().eventMouseClick.clear();
        titleBar.getCloseButton().eventMouseClick.add((sender, args) -> {
            close();
        });
    }

    @Override
    public void show(CoreWindow handler) {
        super.show(handler);
    }

    @Override
    public void close() {
        // actions
        if (onCloseDialog != null)
            onCloseDialog.execute();

        super.close();
    }
}