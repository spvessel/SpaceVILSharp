package com.spvessel.Common;

import java.lang.Object;
import java.util.*;
import java.io.*;
import java.nio.*;

public final class CommonService {

    private CommonService() {
    }

    public static String ClipboardTextStorage = "";
    public final static Object GlobalLocker = new Object();

    public String getResourceString(String resource) {

        StringBuilder result = new StringBuilder("");

        // Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resource).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}