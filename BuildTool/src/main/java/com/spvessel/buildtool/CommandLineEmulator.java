package com.spvessel.buildtool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandLineEmulator {
    static MainWindow handler;

    public static void CopyFolders(String fromPath, String toPath) {
        String cpCom = "xcopy " + "\"" + fromPath + "\" \"" + toPath + "\" /e";

        Execute(cpCom);
        // return
    }

    public static void CmdExecute(String command) {
        Execute("cmd.exe /c " + command);
        // return
    }

    public static void Execute(String command) {
        try {
            String[] commands = (command).split("\\s");

            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);

            Process process = builder.start();
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), process.getErrorStream(),
                    handler::appendText);

            ExecutorService es = Executors.newSingleThreadExecutor();
            java.util.concurrent.Future future = es.submit(streamGobbler);

            future.get();
            es.shutdown();

            // Thread t = new Thread(streamGobbler);
            // t.run();
            // t.join();

            int exitCode = process.waitFor();
            assert exitCode == 0;

            // return streamGobbler.getOutStr();

        } catch (Exception e) {
            handler.appendText("Exception " + e);
            // return ("Exception " + e);
        }
    }
}
