package com.spvessel.buildtool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class StreamGobbler implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;
    private InputStream errorStream;

    // private String outStr;

    public StreamGobbler(InputStream inputStream, InputStream errorStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
        this.errorStream = errorStream;
        // outStr = "";
    }

    public StreamGobbler(InputStream inputStream, InputStream errorStream) {
        this(inputStream, errorStream, System.out::println);
    }

    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        try {
            // String str = "-->";//"\nCommand out \n";
            // sb.append(str);
            // consumer.accept(str);

            InputStreamReader isr = new InputStreamReader(inputStream, "cp866");
            // new BufferedReader(isr).lines().forEach(consumer);

            List<String> list = new BufferedReader(isr).lines().collect(Collectors.toList());
            sb = new StringBuilder();
            for (String s : list) {
                sb.append(s);
                sb.append("\n");
            }
            consumer.accept(sb.toString());

            // str = "\nErrors out \n";
            // sb.append(str);
            // consumer.accept(str);

            isr = new InputStreamReader(errorStream, "cp866");
            // new BufferedReader(isr).lines().forEach(consumer);

            list = new BufferedReader(isr).lines().collect(Collectors.toList());

            sb = new StringBuilder();
            for (String s : list) {
                sb.append(s);
                sb.append("\n");
            }
            consumer.accept(sb.toString());

        } catch (IOException e) {
            // System.out.println("IOException " + e);
            // sb.append("IOException " + e);
            consumer.accept("IOException " + e);
        } finally {
            // outStr = sb.toString();
        }
    }

    // public String getOutStr() {
    // return outStr;
    // }

}
