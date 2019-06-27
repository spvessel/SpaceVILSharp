package OwnLibs.Owls;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        // for (String record : list) {
        // System.out.println(record);
        // }
        // Collections.reverse(list);
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
        try {

            FileOutputStream fos = new FileOutputStream(history._path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(history);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            System.out.println("exception while try serializing.");
            e.printStackTrace();
        }
    }

    public static History deserialize(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return new History();
            }
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream oin = new ObjectInputStream(fis);
            History h = (History) oin.readObject();
            oin.close();
            return h;
        } catch (Exception ex) {
            System.out.println("exception while try deserializing.");
            return new History();
        }
    }
}