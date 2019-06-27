package OwnLibs.Owls;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class ConfigSerializer implements Serializable {
    private static final long serialVersionUID = 6613167333899480006L;
    protected String _path = "";

    public void serialize() {
        try {
            FileOutputStream fos = new FileOutputStream(_path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
        } catch (Exception exception) {
            System.out.println("Exception while try serializing.");
            exception.printStackTrace();
        }
    }

    public ConfigSerializer deserialize() {
        try {
            FileInputStream fis = new FileInputStream(_path);
            ObjectInputStream oin = new ObjectInputStream(fis);
            ConfigSerializer instance = (ConfigSerializer) oin.readObject();
            oin.close();
            return instance;
        } catch (Exception exception) {
            System.out.println("Exception while try deserializing.");
            exception.printStackTrace();
            return null;
        }
    }
}