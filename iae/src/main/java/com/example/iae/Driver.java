package com.example.iae;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Driver {

    Project project;
    String path;
    String name = "main";
    String language = "Python";
    ArrayList<String> libraries = new ArrayList<String>();

    public Driver(Project project, String path) {
        this.project = project;
        this.path = path;

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

        String command = "default";
        switch (language) {

            case "C":
                command = "cmd.exe /c gcc -w -o " + name + ".exe ";
                for (String fileName : fileNames) {
                    if (fileName != null && (fileName.endsWith(".c") || fileName.endsWith(".h"))) {
                        command += fileName + " ";
                    }

                }
                command += "&& " + name;
                break;

            case "C++":
                command = "cmd.exe /c g++ -w -o " + name + ".exe ";
                for (String fileName : fileNames) {
                    if (fileName != null && (fileName.endsWith(".cpp") || fileName.endsWith(".h"))) {
                        command += fileName + " ";
                    }
                }
                command += "&& " + name;
                break;
            case "Java":
                command = "cmd.exe /c javac " + name + ".java & java " + name;

                break;
            case "Dart":
                command = "dart compile exe ";
                for (String fileName : fileNames) {
                    if (fileName.endsWith(".dart")) {
                        command += fileName + " ";
                    }
                }
                command += name + ".exe\"";
                break;

            case "Python":
                command = "cmd.exe /c py -m py_compile " + name + ".py && " + "py " + name + ".py";

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
            }

            process.waitFor();

        } catch (IOException | InterruptedException e) {
            ((Throwable) e).printStackTrace();
        }

    }
}