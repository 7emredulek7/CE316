package com.example.iae;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Driver {
    public Driver(Project project, String path) {
        this.project = project;
        this.path = path;
    }

    Project project;
    String path;
    String language = project.getConfiguration().getSource();
    ArrayList<String> libraries = new ArrayList<String>();

    private String compileProject() {
        String command = "default";

        switch (language) {
            case "C":
                command = "cmd.exe /c gcc -o " + project.getName() + ".exe " + project.getName() + ".c & " + project
                        .getName();
                break;
            case "C++":
                command = "cmd.exe /c g++ -o " + project.getName() + ".exe " + project.getName() + ".cpp & "
                        + project.getName();
                break;
            case "Python":
                command = "cmd.exe /c py " + project.getName() + ".py";
                break;
            case "Java":
                command = "cmd.exe /c javac " + project.getName() + ".java & java " + project.getName();
                break;
            case "Dart":
                command = "cmd.exe /c dart run " + project.getName() + ".dart";
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