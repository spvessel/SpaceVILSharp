package OwnLibs.Owls;

import OwnLibs.Owls.Views.Items.HistoryRecordItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

class History {

    private static final long serialVersionUID = -6879691495422628222L;

    private int _capacity = 50;
    private Controller _controller;
    // public Set<String> listHistory;
    private Map<String, HistoryRecordItem> historyPairs;

    // public List<String> getRecords() {
    // List<String> list = new LinkedList<>(listHistory);
    // // for (String record : list) {
    // // System.out.println(record);
    // // }
    // // Collections.reverse(list);
    // return list;
    // }

    private String _path;

    History(Controller controller) {
        _controller = controller;
        _path = "history.cfg";
        historyPairs = new LinkedHashMap<>();

    }

    void addRecord(String record) {
        if (historyPairs.containsKey(record)) {
            historyPairs.get(record).remove();
        }
        if (historyPairs.size() == _capacity) {
            String first = historyPairs.keySet().toArray(new String[0])[0];
            historyPairs.get(first).remove();
        }

        addNewRecord(record);
    }

    private void addNewRecord(String recordPath) {
        HistoryRecordItem hri = new HistoryRecordItem(recordPath);
        historyPairs.put(recordPath, hri);
        _controller.historyAddRecordSetEvent(hri, recordPath);
        hri.eventOnRemove.add(() ->
            removeRecord(hri));
    }

    private void removeRecord(HistoryRecordItem hri) {
//        if (historyPairs.containsKey(hri.getRecordPath())) {
            historyPairs.remove(hri.getRecordPath());
//        }
    }

    private void clearRecords() {
        for (HistoryRecordItem hri : historyPairs.values()) {
            hri.remove();
        }
        historyPairs = new LinkedHashMap<>();
    }

    private void fillHistoryRecords(List<String> recordsList) {
        if (historyPairs.size() != 0) {
            clearRecords();
        }

        // for (int i = recordsList.size() - 1; i >= 0; i--) {
        // addRecord(recordsList.get(i));
        // }

        for (String record : recordsList) {
            addRecord(record);
        }
    }

    void serialize() {
        try {
            FileOutputStream fos = new FileOutputStream(_path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            List<String> list = new LinkedList<>(historyPairs.keySet());
            oos.writeObject(list); // history);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            System.out.println("exception while try serializing. " + e);
            // e.printStackTrace();
        }
    }

    void deserialize() {
        try {
            File f = new File(_path);
            if (!f.exists()) {
                clearRecords();
                return; // new History();
            }
            FileInputStream fis = new FileInputStream(_path);
            ObjectInputStream oin = new ObjectInputStream(fis);

            List<String> recordsSet = (LinkedList<String>) oin.readObject();
            // History h = (History) oin.readObject();
            oin.close();
            // return h;

            fillHistoryRecords(recordsSet);
        } catch (Exception ex) {
            System.out.println("exception while try deserializing. " + ex);
            return; // new History();
        }
    }
}