package com.example.iae;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Driver {

    private Project project;
    private String path;
    private final String name = "main";
    private String language;
    private String compilerPath;
    private Map<String, String> env;

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

    private String compileProject(File directory) {
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

    public void evaluateAllStudents() {
        File folder = new File(path);
        for (File studentFolder : folder.listFiles()) {

            System.out.println(studentFolder.getAbsolutePath());
            boolean isPassed = evaluateStudent(studentFolder);
            project.addStudent(new Student(studentFolder.getName(), isPassed));

        }

        DBConnection.getInstance().addProject(project);
    }

    private boolean evaluateStudent(File studentFolder) {
        ArrayList<String> output = new ArrayList<String>();
        try {
            String[] commandArgs = compileProject(studentFolder).split("\\s+");
            ProcessBuilder builder = new ProcessBuilder(commandArgs);
            builder.environment().putAll(env);
            builder.inheritIO();
            builder.directory(studentFolder);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            String line;

            builder.redirectInput(new File(project.getInputFilePath()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            Process executeProcess = builder.start();

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                output.add(line);
            }
            reader.close();
            process.destroy();
            return compareOutput(output);

        } catch (IOException e) {
            ((Throwable) e).printStackTrace();
        }
        return false;

    }

    private boolean compareOutput(ArrayList<String> outputLines) {

        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(project.getOutputFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.equals(outputLines.get(i)))
                    return false;
                i++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return true;

    }
}
