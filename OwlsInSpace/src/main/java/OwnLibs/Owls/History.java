package OwnLibs.Owls;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class History implements Serializable {

    private static final long serialVersionUID = -6879691495422628222L;

    private int _capacity = 50;
    public Set<String> listHistory;

    public List<String> getRecords() {
        List<String> list = new LinkedList<>(listHistory);
        Collections.reverse(list);
        return list;
    }

    String _path;

    public History() {
        _path = "history.cfg";
        listHistory = new LinkedHashSet<>();
    }

    public void addRecord(String record) {
        if (listHistory.contains(record)) {
            listHistory.remove(record);
        }
        if (listHistory.size() < _capacity)
            listHistory.add(record);
    }

    public void removeRecord(String record) {
        if (listHistory.contains(record)) {
            listHistory.remove(record);
        }
    }

    public void clearRecords() {
        listHistory = new LinkedHashSet<>();
    }

    public static void serialize(History history) {
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(history._path)));
            encoder.writeObject(history);
            encoder.close();
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("ERROR: While Creating or Opening the File dvd.xml");
        }
    }

    public static History deserialize(String path) {
        XMLDecoder encoder = null;
        try {
            encoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(path)));
            History h = (History) encoder.readObject();
            encoder.close();
            return h;
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("ERROR: While Creating or Opening the File dvd.xml");
            return new History();
        }
    }
}