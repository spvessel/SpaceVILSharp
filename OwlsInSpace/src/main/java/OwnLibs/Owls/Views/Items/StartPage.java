package OwnLibs.Owls.Views.Items;

import java.awt.Color;
import java.awt.Font;

import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.EmbeddedCursor;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

public class StartPage extends VerticalStack {

    Label startLabel = new Label("Start");
    Label recentLabel = new Label("Recent");
    Label aboutLabel = new Label("About");

    public Label newFileLabel = new Label("Create new file...", true);
    public Label newFolderLabel = new Label("Create new folder...", true);
    public Label importFileLabel = new Label("Import file...", true);

    public Label settingsLabel = new Label("Settings", true);
    public Label quickTipsLabel = new Label("Quick tips", true);

    public Label recentCase1Label = new Label("TestCase_1", true);
    public Label recentCase2Label = new Label("TestCase_1", true);
    public Label recentMoreLabel = new Label("more...", true);

    public StartPage() {
        setPadding(100, 0, 50, 50);
        setSpacing(0, 10);
    }

    @Override
    public void initElements() {
        getHeaderLabelStyle().setStyle(startLabel, recentLabel, aboutLabel);

        getCasualLabelStyle().setStyle(newFileLabel, newFolderLabel, importFileLabel, settingsLabel, quickTipsLabel,
                recentCase1Label, recentCase2Label, recentMoreLabel);
        setLabelHoverness(newFileLabel, newFolderLabel, importFileLabel, settingsLabel, quickTipsLabel,
                recentCase1Label, recentCase2Label, recentMoreLabel);
        setLabelCursor(newFileLabel, newFolderLabel, importFileLabel, settingsLabel, quickTipsLabel);

        addItems(startLabel, newFileLabel, newFolderLabel, importFileLabel, recentLabel, recentCase1Label,
                recentCase2Label, recentMoreLabel, aboutLabel, settingsLabel, quickTipsLabel);
    }

    private Style getHeaderLabelStyle() {
        Style style = Style.getLabelStyle();
        style.foreground = new Color(20, 180, 255);
        style.font = DefaultsService.getDefaultFont(16);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        style.height = 50;
        style.setMargin(0, 30, 0, 0);
        return style;
    }

    private Style getCasualLabelStyle() {
        Style style = Style.getLabelStyle();
        style.font = DefaultsService.getDefaultFont(12);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        style.height = 25;
        return style;
    }

    private void setLabelCursor(Label... labels) {
        for (Label label : labels) {
            label.setCursor(EmbeddedCursor.HAND);
        }
    }

    private void setLabelHoverness(Label... labels) {
        for (Label label : labels) {
            label.eventMouseHover.add((sender, args) -> {
                label.setFontStyle(Font.BOLD);
            });
            label.eventMouseLeave.add((sender, args) -> {
                label.setFontStyle(Font.PLAIN);
            });
        }
    }
}