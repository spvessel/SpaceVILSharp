package OwnLibs.Owls;

import java.util.LinkedHashSet;
import java.util.Set;

import OwnLibs.Owls.Views.Items.FileEntryTreeItem;

public class TabsRestorator extends ConfigSerializer {

    private static final long serialVersionUID = 548645999223203392L;

    private Set<FileEntryTreeItem> _listTabs;

    private TabsRestorator() {
        _path = "lastopened.cfg";
        _listTabs = new LinkedHashSet<>();
    }

    public void addRecord(FileEntryTreeItem record) {
        if (_listTabs.contains(record)) {
            _listTabs.remove(record);
        }
        _listTabs.add(record);
    }

    public void removeRecord(FileEntryTreeItem record) {
        if (_listTabs.contains(record)) {
            _listTabs.remove(record);
        }
    }
    
    public void clearRecords() {
        _listTabs = new LinkedHashSet<>();
    }
}