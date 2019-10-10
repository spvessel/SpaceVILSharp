package com.spvessel.buildtool;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Configs {
    public final static String ConfPath = "conf.txt";
    public static String VerMajor = "";
    public static String VerMiddle = "";
    public static String VerMinor = "";
    public static String VerExtend = "";
    public static String VerPhase = "";
    public static String VerMonth = "";

    public static String SrcPathJVM = "";
    public static String SrcPathNet = "";

    public static String TemplatePathJVM = "";
    public static String TemplatePathNetCore = "";
    public static String TemplatePathNetStandard = "";

    public static String ObfPathNet = "";
    public static String ObfPathJVM = "";

    public static String VerificateJVM = "";
    public static String VerificateNetCore = "";
    public static String VerificateNetStandard = "";

    public static String ReleasePathJVM = "";
    public static String ReleasePathNetCore = "";
    public static String ReleasePathNetStandard = "";

    public static String ZipDir = "";
    public static String ZipJVM = "";
    public static String ZipNetCore = "";
    public static String ZipNetStandard = "";

    public static List<String> ValExtensions;
    public static List<String> ExcludeDirs;
    public static List<String> ExcludeFiles;

    public static String AssemblyInfoNetStandard = "";
    public static String AssemblyInfoNetCore = "";

    public static String jvm_compile = "compile.bat";

    public static String netCore_compile = "compile.bat";

    public static String netStandard_compile = "compile.bat";
    // "\"C:\\Program Files (x86)\\Microsoft Visual
    // Studio\\2017\\Community\\MSBuild\\15.0\\Bin\\Roslyn\\csc.exe\" "
    // + "-platform:anycpu " + "-optimize " + "/unsafe " + "/t:library " +
    // "/r:System.Drawing.dll "
    // + "-appconfig:App.config " + "-out:SpaceVIL.dll "
    // + "-res:Shaders\\fs_primitive.glsl,SpaceVIL.Shaders.fs_primitive.glsl "
    // + "-res:Shaders\\vs_primitive.glsl,SpaceVIL.Shaders.vs_primitive.glsl "
    // + "-res:Shaders\\fs_texture.glsl,SpaceVIL.Shaders.fs_texture.glsl "
    // + "-res:Shaders\\vs_texture.glsl,SpaceVIL.Shaders.vs_texture.glsl "
    // + "-res:Shaders\\fs_char.glsl,SpaceVIL.Shaders.fs_char.glsl "
    // + "-res:Shaders\\vs_char.glsl,SpaceVIL.Shaders.vs_char.glsl "
    // + "-res:Shaders\\gs_points.glsl,SpaceVIL.Shaders.gs_points.glsl "
    // + "-res:Shaders\\vs_points.glsl,SpaceVIL.Shaders.vs_points.glsl "
    // + "-res:Shaders\\fs_blur.glsl,SpaceVIL.Shaders.fs_blur.glsl "
    // + "-res:Shaders\\vs_blur.glsl,SpaceVIL.Shaders.vs_blur.glsl "
    // + "-res:Fonts\\Ubuntu-Regular.ttf,SpaceVIL.Fonts.Ubuntu-Regular.ttf " +
    // "-nowarn:CS0414 "
    // + "-nowarn:CS0169 " + "-nowarn:CS0649 " + "-recurse:*.cs";

    public static String obfJava_proguardgui = "D:\\OBF\\proguard6.0.3\\proguard6.0.3\\bin\\proguardgui.bat";

    public static String obfJava_run = "run.bat";

    public static String obfNet_start = "start.bat";

    public static void readConfigs(String pathName) {
        String conFile = pathName;

        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(conFile);
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String firstWord = sCurrentLine.split(":")[0];
                int i = sCurrentLine.indexOf(":") + 1;
                if (i <= 0)
                    continue;
                String rightPart = sCurrentLine.substring(i);
                if (firstWord.equals("SrcPathJVM")) {
                    Configs.SrcPathJVM = rightPart;
                } else if (firstWord.equals("SrcPathNet")) {
                    Configs.SrcPathNet = rightPart;
                } else if (firstWord.equals("TemplatePathJVM")) {
                    Configs.TemplatePathJVM = rightPart;
                } else if (firstWord.equals("TemplatePathNetCore")) {
                    Configs.TemplatePathNetCore = rightPart;
                } else if (firstWord.equals("TemplatePathNetStandard")) {
                    Configs.TemplatePathNetStandard = rightPart;
                } else if (firstWord.equals("ObfPathNet")) {
                    Configs.ObfPathNet = rightPart;
                } else if (firstWord.equals("ObfPathJVM")) {
                    Configs.ObfPathJVM = rightPart;
                } else if (firstWord.equals("ReleasePathJVM")) {
                    Configs.ReleasePathJVM = rightPart;
                } else if (firstWord.equals("ReleasePathNetCore")) {
                    Configs.ReleasePathNetCore = rightPart;
                } else if (firstWord.equals("ReleasePathNetStandard")) {
                    Configs.ReleasePathNetStandard = rightPart;
                } else if (firstWord.equals("ExcludeDirs")) {
                    Configs.ExcludeDirs = Arrays.asList(rightPart.split(";"));
                } else if (firstWord.equals("ValExtensions")) {
                    Configs.ValExtensions = Arrays.asList(rightPart.split(";"));
                } else if (firstWord.equals("ExcludeFiles")) {
                    Configs.ExcludeFiles = Arrays.asList(rightPart.split(";"));
                } else if (firstWord.equals("ZipDir")) {
                    Configs.ZipDir = rightPart;
                } else if (firstWord.equals("VerMajor")) {
                    Configs.VerMajor = rightPart;
                } else if (firstWord.equals("VerMiddle")) {
                    Configs.VerMiddle = rightPart;
                } else if (firstWord.equals("VerMinor")) {
                    Configs.VerMinor = rightPart;
                } else if (firstWord.equals("VerExtend")) {
                    Configs.VerExtend = rightPart;
                } else if (firstWord.equals("VerPhase")) {
                    Configs.VerPhase = rightPart;
                } else if (firstWord.equals("VerMonth")) {
                    Configs.VerMonth = rightPart;
                } else if (firstWord.equals("AssemblyInfoNetStandard")) {
                    Configs.AssemblyInfoNetStandard = rightPart;
                } else if (firstWord.equals("AssemblyInfoNetCore")) {
                    Configs.AssemblyInfoNetCore = rightPart;
                } else if (firstWord.equals("ZipJVM")) {
                    Configs.ZipJVM = rightPart;
                } else if (firstWord.equals("ZipNetCore")) {
                    Configs.ZipNetCore = rightPart;
                } else if (firstWord.equals("ZipNetStandard")) {
                    Configs.ZipNetStandard = rightPart;
                }

                else if (firstWord.equals("VerificateJVM")) {
                    Configs.VerificateJVM = rightPart;
                } else if (firstWord.equals("VerificateNetCore")) {
                    Configs.VerificateNetCore = rightPart;
                } else if (firstWord.equals("VerificateNetStandard")) {
                    Configs.VerificateNetStandard = rightPart;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String getCurrentConfig() {
        return VerMajor + "." + VerMiddle + "." + VerMinor + "." + VerExtend + "-" + VerPhase + " - " + VerMonth + "\n"
                + Configs.SrcPathJVM + "\n" + Configs.SrcPathNet + "\n" + Configs.TemplatePathJVM + "\n"
                + Configs.TemplatePathNetCore + "\n" + Configs.TemplatePathNetStandard + "\n" + Configs.ObfPathJVM
                + "\n" + Configs.ObfPathNet + "\n" + Configs.ReleasePathJVM + "\n" + Configs.ReleasePathNetCore + "\n"
                + Configs.ReleasePathNetStandard + "\n" + Configs.ZipDir + "\n" + Configs.ZipJVM + "\n"
                + Configs.ZipNetCore + "\n" + Configs.ZipNetStandard + "\n" + Configs.AssemblyInfoNetStandard + "\n"
                + Configs.AssemblyInfoNetCore + "\n" + String.join(";", Configs.ExcludeDirs) + "\n"
                + String.join(";", Configs.ExcludeFiles) + "\n" + String.join(";", Configs.ValExtensions) + "\n";
    }

    public static void saveConfig(String pathName) {
        try {
            StringBuilder outsb = new StringBuilder();
            outsb.append("//version\n");
            outsb.append("VerMajor:" + Configs.VerMajor + "\n");
            outsb.append("VerMiddle:" + Configs.VerMiddle + "\n");
            outsb.append("VerMinor:" + Configs.VerMinor + "\n");
            outsb.append("VerExtend:" + Configs.VerExtend + "\n");
            outsb.append("VerPhase:" + Configs.VerPhase + "\n");
            outsb.append("VerMonth:" + Configs.VerMonth + "\n\n");

            outsb.append("//src\n");
            outsb.append("SrcPathJVM:" + Configs.SrcPathJVM + "\n");
            outsb.append("SrcPathNet:" + Configs.SrcPathNet + "\n\n");

            outsb.append("//template\n");
            outsb.append("TemplatePathJVM:" + Configs.TemplatePathJVM + "\n");
            outsb.append("TemplatePathNetCore:" + Configs.TemplatePathNetCore + "\n");
            outsb.append("TemplatePathNetStandard:" + Configs.TemplatePathNetStandard + "\n\n");

            outsb.append("//obf\n");
            outsb.append("ObfPathJVM:" + Configs.ObfPathJVM + "\n");
            outsb.append("ObfPathNet:" + Configs.ObfPathNet + "\n\n");

            outsb.append("//release\n");
            outsb.append("ReleasePathJVM:" + Configs.ReleasePathJVM + "\n");
            outsb.append("ReleasePathNetCore:" + Configs.ReleasePathNetCore + "\n");
            outsb.append("ReleasePathNetStandard:" + Configs.ReleasePathNetStandard + "\n\n");

            outsb.append("//verification\n");
            outsb.append("VerificateJVM:" + Configs.VerificateJVM + "\n");
            outsb.append("VerificateNetCore:" + Configs.VerificateNetCore + "\n");
            outsb.append("VerificateNetStandard:" + Configs.VerificateNetStandard + "\n\n");

            outsb.append("//zip\n");
            outsb.append("ZipDir:" + Configs.ZipDir + "\n");
            outsb.append("ZipJVM:" + Configs.ZipJVM + "\n");
            outsb.append("ZipNetCore:" + Configs.ZipNetCore + "\n");
            outsb.append("ZipNetStandard:" + Configs.ZipNetStandard + "\n\n");

            outsb.append("//exclude folders\n");
            outsb.append("ExcludeDirs:" + String.join(";", Configs.ExcludeDirs) + "\n\n");

            outsb.append("//exclude files\n");
            outsb.append("ExcludeFiles:" + String.join(";", Configs.ExcludeFiles) + "\n\n");

            outsb.append("//valuable extensions\n");
            outsb.append("ValExtensions:" + String.join(";", Configs.ValExtensions) + "\n\n");

            outsb.append("//assembly info\n");
            outsb.append("AssemblyInfoNetStandard:" + Configs.AssemblyInfoNetStandard + "\n");
            outsb.append("AssemblyInfoNetCore:" + Configs.AssemblyInfoNetCore + "\n");

            File file = new File(pathName);
            FileWriter writer = new FileWriter(file);
            writer.write(outsb.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
