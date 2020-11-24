package com.spvessel.spacevil.Common;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.spvessel.spacevil.Flags.OSType;

final class NativeLibrary {
    private NativeLibrary() {
    }

    private static boolean isLoaded = false;

    static void extractEmbeddedLibrary() {
        if (isLoaded) {
            return;
        }
        String glfw = System.mapLibraryName("glfw3");
        String spwrapper = System.mapLibraryName("wrapper");

        extract(glfw);
        extract(spwrapper);

        load(spwrapper);
        isLoaded = true;
    }

    private static void extract(String libname) {
        try {
            String prefix = "";
            if (CommonService.getOSType() == OSType.Mac) {
                prefix = "macos/";
            } else if (CommonService.getOSType() == OSType.Linux) {
                prefix = "linux/";
            } else if (CommonService.getOSType() == OSType.Windows) {
                prefix = "windows/";
            }

            InputStream resource = NativeLibrary.class.getResourceAsStream("/native/" + prefix + libname);

            String path = Paths.get("").toAbsolutePath().toString() + File.separator + "wrapper" + File.separator;
            File library = new File(path);
            library.mkdirs();
            library = new File(library, libname);
            if (!library.exists()) {
                Files.copy(resource, library.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            resource.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void load(String libname) {
        System.load(Paths.get("").toAbsolutePath().toString() + File.separator + "wrapper" + File.separator + libname);
    }
}