package com.example.iae;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Alert;

public class Driver {

    private Project project;
    private String path;
    private final String name = "main";
    private String language;
    private String compilerPath;
    private Map<String, String> env;
    private ArrayList<String> libraries;
    private boolean compilerError = false;

    public Driver(Project project, String path) {
        this.project = project;
        this.path = path;
        this.language = project.getConfiguration().getSource();
        this.compilerPath = Paths.get(project.getConfiguration().getCompilerPath()).toAbsolutePath().normalize()
                .toString();
        this.env = new HashMap<>(System.getenv());
        env.put("PATH", compilerPath + File.pathSeparator + compilerPath + File.pathSeparator + env.get("PATH"));
        this.libraries = project.getConfiguration().getLibraries();

    }

    public boolean getCompileError() {
        return compilerError;
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
                for (int i = 0; i < libraries.size(); i++) {
                    command += libraries.get(i).toString() + " ";
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
                for (int i = 0; i < libraries.size(); i++) {
                    command += libraries.get(i).toString() + " ";
                }
                command += "&& " + name;
                break;
            case "Java":
                command += "javac *.java ";
                for (int i = 0; i < libraries.size(); i++) {
                    command += libraries.get(i).toString() + " ";
                }
                command += "&& java " + name;

                break;
            case "Dart":
                command += "dart ";
                for (int i = 0; i < libraries.size(); i++) {
                    command += libraries.get(i).toString() + " ";
                }
                command += name + ".dart";
                break;
            case "Python":
                command += "py -m py_compile " + name + ".py ";
                for (int i = 0; i < libraries.size(); i++) {
                    command += libraries.get(i).toString() + " ";
                }
                command += "&& " + "py " + name + ".py";

                break;
            default:
                break;
        }

        return command;
    }

    public void evaluateAllStudents() {
        File folder = new File(path);
        for (File studentFolder : folder.listFiles()) {

            boolean isPassed = evaluateStudent(studentFolder);

            project.addStudent(new Student(studentFolder.getName(), isPassed));

        }
        if (compilerError == false) {
            DBConnection.getInstance().addProject(project);

        }

    }

    private boolean evaluateStudent(File studentFolder) {
        ArrayList<String> output = new ArrayList<String>();
        try {
            String[] commandArgs = compileProject(studentFolder).split("\\s+");
            ProcessBuilder builder = new ProcessBuilder(commandArgs);
            builder.environment().putAll(env);
            builder.directory(studentFolder);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            OutputStream outputStream = process.getOutputStream();
            File inputFile = new File(project.getInputFilePath());
            byte[] inputBytes = Files.readAllBytes(inputFile.toPath());
            outputStream.write(inputBytes);
            outputStream.close();

            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                output.add(line);
            }

            process.waitFor();
            process.destroy();

            if (output.get(0).toString().contains("is not recognized as an internal or external command,")) {
                compilerError = true;
            }

            return compareOutput(output);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean compareOutput(ArrayList<String> outputLines) {

        int i = 0;
        try (

                BufferedReader br = new BufferedReader(new FileReader(project.getOutputFilePath()))) {
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
