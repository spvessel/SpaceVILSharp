package OwnLibs.Owls.Views.Windows;

import java.awt.image.BufferedImage;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.CheckBox;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.Rectangle;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

import OwnLibs.Owls.Views.Items.SelectionMarker;
import OwnLibs.Owls.Views.Items.SettingsCommon;
import OwnLibs.Owls.Views.Items.SettingsEditor;
import OwnLibs.Owls.Views.Items.SettingsShortcuts;

public class SettingsWindow extends ActiveWindow {
    TitleBar titleBar;
    HorizontalStack tabSections;
    CheckBox openInEditMode;
    SettingsCommon _commonView;
    SelectionMarker _commonTab;
    SettingsEditor _editorView;
    SelectionMarker _editorTab;
    SettingsShortcuts _shortcutsView;
    SelectionMarker _shortcutsTab;
    private Rectangle _subline;

    @Override
    public void initWindow() {
        setParameters("Settings", "Settings", 800, 600, false);
        setMinSize(400, 400);
        setPadding(2, 2, 2, 2);
        isCentered = true;

        BufferedImage iBig = DefaultsService.getDefaultImage(EmbeddedImage.GEAR, EmbeddedImageSize.SIZE_64X64);
        BufferedImage iSmall = DefaultsService.getDefaultImage(EmbeddedImage.GEAR, EmbeddedImageSize.SIZE_32X32);
        setIcon(iBig, iSmall);

        titleBar = new TitleBar("Settings");
        titleBar.setIcon(iSmall, 16, 16);
        titleBar.setTextMargin(new Indents(10, 0, 0, 0));

        Frame layout = new Frame();
        layout.setMargin(0, 30, 0, 0);
        layout.setPadding(0, 20, 0, 0);
        layout.setBackground(55, 55, 55);

        tabSections = new HorizontalStack();
        tabSections.setContentAlignment(ItemAlignment.HCENTER);
        tabSections.setHeightPolicy(SizePolicy.FIXED);
        tabSections.setHeight(30);

        _subline = new Rectangle();
        _subline.setBackground(32, 32, 32);
        _subline.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        _subline.setHeight(1);
        _subline.setMargin(0, 30, 0, 0);

        openInEditMode = new CheckBox("Open files in EditMode");

        _commonView = new SettingsCommon();
        _commonTab = new SelectionMarker("COMMON", _commonView);
        _editorView = new SettingsEditor();
        _editorTab = new SelectionMarker("EDITOR", _editorView);
        _shortcutsView = new SettingsShortcuts();
        _shortcutsTab = new SelectionMarker("SHORTCUTS", _shortcutsView);

        addItems(titleBar, layout);
        layout.addItems(tabSections, _subline, _commonView, _editorView, _shortcutsView);
        tabSections.addItems(_commonTab, _editorTab, _shortcutsTab);

        _commonTab.setToggled(true);
    }
}