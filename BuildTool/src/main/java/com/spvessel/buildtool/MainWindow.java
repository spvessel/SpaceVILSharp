package com.spvessel.buildtool;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.MSAA;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.MenuItem;
import com.spvessel.spacevil.TextArea;
import com.spvessel.spacevil.OpenEntryDialog.OpenDialogType;
import com.spvessel.spacevil.Flags.FileSystemEntryType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class MainWindow extends ActiveWindow {
    TextArea textServiceInfo;

    TextEdit v_major;
    TextEdit v_middle;
    TextEdit v_minor;
    TextEdit v_extend;

    TextEdit folderFrom;
    TextEdit folderTo;

    ButtonCore phase;
    ButtonCore month;
    ButtonCore net_version;

    @Override
    public void initWindow() {
        CommandLineEmulator.handler = this;
        setParameters("MainWindow", "MainWindow", 800, 900, false);
        setAntiAliasingQuality(MSAA.MSAA_8X);
        setMinSize(585, 500);
        setBackground(45, 45, 45);
        setPadding(2, 2, 2, 2);
        isCentered = true;

        BufferedImage ibig = null;
        BufferedImage ismall = null;
        BufferedImage isettings = null;
        BufferedImage ibrowse = null;
        try {
            ibig = ImageIO.read(MainWindow.class.getResourceAsStream("/img/ibig.png"));
            ismall = ImageIO.read(MainWindow.class.getResourceAsStream("/img/ismall.png"));
            isettings = ImageIO.read(MainWindow.class.getResourceAsStream("/img/settings.png"));
            ibrowse = ImageIO.read(MainWindow.class.getResourceAsStream("/img/browse.png"));
        } catch (IOException e) {
        }
        if (ibig != null && ismall != null)
            setIcon(ibig, ismall);

        ImageItem imgSets = new ImageItem(isettings);
        imgSets.setBackground(0, 0, 0, 0);
        imgSets.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        imgSets.setMargin(2, 2, 2, 2);
        ImageItem imgBrowse = new ImageItem(ibrowse);
        imgBrowse.setBackground(0, 0, 0, 0);
        imgBrowse.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        imgBrowse.setMargin(2, 2, 2, 2);

        ContextMenu phase_menu = getMenu();
        ContextMenu month_menu = getMenu();
        ContextMenu net_version_menu = getMenu();

        Style v_style = Style.getTextEditStyle();
        v_style.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.RIGHT);
        v_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        v_style.borderRadius = new CornerRadius(15);
        v_style.setSize(47, 30);

        TitleBar title = new TitleBar("SpaceVIL Builder Tool");
        title.setBackground(60, 60, 60);
        title.setShadow(5, 0, 3, new Color(0, 0, 0, 180));
        title.setIcon(ismall, 24, 24);

        VerticalStack layout = new VerticalStack();
        layout.setMargin(0, 30, 0, 0);
        layout.setPadding(6, 25, 6, 6);
        layout.setBackground(70, 70, 70);
        layout.setSpacing(6, 15);

        VerticalStack buildTool = new VerticalStack();
        buildTool.setSpacing(6, 15);
        buildTool.setHeightPolicy(SizePolicy.FIXED);
        buildTool.setHeight(130);

        /////////////////////////////////////////////////////////////
        VerticalStack resourceTool = new VerticalStack();
        resourceTool.setSpacing(6, 15);
        resourceTool.setVisible(false);
        resourceTool.setHeight(130);
        resourceTool.setHeightPolicy(SizePolicy.FIXED);

        HorizontalStack browseFrom = new HorizontalStack();
        browseFrom.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        browseFrom.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        browseFrom.setSize(450, 30);
        browseFrom.setSpacing(15, 0);
        browseFrom.setMargin(0, 5, 0, 0);

        HorizontalStack browseTo = new HorizontalStack();
        browseTo.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        browseTo.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        browseTo.setSize(450, 30);
        browseTo.setSpacing(15, 0);
        browseTo.setMargin(0, 0, 0, 0);

        folderFrom = new TextEdit(System.getProperty("user.home") + "\\images");
        folderFrom.setStyle(v_style);
        folderFrom.setTextAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        folderFrom.setWidthPolicy(SizePolicy.EXPAND);
        folderFrom.setShadow(5, 2, 2, new Color(0, 0, 0, 180));

        folderTo = new TextEdit(folderFrom.getText());
        folderTo.setStyle(v_style);
        folderTo.setTextAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        folderTo.setWidthPolicy(SizePolicy.EXPAND);
        folderTo.setShadow(5, 2, 2, new Color(0, 0, 0, 180));

        ButtonCore btnFrom = new ButtonCore();
        btnFrom.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        btnFrom.setSize(30, 30);
        btnFrom.setBorderRadius(15);
        btnFrom.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
        btnFrom.setBackground(100, 255, 150);

        ButtonCore btnTo = new ButtonCore();
        btnTo.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        btnTo.setSize(30, 30);
        btnTo.setBorderRadius(15);
        btnTo.setShadow(5, 2, 2, new Color(0, 0, 0, 180));

        ButtonCore makeRes = new ButtonCore("Make");
        makeRes.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        makeRes.setSize(150, 30);
        makeRes.setBorderRadius(15);
        makeRes.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
        makeRes.setAlignment(ItemAlignment.HCENTER);
        makeRes.setMargin(0, 0, 18, 0);
        makeRes.setBackground(255, 128, 128);
        /////////////////////////////////////////////////////////////

        HorizontalStack version = new HorizontalStack();
        version.setContentAlignment(ItemAlignment.HCENTER);
        version.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        version.setSize(450, 30);
        version.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        version.setSpacing(5, 0);

        HorizontalStack platform = new HorizontalStack();
        platform.setContentAlignment(ItemAlignment.HCENTER);
        platform.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        platform.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        platform.setSize(450, 30);
        platform.setSpacing(5, 0);

        ButtonCore settings = new ButtonCore();
        settings.setSize(30, 30);
        settings.setBorderRadius(15);
        settings.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
        settings.eventMouseClick.add((sender, args) -> {
            Settings sets = new Settings();
            sets.show(this);
        });

        v_major = new TextEdit();
        v_major.setText(Configs.VerMajor);
        v_major.setStyle(v_style);
        v_major.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
        v_middle = new TextEdit();
        v_middle.setText(Configs.VerMiddle);
        v_middle.setStyle(v_style);
        v_middle.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
        v_minor = new TextEdit();
        v_minor.setText(Configs.VerMinor);
        v_minor.setStyle(v_style);
        v_minor.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
        v_extend = new TextEdit();
        v_extend.setText(Configs.VerExtend);
        v_extend.setStyle(v_style);
        v_extend.setShadow(5, 2, 2, new Color(0, 0, 0, 180));

        phase = new ButtonCore();
        phase.setBorderRadius(15);
        phase.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        phase.setSize(100, 30);
        phase.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
        phase.eventMouseClick.add((sender, args) -> {
            phase_menu.show(sender, args);
        });
        month = new ButtonCore();
        month.setBorderRadius(15);
        month.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        month.setSize(100, 30);
        month.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
        month.eventMouseClick.add((sender, args) -> {
            month_menu.show(sender, args);
        });

        RadioButton java = new RadioButton("JVM / Gradle platform");
        java.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        java.setWidth(180);
        java.setChecked(true);
        java.setShadow(5, 2, 2, new Color(0, 0, 0, 180));

        RadioButton net = new RadioButton(".Net platform");
        net.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        net.setWidth(120);
        net.setMargin(28, 0, 0, 0);
        net.setShadow(5, 2, 2, new Color(0, 0, 0, 180));

        net_version = new ButtonCore();
        net_version.setBorderRadius(15);
        net_version.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        net_version.setSize(120, 30);
        net_version.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
        net_version.eventMouseClick.add((sender, args) -> {
            net_version_menu.show(sender, args);
        });

        Frame toolbar = new Frame();
        toolbar.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        toolbar.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        toolbar.setSize(450, 40);

        ButtonCore startBtn = new ButtonCore("Compile SpaceVIL");
        startBtn.setBackground(255, 128, 128);
        startBtn.setBorderRadius(15);
        startBtn.setWidth(150);
        startBtn.setHeight(30);
        startBtn.setBorderRadius(15);
        startBtn.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        startBtn.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        startBtn.setShadow(5, 2, 2, new Color(0, 0, 0, 180));

        ButtonCore revealBtn = new ButtonCore();
        revealBtn.setBackground(100, 255, 150);
        revealBtn.setWidth(30);
        revealBtn.setHeight(30);
        revealBtn.setBorderRadius(15);
        revealBtn.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        revealBtn.setAlignment(ItemAlignment.RIGHT, ItemAlignment.VCENTER);
        revealBtn.setShadow(5, 2, 2, new Color(0, 0, 0, 180));

        ButtonCore checkJVMBtn = new ButtonCore("JVM");
        checkJVMBtn.setFontStyle(Font.BOLD);
        checkJVMBtn.setBackground(255, 155, 90);
        checkJVMBtn.setWidth(50);
        checkJVMBtn.setHeight(30);
        checkJVMBtn.setBorderRadius(15);
        checkJVMBtn.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        checkJVMBtn.setAlignment(ItemAlignment.RIGHT, ItemAlignment.VCENTER);
        checkJVMBtn.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
        checkJVMBtn.setMargin(0, 0, 100, 0);
        checkJVMBtn.eventMouseClick.add((sender, args) -> {
            if (FileManager.CopyLibs(Configs.ReleasePathJVM + "\\spacevil.jar",
                    Configs.VerificateJVM + "\\libs\\spacevil.jar")) {
                appendText("Verifying JVM: " + Configs.VerificateJVM + "\\" + "run.bat");
                CommandLineEmulator.CmdExecute("cd " + Configs.VerificateJVM + " && " + "run.bat");
                appendText("Verifying: done.\n");
            }
        });

        ButtonCore checkNetBtn = new ButtonCore(".Net");
        checkNetBtn.setFontStyle(Font.BOLD);
        checkNetBtn.setWidth(50);
        checkNetBtn.setHeight(30);
        checkNetBtn.setBorderRadius(15);
        checkNetBtn.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        checkNetBtn.setAlignment(ItemAlignment.RIGHT, ItemAlignment.VCENTER);
        checkNetBtn.setShadow(5, 2, 2, new Color(0, 0, 0, 180));
        checkNetBtn.setMargin(0, 0, 40, 0);
        checkNetBtn.eventMouseClick.add((sender, args) -> {
            if (net_version.getText().equals(".Net Standard")) {
                if (FileManager.CopyLibs(Configs.ReleasePathNetStandard + "\\SpaceVIL.dll",
                        Configs.VerificateNetStandard + "\\SpaceVIL.dll")) {
                    appendText("1. -->");
                    appendText("Verifying .Net Standard: " + Configs.VerificateNetStandard + "\\" + "run.bat");
                    CommandLineEmulator.CmdExecute("cd " + Configs.VerificateNetStandard + " && " + "run.bat");
                    appendText("Verifying: done.\n");
                    appendText("--------------------------------------------------------------------------------");
                    appendText(" ");
                }
            } else {
                appendText("1. -->");
                appendText("Verifying .Net Core: " + Configs.VerificateNetCore);
                // linux
                if (FileManager.CopyLibs(Configs.ReleasePathNetCore + "\\SpaceVIL.dll",
                        Configs.VerificateNetCore + "\\SpaceVIL.dll")) {
                    CommandLineEmulator.CmdExecute("cd " + Configs.VerificateNetCore + " && " + "run.bat");
                }
                appendText("Verifying: done.\n");
                appendText("--------------------------------------------------------------------------------");
                appendText(" ");
            }
        });

        textServiceInfo = new TextArea();
        textServiceInfo.setBorderRadius(6);
        textServiceInfo.setBackground(32, 32, 32);
        textServiceInfo.setForeground(35, 255, 100);
        textServiceInfo.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        textServiceInfo.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        // textServiceInfo.setEditable(false);
        textServiceInfo.setFont(DefaultsService.getDefaultFont(12));
        VerticalScrollBar vs = textServiceInfo.vScrollBar;
        vs.setStyle(Style.getSimpleVerticalScrollBarStyle());
        // vs.slider.removeAllItemStates();
        // vs.setArrowsVisible(false);
        // vs.setBackground(0, 0, 0, 0);
        // vs.setPadding(0, 2, 0, 2);
        // vs.slider.track.setBackground(new Color(0, 0, 0, 0));
        // vs.slider.setBorderRadius(4);
        // vs.slider.setBackground(80, 80, 80);
        // vs.slider.setMargin(5, 0, 4, 0);
        HorizontalScrollBar hs = textServiceInfo.hScrollBar;
        hs.setStyle(Style.getSimpleHorizontalScrollBarStyle());
        // hs.slider.removeAllItemStates();
        // hs.setArrowsVisible(false);
        // hs.setBackground(0, 0, 0, 0);
        // hs.setPadding(2, 0, 2, 0);
        // hs.slider.track.setBackground(new Color(0, 0, 0, 0));
        // hs.slider.setBorderRadius(5);
        // hs.slider.setBackground(80, 80, 80);
        // hs.slider.setMargin(0, 5, 0, 4);
        textServiceInfo.menu.setBorderRadius(4);
        textServiceInfo.menu.setMargin(3, 3, 3, 3);
        textServiceInfo.menu.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 20)));

        // adding

        addItems(layout, title);
        layout.addItems(buildTool, resourceTool, textServiceInfo);
        buildTool.addItems(version, platform, toolbar);
        version.addItems(settings, v_major, v_middle, v_minor, v_extend, phase, month);
        platform.addItems(java, net, net_version);
        toolbar.addItems(startBtn, checkJVMBtn, checkNetBtn, revealBtn);
        settings.addItem(imgSets);
        revealBtn.addItem(imgBrowse);

        // post actions
        phase_menu.addItems(getMenuItem("ALPHA", phase), getMenuItem("BETA", phase), getMenuItem("RELEASE", phase));
        phase.setText(getPhaseByIndex(Integer.parseInt(Configs.VerPhase)));

        month_menu.addItems(getMenuItem("January", month), getMenuItem("February", month), getMenuItem("March", month),
                getMenuItem("April", month), getMenuItem("May", month), getMenuItem("June", month),
                getMenuItem("July", month), getMenuItem("August", month), getMenuItem("September", month),
                getMenuItem("October", month), getMenuItem("November", month), getMenuItem("December", month));
        month.setText(getMonthByIndex(Integer.parseInt(Configs.VerMonth)));

        net_version_menu.addItems(getMenuItem(".Net Standard", net_version), getMenuItem(".Net Core", net_version));
        net_version.setText(".Net Standard");

        // events
        revealBtn.eventMouseClick.add((sender, args) -> {
            CommandLineEmulator.Execute("explorer.exe " + ((java.isChecked()) ? Configs.ReleasePathJVM
                    : ((net_version.getText().equals(".Net Standard")) ? Configs.ReleasePathNetStandard
                            : Configs.ReleasePathNetCore)));
        });
        startBtn.eventMouseClick.add((args, sender) -> {
            Configs.VerMajor = v_major.getText();
            Configs.VerMiddle = v_middle.getText();
            Configs.VerMinor = v_minor.getText();
            Configs.VerExtend = v_extend.getText();
            Configs.VerPhase = getPhaseByWord(phase.getText());
            Configs.VerMonth = getMonthByWord(month.getText());
            Configs.saveConfig(Configs.ConfPath);

            Thread task = new Thread(() -> {
                startProg(java.isChecked());
                PopUpMessage pop = new PopUpMessage("Compile done.");
                pop.setMargin(new Indents(50, 50, 50, 20));
                pop.setSize(200, 70);
                pop.removeAllItemStates();
                pop.setBackground(64, 64, 64);
                pop.setBorderRadius(6);
                pop.setTimeOut(2000);
                pop.show(this);
            });
            task.start();
        });

        SwitchItem switcher = new SwitchItem();
        switcher.setMargin(200, 0, 0, 0);
        addItem(switcher);
        switcher.AddMenuItem(SwitchItem.getSwitchItem("Build Tool", new InterfaceMouseMethodState() {
            @Override
            public void execute(InterfaceItem sender, MouseArgs args) {
                resourceTool.setVisible(false);
                buildTool.setVisible(true);
            }
        }));
        switcher.AddMenuItem(SwitchItem.getSwitchItem("Resource Tool", new InterfaceMouseMethodState() {
            @Override
            public void execute(InterfaceItem sender, MouseArgs args) {
                buildTool.setVisible(false);
                resourceTool.setVisible(true);
            }
        }));

        resourceTool.addItems(browseFrom, browseTo, makeRes);
        browseFrom.addItems(folderFrom, btnFrom);
        browseTo.addItems(folderTo, btnTo);
        ImageItem imgFrom = new ImageItem(ibrowse);
        imgFrom.setBackground(0, 0, 0, 0);
        imgFrom.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        imgFrom.setMargin(2, 2, 2, 2);
        btnFrom.addItem(imgFrom);
        ImageItem imgTo = new ImageItem(ibrowse);
        imgTo.setBackground(0, 0, 0, 0);
        imgTo.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        imgTo.setMargin(2, 2, 2, 2);
        btnTo.addItem(imgTo);

        btnFrom.eventMouseClick.add((sender, args) -> {
            OpenEntryDialog opd = new OpenEntryDialog("Select Directory:", FileSystemEntryType.DIRECTORY,
                    OpenDialogType.OPEN);
            opd.onCloseDialog.add(() -> {
                if (opd.getResult() != null)
                folderFrom.setText(opd.getResult());
            });
            opd.show(this);
        });

        btnTo.eventMouseClick.add((sender, args) -> {
            OpenEntryDialog opd = new OpenEntryDialog("Select Directory:", FileSystemEntryType.FILE,
                    OpenDialogType.SAVE);
            opd.onCloseDialog.add(() -> {
                if (opd.getResult() != null)
                    folderTo.setText(opd.getResult());
            });
            opd.show(this);
        });

        makeRes.eventMouseClick.add((sender, args) -> {
            makeResourceFile();
        });
    }

    private void startProg(boolean isJava) {
        if (isJava) { // java stuff
            // 0. Clear Libs
            FileManager.ClearLibs(Configs.ReleasePathJVM + "\\spacevil.jar");

            // 1. copy src -> template
            appendText("1. -->");
            appendText("Copy:" + Configs.SrcPathJVM + "\\src" + " -> " + Configs.TemplatePathJVM + "\\src");
            List<String> srcList = FileManager.CopyFolder(Configs.SrcPathJVM + "\\src",
                    Configs.TemplatePathJVM + "\\src", null);
            appendText("Copy: done.\n");
            appendText(" ");

            File commonPath = null;
            for (String s : srcList) {
                if (s.contains("CommonService"))
                    commonPath = new File(s);
            }
            if (commonPath != null)
                setVersion(commonPath);

            // 2. exec compile
            appendText("2. --> ");
            appendText("Compile JVM: " + Configs.TemplatePathJVM + "\\" + Configs.jvm_compile);
            // String outStr = ;
            CommandLineEmulator.CmdExecute("cd " + Configs.TemplatePathJVM + " && " + Configs.jvm_compile);
            // appendText(outStr);
            appendText("Compile: done.");
            appendText(" ");

            // 3. copy spacevil.jar -> release
            appendText("3. --> ");
            appendText("Extract Release JVM: " + Configs.TemplatePathJVM + "\\build\\libs -> " + Configs.ObfPathJVM
                    + "\\spacevil.jar");
            FileManager.CopyLibs(Configs.TemplatePathJVM + "\\build\\libs\\spacevil.jar",
                    Configs.ObfPathJVM + "\\spacevil.jar");
            appendText("Extract Release JVM: done.");
            appendText(" ");

            // 4. delete spacevil.jar & template list
            appendText("4. --> ");
            appendText("Clear Template JVM...");
            FileManager.RemoveAddedItems(srcList);
            appendText("Clear Template JVM: done.");
            appendText(" ");

            // 5. obfuscation
            appendText("5. --> ");
            appendText("Obfuscating SpaceVIL: JVM version.");
            // outStr = ;
            CommandLineEmulator.CmdExecute("cd " + Configs.ObfPathJVM + " && " + Configs.obfJava_run);
            // appendText(outStr);
            FileManager.CopyLibs(Configs.ObfPathJVM + "\\spacevil-obf.jar", Configs.ReleasePathJVM + "\\spacevil.jar");
            appendText("Obfuscating: done.");
            appendText(" ");

            // 6. make zip
            appendText("6. --> ");
            appendText("Zipping SpaceVIL: JVM version.");
            FileManager.MakeZip(Configs.ReleasePathJVM, Configs.ZipDir + "\\" + Configs.ZipJVM);
            appendText("Zipping: done.");
            appendText("--------------------------------------------------------------------------------");
            appendText(" ");
        } else { // cs stuff
            if (net_version.getText().equals(".Net Standard")) {
                // 0. Clear Libs
                FileManager.ClearLibs(Configs.ReleasePathNetStandard + "\\SpaceVIL.dll");
                // 1. copy src -> template
                appendText("1. -->");
                appendText("Copy: " + Configs.SrcPathNet + " -> " + Configs.TemplatePathNetStandard);
                List<String> srcList = FileManager.CopyFolder(Configs.SrcPathNet, Configs.TemplatePathNetStandard,
                        null);
                appendText("Copy: done.");
                appendText(" ");

                File commonPath = null;
                for (String s : srcList) {
                    if (s.contains("CommonService"))
                        commonPath = new File(s);
                }
                if (commonPath != null)
                    setVersion(commonPath);

                setAssembly(Configs.TemplatePathNetStandard + "\\" + Configs.AssemblyInfoNetStandard);

                // 2. exec compile
                appendText("2. -->");
                appendText("Compile .Net Standard: " + Configs.TemplatePathNetStandard + "\\"
                        + Configs.netStandard_compile);
                // String outStr = ;
                CommandLineEmulator
                        .CmdExecute("cd " + Configs.TemplatePathNetStandard + " && " + Configs.netStandard_compile);
                // appendText(outStr);
                appendText("Compile: done.");
                appendText(" ");

                // 3. copy spacevil.jar -> release
                appendText("3. -->");
                appendText("Extract Release .Net Standard: " + Configs.TemplatePathNetStandard + " -> "
                        + Configs.ObfPathNet + "\\input");
                FileManager.CopyLibs(Configs.TemplatePathNetStandard + "\\SpaceVIL.dll",
                        Configs.ObfPathNet + "\\input\\SpaceVIL.dll");
                appendText("Extract Release .Net: done.");
                appendText(" ");

                // 4. delete spacevil.jar & template list
                appendText("4. -->");
                appendText("Clear Template Net...");
                FileManager.RemoveAddedItems(srcList);
                appendText("Clear Template Net: done.");
                appendText(" ");

                // 5. obfuscation
                appendText("5. -->");
                appendText("Obfuscating SpaceVIL: .Net version. " + "cd " + Configs.ObfPathNet + " && "
                        + Configs.obfNet_start);
                // outStr = ;
                CommandLineEmulator.CmdExecute("cd " + Configs.ObfPathNet + " && " + Configs.obfNet_start);
                // appendText(outStr);
                FileManager.CopyLibs(Configs.ObfPathNet + "\\SpaceVIL.dll",
                        Configs.ReleasePathNetStandard + "\\SpaceVIL.dll");
                appendText("Obfuscating: done.");
                appendText(" ");

                // 6. make zip
                appendText("5. -->");
                appendText("Zipping SpaceVIL: .Net version.");
                FileManager.MakeZip(Configs.ReleasePathNetStandard, Configs.ZipDir + "\\" + Configs.ZipNetStandard);
                appendText("Zipping: done.");
                appendText("--------------------------------------------------------------------------------");
                appendText(" ");
            } else {
                // 0. Clear Libs
                FileManager.ClearLibs(Configs.ReleasePathNetCore + "\\SpaceVIL.dll");

                // 1. copy src -> template
                appendText("1. -->");
                appendText("Copy: " + Configs.SrcPathNet + " -> " + Configs.TemplatePathNetCore);
                List<String> srcList = FileManager.CopyFolder(Configs.SrcPathNet, Configs.TemplatePathNetCore, null);
                appendText("Copy: done.");
                appendText(" ");

                File commonPath = null;
                for (String s : srcList) {
                    if (s.contains("CommonService"))
                        commonPath = new File(s);
                }
                if (commonPath != null)
                    setVersion(commonPath);

                setAssembly(Configs.TemplatePathNetCore + "\\" + Configs.AssemblyInfoNetCore);

                appendText("2. -->");
                // NETCORE///////////////////////////
                // 2. exec compile
                appendText("Compile Net Core: " + Configs.TemplatePathNetCore + "\\"
                        + Configs.netCore_compile);
                // setNetCorePackage(Configs.TemplatePathNetCore + "\\SpaceVIL.csproj", 0);
                // String outStr = ;
                CommandLineEmulator.CmdExecute(
                        "cd " + Configs.TemplatePathNetCore + " && dotnet restore && " + Configs.netCore_compile);
                // appendText(outStr);
                appendText("Compile: done.\n");
                appendText(" ");
                // 3. copy spacevil.jar -> release
                appendText("Extract Release .Net Core LINUX: " + Configs.TemplatePathNetCore
                        + "\\bin\\Release\\netcoreapp2.1 -> " + Configs.ObfPathNet + "\\input\\SpaceVIL.dll");
                FileManager.CopyLibs(Configs.TemplatePathNetCore + "\\bin\\Release\\netcoreapp2.1\\SpaceVIL.dll",
                        Configs.ObfPathNet + "\\input\\SpaceVIL.dll");
                appendText("Extract Release .Net: done.\n");
                appendText(" ");

                // 5. obfuscation
                appendText("Obfuscating SpaceVIL: .Net version. " + "cd " + Configs.ObfPathNet + " && "
                        + Configs.obfNet_start);
                // outStr = ;
                CommandLineEmulator.CmdExecute("cd " + Configs.ObfPathNet + " && " + Configs.obfNet_start);
                // appendText(outStr);
                FileManager.CopyLibs(Configs.ObfPathNet + "\\SpaceVIL.dll",
                        Configs.ReleasePathNetCore + "\\SpaceVIL.dll");
                appendText("Obfuscating: done.\n");
                appendText(" ");
                /////////////////////////////////////

                // 4. delete spacevil.jar & template list

                appendText("3. -->");
                appendText("Clear Template Net...");
                FileManager.RemoveAddedItems(srcList);
                appendText("Clear Template Net: done.\n");
                appendText(" ");

                // 6. make zip
                appendText("4. -->");
                appendText("Zipping SpaceVIL: .Net version.");
                FileManager.MakeZip(Configs.ReleasePathNetCore, Configs.ZipDir + "\\" + Configs.ZipNetCore);
                appendText("Zipping: done.\n");
                appendText("--------------------------------------------------------------------------------");
                appendText(" ");
            }
        }
    }

    void appendText(String text) {
        // String separator =
        // "\n--------------------------------------------------------------------------------";
        if (text == null || text.equals(""))
            return;
        String oldText = textServiceInfo.getText();
        if (oldText == null || oldText.equals(""))
            textServiceInfo.appendText(text);
        else
            textServiceInfo.appendText("\n" + text);
    }

    private MenuItem getMenuItem(String name, ButtonCore selection) {
        MenuItem menu = new MenuItem(name);
        menu.setForeground(210, 210, 210);
        menu.setMargin(1, 1, 1, 1);
        menu.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 120)));
        menu.setBorderRadius(3);
        menu.eventMouseClick.add((sender, args) -> {
            selection.setText(name);
        });
        return menu;
    }

    private void setVersion(File commonPath) {
        BufferedReader br = null;
        FileReader fr = null;

        try {
            // br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader(commonPath);
            br = new BufferedReader(fr);

            String sCurrentLine;
            StringBuilder outsb = new StringBuilder();
            while ((sCurrentLine = br.readLine()) != null) {
                String str = sCurrentLine;
                if (sCurrentLine.contains("private static String _version = ")) {
                    int ind = sCurrentLine.indexOf("=");
                    str = sCurrentLine.substring(0, ind + 2) + getFullVersion();
                }
                outsb.append(str);
                outsb.append("\n");
            }

            File file = new File(commonPath.getPath());
            // file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(outsb.toString());
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // private void setNetCorePackage(String projectName, int indexVersion) {
    //     File commonPath = null;
    //     commonPath = new File(projectName);
    //     if (commonPath != null)
    //         setVersion(commonPath);

    //     BufferedReader br = null;
    //     FileReader fr = null;
    //     boolean packRef = true;

    //     try {
    //         fr = new FileReader(projectName);
    //         br = new BufferedReader(fr);

    //         String sCurrentLine;
    //         StringBuilder outsb = new StringBuilder();
    //         while ((sCurrentLine = br.readLine()) != null) {
    //             String str = sCurrentLine;
    //             if (sCurrentLine.contains("PackageReference")) {
    //                 if (packRef) {
    //                     if (indexVersion == 0 || indexVersion == 2) {
    //                         str = "<PackageReference Include=\"System.Drawing.Common\" Version=\"4.6.0-preview7.19362.9\" />";
    //                     } else if (indexVersion == 1) {
    //                         str = "<PackageReference Include=\"System.Drawing.Common\" Version=\"4.6.0-preview7.19362.9\" />";
    //                         // str += "\n";
    //                         // str += "<PackageReference Include=\"runtime.osx.10.10-x64.CoreCompat.System.Drawing\" Version=\"5.8.64\" />";
    //                     }
    //                     packRef = false;
    //                 } else {
    //                     str = "";
    //                 }
    //             }
    //             outsb.append(str);
    //             outsb.append("\n");
    //         }

    //         File file = new File(projectName);
    //         // file.createNewFile();
    //         FileWriter writer = new FileWriter(file);
    //         writer.write(outsb.toString());
    //         writer.flush();
    //         writer.close();

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     } finally {
    //         try {
    //             if (br != null)
    //                 br.close();
    //             if (fr != null)
    //                 fr.close();
    //         } catch (IOException ex) {
    //             ex.printStackTrace();
    //         }
    //     }
    // }

    private String getFullVersion() {
        StringBuilder sb = new StringBuilder("\"");
        sb.append(getVersionNumber());
        sb.append("-");
        sb.append(phase.getText());
        sb.append(" - ");
        sb.append(month.getText());
        sb.append(" 2019\";");
        return sb.toString();
    }

    private String getVersionNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append(v_major.getText());
        sb.append(".");
        sb.append(v_middle.getText());
        sb.append(".");
        sb.append(v_minor.getText());
        sb.append(".");
        sb.append(v_extend.getText());
        return sb.toString();
    }

    private void setAssembly(String filePath) {
        System.out.println(filePath);
        File assFile = new File(filePath);
        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(assFile);
            br = new BufferedReader(fr);

            String sCurrentLine;
            StringBuilder outsb = new StringBuilder();
            while ((sCurrentLine = br.readLine()) != null) {
                String str = sCurrentLine;
                if (sCurrentLine.contains("AssemblyVersion") || sCurrentLine.contains("AssemblyFileVersion")
                        || sCurrentLine.contains("AssemblyInformationalVersion")) {
                    int ind = sCurrentLine.indexOf("(");
                    str = sCurrentLine.substring(0, ind + 2) + getVersionNumber() + "\")]";
                }
                outsb.append(str);
                outsb.append("\n");
            }

            File file = new File(filePath);
            // file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(outsb.toString());
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getPhaseByIndex(int index) {
        if (index == 0)
            return "ALPHA";
        else if (index == 1)
            return "BETA";
        else
            return "RELEASE";
    }

    public String getPhaseByWord(String word) {
        if (word.equals("ALPHA"))
            return "0";
        else if (word.equals("BETA"))
            return "1";
        else
            return "2";
    }

    public String getMonthByIndex(int index) {
        if (index == 1)
            return "January";
        else if (index == 2)
            return "February";
        else if (index == 3)
            return "March";
        else if (index == 4)
            return "April";
        else if (index == 5)
            return "May";
        else if (index == 6)
            return "June";
        else if (index == 7)
            return "July";
        else if (index == 8)
            return "August";
        else if (index == 9)
            return "September";
        else if (index == 10)
            return "October";
        else if (index == 11)
            return "November";
        else if (index == 12)
            return "December";
        else
            return "January";
    }

    public String getMonthByWord(String word) {
        if (word.equals("January"))
            return "1";
        else if (word.equals("February"))
            return "2";
        else if (word.equals("March"))
            return "3";
        else if (word.equals("April"))
            return "4";
        else if (word.equals("May"))
            return "5";
        else if (word.equals("June"))
            return "6";
        else if (word.equals("July"))
            return "7";
        else if (word.equals("August"))
            return "8";
        else if (word.equals("September"))
            return "9";
        else if (word.equals("October"))
            return "10";
        else if (word.equals("November"))
            return "11";
        else if (word.equals("December"))
            return "12";
        else
            return "1";
    }

    public ContextMenu getMenu() {
        ContextMenu menu = new ContextMenu(this);
        menu.setWidth(100);
        menu.setBorderRadius(5);
        menu.setBorderThickness(1);
        menu.setBorderFill(32, 32, 32);
        menu.setBackground(60, 60, 60);
        menu.setWidth(100);
        menu.itemList.setSelectionVisible(false);
        menu.activeButton = MouseButton.BUTTON_LEFT;
        return menu;
    }

    private void makeResourceFile() {
        File outFile = new File(folderTo.getText());
        try {
            outFile.createNewFile();
            FileWriter writer = new FileWriter(outFile);
            writer.write("----------------Net Core resources:");
            writer.write(System.lineSeparator());
            makeNetCore(writer);
            writer.write(System.lineSeparator());
            writer.write(System.lineSeparator());
            writer.write("----------------Net Standard resources:");
            writer.write(System.lineSeparator());
            makeNetStandard(writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            appendText("Enable to create out file");
            return;
        }
    }

    private void makeNetCore(FileWriter writer) throws IOException {
        appendText("Make Net Core Resources\nUsing folder: " + folderFrom.getText());

        File resFolder = new File(folderFrom.getText());

        File[] filesList = resFolder.listFiles();
        if (filesList == null) {
            appendText("No files in the folder");
            return;
        }

        writer.write("<ItemGroup>");
        writer.write(System.lineSeparator());
        for (File resFile : filesList) {
            if (resFile.getName().endsWith(".txt"))
                continue;
            writer.write("  <None Remove=\"" + resFolder.getName() + "\\" + resFile.getName() + "\" />");
            writer.write(System.lineSeparator());

            appendText("    file: " + resFile.getName());
        }
        writer.write("</ItemGroup>");
        writer.write(System.lineSeparator());
        writer.write(System.lineSeparator());

        writer.write("<ItemGroup>");
        writer.write(System.lineSeparator());
        for (File resFile : filesList) {
            if (resFile.getName().endsWith(".txt"))
                continue;
            writer.write("  <EmbeddedResource Include=\"" + resFolder.getName() + "\\" + resFile.getName() + "\" />");
            writer.write(System.lineSeparator());
        }
        writer.write("</ItemGroup>");
        writer.write(System.lineSeparator());

        appendText("Net Core Resources is done\n");
    }

    private void makeNetStandard(FileWriter writer) throws IOException {
        appendText("Make Net Standard Resources\nUsing folder: " + folderFrom.getText());

        File resFolder = new File(folderFrom.getText());

        File[] filesList = resFolder.listFiles();
        if (filesList == null) {
            appendText("No files in the folder");
            return;
        }

        for (File resFile : filesList) {
            if (resFile.getName().endsWith(".txt"))
                continue;

            writer.write("-res:" + resFolder.getName() + "\\" + resFile.getName() + ",SpaceVIL." + resFolder.getName()
                    + "." + resFile.getName() + " ^");
            writer.write(System.lineSeparator());

            appendText("    file: " + resFile.getName());
        }

        appendText("Net Standard Resources is done\n");
    }
}
