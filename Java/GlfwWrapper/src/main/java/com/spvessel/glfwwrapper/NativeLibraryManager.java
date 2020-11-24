package com.spvessel.glfwwrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

final class NativeLibraryManager {

    private NativeLibraryManager() {
    }

    static void ExtractEmbeddedLibrary() {
        String libraryName = "glfwwrapper.dll";

        try {
            InputStream resource = NativeLibraryManager.class.getResourceAsStream("/native/windows/" + libraryName);
            byte[] buffer = new byte[resource.available()];
            resource.read(buffer);

            OutputStream outStream = new FileOutputStream(new File(libraryName));
            outStream.write(buffer);

            resource.close();
            outStream.close();

            System.load(Paths.get("").toAbsolutePath().toString() + File.separator + libraryName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}