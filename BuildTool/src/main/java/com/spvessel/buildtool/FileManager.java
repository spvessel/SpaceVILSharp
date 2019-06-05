package com.spvessel.buildtool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileManager {
    static void ClearFolder(String folderPath) {
        File fileFolder = new File(folderPath);
        File[] fileList = fileFolder.listFiles();
        if (fileList == null)
            return;
        for (File f : fileList) {
            if (f.isDirectory()) { // Если элемент - папка
                ClearFolder(f.toString());

                if (!f.delete()) {
                    System.err.println("Enable to delete directory " + f.toString());
                }
            } else { // Если элемент - файл
                if (!f.delete()) {
                    System.err.println("Enable to delete file " + f.toString());
                }
            }
        }
    }

    static List<String> CopyFolder(String fromPath, String toPath, List<String> filesNFolders) {
        if (filesNFolders == null)
            filesNFolders = new LinkedList<>();

        File fileFolder = new File(fromPath);
        File[] fileList = fileFolder.listFiles();
        if (fileList == null)
            return filesNFolders;
        for (File f : fileList) {
            if (f.isDirectory()) { // Если элемент - папка
                File dir = new File(toPath + "\\" + f.getName());
                if (Configs.ExcludeDirs.contains(f.getName()))
                    continue;
                if (f.getName().substring(0, 1).equals("."))
                    continue;
                try {
                    if (!(dir.exists())) {
                        dir.mkdir();
                        filesNFolders.add(dir.getPath());
                    }
                } catch (Exception e) {
                    System.err.println("mkdir exception " + e);
                }
                filesNFolders = CopyFolder(f.getPath(), dir.getPath(), filesNFolders);
            } else { // Если элемент - файл
                if (Configs.ExcludeFiles.contains(f.getName()))
                    continue;
                int lpi = f.getName().lastIndexOf(".");
                String ext = f.getName().substring(lpi + 1);
                if (!Configs.ValExtensions.contains(ext))
                    continue;
                try {
                    File dest = new File(toPath + "\\" + f.getName());
                    Files.copy(f.toPath(), dest.toPath());
                    filesNFolders.add(dest.getPath());
                } catch (IOException e) {
                    System.err.println("copy file exception " + e);
                }
            }
        }

        return filesNFolders;
    }

    static void RemoveAddedItems(List<String> addedItemsList) {
        for (int i = addedItemsList.size() - 1; i >= 0; i--) {
            File file = new File(addedItemsList.get(i));
            file.delete();
        }
    }

    static boolean CopyLibs(String fromPath, String toPath) {
        File f = new File(fromPath);
        try {
            File dest = new File(toPath);
            if ((dest.exists())) {
                dest.delete();
            }
            Files.copy(f.toPath(), dest.toPath());
            return true;
        } catch (IOException e) {
            System.err.println("copy file exception " + e);
            return false;
        }
    }

    static void ClearLibs(String lib) {
        File file = new File(lib);
        file.delete();
    }

    static void MakeZip(String source, String path) {
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        try {
            fos = new FileOutputStream(path);
            zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(source);

            zipFile(fileToZip, fileToZip.getName(), zipOut);
        } catch (IOException e) {
            System.err.println("Make zip exception " + e);
        } finally {
            try {
                if (zipOut != null)
                    zipOut.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}
