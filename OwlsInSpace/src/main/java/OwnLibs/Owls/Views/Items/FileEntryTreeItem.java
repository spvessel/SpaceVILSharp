package OwnLibs.Owls.Views.Items;

import com.spvessel.spacevil.Flags.TreeItemType;

import com.spvessel.spacevil.TreeItem;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileEntryTreeItem extends TreeItem {
    private boolean editing = false;

    private String fullPath;

    private String keyWordsString = "";
    private List<String> keyWordsArr = new ArrayList<>();

    public FileEntryTreeItem(TreeItemType type) {
        super(type);
    }

    public FileEntryTreeItem(TreeItemType type, String text) {
        super(type, text);
    }

    public FileEntryTreeItem(TreeItemType type, String name, String fullPath, boolean isNew) {
        this(type, name);

        this.fullPath = fullPath;

        if (type.equals(TreeItemType.LEAF) && !isNew)
            readKeyWordsString(); // (&&(name != "")) Возможно, нужно как-то по-лучше сделать вариант с временным файлом
        //Теперь нет проверки на некорректное имя файла
    }

    @Override
    public void initElements() {
        super.initElements();
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public Boolean isDirectory() {
        return getItemType().equals(TreeItemType.BRANCH);
    }

    public String getFullPath() {
        return fullPath;
    }

    //void setFullPath(String fullPath) { this.fullPath = fullPath; }

    public String getDirectoryPath() {
        // fullPath.substring(0, fullPath.length() - getText().length() - 1);
        File f = new File(fullPath);
        return f.getParentFile().getPath();
    }

    public List<String> getKeyWordsArr() {
        return keyWordsArr;
    }

    public void clearKeyWordArr() {
        keyWordsArr = new ArrayList<>();
    }

    /**
     * Check if
     *
     * @param kWord is contains in the  keyWordsArray
     * @return true or false
     */
    public boolean isKeyWordExist(String kWord) {
        return keyWordsArr.contains(kWord);
    }

    public boolean isKeyWordExist(List<String> kWords) {
        return keyWordsArr.containsAll(kWords);
    }

    public String loadItem() {
        String s = "";
        //Обновляем строку ключевых слов. На всякий случай. Может быть не нужно это делать
        //Если будет какая-то проверка на то, изменился ли файл с момента запуска программы (в другой программе),
        //то эта штука не понадобится
        readKeyWordsString();
        try (BufferedReader br = new BufferedReader(
                new FileReader(fullPath))) { // new InputStreamReader(new FileInputStream(fullPath),
                                             // StandardCharsets.UTF_8))) { //
            br.readLine();
            s = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            //FileNotFoundException
            e.printStackTrace();
        }

        return s;
    }

    private void readKeyWordsString() {
        //Чтение строки ключевых слов из файла
        try (BufferedReader br = new BufferedReader(new FileReader(fullPath))) {// new InputStreamReader(new
                                                                                // FileInputStream(fullPath),
                                                                                // StandardCharsets.UTF_8))) { //
            keyWordsString = br.readLine();
        } catch (IOException e) {
            System.out.println("IOException during key words reading\n" + e);
            //e.printStackTrace();
        }

        if (keyWordsString != null)
            parseKeyWordsString();
    }

    private void parseKeyWordsString() {
        //Преобразование строки ключевых слов в массив
        keyWordsArr.clear();
        if (!keyWordsString.isEmpty())
            keyWordsArr.addAll(Arrays.asList(keyWordsString.split("\\s+")));

    }

    private void unparseKeyWords() {
        //Преобразование массива ключевых слов обратно в строку
        keyWordsString = String.join(" ", keyWordsArr);
    }

    public void saveItem(String text) {
        try {
            File file = new File(fullPath);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));

            // BufferedWriter out = new BufferedWriter(
            //         new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));


            unparseKeyWords(); //Формирование одной строки из массива ключевых слов

            out.write(keyWordsString + "\n");
            out.append(text);
            out.close();
        } catch (Exception e) {
            //TODO: Обработка исключений с именами файлов
        }
    }

    public boolean newFolder() {
        File dir = new File(getFullPath());
        if (!(dir.exists())) {
            return dir.mkdir();
        } else
            return false;
    }

    public boolean removeItem() {
        File file = new File(fullPath);
        if (file.exists())
            return file.delete();
        else
            return false;
    }

    /*
        private void recursiveRemove(SimpleTreeItem sti) {
            if (sti.isDirectory) {
                File file = new File(sti.getFullPath());

                Iterator<TreeItem<String>> elementListIterator = sti.getChildren().iterator();
                while (elementListIterator.hasNext()) {
                    SimpleTreeItem innerLTI = (SimpleTreeItem) elementListIterator.next();
                    recursiveRemove(innerLTI);
                }

                file.delete();
                //System.out.println("folder deleted: " + sti.getFullPath());
            } else {
                //Должна быть проверка на существование файла
                File file = new File(sti.getFullPath());
                file.delete();
                //System.out.println("file deleted: " + sti.getFullPath());
            }
        }
    */
    public boolean renameFile(String newName) {
        File file = new File(fullPath);
        fullPath = getDirectoryPath() + File.separator + newName;

        setText(newName);
        return file.renameTo(new File(fullPath));
    }

    public void closeItem() {
        //Закрытие элемента и обнуление несохраненных изменений
        parseKeyWordsString();
    }

    @Override
    public String toString() {
        return getText();
    }
}
