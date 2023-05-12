package com.example.iae;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Driver {

    Project project;
    String path;
    String name = "main";
    String language;
    String compilerPath;
    ArrayList<String> output = new ArrayList<String>();
    Map<String, String> env;

    public Driver(Project project, String path) {
        this.project = project;
        this.path = path;
        this.language = project.getConfiguration().getSource();
        this.compilerPath = Paths.get(project.getConfiguration().getCompilerPath()).toAbsolutePath().normalize()
                .toString();
        System.out.println(compilerPath);
        this.env = new HashMap<>(System.getenv());
        env.put("PATH", compilerPath + File.pathSeparator + compilerPath + File.pathSeparator + env.get("PATH"));

    }

    private String compileProject() {

        File directory = new File(path);
        File[] fileList = directory.listFiles();
        String[] fileNames = new String[fileList.length];

        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                String fileName = fileList[i].getName();
                fileNames[i] = fileName;
            }
        }

        String command = "cmd.exe /c ";

        switch (language) {

            case "C":
                command += "gcc -w -o " + name + ".exe ";
                for (String fileName : fileNames) {
                    if (fileName != null && (fileName.endsWith(".c") || fileName.endsWith(".h"))) {
                        command += fileName + " ";
                    }

                }
                command += "&& " + name;
                break;

            case "C++":
                command += "g++ -w -o " + name + ".exe ";
                for (String fileName : fileNames) {
                    if (fileName != null && (fileName.endsWith(".cpp") || fileName.endsWith(".h"))) {
                        command += fileName + " ";
                    }
                }
                command += "&& " + name;
                break;
            case "Java":
                command += "javac *.java ";
                command += "&& java " + name;

                break;
            case "Dart":
                command += "dart " + name + ".dart";
                break;
            case "Python":
                command += "py -m py_compile " + name + ".py && " + "py " + name + ".py";

                break;
            default:
                break;
        }

        return command;
    }

    void evaluateStudent() {
        try {
            String[] commandArgs = compileProject().split("\\s+");
            ProcessBuilder builder = new ProcessBuilder(commandArgs);
            builder.environment().putAll(env);
            builder.inheritIO();
            builder.directory(new File(path));
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            builder.redirectInput(new File(project.getInputFilePath()));
            Process executeProcess = builder.start();

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                output.add(line);
            }
            process.destroy();

        } catch (IOException e) {
            ((Throwable) e).printStackTrace();
        }

    }
}
