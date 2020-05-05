package OwnLibs.Owls.Views.Items;

import java.awt.Color;
// import java.awt.Font;

import com.spvessel.spacevil.CheckBox;
import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.ListBox;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.Decorations.Style;
// import com.spvessel.spacevil.Flags.EmbeddedCursor;
import com.spvessel.spacevil.Flags.VisibilityPolicy;

import OwnLibs.Owls.ElementsFactory;

public class HomePage extends HorizontalStack {

    private CheckBox showOnSturtup = new CheckBox("Show this page on startup");
    private VerticalStack menuLayout = new VerticalStack();
    private VerticalStack historyLayout = new VerticalStack();
    private ListBox historyList = new ListBox();

    private Label startLabel = new Label("START");
    private Label historyLabel = new Label("HISTORY");
    private Label aboutLabel = new Label("ABOUT");

    public HomeMenuItem newFileLabel = new HomeMenuItem("Create new file...");
    public HomeMenuItem newFolderLabel = new HomeMenuItem("Create new folder...");
    public HomeMenuItem importFileLabel = new HomeMenuItem("Import file...");

    public HomeMenuItem settingsLabel = new HomeMenuItem("Settings");
    public HomeMenuItem quickTipsLabel = new HomeMenuItem("Quick tips");

    //    public History history = new History();

    public HomePage() {
        setPadding(50, 0, 10, 50);
        setSpacing(20, 0);
    }

    @Override
    public void initElements() {
        showOnSturtup.setStyle(ElementsFactory.getIndicatorStyle());
        showOnSturtup.setChecked(true);
        showOnSturtup.setMargin(0, 10, 0, 0);

        menuLayout.setSpacing(0, 10);

        historyLayout.setSpacing(0, 10);
        historyList.setBackground(57, 57, 57);
        historyList.setHScrollBarPolicy(VisibilityPolicy.NEVER);
        historyList.disableMenu(true);
        historyList.menu.setDrawable(false);
        historyList.vScrollBar.setStyle(ElementsFactory.getVScrollBarStyle());
        historyList.hScrollBar.setStyle(ElementsFactory.getHScrollBarStyle());
        historyList.setShadow(6, 0, 0, new Color(0, 0, 0, 255));
        historyList.setSelectionVisible(false);
        historyList.setPadding(3, 6, 3, 6);

        newFileLabel
                .setDescription("Opens a dialog for entering a name and creates a new file in the workspace directory");
        newFolderLabel.setDescription(
                "Opens a dialog for entering a name and creates a new folder in the workspace directory");
        importFileLabel.setDescription(
                "Opens a dialog for select an existing file and creates a copy in the workspace directory");
        settingsLabel.setDescription("Opens a settings window");
        quickTipsLabel.setDescription("Opens a window with keyboard shortcuts");

        ElementsFactory.getHeaderLabelStyle().setStyle(startLabel, aboutLabel, historyLabel);

        addItems(menuLayout, historyLayout);
        menuLayout.addItems(startLabel, newFileLabel, newFolderLabel, importFileLabel, aboutLabel, settingsLabel,
                quickTipsLabel, showOnSturtup);
        historyLayout.addItems(historyLabel, historyList);

        //
        // historyList.addItems(new HistoryRecordItem("TestCase_1"), new HistoryRecordItem("TestCase_2"));
    }

    //    public HistoryRecordItem addItemToHistoryList(String path) {
    //        int ind = path.lastIndexOf(File.separator);
    //        String name = path.substring(ind + 1);
    //        HistoryRecordItem hri = new HistoryRecordItem(name);
    //        historyList.insertItem(hri, 0);
    //        return hri;
    //    }

    public HistoryRecordItem addItemToHistoryList(HistoryRecordItem hri) {
        historyList.insertItem(hri, 0);
        return hri;
    }

    // private void setLabelCursor(Label... labels) {
    //     for (Label label : labels) {
    //         label.setCursor(EmbeddedCursor.HAND);
    //     }
    // }

    // private void setLabelHoverness(Label... labels) {
    //     for (Label label : labels) {
    //         label.eventMouseHover.add((sender, args) -> {
    //             label.setFontStyle(Font.BOLD);
    //         });
    //         label.eventMouseLeave.add((sender, args) -> {
    //             label.setFontStyle(Font.PLAIN);
    //         });
    //     }
    // }
}