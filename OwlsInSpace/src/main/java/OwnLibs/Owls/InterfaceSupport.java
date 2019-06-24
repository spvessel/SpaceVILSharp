package OwnLibs.Owls;

import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.TreeItemType;
import com.spvessel.spacevil.TreeItem;
import com.spvessel.spacevil.Core.MouseArgs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class InterfaceSupport {
    public static Controller controller;
    private static boolean owlMode = true; // true - tree mode, false - find mode.

    static OwlsTreeItem makeOwlItem(String name, String fullPath, TreeItemType treeItemType, OwlsTreeItem parentItem,
            boolean isNew) {
        OwlsTreeItem owlItem;

        owlItem = new OwlsTreeItem(treeItemType, name, fullPath, isNew);

        if (parentItem != null)
            parentItem.addItem(owlItem);

        owlItem.eventMouseDoubleClick.add((sender, args) -> {
            controller.loadFileLauncher(owlItem);
        });

        owlItem.eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.ENTER || args.key == KeyCode.NUMPADENTER)
                controller.loadFileLauncher(owlItem);
        });
        return owlItem;
    }

    static OwlsTreeItem makeOwlItem(String name, String fullPath, TreeItemType treeItemType, OwlsTreeItem parentItem) {
        return makeOwlItem(name, fullPath, treeItemType, parentItem, false);
    }

    private static OwlsTreeItem makeOwlItem(OwlsTreeItem copyingItem, OwlsTreeItem newParent) {
        return makeOwlItem(copyingItem.getText(), copyingItem.getFullPath(), copyingItem.getItemType(), newParent);
    }

    // Создание иерархической структуры элементов типа
    // LibTreeItem-------------------------------------------------------
    // Скорее всего, можно заменить на walkFilesTree
    static OwlsTreeItem filesTreeMaker(String rootFolder, OwlsTreeItem rootSti) {
        // OwlsTreeItem rootSti = makeOwlItem(rootFolder.substring(2), rootFolder,
        // TreeItemType.BRANCH, null);
        rootSti.setExpanded(true);
        if ((new File(rootFolder).exists()) && (new File(rootFolder).isDirectory())) {
            rootSti = makeFilesTree(rootFolder, rootSti);
        } else { // Это на случай, если указать корневую папку, которая не существует. Здесь
                 // должно быть предупреждение и
            // предложение создать ее. Но возможно, это будет производиться вообще в другом
            // месте
            new File(rootFolder).mkdir();
        }
        // refreshDir(rootSti);
        return rootSti;
    }

    private static OwlsTreeItem makeFilesTree(String folderPath, OwlsTreeItem rootBranch) {
        File fileFolder = new File(folderPath);
        File[] fileList = fileFolder.listFiles();

        if (fileList == null)
            return rootBranch;

        for (File f : fileList) {
            String name = f.getName();

            if (f.isDirectory()) { // Если элемент - папка
                OwlsTreeItem branch = makeOwlItem(name, f.getPath(), TreeItemType.BRANCH, rootBranch);
                // branch.eventMouseDoubleClick.add((sender, args) ->
                // controller.loadFileLauncher(branch));
                makeFilesTree(f.getPath(), branch);
                // refreshDir(branch); //После заполнения папки, упорядочить ее элементы
            } else { // Если элемент - файл
                OwlsTreeItem oti = makeOwlItem(name, f.getPath(), TreeItemType.LEAF, rootBranch);
                // oti.eventMouseDoubleClick.add((sender, args) ->
                // controller.loadFileLauncher(oti));
            }
        }

        return rootBranch;
    }

    // private static Pattern BOUNDARYSPLIT =
    // Pattern.compile("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
    // static void refreshDir(OwlsTreeItem dir) {
    // dir.getChildren().sort((OwlsTreeItem ti1, OwlsTreeItem ti2) -> {
    // //Проверять преобразование в sti или нет?
    // SimpleTreeItem sti1 = ti1.getValue();
    // SimpleTreeItem sti2 = ti2.getValue();
    // if (sti1.isDirectory() != sti2.isDirectory()) {
    // if (sti1.isDirectory()) return -1;
    // else return 1;
    // }
    //
    // String[] parts1 = BOUNDARYSPLIT.split(sti1.getItemName());
    // String[] parts2 = BOUNDARYSPLIT.split(sti2.getItemName());
    //
    // //if ((parts1.length == 1)||(parts2.length == 1)) return
    // sti1.getItemName().compareToIgnoreCase(sti2.getItemName());
    //
    // int ind = 0;
    // int min = Math.min(parts1.length, parts2.length);
    //
    // while (ind < min) {
    // if ((parts1[ind].charAt(0) >= '0' && parts1[ind].charAt(0) <=
    // '9')&&(parts2[ind].charAt(0) >= '0' && parts2[ind].charAt(0) <= '9')) {
    // Integer num1 = Integer.parseInt(parts1[ind]);
    // Integer num2 = Integer.parseInt(parts2[ind]);
    // if (!num1.equals(num2)) return num1.compareTo(num2);
    // } else {
    // if (!parts1[ind].equals(parts2[ind])) {
    // //System.out.println(sti1.getItemName() + " " + sti2.getItemName());
    // return sti1.getItemName().compareToIgnoreCase(sti2.getItemName());
    // //parts1[ind].compareToIgnoreCase(parts2[ind]);
    // }
    // }
    // ind++;
    // }
    //
    // if (parts1.length == parts2.length) return 0;
    // else if (parts1.length > parts2.length) return 1;
    // else return -1;
    // });
    // }

    // ------------------------------------------------------------------------------------------------------------------

    static String getFullPath(OwlsTreeItem dir) {
        return dir.getFullPath();
    }

    static String getDirectoryPath(OwlsTreeItem dir) {
        return dir.getDirectoryPath();
    }

    static boolean isDirectory(OwlsTreeItem sti) {
        return sti.isDirectory();
    }

    static String getFileName(OwlsTreeItem sti) {
        return sti.getText();
    }

    static String getFileText(OwlsTreeItem sti) {
        return sti.loadItem();
    }

    static void setOwlMode(boolean owlMode) {
        InterfaceSupport.owlMode = owlMode;
    }

    static boolean getOwlMode() {
        return owlMode;
    }

    static void saveOwlFile(OwlsTreeItem savingItem, String owlText) {
        savingItem.saveItem(owlText);
        setEditing(savingItem, false);
    }

    /**
     * Close without saving
     */
    static void closeOwlFile(OwlsTreeItem closingItem) {
        closingItem.closeItem();
        setEditing(closingItem, false);
    }

    static void removeOwlFile(OwlsTreeItem removingItem) {
        // removingItem.getParent().getChildren().remove(removingItem);

        if (!removingItem.getItemType().equals(TreeItemType.LEAF)) {
            recursiveRemove(removingItem);
        }

        removingItem.removeItem();
        removingItem.getParentBranch().removeChild(removingItem);

    }

    private static void recursiveRemove(OwlsTreeItem removingItem) {
        if (!removingItem.getItemType().equals(TreeItemType.LEAF)) {
            for (TreeItem ti : removingItem.getChildren()) {
                recursiveRemove((OwlsTreeItem) ti);
            }
        } else {
            removingItem.removeItem();
        }
    }

    static OwlsTreeItem makeNewOwlFile(OwlsTreeItem parentSti, String name) {
        String fullPath = makeFullPath(parentSti, name);
        return makeOwlItem(name, fullPath, TreeItemType.LEAF, parentSti, true);
    }

    static OwlsTreeItem makeNewOwlDir(OwlsTreeItem parentSti, String name) {
        String fullPath = makeFullPath(parentSti, name);
        OwlsTreeItem newTreeItem = makeOwlItem(name, fullPath, TreeItemType.BRANCH, parentSti, true);
        newTreeItem.newFolder();
        return newTreeItem;
    }

    static boolean renameOwlFile(OwlsTreeItem sti, String newName) {
        String fp = makeFullPath((OwlsTreeItem) sti.getParentBranch(), newName);

        if (new File(fp).exists()) {
            System.out.println("File with such name already exist in this folder");
            return false;
        } else {
            return sti.renameFile(newName);
        }
    }

    static String makeFullPath(OwlsTreeItem workDirectory, String name) {
        String parentPath = getFullPath(workDirectory);
        return makeFullPath(parentPath, name);
    }

    static String makeFullPath(String parentPath, String name) {
        return (parentPath + File.separator + name); // Нужна проверка разделителя для винды и линукса
    }

    static boolean isEditing(OwlsTreeItem sti) {
        return sti.isEditing();
    }

    static void setEditing(OwlsTreeItem sti, boolean editType) {
        sti.setEditing(editType);
    }

    static void removeKeyWord(OwlsTreeItem sti, String keyWord) {
        sti.getKeyWordsArr().remove(keyWord);
    }

    static boolean addKeyWords(String dialogLinks, OwlsTreeItem ti) {
        List<String> newLinks = new ArrayList<>(Arrays.asList(dialogLinks.split("\\s")));
        OwlsTreeItem sti = ti;
        // int count = 0;
        boolean b = false;
        for (String s : newLinks) {
            if (!sti.isKeyWordExist(s)) {
                sti.getKeyWordsArr().add(s);
                b = true;
            } // else {
              // count++;
              // }
        }

        // ExceptionHandling.missedKeyWords(); //Оставить здесь или переместить в
        // Controller?
        // System.out.println("Пропущено " + count + " слов.");

        return b;
    }

    static void removeAllKeyWords(OwlsTreeItem sti) {
        sti.clearKeyWordArr();
    }

    /**
     * @param searchSTIItem search directory
     */
    static List<OwlsTreeItem> findOwlFiles(String keyWord, OwlsTreeItem searchSTIItem) {
        List<OwlsTreeItem> list = new LinkedList<>();

        list = findFilesWithKeyWord(Arrays.asList(keyWord.split("\\s")), searchSTIItem, list);

        return list;
    }

    private static List<OwlsTreeItem> findFilesWithKeyWord(List<String> s, OwlsTreeItem ti,
            List<OwlsTreeItem> filesWithKW) {
        if (!ti.getItemType().equals(TreeItemType.LEAF)) { // isDirectory()) {
            if (!ti.getChildren().isEmpty()) {
                for (TreeItem childLti : ti.getChildren()) {
                    filesWithKW = findFilesWithKeyWord(s, (OwlsTreeItem) childLti, filesWithKW);
                }
            }
        } else {
            if (ti.isKeyWordExist(s)) {
                filesWithKW.add(ti);
                // makeOwlItem(ti, filesWithKW);
            }
        }
        return filesWithKW;
    }

    static OwlsTreeItem importNewFile(File newFile, OwlsTreeItem parentItem) {
        String newPath = makeFullPath(getFullPath(parentItem), newFile.getName());
        if (new File(newPath).exists())
            return null; // Файл с таким именем уже есть

        String s = "";
        try (BufferedReader br = new BufferedReader(new FileReader(newFile))) { // Читаем старый файл
            s = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            // FileNotFoundException
            e.printStackTrace();
        }

        OwlsTreeItem sti = makeOwlItem(newFile.getName(), newPath, TreeItemType.LEAF, parentItem, true); // Создаем
                                                                                                         // элемент
                                                                                                         // дерева
        saveOwlFile(sti, s);
        return sti;
    }
}
